package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AAPStockFact extends AbstractFact {

    private IdType idType;

    public AAPStockFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_AAP_STOCK;
    }

}