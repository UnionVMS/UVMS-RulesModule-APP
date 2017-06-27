package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.SalesDocumentType;
import eu.europa.ec.fisheries.schema.sales.ValidationResultDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesReportFact extends AbstractFact {

    private IdType id;
    private CodeType itemTypeCode;
    private List<SalesDocumentType> includedSalesDocuments;
    private List<ValidationResultDocumentType> includedValidationResultDocuments;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_REPORT;
    }

    public IdType getID() {
        return this.id;
    }

    public CodeType getItemTypeCode() {
        return this.itemTypeCode;
    }

    public List<SalesDocumentType> getIncludedSalesDocuments() {
        return this.includedSalesDocuments;
    }

    public List<ValidationResultDocumentType> getIncludedValidationResultDocuments() {
        return this.includedValidationResultDocuments;
    }

    public void setID(IdType id) {
        this.id = id;
    }

    public void setItemTypeCode(CodeType itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
    }

    public void setIncludedSalesDocuments(List<SalesDocumentType> includedSalesDocuments) {
        this.includedSalesDocuments = includedSalesDocuments;
    }

    public void setIncludedValidationResultDocuments(List<ValidationResultDocumentType> includedValidationResultDocuments) {
        this.includedValidationResultDocuments = includedValidationResultDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesReportFact)) return false;
        SalesReportFact that = (SalesReportFact) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(itemTypeCode, that.itemTypeCode) &&
                Objects.equals(includedSalesDocuments, that.includedSalesDocuments) &&
                Objects.equals(includedValidationResultDocuments, that.includedValidationResultDocuments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemTypeCode, includedSalesDocuments, includedValidationResultDocuments);
    }
}
