package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
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

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesSizeDistributionFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType")
                .verify();
    }

}