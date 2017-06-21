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

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;

import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@Mapper(uses = CustomMapper.class)
public interface ActivityFactMapper {

    ActivityFactMapper INSTANCE = Mappers.getMapper(ActivityFactMapper.class);
    String AAP_PRODUCT_PACKAGING_UNIT_QUANTITY = "PackagingUnitQuantity";
    String AAP_PRODUCT_WEIGHT_MEASURE = "WeightMeasure";
    String AAP_PRODUCT_AVERAGE_WEIGHT_MEASURE = "AverageWeightMeasure";
    String AAP_PRODUCT_UNIT_QUANTITY = "UnitQuantity";
    String CODE_TYPE_FOR_FACATCH_FLUXLOCATION = "facatchFluxlocationTypeCode";
    String CODE_TYPE_FOR_FACATCH = "facatchTypeCode";

    @Mappings({
            @Mapping(target = "acceptanceDateTime", source = "acceptanceDateTime"),
            @Mapping(target = "creationDateTime", source = "relatedFLUXReportDocument.creationDateTime"),
            @Mapping(target = "purposeCode", source = "relatedFLUXReportDocument.purposeCode"),
            @Mapping(target = "ids", source = "relatedFLUXReportDocument.IDS"),
            @Mapping(target = "ownerFluxPartyIds", source = "relatedFLUXReportDocument.ownerFLUXParty.IDS"),
            @Mapping(target = "uniqueIds", source = "relatedFLUXReportDocument"),
            @Mapping(target = "relatedFLUXReportDocumentIDs", source = "relatedFLUXReportDocument.IDS"),
            @Mapping(target = "relatedFLUXReportDocumentReferencedID", source = "relatedFLUXReportDocument.referencedID")
    })
    FaReportDocumentFact generateFactForFaReportDocument(FAReportDocument faReportDocuments);

    List<FaReportDocumentFact> generateFactForFaReportDocuments(List<FAReportDocument> faReportDocuments);

    @Mappings({
            @Mapping(target = "delimitedPeriods", source = "specifiedDelimitedPeriods"),
            @Mapping(target = "operationQuantity", source = "operationsQuantity.value"),
            @Mapping(target = "isDateProvided", source = "occurrenceDateTime"),
            @Mapping(target = "relatedFishingTrip", source = "relatedFishingActivities"),
            @Mapping(target = "fluxCharacteristicsTypeCode", source = "specifiedFLUXCharacteristics"),
            @Mapping(target = "relatedDelimitedPeriods", source = "relatedFishingActivities"),
            @Mapping(target = "relatedActivityFluxLocations", source = "relatedFishingActivities"),
            @Mapping(target = "durationMeasure", source = "specifiedDelimitedPeriods")
    })
    FishingActivityFact generateFactForFishingActivity(FishingActivity fishingActivity);

    @Mappings({
            @Mapping(target = "delimitedPeriods", source = "fishingActivity.specifiedDelimitedPeriods"),
            @Mapping(target = "operationQuantity", source = "fishingActivity.operationsQuantity.value"),
            @Mapping(target = "isDateProvided", source = "fishingActivity.occurrenceDateTime"),
            @Mapping(target = "relatedFishingTrip", source = "fishingActivity.relatedFishingActivities"),
            @Mapping(target = "fluxCharacteristicsTypeCode", source = "fishingActivity.specifiedFLUXCharacteristics"),
            @Mapping(target = "relatedDelimitedPeriods", source = "fishingActivity.relatedFishingActivities"),
            @Mapping(target = "relatedActivityFluxLocations", source = "fishingActivity.relatedFishingActivities"),
            @Mapping(target = "durationMeasure", source = "fishingActivity.specifiedDelimitedPeriods"),
            @Mapping(target = "isSubActivity", source = "isSubActivity")
    })
    FishingActivityFact generateFactForFishingActivity(FishingActivity fishingActivity, boolean isSubActivity);

