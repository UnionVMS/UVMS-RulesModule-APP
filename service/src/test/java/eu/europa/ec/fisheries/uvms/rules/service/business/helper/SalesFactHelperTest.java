package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MATBUL on 21/06/2017.
 */
public class SalesFactHelperTest {

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