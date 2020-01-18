package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class SalesBatchFactTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesBatchFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(AAPProductType.class, new AAPProductType().withSpeciesCode(new CodeType().withValue("a")), new AAPProductType().withSpeciesCode(new CodeType().withValue("b")))
                .withRedefinedSuperclass()
                .withIgnoredFields("messageType", "factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType", "originatingPlugin", "creationJavaDateOfMessage", "messageDataFlow")
                .verify();
    }

}