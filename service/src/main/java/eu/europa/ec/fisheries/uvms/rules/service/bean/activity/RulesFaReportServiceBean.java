/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.activity;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
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
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.entity.FAUUIDType;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesActivityProducerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesExchangeProducerBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesExchangeServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.permission.PermissionData;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.impl.AssetClientBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FAReportQueryResponseIdsMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.MDC;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.xml.bind.JAXBException;
import javax.inject.Inject;
import javax.xml.bind.UnmarshalException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;

@Slf4j
@LocalBean
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RulesFaReportServiceBean {

    private static final ValidationResult FAILURE = new ValidationResult(true, false, false, null);

    private FAReportQueryResponseIdsMapper faIdsMapper;

    @EJB
    private RulesResponseConsumer rulesConsumer;

    @EJB
    private RulesActivityProducerBean activityProducer;

    @EJB
    private RulesExchangeProducerBean exchangeProducer;

    @EJB
    private RulesConfigurationCache rulesConfigurationCache;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private GearMatrix fishingGearTypeCharacteristics;

    @EJB
    private RulesActivityServiceBean activityServiceBean;

    @EJB
    private RulesExchangeServiceBean exchangeServiceBean;

    @Inject
    private AssetClientBean assetClientBean;

    @EJB
    private RulesDao rulesDaoBean;

    private RulesFLUXMessageHelper fluxMessageHelper;

    @PostConstruct
    public void init() {
        faIdsMapper = FAReportQueryResponseIdsMapper.INSTANCE;
        fluxMessageHelper = new RulesFLUXMessageHelper(rulesConfigurationCache);
    }

    public void evaluateIncomingFLUXFAReport(SetFLUXFAReportMessageRequest request) {
        String requestStr = request.getRequest();
        String exchangeLogGuid = request.getLogGuid();
        String dataFlow = request.getFluxDataFlow();
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {
            fluxfaReportMessage = fluxMessageHelper.unMarshallAndValidateSchema(requestStr);
            List<IDType> messageGUID = collectReportMessageIds(fluxfaReportMessage);

            Set<FADocumentID> idsFromIncomingMessage = fluxMessageHelper.mapToFADocumentID(fluxfaReportMessage);
            rulesDaoBean.takeNoteOfDocumentIds(idsFromIncomingMessage);
            rulesDaoBean.lockDocumentIds(idsFromIncomingMessage);
            List<FADocumentID> reportAndMessageIdsFromDB = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncomingMessage);

            List<String> faIdsPerTripsFromMessage = fluxMessageHelper.collectFaIdsAndTripIds(fluxfaReportMessage);
            List<String> faIdsPerTripsListFromDb = rulesDaoBean.loadExistingFaIdsPerTrip(faIdsPerTripsFromMessage);

            Map<ExtraValueType, Object> extraValues = fetchExtraValues(request.getSenderOrReceiver(), fluxfaReportMessage, reportAndMessageIdsFromDB, faIdsPerTripsListFromDb, true);
            extraValues.put(XML, requestStr);
            extraValues.put(DATA_FLOW, dataFlow);
            Collection<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValues, CollectionUtils.isNotEmpty(messageGUID) ? messageGUID.get(0).getValue() : String.valueOf(messageGUID));

            idsFromIncomingMessage.removeAll(reportAndMessageIdsFromDB);
            faIdsPerTripsFromMessage.removeAll(faIdsPerTripsListFromDb);

            ValidationResult faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, requestStr, exchangeLogGuid, RawMsgType.FA_REPORT);
