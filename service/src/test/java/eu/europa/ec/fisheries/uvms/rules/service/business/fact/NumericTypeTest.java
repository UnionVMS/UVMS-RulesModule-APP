package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class NumericTypeTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(NumericType.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}