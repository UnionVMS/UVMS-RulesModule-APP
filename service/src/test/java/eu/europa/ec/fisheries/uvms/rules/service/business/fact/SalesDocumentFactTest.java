package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.FACatchType;
import eu.europa.ec.fisheries.schema.sales.FLUXLocationType;
import eu.europa.ec.fisheries.schema.sales.FishingActivityType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.SalesBatchType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Ignore;
import org.junit.Test;

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


    @Test
    @Ignore // FIXME
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
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok")
                .verify();
    }

}