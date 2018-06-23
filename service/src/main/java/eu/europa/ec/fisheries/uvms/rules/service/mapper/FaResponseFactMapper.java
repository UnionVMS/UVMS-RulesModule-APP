/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.CREATION_DATE_TIME;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.ID;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.REFERENCED_ID;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.RELATED_VALIDATION_RESULT_DOCUMENT;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.RESPONDENT_FLUX_PARTY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.RESPONSE_CODE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.VALIDATOR_ID;
import static eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper.getDate;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.commons.date.XMLDateUtils;
import eu.europa.ec.fisheries.uvms.rules.service.business.MessageType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaResponseFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@NoArgsConstructor
@Data
public class FaResponseFactMapper {

    private static final String REFERENCED_ID_ATTR = "referencedID";
    private static final String RESPONSE_CODE_ATTR = "responseCode";
    private static final String FA_RESPONSE_IDS_ATTR = "faResponseIds";
    private static final String IDS_ATTR = "ids";
    private static final String CREATION_DATE_TIME_ATTR = "creationDateTime";

    private XPathStringWrapper stringWrapper;
    private String from;
    private MessageType messageType;

    private List<IdType> faResponseIds = new ArrayList<>();

    public FaResponseFactMapper(XPathStringWrapper stringWrapper) {
        this.stringWrapper = stringWrapper;
    }

    public FaResponseFact generateFactsForFaResponse(FLUXResponseMessage fluxResponseMessage) {
        if (fluxResponseMessage == null) {
            return null;
        }

        FaResponseFact fact = new FaResponseFact();
        fact.setSenderOrReceiver(from);
        fact.setMessageType(messageType);
        String partialXpath = stringWrapper.getValue();

        FLUXResponseDocument fluxResponseDocument = fluxResponseMessage.getFLUXResponseDocument();

        if (fluxResponseDocument != null) {

            fact.setReferencedID(mapToIdType(fluxResponseDocument.getReferencedID()));
            stringWrapper.appendWithoutWrapping(partialXpath).append(REFERENCED_ID).storeInRepo(fact, REFERENCED_ID_ATTR);

            fact.setIds(mapToIdType(fluxResponseDocument.getIDS()));
            stringWrapper.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(fact, IDS_ATTR);

            stringWrapper.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(fact, FA_RESPONSE_IDS_ATTR);

            fact.setResponseCode(mapToCodeType(fluxResponseDocument.getResponseCode()));
            stringWrapper.appendWithoutWrapping(partialXpath).append(RESPONSE_CODE).storeInRepo(fact, RESPONSE_CODE_ATTR);

            fact.setCreationDateTime(getDate(fluxResponseDocument.getCreationDateTime()));
            stringWrapper.appendWithoutWrapping(partialXpath).append(CREATION_DATE_TIME).storeInRepo(fact, CREATION_DATE_TIME_ATTR);

            DateTimeType creationDateTime = fluxResponseDocument.getCreationDateTime();
            if (creationDateTime != null){
                fact.setCreationDateTimeString(XMLDateUtils.xmlGregorianCalendarToDate(fluxResponseDocument.getCreationDateTime().getDateTime()).toString());
            }

            fact.setRespondentFLUXParty(fluxResponseDocument.getRespondentFLUXParty());
            stringWrapper.appendWithoutWrapping(partialXpath).append(RESPONDENT_FLUX_PARTY).storeInRepo(fact, "respondentFLUXParty");

            if (fluxResponseDocument.getRespondentFLUXParty() != null) {
                fact.setFluxPartyIds(mapToIdType(fluxResponseDocument.getRespondentFLUXParty().getIDS()));
            }

            stringWrapper.appendWithoutWrapping(partialXpath).append(RESPONDENT_FLUX_PARTY, ID).storeInRepo(fact, "fluxPartyIds");

            fact.setValidatorIDs(extractValidatorIdFromValidationResultDocument(fluxResponseDocument));
            stringWrapper.appendWithoutWrapping(partialXpath).append(VALIDATOR_ID).storeInRepo(fact, "validatorIDs");

            fact.setRelatedValidationResultDocuments(fluxResponseDocument.getRelatedValidationResultDocuments());
            stringWrapper.appendWithoutWrapping(partialXpath).append(RELATED_VALIDATION_RESULT_DOCUMENT).storeInRepo(fact, "relatedValidationResultDocuments");

        }
        return fact;
    }

    private IdType mapToIdType(IDType idType) { // FIXME Mapstruct
        if (idType == null) {
            return null;
        }
        IdType idType1 = new IdType();
        idType1.setSchemeId(idType.getSchemeID());
        idType1.setValue(idType.getValue());
        return idType1;
    }

    private List<IdType> mapToIdType(List<IDType> idTypes) { // FIXME Mapstruct
        List<IdType> idTypeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(idTypes)) {
            for (IDType iDType : idTypes) {
                IdType idType = mapToIdType(iDType);
                if (idType != null) {
                    idTypeList.add(mapToIdType(iDType));
                }
            }
        }
        return idTypeList;
    }

    private List<IdType> extractValidatorIdFromValidationResultDocument(FLUXResponseDocument fluxResponseDocument) {
        List<IdType> idTypes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fluxResponseDocument.getRelatedValidationResultDocuments())) {
            List<ValidationResultDocument> validationResultDocuments = fluxResponseDocument.getRelatedValidationResultDocuments();
            for (ValidationResultDocument validationResultDocument : validationResultDocuments) {
                if (validationResultDocument.getValidatorID() != null) {
                    idTypes.add(mapToIdType(validationResultDocument.getValidatorID()));
                }
            }
        }
        return idTypes;
    }

    private eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType mapToCodeType(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType) {  // FIXME Mapstruct
        if (codeType == null) {
            return null;
        }
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType codeType1 = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        codeType1.setListId(codeType.getListID());
        codeType1.setValue(codeType.getValue());
        return codeType1;
    }
}
