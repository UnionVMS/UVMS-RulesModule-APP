package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesFLUXResponseDocumentFact extends AbstractFact {

    private List<IdType> ids;
    private IdType referencedID;
    private DateTimeType creationDateTime;
    private CodeType responseCode;
    private TextType remarks;
    private TextType rejectionReason;
    private CodeType typeCode;
    private List<ValidationResultDocumentType> relatedValidationResultDocuments;
    private FLUXPartyType respondentFLUXParty;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_RESPONSE_DOCUMENT;
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

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setReferencedID(IdType referencedID) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXResponseDocumentFact)) return false;
        SalesFLUXResponseDocumentFact that = (SalesFLUXResponseDocumentFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(referencedID, that.referencedID) &&
                Objects.equals(creationDateTime, that.creationDateTime) &&
                Objects.equals(responseCode, that.responseCode) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(rejectionReason, that.rejectionReason) &&
                Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(relatedValidationResultDocuments, that.relatedValidationResultDocuments) &&
                Objects.equals(respondentFLUXParty, that.respondentFLUXParty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, referencedID, creationDateTime, responseCode, remarks, rejectionReason, typeCode, relatedValidationResultDocuments, respondentFLUXParty);
    }

    // TODO test
    public boolean anyValidationResultDocumentsWithEmptyValidationQualityAnalyses(){
        for (ValidationResultDocumentType validationResultDocument:relatedValidationResultDocuments) {
            if (validationResultDocument == null || isEmpty(validationResultDocument.getRelatedValidationQualityAnalysises())){
                return true;
            }
        }

        return false;
    }

    // todo test
    public boolean anyValidationQualityAnalysisWithoutReferencedTextItems(){
        if (isEmpty(relatedValidationResultDocuments)){
            return false;
        }

        for (ValidationResultDocumentType validationResultDocument:relatedValidationResultDocuments) {
            if (validationResultDocument != null && !isEmpty(validationResultDocument.getRelatedValidationQualityAnalysises())){
                for (ValidationQualityAnalysisType validationQualityAnalysis:validationResultDocument.getRelatedValidationQualityAnalysises()) {
                    if(validationQualityAnalysis != null && isEmpty(validationQualityAnalysis.getReferencedItems())){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
