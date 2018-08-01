package eu.europa.ec.fisheries.uvms.rules.service.bean.activity;


import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityRulesHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.UnmarshalException;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.XML;

@Stateless
@LocalBean
@Slf4j
public class FaQueryRulesRulesMessageServiceBean extends BaseFaRulesMessageServiceBean {

    private static final String VALIDATION_RESULTED_IN_ERRORS = "[WARN] Validation resulted in errors. Not going to send msg to Activity module..";

    @EJB
    private RulesMessageProducer producer;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesActivityServiceBean activityServiceBean;

    @EJB
    private ActivityOutQueueConsumer activityConsumer;

    @EJB
    private FaResponseRulesMessageServiceBean faResponseValidatorAndSender;

    @EJB
    private RulesDao rulesDaoBean;

    @EJB
    private FaReportRulesRulesMessageServiceBean faReportRulesMessageBean;

    private FishingActivityRulesHelper faMessageHelper;

    private XSDJaxbUtil xsdJaxbUtil;

    @PostConstruct
    public void init() {
        faMessageHelper = new FishingActivityRulesHelper();
        xsdJaxbUtil = new XSDJaxbUtil();
    }

    public void evaluateIncomingFAQuery(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        final String onValue = request.getOnValue();
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            faQueryMessage = xsdJaxbUtil.unMarshallFaQueryMessage(requestStr);
            IDType faQueryGUID = collectFaQueryId(faQueryMessage);
            log.info("Evaluate FAQuery with ID " + faQueryGUID.getValue());
            boolean needToSendToExchange = true;

            Set<FADocumentID> idsFromIncommingMessage = faMessageHelper.mapQueryToFADocumentID(faQueryMessage);
            List<FADocumentID> faQueryIdsFromDb = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);

            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, request.getSenderOrReceiver());

            extraValues.put(XML, requestStr);
            Collection<AbstractFact> faQueryFacts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, faQueryMessage, extraValues, String.valueOf(faQueryMessage.getFAQuery().getID()));

            idsFromIncommingMessage.removeAll(faQueryIdsFromDb);
            rulesDaoBean.createFaDocumentIdEntity(idsFromIncommingMessage);

            ValidationResultDto faQueryValidationReport = rulePostProcessBean.checkAndUpdateValidationResult(faQueryFacts, requestStr, logGuid, RawMsgType.FA_QUERY);
            updateRequestMessageStatusInExchange(logGuid, faQueryValidationReport);

            SetFLUXFAReportMessageRequest setFLUXFAReportMessageRequest = null;
            if (faQueryValidationReport != null && !faQueryValidationReport.isError()) {
                log.debug("The Validation of FaQueryMessage is successful, going to check permissions (Subscriptions)..");
                boolean hasPermissions = activityServiceBean.checkSubscriptionPermissions(requestStr, MessageType.FLUX_FA_QUERY_MESSAGE);
                if (hasPermissions) { // Send query to activity.
                    log.debug("Request has permissions. Going to send FaQuery to Activity Module...");
                    setFLUXFAReportMessageRequest = sendSyncQueryRequestToActivity(requestStr, request.getUsername(), request.getType());
                    if (setFLUXFAReportMessageRequest.isIsEmptyReport()) {
                        needToSendToExchange = sendToExchangeOnEmptyReport(request, requestStr, logGuid, onValue, faQueryMessage, faQueryValidationReport);
                    }
                } else { // Request doesn't have permissions
                    log.debug("Request doesn't have permission! It won't be transmitted to Activity Module!");
                    updateRequestMessageStatusInExchange(logGuid, ExchangeLogStatusTypeType.FAILED);
                    faResponseValidatorAndSender.sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.PERMISSION_DENIED, onValue, faQueryValidationReport);
                    needToSendToExchange = false;
                }
            } else {
                log.debug(VALIDATION_RESULTED_IN_ERRORS);
            }
            FLUXResponseMessage fluxResponseMessageType = faResponseValidatorAndSender.generateFluxResponseMessageForFaQuery(faQueryValidationReport, faQueryMessage, onValue);
            XPathRepository.INSTANCE.clear(faQueryFacts);

            // A Response won't be sent only in the case of permissionDenied from Subscription,
            // since in this particular case a response will be send in the spot, and there's no need to send it here also.
            if (needToSendToExchange) {
                faResponseValidatorAndSender.validateAndSendResponseToExchange(fluxResponseMessageType, request, request.getType(), isCorrectUUID(Collections.singletonList(faQueryGUID)));
            }

            // We have received a SetFLUXFAReportMessageRequest (from activity) and it contains reports so needs to be processed (validated/sent through the normal flow).
            if (setFLUXFAReportMessageRequest != null && !setFLUXFAReportMessageRequest.isIsEmptyReport()) {
                faReportRulesMessageBean.evaluateOutgoingFaReport(setFLUXFAReportMessageRequest);
            }
        } catch (UnmarshalException e) {
            log.error("Error while trying to parse FLUXFAQueryMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException | ServiceException e) {
            log.error("Error during validation of the received FLUXFAQueryMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        }
    }

    public void evaluateOutgoingFAQuery(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("Evaluating FAQuery with GUID " + logGuid);
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            // Validate xsd schema
            faQueryMessage = xsdJaxbUtil.unMarshallFaQueryMessage(requestStr);

            Set<FADocumentID> idsFromIncommingMessage = faMessageHelper.mapQueryToFADocumentID(faQueryMessage);
            List<FADocumentID> faQueryIdsFromDb = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);

            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, request.getSenderOrReceiver());
            extraValues.put(XML, requestStr);
            Collection<AbstractFact> faQueryFacts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, faQueryMessage, extraValues);
            ValidationResultDto faQueryValidationReport = rulePostProcessBean.checkAndUpdateValidationResult(faQueryFacts, requestStr, logGuid, RawMsgType.FA_QUERY);
            if (faQueryValidationReport != null && !faQueryValidationReport.isError()) {
                log.debug("The Validation of FaQueryMessage is successful, forwarding message to Exchange");
                String exchangeReq = ExchangeModuleRequestMapper.createSendFaQueryMessageRequest(request.getRequest(), "flux", logGuid, request.getFluxDataFlow(), request.getSenderOrReceiver());
                sendToExchange(exchangeReq);
            } else {
                log.debug("Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(faQueryFacts);
            idsFromIncommingMessage.removeAll(faQueryIdsFromDb);
            rulesDaoBean.createFaDocumentIdEntity(idsFromIncommingMessage);

        } catch (UnmarshalException e) {
            log.error("Error while trying to parse FLUXFaQueryMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error("Error during validation of the received FLUXFaQueryMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        } catch (MessageException | ExchangeModelMarshallException | ServiceException e) {
            log.error("Error during validation of the received FLUXFaQueryMessage!", e);
        }
    }

    @Override
    RulesMessageProducer getRulesProducer() {
        return producer;
    }

    @Override
    AbstractConsumer getActivityConsumer(){
        return activityConsumer;
    }

    @Override
    FaResponseRulesMessageServiceBean getResponseValidator() {
        return faResponseValidatorAndSender;
    }
}
