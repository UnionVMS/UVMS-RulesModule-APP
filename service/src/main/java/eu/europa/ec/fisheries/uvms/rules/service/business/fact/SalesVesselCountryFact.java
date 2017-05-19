package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class SalesVesselCountryFact extends AbstractFact {

    private IDType id;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_VESSEL_COUNTRY;
    }
}
