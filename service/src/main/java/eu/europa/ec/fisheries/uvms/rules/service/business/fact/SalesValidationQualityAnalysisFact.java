package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@ToString
public class SalesValidationQualityAnalysisFact extends SalesAbstractFact {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesValidationQualityAnalysisFact)) return false;
        SalesValidationQualityAnalysisFact that = (SalesValidationQualityAnalysisFact) o;
        return Objects.equals(levelCode, that.levelCode) &&
                Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(results, that.results) &&
                Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(referencedItems, that.referencedItems) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage) &&
                Objects.equals(messageDataFlow, that.messageDataFlow) &&
                Objects.equals(creationJavaDateOfMessage, that.creationJavaDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(levelCode, typeCode, results, id, description, referencedItems, creationDateOfMessage, messageDataFlow, creationJavaDateOfMessage);
    }
}
