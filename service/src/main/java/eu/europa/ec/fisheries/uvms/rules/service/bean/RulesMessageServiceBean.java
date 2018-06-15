/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.*;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.FANamespaceMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SyncAsyncRequestType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.dao.FADocumentIDDAO;
import eu.europa.ec.fisheries.uvms.rules.dto.FishingGearTypeCharacteristics;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.RulesMessageService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesMessageFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.config.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CodeTypeMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityRulesHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.xml.sax.SAXException;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationQualityAnalysis;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.*;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by padhyad, kovian on 5/9/2017.
 */
@Slf4j
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@DependsOn({"RulesConfigurationCache"})
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RulesMessageServiceBean implements RulesMessageService {

    private static final String FLUXFAREPORT_MESSAGE_3P1_XSD = "xsd/contract/fa/data/standard/FLUXFAReportMessage_3p1.xsd";
    private static final String FLUXFAQUERY_MESSAGE_3P0_XSD = "xsd/contract/fa/data/standard/FLUXFAQueryMessage_3p0.xsd";
    private static final String FLUXFARESPONSE_MESSAGE_6P0_XSD = "xsd/contract/fa/data/standard/FLUXResponseMessage_6p0.xsd";
    private static final String VALIDATION_RESULTED_IN_ERRORS = "[WARN] Validation resulted in errors. Not going to send msg to Activity module..";
    private static final List<String> RULES_TO_USE_ON_VALUE = Arrays.asList("SALE-L01-00-0011", "SALE-L01-00-0400", "SALE-L01-00-0010");
    private static final String FLUX_LOCAL_NATION_CODE = "flux_local_nation_code";

    private FishingActivityMapper fishingActivityMapper;

    @PersistenceContext(unitName = "rulesPostgresPU")
    private EntityManager em;

    @EJB
    private MDRCacheRuleService mdrCacheService;

    @EJB
    private RulesMessageProducer producer;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesPreProcessBean rulesPreProcessBean;

    @Inject
    private CodeTypeMapper codeTypeMapper;

    @EJB
    private RulesConfigurationCache ruleModuleCache;

    @EJB
    private RulesActivityServiceBean activityService;

    @EJB
    private ParameterService parameterService;

    @EJB
    private FishingGearTypeCharacteristics fishingGearTypeCharacteristics;


    @EJB private SalesMessageFactory salesMessageFactory;

    @EJB private RulesActivityServiceBean activityServiceBean;

    @EJB private RuleAssetsBean ruleAssetsBean;

    @EJB private ActivityOutQueueConsumer activityConsumer;

    private FADocumentIDDAO fishingActivityIdDao;

    private FishingActivityRulesHelper faReportMessageHelper;

    @PostConstruct public void init() {
        fishingActivityMapper = FishingActivityMapper.INSTANCE;
        fishingActivityIdDao = new FADocumentIDDAO(em);
        faReportMessageHelper = new FishingActivityRulesHelper();
    }

    @Override
    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.READ)
    public void receiveSalesQueryRequest(ReceiveSalesQueryRequest receiveSalesQueryRequest) {
        log.info("Received ReceiveSalesQueryRequest message");
        try {
            //get sales query message
            String salesQueryMessageAsString = receiveSalesQueryRequest.getRequest();
            String logGuid = receiveSalesQueryRequest.getLogGuid();

            FLUXSalesQueryMessage salesQueryMessage = JAXBUtils.unMarshallMessage(salesQueryMessageAsString, FLUXSalesQueryMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, receiveSalesQueryRequest.getSender());
            extraValues.put(ORIGINATING_PLUGIN, receiveSalesQueryRequest.getPluginType());
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(salesQueryMessage).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_QUERY_MSG, salesQueryMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesQueryMessageAsString, logGuid, RawMsgType.SALES_QUERY);

            //send to sales
            if (validationResult.isError()) {
                String requestForSales;
                if (shouldUseFluxOn(validationResult)) {
                    requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesQueryRequest.getOnValue(), validationResult, receiveSalesQueryRequest.getPluginType(), receiveSalesQueryRequest.getSender(), SalesIdType.FLUXTL_ON);
                } else {
                    requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesQueryRequest.getMessageGuid(), validationResult, receiveSalesQueryRequest.getPluginType(), receiveSalesQueryRequest.getSender(), SalesIdType.GUID);
                }
                log.info("Send RespondToInvalidMessageRequest message to Sales");
                sendToSales(requestForSales);
            } else {
                String requestForSales = salesMessageFactory.createSalesQueryRequest(receiveSalesQueryRequest.getRequest(), validationResult, receiveSalesQueryRequest.getPluginType());
                log.info("Send SalesQueryRequest message to Sales");
                sendToSales(requestForSales);
            }
            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (SalesMarshallException | RulesValidationException | MessageException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales query", e);
        }
    }

    @Override
    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.READ)
    public void receiveSalesReportRequest(ReceiveSalesReportRequest receiveSalesReportRequest) {
        log.info("Received ReceiveSalesReportRequest message");
        try {
            //get sales report message
            String salesReportMessageAsString = receiveSalesReportRequest.getRequest();
            String logGuid = receiveSalesReportRequest.getLogGuid();
            Report salesReportMessage = JAXBUtils.unMarshallMessage(salesReportMessageAsString, Report.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, receiveSalesReportRequest.getSender());
            extraValues.put(ORIGINATING_PLUGIN, receiveSalesReportRequest.getPluginType());
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(salesReportMessage).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_REPORT_MSG, salesReportMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesReportMessageAsString, logGuid, RawMsgType.SALES_REPORT);

            //send to sales
            if (validationResult.isError()) {
                String requestForSales = createInvalidSalesResponseMessage(receiveSalesReportRequest, validationResult);
                log.info("Send RespondToInvalidMessageRequest message to Sales");
                sendToSales(requestForSales);
            } else {
                String requestForSales = salesMessageFactory.createSalesReportRequest(receiveSalesReportRequest.getRequest(), validationResult, receiveSalesReportRequest.getPluginType());
                log.info("Send SalesReportRequest message to Sales");
                sendToSales(requestForSales);
            }
            //update log status
            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (SalesMarshallException | RulesValidationException | MessageException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales report", e);
        }
    }

    private String createInvalidSalesResponseMessage(ReceiveSalesReportRequest receiveSalesReportRequest, ValidationResultDto validationResult) throws SalesMarshallException {
        if (shouldUseFluxOn(validationResult)) {
            return salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesReportRequest.getOnValue(), validationResult, receiveSalesReportRequest.getPluginType(), receiveSalesReportRequest.getSender(), SalesIdType.FLUXTL_ON);
        } else {
            return salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesReportRequest.getMessageGuid(), validationResult, receiveSalesReportRequest.getPluginType(), receiveSalesReportRequest.getSender(), SalesIdType.GUID);
        }
    }

    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.READ)
    protected boolean shouldUseFluxOn(ValidationResultDto validationResult) {
        for (ValidationMessageType validationMessage : validationResult.getValidationMessages()) {
            if (RULES_TO_USE_ON_VALUE.contains(validationMessage.getBrId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.READ)
    public void receiveSalesResponseRequest(ReceiveSalesResponseRequest rulesRequest) {
        log.info("Received ReceiveSalesResponseRequest message");
        try {
            //get sales response message
            String salesResponseMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            FLUXSalesResponseMessage salesResponseMessage = JAXBUtils.unMarshallMessage(salesResponseMessageAsString, FLUXSalesResponseMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue(FLUX_LOCAL_NATION_CODE));
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(salesResponseMessage).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_RESPONSE_MSG, salesResponseMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesResponseMessageAsString, logGuid, RawMsgType.SALES_RESPONSE);

            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (RulesValidationException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales response", e);
        } catch (ConfigServiceException e) {
            throw new RulesServiceException("Couldn't retrieve the FLUX local nation code from the settings", e);
        }
    }

    private Optional<DateTime> getCreationDate(FLUXSalesQueryMessage salesQueryMessage) {
        if (salesQueryMessage != null && salesQueryMessage.getSalesQuery() != null && salesQueryMessage.getSalesQuery().getSubmittedDateTime() != null && salesQueryMessage.getSalesQuery().getSubmittedDateTime().getDateTime() != null) {
            return Optional.of(salesQueryMessage.getSalesQuery().getSubmittedDateTime().getDateTime());
        } else {
            return Optional.absent();
        }
    }

    private Optional<DateTime> getCreationDate(Report salesReportMessage) {
        if (salesReportMessage != null) {
            return getCreationDate(salesReportMessage.getFLUXSalesReportMessage());
        } else {
            return Optional.absent();
        }
    }

    private Optional<DateTime> getCreationDate(FLUXSalesReportMessage salesReportMessage) {
        if (salesReportMessage != null && salesReportMessage.getFLUXReportDocument() != null && salesReportMessage.getFLUXReportDocument().getCreationDateTime() != null && salesReportMessage.getFLUXReportDocument().getCreationDateTime().getDateTime() != null) {
            return Optional.of(salesReportMessage.getFLUXReportDocument().getCreationDateTime().getDateTime());
        } else {
            return Optional.absent();
        }
    }

    private Optional<DateTime> getCreationDate(FLUXSalesResponseMessage salesResponseMessage) {
        if (salesResponseMessage != null && salesResponseMessage.getFLUXResponseDocument() != null && salesResponseMessage.getFLUXResponseDocument().getCreationDateTime() != null && salesResponseMessage.getFLUXResponseDocument().getCreationDateTime().getDateTime() != null) {
            return Optional.of(salesResponseMessage.getFLUXResponseDocument().getCreationDateTime().getDateTime());
        } else {
            return Optional.absent();
        }
    }

    @Override
    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.READ)
    public void sendSalesResponseRequest(SendSalesResponseRequest rulesRequest) {
        try {
            //get sales response message
            String salesResponseMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            FLUXSalesResponseMessage salesResponseMessage = JAXBUtils.unMarshallMessage(salesResponseMessageAsString, FLUXSalesResponseMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue(FLUX_LOCAL_NATION_CODE));
            extraValues.put(ORIGINATING_PLUGIN, rulesRequest.getPluginToSendResponseThrough());
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(salesResponseMessage).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_RESPONSE_MSG, salesResponseMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesResponseMessageAsString, logGuid, RawMsgType.SALES_RESPONSE);
            ExchangeLogStatusTypeType validationStatus = calculateMessageValidationStatus(validationResult);

            //send to exchange
            String requestForExchange = ExchangeModuleRequestMapper.createSendSalesResponseRequest(rulesRequest.getRequest(), rulesRequest.getMessageGuid(), rulesRequest.getFluxDataFlow(), rulesRequest.getRecipient(), rulesRequest.getDateSent(), validationStatus, rulesRequest.getPluginToSendResponseThrough());

            log.info("Send SendSalesResponseRequest message to Exchange");
            sendToExchange(requestForExchange);
        } catch (ExchangeModelMarshallException | MessageException | RulesValidationException | ConfigServiceException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales response", e);
        }
    }

    @Override
    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.READ)
    public void sendSalesReportRequest(SendSalesReportRequest rulesRequest) {
        try {
            //get sales report message
            String salesReportMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            Report report = JAXBUtils.unMarshallMessage(salesReportMessageAsString, Report.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue(FLUX_LOCAL_NATION_CODE));
            extraValues.put(ORIGINATING_PLUGIN, rulesRequest.getPluginToSendResponseThrough());
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(report).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_REPORT_MSG, report, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesReportMessageAsString, logGuid, RawMsgType.SALES_REPORT);
            ExchangeLogStatusTypeType validationStatus = calculateMessageValidationStatus(validationResult);

            //send to exchange
            String requestForExchange = ExchangeModuleRequestMapper.createSendSalesReportRequest(rulesRequest.getRequest(), rulesRequest.getMessageGuid(), rulesRequest.getFluxDataFlow(), rulesRequest.getRecipient(), rulesRequest.getDateSent(), validationStatus, rulesRequest.getPluginToSendResponseThrough());

            log.info("Send SendSalesReportRequest message to Exchange");
            sendToExchange(requestForExchange);
        } catch (ExchangeModelMarshallException | MessageException | RulesValidationException | ConfigServiceException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales report", e);
        }
    }

    @Override
    @Lock(LockType.WRITE)
    @Transactional
    public void evaluateIncomingFLUXFAReport(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {

            fluxfaReportMessage = unMarshallAndValidateSchema(requestStr);
            List<IDType> reportGUID = null;
            if (fluxfaReportMessage.getFLUXReportDocument() != null) {
                reportGUID = fluxfaReportMessage.getFLUXReportDocument().getIDS();
            }
            log.info("[INFO] Evaluating FLUXFAReportMessage with ID [ " + reportGUID + " ].");

            Set<FADocumentID> incomingIDs = faReportMessageHelper.mapToFishingActivityId(fluxfaReportMessage);
            List<FADocumentID> fishingActivityIds = fishingActivityIdDao.loadFADocumentIDByIdsByIds(incomingIDs);
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, request.getSenderOrReceiver());
            extraValues.put(ASSET_ID, ruleAssetsBean.getAssetList(fluxfaReportMessage));
            extraValues.put(FA_REPORT_DOCUMENT_IDS, fishingActivityMapper.mapToFishingActivityIdDto(fishingActivityIds));
            extraValues.put(FISHING_GEAR_TYPE_CHARACTERISTICS, fishingGearTypeCharacteristics.getCharacteristicList());
            extraValues.put(TRIP_ID, activityService.getFishingActivitiesForTrips(fishingActivityIds));

            List<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValues);
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

            FLUXResponseMessage fluxResponseMessage = generateFluxResponseMessageForFaReport(faReportValidationResult, fluxfaReportMessage);
            XPathRepository.INSTANCE.clear(faReportFacts);

            validateAndSendResponseToExchange(fluxResponseMessage, request, request.getType(), isCorrectUUID(reportGUID));

            incomingIDs.removeAll(fishingActivityIds);
            for (FADocumentID incomingID : incomingIDs) {
                fishingActivityIdDao.createEntity(incomingID);
            }

        } catch (UnmarshalException e) {
            log.error("[ERROR] Error while trying to parse FLUXFAReportMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException | ServiceException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAReportMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        }
        log.info("[END] Finished evaluating FLUXFAReportMessage with GUID [[ " + logGuid + " ]].");
    }

    @Override
    public void evaluateOutgoingFaReport(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FLUXFAReportMessage with GUID [[ " + logGuid + " ]].");
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {

            Set<FADocumentID> incomingIDs = faReportMessageHelper.mapToFishingActivityId(fluxfaReportMessage);
            List<FADocumentID> fishingActivityIds = fishingActivityIdDao.loadFADocumentIDByIdsByIds(incomingIDs);
            fluxfaReportMessage = unMarshallAndValidateSchema(requestStr);
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, request.getSenderOrReceiver());
            extraValues.put(ASSET_ID, ruleAssetsBean.getAssetList(fluxfaReportMessage));
            extraValues.put(FISHING_GEAR_TYPE_CHARACTERISTICS, fishingGearTypeCharacteristics.getCharacteristicList());
            extraValues.put(FA_REPORT_DOCUMENT_IDS, fishingActivityMapper.mapToFishingActivityIdDto(fishingActivityIds));
            extraValues.put(TRIP_ID, activityService.getFishingActivitiesForTrips(fishingActivityIds));

            List<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValues);
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
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAReportMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        } catch (MessageException | ExchangeModelMarshallException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAReportMessage!", e);
        }
    }

    @Override
    @Lock(LockType.READ)
    public void evaluateIncomingFAQuery(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        final String onValue = request.getOnValue();
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            faQueryMessage = unMarshallFaQueryMessage(requestStr);
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
                    sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.PERMISSION_DENIED, onValue);
                    needToSendToExchange = false;
                }
            } else {
                log.info(VALIDATION_RESULTED_IN_ERRORS);
            }
            fluxResponseMessageType = generateFluxResponseMessageForFaQuery(faQueryValidationReport, faQueryMessage, onValue);
            XPathRepository.INSTANCE.clear(faQueryFacts);

            // A Response won't be sent only in the case of permissionDenied from Subscription,
            // since in this particular case a response will be send in the spot, and there's no need to send it here also.
            if (needToSendToExchange) {
                validateAndSendResponseToExchange(fluxResponseMessageType, request, request.getType(), isCorrectUUID(Collections.singletonList(faQueryGUID)));
            }

            // We have received a SetFLUXFAReportMessageRequest (from activity) and it contains reports so needs to be processed.
            if (setFLUXFAReportMessageRequest != null && !setFLUXFAReportMessageRequest.isIsEmptyReport()) {
                evaluateOutgoingFaReport(setFLUXFAReportMessageRequest);
            }

        } catch (UnmarshalException e) {
            log.error("[ERROR] Error while trying to parse FLUXFAQueryMessage received message! It is malformed!");
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAQueryMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        }
    }

    private boolean isNeedToSendToExchange(SetFaQueryMessageRequest request, String requestStr, String logGuid, String onValue, FLUXFAQueryMessage faQueryMessage) {
        log.info("[WARN] The report generated from Activity doesn't contain data (Empty report)!");
        updateRequestMessageStatusInExchange(logGuid, ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS);
        sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.EMPTY_REPORT, onValue);
        return false;
    }

    @Override
    public void evaluateOutgoingFAQuery(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FAQuery with GUID [[ " + logGuid + " ]].");
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            // Validate xsd schema
            faQueryMessage = unMarshallFaQueryMessage(requestStr);
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
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFaQueryMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        } catch (MessageException | ExchangeModelMarshallException e) {
            log.error("[ERROR] Error during validation of the received FLUXFaQueryMessage!", e);
        }
    }

    @Override
    public void evaluateSetFluxFaResponseRequest(SetFluxFaResponseMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FLUXResponseMessage with GUID [[ " + logGuid + " ]].");
        FLUXResponseMessage fluxResponseMessage;
        try {
            // Validate xsd schema
            fluxResponseMessage = unMarshallFluxResponseMessage(requestStr);
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

    private boolean isCorrectUUID(List<IDType> ids) {
        boolean uuidIsCorrect = false;
        String uuidString = null;
        try {
            if (CollectionUtils.isNotEmpty(ids)) {
                IDType idType = ids.get(0);
                uuidString = idType.getValue();
                String schemeID = idType.getSchemeID();
                if ("UUID".equals(schemeID)) {
                    uuidIsCorrect = StringUtils.equalsIgnoreCase(UUID.fromString(uuidString).toString(),uuidString);
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

    private FLUXFAReportMessage unMarshallAndValidateSchema(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXFAReportMessage.class, loadXSDSchema(FLUXFAREPORT_MESSAGE_3P1_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    private FLUXFAQueryMessage unMarshallFaQueryMessage(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXFAQueryMessage.class, loadXSDSchema(FLUXFAQUERY_MESSAGE_3P0_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    private FLUXResponseMessage unMarshallFluxResponseMessage(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXResponseMessage.class, loadXSDSchema(FLUXFARESPONSE_MESSAGE_6P0_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    private Schema loadXSDSchema(String xsdLocation) throws UnmarshalException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = getClass().getClassLoader().getResource(xsdLocation);
        if (resource != null) {
            try {
                return sf.newSchema(resource);
            } catch (SAXException e) {
                throw new UnmarshalException(e.getMessage(), e);
            }
        }
        throw new UnmarshalException("ERROR WHILE TRYING TO LOOKUP XSD SCHEMA");
    }

    private void sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(String rawMessage, RulesBaseRequest request, FLUXFAQueryMessage queryMessage, Rule9998Or9999ErrorType type, String onValue) {
        if (request == null || type == null) {
            log.error("Could not send FLUXResponseMessage. Request is null or Rule9998Or9999ErrorType not provided.");
            return;
        }
        RuleError ruleWarning;
        if (Rule9998Or9999ErrorType.EMPTY_REPORT.equals(type)) {
            ruleWarning = new RuleError(ServiceConstants.EMPTY_REPORT_RULE, ServiceConstants.EMPTY_REPORT_RULE_MESSAGE, "L03", Collections.<String>singletonList(null));
        } else {
            ruleWarning = new RuleError(ServiceConstants.PERMISSION_DENIED_RULE, ServiceConstants.PERMISSION_DENIED_RULE_MESSAGE, "L00", Collections.<String>singletonList(null));
        }
        ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResultForGeneralBusinessRules(ruleWarning, rawMessage, request.getLogGuid(), RawMsgType.FA_REPORT);
        validationResultDto.setError(true);
        validationResultDto.setOk(false);
        FLUXResponseMessage fluxResponseMessage = generateFluxResponseMessageForFaQuery(validationResultDto, queryMessage, onValue);
        log.debug("FLUXResponseMessage has been generated after exception: " + fluxResponseMessage);
        validateAndSendResponseToExchange(fluxResponseMessage, request, PluginType.FLUX, true);
    }

    private void sendFLUXResponseMessageOnException(String errorMessage, String rawMessage, RulesBaseRequest request, Object message) {
        if (request == null) {
            log.error("Could not send FLUXResponseMessage. Request is null.");
            return;
        }
        List<String> xpaths = new ArrayList<>();
        xpaths.add(errorMessage);
        RuleError ruleError = new RuleError(ServiceConstants.INVALID_XML_RULE, ServiceConstants.INVALID_XML_RULE_MESSAGE, "L00", xpaths);
        RawMsgType messageType = getMessageType(request.getMethod());
        ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResultForGeneralBusinessRules(ruleError, rawMessage, request.getLogGuid(), messageType);
        validationResultDto.setError(true);
        validationResultDto.setOk(false);
        FLUXResponseMessage fluxResponseMessage = null;
        if (messageType != null) {
            switch (messageType) {
                case MOVEMENT:
                    break;
                case POLL:
                    break;
                case ALARM:
                    break;
                case UNKNOWN:
                    break;
                case SALES_QUERY:
                    break;
                case SALES_REPORT:
                    break;
                case SALES_RESPONSE:
                    break;
                case FA_QUERY:
                    fluxResponseMessage = generateFluxResponseMessageForFaQuery(validationResultDto, message != null ? (FLUXFAQueryMessage) message : null, request.getOnValue());
                    break;
                case FA_REPORT:
                    fluxResponseMessage = generateFluxResponseMessageForFaReport(validationResultDto, message != null ? (FLUXFAReportMessage) message : null);
                    break;
                case FA_RESPONSE:
                    fluxResponseMessage = generateFluxResponseMessageForFaResponse(validationResultDto, message != null ? (FLUXResponseMessage) message : null);
                    break;
                default:
                    fluxResponseMessage = generateFluxResponseMessage(validationResultDto);
            }
        }
        if (fluxResponseMessage != null) {
            fillFluxTLOnValue(fluxResponseMessage, request.getOnValue());
            log.debug("FLUXResponseMessage has been generated after exception: " + fluxResponseMessage);
            validateAndSendResponseToExchange(fluxResponseMessage, request, PluginType.FLUX, false);
        }
    }

    private RawMsgType getMessageType(RulesModuleMethod method) {
        if (null == method) {
            return null;
        }
        RawMsgType msgType = null;
        if (RulesModuleMethod.SET_FLUX_FA_REPORT.equals(method) || RulesModuleMethod.SEND_FLUX_FA_REPORT.equals(method)) {
            msgType = RawMsgType.FA_REPORT;
        } else if (RulesModuleMethod.SET_FLUX_FA_QUERY.equals(method) || RulesModuleMethod.SEND_FLUX_FA_QUERY.equals(method)) {
            msgType = RawMsgType.FA_QUERY;
        } else if (RulesModuleMethod.SET_FLUX_RESPONSE.equals(method) || RulesModuleMethod.RCV_FLUX_RESPONSE.equals(method)) {
            msgType = RawMsgType.FA_RESPONSE;
        }
        return msgType;
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

    private List<ValidationResultDocument> getValidationResultDocument(ValidationResultDto faReportValidationResult) throws DatatypeConfigurationException {
        ValidationResultDocument validationResultDocument = new ValidationResultDocument();

        GregorianCalendar date = DateTime.now(DateTimeZone.UTC).toGregorianCalendar();
        XMLGregorianCalendar calender = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
        DateTimeType dateTime = new DateTimeType();
        dateTime.setDateTime(calender);
        validationResultDocument.setCreationDateTime(dateTime);

        IDType idType = new IDType();
        String fluxNationCode = ruleModuleCache.getSingleConfig(FLUX_LOCAL_NATION_CODE);
        idType.setValue(StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "XEU");
        idType.setSchemeID("FLUX_GP_PARTY");
        validationResultDocument.setValidatorID(idType);

        List<ValidationQualityAnalysis> validationQuality = new ArrayList<>();
        for (ValidationMessageType validationMessage : faReportValidationResult.getValidationMessages()) {
            ValidationQualityAnalysis analysis = new ValidationQualityAnalysis();

            IDType identification = new IDType();
            identification.setValue(validationMessage.getBrId());
            identification.setSchemeID("FA_BR");
            analysis.setID(identification);

            CodeType level = new CodeType();
            level.setValue(validationMessage.getLevel());
            level.setListID("FLUX_GP_VALIDATION_LEVEL");
            analysis.setLevelCode(level);

            eu.europa.ec.fisheries.uvms.rules.service.constants.ErrorType errorType = codeTypeMapper.mapErrorType(validationMessage.getErrorType());

            CodeType type = new CodeType();
            type.setValue(errorType.name());
            type.setListID("FLUX_GP_VALIDATION_TYPE");
            analysis.setTypeCode(type);

            TextType text = new TextType();
            text.setValue(validationMessage.getMessage());
            text.setLanguageID("GBR");
            analysis.getResults().add(text);

            List<String> xpaths = validationMessage.getXpaths();
            if (CollectionUtils.isNotEmpty(xpaths)) {
                for (String xpath : xpaths) {
                    TextType referenceItem = new TextType();
                    referenceItem.setValue(xpath);
                    referenceItem.setLanguageID("XPATH");
                    analysis.getReferencedItems().add(referenceItem);
                }
            }

            validationQuality.add(analysis);
        }
        validationResultDocument.setRelatedValidationQualityAnalysises(validationQuality);
        return singletonList(validationResultDocument);
    }

    @Override public FLUXResponseMessage generateFluxResponseMessageForFaReport(ValidationResultDto faReportValidationResult, FLUXFAReportMessage fluxfaReportMessage) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {
            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            if (fluxfaReportMessage != null && fluxfaReportMessage.getFLUXReportDocument() != null) {
                List<IDType> requestId = fluxfaReportMessage.getFLUXReportDocument().getIDS();
                fluxResponseDocument.setReferencedID((requestId != null && !requestId.isEmpty()) ? requestId.get(0) : null); // Set Request Id
            }
            populateFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
            responseMessage.setFLUXResponseDocument(fluxResponseDocument);
            return responseMessage;

        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }

    @Override public FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {
            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            populateFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
            responseMessage.setFLUXResponseDocument(fluxResponseDocument);
            return responseMessage;
        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }

    private void fillFluxTLOnValue(FLUXResponseMessage fluxResponseMessage, String onValue) {
        IDType idType = new IDType();
        idType.setSchemeID("FLUXTL_ON");
        idType.setValue(onValue);
        fluxResponseMessage.getFLUXResponseDocument().setReferencedID(idType);
    }

    @Override public FLUXResponseMessage generateFluxResponseMessageForFaQuery(ValidationResultDto faReportValidationResult, FLUXFAQueryMessage fluxfaQueryMessage, String onValue) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {
            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            responseMessage.setFLUXResponseDocument(fluxResponseDocument);
            if (fluxfaQueryMessage != null && fluxfaQueryMessage.getFAQuery() != null) {
                final IDType guid = fluxfaQueryMessage.getFAQuery().getID();
                if (isCorrectUUID(Collections.singletonList(guid))) {
                    fluxResponseDocument.setReferencedID(guid);
                } else {
                    fillFluxTLOnValue(responseMessage, onValue);
                }
            }
            populateFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }

    @Override public FLUXResponseMessage generateFluxResponseMessageForFaResponse(ValidationResultDto faReportValidationResult, FLUXResponseMessage fluxResponseMessage) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {
            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            if (fluxResponseMessage != null && fluxResponseMessage.getFLUXResponseDocument() != null) {
                List<IDType> ids = fluxResponseMessage.getFLUXResponseDocument().getIDS();
                fluxResponseDocument.setReferencedID((CollectionUtils.isNotEmpty(ids)) ? ids.get(0) : null);
            }
            populateFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
            responseMessage.setFLUXResponseDocument(fluxResponseDocument);
        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }


    private void setFluxResponseDocumentIDs(FLUXResponseDocument fluxResponseDocument) {
        IDType responseId = new IDType();
        responseId.setValue(UUID.randomUUID().toString());
        responseId.setSchemeID("UUID");
        fluxResponseDocument.setIDS(singletonList(responseId)); // Set random ID
    }

    private void populateFluxResponseDocument(ValidationResultDto faReportValidationResult, FLUXResponseDocument fluxResponseDocument) throws DatatypeConfigurationException {
        setFluxResponseDocumentIDs(fluxResponseDocument);
        setFluxResponseCreationDate(fluxResponseDocument);
        setFluxResponseDocumentResponseCode(faReportValidationResult, fluxResponseDocument);
        // INFO : From IMPL DOC 2.2 This tag (RejectionReason) will not be there! Requested by DG MAre
        //setFluxResponseDocumentRejectionReason(faReportValidationResult, fluxResponseDocument);
        setFluxResponseDocumentRelatedValidationResultDocuments(faReportValidationResult, fluxResponseDocument);
        setFluxReportDocumentRespondentFluxParty(fluxResponseDocument);
    }

    private void setFluxReportDocumentRespondentFluxParty(FLUXResponseDocument fluxResponseDocument) {
        fluxResponseDocument.setRespondentFLUXParty(getRespondedFluxParty()); // Set flux party in the response
    }

    private void setFluxResponseDocumentRelatedValidationResultDocuments(ValidationResultDto faReportValidationResult, FLUXResponseDocument fluxResponseDocument) throws DatatypeConfigurationException {
        fluxResponseDocument.setRelatedValidationResultDocuments(getValidationResultDocument(faReportValidationResult)); // Set validation result
    }

    private void setFluxResponseDocumentResponseCode(ValidationResultDto faReportValidationResult, FLUXResponseDocument fluxResponseDocument) {
        CodeType responseCode = new CodeType();
        if (faReportValidationResult.isError()) {
            responseCode.setValue("NOK");
        } else if (faReportValidationResult.isWarning()) {
            responseCode.setValue("WOK");
        } else {
            responseCode.setValue("OK");
        }
        responseCode.setListID("FLUX_GP_RESPONSE");
        fluxResponseDocument.setResponseCode(responseCode); // Set response Code
    }

    private void setFluxResponseCreationDate(FLUXResponseDocument fluxResponseDocument) throws DatatypeConfigurationException {
        GregorianCalendar date = DateTime.now(DateTimeZone.UTC).toGregorianCalendar();
        XMLGregorianCalendar calender = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
        DateTimeType dateTime = new DateTimeType();
        dateTime.setDateTime(calender);
        fluxResponseDocument.setCreationDateTime(dateTime); // Set creation date time
    }

    private FLUXParty getRespondedFluxParty() {
        IDType idType = new IDType();
        String fluxNationCode = ruleModuleCache.getSingleConfig(FLUX_LOCAL_NATION_CODE);
        String nationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "XEU";
        idType.setValue(nationCode);
        idType.setSchemeID("FLUX_GP_PARTY");
        FLUXParty fluxParty = new FLUXParty();
        fluxParty.setIDS(singletonList(idType));
        return fluxParty;
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

    private void sendToSales(String message) throws MessageException {
        producer.sendDataSourceMessage(message, DataSourceQueue.SALES);
    }

    private void sendToExchange(String message) throws MessageException {
        producer.sendDataSourceMessage(message, DataSourceQueue.EXCHANGE);
    }

    @Override public void validateAndSendResponseToExchange(FLUXResponseMessage fluxResponseMessageObj, RulesBaseRequest request, PluginType pluginType, boolean correctGuidProvided) {
        try {
            log.info("[START] Preparing FLUXResponseMessage to send back to Exchange module.");
            if (!correctGuidProvided) {
                fillFluxTLOnValue(fluxResponseMessageObj, request.getOnValue());
            }

            // Get fluxNationCode (Eg. XEU) from Config Module.
            String fluxNationCode = ruleModuleCache.getSingleConfig("flux_local_nation_code");
            String nationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "flux_local_nation_code_is_missing_in_config_settings_table_please_set_it";

            String fluxResponse = JAXBUtils.marshallJaxBObjectToString(fluxResponseMessageObj, "UTF-8", false, new FANamespaceMapper());
            String logGuid = request.getLogGuid();
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, fluxNationCode);
            List<AbstractFact> fluxResponseFacts = rulesEngine.evaluate(SENDING_FA_RESPONSE_MSG, fluxResponseMessageObj, extraValues);
            ValidationResultDto fluxResponseValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(fluxResponseFacts, fluxResponse, logGuid, RawMsgType.FA_RESPONSE);
            ExchangeLogStatusTypeType status = calculateMessageValidationStatus(fluxResponseValidationResult);
            //Create Response

            String df = request.getFluxDataFlow(); //e.g. "urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2" // TODO should come from subscription. Also could be a link between DF and AD value
            String destination = request.getSenderOrReceiver();  // e.g. "AHR:VMS"
            String onValue = request.getOnValue();
            // We need to link the message that came in with the FLUXResponseMessage we're sending... That's the why of the commented line here..
            //String messageGuid = ActivityFactMapper.getUUID(fluxResponseMessageType.getFLUXResponseDocument().getIDS());
            String fluxFAReponseText = ExchangeModuleRequestMapper.createFluxFAResponseRequestWithOnValue(fluxResponse, request.getUsername(), df, logGuid, nationCode, onValue, status, destination, getExchangePluginType(pluginType));
            log.debug("Message to exchange {}", fluxFAReponseText);
            producer.sendDataSourceMessage(fluxFAReponseText, DataSourceQueue.EXCHANGE);
            XPathRepository.INSTANCE.clear(fluxResponseFacts);
            log.info("[END] FLUXFAResponse successfully sent back to Exchange.");
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
            sendFLUXResponseMessageOnException(e.getMessage(), null, request, fluxResponseMessageObj);
        } catch (ExchangeModelMarshallException | MessageException | RulesValidationException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    /*
     * Maps a Request String to a eu.europa.ec.fisheries.schema.exchange.module.v1.SetFLUXMDRSyncMessageRequest
     * to send a message to ExchangeModule
     *
     * @see eu.europa.ec.fisheries.uvms.rules.service.RulesService#mapAndSendFLUXMdrRequestToExchange(java.lang.String)
     */
    @Override public void mapAndSendFLUXMdrRequestToExchange(String request, String fr) {
        String exchangerStrReq;
        try {
            exchangerStrReq = ExchangeModuleRequestMapper.createFluxMdrSyncEntityRequest(request, StringUtils.EMPTY, fr);
            if (StringUtils.isNotEmpty(exchangerStrReq)) {
                producer.sendDataSourceMessage(exchangerStrReq, DataSourceQueue.EXCHANGE);
            } else {
                log.error("ERROR : REQUEST TO BE SENT TO EXCHANGE MODULE RESULTS NULL. NOT SENDING IT!");
            }
        } catch (ExchangeModelMarshallException e) {
            log.error("Unable to marshall SetFLUXMDRSyncMessageRequest in RulesServiceBean.mapAndSendFLUXMdrRequestToExchange(String) : " + e.getMessage());
        } catch (MessageException e) {
            log.error("Unable to send SetFLUXMDRSyncMessageRequest to ExchangeModule : " + e.getMessage());
        }
    }

    @Override public void mapAndSendFLUXMdrResponseToMdrModule(String request) {
        String mdrSyncResponseReq;
        try {
            mdrSyncResponseReq = MdrModuleMapper.createFluxMdrSyncEntityRequest(request, StringUtils.EMPTY);
            if (StringUtils.isNotEmpty(mdrSyncResponseReq)) {
                producer.sendDataSourceMessage(mdrSyncResponseReq, DataSourceQueue.MDR_EVENT);
            } else {
                log.error("ERROR : REQUEST TO BE SENT TO MDR MODULE RESULTS NULL. NOT SENDING IT!");
            }
        } catch (MdrModelMarshallException e) {
            log.error("Unable to marshall SetFLUXMDRSyncMessageResponse in RulesServiceBean.mapAndSendFLUXMdrResponseToMdrModule(String) : " + e.getMessage());
        } catch (MessageException e) {
            log.error("Unable to send SetFLUXMDRSyncMessageResponse to MDR Module : " + e.getMessage());
        }
    }

    private eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType getExchangePluginType(PluginType pluginType) {
        if (pluginType == PluginType.BELGIAN_ACTIVITY) {
            return eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType.BELGIAN_ACTIVITY;
        } else {
            return eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType.FLUX;
        }
    }
}