package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.SalesPriceType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesBatchFact extends AbstractFact {

    private List<IDType> ids;
    private List<AAPProductType> specifiedAAPProducts;
    private SalesPriceType totalSalesPrice;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_BATCH;
    }

    public List<IDType> getIDS() {
        return this.ids;
    }

    public List<AAPProductType> getSpecifiedAAPProducts() {
        return this.specifiedAAPProducts;
    }

    public SalesPriceType getTotalSalesPrice() {
        return this.totalSalesPrice;
    }

    public void setIDS(List<IDType> ids) {
        this.ids = ids;
    }

    public void setSpecifiedAAPProducts(List<AAPProductType> specifiedAAPProducts) {
        this.specifiedAAPProducts = specifiedAAPProducts;
    }

    public void setTotalSalesPrice(SalesPriceType totalSalesPrice) {
        this.totalSalesPrice = totalSalesPrice;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesBatchFact)) return false;
        final SalesBatchFact other = (SalesBatchFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIDS();
        final Object other$ids = other.getIDS();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$specifiedAAPProducts = this.getSpecifiedAAPProducts();
        final Object other$specifiedAAPProducts = other.getSpecifiedAAPProducts();
        if (this$specifiedAAPProducts == null ? other$specifiedAAPProducts != null : !this$specifiedAAPProducts.equals(other$specifiedAAPProducts))
            return false;
        final Object this$totalSalesPrice = this.getTotalSalesPrice();
        final Object other$totalSalesPrice = other.getTotalSalesPrice();
        if (this$totalSalesPrice == null ? other$totalSalesPrice != null : !this$totalSalesPrice.equals(other$totalSalesPrice))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ids = this.getIDS();
        result = result * PRIME + ($ids == null ? 43 : $ids.hashCode());
        final Object $specifiedAAPProducts = this.getSpecifiedAAPProducts();
        result = result * PRIME + ($specifiedAAPProducts == null ? 43 : $specifiedAAPProducts.hashCode());
        final Object $totalSalesPrice = this.getTotalSalesPrice();
        result = result * PRIME + ($totalSalesPrice == null ? 43 : $totalSalesPrice.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesBatchFact;
    }
}
