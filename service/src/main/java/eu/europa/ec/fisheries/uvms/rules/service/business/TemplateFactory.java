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
import org.drools.core.util.StringUtils;

/**
 * Created by padhyad on 4/13/2017.
 */
public class TemplateFactory {

    private TemplateFactory(){
        super();
    }

    public static String getTemplateFileName(FactType factType) {
        String drtPath;
        switch (factType) {
            case FA_DEPARTURE:
                drtPath = "/templates/FaDeparture.drt";
                break;
            case FA_ARRIVAL:
                drtPath = "/templates/FaArrival.drt";
                break;
            case FA_CATCH:
                drtPath = "/templates/FaCatch.drt";
                break;
            case FA_DISCARD:
                drtPath = "/templates/FaDiscard.drt";
                break;
            case FA_ENTRY_TO_SEA:
                drtPath = "/templates/FaEntryToSea.drt";
                break;
            case FA_EXIT_FROM_SEA:
                drtPath = "/templates/FaExitFromSea.drt";
                break;
            case FA_FISHING_OPERATION:
                drtPath = "/templates/FaFishingOperation.drt";
                break;
            case FA_JOINT_FISHING_OPERATION:
                drtPath = "/templates/FaJointFishingOperation.drt";
                break;
            case FA_LANDING:
                drtPath = "/templates/FaLanding.drt";
                break;
            case FA_NOTIFICATION_OF_ARRIVAL:
                drtPath = "/templates/FaNotificationOfArrival.drt";
                break;
            case FA_NOTIFICATION_OF_TRANSHIPMENT:
                drtPath = "/templates/FaNotificationOfTranshipment.drt";
                break;
            case FA_REPORT_DOCUMENT:
                drtPath = "/templates/FaReportDocument.drt";
                break;
            case FISHING_ACTIVITY:
                drtPath = "/templates/FishingActivity.drt";
                break;
            case FA_QUERY:
                drtPath = "/templates/FaQuery.drt";
                break;
            case FA_QUERY_PARAMETER:
                drtPath = "/templates/FaQueryParameter.drt";
                break;
            case FA_RELOCATION:
                drtPath = "/templates/FaRelocation.drt";
                break;
            case FA_RESPONSE:
                drtPath = "/templates/FaResponse.drt";
                break;
            case FA_TRANSHIPMENT:
                drtPath = "/templates/FaTranshipment.drt";
                break;
            case FISHING_GEAR:
                drtPath = "/templates/FishingGear.drt";
                break;
            case FISHING_TRIP:
                drtPath = "/templates/FishingTrip.drt";
                break;
            case FLUX_CHARACTERISTIC:
                drtPath = "/templates/FluxCharacteristics.drt";
                break;
            case FLUX_FA_REPORT_MESSAGE:
                drtPath = "/templates/FluxFaReportMessage.drt";
                break;
            case FLUX_LOCATION:
                drtPath = "/templates/FluxLocation.drt";
                break;
            case GEAR_CHARACTERISTIC:
                drtPath = "/templates/GearCharacteristics.drt";
                break;
            case GEAR_PROBLEM:
                drtPath = "/templates/GearProblem.drt";
                break;
            case STRUCTURED_ADDRESS:
                drtPath = "/templates/StructuredAddress.drt";
                break;
            case VESSEL_STORAGE_CHARACTERISTIC:
                drtPath = "/templates/VesselStorageCharacteristics.drt";
                break;
            case VESSEL_TRANSPORT_MEANS:
                drtPath = "/templates/VesselTransportMeans.drt";
                break;
            default:drtPath = StringUtils.EMPTY;
                break;
        }
        return drtPath;
    }
}
