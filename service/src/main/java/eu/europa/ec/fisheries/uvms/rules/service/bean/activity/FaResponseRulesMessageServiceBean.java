/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.activity;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.SENDING_FA_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.RESPONSE_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.XML;
import static java.util.Collections.singletonList;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.FANamespaceMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CodeTypeMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FAReportQueryResponseIdsMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityRulesHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.MDC;
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

@Stateless
@LocalBean
@Slf4j
public class FaResponseRulesMessageServiceBean extends BaseFaRulesMessageServiceBean {

    private static final String FLUX_LOCAL_NATION_CODE = "flux_local_nation_code";

    @EJB
    private RulesMessageProducer rulesProducer;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private ActivityOutQueueConsumer activityConsumer;

    @EJB
    private FaResponseRulesMessageServiceBean faResponseValidatorAndSender;

    @EJB
    private RulesDao rulesDaoBean;

    @EJB
    private RulesConfigurationCache ruleModuleCache;

    @EJB
    private RulesMessageProducer producer;

    private FishingActivityRulesHelper faMessageHelper;

    private XSDJaxbUtil xsdJaxbUtil;

    private FAReportQueryResponseIdsMapper faIdsMapper;

    @Inject
    private CodeTypeMapper codeTypeMapper;

    @PostConstruct
    public void init() {
        faIdsMapper = FAReportQueryResponseIdsMapper.INSTANCE;
        faMessageHelper = new FishingActivityRulesHelper();
        xsdJaxbUtil = new XSDJaxbUtil();
    }

