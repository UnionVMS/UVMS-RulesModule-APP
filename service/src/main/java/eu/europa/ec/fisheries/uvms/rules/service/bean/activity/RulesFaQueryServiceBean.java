package eu.europa.ec.fisheries.uvms.rules.service.bean.activity;


import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SyncAsyncRequestType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesActivityProducerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesExchangeProducerBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesExchangeServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;

@Stateless
@LocalBean
@Slf4j
public class RulesFaQueryServiceBean {

    private static final String VALIDATION_RESULTED_IN_ERRORS = "[WARN] Validation resulted in errors. Not going to send msg to Activity module..";

    private static ValidationResult failure = new ValidationResult(true, false, false, null);

    @EJB
    private RulesExchangeProducerBean exchangeProducer;

    @EJB
    private RulesResponseConsumer rulesConsumer;

    @EJB
    private RulesActivityProducerBean activityProducer;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesActivityServiceBean activityServiceBean;

    @EJB
    private ActivityOutQueueConsumer activityConsumer;

    @EJB
    private RulesFAResponseServiceBean faResponseServiceBean;

    @EJB
    private RulesDao rulesDaoBean;

    @EJB
    private RulesExchangeServiceBean exchangeServiceBean;

    @EJB
    private RulesConfigurationCache configurationCache;

    @EJB
    private RulesFaReportServiceBean faReportRulesMessageBean;

    private RulesFLUXMessageHelper fluxMessageHelper;

    @PostConstruct
    public void init() {
        fluxMessageHelper = new RulesFLUXMessageHelper(configurationCache);
    }

