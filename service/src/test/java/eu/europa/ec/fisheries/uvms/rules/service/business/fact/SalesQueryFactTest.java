package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.FLUXPartyType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.SalesQueryParameterType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType")
                .verify();
    }

    @Test
    public void hasIDInvalidFormatWhenTrue() {
        fact.setID(new IdType("abc"));
        assertTrue(fact.hasIDInvalidFormat());
    }

    @Test
    public void hasIDInvalidFormatWhenFalseBecauseIDIsNull() {
        fact.setID(null);
        assertFalse(fact.hasIDInvalidFormat());
    }

    @Test
    public void hasIDInvalidFormatWhenFalseBecauseOfAnInvalidFormat() {
        fact.setID(new IdType("c2731113-9e77-4e42-9c10-821575b72115"));
        assertFalse(fact.hasIDInvalidFormat());
    }

    @Test
    public void triesToQueryOnTypeFlagButIsNotAnInternationalOrganizationWhenQueryingOnFlagAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("FLAG").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeFlagButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeFlagButIsNotAnInternationalOrganizationWhenQueryingOnFlagAndNotBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("FLAG").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertTrue(fact.triesToQueryOnTypeFlagButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeFlagButIsNotAnInternationalOrganizationWhenQueryingOnRoleAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeFlagButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeFlagButIsNotAnInternationalOrganizationWhenQueryingOnRoleAndNotBeingAnInternationOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeFlagButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeFlagButIsNotAnInternationalOrganizationWhenNotSubmitterFLUXParty() {
        fact.setSubmitterFLUXParty(null);

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("FLAG").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertTrue(fact.triesToQueryOnTypeFlagButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeFlagButIsNotAnInternationalOrganizationWhenNoQueryParameter() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));
        fact.setSimpleSalesQueryParameters(new ArrayList<SalesQueryParameterType>());

        assertFalse(fact.triesToQueryOnTypeFlagButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenQueryingOnRoleWithValueFlagAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("FLAG"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertTrue(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenQueryingOnRoleWithValueFlagAndNotBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("FLAG"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenQueryingOnRoleWithValueLandAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("LAND"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertTrue(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenQueryingOnRoleWithValueLandAndNotBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("LAND"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenQueryingOnRoleWithValueIntAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("INT"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenQueryingOnRoleWithValueIntAndNotBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("INT"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenQueryingOnFlagAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("FLAG").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenQueryingOnFlagAndNotBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("FLAG").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenNoSubmitterFLUXParty() {
        fact.setSubmitterFLUXParty(null);

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("FLAG"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganizationWhenNoQueryParameter() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));
        fact.setSimpleSalesQueryParameters(new ArrayList<SalesQueryParameterType>());

        assertFalse(fact.triesToQueryOnTypeRoleAndValueFlagOrLandButIsAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganizationWhenQueryingOnRoleWithValueFlagAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("FLAG"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganizationWhenQueryingOnRoleWithValueFlagAndNotBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("FLAG"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganizationWhenQueryingOnRoleWithValueIntAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("INT"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganizationWhenQueryingOnRoleWithValueIntAndNotBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("INT"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertTrue(fact.triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganizationWhenQueryingOnFlagAndBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("FLAG").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganizationWhenQueryingOnFlagAndNotBeingAnInternationalOrganization() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("BEL")));

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("FLAG").withListID("FLUX_SALES_QUERY_PARAM"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertFalse(fact.triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganizationWhenNoSubmitterFLUXParty() {
        fact.setSubmitterFLUXParty(null);

        SalesQueryParameterType salesQueryParameterType = new SalesQueryParameterType()
                .withTypeCode(new CodeType().withValue("ROLE").withListID("FLUX_SALES_QUERY_PARAM"))
                .withValueCode(new CodeType().withValue("INT"));
        fact.setSimpleSalesQueryParameters(Arrays.asList(salesQueryParameterType));

        assertTrue(fact.triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganization());
    }

    @Test
    public void triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganizationWhenNoQueryParameter() {
        fact.setSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("XEU")));
        fact.setSimpleSalesQueryParameters(new ArrayList<SalesQueryParameterType>());

        assertFalse(fact.triesToQueryOnTypeRoleAndValueIntButIsNotAnInternationalOrganization());
    }

}