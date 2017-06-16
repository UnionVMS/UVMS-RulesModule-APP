package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXResponseDocumentType;
import eu.europa.ec.fisheries.schema.sales.SalesReportType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

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
}
