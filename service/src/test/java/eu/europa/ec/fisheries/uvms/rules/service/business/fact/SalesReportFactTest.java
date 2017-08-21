package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import com.google.common.collect.Lists;
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
    public void isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchaseWhenOnlySellerIsSpecified() throws Exception {
        SalesPartyFact seller = new SalesPartyFact();
        seller.setRoleCodes(Lists.newArrayList(new CodeType("SELLER")));

        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesParties(Lists.newArrayList(seller));
        salesDocumentFact.setSpecifiedSalesBatches(Lists.newArrayList(new SalesBatchType().withSpecifiedAAPProducts(new AAPProductType().withTotalSalesPrice(new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ONE))))));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchase());
    }

    @Test
    public void isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchaseWhenOnlyBuyerIsSpecified() throws Exception {
        SalesPartyFact buyer = new SalesPartyFact();
        buyer.setRoleCodes(Lists.newArrayList(new CodeType("BUYER")));

        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesParties(Lists.newArrayList(buyer));
        salesDocumentFact.setSpecifiedSalesBatches(Lists.newArrayList(new SalesBatchType().withSpecifiedAAPProducts(new AAPProductType().withTotalSalesPrice(new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ONE))))));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchase());
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
    public void isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithTakeOverDocument() throws Exception {
        fact.setItemTypeCode(new CodeType("TOD"));
        assertFalse(fact.isSellerRoleOrBuyerNotSpecifiedForSalesNoteWithPurchase());
    }

    @Test
    public void isRecipientRoleNotSpecifiedForTakeOverDocumentWhenRecipientIsPresent() throws Exception {
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        CodeType codeType = new CodeType();
        codeType.setValue("RECIPIENT");

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
        codeType.setValue("RECIPIENTNOTPRESENT");

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
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType")
                .verify();
    }

    @Test
    public void isSalesNoteAndIsFreshnessNotPresentWhenTakeOverDocumentAndFreshnessIsPresent() {
        eu.europa.ec.fisheries.schema.sales.CodeType presentation = new eu.europa.ec.fisheries.schema.sales.CodeType().withListID("FISH_PRESENTATION");
        eu.europa.ec.fisheries.schema.sales.CodeType freshness = new eu.europa.ec.fisheries.schema.sales.CodeType().withListID("FISH_FRESHNESS");

        AAPProcessType aapProcessType1 = new AAPProcessType().withTypeCodes(presentation, freshness);
        AAPProcessType aapProcessType2 = new AAPProcessType().withTypeCodes(presentation, freshness);

        AAPProductType aapProductType1 = new AAPProductType().withAppliedAAPProcesses(aapProcessType1, aapProcessType2);
        AAPProductType aapProductType2 = new AAPProductType().withAppliedAAPProcesses(aapProcessType1, aapProcessType2);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndIsFreshnessNotPresent());
    }

    @Test
    public void isSalesNoteAndIsFreshnessNotPresentWhenTakeOverDocumentAndFreshnessIsNotPresent() {
        eu.europa.ec.fisheries.schema.sales.CodeType presentation = new eu.europa.ec.fisheries.schema.sales.CodeType().withListID("FISH_PRESENTATION");
        eu.europa.ec.fisheries.schema.sales.CodeType freshness = new eu.europa.ec.fisheries.schema.sales.CodeType().withListID("FISH_FRESHNESS");

        AAPProcessType aapProcessTypeWithFreshness = new AAPProcessType().withTypeCodes(presentation, freshness);
        AAPProcessType aapProcessWithoutFreshness = new AAPProcessType().withTypeCodes(presentation);

        AAPProductType aapProductType1 = new AAPProductType().withAppliedAAPProcesses(aapProcessTypeWithFreshness, aapProcessWithoutFreshness);
        AAPProductType aapProductType2 = new AAPProductType().withAppliedAAPProcesses(aapProcessTypeWithFreshness, aapProcessWithoutFreshness);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndIsFreshnessNotPresent());
    }

    @Test
    public void isSalesNoteAndIsFreshnessNotPresentWhenSalesNoteAndFreshnessIsPresentEverywhere() {
        eu.europa.ec.fisheries.schema.sales.CodeType presentation = new eu.europa.ec.fisheries.schema.sales.CodeType().withListID("FISH_PRESENTATION");
        eu.europa.ec.fisheries.schema.sales.CodeType freshness = new eu.europa.ec.fisheries.schema.sales.CodeType().withListID("FISH_FRESHNESS");

        AAPProcessType aapProcessType1 = new AAPProcessType().withTypeCodes(presentation, freshness);
        AAPProcessType aapProcessType2 = new AAPProcessType().withTypeCodes(presentation, freshness);

        AAPProductType aapProductType1 = new AAPProductType().withAppliedAAPProcesses(aapProcessType1, aapProcessType2);
        AAPProductType aapProductType2 = new AAPProductType().withAppliedAAPProcesses(aapProcessType1, aapProcessType2);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isSalesNoteAndIsFreshnessNotPresent());
    }

    @Test
    public void isSalesNoteAndIsFreshnessNotPresentWhenSalesNoteAndFreshnessIsNotPresentEverywhere() {
        eu.europa.ec.fisheries.schema.sales.CodeType presentation = new eu.europa.ec.fisheries.schema.sales.CodeType().withListID("FISH_PRESENTATION");
        eu.europa.ec.fisheries.schema.sales.CodeType freshness = new eu.europa.ec.fisheries.schema.sales.CodeType().withListID("FISH_FRESHNESS");

        AAPProcessType aapProcessTypeWithFreshness = new AAPProcessType().withTypeCodes(presentation, freshness);
        AAPProcessType aapProcessWithoutFreshness = new AAPProcessType().withTypeCodes(presentation);

        AAPProductType aapProductType1 = new AAPProductType().withAppliedAAPProcesses(aapProcessTypeWithFreshness, aapProcessWithoutFreshness);
        AAPProductType aapProductType2 = new AAPProductType().withAppliedAAPProcesses(aapProcessTypeWithFreshness, aapProcessWithoutFreshness);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSalesNoteAndIsFreshnessNotPresent());
    }

    @Test
    public void isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveANameWhenSalesNoteAndAllFLUXOrganizationsHaveAName() {
        FLUXOrganizationType fluxOrganizationWithName = new FLUXOrganizationType().withName(new TextType().withValue("name"));

        VehicleTransportMeansType vehicleTransportMeans = new VehicleTransportMeansType()
                .withOwnerSalesParty(new SalesPartyType()
                    .withSpecifiedFLUXOrganization(fluxOrganizationWithName));

        SalesPartyFact salesPartyFact1 = new SalesPartyFact();
        salesPartyFact1.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesPartyFact salesPartyFact2 = new SalesPartyFact();
        salesPartyFact2.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedVehicleTransportMeans(vehicleTransportMeans);
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact1, salesPartyFact2));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveAName());
    }

    @Test
    public void isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveANameWhenSalesNoteAndNotAllFLUXOrganizationsHaveANameBecauseOfASalesPartyInTheDocument() {
        FLUXOrganizationType fluxOrganizationWithName = new FLUXOrganizationType().withName(new TextType().withValue("name"));
        FLUXOrganizationType fluxOrganizationWithoutName = new FLUXOrganizationType();

        VehicleTransportMeansType vehicleTransportMeans = new VehicleTransportMeansType()
                .withOwnerSalesParty(new SalesPartyType()
                        .withSpecifiedFLUXOrganization(fluxOrganizationWithName));

        SalesPartyFact salesPartyFact1 = new SalesPartyFact();
        salesPartyFact1.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesPartyFact salesPartyFact2 = new SalesPartyFact();
        salesPartyFact2.setSpecifiedFLUXOrganization(fluxOrganizationWithoutName);

        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedVehicleTransportMeans(vehicleTransportMeans);
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact1, salesPartyFact2));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveAName());
    }

    @Test
    public void isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveANameWhenSalesNoteAndNotAllFLUXOrganizationsHaveANameBecauseOfASalesPartyInTheVesselTransportMeans() {
        FLUXOrganizationType fluxOrganizationWithName = new FLUXOrganizationType().withName(new TextType().withValue("name"));
        FLUXOrganizationType fluxOrganizationWithoutName = new FLUXOrganizationType();

        VehicleTransportMeansType vehicleTransportMeans = new VehicleTransportMeansType()
                .withOwnerSalesParty(new SalesPartyType()
                        .withSpecifiedFLUXOrganization(fluxOrganizationWithoutName));

        SalesPartyFact salesPartyFact1 = new SalesPartyFact();
        salesPartyFact1.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesPartyFact salesPartyFact2 = new SalesPartyFact();
        salesPartyFact2.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedVehicleTransportMeans(vehicleTransportMeans);
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact1, salesPartyFact2));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveAName());
    }

    @Test
    public void isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveANameWhenTakeOverDocumentAndAllFLUXOrganizationsHaveAName() {
        FLUXOrganizationType fluxOrganizationWithName = new FLUXOrganizationType().withName(new TextType().withValue("name"));

        VehicleTransportMeansType vehicleTransportMeans = new VehicleTransportMeansType()
                .withOwnerSalesParty(new SalesPartyType()
                        .withSpecifiedFLUXOrganization(fluxOrganizationWithName));

        SalesPartyFact salesPartyFact1 = new SalesPartyFact();
        salesPartyFact1.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesPartyFact salesPartyFact2 = new SalesPartyFact();
        salesPartyFact2.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedVehicleTransportMeans(vehicleTransportMeans);
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact1, salesPartyFact2));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveAName());
    }

    @Test
    public void isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveANameWhenTakeOverDocumentAndNotAllFLUXOrganizationsHaveANameBecauseOfASalesPartyInTheDocument() {
        FLUXOrganizationType fluxOrganizationWithName = new FLUXOrganizationType().withName(new TextType().withValue("name"));
        FLUXOrganizationType fluxOrganizationWithoutName = new FLUXOrganizationType();

        VehicleTransportMeansType vehicleTransportMeans = new VehicleTransportMeansType()
                .withOwnerSalesParty(new SalesPartyType()
                        .withSpecifiedFLUXOrganization(fluxOrganizationWithName));

        SalesPartyFact salesPartyFact1 = new SalesPartyFact();
        salesPartyFact1.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesPartyFact salesPartyFact2 = new SalesPartyFact();
        salesPartyFact2.setSpecifiedFLUXOrganization(fluxOrganizationWithoutName);

        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedVehicleTransportMeans(vehicleTransportMeans);
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact1, salesPartyFact2));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertTrue(fact.isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveAName());
    }

    @Test
    public void isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveANameWhenTakeOverDocumentAndNotAllFLUXOrganizationsHaveANameBecauseOfASalesPartyInTheVesselTransportMeans() {
        FLUXOrganizationType fluxOrganizationWithName = new FLUXOrganizationType().withName(new TextType().withValue("name"));
        FLUXOrganizationType fluxOrganizationWithoutName = new FLUXOrganizationType();

        VehicleTransportMeansType vehicleTransportMeans = new VehicleTransportMeansType()
                .withOwnerSalesParty(new SalesPartyType()
                        .withSpecifiedFLUXOrganization(fluxOrganizationWithoutName));

        SalesPartyFact salesPartyFact1 = new SalesPartyFact();
        salesPartyFact1.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesPartyFact salesPartyFact2 = new SalesPartyFact();
        salesPartyFact2.setSpecifiedFLUXOrganization(fluxOrganizationWithName);

        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedVehicleTransportMeans(vehicleTransportMeans);
        salesDocumentFact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact1, salesPartyFact2));

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertTrue(fact.isTakeOverDocumentAndDoesAFLUXOrganizationNotHaveAName());
    }

    @Test
    public void isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZeroWhenSalesNoteAndAllChargeAmountsAreGreater() {
        SalesPriceType positiveAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(positiveAmount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(zeroAmount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(positiveAmount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZero());
    }

    @Test
    public void isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZeroWhenSalesNoteAndTheTotalPriceIsNegative() {
        SalesPriceType positiveAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));
        SalesPriceType negativeAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.valueOf(-15)));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(positiveAmount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(zeroAmount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(negativeAmount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZero());
    }


    @Test
    public void isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZeroWhenSalesNoteAndThePriceOfOneOfTheProductsIsNegative() {
        SalesPriceType positiveAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));
        SalesPriceType negativeAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.valueOf(-15)));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(negativeAmount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(zeroAmount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(positiveAmount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZero());
    }

    @Test
    public void isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZeroWhenTakeOverDocumentAndAllChargeAmountsAreGreater() {
        SalesPriceType positiveAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(positiveAmount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(zeroAmount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(positiveAmount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZero());
    }

    @Test
    public void isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZeroWhenTakeOverDocumentAndTheTotalPriceIsNegative() {
        SalesPriceType positiveAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));
        SalesPriceType negativeAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.valueOf(-15)));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(positiveAmount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(zeroAmount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(negativeAmount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZero());
    }


    @Test
    public void isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZeroWhenTakeOverDocumentAndThePriceOfOneOfTheProductsIsNegative() {
        SalesPriceType positiveAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));
        SalesPriceType negativeAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.valueOf(-15)));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(positiveAmount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(negativeAmount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(zeroAmount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndNotAllChargeAmountsAreGreaterThanOrEqualToZero());
    }

    @Test
    public void isSalesNoteAndAtLeastOneChargeAmountIsNullWhenSalesNoteAndNoChargeAmountsAreNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isSalesNoteAndAtLeastOneChargeAmountIsNull());
    }

    @Test
    public void isSalesNoteAndAtLeastOneChargeAmountIsNullWhenSalesNoteAndTheTotalChargeAmountsIsNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType amountWithEmptyCharge = new SalesPriceType();

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amountWithEmptyCharge);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSalesNoteAndAtLeastOneChargeAmountIsNull());
    }

    @Test
    public void isSalesNoteAndAtLeastOneChargeAmountIsNullWhenSalesNoteAndTheChargeAmountsOfOneOfTheProductsIsNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType amountWithEmptyCharge = new SalesPriceType();

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amountWithEmptyCharge);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSalesNoteAndAtLeastOneChargeAmountIsNull());
    }

    @Test
    public void isSalesNoteAndAtLeastOneChargeAmountIsNullWhenTakeOverDocumentAndNoChargeAmountsAreNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndAtLeastOneChargeAmountIsNull());
    }

    @Test
    public void isSalesNoteAndAtLeastOneChargeAmountIsNullWhenTakeOverDocumentAndTheTotalChargeAmountsIsNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType amountWithEmptyCharge = new SalesPriceType();

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amountWithEmptyCharge);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndAtLeastOneChargeAmountIsNull());
    }

    @Test
    public void isSalesNoteAndAtLeastOneChargeAmountIsNullWhenTakeOverDocumentAndTheChargeAmountsOfOneOfTheProductsIsNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType amountWithEmptyCharge = new SalesPriceType();

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amountWithEmptyCharge);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndAtLeastOneChargeAmountIsNull());
    }


    @Test
    public void isSalesNoteAndAnyChargeAmountIsEqualToZeroWhenSalesNoteAndNoChargeAmountsAreZero() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertFalse(fact.isSalesNoteAndAnyChargeAmountIsEqualToZero());
    }

    @Test
    public void isSalesNoteAndAnyChargeAmountIsEqualToZeroWhenSalesNoteAndTheTotalChargeAmountsIsZero() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(zeroAmount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSalesNoteAndAnyChargeAmountIsEqualToZero());
    }

    @Test
    public void isSalesNoteAndAnyChargeAmountIsEqualToZeroWhenSalesNoteAndTheChargeAmountsOfOneOfTheProductsIsNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(zeroAmount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("SN"));

        assertTrue(fact.isSalesNoteAndAnyChargeAmountIsEqualToZero());
    }

    @Test
    public void isSalesNoteAndAnyChargeAmountIsEqualToZeroWhenTakeOverDocumentAndNoChargeAmountsAreNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndAnyChargeAmountIsEqualToZero());
    }

    @Test
    public void isSalesNoteAndAnyChargeAmountIsEqualToZeroWhenTakeOverDocumentAndTheTotalChargeAmountsIsNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(amount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(zeroAmount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndAnyChargeAmountIsEqualToZero());
    }

    @Test
    public void isSalesNoteAndAnyChargeAmountIsEqualToZeroWhenTakeOverDocumentAndTheChargeAmountsOfOneOfTheProductsIsNull() {
        SalesPriceType amount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN));
        SalesPriceType zeroAmount = new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ZERO));

        AAPProductType aapProductType1 = new AAPProductType().withTotalSalesPrice(zeroAmount);
        AAPProductType aapProductType2 = new AAPProductType().withTotalSalesPrice(amount);

        SalesBatchType salesBatchType = new SalesBatchType().withSpecifiedAAPProducts(aapProductType1, aapProductType2);
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setSpecifiedSalesBatches(Arrays.asList(salesBatchType));
        salesDocumentFact.setTotalSalesPrice(amount);

        fact.setIncludedSalesDocuments(Arrays.asList(salesDocumentFact));
        fact.setItemTypeCode(new CodeType("TOD"));

        assertFalse(fact.isSalesNoteAndAnyChargeAmountIsEqualToZero());
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