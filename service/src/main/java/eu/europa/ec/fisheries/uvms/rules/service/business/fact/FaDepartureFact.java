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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;

import java.util.Date;
import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FaDepartureFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;

    private CodeType faReportDocumentTypeCode;

    private Date occurrenceDateTime;

    private CodeType reasonCode;

    private String purposeCode;

    private List<FLUXLocation> relatedFLUXLocations;

    private List<CodeType> relatedFLUXLocationTypeCodes;

    private List<CodeType> specifiedFishingGearRoleCodeTypes;

    private List<CodeType> specifiedFACatchCodeTypes;

    private FishingTrip specifiedFishingTrip;

    private List<IdType> specifiedFishingTripIds;

    private List<String> faTypesPerTrip;

    public FaDepartureFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_DEPARTURE;
    }

    public CodeType getFishingActivityTypeCode() {
        return fishingActivityTypeCode;
    }
    public void setFishingActivityTypeCode(CodeType fishingActivityTypeCode) {
        this.fishingActivityTypeCode = fishingActivityTypeCode;
    }
    public CodeType getFaReportDocumentTypeCode() {
        return faReportDocumentTypeCode;
    }
    public void setFaReportDocumentTypeCode(CodeType faReportDocumentTypeCode) {
        this.faReportDocumentTypeCode = faReportDocumentTypeCode;
    }
    public CodeType getReasonCode() {
        return reasonCode;
    }
    public void setReasonCode(CodeType reasonCode) {
        this.reasonCode = reasonCode;
    }
    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }
    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }
    public FishingTrip getSpecifiedFishingTrip() {
        return specifiedFishingTrip;
    }
    public void setSpecifiedFishingTrip(FishingTrip specifiedFishingTrip) {
        this.specifiedFishingTrip = specifiedFishingTrip;
    }
    public Date getOccurrenceDateTime() {
        return occurrenceDateTime;
    }
    public void setOccurrenceDateTime(Date occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
    }
    public List<CodeType> getRelatedFLUXLocationTypeCodes() {
        return relatedFLUXLocationTypeCodes;
    }
    public void setRelatedFLUXLocationTypeCodes(List<CodeType> relatedFLUXLocationTypeCodes) {
        this.relatedFLUXLocationTypeCodes = relatedFLUXLocationTypeCodes;
    }
    public List<CodeType> getSpecifiedFishingGearRoleCodeTypes() {
        return specifiedFishingGearRoleCodeTypes;
    }
    public void setSpecifiedFishingGearRoleCodeTypes(List<CodeType> specifiedFishingGearRoleCodeTypes) {
        this.specifiedFishingGearRoleCodeTypes = specifiedFishingGearRoleCodeTypes;
    }
    public List<CodeType> getSpecifiedFACatchCodeTypes() {
        return specifiedFACatchCodeTypes;
    }
    public void setSpecifiedFACatchCodeTypes(List<CodeType> specifiedFACatchCodeTypes) {
        this.specifiedFACatchCodeTypes = specifiedFACatchCodeTypes;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public List<IdType> getSpecifiedFishingTripIds() {
        return specifiedFishingTripIds;
    }

    public void setSpecifiedFishingTripIds(List<IdType> specifiedFishingTripIds) {
        this.specifiedFishingTripIds = specifiedFishingTripIds;
    }

    public List<String> getFaTypesPerTrip() {
        return faTypesPerTrip;
    }

    public void setFaTypesPerTrip(List<String> faTypesPerTrip) {
        this.faTypesPerTrip = faTypesPerTrip;
    }


}
