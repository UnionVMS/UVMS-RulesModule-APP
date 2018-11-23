/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import javax.xml.XMLConstants;
import javax.xml.bind.UnmarshalException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;
import java.util.*;
import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.entity.FAUUIDType;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.xml.sax.SAXException;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;
import static eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType.FA_QUERY;
import static eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType.FA_REPORT;
import static eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType.FA_RESPONSE;
import static eu.europa.ec.fisheries.uvms.rules.entity.FAUUIDType.FA_QUERY_ID;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.RESPONSE_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static java.util.Collections.singletonList;

@Slf4j
public class RulesFLUXMessageHelper {

    public static final String FLUX_LOCAL_NATION_CODE = "flux_local_nation_code";
    private static final String FLUXFAREPORT_MESSAGE_3P1_XSD = "xsd/contract/fa/data/standard/FLUXFAReportMessage_3p1.xsd";
    private static final String FLUXFAQUERY_MESSAGE_3P0_XSD = "xsd/contract/fa/data/standard/FLUXFAQueryMessage_3p0.xsd";
    private static final String FLUXFARESPONSE_MESSAGE_6P0_XSD = "xsd/contract/fa/data/standard/FLUXResponseMessage_6p0.xsd";
    private static final String DASH = "-";

    private FAReportQueryResponseIdsMapper faIdsMapper = new FAReportQueryResponseIdsMapperImpl();

    private RulesConfigurationCache ruleModuleCache;

    private CodeTypeMapper codeTypeMapper = new CodeTypeMapperImpl();

    public RulesFLUXMessageHelper(RulesConfigurationCache configurationCache){
        ruleModuleCache = configurationCache;
    }

    public RawMsgType getMessageType(RulesModuleMethod method) {
        if (null == method) {
            return null;
        }
        RawMsgType msgType = null;
        if (RulesModuleMethod.SET_FLUX_FA_REPORT.equals(method) || RulesModuleMethod.SEND_FLUX_FA_REPORT.equals(method)) {
            msgType = FA_REPORT;
        } else if (RulesModuleMethod.SET_FLUX_FA_QUERY.equals(method) || RulesModuleMethod.SEND_FLUX_FA_QUERY.equals(method)) {
            msgType = FA_QUERY;
        } else if (RulesModuleMethod.SET_FLUX_RESPONSE.equals(method) || RulesModuleMethod.RCV_FLUX_RESPONSE.equals(method)) {
            msgType = FA_RESPONSE;
        }
        return msgType;
    }

    public Map<ExtraValueType, Object> populateExtraValuesMap(String fluxNationCode, List<FADocumentID> matchingIdsFromDB) {
        Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
        extraValues.put(SENDER_RECEIVER, fluxNationCode);
        extraValues.put(RESPONSE_IDS, faIdsMapper.mapToFishingActivityIdDto(matchingIdsFromDB));
        return extraValues;
    }

    public Set<FADocumentID> mapToFADocumentID(FLUXFAReportMessage fluxfaReportMessage) {
        Set<FADocumentID> ids = new HashSet<>();
        if (fluxfaReportMessage != null){
            FLUXReportDocument fluxReportDocument = fluxfaReportMessage.getFLUXReportDocument();
            if (fluxReportDocument != null){
                mapFluxReportDocumentIDS(ids, fluxReportDocument, FAUUIDType.FA_MESSAGE_ID);
            }
            List<FAReportDocument> faReportDocuments = fluxfaReportMessage.getFAReportDocuments();
            if (CollectionUtils.isNotEmpty(faReportDocuments)){
                mapFaReportDocuments(ids, faReportDocuments);
            }
        }
        return ids;
    }

    public void fillFluxTLOnValue(FLUXResponseMessage fluxResponseMessage, String onValue) {
        IDType idType = new IDType();
        idType.setSchemeID("FLUXTL_ON");
        idType.setValue(onValue);
        fluxResponseMessage.getFLUXResponseDocument().setReferencedID(idType);
    }

    public Set<FADocumentID> mapQueryToFADocumentID(FLUXFAQueryMessage faQueryMessage) {
        Set<FADocumentID> idsList = new HashSet<>();
        if (faQueryMessage != null){
            FAQuery faQuery = faQueryMessage.getFAQuery();
            if (faQuery != null && faQuery.getID() != null){
                IDType faQueryId = faQuery.getID();
                idsList.add(new FADocumentID(faQueryId.getValue(), FA_QUERY_ID));
            }
        }
        return idsList;
    }

    private void mapFaReportDocuments(Set<FADocumentID> ids, List<FAReportDocument> faReportDocuments) {
        if (CollectionUtils.isNotEmpty(faReportDocuments)){
            for (FAReportDocument faReportDocument : faReportDocuments) {
                if (faReportDocument != null){
                    mapFluxReportDocumentIDS(ids, faReportDocument.getRelatedFLUXReportDocument(), FAUUIDType.FA_REPORT_ID);
                }
            }
        }
    }

