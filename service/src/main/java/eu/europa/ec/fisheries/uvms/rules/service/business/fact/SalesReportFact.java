package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.SalesDocumentType;
import eu.europa.ec.fisheries.schema.sales.ValidationResultDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesReportFact extends AbstractFact {

    private IDType id;
    private eu.europa.ec.fisheries.schema.sales.CodeType itemTypeCode;
    private List<SalesDocumentType> includedSalesDocuments;
    private List<ValidationResultDocumentType> includedValidationResultDocuments;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_REPORT;
    }

    public IDType getId() {
        return this.id;
    }

    public eu.europa.ec.fisheries.schema.sales.CodeType getItemTypeCode() {
        return this.itemTypeCode;
    }

    public List<SalesDocumentType> getIncludedSalesDocuments() {
        return this.includedSalesDocuments;
    }

    public List<ValidationResultDocumentType> getIncludedValidationResultDocuments() {
        return this.includedValidationResultDocuments;
    }

    public void setId(IDType id) {
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesReportFact)) return false;
        final SalesReportFact other = (SalesReportFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$itemTypeCode = this.getItemTypeCode();
        final Object other$itemTypeCode = other.getItemTypeCode();
        if (this$itemTypeCode == null ? other$itemTypeCode != null : !this$itemTypeCode.equals(other$itemTypeCode))
            return false;
        final Object this$includedSalesDocuments = this.getIncludedSalesDocuments();
        final Object other$includedSalesDocuments = other.getIncludedSalesDocuments();
        if (this$includedSalesDocuments == null ? other$includedSalesDocuments != null : !this$includedSalesDocuments.equals(other$includedSalesDocuments))
            return false;
        final Object this$includedValidationResultDocuments = this.getIncludedValidationResultDocuments();
        final Object other$includedValidationResultDocuments = other.getIncludedValidationResultDocuments();
        if (this$includedValidationResultDocuments == null ? other$includedValidationResultDocuments != null : !this$includedValidationResultDocuments.equals(other$includedValidationResultDocuments))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $itemTypeCode = this.getItemTypeCode();
        result = result * PRIME + ($itemTypeCode == null ? 43 : $itemTypeCode.hashCode());
        final Object $includedSalesDocuments = this.getIncludedSalesDocuments();
        result = result * PRIME + ($includedSalesDocuments == null ? 43 : $includedSalesDocuments.hashCode());
        final Object $includedValidationResultDocuments = this.getIncludedValidationResultDocuments();
        result = result * PRIME + ($includedValidationResultDocuments == null ? 43 : $includedValidationResultDocuments.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesReportFact;
    }
}
