package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MATBUL on 22/06/2017.
 */
public class SalesDocumentFactTest {

    private SalesDocumentFact fact;

    @Before
    public void setUp() throws Exception {
        fact = new SalesDocumentFact();
    }

    @Test
    public void doesDocumentContainDuplicateSalesPartyRolesWhenThereAreDuplicates() throws Exception {
        SalesPartyFact salesPartyFact = new SalesPartyFact();
        salesPartyFact.setRoleCodes(Arrays.asList(new CodeType("bla"), new CodeType("dsq")));
        fact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact, salesPartyFact));

        assertTrue(fact.doesDocumentContainDuplicateSalesPartyRoles());
    }

    @Test
    public void doesDocumentContainDuplicateSalesPartyRolesWhenThereAreNoDuplicates() throws Exception {
        SalesPartyFact salesPartyFact = new SalesPartyFact();
        salesPartyFact.setRoleCodes(Arrays.asList(new CodeType("bla"), new CodeType("dsq")));
        SalesPartyFact salesPartyFact2 = new SalesPartyFact();
        salesPartyFact.setRoleCodes(Arrays.asList(new CodeType("alb"), new CodeType("qsd")));

        fact.setSpecifiedSalesParties(Arrays.asList(salesPartyFact, salesPartyFact2));

        assertFalse(fact.doesDocumentContainDuplicateSalesPartyRoles());
    }

    @Test
    public void isAnySalesDateBeforeLandingDateWhenLandingDatesAreValid() throws Exception {
        fact.setSpecifiedFishingActivities(Arrays.asList(
                new FishingActivityType().withSpecifiedDelimitedPeriods(
                        new DelimitedPeriodType().withStartDateTime(
                                new DateTimeType().withDateTime(DateTime.parse("1995-11-24"))))));
        fact.setSpecifiedSalesEvents(Collections.singletonList(new SalesEventType().withOccurrenceDateTime(new DateTimeType().withDateTime(DateTime.now()))));

        assertFalse(fact.isAnySalesDateBeforeLandingDate());
    }

    @Test
    public void isAnySalesDateBeforeLandingDateWhenLandingDatesAreInvalid() throws Exception {
        fact.setSpecifiedFishingActivities(Arrays.asList(
                new FishingActivityType().withSpecifiedDelimitedPeriods(
                        new DelimitedPeriodType().withStartDateTime(
                                new DateTimeType().withDateTime(DateTime.parse("2995-11-24"))))));

        fact.setSpecifiedSalesEvents(Collections.singletonList(new SalesEventType().withOccurrenceDateTime(new DateTimeType().withDateTime(DateTime.now()))));

        assertTrue(fact.isAnySalesDateBeforeLandingDate());
    }

    @Test
    public void isTotalPriceFieldDifferentFromSumOfProductsWhenTotalPriceIsDifferent() throws Exception {
        fact.setTotalSalesPrice(new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.ONE)));
        fact.setSpecifiedSalesBatches(Arrays.asList(
                new SalesBatchType().withSpecifiedAAPProducts(
                        Arrays.asList(new AAPProductType().withTotalSalesPrice(
                                new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN)))))));

        assertTrue(fact.isTotalPriceFieldDifferentFromSumOfProducts());
    }

    @Test
    public void isTotalPriceFieldDifferentFromSumOfProductsWhenTotalPriceIsSame() throws Exception {
        fact.setTotalSalesPrice(new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN)));
        fact.setSpecifiedSalesBatches(Arrays.asList(
                new SalesBatchType().withSpecifiedAAPProducts(
                        Arrays.asList(new AAPProductType().withTotalSalesPrice(
                                new SalesPriceType().withChargeAmounts(new AmountType().withValue(BigDecimal.TEN)))))));

        assertFalse(fact.isTotalPriceFieldDifferentFromSumOfProducts());
    }

    @Test
    public void isInvalidCurrencyCodeWhenValid() throws Exception {
        fact.setCurrencyCode(new CodeType("EUR"));
        boolean validCurrencyCode = fact.isInvalidCurrencyCode();

        assertFalse(validCurrencyCode);
    }

    @Test
    public void isInvalidCurrencyCodeWhenInvalid() throws Exception {
        fact.setCurrencyCode(new CodeType("Gimme gimme gimme"));
        boolean validCurrencyCode = fact.isInvalidCurrencyCode();

        assertTrue(validCurrencyCode);
    }


    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesDocumentFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(SalesBatchType.class, new SalesBatchType().withIDS(new IDType().withValue("a")), new SalesBatchType().withIDS(new IDType().withValue("b")))
                .withPrefabValues(AAPProductType.class, new AAPProductType().withSpeciesCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new AAPProductType().withSpeciesCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FACatchType.class, new FACatchType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FACatchType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FishingActivityType.class, new FishingActivityType().withIDS(new IDType().withValue("BE")), new FishingActivityType().withIDS(new IDType().withValue("SWE")))
                .withPrefabValues(FLUXLocationType.class, new FLUXLocationType().withID(new IDType().withValue("a")), new FLUXLocationType().withID(new IDType().withValue("b")))
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "source", "sequence")
                .verify();
    }


    @Test
    public void isInvalidSalesNoteIDWhenTrue() {
        fact.setSalesNoteIDs(Arrays.asList(new IdType("abcd", "UUID")));
        assertTrue(fact.isInvalidSalesNoteID());
    }

    @Test
    public void isInvalidSalesNoteIDWhenFalse() {
        fact.setSalesNoteIDs(Arrays.asList(new IdType("ABC-SN-zfgjdHdjqH", "UUID")));
        assertFalse(fact.isInvalidSalesNoteID());
    }

    @Test
    public void hasTheNationalNumberPartOfTheIDAnIncorrectFormatWhenTrue() {
        fact.setIDS(Lists.newArrayList(new IdType("xyz")));
        assertTrue(fact.hasTheNationalNumberPartOfTheIDAnIncorrectFormat());

    }

    @Test
    public void hasTheNationalNumberPartOfTheIDAnIncorrectFormatWhenFalseBecauseIDsIsNull() {
        fact.setIDS(null);
        assertFalse(fact.hasTheNationalNumberPartOfTheIDAnIncorrectFormat());
    }

    @Test
    public void hasTheNationalNumberPartOfTheIDAnIncorrectFormatWhenFalseBecauseIDsIsEmpty() {
        fact.setIDS(new ArrayList<IdType>());
        assertFalse(fact.hasTheNationalNumberPartOfTheIDAnIncorrectFormat());
    }

    @Test
    public void hasTheNationalNumberPartOfTheIDAnIncorrectFormatWhenFalseBecauseOfTheFormat() {
        fact.setIDS(Lists.newArrayList(new IdType("DEF-SN-ABCD123456")));
        assertFalse(fact.hasTheNationalNumberPartOfTheIDAnIncorrectFormat());
    }

    @Test
    public void hasTheCommonPartOfTheIDAnIncorrectFormatWhenTrue() {
        fact.setIDS(Lists.newArrayList(new IdType("xyz")));
        assertTrue(fact.hasTheCommonPartOfTheIDAnIncorrectFormat());
    }

    @Test
    public void hasTheCommonPartOfTheIDAnIncorrectFormatWhenFalseBecauseIDsIsNull() {
        fact.setIDS(null);
        assertFalse(fact.hasTheCommonPartOfTheIDAnIncorrectFormat());
    }

    @Test
    public void hasTheCommonPartOfTheIDAnIncorrectFormatWhenFalseBecauseIDsIsEmpty() {
        fact.setIDS(new ArrayList<IdType>());
        assertFalse(fact.hasTheCommonPartOfTheIDAnIncorrectFormat());
    }

    @Test
    public void hasTheCommonPartOfTheIDAnIncorrectFormatWhenFalseBecauseOfTheFormat() {
        fact.setIDS(Lists.newArrayList(new IdType("DEF-SN-465468")));
        assertFalse(fact.hasTheCommonPartOfTheIDAnIncorrectFormat());
    }

    @Test
    public void hasTheTakeOverDocumentIdAnIncorrectFormatWhenTrue() {
        fact.setTakeoverDocumentIDs(Lists.newArrayList(new IdType("xyz")));
        assertTrue(fact.hasTheTakeOverDocumentIdAnIncorrectFormat());
    }

    @Test
    public void hasTheTakeOverDocumentIdAnIncorrectFormatWhenFalseBecauseIDsIsNull() {
        fact.setTakeoverDocumentIDs(null);
        assertFalse(fact.hasTheTakeOverDocumentIdAnIncorrectFormat());
    }

    @Test
    public void hasTheTakeOverDocumentIdAnIncorrectFormatWhenFalseBecauseIDsIsEmpty() {
        fact.setTakeoverDocumentIDs(new ArrayList<IdType>());
        assertFalse(fact.hasTheTakeOverDocumentIdAnIncorrectFormat());
    }

    @Test
    public void hasTheTakeOverDocumentIdAnIncorrectFormatWhenFalseBecauseOfTheFormat() {
        fact.setTakeoverDocumentIDs(Lists.newArrayList(new IdType("DEF-TOD-465468")));
        assertFalse(fact.hasTheTakeOverDocumentIdAnIncorrectFormat());
    }

}