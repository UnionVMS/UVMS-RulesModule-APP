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
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

@Data
@EqualsAndHashCode(callSuper = false)
public class FishingActivityFact extends AbstractFact {

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
    private boolean subActivity = false;
    private List<MeasureType> durationMeasure;
    private DelimitedPeriod delimitedPeriod;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<FLUXLocation> relatedActivityFluxLocations;
    private CodeType vesselRelatedActivityCode;
    private CodeType faReportDocumentTypeCode;
    private List<CodeType> relatedFluxLocationRFMOCodeList;

    public FishingActivityFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FISHING_ACTIVITY;
    }

    public boolean validDates(){
        if (!subActivity && (occurrenceDateTime != null || delimitedPeriod != null)) {
            return true;
        }
        return validDelimitedPeriod(relatedFishingActivities);
    }

    public boolean validDelimitedPeriod(List<FishingActivity> relatedFishingActivities){
        Boolean isMatch = false;
        if (CollectionUtils.isEmpty(relatedFishingActivities)){
            return false;
        }
        for (FishingActivity related : relatedFishingActivities) {
            isMatch = related.getOccurrenceDateTime() != null
            || CollectionUtils.isNotEmpty(related.getSpecifiedDelimitedPeriods())
            && validDelimitedPeriod(related.getSpecifiedDelimitedPeriods().get(0), true, true);
            if(!isMatch){
                return false;
            }
        }
        return isMatch;
    }

    public boolean rfmoProvided( List<FLUXLocation> relatedFLUXLocations){
        if (CollectionUtils.isEmpty(relatedFLUXLocations)){
            return true;
        }
        else {
            for (FLUXLocation relatedFLUXLocation : relatedFLUXLocations) {
                un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType rfmo = relatedFLUXLocation.getRegionalFisheriesManagementOrganizationCode();
                if (rfmo != null){
                    return true;
                }
            }

        }
        return false;
    }


}