    public void evaluateIncomingFAQuery(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        String exchangeLogGuid = request.getLogGuid();
        String onValue = request.getOnValue();
        String dataFlow = request.getFluxDataFlow();
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            faQueryMessage = fluxMessageHelper.unMarshallFaQueryMessage(requestStr);
            IDType faQueryGUID = collectFaQueryId(faQueryMessage);
            log.info("Evaluate FAQuery with ID {}", faQueryGUID);
            boolean needToSendToExchange = true;
            Set<FADocumentID> idsFromIncomingMessage = fluxMessageHelper.mapQueryToFADocumentID(faQueryMessage);
            List<FADocumentID> faQueryIdsFromDb = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncomingMessage);

            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, request.getSenderOrReceiver());
            extraValues.put(XML, requestStr);
            extraValues.put(DATA_FLOW, dataFlow);

            Collection<AbstractFact> faQueryFacts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, faQueryMessage, extraValues, String.valueOf(faQueryMessage.getFAQuery().getID()));

            idsFromIncomingMessage.removeAll(faQueryIdsFromDb);

            ValidationResult faQueryValidationReport = rulePostProcessBean.checkAndUpdateValidationResult(faQueryFacts, requestStr, exchangeLogGuid, RawMsgType.FA_QUERY);
            exchangeServiceBean.updateExchangeMessage(exchangeLogGuid, fluxMessageHelper.calculateMessageValidationStatus(faQueryValidationReport));
            rulesDaoBean.createFaDocumentIdEntity(idsFromIncomingMessage,false);
            SetFLUXFAReportMessageRequest setFLUXFAReportMessageRequest = null;
            if (faQueryValidationReport != null && !faQueryValidationReport.isError()) {
                log.debug("The Validation of FaQueryMessage is successful, going to check permissions (Subscriptions)..");
                boolean hasPermissions = activityServiceBean.checkSubscriptionPermissions(requestStr, MessageType.FLUX_FA_QUERY_MESSAGE);
                if (hasPermissions) { // Send query to activity.
                    log.debug("Request has permissions. Going to send FaQuery to Activity Module...");
                    setFLUXFAReportMessageRequest = sendSyncQueryRequestToActivity(requestStr, request.getUsername(), request.getType(), exchangeLogGuid);
                    if (setFLUXFAReportMessageRequest.isIsEmptyReport()) {
                        log.info("[WARN] The report generated from Activity doesn't contain data (Empty report)!");
                        updateRequestMessageStatusInExchange(exchangeLogGuid, ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS);
                        faResponseServiceBean.sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.EMPTY_REPORT, onValue, faQueryValidationReport);
                        needToSendToExchange = false;
                    }
                } else { // Request doesn't have permissions
                    log.debug("Request doesn't have permission! It won't be transmitted to Activity Module!");
                    exchangeServiceBean.updateExchangeMessage(exchangeLogGuid, ExchangeLogStatusTypeType.FAILED);
                    faResponseServiceBean.sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.PERMISSION_DENIED, onValue, faQueryValidationReport);
                    needToSendToExchange = false;
                }
            } else {
                log.debug(VALIDATION_RESULTED_IN_ERRORS);
            }
            FLUXResponseMessage fluxResponseMessageType = fluxMessageHelper.generateFluxResponseMessageForFaQuery(faQueryValidationReport, faQueryMessage, onValue);
            XPathRepository.INSTANCE.clear(faQueryFacts);

            // A Response won't be sent only in the case of permissionDenied from Subscription,
            // since in this particular case a response will be send in the spot, and there's no need to send it here also.
            if (needToSendToExchange) {
                exchangeServiceBean.evaluateAndSendToExchange(fluxResponseMessageType, request, request.getType(), fluxMessageHelper.isCorrectUUID(Collections.singletonList(faQueryGUID)), MDC.getCopyOfContextMap());
            }

            // We have received a SetFLUXFAReportMessageRequest (from activity) and it contains reports so needs to be processed (validated/sent through the normal flow).
            if (setFLUXFAReportMessageRequest != null && !setFLUXFAReportMessageRequest.isIsEmptyReport()) {
                faReportRulesMessageBean.evaluateOutgoingFaReport(setFLUXFAReportMessageRequest);
            }
        } catch (UnmarshalException e) {
            log.error("Error while trying to parse FLUXFAQueryMessage received message! It is malformed! Reason : {{}}", e.getMessage());
            exchangeServiceBean.updateExchangeMessage(exchangeLogGuid, fluxMessageHelper.calculateMessageValidationStatus(failure));
            exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException | ServiceException e) {
            log.error("Error during validation of the received FLUXFAQueryMessage!", e);
            exchangeServiceBean.updateExchangeMessage(exchangeLogGuid, fluxMessageHelper.calculateMessageValidationStatus(failure));
            exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        }
    }

    private SetFLUXFAReportMessageRequest sendSyncQueryRequestToActivity(String activityQueryMsgStr, String username, PluginType pluginType, String exchangeLogGuid) {
        try {

            String activityRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportOrQueryMessageRequest(activityQueryMsgStr, pluginType.toString(), MessageType.FLUX_FA_QUERY_MESSAGE, SyncAsyncRequestType.SYNC, exchangeLogGuid);
            final String corrId = activityProducer.sendModuleMessage(activityRequest, rulesConsumer.getDestination());
            final TextMessage message = activityConsumer.getMessage(corrId, TextMessage.class);
            return JAXBUtils.unMarshallMessage(message.getText(), SetFLUXFAReportMessageRequest.class);
        } catch (ActivityModelMarshallException | MessageException | JAXBException | JMSException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    public void evaluateOutgoingFAQuery(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("Evaluating FAQuery with GUID " + logGuid);
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            // Validate xsd schema
            faQueryMessage = fluxMessageHelper.unMarshallFaQueryMessage(requestStr);

            Set<FADocumentID> idsFromIncommingMessage = fluxMessageHelper.mapQueryToFADocumentID(faQueryMessage);
            List<FADocumentID> faQueryIdsFromDb = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);

            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, request.getSenderOrReceiver());
            extraValues.put(XML, requestStr);
            Collection<AbstractFact> faQueryFacts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, faQueryMessage, extraValues);
            ValidationResult faQueryValidationReport = rulePostProcessBean.checkAndUpdateValidationResult(faQueryFacts, requestStr, logGuid, RawMsgType.FA_QUERY);
            if (faQueryValidationReport != null && !faQueryValidationReport.isError()) {
                log.debug("The Validation of FaQueryMessage is successful, forwarding message to Exchange");
                String exchangeReq = ExchangeModuleRequestMapper.createSendFaQueryMessageRequest(request.getRequest(),
                        "movement", logGuid, request.getFluxDataFlow(), request.getSenderOrReceiver(), "IMPLEMENTTODT_FROM_REQUEST", "IMPLEMENTTO_FROM_REQUEST", "IMPLEMENTTO_FROM_REQUEST");
                sendToExchange(exchangeReq);
            } else {
                log.debug("Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(faQueryFacts);
            idsFromIncommingMessage.removeAll(faQueryIdsFromDb);
            rulesDaoBean.createFaDocumentIdEntity(idsFromIncommingMessage,false);
        } catch (UnmarshalException e) {
            log.error("Error while trying to parse FLUXFaQueryMessage received message! It is malformed! Reason : {{}}", e.getMessage());
            exchangeServiceBean.updateExchangeMessage(logGuid, fluxMessageHelper.calculateMessageValidationStatus(failure));
            exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error("Error during validation of the received FLUXFaQueryMessage!", e);
            exchangeServiceBean.updateExchangeMessage(logGuid, fluxMessageHelper.calculateMessageValidationStatus(failure));
            exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        } catch (MessageException | ExchangeModelMarshallException | ServiceException e) {
            log.error("Error during validation of the received FLUXFaQueryMessage!", e);
        }
    }

    private void sendToExchange(String message) throws MessageException {
        exchangeProducer.sendModuleMessage(message, rulesConsumer.getDestination());
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, statusType);
            log.debug("Message to exchange to update status : {}", statusMsg);
            exchangeProducer.sendModuleMessage(statusMsg, rulesConsumer.getDestination());
        } catch (ExchangeModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }


    private IDType collectFaQueryId(FLUXFAQueryMessage faQueryMessage) {
        IDType faQueryGUID = null;
        if (faQueryMessage.getFAQuery() != null) {
            faQueryGUID = faQueryMessage.getFAQuery().getID();
        }
        return faQueryGUID;
    }
}