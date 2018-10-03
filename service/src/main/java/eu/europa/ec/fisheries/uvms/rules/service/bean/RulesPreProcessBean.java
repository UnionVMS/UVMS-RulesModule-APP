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

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.*;

/**
 * Created by padhyad on 5/8/2017.
 */
@Stateless
@LocalBean
@Slf4j
public class RulesPreProcessBean {

    @EJB
    private RulesDomainModel rulesDomainModel;

    public ValidationResult loadValidationResults(List<String> ids) throws RulesModelException {
        List<ValidationMessageType> validationMessages = rulesDomainModel.getValidationMessagesById(ids);
        ValidationResult validationResultDto = new ValidationResult();
        if (CollectionUtils.isEmpty(validationMessages)) {
            validationResultDto.setOk(true);
        } else {
            for (ValidationMessageType validationMessageType : validationMessages) {
                validationResultDto.setError(validationMessageType.getErrorType().equals(ErrorType.ERROR));
                validationResultDto.setWarning(!validationMessageType.getErrorType().equals(ErrorType.ERROR));
                if(validationResultDto.isError()){
                    break;
                }
            }
            validationResultDto.setValidationMessages(validationMessages);
        }
        return validationResultDto;
    }

    public Map<Boolean, ValidationResult> checkDuplicateIdInRequest(FLUXFAQueryMessage faQueryMessage) throws RulesServiceException {
        Map<Boolean, ValidationResult> validationResultMap = new HashMap<>();
        ValidationResult validationResult;
        try {
            FAQuery faQuery = faQueryMessage.getFAQuery();
            if (faQuery != null) {
                IDType idType = faQuery.getID();
                validationResult = loadValidationResults(idType != null ? Collections.singletonList(idType.getValue()) : Collections.<String>emptyList());
                validationResultMap.put(!(validationResult != null && !validationResult.isOk()), validationResult);
            }
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
        return validationResultMap;
    }

    /**
     *
     * @param fluxFaReportMessage
     * @return
     * @throws RulesServiceException
     */
    public Map<Boolean, ValidationResult> checkDuplicateIdInRequest(FLUXFAReportMessage fluxFaReportMessage) throws RulesServiceException {
        boolean isContinueValidation = true;
        Map<Boolean, ValidationResult> validationResultMap = new HashMap<>();
        ValidationResult validationResult;
        try {
            validationResult = loadValidationResults(getFLUXReportDocumentIDs(fluxFaReportMessage));
            if (validationResult != null && !validationResult.isOk()) {
                isContinueValidation = false;
            } else if (CollectionUtils.isNotEmpty(fluxFaReportMessage.getFAReportDocuments())) {
                Iterator it = fluxFaReportMessage.getFAReportDocuments().iterator();
                while (it.hasNext()) {
                    FAReportDocument faReportDocument = (FAReportDocument) it.next();
                    ValidationResult validationResultFa = loadValidationResults(getIdsFromFluxFaReportDocument(faReportDocument.getRelatedFLUXReportDocument()));
                    if (validationResultFa != null && !validationResultFa.isOk()) {
                        it.remove();
                        addToValidationResult(validationResult, validationResultFa);
                    }
                }
              if (fluxFaReportMessage.getFAReportDocuments().isEmpty()) {
                    isContinueValidation = false;
              }
            }
            validationResultMap.put(isContinueValidation, validationResult);

        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
        return validationResultMap;
    }

    private List<String> getIdsFromFluxFaReportDocument(FLUXReportDocument fluxReportDocument) {
        if (fluxReportDocument == null) {
            return Collections.emptyList();
        }
        return mapToIdsStrList(fluxReportDocument.getIDS());
    }

    public Map<Boolean, ValidationResult> checkDuplicateIdInRequest(FLUXResponseMessage fluxResponseMessage) throws RulesServiceException {
        Map<Boolean, ValidationResult> validationResultMap = new HashMap<>();
        ValidationResult validationResult;
        try {
            FLUXResponseDocument fluxRespDoc = fluxResponseMessage.getFLUXResponseDocument();
            if (fluxRespDoc != null) {
                validationResult = loadValidationResults(mapToIdsStrList(fluxRespDoc.getIDS()));
                validationResultMap.put(!(validationResult != null && !validationResult.isOk()), validationResult);
            }
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
        return validationResultMap;
    }

    private void addToValidationResult(ValidationResult globalValidationResult, ValidationResult validationResultFa) {
        if (globalValidationResult == null) {
            globalValidationResult = validationResultFa;
        }
        globalValidationResult.setError(globalValidationResult.isError() || validationResultFa.isError());
        globalValidationResult.setWarning(globalValidationResult.isWarning() || validationResultFa.isWarning());
        globalValidationResult.setOk(globalValidationResult.isOk() || validationResultFa.isOk());
        globalValidationResult.getValidationMessages().addAll(validationResultFa.getValidationMessages());
    }

    private List<String> getFLUXReportDocumentIDs(FLUXFAReportMessage fluxfaReportMessage) {
        List<IDType> collectedIDs = new ArrayList<>();
        List<FAReportDocument> faReportDocuments = fluxfaReportMessage.getFAReportDocuments();
        if (CollectionUtils.isNotEmpty(faReportDocuments)){
            for (FAReportDocument faReportDocument : faReportDocuments){
                FLUXReportDocument relatedFLUXReportDocument = faReportDocument.getRelatedFLUXReportDocument();
                List<IDType> fluxReportDocumentIDS = relatedFLUXReportDocument.getIDS();
                addIDs(collectedIDs, fluxReportDocumentIDS);
            }
        }
        FLUXReportDocument fluxReportDocument = fluxfaReportMessage.getFLUXReportDocument();
        List<IDType> fluxReportDocumentIDS = fluxReportDocument.getIDS();
        addIDs(collectedIDs, fluxReportDocumentIDS);
        return mapToIdsStrList(collectedIDs);
    }

    private void addIDs(List<IDType> collectedIDIdTypes, List<IDType> fluxReportDocumentIDS) {
        if (CollectionUtils.isNotEmpty(fluxReportDocumentIDS)){
            collectedIDIdTypes.addAll(fluxReportDocumentIDS);
        }
    }

    private List<String> mapToIdsStrList(List<IDType> idTypes) {
        List<String> ids = new ArrayList<>();
        for (IDType idType : idTypes) {
            ids.add(idType.getValue().concat("_").concat(idType.getSchemeID()));
        }
        return ids;
    }
}
