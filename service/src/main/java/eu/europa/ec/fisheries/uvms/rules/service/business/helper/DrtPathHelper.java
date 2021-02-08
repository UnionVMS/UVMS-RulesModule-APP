/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import org.drools.core.util.StringUtils;

/**
 * Created by kovian on 18/07/2017.
 */
public enum DrtPathHelper {

    FA_DEPARTURE("/templates/FaDeparture.drt"),
    FA_ARRIVAL("/templates/FaArrival.drt"),
    FA_CATCH("/templates/FaCatch.drt"),
    FA_DISCARD("/templates/FaDiscard.drt"),
    FA_ENTRY_TO_SEA("/templates/FaEntryToSea.drt"),
    FA_EXIT_FROM_SEA("/templates/FaExitFromSea.drt"),
    FA_FISHING_OPERATION("/templates/FaFishingOperation.drt"),
    FA_JOINT_FISHING_OPERATION("/templates/FaJointFishingOperation.drt"),
    FA_LANDING("/templates/FaLanding.drt"),
    FA_NOTIFICATION_OF_ARRIVAL("/templates/FaNotificationOfArrival.drt"),
    FA_NOTIFICATION_OF_TRANSHIPMENT_OR_RELOCATION("/templates/FaNotificationOfTranshipmentOrRelocation.drt"),
    FA_DECLARATION_OF_TRANSHIPMENT_OR_RELOCATION("/templates/FaDeclarationOfTranshipmentOrRelocation.drt"),
    FA_REPORT_DOCUMENT("/templates/FaReportDocument.drt"),
    FISHING_ACTIVITY("/templates/FishingActivity.drt"),
    FA_QUERY("/templates/FaQuery.drt"),
    FA_QUERY_PARAMETER("/templates/FaQueryParameter.drt"),
    MOVEMENT_REPORT_DOCUMENT("/templates/MovementReportDocument.drt"),
    MOVEMENT_REPORT_DOCUMENT_ID("/templates/MovementReportDocumentId.drt"),
    MOVEMENT_REPORT_DOC_OWNER_FLUX_PARTY_ID("/templates/MovementReportDocOwnerFluxPartyId.drt"),
    MOVEMENT_VESSEL_TRANSPORT_MEANS_ID("/templates/MovementVesselTransportMeansId.drt"),
    MOVEMENT_SPECIFIED_VESSEL_POSITION_EVENT("/templates/MovementSpecifiedVesselPositionEvent.drt"),
    MOVEMENT_VESSEL_TRANSPORT_MEANS_ID("/templates/MovementVesselTransportMeansId.drt"),
    MOVEMENT_SPECIFIED_VESSEL_POSITION_EVENT("/templates/MovementSpecifiedVesselPositionEvent.drt"),
    FA_RELOCATION("/templates/FaRelocation.drt"),
    FA_RESPONSE("/templates/FaResponse.drt"),
    FA_TRANSHIPMENT("/templates/FaTranshipment.drt"),
    FA_APPLIED_AAP_PROCESS("/templates/FaAppliedAAPProcess.drt"),
    FISHING_GEAR("/templates/FishingGear.drt"),
    FISHING_TRIP("/templates/FishingTrip.drt"),
    FA_FLAP_DOCUMENT("/templates/FaFlapDocument.drt"),
    FLUX_CHARACTERISTIC("/templates/FluxCharacteristics.drt"),
    FLUX_FA_REPORT_MESSAGE("/templates/FluxFaReportMessage.drt"),
    FLUX_LOCATION("/templates/FluxLocation.drt"),
    GEAR_CHARACTERISTIC("/templates/GearCharacteristics.drt"),
    GEAR_PROBLEM("/templates/GearProblem.drt"),
    STRUCTURED_ADDRESS("/templates/StructuredAddress.drt"),
    VESSEL_STORAGE_CHARACTERISTIC("/templates/VesselStorageCharacteristics.drt"),
    VESSEL_TRANSPORT_MEANS("/templates/VesselTransportMeans.drt"),
    FA_VALIDATION_QUALITY_ANALYSIS("/templates/FaValidationQualityAnalysis.drt"),
    FA_VESSEL_POSITION_EVENT("/templates/FaVesselPositionEvent.drt"),
    FA_AAP_STOCK("/templates/FAAAPStock.drt"),
    SIMPLE_ID_TYPE_FACT("/templates/IdTypeTemplate.drt"),
    SALES_FLUX_SALES_REPORT_MESSAGE("/templates/SalesFLUXSalesReportMessage.drt"),
    SALES_FLUX_REPORT_DOCUMENT("/templates/SalesFLUXReportDocument.drt"),
    SALES_FLUX_PARTY("/templates/SalesFLUXParty.drt"),
    SALES_REPORT("/templates/SalesReport.drt"),
    SALES_DOCUMENT("/templates/SalesDocument.drt"),
    SALES_PARTY("/templates/SalesParty.drt"),
    SALES_EVENT("/templates/SalesEvent.drt"),
    SALES_BATCH("/templates/SalesBatch.drt"),
    SALES_AAP_PRODUCT("/templates/SalesAAPProduct.drt"),
    SALES_AAP_PROCESS("/templates/SalesAAPProcess.drt"),
    SALES_SIZE_DISTRIBUTION("/templates/SalesSizeDistribution.drt"),
    SALES_PRICE("/templates/SalesPrice.drt"),
    SALES_FLUX_ORGANIZATION("/templates/SalesFLUXOrganization.drt"),
    SALES_FISHING_ACTIVITY("/templates/SalesFishingActivity.drt"),
    SALES_DELIMITED_PERIOD("/templates/SalesDelimitedPeriod.drt"),
    SALES_VESSEL_TRANSPORT_MEANS("/templates/SalesVesselTransportMeans.drt"),
    SALES_VESSEL_COUNTRY("/templates/SalesVesselCountry.drt"),
    SALES_CONTACT_PARTY("/templates/SalesContactParty.drt"),
    SALES_CONTACT_PERSON("/templates/SalesContactPerson.drt"),
    SALES_FISHING_TRIP("/templates/SalesFishingTrip.drt"),
    SALES_FLUX_LOCATION("/templates/SalesFLUXLocation.drt"),
    SALES_FLUX_GEOGRAPHICAL_COORDINATE("/templates/SalesFLUXGeographicalCoordinate.drt"),
    SALES_STRUCTURED_ADDRESS("/templates/SalesStructuredAddress.drt"),
    SALES_QUERY("/templates/SalesQuery.drt"),
    SALES_FLUX_RESPONSE_DOCUMENT("/templates/SalesFLUXResponseDocument.drt"),
    SALES_VALIDATION_RESULT_DOCUMENT("/templates/SalesValidationResultDocument.drt"),
    SALES_VALIDATION_QUALITY_ANALYSIS("/templates/SalesValidationQualityAnalysis.drt"),
    SALES_REPORT_WRAPPER("/templates/SalesReportWrapper.drt"),
    SALES_AUCTION_SALE("/templates/SalesAuctionSale.drt"),
    SALES_FLUX_SALES_QUERY_MESSAGE("/templates/SalesFLUXSalesQuery.drt"),
    SALES_QUERY_PARAMETER("/templates/SalesQueryParameter.drt"),
    SALES_FLUX_SALES_RESPONSE_MESSAGE("/templates/SalesFLUXSalesResponseMessage.drt");

    private String path;

    DrtPathHelper(String PATH) {
        this.path = PATH;
    }

    public String getPath() {
        return path;
    }

    public static String getDrtPath(FactType factType) {
        try {
            return valueOf(factType.name()).getPath();
        } catch (IllegalArgumentException e) {
            return StringUtils.EMPTY;
        }
    }
}
