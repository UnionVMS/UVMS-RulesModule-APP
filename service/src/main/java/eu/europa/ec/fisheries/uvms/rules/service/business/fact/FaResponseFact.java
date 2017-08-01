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

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;

import java.util.Date;
import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FaResponseFact extends AbstractFact {

    private List<IdType> ids;
    private IdType referencedID;
    private CodeType responseCode;
    private Date creationDateTime;
    private List<IdType> fluxPartyIds;
    List<ValidationResultDocument> relatedValidationResultDocuments;
    private List<IdType> validatorIDs;

    public IdType getReferencedID() {
        return referencedID;
    }

    public void setReferencedID(IdType referencedID) {
        this.referencedID = referencedID;
    }

    public FaResponseFact() {
        setFactType();
    }

    public List<IdType> getIds() {
        return ids;
    }

    public void setIds(List<IdType> ids) {
        this.ids = ids;
    }

    public CodeType getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(CodeType responseCode) {
        this.responseCode = responseCode;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public List<IdType> getFluxPartyIds() {
        return fluxPartyIds;
    }

    public void setFluxPartyIds(List<IdType> fluxPartyIds) {
        this.fluxPartyIds = fluxPartyIds;
    }

    public List<ValidationResultDocument> getRelatedValidationResultDocuments() {
        return relatedValidationResultDocuments;
    }

    public void setRelatedValidationResultDocuments(List<ValidationResultDocument> relatedValidationResultDocuments) {
        this.relatedValidationResultDocuments = relatedValidationResultDocuments;
    }

    public List<IdType> getValidatorIDs() {
        return validatorIDs;
    }

    public void setValidatorIDs(List<IdType> validatorIDs) {
        this.validatorIDs = validatorIDs;
    }

    public boolean ifValidatorIdPresent(List<ValidationResultDocument> relatedValidationResultDocuments){
        if(CollectionUtils.isEmpty(relatedValidationResultDocuments)){
            return false;
        }

        for(ValidationResultDocument validationResultDocument : relatedValidationResultDocuments){
            if(validationResultDocument.getValidatorID() ==null){
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
