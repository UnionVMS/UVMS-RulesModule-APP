package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Ignore;
import org.junit.Test;

public class CodeTypeTest {

    @Test
    @Ignore // FIXME
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(CodeType.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}