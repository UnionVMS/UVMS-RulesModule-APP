package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.List;
import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

public class SalesValidationResultDocumentFact extends SalesAbstractFact {

    private IdType validatorID;
    private DateTimeType creationDateTime;
    private List<ValidationQualityAnalysisType> relatedValidationQualityAnalysises;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_VALIDATION_RESULT_DOCUMENT;
    }

    public IdType getValidatorID() {
        return this.validatorID;
    }

    public DateTimeType getCreationDateTime() {
        return this.creationDateTime;
    }

    public List<ValidationQualityAnalysisType> getRelatedValidationQualityAnalysises() {
        return this.relatedValidationQualityAnalysises;
    }

    public void setValidatorID(IdType validatorID) {
        this.validatorID = validatorID;
    }

    public void setCreationDateTime(DateTimeType creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public void setRelatedValidationQualityAnalysises(List<ValidationQualityAnalysisType> relatedValidationQualityAnalysises) {
        this.relatedValidationQualityAnalysises = relatedValidationQualityAnalysises;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesValidationResultDocumentFact)) return false;
        SalesValidationResultDocumentFact that = (SalesValidationResultDocumentFact) o;
        return Objects.equals(validatorID, that.validatorID) &&
                Objects.equals(creationDateTime, that.creationDateTime) &&
                Objects.equals(relatedValidationQualityAnalysises, that.relatedValidationQualityAnalysises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validatorID, creationDateTime, relatedValidationQualityAnalysises);
    }
}
