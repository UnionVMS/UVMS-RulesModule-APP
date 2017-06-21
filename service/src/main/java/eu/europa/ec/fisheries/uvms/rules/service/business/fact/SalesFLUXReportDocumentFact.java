package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFLUXReportDocumentFact extends AbstractFact {

    private List<IdType> ids;
    private IdType referencedID;
    private DateTimeType creationDateTime;
    private CodeType purposeCode;
    private TextType purpose;
    private CodeType typeCode;
    private SalesFLUXPartyFact ownerFLUXParty;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_REPORT_DOCUMENT;
    }

    public List<IdType> getIDS() {
        return this.ids;
    }

    public IdType getReferencedID() {
        return this.referencedID;
    }

    public DateTimeType getCreationDateTime() {
        return this.creationDateTime;
    }

    public CodeType getPurposeCode() {
        return this.purposeCode;
    }

    public TextType getPurpose() {
        return this.purpose;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }


    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setReferencedID(IdType referencedID) {
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

    public List<IdType> getIds() {
        return ids;
    }

    public void setIds(List<IdType> ids) {
        this.ids = ids;
    }

    public SalesFLUXPartyFact getOwnerFLUXParty() {
        return ownerFLUXParty;
    }

    public void setOwnerFLUXParty(SalesFLUXPartyFact ownerFLUXParty) {
        this.ownerFLUXParty = ownerFLUXParty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalesFLUXReportDocumentFact that = (SalesFLUXReportDocumentFact) o;

        if (ids != null ? !ids.equals(that.ids) : that.ids != null) return false;
        if (referencedID != null ? !referencedID.equals(that.referencedID) : that.referencedID != null) return false;
        if (creationDateTime != null ? !creationDateTime.equals(that.creationDateTime) : that.creationDateTime != null)
            return false;
        if (purposeCode != null ? !purposeCode.equals(that.purposeCode) : that.purposeCode != null) return false;
        if (purpose != null ? !purpose.equals(that.purpose) : that.purpose != null) return false;
        if (typeCode != null ? !typeCode.equals(that.typeCode) : that.typeCode != null) return false;
        return ownerFLUXParty != null ? ownerFLUXParty.equals(that.ownerFLUXParty) : that.ownerFLUXParty == null;
    }

    @Override
    public int hashCode() {
        int result = ids != null ? ids.hashCode() : 0;
        result = 31 * result + (referencedID != null ? referencedID.hashCode() : 0);
        result = 31 * result + (creationDateTime != null ? creationDateTime.hashCode() : 0);
        result = 31 * result + (purposeCode != null ? purposeCode.hashCode() : 0);
        result = 31 * result + (purpose != null ? purpose.hashCode() : 0);
        result = 31 * result + (typeCode != null ? typeCode.hashCode() : 0);
        result = 31 * result + (ownerFLUXParty != null ? ownerFLUXParty.hashCode() : 0);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFLUXReportDocumentFact;
    }
}
