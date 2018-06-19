package eu.europa.ec.fisheries.uvms.rules.service.bean.messageprocessors;


import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.FANamespaceMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CodeTypeMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.SENDING_FA_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static java.util.Collections.singletonList;

@Stateless
@LocalBean
@Slf4j
public class FaResponseValidatorAndSender {

    @EJB
    private RulesConfigurationCache ruleModuleCache;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesMessageProducer producer;

    @Inject
    private CodeTypeMapper codeTypeMapper;

    private static final String FLUX_LOCAL_NATION_CODE = "flux_local_nation_code";

    @Asynchronous
    public void validateAndSendResponseToExchange(FLUXResponseMessage fluxResponseMessageObj, RulesBaseRequest request, PluginType pluginType, boolean correctGuidProvided) {
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
            String fluxFAReponseText = ExchangeModuleRequestMapper.createFluxFAResponseRequestWithOnValue(fluxResponse, request.getUsername(), df, logGuid,
                    nationCode, onValue, status, destination, getExchangePluginType(pluginType));
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

    public void sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(String rawMessage, RulesBaseRequest request, FLUXFAQueryMessage queryMessage, Rule9998Or9999ErrorType type, String onValue) {
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

    private boolean isCorrectUUID(List<IDType> ids){
        boolean uuidIsCorrect = false;
        String uuidString = null;
        try {
            if(CollectionUtils.isNotEmpty(ids)){
                IDType idType = ids.get(0);
                uuidString = idType.getValue();
                String schemeID = idType.getSchemeID();
                if ("UUID".equals(schemeID)){
                    uuidIsCorrect = UUID.fromString(uuidString).toString().equals(uuidString);
                }
                if(!uuidIsCorrect){
                    log.debug("[WARN] The given UUID is not in a correct format {}", uuidString);
                }
            }
        } catch (IllegalArgumentException exception){
            log.debug("[WARN] The given UUID is not in a correct format {}", uuidString);
        }
        return uuidIsCorrect;
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


}
