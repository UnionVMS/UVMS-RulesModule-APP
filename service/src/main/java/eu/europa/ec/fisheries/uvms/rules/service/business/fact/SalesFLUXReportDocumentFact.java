package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFLUXReportDocumentFact extends AbstractFact {

    private List<IDType> ids;
    private IDType referencedID;
    private DateTimeType creationDateTime;
    private CodeType purposeCode;
    private TextType purpose;
    private CodeType typeCode;
    private FLUXPartyType ownerFLUXParty;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_REPORT_DOCUMENT;
    }

    public List<IDType> getIDS() {
        return this.ids;
    }

    public IDType getReferencedID() {
        return this.referencedID;
    }

    public DateTimeType getCreationDateTime() {
        return this.creationDateTime;
    }

    public eu.europa.ec.fisheries.schema.sales.CodeType getPurposeCode() {
        return this.purposeCode;
    }

    public TextType getPurpose() {
        return this.purpose;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public FLUXPartyType getOwnerFLUXParty() {
        return this.ownerFLUXParty;
    }

    public void setIDS(List<IDType> ids) {
        this.ids = ids;
    }

    public void setReferencedID(IDType referencedID) {
        this.referencedID = referencedID;
    }

    public void setCreationDateTime(DateTimeType creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public void setPurposeCode(CodeType purposeCode) {
        this.purposeCode = purposeCode;
    }

    public void setPurpose(TextType purpose) {
        this.purpose = purpose;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setOwnerFLUXParty(FLUXPartyType ownerFLUXParty) {
        this.ownerFLUXParty = ownerFLUXParty;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFLUXReportDocumentFact)) return false;
        final SalesFLUXReportDocumentFact other = (SalesFLUXReportDocumentFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIDS();
        final Object other$ids = other.getIDS();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$referencedID = this.getReferencedID();
        final Object other$referencedID = other.getReferencedID();
        if (this$referencedID == null ? other$referencedID != null : !this$referencedID.equals(other$referencedID))
            return false;
        final Object this$creationDateTime = this.getCreationDateTime();
        final Object other$creationDateTime = other.getCreationDateTime();
        if (this$creationDateTime == null ? other$creationDateTime != null : !this$creationDateTime.equals(other$creationDateTime))
            return false;
        final Object this$purposeCode = this.getPurposeCode();
        final Object other$purposeCode = other.getPurposeCode();
        if (this$purposeCode == null ? other$purposeCode != null : !this$purposeCode.equals(other$purposeCode))
            return false;
        final Object this$purpose = this.getPurpose();
        final Object other$purpose = other.getPurpose();
        if (this$purpose == null ? other$purpose != null : !this$purpose.equals(other$purpose)) return false;
        final Object this$typeCode = this.getTypeCode();
        final Object other$typeCode = other.getTypeCode();
        if (this$typeCode == null ? other$typeCode != null : !this$typeCode.equals(other$typeCode)) return false;
        final Object this$ownerFLUXParty = this.getOwnerFLUXParty();
        final Object other$ownerFLUXParty = other.getOwnerFLUXParty();
        if (this$ownerFLUXParty == null ? other$ownerFLUXParty != null : !this$ownerFLUXParty.equals(other$ownerFLUXParty))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ids = this.getIDS();
        result = result * PRIME + ($ids == null ? 43 : $ids.hashCode());
        final Object $referencedID = this.getReferencedID();
        result = result * PRIME + ($referencedID == null ? 43 : $referencedID.hashCode());
        final Object $creationDateTime = this.getCreationDateTime();
        result = result * PRIME + ($creationDateTime == null ? 43 : $creationDateTime.hashCode());
        final Object $purposeCode = this.getPurposeCode();
        result = result * PRIME + ($purposeCode == null ? 43 : $purposeCode.hashCode());
        final Object $purpose = this.getPurpose();
        result = result * PRIME + ($purpose == null ? 43 : $purpose.hashCode());
        final Object $typeCode = this.getTypeCode();
        result = result * PRIME + ($typeCode == null ? 43 : $typeCode.hashCode());
        final Object $ownerFLUXParty = this.getOwnerFLUXParty();
        result = result * PRIME + ($ownerFLUXParty == null ? 43 : $ownerFLUXParty.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFLUXReportDocumentFact;
    }
}
