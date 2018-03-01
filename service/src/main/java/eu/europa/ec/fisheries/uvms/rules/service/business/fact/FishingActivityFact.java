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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;

import java.util.Date;
import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FishingActivityFact extends AbstractFact {

    private CodeType typeCode;

    private Date occurrenceDateTime;

    private boolean isDateProvided;

    private FishingTrip specifiedFishingTrip;

    private List<FishingTrip> relatedFishingTrip;

    private List<FishingActivity> relatedFishingActivities;

    private List<CodeType> fluxCharacteristicsTypeCode;

    private CodeType reasonCode;

    private CodeType fisheryTypeCode;

    private CodeType speciesTargetCode;

    private Integer operationQuantity;

    private boolean isSubActivity = false;

    private List<MeasureType> durationMeasure;

    private List<DelimitedPeriod> delimitedPeriods;

    private List<FLUXLocation> relatedFLUXLocations;

    private List<FLUXLocation> relatedActivityFluxLocations;

    private CodeType vesselRelatedActivityCode;

    private CodeType faReportDocumentTypeCode;

    private List<CodeType> relatedFishingActivityCodeTypes;

    private List<CodeType> relatedFluxLocationRFMOCodeList;

    public FishingActivityFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FISHING_ACTIVITY;
    }

    public boolean anyRelatedActivityIsMissingBothDates(){
        if(CollectionUtils.isEmpty(relatedFishingActivities)){
            return true;
        }
        for (FishingActivity relFishingActivity : relatedFishingActivities) {
            if(relFishingActivity.getOccurrenceDateTime() == null && validateDelimitedPeriod(relFishingActivity.getSpecifiedDelimitedPeriods(),true,true)){
                return true;
            }
        }
        return false;
    }

    public void setIsSubActivity(boolean isSubActivity) {
        this.isSubActivity = isSubActivity;
    }

    public boolean getIsSubActivity() {
        return isSubActivity;
    }

    public void setSubActivity(boolean subActivity) {

        isSubActivity = subActivity;
    }

    public List<FishingActivity> getRelatedFishingActivities() {
        return relatedFishingActivities;
    }

    public void setRelatedFishingActivities(List<FishingActivity> relatedFishingActivities) {
        this.relatedFishingActivities = relatedFishingActivities;
    }

    public List<DelimitedPeriod> getDelimitedPeriods() {
        return delimitedPeriods;
    }

    public void setDelimitedPeriods(List<DelimitedPeriod> delimitedPeriods) {
        this.delimitedPeriods = delimitedPeriods;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public CodeType getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(CodeType reasonCode) {
        this.reasonCode = reasonCode;
    }

    public CodeType getFisheryTypeCode() {
        return fisheryTypeCode;
    }

    public void setFisheryTypeCode(CodeType fisheryTypeCode) {
        this.fisheryTypeCode = fisheryTypeCode;
    }

    public CodeType getSpeciesTargetCode() {
        return speciesTargetCode;
    }

    public void setSpeciesTargetCode(CodeType speciesTargetCode) {
        this.speciesTargetCode = speciesTargetCode;
    }

    public Integer getOperationQuantity() {
        return operationQuantity;
    }

    public void setOperationQuantity(Integer operationQuantity) {
        this.operationQuantity = operationQuantity;
    }

    public FishingTrip getSpecifiedFishingTrip() {
        return specifiedFishingTrip;
    }

    public void setSpecifiedFishingTrip(FishingTrip specifiedFishingTrip) {
        this.specifiedFishingTrip = specifiedFishingTrip;
    }

    public List<CodeType> getFluxCharacteristicsTypeCode() {
        return fluxCharacteristicsTypeCode;
    }

    public void setFluxCharacteristicsTypeCode(List<CodeType> fluxCharacteristicsTypeCode) {
        this.fluxCharacteristicsTypeCode = fluxCharacteristicsTypeCode;
    }

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public Date getOccurrenceDateTime() {
        return occurrenceDateTime;
    }

    public void setOccurrenceDateTime(Date occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
    }

    public boolean getIsDateProvided() {
        return isDateProvided;
    }

    public void setIsDateProvided(boolean isDateProvided) {
        this.isDateProvided = isDateProvided;
    }

    public List<FishingTrip> getRelatedFishingTrip() {
        return relatedFishingTrip;
    }

    public void setRelatedFishingTrip(List<FishingTrip> relatedFishingTrip) {
        this.relatedFishingTrip = relatedFishingTrip;
    }

    public List<FLUXLocation> getRelatedActivityFluxLocations() {
        return relatedActivityFluxLocations;
    }

    public void setRelatedActivityFluxLocations(List<FLUXLocation> relatedActivityFluxLocations) {
        this.relatedActivityFluxLocations = relatedActivityFluxLocations;
    }

    public CodeType getVesselRelatedActivityCode() {
        return vesselRelatedActivityCode;
    }

    public void setVesselRelatedActivityCode(CodeType vesselRelatedActivityCode) {
        this.vesselRelatedActivityCode = vesselRelatedActivityCode;
    }

    public List<MeasureType> getDurationMeasure() {
        return durationMeasure;
    }

    public void setDurationMeasure(List<MeasureType> durationMeasure) {
        this.durationMeasure = durationMeasure;
    }

    public CodeType getFaReportDocumentTypeCode() {
        return faReportDocumentTypeCode;
    }

    public void setFaReportDocumentTypeCode(CodeType faReportDocumentTypeCode) {
        this.faReportDocumentTypeCode = faReportDocumentTypeCode;
    }

    public List<CodeType> getRelatedFishingActivityCodeTypes() {
        return relatedFishingActivityCodeTypes;
    }

    public void setRelatedFishingActivityCodeTypes(List<CodeType> relatedFishingActivityCodeTypes) {
        this.relatedFishingActivityCodeTypes = relatedFishingActivityCodeTypes;
    }

    public List<CodeType> getRelatedFluxLocationRFMOCodeList() {
        return relatedFluxLocationRFMOCodeList;
    }

    public void setRelatedFluxLocationRFMOCodeList(List<CodeType> relatedFluxLocationRFMOCodeList) {
        this.relatedFluxLocationRFMOCodeList = relatedFluxLocationRFMOCodeList;
    }
}
