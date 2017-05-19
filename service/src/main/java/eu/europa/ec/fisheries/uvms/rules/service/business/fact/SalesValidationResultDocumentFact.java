package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesValidationResultDocumentFact extends AbstractFact {

    private IDType validatorID;
    private DateTimeType creationDateTime;
    private List<ValidationQualityAnalysisType> relatedValidationQualityAnalysises;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_VALIDATION_RESULT_DOCUMENT;
    }
}
