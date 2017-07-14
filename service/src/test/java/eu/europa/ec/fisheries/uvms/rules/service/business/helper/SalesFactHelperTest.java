package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;

import eu.europa.ec.fisheries.schema.sales.AmountType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import org.junit.Test;

/**
 * Created by MATBUL on 21/06/2017.
 */
public class SalesFactHelperTest {

    @Test
    public void setContainsAnyValueWhenValueIsFound() throws Exception {
        boolean valid = SalesFactHelper.doesSetContainAnyValue(Arrays.asList("EUR"), SalesFactHelper.getValidCurrencies());
        assertTrue(valid);
    }

    @Test
    public void setContainsAnyValueWhenValueIsNotFound() throws Exception {
        boolean valid = SalesFactHelper.doesSetContainAnyValue(Arrays.asList("AAAAAAAAAAAAAAAAAAAAAAAAA"), SalesFactHelper.getValidCurrencies());
        assertFalse(valid);
    }

    @Test
    public void testAllValuesGreaterOrEqualToZeroWhenValuesAreGreaterOrEqualToZero() throws Exception {
        boolean allValuesGreaterOrEqualToZero = SalesFactHelper.allValuesGreaterOrEqualToZero(Arrays.asList(new AmountType().withValue(BigDecimal.ONE), new AmountType().withValue(BigDecimal.TEN), new AmountType().withValue(BigDecimal.ZERO)));
        assertTrue(allValuesGreaterOrEqualToZero);
    }

    @Test
    public void testAllValuesGreaterOrEqualToZeroWhenValuesAreNotGreaterNorEqualToZero() throws Exception {
        boolean allValuesGreaterOrEqualToZero = SalesFactHelper.allValuesGreaterOrEqualToZero(Arrays.asList(new AmountType().withValue(new BigDecimal(-5)), new AmountType().withValue(BigDecimal.TEN), new AmountType().withValue(BigDecimal.ZERO)));
        assertFalse(allValuesGreaterOrEqualToZero);
    }

    @Test
    public void testAllValuesGreaterOrEqualToZeroWhenOneOfTheValuesIsNull() throws Exception {
        boolean allValuesGreaterOrEqualToZero = SalesFactHelper.allValuesGreaterOrEqualToZero(Arrays.asList(new AmountType().withValue(null), new AmountType().withValue(BigDecimal.TEN), new AmountType().withValue(BigDecimal.ZERO)));
        assertFalse(allValuesGreaterOrEqualToZero);
    }

    @Test
    public void testAnyValueEqualToZeroWhenValueIsEqualToZero() throws Exception {
        boolean valueEqualToZero = SalesFactHelper.anyValueEqualToZero(Arrays.asList(new AmountType().withValue(BigDecimal.ZERO)));
        assertTrue(valueEqualToZero);
    }

    @Test
    public void testAnyValueEqualToZeroWhenValueNotEqualToZero() throws Exception {
        boolean valueEqualToZero = SalesFactHelper.anyValueEqualToZero(Arrays.asList(new AmountType().withValue(BigDecimal.TEN)));
        assertFalse(valueEqualToZero);
    }

    @Test
    public void testAnyValueEqualToZeroWhenValueIsNull() throws Exception {
        boolean valueEqualToZero = SalesFactHelper.anyValueEqualToZero(Arrays.asList(new AmountType().withValue(null)));
        assertTrue(valueEqualToZero);
    }

    @Test
    public void testIfCountryIdIsValidWhenValueIsValid() throws Exception {
        IdType idType = new IdType();
        idType.setValue("BEL");

        boolean isValid = SalesFactHelper.isCountryIdValid(idType);
        assertTrue(isValid);
    }

    @Test
    public void testIfCountryIdIsValidWhenValueIsInvalid() throws Exception {
        IdType idType = new IdType();
        idType.setValue("LEB");

        boolean isValid = SalesFactHelper.isCountryIdValid(idType);
        assertFalse(isValid);
    }

    @Test
    public void testIfCountryIdIsValidWhenValueIsNull() throws Exception {
        boolean isValid = SalesFactHelper.isCountryIdValid(null);
        assertFalse(isValid);
    }

    @Test
    public void testIfCountryIdIsValidWhenIdTypeValueIsNull() throws Exception {
        IdType idType = new IdType();
        idType.setValue("");

        boolean isValid = SalesFactHelper.isCountryIdValid(idType);
        assertFalse(isValid);
    }

}