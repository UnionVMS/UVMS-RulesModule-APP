package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.SalesPriceType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@ToString
public class SalesBatchFact extends SalesAbstractFact {

    private List<IdType> ids;
    private List<AAPProductType> specifiedAAPProducts;
    private SalesPriceType totalSalesPrice;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_BATCH;
    }

    public List<IdType> getIDS() {
        return this.ids;
    }

    public List<AAPProductType> getSpecifiedAAPProducts() {
        return this.specifiedAAPProducts;
    }

    public SalesPriceType getTotalSalesPrice() {
        return this.totalSalesPrice;
    }

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setSpecifiedAAPProducts(List<AAPProductType> specifiedAAPProducts) {
        this.specifiedAAPProducts = specifiedAAPProducts;
    }

    public void setTotalSalesPrice(SalesPriceType totalSalesPrice) {
        this.totalSalesPrice = totalSalesPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesBatchFact)) return false;
        SalesBatchFact that = (SalesBatchFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(specifiedAAPProducts, that.specifiedAAPProducts) &&
                Objects.equals(totalSalesPrice, that.totalSalesPrice) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage) &&
                Objects.equals(messageDataFlow, that.messageDataFlow) &&
                Objects.equals(creationJavaDateOfMessage, that.creationJavaDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, specifiedAAPProducts, totalSalesPrice, creationDateOfMessage, messageDataFlow, creationJavaDateOfMessage);
    }
}
