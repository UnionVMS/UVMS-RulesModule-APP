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
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
@Mapper
public abstract class ActivityFactMapper {

    public static ActivityFactMapper INSTANCE = Mappers.getMapper(ActivityFactMapper.class);

    @Mappings({
            @Mapping(target = "acceptanceDateTime", source = "acceptanceDateTime"),
            @Mapping(target = "creationDateTime", source = "relatedFLUXReportDocument.creationDateTime"),
            @Mapping(target = "purposeCode", source = "relatedFLUXReportDocument.purposeCode"),
            @Mapping(target = "ids", source = "relatedFLUXReportDocument.IDS"),
            @Mapping(target = "ownerFluxPartyIds", source = "relatedFLUXReportDocument.ownerFLUXParty.IDS")
    })
    public abstract FaReportDocumentFact generateFactForFaReportDocument(FAReportDocument faReportDocuments);

    public abstract List<FaReportDocumentFact> generateFactForFaReportDocuments(List<FAReportDocument> faReportDocuments);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FishingActivityFact generateFactForFishingActivity(FishingActivity fishingActivity);

    abstract List<FishingActivityFact> generateFactForFishingActivities(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "referencedID", source = "FLUXReportDocument.referencedID.value")
    })
    abstract FluxFaReportMessageFact generateFactForFluxReportMessage(FLUXFAReportMessage fluxfaReportMessage);

    @Mappings({
            @Mapping(target = "roleCode", source = "roleCode.value")
    })
    abstract VesselTransportMeansFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean);

    abstract List<VesselTransportMeansFact> generateFactForVesselTransportMeans(List<VesselTransportMeans> vesselTransportMeans);


    @Mappings({
            @Mapping(target = "postcodeCode", source = "postcodeCode.value")
    })
    abstract StructuredAddressFact generateFactsForStructureAddress(StructuredAddress structuredAddress);

    abstract List<StructuredAddressFact> generateFactsForStructureAddresses(List<StructuredAddress> structuredAddresses);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FishingGearFact generateFactsForFishingGear(FishingGear fishingGear);

    abstract List<FishingGearFact> generateFactsForFishingGears(List<FishingGear> fishingGears);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract GearCharacteristicsFact generateFactsForGearCharacteristic(GearCharacteristic gearCharacteristic);

    abstract List<GearCharacteristicsFact> generateFactsForGearCharacteristics(List<GearCharacteristic> gearCharacteristics);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract GearProblemFact generateFactsForGearProblem(GearProblem gearProblem);

    abstract List<GearProblemFact> generateFactsForGearProblems(List<GearProblem> gearProblems);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaCatchFact generateFactsForFaCatchs(FACatch faCatches);

    abstract List<FaCatchFact> generateFactsForFaCatchs(List<FACatch> faCatches);

    abstract VesselStorageCharacteristicsFact generateFactsForVesselStorageCharacteristic(VesselStorageCharacteristic vesselStorageCharacteristic);

    abstract List<VesselStorageCharacteristicsFact> generateFactsForVesselStorageCharacteristics(List<VesselStorageCharacteristic> vesselStorageCharacteristics);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FishingTripFact generateFactForFishingTrip(FishingTrip fishingTrip);

    abstract List<FishingTripFact> generateFactForFishingTrips(List<FishingTrip> fishingTrip);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FluxLocationFact generateFactForFluxLocation(FLUXLocation fluxLocation);

    abstract List<FluxLocationFact> generateFactsForFluxLocations(List<FLUXLocation> fluxLocation);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FluxCharacteristicsFact generateFactForFluxCharacteristics(FLUXCharacteristic fluxCharacteristic);

    abstract List<FluxCharacteristicsFact> generateFactsForFluxCharacteristics(List<FLUXCharacteristic> fluxCharacteristic);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaDepartureFact generateFactsForFaDeparture(FishingActivity fishingActivity);

    abstract List<FaDepartureFact> generateFactsForFaDepartures(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaEntryToSeaFact generateFactsForEntryIntoSea(FishingActivity fishingActivity);

    abstract List<FaEntryToSeaFact> generateFactsForEntryIntoSeas(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaFishingOperationFact generateFactsForFishingOperation(FishingActivity fishingActivity);

    abstract List<FaFishingOperationFact> generateFactsForFishingOperations(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaJointFishingOperationFact generateFactsForJointFishingOperation(FishingActivity fishingActivity);

    abstract List<FaJointFishingOperationFact> generateFactsForJointFishingOperations(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaRelocationFact generateFactsForRelocation(FishingActivity fishingActivity);

    abstract List<FaRelocationFact> generateFactsForRelocations(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaDiscardFact generateFactsForDiscard(FishingActivity fishingActivity);

    abstract List<FaDiscardFact> generateFactsForDiscards(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaExitFromSeaFact generateFactsForExitArea(FishingActivity fishingActivity);

    abstract List<FaExitFromSeaFact> generateFactsForExitAreas(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaNotificationOfArrivalFact generateFactsForPriorNotificationOfArrival(FishingActivity fishingActivity);

    abstract List<FaNotificationOfArrivalFact> generateFactsForPriorNotificationOfArrivals(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaArrivalFact generateFactsForArrival(FishingActivity fishingActivity);

    abstract List<FaArrivalFact> generateFactsForArrivals(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaLandingFact generateFactsForLanding(FishingActivity fishingActivity);

    abstract List<FaLandingFact> generateFactsForLandings(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaTranshipmentFact generateFactsForTranshipment(FishingActivity fishingActivity);

    abstract List<FaTranshipmentFact> generateFactsForTranshipments(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaNotificationOfTranshipmentFact generateFactsForNotificationOfTranshipment(FishingActivity fishingActivity);

    abstract List<FaNotificationOfTranshipmentFact> generateFactsForNotificationOfTranshipments(List<FishingActivity> fishingActivities);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaQueryFact generateFactsForFaQuery(FAQuery faQuery);

    @Mappings({
            @Mapping(target = "typeCode", source = "typeCode.value")
    })
    abstract FaQueryParameterFact generateFactsForFaQueryParameter(FAQueryParameter faQueryParameter);

    @Mappings({
            @Mapping(target = "referencedID", source = "FLUXResponseDocument.referencedID.value")
    })
    abstract FaResponseFact generateFactsForFaResponse(FLUXResponseMessage fluxResponseMessage);

    @Mappings({
            @Mapping(target = "listId", source = "listID")
    })
    abstract CodeType mapToCodeType(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType);

    abstract List<CodeType> mapToCodeType(List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> codeType);

    @Mappings({
            @Mapping(target = "schemeId", source = "schemeID")
    })
    abstract IdType mapToIdType(IDType idType);

    abstract List<IdType> mapToIdType(List<IDType> idType);

    protected Date getDate(DateTimeType dateTimeType) {
        Date date;
        try {
            if (dateTimeType.getDateTime() != null) {
                date = dateTimeType.getDateTime().toGregorianCalendar().getTime();
            } else {
                String format = dateTimeType.getDateTimeString().getFormat();
                String value = dateTimeType.getDateTimeString().getValue();
                date = new SimpleDateFormat(format).parse(value);
            }
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

}
