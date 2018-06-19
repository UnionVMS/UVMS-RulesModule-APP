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

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FaJointFishingOperationFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;

    private List<IdType> fishingActivityIds;

    private CodeType faReportDocumentTypeCode;

    private List<FLUXLocation> relatedFLUXLocations;

    private List<CodeType> fluxLocationTypeCode;

    private CodeType faReportDocVesselRoleCode;

    private List<FishingActivity> relatedFishingActivities;

    private List<CodeType> relatedFishingActivityTypeCode;

    private List<CodeType> relatedFishingActivityFaCatchTypeCodes;

    private List<FACatch> relatedFishingActivityFaCatch;

    public List<String> getSpecifiedFACatchSpeciesCodes(){
        List<String> results = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(relatedFishingActivityFaCatch)){
            for (FACatch fishingActivityFaCatch : relatedFishingActivityFaCatch) {
                un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType speciesCode = fishingActivityFaCatch.getSpeciesCode();
                if (speciesCode != null){
                    results.add(speciesCode.getValue());
                }
            }
        }
        return results;
    }

    public FaJointFishingOperationFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_JOINT_FISHING_OPERATION;
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

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public List<CodeType> getFluxLocationTypeCode() {
        return fluxLocationTypeCode;
    }

    public void setFluxLocationTypeCode(List<CodeType> fluxLocationTypeCode) {
        this.fluxLocationTypeCode = fluxLocationTypeCode;
    }

    public List<CodeType> getRelatedFishingActivityTypeCode() {
        return relatedFishingActivityTypeCode;
    }

    public void setRelatedFishingActivityTypeCode(List<CodeType> relatedFishingActivityTypeCode) {
        this.relatedFishingActivityTypeCode = relatedFishingActivityTypeCode;
    }

    public List<FishingActivity> getRelatedFishingActivities() {
        return relatedFishingActivities;
    }

    public void setRelatedFishingActivities(List<FishingActivity> relatedFishingActivities) {
        this.relatedFishingActivities = relatedFishingActivities;
    }

    public CodeType getFaReportDocVesselRoleCode() {
        return faReportDocVesselRoleCode;
    }

    public void setFaReportDocVesselRoleCode(CodeType faReportDocVesselRoleCode) {
        this.faReportDocVesselRoleCode = faReportDocVesselRoleCode;
    }

    public List<CodeType> getRelatedFishingActivityFaCatchTypeCodes() {
        return relatedFishingActivityFaCatchTypeCodes;
    }

    public void setRelatedFishingActivityFaCatchTypeCodes(List<CodeType> relatedFishingActivityFaCatchTypeCodes) {
        this.relatedFishingActivityFaCatchTypeCodes = relatedFishingActivityFaCatchTypeCodes;
    }

    public List<FACatch> getRelatedFishingActivityFaCatch() {
        return relatedFishingActivityFaCatch;
    }

    public void setRelatedFishingActivityFaCatch(List<FACatch> relatedFishingActivityFaCatch) {
        this.relatedFishingActivityFaCatch = relatedFishingActivityFaCatch;
    }

    public List<IdType> getFishingActivityIds() {
        return fishingActivityIds;
    }

    public void setFishingActivityIds(List<IdType> fishingActivityIds) {
        this.fishingActivityIds = fishingActivityIds;
    }

    /**
     * This will check if every FishingActivity has atlease one VesselTransportMeans associated with it.
     *
     * @return TRUE : If every FishingActivity has VesselTransport
     * FALSE : If atleast one FishingActivity is without VesselTransport OR fishingActivityList is empty
     */
    public boolean ifVesselTransportPresent(List<FishingActivity> fishingActivityList) {

        if (CollectionUtils.isEmpty(fishingActivityList)) {
            return false;
        }

        for (FishingActivity fishingActivity : fishingActivityList) {
            if (CollectionUtils.isEmpty(fishingActivity.getRelatedVesselTransportMeans())) {
                return false;
            }
        }

        return true;
    }

    /**
     * This will check if every FishingActivity has atlease one FACatch associated with it.
     *
     * @return TRUE : If every FishingActivity has FACatch
     * FALSE : If atleast one FishingActivity is without FACatch OR fishingActivityList is empty
     */
    public boolean ifFACatchPresent(List<FishingActivity> fishingActivityList) {

        if (CollectionUtils.isEmpty(fishingActivityList)) {
            return false;
        }

        for (FishingActivity fishingActivity : fishingActivityList) {
            if (CollectionUtils.isEmpty(fishingActivity.getSpecifiedFACatches())) {
                return false;
            }
        }

        return true;
    }

    /**
     * This will check if every FACatch in FishingActivity has atleast one with typeCode value ALLOCATED_TO_QUOTA if FACatchSpecies is BFT
     *
     * @return TRUE : If every FishingActivity has FACatch with typeCode value ALLOCATED_TO_QUOTA
     * FALSE : If atleast one FishingActivity is without FACatch with typeCode value ALLOCATED_TO_QUOTA OR fishingActivityList is empty
     */
    public boolean verifyAtLeastOneFaCatchTypeCodePresent(List<FishingActivity> fishingActivityList) {
        if (CollectionUtils.isEmpty(fishingActivityList)) {
            return true;
        }
        for (FishingActivity fishingActivity : fishingActivityList) {
            if (CollectionUtils.isNotEmpty(fishingActivity.getSpecifiedFACatches())) {
                for (FACatch faCatch : fishingActivity.getSpecifiedFACatches()) {
                    if (faCatch.getSpeciesCode()!=null && "BFT".equals(faCatch.getSpeciesCode().getValue()) && (faCatch.getTypeCode() == null || !"ALLOCATED_TO_QUOTA".equals(faCatch.getTypeCode().getValue()))) {
                       return false;
                    }
                }
            }
        }
        return true;
    }

}
