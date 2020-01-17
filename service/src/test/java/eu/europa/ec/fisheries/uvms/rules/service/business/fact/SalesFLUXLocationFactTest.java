package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.FLUXCharacteristicType;
import eu.europa.ec.fisheries.schema.sales.FLUXLocationType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class SalesFLUXLocationFactTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesFLUXLocationFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(FLUXLocationType.class, new FLUXLocationType().withID(new IDType().withValue("a")), new FLUXLocationType().withID(new IDType().withValue("b")))
                .withPrefabValues(FLUXCharacteristicType.class, new FLUXCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FLUXCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withRedefinedSuperclass()
                .withIgnoredFields("messageType", "factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType", "originatingPlugin", "creationJavaDateOfMessage", "messageDataFlow", "creationJavaDateOfMessage", "messageDataFlow")
                .verify();
    }

}