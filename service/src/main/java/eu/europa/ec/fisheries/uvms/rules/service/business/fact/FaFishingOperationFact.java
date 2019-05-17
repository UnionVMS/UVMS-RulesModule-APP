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
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@Data
public class FaFishingOperationFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType faReportDocumentTypeCode;
    private CodeType vesselRelatedActivityCode;
    private String operationsQuantity;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<FLUXLocation> relatedFishingActivitiesRelatedFLUXLocations;
    private List<IdType> relatedFLUXLocationIds;
    private List<FishingActivity> relatedFishingActivities;
    private List<CodeType> fishingGearRoleCodes;
    private List<CodeType> relatedFishingActivityTypeCodes;
    private List<ContactParty> vesselTransportMeansContactParties;
    private List<VesselTransportMeans> vesselTransportMeans;
    private List<CodeType> vesselTransportMeansRoleCodes;
    private List<FACatch> specifiedFACatches;
    private List<CodeType> specifiedFACatchesTypeCodes;
    private CodeType faReportDocVesselRoleCode;
    private List<DelimitedPeriod> specifiedDelimitedPeriods;

    public FaFishingOperationFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_FISHING_OPERATION;
    }


    public boolean isFLUXLocationPresentForFishingActivity(List<FishingActivity> relatedFishingActivities) {
        if (CollectionUtils.isEmpty(relatedFishingActivities)) {
            return false;
        }
        for (FishingActivity fishingActivity : relatedFishingActivities) {
            if (CollectionUtils.isEmpty(fishingActivity.getRelatedFLUXLocations())) {
                return false;
            }
        }
        return true;
    }

    public boolean noCatchesDeclaredForCodeTypes(String... codeValues) {
        AtomicBoolean noCatchesDeclared = new AtomicBoolean(true);
        if (CollectionUtils.isNotEmpty(relatedFishingActivities)) {
            List<String> codeValuesToMatch = Arrays.asList(codeValues);
            relatedFishingActivities.forEach((relatedFishAct) -> {
                un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = relatedFishAct.getTypeCode();
                String codeVal = typeCode != null ? typeCode.getValue() : StringUtils.EMPTY;
                codeValuesToMatch.forEach((valToMatch) -> {
                    if (valToMatch.equals(codeVal) && CollectionUtils.isNotEmpty(relatedFishAct.getSpecifiedFACatches())) {
                        noCatchesDeclared.set(false);
                    }
                });
            });
        }
        return noCatchesDeclared.get();

    }

    // FA-L00-00-0079
    public boolean verifyContactPartyRule(List<VesselTransportMeans> vesselTransportMeans) {
        outerloop:
        for (VesselTransportMeans vesselTransportMean : vesselTransportMeans) {
            final un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType roleCode = vesselTransportMean.getRoleCode();
            if (roleCode != null && StringUtils.equals(roleCode.getValue(), "PAIR_FISHING_PARTNER")) {
                // Condition1. PAIR_FISHING_PARTNER so at least one of the SpecifiedContactParties must have a SpecifiedStructuredAddress;
                final List<ContactParty> specifiedContactParties = vesselTransportMean.getSpecifiedContactParties();
                if (CollectionUtils.isEmpty(specifiedContactParties)) {
                    return true;
                }
                for (ContactParty specifiedContactParty : specifiedContactParties) {
                    if (CollectionUtils.isNotEmpty(specifiedContactParty.getSpecifiedStructuredAddresses())) {
                        // Means that for this vesselTransportMean a non empty SpecifiedStructuredAddress was found.
                        continue outerloop;
                    }
                }
                // If we arrived here means that Condition1 wasn't met so the rule fails;
                return true;
            }
        }
        return false;
    }

    public boolean allDelimitedPeriodsHaveDurationMeasures(List<DelimitedPeriod> delimitedPeriods) {
        if (CollectionUtils.isNotEmpty(delimitedPeriods)) {
            for (DelimitedPeriod delimitedPeriod : delimitedPeriods) {
                if (delimitedPeriod.getDurationMeasure() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean faoIdsHaveThreeSubdivisions(List<IdType> ids){
        String devisionDevider = ".";
        for (IdType id : ids) {
            if(id != null && "FAO_AREA".equals(id.getSchemeId())){
                String value = id.getValue() != null ? id.getValue() : StringUtils.EMPTY;
                int lastIndex = 0;
                int count = 0;
                while(lastIndex != -1){
                    lastIndex = value.indexOf(devisionDevider,lastIndex);
                    if(lastIndex != -1){
                        count ++;
                        lastIndex += devisionDevider.length();
                    }
                }
                System.out.println(count);
                if(count < 2){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkRelatedFishingActivitiesRelatedFluxLocationsTypeCodes(String fishingActivityTypeCode){
        if(StringUtils.isEmpty(fishingActivityTypeCode)){
            return true;
        }
        for (FishingActivity relatedFishingActivity : relatedFishingActivities) {
            if(relatedFishingActivity != null && relatedFishingActivity.getTypeCode() != null && fishingActivityTypeCode.equals(relatedFishingActivity.getTypeCode().getValue())){
                List<FLUXLocation> relatedFluxLocations = collectRelatedFluxLocations(relatedFishingActivity);
                if(!anyFluxLocationTypeCodeContainsValue(relatedFluxLocations, "POSITION")){
                    return false;
                }
            }
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
