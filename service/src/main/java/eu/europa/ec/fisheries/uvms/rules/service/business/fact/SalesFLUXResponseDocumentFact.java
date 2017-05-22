package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFLUXResponseDocumentFact extends AbstractFact {

    private List<IDType> ids;
    private IDType referencedID;
    private DateTimeType creationDateTime;
    private eu.europa.ec.fisheries.schema.sales.CodeType responseCode;
    private TextType remarks;
    private TextType rejectionReason;
    private eu.europa.ec.fisheries.schema.sales.CodeType typeCode;
    private List<ValidationResultDocumentType> relatedValidationResultDocuments;
    private FLUXPartyType respondentFLUXParty;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_RESPONSE_DOCUMENT;
    }

    public List<IDType> getIds() {
        return this.ids;
    }

    public IDType getReferencedID() {
        return this.referencedID;
    }

    public DateTimeType getCreationDateTime() {
        return this.creationDateTime;
    }

    public CodeType getResponseCode() {
        return this.responseCode;
    }

    public TextType getRemarks() {
        return this.remarks;
    }

    public TextType getRejectionReason() {
        return this.rejectionReason;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public List<ValidationResultDocumentType> getRelatedValidationResultDocuments() {
        return this.relatedValidationResultDocuments;
    }

    public FLUXPartyType getRespondentFLUXParty() {
        return this.respondentFLUXParty;
    }

    public void setIds(List<IDType> ids) {
        this.ids = ids;
    }

    public void setReferencedID(IDType referencedID) {
        this.referencedID = referencedID;
    }

    public void setCreationDateTime(DateTimeType creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public void setResponseCode(CodeType responseCode) {
        this.responseCode = responseCode;
    }

    public void setRemarks(TextType remarks) {
        this.remarks = remarks;
    }

    public void setRejectionReason(TextType rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setRelatedValidationResultDocuments(List<ValidationResultDocumentType> relatedValidationResultDocuments) {
        this.relatedValidationResultDocuments = relatedValidationResultDocuments;
    }

    public void setRespondentFLUXParty(FLUXPartyType respondentFLUXParty) {
        this.respondentFLUXParty = respondentFLUXParty;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFLUXResponseDocumentFact)) return false;
        final SalesFLUXResponseDocumentFact other = (SalesFLUXResponseDocumentFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIds();
        final Object other$ids = other.getIds();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$referencedID = this.getReferencedID();
        final Object other$referencedID = other.getReferencedID();
        if (this$referencedID == null ? other$referencedID != null : !this$referencedID.equals(other$referencedID))
            return false;
        final Object this$creationDateTime = this.getCreationDateTime();
        final Object other$creationDateTime = other.getCreationDateTime();
        if (this$creationDateTime == null ? other$creationDateTime != null : !this$creationDateTime.equals(other$creationDateTime))
            return false;
        final Object this$responseCode = this.getResponseCode();
        final Object other$responseCode = other.getResponseCode();
        if (this$responseCode == null ? other$responseCode != null : !this$responseCode.equals(other$responseCode))
            return false;
        final Object this$remarks = this.getRemarks();
        final Object other$remarks = other.getRemarks();
        if (this$remarks == null ? other$remarks != null : !this$remarks.equals(other$remarks)) return false;
        final Object this$rejectionReason = this.getRejectionReason();
        final Object other$rejectionReason = other.getRejectionReason();
        if (this$rejectionReason == null ? other$rejectionReason != null : !this$rejectionReason.equals(other$rejectionReason))
            return false;
        final Object this$typeCode = this.getTypeCode();
        final Object other$typeCode = other.getTypeCode();
        if (this$typeCode == null ? other$typeCode != null : !this$typeCode.equals(other$typeCode)) return false;
        final Object this$relatedValidationResultDocuments = this.getRelatedValidationResultDocuments();
        final Object other$relatedValidationResultDocuments = other.getRelatedValidationResultDocuments();
        if (this$relatedValidationResultDocuments == null ? other$relatedValidationResultDocuments != null : !this$relatedValidationResultDocuments.equals(other$relatedValidationResultDocuments))
            return false;
        final Object this$respondentFLUXParty = this.getRespondentFLUXParty();
        final Object other$respondentFLUXParty = other.getRespondentFLUXParty();
        if (this$respondentFLUXParty == null ? other$respondentFLUXParty != null : !this$respondentFLUXParty.equals(other$respondentFLUXParty))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ids = this.getIds();
        result = result * PRIME + ($ids == null ? 43 : $ids.hashCode());
        final Object $referencedID = this.getReferencedID();
        result = result * PRIME + ($referencedID == null ? 43 : $referencedID.hashCode());
        final Object $creationDateTime = this.getCreationDateTime();
        result = result * PRIME + ($creationDateTime == null ? 43 : $creationDateTime.hashCode());
        final Object $responseCode = this.getResponseCode();
        result = result * PRIME + ($responseCode == null ? 43 : $responseCode.hashCode());
        final Object $remarks = this.getRemarks();
        result = result * PRIME + ($remarks == null ? 43 : $remarks.hashCode());
        final Object $rejectionReason = this.getRejectionReason();
        result = result * PRIME + ($rejectionReason == null ? 43 : $rejectionReason.hashCode());
        final Object $typeCode = this.getTypeCode();
        result = result * PRIME + ($typeCode == null ? 43 : $typeCode.hashCode());
        final Object $relatedValidationResultDocuments = this.getRelatedValidationResultDocuments();
        result = result * PRIME + ($relatedValidationResultDocuments == null ? 43 : $relatedValidationResultDocuments.hashCode());
        final Object $respondentFLUXParty = this.getRespondentFLUXParty();
        result = result * PRIME + ($respondentFLUXParty == null ? 43 : $respondentFLUXParty.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFLUXResponseDocumentFact;
    }
}
