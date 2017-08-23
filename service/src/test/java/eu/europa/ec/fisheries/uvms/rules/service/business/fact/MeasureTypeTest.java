package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class MeasureTypeTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(MeasureType.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}