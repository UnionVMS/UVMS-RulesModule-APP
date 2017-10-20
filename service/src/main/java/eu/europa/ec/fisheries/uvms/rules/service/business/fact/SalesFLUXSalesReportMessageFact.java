package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXReportDocumentType;
import eu.europa.ec.fisheries.schema.sales.SalesReportType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesFLUXSalesReportMessageFact extends SalesAbstractFact {

    private FLUXReportDocumentType fluxReportDocument;
    private List<SalesReportType> salesReports;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_SALES_REPORT_MESSAGE;
    }

    public FLUXReportDocumentType getFLUXReportDocument() {
        return this.fluxReportDocument;
    }

    public List<SalesReportType> getSalesReports() {
        return this.salesReports;
    }

    public void setFLUXReportDocument(FLUXReportDocumentType fluxReportDocument) {
        this.fluxReportDocument = fluxReportDocument;
    }

    public void setSalesReports(List<SalesReportType> salesReports) {
        this.salesReports = salesReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXSalesReportMessageFact)) return false;
        SalesFLUXSalesReportMessageFact that = (SalesFLUXSalesReportMessageFact) o;
        return Objects.equals(fluxReportDocument, that.fluxReportDocument) &&
                Objects.equals(salesReports, that.salesReports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluxReportDocument, salesReports);
    }
}