//            exchangeServiceBean.updateExchangeMessage(exchangeLogGuid, fluxMessageHelper.calculateMessageValidationStatus(faReportValidationResult));
            XPathRepository.INSTANCE.clear(faReportFacts);

            if (faReportValidationResult != null && !faReportValidationResult.isError()) {
                PermissionData permissionData = createPermissionData(request, exchangeLogGuid, fluxfaReportMessage, messageGUID, idsFromIncomingMessage, faIdsPerTripsFromMessage, faReportValidationResult);
                sendRequestToActivity(requestStr, request.getPluginType(), MessageType.FLUX_FA_REPORT_MESSAGE, exchangeLogGuid, permissionData);
            } else {
                log.debug("Validation resulted in errors.");
                FLUXResponseMessage fluxResponseMessage = fluxMessageHelper.generateFluxResponseMessageForFaReport(faReportValidationResult, fluxfaReportMessage);
                exchangeServiceBean.updateExchangeMessage(exchangeLogGuid, fluxMessageHelper.calculateMessageValidationStatus(faReportValidationResult));
                exchangeServiceBean.evaluateAndSendToExchange(fluxResponseMessage, request, request.getPluginType(), fluxMessageHelper.isCorrectUUID(messageGUID), MDC.getCopyOfContextMap());
            }
        } catch (UnmarshalException e) {
            log.error(" Error while trying to parse FLUXFAReportMessage received message! It is malformed! Reason : {{}}", e.getMessage());
            exchangeServiceBean.updateExchangeMessage(exchangeLogGuid, fluxMessageHelper.calculateMessageValidationStatus(FAILURE));
            exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error(" Error during validation of the received FLUXFAReportMessage!", e);
            exchangeServiceBean.updateExchangeMessage(exchangeLogGuid, fluxMessageHelper.calculateMessageValidationStatus(FAILURE));
            exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        }
        log.debug("Finished eval of FLUXFAReportMessage " + exchangeLogGuid);
    }

    public void completeIncomingFLUXFAReportEvaluation(PermissionData permissionData) {
        if (permissionData.isRequestPermitted()) {
            log.debug(" Request has permissions. Going to send FaReportMessage to Activity Module...");
            rulesDaoBean.saveFaIdsPerTripList(permissionData.getFaIdsPerTripsFromMessage());
            Set<FADocumentID> result = permissionData.getIdsFromIncomingMessage().stream().filter(faDocumentID -> !FAUUIDType.FA_REPORT_REF_ID.equals(faDocumentID.getType())).collect(Collectors.toSet());
            try {
                rulesDaoBean.createFaDocumentIdEntity(result);
                exchangeServiceBean.updateExchangeMessage(permissionData.getRawMsgGuid(), fluxMessageHelper.calculateMessageValidationStatus(permissionData.getFaReportValidationResult()));
                FLUXResponseMessage fluxResponseMessage = fluxMessageHelper.generateFluxResponseMessageForFaReport(permissionData.getFaReportValidationResult(), permissionData.getFluxfaReportMessage());
                exchangeServiceBean.evaluateAndSendToExchange(fluxResponseMessage, permissionData.getRequest(), permissionData.getRequest().getPluginType(), fluxMessageHelper.isCorrectUUID(permissionData.getMessageGUID()), permissionData.getMdcContextMap());
            } catch (ServiceException e) {
                log.error(" Error during validation of the received FLUXFAReportMessage!", e);
                exchangeServiceBean.updateExchangeMessage(permissionData.getRawMsgGuid(), fluxMessageHelper.calculateMessageValidationStatus(FAILURE));
                exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), permissionData.getRequest().getRequest(), permissionData.getRequest(), permissionData.getFluxfaReportMessage());
            }
        } else {
            log.debug(" Request doesn't have permissions!");
            updateValidationResultWithPermission(permissionData);
            FLUXResponseMessage fluxResponseMessage = fluxMessageHelper.generateFluxResponseMessageForFaReport(permissionData.getFaReportValidationResult(), permissionData.getFluxfaReportMessage());
            exchangeServiceBean.evaluateAndSendToExchange(fluxResponseMessage, permissionData.getRequest(), permissionData.getRequest().getPluginType(), fluxMessageHelper.isCorrectUUID(permissionData.getMessageGUID()),permissionData.getMdcContextMap());

            exchangeServiceBean.updateExchangeMessage(permissionData.getRawMsgGuid(), ExchangeLogStatusTypeType.FAILED);
            rulePostProcessBean.saveOrUpdateValidationResultForPermission(permissionData.getRawMsgGuid(), permissionData.getRawMsgType(), permissionData.getRequest().getRequest(), permissionData.getFaReportValidationResult());
        }
    }

    private void updateValidationResultWithPermission(PermissionData permissionData) {
        ValidationResult faReportValidationResult = permissionData.getFaReportValidationResult();
        faReportValidationResult.setOk(false);
        faReportValidationResult.setError(true);
        faReportValidationResult.setWarning(false);
        faReportValidationResult.getValidationMessages().add(createPermissionValidationMessage());
    }

    private ValidationMessageType createPermissionValidationMessage() {
        ValidationMessageType permissionValidationMessage = new ValidationMessageType();
        permissionValidationMessage.setLevel("L00");
        permissionValidationMessage.setMessage("Permission Denied");
        permissionValidationMessage.setBrId("FA-L00-00-9999");
        permissionValidationMessage.getXpaths().add("((//*[local-name()='FLUXFAReportMessage']//*[local-name()='FAReportDocument'])[1]//*[local-name()='RelatedFLUXReportDocument'])[1]//*[local-name()='ID']");
        permissionValidationMessage.setErrorType(ErrorType.ERROR);
        permissionValidationMessage.setFactDate(Date.from(Instant.now()));
        return permissionValidationMessage;
    }

    private PermissionData createPermissionData(SetFLUXFAReportMessageRequest request, String exchangeLogGuid, FLUXFAReportMessage fluxfaReportMessage, List<IDType> messageGUID,
                                                Set<FADocumentID> idsFromIncomingMessage, List<String> faIdsPerTripsFromMessage, ValidationResult faReportValidationResult) {
        PermissionData permissionData = new PermissionData();
        permissionData.setFluxfaReportMessage(fluxfaReportMessage);
        permissionData.setRawMsgGuid(exchangeLogGuid);
        permissionData.setRawMsgType(RawMsgType.FA_REPORT);
        permissionData.setRequest(request);
        permissionData.setMessageGUID(messageGUID);
        permissionData.setIdsFromIncomingMessage(idsFromIncomingMessage);
        permissionData.setFaIdsPerTripsFromMessage(faIdsPerTripsFromMessage);
        permissionData.setFaReportValidationResult(faReportValidationResult);
        permissionData.setMdcContextMap(MDC.getCopyOfContextMap());
        return permissionData;
    }

    public void evaluateOutgoingFaReport(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info(" Going to evaluate FLUXFAReportMessage with GUID [[ " + logGuid + " ]].");
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {
            fluxfaReportMessage = fluxMessageHelper.unMarshallAndValidateSchema(requestStr);
            List<IDType> messageGUID = collectReportMessageIds(fluxfaReportMessage);

            log.info("Evaluating FLUXFAReportMessage with ID [ " + messageGUID + " ].");

            Set<FADocumentID> idsFromIncommingMessage = fluxMessageHelper.mapToFADocumentID(fluxfaReportMessage);
            List<String> faIdsPerTripsFromMessage = fluxMessageHelper.collectFaIdsAndTripIds(fluxfaReportMessage);

            List<FADocumentID> reportAndMessageIdsFromDB = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);
            List<String> faIdsPerTripsListFromDb = rulesDaoBean.loadExistingFaIdsPerTrip(faIdsPerTripsFromMessage);

            Map<ExtraValueType, Object> extraValuesMap = fetchExtraValues(request.getSenderOrReceiver(), fluxfaReportMessage, reportAndMessageIdsFromDB, faIdsPerTripsListFromDb, false);
            extraValuesMap.put(XML, requestStr);
            Collection<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValuesMap, String.valueOf(fluxfaReportMessage.getFLUXReportDocument().getIDS()));
            ValidationResult faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, requestStr, logGuid, RawMsgType.FA_REPORT);
            if (faReportValidationResult != null && !faReportValidationResult.isError()) {
                log.info(" The Validation of FLUXFAReportMessage is successful, forwarding message to Exchange");
                sendToExchange(ExchangeModuleRequestMapper.createSendFaReportMessageRequest(request.getRequest(), "movement", logGuid, request.getFluxDataFlow(),
                        request.getSenderOrReceiver(), request.getOnValue(), "IMPLEMENTTODT_FROM_REQUEST", "IMPLEMENTTO_FROM_REQUEST", request.getAd() == null ? "IMPLEMENTTO_FROM_REQUEST" : request.getAd()));
            } else {
                log.info(" Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(faReportFacts);

        } catch (UnmarshalException e) {
            log.error(" Error while trying to parse FLUXFAReportMessage received message! It is malformed! Reason : {{}}", e.getMessage());
            exchangeServiceBean.updateExchangeMessage(logGuid, fluxMessageHelper.calculateMessageValidationStatus(FAILURE));
            exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error(" Error during validation of the received FLUXFAReportMessage!", e);
            exchangeServiceBean.updateExchangeMessage(logGuid, fluxMessageHelper.calculateMessageValidationStatus(FAILURE));
            exchangeServiceBean.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        } catch (MessageException | ExchangeModelMarshallException e) {
            log.error(" Error during validation of the received FLUXFAReportMessage!", e);
        }
    }

    private void sendToExchange(String message) throws MessageException {
        exchangeProducer.sendModuleMessage(message, rulesConsumer.getDestination());
    }

    private Map<ExtraValueType, Object> fetchExtraValues(String senderReceiver, FLUXFAReportMessage fluxfaReportMessage,
                                                         List<FADocumentID> reportAndMessageIdsFromDB, List<String> faIdsPerTripsListFromDb, boolean isIncomingMessage) {
        Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
        extraValues.put(SENDER_RECEIVER, senderReceiver);
        extraValues.put(ExtraValueType.ASSET, assetClientBean.findHistoryOfAssetBy(fluxfaReportMessage.getFAReportDocuments()));
        extraValues.put(FA_QUERY_AND_REPORT_IDS, faIdsMapper.mapToFishingActivityIdDto(reportAndMessageIdsFromDB));
        extraValues.put(FISHING_GEAR_TYPE_CHARACTERISTICS, fishingGearTypeCharacteristics.getMatrix());
        extraValues.put(FISHING_GEAR_TYPE_NEAFC_CHARACTERISTICS, fishingGearTypeCharacteristics.getNeafcMatrix());
        if (isIncomingMessage) {
            extraValues.put(TRIP_ID, faIdsPerTripsListFromDb);
        }
        return extraValues;
    }

    private void sendRequestToActivity(String activityMsgStr,
                                       eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType pluginType,
                                       MessageType messageType,
                                       String exchangeLogGuid,
                                       PermissionData permissionData) {
        try {
            String activityRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportOrQueryMessageRequest(activityMsgStr, pluginType.toString(), messageType, SyncAsyncRequestType.ASYNC, exchangeLogGuid, JAXBUtils.marshallJaxBObjectToString(permissionData));
            activityProducer.sendModuleMessage(activityRequest, rulesConsumer.getDestination());
        } catch (ActivityModelMarshallException | MessageException | JAXBException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private List<IDType> collectReportMessageIds(FLUXFAReportMessage fluxfaReportMessage) {
        List<IDType> reportGUID = null;
        if (fluxfaReportMessage.getFLUXReportDocument() != null) {
            reportGUID = fluxfaReportMessage.getFLUXReportDocument().getIDS();
        }
        return reportGUID;
    }
}
