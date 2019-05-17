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

import java.util.*;
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
    private List<IdType> faReportMessageOwnerFluxPartyIds;
    private VesselTransportMeans specifiedVesselTransportMeans;
    private List<FishingActivity> specifiedFishingActivities;
    private List<String> specifiedFishingActivitiesTypes;
    private List<IdType> relatedFLUXReportDocumentIDs;
    private List<IdType> nonUniqueIdsList;
    private List<IdType> faSpecifiedFishingTripIds;
    private List<String> faTypesPerTrip;
    private Map<String, Integer> fishingActivitiesArrivalDeclarationList;
    private Map<String, Integer> fishingActivitiesDepartureDeclarationList;
    private CodeType fmcMarkerCode;

    private Map<FishingActivityType, List<String>> tripsPerFaTypeFromMessage = new EnumMap<>(FishingActivityType.class);
    private Map<FishingActivityType, List<String>> tripsPerFaTypeFromThisReport = new EnumMap<>(FishingActivityType.class);

    public FaReportDocumentFact() {
        setFactType();
    }

    public boolean isValid(List<FishingActivity> specifiedFishingActivities){
        if (CollectionUtils.isEmpty(specifiedFishingActivities)){
            return false;
        }
        Set<String> notFishingOps =  new HashSet<>();
        Set<DayMonthYearType> fishingOps =  new HashSet<>();
        try {
            for (FishingActivity activity : specifiedFishingActivities) {
                String value = activity.getTypeCode().getValue();
                if (("FISHING_OPERATION".equals(value) || "JOINED_FISHING_OPERATION".equals(value))){
                    DateTimeType occurrence = findOccurrence(activity);
                    if (occurrence != null){
                        DayMonthYearType operation = new DayMonthYearType(occurrence);
                        if (CollectionUtils.isNotEmpty(fishingOps) && !fishingOps.contains(operation)) {
                            return false;
                        }
                        fishingOps.add(operation);
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(notFishingOps) && notFishingOps.contains(value)) {
                        return false;
                    }
                    notFishingOps.add(value);
                }
            }
        }
        catch (Exception e){
            log.trace(e.getMessage(), e);
            return true;
        }
        return true;
    }

    private DateTimeType findOccurrence(FishingActivity activity) {
        DateTimeType occurrence = activity.getOccurrenceDateTime();
        if (occurrence == null){
            List<DateTimeType> occurrences = new ArrayList<>();
            List<FishingActivity> relatedFishingActivities = activity.getRelatedFishingActivities();
            for (FishingActivity relatedFishingActivity : relatedFishingActivities) {
                occurrences.add(relatedFishingActivity.getOccurrenceDateTime());
            }
            occurrence = Collections.min(occurrences, new DateTimeTypeComparator());
        }
        return occurrence;
    }

    @Data
    @EqualsAndHashCode
    private class DayMonthYearType {
        private int day;
        private int month;
        private int year;

        DayMonthYearType(DateTimeType dateTimeType){
            this.day = dateTimeType.getDateTime().getDay();
            this.month = dateTimeType.getDateTime().getMonth();
            this.year = dateTimeType.getDateTime().getYear();
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

    public boolean containsMoreThenOneArrivalOrDepartureInFaReportsOfTheMessage(FishingActivityType type) {
        List<String> declaredArrivalInMessageList = new ArrayList<>();
        List<String> declaredDepartureInMessageList = new ArrayList<>();
        List<String> declaredArrivalInFaReportList = new ArrayList<>();
        List<String> declaredDepartureInFaReportList = new ArrayList<>();
        if (FishingActivityType.ARRIVAL.equals(type)) {
            declaredArrivalInMessageList = tripsPerFaTypeFromMessage.get(FishingActivityType.ARRIVAL);
            declaredArrivalInFaReportList = tripsPerFaTypeFromThisReport.get(FishingActivityType.ARRIVAL);
        } else if (FishingActivityType.DEPARTURE.equals(type)) {
            declaredDepartureInMessageList = tripsPerFaTypeFromMessage.get(FishingActivityType.DEPARTURE);
            declaredDepartureInFaReportList = tripsPerFaTypeFromThisReport.get(FishingActivityType.DEPARTURE);
        }
        if (CollectionUtils.isNotEmpty(declaredArrivalInFaReportList)){
            for (String s : declaredArrivalInFaReportList) {
                if (Collections.frequency(declaredArrivalInMessageList, s) >= 2){
                    return true;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(declaredDepartureInFaReportList)){
            for (String s : declaredDepartureInFaReportList) {
                if (Collections.frequency(declaredDepartureInMessageList, s) >= 2){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsMoreThenOneDeclarationPerTrip(List<IdType> specifiedFishingTripIds, Map<String, List<FishingActivityWithIdentifiers>> faTypesPerTrip, FishingActivityType faType) {
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
            messageTripIds.add(faSpecifiedFishingTripId.getValue() + DASH + faSpecifiedFishingTripId.getSchemeId() + DASH + faType.toString() + DASH + "DECLARATION");
        }

        List<String> strings = tripsPerFaTypeFromThisReport.get(faType);

        if (CollectionUtils.isNotEmpty(strings)) {
            for (String tripId : strings) {
                if (messageTripIds.contains(tripId + DASH + "EU_TRIP_ID" + DASH + faType.toString() + DASH + "DECLARATION")){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_REPORT_DOCUMENT;
    }

}


