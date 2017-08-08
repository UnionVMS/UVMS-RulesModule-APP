package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.sales.AmountType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesAAPProductFact;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesAbstractFactTest {

    private SalesAbstractFact fact;

    @Before
    public void init() {
        fact = new SalesAAPProductFact();
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