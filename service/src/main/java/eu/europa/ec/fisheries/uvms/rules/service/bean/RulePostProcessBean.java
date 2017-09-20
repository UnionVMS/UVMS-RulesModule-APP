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
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMessageType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.MessageService;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleWarning;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by padhyad on 5/4/2017.
 */
@Stateless
@LocalBean
@Slf4j
public class RulePostProcessBean {

    @EJB(lookup = ServiceConstants.DB_ACCESS_RULES_DOMAIN_MODEL)
    private RulesDomainModel rulesDomainModel;

    @EJB
    private MessageService messageService;

    @Transactional(Transactional.TxType.REQUIRED)
    public ValidationResultDto checkAndUpdateValidationResult(List<AbstractFact> facts, String rawMessage) throws RulesServiceException {
        try {
            boolean isError = false;
            boolean isWarning = false;
            boolean isOk = false;
            List<ValidationMessageType> validationMessages = new ArrayList<>();
            for (AbstractFact fact : facts) {
                if (!fact.isOk()) {
                    for (RuleError error : fact.getErrors()) {
                        isError = true;
                        ValidationMessageType validationMessage = getValidationMessageType(error.getRuleId(), ErrorType.ERROR, error.getMessage(), error.getLevel(), fact.getUniqueIds(), error.getXpaths());
                        validationMessages.add(validationMessage);
                    }
                    for (RuleWarning warning : fact.getWarnings()) {
                        isWarning = true;
                        ValidationMessageType validationMessage = getValidationMessageType(warning.getRuleId(), ErrorType.WARNING, warning.getMessage(), warning.getLevel(), fact.getUniqueIds(), warning.getXpaths());
                        validationMessages.add(validationMessage);
                    }
                }
            }
            if (validationMessages.isEmpty()) {
                isOk = true;
            }
            saveValidationResult(validationMessages, rawMessage);
            ValidationResultDto validationResultDto = getValidationResultDto(isError, isWarning, isOk, validationMessages);

            // TODO : Create alarm in future
            return validationResultDto;
        } catch (RulesModelException e) {
            log.error(e.getMessage(), e);
            throw new RulesServiceException(e.getMessage(), e);
        }
    }



    @Transactional(Transactional.TxType.REQUIRED)
    public ValidationResultDto checkAndUpdateValidationResultForGeneralBuinessRules(RuleError error, ErrorType errorType, String rawMessage) throws RulesServiceException {
        try {
            boolean isError = false;
            boolean isWarning = false;
            boolean isOk = false;
            List<ValidationMessageType> validationMessages = new ArrayList<>();
            ValidationMessageType validationMessage = getValidationMessageType(error.getRuleId(), ErrorType.ERROR, error.getMessage(), error.getLevel(), Collections.<String>emptyList(), Collections.<String>emptyList());
            validationMessages.add(validationMessage);

            if (validationMessages.isEmpty()) {
                isOk = true;
            }
            saveValidationResult(validationMessages, rawMessage);
            ValidationResultDto validationResultDto = getValidationResultDto(isError, isWarning, isOk, validationMessages);

            // TODO : Create alarm in future
            return validationResultDto;
        } catch (RulesModelException e) {
            log.error(e.getMessage(), e);
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

  /*  public FLUXResponseMessage sendXMLErrorResponseMessage(String errorMessage,String rawMessage,Object request){
        List<String> xpaths = new ArrayList<>();
        xpaths.add(errorMessage);
        RuleError ruleError= new RuleError(ServiceConstants.INVALID_XML_RULE,  ServiceConstants.INVALID_XML_RULE_MESSAGE,  "L00", xpaths);;
        ValidationResultDto validationResultDto=checkAndUpdateValidationResultForGeneralBuinessRules(ruleError,ErrorType.ERROR,rawMessage);
        FLUXResponseMessage fluxResponseMessage=null;
        if(request instanceof  FLUXFAReportMessage) {
            fluxResponseMessage = messageService.generateFluxResponseMessage(validationResultDto, (FLUXFAReportMessage) request);
        }else if(request instanceof FLUXFAQueryMessage){
            fluxResponseMessage = messageService.generateFluxResponseMessage(validationResultDto, (FLUXFAQueryMessage) request);
        }else if(request instanceof FLUXResponseMessage){
            fluxResponseMessage = messageService.generateFluxResponseMessage(validationResultDto, (FLUXResponseMessage) request);
        }

        return fluxResponseMessage;
    }*/





    private ValidationResultDto getValidationResultDto(boolean isError, boolean isWarning, boolean isOk, List<ValidationMessageType> validationMessages) {
        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setIsError(isError);
        validationResultDto.setIsWarning(isWarning);
        validationResultDto.setIsOk((isOk));
        validationResultDto.setValidationMessages(validationMessages);
        return validationResultDto;
    }

    private ValidationMessageType getValidationMessageType(String ruleId, ErrorType warning2, String message, String level, List<String> uniqueIds, List<String> xpaths) {
        ValidationMessageType validationMessage = new ValidationMessageType();
        validationMessage.setBrId(ruleId);
        validationMessage.setErrorType(warning2);
        validationMessage.setMessage(message);
        validationMessage.setLevel(level);
        validationMessage.getMessageId().addAll(uniqueIds);
        validationMessage.getXpaths().addAll(xpaths);
        return validationMessage;
    }


    private void saveValidationResult(List<ValidationMessageType> validationMessageTypes, String rawMessage) throws RulesModelException {
        if (!CollectionUtils.isEmpty(validationMessageTypes)) {
            RawMessageType message = new RawMessageType();
            message.setMessage(rawMessage);
            message.getValidationMessage().addAll(validationMessageTypes);
            rulesDomainModel.saveValidationMessages(message);
        }
    }
}
