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

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_SALES_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_SALES_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_SALES_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.SENDING_FA_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.SENDING_FA_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.SENDING_FA_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ORIGINATING_PLUGIN;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static java.util.Collections.singletonList;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jms.TextMessage;
import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesQueryRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageTypeResponse;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesQueryMessage;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesResponseMessage;
import eu.europa.ec.fisheries.schema.sales.Report;
import eu.europa.ec.fisheries.schema.sales.SalesIdType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SyncAsyncRequestType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.RulesMessageService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesMessageFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.config.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.interceptor.RulesPreValidationInterceptor;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CodeTypeMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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
    private static final String TRIGGERING_DROOLS_VALIDATION_ON_MESSAGE = "[INFO] Triggering drools validation on message...";
    private static final String VALIDATION_RESULTED_IN_ERRORS = "[WARN] Validation resulted in errors. Not going to send msg to Activity module..";
    private static final List<String> RULES_TO_USE_ON_VALUE = Arrays.asList("SALE-L01-00-0011", "SALE-L01-00-0400");

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
    private ParameterService parameterService;

    @EJB
    private SalesMessageFactory salesMessageFactory;

    @EJB
    private RulesActivityServiceBean activityServiceBean;

    @EJB
    private ActivityOutQueueConsumer activityConsumer;

    @EJB
    private RulesExtraValuesMapGeneratorBean extraValueGenerator;

    @Override
    //@Interceptors(RulesPreValidationInterceptor.class)
    public void receiveSalesQueryRequest(ReceiveSalesQueryRequest receiveSalesQueryRequest) {
        try {
            //get sales query message
            String salesQueryMessageAsString = receiveSalesQueryRequest.getRequest();
            String logGuid = receiveSalesQueryRequest.getLogGuid();

            FLUXSalesQueryMessage salesQueryMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesQueryMessageAsString, FLUXSalesQueryMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new HashMap<>();
            extraValues.put(SENDER_RECEIVER, receiveSalesQueryRequest.getSender());
            extraValues.put(ORIGINATING_PLUGIN, receiveSalesQueryRequest.getPluginType());

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_QUERY_MSG, salesQueryMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesQueryMessageAsString, logGuid, RawMsgType.SALES_QUERY);

            //send to sales
            if (validationResult.isError()) {
                String requestForSales;
                if (shouldUseFluxOn(validationResult)) {
                    requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesQueryRequest.getOnValue(),
                            validationResult, receiveSalesQueryRequest.getPluginType(), receiveSalesQueryRequest.getSender(), SalesIdType.FLUXTL_ON);
                } else {
                    requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesQueryRequest.getMessageGuid(),
                            validationResult, receiveSalesQueryRequest.getPluginType(), receiveSalesQueryRequest.getSender(), SalesIdType.GUID);
                }

                sendToSales(requestForSales);
            } else {
                String requestForSales = salesMessageFactory.createSalesQueryRequest(receiveSalesQueryRequest.getRequest(), validationResult, receiveSalesQueryRequest.getPluginType());
                sendToSales(requestForSales);
            }
            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (SalesMarshallException | RulesValidationException | MessageException e) {
            throw new RulesServiceException("Couldn't validate sales query", e);
        }
    }

    @Override
    @Interceptors(RulesPreValidationInterceptor.class)
    public void receiveSalesReportRequest(ReceiveSalesReportRequest receiveSalesReportRequest) {
        try {
            //get sales report message
            String salesReportMessageAsString = receiveSalesReportRequest.getRequest();
            String logGuid = receiveSalesReportRequest.getLogGuid();
            Report salesReportMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesReportMessageAsString, Report.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new HashMap<>();
            extraValues.put(SENDER_RECEIVER, receiveSalesReportRequest.getSender());
            extraValues.put(ORIGINATING_PLUGIN, receiveSalesReportRequest.getPluginType());

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_REPORT_MSG, salesReportMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesReportMessageAsString, logGuid, RawMsgType.SALES_REPORT);

            //send to sales
            if (validationResult.isError()) {
                String requestForSales = createInvalidSalesResponseMessage(receiveSalesReportRequest, validationResult);
                sendToSales(requestForSales);
            } else {
                String requestForSales = salesMessageFactory.createSalesReportRequest(receiveSalesReportRequest.getRequest(), validationResult, receiveSalesReportRequest.getPluginType());
                sendToSales(requestForSales);
            }
            //update log status
            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (SalesMarshallException | RulesValidationException | MessageException e) {
            throw new RulesServiceException("Couldn't validate sales report", e);
        }
    }

    private String createInvalidSalesResponseMessage(ReceiveSalesReportRequest receiveSalesReportRequest, ValidationResultDto validationResult) throws SalesMarshallException {
        String requestForSales;
        if (shouldUseFluxOn(validationResult)) {
            requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesReportRequest.getOnValue(), validationResult,
                    receiveSalesReportRequest.getPluginType(), receiveSalesReportRequest.getSender(), SalesIdType.FLUXTL_ON);
        } else {
            requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesReportRequest.getMessageGuid(), validationResult,
                    receiveSalesReportRequest.getPluginType(), receiveSalesReportRequest.getSender(), SalesIdType.GUID);
        }
        return requestForSales;
    }

    protected boolean shouldUseFluxOn(ValidationResultDto validationResult) {
        for (ValidationMessageType validationMessage : validationResult.getValidationMessages()) {
            if (RULES_TO_USE_ON_VALUE.contains(validationMessage.getBrId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    //@Interceptors(RulesPreValidationInterceptor.class)
    public void receiveSalesResponseRequest(ReceiveSalesResponseRequest rulesRequest) {
        try {
            //get sales response message
            String salesResponseMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            FLUXSalesResponseMessage salesResponseMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesResponseMessageAsString, FLUXSalesResponseMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new HashMap<>();
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue("flux_local_nation_code"));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_RESPONSE_MSG, salesResponseMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesResponseMessageAsString, logGuid, RawMsgType.SALES_RESPONSE);

            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (SalesMarshallException | RulesValidationException e) {
            throw new RulesServiceException("Couldn't validate sales response", e);
        } catch (ConfigServiceException e) {
            throw new RulesServiceException("Couldn't retrieve the FLUX local nation code from the settings", e);
        }
    }

    @Override
    //@Interceptors(RulesPreValidationInterceptor.class)
    public void sendSalesResponseRequest(SendSalesResponseRequest rulesRequest) {
        try {
            //get sales response message
            String salesResponseMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            FLUXSalesResponseMessage salesResponseMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesResponseMessageAsString, FLUXSalesResponseMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new HashMap<>();
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue("flux_local_nation_code"));
            extraValues.put(ORIGINATING_PLUGIN, rulesRequest.getPluginToSendResponseThrough());

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_RESPONSE_MSG, salesResponseMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesResponseMessageAsString, logGuid, RawMsgType.SALES_RESPONSE);
            ExchangeLogStatusTypeType validationStatus = calculateMessageValidationStatus(validationResult);

            //send to exchange
            String requestForExchange = ExchangeModuleRequestMapper.createSendSalesResponseRequest(rulesRequest.getRequest(),
                    rulesRequest.getMessageGuid(),
                    rulesRequest.getFluxDataFlow(),
                    rulesRequest.getRecipient(),
                    rulesRequest.getDateSent(),
                    validationStatus,
                    rulesRequest.getPluginToSendResponseThrough());
            sendToExchange(requestForExchange);
        } catch (ExchangeModelMarshallException | MessageException | SalesMarshallException | RulesValidationException | ConfigServiceException e) {
            throw new RulesServiceException("Couldn't validate sales response", e);
        }
    }


    @Override
   // @Interceptors(RulesPreValidationInterceptor.class)
    public void sendSalesReportRequest(SendSalesReportRequest rulesRequest) {
        try {
            //get sales report message
            String salesReportMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            FLUXSalesReportMessage salesReportMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesReportMessageAsString, FLUXSalesReportMessage.class);

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_REPORT_MSG, salesReportMessage);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesReportMessageAsString, logGuid, RawMsgType.SALES_REPORT);
            ExchangeLogStatusTypeType validationStatus = calculateMessageValidationStatus(validationResult);

            //send to exchange
            String requestForExchange = ExchangeModuleRequestMapper.createSendSalesReportRequest(rulesRequest.getRequest(),
                    rulesRequest.getMessageGuid(),
                    rulesRequest.getFluxDataFlow(),
                    rulesRequest.getRecipient(),
                    rulesRequest.getDateSent(),
                    validationStatus,
                    rulesRequest.getPluginToSendResponseThrough());
            sendToExchange(requestForExchange);
        } catch (ExchangeModelMarshallException | MessageException | SalesMarshallException | RulesValidationException e) {
            throw new RulesServiceException("Couldn't validate sales report", e);
        }
    }


    @Override
    //@Interceptors(RulesPreValidationInterceptor.class)
    @Lock(LockType.READ)
    public void evaluateReceiveFLUXFAReportRequest(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FLUXFAReportMessage with GUID [[ " + logGuid + " ]].");
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {
            // Validate xsd schema
            fluxfaReportMessage = extractFluxFaReportMessage(requestStr);
            if (fluxfaReportMessage != null) {
                FLUXResponseMessage fluxResponseMessageType;
                Map<Boolean, ValidationResultDto> validationMap = rulesPreProcessBean.checkDuplicateIdInRequest(fluxfaReportMessage);
                boolean needToValidate = validationIsToContinue(validationMap);
                log.info("[INFO] Validation needs to continue : [[ " + needToValidate + " ]].");
                if (needToValidate) {
                    Map<ExtraValueType, Object> extraValueTypeObjectMap = extraValueGenerator.generateExtraValueMap(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage);
                    extraValueTypeObjectMap.put(SENDER_RECEIVER, request.getSenderOrReceiver());
                    log.info(TRIGGERING_DROOLS_VALIDATION_ON_MESSAGE);
                    List<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValueTypeObjectMap);
                    ValidationResultDto faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, requestStr, logGuid, RawMsgType.FA_REPORT);
                    updateValidationResultWithExisting(faReportValidationResult, validationMap.get(true));
                    updateRequestMessageStatusInExchange(logGuid, faReportValidationResult);
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
                    fluxResponseMessageType = generateFluxResponseMessageForFaReport(faReportValidationResult, fluxfaReportMessage);
                    XPathRepository.INSTANCE.clear(faReportFacts);
                } else {
                    log.info("[WARNING] Found already existing Validation(s) for message with GUID [" + logGuid + "]. \nNot going to process or send it to Business module!");
                    updateRequestMessageStatusInExchange(logGuid, validationMap.get(false));
                    fluxResponseMessageType = generateFluxResponseMessageForFaReport(validationMap.get(false), fluxfaReportMessage);
                    log.info("[INFO] The Validation of FLUXFAReport is complete and FluxResponse is generated");
                }
                List<IDType> reportGUID = null;
                if(fluxfaReportMessage.getFLUXReportDocument() != null){
                    reportGUID = fluxfaReportMessage.getFLUXReportDocument().getIDS();
                }
                validateAndSendResponseToExchange(fluxResponseMessageType, request, request.getType(), isCorrectUUID(reportGUID));
            }
        } catch (SAXException | RulesModelMarshallException e) {
            log.error("[ERROR] Error while trying to parse FLUXFAReportMessage received message! It is malformed!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
            throw new RulesServiceException(e.getMessage(), e);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAReportMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        }
    }

    @Override
    public void evaluateSendFaReportMessage(SetFLUXFAReportMessageRequest request) {
        final String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FLUXFAReportMessage with GUID [[ " + logGuid + " ]].");
        FLUXFAReportMessage fluxfaReportMessage = null;
        try {
            // Validate xsd schema
            fluxfaReportMessage = extractFluxFaReportMessage(requestStr);
            if (fluxfaReportMessage != null) {
                Map<Boolean, ValidationResultDto> validationMap = rulesPreProcessBean.checkDuplicateIdInRequest(fluxfaReportMessage);
                boolean needToValidate = validationIsToContinue(validationMap);
                log.info("[INFO] Validation needs to continue : [[ " + needToValidate + " ]].");
                if (needToValidate) {
                    Map<ExtraValueType, Object> extraValueTypeObjectMap = extraValueGenerator.generateExtraValueMap(SENDING_FA_REPORT_MSG, fluxfaReportMessage);
                    extraValueTypeObjectMap.put(SENDER_RECEIVER, request.getSenderOrReceiver());
                    log.info(TRIGGERING_DROOLS_VALIDATION_ON_MESSAGE);
                    List<AbstractFact> faReportFacts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, fluxfaReportMessage, extraValueTypeObjectMap);
                    ValidationResultDto faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, requestStr, logGuid, RawMsgType.FA_REPORT);
                    updateValidationResultWithExisting(faReportValidationResult, validationMap.get(true));
                    if (faReportValidationResult != null && !faReportValidationResult.isError()) {
                        log.info("[INFO] The Validation of FLUXFAReportMessage is successful, forwarding message to Exchange");
                        sendToExchange(ExchangeModuleRequestMapper
                                .createSendFaReportMessageRequest(request.getRequest(), "flux", logGuid, request.getFluxDataFlow(),
                                        request.getSenderOrReceiver(), request.getOnValue()));
                    } else {
                        log.info("[WARN] Validation resulted in errors. Not going to send msg to Exchange module..");
                    }
                    XPathRepository.INSTANCE.clear(faReportFacts);
                } else {
                    log.info("[WARNING] Found already existing Validation(s) for message with GUID [" + logGuid + "]. \nNot going to process or send it to Exchange module!");
                }
            }
        } catch (SAXException | RulesModelMarshallException e) {
            log.error("[ERROR] Error while trying to parse FLUXFAReportMessage received message! It is malformed!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
            throw new RulesServiceException(e.getMessage(), e);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAReportMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, fluxfaReportMessage);
        } catch (MessageException | ExchangeModelMarshallException e) {
            e.printStackTrace();
        }
    }


    @Override
    //@Interceptors(RulesPreValidationInterceptor.class)
    @Lock(LockType.READ)
    public void evaluateReceiveFaQueryRequest(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        final String onValue = request.getOnValue();
        log.info("[INFO] Going to evaluate FAQuery with GUID [[ " + logGuid + " ]].");
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            // Validate xsd schema
            faQueryMessage = extractFaQueryMessage(requestStr);
            if (faQueryMessage != null) {
                FLUXResponseMessage fluxResponseMessageType;
                Map<Boolean, ValidationResultDto> validationMap = rulesPreProcessBean.checkDuplicateIdInRequest(faQueryMessage);
                boolean needToValidate = validationIsToContinue(validationMap);
                log.info("[INFO] Validation needs to continue : [[ " + needToValidate + " ]].");
                boolean needToSendToExchange = true;
                SetFLUXFAReportMessageRequest setFLUXFAReportMessageRequest = null;
                if (needToValidate) {
                    Map<ExtraValueType, Object> extraValueTypeObjectMap = extraValueGenerator.generateExtraValueMap(RECEIVING_FA_QUERY_MSG, faQueryMessage);
                    extraValueTypeObjectMap.put(SENDER_RECEIVER, request.getSenderOrReceiver());
                    log.info(TRIGGERING_DROOLS_VALIDATION_ON_MESSAGE);
                    List<AbstractFact> faQueryFacts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, faQueryMessage, extraValueTypeObjectMap);
                    ValidationResultDto faQueryValidationReport = rulePostProcessBean.checkAndUpdateValidationResult(faQueryFacts, requestStr, logGuid, RawMsgType.FA_QUERY);
                    updateValidationResultWithExisting(faQueryValidationReport, validationMap.get(true));
                    updateRequestMessageStatusInExchange(logGuid, faQueryValidationReport);
                    if (faQueryValidationReport != null && !faQueryValidationReport.isError()) {
                        log.info("[INFO] The Validation of FaQueryMessage is successful, going to check permissions (Subscriptions)..");
                        boolean hasPermissions = activityServiceBean.checkSubscriptionPermissions(requestStr, MessageType.FLUX_FA_QUERY_MESSAGE);
                        if (hasPermissions) { // Send query to activity.
                            log.info("[INFO] Request has permissions. Going to send FaQuery to Activity Module...");
                            setFLUXFAReportMessageRequest = sendSyncQueryRequestToActivity(requestStr, request.getUsername(), request.getType());
                            if(setFLUXFAReportMessageRequest.isIsEmptyReport()){
                                log.info("[WARN] The report generated from Activity doesn't contain data (Empty report)!");
                                updateRequestMessageStatusInExchange(logGuid, ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS);
                                sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.EMPTY_REPORT, onValue);
                                needToSendToExchange = false;
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
                } else {
                    log.info("[WARNING] Found already existing Validation(s) for message with GUID [" + logGuid + "]. Not going to process or send it to Activity module!");
                    updateRequestMessageStatusInExchange(logGuid, validationMap.get(false));
                    fluxResponseMessageType = generateFluxResponseMessageForFaQuery(validationMap.get(false), faQueryMessage, onValue);
                    log.info("[END] Validation of FLUXFAQuery is complete and FluxResponse is generated");
                }
                // A Response won't be sent only in the case of permissionDenied from Subscription,
                // since in this particular case a response will be send in the spot, and there's no need to send it here also.
                if(needToSendToExchange){
                    IDType faQueryGUID = null;
                    if(faQueryMessage.getFAQuery() != null){
                        faQueryGUID = faQueryMessage.getFAQuery().getID();
                    }
                    validateAndSendResponseToExchange(fluxResponseMessageType, request, request.getType(), isCorrectUUID(Collections.singletonList(faQueryGUID)));
                }

                // We have received a SetFLUXFAReportMessageRequest (from activity) and it contains reports so needs to be processed.
                if(setFLUXFAReportMessageRequest != null && !setFLUXFAReportMessageRequest.isIsEmptyReport()){
                    evaluateSendFaReportMessage(setFLUXFAReportMessageRequest);
                }
            }
        } catch (SAXException | RulesModelMarshallException e) {
            log.error("[ERROR] Error while trying to parse FLUXFAQueryMessage received message! It is malformed!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
            throw new RulesServiceException(e.getMessage(), e);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFAQueryMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        }
    }


    @Override
    public void evaluateSendFaQueryRequest(SetFaQueryMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FAQuery with GUID [[ " + logGuid + " ]].");
        FLUXFAQueryMessage faQueryMessage = null;
        try {
            // Validate xsd schema
            faQueryMessage = extractFaQueryMessage(requestStr);
            if (faQueryMessage != null) {
                Map<Boolean, ValidationResultDto> validationMap = rulesPreProcessBean.checkDuplicateIdInRequest(faQueryMessage);
                boolean needToValidate = validationIsToContinue(validationMap);
                log.info("[INFO] Validation needs to continue : [[ " + needToValidate + " ]].");
                if (needToValidate) {
                    Map<ExtraValueType, Object> extraValueTypeObjectMap = extraValueGenerator.generateExtraValueMap(SENDING_FA_QUERY_MSG, faQueryMessage);
                    extraValueTypeObjectMap.put(SENDER_RECEIVER, request.getSenderOrReceiver());
                    log.info(TRIGGERING_DROOLS_VALIDATION_ON_MESSAGE);
                    List<AbstractFact> faQueryFacts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, faQueryMessage, extraValueTypeObjectMap);
                    ValidationResultDto faQueryValidationReport = rulePostProcessBean.checkAndUpdateValidationResult(faQueryFacts, requestStr, logGuid, RawMsgType.FA_QUERY);
                    updateValidationResultWithExisting(faQueryValidationReport, validationMap.get(true));
                    if (faQueryValidationReport != null && !faQueryValidationReport.isError()) {
                        log.info("[INFO] The Validation of FaQueryMessage is successful, forwarding message to Exchange");
                        String exchangeReq = ExchangeModuleRequestMapper
                                .createSendFaQueryMessageRequest(request.getRequest(), "flux", logGuid, request.getFluxDataFlow(), request.getSenderOrReceiver());
                        sendToExchange(exchangeReq);
                    } else {
                        log.info("[WARN] Validation resulted in errors. Not going to send msg to Exchange module..");
                    }
                    XPathRepository.INSTANCE.clear(faQueryFacts);
                } else {
                    log.info("[WARNING] Found already existing Validation(s) for message with GUID [" + logGuid + "]. Not going to process or send it to Exchange module!");
                }
            }
        } catch (SAXException | RulesModelMarshallException e) {
            log.error("[ERROR] Error while trying to parse FLUXFaQueryMessage received message! It is malformed!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, null);
            throw new RulesServiceException(e.getMessage(), e);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXFaQueryMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), requestStr, request, faQueryMessage);
        } catch (MessageException | ExchangeModelMarshallException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateSetFluxFaResponseRequest(SetFluxFaResponseMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("[INFO] Going to evaluate FLUXResponseMessage with GUID [[ " + logGuid + " ]].");
        FLUXResponseMessage fluxResponseMessage = null;
        try {
            // Validate xsd schema
            fluxResponseMessage = extractFluxResponseMessage(requestStr);
            if (fluxResponseMessage != null) {
                Map<Boolean, ValidationResultDto> validationMap = rulesPreProcessBean.checkDuplicateIdInRequest(fluxResponseMessage);
                boolean needToValidate = validationIsToContinue(validationMap);
                log.info("[INFO] Validation needs to continue : [[ " + needToValidate + " ]].");
                if (needToValidate) {
                    log.info(TRIGGERING_DROOLS_VALIDATION_ON_MESSAGE);
                    List<AbstractFact> fluxFaResponseFacts = rulesEngine.evaluate(RECEIVING_FA_RESPONSE_MSG, fluxResponseMessage, new HashMap<ExtraValueType, Object>());
                    ValidationResultDto fluxResponseValidResults = rulePostProcessBean.checkAndUpdateValidationResult(fluxFaResponseFacts, requestStr, logGuid, RawMsgType.FA_RESPONSE);
                    updateValidationResultWithExisting(fluxResponseValidResults, validationMap.get(true));
                    updateRequestMessageStatusInExchange(logGuid, fluxResponseValidResults);
                    if (fluxResponseValidResults != null && !fluxResponseValidResults.isError()) {
                        log.info("[INFO] The Validation of FLUXResponseMessage is successful, forwarding message to Exchange");
                    } else {
                        log.info("[WARN] Validation resulted in errors. Not going to send msg to Exchange module..");
                    }
                    XPathRepository.INSTANCE.clear(fluxFaResponseFacts);
                } else {
                    log.info("[WARNING] Found already existing Validation(s) for message with GUID [" + logGuid + "]. Not going to process!");
                }
            }
        } catch (SAXException | RulesModelMarshallException e) {
            log.error("[ERROR] Error while trying to parse FLUXResponseMessage received message! It is malformed!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            throw new RulesServiceException(e.getMessage(), e);
        } catch (RulesValidationException e) {
            log.error("[ERROR] Error during validation of the received FLUXResponseMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
        }
    }

    private boolean isCorrectUUID(List<IDType> ids){
        boolean uuidIsCorrect = false;
        String uuidString = null;
        try {
            if(CollectionUtils.isNotEmpty(ids)){
                uuidString =ids.get(0).getValue();
                UUID.fromString(uuidString);
                uuidIsCorrect = true;
            }
        } catch (IllegalArgumentException exception){
            log.warn("[WARN] The given UUID is not in a correct format {}", uuidString);
        }
        return uuidIsCorrect;
    }

    private ValidationResultDto generateValidationResultDtoForFailure() {
        ValidationResultDto resultDto = new ValidationResultDto();
        resultDto.setIsOk(false);
        resultDto.setIsError(true);
        return resultDto;
    }

    private FLUXFAReportMessage extractFluxFaReportMessage(String request) throws SAXException, RulesModelMarshallException {
        return JAXBMarshaller.unMarshallMessage(request, FLUXFAReportMessage.class, getSchemaForXsd(FLUXFAREPORT_MESSAGE_3P1_XSD));
    }

    private FLUXFAQueryMessage extractFaQueryMessage(String request) throws SAXException, RulesModelMarshallException {
        return JAXBMarshaller.unMarshallMessage(request, FLUXFAQueryMessage.class, getSchemaForXsd(FLUXFAQUERY_MESSAGE_3P0_XSD));
    }

    private FLUXResponseMessage extractFluxResponseMessage(String request) throws SAXException, RulesModelMarshallException {
        return JAXBMarshaller.unMarshallMessage(request, FLUXResponseMessage.class, getSchemaForXsd(FLUXFARESPONSE_MESSAGE_6P0_XSD));
    }

    private Schema getSchemaForXsd(String xsdLocation) throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = getClass().getClassLoader().getResource(xsdLocation);
        return sf.newSchema(resource);
    }

    private Boolean validationIsToContinue(Map<Boolean, ValidationResultDto> validationMap) {
        if (MapUtils.isNotEmpty(validationMap)) {
            return validationMap.entrySet().iterator().next().getKey();
        }
        return false;
    }

    private void sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(String rawMessage, RulesBaseRequest request, FLUXFAQueryMessage queryMessage, Rule9998Or9999ErrorType type, String onValue) {
        if (request == null || type == null) {
            log.error("Could not send FLUXResponseMessage. Request is null or Rule9998Or9999ErrorType not provided.");
            return;
        }
        RuleError ruleWarning;
        if(Rule9998Or9999ErrorType.EMPTY_REPORT.equals(type)){
            ruleWarning = new RuleError(ServiceConstants.EMPTY_REPORT_RULE, ServiceConstants.EMPTY_REPORT_RULE_MESSAGE, "L03", Collections.<String>singletonList(null));
        } else {
            ruleWarning = new RuleError(ServiceConstants.PERMISSION_DENIED_RULE, ServiceConstants.PERMISSION_DENIED_RULE_MESSAGE, "L00", Collections.<String>singletonList(null));
        }
        ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResultForGeneralBusinessRules(ruleWarning, rawMessage, request.getLogGuid(), RawMsgType.FA_REPORT);
        validationResultDto.setIsError(true);
        validationResultDto.setIsOk(false);
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
        validationResultDto.setIsError(true);
        validationResultDto.setIsOk(false);
        FLUXResponseMessage fluxResponseMessage;
        switch (messageType) {
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
        fillFluxTLOnValue(fluxResponseMessage, request.getOnValue());
        log.debug("FLUXResponseMessage has been generated after exception: " + fluxResponseMessage);
        validateAndSendResponseToExchange(fluxResponseMessage, request, PluginType.FLUX, false);
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
        updateRequestMessageStatusInExchange(logGuid, calculateMessageValidationStatus(validationResult));
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, statusType);
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

    private void updateValidationResultWithExisting(ValidationResultDto actualValidationResult, ValidationResultDto previousValidationResultDto) {
        if (previousValidationResultDto != null) {
            actualValidationResult.setIsError(actualValidationResult.isError() || previousValidationResultDto.isError());
            actualValidationResult.setIsWarning(actualValidationResult.isWarning() || previousValidationResultDto.isWarning());
            actualValidationResult.setIsOk(actualValidationResult.isOk() || previousValidationResultDto.isOk());
            actualValidationResult.getValidationMessages().addAll(previousValidationResultDto.getValidationMessages());
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
        String fluxNationCode = ruleModuleCache.getSingleConfig("flux_local_nation_code");
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

    @Override
    public FLUXResponseMessage generateFluxResponseMessageForFaReport(ValidationResultDto faReportValidationResult, FLUXFAReportMessage fluxfaReportMessage) {
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

    @Override
    public FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult) {
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

    @Override
    public FLUXResponseMessage generateFluxResponseMessageForFaQuery(ValidationResultDto faReportValidationResult, FLUXFAQueryMessage fluxfaQueryMessage, String onValue) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {
            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            responseMessage.setFLUXResponseDocument(fluxResponseDocument);
            if (fluxfaQueryMessage != null && fluxfaQueryMessage.getFAQuery() != null) {
                final IDType guid = fluxfaQueryMessage.getFAQuery().getID();
                if(isCorrectUUID(Collections.singletonList(guid))){
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

    @Override
    public FLUXResponseMessage generateFluxResponseMessageForFaResponse(ValidationResultDto faReportValidationResult, FLUXResponseMessage fluxResponseMessage) {
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

    private void setFluxResponseDocumentRejectionReason(ValidationResultDto faReportValidationResult, FLUXResponseDocument fluxResponseDocument) {
        if (faReportValidationResult.isError()) {
            TextType rejectionReason = new TextType();
            rejectionReason.setValue("VALIDATION");
            fluxResponseDocument.setRejectionReason(rejectionReason); // Set rejection reason
        }
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
        String fluxNationCode = ruleModuleCache.getSingleConfig("flux_local_nation_code");
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
            return JAXBMarshaller.unmarshallTextMessage(message, SetFLUXFAReportMessageRequest.class);
        } catch (ActivityModelMarshallException | MessageException | RulesModelMarshallException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private void sendToSales(String message) throws MessageException {
        producer.sendDataSourceMessage(message, DataSourceQueue.SALES);
    }

    private void sendToExchange(String message) throws MessageException {
        producer.sendDataSourceMessage(message, DataSourceQueue.EXCHANGE);
    }

    @Override
    public void validateAndSendResponseToExchange(FLUXResponseMessage fluxResponseMessageObj, RulesBaseRequest request, PluginType pluginType, boolean correctGuidProvided) {
        try {
            log.info("[START] Preparing FLUXResponseMessage to send back to Exchange module.");
            if(!correctGuidProvided){
                fillFluxTLOnValue(fluxResponseMessageObj, request.getOnValue());
            }
            String fluxResponse = JAXBMarshaller.marshallJaxBObjectToString(fluxResponseMessageObj);
            String logGuid = request.getLogGuid();
            Map<ExtraValueType, Object> extraValueTypeObjectMap = extraValueGenerator.generateExtraValueMap(SENDING_FA_RESPONSE_MSG, fluxResponseMessageObj);
            log.info(TRIGGERING_DROOLS_VALIDATION_ON_MESSAGE);
            List<AbstractFact> fluxResponseFacts = rulesEngine.evaluate(SENDING_FA_RESPONSE_MSG, fluxResponseMessageObj, extraValueTypeObjectMap);
            ValidationResultDto fluxResponseValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(fluxResponseFacts, fluxResponse, logGuid, RawMsgType.FA_RESPONSE);
            ExchangeLogStatusTypeType status = calculateMessageValidationStatus(fluxResponseValidationResult);
            //Create Response
            String fluxNationCode = ruleModuleCache.getSingleConfig("flux_local_nation_code");
            String nationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "XEU";
            String df = request.getFluxDataFlow(); //e.g. "urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2" // TODO should come from subscription. Also could be a link between DF and AD value
            String destination = request.getSenderOrReceiver();  // e.g. "AHR:VMS"
            String onValue = request.getOnValue();
            // We need to link the message that came in with the FLUXResponseMessage we're sending... That's the why of the commented line here..
            //String messageGuid = ActivityFactMapper.getUUID(fluxResponseMessageType.getFLUXResponseDocument().getIDS());
            String fluxFAReponseText = ExchangeModuleRequestMapper.createFluxFAResponseRequestWithOnValue(fluxResponse, request.getUsername(), df, logGuid,
                    nationCode, onValue, status, destination, getExchangePluginType(pluginType));
            log.debug("Message to exchange {}", fluxFAReponseText);
            producer.sendDataSourceMessage(fluxFAReponseText, DataSourceQueue.EXCHANGE);
            XPathRepository.INSTANCE.clear(fluxResponseFacts);
            log.info("[END] FLUXFAResponse successfully sent back to Exchange.");
        } catch (RulesModelMarshallException e) {
            log.error(e.getMessage(), e);
            sendFLUXResponseMessageOnException(e.getMessage(), null, request, fluxResponseMessageObj);
        } catch (ExchangeModelMarshallException | MessageException | RulesValidationException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    @Override
    public String getValidationsForRawMessageGuid(String guid, String type) {
        try {
            ValidationMessageTypeResponse validationsResponse = rulePostProcessBean.getValidationResultsFromRawMsgGuid(guid, type);
            return JAXBMarshaller.marshallJaxBObjectToString(validationsResponse);
        } catch (RulesModelException | RulesModelMarshallException e) {
            log.error("Error while getting List<ValidationMessageType> with rawMessage GUID!", e);
        }
        return StringUtils.EMPTY;
    }


    /*
     * Maps a Request String to a eu.europa.ec.fisheries.schema.exchange.module.v1.SetFLUXMDRSyncMessageRequest
     * to send a message to ExchangeModule
     *
     * @see eu.europa.ec.fisheries.uvms.rules.service.RulesService#mapAndSendFLUXMdrRequestToExchange(java.lang.String)
     */
    @Override
    public void mapAndSendFLUXMdrRequestToExchange(String request) {
        String exchangerStrReq;
        try {
            exchangerStrReq = ExchangeModuleRequestMapper.createFluxMdrSyncEntityRequest(request, StringUtils.EMPTY);
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

    @Override
    public void mapAndSendFLUXMdrResponseToMdrModule(String request) {
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
        switch (pluginType) {
            case BELGIAN_ACTIVITY:
                return eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType.BELGIAN_ACTIVITY;
            default:
                return eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType.FLUX;
        }
    }
}