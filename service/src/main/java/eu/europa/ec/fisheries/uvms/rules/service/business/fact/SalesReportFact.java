package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class SalesReportFact extends AbstractFact {

    private IdType id;
    private CodeType itemTypeCode;
    private List<SalesDocumentFact> includedSalesDocuments;
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

    public List<SalesDocumentFact> getIncludedSalesDocuments() {
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

    public void setIncludedSalesDocuments(List<SalesDocumentFact> includedSalesDocuments) {
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

    public boolean isSellerRoleNotSpecifiedForSalesNote(){
        if(itemTypeCode != null && Objects.equals(itemTypeCode.getValue(), "SN") && !isEmpty(includedSalesDocuments))
        {
            for (SalesDocumentFact salesDocument:includedSalesDocuments) {
                if (salesDocument == null || salesDocument.getSpecifiedSalesParties() == null){
                    return true;
                }

                boolean sellerAvailable = false;
                for (SalesPartyFact salesParty:salesDocument.getSpecifiedSalesParties()) {
                    if (salesParty != null && !listIdDoesNotContainAll(salesParty.getRoleCodes(), "SELLER")) {
                        sellerAvailable = true;
                    }
                }

                if (!sellerAvailable) {
                    return true;
                }
            }
        }

        return false;
    }

        public boolean isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchase(){
        if(itemTypeCode != null && Objects.equals(itemTypeCode.getValue(), "SN") && !isEmpty(includedSalesDocuments))
        {
            for (SalesDocumentFact salesDocument:includedSalesDocuments) {
                // If the document does not have a price greater than zero it can not pass the test since it is not considered a purchase
                if (salesDocument == null  || isTotalZero(salesDocument))
                {
                    return false;
                }

                if ( salesDocument.getSpecifiedSalesParties() == null){
                    return true;
                }

                boolean sellerAvailable = false;
                boolean buyerAvailable = false;
                for (SalesPartyFact salesParty:salesDocument.getSpecifiedSalesParties()) {
                    if (salesParty != null && !listIdDoesNotContainAll(salesParty.getRoleCodes(), "SELLER")) {
                        sellerAvailable = true;
                    }
                    if (salesParty != null && !listIdDoesNotContainAll(salesParty.getRoleCodes(), "BUYER")) {
                        buyerAvailable = true;
                    }
                }

                if (sellerAvailable || buyerAvailable) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isRecipientRoleNotSpecifiedForTakeOverDocument(){
        if(itemTypeCode != null && Objects.equals(itemTypeCode.getValue(), "TOD") && !isEmpty(includedSalesDocuments))
        {
            for (SalesDocumentFact salesDocument:includedSalesDocuments) {
                if (salesDocument == null || salesDocument.getSpecifiedSalesParties() == null){
                    return true;
                }

                boolean recipientAvailable = false;
                for (SalesPartyFact salesParty:salesDocument.getSpecifiedSalesParties()) {
                    if (salesParty != null && !listIdDoesNotContainAll(salesParty.getRoleCodes(), "RECIPIENT")) {
                        recipientAvailable = true;
                    }
                }

                if (!recipientAvailable) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isFluxOrganizationNotSpecifiedOnAllSalesPartiesForTakeOverDocument(){
        if(itemTypeCode != null && Objects.equals(itemTypeCode.getValue(), "TOD") && !isEmpty(includedSalesDocuments))
        {
            for (SalesDocumentFact salesDocument:includedSalesDocuments) {
                if (salesDocument == null || salesDocument.getSpecifiedSalesParties() == null){
                    continue;
                }

                for (SalesPartyFact salesParty:salesDocument.getSpecifiedSalesParties()) {
                    if (salesParty != null && salesParty.getSpecifiedFLUXOrganization() == null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public boolean isSalesNoteIdentifierNotSpecifiedForTakeOverDocumentWithStoredProducts(){
        if(itemTypeCode != null && Objects.equals(itemTypeCode.getValue(), "TOD") && !isEmpty(includedSalesDocuments))
        {
            for (SalesDocumentFact salesDocument:includedSalesDocuments) {
                if (isAnyProductSetWithStorageAsUsage(salesDocument)){
                    if (isEmpty(salesDocument.getSalesNoteIDs()) || isEmpty(salesDocument.getSalesNoteIDs().get(0).getValue())){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isAnyProductSetWithStorageAsUsage(SalesDocumentFact salesDocument){
        if (salesDocument == null || isEmpty(salesDocument.getSpecifiedSalesBatches())){
            return false;
        }

        for (SalesBatchType salesBatch : salesDocument.getSpecifiedSalesBatches()) {
            if (salesBatch != null && !isEmpty(salesBatch.getSpecifiedAAPProducts())) {
                for (AAPProductType aapProduct : salesBatch.getSpecifiedAAPProducts()) {
                    if (aapProduct != null && aapProduct.getUsageCode() != null && aapProduct.getUsageCode().getValue() == "STO") {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isTotalZero(SalesDocumentFact salesDocument){

        if(salesDocument.getSpecifiedSalesBatches() == null || salesDocument.getSpecifiedSalesBatches().size() == 0)        {
            return true;
        }

        for (SalesBatchType salesBatch :salesDocument.getSpecifiedSalesBatches()) {
            if(!isEmpty(salesBatch.getSpecifiedAAPProducts())){
                for (AAPProductType product: salesBatch.getSpecifiedAAPProducts()) {
                    if (product != null && product.getTotalSalesPrice() != null
                            && !isEmpty(product.getTotalSalesPrice().getChargeAmounts())){
                        for (AmountType amount :product.getTotalSalesPrice().getChargeAmounts()) {
                            if (amount != null && BigDecimal.ZERO.compareTo(amount.getValue()) == -1){
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
