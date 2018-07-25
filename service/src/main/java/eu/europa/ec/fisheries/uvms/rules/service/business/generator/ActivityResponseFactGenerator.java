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

package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.uvms.commons.date.XMLDateUtils;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.ValidationQualityAnalysisFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FaResponseFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationQualityAnalysis;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.*;

public class ActivityResponseFactGenerator extends AbstractGenerator {

    private FLUXResponseMessage fluxResponseMessage;
    private FaResponseFactMapper responseFactMapper;
    private XPathStringWrapper stringWrapper;

    public ActivityResponseFactGenerator(FLUXResponseMessage fluxResponseMessage, FaResponseFactMapper responseFactMapper) {
        this.fluxResponseMessage = fluxResponseMessage;
        this.responseFactMapper = responseFactMapper;
        this.stringWrapper = responseFactMapper.getStringWrapper();
    }

    @Override
    public List<AbstractFact> generateAllFacts() {
        List<AbstractFact> facts = new ArrayList<>();
        stringWrapper.append(FLUX_RESPONSE_MESSAGE).append(FLUX_RESPONSE_DOCUMENT);
        facts.add(responseFactMapper.generateFactsForFaResponse(fluxResponseMessage));
        FLUXResponseDocument fluxResponseDocument = fluxResponseMessage.getFLUXResponseDocument();
        if (fluxResponseDocument != null) {
            List<ValidationResultDocument> validationResultDocuments = fluxResponseDocument.getRelatedValidationResultDocuments();
            int index = 1;
            String partialXpath = stringWrapper.getValue();
            for (ValidationResultDocument validationResultDocument : validationResultDocuments) {
                String partialSpecFishActXpath = stringWrapper.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VALIDATION_RESULT_DOCUMENT, index).getValue();
                stringWrapper.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(addFactsForValidationQualityAnalysis(validationResultDocument.getRelatedValidationQualityAnalysises()));
                index++;
            }
        }

        if (fluxResponseMessage.getFLUXResponseDocument() != null){
            DateTimeType creationDateTime = fluxResponseMessage.getFLUXResponseDocument().getCreationDateTime();
            for (AbstractFact fact : facts) {
                if(creationDateTime != null){
                    Date repDat = XMLDateUtils.xmlGregorianCalendarToDate(creationDateTime.getDateTime());
                    if(repDat != null){
                        fact.setCreationDateOfMessage(new DateTime(repDat));
                    }
                }
            }
        }

        return facts;
    }

    private List<AbstractFact> addFactsForValidationQualityAnalysis(List<ValidationQualityAnalysis> validationResultDocuments) {
        List<AbstractFact> facts = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(validationResultDocuments)) {
            int index = 1;
            String partialXpath = stringWrapper.getValue();
            for (ValidationQualityAnalysis validationQualityAnalysis : validationResultDocuments) {
                stringWrapper.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VALIDATION_QUALITY_ANALYSIS, index);
                facts.add(generateFactsForValidationQualityAnalysis(validationQualityAnalysis));
                index++;
            }
        }
        return facts;
    }

    private ValidationQualityAnalysisFact generateFactsForValidationQualityAnalysis(ValidationQualityAnalysis validationQualityAnalysis) {
        if (validationQualityAnalysis == null) {
            return null;
        }
        ValidationQualityAnalysisFact qualityAnalysisFact = new ValidationQualityAnalysisFact();
        String partialXpath = stringWrapper.getValue();
        stringWrapper.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(qualityAnalysisFact, "id");
        qualityAnalysisFact.setId(mapToIdType(validationQualityAnalysis.getID()));
        stringWrapper.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(qualityAnalysisFact, "typeCode");
        qualityAnalysisFact.setTypeCode(mapToCodeType(validationQualityAnalysis.getTypeCode()));
        stringWrapper.appendWithoutWrapping(partialXpath).append(LEVEL_CODE).storeInRepo(qualityAnalysisFact, "levelCode");
        qualityAnalysisFact.setLevelCode(mapToCodeType(validationQualityAnalysis.getLevelCode()));
        stringWrapper.appendWithoutWrapping(partialXpath).append(RESULT).storeInRepo(qualityAnalysisFact, "results");
        qualityAnalysisFact.setResults(mapFromTextTypeToString(validationQualityAnalysis.getResults()));
        stringWrapper.appendWithoutWrapping(partialXpath).append(REFERENCED_ITEM).storeInRepo(qualityAnalysisFact, "referencedItems");
        qualityAnalysisFact.setReferencedItems(mapFromTextTypeToString(validationQualityAnalysis.getReferencedItems()));
        return qualityAnalysisFact;
    }

    private IdType mapToIdType(IDType idType) {
        if (idType == null) {
            return null;
        }
        IdType idType1 = new IdType();
        idType1.setSchemeId(idType.getSchemeID());
        idType1.setValue(idType.getValue());
        return idType1;
    }

    private eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType mapToCodeType(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType) {
        if (codeType == null) {
            return null;
        }
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType codeType1 = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        codeType1.setListId(codeType.getListID());
        codeType1.setValue(codeType.getValue());
        return codeType1;
    }

    private List<String> mapFromTextTypeToString(List<TextType> txTypeList) {
        if (CollectionUtils.isEmpty(txTypeList)) {
            return Collections.emptyList();
        }
        List<String> strList = new ArrayList<>();
        for (TextType txType : txTypeList) {
            strList.add(txType.getValue());
        }
        return strList;
    }

    @Override
    public void setBusinessObjectMessage(Object businessObject) throws RulesValidationException {
        if (!(businessObject instanceof FLUXResponseMessage)) {
            throw new RulesValidationException("Business object does not match required type");
        }
        this.fluxResponseMessage = (FLUXResponseMessage) businessObject;
    }
}
