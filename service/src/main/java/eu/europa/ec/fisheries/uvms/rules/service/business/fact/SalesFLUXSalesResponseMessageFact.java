package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXResponseDocumentType;
import eu.europa.ec.fisheries.schema.sales.SalesReportType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesFLUXSalesResponseMessageFact extends AbstractFact {

    protected FLUXResponseDocumentType fluxResponseDocument;

    protected List<SalesReportType> salesReports;


    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_SALES_RESPONSE_MESSAGE;
    }

    public FLUXResponseDocumentType getFluxResponseDocument() {
        return fluxResponseDocument;
    }

    public void setFluxResponseDocument(FLUXResponseDocumentType fluxResponseDocument) {
        this.fluxResponseDocument = fluxResponseDocument;
    }

    public List<SalesReportType> getSalesReports() {
        return salesReports;
    }

    public void setSalesReports(List<SalesReportType> salesReports) {
        this.salesReports = salesReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXSalesResponseMessageFact)) return false;
        SalesFLUXSalesResponseMessageFact that = (SalesFLUXSalesResponseMessageFact) o;
        return Objects.equals(fluxResponseDocument, that.fluxResponseDocument) &&
                Objects.equals(salesReports, that.salesReports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluxResponseDocument, salesReports);
    }
}
