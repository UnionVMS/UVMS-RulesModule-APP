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

package eu.europa.ec.fisheries.uvms.rules.service.mapper.fact;

import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaCatchFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaDepartureFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaDiscardFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaEntryToSeaFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaExitFromSeaFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaFishingOperationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaJointFishingOperationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaLandingFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaNotificationOfArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaNotificationOfTranshipmentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaQueryFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaQueryParameterFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaRelocationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaResponseFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaTranshipmentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingActivityFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingGearFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingTripFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FluxCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FluxFaReportMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FluxLocationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.GearCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.GearProblemFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.StructuredAddressFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselStorageCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQueryParameter;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearProblem;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.StructuredAddress;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselStorageCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@Mapper(uses = CustomMapper.class)
public interface ActivityFactMapper {

    ActivityFactMapper INSTANCE = Mappers.getMapper(ActivityFactMapper.class);

    @Mappings({
            @Mapping(target = "acceptanceDateTime", source = "acceptanceDateTime"),
            @Mapping(target = "creationDateTime", source = "relatedFLUXReportDocument.creationDateTime"),
            @Mapping(target = "purposeCode", source = "relatedFLUXReportDocument.purposeCode"),
            @Mapping(target = "ids", source = "relatedFLUXReportDocument.IDS"),
            @Mapping(target = "ownerFluxPartyIds", source = "relatedFLUXReportDocument.ownerFLUXParty.IDS")
    })
    FaReportDocumentFact generateFactForFaReportDocument(FAReportDocument faReportDocuments);

    List<FaReportDocumentFact> generateFactForFaReportDocuments(List<FAReportDocument> faReportDocuments);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FishingActivityFact generateFactForFishingActivity(FishingActivity fishingActivity);

    List<FishingActivityFact> generateFactForFishingActivities(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "referencedID", source = "FLUXReportDocument.referencedID.value")
    })
    FluxFaReportMessageFact generateFactForFluxReportMessage(FLUXFAReportMessage fluxfaReportMessage);

    @Mappings({
            @Mapping(target = "ids", source = "IDS"),
            @Mapping(target = "registrationVesselCountryId", source = "registrationVesselCountry.ID"),
            @Mapping(target = "specifiedContactPartyRoleCodes", source = "specifiedContactParties"),
            @Mapping(target = "specifiedContactPersons", source = "specifiedContactParties")
    })
    VesselTransportMeansFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean);

    List<VesselTransportMeansFact> generateFactForVesselTransportMeans(List<VesselTransportMeans> vesselTransportMeans);

    @Mappings({
            @Mapping(target = "postcodeCode", source = "postcodeCode.value")
    })
    StructuredAddressFact generateFactsForStructureAddress(StructuredAddress structuredAddress);

    List<StructuredAddressFact> generateFactsForStructureAddresses(List<StructuredAddress> structuredAddresses);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FishingGearFact generateFactsForFishingGear(FishingGear fishingGear);

    List<FishingGearFact> generateFactsForFishingGears(List<FishingGear> fishingGears);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    GearCharacteristicsFact generateFactsForGearCharacteristic(GearCharacteristic gearCharacteristic);

    List<GearCharacteristicsFact> generateFactsForGearCharacteristics(List<GearCharacteristic> gearCharacteristics);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    GearProblemFact generateFactsForGearProblem(GearProblem gearProblem);

    List<GearProblemFact> generateFactsForGearProblems(List<GearProblem> gearProblems);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaCatchFact generateFactsForFaCatchs(FACatch faCatches);

    List<FaCatchFact> generateFactsForFaCatchs(List<FACatch> faCatches);

    VesselStorageCharacteristicsFact generateFactsForVesselStorageCharacteristic(VesselStorageCharacteristic vesselStorageCharacteristic);

    List<VesselStorageCharacteristicsFact> generateFactsForVesselStorageCharacteristics(List<VesselStorageCharacteristic> vesselStorageCharacteristics);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FishingTripFact generateFactForFishingTrip(FishingTrip fishingTrip);

    List<FishingTripFact> generateFactForFishingTrips(List<FishingTrip> fishingTrip);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FluxLocationFact generateFactForFluxLocation(FLUXLocation fluxLocation);

    List<FluxLocationFact> generateFactsForFluxLocations(List<FLUXLocation> fluxLocation);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FluxCharacteristicsFact generateFactForFluxCharacteristics(FLUXCharacteristic fluxCharacteristic);

    List<FluxCharacteristicsFact> generateFactsForFluxCharacteristics(List<FLUXCharacteristic> fluxCharacteristic);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaDepartureFact generateFactsForFaDeparture(FishingActivity fishingActivity);

    List<FaDepartureFact> generateFactsForFaDepartures(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaEntryToSeaFact generateFactsForEntryIntoSea(FishingActivity fishingActivity);

    List<FaEntryToSeaFact> generateFactsForEntryIntoSeas(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaFishingOperationFact generateFactsForFishingOperation(FishingActivity fishingActivity);

    List<FaFishingOperationFact> generateFactsForFishingOperations(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaJointFishingOperationFact generateFactsForJointFishingOperation(FishingActivity fishingActivity);

    List<FaJointFishingOperationFact> generateFactsForJointFishingOperations(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaRelocationFact generateFactsForRelocation(FishingActivity fishingActivity);

    List<FaRelocationFact> generateFactsForRelocations(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaDiscardFact generateFactsForDiscard(FishingActivity fishingActivity);

    List<FaDiscardFact> generateFactsForDiscards(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaExitFromSeaFact generateFactsForExitArea(FishingActivity fishingActivity);

    List<FaExitFromSeaFact> generateFactsForExitAreas(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaNotificationOfArrivalFact generateFactsForPriorNotificationOfArrival(FishingActivity fishingActivity);

    List<FaNotificationOfArrivalFact> generateFactsForPriorNotificationOfArrivals(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaArrivalFact generateFactsForArrival(FishingActivity fishingActivity);

    List<FaArrivalFact> generateFactsForArrivals(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaLandingFact generateFactsForLanding(FishingActivity fishingActivity);

    List<FaLandingFact> generateFactsForLandings(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaTranshipmentFact generateFactsForTranshipment(FishingActivity fishingActivity);

    List<FaTranshipmentFact> generateFactsForTranshipments(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaNotificationOfTranshipmentFact generateFactsForNotificationOfTranshipment(FishingActivity fishingActivity);

    List<FaNotificationOfTranshipmentFact> generateFactsForNotificationOfTranshipments(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaQueryFact generateFactsForFaQuery(FAQuery faQuery);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaQueryParameterFact generateFactsForFaQueryParameter(FAQueryParameter faQueryParameter);

    @Mappings({
            @Mapping(target = "referencedID", source = "FLUXResponseDocument.referencedID.value")
    })
    FaResponseFact generateFactsForFaResponse(FLUXResponseMessage fluxResponseMessage);

    @Mappings({
            @Mapping(target = "listId", source = "listID")
    })
    CodeType mapToCodeType(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType);

    @Mappings({
            @Mapping(target = "schemeId", source = "schemeID")
    })
    IdType mapToCodeType(un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType idType);

    List<IdType> mapToIdType(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType> idTypes);

    List<CodeType> mapToCodeType(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> codeTypes);

}
