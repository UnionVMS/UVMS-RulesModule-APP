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

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
@Mapper(imports = {
        FaReportDocumentFact.class,
        FishingActivityFact.class,
        FluxFaReportMessageFact.class,
        VesselTransportMeansFact.class,
        StructuredAddressFact.class,
        FishingGearFact.class,
        GearCharacteristicsFact.class,
        GearProblemFact.class,
        FaCatchFact.class,
        VesselStorageCharacteristicsFact.class,
        FaDepartureFact.class,
        FaEntryToSeaFact.class,
        FaFishingOperationFact.class,
        FaJointFishingOperationFact.class,
        FaRelocationFact.class,
        FaDiscardFact.class,
        FaExitFromSeaFact.class,
        FaNotificationOfArrivalFact.class,
        FaArrivalFact.class,
        FaLandingFact.class,
        FaTranshipmentFact.class,
        FaNotificationOfTranshipmentFact.class,
        FaQueryFact.class,
        FaQueryParameterFact.class,
        FaResponseFact.class,
        FishingTripFact.class,
        FluxLocationFact.class,
        FluxCharacteristicsFact.class
})
public interface ActivityFactMapper {

    ActivityFactMapper INSTANCE = Mappers.getMapper(ActivityFactMapper.class);

    @BeanMapping(resultType = FaReportDocumentFact.class)
    AbstractFact generateFactForFaReportDocument(FAReportDocument faReportDocuments);

    @BeanMapping(resultType = FaReportDocumentFact.class)
    List<AbstractFact> generateFactForFaReportDocuments(List<FAReportDocument> faReportDocuments);

    @BeanMapping(resultType = FishingActivityFact.class)
    AbstractFact generateFactForFishingActivity(FishingActivity fishingActivity);

    @BeanMapping(resultType = FishingActivityFact.class)
    List<AbstractFact> generateFactForFishingActivities(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FluxFaReportMessageFact.class)
    AbstractFact generateFactForFluxReportMessage(FLUXFAReportMessage fluxfaReportMessage);

    @BeanMapping(resultType = VesselTransportMeansFact.class)
    AbstractFact generateFactForVesselTransportMean(VesselTransportMeans vesselTransportMean);

    @BeanMapping(resultType = VesselTransportMeansFact.class)
    List<AbstractFact> generateFactForVesselTransportMeans(List<VesselTransportMeans> vesselTransportMeans);

    @BeanMapping(resultType = StructuredAddressFact.class)
    AbstractFact generateFactsForStructureAddress(StructuredAddress structuredAddress);

    @BeanMapping(resultType = StructuredAddressFact.class)
    List<AbstractFact> generateFactsForStructureAddresses(List<StructuredAddress> structuredAddresses);

    @BeanMapping(resultType = FishingGearFact.class)
    AbstractFact generateFactsForFishingGear(FishingGear fishinggear);

    @BeanMapping(resultType = FishingGearFact.class)
    List<AbstractFact> generateFactsForFishingGears(List<FishingGear> fishinggears);

    @BeanMapping(resultType = GearCharacteristicsFact.class)
    AbstractFact generateFactsForGearCharacteristic(GearCharacteristic gearCharacteristic);

    @BeanMapping(resultType = GearCharacteristicsFact.class)
    List<AbstractFact> generateFactsForGearCharacteristics(List<GearCharacteristic> gearCharacteristics);

    @BeanMapping(resultType = GearProblemFact.class)
    AbstractFact generateFactsForGearProblem(GearProblem gearProblem);

    @BeanMapping(resultType = GearProblemFact.class)
    List<AbstractFact> generateFactsForGearProblems(List<GearProblem> gearProblems);

    @BeanMapping(resultType = FaCatchFact.class)
    AbstractFact generateFactsForFaCatchs(FACatch faCatches);

    @BeanMapping(resultType = FaCatchFact.class)
    List<AbstractFact> generateFactsForFaCatchs(List<FACatch> faCatches);

    @BeanMapping(resultType = VesselStorageCharacteristicsFact.class)
    AbstractFact generateFactsForVesselStorageCharacteristic(VesselStorageCharacteristic vesselStorageCharacteristic);

