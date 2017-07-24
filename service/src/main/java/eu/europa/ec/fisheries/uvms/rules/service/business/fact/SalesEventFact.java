package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.List;
import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.SalesBatchType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

public class SalesEventFact extends AbstractFact {

    private DateTimeType occurrenceDateTime;
    private TextType sellerName;
    private TextType buyerName;
    private List<SalesBatchType> relatedSalesBatches;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_EVENT;
    }

    public DateTimeType getOccurrenceDateTime() {
        return this.occurrenceDateTime;
    }

    public TextType getSellerName() {
        return this.sellerName;
    }

    public TextType getBuyerName() {
        return this.buyerName;
    }

    public List<SalesBatchType> getRelatedSalesBatches() {
        return this.relatedSalesBatches;
    }

    public void setOccurrenceDateTime(DateTimeType occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
    }

    public void setSellerName(TextType sellerName) {
        this.sellerName = sellerName;
    }

    public void setBuyerName(TextType buyerName) {
        this.buyerName = buyerName;
    }

    public void setRelatedSalesBatches(List<SalesBatchType> relatedSalesBatches) {
        this.relatedSalesBatches = relatedSalesBatches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesEventFact)) return false;
        SalesEventFact that = (SalesEventFact) o;
        return Objects.equals(occurrenceDateTime, that.occurrenceDateTime) &&
                Objects.equals(sellerName, that.sellerName) &&
                Objects.equals(buyerName, that.buyerName) &&
                Objects.equals(relatedSalesBatches, that.relatedSalesBatches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(occurrenceDateTime, sellerName, buyerName, relatedSalesBatches);
    }
}