    private void mapFluxReportDocumentIDS(Set<FADocumentID> ids, FLUXReportDocument fluxReportDocument, FAUUIDType faFluxMessageId) {
        if (fluxReportDocument != null){
            List<IDType> fluxReportDocumentIDS = fluxReportDocument.getIDS();
            IDType referencedID = fluxReportDocument.getReferencedID();
            if (CollectionUtils.isNotEmpty(fluxReportDocumentIDS)){
                IDType idType = fluxReportDocumentIDS.get(0);
                if (idType != null){
                    ids.add(new FADocumentID(idType.getValue(), faFluxMessageId));
                }
                if(referencedID != null){
                    ids.add(new FADocumentID(referencedID.getValue(), faFluxMessageId));
                }
            }
        }
    }

    public List<String> collectFaIdsAndTripIds(FLUXFAReportMessage fluxFaRepMessage) {
        List<String> idsReqList = new ArrayList<>();
        FLUXReportDocument fluxReportDocument = fluxFaRepMessage.getFLUXReportDocument();
        List<FAReportDocument> faReportDocuments = fluxFaRepMessage.getFAReportDocuments();
        if (fluxReportDocument == null || CollectionUtils.isEmpty(faReportDocuments)) {
            return idsReqList;
        }

        // FishinActivity type, tripId, tripSchemeId
        for (FAReportDocument faRepDoc : faReportDocuments) {
            collectFromActivityList(idsReqList, faRepDoc.getTypeCode(), faRepDoc.getSpecifiedFishingActivities());
        }
        return idsReqList;
    }

    private void collectFromActivityList(List<String> aIdsPerTripEntityList, CodeType faReportTypeCode, List<FishingActivity> specifiedFishingActivities) {
        if (CollectionUtils.isEmpty(specifiedFishingActivities) || faReportTypeCode == null || StringUtils.isEmpty(faReportTypeCode.getValue())) {
            return;
        }
        String faReportTypeCodeStr = faReportTypeCode.getValue();
        for (FishingActivity fishAct : specifiedFishingActivities) {
            CodeType typeCode = fishAct.getTypeCode();
            FishingTrip fishTrip = fishAct.getSpecifiedFishingTrip();
            if (typeCode == null || StringUtils.isEmpty(typeCode.getValue()) || fishTrip == null || CollectionUtils.isEmpty(fishTrip.getIDS())) {
                continue;
            }
            for (IDType tripId : fishTrip.getIDS()) {
                aIdsPerTripEntityList.add(tripId.getValue() +DASH+ tripId.getSchemeID() +DASH+ typeCode.getValue() +DASH+ faReportTypeCodeStr);
            }
        }
    }

    public Set<FADocumentID> mapToResponseToFADocumentID(FLUXResponseMessage fluxResponseMessageObj) {
        Set<FADocumentID> faDocumentID = new HashSet<>();
        if(fluxResponseMessageObj != null && fluxResponseMessageObj.getFLUXResponseDocument() != null && CollectionUtils.isNotEmpty(fluxResponseMessageObj.getFLUXResponseDocument().getIDS())){
            IDType responseId = fluxResponseMessageObj.getFLUXResponseDocument().getIDS().get(0);
            faDocumentID.add(new FADocumentID(responseId.getValue(), FAUUIDType.FA_RESPONSE));

            IDType responseReferenceId = fluxResponseMessageObj.getFLUXResponseDocument().getReferencedID();
            if(responseReferenceId != null){
                faDocumentID.add(new FADocumentID(responseReferenceId.getValue(), FAUUIDType.FA_RESPONSE));
            }
        }
        return faDocumentID;
    }

