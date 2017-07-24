package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Ignore;
import org.junit.Test;

public class SalesDelimitedPeriodFactTest {

    @Test
    @Ignore // FIXME
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesDelimitedPeriodFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok")
                .verify();
    }
}