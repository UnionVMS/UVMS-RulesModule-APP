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

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
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

    public ValidationResultDto getValidationResultIfExist(List<String> ids) throws RulesModelException {
        List<ValidationMessageType> validationMessages = rulesDomainModel.getValidationMessagesById(ids);
        ValidationResultDto validationResultDto = new ValidationResultDto();
        if (CollectionUtils.isEmpty(validationMessages)) {
            validationResultDto.setIsOk(true);
        } else {
            for (ValidationMessageType validationMessageType : validationMessages) {
                validationResultDto.setIsError(validationMessageType.getErrorType().equals(ErrorType.ERROR));
                validationResultDto.setIsWarning(!validationMessageType.getErrorType().equals(ErrorType.ERROR));
            }
            validationResultDto.setValidationMessages(validationMessages);
        }
        return validationResultDto;
    }

    public Map<Boolean, ValidationResultDto> checkDuplicateIdInRequest(FLUXFAReportMessage fluxfaReportMessage) throws RulesServiceException {
        boolean isContinueValidation = true;
        Map<Boolean, ValidationResultDto> validationResultMap = new HashMap<>();
        ValidationResultDto validationResult;
        try {
            validationResult = getValidationResultIfExist(getIds(fluxfaReportMessage.getFLUXReportDocument()));
            if (validationResult != null && !validationResult.isOk()) {
                isContinueValidation = false;
            } else if (fluxfaReportMessage.getFAReportDocuments() != null) {
                Iterator it = fluxfaReportMessage.getFAReportDocuments().iterator();
                while (it.hasNext()) {
                    FAReportDocument faReportDocument = (FAReportDocument)it.next();
                    ValidationResultDto validationResultFa = getValidationResultIfExist(getIds(faReportDocument.getRelatedFLUXReportDocument()));
                    if (validationResultFa != null && !validationResultFa.isOk()) {
                        it.remove();
                        addToValidationResult(validationResult, validationResultFa);
                    }
                }
                if (fluxfaReportMessage.getFAReportDocuments().isEmpty()) {
                    isContinueValidation = false;
                }
            }
            validationResultMap.put(isContinueValidation, validationResult);

        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
        return validationResultMap;
    }

    private void addToValidationResult(ValidationResultDto globalValidationResult, ValidationResultDto validationResultFa) {
        if (globalValidationResult == null) {
            globalValidationResult = validationResultFa;
        }
        globalValidationResult.setIsError(globalValidationResult.isError() || validationResultFa.isError());
        globalValidationResult.setIsWarning(globalValidationResult.isWarning() || validationResultFa.isWarning());
        globalValidationResult.setIsOk(globalValidationResult.isOk() || validationResultFa.isOk());
        globalValidationResult.getValidationMessages().addAll(validationResultFa.getValidationMessages());
    }

    private List<String> getIds(FLUXReportDocument fluxReportDocument) {
        if (fluxReportDocument == null) {
            return Collections.emptyList();
        }
        List<IDType> idTypes = fluxReportDocument.getIDS();
        List<String> ids = new ArrayList<>();
        for (IDType idType : idTypes) {
            ids.add(idType.getValue().concat("_").concat(idType.getSchemeID()));
        }
        return ids;
    }
}
