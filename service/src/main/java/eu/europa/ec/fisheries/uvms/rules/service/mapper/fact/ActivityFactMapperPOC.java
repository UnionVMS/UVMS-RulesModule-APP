/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.mapper.fact;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.QuantityType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.*;

/**
 * Created by kovian on 14/06/2017.
 */
public class ActivityFactMapperPOC {

    public static final ActivityFactMapperPOC INSTANCE = new ActivityFactMapperPOC();

    XPathStringWrapper xPathUtil = XPathStringWrapper.INSTANCE;

    String AAP_PRODUCT_PACKAGING_UNIT_QUANTITY = PACKAGING_UNIT_QUANTITY;
    String AAP_PRODUCT_WEIGHT_MEASURE = WEIGHT_MEASURE;
    String AAP_PRODUCT_AVERAGE_WEIGHT_MEASURE = "AverageWeightMeasure";
    String AAP_PRODUCT_UNIT_QUANTITY = UNIT_QUANTITY;
    String CODE_TYPE_FOR_FACATCH_FLUXLOCATION = "facatchFluxlocationTypeCode";
    String CODE_TYPE_FOR_FACATCH = "facatchTypeCode";


    private ActivityFactMapperPOC(){
        super();
    }

    public FaReportDocumentFact generateFactForFaReportDocument(FAReportDocument faReportDocument) {
        if (faReportDocument == null) {
            xPathUtil.clear();
            return null;
        }

        String partialXpath = xPathUtil.getValue();

        FaReportDocumentFact faReportDocumentFact = new FaReportDocumentFact();

        faReportDocumentFact.setCreationDateTime(CustomMapper.getDate(faReportDocumentsRelatedFLUXReportDocumentCreationDateTime(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, CREATION_DATE_TIME).storeInRepo(faReportDocumentFact, "creationDateTime");

        faReportDocumentFact.setAcceptanceDateTime(CustomMapper.getDate(faReportDocument.getAcceptanceDateTime()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(ACCEPTANCE_DATE_TIME).storeInRepo(faReportDocumentFact, "acceptanceDateTime");

        faReportDocumentFact.setIds(mapToIdType(faReportDocumentsRelatedFLUXReportDocumentIDS(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, ID).storeInRepo(faReportDocumentFact, "ids");

        faReportDocumentFact.setOwnerFluxPartyIds(mapToIdType(faReportDocumentsRelatedFLUXReportDocumentOwnerFLUXPartyIDS(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, OWNER_FLUX_PARTY, ID).storeInRepo(faReportDocumentFact, "ownerFluxPartyIds");

        faReportDocumentFact.setUniqueIds(CustomMapper.getIds(faReportDocument.getRelatedFLUXReportDocument()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, ID).storeInRepo(faReportDocumentFact, "uniqueIds");

        faReportDocumentFact.setRelatedFLUXReportDocumentReferencedID(mapToCodeType(faReportDocumentsRelatedFLUXReportDocumentReferencedID(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, REFERENCED_ID).storeInRepo(faReportDocumentFact, "relatedFLUXReportDocumentReferencedID");

        faReportDocumentFact.setRelatedFLUXReportDocumentIDs(mapToIdType(faReportDocumentsRelatedFLUXReportDocumentIDS_(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, ID).storeInRepo(faReportDocumentFact, "relatedFLUXReportDocumentIDs");

        faReportDocumentFact.setPurposeCode(mapToCodeType(faReportDocumentsRelatedFLUXReportDocumentPurposeCode(faReportDocument)));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT, PURPOSE_CODE).storeInRepo(faReportDocumentFact, "purposeCode");

        faReportDocumentFact.setTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(faReportDocumentFact, "typeCode");

        faReportDocumentFact.setRelatedReportIDs(mapToIdType(faReportDocument.getRelatedReportIDs()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_REPORT_ID).storeInRepo(faReportDocumentFact, "relatedReportIDs");

        faReportDocumentFact.setRelatedFLUXReportDocument(faReportDocument.getRelatedFLUXReportDocument());
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_REPORT_DOCUMENT).storeInRepo(faReportDocumentFact, "");

        faReportDocumentFact.setSpecifiedVesselTransportMeans(faReportDocument.getSpecifiedVesselTransportMeans());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_VESSEL_TRANSPORT_MEANS).storeInRepo(faReportDocumentFact, "specifiedVesselTransportMeans");

        if (faReportDocument.getSpecifiedFishingActivities() != null) {
            faReportDocumentFact.setSpecifiedFishingActivities(new ArrayList<>(faReportDocument.getSpecifiedFishingActivities()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_ACTIVITY).storeInRepo(faReportDocumentFact, "specifiedFishingActivities");
        }

        return faReportDocumentFact;
    }


    public List<FaReportDocumentFact> generateFactForFaReportDocuments(List<FAReportDocument> faReportDocuments) {
        if (faReportDocuments == null) {
            return null;
        }
        int index = 1;
        List<FaReportDocumentFact> list = new ArrayList<>();
        for (FAReportDocument fAReportDocument : faReportDocuments) {
            xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
            list.add(generateFactForFaReportDocument(fAReportDocument));
        }

        return list;
    }


    public FishingActivityFact generateFactForFishingActivity(FishingActivity fishingActivity) {
        if (fishingActivity == null) {
            xPathUtil.clear();
            return null;
        }

        String partialXpath = xPathUtil.getValue();

        FishingActivityFact fishingActivityFact = new FishingActivityFact();

        if (fishingActivity.getSpecifiedDelimitedPeriods() != null) {
            fishingActivityFact.setDelimitedPeriods(new ArrayList<>(fishingActivity.getSpecifiedDelimitedPeriods()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_DELIMITED_PERIOD).storeInRepo(fishingActivityFact, "delimitedPeriods");
        }
        fishingActivityFact.setRelatedFishingTrip(CustomMapper.getRelatedFishingTrips(fishingActivity.getRelatedFishingActivities()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, SPECIFIED_FISHING_TRIP).storeInRepo(fishingActivityFact, "relatedFishingTrip");

        fishingActivityFact.setDurationMeasure(CustomMapper.getDurationMeasure(fishingActivity.getSpecifiedDelimitedPeriods()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_DELIMITED_PERIOD, DURATION_MEASURE).storeInRepo(fishingActivityFact, "durationMeasure");

        BigDecimal operatQuantity = fishingActivityOperationsQuantityValue(fishingActivity);
        if (operatQuantity != null) {
            fishingActivityFact.setOperationQuantity(operatQuantity.intValue());
            xPathUtil.appendWithoutWrapping(partialXpath).append(OPERATIONS_QUANTITY).storeInRepo(fishingActivityFact, "operationQuantity");
        }
        fishingActivityFact.setRelatedActivityFluxLocations(CustomMapper.getFluxLocations(fishingActivity.getRelatedFishingActivities()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, RELATED_FLUX_LOCATION).storeInRepo(fishingActivityFact, "relatedActivityFluxLocations");

        fishingActivityFact.setIsDateProvided(CustomMapper.isDatePresent(fishingActivity.getOccurrenceDateTime()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(OCCURRENCE_DATE_TIME).storeInRepo(fishingActivityFact, "isDateProvided");

        fishingActivityFact.setFluxCharacteristicsTypeCode(CustomMapper.getApplicableFLUXCharacteristicsTypeCode(fishingActivity.getSpecifiedFLUXCharacteristics()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FLUX_CHARACTERISTIC, TYPE_CODE).storeInRepo(fishingActivityFact, "fluxCharacteristicsTypeCode");

        fishingActivityFact.setRelatedDelimitedPeriods(CustomMapper.getDelimitedPeriod(fishingActivity.getRelatedFishingActivities()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY, SPECIFIED_DELIMITED_PERIOD).storeInRepo(fishingActivityFact, "relatedDelimitedPeriods");

        if (fishingActivity.getRelatedFishingActivities() != null) {
            fishingActivityFact.setRelatedFishingActivities(new ArrayList<>(fishingActivity.getRelatedFishingActivities()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FISHING_ACTIVITY).storeInRepo(fishingActivityFact, "relatedFishingActivities");
        }
        if (fishingActivity.getRelatedFLUXLocations() != null) {
            fishingActivityFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(RELATED_FLUX_LOCATION).storeInRepo(fishingActivityFact, "relatedFLUXLocations");
        }

        fishingActivityFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(REASON_CODE).storeInRepo(fishingActivityFact, "reasonCode");

        fishingActivityFact.setFisheryTypeCode(mapToCodeType(fishingActivity.getFisheryTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(FISHERY_TYPE_CODE).storeInRepo(fishingActivityFact, "fisheryTypeCode");

        fishingActivityFact.setSpeciesTargetCode(mapToCodeType(fishingActivity.getSpeciesTargetCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIES_TARGET_CODE).storeInRepo(fishingActivityFact, "speciesTargetCode");

        fishingActivityFact.setSpecifiedFishingTrip(fishingActivity.getSpecifiedFishingTrip());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_FISHING_TRIP).storeInRepo(fishingActivityFact, "specifiedFishingTrip");

        fishingActivityFact.setTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fishingActivityFact, "typeCode");

        fishingActivityFact.setOccurrenceDateTime(CustomMapper.getDate(fishingActivity.getOccurrenceDateTime()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(OCCURRENCE_DATE_TIME).storeInRepo(fishingActivityFact, "occurrenceDateTime");

        fishingActivityFact.setVesselRelatedActivityCode(mapToCodeType(fishingActivity.getVesselRelatedActivityCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(VESSEL_RELATED_ACTIVITY_CODE).storeInRepo(fishingActivityFact, "vesselRelatedActivityCode");

        return fishingActivityFact;
    }


    public FishingActivityFact generateFactForFishingActivity(FishingActivity fishingActivity, boolean isSubActivity) {
        if (fishingActivity == null) {
            return null;
        }

        FishingActivityFact fishingActivityFact = new FishingActivityFact();

        if (fishingActivity != null) {
            if (fishingActivity.getSpecifiedDelimitedPeriods() != null) {
                fishingActivityFact.setDelimitedPeriods(new ArrayList<>(fishingActivity.getSpecifiedDelimitedPeriods()));
            }
            fishingActivityFact.setRelatedFishingTrip(CustomMapper.getRelatedFishingTrips(fishingActivity.getRelatedFishingActivities()));
            fishingActivityFact.setDurationMeasure(CustomMapper.getDurationMeasure(fishingActivity.getSpecifiedDelimitedPeriods()));
            if (fishingActivityOperationsQuantityValue_(fishingActivity) != null) {
                fishingActivityFact.setOperationQuantity(fishingActivityOperationsQuantityValue_(fishingActivity).intValue());
            }
            fishingActivityFact.setRelatedActivityFluxLocations(CustomMapper.getFluxLocations(fishingActivity.getRelatedFishingActivities()));
            fishingActivityFact.setIsDateProvided(CustomMapper.isDatePresent(fishingActivity.getOccurrenceDateTime()));
            fishingActivityFact.setFluxCharacteristicsTypeCode(CustomMapper.getApplicableFLUXCharacteristicsTypeCode(fishingActivity.getSpecifiedFLUXCharacteristics()));
            fishingActivityFact.setRelatedDelimitedPeriods(CustomMapper.getDelimitedPeriod(fishingActivity.getRelatedFishingActivities()));
            if (fishingActivity.getRelatedFishingActivities() != null) {
                fishingActivityFact.setRelatedFishingActivities(new ArrayList<>(fishingActivity.getRelatedFishingActivities()));
            }
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                fishingActivityFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
            fishingActivityFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
            fishingActivityFact.setFisheryTypeCode(mapToCodeType(fishingActivity.getFisheryTypeCode()));
            fishingActivityFact.setSpeciesTargetCode(mapToCodeType(fishingActivity.getSpeciesTargetCode()));
            fishingActivityFact.setSpecifiedFishingTrip(fishingActivity.getSpecifiedFishingTrip());
            fishingActivityFact.setTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            fishingActivityFact.setOccurrenceDateTime(CustomMapper.getDate(fishingActivity.getOccurrenceDateTime()));
            fishingActivityFact.setVesselRelatedActivityCode(mapToCodeType(fishingActivity.getVesselRelatedActivityCode()));
        }
        fishingActivityFact.setIsSubActivity(isSubActivity);

        return fishingActivityFact;
    }


    public List<FishingActivityFact> generateFactForFishingActivities(List<FishingActivity> fishingActivities) {
        if (fishingActivities == null) {
            return null;
        }

        List<FishingActivityFact> list = new ArrayList<>();
        for (FishingActivity fishingActivity : fishingActivities) {
            list.add(generateFactForFishingActivity(fishingActivity));
        }

        return list;
    }


    public FluxFaReportMessageFact generateFactForFluxReportMessage(FLUXFAReportMessage fluxfaReportMessage) {
        if (fluxfaReportMessage == null) {
            return null;
        }

        FluxFaReportMessageFact fluxFaReportMessageFact = new FluxFaReportMessageFact();

        fluxFaReportMessageFact.setCreationDateTime(CustomMapper.getDate(fluxfaReportMessageFLUXReportDocumentCreationDateTime(fluxfaReportMessage)));
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FLUX_REPORT_DOCUMENT, CREATION_DATE_TIME).storeInRepo(fluxFaReportMessageFact, "creationDateTime");

        if (fluxfaReportMessage.getFAReportDocuments() != null) {
            fluxFaReportMessageFact.setFaReportDocuments(new ArrayList<>(fluxfaReportMessage.getFAReportDocuments()));
            xPathUtil.append(FLUXFA_REPORT_MESSAGE, FA_REPORT_DOCUMENT).storeInRepo(fluxFaReportMessageFact, "faReportDocuments");
        }
        fluxFaReportMessageFact.setIds(mapToIdType(fluxfaReportMessageFLUXReportDocumentIDS(fluxfaReportMessage)));
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FLUX_REPORT_DOCUMENT, ID).storeInRepo(fluxFaReportMessageFact, "ids");

        fluxFaReportMessageFact.setReferencedID(mapToCodeType(fluxfaReportMessageFLUXReportDocumentReferencedID(fluxfaReportMessage)));
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FLUX_REPORT_DOCUMENT, REFERENCED_ID).storeInRepo(fluxFaReportMessageFact, "referencedID");

        fluxFaReportMessageFact.setOwnerFluxPartyIds(mapToIdType(fluxfaReportMessageFLUXReportDocumentOwnerFLUXPartyIDS(fluxfaReportMessage)));
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FLUX_REPORT_DOCUMENT, OWNER_FLUX_PARTY, ID).storeInRepo(fluxFaReportMessageFact, "ownerFluxPartyIds");

        fluxFaReportMessageFact.setUniqueIds(CustomMapper.getIds(fluxfaReportMessage.getFLUXReportDocument()));
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FLUX_REPORT_DOCUMENT, ID).storeInRepo(fluxFaReportMessageFact, "uniqueIds");

        fluxFaReportMessageFact.setPurposeCode(mapToCodeType(fluxfaReportMessageFLUXReportDocumentPurposeCode(fluxfaReportMessage)));
        xPathUtil.append(FLUXFA_REPORT_MESSAGE, FLUX_REPORT_DOCUMENT, PURPOSE_CODE).storeInRepo(fluxFaReportMessageFact, "purposeCode");

        return fluxFaReportMessageFact;
    }

    public VesselTransportMeansFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean, boolean isCommingFromFaReportDocument) {
        if (vesselTransportMean == null) {
            return null;
        }
        VesselTransportMeansFact vesselTransportMeansFact = generateFactForVesselTransportMean(vesselTransportMean);
        vesselTransportMeansFact.setIsFromFaReport(isCommingFromFaReportDocument);
        return vesselTransportMeansFact;
    }

    public List<VesselTransportMeansFact> generateFactForVesselTransportMeans(List<VesselTransportMeans> vesselTransportMean) {
        if (vesselTransportMean == null) {
            xPathUtil.clear();
            return null;
        }
        List<VesselTransportMeansFact> list = new ArrayList<>();
        int index = 1;
        String strToAppend = xPathUtil.getValue();
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMean) {
            xPathUtil.appendWithoutWrapping(strToAppend).appendWithIndex("RelatedVesselTransportMeans", index);
            VesselTransportMeansFact vesselTransportMeansFact = generateFactForVesselTransportMean(vesselTransportMeans);
            list.add(vesselTransportMeansFact);
            index++;
        }
        return list;
    }


    public VesselTransportMeansFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean) {
        if (vesselTransportMean == null) {
            xPathUtil.clear();
            return null;
        }

        // Since every time we get the final value we clean the StringBuffer inside XPathStringWrapper, we need to store and use the initial
        // value which is to be used always (appended as the first string in the buffer).
        String toBeAppendedAlways = xPathUtil.getValue();

        VesselTransportMeansFact vesselTransportMeansFact = new VesselTransportMeansFact();

        vesselTransportMeansFact.setRegistrationVesselCountryId(mapToCodeType(vesselTransportMeanRegistrationVesselCountryID_(vesselTransportMean)));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(REGISTRATION_VESSEL_COUNTRY, ID).storeInRepo(vesselTransportMeansFact, "registrationVesselCountryId");

        vesselTransportMeansFact.setSpecifiedContactPersons(CustomMapper.mapToContactPersonList(vesselTransportMean.getSpecifiedContactParties()));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(SPECIFIED_CONTACT_PARTY, SPECIFIED_CONTACT_PERSON).storeInRepo(vesselTransportMeansFact, "specifiedContactPersons");

        vesselTransportMeansFact.setIds(mapToIdType(vesselTransportMean.getIDS()));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(ID).storeInRepo(vesselTransportMeansFact, "ids");

        vesselTransportMeansFact.setSpecifiedContactPartyRoleCodes(CustomMapper.mapToIdTypeList(vesselTransportMean.getSpecifiedContactParties()));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(SPECIFIED_CONTACT_PARTY, ROLE_CODE).storeInRepo(vesselTransportMeansFact, "specifiedContactPartyRoleCodes");

        vesselTransportMeansFact.setRoleCode(mapToCodeType(vesselTransportMean.getRoleCode()));
        xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(ROLE_CODE).storeInRepo(vesselTransportMeansFact, "roleCode");

        if (vesselTransportMean.getSpecifiedContactParties() != null) {
            vesselTransportMeansFact.setSpecifiedContactParties(new ArrayList<>(vesselTransportMean.getSpecifiedContactParties()));
            xPathUtil.appendWithoutWrapping(toBeAppendedAlways).append(SPECIFIED_CONTACT_PARTY).storeInRepo(vesselTransportMeansFact, "specifiedContactParties");
        }

        return vesselTransportMeansFact;
    }


    public StructuredAddressFact generateFactsForStructureAddress(StructuredAddress structuredAddress) {
        if (structuredAddress == null) {
            xPathUtil.clear();
            return null;
        }

        final String partialXpath = xPathUtil.getValue();

        StructuredAddressFact structuredAddressFact = new StructuredAddressFact();

        structuredAddressFact.setPostcodeCode(structuredAddressPostcodeCodeValue(structuredAddress));
        xPathUtil.appendWithoutWrapping(partialXpath).append(POSTCODE_CODE).storeInRepo(structuredAddressFact, "postcodeCode");

        structuredAddressFact.setCountryID(structuredAddressCountryIDValue(structuredAddress));
        xPathUtil.appendWithoutWrapping(partialXpath).append(COUNTRY_ID).storeInRepo(structuredAddressFact, "countryID");

        structuredAddressFact.setCityName(structuredAddressCityNameValue(structuredAddress));
        xPathUtil.appendWithoutWrapping(partialXpath).append(CITY_NAME).storeInRepo(structuredAddressFact, "cityName");

        structuredAddressFact.setStreetName(structuredAddressStreetNameValue(structuredAddress));
        xPathUtil.appendWithoutWrapping(partialXpath).append(STREET_NAME).storeInRepo(structuredAddressFact, "streetName");

        structuredAddressFact.setPlotIdentification(structuredAddressPlotIdentificationValue(structuredAddress));
        xPathUtil.appendWithoutWrapping(partialXpath).append(PLOT_IDENTIFICATION).storeInRepo(structuredAddressFact, "plotIdentification");

        return structuredAddressFact;
    }


    public List<StructuredAddressFact> generateFactsForStructureAddresses(List<StructuredAddress> structuredAddresses, String adressType) {
        if (structuredAddresses == null) {
            xPathUtil.clear();
            return null;
        }
        final String partialXpath = xPathUtil.getValue();
        List<StructuredAddressFact> list = new ArrayList<>();
        int index = 1;
        for (StructuredAddress structuredAddress : structuredAddresses) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(adressType, index);
            list.add(generateFactsForStructureAddress(structuredAddress));
        }
        return list;
    }


    public FishingGearFact generateFactsForFishingGear(FishingGear fishingGear) {
        if (fishingGear == null) {
            xPathUtil.clear();
            return null;
        }

        FishingGearFact fishingGearFact = new FishingGearFact();
        final String partialXpath = xPathUtil.getValue();

        fishingGearFact.setTypeCode(mapToCodeType(fishingGear.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fishingGearFact, "typeCode");

        if (fishingGear.getApplicableGearCharacteristics() != null) {
            fishingGearFact.setApplicableGearCharacteristics(new ArrayList<>(fishingGear.getApplicableGearCharacteristics()));
            xPathUtil.appendWithoutWrapping(partialXpath).append(APPLICABLE_GEAR_CHARACTERISTIC).storeInRepo(fishingGearFact, "applicableGearCharacteristics");
        }

        return fishingGearFact;
    }


    public List<FishingGearFact> generateFactsForFishingGears(List<FishingGear> fishingGears) {
        if (fishingGears == null) {
            xPathUtil.clear();
            return null;
        }
        final String partialXpath = xPathUtil.getValue();
        List<FishingGearFact> list = new ArrayList<>();
        int index = 1;
        for (FishingGear fishingGear : fishingGears) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FISHING_GEAR, index);
            list.add(generateFactsForFishingGear(fishingGear));
        }

        return list;
    }


    public GearCharacteristicsFact generateFactsForGearCharacteristic(GearCharacteristic gearCharacteristic) {
        if (gearCharacteristic == null) {
            return null;
        }

        GearCharacteristicsFact gearCharacteristicsFact = new GearCharacteristicsFact();

        gearCharacteristicsFact.setTypeCode(mapToCodeType(gearCharacteristic.getTypeCode()));

        return gearCharacteristicsFact;
    }


    public List<GearCharacteristicsFact> generateFactsForGearCharacteristics(List<GearCharacteristic> gearCharacteristics) {
        if (gearCharacteristics == null) {
            return null;
        }

        List<GearCharacteristicsFact> list = new ArrayList<>();
        for (GearCharacteristic gearCharacteristic : gearCharacteristics) {
            list.add(generateFactsForGearCharacteristic(gearCharacteristic));
        }

        return list;
    }


    public GearProblemFact generateFactsForGearProblem(GearProblem gearProblem) {
        if (gearProblem == null) {
            xPathUtil.clear();
            return null;
        }

        GearProblemFact gearProblemFact = new GearProblemFact();

        gearProblemFact.setTypeCode(gearProblemTypeCodeValue(gearProblem));
        xPathUtil.append(TYPE_CODE).storeInRepo(gearProblemFact, "typeCode");

        return gearProblemFact;
    }


    public List<GearProblemFact> generateFactsForGearProblems(List<GearProblem> gearProblems) {
        if (gearProblems == null) {
            xPathUtil.clear();
            return null;
        }
        final String partialXpath = xPathUtil.getValue();
        List<GearProblemFact> list = new ArrayList<>();
        int index = 1;
        for (GearProblem gearProblem : gearProblems) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_GEAR_PROBLEM ,index);
            list.add(generateFactsForGearProblem(gearProblem));
        }

        return list;
    }


    public FaCatchFact generateFactsForFaCatchs(FACatch faCatch) {
        if (faCatch == null) {
            xPathUtil.clear();
            return null;
        }

        String partialXPath = xPathUtil.getValue();

        FaCatchFact faCatchFact = new FaCatchFact();

        faCatchFact.setSpeciesCode(mapToCodeType(faCatch.getSpeciesCode()));
        xPathUtil.appendWithoutWrapping(partialXPath).append(SPECIES_CODE).storeInRepo(faCatchFact, "speciesCode");

        faCatchFact.setResultAAPProduct(CustomMapper.getAppliedProcessAAPProducts(faCatch.getAppliedAAPProcesses()));
        xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT).storeInRepo(faCatchFact, "resultAAPProduct");

        faCatchFact.setTypeCode(mapToCodeType(faCatch.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXPath).append(TYPE_CODE).storeInRepo(faCatchFact, "typeCode");

        faCatchFact.setSizeDistributionClassCode(mapToCodeType(faCatchesSpecifiedSizeDistributionClassCodes(faCatch)));
        xPathUtil.appendWithoutWrapping(partialXPath).append(SPECIFIED_SIZE_DISTRIBUTION, CLASS_CODE).storeInRepo(faCatchFact, "sizeDistributionClassCode");

        faCatchFact.setUnitQuantity(mapQuantityTypeToMeasureType(faCatch.getUnitQuantity()));
        xPathUtil.appendWithoutWrapping(partialXPath).append(UNIT_QUANTITY).storeInRepo(faCatchFact, "unitQuantity");

        faCatchFact.setWeightMeasure(mapToMeasureType(faCatch.getWeightMeasure()));
        xPathUtil.appendWithoutWrapping(partialXPath).append(WEIGHT_MEASURE).storeInRepo(faCatchFact, "weightMeasure");

        faCatchFact.setResultAAPProductPackagingTypeCode(CustomMapper.getAAPProductPackagingTypeCode(faCatch.getAppliedAAPProcesses()));
        xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, PACKAGING_TYPE_CODE).storeInRepo(faCatchFact, "resultAAPProductPackagingTypeCode");

        faCatchFact.setResultAAPProductPackagingUnitQuantity(CustomMapper.getMeasureTypeFromAAPProcess(faCatch.getAppliedAAPProcesses(), AAP_PRODUCT_PACKAGING_UNIT_QUANTITY));
        xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, PACKAGING_UNIT_QUANTITY).storeInRepo(faCatchFact, "resultAAPProductPackagingUnitQuantity");

        faCatchFact.setResultAAPProductWeightMeasure(CustomMapper.getMeasureTypeFromAAPProcess(faCatch.getAppliedAAPProcesses(), AAP_PRODUCT_WEIGHT_MEASURE));
        xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, WEIGHT_MEASURE).storeInRepo(faCatchFact, "resultAAPProductWeightMeasure");

        faCatchFact.setResultAAPProductUnitQuantity(CustomMapper.getMeasureTypeFromAAPProcess(faCatch.getAppliedAAPProcesses(), AAP_PRODUCT_UNIT_QUANTITY));
        xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, UNIT_QUANTITY).storeInRepo(faCatchFact, "resultAAPProductUnitQuantity");

        faCatchFact.setResultAAPProductPackagingUnitAverageWeightMeasure(CustomMapper.getMeasureTypeFromAAPProcess(faCatch.getAppliedAAPProcesses(), AAP_PRODUCT_AVERAGE_WEIGHT_MEASURE));
        xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, RESULT_AAP_PRODUCT, PACKAGING_UNIT_AVERAGE_WEIGHT_MEASURE).storeInRepo(faCatchFact, "resultAAPProductPackagingUnitAverageWeightMeasure");

        faCatchFact.setAppliedAAPProcessTypeCodes(CustomMapper.getAppliedProcessTypeCodes(faCatch.getAppliedAAPProcesses()));
        xPathUtil.appendWithoutWrapping(partialXPath).append(APPLIED_AAP_PROCESS, TYPE_CODE).storeInRepo(faCatchFact, "appliedAAPProcessTypeCodes");

        return faCatchFact;
    }


    public List<FaCatchFact> generateFactsForFaCatchs(List<FACatch> faCatches) {
        if (faCatches == null) {
            xPathUtil.clear();
            return null;
        }
        String partialXpath = xPathUtil.getValue();
        int index = 1;
        List<FaCatchFact> list = new ArrayList<>();
        for (FACatch fACatch : faCatches) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FA_CATCH, index);
            list.add(generateFactsForFaCatchs(fACatch));
        }

        return list;
    }


    public VesselStorageCharacteristicsFact generateFactsForVesselStorageCharacteristic(VesselStorageCharacteristic vesselStorageCharacteristic) {
        if (vesselStorageCharacteristic == null) {
            return null;
        }

        VesselStorageCharacteristicsFact vesselStorageCharacteristicsFact = new VesselStorageCharacteristicsFact();

        vesselStorageCharacteristicsFact.setTypeCodes(mapToCodeType(vesselStorageCharacteristic.getTypeCodes()));

        return vesselStorageCharacteristicsFact;
    }


    public List<VesselStorageCharacteristicsFact> generateFactsForVesselStorageCharacteristics(List<VesselStorageCharacteristic> vesselStorageCharacteristics) {
        if (vesselStorageCharacteristics == null) {
            return null;
        }

        List<VesselStorageCharacteristicsFact> list = new ArrayList<>();
        for (VesselStorageCharacteristic vesselStorageCharacteristic : vesselStorageCharacteristics) {
            list.add(generateFactsForVesselStorageCharacteristic(vesselStorageCharacteristic));
        }

        return list;
    }


    public FishingTripFact generateFactForFishingTrip(FishingTrip fishingTrip) {
        if (fishingTrip == null) {
            return null;
        }

        FishingTripFact fishingTripFact = new FishingTripFact();

        fishingTripFact.setIds(mapToIdType(fishingTrip.getIDS()));
        fishingTripFact.setTypeCode(mapToCodeType(fishingTrip.getTypeCode()));

        return fishingTripFact;
    }


    public List<FishingTripFact> generateFactForFishingTrips(List<FishingTrip> fishingTrip) {
        if (fishingTrip == null) {
            return null;
        }

        List<FishingTripFact> list = new ArrayList<>();
        for (FishingTrip fishingTrip_ : fishingTrip) {
            list.add(generateFactForFishingTrip(fishingTrip_));
        }

        return list;
    }


    public FluxLocationFact generateFactForFluxLocation(FLUXLocation fluxLocation) {
        if (fluxLocation == null) {
            xPathUtil.clear();
            return null;
        }

        final String partialXpath = xPathUtil.getValue();

        FluxLocationFact fluxLocationFact = new FluxLocationFact();

        fluxLocationFact.setId(mapToCodeType(fluxLocation.getID()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(fluxLocationFact, "id");

        fluxLocationFact.setApplicableFLUXCharacteristicTypeCode(CustomMapper.getApplicableFLUXCharacteristicsTypeCode(fluxLocation.getApplicableFLUXCharacteristics()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(APPLICABLE_FLUX_CHARACTERISTIC, TYPE_CODE).storeInRepo(fluxLocationFact, "applicableFLUXCharacteristicTypeCode");

        fluxLocationFact.setCountryID(mapToCodeType(fluxLocation.getCountryID()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(COUNTRY_ID).storeInRepo(fluxLocationFact, "countryID");

        fluxLocationFact.setTypeCode(mapToCodeType(fluxLocation.getTypeCode()));
        xPathUtil.appendWithoutWrapping(partialXpath).append(TYPE_CODE).storeInRepo(fluxLocationFact, "typeCode");

        fluxLocationFact.setSpecifiedPhysicalFLUXGeographicalCoordinate(fluxLocation.getSpecifiedPhysicalFLUXGeographicalCoordinate());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_PHYSICAL_FLUX_GEOGRAPHICAL_COORDINATE).storeInRepo(fluxLocationFact, "specifiedPhysicalFLUXGeographicalCoordinate");

        fluxLocationFact.setPhysicalStructuredAddress(fluxLocation.getPhysicalStructuredAddress());
        xPathUtil.appendWithoutWrapping(partialXpath).append(PHYSICAL_STRUCTURED_ADDRESS).storeInRepo(fluxLocationFact, "physicalStructuredAddress");

        return fluxLocationFact;
    }


    public List<FluxLocationFact> generateFactsForFluxLocations(List<FLUXLocation> fluxLocation) {
        if (fluxLocation == null) {
            xPathUtil.clear();
            return null;
        }
        final String partialXpath = xPathUtil.getValue();
        List<FluxLocationFact> list = new ArrayList<>();
        int index = 1;
        for (FLUXLocation fLUXLocation : fluxLocation) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FLUX_LOCATION ,index);
            list.add(generateFactForFluxLocation(fLUXLocation));
        }
        return list;
    }


    public FluxCharacteristicsFact generateFactForFluxCharacteristics(FLUXCharacteristic fluxCharacteristic) {
        if (fluxCharacteristic == null) {
            return null;
        }

        FluxCharacteristicsFact fluxCharacteristicsFact = new FluxCharacteristicsFact();

        fluxCharacteristicsFact.setTypeCode(fluxCharacteristicTypeCodeValue(fluxCharacteristic));

        return fluxCharacteristicsFact;
    }


    public List<FluxCharacteristicsFact> generateFactsForFluxCharacteristics(List<FLUXCharacteristic> fluxCharacteristic) {
        if (fluxCharacteristic == null) {
            return null;
        }

        List<FluxCharacteristicsFact> list = new ArrayList<>();
        for (FLUXCharacteristic fLUXCharacteristic : fluxCharacteristic) {
            list.add(generateFactForFluxCharacteristics(fLUXCharacteristic));
        }

        return list;
    }


    public FaDepartureFact generateFactsForFaDeparture(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaDepartureFact faDepartureFact = new FaDepartureFact();

        if (fishingActivity != null) {
            faDepartureFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faDepartureFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
            if (fishingActivity.getSpecifiedFishingGears() != null) {
                faDepartureFact.setSpecifiedFishingGears(new ArrayList<>(fishingActivity.getSpecifiedFishingGears()));
            }
            if (fishingActivity.getSpecifiedFACatches() != null) {
                faDepartureFact.setSpecifiedFACatches(new ArrayList<>(fishingActivity.getSpecifiedFACatches()));
            }
            faDepartureFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
            faDepartureFact.setSpecifiedFishingTrip(fishingActivity.getSpecifiedFishingTrip());
            faDepartureFact.setOccurrenceDateTime(CustomMapper.getDate(fishingActivity.getOccurrenceDateTime()));
        }
        if (faReportDocument != null) {
            faDepartureFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }

        return faDepartureFact;
    }


    public FaEntryToSeaFact generateFactsForEntryIntoSea(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaEntryToSeaFact faEntryToSeaFact = new FaEntryToSeaFact();

        if (fishingActivity != null) {
            faEntryToSeaFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faEntryToSeaFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
            faEntryToSeaFact.setSpeciesTargetCode(mapToCodeType(fishingActivity.getSpeciesTargetCode()));
            faEntryToSeaFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
        }
        if (faReportDocument != null) {
            faEntryToSeaFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }

        return faEntryToSeaFact;
    }


    public FaFishingOperationFact generateFactsForFishingOperation(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaFishingOperationFact faFishingOperationFact = new FaFishingOperationFact();

        if (fishingActivity != null) {
            faFishingOperationFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faFishingOperationFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
            if (fishingActivityOperationsQuantityValue__(fishingActivity) != null) {
                faFishingOperationFact.setOperationsQuantity(fishingActivityOperationsQuantityValue__(fishingActivity).toString());
            }
            faFishingOperationFact.setVesselRelatedActivityCode(mapToCodeType(fishingActivity.getVesselRelatedActivityCode()));
        }
        if (faReportDocument != null) {
            faFishingOperationFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }

        return faFishingOperationFact;
    }


    public FaJointFishingOperationFact generateFactsForJointFishingOperation(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaJointFishingOperationFact faJointFishingOperationFact = new FaJointFishingOperationFact();

        if (fishingActivity != null) {
            faJointFishingOperationFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faJointFishingOperationFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
        }
        if (faReportDocument != null) {
            faJointFishingOperationFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }

        return faJointFishingOperationFact;
    }


    public FaRelocationFact generateFactsForRelocation(FishingActivity fishingActivity) {
        if (fishingActivity == null) {
            return null;
        }

        FaRelocationFact faRelocationFact = new FaRelocationFact();

        faRelocationFact.setTypeCode(fishingActivityTypeCodeValue(fishingActivity));

        return faRelocationFact;
    }


    public FaDiscardFact generateFactsForDiscard(FishingActivity fishingActivity) {
        if (fishingActivity == null) {
            return null;
        }

        FaDiscardFact faDiscardFact = new FaDiscardFact();

        faDiscardFact.setTypeCode(fishingActivityTypeCodeValue_(fishingActivity));

        return faDiscardFact;
    }


    public FaExitFromSeaFact generateFactsForExitArea(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaExitFromSeaFact faExitFromSeaFact = new FaExitFromSeaFact();

        if (fishingActivity != null) {
            faExitFromSeaFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faExitFromSeaFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
        }
        if (faReportDocument != null) {
            faExitFromSeaFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }

        return faExitFromSeaFact;
    }


    public FaNotificationOfArrivalFact generateFactsForPriorNotificationOfArrival(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaNotificationOfArrivalFact faNotificationOfArrivalFact = new FaNotificationOfArrivalFact();

        if (fishingActivity != null) {
            faNotificationOfArrivalFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faNotificationOfArrivalFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
            if (fishingActivity.getSpecifiedFACatches() != null) {
                faNotificationOfArrivalFact.setSpecifiedFACatches(new ArrayList<>(fishingActivity.getSpecifiedFACatches()));
            }
            faNotificationOfArrivalFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
            faNotificationOfArrivalFact.setOccurrenceDateTime(CustomMapper.getDate(fishingActivity.getOccurrenceDateTime()));
        }
        if (faReportDocument != null) {
            faNotificationOfArrivalFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }

        return faNotificationOfArrivalFact;
    }


    public FaTranshipmentFact generateFactsForTranshipment(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaTranshipmentFact faTranshipmentFact = new FaTranshipmentFact();

        if (fishingActivity != null) {
            faTranshipmentFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faTranshipmentFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
            if (fishingActivity.getRelatedVesselTransportMeans() != null) {
                faTranshipmentFact.setRelatedVesselTransportMeans(new ArrayList<>(fishingActivity.getRelatedVesselTransportMeans()));
            }
            if (fishingActivity.getSpecifiedFACatches() != null) {
                faTranshipmentFact.setSpecifiedFACatches(new ArrayList<>(fishingActivity.getSpecifiedFACatches()));
            }
        }
        if (faReportDocument != null) {
            faTranshipmentFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }

        return faTranshipmentFact;
    }


    public FaArrivalFact generateFactsForDeclarationOfArrival(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaArrivalFact faArrivalFact = new FaArrivalFact();

        if (fishingActivity != null) {
            faArrivalFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faArrivalFact.setRelatedFLUXLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
            faArrivalFact.setFishingGearRoleCodes(CustomMapper.getFishingGearRoleCodes(fishingActivity.getSpecifiedFishingGears()));
            faArrivalFact.setFluxLocationTypeCodes(CustomMapper.getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
            faArrivalFact.setFishingTripIds(mapToIdType(fishingActivitySpecifiedFishingTripIDS(fishingActivity)));
            faArrivalFact.setReasonCode(mapToCodeType(fishingActivity.getReasonCode()));
            faArrivalFact.setOccurrenceDateTime(fishingActivity.getOccurrenceDateTime());
        }
        if (faReportDocument != null) {
            faArrivalFact.setFaReportTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }

        return faArrivalFact;
    }


    public FaQueryFact generateFactsForFaQuery(FAQuery faQuery) {
        if (faQuery == null) {
            return null;
        }

        FaQueryFact faQueryFact = new FaQueryFact();

        faQueryFact.setId(mapToCodeType(faQuery.getID()));
        faQueryFact.setTypeCode(mapToCodeType(faQuery.getTypeCode()));
        faQueryFact.setSubmittedDateTime(CustomMapper.getDate(faQuery.getSubmittedDateTime()));

        return faQueryFact;
    }


    public FaLandingFact generateFactsForLanding(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaLandingFact faLandingFact = new FaLandingFact();

        if (fishingActivity != null) {
            faLandingFact.setRelatedFluxLocationTypeCodes(CustomMapper.getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
            if (fishingActivity.getSpecifiedFACatches() != null) {
                faLandingFact.setSpecifiedFaCatches(new ArrayList<>(fishingActivity.getSpecifiedFACatches()));
            }
            faLandingFact.setFishingActivityCodeType(mapToCodeType(fishingActivity.getTypeCode()));
            if (fishingActivity.getRelatedFLUXLocations() != null) {
                faLandingFact.setRelatedFluxLocations(new ArrayList<>(fishingActivity.getRelatedFLUXLocations()));
            }
        }
        if (faReportDocument != null) {
            faLandingFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }
        faLandingFact.setSpecifiedFaCatchFluxLocationTypeCode(CustomMapper.getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(), CODE_TYPE_FOR_FACATCH_FLUXLOCATION));
        faLandingFact.setSpecifiedFaCatchTypeCode(CustomMapper.getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(), CODE_TYPE_FOR_FACATCH));

        return faLandingFact;
    }


    public FaNotificationOfTranshipmentFact generateFactsForNotificationOfTranshipment(FishingActivity fishingActivity, FAReportDocument faReportDocument) {
        if (fishingActivity == null && faReportDocument == null) {
            return null;
        }

        FaNotificationOfTranshipmentFact faNotificationOfTranshipmentFact = new FaNotificationOfTranshipmentFact();

        if (fishingActivity != null) {
            faNotificationOfTranshipmentFact.setFishingActivityTypeCode(mapToCodeType(fishingActivity.getTypeCode()));
            faNotificationOfTranshipmentFact.setVesselTransportMeansRoleCode(CustomMapper.getVesselTransportMeansRoleCodes(fishingActivity.getRelatedVesselTransportMeans()));
            faNotificationOfTranshipmentFact.setFluxLocationTypeCode(CustomMapper.getFLUXLocationTypeCodes(fishingActivity.getRelatedFLUXLocations()));
            faNotificationOfTranshipmentFact.setFluxCharacteristicValueQuantity(CustomMapper.getApplicableFLUXCharacteristicsValueQuantity(fishingActivity.getSpecifiedFLUXCharacteristics()));
            if (fishingActivity.getRelatedVesselTransportMeans() != null) {
                faNotificationOfTranshipmentFact.setRelatedVesselTransportMeans(new ArrayList<>(fishingActivity.getRelatedVesselTransportMeans()));
            }
        }
        if (faReportDocument != null) {
            faNotificationOfTranshipmentFact.setFaReportDocumentTypeCode(mapToCodeType(faReportDocument.getTypeCode()));
        }
        faNotificationOfTranshipmentFact.setFaCatchTypeCode(CustomMapper.getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(), CODE_TYPE_FOR_FACATCH));

        return faNotificationOfTranshipmentFact;
    }


    public FaQueryParameterFact generateFactsForFaQueryParameter(FAQueryParameter faQueryParameter) {
        if (faQueryParameter == null) {
            return null;
        }

        FaQueryParameterFact faQueryParameterFact = new FaQueryParameterFact();

        faQueryParameterFact.setTypeCode(faQueryParameterTypeCodeValue(faQueryParameter));

        return faQueryParameterFact;
    }


    public FaResponseFact generateFactsForFaResponse(FLUXResponseMessage fluxResponseMessage) {
        if (fluxResponseMessage == null) {
            return null;
        }

        FaResponseFact faResponseFact = new FaResponseFact();

        faResponseFact.setReferencedID(fluxResponseMessageFLUXResponseDocumentReferencedIDValue(fluxResponseMessage));

        return faResponseFact;
    }


    public eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType mapToCodeType(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType) {
        if (codeType == null) {
            return null;
        }

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType codeType_________________________________________________ = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();

        codeType_________________________________________________.setListId(codeType.getListID());
        codeType_________________________________________________.setValue(codeType.getValue());

        return codeType_________________________________________________;
    }


    public IdType mapToCodeType(IDType idType) {
        if (idType == null) {
            return null;
        }

        IdType idType________ = new IdType();

        idType________.setSchemeId(idType.getSchemeID());
        idType________.setValue(idType.getValue());

        return idType________;
    }


    public List<IdType> mapToIdType(List<IDType> idTypes) {
        if (idTypes == null) {
            return null;
        }

        List<IdType> list__________ = new ArrayList<>();
        for (IDType iDType : idTypes) {
            list__________.add(mapToCodeType(iDType));
        }

        return list__________;
    }


    public List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType> mapToCodeType(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> codeTypes) {
        if (codeTypes == null) {
            return null;
        }

        List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType> list__ = new ArrayList<>();
        for (un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType : codeTypes) {
            list__.add(mapToCodeType(codeType));
        }

        return list__;
    }


    public eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType mapToMeasureType(un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType measureType) {
        if (measureType == null) {
            return null;
        }

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType measureType__ = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType();

        measureType__.setValue(measureType.getValue());
        measureType__.setUnitCode(measureType.getUnitCode());

        return measureType__;
    }


    public List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType> mapToMeasureType(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType> measureTypes) {
        if (measureTypes == null) {
            return null;
        }

        List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType> list = new ArrayList<>();
        for (un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType measureType : measureTypes) {
            list.add(mapToMeasureType(measureType));
        }

        return list;
    }


    public eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType mapQuantityTypeToMeasureType(QuantityType quantityType) {
        if (quantityType == null) {
            return null;
        }

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType measureType_ = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType();

        measureType_.setValue(quantityType.getValue());
        measureType_.setUnitCode(quantityType.getUnitCode());

        return measureType_;
    }


    public List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType> mapToQuantityTypeToMeasureType(List<QuantityType> quantityTypes) {
        if (quantityTypes == null) {
            return null;
        }

        List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType> list = new ArrayList<>();
        for (QuantityType quantityType : quantityTypes) {
            list.add(mapQuantityTypeToMeasureType(quantityType));
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
            return null;
        }
        FLUXReportDocument relatedFLUXReportDocument = fAReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument == null) {
            return null;
        }
        List<IDType> iDS = relatedFLUXReportDocument.getIDS();
        if (iDS == null) {
            return null;
        }
        return iDS;
    }

    private List<IDType> faReportDocumentsRelatedFLUXReportDocumentOwnerFLUXPartyIDS(FAReportDocument fAReportDocument) {

        if (fAReportDocument == null) {
            return null;
        }
        FLUXReportDocument relatedFLUXReportDocument = fAReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument == null) {
            return null;
        }
        FLUXParty ownerFLUXParty = relatedFLUXReportDocument.getOwnerFLUXParty();
        if (ownerFLUXParty == null) {
            return null;
        }
        List<IDType> iDS = ownerFLUXParty.getIDS();
        if (iDS == null) {
            return null;
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

    private List<IDType> faReportDocumentsRelatedFLUXReportDocumentIDS_(FAReportDocument fAReportDocument) {

        if (fAReportDocument == null) {
            return null;
        }
        FLUXReportDocument relatedFLUXReportDocument = fAReportDocument.getRelatedFLUXReportDocument();
        if (relatedFLUXReportDocument == null) {
            return null;
        }
        List<IDType> iDS = relatedFLUXReportDocument.getIDS();
        if (iDS == null) {
            return null;
        }
        return iDS;
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

    private BigDecimal fishingActivityOperationsQuantityValue_(FishingActivity fishingActivity) {

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
            return null;
        }
        FLUXReportDocument fLUXReportDocument = fLUXFAReportMessage.getFLUXReportDocument();
        if (fLUXReportDocument == null) {
            return null;
        }
        List<IDType> iDS = fLUXReportDocument.getIDS();
        if (iDS == null) {
            return null;
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
            return null;
        }
        FLUXReportDocument fLUXReportDocument = fLUXFAReportMessage.getFLUXReportDocument();
        if (fLUXReportDocument == null) {
            return null;
        }
        FLUXParty ownerFLUXParty = fLUXReportDocument.getOwnerFLUXParty();
        if (ownerFLUXParty == null) {
            return null;
        }
        List<IDType> iDS = ownerFLUXParty.getIDS();
        if (iDS == null) {
            return null;
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

    private IDType vesselTransportMeanRegistrationVesselCountryID(VesselTransportMeans vesselTransportMeans) {
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

    private IDType vesselTransportMeanRegistrationVesselCountryID_(VesselTransportMeans vesselTransportMeans) {

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

    private String structuredAddressPostcodeCodeValue(StructuredAddress structuredAddress) {

        if (structuredAddress == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType postcodeCode = structuredAddress.getPostcodeCode();
        if (postcodeCode == null) {
            return null;
        }
        String value = postcodeCode.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private String structuredAddressCountryIDValue(StructuredAddress structuredAddress) {

        if (structuredAddress == null) {
            return null;
        }
        IDType countryID = structuredAddress.getCountryID();
        if (countryID == null) {
            return null;
        }
        String value = countryID.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private String structuredAddressCityNameValue(StructuredAddress structuredAddress) {

        if (structuredAddress == null) {
            return null;
        }
        TextType cityName = structuredAddress.getCityName();
        if (cityName == null) {
            return null;
        }
        String value = cityName.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private String structuredAddressStreetNameValue(StructuredAddress structuredAddress) {

        if (structuredAddress == null) {
            return null;
        }
        TextType streetName = structuredAddress.getStreetName();
        if (streetName == null) {
            return null;
        }
        String value = streetName.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private String structuredAddressPlotIdentificationValue(StructuredAddress structuredAddress) {

        if (structuredAddress == null) {
            return null;
        }
        TextType plotIdentification = structuredAddress.getPlotIdentification();
        if (plotIdentification == null) {
            return null;
        }
        String value = plotIdentification.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private String gearProblemTypeCodeValue(GearProblem gearProblem) {

        if (gearProblem == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = gearProblem.getTypeCode();
        if (typeCode == null) {
            return null;
        }
        String value = typeCode.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> faCatchesSpecifiedSizeDistributionClassCodes(FACatch fACatch) {

        if (fACatch == null) {
            return null;
        }
        SizeDistribution specifiedSizeDistribution = fACatch.getSpecifiedSizeDistribution();
        if (specifiedSizeDistribution == null) {
            return null;
        }
        List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> classCodes = specifiedSizeDistribution.getClassCodes();
        if (classCodes == null) {
            return null;
        }
        return classCodes;
    }

    private String fluxCharacteristicTypeCodeValue(FLUXCharacteristic fLUXCharacteristic) {

        if (fLUXCharacteristic == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = fLUXCharacteristic.getTypeCode();
        if (typeCode == null) {
            return null;
        }
        String value = typeCode.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private BigDecimal fishingActivityOperationsQuantityValue__(FishingActivity fishingActivity) {

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

    private String fishingActivityTypeCodeValue(FishingActivity fishingActivity) {

        if (fishingActivity == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = fishingActivity.getTypeCode();
        if (typeCode == null) {
            return null;
        }
        String value = typeCode.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private String fishingActivityTypeCodeValue_(FishingActivity fishingActivity) {

        if (fishingActivity == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = fishingActivity.getTypeCode();
        if (typeCode == null) {
            return null;
        }
        String value = typeCode.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private List<IDType> fishingActivitySpecifiedFishingTripIDS(FishingActivity fishingActivity) {

        if (fishingActivity == null) {
            return null;
        }
        FishingTrip specifiedFishingTrip = fishingActivity.getSpecifiedFishingTrip();
        if (specifiedFishingTrip == null) {
            return null;
        }
        List<IDType> iDS = specifiedFishingTrip.getIDS();
        if (iDS == null) {
            return null;
        }
        return iDS;
    }

    private String faQueryParameterTypeCodeValue(FAQueryParameter fAQueryParameter) {

        if (fAQueryParameter == null) {
            return null;
        }
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = fAQueryParameter.getTypeCode();
        if (typeCode == null) {
            return null;
        }
        String value = typeCode.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

    private String fluxResponseMessageFLUXResponseDocumentReferencedIDValue(FLUXResponseMessage fLUXResponseMessage) {

        if (fLUXResponseMessage == null) {
            return null;
        }
        FLUXResponseDocument fLUXResponseDocument = fLUXResponseMessage.getFLUXResponseDocument();
        if (fLUXResponseDocument == null) {
            return null;
        }
        IDType referencedID = fLUXResponseDocument.getReferencedID();
        if (referencedID == null) {
            return null;
        }
        String value = referencedID.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }

}
