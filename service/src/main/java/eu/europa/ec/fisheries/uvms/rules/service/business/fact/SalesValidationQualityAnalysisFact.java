package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesValidationQualityAnalysisFact extends AbstractFact {

    private CodeType levelCode;
    private CodeType typeCode;
    private List<TextType> results;
    private IdType id;
    private TextType description;
    private List<TextType> referencedItems;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_VALIDATION_QUALITY_ANALYSIS;
    }

    public CodeType getLevelCode() {
        return this.levelCode;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public List<TextType> getResults() {
        return this.results;
    }

    public IdType getID() {
        return this.id;
    }

    public TextType getDescription() {
        return this.description;
    }

    public List<TextType> getReferencedItems() {
        return this.referencedItems;
    }

    public void setLevelCode(CodeType levelCode) {
        this.levelCode = levelCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setResults(List<TextType> results) {
        this.results = results;
    }

    public void setID(IdType id) {
        this.id = id;
    }

    public void setDescription(TextType description) {
        this.description = description;
    }

    public void setReferencedItems(List<TextType> referencedItems) {
        this.referencedItems = referencedItems;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesValidationQualityAnalysisFact)) return false;
        final SalesValidationQualityAnalysisFact other = (SalesValidationQualityAnalysisFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$levelCode = this.getLevelCode();
        final Object other$levelCode = other.getLevelCode();
        if (this$levelCode == null ? other$levelCode != null : !this$levelCode.equals(other$levelCode)) return false;
        final Object this$typeCode = this.getTypeCode();
        final Object other$typeCode = other.getTypeCode();
        if (this$typeCode == null ? other$typeCode != null : !this$typeCode.equals(other$typeCode)) return false;
        final Object this$results = this.getResults();
        final Object other$results = other.getResults();
        if (this$results == null ? other$results != null : !this$results.equals(other$results)) return false;
        final Object this$id = this.getID();
        final Object other$id = other.getID();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$referencedItems = this.getReferencedItems();
        final Object other$referencedItems = other.getReferencedItems();
        if (this$referencedItems == null ? other$referencedItems != null : !this$referencedItems.equals(other$referencedItems))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $levelCode = this.getLevelCode();
        result = result * PRIME + ($levelCode == null ? 43 : $levelCode.hashCode());
        final Object $typeCode = this.getTypeCode();
        result = result * PRIME + ($typeCode == null ? 43 : $typeCode.hashCode());
        final Object $results = this.getResults();
        result = result * PRIME + ($results == null ? 43 : $results.hashCode());
        final Object $id = this.getID();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $referencedItems = this.getReferencedItems();
        result = result * PRIME + ($referencedItems == null ? 43 : $referencedItems.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesValidationQualityAnalysisFact;
    }
}
