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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private CodeType purposeCode;
    private DateTimeType occurrenceDateTime;
    private FishingTrip specifiedFishingTrip;
    private List<FishingTrip> relatedFishingTrip;
    private List<FishingActivity> relatedFishingActivities;
    private List<CodeType> fluxCharacteristicsTypeCode;
    private CodeType reasonCode;
    private CodeType fisheryTypeCode;
    private CodeType speciesTargetCode;
    private BigDecimal operationQuantity;
    private List<FAReportDocument> faReportDocuments;
    private FAReportDocument faReportDocument;
    // This field will be set only when this is a subactivity;
    private String mainActivityType;
    private List<MeasureType> durationMeasure;
    private DelimitedPeriod delimitedPeriod;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<FLUXLocation> relatedActivityFluxLocations;
    private List<CodeType> relatedActivityFluxLocationCodes;
    private CodeType vesselRelatedActivityCode;
    private CodeType faReportDocumentTypeCode;
    private List<CodeType> relatedFluxLocationRFMOCodeList;
    private List<FACatch> specifiedFaCatch;
    private List<FishingGear> specifiedFishingGears;

    private List<CodeType> relatedVesselTransportMeansRoleCodes;
    private List<CodeType> relFishActRelatedVesselTransportMeansRoleCodes;
    private List<CodeType> faRepDockSpecifiedVesselTransportMeansRoleCodes;
    private List<FLUXCharacteristic>  fishingActivityFluxCharacteristic;
    private List<FLUXLocation> fishingActivityRelatedFLUXLocations;

    private FishingActivity thisFishingActivity;

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

    public boolean containsActivityTypeForSchema(List<FAReportDocument> faReportDocuments, String activityType, FAReportDocument faReportDocument,String documentType){

        if(faReportDocuments == null || faReportDocuments.isEmpty() || faReportDocument.getSpecifiedFishingActivities() == null
                || faReportDocument.getSpecifiedFishingActivities().get(0) == null
                || faReportDocument.getSpecifiedFishingActivities().get(0).getTypeCode() == null){
            return true;
        }
        List<FAReportDocument> filteredReports = faReportDocuments.stream().filter(f -> activityType.equals(f.getSpecifiedFishingActivities().get(0).getTypeCode().getValue()))
                .filter(f -> f.getTypeCode() != null && documentType.equals(f.getTypeCode().getValue()))
                .collect(Collectors.toList());

        //if there is only one activity type we are ok
        if(filteredReports.size() == 1 ){
            return true;
        }
        //if there is none, we want the rule to fail in first element of the list only
        if(filteredReports.size() == 0 && faReportDocuments.get(0).equals(faReportDocument)){
            return false;
        }

        //just ignore activities which are not of type activityType
        if(!activityType.equals(faReportDocument.getSpecifiedFishingActivities().get(0).getTypeCode().getValue()) || filteredReports.size() == 0){
            return true;
        }
        //if there are more than one results, we want the rule to fail for the activity which has different schema id
        return filteredReports.get(0).getRelatedFLUXReportDocument().getIDS().get(0).equals(faReportDocument.getRelatedFLUXReportDocument().getIDS().get(0));
    }

    public boolean validDelimitedPeriod(List<FishingActivity> relatedFishingActivities) {
        boolean isMatch = false;
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

    public boolean hasUniqueFluxCharacteristics(List<CodeType> fluxCharacteristicsTypeCode){
        if(fluxCharacteristicsTypeCode == null || fluxCharacteristicsTypeCode.isEmpty()){
            return true;
        }

        HashSet listToSet = new HashSet(fluxCharacteristicsTypeCode);
        return listToSet.size() == fluxCharacteristicsTypeCode.size();
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
            return (codeTypeValueEquals(faReportDocumentTypeCode, "DECLARATION") && (codeTypeValueEquals(typeCode, "FISHING_OPERATION") || codeTypeValueEquals(typeCode, "JOINT_FISHING_OPERATION")))
                    || codeTypeValueEquals(faReportDocumentTypeCode, "NOTIFICATION") && codeTypeValueEquals(typeCode, "AREA_ENTRY");
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


    public boolean hasPositionLocationForFaTypeCode(String fishingActivityTypeCode) {
        if(!isSubActivity() || org.apache.commons.lang3.StringUtils.isEmpty(fishingActivityTypeCode)){
            return true;
        }
        if(thisFishingActivity != null && thisFishingActivity.getTypeCode() != null && fishingActivityTypeCode.equals(thisFishingActivity.getTypeCode().getValue())){
            List<FLUXLocation> relatedFluxLocations = collectRelatedFluxLocations(thisFishingActivity);
            return anyFluxLocationTypeCodeContainsValue(relatedFluxLocations, "POSITION");
        }
        return true;
    }

    private List<FLUXLocation> collectRelatedFluxLocations(FishingActivity relatedFishingActivity) {
        List<FLUXLocation> nonNullFlucLocations = new ArrayList<>();
        if(relatedFishingActivity != null && CollectionUtils.isNotEmpty(relatedFishingActivity.getRelatedFLUXLocations())){
            nonNullFlucLocations = relatedFishingActivity.getRelatedFLUXLocations().stream().filter(Objects::nonNull).collect(Collectors.toList());
        }
        return nonNullFlucLocations;
    }
}
