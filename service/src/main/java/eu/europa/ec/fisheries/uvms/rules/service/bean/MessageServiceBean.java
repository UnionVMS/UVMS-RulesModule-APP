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

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_ACTIVITY_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_SALES_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_SALES_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_SALES_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ORIGINATING_PLUGIN;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;

import static java.util.Collections.singletonList;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesQueryRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesQueryMessage;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesResponseMessage;
import eu.europa.ec.fisheries.schema.sales.Report;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.MessageService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesMessageFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.interceptor.RulesPreValidationInterceptor;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
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
@Stateless
@Slf4j
public class MessageServiceBean implements MessageService {

    @EJB
    private RulesMessageProducer producer;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesPreProcessBean rulesPreProcessBean;

    @EJB
    private RulesConfigurationCache ruleModuleCache;

    @EJB
    private ParameterService parameterService;

    @EJB
    private SalesMessageFactory salesMessageFactory;

    @Override
    @Interceptors(RulesPreValidationInterceptor.class)
    public void receiveSalesQueryRequest(ReceiveSalesQueryRequest receiveSalesQueryRequest) {
        try {
            //get sales query message
            String salesQueryMessageAsString = receiveSalesQueryRequest.getRequest();
            FLUXSalesQueryMessage salesQueryMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesQueryMessageAsString, FLUXSalesQueryMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new HashMap<>();
            extraValues.put(SENDER_RECEIVER, receiveSalesQueryRequest.getSender());
            extraValues.put(ORIGINATING_PLUGIN, receiveSalesQueryRequest.getPluginType());

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_QUERY_MSG, salesQueryMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesQueryMessageAsString);

            //send to sales
            if (validationResult.isError()) {
                String requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesQueryRequest.getMessageGuid(), validationResult, receiveSalesQueryRequest.getPluginType(), receiveSalesQueryRequest.getSender());
                sendToSales(requestForSales);
            } else {
                String requestForSales = salesMessageFactory.createSalesQueryRequest(receiveSalesQueryRequest.getRequest(), validationResult, receiveSalesQueryRequest.getPluginType());
                sendToSales(requestForSales);
            }


