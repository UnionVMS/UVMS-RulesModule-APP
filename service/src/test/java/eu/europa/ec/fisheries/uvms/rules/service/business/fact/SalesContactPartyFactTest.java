package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class SalesContactPartyFactTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesContactPartyFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(AAPProductType.class, new AAPProductType().withSpeciesCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new AAPProductType().withSpeciesCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver")
                .verify();
    }

}