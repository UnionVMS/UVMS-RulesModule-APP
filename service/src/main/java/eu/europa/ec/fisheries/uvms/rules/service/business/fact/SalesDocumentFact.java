package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SalesDocumentFact extends SalesAbstractFact {

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


    public boolean isInvalidSalesNoteID() {
        return !validateFormat(salesNoteIDs.get(0).getValue(), FORMATS.EU_SALES_SALES_NOTE_ID.getFormatStr());
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
                Objects.equals(arrivalSpecifiedFLUXLocation, that.arrivalSpecifiedFLUXLocation) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, currencyCode, transportDocumentIDs, salesNoteIDs, takeoverDocumentIDs, specifiedSalesBatches, specifiedSalesEvents, specifiedFishingActivities, specifiedFLUXLocations, specifiedSalesParties, specifiedVehicleTransportMeans, relatedValidationResultDocuments, totalSalesPrice, departureSpecifiedFLUXLocation, arrivalSpecifiedFLUXLocation, creationDateOfMessage);
    }

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

    public boolean isAnySalesDateBeforeLandingDate() {
        if (isEmpty(specifiedFishingActivities) || isEmpty(specifiedSalesEvents)) {
            return false;
        }

        // Due to the setup of the domain design (and related rules) there can be only a single FishingActivity with one delimited period
        List<DelimitedPeriodType> delimitedPeriods = getSpecifiedFishingActivities().get(0).getSpecifiedDelimitedPeriods();
        if (delimitedPeriods == null || delimitedPeriods.size() == 0 || delimitedPeriods.get(0).getStartDateTime() == null || delimitedPeriods.get(0).getStartDateTime().getDateTime() == null) {
            return false;
        }

        long startTimeInMillis = delimitedPeriods.get(0).getStartDateTime().getDateTime().getMillis();
        for (SalesEventType salesEvent:getSpecifiedSalesEvents()){
            if(salesEvent!= null && salesEvent.getOccurrenceDateTime() != null && salesEvent.getOccurrenceDateTime().getDateTime() != null
                    && startTimeInMillis > salesEvent.getOccurrenceDateTime().getDateTime().getMillis()){
                return true;
            }
        }

        return false;
    }

    public boolean isTotalPriceFieldDifferentFromSumOfProducts(){
        // Field is optional so no value is ok
        if(totalSalesPrice == null
                || isEmpty(totalSalesPrice.getChargeAmounts())
                || totalSalesPrice.getChargeAmounts().get(0) == null
                || totalSalesPrice.getChargeAmounts().get(0).getValue() == null){
            return false;
        }

        return getTotalOfAllProducts().compareTo(totalSalesPrice.getChargeAmounts().get(0).getValue()) != 0;
    }

    public boolean hasTheNationalNumberPartOfTheIDAnIncorrectFormat() {
        return ids != null && !ids.isEmpty() && !validateFormat(ids.get(0).getValue(), AbstractFact.FORMATS.EU_SALES_ID_SPECIFIC.getFormatStr());
    }

    public boolean hasTheCommonPartOfTheIDAnIncorrectFormat() {
        return ids != null && !ids.isEmpty() && !validateFormat(ids.get(0).getValue(), AbstractFact.FORMATS.EU_SALES_ID_COMMON.getFormatStr());
    }

    public boolean hasTheTakeOverDocumentIdAnIncorrectFormat() {
        return takeoverDocumentIDs != null && !takeoverDocumentIDs.isEmpty() && !validateFormat(takeoverDocumentIDs.get(0).getValue(), AbstractFact.FORMATS.EU_SALES_TAKE_OVER_DOCUMENT_ID.getFormatStr());
    }


    private BigDecimal getTotalOfAllProducts() {
        if (isEmpty(specifiedSalesBatches)) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal price : getPriceOfEveryProduct()) {
            total = total.add(price);
        }

        return total;
    }

    private List<BigDecimal> getPriceOfEveryProduct() {
        List<BigDecimal> prices = new ArrayList<>();
        for (AAPProductType product : getAllProducts()) {
            if (product.getTotalSalesPrice() != null
                    && !isEmpty(product.getTotalSalesPrice().getChargeAmounts())
                    && product.getTotalSalesPrice().getChargeAmounts().get(0) != null
                    && product.getTotalSalesPrice().getChargeAmounts().get(0).getValue() != null) {
                prices.add(product.getTotalSalesPrice().getChargeAmounts().get(0).getValue());
            }
        }
        return prices;
    }

    private List<AAPProductType> getAllProducts() {
        List<AAPProductType> products = new ArrayList<>();
        for (SalesBatchType salesBatch : specifiedSalesBatches) {
            if(!isEmpty(salesBatch.getSpecifiedAAPProducts())){
                for (AAPProductType product: salesBatch.getSpecifiedAAPProducts()) {
                    if (product != null) {
                        products.add(product);
                    }
                }
            }
        }
        return products;
    }

    public Optional<String> getCurrencyCodeIfExists() {
        if (currencyCode != null && isNotBlank(currencyCode.getValue())) {
            return Optional.of(currencyCode.getValue());
        } else {
            return Optional.absent();
        }
    }

    public Optional<String> getCountryIfExists() {
        if (isNotEmpty(specifiedFLUXLocations)
                && specifiedFLUXLocations.get(0) != null
                && specifiedFLUXLocations.get(0).getCountryID() != null
                && isNotBlank(specifiedFLUXLocations.get(0).getCountryID().getValue())) {
            return Optional.of(specifiedFLUXLocations.get(0).getCountryID().getValue());
        } else {
            return Optional.absent();
        }
    }

    public Optional<DateTime> getOccurrenceIfPresent() {
        if (isNotEmpty(specifiedSalesEvents)
            && specifiedSalesEvents.get(0) != null
            && specifiedSalesEvents.get(0).getOccurrenceDateTime() != null
            && specifiedSalesEvents.get(0).getOccurrenceDateTime().getDateTime() != null) {
            return Optional.of(specifiedSalesEvents.get(0).getOccurrenceDateTime().getDateTime());
        } else {
            return Optional.absent();
        }
    }

    // This is here because of a bug.
    // Calling the isEmpty() method of AbstractFact
    // from the expression of rule SALE-L01-00-0036
    // we get an index out of bounds exception.
    // For some reason, adding this fixes the problem
    @Override
    public boolean isEmpty(Collection collection) {
        return super.isEmpty(collection);
    }

}
