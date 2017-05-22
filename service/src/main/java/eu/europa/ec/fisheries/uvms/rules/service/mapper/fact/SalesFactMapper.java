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

import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@Mapper(uses = CustomMapper.class)
public interface SalesFactMapper {

    SalesFactMapper INSTANCE = Mappers.getMapper(SalesFactMapper.class);
    String AAP_PRODUCT_PACKAGING_UNIT_QUANTITY = "PackagingUnitQuantity";
    String AAP_PRODUCT_WEIGHT_MEASURE = "WeightMeasure";
    String AAP_PRODUCT_AVERAGE_WEIGHT_MEASURE = "AverageWeightMeasure";
    String AAP_PRODUCT_UNIT_QUANTITY = "UnitQuantity";
    String CODE_TYPE_FOR_FACATCH_FLUXLOCATION = "facatchFluxlocationTypeCode";
    String CODE_TYPE_FOR_FACATCH = "facatchTypeCode";

//    @Mappings({
//            @Mapping(target = "fluxReportDocument", source = "fluxReportDocument"),
//            @Mapping(target = "salesReports", source = "salesReports")
//    })
    SalesFLUXSalesReportMessageFact generateFactForFLUXSalesReportMessage(FLUXSalesReportMessage fluxSalesReportMessage);

    SalesAAPProcessFact generateFactForAAPProcessFact(AAPProcessType salesFact);
    SalesAAPProductFact generateFactForAAPProductFact(AAPProductType salesFact);
    SalesBatchFact generateFactForBatchFact(SalesBatchType salesFact);
    SalesContactPartyFact generateFactForContactPartyFact(ContactPartyType salesFact);
    SalesContactPersonFact generateFactForContactPersonFact(ContactPersonType salesFact);
    SalesDelimitedPeriodFact generateFactForSalesDelimitedPeriodFact(DelimitedPeriodType salesFact);
    SalesDocumentFact generateFactForSalesDocument(SalesDocumentType salesFact);
    List<SalesDocumentFact> generateFactForSalesDocument(List<SalesDocumentType> salesFact);
    SalesEventFact generateFactForSalesEvent(SalesEventType salesFact);
    SalesFishingActivityFact generateFactForSalesFishingActivity(FishingActivityType salesFact);
    SalesFishingTripFact generateFactForSalesFishingTrip(FishingTripType salesFact);
    SalesFLUXGeographicalCoordinateFact generateFactForFLUXGeographicalCoordinateFact(FLUXGeographicalCoordinateType salesFact);
    SalesFLUXLocationFact generateFactForFLUXLocationFact(FLUXLocationType salesFact);
    SalesFLUXOrganizationFact generateFactForFLUXOrganizationFact(FLUXOrganizationType salesFact);
    SalesFLUXPartyFact generateFactForFLUXPartyFact(FLUXPartyType salesFact);
    SalesFLUXReportDocumentFact generateFactForSalesFLUXReportDocumentFact(FLUXReportDocumentType salesFact);
    SalesFLUXResponseDocumentFact generateFactForSalesFLUXResponseDocumentFact(FLUXResponseDocumentType salesFact);
    SalesFLUXSalesReportMessageFact generateFactForSalesFLUXSalesReportMessageFact(FLUXSalesReportMessage salesFact);
    SalesPartyFact generateFactForSalesPartyFact(SalesPartyType salesFact);
    SalesPriceFact generateFactForSalesPriceFact(SalesPriceType salesFact);
    SalesQueryFact generateFactForSalesQueryFact(SalesQueryType salesFact);
    SalesReportFact generateFactForSalesReportFact(SalesReportType salesFact);
    List<SalesReportFact> generateFactForSalesReportFact(List<SalesReportType> salesFact);
    SalesSizeDistributionFact generateFactForSalesSizeDistributionFact(SizeDistributionType salesFact);
    SalesStructuredAddressFact generateFactForSalesStructuredAddressFact(StructuredAddressType salesFact);
    SalesValidationQualityAnalysisFact generateFactForSalesValidationQualityAnalysisFact(ValidationQualityAnalysisType salesFact);
    SalesValidationResultDocumentFact generateFactForSalesValidationResultDocumentFact(ValidationResultDocumentType salesFact);
    SalesVesselCountryFact generateFactForSalesVesselCountryFact(VesselCountryType salesFact);
    SalesVesselTransportMeansFact generateFactForSalesVesselTransportMeansFact(VesselTransportMeansType salesFact);


}
