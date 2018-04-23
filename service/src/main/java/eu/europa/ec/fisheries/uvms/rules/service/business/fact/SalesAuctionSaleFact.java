package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.SalesCategoryType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

import java.util.Objects;

public class SalesAuctionSaleFact extends SalesAbstractFact {

    protected String countryCode;
    protected SalesCategoryType salesCategory;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesAuctionSaleFact)) return false;
        SalesAuctionSaleFact that = (SalesAuctionSaleFact) o;
        return Objects.equals(countryCode, that.countryCode) &&
                salesCategory == that.salesCategory &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, salesCategory, creationDateOfMessage);
    }
}
