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
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import java.util.List;
import java.util.Map;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FaArrivalFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType faReportTypeCode;
    private CodeType faReportPurposeCode;
    private DateTimeType occurrenceDateTime;
    private CodeType reasonCode;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<CodeType> fluxLocationTypeCodes;
    private List<CodeType> fishingGearRoleCodes;
    private List<IdType> fishingTripIds;
    private Map<String, List<FishingActivityWithIdentifiers>> faTypesPerTrip;

    @Override
    public void setFactType() {
        this.factType = FactType.FA_ARRIVAL;
    }

    public CodeType getFishingActivityTypeCode() {
        return fishingActivityTypeCode;
    }
    public void setFishingActivityTypeCode(CodeType fishingActivityTypeCode) {
        this.fishingActivityTypeCode = fishingActivityTypeCode;
    }
    public CodeType getFaReportTypeCode() {
        return faReportTypeCode;
    }
    public void setFaReportTypeCode(CodeType faReportTypeCode) {
        this.faReportTypeCode = faReportTypeCode;
    }
    public DateTimeType getOccurrenceDateTime() {
        return occurrenceDateTime;
    }
    public void setOccurrenceDateTime(DateTimeType occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
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
    public List<CodeType> getFluxLocationTypeCodes() {
        return fluxLocationTypeCodes;
    }
    public void setFluxLocationTypeCodes(List<CodeType> fluxLocationTypeCodes) {
        this.fluxLocationTypeCodes = fluxLocationTypeCodes;
    }
    public List<CodeType> getFishingGearRoleCodes() {
        return fishingGearRoleCodes;
    }
    public void setFishingGearRoleCodes(List<CodeType> fishingGearRoleCodes) {
        this.fishingGearRoleCodes = fishingGearRoleCodes;
    }
    public List<IdType> getFishingTripIds() {
        return fishingTripIds;
    }
    public void setFishingTripIds(List<IdType> fishingTripIds) {
        this.fishingTripIds = fishingTripIds;
    }
    public Map<String, List<FishingActivityWithIdentifiers>> getFaTypesPerTrip() {
        return faTypesPerTrip;
    }
    public void setFaTypesPerTrip(Map<String, List<FishingActivityWithIdentifiers>> faTypesPerTrip) {
        this.faTypesPerTrip = faTypesPerTrip;
    }
    public CodeType getFaReportPurposeCode() {
        return faReportPurposeCode;
    }
    public void setFaReportPurposeCode(CodeType faReportPurposeCode) {
        this.faReportPurposeCode = faReportPurposeCode;
    }
}
