package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by MATBUL on 22/06/2017.
 */
public class SalesAAPProductFactTest {

    private SalesAAPProductFact fact;

    @Before
    public void setUp() throws Exception {
        fact = new SalesAAPProductFact();
    }

    @Test
    public void isBMSSpeciesAndUsageIsNotForNonDirectHumanConsumptionWhenUsageIsHCN() throws Exception {
        fact.setSpecifiedSizeDistribution(new SizeDistributionType().withClassCodes(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("BMS")));
        fact.setUsageCode(new CodeType("HCN"));

        assertTrue(fact.isBMSSpeciesAndUsageIsNotForNonDirectHumanConsumption());
    }

    @Test
    public void isBMSSpeciesAndUsageIsNotForNonDirectHumanConsumptionWhenUsageIsHCNIndirect() throws Exception {
        fact.setSpecifiedSizeDistribution(new SizeDistributionType().withClassCodes(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("BMS")));
        fact.setUsageCode(new CodeType("HCN-INDIRECT"));

        assertFalse(fact.isBMSSpeciesAndUsageIsNotForNonDirectHumanConsumption());
    }

    @Test
    public void isOriginFLUXLocationEmptyOrTypeNotLocationWhenTypeIsNotLocation() throws Exception {
        fact.setOriginFLUXLocations(Collections.singletonList(new FLUXLocationType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("NOT LOCATION"))));

        assertTrue(fact.isOriginFLUXLocationEmptyOrTypeNotLocation());
    }

    @Test
    public void isOriginFLUXLocationEmptyOrTypeNotLocationWhenTypeIsLocation() throws Exception {
        fact.setOriginFLUXLocations(Collections.singletonList(new FLUXLocationType()
                .withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("LOCATION"))));

        assertFalse(fact.isOriginFLUXLocationEmptyOrTypeNotLocation());
    }

    @Test
    public void isInvalidUsageCodeWhenInvalid() throws Exception {
        fact.setUsageCode(new CodeType("INVALID"));

        boolean invalidUsageCode = fact.isInvalidUsageCode();
        assertTrue(invalidUsageCode);
    }

    @Test
    public void isInvalidUsageCodeWhenValid() throws Exception {
        fact.setUsageCode(new CodeType("HCN"));

        boolean validCode = fact.isInvalidUsageCode();
        assertFalse(validCode);
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
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver")
                .verify();
    }

}