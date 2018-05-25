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

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMessageType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleWarning;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Stateless
@LocalBean
@Slf4j
public class RulePostProcessBean {

    @EJB
    private RulesDomainModel rulesDomainModel;

    @Transactional(Transactional.TxType.REQUIRED)
    public ValidationResultDto checkAndUpdateValidationResult(List<AbstractFact> facts, String rawMessage, String rawMsgGuid, RawMsgType type) {
        try {
            boolean isError = false;
            boolean isWarning = false;
            List<ValidationMessageType> validationMessages = new ArrayList<>();
            for (AbstractFact fact : facts) {
                if (fact.hasWarOrErr()) {
                    List<String> uniqueIds = fact.getUniqueIds();
                    for (RuleError error : fact.getErrors()) {
                        isError = true;
                        ValidationMessageType validationMessage = createValidationMessageFromParams(error.getRuleId(), ErrorType.ERROR, error.getMessage(), error.getLevel(), uniqueIds, error.getXpaths());
                        validationMessages.add(validationMessage);
                    }
                    for (RuleWarning warning : fact.getWarnings()) {
                        isWarning = true;
                        ValidationMessageType validationMessage = createValidationMessageFromParams(warning.getRuleId(), ErrorType.WARNING, warning.getMessage(), warning.getLevel(), uniqueIds, warning.getXpaths());
                        validationMessages.add(validationMessage);
                    }
                }
            }
            saveValidationResult(validationMessages, rawMessage, rawMsgGuid, type);

            // TODO : Create alarm in future
            return createValidationResultDtoFromParams(isError, isWarning, validationMessages.isEmpty(), validationMessages);
        } catch (RulesModelException e) {
            log.error(e.getMessage(), e);
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public ValidationResultDto checkAndUpdateValidationResultForGeneralBusinessRules(RuleError error, String rawMessage, String rawMsgGuid, RawMsgType type) throws RulesServiceException {
        try {
            final ValidationMessageType validationMessage = createValidationMessageFromParams(error.getRuleId(), ErrorType.ERROR, error.getMessage(), error.getLevel(), Collections.<String>emptyList(), Collections.<String>emptyList());
            List<ValidationMessageType> validationMessages = new ArrayList<>();
            validationMessages.add(validationMessage);
            saveValidationResult(validationMessages, rawMessage, rawMsgGuid, type);
            // TODO : Create alarm in future
            return createValidationResultDtoFromParams(false, false, validationMessages.isEmpty(), validationMessages);
        } catch (RulesModelException e) {
            log.error(e.getMessage(), e);
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private void saveValidationResult(List<ValidationMessageType> validationMessageTypes, String rawMessage, String rawMsgGuid, RawMsgType type) throws RulesModelException {
        if (!CollectionUtils.isEmpty(validationMessageTypes)) {
            RawMessageType message = new RawMessageType();
            message.setMessage(rawMessage);
            message.getValidationMessage().addAll(validationMessageTypes);
            message.setRawMessageGuid(rawMsgGuid);
            message.setMsgType(type);
            rulesDomainModel.saveValidationMessages(message);
        }
    }

    private ValidationResultDto createValidationResultDtoFromParams(boolean isError, boolean isWarning, boolean isOk, List<ValidationMessageType> validationMessages) {
        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setError(isError);
        validationResultDto.setWarning(isWarning);
        validationResultDto.setOk((isOk));
        validationResultDto.setValidationMessages(validationMessages);
        return validationResultDto;
    }

    private ValidationMessageType createValidationMessageFromParams(String ruleId, ErrorType warning2, String message, String level, List<String> uniqueIds, List<String> xpaths) {
        ValidationMessageType validationMessage = new ValidationMessageType();
        validationMessage.setBrId(ruleId);
        validationMessage.setErrorType(warning2);
        validationMessage.setMessage(message);
        validationMessage.setLevel(level);
        validationMessage.getMessageId().addAll(uniqueIds);
        validationMessage.getXpaths().addAll(xpaths);
        return validationMessage;
    }

}