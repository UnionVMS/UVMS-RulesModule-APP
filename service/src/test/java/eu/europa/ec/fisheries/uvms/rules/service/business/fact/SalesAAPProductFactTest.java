package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import eu.europa.ec.fisheries.schema.sales.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;


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

    }
    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesAAPProductFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(FACatchType.class, new FACatchType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FACatchType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(AAPProductType.class, new AAPProductType().withSpeciesCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new AAPProductType().withSpeciesCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FLUXLocationType.class, new FLUXLocationType().withID(new IDType().withValue("BE")), new FLUXLocationType().withID(new IDType().withValue("SWE")))
                .withPrefabValues(FishingActivityType.class, new FishingActivityType().withIDS(new IDType().withValue("BE")), new FishingActivityType().withIDS(new IDType().withValue("SWE")))
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok")
                .verify();
    }

}