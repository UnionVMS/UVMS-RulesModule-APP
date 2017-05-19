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

import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FishingActivityFact extends AbstractFact {

    private CodeType typeCode;

    private Date occurrenceDateTime;

    private FishingTrip specifiedFishingTrip;

    private List<FishingActivity> relatedFishingActivities;

    private List<FLUXCharacteristic> specifiedFLUXCharacteristics;

    private CodeType reasonCode;

    private CodeType fisheryTypeCode;

    private CodeType speciesTargetCode;

    private String operationQuantity;

    private boolean isSubActivity;

    private List<DelimitedPeriod> delimitedPeriods;

    private List<FLUXLocation> relatedFLUXLocations;

    public FishingActivityFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FISHING_ACTIVITY;
    }

    public void setIsSubActivity(boolean isSubActivity) {
        this.isSubActivity = isSubActivity;
    }

    public boolean isSubActivity() {
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

    public String getOperationQuantity() {
        return operationQuantity;
    }

    public void setOperationQuantity(String operationQuantity) {
        this.operationQuantity = operationQuantity;
    }

    public FishingTrip getSpecifiedFishingTrip() {
        return specifiedFishingTrip;
    }

    public void setSpecifiedFishingTrip(FishingTrip specifiedFishingTrip) {
        this.specifiedFishingTrip = specifiedFishingTrip;
    }

    public List<FLUXCharacteristic> getSpecifiedFLUXCharacteristics() {
        return specifiedFLUXCharacteristics;
    }

    public void setSpecifiedFLUXCharacteristics(List<FLUXCharacteristic> specifiedFLUXCharacteristics) {
        this.specifiedFLUXCharacteristics = specifiedFLUXCharacteristics;
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
}
