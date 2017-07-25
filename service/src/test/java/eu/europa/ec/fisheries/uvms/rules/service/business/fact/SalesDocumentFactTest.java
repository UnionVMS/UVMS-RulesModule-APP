package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
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
    public void issAnySalesDateBeforeLandingDateWhenLandingDatesAreValid() throws Exception {
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
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setCurrencyCode(new CodeType("EUR"));
        boolean validCurrencyCode = salesDocumentFact.isInvalidCurrencyCode();

        assertFalse(validCurrencyCode);
    }

    @Test
    public void isInvalidCurrencyCodeWhenInvalid() throws Exception {
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setCurrencyCode(new CodeType("Gimme gimme gimme"));
        boolean validCurrencyCode = salesDocumentFact.isInvalidCurrencyCode();

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
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "source")
                .verify();
    }

}