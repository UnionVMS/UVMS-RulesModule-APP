/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/

package eu.europa.ec.fisheries.uvms.rules.service.mapper.fact;

import java.math.BigDecimal;
import java.util.*;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.date.XMLDateUtils;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MessageType;
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FaReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.QuantityType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

@Slf4j
public class ActivityFactMapper {

    private static final String FLUX_LOCATION_TYPE_CODE_PROP = "fluxLocationTypeCode";
    private static final String RELATED_FISHING_ACTIVITIES_PROP = "relatedFishingActivities";
    private static final String POST_OFFICE_BOX = "PostOfficeBox";
    private static final String FISHING_ACTIVITY_TYPE_CODE = "fishingActivityTypeCode";
    private static final String CREATION_DATE_TIME = "creationDateTime";
    private List<VesselTransportMeansDto> transportMeans;
    private XPathStringWrapper xPathUtil;
    private List<IdType> faReportMessageIds = new ArrayList<>();
    private List<IdType> faRelatedReportIds = new ArrayList<>();
    private List<IdType> faQueryIds = new ArrayList<>();
    private List<IdType> faResponseIds = new ArrayList<>();
    private String senderReceiver = null;
    private List<String> fishingActivitiesWithTripIds = new ArrayList<>();

    private static final String VALUE_PROP = "value";
    private static final String VALUE_INDICATOR_PROP = "valueIndicator";
    private static final String VALUE_MEASURE_PROP = "valueMeasure";
    private static final String VALUE_CODE_PROP = "valueCode";
    private static final String VALUE_QUANTITY_PROP = "valueQuantity";
    private static final String VALUE_DATE_TIME_PROP = "valueDateTime";
    private static final String REFERENCED_ID_PROP = "referencedID";
    private static final String SPECIFIED_VESSEL_TRANSPORT_MEANS_PROP = "specifiedVesselTransportMeans";
    private static final String RELATED_FLUX_REPORT_DOCUMENT_PROP = "relatedFLUXReportDocument";
    private static final String PURPOSE_CODE_PROP = "purposeCode";
    private static final String RELATED_FLUX_REPORT_DOCUMENT_I_DS_PROP = "relatedFLUXReportDocumentIDs";
    private static final String RELATED_FLUX_REPORT_DOCUMENT_REFERENCED_ID_PROP = "relatedFLUXReportDocumentReferencedID";
    private static final String UNIQUE_IDS_PROP = "uniqueIds";
    private static final String OWNER_FLUX_PARTY_IDS = "ownerFluxPartyIds";
    private static final String OWNER_FLUX_PARTY_IDS_PROP = OWNER_FLUX_PARTY_IDS;
    private static final String IDS_PROP = "ids";
    private static final String ACCEPTANCE_DATE_TIME_PROP = "acceptanceDateTime";
    private static final String VESSEL_RELATED_ACTIVITY_CODE_PROP = "vesselRelatedActivityCode";
    private static final String RELATED_FLUX_LOCATION_RFMO_CODE_LIST_PROP = "relatedFluxLocationRFMOCodeList";
    private static final String FISHING_ACTIVITY_TYPE_CODE_PROP = "fishingActivityTypeCode";
    private static final String FA_QUERY_TYPE_CODE_PROP = "faQueryTypeCode";
    private static final String VALUE_ID_PROP = "valueID";
    private static final String ID_PROP = "id";
    private static final String SUBMITTED_DATE_TIME_PROP = "submittedDateTime";
    private static final String SUBMITTED_FLUX_PARTY_IDS_PROP = "submittedFLUXPartyIds";
    private static final String SPECIFIED_DELIMITED_PERIOD_PROP = "specifiedDelimitedPeriod";
    private static final String SIMPLE_FA_QUERY_PARAMETER_TYPE_CODES_PROP = "simpleFAQueryParameterTypeCodes";
    private static final String FISHING_GEAR_ROLE_CODES_PROP = "fishingGearRoleCodes";
    private static final String SPECIFIED_FA_CATCHES_PROP = "specifiedFACatches";
    private static final String SPECIFIED_FA_CATCHES = "specifiedFACatches";
    private static final String RELATED_VESSEL_TRANSPORT_MEANS_PROP = "relatedVesselTransportMeans";
    private static final String DESTINATION_VESSEL_STORAGE_CHARACTERISTIC_PROP = "destinationVesselStorageCharacteristic";
    private static final String SOURCE_VESSEL_STORAGE_CHARACTERISTIC_PROP = "sourceVesselStorageCharacteristic";
    private static final String RELATED_REPORT_IDS_PROP = "relatedReportIDs";
    private static final String TYPE_CODE_PROP = "typeCode";
    private static final String ROLE_CODES_PROP = "roleCodes";
    private static final String APPLICABLE_GEAR_CHARACTERISTICS_PROP = "applicableGearCharacteristics";
    private static final String AFFECTED_QUANTITY_PROP = "affectedQuantity";
    private static final String SPECIFIED_FLUX_LOCATION_PROP = "specifiedFluxLocations";
    private static final String RECOVERY_MEASURE_CODE_PROP = "recoveryMeasureCodes";
    private static final String REASON_CODE_PROP = "reasonCode";
    private static final String FA_REPORT_DOCUMENT_TYPE_CODE = "faReportDocumentTypeCode";
    private static final String FA_REPORT_DOCUMENT_TYPE_CODE_PROP = FA_REPORT_DOCUMENT_TYPE_CODE;
    private static final String RELATED_FLUX_LOCATIONS_PROP = "relatedFLUXLocations";
    private static final String FISHERY_TYPE_CODE_PROP = "fisheryTypeCode";
    private static final String SPECIES_TARGET_CODE_PROP = "speciesTargetCode";
    private static final String SPECIFIED_FISHING_TRIP_PROP = "specifiedFishingTrip";
    private static final String OCCURRENCE_DATE_TIME_PROP = "occurrenceDateTime";
    private static final String CODE_TYPE_FOR_FACATCH_FLUXLOCATION = "facatchFluxlocationTypeCode";
    private static final String CODE_TYPE_FOR_FACATCH_PROP = "facatchTypeCode";
    private static final String SPECIES_CODE_FOR_FACATCH_PROP = "facatchSpeciesCode";
    private static final String SPECIFIED_FA_CATCHES_TYPE_CODE_PROP = "specifiedFACatchesTypeCodes";
    private static final String RELATED_FLUX_LOCATIONS_TYPE_CODE_PROP = "relatedFluxLocationTypeCodes";
    private static final String RELATED_FLUX_LOCATIONS_ID_PROP = "relatedFluxLocationIDs";

    public ActivityFactMapper() {
        setxPathUtil(new XPathStringWrapper());
    }

    public ActivityFactMapper(XPathStringWrapper strUtil1) {
        setxPathUtil(strUtil1);
    }

    public FaReportDocumentFact generateFactForFaReportDocument(FAReportDocument faReportDocument) {
        if (faReportDocument == null) {
            xPathUtil.clear();
            return null;
        }

        String partialXpath = xPathUtil.getValue();

        FaReportDocumentFact faReportDocumentFact = new FaReportDocumentFact();

        faReportDocumentFact.setSenderOrReceiver(senderReceiver);

        String s = dateTimeAsString(faReportDocumentsRelatedFLUXReportDocumentCreationDateTime(faReportDocument));
        faReportDocumentFact.setCreationDateTimeString(s);

        Date date = getDate(faReportDocumentsRelatedFLUXReportDocumentCreationDateTime(faReportDocument));
        faReportDocumentFact.setCreationDateTime(date);

        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, XPathConstants.CREATION_DATE_TIME).storeInRepo(faReportDocumentFact, CREATION_DATE_TIME);

        faReportDocumentFact.setAcceptanceDateTime(getDate(faReportDocument.getAcceptanceDateTime()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(ACCEPTANCE_DATE_TIME).storeInRepo(faReportDocumentFact, ACCEPTANCE_DATE_TIME_PROP);

        faReportDocumentFact.setAcceptanceDateTimeString(getDateXMLString(faReportDocument.getAcceptanceDateTime()));

        faReportDocumentFact.setIds(mapToIdType(faReportDocumentsRelatedFLUXReportDocumentIDS(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, ID).storeInRepo(faReportDocumentFact, IDS_PROP);

        faReportDocumentFact.setOwnerFluxPartyIds(mapToIdType(faReportDocumentsRelatedFLUXReportDocumentOwnerFLUXPartyIDS(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, OWNER_FLUX_PARTY, ID).storeInRepo(faReportDocumentFact, OWNER_FLUX_PARTY_IDS_PROP);

        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, ID).storeInRepo(faReportDocumentFact, UNIQUE_IDS_PROP);

        faReportDocumentFact.setReferencedID(mapToIdType(faReportDocumentsRelatedFLUXReportDocumentReferencedID(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, REFERENCED_ID).storeInRepo(faReportDocumentFact, RELATED_FLUX_REPORT_DOCUMENT_REFERENCED_ID_PROP);

        faReportDocumentFact.setRelatedFLUXReportDocumentIDs(mapToIdType(faReportDocumentsRelatedFLUXReportDocumentIDS(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, ID).storeInRepo(faReportDocumentFact, RELATED_FLUX_REPORT_DOCUMENT_I_DS_PROP);

        faReportDocumentFact.setPurposeCode(mapToCodeType(faReportDocumentsRelatedFLUXReportDocumentPurposeCode(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, PURPOSE_CODE).storeInRepo(faReportDocumentFact, PURPOSE_CODE_PROP);

        faReportDocumentFact.setTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faReportDocumentFact, TYPE_CODE_PROP);

        faReportDocumentFact.setRelatedReportIDs(mapToIdType(faReportDocument.getRelatedReportIDs()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_REPORT_ID).storeInRepo(faReportDocumentFact, RELATED_REPORT_IDS_PROP);

        faReportDocumentFact.setRelatedFLUXReportDocument(faReportDocument.getRelatedFLUXReportDocument());
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT).storeInRepo(faReportDocumentFact, RELATED_FLUX_REPORT_DOCUMENT_PROP);

        faReportDocumentFact.setSpecifiedVesselTransportMeans(faReportDocument.getSpecifiedVesselTransportMeans());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_VESSEL_TRANSPORT_MEANS).storeInRepo(faReportDocumentFact, SPECIFIED_VESSEL_TRANSPORT_MEANS_PROP);

        faReportDocumentFact.setReferencedID(referencedIdFromRelatedFLUXReportDocument(faReportDocument));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, REFERENCED_ID).storeInRepo(faReportDocumentFact, REFERENCED_ID_PROP);

        List<FishingActivity> specifiedFishingActivities = faReportDocument.getSpecifiedFishingActivities();
        if (CollectionUtils.isNotEmpty(specifiedFishingActivities)) {
            faReportDocumentFact.setSpecifiedFishingActivities(new ArrayList<>(specifiedFishingActivities));
            faReportDocumentFact.setSpecifiedFishingActivitiesTypes(mapFishingActivityTypes(specifiedFishingActivities));
            // Added for checking that only one DECLARATION of DEPARTURE/ARRIVAL exists in xml and Activity (FA-L03-00-0306, FA-L03-00-0241)
            List<IdType> faSpecifiedFishingTripIds = new ArrayList<>();
            for (FishingActivity specFishAct : specifiedFishingActivities) {
                faSpecifiedFishingTripIds.addAll(mapToIdType(fishingActivitySpecifiedFishingTripIDS(specFishAct, faReportDocument)));
            }
            faReportDocumentFact.setFaSpecifiedFishingTripIds(faSpecifiedFishingTripIds);
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_ACTIVITY, SPECIFIED_FISHING_TRIP, ID).storeInRepo(faReportDocumentFact, "faSpecifiedFishingTripIds");

            faReportDocumentFact.setFaTypesPerTrip(fishingActivitiesWithTripIds);
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_ACTIVITY, TYPE_CODE).storeInRepo(faReportDocumentFact, "faTypesPerTrip");

        }
        // Even if specifiedFishingActivities is empty we still need to map the xpath, cause those properties have rules being applied to them,
        // and if the rule fails (ex. cause of the property being empty or null) then we still need to return the xpath to what failed.
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_ACTIVITY).storeInRepo(faReportDocumentFact, "specifiedFishingActivities");
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_ACTIVITY, TYPE_CODE).storeInRepo(faReportDocumentFact, "specifiedFishingActivitiesTypes");
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_ACTIVITY, OCCURRENCE_DATE_TIME).storeInRepo(faReportDocumentFact, "specifiedAndRealtedFishActOccurrenceDateTimes");

        faReportDocumentFact.setNonUniqueIdsList(faRelatedReportIds);
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, ID).storeInRepo(faReportDocumentFact, "nonUniqueIdsList");

        return faReportDocumentFact;
    }

    private List<String> mapFishingActivityTypes(List<FishingActivity> specifiedFishingActivities) {
        List<String> actTypesList = new ArrayList<>();
        if (CollectionUtils.isEmpty(specifiedFishingActivities)) {
            return actTypesList;
        }
        for (FishingActivity fishAct : specifiedFishingActivities) {
            un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = fishAct.getTypeCode();
            if (typeCode != null) {
                actTypesList.add(typeCode.getValue());
            }
        }
        return actTypesList;
    }

    private IdType referencedIdFromRelatedFLUXReportDocument(FAReportDocument faReportDocument) {
        FLUXReportDocument relatedFLUXReportDocument = faReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument != null && relatedFLUXReportDocument.getReferencedID() != null) {
            return mapToIdType(relatedFLUXReportDocument.getReferencedID());
        }
        return null;
    }

    public List<FaReportDocumentFact> generateFactForFaReportDocuments(List<FAReportDocument> faReportDocuments, MessageType messageType) {
        if (faReportDocuments == null) {
            return emptyList();
        }
        int index = 1;
        List<FaReportDocumentFact> list = new ArrayList<>();
        Map<FishingActivityType, List<String>> tripsPerFaTypeFromMessage = collectTripsPerFaTypeFromMessage(faReportDocuments);
        for (FAReportDocument fAReportDocument : faReportDocuments) {
            xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
            FaReportDocumentFact faReportDocumentFact = generateFactForFaReportDocument(fAReportDocument);
            faReportDocumentFact.setMessageType(messageType);
            if(fAReportDocument.getRelatedFLUXReportDocument() != null){
                faReportDocumentFact.setCreationDateOfMessage(mapToJodaDateTime(fAReportDocument.getRelatedFLUXReportDocument().getCreationDateTime()));
            }
            faReportDocumentFact.setTripsPerFaTypeFromMessage(tripsPerFaTypeFromMessage);
            list.add(faReportDocumentFact);
            index++;
        }
        return list;
    }

    private Map<FishingActivityType, List<String>> collectTripsPerFaTypeFromMessage(List<FAReportDocument> faReportDocuments) {
        HashMap<FishingActivityType, List<String>> tripsPerFaTypeFromFasInReports = new HashMap<>();
        tripsPerFaTypeFromFasInReports.put(FishingActivityType.ARRIVAL, new ArrayList<>());
        tripsPerFaTypeFromFasInReports.put(FishingActivityType.DEPARTURE, new ArrayList<>());
        if (CollectionUtils.isEmpty(faReportDocuments)){
            return tripsPerFaTypeFromFasInReports;
        }
        for (FAReportDocument faReportDocument : faReportDocuments) {
            if(CollectionUtils.isNotEmpty(faReportDocument.getSpecifiedFishingActivities())){
                for (FishingActivity fishingActivity : faReportDocument.getSpecifiedFishingActivities()) {
                    FishingActivityType activityType = fetchActivityType(fishingActivity.getTypeCode());
                    FishingTrip specifiedFishingTrip = fishingActivity.getSpecifiedFishingTrip();
                    if(specifiedFishingTrip != null && CollectionUtils.isNotEmpty(specifiedFishingTrip.getIDS()) &&
                            (FishingActivityType.DEPARTURE.equals(activityType) || FishingActivityType.ARRIVAL.equals(activityType))){
                        tripsPerFaTypeFromFasInReports.get(activityType).add(specifiedFishingTrip.getIDS().get(0).getValue());
                    }
                }
            }
        }
        return tripsPerFaTypeFromFasInReports;
    }

    private FishingActivityType fetchActivityType(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode) {
        FishingActivityType faType = null;
        if(typeCode != null && StringUtils.isNotEmpty(typeCode.getValue())){
            try {
                faType = FishingActivityType.valueOf(typeCode.getValue());
            } catch(IllegalArgumentException ex){
                log.error(ex.getMessage());
            }
        }
        return faType;
    }

