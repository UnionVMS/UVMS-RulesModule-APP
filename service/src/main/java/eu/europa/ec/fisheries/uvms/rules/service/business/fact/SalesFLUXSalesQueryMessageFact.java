package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.SalesQueryType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.Objects;

public class SalesFLUXSalesQueryMessageFact extends AbstractFact {

    protected SalesQueryType salesQuery;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_SALES_QUERY_MESSAGE;
    }
    public SalesQueryType getSalesQuery() {
        return salesQuery;
    }

    public void setSalesQuery(SalesQueryType value) {
        this.salesQuery = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXSalesQueryMessageFact)) return false;
        SalesFLUXSalesQueryMessageFact that = (SalesFLUXSalesQueryMessageFact) o;
        return Objects.equals(salesQuery, that.salesQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salesQuery);
    }
}
