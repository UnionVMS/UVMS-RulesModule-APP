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
        if(factType == null){
            return StringUtils.EMPTY;
        }
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
                return "/templates/VesselTransportMeans.drt";
            case FA_VALIDATION_RESULT_DOCUMENT:
                return "/templates/FaValidationResultDocument.drt";
            case FA_VALIDATION_QUALITY_ANALYSIS:
                return "/templates/FaValidationQualityAnalysis.drt";
            case SALES_FLUX_SALES_REPORT_MESSAGE:
                return "/templates/SalesFLUXSalesReportMessage.drt";
            case SALES_FLUX_REPORT_DOCUMENT:
                return "/templates/SalesFLUXReportDocument.drt";
            case SALES_FLUX_PARTY:
                return "/templates/SalesFLUXParty.drt";
            case SALES_REPORT:
                return "/templates/SalesReport.drt";
            case SALES_DOCUMENT:
                return "/templates/SalesDocument.drt";
            case SALES_PARTY:
                return "/templates/SalesParty.drt";
            case SALES_EVENT:
                return "/templates/SalesEvent.drt";
            case SALES_BATCH:
                return "/templates/SalesBatch.drt";
            case SALES_AAP_PRODUCT:
                return "/templates/SalesAAPProduct.drt";
            case SALES_AAP_PROCESS:
                return "/templates/SalesAAPProcess.drt";
            case SALES_SIZE_DISTRIBUTION:
                return "/templates/SalesSizeDistribution.drt";
            case SALES_PRICE:
                return "/templates/SalesPrice.drt";
            case SALES_FLUX_ORGANIZATION:
                return "/templates/SalesFLUXOrganization.drt";
            case SALES_FISHING_ACTIVITY:
                return "/templates/SalesFishingActivity.drt";
            case SALES_DELIMITED_PERIOD:
                return "/templates/SalesDelimitedPeriod.drt";
            case SALES_VESSEL_TRANSPORT_MEANS:
                return "/templates/SalesVesselTransportMeans.drt";
            case SALES_VESSEL_COUNTRY:
                return "/templates/SalesVesselCountry.drt";
            case SALES_CONTACT_PARTY:
                return "/templates/SalesContactParty.drt";
            case SALES_CONTACT_PERSON:
                return "/templates/SalesContactPerson.drt";
            case SALES_FISHING_TRIP:
                return "/templates/SalesFishingTrip.drt";
            case SALES_FLUX_LOCATION:
                return "/templates/SalesFLUXLocation.drt";
            case SALES_FLUX_GEOGRAPHICAL_COORDINATE:
                return "/templates/SalesFLUXGeographicalCoordinate.drt";
            case SALES_STRUCTURED_ADDRESS:
                return "/templates/SalesStructuredAddress.drt";
            case SALES_QUERY:
                return "/templates/SalesQuery.drt";
            case SALES_FLUX_RESPONSE_DOCUMENT:
                return "/templates/SalesFLUXResponseDocument.drt";
            case SALES_VALIDATION_RESULT_DOCUMENT:
                return "/templates/SalesValidationDocument.drt";
            case SALES_VALIDATION_QUALITY_ANALYSIS:
                return "/templates/SalesQualityAnalysis.drt";
            case SALES_REPORT_WRAPPER:
                return "/templates/SalesReportWrapper.drt";
            case SALES_AUCTION_SALE:
                return "/templates/SalesAuctionSale.drt";
            case SALES_FLUX_SALES_QUERY_MESSAGE:
                return "/templates/SalesFLUXSalesQuery.drt";
            case SALES_QUERY_PARAMETER:
                return "/templates/SalesQueryParameter.drt";
            case SALES_FLUX_SALES_RESPONSE_MESSAGE:
                return "/templates/SalesFLUXSalesResponseMessage.drt";
            default:drtPath = StringUtils.EMPTY;
                break;
        }
        return drtPath;
    }
}