    List<FishingActivityFact> generateFactForFishingActivities(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "referencedID", source = "FLUXReportDocument.referencedID"),
            @Mapping(target = "creationDateTime", source = "FLUXReportDocument.creationDateTime"),
            @Mapping(target = "purposeCode", source = "FLUXReportDocument.purposeCode"),
            @Mapping(target = "ids", source = "FLUXReportDocument.IDS"),
            @Mapping(target = "ownerFluxPartyIds", source = "FLUXReportDocument.ownerFLUXParty.IDS"),
            @Mapping(target = "faReportDocuments", source = "FAReportDocuments"),
            @Mapping(target = "uniqueIds", source = "FLUXReportDocument")
    })
    FluxFaReportMessageFact generateFactForFluxReportMessage(FLUXFAReportMessage fluxfaReportMessage);

    @Mappings({
            @Mapping(target = "ids", source = "vesselTransportMean.IDS"),
            @Mapping(target = "registrationVesselCountryId", source = "vesselTransportMean.registrationVesselCountry.ID"),
            @Mapping(target = "specifiedContactPartyRoleCodes", source = "vesselTransportMean.specifiedContactParties"),
            @Mapping(target = "specifiedContactPersons", source = "vesselTransportMean.specifiedContactParties"),
            @Mapping(target = "isFromFaReport", source = "isCommingFromFaReportDocument")
    })
    VesselTransportMeansFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean, boolean isCommingFromFaReportDocument);

    @Mappings({
            @Mapping(target = "ids", source = "vesselTransportMean.IDS"),
            @Mapping(target = "registrationVesselCountryId", source = "vesselTransportMean.registrationVesselCountry.ID"),
            @Mapping(target = "specifiedContactPartyRoleCodes", source = "vesselTransportMean.specifiedContactParties"),
            @Mapping(target = "specifiedContactPersons", source = "vesselTransportMean.specifiedContactParties")
    })
    VesselTransportMeansFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean);

    List<VesselTransportMeansFact> generateFactForVesselTransportMeans(List<VesselTransportMeans> vesselTransportMean);

    @Mappings({
            @Mapping(target = "postcodeCode", source = "postcodeCode.value"),
            @Mapping(target = "streetName", source = "streetName.value"),
            @Mapping(target = "cityName", source = "cityName.value"),
            @Mapping(target = "countryID", source = "countryID.value"),
            @Mapping(target = "plotIdentification", source = "plotIdentification.value")
    })
    StructuredAddressFact generateFactsForStructureAddress(StructuredAddress structuredAddress);

    List<StructuredAddressFact> generateFactsForStructureAddresses(List<StructuredAddress> structuredAddresses);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode")
    })
    FishingGearFact generateFactsForFishingGear(FishingGear fishingGear);

    List<FishingGearFact> generateFactsForFishingGears(List<FishingGear> fishingGears);

    @Mappings({
    })
    GearCharacteristicsFact generateFactsForGearCharacteristic(GearCharacteristic gearCharacteristic);

    List<GearCharacteristicsFact> generateFactsForGearCharacteristics(List<GearCharacteristic> gearCharacteristics);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    GearProblemFact generateFactsForGearProblem(GearProblem gearProblem);

    List<GearProblemFact> generateFactsForGearProblems(List<GearProblem> gearProblems);

    @Mappings({
            @Mapping(target = "typeCode", source = "faCatches.typeCode"),
            @Mapping(target = "speciesCode", source = "faCatches.speciesCode"),
            @Mapping(target = "sizeDistributionClassCode", source = "faCatches.specifiedSizeDistribution.classCodes"),
            @Mapping(target = "resultAAPProduct", source = "faCatches.appliedAAPProcesses"),
            @Mapping(target = "fluxLocationId", source = "fluxLocations"),
            @Mapping(target = "appliedAAPProcessConversionFactorNumber", source = "faCatches.appliedAAPProcesses"),
            @Mapping(target = "categoryCode", source = "faCatches.specifiedSizeDistribution.categoryCode"),
            @Mapping(target = "appliedAAPProcessTypeCodes", expression = "java(CustomMapper.getAppliedProcessTypeCodes(faCatches.getAppliedAAPProcesses()))"),
            @Mapping(target = "resultAAPProductPackagingTypeCode", expression = "java(CustomMapper.getAAPProductPackagingTypeCode(faCatches.getAppliedAAPProcesses()))"),
            @Mapping(target = "resultAAPProductPackagingUnitQuantity", expression = "java(CustomMapper.getMeasureTypeFromAAPProcess(faCatches.getAppliedAAPProcesses(),AAP_PRODUCT_PACKAGING_UNIT_QUANTITY))"),
            @Mapping(target = "resultAAPProductWeightMeasure", expression = "java(CustomMapper.getMeasureTypeFromAAPProcess(faCatches.getAppliedAAPProcesses(),AAP_PRODUCT_WEIGHT_MEASURE))"),
            @Mapping(target = "resultAAPProductPackagingUnitAverageWeightMeasure", expression = "java(CustomMapper.getMeasureTypeFromAAPProcess(faCatches.getAppliedAAPProcesses(),AAP_PRODUCT_AVERAGE_WEIGHT_MEASURE))"),
            @Mapping(target = "resultAAPProductUnitQuantity", expression = "java(CustomMapper.getMeasureTypeFromAAPProcess(faCatches.getAppliedAAPProcesses(),AAP_PRODUCT_UNIT_QUANTITY))")
    })
    FaCatchFact generateFactsForFaCatch(FACatch faCatches, List<FLUXLocation> fluxLocations);

    @Mappings({
            @Mapping(target = "typeCodes", source = "vesselStorageCharacteristic.typeCodes")
    })
    VesselStorageCharacteristicsFact generateFactsForVesselStorageCharacteristic(VesselStorageCharacteristic vesselStorageCharacteristic);

    List<VesselStorageCharacteristicsFact> generateFactsForVesselStorageCharacteristics(List<VesselStorageCharacteristic> vesselStorageCharacteristics);

    @Mappings({
            @Mapping(target = "ids", source = "IDS"),
            @Mapping(target = "typeCode", source = "typeCode")
    })
    FishingTripFact generateFactForFishingTrip(FishingTrip fishingTrip);

    List<FishingTripFact> generateFactForFishingTrips(List<FishingTrip> fishingTrip);

    @Mappings({
            @Mapping(target = "id", source = "ID"),
            @Mapping(target = "typeCode", source = "typeCode"),
            @Mapping(target = "countryID", source = "countryID"),
            @Mapping(target = "applicableFLUXCharacteristicTypeCode", source = "fluxLocation.applicableFLUXCharacteristics")

    })
    FluxLocationFact generateFactForFluxLocation(FLUXLocation fluxLocation);

    List<FluxLocationFact> generateFactsForFluxLocations(List<FLUXLocation> fluxLocation);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FluxCharacteristicsFact generateFactForFluxCharacteristics(FLUXCharacteristic fluxCharacteristic);

    List<FluxCharacteristicsFact> generateFactsForFluxCharacteristics(List<FLUXCharacteristic> fluxCharacteristic);

    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "occurrenceDateTime", source = "fishingActivity.occurrenceDateTime"),
            @Mapping(target = "reasonCode", source = "fishingActivity.reasonCode"),
            @Mapping(target = "relatedFLUXLocations", source = "fishingActivity.relatedFLUXLocations"),
            @Mapping(target = "specifiedFishingGears", source = "fishingActivity.specifiedFishingGears"),
            @Mapping(target = "specifiedFACatches", source = "fishingActivity.specifiedFACatches"),
            @Mapping(target = "specifiedFishingTrip", source = "fishingActivity.specifiedFishingTrip")
    })
    FaDepartureFact generateFactsForFaDeparture(FishingActivity fishingActivity, FAReportDocument faReportDocument);

    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "reasonCode", source = "fishingActivity.reasonCode"),
            @Mapping(target = "speciesTargetCode", source = "fishingActivity.speciesTargetCode"),
            @Mapping(target = "relatedFLUXLocations", source = "fishingActivity.relatedFLUXLocations")
    })
    FaEntryToSeaFact generateFactsForEntryIntoSea(FishingActivity fishingActivity, FAReportDocument faReportDocument);

    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "operationsQuantity", source = "fishingActivity.operationsQuantity.value"),
            @Mapping(target = "relatedFLUXLocations", source = "fishingActivity.relatedFLUXLocations")
    })
    FaFishingOperationFact generateFactsForFishingOperation(FishingActivity fishingActivity, FAReportDocument faReportDocument);

    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "relatedFLUXLocations", source = "fishingActivity.relatedFLUXLocations"),
    })
    FaJointFishingOperationFact generateFactsForJointFishingOperation(FishingActivity fishingActivity, FAReportDocument faReportDocument);

   @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
   FaRelocationFact generateFactsForRelocation(FishingActivity fishingActivity);


    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    FaDiscardFact generateFactsForDiscard(FishingActivity fishingActivity);


    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "relatedFLUXLocations", source = "fishingActivity.relatedFLUXLocations")
    })
    FaExitFromSeaFact generateFactsForExitArea(FishingActivity fishingActivity, FAReportDocument faReportDocument);

    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "relatedFLUXLocations", source = "fishingActivity.relatedFLUXLocations"),
            @Mapping(target = "occurrenceDateTime", source = "fishingActivity.occurrenceDateTime"),
            @Mapping(target = "reasonCode", source = "fishingActivity.reasonCode"),
            @Mapping(target = "specifiedFACatches", source = "fishingActivity.specifiedFACatches")
    })
    FaNotificationOfArrivalFact generateFactsForPriorNotificationOfArrival(FishingActivity fishingActivity, FAReportDocument faReportDocument);

    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "relatedFLUXLocations", source = "fishingActivity.relatedFLUXLocations"),
            @Mapping(target = "relatedVesselTransportMeans", source = "fishingActivity.relatedVesselTransportMeans"),
            @Mapping(target = "specifiedFACatches", source = "fishingActivity.specifiedFACatches"),
    })
    FaTranshipmentFact generateFactsForTranshipment(FishingActivity fishingActivity, FAReportDocument faReportDocument);


    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "occurrenceDateTime", source = "fishingActivity.occurrenceDateTime"),
            @Mapping(target = "reasonCode", source = "fishingActivity.reasonCode"),
            @Mapping(target = "relatedFLUXLocations", source = "fishingActivity.relatedFLUXLocations"),
            @Mapping(target = "fluxLocationTypeCodes", source = "fishingActivity.relatedFLUXLocations"),
            @Mapping(target = "fishingGearRoleCodes", source = "fishingActivity.specifiedFishingGears"),
            @Mapping(target = "fishingTripIds", source = "fishingActivity.specifiedFishingTrip.IDS")
    })
    FaArrivalFact generateFactsForDeclarationOfArrival(FishingActivity fishingActivity, FAReportDocument faReportDocument);

    @Mappings({
            @Mapping(target = "id", source = "ID")
    })
    FaQueryFact generateFactsForFaQuery(FAQuery faQuery);

    @Mappings({
            @Mapping(target = "fishingActivityCodeType", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "relatedFluxLocations", source = "fishingActivity.relatedFLUXLocations"),
            @Mapping(target = "relatedFluxLocationTypeCodes", source = "fishingActivity.relatedFLUXLocations"),
            @Mapping(target = "specifiedFaCatches",source = "fishingActivity.specifiedFACatches"),
            @Mapping(target = "specifiedFaCatchTypeCode", expression = "java(CustomMapper.getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(),CODE_TYPE_FOR_FACATCH))"),
            @Mapping(target = "specifiedFaCatchFluxLocationTypeCode", expression = "java(CustomMapper.getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(),CODE_TYPE_FOR_FACATCH_FLUXLOCATION))")
    })
    FaLandingFact generateFactsForLanding(FishingActivity fishingActivity, FAReportDocument faReportDocument);


    @Mappings({
            @Mapping(target = "fishingActivityTypeCode", source = "fishingActivity.typeCode"),
            @Mapping(target = "faReportDocumentTypeCode", source = "faReportDocument.typeCode"),
            @Mapping(target = "faCatchTypeCode", expression = "java(CustomMapper.getCodeTypesFromFaCatch(fishingActivity.getSpecifiedFACatches(),CODE_TYPE_FOR_FACATCH))"),
            @Mapping(target = "fluxLocationTypeCode", source = "fishingActivity.relatedFLUXLocations"),
            @Mapping(target = "vesselTransportMeansRoleCode", source = "fishingActivity.relatedVesselTransportMeans"),
            @Mapping(target = "fluxCharacteristicValueQuantity", source = "fishingActivity.specifiedFLUXCharacteristics")
    })
    FaNotificationOfTranshipmentFact generateFactsForNotificationOfTranshipment(FishingActivity fishingActivity, FAReportDocument faReportDocument);


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

    MeasureType mapToMeasureType(un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType measureType);

    List<MeasureType> mapToMeasureType(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType> measureTypes);

    MeasureType mapQuantityTypeToMeasureType(un.unece.uncefact.data.standard.unqualifieddatatype._20.QuantityType quantityType);

    List<MeasureType> mapToQuantityTypeToMeasureType(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.QuantityType> quantityTypes);

}
