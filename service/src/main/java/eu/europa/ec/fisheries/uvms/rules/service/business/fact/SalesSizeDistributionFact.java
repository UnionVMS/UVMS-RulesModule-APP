package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesSizeDistributionFact extends AbstractFact {

    private eu.europa.ec.fisheries.schema.sales.CodeType categoryCode;
    private List<CodeType> classCodes;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_SIZE_DISTRIBUTION;
    }
}
