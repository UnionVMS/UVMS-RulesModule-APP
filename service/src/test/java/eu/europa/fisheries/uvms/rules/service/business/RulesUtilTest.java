package eu.europa.fisheries.uvms.rules.service.business;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import eu.europa.ec.fisheries.schema.rules.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleActionType;
import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleSegmentType;
import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.service.business.CustomRuleDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;

public class RulesUtilTest {
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    /**
     * @throws Exception
     */
    @Test
    public void testResourceDrl() throws Exception {
        List<CustomRuleType> rawRules = new ArrayList<CustomRuleType>();

        CustomRuleType rawRule = new CustomRuleType();
        rawRule.setName("DummyName");

        // First action
        CustomRuleActionType action1 = new CustomRuleActionType();
        action1.setType(ActionType.EMAIL);
        action1.setText("user@company.se");
        rawRule.getNotifications().add(action1);

        // Second action
        CustomRuleActionType action2 = new CustomRuleActionType();
        action2.setType(ActionType.SMS);
        action2.setText("+46111111111");
        rawRule.getNotifications().add(action2);

        // First part of rule
        CustomRuleSegmentType segment1 = new CustomRuleSegmentType();
        segment1.setStartOperator("(");
        segment1.setCriteria("VESSEL");
        segment1.setSubCriteria("CFR");
        segment1.setCondition("EQ");
        segment1.setValue("SWE111222");
        segment1.setEndOperator("");
        segment1.setLogicBoolOperator("OR");
        segment1.setOrder("0");
        rawRule.getDefinitions().add(segment1);

        // Second part of rule
        CustomRuleSegmentType segment2 = new CustomRuleSegmentType();
        segment2.setStartOperator("");
        segment2.setCriteria("VESSEL");
        segment2.setSubCriteria("CFR");
        segment2.setCondition("EQ");
        segment2.setValue("SWE111333");
        segment2.setEndOperator(")");
        segment2.setLogicBoolOperator("AND");
        segment2.setOrder("1");
        rawRule.getDefinitions().add(segment2);

        // Third part of rule
        CustomRuleSegmentType segment3 = new CustomRuleSegmentType();
        segment3.setStartOperator("");
        segment3.setCriteria("MOBILE_TERMINAL");
        segment3.setSubCriteria("Member_id");
        segment3.setCondition("EQ");
        segment3.setValue("ABC99");
        segment3.setEndOperator("");
        segment3.setLogicBoolOperator("NONE");
        segment3.setOrder("2");
        rawRule.getDefinitions().add(segment3);

        rawRules.add(rawRule);

        List<CustomRuleDto> rules = RulesUtil.parseRules(rawRules);
        assertEquals("(VESSEL_CFR == \"SWE111222\" || VESSEL_CFR == \"SWE111333\") && MOBILE_TERMINAL_Member_id == \"ABC99\"", rules.get(0)
                .getExpression());
        assertEquals("EMAIL,user@company.se;SMS,+46111111111;", rules.get(0)
                .getAction());

    }

}
