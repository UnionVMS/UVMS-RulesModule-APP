package eu.europa.ec.fisheries.uvms.rules.service.bean.messageprocessors;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SyncAsyncRequestType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.dto.FishingGearTypeCharacteristics;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleAssetsBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesActivityServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.utils.XSDJaxbUtil;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityRulesHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.*;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;

@Slf4j
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@DependsOn({"RulesConfigurationCache"})
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class FaRulesMessageServiceBean {

    private static final String VALIDATION_RESULTED_IN_ERRORS = "[WARN] Validation resulted in errors. Not going to send msg to Activity module..";
    private static final List<String> RULES_TO_USE_ON_VALUE = Arrays.asList("SALE-L01-00-0011", "SALE-L01-00-0400", "SALE-L01-00-0010");

    private FishingActivityMapper fishingActivityMapper;

    @PersistenceContext(unitName = "rulesPostgresPU")
    private EntityManager em;

    @EJB
    private RulesMessageProducer producer;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesActivityServiceBean activityService;

    @EJB
    private ParameterService parameterService;

    @EJB
    private FishingGearTypeCharacteristics fishingGearTypeCharacteristics;

    @EJB
    private RulesActivityServiceBean activityServiceBean;

    @EJB
    private RuleAssetsBean ruleAssetsBean;

    @EJB
    private ActivityOutQueueConsumer activityConsumer;

    @EJB
    private FaResponseValidatorAndSender faResponseValidatorAndSender;

    @EJB
    private RulesDao rulesDaoBean;

    private FishingActivityRulesHelper faReportMessageHelper;

    private XSDJaxbUtil xsdJaxbUtil;

    @PostConstruct
    public void init() {
        fishingActivityMapper = FishingActivityMapper.INSTANCE;
        faReportMessageHelper = new FishingActivityRulesHelper();
        xsdJaxbUtil = new XSDJaxbUtil();
    }

    public void evaluateIncomingFLUXFAReport(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {
            fluxfaReportMessage = xsdJaxbUtil.unMarshallAndValidateSchema(requestStr);
            List<IDType> messageGUID = getReportMessageIds(fluxfaReportMessage);

            log.info("[INFO] Evaluating FLUXFAReportMessage with ID [ " + messageGUID + " ].");

            Set<FADocumentID> idsFromIncommingMessage = faReportMessageHelper.collectReportIds(fluxfaReportMessage);
            List<FADocumentID> reportAndMessageIdsFromDB = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);

            List<String> faIdsPerTripsFromMessage = faReportMessageHelper.collectFaIdsAndTripIds(fluxfaReportMessage);
            List<String> faIdsPerTripsListFromDb = rulesDaoBean.loadExistingFaIdsPerTrip(faIdsPerTripsFromMessage);

            Map<ExtraValueType, Object> extraValuesMap = populateExtraValueTypeObjectMap(request.getSenderOrReceiver(), fluxfaReportMessage, reportAndMessageIdsFromDB, faIdsPerTripsListFromDb, true);
            List<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValuesMap);

            ValidationResultDto faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, requestStr, logGuid, RawMsgType.FA_REPORT);
            updateRequestMessageStatusInExchange(logGuid, faReportValidationResult, false);

            if (faReportValidationResult != null && !faReportValidationResult.isError()) {
                log.info("[INFO] The Validation of Report is successful, forwarding message to Activity.");
                boolean hasPermissions = activityServiceBean.checkSubscriptionPermissions(requestStr, MessageType.FLUX_FA_REPORT_MESSAGE);
                if (hasPermissions) {
                    log.info("[INFO] Request has permissions. Going to send FaReportMessage to Activity Module...");
                    sendRequestToActivity(requestStr, request.getUsername(), request.getType(), MessageType.FLUX_FA_REPORT_MESSAGE);
                } else {
                    log.info("[WARN] Request doesn't have permissions!");
                }
            } else {
                log.info(VALIDATION_RESULTED_IN_ERRORS);
            }

            FLUXResponseMessage fluxResponseMessage = faResponseValidatorAndSender.generateFluxResponseMessageForFaReport(faReportValidationResult, fluxfaReportMessage);
            XPathRepository.INSTANCE.clear(faReportFacts);

            faResponseValidatorAndSender.validateAndSendResponseToExchange(fluxResponseMessage, request, request.getType(), isCorrectUUID(messageGUID));

            // So that we don't duplicate in the DB.
            idsFromIncommingMessage.removeAll(reportAndMessageIdsFromDB);
            faIdsPerTripsFromMessage.removeAll(faIdsPerTripsListFromDb);

            rulesDaoBean.createFaDocumentIdEntity(idsFromIncommingMessage);
            rulesDaoBean.saveFaIdsPerTripList(faIdsPerTripsFromMessage);

        } catch (UnmarshalException e) {
            log.error("[ERROR] Error while trying to parse FLUXFAReportMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException | ServiceException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAReportMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        }
        log.info("[END] Finished evaluating FLUXFAReportMessage with GUID [[ " + logGuid + " ]].");
    }

    private List<IDType> getReportMessageIds(FLUXFAReportMessage fluxfaReportMessage) {
        List<IDType> reportGUID = null;
        if (fluxfaReportMessage.getFLUXReportDocument() != null) {
            reportGUID = fluxfaReportMessage.getFLUXReportDocument().getIDS();
        }
        return reportGUID;
    }

    public void evaluateOutgoingFaReport(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FLUXFAReportMessage with GUID [[ " + logGuid + " ]].");
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {
            fluxfaReportMessage = xsdJaxbUtil.unMarshallAndValidateSchema(requestStr);
            List<IDType> messageGUID = getReportMessageIds(fluxfaReportMessage);

            log.info("[INFO] Evaluating FLUXFAReportMessage with ID [ " + messageGUID + " ].");

            Set<FADocumentID> idsFromIncommingMessage = faReportMessageHelper.collectReportIds(fluxfaReportMessage);
            List<String> faIdsPerTripsFromMessage = faReportMessageHelper.collectFaIdsAndTripIds(fluxfaReportMessage);

            List<FADocumentID> reportAndMessageIdsFromDB = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);
            List<String> faIdsPerTripsListFromDb = rulesDaoBean.loadExistingFaIdsPerTrip(faIdsPerTripsFromMessage);

            Map<ExtraValueType, Object> extraValuesMap = populateExtraValueTypeObjectMap(request.getSenderOrReceiver(), fluxfaReportMessage, reportAndMessageIdsFromDB, faIdsPerTripsListFromDb, false);

            List<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValuesMap);
            ValidationResultDto faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, requestStr, logGuid, RawMsgType.FA_REPORT);
            if (faReportValidationResult != null && !faReportValidationResult.isError()) {
                log.info("[INFO] The Validation of FLUXFAReportMessage is successful, forwarding message to Exchange");
                sendToExchange(ExchangeModuleRequestMapper.createSendFaReportMessageRequest(request.getRequest(), "flux", logGuid, request.getFluxDataFlow(), request.getSenderOrReceiver(), request.getOnValue()));
            } else {
                log.info("[WARN] Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(faReportFacts);

        } catch (UnmarshalException e) {
            log.error("[ERROR] Error while trying to parse FLUXFAReportMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAReportMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        } catch (MessageException | ExchangeModelMarshallException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAReportMessage!", e);
        }
    }

    public void evaluateIncomingFAQuery(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        final String onValue = request.getOnValue();
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            faQueryMessage = xsdJaxbUtil.unMarshallFaQueryMessage(requestStr);
            IDType faQueryGUID = null;
            if (faQueryMessage.getFAQuery() != null) {
                faQueryGUID = faQueryMessage.getFAQuery().getID();
            }
            log.info("[INFO] Going to evaluate FAQuery with ID [ " + faQueryGUID + " ].");
            FLUXResponseMessage fluxResponseMessageType;
            boolean needToSendToExchange = true;
            SetFLUXFAReportMessageRequest setFLUXFAReportMessageRequest = null;
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, request.getSenderOrReceiver());
            List<AbstractFact> faQueryFacts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, faQueryMessage, extraValues);
            ValidationResultDto faQueryValidationReport = rulePostProcessBean.checkAndUpdateValidationResult(faQueryFacts, requestStr, logGuid, RawMsgType.FA_QUERY);
            updateRequestMessageStatusInExchange(logGuid, faQueryValidationReport);
            if (faQueryValidationReport != null && !faQueryValidationReport.isError()) {
                log.info("[INFO] The Validation of FaQueryMessage is successful, going to check permissions (Subscriptions)..");
                boolean hasPermissions = activityServiceBean.checkSubscriptionPermissions(requestStr, MessageType.FLUX_FA_QUERY_MESSAGE);
                if (hasPermissions) { // Send query to activity.
                    log.info("[INFO] Request has permissions. Going to send FaQuery to Activity Module...");
                    setFLUXFAReportMessageRequest = sendSyncQueryRequestToActivity(requestStr, request.getUsername(), request.getType());
                    if (setFLUXFAReportMessageRequest.isIsEmptyReport()) {
                        needToSendToExchange = isNeedToSendToExchange(request, requestStr, logGuid, onValue, faQueryMessage);
                    }
                } else { // Request doesn't have permissions
                    log.info("[WARN] Request doesn't have permission! It won't be transmitted to Activity Module!");
                    updateRequestMessageStatusInExchange(logGuid, ExchangeLogStatusTypeType.FAILED);
                    faResponseValidatorAndSender.sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.PERMISSION_DENIED, onValue);
                    needToSendToExchange = false;
                }
            } else {
                log.info(VALIDATION_RESULTED_IN_ERRORS);
            }
            fluxResponseMessageType = faResponseValidatorAndSender.generateFluxResponseMessageForFaQuery(faQueryValidationReport, faQueryMessage, onValue);
            XPathRepository.INSTANCE.clear(faQueryFacts);

            // A Response won't be sent only in the case of permissionDenied from Subscription,
            // since in this particular case a response will be send in the spot, and there's no need to send it here also.
            if (needToSendToExchange) {
                faResponseValidatorAndSender.validateAndSendResponseToExchange(fluxResponseMessageType, request, request.getType(), isCorrectUUID(Collections.singletonList(faQueryGUID)));
            }

            // We have received a SetFLUXFAReportMessageRequest (from activity) and it contains reports so needs to be processed.
            if (setFLUXFAReportMessageRequest != null && !setFLUXFAReportMessageRequest.isIsEmptyReport()) {
                evaluateOutgoingFaReport(setFLUXFAReportMessageRequest);
            }

        } catch (UnmarshalException e) {
            log.error("[ERROR] Error while trying to parse FLUXFAQueryMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAQueryMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        }
    }

    public void evaluateOutgoingFAQuery(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FAQuery with GUID [[ " + logGuid + " ]].");
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            // Validate xsd schema
            faQueryMessage = xsdJaxbUtil.unMarshallFaQueryMessage(requestStr);
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, request.getSenderOrReceiver());
            List<AbstractFact> faQueryFacts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, faQueryMessage, extraValues);
            ValidationResultDto faQueryValidationReport = rulePostProcessBean.checkAndUpdateValidationResult(faQueryFacts, requestStr, logGuid, RawMsgType.FA_QUERY);
            if (faQueryValidationReport != null && !faQueryValidationReport.isError()) {
                log.info("[INFO] The Validation of FaQueryMessage is successful, forwarding message to Exchange");
                String exchangeReq = ExchangeModuleRequestMapper.createSendFaQueryMessageRequest(request.getRequest(), "flux", logGuid, request.getFluxDataFlow(), request.getSenderOrReceiver());
                sendToExchange(exchangeReq);
            } else {
                log.info("[WARN] Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(faQueryFacts);

        } catch (UnmarshalException e) {
            log.error("[ERROR] Error while trying to parse FLUXFaQueryMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFaQueryMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        } catch (MessageException | ExchangeModelMarshallException e) {
            log.error("[ERROR] Error during validation of the received FLUXFaQueryMessage!", e);
        }
    }

    public void evaluateSetFluxFaResponseRequest(SetFluxFaResponseMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FLUXResponseMessage with GUID [[ " + logGuid + " ]].");
        FLUXResponseMessage fluxResponseMessage;
        try {
            // Validate xsd schema
            fluxResponseMessage = xsdJaxbUtil.unMarshallFluxResponseMessage(requestStr);
            List<AbstractFact> fluxFaResponseFacts = rulesEngine.evaluate(RECEIVING_FA_RESPONSE_MSG, fluxResponseMessage);
            ValidationResultDto fluxResponseValidResults = rulePostProcessBean.checkAndUpdateValidationResult(fluxFaResponseFacts, requestStr, logGuid, RawMsgType.FA_RESPONSE);
            updateRequestMessageStatusInExchange(logGuid, fluxResponseValidResults);
            if (fluxResponseValidResults != null && !fluxResponseValidResults.isError()) {
                log.info("[INFO] The Validation of FLUXResponseMessage is successful, forwarding message to Exchange");
            } else {
                log.info("[WARN] Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(fluxFaResponseFacts);

        } catch (UnmarshalException e) {
            log.error("[ERROR] Error while trying to parse FLUXResponseMessage received message! It is malformed!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            throw new RulesServiceException(e.getMessage(), e);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXResponseMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
        }
    }

    private Map<ExtraValueType, Object> populateExtraValueTypeObjectMap(String senderReceiver, FLUXFAReportMessage fluxfaReportMessage,
                                                                        List<FADocumentID> reportAndMessageIdsFromDB, List<String> faIdsPerTripsListFromDb, boolean isIncommingMessage) {
        Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
        extraValues.put(SENDER_RECEIVER, senderReceiver);
        extraValues.put(ASSET_ID, ruleAssetsBean.getAssetList(fluxfaReportMessage));
        extraValues.put(FA_REPORT_DOCUMENT_IDS, fishingActivityMapper.mapToFishingActivityIdDto(reportAndMessageIdsFromDB));
        extraValues.put(FISHING_GEAR_TYPE_CHARACTERISTICS, fishingGearTypeCharacteristics.getCharacteristicList());
        if (isIncommingMessage) {
            extraValues.put(TRIP_ID, faIdsPerTripsListFromDb);
        }
        return extraValues;
    }

    private boolean isCorrectUUID(List<IDType> ids) {
        boolean uuidIsCorrect = false;
        String uuidString = null;
        try {
            if (CollectionUtils.isNotEmpty(ids)) {
                IDType idType = ids.get(0);
                uuidString = idType.getValue();
                String schemeID = idType.getSchemeID();
                if ("UUID".equals(schemeID)) {
                    uuidIsCorrect = StringUtils.equalsIgnoreCase(UUID.fromString(uuidString).toString(), uuidString);
                }
                if (!uuidIsCorrect) {
                    log.debug("[WARN] The given UUID is not in a correct format {}", uuidString);
                }
            }
        } catch (IllegalArgumentException exception) {
            log.debug("[WARN] The given UUID is not in a correct format {}", uuidString);
        }
        return uuidIsCorrect;
    }

    private ValidationResultDto generateValidationResultDtoForFailure() {
        ValidationResultDto resultDto = new ValidationResultDto();
        resultDto.setOk(false);
        resultDto.setError(true);
        return resultDto;
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ValidationResultDto validationResult) {
        updateRequestMessageStatusInExchange(logGuid, validationResult, false);
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ValidationResultDto validationResult, Boolean duplicate) {
        updateRequestMessageStatusInExchange(logGuid, calculateMessageValidationStatus(validationResult), duplicate);
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType) {
        updateRequestMessageStatusInExchange(logGuid, statusType, false);
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType, Boolean duplicate) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, statusType, duplicate);
            log.debug("Message to exchange to update status : {}", statusMsg);
            producer.sendDataSourceMessage(statusMsg, DataSourceQueue.EXCHANGE);
        } catch (ExchangeModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private ExchangeLogStatusTypeType calculateMessageValidationStatus(ValidationResultDto validationResult) {
        if (validationResult != null) {
            if (validationResult.isError()) {
                return ExchangeLogStatusTypeType.FAILED;
            } else if (validationResult.isWarning()) {
                return ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS;
            } else {
                return ExchangeLogStatusTypeType.SUCCESSFUL;
            }
        } else {
            return ExchangeLogStatusTypeType.UNKNOWN;
        }
    }

    public void sendRequestToActivity(String activityMsgStr, String username, PluginType pluginType, MessageType messageType) {
        try {
            String activityRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportOrQueryMessageRequest(activityMsgStr, username, pluginType.toString(), messageType, SyncAsyncRequestType.ASYNC);
            producer.sendDataSourceMessage(activityRequest, DataSourceQueue.ACTIVITY);
        } catch (ActivityModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private SetFLUXFAReportMessageRequest sendSyncQueryRequestToActivity(String activityQueryMsgStr, String username, PluginType pluginType) {
        try {
            String activityRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportOrQueryMessageRequest(activityQueryMsgStr, username, pluginType.toString(), MessageType.FLUX_FA_QUERY_MESSAGE, SyncAsyncRequestType.SYNC);
            final String corrId = producer.sendDataSourceMessage(activityRequest, DataSourceQueue.ACTIVITY);
            final TextMessage message = activityConsumer.getMessage(corrId, TextMessage.class);
            return JAXBUtils.unMarshallMessage(message.getText(), SetFLUXFAReportMessageRequest.class);
        } catch (ActivityModelMarshallException | MessageException | JAXBException | JMSException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private boolean isNeedToSendToExchange(SetFaQueryMessageRequest request, String requestStr, String logGuid, String onValue, FLUXFAQueryMessage faQueryMessage) {
        log.info("[WARN] The report generated from Activity doesn't contain data (Empty report)!");
        updateRequestMessageStatusInExchange(logGuid, ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS);
        faResponseValidatorAndSender.sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.EMPTY_REPORT, onValue);
        return false;
    }

    private void sendToExchange(String message) throws MessageException {
        producer.sendDataSourceMessage(message, DataSourceQueue.EXCHANGE);
    }

    public boolean shouldUseFluxOn(ValidationResultDto validationResult) {
        for (ValidationMessageType validationMessage : validationResult.getValidationMessages()) {
            if (RULES_TO_USE_ON_VALUE.contains(validationMessage.getBrId())) {
                return true;
            }
        }
        return false;
    }

}
