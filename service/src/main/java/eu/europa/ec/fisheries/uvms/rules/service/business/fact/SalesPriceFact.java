package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AmountType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.SalesFactHelper;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@ToString
public class SalesPriceFact extends SalesAbstractFact {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesPriceFact)) return false;
        SalesPriceFact that = (SalesPriceFact) o;
        return Objects.equals(chargeAmounts, that.chargeAmounts) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage) &&
                Objects.equals(messageDataFlow, that.messageDataFlow) &&
                Objects.equals(creationJavaDateOfMessage, that.creationJavaDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargeAmounts, creationDateOfMessage, messageDataFlow, creationJavaDateOfMessage);
    }

    public boolean allValuesGreaterOrEqualToZero(List<AmountType> amountTypes) {
        return SalesFactHelper.allValuesGreaterOrEqualToZero(amountTypes);
    }

    public boolean anyValueEqualToZero(List<AmountType> amountTypes) {
        return SalesFactHelper.anyValueEqualToZero(amountTypes);
    }
}
