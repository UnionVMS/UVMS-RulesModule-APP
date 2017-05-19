package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXReportDocumentType;
import eu.europa.ec.fisheries.schema.sales.SalesReportType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesFLUXSalesReportMessageFact extends AbstractFact {

    private FLUXReportDocumentType fluxReportDocument;
    private List<SalesReportType> salesReports;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_SALES_REPORT_MESSAGE;
    }
}
