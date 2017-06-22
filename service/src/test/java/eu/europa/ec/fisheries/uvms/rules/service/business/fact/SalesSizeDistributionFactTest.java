package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by MATBUL on 22/06/2017.
 */
public class SalesSizeDistributionFactTest {

    @Test
    public void testIsInvalidCategoryCodeWhenCodeIsInvalid() throws Exception {
        SalesSizeDistributionFact salesSizeDistributionFact = new SalesSizeDistributionFact();
        salesSizeDistributionFact.setCategoryCode(new CodeType("bla"));

        Assert.assertTrue(salesSizeDistributionFact.isInvalidCategoryCode());
    }

    @Test
    public void testIsInvalidCategoryCodeWhenCodeIsValid() throws Exception {
        SalesSizeDistributionFact salesSizeDistributionFact = new SalesSizeDistributionFact();
        salesSizeDistributionFact.setCategoryCode(new CodeType("8"));

        Assert.assertFalse(salesSizeDistributionFact.isInvalidCategoryCode());
    }

    @Test
    public void testIsInvalidCategoryCodeWhenCodeIsNull() throws Exception {
        SalesSizeDistributionFact salesSizeDistributionFact = new SalesSizeDistributionFact();
        salesSizeDistributionFact.setCategoryCode(new CodeType(null));

        Assert.assertTrue(salesSizeDistributionFact.isInvalidCategoryCode());
    }

}