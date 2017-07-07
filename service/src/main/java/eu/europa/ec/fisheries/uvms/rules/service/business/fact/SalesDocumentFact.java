package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.SalesFactHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SalesDocumentFact extends AbstractFact {

    private List<IdType> ids;
    private CodeType currencyCode;
    private List<IdType> transportDocumentIDs;
    private List<IdType> salesNoteIDs;
    private List<IdType> takeoverDocumentIDs;
    private List<SalesBatchType> specifiedSalesBatches;
    private List<SalesEventType> specifiedSalesEvents;
    private List<FishingActivityType> specifiedFishingActivities;
    private List<FLUXLocationType> specifiedFLUXLocations;
    private List<SalesPartyFact> specifiedSalesParties;
    private VehicleTransportMeansType specifiedVehicleTransportMeans;
    private List<ValidationResultDocumentType> relatedValidationResultDocuments;
    private SalesPriceType totalSalesPrice;
    private FLUXLocationType departureSpecifiedFLUXLocation;
    private FLUXLocationType arrivalSpecifiedFLUXLocation;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_DOCUMENT;
    }

    public List<IdType> getIDS() {
        return this.ids;
    }

    public CodeType getCurrencyCode() {
        return this.currencyCode;
    }

    public List<IdType> getTransportDocumentIDs() {
        return this.transportDocumentIDs;
    }

    public List<IdType> getSalesNoteIDs() {
        return this.salesNoteIDs;
    }

    public List<IdType> getTakeoverDocumentIDs() {
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

    public List<SalesPartyFact> getSpecifiedSalesParties() {
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

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setCurrencyCode(CodeType currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setTransportDocumentIDs(List<IdType> transportDocumentIDs) {
        this.transportDocumentIDs = transportDocumentIDs;
    }

    public void setSalesNoteIDs(List<IdType> salesNoteIDs) {
        this.salesNoteIDs = salesNoteIDs;
    }

    public void setTakeoverDocumentIDs(List<IdType> takeoverDocumentIDs) {
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

    public void setSpecifiedSalesParties(List<SalesPartyFact> specifiedSalesParties) {
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


    public boolean isInvalidCurrencyCode() {
        return !SalesFactHelper.doesSetContainAnyValue(Arrays.asList(currencyCode.getValue()), SalesFactHelper.getValidCurrencies());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesDocumentFact)) return false;
        SalesDocumentFact that = (SalesDocumentFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(transportDocumentIDs, that.transportDocumentIDs) &&
                Objects.equals(salesNoteIDs, that.salesNoteIDs) &&
                Objects.equals(takeoverDocumentIDs, that.takeoverDocumentIDs) &&
                Objects.equals(specifiedSalesBatches, that.specifiedSalesBatches) &&
                Objects.equals(specifiedSalesEvents, that.specifiedSalesEvents) &&
                Objects.equals(specifiedFishingActivities, that.specifiedFishingActivities) &&
                Objects.equals(specifiedFLUXLocations, that.specifiedFLUXLocations) &&
                Objects.equals(specifiedSalesParties, that.specifiedSalesParties) &&
                Objects.equals(specifiedVehicleTransportMeans, that.specifiedVehicleTransportMeans) &&
                Objects.equals(relatedValidationResultDocuments, that.relatedValidationResultDocuments) &&
                Objects.equals(totalSalesPrice, that.totalSalesPrice) &&
                Objects.equals(departureSpecifiedFLUXLocation, that.departureSpecifiedFLUXLocation) &&
                Objects.equals(arrivalSpecifiedFLUXLocation, that.arrivalSpecifiedFLUXLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, currencyCode, transportDocumentIDs, salesNoteIDs, takeoverDocumentIDs, specifiedSalesBatches, specifiedSalesEvents, specifiedFishingActivities, specifiedFLUXLocations, specifiedSalesParties, specifiedVehicleTransportMeans, relatedValidationResultDocuments, totalSalesPrice, departureSpecifiedFLUXLocation, arrivalSpecifiedFLUXLocation);
    }

    // TODO test
    public boolean doesDocumentContainDuplicateSalesPartyRoles() {
        if (isEmpty(specifiedSalesParties)) {
            return false;
        }

        List<String> roles = new ArrayList<>();
        for (SalesPartyFact salesParty : specifiedSalesParties) {
            if (salesParty != null && !isEmpty(salesParty.getRoleCodes())) {
                for (CodeType roleCode : salesParty.getRoleCodes()) {
                    if (roleCode != null && !roleCode.getValue().isEmpty()) {
                        if (roles.contains(roleCode.getValue())) {
                            return true;
                        } else {
                            roles.add(roleCode.getValue());
                        }
                    }
                }
            }
        }

        return false;
    }

    // TODO test
    public boolean isLadingDateBeforeAnySalesDate() {
        if (isEmpty(specifiedFishingActivities) || isEmpty(specifiedSalesEvents)) {
            return false;
        }

        // Due to the setup of the domain design (and related rules) there can be only a single FishingActivity with one delimited period
        List<DelimitedPeriodType> delimitedPeriods = getSpecifiedFishingActivities().get(0).getSpecifiedDelimitedPeriods();
        if (delimitedPeriods == null || delimitedPeriods.size() == 0 || delimitedPeriods.get(0).getStartDateTime() == null) {
            return false;
        }

        long startTimeInMillis = delimitedPeriods.get(0).getStartDateTime().getDateTime().getMillis();
        for (SalesEventType salesEvent:getSpecifiedSalesEvents()){
            if(salesEvent!= null && salesEvent.getOccurrenceDateTime() != null
                    && startTimeInMillis > salesEvent.getOccurrenceDateTime().getDateTime().getMillis()){
                return true;
            }
        }

        return false;
    }

    // TODO test
    public boolean isTotalPriceFieldDifferentFromSumOfProducts(){
        // Field is optional so no value is ok
        if(totalSalesPrice == null ||  isEmpty(totalSalesPrice.getChargeAmounts())){
            return false;
        }

        return getTotalOfAllProducts().compareTo(getSum(totalSalesPrice.getChargeAmounts())) != 0;
    }

    private BigDecimal getTotalOfAllProducts(){

        if(isEmpty(specifiedSalesBatches))        {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (SalesBatchType salesBatch :specifiedSalesBatches) {
            if(!isEmpty(salesBatch.getSpecifiedAAPProducts())){
                for (AAPProductType product: salesBatch.getSpecifiedAAPProducts()) {
                    if (product != null && product.getTotalSalesPrice() != null){
                        total.add(getSum(product.getTotalSalesPrice().getChargeAmounts()));
                    }
                }
            }
        }

        return total;
    }

    private BigDecimal getSum(List<AmountType> amounts){
        if(isEmpty(amounts))        {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (AmountType amount :amounts) {
            if (amount != null){
                total.add(amount.getValue());
            }
        }

        return total;
    }
}
