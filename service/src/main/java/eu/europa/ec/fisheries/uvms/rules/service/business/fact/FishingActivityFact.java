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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

@Data
@EqualsAndHashCode(callSuper = false)
public class FishingActivityFact extends AbstractFact {

    private boolean subActivity = false;
    private CodeType typeCode;
    private DateTimeType occurrenceDateTime;
    private FishingTrip specifiedFishingTrip;
    private List<FishingTrip> relatedFishingTrip;
    private List<FishingActivity> relatedFishingActivities;
    private List<CodeType> fluxCharacteristicsTypeCode;
    private CodeType reasonCode;
    private CodeType fisheryTypeCode;
    private CodeType speciesTargetCode;
    private BigDecimal operationQuantity;
    // This field will be set only when this is a subactivity;
    private String mainActivityType;
    private List<MeasureType> durationMeasure;
    private DelimitedPeriod delimitedPeriod;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<FLUXLocation> relatedActivityFluxLocations;
    private CodeType vesselRelatedActivityCode;
    private CodeType faReportDocumentTypeCode;
    private List<CodeType> relatedFluxLocationRFMOCodeList;
    private List<FACatch> specifiedFaCatch;

    private List<CodeType> relatedVesselTransportMeansRoleCodes;
    private List<CodeType> faRepDockSpecifiedVesselTransportMeansRoleCodes;

    public FishingActivityFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FISHING_ACTIVITY;
    }

    public boolean validDates() {
        if (!subActivity && (occurrenceDateTime != null || delimitedPeriod != null)) {
            return true;
        }
        return validDelimitedPeriod(relatedFishingActivities);
    }

    public boolean validDelimitedPeriod(List<FishingActivity> relatedFishingActivities) {
        Boolean isMatch = false;
        if (CollectionUtils.isEmpty(relatedFishingActivities)) {
            return false;
        }
        for (FishingActivity related : relatedFishingActivities) {
            isMatch = related.getOccurrenceDateTime() != null
                    || CollectionUtils.isNotEmpty(related.getSpecifiedDelimitedPeriods())
                    && validDelimitedPeriod(related.getSpecifiedDelimitedPeriods().get(0), true, true);
            if (!isMatch) {
                return false;
            }
        }
        return isMatch;
    }

    public boolean rfmoProvided(List<FLUXLocation> relatedFLUXLocations) {
        if (CollectionUtils.isEmpty(relatedFLUXLocations)) {
            return true;
        } else {
            for (FLUXLocation relatedFLUXLocation : relatedFLUXLocations) {
                un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType rfmo = relatedFLUXLocation.getRegionalFisheriesManagementOrganizationCode();
                if (rfmo != null) {
                    return true;
                }
            }

        }
        return false;
    }

    public boolean isAllowedToHaveSubactivities() {
        if (!subActivity && !isEmpty(relatedFishingActivities)) {
            if ((codeTypeValueEquals(faReportDocumentTypeCode, "DECLARATION") && (codeTypeValueEquals(typeCode, "FISHING_OPERATION") || codeTypeValueEquals(typeCode, "JOINT_FISHING_OPERATION")))
                    || codeTypeValueEquals(faReportDocumentTypeCode, "NOTIFICATION") && codeTypeValueEquals(typeCode, "AREA_ENTRY")) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * This will check if every FACatch in FishingActivity has at least one with typeCode value ALLOCATED_TO_QUOTA if FACatchSpecies is BFT
     *
     * @return TRUE : If every FishingActivity has FACatch with typeCode value ALLOCATED_TO_QUOTA
     * FALSE : If atleast one FishingActivity is without FACatch with typeCode value ALLOCATED_TO_QUOTA OR fishingActivityList is empty
     */
    public boolean atLeastOneFaCatchWithBFTAndAllocatedQuotaPresent() {
        if (CollectionUtils.isEmpty(specifiedFaCatch)) {
            return true;
        }
        for (FACatch faCatch : specifiedFaCatch) {
            if (faCatch.getSpeciesCode() != null && "BFT".equals(faCatch.getSpeciesCode().getValue()) && (faCatch.getTypeCode() == null || !"ALLOCATED_TO_QUOTA".equals(faCatch.getTypeCode().getValue()))) {
                return false;
            }
        }
        return true;
    }

    public boolean atMostOneVTMRoleCodeWithValue(String value){
        if(StringUtils.isEmpty(value)){
            return true;
        }
        List<CodeType> allRoleCodes = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(relatedVesselTransportMeansRoleCodes)){
            allRoleCodes = new ArrayList<>(relatedVesselTransportMeansRoleCodes);
        }
        if(CollectionUtils.isNotEmpty(faRepDockSpecifiedVesselTransportMeansRoleCodes)){
            allRoleCodes.addAll(faRepDockSpecifiedVesselTransportMeansRoleCodes);
        }
        return allRoleCodes.stream().filter(role -> role != null && value.equals(role.getValue())).count() > 1;
    }

    public boolean allDurationMeasuresHaveUnitCodeProvidedWhenValueIsProvided(){
        if(CollectionUtils.isEmpty(durationMeasure)){
            return true;
        }
        for (MeasureType durMeas : durationMeasure) {
            if ((durMeas.getValue() != null) && durMeas.getUnitCode() == null) {
                return false;
            }
        }
        return true;
    }

}
