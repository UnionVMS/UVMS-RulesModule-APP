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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class FaReportDocumentFact extends AbstractFact {

    private static final String DASH = "-";

    private CodeType typeCode;
    private List<IdType> relatedReportIDs;
    private Date acceptanceDateTime;
    private String acceptanceDateTimeString;
    private List<IdType> ids;
    private FLUXReportDocument relatedFLUXReportDocument;
    private CodeType purposeCode;
    private IdType referencedID;
    private Date creationDateTime;
    private String creationDateTimeString;
    private List<IdType> ownerFluxPartyIds;
    private VesselTransportMeans specifiedVesselTransportMeans;
    private List<FishingActivity> specifiedFishingActivities;
    private List<Date> specifiedAndRealtedFishActOccurrenceDateTimes;
    private List<String> specifiedFishingActivitiesTypes;
    private List<IdType> relatedFLUXReportDocumentIDs;
    private IdType relatedFLUXReportDocumentReferencedID;
    private List<IdType> nonUniqueIdsList;
    private List<IdType> faSpecifiedFishingTripIds;
    private List<String> faTypesPerTrip;
    private Map<String, Integer> fishingActivitiesArrivalDeclarationList;
    private Map<String, Integer> fishingActivitiesDepartureDeclarationList;

    public boolean containsMoreThenOneArrivalOrDeparture(FishingActivityType type) {
        Map<String, Integer> declarationList = new HashMap<>();
        if (FishingActivityType.ARRIVAL.equals(type)) {
            declarationList = fishingActivitiesArrivalDeclarationList;
        } else if (FishingActivityType.DEPARTURE.equals(type)) {
            declarationList = fishingActivitiesDepartureDeclarationList;
        }
        if (MapUtils.isNotEmpty(declarationList)) {
            for (Map.Entry<String, Integer> entry : declarationList.entrySet()) {
                if (entry.getValue() > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsMoreThenOneDeclarationPerTrip(List<IdType> specifiedFishingTripIds,
                                                         Map<String, List<FishingActivityWithIdentifiers>> faTypesPerTrip,
                                                         FishingActivityType faType) {
        if (MapUtils.isEmpty(faTypesPerTrip) || CollectionUtils.isEmpty(specifiedFishingTripIds) || faType == null) {
            return false;
        }
        for (IdType idType : specifiedFishingTripIds) {
            List<FishingActivityWithIdentifiers> fishingActivityWithIdentifiers = faTypesPerTrip.get(idType.getValue());
            if (fishingActivityWithIdentifiers != null) {
                return true;
            }
        }
        return false;
    }

    public boolean containsMoreThenOneDeclarationPerTrip(List<String> faTypesPerTripFromDb, FishingActivityType faType) {
        if (CollectionUtils.isEmpty(faTypesPerTripFromDb) || CollectionUtils.isEmpty(faSpecifiedFishingTripIds) || faType == null) {
            return false;
        }
        List<String> messageTripIds = new ArrayList<>();
        for (IdType faSpecifiedFishingTripId : faSpecifiedFishingTripIds) {
            messageTripIds.add(faSpecifiedFishingTripId.getValue() +DASH+ faSpecifiedFishingTripId.getSchemeId() +DASH+ faType.toString() +DASH+ "DECLARATION");
        }
        for (String tripId : messageTripIds) {
            if(faTypesPerTripFromDb.contains(tripId)){
                return true;
            }
        }
        return false;
    }


    public boolean containsSameDayMoreTheOnce(List<Date> dateList) {
        if (CollectionUtils.isEmpty(dateList)) {
            return false;
        }
        int listSize = dateList.size();
        for (int i = 0; i < listSize; i++) {
            Date comparisonDate = dateList.get(i);
            for (int j = i + 1; j < listSize; j++) {
                if (isSameDay(comparisonDate, dateList.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSameDay(Date date1, Date date2) {
        return DateUtils.isSameDay(date1, date2);
    }

    public FaReportDocumentFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_REPORT_DOCUMENT;
    }

}


