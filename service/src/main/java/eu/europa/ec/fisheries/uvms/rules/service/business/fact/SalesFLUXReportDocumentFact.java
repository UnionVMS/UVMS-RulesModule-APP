package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.FLUXPartyType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesFLUXReportDocumentFact extends AbstractFact {

    private List<IDType> ids;
    private IDType referencedID;
    private DateTimeType creationDateTime;
    private eu.europa.ec.fisheries.schema.sales.CodeType purposeCode;
    private TextType purpose;
    private eu.europa.ec.fisheries.schema.sales.CodeType typeCode;
    private FLUXPartyType ownerFLUXParty;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_REPORT_DOCUMENT;
    }
}
