package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.SalesQueryParameterType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesQueryFactTest {
    SalesQueryFact fact;

    @Before
    public void setUp() throws Exception {
        fact = new SalesQueryFact();
    }

    @Test
    public void anyQueryParameterOfTypeRoleWithValueWhenValid() throws Exception {
        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("abc123456789").withListID("CFR"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertTrue(fact.anyQueryParameterOfTypeRoleWithValue());
    }

    @Test
    public void anyQueryParameterOfTypeRoleWithValueWhenInvalid() throws Exception {
        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE"))
                .withValueCode(new CodeType());
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.anyQueryParameterOfTypeRoleWithValue());
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesQueryFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "source")
                .verify();
    }

}