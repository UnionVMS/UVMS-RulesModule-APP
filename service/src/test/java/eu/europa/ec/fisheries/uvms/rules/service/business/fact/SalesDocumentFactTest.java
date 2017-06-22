package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MATBUL on 22/06/2017.
 */
public class SalesDocumentFactTest {

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
}