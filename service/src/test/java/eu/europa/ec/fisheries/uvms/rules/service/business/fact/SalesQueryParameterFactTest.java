package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesQueryParameterFactTest {
    SalesQueryParameterFact fact;

    @Before
    public void setUp() throws Exception {
        fact = new SalesQueryParameterFact();
    }

    @Test
    public void isValueNotValidWhenNotValid() throws Exception {
        CodeType valueCode = new CodeType();
        valueCode.setValue("INVALIDFLAG");

        CodeType typeCode = new CodeType();
        typeCode.setValue("FLAG");

        fact.setValueCode(valueCode);
        fact.setTypeCode(typeCode);
        assertTrue(fact.isValueNotValid());
    }

    @Test
    public void isValueNotValidWhenValid() throws Exception {
        CodeType valueCode = new CodeType();
        valueCode.setValue("BEL");
        valueCode.setListId("LOCATION");

        CodeType typeCode = new CodeType();
        typeCode.setValue("FLAG");

        fact.setValueCode(valueCode);
        fact.setTypeCode(typeCode);
        assertFalse(fact.isValueNotValid());
    }

    @Test
    public void isValueNotValidWhenInvalidTypeCode() throws Exception {
        CodeType valueCode = new CodeType();
        valueCode.setValue("BEL");

        CodeType typeCode = new CodeType();
        typeCode.setValue("Please trigger the default case");

        fact.setValueCode(valueCode);
        fact.setTypeCode(typeCode);
        assertTrue(fact.isValueNotValid());
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesQueryParameterFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "source", "sequence")
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