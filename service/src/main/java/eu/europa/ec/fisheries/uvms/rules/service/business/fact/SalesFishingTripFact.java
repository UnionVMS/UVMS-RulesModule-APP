package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DelimitedPeriodType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesFishingTripFact extends AbstractFact {

    private List<IDType> ids;
    private eu.europa.ec.fisheries.schema.sales.CodeType typeCode;
    private List<DelimitedPeriodType> specifiedDelimitedPeriods;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FISHING_TRIP;
    }
}
