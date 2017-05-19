package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
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
public class SalesValidationQualityAnalysisFact extends AbstractFact {

    private eu.europa.ec.fisheries.schema.sales.CodeType levelCode;
    private eu.europa.ec.fisheries.schema.sales.CodeType typeCode;
    private List<TextType> results;
    private IDType id;
    private TextType description;
    private List<TextType> referencedItems;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_VALIDATION_QUALITY_ANALYSIS;
    }
}
