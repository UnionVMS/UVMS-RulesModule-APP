package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleTestHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.MDRCacheHolder;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesQueryParameterFactTest {
    private SalesQueryParameterFact fact;

    @Before
    public void setUp() throws Exception {
        fact = new SalesQueryParameterFact();
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.FLUX_SALES_QUERY_PARAM_ROLE, RuleTestHelper.getObjectRepresentationForFLUX_SALES_QUERY_PARAM_ROLE());
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.TERRITORY, RuleTestHelper.getObjectRepresentationForTERRITORY());
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.LOCATION, RuleTestHelper.getObjectRepresentationForLOCATION());
    }

    @Test
    public void isValueNotValidWhenTypeCodeIsFlagAndValueCodeIsNotValid() throws Exception {
        CodeType valueCode = new CodeType();
        valueCode.setValue("INVALIDFLAG");

        CodeType typeCode = new CodeType();
        typeCode.setValue("FLAG");

        fact.setValueCode(valueCode);
        fact.setTypeCode(typeCode);
        assertTrue(fact.isValueNotValid());
    }

    @Test
    public void isValueNotValidWhenTypeCodeIsFlagAndValueCodeIsValid() throws Exception {
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
    public void isValueNotValidWhenTypeCodeIsRoleAndValueCodeIsNotValid() throws Exception {
        CodeType valueCode = new CodeType();
        valueCode.setValue("UNKNOWN");

        CodeType typeCode = new CodeType();
        typeCode.setValue("ROLE");

        fact.setValueCode(valueCode);
        fact.setTypeCode(typeCode);
        assertTrue(fact.isValueNotValid());
    }

    @Test
    public void isValueNotValidWhenTypeCodeIsRoleAndValueCodeIsValid() throws Exception {
        CodeType valueCode = new CodeType();
        valueCode.setValue("FLAG");

        CodeType typeCode = new CodeType();
        typeCode.setValue("ROLE");

        fact.setValueCode(valueCode);
        fact.setTypeCode(typeCode);
        assertFalse(fact.isValueNotValid());
    }

    @Test
    public void isValueNotValidWhenTypeCodeIsPlaceAndValueCodeIsNotValid() throws Exception {
        CodeType valueCode = new CodeType();
        valueCode.setValue("UNKNOWN");

        CodeType typeCode = new CodeType();
        typeCode.setValue("PLACE");

        fact.setValueCode(valueCode);
        fact.setTypeCode(typeCode);
        assertTrue(fact.isValueNotValid());
    }

    @Test
    public void isValueNotValidWhenTypeCodeIsPlaceAndValueCodeIsValid() throws Exception {
        CodeType valueCode = new CodeType();
        valueCode.setValue("BEOST");

        CodeType typeCode = new CodeType();
        typeCode.setValue("PLACE");

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
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType", "rulesDomainModel", "originatingPlugin", "sender")
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