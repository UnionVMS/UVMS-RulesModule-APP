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
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

/**
 * Created by padhyad on 4/7/2017.
 */
@Slf4j
public class FaReportDocumentFact extends AbstractFact {

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
    private Map<String, List<FishingActivityWithIdentifiers>> faTypesPerTrip;
    // String = tripId, Integer = Arrival declarations for this trip ID
    private Map<String, Integer> fishingActivitiesArrivalDeclarationList;
    // String = tripId, Integer = Departure declarations for this trip ID
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


    public FaReportDocumentFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_REPORT_DOCUMENT;
    }

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public List<IdType> getRelatedReportIDs() {
        return relatedReportIDs;
    }

    public void setRelatedReportIDs(List<IdType> relatedReportIDs) {
        this.relatedReportIDs = relatedReportIDs;
    }

    public List<IdType> getIds() {
        return ids;
    }

    public void setIds(List<IdType> ids) {
        this.ids = ids;
    }

    public FLUXReportDocument getRelatedFLUXReportDocument() {
        return relatedFLUXReportDocument;
    }

    public void setRelatedFLUXReportDocument(FLUXReportDocument relatedFLUXReportDocument) {
        this.relatedFLUXReportDocument = relatedFLUXReportDocument;
    }

    public CodeType getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(CodeType purposeCode) {
        this.purposeCode = purposeCode;
    }

    public IdType getReferencedID() {
        return referencedID;
    }

    public void setReferencedID(IdType referencedID) {
        this.referencedID = referencedID;
    }

    public List<IdType> getOwnerFluxPartyIds() {
        return ownerFluxPartyIds;
    }

    public void setOwnerFluxPartyIds(List<IdType> ownerFluxPartyIds) {
        this.ownerFluxPartyIds = ownerFluxPartyIds;
    }

    public VesselTransportMeans getSpecifiedVesselTransportMeans() {
        return specifiedVesselTransportMeans;
    }

    public void setSpecifiedVesselTransportMeans(VesselTransportMeans specifiedVesselTransportMeans) {
        this.specifiedVesselTransportMeans = specifiedVesselTransportMeans;
    }

    public List<FishingActivity> getSpecifiedFishingActivities() {
        return specifiedFishingActivities;
    }

    public void setSpecifiedFishingActivities(List<FishingActivity> specifiedFishingActivities) {
        this.specifiedFishingActivities = specifiedFishingActivities;
    }

    public Date getAcceptanceDateTime() {
        return acceptanceDateTime;
    }

    public void setAcceptanceDateTime(Date acceptanceDateTime) {
        this.acceptanceDateTime = acceptanceDateTime;
    }

    public String getAcceptanceDateTimeString() {
        return acceptanceDateTimeString;
    }

    public void setAcceptanceDateTimeString(String acceptanceDateTimeString) {
        this.acceptanceDateTimeString = acceptanceDateTimeString;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCreationDateTimeString() {
        return creationDateTimeString;
    }

    public void setCreationDateTimeString(String creationDateTimeString) {
        this.creationDateTimeString = creationDateTimeString;
    }

    public List<IdType> getRelatedFLUXReportDocumentIDs() {
        return relatedFLUXReportDocumentIDs;
    }

    public void setRelatedFLUXReportDocumentIDs(List<IdType> relatedFLUXReportDocumentIDs) {
        this.relatedFLUXReportDocumentIDs = relatedFLUXReportDocumentIDs;
    }

    public IdType getRelatedFLUXReportDocumentReferencedID() {
        return relatedFLUXReportDocumentReferencedID;
    }

    public void setRelatedFLUXReportDocumentReferencedID(IdType relatedFLUXReportDocumentReferencedID) {
        this.relatedFLUXReportDocumentReferencedID = relatedFLUXReportDocumentReferencedID;
    }

    public List<IdType> getNonUniqueIdsList() {
        return nonUniqueIdsList;
    }

    public void setNonUniqueIdsList(List<IdType> nonUniqueIdsList) {
        this.nonUniqueIdsList = nonUniqueIdsList;
    }

    public List<Date> getSpecifiedAndRealtedFishActOccurrenceDateTimes() {
        return specifiedAndRealtedFishActOccurrenceDateTimes;
    }

    public void setSpecifiedAndRealtedFishActOccurrenceDateTimes(List<Date> specifiedAndRealtedFishActOccurrenceDateTimes) {
        this.specifiedAndRealtedFishActOccurrenceDateTimes = specifiedAndRealtedFishActOccurrenceDateTimes;
    }

    public List<String> getSpecifiedFishingActivitiesTypes() {
        return specifiedFishingActivitiesTypes;
    }

    public void setSpecifiedFishingActivitiesTypes(List<String> specifiedFishingActivitiesTypes) {
        this.specifiedFishingActivitiesTypes = specifiedFishingActivitiesTypes;
    }

    public List<IdType> getFaSpecifiedFishingTripIds() {
        return faSpecifiedFishingTripIds;
    }

    public void setFaSpecifiedFishingTripIds(List<IdType> faSpecifiedFishingTripIds) {
        this.faSpecifiedFishingTripIds = faSpecifiedFishingTripIds;
    }

    public void setFaTypesPerTrip(Map<String, List<FishingActivityWithIdentifiers>> faTypesPerTrip) {
        this.faTypesPerTrip = faTypesPerTrip;
    }

    public Map<String, List<FishingActivityWithIdentifiers>> getFaTypesPerTrip() {
        return faTypesPerTrip;
    }

    public void setFishingActivitiesArrivalDeclarationList(Map<String, Integer> fishingActivitiesArrivalDeclarationList) {
        this.fishingActivitiesArrivalDeclarationList = fishingActivitiesArrivalDeclarationList;
    }

    public Map<String, Integer> getFishingActivitiesDepartureDeclarationList() {
        return fishingActivitiesDepartureDeclarationList;
    }

    public void setFishingActivitiesDepartureDeclarationList(Map<String, Integer> fishingActivitiesDepartureDeclarationList) {
        this.fishingActivitiesDepartureDeclarationList = fishingActivitiesDepartureDeclarationList;
    }
}