    public FLUXFAReportMessage unMarshallAndValidateSchema(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXFAReportMessage.class, loadXSDSchema(FLUXFAREPORT_MESSAGE_3P1_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    public FLUXFAQueryMessage unMarshallFaQueryMessage(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXFAQueryMessage.class, loadXSDSchema(FLUXFAQUERY_MESSAGE_3P0_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    public FLUXResponseMessage unMarshallFluxResponseMessage(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXResponseMessage.class, loadXSDSchema(FLUXFARESPONSE_MESSAGE_6P0_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    public Schema loadXSDSchema(String xsdLocation) throws UnmarshalException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = RulesFLUXMessageHelper.class.getClassLoader().getResource(xsdLocation);
        if (resource != null) {
            try {
                return sf.newSchema(resource);
            } catch (SAXException e) {
                throw new UnmarshalException(e.getMessage(), e);
            }
        }
        throw new UnmarshalException("ERROR WHILE TRYING TO LOOKUP XSD SCHEMA");
    }

    public String getIDs(FLUXResponseMessage fluxResponseMessageObj) {
        String id = null;
        if (fluxResponseMessageObj != null){
            FLUXResponseDocument fluxResponseDocument = fluxResponseMessageObj.getFLUXResponseDocument();
            if (fluxResponseDocument != null){
                List<IDType> ids = fluxResponseMessageObj.getFLUXResponseDocument().getIDS();
                if (CollectionUtils.isNotEmpty(ids)){
                    id = ids.get(0).getValue();
                }
            }
        }
        return id;
    }

    public ExchangeLogStatusTypeType calculateMessageValidationStatus(ValidationResult validationResult) {
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

    public FLUXResponseMessage generateFluxResponseMessage(ValidationResult faReportValidationResult) {
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

    public FLUXResponseMessage generateFluxResponseMessageForFaResponse(ValidationResult faReportValidationResult, FLUXResponseMessage fluxResponseMessage) {
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

    public FLUXResponseMessage generateFluxResponseMessageForFaReport(ValidationResult faReportValidationResult, FLUXFAReportMessage fluxfaReportMessage) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {
            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
            if (fluxfaReportMessage != null && fluxfaReportMessage.getFLUXReportDocument() != null) {
                List<IDType> ids = fluxfaReportMessage.getFLUXReportDocument().getIDS();
                fluxResponseDocument.setReferencedID((CollectionUtils.isNotEmpty(ids)) ? ids.get(0) : null);
            }
            populateFluxResponseDocument(faReportValidationResult, fluxResponseDocument);
            responseMessage.setFLUXResponseDocument(fluxResponseDocument);
            return responseMessage;

        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }

    public FLUXResponseMessage generateFluxResponseMessageForFaQuery(ValidationResult faReportValidationResult, FLUXFAQueryMessage fluxfaQueryMessage, String onValue) {
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

    public boolean isCorrectUUID(List<IDType> ids) {
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
                    log.debug("The given UUID is not in a correct format {}", uuidString);
                }
            }
        } catch (IllegalArgumentException exception) {
            log.debug("The given UUID is not in a correct format {}", uuidString);
        }
        return uuidIsCorrect;
    }

    public void populateFluxResponseDocument(ValidationResult faReportValidationResult, FLUXResponseDocument fluxResponseDocument) throws DatatypeConfigurationException {
        setFluxResponseDocumentIDs(fluxResponseDocument);
        setFluxResponseCreationDate(fluxResponseDocument);
        setFluxResponseDocumentResponseCode(faReportValidationResult, fluxResponseDocument);
        // INFO : From IMPL DOC 2.2 This tag (RejectionReason) will not be there! Requested by DG MAre
        //setFluxResponseDocumentRejectionReason(faReportValidationResult, fluxResponseDocument);
        fluxResponseDocument.setRelatedValidationResultDocuments(getValidationResultDocument(faReportValidationResult)); // Set validation result
        fluxResponseDocument.setRespondentFLUXParty(getRespondedFluxParty()); // Set movement party in the response
    }

    private List<ValidationResultDocument> getValidationResultDocument(ValidationResult faReportValidationResult) throws DatatypeConfigurationException {
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

            if (errorType != null){
                CodeType type = new CodeType();
                type.setValue(errorType.name());
                type.setListID("FLUX_GP_VALIDATION_TYPE");
                analysis.setTypeCode(type);
            }

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

    public void setFluxResponseDocumentResponseCode(ValidationResult faReportValidationResult, FLUXResponseDocument fluxResponseDocument) {
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

    public void setFluxResponseCreationDate(FLUXResponseDocument fluxResponseDocument) throws DatatypeConfigurationException {
        GregorianCalendar date = DateTime.now(DateTimeZone.UTC).toGregorianCalendar();
        XMLGregorianCalendar calender = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
        DateTimeType dateTime = new DateTimeType();
        dateTime.setDateTime(calender);
        fluxResponseDocument.setCreationDateTime(dateTime); // Set creation date time
    }

    public FLUXParty getRespondedFluxParty() {
        IDType idType = new IDType();
        String fluxNationCode = ruleModuleCache.getSingleConfig(FLUX_LOCAL_NATION_CODE);
        String nationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "XEU";
        idType.setValue(nationCode);
        idType.setSchemeID("FLUX_GP_PARTY");
        FLUXParty fluxParty = new FLUXParty();
        fluxParty.setIDS(singletonList(idType));
        return fluxParty;
    }

    private void setFluxResponseDocumentIDs(FLUXResponseDocument fluxResponseDocument) {
        IDType responseId = new IDType();
        responseId.setValue(UUID.randomUUID().toString());
        responseId.setSchemeID("UUID");
        fluxResponseDocument.setIDS(singletonList(responseId)); // Set random ID
    }

}
