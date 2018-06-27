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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

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
    private List<String> specifiedFishingActivitiesTypes;
    private List<IdType> relatedFLUXReportDocumentIDs;
    private IdType relatedFLUXReportDocumentReferencedID;
    private List<IdType> nonUniqueIdsList;
    private List<IdType> faSpecifiedFishingTripIds;
    private List<String> faTypesPerTrip;
    private Map<String, Integer> fishingActivitiesArrivalDeclarationList;
    private Map<String, Integer> fishingActivitiesDepartureDeclarationList;

    public boolean isValid(List<FishingActivity> specifiedFishingActivities){
        if (CollectionUtils.isEmpty(specifiedFishingActivities)){
            return false;
        }
        Set<DayMonthYearType> sameDays =  new HashSet<>();
        try {
            for (FishingActivity activity : specifiedFishingActivities)
                if (isOnSameDay(sameDays, activity)) {
                    return false;
                }
        }
        catch (Exception e){
            log.trace(e.getMessage(), e);
            return true;
        }
        return sameDays.size() != 1;
    }

    private boolean isOnSameDay(Set<DayMonthYearType> total, FishingActivity activity) {
        FAType activityTypeEnum = FAType.valueOf(activity.getTypeCode().getValue());
        if (!FAType.FISHING_OPERATION.equals(activityTypeEnum) && !FAType.JOINED_FISHING_OPERATION.equals(activityTypeEnum) && activity.getOccurrenceDateTime() != null){
            DayMonthYearType incomingDay = new DayMonthYearType(activity.getOccurrenceDateTime(), activityTypeEnum);
            if (total.contains(incomingDay)){
                return true;
            }
            total.add(incomingDay);
        }
        return false;
    }

    enum FAType {
        DEPARTURE, ARRIVAL, AREA_ENTRY, AREA_EXIT, FISHING_OPERATION, LANDING, TRANSHIPMENT, RELOCATION,
        GEAR_SHOT, GEAR_RETRIEVAL, START_FISHING, JOINED_FISHING_OPERATION, START_ACTIVITY, DISCARD
    }

    @Data
    @EqualsAndHashCode
    private class DayMonthYearType {
        private int day;
        private int month;
        private int year;
        private FAType type;

        DayMonthYearType(DateTimeType dateTimeType, FAType type){
            this.day = dateTimeType.getDateTime().getDay();
            this.month = dateTimeType.getDateTime().getMonth();
            this.year = dateTimeType.getDateTime().getYear();
            this.type = type;

        }
    }

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

    public FaReportDocumentFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_REPORT_DOCUMENT;
    }

}


