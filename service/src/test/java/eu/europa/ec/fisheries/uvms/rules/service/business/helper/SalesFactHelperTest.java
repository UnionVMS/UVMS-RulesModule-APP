package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import eu.europa.ec.fisheries.schema.sales.AmountType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MATBUL on 21/06/2017.
 */
public class SalesFactHelperTest {

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
    public void testAllValuesGreaterOrEqualToZeroWhenOneOfTheValuesIsNullAndTheRestIsGreaterThanOrEqualToZero() throws Exception {
        boolean allValuesGreaterOrEqualToZero = SalesFactHelper.allValuesGreaterOrEqualToZero(Arrays.asList(new AmountType().withValue(null), new AmountType().withValue(BigDecimal.TEN), new AmountType().withValue(BigDecimal.ZERO)));
        assertTrue(allValuesGreaterOrEqualToZero);
    }

    @Test
    public void testAllValuesGreaterOrEqualToZeroWhenOneOfTheValuesIsNullAndTheRestIsSmallerThanZero() throws Exception {
        boolean allValuesGreaterOrEqualToZero = SalesFactHelper.allValuesGreaterOrEqualToZero(Arrays.asList(new AmountType().withValue(null), new AmountType().withValue(new BigDecimal(-5))));
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

}