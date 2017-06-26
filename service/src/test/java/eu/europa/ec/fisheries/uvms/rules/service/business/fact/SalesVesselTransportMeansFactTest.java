package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class SalesVesselTransportMeansFactTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesVesselTransportMeansFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withPrefabValues(FishingGearType.class, new FishingGearType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FishingGearType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(GearCharacteristicType.class, new GearCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new GearCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FLUXLocationType.class, new FLUXLocationType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FLUXLocationType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FLUXCharacteristicType.class, new FLUXCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FLUXCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok")
                .verify();
    }

}