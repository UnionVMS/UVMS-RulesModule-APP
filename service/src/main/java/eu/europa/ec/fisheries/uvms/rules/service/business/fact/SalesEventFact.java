package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.SalesBatchType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesEventFact)) return false;
        final SalesEventFact other = (SalesEventFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$occurrenceDateTime = this.getOccurrenceDateTime();
        final Object other$occurrenceDateTime = other.getOccurrenceDateTime();
        if (this$occurrenceDateTime == null ? other$occurrenceDateTime != null : !this$occurrenceDateTime.equals(other$occurrenceDateTime))
            return false;
        final Object this$sellerName = this.getSellerName();
        final Object other$sellerName = other.getSellerName();
        if (this$sellerName == null ? other$sellerName != null : !this$sellerName.equals(other$sellerName))
            return false;
        final Object this$buyerName = this.getBuyerName();
        final Object other$buyerName = other.getBuyerName();
        if (this$buyerName == null ? other$buyerName != null : !this$buyerName.equals(other$buyerName)) return false;
        final Object this$relatedSalesBatches = this.getRelatedSalesBatches();
        final Object other$relatedSalesBatches = other.getRelatedSalesBatches();
        if (this$relatedSalesBatches == null ? other$relatedSalesBatches != null : !this$relatedSalesBatches.equals(other$relatedSalesBatches))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $occurrenceDateTime = this.getOccurrenceDateTime();
        result = result * PRIME + ($occurrenceDateTime == null ? 43 : $occurrenceDateTime.hashCode());
        final Object $sellerName = this.getSellerName();
        result = result * PRIME + ($sellerName == null ? 43 : $sellerName.hashCode());
        final Object $buyerName = this.getBuyerName();
        result = result * PRIME + ($buyerName == null ? 43 : $buyerName.hashCode());
        final Object $relatedSalesBatches = this.getRelatedSalesBatches();
        result = result * PRIME + ($relatedSalesBatches == null ? 43 : $relatedSalesBatches.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesEventFact;
    }
}
