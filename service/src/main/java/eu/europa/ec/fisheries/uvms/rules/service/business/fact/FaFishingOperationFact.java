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
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FaFishingOperationFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;

    private CodeType faReportDocumentTypeCode;

    private CodeType vesselRelatedActivityCode;

    private String operationsQuantity;

    private List<FLUXLocation> relatedFLUXLocations;

    private List<FishingActivity> relatedFishingActivities;

    private List<CodeType> fishingGearRoleCodes;

    private List<CodeType> relatedFishingActivityTypeCodes;

    private List<ContactParty> vesselTransportMeansContactParties;

    private List<VesselTransportMeans> vesselTransportMeans;

    public FaFishingOperationFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_FISHING_OPERATION;
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

    public CodeType getVesselRelatedActivityCode() {
        return vesselRelatedActivityCode;
    }

    public void setVesselRelatedActivityCode(CodeType vesselRelatedActivityCode) {
        this.vesselRelatedActivityCode = vesselRelatedActivityCode;
    }

    public String getOperationsQuantity() {
        return operationsQuantity;
    }

    public void setOperationsQuantity(String operationsQuantity) {
        this.operationsQuantity = operationsQuantity;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public List<FishingActivity> getRelatedFishingActivities() {
        return relatedFishingActivities;
    }

    public void setRelatedFishingActivities(List<FishingActivity> relatedFishingActivities) {
        this.relatedFishingActivities = relatedFishingActivities;
    }

    public List<CodeType> getRelatedFishingActivityTypeCodes() {
        return relatedFishingActivityTypeCodes;
    }

    public void setRelatedFishingActivityTypeCodes(List<CodeType> relatedFishingActivityTypeCodes) {
        this.relatedFishingActivityTypeCodes = relatedFishingActivityTypeCodes;
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

    public List<CodeType> getFishingGearRoleCodes() {
        return fishingGearRoleCodes;
    }

    public void setFishingGearRoleCodes(List<CodeType> fishingGearRoleCodes) {
        this.fishingGearRoleCodes = fishingGearRoleCodes;
    }

    public List<ContactParty> getVesselTransportMeansContactParties() {
        return vesselTransportMeansContactParties;
    }

    public void setVesselTransportMeansContactParties(List<ContactParty> vesselTransportMeansContactParties) {
        this.vesselTransportMeansContactParties = vesselTransportMeansContactParties;
    }

    // FA-L00-00-0079
    public boolean verifyContactPartyRule(List<VesselTransportMeans> vesselTransportMeans) {
        outerloop :
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

    public List<VesselTransportMeans> getVesselTransportMeans() {
        return vesselTransportMeans;
    }

    public void setVesselTransportMeans(List<VesselTransportMeans> vesselTransportMeans) {
        this.vesselTransportMeans = vesselTransportMeans;
    }
}
