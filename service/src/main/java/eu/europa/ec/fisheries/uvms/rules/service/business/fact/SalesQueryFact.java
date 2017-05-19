package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesQueryFact extends AbstractFact {

    private IDType id;
    private DateTimeType submittedDateTime;
    private eu.europa.ec.fisheries.schema.sales.CodeType typeCode;
    private DelimitedPeriodType specifiedDelimitedPeriod;
    private FLUXPartyType submitterFLUXParty;
    private List<SalesQueryParameterType> simpleSalesQueryParameters;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_QUERY;
    }
}
