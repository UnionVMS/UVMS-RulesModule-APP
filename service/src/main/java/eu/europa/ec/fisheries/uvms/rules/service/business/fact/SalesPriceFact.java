package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AmountType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.math.BigDecimal;
import java.util.List;

public class SalesPriceFact extends AbstractFact {

    private List<AmountType> chargeAmounts;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_PRICE;
    }

    public List<AmountType> getChargeAmounts() {
        return this.chargeAmounts;
    }

    public void setChargeAmounts(List<AmountType> chargeAmounts) {
        this.chargeAmounts = chargeAmounts;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesPriceFact)) return false;
        final SalesPriceFact other = (SalesPriceFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$chargeAmounts = this.getChargeAmounts();
        final Object other$chargeAmounts = other.getChargeAmounts();
        if (this$chargeAmounts == null ? other$chargeAmounts != null : !this$chargeAmounts.equals(other$chargeAmounts))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $chargeAmounts = this.getChargeAmounts();
        result = result * PRIME + ($chargeAmounts == null ? 43 : $chargeAmounts.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesPriceFact;
    }

    // TODO test
    public boolean allValuesGreaterOrEqualToZero(List<AmountType> amountTypes){
        for (AmountType amountType:amountTypes) {
            if (amountType == null || amountType.getValue().compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        }

        return true;
    }

    // TODO test
    public boolean anyValueEqualToZero(List<AmountType> amountTypes){
        for (AmountType amountType:amountTypes) {
            if (amountType == null || amountType.getValue().compareTo(BigDecimal.ZERO) == 0) {
                return true;
            }
        }

        return false;
    }
}
