package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class SalesFLUXGeographicalCoordinateFactTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesFLUXGeographicalCoordinateFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType", "originatingPlugin", "sender")
                .verify();
    }

}