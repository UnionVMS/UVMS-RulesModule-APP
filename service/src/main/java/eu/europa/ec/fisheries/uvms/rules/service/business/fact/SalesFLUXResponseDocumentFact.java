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
public class SalesFLUXResponseDocumentFact extends AbstractFact {

    private List<IDType> ids;
    private IDType referencedID;
    private DateTimeType creationDateTime;
    private eu.europa.ec.fisheries.schema.sales.CodeType responseCode;
    private TextType remarks;
    private TextType rejectionReason;
    private eu.europa.ec.fisheries.schema.sales.CodeType typeCode;
    private List<ValidationResultDocumentType> relatedValidationResultDocuments;
    private FLUXPartyType respondentFLUXParty;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_RESPONSE_DOCUMENT;
    }
}
