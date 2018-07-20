/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET_BY_CFR;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET_BY_EXT;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET_BY_ICCAT;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FA_QUERY_AND_REPORT_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.XML;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FISHING_GEAR_TYPE_CHARACTERISTICS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.TRIP_ID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.bind.UnmarshalException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SyncAsyncRequestType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.utils.XSDJaxbUtil;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FAReportQueryResponseIdsMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityRulesHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@Slf4j
@LocalBean
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class FaReportRulesRulesMessageServiceBean extends BaseFaRulesMessageServiceBean {

    private FAReportQueryResponseIdsMapper faIdsMapper;

    @EJB
    private RulesMessageProducer rulesProducer;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private GearMatrix fishingGearTypeCharacteristics;

    @EJB
    private RulesActivityServiceBean activityServiceBean;

    @EJB
    private RuleAssetsBean ruleAssetsBean;

    @EJB
    private ActivityOutQueueConsumer activityConsumer;

    @EJB
    private FaResponseRulesMessageServiceBean faResponseValidatorAndSender;

    @EJB
    private RulesDao rulesDaoBean;

    private FishingActivityRulesHelper faMessageHelper;

    private XSDJaxbUtil xsdJaxbUtil;

    @PostConstruct
    public void init() {
        faIdsMapper = FAReportQueryResponseIdsMapper.INSTANCE;
        faMessageHelper = new FishingActivityRulesHelper();
        xsdJaxbUtil = new XSDJaxbUtil();
    }

    public void evaluateIncomingFLUXFAReport(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {
            fluxfaReportMessage = xsdJaxbUtil.unMarshallAndValidateSchema(requestStr);
            List<IDType> messageGUID = collectReportMessageIds(fluxfaReportMessage);

            log.info("Evaluating FLUXFAReportMessage {} ", messageGUID);

            Set<FADocumentID> idsFromIncomingMessage = faMessageHelper.mapToFADocumentID(fluxfaReportMessage);
            List<FADocumentID> reportAndMessageIdsFromDB = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncomingMessage);

            List<String> faIdsPerTripsFromMessage = faMessageHelper.collectFaIdsAndTripIds(fluxfaReportMessage);
            List<String> faIdsPerTripsListFromDb = rulesDaoBean.loadExistingFaIdsPerTrip(faIdsPerTripsFromMessage);

            Map<ExtraValueType, Object> extraValuesMap = populateExtraValueTypeObjectMap(request.getSenderOrReceiver(), fluxfaReportMessage, reportAndMessageIdsFromDB, faIdsPerTripsListFromDb, true);
            extraValuesMap.put(XML, requestStr);
            Collection<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValuesMap);

            idsFromIncomingMessage.removeAll(reportAndMessageIdsFromDB);
            faIdsPerTripsFromMessage.removeAll(faIdsPerTripsListFromDb);
            rulesDaoBean.createFaDocumentIdEntity(idsFromIncomingMessage);
            rulesDaoBean.saveFaIdsPerTripList(faIdsPerTripsFromMessage);

            ValidationResultDto faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, requestStr, logGuid, RawMsgType.FA_REPORT);
            updateRequestMessageStatusInExchange(logGuid, faReportValidationResult, false);

            if (faReportValidationResult != null && !faReportValidationResult.isError()) {
                log.debug(" The Validation of Report is successful, forwarding message to Activity.");
                boolean hasPermissions = activityServiceBean.checkSubscriptionPermissions(requestStr, MessageType.FLUX_FA_REPORT_MESSAGE);
                if (hasPermissions) {
                    log.debug(" Request has permissions. Going to send FaReportMessage to Activity Module...");
                    sendRequestToActivity(requestStr, request.getUsername(), request.getType(), MessageType.FLUX_FA_REPORT_MESSAGE);
                } else {
                    log.debug(" Request doesn't have permissions!");
                }
            } else {
                log.debug("Validation resulted in errors.");
            }

            FLUXResponseMessage fluxResponseMessage = faResponseValidatorAndSender.generateFluxResponseMessageForFaReport(faReportValidationResult, fluxfaReportMessage);
            XPathRepository.INSTANCE.clear(faReportFacts);

            faResponseValidatorAndSender.validateAndSendResponseToExchange(fluxResponseMessage, request, request.getType(), isCorrectUUID(messageGUID));

        } catch (UnmarshalException e) {
            log.error(" Error while trying to parse FLUXFAReportMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException | ServiceException e) {
            log.error(" Error during validation of the received FLUXFAReportMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        }
        log.debug("Finished eval of FLUXFAReportMessage " + logGuid);
    }

    public void evaluateOutgoingFaReport(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info(" Going to evaluate FLUXFAReportMessage with GUID [[ " + logGuid + " ]].");
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {
            fluxfaReportMessage = xsdJaxbUtil.unMarshallAndValidateSchema(requestStr);
            List<IDType> messageGUID = collectReportMessageIds(fluxfaReportMessage);

            log.info("Evaluating FLUXFAReportMessage with ID [ " + messageGUID + " ].");

            Set<FADocumentID> idsFromIncommingMessage = faMessageHelper.mapToFADocumentID(fluxfaReportMessage);
            List<String> faIdsPerTripsFromMessage = faMessageHelper.collectFaIdsAndTripIds(fluxfaReportMessage);

            List<FADocumentID> reportAndMessageIdsFromDB = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);
            List<String> faIdsPerTripsListFromDb = rulesDaoBean.loadExistingFaIdsPerTrip(faIdsPerTripsFromMessage);

            Map<ExtraValueType, Object> extraValuesMap = populateExtraValueTypeObjectMap(request.getSenderOrReceiver(), fluxfaReportMessage, reportAndMessageIdsFromDB, faIdsPerTripsListFromDb, false);
            extraValuesMap.put(XML, requestStr);
            Collection<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValuesMap);
            ValidationResultDto faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, requestStr, logGuid, RawMsgType.FA_REPORT);
            if (faReportValidationResult != null && !faReportValidationResult.isError()) {
                log.info(" The Validation of FLUXFAReportMessage is successful, forwarding message to Exchange");
                sendToExchange(ExchangeModuleRequestMapper.createSendFaReportMessageRequest(request.getRequest(), "flux", logGuid, request.getFluxDataFlow(), request.getSenderOrReceiver(), request.getOnValue()));
            } else {
                log.info(" Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(faReportFacts);

        } catch (UnmarshalException e) {
            log.error(" Error while trying to parse FLUXFAReportMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error(" Error during validation of the received FLUXFAReportMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            faResponseValidatorAndSender.sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        } catch (MessageException | ExchangeModelMarshallException e) {
            log.error(" Error during validation of the received FLUXFAReportMessage!", e);
        }
    }

    private Map<ExtraValueType, Object> populateExtraValueTypeObjectMap(String senderReceiver, FLUXFAReportMessage fluxfaReportMessage,
                                                                        List<FADocumentID> reportAndMessageIdsFromDB, List<String> faIdsPerTripsListFromDb, boolean isIncomingMessage) {
        Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
        extraValues.put(SENDER_RECEIVER, senderReceiver);
        extraValues.put(ASSET_BY_CFR, ruleAssetsBean.getAssetListByCFR(fluxfaReportMessage));
        extraValues.put(ASSET_BY_ICCAT, ruleAssetsBean.getAssetListByICCAT(fluxfaReportMessage));
        extraValues.put(ASSET_BY_EXT, ruleAssetsBean.getAssetListByIRCSAndExtMarkAndNoCFR(fluxfaReportMessage));
        extraValues.put(FA_QUERY_AND_REPORT_IDS, faIdsMapper.mapToFishingActivityIdDto(reportAndMessageIdsFromDB));
        extraValues.put(FISHING_GEAR_TYPE_CHARACTERISTICS, fishingGearTypeCharacteristics.getMatrix());
        if (isIncomingMessage) {
            extraValues.put(TRIP_ID, faIdsPerTripsListFromDb);
        }
        return extraValues;
    }

    private void sendRequestToActivity(String activityMsgStr, String username, PluginType pluginType, MessageType messageType) {
        try {
            String activityRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportOrQueryMessageRequest(activityMsgStr, username, pluginType.toString(), messageType, SyncAsyncRequestType.ASYNC);
            rulesProducer.sendDataSourceMessage(activityRequest, DataSourceQueue.ACTIVITY);
        } catch (ActivityModelMarshallException | MessageException e) {
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


    @Override
    RulesMessageProducer getRulesProducer() {
        return rulesProducer;
    }

    @Override
    AbstractConsumer getActivityConsumer() {
        return activityConsumer;
    }

    @Override
    FaResponseRulesMessageServiceBean getResponseValidator() {
        return faResponseValidatorAndSender;
    }

}