    public FishingActivityFact generateFishingActivityFact(FishingActivity fishingActivity, boolean isSubActivity,
                                                           un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType faReportType,
                                                           un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType mainActivityType) {
        FishingActivityFact fishingActivityFact = new FishingActivityFact();
        fishingActivityFact.setSubActivity(isSubActivity);
        fishingActivityFact.setFaReportDocumentTypeCode(mapToCodeType(faReportType));

        String partialXpath = xPathUtil.getValue();

        if (CollectionUtils.isNotEmpty(fishingActivity.getSpecifiedDelimitedPeriods())) {
            fishingActivityFact.setDelimitedPeriod(fishingActivity.getSpecifiedDelimitedPeriods().get(0));
        }
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_DELIMITED_PERIOD, START_DATE).storeInRepo(fishingActivityFact, "delimitedPeriodStartTime");
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_DELIMITED_PERIOD, END_DATE).storeInRepo(fishingActivityFact, "delimitedPeriodEndTime");

        if(mainActivityType != null){
            fishingActivityFact.setMainActivityType(mainActivityType.getValue());
            fishingActivityFact.setSpecifiedFaCatch(fishingActivity.getSpecifiedFACatches());
        }
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(fishingActivityFact, "specifiedFaCatch");

        fishingActivityFact.setRelatedFishingTrip(mapRelatedFishingTrips(fishingActivity.getRelatedFishingActivities()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, SPECIFIED_FISHING_TRIP).storeInRepo(fishingActivityFact, "relatedFishingTrip");

        fishingActivityFact.setDurationMeasure(mapDurationMeasure(fishingActivity.getSpecifiedDelimitedPeriods()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_DELIMITED_PERIOD, DURATION_MEASURE).storeInRepo(fishingActivityFact, "durationMeasure");

        BigDecimal operatQuantity = fishingActivityOperationsQuantityValue(fishingActivity);
        fishingActivityFact.setOperationQuantity(operatQuantity);
        xPathUtil.appendWithoutWrapping(partialXpath).append(OPERATIONS_QUANTITY).storeInRepo(fishingActivityFact, "operationQuantity");

        fishingActivityFact.setRelatedActivityFluxLocations(getFluxLocations(fishingActivity.getRelatedFishingActivities()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, RELATED_FLUX_LOCATION).storeInRepo(fishingActivityFact, "relatedActivityFluxLocations");

        fishingActivityFact.setFluxCharacteristicsTypeCode(getApplicableFLUXCharacteristicsTypeCode(fishingActivity.getSpecifiedFLUXCharacteristics()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, TYPE_CODE).storeInRepo(fishingActivityFact, "fluxCharacteristicsTypeCode");

        fishingActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY).storeInRepo(fishingActivityFact, RELATED_FISHING_ACTIVITIES_PROP);

        fishingActivityFact.setRelatedFLUXLocations(fishingActivity.getRelatedFLUXLocations());
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(fishingActivityFact, RELATED_FLUX_LOCATIONS_PROP);

        fishingActivityFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(REASON_CODE).storeInRepo(fishingActivityFact, REASON_CODE_PROP);

        fishingActivityFact.setFisheryTypeCode(mapToCodeType(fishingActivity.getFisheryTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(FISHERY_TYPE_CODE).storeInRepo(fishingActivityFact, FISHERY_TYPE_CODE_PROP);

        fishingActivityFact.setSpeciesTargetCode(mapToCodeType(fishingActivity.getSpeciesTargetCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIES_TARGET_CODE).storeInRepo(fishingActivityFact, SPECIES_TARGET_CODE_PROP);

        fishingActivityFact.setSpecifiedFishingTrip(fishingActivity.getSpecifiedFishingTrip());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_TRIP).storeInRepo(fishingActivityFact, SPECIFIED_FISHING_TRIP_PROP);

        fishingActivityFact.setTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fishingActivityFact, TYPE_CODE_PROP);

        fishingActivityFact.setOccurrenceDateTime(fishingActivity.getOccurrenceDateTime());
        xPathUtil.appendWithoutWrapping(partialXpath).append(OCCURRENCE_DATE_TIME).storeInRepo(fishingActivityFact, OCCURRENCE_DATE_TIME_PROP);

        fishingActivityFact.setVesselRelatedActivityCode(mapToCodeType(fishingActivity.getVesselRelatedActivityCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(VESSEL_RELATED_ACTIVITY_CODE).storeInRepo(fishingActivityFact, VESSEL_RELATED_ACTIVITY_CODE_PROP);

        fishingActivityFact.setRelatedFluxLocationRFMOCodeList(getFLUXLocationRFMOCodes(fishingActivity.getRelatedFLUXLocations()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, REGIONAL_FISHERIES_MANAGEMENT_ORGANIZATION_CODE).storeInRepo(fishingActivityFact, RELATED_FLUX_LOCATION_RFMO_CODE_LIST_PROP);

        return fishingActivityFact;
    }

    public DateTime mapToJodaDateTime(DateTimeType creationDateTime) {
        DateTime dateTimeOfCreationOfMessage = null;
        if (creationDateTime != null) {
            Date repDat = XMLDateUtils.xmlGregorianCalendarToDate(creationDateTime.getDateTime());
            if (repDat != null) {
                dateTimeOfCreationOfMessage = new DateTime(repDat);
            } else if (creationDateTime.getDateTimeString() != null && org.apache.commons.lang3.StringUtils.isNotEmpty(creationDateTime.getDateTimeString().getValue())) {
                try {
                    Date parsedDate = DateUtils.parseToUTCDate(creationDateTime.getDateTimeString().getValue(),creationDateTime.getDateTimeString().getFormat());
                    dateTimeOfCreationOfMessage = new DateTime(parsedDate);
                } catch (IllegalArgumentException e) {
                    log.warn("[WARN] Couldn't extract date from CreationDateTime of Message!", e);
                }
            }
        }
        return dateTimeOfCreationOfMessage;
    }

    public FluxFaReportMessageFact generateFactForFluxFaReportMessage(FLUXFAReportMessage fluxfaReportMessage) {
        if (fluxfaReportMessage == null) {
            return null;
        }

        FluxFaReportMessageFact fluxFaReportMessageFact = new FluxFaReportMessageFact();

        fluxFaReportMessageFact.setSenderOrReceiver(senderReceiver);

        if(fluxfaReportMessage.getFLUXReportDocument() != null){
            fluxFaReportMessageFact.setCreationDateOfMessage(mapToJodaDateTime(fluxfaReportMessage.getFLUXReportDocument().getCreationDateTime()));
        }

        String partialXpath = xPathUtil.append(FLUXFA_REPORT_MESSAGE).getValue();

        Date date = getDate(fluxfaReportMessageFLUXReportDocumentCreationDateTime(fluxfaReportMessage));

        String dateXMLString = getDateXMLString(fluxfaReportMessageFLUXReportDocumentCreationDateTime(fluxfaReportMessage));
        fluxFaReportMessageFact.setCreationDateTimeString(dateXMLString);

        fluxFaReportMessageFact.setCreationDateTime(date);
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, XPathConstants.CREATION_DATE_TIME).storeInRepo(fluxFaReportMessageFact, CREATION_DATE_TIME);

        if (fluxfaReportMessage.getFAReportDocuments() != null) {
            fluxFaReportMessageFact.setFaReportDocuments(new ArrayList<>(fluxfaReportMessage.getFAReportDocuments()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(FA_REPORT_DOCUMENT).storeInRepo(fluxFaReportMessageFact, "faReportDocuments");
        }

        fluxFaReportMessageFact.setIds(mapToIdType(fluxfaReportMessageFLUXReportDocumentIDS(fluxfaReportMessage)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, ID).storeInRepo(fluxFaReportMessageFact, "ids");

        fluxFaReportMessageFact.setReferencedID(mapToIdType(fluxfaReportMessageFLUXReportDocumentReferencedID(fluxfaReportMessage)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, REFERENCED_ID).storeInRepo(fluxFaReportMessageFact, REFERENCED_ID_PROP);

        fluxFaReportMessageFact.setOwnerFluxPartyIds(mapToIdType(fluxfaReportMessageFLUXReportDocumentOwnerFLUXPartyIDS(fluxfaReportMessage)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, OWNER_FLUX_PARTY, ID).storeInRepo(fluxFaReportMessageFact, OWNER_FLUX_PARTY_IDS);

        fluxFaReportMessageFact.setUniqueIds(getIds(fluxfaReportMessage.getFLUXReportDocument()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, ID).storeInRepo(fluxFaReportMessageFact, "uniqueIds");

        fluxFaReportMessageFact.setPurposeCode(mapToCodeType(fluxfaReportMessageFLUXReportDocumentPurposeCode(fluxfaReportMessage)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, PURPOSE_CODE).storeInRepo(fluxFaReportMessageFact, PURPOSE_CODE_PROP);

        fluxFaReportMessageFact.setNonUniqueIdsList(faReportMessageIds);
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, ID).storeInRepo(fluxFaReportMessageFact, "nonUniqueIdsList");

        fluxFaReportMessageFact.setRelatedFaQueryIDs(faQueryIds);
        xPathUtil.appendWithoutWrapping(partialXpath).append(REFERENCED_ID).storeInRepo(fluxFaReportMessageFact, "relatedFaQueryIDs");

        return fluxFaReportMessageFact;
    }

    public static List<String> getIds(FLUXReportDocument fluxReportDocument) {
        if (fluxReportDocument == null) {
            return emptyList();
        }
        List<IDType> idTypes = fluxReportDocument.getIDS();
        List<String> ids = new ArrayList<>();
        for (IDType idType : idTypes) {
            String value = idType.getValue();
            String schemeID = idType.getSchemeID();
            if (value != null && schemeID != null) {
                ids.add(value.concat("_").concat(schemeID));
            }
        }
        return ids;
    }

    public VesselTransportMeansFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean, boolean isCommingFromFaReportDocument, List<AbstractFact> facts) {
        if (vesselTransportMean == null) {
            xPathUtil.clear();
            return null;
        }
        VesselTransportMeansFact vesselTransportMeansFact = generateFactForVesselTransportMean(vesselTransportMean, facts);
        vesselTransportMeansFact.setIsFromFaReport(isCommingFromFaReportDocument);
        return vesselTransportMeansFact;
    }

    public List<VesselTransportMeansFact> generateFactForVesselTransportMeans(List<VesselTransportMeans> vesselTransportMeans, List<AbstractFact> facts) {
        if (CollectionUtils.isEmpty(vesselTransportMeans)) {
            xPathUtil.clear();
            return emptyList();
        }
        List<VesselTransportMeansFact> list = new ArrayList<>();
        int index = 1;
        String strToAppend = xPathUtil.getValue();
        for (VesselTransportMeans vesselTransportMean : vesselTransportMeans) {
            xPathUtil.appendWithoutWrapping(strToAppend).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, index);
            list.add(generateFactForVesselTransportMean(vesselTransportMean, facts));
            index++;
        }
        return list;
    }

    public VesselTransportMeansFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean, List<AbstractFact> facts) {
        if (vesselTransportMean == null) {
            xPathUtil.clear();
            return null;
        }

        // Since every time we get the final value (Eg. when storing in repo) we clean the StringBuffer inside XPathStringWrapper, we need to store and use the initial
        // value which is to be used always (appended as the first string in the buffer).
        String toBeAppendedAlways = xPathUtil.getValue();

        // Generate facts for ID Types. This is done so that we can have the right xPath to the specific ID that failed!
        if(CollectionUtils.isNotEmpty(vesselTransportMean.getIDS())){
            generateFactsForSimpleIdTypes(facts, vesselTransportMean.getIDS(), toBeAppendedAlways);
        }

        VesselTransportMeansFact vesselTransportMeansFact = new VesselTransportMeansFact();

        List<ContactParty> specifiedContactParties = vesselTransportMean.getSpecifiedContactParties();

        vesselTransportMeansFact.setRegistrationVesselCountryId(mapToIdType(vesselTransportMeanRegistrationVesselCountryID(vesselTransportMean)));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(REGISTRATION_VESSEL_COUNTRY, ID).storeInRepo(vesselTransportMeansFact, "registrationVesselCountryId");

        vesselTransportMeansFact.setSpecifiedContactPersons(mapToContactPersonList(specifiedContactParties));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(SPECIFIED_CONTACT_PARTY, SPECIFIED_CONTACT_PERSON).storeInRepo(vesselTransportMeansFact, "specifiedContactPersons");

        vesselTransportMeansFact.setIds(mapToIdType(vesselTransportMean.getIDS()));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(ID).storeInRepo(vesselTransportMeansFact, "ids");

        vesselTransportMeansFact.setSpecifiedContactPartyRoleCodes(mapFromContactPartyToCodeType(specifiedContactParties));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(SPECIFIED_CONTACT_PARTY, ROLE_CODE).storeInRepo(vesselTransportMeansFact, "specifiedContactPartyRoleCodes");

        vesselTransportMeansFact.setRoleCode(mapToCodeType(vesselTransportMean.getRoleCode()));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(ROLE_CODE).storeInRepo(vesselTransportMeansFact, "roleCode");

        if (specifiedContactParties != null) {
            vesselTransportMeansFact.setSpecifiedContactParties(new ArrayList<>(specifiedContactParties));
        }
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(SPECIFIED_CONTACT_PARTY).storeInRepo(vesselTransportMeansFact, "specifiedContactParties");

        vesselTransportMeansFact.setSpecifiedStructuredAddresses(mapSpecifiedStructuredAddresses(specifiedContactParties));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(SPECIFIED_CONTACT_PARTY, SPECIFIED_STRUCTURED_ADDRESS).storeInRepo(vesselTransportMeansFact, "specifiedStructuredAddresses");

        return vesselTransportMeansFact;
    }

    private void generateFactsForSimpleIdTypes(List<AbstractFact> facts, List<IDType> ids, String partialXpath) {
        if(CollectionUtils.isEmpty(ids)){
            return;
        }
        int index = 1;
        for (IDType id : ids) {
            IdTypeFact idTypeFact = generateFactForSimpleIdType(id);
            xPathUtil.append(partialXpath).appendWithIndex(ID, index).storeInRepo(idTypeFact, "id");
            facts.add(idTypeFact);
            index++;
        }
    }

    private IdTypeFact generateFactForSimpleIdType(IDType id) {
        IdTypeFact idTypeFact = new IdTypeFact();
        idTypeFact.setId(mapToIdType(id));
        return idTypeFact;
    }

    private List<StructuredAddress> mapSpecifiedStructuredAddresses(List<ContactParty> specifiedContactParties) {
        List<StructuredAddress> structAdrList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(specifiedContactParties)) {
            for (ContactParty contParty : specifiedContactParties) {
                structAdrList.addAll(contParty.getSpecifiedStructuredAddresses());
            }
        }
        structAdrList.removeAll(singleton(null));
        return structAdrList;
    }

    public StructuredAddressFact generateFactsForStructureAddress(StructuredAddress structuredAddress) {
        if (structuredAddress == null) {
            xPathUtil.clear();
            return null;
        }

        String partialXpath = xPathUtil.getValue();

        StructuredAddressFact structuredAddressFact = new StructuredAddressFact();

        structuredAddressFact.setPostalArea(getValueFromTextType(structuredAddress.getPostalArea()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(POSTAL_AREA).storeInRepo(structuredAddressFact, "postalArea");

        structuredAddressFact.setCountryID(structuredAddressCountryIDValue(structuredAddress));
        xPathUtil.appendWithoutWrapping(partialXpath).append(COUNTRY_ID).storeInRepo(structuredAddressFact, "countryID");

        structuredAddressFact.setCityName(getValueFromTextType(structuredAddress.getCityName()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(CITY_NAME).storeInRepo(structuredAddressFact, "cityName");

        structuredAddressFact.setStreetName(getValueFromTextType(structuredAddress.getStreetName()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(STREET_NAME).storeInRepo(structuredAddressFact, "streetName");

        structuredAddressFact.setPlotIdentification(getValueFromTextType(structuredAddress.getPlotIdentification()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(PLOT_IDENTIFICATION).storeInRepo(structuredAddressFact, "plotIdentification");

        structuredAddressFact.setPostOfficeBox(getValueFromTextType(structuredAddress.getPostOfficeBox()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(POST_OFFICE_BOX).storeInRepo(structuredAddressFact, "postOfficeBox");

        return structuredAddressFact;
    }

    public List<StructuredAddressFact> generateFactsForStructureAddresses(List<StructuredAddress> structuredAddresses, String adressType) {
        if (CollectionUtils.isEmpty(structuredAddresses)) {
            xPathUtil.clear();
            return emptyList();
        }
        String partialXpath = xPathUtil.getValue();
        List<StructuredAddressFact> list = new ArrayList<>();
        int index = 1;
        for (StructuredAddress structuredAddress : structuredAddresses) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(adressType, index);
            list.add(generateFactsForStructureAddress(structuredAddress));
            index++;
        }
        return list;
    }

    public FishingGearFact generateFactsForFishingGear(FishingGear fishingGear, String gearType) {
        if (fishingGear == null) {
            xPathUtil.clear();
            return null;
        }

        FishingGearFact fishingGearFact = new FishingGearFact();
        if (SPECIFIED_FISHING_GEAR.equals(gearType)) {
            fishingGearFact.setFishingActivity(true);
        }

        final String partialXpath = xPathUtil.getValue();

        fishingGearFact.setTypeCode(mapToCodeType(fishingGear.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fishingGearFact, TYPE_CODE_PROP);

        fishingGearFact.setRoleCodes(mapToCodeTypes(fishingGear.getRoleCodes()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(ROLE_CODE).storeInRepo(fishingGearFact, ROLE_CODES_PROP);

        fishingGearFact.setFishingGear(fishingGear);
        if (fishingGear.getApplicableGearCharacteristics() != null) {
            fishingGearFact.setApplicableGearCharacteristics(fishingGear.getApplicableGearCharacteristics());
            xPathUtil.appendWithoutWrapping(partialXpath).append(APPLICABLE_GEAR_CHARACTERISTIC).storeInRepo(fishingGearFact, APPLICABLE_GEAR_CHARACTERISTICS_PROP);
        }

        return fishingGearFact;
    }

    public List<FishingGearFact> generateFactsForFishingGears(List<FishingGear> fishingGears, String gearType, Map<String, List<GearMatrix.Condition>> matrix) {
        if (fishingGears == null) {
            xPathUtil.clear();
            return emptyList();
        }
        final String partialXpath = xPathUtil.getValue();
        List<FishingGearFact> list = new ArrayList<>();
        int index = 1;
        for (FishingGear fishingGear : fishingGears) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(gearType, index);
            FishingGearFact fishingGearFact = generateFactsForFishingGear(fishingGear, gearType);
            fishingGearFact.setMatrix(matrix);
            list.add(fishingGearFact);
            index++;
        }

        return list;
    }

    public GearCharacteristicsFact generateFactsForGearCharacteristic(GearCharacteristic gearCharacteristic) {
        if (gearCharacteristic == null) {
            return null;
        }

        final String partialXpath = xPathUtil.getValue();

        GearCharacteristicsFact gearCharacteristicsFact = new GearCharacteristicsFact();

        gearCharacteristicsFact.setTypeCode(mapToCodeType(gearCharacteristic.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(gearCharacteristicsFact, TYPE_CODE_PROP);

        gearCharacteristicsFact.setValue(gearCharacteristic.getValue());
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE).storeInRepo(gearCharacteristicsFact, VALUE_PROP);

        gearCharacteristicsFact.setValueIndicator(gearCharacteristic.getValueIndicator());
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_INDICATOR).storeInRepo(gearCharacteristicsFact, VALUE_INDICATOR_PROP);

        gearCharacteristicsFact.setValueMeasure(mapToMeasureType(gearCharacteristic.getValueMeasure()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_MEASURE).storeInRepo(gearCharacteristicsFact, VALUE_MEASURE_PROP);

        gearCharacteristicsFact.setValueCode(mapToCodeType(gearCharacteristic.getValueCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_CODE).storeInRepo(gearCharacteristicsFact, VALUE_CODE_PROP);

        gearCharacteristicsFact.setValueQuantity(mapToMeasureType(gearCharacteristic.getValueQuantity()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_QUANTITY).storeInRepo(gearCharacteristicsFact, VALUE_QUANTITY_PROP);

        return gearCharacteristicsFact;
    }

    public List<GearCharacteristicsFact> generateFactsForGearCharacteristics(List<GearCharacteristic> gearCharacteristics, String gearType) {
        if (gearCharacteristics == null) {
            return emptyList();
        }

        String partialXpath = xPathUtil.getValue();

        List<GearCharacteristicsFact> list = new ArrayList<>();
        int index = 1;
        for (GearCharacteristic gearCharacteristic : gearCharacteristics) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(gearType, index);
            list.add(generateFactsForGearCharacteristic(gearCharacteristic));
            index++;
        }

        return list;
    }

    public GearProblemFact generateFactsForGearProblem(GearProblem gearProblem) {
        if (gearProblem == null) {
            xPathUtil.clear();
            return null;
        }

        final String partialXpath = xPathUtil.getValue();
        GearProblemFact gearProblemFact = new GearProblemFact();

        gearProblemFact.setTypeCode(mapToCodeType(gearProblem.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(gearProblemFact, TYPE_CODE_PROP);

        if (gearProblem.getRecoveryMeasureCodes() != null) {
            gearProblemFact.setRecoveryMeasureCodes(mapToCodeTypes(gearProblem.getRecoveryMeasureCodes()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RECOVERY_MEASURE_CODE).storeInRepo(gearProblemFact, RECOVERY_MEASURE_CODE_PROP);
        }

        gearProblemFact.setAffectedQuantity(gearProblem.getAffectedQuantity());
        xPathUtil.appendWithoutWrapping(partialXpath).append(AFFECTED_QUANTITY).storeInRepo(gearProblemFact, AFFECTED_QUANTITY_PROP);

        if (gearProblem.getSpecifiedFLUXLocations() != null) {
            gearProblemFact.setSpecifiedFluxLocations(gearProblem.getSpecifiedFLUXLocations());
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_LOCATION).storeInRepo(gearProblemFact, SPECIFIED_FLUX_LOCATION_PROP);
        }

        return gearProblemFact;
    }

    public List<GearProblemFact> generateFactsForGearProblems(List<GearProblem> gearProblems) {
        if (gearProblems == null) {
            xPathUtil.clear();
            return emptyList();
        }
        final String partialXpath = xPathUtil.getValue();
        List<GearProblemFact> list = new ArrayList<>();
        int index = 1;
        for (GearProblem gearProblem : gearProblems) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_GEAR_PROBLEM, index);
            list.add(generateFactsForGearProblem(gearProblem));
            index++;
        }

        return list;
    }

    public List<FaCatchFact> generateFactsForFaCatch(FishingActivity activity, boolean isSubActivity, un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode) {

        if (activity == null) {
            return emptyList();
        }

        List<FACatch> faCatches = activity.getSpecifiedFACatches();
        List<FLUXLocation> fishActRelatedFluxLocations = activity.getRelatedFLUXLocations();
        List<FaCatchFact> facts = new ArrayList<>();

        if (CollectionUtils.isEmpty(faCatches) && fishActRelatedFluxLocations == null) {
            xPathUtil.clear();
            return emptyList();
        }

        String partialXPath1 = xPathUtil.getValue();
        int index = 1;

        if (CollectionUtils.isNotEmpty(faCatches)) {
            for (FACatch faCatch : faCatches) {

                String partialXPath = partialXPath1;
                FaCatchFact faCatchFact = new FaCatchFact();

                faCatchFact.setFishingActivityTypeCode(mapToCodeType(activity.getTypeCode()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(TYPE_CODE).storeInRepo(faCatchFact, FISHING_ACTIVITY_TYPE_CODE);

                faCatchFact.setFaReportDocumentTypeCode(mapToCodeType(typeCode));
                xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT).storeInRepo(faCatchFact, FA_REPORT_DOCUMENT_TYPE_CODE);

                partialXPath = xPathUtil.appendWithoutWrapping(partialXPath1).appendWithIndex(SPECIFIED_FA_CATCH, index).getValue();

                faCatchFact.setAppliedAAPProcess(faCatch.getAppliedAAPProcesses());
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS).storeInRepo(faCatchFact, "appliedAAPProcess");

                faCatchFact.setResultAAPProduct(getAppliedProcessAAPProducts(faCatch.getAppliedAAPProcesses()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT).storeInRepo(faCatchFact, "resultAAPProduct");


                faCatchFact.setDestinationFLUXLocations(faCatch.getDestinationFLUXLocations());
                xPathUtil.appendWithoutWrapping(partialXPath).append(DESTINATION_FLUX_LOCATION).storeInRepo(faCatchFact, "destinationFLUXLocations");

                faCatchFact.setSpeciesCode(mapToCodeType(faCatch.getSpeciesCode()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(SPECIES_CODE).storeInRepo(faCatchFact, "speciesCode");

                faCatchFact.setAppliedAAPProcessConversionFactorNumber(mapAAPProcessList(faCatch.getAppliedAAPProcesses()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, CONVERSION_FACTOR_NUMERIC).storeInRepo(faCatchFact, "appliedAAPProcessConversionFactorNumber");

                faCatchFact.setCategoryCode(mapToCodeType(faCatchSpecifiedSizeDistributionCategoryCode(faCatch)));
                xPathUtil.appendWithoutWrapping(partialXPath).append(SPECIFIED_SIZE_DISTRIBUTION, CATEGORY_CODE).storeInRepo(faCatchFact, "categoryCode");

                faCatchFact.setTypeCode(mapToCodeType(faCatch.getTypeCode()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(TYPE_CODE).storeInRepo(faCatchFact, TYPE_CODE_PROP);

                faCatchFact.setSizeDistributionClassCode(mapToCodeTypes(faCatchesSpecifiedSizeDistributionClassCodes(faCatch)));
                xPathUtil.appendWithoutWrapping(partialXPath).append(SPECIFIED_SIZE_DISTRIBUTION, CLASS_CODE).storeInRepo(faCatchFact, "sizeDistributionClassCode");

                faCatchFact.setUnitQuantity(mapToMeasureType(faCatch.getUnitQuantity()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(UNIT_QUANTITY).storeInRepo(faCatchFact, "unitQuantity");

                faCatchFact.setWeightMeasure(mapToMeasureType(faCatch.getWeightMeasure()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(WEIGHT_MEASURE).storeInRepo(faCatchFact, "weightMeasure");

                faCatchFact.setWeighingMeansCode(mapToCodeType(faCatch.getWeighingMeansCode()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(WEIGHING_MEANS_CODE).storeInRepo(faCatchFact, "weighingMeansCode");

                faCatchFact.setResultAAPProductWeightMeasure(getMeasureTypeFromAAPProcess(faCatch.getAppliedAAPProcesses(), WEIGHT_MEASURE));
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, WEIGHT_MEASURE).storeInRepo(faCatchFact, "resultAAPProductWeightMeasure");

                faCatchFact.setResultAAPProductUnitQuantity(getMeasureTypeFromAAPProcess(faCatch.getAppliedAAPProcesses(), UNIT_QUANTITY));
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, UNIT_QUANTITY).storeInRepo(faCatchFact, "resultAAPProductUnitQuantity");

                faCatchFact.setAppliedAAPProcessTypeCodes(getAppliedProcessTypeCodes(faCatch.getAppliedAAPProcesses()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, TYPE_CODE).storeInRepo(faCatchFact, "appliedAAPProcessTypeCodes");

                faCatchFact.setResultAAPProductPackagingTypeCode(getAAPProductPackagingTypeCode(faCatch.getAppliedAAPProcesses()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, PACKAGING_TYPE_CODE).storeInRepo(faCatchFact, "resultAAPProductPackagingTypeCode");

                faCatchFact.setResultAAPProductPackagingUnitQuantity(getMeasureTypeFromAAPProcess(faCatch.getAppliedAAPProcesses(), PACKAGING_UNIT_QUANTITY));
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, PACKAGING_UNIT_QUANTITY).storeInRepo(faCatchFact, "resultAAPProductPackagingUnitQuantity");

                faCatchFact.setResultAAPProductPackagingUnitAverageWeightMeasure(getMeasureTypeFromAAPProcess(faCatch.getAppliedAAPProcesses(), AVERAGE_WEIGHT_MEASURE));
                xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, PACKAGING_UNIT_AVERAGE_WEIGHT_MEASURE).storeInRepo(faCatchFact, "resultAAPProductPackagingUnitAverageWeightMeasure");

                faCatchFact.setSpecifiedFLUXLocations(faCatch.getSpecifiedFLUXLocations());
                xPathUtil.appendWithoutWrapping(partialXPath).append(SPECIFIED_FLUX_LOCATION).storeInRepo(faCatchFact, "specifiedFLUXLocations");

                if (faCatch.getSpecifiedFLUXLocations() != null) {
                    faCatchFact.setFaCatchFluxLocationId(mapFLUXLocationIDs(faCatch.getSpecifiedFLUXLocations()));
                }
                xPathUtil.appendWithoutWrapping(partialXPath).append(SPECIFIED_FLUX_LOCATION, ID).storeInRepo(faCatchFact, "faCatchFluxLocationId");

                if(CollectionUtils.isNotEmpty(fishActRelatedFluxLocations)){
                    faCatchFact.setFishActRelatedFluxLocationIds(mapFLUXLocationIDs(fishActRelatedFluxLocations));
                }
                xPathUtil.appendWithoutWrapping(partialXPath1).append(RELATED_FLUX_LOCATION, ID).storeInRepo(faCatchFact, "fishActRelatedFluxLocationIds");

                faCatchFact.setSpecifiedFluxLocationRFMOCodeList(getFLUXLocationRFMOCodes(faCatch.getSpecifiedFLUXLocations()));
                xPathUtil.appendWithoutWrapping(partialXPath).append(SPECIFIED_FLUX_LOCATION, REGIONAL_FISHERIES_MANAGEMENT_ORGANIZATION_CODE).storeInRepo(faCatchFact, "specifiedFluxLocationRFMOCodeList");

                if (fishActRelatedFluxLocations != null) {
                    faCatchFact.setFluxLocationId(mapFLUXLocationIDs(fishActRelatedFluxLocations));
                    xPathUtil.appendWithoutWrapping(partialXPath).append(RELATED_FLUX_LOCATION, ID).storeInRepo(faCatchFact, "fluxLocationId");
                }

                faCatchFact.setSubActivity(isSubActivity);

                facts.add(faCatchFact);

                index++;
            }
        }
        return facts;
    }

    public VesselStorageCharacteristicsFact generateFactsForVesselStorageCharacteristic(VesselStorageCharacteristic vesselStorageCharacteristic) {
        if (vesselStorageCharacteristic == null) {
            xPathUtil.clear();
            return null;
        }

        VesselStorageCharacteristicsFact vesselStorageCharacteristicsFact = new VesselStorageCharacteristicsFact();

        vesselStorageCharacteristicsFact.setTypeCodes(mapToCodeTypes(vesselStorageCharacteristic.getTypeCodes()));
        xPathUtil.append(TYPE_CODE).storeInRepo(vesselStorageCharacteristicsFact, "typeCodes");

        return vesselStorageCharacteristicsFact;
    }

    public List<VesselStorageCharacteristicsFact> generateFactsForVesselStorageCharacteristics(List<VesselStorageCharacteristic> vesselStorageCharacteristics) {
        if (vesselStorageCharacteristics == null) {
            xPathUtil.clear();
            return emptyList();
        }

        List<VesselStorageCharacteristicsFact> list = new ArrayList<>();
        for (VesselStorageCharacteristic vesselStorageCharacteristic : vesselStorageCharacteristics) {
            list.add(generateFactsForVesselStorageCharacteristic(vesselStorageCharacteristic));
        }

        return list;
    }

    public FishingTripFact generateFactForFishingTrip(FishingTrip fishingTrip) {
        if (fishingTrip == null) {
            xPathUtil.clear();
            return null;
        }
        FishingTripFact fishingTripFact = new FishingTripFact();

        String partialXpath = xPathUtil.getValue();

        fishingTripFact.setIds(mapToIdType(fishingTrip.getIDS()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(fishingTripFact, "ids");

        fishingTripFact.setTypeCode(mapToCodeType(fishingTrip.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fishingTripFact, TYPE_CODE_PROP);

        return fishingTripFact;
    }

    public List<FishingTripFact> generateFactForFishingTrips(List<FishingTrip> fishingTrip, String fishingTripType) {
        if (fishingTrip == null) {
            xPathUtil.clear();
            return emptyList();
        }
        List<FishingTripFact> list = new ArrayList<>();
        String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (FishingTrip fishingTrip_ : fishingTrip) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fishingTripType, index);
            list.add(generateFactForFishingTrip(fishingTrip_));
            index++;
        }
        return list;
    }

    public FluxLocationFact generateFactForFluxLocation(FLUXLocation fluxLocation, boolean isSpecifiedFluxLocFromFaCatch, String activityTypeStr, String faReportTypeStr, FACatch faCatch) {
        if (fluxLocation == null) {
            xPathUtil.clear();
            return null;
        }

        final String partialXpath = xPathUtil.getValue();

        FluxLocationFact fluxLocationFact = new FluxLocationFact();

        fluxLocationFact.setSpecifiedFluxLocFromFaCatch(isSpecifiedFluxLocFromFaCatch);
        fluxLocationFact.setActivityType(activityTypeStr);
        fluxLocationFact.setFaRepDocType(faReportTypeStr);
        fluxLocationFact.setFaCatch(faCatch);

        fluxLocationFact.setId(mapToIdType(fluxLocation.getID()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(fluxLocationFact, ID_PROP);

        fluxLocationFact.setApplicableFLUXCharacteristicTypeCode(getApplicableFLUXCharacteristicsTypeCode(fluxLocation.getApplicableFLUXCharacteristics()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(APPLICABLE_FLUX_CHARACTERISTIC, TYPE_CODE).storeInRepo(fluxLocationFact, "applicableFLUXCharacteristicTypeCode");

        fluxLocationFact.setCountryID(mapToIdType(fluxLocation.getCountryID()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(COUNTRY_ID).storeInRepo(fluxLocationFact, "countryID");

        fluxLocationFact.setTypeCode(mapToCodeType(fluxLocation.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fluxLocationFact, TYPE_CODE_PROP);

        fluxLocationFact.setSpecifiedPhysicalFLUXGeographicalCoordinate(fluxLocation.getSpecifiedPhysicalFLUXGeographicalCoordinate());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_PHYSICAL_FLUX_GEOGRAPHICAL_COORDINATE).storeInRepo(fluxLocationFact, "specifiedPhysicalFLUXGeographicalCoordinate");

        fluxLocationFact.setPhysicalStructuredAddress(fluxLocation.getPhysicalStructuredAddress());
        xPathUtil.appendWithoutWrapping(partialXpath).append(PHYSICAL_STRUCTURED_ADDRESS).storeInRepo(fluxLocationFact, "physicalStructuredAddress");

        fluxLocationFact.setRfmo(mapToCodeType(fluxLocation.getRegionalFisheriesManagementOrganizationCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append("RegionalFisheriesManagementOrganizationCode").storeInRepo(fluxLocationFact, "rfmo");

        return fluxLocationFact;
    }

    public List<FluxLocationFact> generateFactsForFluxLocations(List<FLUXLocation> fluxLocation, boolean isSpecifiedFluxLocFromFaCatch, String activityTypeStr, String faReportTypeStr, FACatch facatch) {
        if (fluxLocation == null) {
            xPathUtil.clear();
            return emptyList();
        }
        final String partialXpath = xPathUtil.getValue();
        List<FluxLocationFact> list = new ArrayList<>();
        int index = 1;
        for (FLUXLocation fLUXLocation : fluxLocation) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FLUX_LOCATION, index);
            list.add(generateFactForFluxLocation(fLUXLocation, isSpecifiedFluxLocFromFaCatch, activityTypeStr, faReportTypeStr, facatch));
            index++;
        }
        return list;
    }

    public FluxCharacteristicsFact generateFactForFluxCharacteristic(FLUXCharacteristic fluxCharacteristic, String fluxCharacteristicType) {
        if (fluxCharacteristic == null) {
            xPathUtil.clear();
            return null;
        }

        final String partialXpath = xPathUtil.getValue();
        FluxCharacteristicsFact fluxCharacteristicsFact = new FluxCharacteristicsFact();
        fluxCharacteristicsFact.setFluxCharacteristicType(fluxCharacteristicType);

        fluxCharacteristicsFact.setTypeCode(mapToCodeType(fluxCharacteristic.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fluxCharacteristicsFact, TYPE_CODE_PROP);

        fluxCharacteristicsFact.setValueMeasure(mapToMeasureType(fluxCharacteristic.getValueMeasure()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_MEASURE).storeInRepo(fluxCharacteristicsFact, VALUE_MEASURE_PROP);

        fluxCharacteristicsFact.setValueDateTime(getDate(fluxCharacteristic.getValueDateTime()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_DATE_TIME).storeInRepo(fluxCharacteristicsFact, VALUE_DATE_TIME_PROP);

        fluxCharacteristicsFact.setValueIndicator(fluxCharacteristic.getValueIndicator());
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_INDICATOR).storeInRepo(fluxCharacteristicsFact, VALUE_INDICATOR_PROP);

        fluxCharacteristicsFact.setValueCode(mapToCodeType(fluxCharacteristic.getValueCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_CODE).storeInRepo(fluxCharacteristicsFact, VALUE_CODE_PROP);

        fluxCharacteristicsFact.setValues(fluxCharacteristic.getValues());
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE).storeInRepo(fluxCharacteristicsFact, "values");

        fluxCharacteristicsFact.setValueQuantity(fluxCharacteristic.getValueQuantity());
        xPathUtil.appendWithoutWrapping(partialXpath).append(VALUE_QUANTITY).storeInRepo(fluxCharacteristicsFact, VALUE_QUANTITY_PROP);

        return fluxCharacteristicsFact;
    }


    public List<FluxCharacteristicsFact> generateFactsForFluxCharacteristics(List<FLUXCharacteristic> fluxCharacteristic, String fluxCharacteristicType) {
        if (fluxCharacteristic == null) {
            return emptyList();
        }
        String partialXpath = xPathUtil.getValue();
        List<FluxCharacteristicsFact> list = new ArrayList<>();
        int index = 1;
        for (FLUXCharacteristic fLUXCharacteristic : fluxCharacteristic) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxCharacteristicType, index);
            list.add(generateFactForFluxCharacteristic(fLUXCharacteristic, fluxCharacteristicType));
        }
        return list;
    }

    public FaDepartureFact generateFactsForFaDeparture(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaDepartureFact faDepartureFact = new FaDepartureFact();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {
            faDepartureFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faDepartureFact, FISHING_ACTIVITY_TYPE_CODE_PROP);
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                List<FLUXLocation> relatedFLUXLocations = fishingActivity.getRelatedFLUXLocations();
                faDepartureFact.setRelatedFLUXLocations(relatedFLUXLocations);
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faDepartureFact, RELATED_FLUX_LOCATIONS_PROP);

                List<CodeType> codeTypes = new ArrayList<>();
                for (FLUXLocation fluxLocation : relatedFLUXLocations) {
                    un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = fluxLocation.getTypeCode();
                    if (typeCode != null)
                        codeTypes.add(mapToCodeType(typeCode));
                }
                faDepartureFact.setRelatedFLUXLocationTypeCodes(codeTypes);
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faDepartureFact, RELATED_FLUX_LOCATIONS_TYPE_CODE_PROP);
            }

            List<FishingGear> specifiedFishingGears = fishingActivity.getSpecifiedFishingGears();
            List<CodeType> roleCodes = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(specifiedFishingGears)){
                for (FishingGear fishingGear : specifiedFishingGears) {
                    roleCodes.addAll(mapToCodeTypes(fishingGear.getRoleCodes()));
                }
                faDepartureFact.setSpecifiedFishingGearsArePresent(true);
            } else {
                faDepartureFact.setSpecifiedFishingGearsArePresent(false);
            }
            faDepartureFact.setSpecifiedFishingGearRoleCodeTypes(roleCodes);
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_GEAR, ROLE_CODE).storeInRepo(faDepartureFact, "specifiedFishingGearRoleCodeTypes");

            List<FACatch> specifiedFACatches = fishingActivity.getSpecifiedFACatches();
            if (specifiedFACatches != null) {
                List<CodeType> codeTypeList = new ArrayList<>();

                for (FACatch faCatch : specifiedFACatches) {
                    CodeType codeType = mapToCodeType(faCatch.getTypeCode());
                    if (codeType != null){
                        codeTypeList.add(codeType);
                    }
                }
                faDepartureFact.setSpecifiedFACatchCodeTypes(codeTypeList);
                xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faDepartureFact, "specifiedFACatchCodeTypes");
            }

            faDepartureFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(REASON_CODE).storeInRepo(faDepartureFact, REASON_CODE_PROP);

            FishingTrip specifiedFishingTrip = fishingActivity.getSpecifiedFishingTrip();
            faDepartureFact.setSpecifiedFishingTrip(specifiedFishingTrip);
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_TRIP).storeInRepo(faDepartureFact, SPECIFIED_FISHING_TRIP_PROP);

            if (specifiedFishingTrip != null) {
                faDepartureFact.setSpecifiedFishingTripIds(mapToIdType(specifiedFishingTrip.getIDS()));
                faDepartureFact.setFaTypesPerTrip(fishingActivitiesWithTripIds);
                xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_TRIP, ID).storeInRepo(faDepartureFact, "specifiedFishingTripIds");
            }

            faDepartureFact.setOccurrenceDateTime(getDate(fishingActivity.getOccurrenceDateTime()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(OCCURRENCE_DATE_TIME).storeInRepo(faDepartureFact, OCCURRENCE_DATE_TIME_PROP);

        }
        if (faReportDocument != null) {
            faDepartureFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faDepartureFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);

            FLUXReportDocument relatedFLUXReportDocument = faReportDocument.getRelatedFLUXReportDocument();
            String purposeCode = StringUtils.EMPTY;
            if (relatedFLUXReportDocument != null && relatedFLUXReportDocument.getPurposeCode() != null && relatedFLUXReportDocument.getPurposeCode().getValue() != null) {
                purposeCode = relatedFLUXReportDocument.getPurposeCode().getValue();
            }
            faDepartureFact.setPurposeCode(purposeCode);
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, RELATED_FLUX_REPORT_DOCUMENT, PURPOSE_CODE).storeInRepo(faDepartureFact, PURPOSE_CODE_PROP);
        }

        return faDepartureFact;
    }

    public FaEntryToSeaFact generateFactsForEntryIntoSea(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaEntryToSeaFact faEntryToSeaFact = new FaEntryToSeaFact();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {
            faEntryToSeaFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faEntryToSeaFact, FISHING_ACTIVITY_TYPE_CODE_PROP);
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faEntryToSeaFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faEntryToSeaFact, RELATED_FLUX_LOCATIONS_PROP);

                faEntryToSeaFact.setRelatedFluxLocationTypeCodes(getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faEntryToSeaFact, RELATED_FLUX_LOCATIONS_TYPE_CODE_PROP);

                faEntryToSeaFact.setRelatedFluxLocationIDs(mapFLUXLocationIDs(fishingActivity.getRelatedFLUXLocations()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, ID).storeInRepo(faEntryToSeaFact, RELATED_FLUX_LOCATIONS_ID_PROP);
            }
            faEntryToSeaFact.setSpeciesTargetCode(mapToCodeType(fishingActivity.getSpeciesTargetCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIES_TARGET_CODE).storeInRepo(faEntryToSeaFact, SPECIES_TARGET_CODE_PROP);
            faEntryToSeaFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(REASON_CODE).storeInRepo(faEntryToSeaFact, REASON_CODE_PROP);
            faEntryToSeaFact.setSpecifiedFACatches(fishingActivity.getSpecifiedFACatches());
            faEntryToSeaFact.setSpecifiedFACatchesTypeCodes(getFACatchesTypeCodes(fishingActivity.getSpecifiedFACatches()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faEntryToSeaFact, SPECIFIED_FA_CATCHES_TYPE_CODE_PROP);

            faEntryToSeaFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY).storeInRepo(faEntryToSeaFact, "relatedFishingActivities");
        }
        if (faReportDocument != null) {
            faEntryToSeaFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faEntryToSeaFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);
        }

        return faEntryToSeaFact;
    }

    public FaExitFromSeaFact generateFactsForExitArea(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaExitFromSeaFact faExitFromSeaFact = new FaExitFromSeaFact();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {

            faExitFromSeaFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faExitFromSeaFact, FISHING_ACTIVITY_TYPE_CODE_PROP);

            faExitFromSeaFact.setSpecifiedFACatchesTypeCodes(getFACatchesTypeCodes(fishingActivity.getSpecifiedFACatches()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faExitFromSeaFact, SPECIFIED_FA_CATCHES_TYPE_CODE_PROP);

            final List<FLUXLocation> relatedFLUXLocations = fishingActivity.getRelatedFLUXLocations();
            if (relatedFLUXLocations != null) {
                faExitFromSeaFact.setRelatedFLUXLocations(new ArrayList<>(relatedFLUXLocations));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faExitFromSeaFact, RELATED_FLUX_LOCATIONS_PROP);

                faExitFromSeaFact.setRelatedFluxLocationTypeCodes(getFLUXLocationTypeCodes(relatedFLUXLocations));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faExitFromSeaFact, RELATED_FLUX_LOCATIONS_TYPE_CODE_PROP);

                faExitFromSeaFact.setRelatedFluxLocationIDs(mapFLUXLocationIDs(relatedFLUXLocations));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY).storeInRepo(faExitFromSeaFact, RELATED_FISHING_ACTIVITIES_PROP);
            }

            faExitFromSeaFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY).storeInRepo(faExitFromSeaFact, "relatedFishingActivities");

        }
        if (faReportDocument != null) {
            faExitFromSeaFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faExitFromSeaFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);
        }

        return faExitFromSeaFact;
    }

    public FaFishingOperationFact generateFactsForFishingOperation(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaFishingOperationFact faFishingOperationFact = new FaFishingOperationFact();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {
            faFishingOperationFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faFishingOperationFact, FISHING_ACTIVITY_TYPE_CODE_PROP);

            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faFishingOperationFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faFishingOperationFact, RELATED_FLUX_LOCATIONS_PROP);

            BigDecimal operatQuantity = fishingActivityOperationsQuantityValue(fishingActivity);
            faFishingOperationFact.setOperationsQuantity(operatQuantity == null ? null : operatQuantity.toString());
            xPathUtil.appendWithoutWrapping(partialXpath).append(OPERATIONS_QUANTITY).storeInRepo(faFishingOperationFact, "operationsQuantity");

            faFishingOperationFact.setVesselRelatedActivityCode(mapToCodeType(fishingActivity.getVesselRelatedActivityCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(VESSEL_RELATED_ACTIVITY_CODE).storeInRepo(faFishingOperationFact, VESSEL_RELATED_ACTIVITY_CODE_PROP);

            List<VesselTransportMeans> vesselTransportMeans = fishingActivity.getRelatedVesselTransportMeans();
            faFishingOperationFact.setVesselTransportMeans(vesselTransportMeans);
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_VESSEL_TRANSPORT_MEANS, SPECIFIED_CONTACT_PARTY).storeInRepo(faFishingOperationFact, "vesselTransportMeans");

            if (CollectionUtils.isNotEmpty(vesselTransportMeans)) {
                faFishingOperationFact.setVesselTransportMeansContactParties(getContactPartiesFromVesselTransportMeans(vesselTransportMeans));
            }
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_VESSEL_TRANSPORT_MEANS, SPECIFIED_CONTACT_PARTY).storeInRepo(faFishingOperationFact, "vesselTransportMeansContactParties");

            faFishingOperationFact.setFishingGearRoleCodes(getFishingGearRoleCodes(fishingActivity.getSpecifiedFishingGears()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_ACTIVITY, VESSEL_RELATED_ACTIVITY_CODE).storeInRepo(faFishingOperationFact, FISHING_GEAR_ROLE_CODES_PROP);

            List<FishingActivity> relatedFishingActivities = fishingActivity.getRelatedFishingActivities();
            if (CollectionUtils.isNotEmpty(relatedFishingActivities)) {
                faFishingOperationFact.setRelatedFishingActivityTypeCodes(getFishingActivityTypeCodeList(relatedFishingActivities));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, TYPE_CODE).storeInRepo(faFishingOperationFact, "relatedFishingActivityTypeCodes");

                faFishingOperationFact.setRelatedFishingActivities(relatedFishingActivities);
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY).storeInRepo(faFishingOperationFact, "relatedFishingActivities");
                int activityIndex = 1;
                for (FishingActivity activity : relatedFishingActivities) {
                    List<CodeType> fishingGearRoleCodes1 = getFishingGearRoleCodes(activity.getSpecifiedFishingGears());
                    List<CodeType> fishingGearRoleCodes = faFishingOperationFact.getFishingGearRoleCodes();
                    if (CollectionUtils.isNotEmpty(fishingGearRoleCodes)){
                        fishingGearRoleCodes.addAll(fishingGearRoleCodes1);
                    }
                    faFishingOperationFact.setFishingGearRoleCodes(fishingGearRoleCodes);
                    xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY).appendWithIndex(VESSEL_RELATED_ACTIVITY_CODE, activityIndex)
                            .storeInRepo(faFishingOperationFact, FISHING_GEAR_ROLE_CODES_PROP);
                }
            }
        }

        if (faReportDocument != null) {
            faFishingOperationFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faFishingOperationFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);
        }

        return faFishingOperationFact;
    }


    private List<ContactParty> getContactPartiesFromVesselTransportMeans(List<VesselTransportMeans> vesselTransportMeansList) {
        List<ContactParty> contactParties = new ArrayList<>();
        if (CollectionUtils.isEmpty(vesselTransportMeansList)) {
            return contactParties;
        }
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMeansList) {
            if (CollectionUtils.isNotEmpty(vesselTransportMeans.getSpecifiedContactParties())) {
                contactParties.addAll(vesselTransportMeans.getSpecifiedContactParties());
            }
        }
        return contactParties;
    }


    public FaJointFishingOperationFact generateFactsForJointFishingOperation(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaJointFishingOperationFact faJointFishingOperationFact = new FaJointFishingOperationFact();

        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {
            faJointFishingOperationFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faJointFishingOperationFact, FISHING_ACTIVITY_TYPE_CODE_PROP);

            faJointFishingOperationFact.setFishingActivityIds(mapToIdType(fishingActivity.getIDS()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(faJointFishingOperationFact, "fishingActivityIds");

            final List<FLUXLocation> relatedFLUXLocations = fishingActivity.getRelatedFLUXLocations();
            if (relatedFLUXLocations != null) {
                faJointFishingOperationFact.setRelatedFLUXLocations(new ArrayList<>(relatedFLUXLocations));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faJointFishingOperationFact, RELATED_FLUX_LOCATIONS_PROP);

                faJointFishingOperationFact.setFluxLocationTypeCode(getFLUXLocationTypeCodes(relatedFLUXLocations));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faJointFishingOperationFact, FLUX_LOCATION_TYPE_CODE_PROP);
            }

            faJointFishingOperationFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY).storeInRepo(faJointFishingOperationFact, RELATED_FISHING_ACTIVITIES_PROP);

            if (CollectionUtils.isNotEmpty(fishingActivity.getRelatedFishingActivities())) {
                faJointFishingOperationFact.setRelatedFishingActivityTypeCode(getFishingActivityTypeCodeList(fishingActivity.getRelatedFishingActivities()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, TYPE_CODE).storeInRepo(faJointFishingOperationFact, "relatedFishingActivityTypeCode");

                faJointFishingOperationFact.setRelatedFishingActivityFaCatchTypeCodes(getFishingActivityFaCatchTypeCodes(fishingActivity.getRelatedFishingActivities()));

                faJointFishingOperationFact.setRelatedFishingActivityFaCatch(getFishingActivityFaCatches(fishingActivity.getRelatedFishingActivities()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, SPECIFIED_FA_CATCH).storeInRepo(faJointFishingOperationFact, "specifiedFaCatch");
            }
        }
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faJointFishingOperationFact, "relatedFishingActivityFaCatchTypeCodes");
        if (faReportDocument != null) {
            faJointFishingOperationFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faJointFishingOperationFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);

            if (faReportDocument.getSpecifiedVesselTransportMeans() != null) {
                faJointFishingOperationFact.setFaReportDocVesselRoleCode(mapToCodeType(faReportDocument.getSpecifiedVesselTransportMeans().getRoleCode()));
            }
        }
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, SPECIFIED_VESSEL_TRANSPORT_MEANS, ROLE_CODE).storeInRepo(faJointFishingOperationFact, "faReportDocVesselRoleCode");

        return faJointFishingOperationFact;
    }

    private List<FACatch> getFishingActivityFaCatches(List<FishingActivity> fishingActivities) {
        List<FACatch> faCatchs = new ArrayList<>();
        for (FishingActivity activity : fishingActivities) {
            List<FACatch> specifiedFACatches = activity.getSpecifiedFACatches();
            if (!CollectionUtils.isEmpty(specifiedFACatches)) {
                faCatchs.addAll(activity.getSpecifiedFACatches());
            }
        }
        return faCatchs;
    }

    private List<CodeType> getFishingActivityFaCatchTypeCodes(List<FishingActivity> fishingActivities) {
        List<CodeType> faCatchTypeCodes = new ArrayList<>();
        for (FishingActivity activity : fishingActivities) {
            List<CodeType> faCatchCodes = getFACatchesTypeCodes(activity.getSpecifiedFACatches());
            if (faCatchCodes != null) {
                faCatchTypeCodes.addAll(faCatchCodes);
            }
        }
        return faCatchTypeCodes;

    }

    private List<CodeType> getFishingActivityTypeCodeList(List<FishingActivity> fishingActivities) {
        if (CollectionUtils.isEmpty(fishingActivities)) {
            return emptyList();
        }
        List<CodeType> fishingActivityTypeCodes = new ArrayList<>();
        for (FishingActivity activity : fishingActivities) {
            CodeType codeType = mapToCodeType(activity.getTypeCode());
            if(codeType != null){
                fishingActivityTypeCodes.add(codeType);
            }
        }

        return fishingActivityTypeCodes;
    }

    public FaRelocationFact generateFactsForRelocation(FishingActivity fishingActivity, FAReportDocument faReportDocument, boolean isSubActivity) {
        if (fishingActivity == null) {
            xPathUtil.clear();
            return null;
        }

        final String partialXpath = xPathUtil.getValue();
        FaRelocationFact faRelocationFact = new FaRelocationFact();
        faRelocationFact.setSubActivity(isSubActivity);

        if (CollectionUtils.isNotEmpty(fishingActivity.getSpecifiedFACatches())) {
            List<CodeType> typeCodes = new ArrayList<>();
            List<CodeType> speciesCodes = new ArrayList<>();
            List<CodeType> fluxLocationTypeCodes = new ArrayList<>();
            List<IdType> fluxLocationIds = new ArrayList<>();
            List<FLUXLocation> destinationFluxLocations = new ArrayList<>();

            int faCatchIndex = 1;
            for (FACatch faCatch : fishingActivity.getSpecifiedFACatches()) {

                typeCodes.add(mapToCodeType(faCatch.getTypeCode()));
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FA_CATCH, faCatchIndex).append(TYPE_CODE).storeInRepo(faRelocationFact, "specifiedFACatchTypeCodes");

                speciesCodes.add(mapToCodeType(faCatch.getSpeciesCode()));
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FA_CATCH, faCatchIndex).append(SPECIES_CODE).storeInRepo(faRelocationFact, "specifiedFACatchSpeciesCodes");

                if (CollectionUtils.isNotEmpty(faCatch.getDestinationFLUXLocations())) {
                    for (FLUXLocation fluxLocation : faCatch.getDestinationFLUXLocations()) {
                        fluxLocationTypeCodes.add(mapToCodeType(fluxLocation.getTypeCode()));
                        IdType idType = mapToIdType(fluxLocation.getID());
                        if (idType != null){
                            fluxLocationIds.add(idType);
                        }
                        destinationFluxLocations.add(fluxLocation);
                    }
                }
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FA_CATCH, faCatchIndex).append(DESTINATION_FLUX_LOCATION).storeInRepo(faRelocationFact, "destinationFLUXLocations");
                faCatchIndex++;
            }

            faRelocationFact.setSpecifiedFACatchTypeCodes(typeCodes);
            faRelocationFact.setSpecifiedFACatchSpeciesCodes(speciesCodes);

            faRelocationFact.setSpecifiedFACatchDestinationFluxLocationTypeCodes(fluxLocationTypeCodes);
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FA_CATCH, faCatchIndex).append(DESTINATION_FLUX_LOCATION, TYPE_CODE).storeInRepo(faRelocationFact, "specifiedFACatchDestinationFluxLocationTypeCodes");
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FA_CATCH, faCatchIndex).append(DESTINATION_FLUX_LOCATION, ID).storeInRepo(faRelocationFact, "specifiedFACatchDestinationFluxLocationIDs");

            faRelocationFact.setSpecifiedFACatchDestinationFluxLocationIDs(fluxLocationIds);
            faRelocationFact.setDestinationFLUXLocations(destinationFluxLocations);
        }

        faRelocationFact.setRelatedFLUXLocationTypeCodes(mapFluxLocationsToCodeTypes(fishingActivity.getRelatedFLUXLocations()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faRelocationFact, "relatedFLUXLocationTypeCodes");

        final VesselStorageCharacteristic destinationVesselStorageCharacteristic = fishingActivity.getDestinationVesselStorageCharacteristic();
        if (destinationVesselStorageCharacteristic != null) {
            faRelocationFact.setDestinationVesselStorageCharacteristicTypeCodes(mapToCodeTypes(destinationVesselStorageCharacteristic.getTypeCodes()));
        }
        xPathUtil.appendWithoutWrapping(partialXpath).append(DESTINATION_VESSEL_STORAGE_CHARACTERISTIC, TYPE_CODE).storeInRepo(faRelocationFact, "destinationVesselStorageCharacteristicTypeCodes");

        if (destinationVesselStorageCharacteristic != null) {
            faRelocationFact.setDestinationVesselStorageCharacteristicID(mapToIdType(destinationVesselStorageCharacteristic.getID()));
        }
        xPathUtil.appendWithoutWrapping(partialXpath).append(DESTINATION_VESSEL_STORAGE_CHARACTERISTIC, ID).storeInRepo(faRelocationFact, "destinationVesselStorageCharacteristicID");

        if (CollectionUtils.isNotEmpty(fishingActivity.getRelatedVesselTransportMeans())) {
            int vessTrspMeansIndex = 1;
            for (VesselTransportMeans vesselTransportMeans : fishingActivity.getRelatedVesselTransportMeans()) {

                faRelocationFact.setRelatedVesselTransportMeansIDs(mapToIdType(vesselTransportMeans.getIDS()));
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, vessTrspMeansIndex).append(ID).storeInRepo(faRelocationFact, "relatedVesselTransportMeansIDs");

                faRelocationFact.setRelatedVesselTransportMeansRoleCodes(Collections.singletonList(mapToCodeType(vesselTransportMeans.getRoleCode())));
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, vessTrspMeansIndex).append(ROLE_CODE).storeInRepo(faRelocationFact, "relatedVesselTransportMeansRoleCodes");

                faRelocationFact.setRelatedVesselTransportMeansContactPartyRoleCodes(mapToContactPartyRoleCodes(vesselTransportMeans));
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, vessTrspMeansIndex).append(SPECIFIED_CONTACT_PARTY, ROLE_CODE).storeInRepo(faRelocationFact, "relatedVesselTransportMeansContactPartyRoleCodes");

                faRelocationFact.setRelatedVesselTransportMeansNames(vesselTransportMeans.getNames());
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, vessTrspMeansIndex).append("Name").storeInRepo(faRelocationFact, "relatedVesselTransportMeansNames");

                vessTrspMeansIndex++;
            }
        } else {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, 1).append(SPECIFIED_CONTACT_PARTY, ROLE_CODE).storeInRepo(faRelocationFact, "relatedVesselTransportMeansContactPartyRoleCodes");
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, 1).append("Name").storeInRepo(faRelocationFact, "relatedVesselTransportMeansNames");
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, 1).append(ID).storeInRepo(faRelocationFact, "relatedVesselTransportMeansIDs");
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, 1).append(ROLE_CODE).storeInRepo(faRelocationFact, "relatedVesselTransportMeansRoleCodes");
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, 1).append(SPECIFIED_CONTACT_PARTY, ROLE_CODE).storeInRepo(faRelocationFact, "relatedVesselTransportMeansContactPartyRoleCodes");
        }

        final VesselStorageCharacteristic sourceVesselStorageCharacteristic = fishingActivity.getSourceVesselStorageCharacteristic();

        if (sourceVesselStorageCharacteristic != null) {
            faRelocationFact.setSourceVesselStorageCharacteristicTypeCodes(mapToCodeTypes(sourceVesselStorageCharacteristic.getTypeCodes()));
        }
        xPathUtil.appendWithoutWrapping(partialXpath).append(SOURCE_VESSEL_STORAGE_CHARACTERISTIC, TYPE_CODE).storeInRepo(faRelocationFact, "sourceVesselStorageCharacteristicTypeCodes");

        faRelocationFact.setSourceVesselStorageCharacteristic(sourceVesselStorageCharacteristic);
        xPathUtil.appendWithoutWrapping(partialXpath).append(SOURCE_VESSEL_STORAGE_CHARACTERISTIC).storeInRepo(faRelocationFact, SOURCE_VESSEL_STORAGE_CHARACTERISTIC_PROP);

        faRelocationFact.setSpecifiedFACatches(fishingActivity.getSpecifiedFACatches());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH).storeInRepo(faRelocationFact, SPECIFIED_FA_CATCHES);

        faRelocationFact.setRelatedFLUXLocations(fishingActivity.getRelatedFLUXLocations());
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faRelocationFact, RELATED_FLUX_LOCATIONS_PROP);

        faRelocationFact.setRelatedVesselTransportMeans(fishingActivity.getRelatedVesselTransportMeans());
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_VESSEL_TRANSPORT_MEANS).storeInRepo(faRelocationFact, RELATED_VESSEL_TRANSPORT_MEANS_PROP);

        faRelocationFact.setDestinationVesselStorageCharacteristic(destinationVesselStorageCharacteristic);
        xPathUtil.appendWithoutWrapping(partialXpath).append(DESTINATION_VESSEL_STORAGE_CHARACTERISTIC).storeInRepo(faRelocationFact, DESTINATION_VESSEL_STORAGE_CHARACTERISTIC_PROP);

        faRelocationFact.setFaReportDocumentRelatedReportIds(mapToIdType(faReportDocument.getRelatedReportIDs()));
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT).append(RELATED_REPORT_ID).storeInRepo(faRelocationFact, "faReportDocumentRelatedReportIds");

        return faRelocationFact;
    }

    private List<CodeType> mapToContactPartyRoleCodes(VesselTransportMeans vesselTransportMeans) {
        List<CodeType> contactPartyRoleCodes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(vesselTransportMeans.getSpecifiedContactParties())) {
            for (ContactParty contactParty : vesselTransportMeans.getSpecifiedContactParties()) {
                if (CollectionUtils.isNotEmpty(contactParty.getRoleCodes())) {
                    contactPartyRoleCodes.addAll(mapToCodeTypes(contactParty.getRoleCodes()));
                }
            }
        }
        return contactPartyRoleCodes;
    }

    public FaDiscardFact generateFactsForDiscard(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null) {
            return null;
        }

        FaDiscardFact faDiscardFact = new FaDiscardFact();
        String partialXpath = xPathUtil.getValue();

        if (fishingActivity.getRelatedFLUXLocations() != null) {
            faDiscardFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faDiscardFact, "relatedFLUXLocations");

            faDiscardFact.setFluxLocationTypeCode(getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faDiscardFact, FLUX_LOCATION_TYPE_CODE_PROP);
        }

        faDiscardFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(REASON_CODE).storeInRepo(faDiscardFact, "reasonCode");


        if (CollectionUtils.isNotEmpty(fishingActivity.getSpecifiedFACatches())) {
            faDiscardFact.setSpecifiedFACatchTypeCode(getFishingActivityFaCatchTypeCodes(Collections.singletonList(fishingActivity)));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faDiscardFact, "specifiedFACatchTypeCode");
        }

        if (faReportDocument != null) {
            faDiscardFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faDiscardFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);
        }

        return faDiscardFact;
    }

    private List<CodeType> getFACatchesTypeCodes(List<FACatch> faCatches) {
        List<CodeType> codeTypes = null;
        if (faCatches != null) {
            codeTypes = new ArrayList<>();
            for (FACatch faCatch : faCatches) {
                un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = faCatch.getTypeCode();
                if (typeCode != null) {
                    CodeType codeType = new CodeType();
                    codeType.setListId(typeCode.getListID());
                    codeType.setValue(typeCode.getValue());
                    codeTypes.add(codeType);
                }
            }
        }
        return codeTypes;
    }

    public FaNotificationOfArrivalFact generateFactsForPriorNotificationOfArrival(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaNotificationOfArrivalFact faNotificationOfArrivalFact = new FaNotificationOfArrivalFact();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {
            faNotificationOfArrivalFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faNotificationOfArrivalFact, FISHING_ACTIVITY_TYPE_CODE_PROP);
            List<FLUXLocation> relatedFLUXLocations = fishingActivity.getRelatedFLUXLocations();
            if (relatedFLUXLocations != null) {
                faNotificationOfArrivalFact.setRelatedFLUXLocations(new ArrayList<>(relatedFLUXLocations));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faNotificationOfArrivalFact, RELATED_FLUX_LOCATIONS_PROP);
                List<CodeType> codeTypes = new ArrayList<>();
                for (FLUXLocation location : relatedFLUXLocations) {
                    CodeType codeType = mapToCodeType(location.getTypeCode());
                    if (codeType != null) {
                        codeTypes.add(codeType);
                    }
                }

                faNotificationOfArrivalFact.setRelatedFLUXLocationTypeCodes(codeTypes);
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faNotificationOfArrivalFact, RELATED_FLUX_LOCATIONS_TYPE_CODE_PROP);
            }
            List<FACatch> specifiedFACatches = fishingActivity.getSpecifiedFACatches();
            if (specifiedFACatches != null) {
                faNotificationOfArrivalFact.setSpecifiedFACatches(new ArrayList<>(fishingActivity.getSpecifiedFACatches()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH).storeInRepo(faNotificationOfArrivalFact, SPECIFIED_FA_CATCHES_PROP);
                List<CodeType> codeTypeList = new ArrayList<>();
                for (FACatch faCatch : specifiedFACatches) {
                    codeTypeList.add(mapToCodeType(faCatch.getTypeCode()));
                }
                faNotificationOfArrivalFact.setSpecifiedFACatchTypeCodes(codeTypeList);
                xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faNotificationOfArrivalFact, "specifiedFACatchTypeCodes");
            }
            faNotificationOfArrivalFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(REASON_CODE).storeInRepo(faNotificationOfArrivalFact, REASON_CODE_PROP);

            faNotificationOfArrivalFact.setOccurrenceDateTime(getDate(fishingActivity.getOccurrenceDateTime()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(OCCURRENCE_DATE_TIME).storeInRepo(faNotificationOfArrivalFact, OCCURRENCE_DATE_TIME_PROP);

            FishingTrip specifiedFishingTrip = fishingActivity.getSpecifiedFishingTrip();
            if (specifiedFishingTrip != null) {
                if (CollectionUtils.isNotEmpty(specifiedFishingTrip.getSpecifiedDelimitedPeriods())){
                    List<DelimitedPeriod> specifiedDelimitedPeriods = specifiedFishingTrip.getSpecifiedDelimitedPeriods();
                    faNotificationOfArrivalFact.setDelimitedPeriods(specifiedDelimitedPeriods.get(0));
                }
            }
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_TRIP, SPECIFIED_DELIMITED_PERIOD).storeInRepo(faNotificationOfArrivalFact, "delimitedPeriods");
        }

        if (faReportDocument != null) {
            faNotificationOfArrivalFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faNotificationOfArrivalFact, FA_REPORT_DOCUMENT_TYPE_CODE);
        }

        return faNotificationOfArrivalFact;
    }

    public FaTranshipmentFact generateFactsForTranshipment(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaTranshipmentFact faTranshipmentFact = new FaTranshipmentFact();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {
            faTranshipmentFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faTranshipmentFact, FISHING_ACTIVITY_TYPE_CODE_PROP);

            faTranshipmentFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faTranshipmentFact, RELATED_FLUX_LOCATIONS_PROP);

            faTranshipmentFact.setFluxLocationTypeCodes(getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faTranshipmentFact, "fluxLocationTypeCodes");

            faTranshipmentFact.setRelatedVesselTransportMeans(new ArrayList<>(fishingActivity.getRelatedVesselTransportMeans()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_VESSEL_TRANSPORT_MEANS).storeInRepo(faTranshipmentFact, RELATED_VESSEL_TRANSPORT_MEANS_PROP);

            faTranshipmentFact.setVesselTransportMeansRoleCodes(getVesselTransportMeansRoleCodes(fishingActivity.getRelatedVesselTransportMeans()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_VESSEL_TRANSPORT_MEANS, ROLE_CODE).storeInRepo(faTranshipmentFact, "vesselTransportMeansRoleCodes");

            final List<FACatch> specifiedFACatches = fishingActivity.getSpecifiedFACatches();

            faTranshipmentFact.setSpecifiedFACatches(new ArrayList<>(specifiedFACatches));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH).storeInRepo(faTranshipmentFact, SPECIFIED_FA_CATCHES_PROP);

            faTranshipmentFact.setFacatchSpeciesCode(getCodeTypesFromFaCatch(specifiedFACatches, SPECIES_CODE_FOR_FACATCH_PROP));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, SPECIES_CODE).storeInRepo(faTranshipmentFact, "faCatchSpeciesCodes");

            faTranshipmentFact.setFaCatchTypeCodes(getCodeTypesFromFaCatch(specifiedFACatches, CODE_TYPE_FOR_FACATCH_PROP));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faTranshipmentFact, "faCatchTypeCodes");

            faTranshipmentFact.setFaCtchSpecifiedFLUXLocations(getFluxLocationsFromFaCatch(specifiedFACatches));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, SPECIFIED_FLUX_LOCATION).storeInRepo(faTranshipmentFact, "faCtchSpecifiedFLUXLocations");

            faTranshipmentFact.setFaCtchSpecifiedFLUXLocationsTypeCodes(getCodeTypesFromFaCatch(specifiedFACatches, CODE_TYPE_FOR_FACATCH_FLUXLOCATION));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, SPECIFIED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faTranshipmentFact, "faCtchSpecifiedFLUXLocationsTypeCodes");

            final List<FLUXCharacteristic> specifiedFLUXCharacteristics = fishingActivity.getSpecifiedFLUXCharacteristics();

            faTranshipmentFact.setSpecifiedFLUXCharacteristics(specifiedFLUXCharacteristics);
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC).storeInRepo(faTranshipmentFact, "specifiedFLUXCharacteristics");

            faTranshipmentFact.setSpecifiedFlCharSpecifiedLocatIDs(mapToLocationIds(specifiedFLUXCharacteristics));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, SPECIFIED_FLUX_LOCATION, ID).storeInRepo(faTranshipmentFact, "specifiedFlCharSpecifiedLocatIDs");

            faTranshipmentFact.setSpecifiedFlCharSpecifiedLocatTypeCodes(mapToSpecifiedFluxLocationsCodeTypes(specifiedFLUXCharacteristics));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, SPECIFIED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faTranshipmentFact, "specifiedFlCharSpecifiedLocatTypeCodes");

            faTranshipmentFact.setFluxCharacteristicTypeCodes(getApplicableFLUXCharacteristicsTypeCode(specifiedFLUXCharacteristics));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, TYPE_CODE).storeInRepo(faTranshipmentFact, "fluxCharacteristicTypeCodes");
        }
        if (faReportDocument != null) {
            faTranshipmentFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faTranshipmentFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);
        }

        return faTranshipmentFact;
    }

    private List<CodeType> mapToSpecifiedFluxLocationsCodeTypes(List<FLUXCharacteristic> specifiedFLUXCharacteristics) {
        if (CollectionUtils.isEmpty(specifiedFLUXCharacteristics)) {
            return Collections.emptyList();
        }
        List<CodeType> specLocTypeCodes = new ArrayList<>();
        for (FLUXCharacteristic fluxLoc : specifiedFLUXCharacteristics) {
            specLocTypeCodes.addAll(mapFluxLocationsToCodeTypes(fluxLoc.getSpecifiedFLUXLocations()));
        }
        return specLocTypeCodes;
    }

    private List<IdType> mapToLocationIds(List<FLUXCharacteristic> specifiedFLUXCharacteristics) {
        if (CollectionUtils.isEmpty(specifiedFLUXCharacteristics)) {
            return Collections.emptyList();
        }
        List<IdType> idTypes = new ArrayList<>();
        for (FLUXCharacteristic flChar : specifiedFLUXCharacteristics) {
            idTypes.addAll(mapFLUXLocationIDs(flChar.getSpecifiedFLUXLocations()));
        }
        return idTypes;
    }

    private List<FLUXLocation> getFluxLocationsFromFaCatch(List<FACatch> specifiedFACatches) {
        if (CollectionUtils.isEmpty(specifiedFACatches)) {
            return Collections.emptyList();
        }
        List<FLUXLocation> faCatchFLUXLocations = null;
        for (FACatch faCatch : specifiedFACatches) {
            List<FLUXLocation> fluxLocations = faCatch.getSpecifiedFLUXLocations();
            if (CollectionUtils.isNotEmpty(fluxLocations)) {
                if (faCatchFLUXLocations == null) {
                    faCatchFLUXLocations = new ArrayList<>();
                }
                faCatchFLUXLocations.addAll(fluxLocations);
            }
        }
        return faCatchFLUXLocations;
    }

    public FaArrivalFact generateFactsForDeclarationOfArrival(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            xPathUtil.clear();
            return null;
        }

        FaArrivalFact faArrivalFact = new FaArrivalFact();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {

            List<FLUXCharacteristic> specifiedFLUXCharacteristics = fishingActivity.getSpecifiedFLUXCharacteristics();
            if (CollectionUtils.isNotEmpty(specifiedFLUXCharacteristics)) {

                List<CodeType> codeTypes = new ArrayList<>();
                List<String> dates = new ArrayList<>();

                for (FLUXCharacteristic characteristic : specifiedFLUXCharacteristics) {
                    if (characteristic != null) {
                        codeTypes.add(mapToCodeType(characteristic.getTypeCode()));
                        if (characteristic.getValueDateTime() != null && characteristic.getValueDateTime().getDateTime() != null) {
                            dates.add(characteristic.getValueDateTime().getDateTime().toString());
                        } else {
                            dates.add("NOT_VALID_DATE_ELEMENT");
                        }
                    }
                }

                faArrivalFact.setSpecifiedFLUXCharacteristicValueDateTimes(dates);
                faArrivalFact.setSpecifiedFLUXCharacteristicsTypeCodes(codeTypes);
                xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, TYPE_CODE).storeInRepo(faArrivalFact, "specifiedFLUXCharacteristicsTypeCodes");
                xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, "ValueDateTime").storeInRepo(faArrivalFact, "specifiedFLUXCharacteristicValueDateTimes");
            }
            faArrivalFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faArrivalFact, "fishingActivityTypeCode");

            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faArrivalFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faArrivalFact, RELATED_FLUX_LOCATIONS_PROP);
            }

            faArrivalFact.setFishingGearRoleCodes(getFishingGearRoleCodes(fishingActivity.getSpecifiedFishingGears()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_GEAR, ROLE_CODE).storeInRepo(faArrivalFact, FISHING_GEAR_ROLE_CODES_PROP);

            faArrivalFact.setFluxLocationTypeCodes(getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faArrivalFact, "fluxLocationTypeCodes");

            faArrivalFact.setFishingTripIds(mapToIdType(fishingActivitySpecifiedFishingTripIDS(fishingActivity, faReportDocument)));
            faArrivalFact.setFaTypesPerTrip(fishingActivitiesWithTripIds);
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_TRIP, ID).storeInRepo(faArrivalFact, "fishingTripIds");

            faArrivalFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(REASON_CODE).storeInRepo(faArrivalFact, REASON_CODE_PROP);

            faArrivalFact.setOccurrenceDateTime(fishingActivity.getOccurrenceDateTime());
            xPathUtil.appendWithoutWrapping(partialXpath).append(OCCURRENCE_DATE_TIME).storeInRepo(faArrivalFact, OCCURRENCE_DATE_TIME_PROP);
        }
        faArrivalFact.setFaReportTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faArrivalFact, "faReportTypeCode");

        FLUXReportDocument relatedFLUXReportDocument = faReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument != null) {
            faArrivalFact.setFaReportPurposeCode(mapToCodeType(relatedFLUXReportDocument.getPurposeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, PURPOSE_CODE).storeInRepo(faArrivalFact, "faReportPurposeCode");
        }

        return faArrivalFact;
    }

    public FaQueryFact generateFactsForFaQuery(FAQuery faQuery) {
        String partialXpath = xPathUtil.getValue();
        if (faQuery == null) {
            return null;
        }

        FaQueryFact faQueryFact = new FaQueryFact();
        faQueryFact.setSenderOrReceiver(senderReceiver);

        faQueryFact.setId(mapToIdType(faQuery.getID()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(faQueryFact, ID_PROP);

        faQueryFact.setTypeCode(mapToCodeType(faQuery.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faQueryFact, TYPE_CODE_PROP);

        faQueryFact.setSubmittedDateTime(getDate(faQuery.getSubmittedDateTime()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(SUBMITTED_DATE_TIME).storeInRepo(faQueryFact, SUBMITTED_DATE_TIME_PROP);

        faQueryFact.setSubmittedDateTimeString(getDateXMLString(faQuery.getSubmittedDateTime()));

        faQueryFact.setSpecifiedDelimitedPeriod(faQuery.getSpecifiedDelimitedPeriod());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_DELIMITED_PERIOD).storeInRepo(faQueryFact, SPECIFIED_DELIMITED_PERIOD_PROP);

        if (faQuery.getSpecifiedDelimitedPeriod() != null) {
            faQueryFact.setSdpStartDateTimeString(getDateXMLString(faQuery.getSpecifiedDelimitedPeriod().getStartDateTime()));
            faQueryFact.setSdpEndDateTimeString(getDateXMLString(faQuery.getSpecifiedDelimitedPeriod().getEndDateTime()));
        }

        FLUXParty submitterFLUXParty = faQuery.getSubmitterFLUXParty();
        if (submitterFLUXParty != null) {
            xPathUtil.appendWithoutWrapping(partialXpath).append(SUBMITTER_FLUX_PARTY, ID).storeInRepo(faQueryFact, SUBMITTED_FLUX_PARTY_IDS_PROP);
            faQueryFact.setSubmittedFLUXPartyIds(mapToIdType(submitterFLUXParty.getIDS()));
        }

        List<FAQueryParameter> simpleFAQueryParameters = faQuery.getSimpleFAQueryParameters();
        if (CollectionUtils.isNotEmpty(simpleFAQueryParameters)) {
            List<CodeType> codeTypes = new ArrayList<>();
            xPathUtil.appendWithoutWrapping(partialXpath).append(SIMPLE_FA_QUERY_PARAMETER, TYPE_CODE).storeInRepo(faQueryFact, SIMPLE_FA_QUERY_PARAMETER_TYPE_CODES_PROP);

            for (FAQueryParameter faQueryParameter : simpleFAQueryParameters) {
                CodeType codeType = mapToCodeType(faQueryParameter.getTypeCode());
                if (codeType != null) {
                    codeTypes.add(codeType);
                }
            }
            faQueryFact.setSimpleFAQueryParameterTypeCodes(codeTypes);
        }

        return faQueryFact;
    }

    public List<FaQueryParameterFact> generateFactsForFaQueryParameters(List<FAQueryParameter> faQueryParameters, FAQuery faQuery) {
        String partialXpath = xPathUtil.getValue();

        List<FaQueryParameterFact> faQueryParameterFacts = new ArrayList<>();
        int index = 1;
        if (CollectionUtils.isNotEmpty(faQueryParameters) && faQuery != null) {
            for (FAQueryParameter faQueryParameter : faQueryParameters) {
                FaQueryParameterFact fact = new FaQueryParameterFact();

                String partialWithParameter = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SIMPLE_FA_QUERY_PARAMETER, index).getValue();

                fact.setTypeCode(mapToCodeType(faQueryParameter.getTypeCode()));
                xPathUtil.appendWithoutWrapping(partialWithParameter).append(TYPE_CODE).storeInRepo(fact, TYPE_CODE_PROP);

                fact.setFaQueryTypeCode(mapToCodeType(faQuery.getTypeCode()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fact, FA_QUERY_TYPE_CODE_PROP);

                fact.setValueID(mapToIdType(faQueryParameter.getValueID()));
                xPathUtil.appendWithoutWrapping(partialWithParameter).append(VALUE_ID).storeInRepo(fact, VALUE_ID_PROP);

                fact.setValueCode(mapToCodeType(faQueryParameter.getValueCode()));
                xPathUtil.appendWithoutWrapping(partialWithParameter).append(VALUE_CODE).storeInRepo(fact, VALUE_CODE_PROP);

                faQueryParameterFacts.add(fact);
                index++;
            }
        }

        return faQueryParameterFacts;
    }

    public FaLandingFact generateFactsForLanding(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaLandingFact faLandingFact = new FaLandingFact();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {
            faLandingFact.setRelatedFluxLocationTypeCodes(getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faLandingFact, "relatedFluxLocationTypeCodes");

            if (fishingActivity.getSpecifiedFACatches() != null) {
                faLandingFact.setSpecifiedFaCatches(new ArrayList<>(fishingActivity.getSpecifiedFACatches()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH).storeInRepo(faLandingFact, "specifiedFaCatches");
            }
            faLandingFact.setFishingActivityCodeType(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faLandingFact, "fishingActivityCodeType");

            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faLandingFact.setRelatedFluxLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faLandingFact, "relatedFluxLocations");
            }

            faLandingFact.setSpecifiedFaCatchFluxLocationTypeCode(getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(), CODE_TYPE_FOR_FACATCH_FLUXLOCATION));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, SPECIFIED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faLandingFact, "specifiedFaCatchFluxLocationTypeCode");

            faLandingFact.setSpecifiedFaCatchTypeCode(getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(), CODE_TYPE_FOR_FACATCH_PROP));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faLandingFact, "specifiedFaCatchTypeCode");
        }
        if (faReportDocument != null) {
            faLandingFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faLandingFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);
        }

        return faLandingFact;
    }

    public AbstractRelocationOrTranshipmentFact generateFactsForNotificationOrDeclarationOfRelocationOrTranshipment(FishingActivity fishingActivity, FAReportDocument faReportDocument,
                                                                                                                    boolean isSubActivity) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }
        AbstractRelocationOrTranshipmentFact faNotificationOfRelocationOrTranshipmentFact;
        if(FaReportDocumentType.DECLARATION.name().equals(faReportDocument.getTypeCode().getValue())){
            faNotificationOfRelocationOrTranshipmentFact = new FaDeclarationOfRelocationOrTranshipmentFact();
        } else {
            faNotificationOfRelocationOrTranshipmentFact = new FaNotificationOfRelocationOrTranshipmentFact();
        }
        faNotificationOfRelocationOrTranshipmentFact.setSubActivity(isSubActivity);
        String partialXpath = xPathUtil.getValue();
        if (fishingActivity != null) {
            faNotificationOfRelocationOrTranshipmentFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, FISHING_ACTIVITY_TYPE_CODE_PROP);

            faNotificationOfRelocationOrTranshipmentFact.setRelatedFLUXLocations(fishingActivity.getRelatedFLUXLocations());
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, RELATED_FLUX_LOCATIONS_PROP);


            faNotificationOfRelocationOrTranshipmentFact.setFluxLocationTypeCode(getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION, TYPE_CODE).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, FLUX_LOCATION_TYPE_CODE_PROP);

            faNotificationOfRelocationOrTranshipmentFact.setFluxCharacteristicValueQuantity(getApplicableFLUXCharacteristicsValueQuantity(fishingActivity.getSpecifiedFLUXCharacteristics()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, VALUE_QUANTITY).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, "fluxCharacteristicValueQuantity");

            if (fishingActivity.getRelatedVesselTransportMeans() != null) {
                faNotificationOfRelocationOrTranshipmentFact.setRelatedVesselTransportMeans(new ArrayList<>(fishingActivity.getRelatedVesselTransportMeans()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_VESSEL_TRANSPORT_MEANS).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, RELATED_VESSEL_TRANSPORT_MEANS_PROP);

                faNotificationOfRelocationOrTranshipmentFact.setVesselTransportMeansRoleCodes(getVesselTransportMeansRoleCodes(fishingActivity.getRelatedVesselTransportMeans()));
                xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_VESSEL_TRANSPORT_MEANS, ROLE_CODE).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, "vesselTransportMeansRoleCodes");
            }

            faNotificationOfRelocationOrTranshipmentFact.setSpecifiedFACatches(fishingActivity.getSpecifiedFACatches());
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, SPECIFIED_FA_CATCHES_PROP);

            faNotificationOfRelocationOrTranshipmentFact.setFaCatchTypeCode(getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(), CODE_TYPE_FOR_FACATCH_PROP));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, TYPE_CODE).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, "faCatchTypeCode");

            faNotificationOfRelocationOrTranshipmentFact.setFaCatchSpeciesCodes(getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(), SPECIES_CODE_FOR_FACATCH_PROP));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FA_CATCH, SPECIES_CODE).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, "faCatchSpeciesCodes");

            faNotificationOfRelocationOrTranshipmentFact.setSpecifiedFLUXCharacteristics(fishingActivity.getSpecifiedFLUXCharacteristics());
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, "specifiedFLUXCharacteristics");

            faNotificationOfRelocationOrTranshipmentFact.setFluxCharacteristicTypeCodes(getApplicableFLUXCharacteristicsTypeCode(fishingActivity.getSpecifiedFLUXCharacteristics()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, TYPE_CODE).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, "fluxCharacteristicTypeCodes");

            faNotificationOfRelocationOrTranshipmentFact.setSpecifiedFLAPDocuments(fishingActivity.getSpecifiedFLAPDocuments());
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLAP_DOCUMENT).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, "specifiedFLAPDocuments");

            faNotificationOfRelocationOrTranshipmentFact.setFlapDocumentIdTypes(getFLAPDocumentIds(fishingActivity.getSpecifiedFLAPDocuments()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLAP_DOCUMENT, ID).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, "flapDocumentIdTypes");


        }
        if (faReportDocument != null) {
            faNotificationOfRelocationOrTranshipmentFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT, TYPE_CODE).storeInRepo(faNotificationOfRelocationOrTranshipmentFact, FA_REPORT_DOCUMENT_TYPE_CODE_PROP);
        }

        return faNotificationOfRelocationOrTranshipmentFact;
    }

    public ValidationQualityAnalysisFact generateFactsForValidationQualityAnalysis(ValidationQualityAnalysis validationQualityAnalysis) {
        if (validationQualityAnalysis == null) {
            return null;
        }

        ValidationQualityAnalysisFact qualityAnalysisFact = new ValidationQualityAnalysisFact();
        String partialXpath = xPathUtil.getValue();

        xPathUtil.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(qualityAnalysisFact, "id");
        qualityAnalysisFact.setId(mapToIdType(validationQualityAnalysis.getID()));

        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(qualityAnalysisFact, "typeCode");
        qualityAnalysisFact.setTypeCode(mapToCodeType(validationQualityAnalysis.getTypeCode()));

        xPathUtil.appendWithoutWrapping(partialXpath).append(LEVEL_CODE).storeInRepo(qualityAnalysisFact, "levelCode");
        qualityAnalysisFact.setLevelCode(mapToCodeType(validationQualityAnalysis.getLevelCode()));

        xPathUtil.appendWithoutWrapping(partialXpath).append(RESULT).storeInRepo(qualityAnalysisFact, "results");
        qualityAnalysisFact.setResults(mapFromTextTypeToString(validationQualityAnalysis.getResults()));

        xPathUtil.appendWithoutWrapping(partialXpath).append(REFERENCED_ITEM).storeInRepo(qualityAnalysisFact, "referencedItems");
        qualityAnalysisFact.setReferencedItems(mapFromTextTypeToString(validationQualityAnalysis.getReferencedItems()));

        return qualityAnalysisFact;
    }

    private List<String> mapFromTextTypeToString(List<TextType> txTypeList) {
        if (CollectionUtils.isEmpty(txTypeList)) {
            return Collections.emptyList();
        }
        List<String> strList = new ArrayList<>();
        for (TextType txType : txTypeList) {
            strList.add(txType.getValue());
        }
        return strList;
    }

    public eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType mapToCodeType(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType) {
        if (codeType == null) {
            return null;
        }

        boolean notBlankValue = StringUtils.isNotBlank(codeType.getValue());

        if (notBlankValue){
            eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType codeType1 = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
            codeType1.setListId(codeType.getListID());
            codeType1.setValue(codeType.getValue());
            return codeType1;
        }
        return null;
    }

    public MeasureType mapToMeasureType(QuantityType quantityType) {
        if (quantityType == null) {
            return null;
        }

        boolean notNullValue = quantityType.getValue() != null;
        if (notNullValue){
            MeasureType measureType1 = new MeasureType();
            measureType1.setUnitCode(quantityType.getUnitCode());
            measureType1.setValue(quantityType.getValue());
            return measureType1;
        }
        return null;
    }

    public MeasureType mapToMeasureType(un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType measureType) {
        if (measureType == null) {
            return null;
        }

        boolean notNullValue = measureType.getValue() != null;

        if (notNullValue) {
            MeasureType measureType1 = new MeasureType();
            measureType1.setUnitCode(measureType.getUnitCode());
            measureType1.setValue(measureType.getValue());
            return measureType1;
        }
        return null;
    }

    public IdType mapToIdType(IDType idType) {
        return idType == null ? null : new IdType(idType.getValue(), idType.getSchemeID());
    }

    private List<IdType> mapToIdType(List<IDType> idTypes) {
        List<IdType> idTypeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(idTypes)) {
            for (IDType iDType : idTypes) {
                IdType idType = mapToIdType(iDType);
                if (idType != null) {
                    IdType idType1 = mapToIdType(iDType);
                    if (idType1 != null){
                        idTypeList.add(idType1);
                    }
                }
            }
        }
        return idTypeList;
    }

    private List<CodeType> mapToCodeTypes(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> codeTypes) {
        if (codeTypes == null) {
            return emptyList();
        }
        List<CodeType> codeTypeArrayList = new ArrayList<>();
        for (un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType : codeTypes) {
            CodeType codeType1 = mapToCodeType(codeType);
            if (codeType1 != null){
                codeTypeArrayList.add(codeType1);
            }
        }
        return codeTypeArrayList;
    }

    public List<MeasureType> mapToMeasureType(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType> measureTypes) {
        if (measureTypes == null) {
            return emptyList();
        }

        List<MeasureType> list = new ArrayList<>();
        for (un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType measureType : measureTypes) {
            MeasureType measureType1 = mapToMeasureType(measureType);
            if (measureType1 != null){
                list.add(measureType1);
            }
        }
        return list;
    }

    private DateTimeType faReportDocumentsRelatedFLUXReportDocumentCreationDateTime(FAReportDocument fAReportDocument) {

        if (fAReportDocument == null) {
            return null;
        }
        FLUXReportDocument relatedFLUXReportDocument = fAReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument == null) {
            return null;
        }
        DateTimeType creationDateTime = relatedFLUXReportDocument.getCreationDateTime();
        if (creationDateTime == null) {
            return null;
        }
        return creationDateTime;
    }

    private List<IDType> faReportDocumentsRelatedFLUXReportDocumentIDS(FAReportDocument fAReportDocument) {

        if (fAReportDocument == null) {
            return emptyList();
        }
        FLUXReportDocument relatedFLUXReportDocument = fAReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument == null) {
            return emptyList();
        }
        List<IDType> iDS = relatedFLUXReportDocument.getIDS();
        if (iDS == null) {
            return emptyList();
        }
        return iDS;
    }

    private List<IDType> faReportDocumentsRelatedFLUXReportDocumentOwnerFLUXPartyIDS(FAReportDocument fAReportDocument) {

        if (fAReportDocument == null) {
            return emptyList();
        }
        FLUXReportDocument relatedFLUXReportDocument = fAReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument == null) {
            return emptyList();
        }
        FLUXParty ownerFLUXParty = relatedFLUXReportDocument.getOwnerFLUXParty();
        if (ownerFLUXParty == null) {
            return emptyList();
        }
        List<IDType> iDS = ownerFLUXParty.getIDS();
        if (iDS == null) {
            return emptyList();
        }
        return iDS;
    }

    private IDType faReportDocumentsRelatedFLUXReportDocumentReferencedID(FAReportDocument fAReportDocument) {

        if (fAReportDocument == null) {
            return null;
        }
        FLUXReportDocument relatedFLUXReportDocument = fAReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument == null) {
            return null;
        }
        IDType referencedID = relatedFLUXReportDocument.getReferencedID();
        if (referencedID == null) {
            return null;
        }
        return referencedID;
    }

    private un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType faReportDocumentsRelatedFLUXReportDocumentPurposeCode(FAReportDocument fAReportDocument) {

        if (fAReportDocument == null) {
            return null;
        }
        FLUXReportDocument relatedFLUXReportDocument = fAReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType purposeCode = relatedFLUXReportDocument.getPurposeCode();
        if (purposeCode == null) {
            return null;
        }
        return purposeCode;
    }

    private DateTimeType fluxfaReportMessageFLUXReportDocumentCreationDateTime(FLUXFAReportMessage fLUXFAReportMessage) {

        if (fLUXFAReportMessage == null) {
            return null;
        }
        FLUXReportDocument fLUXReportDocument = fLUXFAReportMessage.getFLUXReportDocument();
        if (fLUXReportDocument == null) {
            return null;
        }
        DateTimeType creationDateTime = fLUXReportDocument.getCreationDateTime();
        if (creationDateTime == null) {
            return null;
        }
        return creationDateTime;
    }

    private List<IDType> fluxfaReportMessageFLUXReportDocumentIDS(FLUXFAReportMessage fLUXFAReportMessage) {

        if (fLUXFAReportMessage == null) {
            return emptyList();
        }
        FLUXReportDocument fLUXReportDocument = fLUXFAReportMessage.getFLUXReportDocument();
        if (fLUXReportDocument == null) {
            return emptyList();
        }
        List<IDType> iDS = fLUXReportDocument.getIDS();
        if (iDS == null) {
            return emptyList();
        }
        return iDS;
    }

    private IDType fluxfaReportMessageFLUXReportDocumentReferencedID(FLUXFAReportMessage fLUXFAReportMessage) {

        if (fLUXFAReportMessage == null) {
            return null;
        }
        FLUXReportDocument fLUXReportDocument = fLUXFAReportMessage.getFLUXReportDocument();
        if (fLUXReportDocument == null) {
            return null;
        }
        IDType referencedID = fLUXReportDocument.getReferencedID();
        if (referencedID == null) {
            return null;
        }
        return referencedID;
    }

    private List<IDType> fluxfaReportMessageFLUXReportDocumentOwnerFLUXPartyIDS(FLUXFAReportMessage fLUXFAReportMessage) {

        if (fLUXFAReportMessage == null) {
            return emptyList();
        }
        FLUXReportDocument fLUXReportDocument = fLUXFAReportMessage.getFLUXReportDocument();
        if (fLUXReportDocument == null) {
            return emptyList();
        }
        FLUXParty ownerFLUXParty = fLUXReportDocument.getOwnerFLUXParty();
        if (ownerFLUXParty == null) {
            return emptyList();
        }
        List<IDType> iDS = ownerFLUXParty.getIDS();
        if (iDS == null) {
            return emptyList();
        }
        return iDS;
    }

    private un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType fluxfaReportMessageFLUXReportDocumentPurposeCode(FLUXFAReportMessage fLUXFAReportMessage) {

        if (fLUXFAReportMessage == null) {
            return null;
        }
        FLUXReportDocument fLUXReportDocument = fLUXFAReportMessage.getFLUXReportDocument();
        if (fLUXReportDocument == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType purposeCode = fLUXReportDocument.getPurposeCode();
        if (purposeCode == null) {
            return null;
        }
        return purposeCode;
    }

    public IDType vesselTransportMeanRegistrationVesselCountryID(VesselTransportMeans vesselTransportMeans) {
        if (vesselTransportMeans == null) {
            return null;
        }
        VesselCountry registrationVesselCountry = vesselTransportMeans.getRegistrationVesselCountry();
        if (registrationVesselCountry == null) {
            return null;
        }
        IDType iD = registrationVesselCountry.getID();
        if (iD == null) {
            return null;
        }
        return iD;
    }

    private IdType structuredAddressCountryIDValue(StructuredAddress structuredAddress) {
        if (structuredAddress == null) {
            return null;
        }
        IDType countryID = structuredAddress.getCountryID();
        if (countryID == null) {
            return null;
        }
        String value = countryID.getValue();
        String schemeID = countryID.getSchemeID();
        if (schemeID == null && value == null) {
            return null;
        }
        IdType idType = new IdType();
        idType.setSchemeId(schemeID);
        idType.setValue(value);
        return idType;
    }

    private String getValueFromTextType(TextType textType) {
        return textType != null ? textType.getValue() : null;
    }

    private List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> faCatchesSpecifiedSizeDistributionClassCodes(FACatch fACatch) {

        if (fACatch == null) {
            return emptyList();
        }
        SizeDistribution specifiedSizeDistribution = fACatch.getSpecifiedSizeDistribution();
        if (specifiedSizeDistribution == null) {
            return emptyList();
        }
        List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> classCodes = specifiedSizeDistribution.getClassCodes();
        if (classCodes == null) {
            return emptyList();
        }
        return classCodes;
    }

    private BigDecimal fishingActivityOperationsQuantityValue(FishingActivity fishingActivity) {

        if (fishingActivity == null) {
            return null;
        }
        QuantityType operationsQuantity = fishingActivity.getOperationsQuantity();
        if (operationsQuantity == null) {
            return null;
        }
        BigDecimal value = operationsQuantity.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private List<IDType> fishingActivitySpecifiedFishingTripIDS(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        List<IDType> iDS = emptyList();
        if (fishingActivity != null) {
            FLUXReportDocument relatedFLUXReportDocument = faReportDocument.getRelatedFLUXReportDocument();
            if (relatedFLUXReportDocument != null) {
                un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType purposeCode = relatedFLUXReportDocument.getPurposeCode();
                if (purposeCode != null && "9".equals(purposeCode.getValue())) {
                    FishingTrip specifiedFishingTrip = fishingActivity.getSpecifiedFishingTrip();
                    if (specifiedFishingTrip != null) {
                        iDS = specifiedFishingTrip.getIDS();
                    }
                }
            }
        }
        return iDS;
    }

    private un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType faCatchSpecifiedSizeDistributionCategoryCode(FACatch fACatch) {

        if (fACatch == null) {
            return null;
        }
        SizeDistribution specifiedSizeDistribution = fACatch.getSpecifiedSizeDistribution();
        if (specifiedSizeDistribution == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType categoryCode = specifiedSizeDistribution.getCategoryCode();
        if (categoryCode == null) {
            return null;
        }
        return categoryCode;
    }

    private static List<NumericType> mapAAPProcessList(List<AAPProcess> aapProcesses) {
        List<NumericType> numericTypeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(aapProcesses)) {
            for (AAPProcess aapProcess : aapProcesses) {
                un.unece.uncefact.data.standard.unqualifieddatatype._20.NumericType conversionFactorNumeric = aapProcess.getConversionFactorNumeric();
                if (conversionFactorNumeric != null) {
                    NumericType numericType = new NumericType();
                    numericType.setValue(conversionFactorNumeric.getValue());
                    numericType.setFormat(conversionFactorNumeric.getFormat());
                    numericTypeList.add(numericType);
                }
            }
        }
        return numericTypeList;
    }

    private static List<IdType> mapFLUXLocationIDs(List<FLUXLocation> fluxLocations) {
        List<IdType> idTypeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fluxLocations)) {
            for (FLUXLocation fluxLocation : fluxLocations) {
                IDType id = fluxLocation.getID();
                if (id != null) {
                    IdType idType = new IdType();
                    idType.setSchemeId(fluxLocation.getID().getSchemeID());
                    idType.setValue(fluxLocation.getID().getValue());
                    idTypeList.add(idType);
                }
            }
        }
        return idTypeList;
    }

    private List<CodeType> mapFluxLocationsToCodeTypes(List<FLUXLocation> locations) {
        List<CodeType> typeCodes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(locations)) {
            for (FLUXLocation fluxLocation : locations) {
                CodeType codeType = mapToCodeType(fluxLocation.getTypeCode());
                if(codeType != null){
                    typeCodes.add(codeType);
                }
            }
        }
        return typeCodes;
    }

    private List<MeasureType> mapDurationMeasure(List<DelimitedPeriod> delimitedPeriods) {
        List<MeasureType> measureTypes = null;
        if (CollectionUtils.isNotEmpty(delimitedPeriods)) {
            measureTypes = new ArrayList<>();
            for (DelimitedPeriod delimitedPeriod : delimitedPeriods) {
                if (delimitedPeriod.getDurationMeasure() != null) {
                    MeasureType measureType = mapToMeasureType(delimitedPeriod.getDurationMeasure());
                    if (measureType != null){
                        measureTypes.add(measureType);
                    }
                }
            }
        }
        return measureTypes;
    }

    private static List<FishingTrip> mapRelatedFishingTrips(List<FishingActivity> relatedFishingActivities) {
        List<FishingTrip> fishingTrips = null;
        if (CollectionUtils.isNotEmpty(relatedFishingActivities)) {
            fishingTrips = new ArrayList<>();
            for (FishingActivity fishingActivity : relatedFishingActivities) {
                if (fishingActivity.getSpecifiedFishingTrip() != null) {
                    fishingTrips.add(fishingActivity.getSpecifiedFishingTrip());
                }
            }
        }
        return fishingTrips;
    }

    private static List<FLUXLocation> getFluxLocations(List<FishingActivity> relatedFishingActivities) {
        List<FLUXLocation> fluxLocations = null;
        if (CollectionUtils.isNotEmpty(relatedFishingActivities)) {
            fluxLocations = new ArrayList<>();
            for (FishingActivity activity : relatedFishingActivities) {
                if (activity.getRelatedFLUXLocations() != null) {
                    fluxLocations.addAll(activity.getRelatedFLUXLocations());
                }
            }
        }
        return fluxLocations;
    }

    private List<CodeType> mapFromContactPartyToCodeType(List<ContactParty> contactPartyList) {
        List<CodeType> codeTypes = null;
        if (!CollectionUtils.isEmpty(contactPartyList)) {
            codeTypes = new ArrayList<>();
            for (ContactParty contactParty : contactPartyList) {
                List<CodeType> codeTypes1 = mapToCodeTypes(contactParty.getRoleCodes());
                if (CollectionUtils.isNotEmpty(codeTypes1)){
                    codeTypes.addAll(mapToCodeTypes(contactParty.getRoleCodes()));
                }
            }
        }
        return codeTypes;
    }

    private static List<ContactPerson> mapToContactPersonList(List<ContactParty> contactPartyList) {
        List<ContactPerson> contactPersonList = null;
        if (CollectionUtils.isNotEmpty(contactPartyList)) {
            contactPersonList = new ArrayList<>();
            for (ContactParty contactParty : contactPartyList) {
                contactPersonList.addAll(contactParty.getSpecifiedContactPersons());
            }
        }
        return contactPersonList;
    }

    private static String dateTimeAsString(DateTimeType dateTimeType) {
        String dateAsString = null;

        if (dateTimeType != null) {
            try {
                if (dateTimeType.getDateTime() != null) {
                    dateAsString = dateTimeType.getDateTime().toString();
                }
            } catch (Exception e) {
                log.debug("Error while trying to parse dateTimeType", e);
            }
        }
        return dateAsString;
    }

    private Date getDate(DateTimeType dateTimeType) {
        Date date = null;
        if (dateTimeType != null) {
            try {
                if (dateTimeType.getDateTime() != null) {
                    date = dateTimeType.getDateTime().toGregorianCalendar().getTime();
                }
            } catch (Exception e) {
                log.debug("Error while trying to parse dateTimeType", e);
            }
        }

        return date;
    }

    private String getDateXMLString(DateTimeType dateTimeType) {
        String xmlDateString = null;

        if (dateTimeType != null && dateTimeType.getDateTime() != null) {
            xmlDateString = dateTimeType.getDateTime().toString();
        }

        return xmlDateString;
    }

    private static List<AAPProduct> getAppliedProcessAAPProducts(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return emptyList();
        }
        List<AAPProduct> aapProducts = new ArrayList<>();

        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                aapProducts.addAll(aapProcess.getResultAAPProducts());
            }
        }
        return aapProducts;
    }

    /**
     * Extract List<MeasureType> from AAPProduct. List will be created from different attributes of AAPProduct based on parameter methodToChoose.
     *
     * @param appliedAAPProcesses
     * @param methodToChoose
     * @return measureTypes
     */
    private List<MeasureType> getMeasureTypeFromAAPProcess(List<AAPProcess> appliedAAPProcesses, String methodToChoose) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return emptyList();
        }
        List<MeasureType> measureTypes = new ArrayList<>();
        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                mapAapProductToMeasureType(methodToChoose, measureTypes, aapProcess);
            }
        }
        return measureTypes;
    }

    private void mapAapProductToMeasureType(String methodToChoose, List<MeasureType> measureTypes, AAPProcess aapProcess) {
        for (AAPProduct aapProduct : aapProcess.getResultAAPProducts()) {
            switch (methodToChoose) {
                case PACKAGING_UNIT_QUANTITY:
                    if (aapProduct.getPackagingUnitQuantity() != null) {
                        MeasureType measureType = mapToMeasureType(aapProduct.getPackagingUnitQuantity());
                        if (measureType != null){
                            measureTypes.add(measureType);
                        }
                    }
                    break;
                case AVERAGE_WEIGHT_MEASURE:
                    if (aapProduct.getPackagingUnitAverageWeightMeasure() != null) {
                        MeasureType measureType = mapToMeasureType(aapProduct.getPackagingUnitAverageWeightMeasure());
                        if (measureType != null){
                            measureTypes.add(measureType);
                        }
                    }
                    break;
                case WEIGHT_MEASURE:
                    if (aapProduct.getWeightMeasure() != null) {
                        MeasureType measureType = mapToMeasureType(aapProduct.getWeightMeasure());
                        if (measureType != null){
                            measureTypes.add(measureType);
                        }
                    }
                    break;
                case UNIT_QUANTITY:
                    if (aapProduct.getUnitQuantity() != null) {
                        MeasureType measureType = mapToMeasureType(aapProduct.getUnitQuantity());
                        if (measureType != null){
                            measureTypes.add(measureType);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private List<CodeType> getAAPProductPackagingTypeCode(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                for (AAPProduct aapProduct : aapProcess.getResultAAPProducts()) {
                    if (aapProduct.getPackagingTypeCode() != null) {
                        CodeType codeType = mapToCodeType(aapProduct.getPackagingTypeCode());
                        if (codeType != null){
                            codeTypes.add(codeType);
                        }
                    }
                }
            }
        }
        return codeTypes;
    }

    private List<CodeType> getAppliedProcessTypeCodes(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();

        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getTypeCodes())) {
                codeTypes.addAll(mapToCodeTypes(aapProcess.getTypeCodes()));
            }
        }
        return codeTypes;
    }

    private List<CodeType> getApplicableFLUXCharacteristicsTypeCode(List<FLUXCharacteristic> fluxCharacteristics) {
        if (CollectionUtils.isEmpty(fluxCharacteristics)) {
            return emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FLUXCharacteristic fluxCharacteristic : fluxCharacteristics) {
            if (fluxCharacteristic.getTypeCode() != null) {
                CodeType codeType = mapToCodeType(fluxCharacteristic.getTypeCode());
                if (codeType != null){
                    codeTypes.add(codeType);
                }
            }
        }
        return codeTypes;
    }

    private List<MeasureType> getApplicableFLUXCharacteristicsValueQuantity(List<FLUXCharacteristic> fluxCharacteristics) {
        if (CollectionUtils.isEmpty(fluxCharacteristics)) {
            return emptyList();
        }
        List<MeasureType> measureTypes = new ArrayList<>();
        for (FLUXCharacteristic fluxCharacteristic : fluxCharacteristics) {
            if (fluxCharacteristic.getValueQuantity() != null) {
                MeasureType measureType = mapToMeasureType(fluxCharacteristic.getValueQuantity());
                if (measureType != null){
                    measureTypes.add(measureType);
                }
            }
        }
        return measureTypes;
    }

    private List<CodeType> getFLUXLocationTypeCodes(List<FLUXLocation> fluxLocations) {
        if (CollectionUtils.isEmpty(fluxLocations)) {
            return emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FLUXLocation fluxLocation : fluxLocations) {
            if (fluxLocation.getTypeCode() != null) {
                CodeType codeType = mapToCodeType(fluxLocation.getTypeCode());
                if (codeType != null){
                    codeTypes.add(codeType);
                }
            }
        }
        return codeTypes;
    }

    private List<CodeType> getFLUXLocationRFMOCodes(List<FLUXLocation> fluxLocations) {
        if (CollectionUtils.isEmpty(fluxLocations)) {
            return emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FLUXLocation fluxLocation : fluxLocations) {
            if (fluxLocation.getRegionalFisheriesManagementOrganizationCode() != null) {
                CodeType codeType = mapToCodeType(fluxLocation.getRegionalFisheriesManagementOrganizationCode());
                if (codeType != null){
                    codeTypes.add(codeType);
                }
            }
        }
        return codeTypes;
    }

    private List<CodeType> getFishingGearRoleCodes(List<FishingGear> fishingGears) {
        if (CollectionUtils.isEmpty(fishingGears)) {
            return emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FishingGear fishingGear : fishingGears) {

            if (CollectionUtils.isNotEmpty(fishingGear.getRoleCodes())) {
                codeTypes.addAll(mapToCodeTypes(fishingGear.getRoleCodes()));
            }
        }
        return codeTypes;
    }

    private List<CodeType> getVesselTransportMeansRoleCodes(List<VesselTransportMeans> vesselTransportMeanses) {
        if (CollectionUtils.isEmpty(vesselTransportMeanses)) {
            return emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMeanses) {

            if (vesselTransportMeans.getRoleCode() != null) {
                CodeType codeType = mapToCodeType(vesselTransportMeans.getRoleCode());
                if (codeType != null){
                    codeTypes.add(codeType);
                }
            }
        }
        return codeTypes;
    }

    /**
     * Fetch List<CodeType> from FACatch. CodeType List will be created from FACatch based on parameter methodToChoose
     * i.e code type for FACatch or code type for specified fluxlocation
     *
     * @param faCatch // * @param methodToChoose
     * @return
     */
    private List<CodeType> getCodeTypesFromFaCatch(List<FACatch> faCatch, String methodToChoose) {
        if (CollectionUtils.isEmpty(faCatch)) {
            return java.util.Collections.emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FACatch faCatches : faCatch) {

            switch (methodToChoose) {
                case CODE_TYPE_FOR_FACATCH_PROP:
                    if (faCatches.getTypeCode() != null) {
                        CodeType codeType = mapToCodeType(faCatches.getTypeCode());
                        if (codeType != null){
                            codeTypes.add(codeType);
                        }
                    }
                    break;
                case SPECIES_CODE_FOR_FACATCH_PROP:
                    if (faCatches.getSpeciesCode() != null) {
                        CodeType codeType = mapToCodeType(faCatches.getSpeciesCode());
                        if (codeType != null){
                            codeTypes.add(codeType);
                        }
                    }
                    break;
                case CODE_TYPE_FOR_FACATCH_FLUXLOCATION:
                    mapSpecifiedFluxLocationsCodeTypeList(codeTypes, faCatches);
                    break;
                default:
                    break;
            }

        }
        return codeTypes;
    }


    public List<IdType> getFLAPDocumentIds(List<FLAPDocument> flapDocuments) {
        if (CollectionUtils.isEmpty(flapDocuments)) {
            return emptyList();
        }
        List<IdType> idTypes = new ArrayList<>();
        for (FLAPDocument flapDocument : flapDocuments) {

            if (flapDocument.getID() != null) {
                IdType idType = mapToIdType(flapDocument.getID());
                if (idType != null){
                    idTypes.add(idType);
                }
            }
        }
        return idTypes;
    }

    private void mapSpecifiedFluxLocationsCodeTypeList(List<CodeType> codeTypes, FACatch faCatches) {
        if (CollectionUtils.isNotEmpty(faCatches.getSpecifiedFLUXLocations())) {
            for (FLUXLocation specifiedFluxLocation : faCatches.getSpecifiedFLUXLocations()) {
                if (specifiedFluxLocation.getTypeCode() != null) {
                    CodeType codeType = mapToCodeType(specifiedFluxLocation.getTypeCode());
                    if (codeType != null){
                        codeTypes.add(codeType);
                    }
                }
            }
        }
    }

    private void setxPathUtil(XPathStringWrapper xPathUtil1) {
        this.xPathUtil = xPathUtil1;
    }

    public void setFishingActivitiesWithTripIds(List<String> fishingActivitiesWithTripIds) {
        this.fishingActivitiesWithTripIds = fishingActivitiesWithTripIds;
    }

    public void setSenderReceiver(String value) {
        senderReceiver = value;
    }

    public List<IdType> getFaReportMessageIds() {
        return faReportMessageIds;
    }

    public List<IdType> getFaRelatedReportIds() {
        return faRelatedReportIds;
    }

    public List<IdType> getFaQueryIds() {
        return faQueryIds;
    }

    public List<VesselTransportMeansDto> getTransportMeans() {
        return transportMeans;
    }

    public void setTransportMeans(List<VesselTransportMeansDto> transportMeans) {
        this.transportMeans = transportMeans;
    }

    public  List<VesselTransportMeansDto> getTransportMeansDtos(){
        return transportMeans;
    }

    public void setFaRelatedReportIds(List<IdType> faRelatedReportIds) {
        this.faRelatedReportIds = faRelatedReportIds;
    }

}
