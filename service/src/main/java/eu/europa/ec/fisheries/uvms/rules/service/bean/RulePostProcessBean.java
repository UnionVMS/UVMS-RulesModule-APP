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
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleWarning;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import javax.ejb.*;
import java.util.*;

@Stateless
@LocalBean
@Slf4j
public class RulePostProcessBean {

    @EJB
    private RulesDomainModel rulesDomainModel;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ValidationResult checkAndUpdateValidationResult(Collection<AbstractFact> facts, String rawMessage, String rawMsgGuid, RawMsgType type) {
        try {
            boolean isError = false;
            boolean isWarning = false;
            List<ValidationMessageType> validationMessages = new ArrayList<>();
            for (AbstractFact fact : facts) {
                if (fact!=null && fact.hasWarOrErr()) {
                    List<String> uniqueIds = fact.getUniqueIds();
                    Date factDate = fact.getCreationJavaDateOfMessage();
                    for (RuleError error : fact.getErrors()) {
                        isError = true;
                        ValidationMessageType validationMessage = createValidationMessageFromParams(error.getRuleId(), ErrorType.ERROR, error.getMessage(), error.getLevel(), uniqueIds, error.getXpaths(), factDate);
                        validationMessages.add(validationMessage);
                    }
                    for (RuleWarning warning : fact.getWarnings()) {
                        isWarning = true;
                        ValidationMessageType validationMessage = createValidationMessageFromParams(warning.getRuleId(), ErrorType.WARNING, warning.getMessage(), warning.getLevel(), uniqueIds, warning.getXpaths(), factDate);
                        validationMessages.add(validationMessage);
                    }
                }
            }
            saveValidationResult(validationMessages, rawMessage, rawMsgGuid, type);
            return createValidationResultDtoFromParams(isError, isWarning, validationMessages.isEmpty(), validationMessages);
        } catch (RulesModelException e) {
            log.error(e.getMessage(), e);
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ValidationResult checkAndUpdateValidationResultForGeneralBusinessRules(RuleError error, String rawMessage, String rawMsgGuid, RawMsgType type, Date factDate) throws RulesServiceException {
        try {
            final ValidationMessageType validationMessage = createValidationMessageFromParams(error.getRuleId(), ErrorType.ERROR, error.getMessage(), error.getLevel(), Collections.emptyList(), Collections.emptyList(), factDate);
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

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveOrUpdateValidationResultForPermission(String rawMsgGuid, RawMsgType type, String rawMessage, ValidationResult validationResult) {
        try {
            if (validationResult.getValidationMessages().size() == 1) { // only the permission validation message is present
                saveValidationResult(validationResult.getValidationMessages(), rawMessage, rawMsgGuid, type);
            } else {
                Optional<ValidationMessageType> permissionValidationMessage = validationResult.getValidationMessages().stream().filter(m -> m.getBrId().contains("9999")).findFirst();
                if (permissionValidationMessage.isPresent()){
                    updateValidationResult(rawMsgGuid, type.value(), permissionValidationMessage.get());
                }
            }
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

    public ValidationMessageType createOrUpdateValidationResult(String rawMsgGuid, String rawMessage, RawMsgType type, RuleError ruleError) throws RulesModelException {
        final ValidationMessageType validationMessage = createValidationMessageFromParams(ruleError.getRuleId(), ErrorType.ERROR, ruleError.getMessage(), ruleError.getLevel(), Collections.emptyList(), ruleError.getXpaths(), new Date());
        rulesDomainModel.createOrUpdateValidationMessagesWithPermission(validationMessage, rawMsgGuid, rawMessage, type);
        return validationMessage;
    }

    private void updateValidationResult(String rawMsgGuid, String type, ValidationMessageType validationMessage) throws RulesModelException {
        rulesDomainModel.updateValidationMessagesWithPermission(validationMessage, rawMsgGuid, type);

    }

    private ValidationResult createValidationResultDtoFromParams(boolean isError, boolean isWarning, boolean isOk, List<ValidationMessageType> validationMessages) {
        ValidationResult validationResultDto = new ValidationResult();
        validationResultDto.setError(isError);
        validationResultDto.setWarning(isWarning);
        validationResultDto.setOk((isOk));
        validationResultDto.setValidationMessages(validationMessages);
        return validationResultDto;
    }

    private ValidationMessageType createValidationMessageFromParams(String ruleId, ErrorType warning2, String message, String level, List<String> uniqueIds, List<String> xpaths, Date factDate) {
        ValidationMessageType validationMessage = new ValidationMessageType();
        validationMessage.setBrId(ruleId);
        validationMessage.setErrorType(warning2);
        validationMessage.setMessage(message);
        validationMessage.setLevel(level);
        validationMessage.getMessageId().addAll(uniqueIds);
        validationMessage.getXpaths().addAll(xpaths);
        validationMessage.setFactDate(factDate);
        return validationMessage;
    }

}