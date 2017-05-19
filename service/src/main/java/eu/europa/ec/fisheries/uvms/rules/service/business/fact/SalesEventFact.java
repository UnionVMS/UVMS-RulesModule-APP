package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.SalesBatchType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesEventFact extends AbstractFact {

    private DateTimeType occurrenceDateTime;
    private TextType sellerName;
    private TextType buyerName;
    private List<SalesBatchType> relatedSalesBatches;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_EVENT;
    }
}
