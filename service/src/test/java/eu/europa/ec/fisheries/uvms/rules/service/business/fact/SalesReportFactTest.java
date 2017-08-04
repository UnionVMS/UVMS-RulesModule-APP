package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesReportFactTest {

    private SalesReportFact fact;

    @Before
    public void setUp() throws Exception {
        fact = new SalesReportFact();
    }

    @Test
    public void isSellerRoleNotSpecifiedForSalesNoteWhenSellerNotSpecified() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("NOSELLERHERE", "BUYER", "SENDER");

        //TODO: volgorde van roles beinvloed het resultaat

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSellerRoleNotSpecifiedForSalesNote());
    }


    @Test
    public void isSellerRoleNotSpecifiedForSalesNoteWhenSellerIsSpecified() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("BUYER", "SENDER", "SELLER");

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isSellerRoleNotSpecifiedForSalesNote());
    }

    @Test
    public void isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchaseWhenSellerIsSpecified() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("SELLER", "BUYER", "SELLER");

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchase());
    }

    @Test
    public void isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchaseWhenBuyerIsSpecified() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("BUYER", "BUYER", "SELLER");
        setSalesBatchWithATotalPrice(salesDocumentFact);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchase());
    }

    @Test
    public void isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchaseWhenBuyerOrSellerIsNotSpecified() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("NOTBUYERNORSELLER", "SENDER", "RECEIVER");
        salesDocumentFact.setTotalSalesPrice(new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN)));
        setSalesBatchWithATotalPrice(salesDocumentFact);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchase());
    }

    @Test
    public void isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchaseWhenTotalIsZero() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("NOTBUYERNORSELLER", "BUYER", "SELLER");

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchase());
    }

    @Test
    public void isRecipientRoleNotSpecifiedForTakeOverDocumentWhenRecipientIsPresent() throws Exception {
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        CodeType codeType = new CodeType();
        codeType.setListId("RECIPIENT");

        SalesPartyFact salesPartyFact = new SalesPartyFact();
        salesPartyFact.setRoleCodes(Arrays.asList(codeType));
        salesPartyFact.setSpecifiedFLUXOrganization(new FLUXOrganizationType());
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isRecipientRoleNotSpecifiedForTakeOverDocument());
    }

    @Test
    public void isRecipientRoleNotSpecifiedForTakeOverDocumentWhenRecipientIsNotPresent() throws Exception {
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        CodeType codeType = new CodeType();
        codeType.setListId("RECIPIENTNOTPRESENT");

        SalesPartyFact salesPartyFact = new SalesPartyFact();
        salesPartyFact.setRoleCodes(Arrays.asList(codeType));
        salesPartyFact.setSpecifiedFLUXOrganization(new FLUXOrganizationType());
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertTrue(fact.isRecipientRoleNotSpecifiedForTakeOverDocument());
    }

    @Test
    public void isFluxOrganizationNotSpecifiedOnAllSalesPartiesForTakeOverDocumentWhenFluxOrganizationIsPresent() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("SENDER", "BUYER", "SELLER");

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isFluxOrganizationNotSpecifiedOnAllSalesPartiesForTakeOverDocument());
    }

    @Test
    public void isFluxOrganizationNotSpecifiedOnAllSalesPartiesForTakeOverDocumentWhenFluxOrganizationIsNotPresent() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("SENDER", "BUYER", "SELLER");
        salesDocumentFact.getSpecifiedSalesParties().get(0).setSpecifiedFLUXOrganization(null);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertTrue(fact.isFluxOrganizationNotSpecifiedOnAllSalesPartiesForTakeOverDocument());
    }

    @Test
    public void isSalesNoteIdentifierNotSpecifiedForTakeOverDocumentWithStoredProductsWhenStoredProductsArePresentAndIDSArePresent() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("SENDER", "BUYER", "SELLER");
        salesDocumentFact.getSpecifiedSalesParties().get(0).setSpecifiedFLUXOrganization(null);
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(new SalesBatchType().withSpecifiedAAPProducts(
                new AAPProductType().withUsageCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("STO")))));
        IdType idType = new IdType();
        idType.setValue("VALUE");
        salesDocumentFact.setSalesNoteIDs(Arrays.asList(idType));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteIdentifierNotSpecifiedForTakeOverDocumentWithStoredProducts());
    }

    @Test
    public void isSalesNoteIdentifierNotSpecifiedForTakeOverDocumentWithStoredProductsWhenStoredProductsArePresentAndIDSAreNotPresent() throws Exception {
        SalesDocumentFact salesDocumentFact = createSalesDocumentFactWithRole("SENDER", "BUYER", "SELLER");
        salesDocumentFact.getSpecifiedSalesParties().get(0).setSpecifiedFLUXOrganization(null);
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(new SalesBatchType().withSpecifiedAAPProducts(
                new AAPProductType().withUsageCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("STO")))));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertTrue(fact.isSalesNoteIdentifierNotSpecifiedForTakeOverDocumentWithStoredProducts());
    }

    @Test
    public void equalsAndHashCode() {
        SalesDocumentFact salesDocumentFact1 = new SalesDocumentFact();
        SalesDocumentFact salesDocumentFact2 = new SalesDocumentFact();
        IdType idType1 = new IdType();
        idType1.setValue("a");
        IdType idType2 = new IdType();
        idType2.setValue("b");

        salesDocumentFact1.setIDS(Arrays.asList(idType1));
        salesDocumentFact2.setIDS(Arrays.asList(idType2));

        EqualsVerifier.forClass(SalesReportFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(SalesReportType.class, new SalesReportType().withID(new IDType().withValue("a")), new SalesReportType().withID(new IDType().withValue("b")))
                .withPrefabValues(SalesDocumentFact.class, salesDocumentFact1, salesDocumentFact2)
                .withPrefabValues(SalesBatchType.class, new SalesBatchType().withIDS(new IDType().withValue("a")), new SalesBatchType().withIDS(new IDType().withValue("b")))
                .withPrefabValues(AAPProductType.class, new AAPProductType().withSpeciesCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new AAPProductType().withSpeciesCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(AAPProcessType.class, new AAPProcessType().withTypeCodes(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new AAPProcessType().withTypeCodes(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FACatchType.class, new FACatchType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FACatchType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver")
                .verify();
    }

    private SalesDocumentFact createSalesDocumentFactWithRole(String role, String buyer, String seller) {
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        CodeType codeType = new CodeType();
        codeType.setValue(role);

        CodeType codeType2 = new CodeType();
        codeType2.setValue(buyer);

        CodeType codeType3 = new CodeType();
        codeType3.setValue(seller);

        SalesPartyFact salesPartyFact = new SalesPartyFact();
        salesPartyFact.setRoleCodes(Arrays.asList(codeType, codeType2, codeType3));
        salesPartyFact.setSpecifiedFLUXOrganization(new FLUXOrganizationType());
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact));
        return salesDocumentFact;
    }

    private void setSalesBatchWithATotalPrice(SalesDocumentFact salesDocumentFact) {
        salesDocumentFact.setSpecifiedSalesBatches(
                Arrays.asList(
                        new SalesBatchType().withSpecifiedAAPProducts(
                                new AAPProductType().withTotalSalesPrice(
                                        new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN))))));
    }

}