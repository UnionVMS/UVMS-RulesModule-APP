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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FaNotificationOfArrivalFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;

    private CodeType faReportDocumentTypeCode;

    private List<FLUXLocation> relatedFLUXLocations;

    private List<CodeType> relatedFLUXLocationTypeCodes;

    private Date occurrenceDateTime;

    private List<DelimitedPeriod> delimitedPeriods;

    private CodeType reasonCode;

    private List<CodeType> specifiedFACatchTypeCodes;

    private List<FACatch> specifiedFACatches;

    private List<CodeType> specifiedFLUXCharacteristicsTypeCodes;

    private List<Date> specifiedFLUXCharacteristicValueDateTimes;

    public FaNotificationOfArrivalFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_NOTIFICATION_OF_ARRIVAL;
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

    public CodeType getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(CodeType reasonCode) {
        this.reasonCode = reasonCode;
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

    public List<CodeType> getSpecifiedFACatchTypeCodes() {
        return specifiedFACatchTypeCodes;
    }

    public void setSpecifiedFACatchTypeCodes(List<CodeType> specifiedFACatchTypeCodes) {
        this.specifiedFACatchTypeCodes = specifiedFACatchTypeCodes;
    }

    public List<FACatch> getSpecifiedFACatches() {
        return specifiedFACatches;
    }

    public void setSpecifiedFACatches(List<FACatch> specifiedFACatches) {
        this.specifiedFACatches = specifiedFACatches;
    }

    public List<DelimitedPeriod> getDelimitedPeriods() {
        return delimitedPeriods;
    }

    public void setDelimitedPeriods(List<DelimitedPeriod> delimitedPeriods) {
        this.delimitedPeriods = delimitedPeriods;
    }

    public List<CodeType> getSpecifiedFLUXCharacteristicsTypeCodes() {
        return specifiedFLUXCharacteristicsTypeCodes;
    }

    public void setSpecifiedFLUXCharacteristicsTypeCodes(List<CodeType> specifiedFLUXCharacteristicsTypeCodes) {
        this.specifiedFLUXCharacteristicsTypeCodes = specifiedFLUXCharacteristicsTypeCodes;
    }

    public List<Date> getSpecifiedFLUXCharacteristicValueDateTimes() {
        return specifiedFLUXCharacteristicValueDateTimes;
    }

    public void setSpecifiedFLUXCharacteristicValueDateTimes(List<Date> specifiedFLUXCharacteristicValueDateTimes) {
        this.specifiedFLUXCharacteristicValueDateTimes = specifiedFLUXCharacteristicValueDateTimes;
    }
}