            updateRequestMessageStatus(receiveSalesQueryRequest.getLogGuid(), validationResult);
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
            Report salesReportMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesReportMessageAsString, Report.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new HashMap<>();
            extraValues.put(SENDER_RECEIVER, receiveSalesReportRequest.getSender());
            extraValues.put(ORIGINATING_PLUGIN, receiveSalesReportRequest.getPluginType());

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_REPORT_MSG, salesReportMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesReportMessageAsString);

            //send to sales
            if (validationResult.isError()) {
                String requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesReportRequest.getMessageGuid(), validationResult, receiveSalesReportRequest.getPluginType(), receiveSalesReportRequest.getSender());
                sendToSales(requestForSales);
            } else {
                String requestForSales = salesMessageFactory.createSalesReportRequest(receiveSalesReportRequest.getRequest(), validationResult, receiveSalesReportRequest.getPluginType());
                sendToSales(requestForSales);
            }


            //update log status
            updateRequestMessageStatus(receiveSalesReportRequest.getLogGuid(), validationResult);
        } catch (SalesMarshallException | RulesValidationException | MessageException e) {
            throw new RulesServiceException("Couldn't validate sales report", e);
        }
    }

    @Override
    @Interceptors(RulesPreValidationInterceptor.class)
    public void receiveSalesResponseRequest(ReceiveSalesResponseRequest rulesRequest) {
        try {
            //get sales response message
            String salesResponseMessageAsString = rulesRequest.getRequest();
            FLUXSalesResponseMessage salesResponseMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesResponseMessageAsString, FLUXSalesResponseMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new HashMap<>();
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue("flux_local_nation_code"));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_RESPONSE_MSG, salesResponseMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesResponseMessageAsString);

            updateRequestMessageStatus(rulesRequest.getLogGuid(), validationResult);
        } catch (SalesMarshallException | RulesValidationException e) {
            throw new RulesServiceException("Couldn't validate sales response", e);
        } catch (ConfigServiceException e) {
            throw new RulesServiceException("Couldn't retrieve the FLUX local nation code from the settings", e);
        }
    }

    @Override
    @Interceptors(RulesPreValidationInterceptor.class)
    public void sendSalesResponseRequest(SendSalesResponseRequest rulesRequest) {
        try {
            //get sales response message
            String salesResponseMessageAsString = rulesRequest.getRequest();
            FLUXSalesResponseMessage salesResponseMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesResponseMessageAsString, FLUXSalesResponseMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new HashMap<>();
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue("flux_local_nation_code"));
            extraValues.put(ORIGINATING_PLUGIN, rulesRequest.getPluginToSendResponseThrough());

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_RESPONSE_MSG, salesResponseMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesResponseMessageAsString);
            ExchangeLogStatusTypeType validationStatus = calculateMessageValidationStatus(validationResult);

            //send to exchange
            String requestForExchange = ExchangeModuleRequestMapper.createSendSalesResponseRequest(rulesRequest.getRequest(),
                    rulesRequest.getMessageGuid(),
                    rulesRequest.getFluxDataFlow(),
                    rulesRequest.getPluginToSendResponseThrough(),
                    rulesRequest.getDateSent(),
                    validationStatus);
            sendToExchange(requestForExchange);
        } catch (ExchangeModelMarshallException | MessageException | SalesMarshallException | RulesValidationException | ConfigServiceException e) {
            throw new RulesServiceException("Couldn't validate sales response", e);
        }
    }

    @Override
    @Interceptors(RulesPreValidationInterceptor.class)
    public void sendSalesReportRequest(SendSalesReportRequest rulesRequest) {
        try {
            //get sales report message
            String salesReportMessageAsString = rulesRequest.getRequest();
            FLUXSalesReportMessage salesReportMessage = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(salesReportMessageAsString, FLUXSalesReportMessage.class);

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_REPORT_MSG, salesReportMessage);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesReportMessageAsString);
            ExchangeLogStatusTypeType validationStatus = calculateMessageValidationStatus(validationResult);

            //send to exchange
            String requestForExchange = ExchangeModuleRequestMapper.createSendSalesReportRequest(rulesRequest.getRequest(),
                    rulesRequest.getMessageGuid(),
                    rulesRequest.getFluxDataFlow(),
                    rulesRequest.getRecipient(),
                    rulesRequest.getDateSent(),
                    validationStatus);
            sendToExchange(requestForExchange);
        } catch (ExchangeModelMarshallException | MessageException | SalesMarshallException | RulesValidationException e) {
            throw new RulesServiceException("Couldn't validate sales report", e);
        }
    }


    @Override
    @Interceptors(RulesPreValidationInterceptor.class)
    public void setFLUXFAReportMessageReceived(SetFLUXFAReportMessageRequest request) {
        log.info("inside setFLUXFAReportMessageReceived", request.getRequest());
        final String logGuid = request.getLogGuid();
        try {
            FLUXFAReportMessage fluxfaReportMessage = extractFluxFaReportMessage(request);
            if (fluxfaReportMessage != null) {
                FLUXResponseMessage fluxResponseMessageType;
                Map<Boolean, ValidationResultDto> validationMap = rulesPreProcessBean.checkDuplicateIdInRequest(fluxfaReportMessage);
                boolean isContinueValidation = isContinueValidation(validationMap);
                log.info("Validation continue : {}", isContinueValidation);

                if (isContinueValidation) {
                    log.info("Trigger rule engine to do validation of incoming message");
                    Map<ExtraValueType, Object> extraValueTypeObjectMap = rulesEngine.generateExtraValueMap(FLUX_ACTIVITY_REQUEST_MSG, fluxfaReportMessage);
                    extraValueTypeObjectMap.put(SENDER_RECEIVER, request.getSenderOrReceiver());

                    List<AbstractFact> faReportFacts = rulesEngine.evaluate(FLUX_ACTIVITY_REQUEST_MSG, fluxfaReportMessage, extraValueTypeObjectMap);
                    ValidationResultDto faReportValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(faReportFacts, request.getRequest());

                    updateValidationResultWithExisting(faReportValidationResult, validationMap.get(isContinueValidation));
                    updateRequestMessageStatus(logGuid, faReportValidationResult);

                    if (faReportValidationResult != null && !faReportValidationResult.isError()) {
                        log.info("Validation of Report is successful, forwarding message to Activity");
                        log.debug("message to activity : {}", request.getRequest());
                        sendRequestToActivity(request.getRequest(), request.getUsername(), request.getType());
                    }
                    fluxResponseMessageType = generateFluxResponseMessage(faReportValidationResult, fluxfaReportMessage);
                    XPathRepository.INSTANCE.clear(faReportFacts);
                } else {
                    updateRequestMessageStatus(logGuid, validationMap.get(isContinueValidation));
                    fluxResponseMessageType = generateFluxResponseMessage(validationMap.get(isContinueValidation), fluxfaReportMessage);
                    log.info("Validation of FLUXFAReport is complete and FluxResponse is generated");
                }
                sendResponseToExchange(fluxResponseMessageType, request, request.getType());
            }
        } catch (RulesValidationException e) {
            log.error(e.getMessage(), e);
            updateRequestMessageStatus(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), null, request, request.getRequest());
        } catch (SAXException | RulesModelMarshallException e) {
            log.error(e.getMessage(), e);
            updateRequestMessageStatus(logGuid, generateValidationResultDtoForFailure());
            sendFLUXResponseMessageOnException(e.getMessage(), null, request, request.getRequest());
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private ValidationResultDto generateValidationResultDtoForFailure() {
        ValidationResultDto resultDto = new ValidationResultDto();
        resultDto.setIsOk(false);
        resultDto.setIsError(true);
        return resultDto;
    }

    private FLUXFAReportMessage extractFluxFaReportMessage(SetFLUXFAReportMessageRequest request) throws SAXException, RulesModelMarshallException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = getClass().getClassLoader().getResource("xsd/contract/fa/data/standard/FLUXFAReportMessage_3p1.xsd");
        Schema schema = sf.newSchema(resource);
        return JAXBMarshaller.unMarshallMessage(request.getRequest(), FLUXFAReportMessage.class, schema);
    }

    private Boolean isContinueValidation(Map<Boolean, ValidationResultDto> validationMap) {
        if (MapUtils.isNotEmpty(validationMap)) {
            return validationMap.entrySet().iterator().next().getKey();
        }
        return false;
    }


    public void sendFLUXResponseMessageOnException(String errorMessage, String rawMessage, RulesBaseRequest request, Object message) {
        if (request == null) {
            log.error("Could not send FLUXResponseMessage. Request is null.");
            return;
        }
        List<String> xpaths = new ArrayList<>();
        xpaths.add(errorMessage);
        RuleError ruleError = new RuleError(ServiceConstants.INVALID_XML_RULE, ServiceConstants.INVALID_XML_RULE_MESSAGE, "L00", xpaths);
        ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResultForGeneralBuinessRules(ruleError, ErrorType.ERROR, rawMessage);

        FLUXResponseMessage fluxResponseMessage;
        if (message instanceof FLUXFAReportMessage) {
            fluxResponseMessage = generateFluxResponseMessage(validationResultDto, (FLUXFAReportMessage) message);
        } else if (message instanceof FLUXFAQueryMessage) {
            fluxResponseMessage = generateFluxResponseMessage(validationResultDto, (FLUXFAQueryMessage) message);
        } else if (message instanceof FLUXResponseMessage) {
            fluxResponseMessage = generateFluxResponseMessage(validationResultDto, (FLUXResponseMessage) message);
        } else {
            fluxResponseMessage = generateFluxResponseMessage(validationResultDto);
        }
        fluxResponseMessage.getFLUXResponseDocument().setReferencedID(generateReferenceId(request.getOnValue()));
        log.info("FLUXResponseMessage has been generated after exception: " + fluxResponseMessage);
        sendResponseToExchange(fluxResponseMessage, request, PluginType.FLUX);
    }

    private void updateRequestMessageStatus(String logGuid, ValidationResultDto validationResult) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, calculateMessageValidationStatus(validationResult));
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

    private void updateValidationResultWithExisting(ValidationResultDto faReportValidationResult, ValidationResultDto previousValidationResultDto) {
        if (previousValidationResultDto != null) {
            faReportValidationResult.setIsError(faReportValidationResult.isError() || previousValidationResultDto.isError());
            faReportValidationResult.setIsWarning(faReportValidationResult.isWarning() || previousValidationResultDto.isWarning());
            faReportValidationResult.setIsOk(faReportValidationResult.isOk() || previousValidationResultDto.isOk());
            faReportValidationResult.getValidationMessages().addAll(previousValidationResultDto.getValidationMessages());
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
        idType.setValue("XEU"); // TODO to be received from Global config nation_code
        idType.setSchemeID("FLUX_GP_PARTY");
        validationResultDocument.setValidatorID(idType);

        List<ValidationQualityAnalysis> validationQuality = new ArrayList<>();
        for (ValidationMessageType validationMessage : faReportValidationResult.getValidationMessages()) {
            ValidationQualityAnalysis analysis = new ValidationQualityAnalysis();

            IDType identification = new IDType();
            identification.setValue(validationMessage.getBrId());
            analysis.setID(identification);

            CodeType level = new CodeType();
            level.setValue(validationMessage.getLevel());
            analysis.setLevelCode(level);

            CodeType type = new CodeType();
            type.setValue(validationMessage.getErrorType().value());
            analysis.setTypeCode(type);

            TextType text = new TextType();
            text.setValue(validationMessage.getMessage());
            analysis.getResults().add(text);

            List<String> xpaths = validationMessage.getXpaths();
            if (CollectionUtils.isNotEmpty(xpaths)) {
                for (String xpath : xpaths) {
                    TextType referenceItem = new TextType();
                    referenceItem.setValue(xpath);
                    analysis.getReferencedItems().add(referenceItem);
                }
            }

            validationQuality.add(analysis);
        }
        validationResultDocument.setRelatedValidationQualityAnalysises(validationQuality);
        return singletonList(validationResultDocument);
    }

    @Override
    public FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult, FLUXFAReportMessage fluxfaReportMessage) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {

            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            if (fluxfaReportMessage.getFLUXReportDocument() != null) {
                List<IDType> requestId = fluxfaReportMessage.getFLUXReportDocument().getIDS();
                fluxResponseDocument.setReferencedID((requestId != null && !requestId.isEmpty()) ? requestId.get(0) : null); // Set Request Id
            }
            setFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
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
            setFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
            responseMessage.setFLUXResponseDocument(fluxResponseDocument);
            return responseMessage;
        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }


    private IDType generateReferenceId(String onParam) {
        IDType idType = new IDType();
        idType.setSchemeID("FLUXTL_ON");
        idType.setValue(onParam);
        return idType;
    }


    @Override
    public FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult, FLUXFAQueryMessage fluxfaQueryMessage) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {

            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            FAQuery faQuery = fluxfaQueryMessage.getFAQuery();
            if (faQuery != null) {
                fluxResponseDocument.setReferencedID(faQuery.getID()); // Set Request Id
            }
            setFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
            responseMessage.setFLUXResponseDocument(fluxResponseDocument);

        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }

    @Override
    public FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult, FLUXResponseMessage fluxResponseMessage) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {

            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            FLUXResponseDocument responseDocument = fluxResponseMessage.getFLUXResponseDocument();
            if (responseDocument != null) {
                fluxResponseDocument.setReferencedID((CollectionUtils.isNotEmpty(responseDocument.getIDS())) ? responseDocument.getIDS().get(0) : null); // Set Request Id
            }
            setFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
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

    private void setFluxResponseDocument(ValidationResultDto faReportValidationResult, FLUXResponseDocument fluxResponseDocument) throws DatatypeConfigurationException {
        setFluxResponseDocumentIDs(fluxResponseDocument);
        setFluxResponseCreationDate(fluxResponseDocument);
        setFluxResponseDocumentResponseCode(faReportValidationResult, fluxResponseDocument);
        setFluxResponseDocumentRejectionReason(faReportValidationResult, fluxResponseDocument);
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
        if (faReportValidationResult.isError() || faReportValidationResult.isWarning()) {
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

    public void sendRequestToActivity(String fluxFAReportMessage, String username, PluginType pluginType) {
        try {
            String setFLUXFAReportMessageRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportMessageRequest(fluxFAReportMessage, username, pluginType.toString());
            producer.sendDataSourceMessage(setFLUXFAReportMessageRequest, DataSourceQueue.ACTIVITY);
        } catch (ActivityModelMarshallException | MessageException e) {
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
    public void sendResponseToExchange(FLUXResponseMessage fluxResponseMessageType, RulesBaseRequest request, PluginType pluginType) {
        try {
            log.info("[START] Sending Response (FLUXResponseMessage) Back to Exchange module.");
            String fluxResponse = JAXBMarshaller.marshallJaxBObjectToString(fluxResponseMessageType);

            Map<ExtraValueType, Object> extraValueTypeObjectMap = rulesEngine.generateExtraValueMap(FLUX_ACTIVITY_RESPONSE_MSG, fluxResponseMessageType);
            List<AbstractFact> fluxResponseFacts = rulesEngine.evaluate(FLUX_ACTIVITY_RESPONSE_MSG, fluxResponseMessageType, extraValueTypeObjectMap);
            ValidationResultDto fluxResponseValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(fluxResponseFacts, fluxResponse);

            ExchangeLogStatusTypeType status = calculateMessageValidationStatus(fluxResponseValidationResult);

            //Create Response
            String fluxNationCode = ruleModuleCache.getSingleConfig("flux_local_nation_code");
            String nationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "XEU";
            String df = request.getFluxDataFlow(); //e.g. "urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2" // TODO should come from subscription. Also could be a link between DF and AD value
            String destination = request.getSenderOrReceiver();  // e.g. "AHR:VMS"

            // We need to link the message that came in with the FLUXResponseMessage we're sending... That's the why of the commented line here..
            //String messageGuid = ActivityFactMapper.getUUID(fluxResponseMessageType.getFLUXResponseDocument().getIDS());
            String messageGuid = request.getLogGuid();
            String fluxFAReponseText = ExchangeModuleRequestMapper.createFluxFAResponseRequest(fluxResponse, request.getUsername(), df, messageGuid, nationCode, status, destination, getExchangePluginType(pluginType));
            log.debug("Message to exchange {}", fluxFAReponseText);

            producer.sendDataSourceMessage(fluxFAReponseText, DataSourceQueue.EXCHANGE);
            XPathRepository.INSTANCE.clear(fluxResponseFacts);
            log.info("[END] FLUXFAResponse has been successfully sent back to Exchange.");
        } catch (RulesModelMarshallException e) {
            log.error(e.getMessage(), e);
            sendFLUXResponseMessageOnException(e.getMessage(), null, request, fluxResponseMessageType);
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