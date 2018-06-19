/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageTypeResponse;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.business.EnrichedBRMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;
import java.util.List;

@Stateless
@LocalBean
@Slf4j
public class GetValidationResultService {

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesDomainModel domainModel;

    @EJB
    private MDRCacheRuleService mdrCacheService;

    public String getValidationsForRawMessageUUID(String guid, String type) {
        ValidationMessageTypeResponse validationsResponse = new ValidationMessageTypeResponse();
        try {
            validationsResponse.getValidationsListResponse().addAll(domainModel.getValidationMessagesByRawMsgGuid(guid, type));
            List<ValidationMessageType> validationsListResponse = validationsResponse.getValidationsListResponse();
            for (ValidationMessageType validationMessageType : validationsListResponse) {
                if (validationMessageType != null){
                    String brId = validationMessageType.getBrId();
                    loadValidationMessagesFromMDR(validationMessageType, brId);
                }
            }
            return JAXBUtils.marshallJaxBObjectToString(validationsResponse);
        } catch (RulesModelException | JAXBException e) {
            log.error("Error in method getValidationsForRawMessageUUID()", e);
        }
        return StringUtils.EMPTY;
    }

    private void loadValidationMessagesFromMDR(ValidationMessageType validationMessageType, String brId) {
        EnrichedBRMessage errorMessageForBrId = mdrCacheService.getErrorMessageForBrId(brId);
        if (errorMessageForBrId != null){
            validationMessageType.setExpression(errorMessageForBrId.getExpression());
            validationMessageType.setMessage(errorMessageForBrId.getMessage());
            validationMessageType.setNote(errorMessageForBrId.getNote());
            validationMessageType.setEntity(errorMessageForBrId.getTemplateEntityName());
        }
    }
}