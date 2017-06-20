package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesValidationResultDocumentFact extends AbstractFact {

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesValidationResultDocumentFact)) return false;
        final SalesValidationResultDocumentFact other = (SalesValidationResultDocumentFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$validatorID = this.getValidatorID();
        final Object other$validatorID = other.getValidatorID();
        if (this$validatorID == null ? other$validatorID != null : !this$validatorID.equals(other$validatorID))
            return false;
        final Object this$creationDateTime = this.getCreationDateTime();
        final Object other$creationDateTime = other.getCreationDateTime();
        if (this$creationDateTime == null ? other$creationDateTime != null : !this$creationDateTime.equals(other$creationDateTime))
            return false;
        final Object this$relatedValidationQualityAnalysises = this.getRelatedValidationQualityAnalysises();
        final Object other$relatedValidationQualityAnalysises = other.getRelatedValidationQualityAnalysises();
        if (this$relatedValidationQualityAnalysises == null ? other$relatedValidationQualityAnalysises != null : !this$relatedValidationQualityAnalysises.equals(other$relatedValidationQualityAnalysises))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $validatorID = this.getValidatorID();
        result = result * PRIME + ($validatorID == null ? 43 : $validatorID.hashCode());
        final Object $creationDateTime = this.getCreationDateTime();
        result = result * PRIME + ($creationDateTime == null ? 43 : $creationDateTime.hashCode());
        final Object $relatedValidationQualityAnalysises = this.getRelatedValidationQualityAnalysises();
        result = result * PRIME + ($relatedValidationQualityAnalysises == null ? 43 : $relatedValidationQualityAnalysises.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesValidationResultDocumentFact;
    }
}
