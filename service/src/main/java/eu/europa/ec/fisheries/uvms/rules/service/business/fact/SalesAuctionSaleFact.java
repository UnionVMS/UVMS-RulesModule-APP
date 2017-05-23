package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.SalesCategoryType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

public class SalesAuctionSaleFact extends AbstractFact {

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
        if (o == null || getClass() != o.getClass()) return false;

        SalesAuctionSaleFact that = (SalesAuctionSaleFact) o;

        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) return false;
        return salesCategory == that.salesCategory;
    }

    @Override
    public int hashCode() {
        int result = countryCode != null ? countryCode.hashCode() : 0;
        result = 31 * result + (salesCategory != null ? salesCategory.hashCode() : 0);
        return result;
    }
}