    @BeanMapping(resultType = VesselStorageCharacteristicsFact.class)
    List<AbstractFact> generateFactsForVesselStorageCharacteristics(List<VesselStorageCharacteristic> vesselStorageCharacteristics);

    @BeanMapping(resultType = FishingTripFact.class)
    AbstractFact generateFactForFishingTrip(FishingTrip fishingTrip);

    @BeanMapping(resultType = FishingTripFact.class)
    List<AbstractFact> generateFactForFishingTrips(List<FishingTrip> fishingTrip);

    @BeanMapping(resultType = FluxLocationFact.class)
    AbstractFact generateFactForFluxLocation(FLUXLocation fluxLocation);

    @BeanMapping(resultType = FluxLocationFact.class)
    List<AbstractFact> generateFactsForFluxLocations(List<FLUXLocation> fluxLocation);

    @BeanMapping(resultType = FluxCharacteristicsFact.class)
    AbstractFact generateFactForFluxCharacteristics(FLUXCharacteristic fluxCharacteristic);

    @BeanMapping(resultType = FluxCharacteristicsFact.class)
    List<AbstractFact> generateFactsForFluxCharacteristics(List<FLUXCharacteristic> fluxCharacteristic);

    @BeanMapping(resultType = FaDepartureFact.class)
    AbstractFact generateFactsForFaDeparture(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaDepartureFact.class)
    List<AbstractFact> generateFactsForFaDepartures(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaEntryToSeaFact.class)
    AbstractFact generateFactsForEntryIntoSea(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaEntryToSeaFact.class)
    List<AbstractFact> generateFactsForEntryIntoSeas(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaFishingOperationFact.class)
    AbstractFact generateFactsForFishingOperation(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaFishingOperationFact.class)
    List<AbstractFact> generateFactsForFishingOperations(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaJointFishingOperationFact.class)
    AbstractFact generateFactsForJointFishingOperation(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaJointFishingOperationFact.class)
    List<AbstractFact> generateFactsForJointFishingOperations(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaRelocationFact.class)
    AbstractFact generateFactsForRelocation(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaRelocationFact.class)
    List<AbstractFact> generateFactsForRelocations(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaDiscardFact.class)
    AbstractFact generateFactsForDiscard(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaDiscardFact.class)
    List<AbstractFact> generateFactsForDiscards(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaExitFromSeaFact.class)
    AbstractFact generateFactsForExitArea(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaExitFromSeaFact.class)
    List<AbstractFact> generateFactsForExitAreas(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaNotificationOfArrivalFact.class)
    AbstractFact generateFactsForPriorNotificationOfArrival(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaNotificationOfArrivalFact.class)
    List<AbstractFact> generateFactsForPriorNotificationOfArrivals(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaArrivalFact.class)
    AbstractFact generateFactsForArrival(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaArrivalFact.class)
    List<AbstractFact> generateFactsForArrivals(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaLandingFact.class)
    AbstractFact generateFactsForLanding(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaLandingFact.class)
    List<AbstractFact> generateFactsForLandings(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaTranshipmentFact.class)
    AbstractFact generateFactsForTranshipment(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaTranshipmentFact.class)
    List<AbstractFact> generateFactsForTranshipments(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaNotificationOfTranshipmentFact.class)
    AbstractFact generateFactsForNotificationOfTranshipment(FishingActivity fishingActivity);

    @BeanMapping(resultType = FaNotificationOfTranshipmentFact.class)
    List<AbstractFact> generateFactsForNotificationOfTranshipments(List<FishingActivity> fishingActivities);

    @BeanMapping(resultType = FaQueryFact.class)
    AbstractFact generateFactsForFaQuery(FAQuery faQuery);

    @BeanMapping(resultType = FaQueryParameterFact.class)
    AbstractFact generateFactsForFaQueryParameter(FAQueryParameter faQueryParameter);

    @BeanMapping(resultType = FaResponseFact.class)
    AbstractFact generateFactsForFaResponse(FLUXResponseMessage fluxResponseMessage);
}
