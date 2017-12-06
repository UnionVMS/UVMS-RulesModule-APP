package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesVesselTransportMeansFactTest {

    private SalesVesselTransportMeansFact fact;

    @Before
    public void init() {
        fact = new SalesVesselTransportMeansFact();
    }

    @Test
    public void doContactPartiesWithTheSameRoleExistWhenDuplicatesFound() {
        ContactPartyType contactParty1 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty2 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty3 = new ContactPartyType().withRoleCodes(new CodeType().withValue("C"));
        fact.setSpecifiedContactParties(Arrays.asList(contactParty1, contactParty2, contactParty3));

        assertTrue(fact.doContactPartiesWithTheSameRoleExist());
    }

    @Test
    public void doContactPartiesWithTheSameRoleExistWhenNoDuplicatesFound() {
        ContactPartyType contactParty1 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty2 = new ContactPartyType().withRoleCodes(new CodeType().withValue("B"));
        ContactPartyType contactParty3 = new ContactPartyType().withRoleCodes(new CodeType().withValue("C"));
        fact.setSpecifiedContactParties(Arrays.asList(contactParty1, contactParty2, contactParty3));

        assertFalse(fact.doContactPartiesWithTheSameRoleExist());
    }

    @Test
    public void doContactPartiesWithTheSameRoleExistWhenAContactPartyIsNullAndDuplicatesFound() {
        ContactPartyType contactParty1 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty3 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        fact.setSpecifiedContactParties(Arrays.asList(contactParty1, null, contactParty3));

        assertTrue(fact.doContactPartiesWithTheSameRoleExist());
    }

    @Test
    public void doContactPartiesWithTheSameRoleExistWhenAContactPartyIsNullAndNoDuplicatesFound() {
        ContactPartyType contactParty1 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty3 = new ContactPartyType().withRoleCodes(new CodeType().withValue("C"));
        fact.setSpecifiedContactParties(Arrays.asList(contactParty1, null, contactParty3));

        assertFalse(fact.doContactPartiesWithTheSameRoleExist());
    }

    @Test
    public void doContactPartiesWithTheSameRoleExistWhenARoleCodeIsNullAndDuplicatesFound() {
        ContactPartyType contactParty1 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty2 = new ContactPartyType().withRoleCodes((CodeType) null);
        ContactPartyType contactParty3 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        fact.setSpecifiedContactParties(Arrays.asList(contactParty1, contactParty2, contactParty3));

        assertTrue(fact.doContactPartiesWithTheSameRoleExist());
    }

    @Test
    public void doContactPartiesWithTheSameRoleExistWhenARoleCodeIsNullAndNoDuplicatesFound() {
        ContactPartyType contactParty1 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty2 = new ContactPartyType().withRoleCodes((CodeType) null);
        ContactPartyType contactParty3 = new ContactPartyType().withRoleCodes(new CodeType().withValue("C"));
        fact.setSpecifiedContactParties(Arrays.asList(contactParty1, contactParty2, contactParty3));

        assertFalse(fact.doContactPartiesWithTheSameRoleExist());
    }

    @Test
    public void doContactPartiesWithTheSameRoleExistWhenARoleCodeValueIsNullAndDuplicatesFound() {
        ContactPartyType contactParty1 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty2 = new ContactPartyType().withRoleCodes(new CodeType().withValue(null));
        ContactPartyType contactParty3 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        fact.setSpecifiedContactParties(Arrays.asList(contactParty1, contactParty2, contactParty3));

        assertTrue(fact.doContactPartiesWithTheSameRoleExist());
    }

    @Test
    public void doContactPartiesWithTheSameRoleExistWhenARoleCodeValueIsNullAndNoDuplicatesFound() {
        ContactPartyType contactParty1 = new ContactPartyType().withRoleCodes(new CodeType().withValue("A"));
        ContactPartyType contactParty2 = new ContactPartyType().withRoleCodes(new CodeType().withValue(null));
        ContactPartyType contactParty3 = new ContactPartyType().withRoleCodes(new CodeType().withValue("C"));
        fact.setSpecifiedContactParties(Arrays.asList(contactParty1, contactParty2, contactParty3));

        assertFalse(fact.doContactPartiesWithTheSameRoleExist());
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesVesselTransportMeansFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withPrefabValues(FishingGearType.class, new FishingGearType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FishingGearType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(GearCharacteristicType.class, new GearCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new GearCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FLUXLocationType.class, new FLUXLocationType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FLUXLocationType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withPrefabValues(FLUXCharacteristicType.class, new FLUXCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("a")), new FLUXCharacteristicType().withTypeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("b")))
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType", "originatingPlugin")
                .verify();
    }

}