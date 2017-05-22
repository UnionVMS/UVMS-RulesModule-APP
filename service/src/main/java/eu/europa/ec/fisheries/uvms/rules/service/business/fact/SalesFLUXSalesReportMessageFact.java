package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXReportDocumentType;
import eu.europa.ec.fisheries.schema.sales.SalesReportType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFLUXSalesReportMessageFact extends AbstractFact {

    private FLUXReportDocumentType fluxReportDocument;
    private List<SalesReportType> salesReports;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_SALES_REPORT_MESSAGE;
    }

    public FLUXReportDocumentType getFluxReportDocument() {
        return this.fluxReportDocument;
    }

    public List<SalesReportType> getSalesReports() {
        return this.salesReports;
    }

    public void setFluxReportDocument(FLUXReportDocumentType fluxReportDocument) {
        this.fluxReportDocument = fluxReportDocument;
    }

    public void setSalesReports(List<SalesReportType> salesReports) {
        this.salesReports = salesReports;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFLUXSalesReportMessageFact)) return false;
        final SalesFLUXSalesReportMessageFact other = (SalesFLUXSalesReportMessageFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$fluxReportDocument = this.getFluxReportDocument();
        final Object other$fluxReportDocument = other.getFluxReportDocument();
        if (this$fluxReportDocument == null ? other$fluxReportDocument != null : !this$fluxReportDocument.equals(other$fluxReportDocument))
            return false;
        final Object this$salesReports = this.getSalesReports();
        final Object other$salesReports = other.getSalesReports();
        if (this$salesReports == null ? other$salesReports != null : !this$salesReports.equals(other$salesReports))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fluxReportDocument = this.getFluxReportDocument();
        result = result * PRIME + ($fluxReportDocument == null ? 43 : $fluxReportDocument.hashCode());
        final Object $salesReports = this.getSalesReports();
        result = result * PRIME + ($salesReports == null ? 43 : $salesReports.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFLUXSalesReportMessageFact;
    }
}
