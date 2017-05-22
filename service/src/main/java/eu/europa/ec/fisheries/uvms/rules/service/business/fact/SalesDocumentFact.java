package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesDocumentFact extends AbstractFact {

    private List<IDType> ids;
    private eu.europa.ec.fisheries.schema.sales.CodeType currencyCode;
    private List<IDType> transportDocumentIDs;
    private List<IDType> salesNoteIDs;
    private List<IDType> takeoverDocumentIDs;
    private List<SalesBatchType> specifiedSalesBatches;
    private List<SalesEventType> specifiedSalesEvents;
    private List<FishingActivityType> specifiedFishingActivities;
    private List<FLUXLocationType> specifiedFLUXLocations;
    private List<SalesPartyType> specifiedSalesParties;
    private VehicleTransportMeansType specifiedVehicleTransportMeans;
    private List<ValidationResultDocumentType> relatedValidationResultDocuments;
    private SalesPriceType totalSalesPrice;
    private FLUXLocationType departureSpecifiedFLUXLocation;
    private FLUXLocationType arrivalSpecifiedFLUXLocation;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_DOCUMENT;
    }

    public List<IDType> getIds() {
        return this.ids;
    }

    public CodeType getCurrencyCode() {
        return this.currencyCode;
    }

    public List<IDType> getTransportDocumentIDs() {
        return this.transportDocumentIDs;
    }

    public List<IDType> getSalesNoteIDs() {
        return this.salesNoteIDs;
    }

    public List<IDType> getTakeoverDocumentIDs() {
        return this.takeoverDocumentIDs;
    }

    public List<SalesBatchType> getSpecifiedSalesBatches() {
        return this.specifiedSalesBatches;
    }

    public List<SalesEventType> getSpecifiedSalesEvents() {
        return this.specifiedSalesEvents;
    }

    public List<FishingActivityType> getSpecifiedFishingActivities() {
        return this.specifiedFishingActivities;
    }

    public List<FLUXLocationType> getSpecifiedFLUXLocations() {
        return this.specifiedFLUXLocations;
    }

    public List<SalesPartyType> getSpecifiedSalesParties() {
        return this.specifiedSalesParties;
    }

    public VehicleTransportMeansType getSpecifiedVehicleTransportMeans() {
        return this.specifiedVehicleTransportMeans;
    }

    public List<ValidationResultDocumentType> getRelatedValidationResultDocuments() {
        return this.relatedValidationResultDocuments;
    }

    public SalesPriceType getTotalSalesPrice() {
        return this.totalSalesPrice;
    }

    public FLUXLocationType getDepartureSpecifiedFLUXLocation() {
        return this.departureSpecifiedFLUXLocation;
    }

    public FLUXLocationType getArrivalSpecifiedFLUXLocation() {
        return this.arrivalSpecifiedFLUXLocation;
    }

    public void setIds(List<IDType> ids) {
        this.ids = ids;
    }

    public void setCurrencyCode(CodeType currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setTransportDocumentIDs(List<IDType> transportDocumentIDs) {
        this.transportDocumentIDs = transportDocumentIDs;
    }

    public void setSalesNoteIDs(List<IDType> salesNoteIDs) {
        this.salesNoteIDs = salesNoteIDs;
    }

    public void setTakeoverDocumentIDs(List<IDType> takeoverDocumentIDs) {
        this.takeoverDocumentIDs = takeoverDocumentIDs;
    }

    public void setSpecifiedSalesBatches(List<SalesBatchType> specifiedSalesBatches) {
        this.specifiedSalesBatches = specifiedSalesBatches;
    }

    public void setSpecifiedSalesEvents(List<SalesEventType> specifiedSalesEvents) {
        this.specifiedSalesEvents = specifiedSalesEvents;
    }

    public void setSpecifiedFishingActivities(List<FishingActivityType> specifiedFishingActivities) {
        this.specifiedFishingActivities = specifiedFishingActivities;
    }

    public void setSpecifiedFLUXLocations(List<FLUXLocationType> specifiedFLUXLocations) {
        this.specifiedFLUXLocations = specifiedFLUXLocations;
    }

    public void setSpecifiedSalesParties(List<SalesPartyType> specifiedSalesParties) {
        this.specifiedSalesParties = specifiedSalesParties;
    }

    public void setSpecifiedVehicleTransportMeans(VehicleTransportMeansType specifiedVehicleTransportMeans) {
        this.specifiedVehicleTransportMeans = specifiedVehicleTransportMeans;
    }

    public void setRelatedValidationResultDocuments(List<ValidationResultDocumentType> relatedValidationResultDocuments) {
        this.relatedValidationResultDocuments = relatedValidationResultDocuments;
    }

    public void setTotalSalesPrice(SalesPriceType totalSalesPrice) {
        this.totalSalesPrice = totalSalesPrice;
    }

    public void setDepartureSpecifiedFLUXLocation(FLUXLocationType departureSpecifiedFLUXLocation) {
        this.departureSpecifiedFLUXLocation = departureSpecifiedFLUXLocation;
    }

    public void setArrivalSpecifiedFLUXLocation(FLUXLocationType arrivalSpecifiedFLUXLocation) {
        this.arrivalSpecifiedFLUXLocation = arrivalSpecifiedFLUXLocation;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesDocumentFact)) return false;
        final SalesDocumentFact other = (SalesDocumentFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIds();
        final Object other$ids = other.getIds();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$currencyCode = this.getCurrencyCode();
        final Object other$currencyCode = other.getCurrencyCode();
        if (this$currencyCode == null ? other$currencyCode != null : !this$currencyCode.equals(other$currencyCode))
            return false;
        final Object this$transportDocumentIDs = this.getTransportDocumentIDs();
        final Object other$transportDocumentIDs = other.getTransportDocumentIDs();
        if (this$transportDocumentIDs == null ? other$transportDocumentIDs != null : !this$transportDocumentIDs.equals(other$transportDocumentIDs))
            return false;
        final Object this$salesNoteIDs = this.getSalesNoteIDs();
        final Object other$salesNoteIDs = other.getSalesNoteIDs();
        if (this$salesNoteIDs == null ? other$salesNoteIDs != null : !this$salesNoteIDs.equals(other$salesNoteIDs))
            return false;
        final Object this$takeoverDocumentIDs = this.getTakeoverDocumentIDs();
        final Object other$takeoverDocumentIDs = other.getTakeoverDocumentIDs();
        if (this$takeoverDocumentIDs == null ? other$takeoverDocumentIDs != null : !this$takeoverDocumentIDs.equals(other$takeoverDocumentIDs))
            return false;
        final Object this$specifiedSalesBatches = this.getSpecifiedSalesBatches();
        final Object other$specifiedSalesBatches = other.getSpecifiedSalesBatches();
        if (this$specifiedSalesBatches == null ? other$specifiedSalesBatches != null : !this$specifiedSalesBatches.equals(other$specifiedSalesBatches))
            return false;
        final Object this$specifiedSalesEvents = this.getSpecifiedSalesEvents();
        final Object other$specifiedSalesEvents = other.getSpecifiedSalesEvents();
        if (this$specifiedSalesEvents == null ? other$specifiedSalesEvents != null : !this$specifiedSalesEvents.equals(other$specifiedSalesEvents))
            return false;
        final Object this$specifiedFishingActivities = this.getSpecifiedFishingActivities();
        final Object other$specifiedFishingActivities = other.getSpecifiedFishingActivities();
        if (this$specifiedFishingActivities == null ? other$specifiedFishingActivities != null : !this$specifiedFishingActivities.equals(other$specifiedFishingActivities))
            return false;
        final Object this$specifiedFLUXLocations = this.getSpecifiedFLUXLocations();
        final Object other$specifiedFLUXLocations = other.getSpecifiedFLUXLocations();
        if (this$specifiedFLUXLocations == null ? other$specifiedFLUXLocations != null : !this$specifiedFLUXLocations.equals(other$specifiedFLUXLocations))
            return false;
        final Object this$specifiedSalesParties = this.getSpecifiedSalesParties();
        final Object other$specifiedSalesParties = other.getSpecifiedSalesParties();
        if (this$specifiedSalesParties == null ? other$specifiedSalesParties != null : !this$specifiedSalesParties.equals(other$specifiedSalesParties))
            return false;
        final Object this$specifiedVehicleTransportMeans = this.getSpecifiedVehicleTransportMeans();
        final Object other$specifiedVehicleTransportMeans = other.getSpecifiedVehicleTransportMeans();
        if (this$specifiedVehicleTransportMeans == null ? other$specifiedVehicleTransportMeans != null : !this$specifiedVehicleTransportMeans.equals(other$specifiedVehicleTransportMeans))
            return false;
        final Object this$relatedValidationResultDocuments = this.getRelatedValidationResultDocuments();
        final Object other$relatedValidationResultDocuments = other.getRelatedValidationResultDocuments();
        if (this$relatedValidationResultDocuments == null ? other$relatedValidationResultDocuments != null : !this$relatedValidationResultDocuments.equals(other$relatedValidationResultDocuments))
            return false;
        final Object this$totalSalesPrice = this.getTotalSalesPrice();
        final Object other$totalSalesPrice = other.getTotalSalesPrice();
        if (this$totalSalesPrice == null ? other$totalSalesPrice != null : !this$totalSalesPrice.equals(other$totalSalesPrice))
            return false;
        final Object this$departureSpecifiedFLUXLocation = this.getDepartureSpecifiedFLUXLocation();
        final Object other$departureSpecifiedFLUXLocation = other.getDepartureSpecifiedFLUXLocation();
        if (this$departureSpecifiedFLUXLocation == null ? other$departureSpecifiedFLUXLocation != null : !this$departureSpecifiedFLUXLocation.equals(other$departureSpecifiedFLUXLocation))
            return false;
        final Object this$arrivalSpecifiedFLUXLocation = this.getArrivalSpecifiedFLUXLocation();
        final Object other$arrivalSpecifiedFLUXLocation = other.getArrivalSpecifiedFLUXLocation();
        if (this$arrivalSpecifiedFLUXLocation == null ? other$arrivalSpecifiedFLUXLocation != null : !this$arrivalSpecifiedFLUXLocation.equals(other$arrivalSpecifiedFLUXLocation))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ids = this.getIds();
        result = result * PRIME + ($ids == null ? 43 : $ids.hashCode());
        final Object $currencyCode = this.getCurrencyCode();
        result = result * PRIME + ($currencyCode == null ? 43 : $currencyCode.hashCode());
        final Object $transportDocumentIDs = this.getTransportDocumentIDs();
        result = result * PRIME + ($transportDocumentIDs == null ? 43 : $transportDocumentIDs.hashCode());
        final Object $salesNoteIDs = this.getSalesNoteIDs();
        result = result * PRIME + ($salesNoteIDs == null ? 43 : $salesNoteIDs.hashCode());
        final Object $takeoverDocumentIDs = this.getTakeoverDocumentIDs();
        result = result * PRIME + ($takeoverDocumentIDs == null ? 43 : $takeoverDocumentIDs.hashCode());
        final Object $specifiedSalesBatches = this.getSpecifiedSalesBatches();
        result = result * PRIME + ($specifiedSalesBatches == null ? 43 : $specifiedSalesBatches.hashCode());
        final Object $specifiedSalesEvents = this.getSpecifiedSalesEvents();
        result = result * PRIME + ($specifiedSalesEvents == null ? 43 : $specifiedSalesEvents.hashCode());
        final Object $specifiedFishingActivities = this.getSpecifiedFishingActivities();
        result = result * PRIME + ($specifiedFishingActivities == null ? 43 : $specifiedFishingActivities.hashCode());
        final Object $specifiedFLUXLocations = this.getSpecifiedFLUXLocations();
        result = result * PRIME + ($specifiedFLUXLocations == null ? 43 : $specifiedFLUXLocations.hashCode());
        final Object $specifiedSalesParties = this.getSpecifiedSalesParties();
        result = result * PRIME + ($specifiedSalesParties == null ? 43 : $specifiedSalesParties.hashCode());
        final Object $specifiedVehicleTransportMeans = this.getSpecifiedVehicleTransportMeans();
        result = result * PRIME + ($specifiedVehicleTransportMeans == null ? 43 : $specifiedVehicleTransportMeans.hashCode());
        final Object $relatedValidationResultDocuments = this.getRelatedValidationResultDocuments();
        result = result * PRIME + ($relatedValidationResultDocuments == null ? 43 : $relatedValidationResultDocuments.hashCode());
        final Object $totalSalesPrice = this.getTotalSalesPrice();
        result = result * PRIME + ($totalSalesPrice == null ? 43 : $totalSalesPrice.hashCode());
        final Object $departureSpecifiedFLUXLocation = this.getDepartureSpecifiedFLUXLocation();
        result = result * PRIME + ($departureSpecifiedFLUXLocation == null ? 43 : $departureSpecifiedFLUXLocation.hashCode());
        final Object $arrivalSpecifiedFLUXLocation = this.getArrivalSpecifiedFLUXLocation();
        result = result * PRIME + ($arrivalSpecifiedFLUXLocation == null ? 43 : $arrivalSpecifiedFLUXLocation.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesDocumentFact;
    }
}
