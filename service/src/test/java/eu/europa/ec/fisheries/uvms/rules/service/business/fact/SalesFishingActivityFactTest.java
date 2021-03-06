package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import eu.europa.ec.fisheries.schema.sales.AAPProcessType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.FACatchType;
import eu.europa.ec.fisheries.schema.sales.FLUXCharacteristicType;
import eu.europa.ec.fisheries.schema.sales.FLUXLocationType;
import eu.europa.ec.fisheries.schema.sales.FishingActivityType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

public class SalesFishingActivityFactTest {

    private SalesFishingActivityFact salesFishingActivityFact;

    @Before
    public void setUp() throws Exception {
        salesFishingActivityFact = new SalesFishingActivityFact();
    }

    @Test
    public void isRelatedFLUXLocationsEmptyOrTypeLocationWhenLocation() throws Exception {
        salesFishingActivityFact.setRelatedFLUXLocations(Arrays.asList(new FLUXLocationType().withTypeCode(new CodeType().withValue("LOCATION"))));

        assertTrue(salesFishingActivityFact.isRelatedFLUXLocationsEmptyOrTypeLocation());
    }


    @Test
    public void isRelatedFLUXLocationsEmptyOrTypeLocationWhenNotLocation() throws Exception {
        salesFishingActivityFact.setRelatedFLUXLocations(Arrays.asList(new FLUXLocationType().withTypeCode(new CodeType().withValue("NOT LOCATION"))));

        assertFalse(salesFishingActivityFact.isRelatedFLUXLocationsEmptyOrTypeLocation());
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesFishingActivityFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(FACatchType.class, new FACatchType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FACatchType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(AAPProcessType.class, new AAPProcessType().withTypeCodes(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new AAPProcessType().withTypeCodes(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FLUXLocationType.class, new FLUXLocationType().withID(new IDType().withValue("a")), new FLUXLocationType().withID(new IDType().withValue("b")))
                .withPrefabValues(FLUXCharacteristicType.class, new FLUXCharacteristicType().withTypeCode(new CodeType().withValue("a")), new FLUXCharacteristicType().withTypeCode(new CodeType().withValue("b")))
                .withPrefabValues(FishingActivityType.class, new FishingActivityType().withIDS(new IDType().withValue("BE")), new FishingActivityType().withIDS(new IDType().withValue("SWE")))
                .withRedefinedSuperclass()
                .withIgnoredFields("messageType", "factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType", "originatingPlugin")
                .verify();
    }

}