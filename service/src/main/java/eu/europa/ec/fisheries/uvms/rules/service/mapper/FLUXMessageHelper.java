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
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.entity.FAUUIDType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import static eu.europa.ec.fisheries.uvms.rules.entity.FAUUIDType.FA_QUERY_ID;

public class FLUXMessageHelper {

    private static final String FLUXFAREPORT_MESSAGE_3P1_XSD = "xsd/contract/fa/data/standard/FLUXFAReportMessage_3p1.xsd";
    private static final String FLUXFAQUERY_MESSAGE_3P0_XSD = "xsd/contract/fa/data/standard/FLUXFAQueryMessage_3p0.xsd";
    private static final String FLUXFARESPONSE_MESSAGE_6P0_XSD = "xsd/contract/fa/data/standard/FLUXResponseMessage_6p0.xsd";
    private static final String DASH = "-";

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
        // Purpose code
        //CodeType purposeCode = fluxReportDocument.getPurposeCode();
        // Check if we need the purpose codes! For now it seems not!
        //List<String> purposeCodes = purposeCode != null && StringUtils.isNotEmpty(purposeCode.getValue()) ?
        //Arrays.asList(purposeCode.getValue()) : Arrays.asList("1", "3", "5", "9");

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
        URL resource = FLUXMessageHelper.class.getClassLoader().getResource(xsdLocation);
        if (resource != null) {
            try {
                return sf.newSchema(resource);
            } catch (SAXException e) {
                throw new UnmarshalException(e.getMessage(), e);
            }
        }
        throw new UnmarshalException("ERROR WHILE TRYING TO LOOKUP XSD SCHEMA");
    }

}
