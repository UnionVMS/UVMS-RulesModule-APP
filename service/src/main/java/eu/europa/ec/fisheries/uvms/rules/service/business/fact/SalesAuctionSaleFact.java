package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.SalesCategoryType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.Objects;

@ToString
public class SalesAuctionSaleFact extends SalesAbstractFact {

    protected String countryCode;
    protected SalesCategoryType salesCategory;
    protected String supplier;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_AUCTION_SALE;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public SalesCategoryType getSalesCategory() {
        return salesCategory;
    }

    public void setSalesCategory(SalesCategoryType salesCategory) {
        this.salesCategory = salesCategory;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesAuctionSaleFact)) return false;
        SalesAuctionSaleFact that = (SalesAuctionSaleFact) o;
        return Objects.equals(countryCode, that.countryCode) &&
                salesCategory == that.salesCategory &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage) &&
                Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, salesCategory, creationDateOfMessage, supplier);
    }
}
