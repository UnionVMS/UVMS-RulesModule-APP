package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.sales.AmountType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.SalesCategoryType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesAbstractFactTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private SalesFLUXSalesReportMessageFact fact;

    @Before
    public void init() {
        fact = new SalesFLUXSalesReportMessageFact();
        fact.setSalesCategoryType(SalesCategoryType.FIRST_SALE);
    }

    @Test
    public void testCheckNotNullWhenNotNull() throws Exception {
        fact.setSource(Source.AUCTION);
        fact.setSalesCategoryType(SalesCategoryType.VARIOUS_SUPPLY);

        // No errors
        fact.checkNotNull();
    }

    @Test
    public void testCheckNotNullWhenSourceNull() throws Exception {
        fact.setSource(null);
        fact.setSalesCategoryType(SalesCategoryType.VARIOUS_SUPPLY);

        exception.expect(NullPointerException.class);
        exception.expectMessage("Source cannot be null. Did you forget to add it to the fact generator?");
        fact.checkNotNull();
    }

    @Test
    public void testCheckNotNullWhenCategoryNull() throws Exception {
        fact.setSource(Source.AUCTION);
        fact.setSalesCategoryType(null);

        exception.expect(NullPointerException.class);
        exception.expectMessage("SalesCategoryType cannot be null. Did you forget to add it to the fact generator?");
        fact.checkNotNull();
    }

    @Test
    public void testIsNotVariousSupplyWhenVariousSupply() throws Exception {
        fact.setSource(Source.AUCTION);
        fact.setSalesCategoryType(SalesCategoryType.VARIOUS_SUPPLY);

        assertFalse(fact.isNotVariousSupply());
    }

    @Test
    public void testIsNotVariousSupplyWhenNotVariousSupply() throws Exception {
        fact.setSource(Source.AUCTION);
        fact.setSalesCategoryType(SalesCategoryType.FIRST_SALE);

        assertTrue(fact.isNotVariousSupply());
    }

    @Test
    public void testIsNotUTCWhenNotUTC() throws Exception {
        DateTimeType creationDateTime = new DateTimeType().withDateTime(DateTime.now().withZone(DateTimeZone.forOffsetHours(3)));
        assertTrue(fact.isNotUTC(creationDateTime));
    }

    @Test
    public void testIsNotUTCWhenUTC() throws Exception {
        DateTimeType creationDateTime = new DateTimeType().withDateTime(DateTime.now().withZone(DateTimeZone.UTC));
        assertFalse(fact.isNotUTC(creationDateTime));
    }

    @Test
    public void isQueryWhenTrue() throws Exception {
        fact.setSource(Source.QUERY);
        assertTrue(fact.isQuery());
    }

    @Test
    public void isQueryWhenReport() throws Exception {
        fact.setSource(Source.REPORT);
        assertFalse(fact.isQuery());
    }

    @Test
    public void isQueryWhenResponse() throws Exception {
        fact.setSource(Source.RESPONSE);
        assertFalse(fact.isQuery());
    }

    @Test
    public void isResponseWhenTrue() throws Exception {
        fact.setSource(Source.RESPONSE);
        assertTrue(fact.isResponse());
    }

    @Test
    public void isResponseWhenReport() throws Exception {
        fact.setSource(Source.REPORT);
        assertFalse(fact.isResponse());
    }

    @Test
    public void isResponseWhenQuery() throws Exception {
        fact.setSource(Source.QUERY);
        assertFalse(fact.isResponse());
    }

    @Test
    public void isReportWhenTrue() throws Exception {
        fact.setSource(Source.REPORT);
        assertTrue(fact.isReport());
    }

    @Test
    public void isReportWhenQuery() throws Exception {
        fact.setSource(Source.QUERY);
        assertFalse(fact.isReport());
    }

    @Test
    public void isReportWhenResponse() throws Exception {
        fact.setSource(Source.RESPONSE);
        assertFalse(fact.isReport());
    }

    @Test
    public void nullValuesInAmountTypesWhenTrueBecauseThereIsANullValue() throws Exception {
        List<AmountType> amountTypes = Arrays.asList(new AmountType().withValue(BigDecimal.ONE), new AmountType().withValue(null));
        assertTrue(fact.nullValuesInAmountTypes(amountTypes));
    }

    @Test
    public void nullValuesInAmountTypesWhenTrueBecauseThereIsAnAmountTypeThatIsNull() throws Exception {
        List<AmountType> amountTypes = Arrays.asList(new AmountType().withValue(BigDecimal.ONE), null);
        assertTrue(fact.nullValuesInAmountTypes(amountTypes));
    }

    @Test
    public void nullValuesInAmountTypesWhenTrueBecauseTheListIsNull() throws Exception {
        assertTrue(fact.nullValuesInAmountTypes(null));
    }

    @Test
    public void nullValuesInAmountTypesWhenTrueBecauseTheListIsEmpty() throws Exception {
        assertTrue(fact.nullValuesInAmountTypes(new ArrayList<AmountType>()));
    }

    @Test
    public void nullValuesInAmountTypesWhenFalse() throws Exception {
        List<AmountType> amountTypes = Arrays.asList(new AmountType().withValue(BigDecimal.ONE), new AmountType().withValue(BigDecimal.TEN));
        assertFalse(fact.nullValuesInAmountTypes(amountTypes));
    }

}