package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.SalesPriceType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesBatchFact extends AbstractFact {

    private List<IDType> ids;
    private List<AAPProductType> specifiedAAPProducts;
    private SalesPriceType totalSalesPrice;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_BATCH;
    }
}
