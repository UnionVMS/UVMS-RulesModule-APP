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

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

@Data
@EqualsAndHashCode(callSuper = true)
public class FaResponseFact extends AbstractFact {

    private List<IdType> ids;
    private IdType referencedID;
    private CodeType responseCode;
    private DateTimeType creationDateTime;
    private String creationDateTimeString;
    private List<IdType> fluxPartyIds;
    private List<ValidationResultDocument> relatedValidationResultDocuments;
    private List<IdType> validatorIDs;
    private FLUXParty respondentFLUXParty;
    private List<IdType> idsExistinigInTheDb;

    public boolean isValidatorIdPresent(List<ValidationResultDocument> relatedValidationResultDocuments) {
        if (CollectionUtils.isEmpty(relatedValidationResultDocuments)) {
            return false;
        }
        for (ValidationResultDocument validationResultDocument : relatedValidationResultDocuments) {
            if (validationResultDocument.getValidatorID() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidationQualityAnalysisPresent(List<ValidationResultDocument> relatedValidationResultDocuments) {
        if (CollectionUtils.isEmpty(relatedValidationResultDocuments)) {
            return false;
        }
        for (ValidationResultDocument validationResultDocument : relatedValidationResultDocuments) {
            if (CollectionUtils.isEmpty(validationResultDocument.getRelatedValidationQualityAnalysises())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_RESPONSE;
    }

}
