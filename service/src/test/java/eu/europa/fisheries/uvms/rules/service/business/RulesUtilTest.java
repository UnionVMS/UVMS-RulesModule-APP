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

import eu.europa.ec.fisheries.schema.rules.customrule.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ConditionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CriteriaType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleIntervalType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleSegmentType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.LogicOperatorType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubCriteriaType;
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
        action1.setAction(ActionType.EMAIL);
        action1.setValue("user@company.se");
        rawRule.getActions().add(action1);

        // Second action
        CustomRuleActionType action2 = new CustomRuleActionType();
        action2.setAction(ActionType.SMS);
        action2.setValue("+46111111111");
        rawRule.getActions().add(action2);

        // First part of rule
        CustomRuleSegmentType segment1 = new CustomRuleSegmentType();
        segment1.setStartOperator("(");

        segment1.setCriteria(CriteriaType.VESSEL);
        segment1.setSubCriteria(SubCriteriaType.VESSEL_CFR);
        segment1.setCondition(ConditionType.EQ);
        segment1.setValue("SWE111111");
        segment1.setEndOperator("");
        segment1.setLogicBoolOperator(LogicOperatorType.OR);
        segment1.setOrder("0");
        rawRule.getDefinitions().add(segment1);

        // Second part of rule
        CustomRuleSegmentType segment2 = new CustomRuleSegmentType();
        segment2.setStartOperator("");
        segment2.setCriteria(CriteriaType.VESSEL);
        segment2.setSubCriteria(SubCriteriaType.VESSEL_CFR);
        segment2.setCondition(ConditionType.EQ);
        segment2.setValue("SWE222222");
        segment2.setEndOperator(")");
        segment2.setLogicBoolOperator(LogicOperatorType.AND);
        segment2.setOrder("1");
        rawRule.getDefinitions().add(segment2);

        // Third part of rule
        CustomRuleSegmentType segment3 = new CustomRuleSegmentType();
        segment3.setStartOperator("");
        segment3.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment3.setSubCriteria(SubCriteriaType.MT_MEMBER_ID);
        segment3.setCondition(ConditionType.EQ);
        segment3.setValue("ABC99");
        segment3.setEndOperator("");
        segment3.setLogicBoolOperator(LogicOperatorType.NONE);
        segment3.setOrder("2");
        rawRule.getDefinitions().add(segment3);

        // First interval
        CustomRuleIntervalType interval1 = new CustomRuleIntervalType();
        interval1.setStart("2000-10-01 02:00:00 +0200");
        interval1.setEnd("2000-10-30 01:00:00 +0100");
        rawRule.getTimeIntervals().add(interval1);

        // First interval
        CustomRuleIntervalType interval2 = new CustomRuleIntervalType();
        interval2.setStart("2015-01-01 01:00:00 +0100");
        interval2.setEnd("2016-12-31 01:00:00 +0100");
        rawRule.getTimeIntervals().add(interval2);

        rawRules.add(rawRule);

        String expectedRule =
                "(vesselCfr == \"SWE111111\" || vesselCfr == \"SWE222222\") && mobileTerminalMemberNumber == \"ABC99\" && (RulesUtil.stringToDate(\"2000-10-01 02:00:00 +0200\") <= positionTime && positionTime <= RulesUtil.stringToDate(\"2000-10-30 01:00:00 +0100\") || RulesUtil.stringToDate(\"2015-01-01 01:00:00 +0100\") <= positionTime && positionTime <= RulesUtil.stringToDate(\"2016-12-31 01:00:00 +0100\"))";
        // String expectedRule =
        // "(vesselCfr == \"SWE111222\" || vesselCfr == \"SWE111333\") && mobileTerminalMemberNumber == \"ABC99\"";

        List<CustomRuleDto> rules = RulesUtil.parseRules(rawRules);
        assertEquals(expectedRule, rules.get(0)
                .getExpression());
        assertEquals("EMAIL,user@company.se;SMS,+46111111111;", rules.get(0).getAction());

    }

}