    @Asynchronous
    public void evaluateIncomingFluxResponseRequest(SetFluxFaResponseMessageRequest request) {
        String requestStr = request.getRequest();
        final String logGuid = request.getLogGuid();
        log.info("Evaluate FLUXResponseMessage with GUID " + logGuid);
        FLUXResponseMessage fluxResponseMessage;
        try {
            // Validate xsd schema
            fluxResponseMessage = xsdJaxbUtil.unMarshallFluxResponseMessage(requestStr);
            Collection<AbstractFact> fluxFaResponseFacts = rulesEngine.evaluate(RECEIVING_FA_RESPONSE_MSG, fluxResponseMessage, new EnumMap<>(ExtraValueType.class), String.valueOf(fluxResponseMessage.getFLUXResponseDocument().getIDS()));
            ValidationResultDto fluxResponseValidResults = rulePostProcessBean.checkAndUpdateValidationResult(fluxFaResponseFacts, requestStr, logGuid, RawMsgType.FA_RESPONSE);
            updateRequestMessageStatusInExchange(logGuid, fluxResponseValidResults);
            if (fluxResponseValidResults != null && !fluxResponseValidResults.isError()) {
                log.debug("The Validation of FLUXResponseMessage is successful, forwarding message to Exchange");
            } else {
                log.debug("Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(fluxFaResponseFacts);

        } catch (UnmarshalException e) {
            log.debug("Error while trying to parse FLUXResponseMessage received message! It is malformed!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
            throw new RulesServiceException(e.getMessage(), e);
        } catch (RulesValidationException e) {
            log.debug("Error during validation of the received FLUXResponseMessage!", e);
            updateRequestMessageStatusInExchange(logGuid, generateValidationResultDtoForFailure());
        }
    }

    public void validateAndSendResponseToExchange(FLUXResponseMessage fluxResponseMessageObj, RulesBaseRequest request, PluginType pluginType, boolean correctGuidProvided, Map<String, String> copyOfContextMap) {
        try {
            MDC.setContextMap(copyOfContextMap);
            log.info("Preparing FLUXResponseMessage to send back to Exchange module.");
            if (!correctGuidProvided) {
                fillFluxTLOnValue(fluxResponseMessageObj, request.getOnValue());
            }

            // Get fluxNationCode (Eg. XEU) from Config Module.
            String fluxNationCode = ruleModuleCache.getSingleConfig("flux_local_nation_code");
            String nationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "flux_local_nation_code_is_missing_in_config_settings_table_please_set_it";

            // Get the actual Response ids and match them with the Response Ids from the DB
            Set<FADocumentID> idsFromIncommingMessage = faMessageHelper.mapToResponseToFADocumentID(fluxResponseMessageObj);
            List<FADocumentID> matchingIdsFromDB = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);

            String fluxResponse = JAXBUtils.marshallJaxBObjectToString(fluxResponseMessageObj, "UTF-8", false, new FANamespaceMapper());
            String logGuid = request.getLogGuid();
            Map<ExtraValueType, Object> extraValues = populateExtraValuesMap(fluxNationCode, matchingIdsFromDB);
            extraValues.put(XML, fluxResponse);

            Collection<AbstractFact> fluxResponseFacts = rulesEngine.evaluate(SENDING_FA_RESPONSE_MSG, fluxResponseMessageObj, extraValues);
            ValidationResultDto fluxResponseValidationResult = rulePostProcessBean.checkAndUpdateValidationResult(fluxResponseFacts, fluxResponse, logGuid, RawMsgType.FA_RESPONSE);
            ExchangeLogStatusTypeType status = calculateMessageValidationStatus(fluxResponseValidationResult);
            //Create Response

            String df = request.getFluxDataFlow(); //e.g. "urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2" // TODO should come from subscription. Also could be a link between DF and AD value
            String destination = request.getSenderOrReceiver();  // e.g. "AHR:VMS"
            String onValue = request.getOnValue();
            // We need to link the message that came in with the FLUXResponseMessage we're sending... That's the why of the commented line here..
            //String messageGuid = ActivityFactMapper.getUUID(fluxResponseMessageType.getFLUXResponseDocument().getIDS());
            String fluxFAReponseText = ExchangeModuleRequestMapper.createFluxFAResponseRequestWithOnValue(fluxResponse, request.getUsername(), df, logGuid,
                    nationCode, onValue, status, destination, getExchangePluginType(pluginType),
                    fluxResponseMessageObj.getFLUXResponseDocument().getIDS().get(0).getValue());
            producer.sendDataSourceMessage(fluxFAReponseText, DataSourceQueue.EXCHANGE);
            XPathRepository.INSTANCE.clear(fluxResponseFacts);

            idsFromIncommingMessage.removeAll(matchingIdsFromDB); // To avoid dublication in DB.
            rulesDaoBean.createFaDocumentIdEntity(idsFromIncommingMessage);
            log.info("FLUXFAResponse successfully sent back to Exchange.");
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
            sendFLUXResponseMessageOnException(e.getMessage(), null, request, fluxResponseMessageObj);
        } catch (ExchangeModelMarshallException | MessageException | RulesValidationException | ServiceException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private Map<ExtraValueType, Object> populateExtraValuesMap(String fluxNationCode, List<FADocumentID> matchingIdsFromDB) {
        Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
        extraValues.put(SENDER_RECEIVER, fluxNationCode);
        extraValues.put(RESPONSE_IDS, faIdsMapper.mapToFishingActivityIdDto(matchingIdsFromDB));
        return extraValues;
    }

    public void sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(String rawMessage, RulesBaseRequest request, FLUXFAQueryMessage queryMessage,
                                                                       Rule9998Or9999ErrorType type, String onValue, ValidationResultDto faQueryValidationReport) {
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

        ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResultForGeneralBusinessRules(ruleWarning, rawMessage,
                request.getLogGuid(), RawMsgType.FA_QUERY);
        validationResultDto.setError(true);
        validationResultDto.setOk(false);

        if(CollectionUtils.isNotEmpty(faQueryValidationReport.getValidationMessages())){
            validationResultDto.getValidationMessages().addAll(faQueryValidationReport.getValidationMessages());
        }

        FLUXResponseMessage fluxResponseMessage = generateFluxResponseMessageForFaQuery(validationResultDto, queryMessage, onValue);
        log.debug("FLUXResponseMessage has been generated after exception: " + fluxResponseMessage);
        validateAndSendResponseToExchange(fluxResponseMessage, request, PluginType.FLUX, true, MDC.getCopyOfContextMap());
    }

    private void fillFluxTLOnValue(FLUXResponseMessage fluxResponseMessage, String onValue) {
        IDType idType = new IDType();
        idType.setSchemeID("FLUXTL_ON");
        idType.setValue(onValue);
        fluxResponseMessage.getFLUXResponseDocument().setReferencedID(idType);
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

    private eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType getExchangePluginType(PluginType pluginType) {
        if (pluginType == PluginType.BELGIAN_ACTIVITY) {
            return eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType.BELGIAN_ACTIVITY;
        } else {
            return eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType.FLUX;
        }
    }

    public void sendFLUXResponseMessageOnException(String errorMessage, String rawMessage, RulesBaseRequest request, Object message) {
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
        if (messageType != null){
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
            validateAndSendResponseToExchange(fluxResponseMessage, request, PluginType.FLUX, false, MDC.getCopyOfContextMap());
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
        fluxResponseDocument.setRespondentFLUXParty(getRespondedFluxParty()); // Set movement party in the response
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

    private FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult) {
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

    @Override
    RulesMessageProducer getRulesProducer() {
        return rulesProducer;
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
