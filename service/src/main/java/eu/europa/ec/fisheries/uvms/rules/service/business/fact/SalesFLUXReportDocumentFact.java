package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;
import java.util.Objects;

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


    public SalesFLUXPartyFact getOwnerFLUXParty() {
        return ownerFLUXParty;
    }

    public void setOwnerFLUXParty(SalesFLUXPartyFact ownerFLUXParty) {
        this.ownerFLUXParty = ownerFLUXParty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXReportDocumentFact)) return false;
        SalesFLUXReportDocumentFact that = (SalesFLUXReportDocumentFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(referencedID, that.referencedID) &&
                Objects.equals(creationDateTime, that.creationDateTime) &&
                Objects.equals(purposeCode, that.purposeCode) &&
                Objects.equals(purpose, that.purpose) &&
                Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(ownerFLUXParty, that.ownerFLUXParty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, referencedID, creationDateTime, purposeCode, purpose, typeCode, ownerFLUXParty);
    }
}
