package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MATBUL on 22/06/2017.
 */
public class SalesAAPProductFactTest {

    @Test
    public void isInvalidUsageCodeWhenInvalid() throws Exception {
        SalesAAPProductFact salesAAPProductFact = new SalesAAPProductFact();
        salesAAPProductFact.setUsageCode(new CodeType("INVALID"));

        boolean invalidUsageCode = salesAAPProductFact.isInvalidUsageCode();
        assertTrue(invalidUsageCode);
    }
    @Test
    public void isInvalidUsageCodeWhenValid() throws Exception {
        SalesAAPProductFact salesAAPProductFact = new SalesAAPProductFact();
        salesAAPProductFact.setUsageCode(new CodeType("HCN"));

        boolean validCode = salesAAPProductFact.isInvalidUsageCode();
        assertFalse(validCode);
    }

}