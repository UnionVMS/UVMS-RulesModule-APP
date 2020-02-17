package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

public class SalesQueryParameterFactTest {
    private SalesQueryParameterFact fact;

    @Before
    public void setUp() throws Exception {
        fact = new SalesQueryParameterFact();
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesQueryParameterFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withIgnoredFields("messageType","factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType", "originatingPlugin")
                .verify();
    }

    @Test
    public void hasTheNationalNumberPartOfTheValueIDAnIncorrectFormatWhenTrue() {
        fact.setValueID(new IdType("xyz"));
        assertTrue(fact.hasTheNationalNumberPartOfTheValueIDAnIncorrectFormat());

    }

    @Test
    public void hasTheNationalNumberPartOfTheValueIDAnIncorrectFormatWhenFalseBecauseIDsIsNull() {
        fact.setValueID(null);
        assertFalse(fact.hasTheNationalNumberPartOfTheValueIDAnIncorrectFormat());
    }

    @Test
    public void hasTheNationalNumberPartOfTheValueIDAnIncorrectFormatWhenFalseBecauseOfTheFormat() {
        fact.setValueID((new IdType("DEF-SN-ABCD123456")));
        assertFalse(fact.hasTheNationalNumberPartOfTheValueIDAnIncorrectFormat());
    }


    @Test
    public void hasTheCommonPartOfTheValueIDAnIncorrectFormatWhenFalseBecauseIDsIsNull() {
        fact.setValueID(null);
        assertFalse(fact.hasTheCommonPartOfTheValueIDAnIncorrectFormat());
    }

    @Test
    public void hasTheCommonPartOfTheValueIDAnIncorrectFormatWhenFalseBecauseOfTheFormat() {
        fact.setValueID(new IdType("DEF-SN-465468"));
        assertFalse(fact.hasTheCommonPartOfTheValueIDAnIncorrectFormat());
    }

}