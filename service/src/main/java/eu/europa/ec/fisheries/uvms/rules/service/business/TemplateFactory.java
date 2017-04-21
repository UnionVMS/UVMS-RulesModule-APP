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

package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;

/**
 * Created by padhyad on 4/13/2017.
 */
public class TemplateFactory {

    public static final String getTemplateFileName(FactType factType) {
        switch (factType) {
            case FA_DEPARTURE:
                return "/templates/FaDeparture.drt";
            case FA_ARRIVAL:
                return "/template/FaArrival.drt";
            case FA_CATCH:
                return "/template/FaCatch.drt";
            case FA_DISCARD:
                return "/template/FaDiscard.drt";
            case FA_ENTRY_TO_SEA:
                return "/template/FaEntryToSea.drt";
            case FA_EXIT_FROM_SEA:
                return "/template/FaExitFromSea.drt";
            case FA_FISHING_OPERATION:
                return "/template/FaFishingOperation.drt";
            case FA_JOINT_FISHING_OPERATION:
                return "/template/FaJointFishingOperation.drt";
            case FA_LANDING:
                return "/template/FaLanding.drt";
            case FA_NOTIFICATION_OF_ARRIVAL:
                return "/template/FaNotificationOfArrival.drt";
            case FA_NOTIFICATION_OF_TRANSHIPMENT:
                return "/template/FaNotificationOfTranshipment.drt";
            case FA_REPORT_DOCUMENT:
                return "/templates/FaReportDocument.drt";
            case FISHING_ACTIVITY:
                return "/templates/FishingActivity.drt";
            case FA_QUERY:
                return "/template/FaQuery.drt";
            case FA_QUERY_PARAMETER:
                return "/template/FaQueryParameter.drt";
            case FA_RELOCATION:
                return "/template/FaRelocation.drt";
            case FA_RESPONSE:
                return "/template/FaResponse.drt";
            case FA_TRANSHIPMENT:
                return "/template/FaTranshipment.drt";
            case FISHING_GEAR:
                return "/template/FishingGear.drt";
            case FISHING_TRIP:
                return "/template/FishingTrip.drt";
            case FLUX_CHARACTERISTIC:
                return "/template/FluxCharacteristics.drt";
            case FLUX_FA_REPORT_MESSAGE:
                return "/template/FluxFaReportMessage.drt";
            case FLUX_LOCATION:
                return "/template/FluxLocation.drt";
            case GEAR_CHARACTERISTIC:
                return "/template/GearCharacteristics.drt";
            case GEAR_PROBLEM:
                return "/template/GearProblem.drt";
            case STRUCTURED_ADDRESS:
                return "/template/StructuredAddress.drt";
            case VESSEL_STORAGE_CHARACTERISTIC:
                return "/template/VesselStorageCharacteristics.drt";
            case VESSEL_TRANSPORT_MEANS:
                return "/template/VesselTransportMeans.drt";
        }
        return null;
    }
}
