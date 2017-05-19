package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.FACatchType;
import eu.europa.ec.fisheries.schema.sales.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesAAPProcessFact extends AbstractFact {

    private List<CodeType> typeCodes;
    private NumericType conversionFactorNumeric;
    private List<FACatchType> usedFACatches;
    private List<AAPProductType> resultAAPProducts;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_AAP_PROCESS;
    }
}
