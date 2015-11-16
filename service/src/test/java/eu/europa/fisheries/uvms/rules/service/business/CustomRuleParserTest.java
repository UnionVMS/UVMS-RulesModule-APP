package eu.europa.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.CustomRuleDto;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomRuleParser;
import org.junit.*;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomRuleParserTest {
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

        CustomRuleType customRule = new CustomRuleType();
        customRule.setName("DummyName");

        // First action
        CustomRuleActionType action1 = new CustomRuleActionType();
        action1.setAction(ActionType.EMAIL);
        action1.setValue("user@company.se");
        customRule.getActions().add(action1);

        // Second action
        CustomRuleActionType action2 = new CustomRuleActionType();
        action2.setAction(ActionType.SMS);
        action2.setValue("+46111111111");
        customRule.getActions().add(action2);

        // First part of rule
        CustomRuleSegmentType segment1 = new CustomRuleSegmentType();
        segment1.setStartOperator("(");
        segment1.setCriteria(CriteriaType.ASSET);
        segment1.setSubCriteria(SubCriteriaType.VESSEL_CFR);
        segment1.setCondition(ConditionType.EQ);
        segment1.setValue("SWE111111");
        segment1.setEndOperator("");
        segment1.setLogicBoolOperator(LogicOperatorType.OR);
        segment1.setOrder("0");
        customRule.getDefinitions().add(segment1);

        // Second part of rule
        CustomRuleSegmentType segment2 = new CustomRuleSegmentType();
        segment2.setStartOperator("");
        segment2.setCriteria(CriteriaType.ASSET);
        segment2.setSubCriteria(SubCriteriaType.VESSEL_CFR);
        segment2.setCondition(ConditionType.EQ);
        segment2.setValue("SWE222222");
        segment2.setEndOperator(")");
        segment2.setLogicBoolOperator(LogicOperatorType.AND);
        segment2.setOrder("1");
        customRule.getDefinitions().add(segment2);

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
        customRule.getDefinitions().add(segment3);

        // First interval
        CustomRuleIntervalType interval1 = new CustomRuleIntervalType();
        interval1.setStart("2000-10-01 02:00:00 +0200");
        interval1.setEnd("2000-10-30 01:00:00 +0100");
        customRule.getTimeIntervals().add(interval1);

        // First interval
        CustomRuleIntervalType interval2 = new CustomRuleIntervalType();
        interval2.setStart("2015-01-01 01:00:00 +0100");
        interval2.setEnd("2016-12-31 01:00:00 +0100");
        customRule.getTimeIntervals().add(interval2);

        rawRules.add(customRule);

        String expectedRule =
                "(vesselCfr == \"SWE111111\" || vesselCfr == \"SWE222222\") && mobileTerminalMemberNumber == \"ABC99\" && (RulesUtil.stringToDate(\"2000-10-01 02:00:00 +0200\") <= positionTime && positionTime <= RulesUtil.stringToDate(\"2000-10-30 01:00:00 +0100\") || RulesUtil.stringToDate(\"2015-01-01 01:00:00 +0100\") <= positionTime && positionTime <= RulesUtil.stringToDate(\"2016-12-31 01:00:00 +0100\"))";
        // String expectedRule =
        // "(vesselCfr == \"SWE111222\" || vesselCfr == \"SWE111333\") && mobileTerminalMemberNumber == \"ABC99\"";

        List<CustomRuleDto> rules = CustomRuleParser.parseRules(rawRules);
        assertEquals(expectedRule, rules.get(0)
                .getExpression());
        assertEquals("EMAIL,user@company.se;SMS,+46111111111;", rules.get(0).getAction());

    }

    @Test
    public void testAllCriterias() throws Exception {
        List<CustomRuleType> rawRules = new ArrayList<CustomRuleType>();

        CustomRuleType customRule = new CustomRuleType();
        customRule.setName("DummyName");

        // ACTIVITY_CALLBACK
        CustomRuleSegmentType segment0 = new CustomRuleSegmentType();
        segment0.setStartOperator("(");
        segment0.setCriteria(CriteriaType.ACTIVITY);
        segment0.setSubCriteria(SubCriteriaType.ACTIVITY_CALLBACK);
        segment0.setCondition(ConditionType.EQ);
        segment0.setValue("ACTIVITY_CALLBACK");
        segment0.setEndOperator("");
        segment0.setLogicBoolOperator(LogicOperatorType.OR);
        segment0.setOrder("0");
        customRule.getDefinitions().add(segment0);

        // ACTIVITY_MESSAGE_ID
        CustomRuleSegmentType segment1 = new CustomRuleSegmentType();
        segment1.setStartOperator("");
        segment1.setCriteria(CriteriaType.ACTIVITY);
        segment1.setSubCriteria(SubCriteriaType.ACTIVITY_MESSAGE_ID);
        segment1.setCondition(ConditionType.EQ);
        segment1.setValue("ACTIVITY_MESSAGE_ID");
        segment1.setEndOperator("");
        segment1.setLogicBoolOperator(LogicOperatorType.OR);
        segment1.setOrder("1");
        customRule.getDefinitions().add(segment1);

        // ACTIVITY_MESSAGE_TYPE
        CustomRuleSegmentType segment2 = new CustomRuleSegmentType();
        segment2.setStartOperator("");
        segment2.setCriteria(CriteriaType.ACTIVITY);
        segment2.setSubCriteria(SubCriteriaType.ACTIVITY_MESSAGE_TYPE);
        segment2.setCondition(ConditionType.EQ);
        segment2.setValue("ACTIVITY_MESSAGE_TYPE");
        segment2.setEndOperator("");
        segment2.setLogicBoolOperator(LogicOperatorType.OR);
        segment2.setOrder("2");
        customRule.getDefinitions().add(segment2);

        // AREA_CODE
        CustomRuleSegmentType segment3 = new CustomRuleSegmentType();
        segment3.setStartOperator("");
        segment3.setCriteria(CriteriaType.AREA);
        segment3.setSubCriteria(SubCriteriaType.AREA_CODE);
        segment3.setCondition(ConditionType.EQ);
        segment3.setValue("AREA_CODE");
        segment3.setEndOperator("");
        segment3.setLogicBoolOperator(LogicOperatorType.OR);
        segment3.setOrder("3");
        customRule.getDefinitions().add(segment3);

        // AREA_NAME
        CustomRuleSegmentType segment4 = new CustomRuleSegmentType();
        segment4.setStartOperator("");
        segment4.setCriteria(CriteriaType.AREA);
        segment4.setSubCriteria(SubCriteriaType.AREA_NAME);
        segment4.setCondition(ConditionType.NE);
        segment4.setValue("AREA_NAME");
        segment4.setEndOperator("");
        segment4.setLogicBoolOperator(LogicOperatorType.OR);
        segment4.setOrder("4");
        customRule.getDefinitions().add(segment4);

        // AREA_TYPE
        CustomRuleSegmentType segment5 = new CustomRuleSegmentType();
        segment5.setStartOperator("");
        segment5.setCriteria(CriteriaType.AREA);
        segment5.setSubCriteria(SubCriteriaType.AREA_TYPE);
        segment5.setCondition(ConditionType.NE);
        segment5.setValue("AREA_TYPE");
        segment5.setEndOperator("");
        segment5.setLogicBoolOperator(LogicOperatorType.OR);
        segment5.setOrder("5");
        customRule.getDefinitions().add(segment5);

        // AREA_ID
        CustomRuleSegmentType segment6 = new CustomRuleSegmentType();
        segment6.setStartOperator("");
        segment6.setCriteria(CriteriaType.AREA);
        segment6.setSubCriteria(SubCriteriaType.AREA_ID);
        segment6.setCondition(ConditionType.NE);
        segment6.setValue("AREA_ID");
        segment6.setEndOperator("");
        segment6.setLogicBoolOperator(LogicOperatorType.OR);
        segment6.setOrder("6");
        customRule.getDefinitions().add(segment6);

        // ASSET_ID_GEAR_TYPE
        CustomRuleSegmentType segment7 = new CustomRuleSegmentType();
        segment7.setStartOperator("");
        segment7.setCriteria(CriteriaType.ASSET);
        segment7.setSubCriteria(SubCriteriaType.ASSET_ID_GEAR_TYPE);
        segment7.setCondition(ConditionType.EQ);
        segment7.setValue("ASSET_ID_GEAR_TYPE");
        segment7.setEndOperator("");
        segment7.setLogicBoolOperator(LogicOperatorType.OR);
        segment7.setOrder("7");
        customRule.getDefinitions().add(segment7);

        // EXTERNAL_MARKING
        CustomRuleSegmentType segment8 = new CustomRuleSegmentType();
        segment8.setStartOperator("");
        segment8.setCriteria(CriteriaType.ASSET);
        segment8.setSubCriteria(SubCriteriaType.EXTERNAL_MARKING);
        segment8.setCondition(ConditionType.EQ);
        segment8.setValue("EXTERNAL_MARKING");
        segment8.setEndOperator("");
        segment8.setLogicBoolOperator(LogicOperatorType.OR);
        segment8.setOrder("8");
        customRule.getDefinitions().add(segment8);

        // FLAG_STATE
        CustomRuleSegmentType segment9 = new CustomRuleSegmentType();
        segment9.setStartOperator("");
        segment9.setCriteria(CriteriaType.ASSET);
        segment9.setSubCriteria(SubCriteriaType.FLAG_STATE);
        segment9.setCondition(ConditionType.EQ);
        segment9.setValue("FLAG_STATE");
        segment9.setEndOperator("");
        segment9.setLogicBoolOperator(LogicOperatorType.OR);
        segment9.setOrder("9");
        customRule.getDefinitions().add(segment9);

        // VESSEL_CFR
        CustomRuleSegmentType segment10 = new CustomRuleSegmentType();
        segment10.setStartOperator("");
        segment10.setCriteria(CriteriaType.ASSET);
        segment10.setSubCriteria(SubCriteriaType.VESSEL_CFR);
        segment10.setCondition(ConditionType.EQ);
        segment10.setValue("VESSEL_CFR");
        segment10.setEndOperator("");
        segment10.setLogicBoolOperator(LogicOperatorType.OR);
        segment10.setOrder("10");
        customRule.getDefinitions().add(segment10);

        // VESSEL_IRCS
        CustomRuleSegmentType segment11 = new CustomRuleSegmentType();
        segment11.setStartOperator("");
        segment11.setCriteria(CriteriaType.ASSET);
        segment11.setSubCriteria(SubCriteriaType.VESSEL_IRCS);
        segment11.setCondition(ConditionType.EQ);
        segment11.setValue("VESSEL_IRCS");
        segment11.setEndOperator("");
        segment11.setLogicBoolOperator(LogicOperatorType.OR);
        segment11.setOrder("11");
        customRule.getDefinitions().add(segment11);

        // VESSEL_NAME
        CustomRuleSegmentType segment12 = new CustomRuleSegmentType();
        segment12.setStartOperator("");
        segment12.setCriteria(CriteriaType.ASSET);
        segment12.setSubCriteria(SubCriteriaType.VESSEL_NAME);
        segment12.setCondition(ConditionType.EQ);
        segment12.setValue("VESSEL_NAME");
        segment12.setEndOperator("");
        segment12.setLogicBoolOperator(LogicOperatorType.OR);
        segment12.setOrder("12");
        customRule.getDefinitions().add(segment12);

        // COMCHANNEL_TYPE
        CustomRuleSegmentType segment13 = new CustomRuleSegmentType();
        segment13.setStartOperator("");
        segment13.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment13.setSubCriteria(SubCriteriaType.COMCHANNEL_TYPE);
        segment13.setCondition(ConditionType.EQ);
        segment13.setValue("COMCHANNEL_TYPE");
        segment13.setEndOperator("");
        segment13.setLogicBoolOperator(LogicOperatorType.OR);
        segment13.setOrder("13");
        customRule.getDefinitions().add(segment13);

        // MT_TYPE
        CustomRuleSegmentType segment14 = new CustomRuleSegmentType();
        segment14.setStartOperator("");
        segment14.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment14.setSubCriteria(SubCriteriaType.MT_TYPE);
        segment14.setCondition(ConditionType.EQ);
        segment14.setValue("MT_TYPE");
        segment14.setEndOperator("");
        segment14.setLogicBoolOperator(LogicOperatorType.OR);
        segment14.setOrder("14");
        customRule.getDefinitions().add(segment14);

        // MT_DNID
        CustomRuleSegmentType segment15 = new CustomRuleSegmentType();
        segment15.setStartOperator("");
        segment15.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment15.setSubCriteria(SubCriteriaType.MT_DNID);
        segment15.setCondition(ConditionType.EQ);
        segment15.setValue("MT_DNID");
        segment15.setEndOperator("");
        segment15.setLogicBoolOperator(LogicOperatorType.OR);
        segment15.setOrder("15");
        customRule.getDefinitions().add(segment15);

        // MT_MEMBER_ID
        CustomRuleSegmentType segment16 = new CustomRuleSegmentType();
        segment16.setStartOperator("");
        segment16.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment16.setSubCriteria(SubCriteriaType.MT_MEMBER_ID);
        segment16.setCondition(ConditionType.EQ);
        segment16.setValue("MT_MEMBER_ID");
        segment16.setEndOperator("");
        segment16.setLogicBoolOperator(LogicOperatorType.OR);
        segment16.setOrder("16");
        customRule.getDefinitions().add(segment16);

        // MT_SERIAL_NO
        CustomRuleSegmentType segment17 = new CustomRuleSegmentType();
        segment17.setStartOperator("");
        segment17.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment17.setSubCriteria(SubCriteriaType.MT_SERIAL_NO);
        segment17.setCondition(ConditionType.EQ);
        segment17.setValue("MT_SERIAL_NO");
        segment17.setEndOperator("");
        segment17.setLogicBoolOperator(LogicOperatorType.OR);
        segment17.setOrder("17");
        customRule.getDefinitions().add(segment17);

        // ALTITUDE
        CustomRuleSegmentType segment18 = new CustomRuleSegmentType();
        segment18.setStartOperator("");
        segment18.setCriteria(CriteriaType.POSITION);
        segment18.setSubCriteria(SubCriteriaType.ALTITUDE);
        segment18.setCondition(ConditionType.EQ);
        segment18.setValue("ALTITUDE");
        segment18.setEndOperator("");
        segment18.setLogicBoolOperator(LogicOperatorType.OR);
        segment18.setOrder("18");
        customRule.getDefinitions().add(segment18);

        // LATITUDE
        CustomRuleSegmentType segment19 = new CustomRuleSegmentType();
        segment19.setStartOperator("");
        segment19.setCriteria(CriteriaType.POSITION);
        segment19.setSubCriteria(SubCriteriaType.LATITUDE);
        segment19.setCondition(ConditionType.EQ);
        segment19.setValue("LATITUDE");
        segment19.setEndOperator("");
        segment19.setLogicBoolOperator(LogicOperatorType.OR);
        segment19.setOrder("19");
        customRule.getDefinitions().add(segment19);

        // LONGITUDE
        CustomRuleSegmentType segment20 = new CustomRuleSegmentType();
        segment20.setStartOperator("");
        segment20.setCriteria(CriteriaType.POSITION);
        segment20.setSubCriteria(SubCriteriaType.LONGITUDE);
        segment20.setCondition(ConditionType.EQ);
        segment20.setValue("LONGITUDE");
        segment20.setEndOperator("");
        segment20.setLogicBoolOperator(LogicOperatorType.OR);
        segment20.setOrder("20");
        customRule.getDefinitions().add(segment20);

        // CALCULATED_COURSE
        CustomRuleSegmentType segment21 = new CustomRuleSegmentType();
        segment21.setStartOperator("");
        segment21.setCriteria(CriteriaType.POSITION);
        segment21.setSubCriteria(SubCriteriaType.CALCULATED_COURSE);
        segment21.setCondition(ConditionType.LE);
        segment21.setValue("CALCULATED_COURSE");
        segment21.setEndOperator("");
        segment21.setLogicBoolOperator(LogicOperatorType.OR);
        segment21.setOrder("21");
        customRule.getDefinitions().add(segment21);

        // CALCULATED_SPEED
        CustomRuleSegmentType segment22 = new CustomRuleSegmentType();
        segment22.setStartOperator("");
        segment22.setCriteria(CriteriaType.POSITION);
        segment22.setSubCriteria(SubCriteriaType.CALCULATED_SPEED);
        segment22.setCondition(ConditionType.GE);
        segment22.setValue("CALCULATED_SPEED");
        segment22.setEndOperator("");
        segment22.setLogicBoolOperator(LogicOperatorType.OR);
        segment22.setOrder("22");
        customRule.getDefinitions().add(segment22);

        // MOVEMENT_TYPE
        CustomRuleSegmentType segment23 = new CustomRuleSegmentType();
        segment23.setStartOperator("");
        segment23.setCriteria(CriteriaType.POSITION);
        segment23.setSubCriteria(SubCriteriaType.MOVEMENT_TYPE);
        segment23.setCondition(ConditionType.EQ);
        segment23.setValue("MOVEMENT_TYPE");
        segment23.setEndOperator("");
        segment23.setLogicBoolOperator(LogicOperatorType.OR);
        segment23.setOrder("23");
        customRule.getDefinitions().add(segment23);

        // POSITION_REPORT_TIME
        CustomRuleSegmentType segment24 = new CustomRuleSegmentType();
        segment24.setStartOperator("");
        segment24.setCriteria(CriteriaType.POSITION);
        segment24.setSubCriteria(SubCriteriaType.POSITION_REPORT_TIME);
        segment24.setCondition(ConditionType.EQ);
        segment24.setValue("POSITION_REPORT_TIME");
        segment24.setEndOperator("");
        segment24.setLogicBoolOperator(LogicOperatorType.OR);
        segment24.setOrder("24");
        customRule.getDefinitions().add(segment24);

        // REPORTED_COURSE
        CustomRuleSegmentType segment25 = new CustomRuleSegmentType();
        segment25.setStartOperator("");
        segment25.setCriteria(CriteriaType.POSITION);
        segment25.setSubCriteria(SubCriteriaType.REPORTED_COURSE);
        segment25.setCondition(ConditionType.LT);
        segment25.setValue("REPORTED_COURSE");
        segment25.setEndOperator("");
        segment25.setLogicBoolOperator(LogicOperatorType.OR);
        segment25.setOrder("25");
        customRule.getDefinitions().add(segment25);

        // REPORTED_SPEED
        CustomRuleSegmentType segment26 = new CustomRuleSegmentType();
        segment26.setStartOperator("");
        segment26.setCriteria(CriteriaType.POSITION);
        segment26.setSubCriteria(SubCriteriaType.REPORTED_SPEED);
        segment26.setCondition(ConditionType.GT);
        segment26.setValue("REPORTED_SPEED");
        segment26.setEndOperator("");
        segment26.setLogicBoolOperator(LogicOperatorType.OR);
        segment26.setOrder("26");
        customRule.getDefinitions().add(segment26);

        // SEGMENT_TYPE
        CustomRuleSegmentType segment27 = new CustomRuleSegmentType();
        segment27.setStartOperator("");
        segment27.setCriteria(CriteriaType.POSITION);
        segment27.setSubCriteria(SubCriteriaType.SEGMENT_TYPE);
        segment27.setCondition(ConditionType.NE);
        segment27.setValue("SEGMENT_TYPE");
        segment27.setEndOperator("");
        segment27.setLogicBoolOperator(LogicOperatorType.OR);
        segment27.setOrder("27");
        customRule.getDefinitions().add(segment27);

        // SOURCE
        CustomRuleSegmentType segment28 = new CustomRuleSegmentType();
        segment28.setStartOperator("");
        segment28.setCriteria(CriteriaType.POSITION);
        segment28.setSubCriteria(SubCriteriaType.SOURCE);
        segment28.setCondition(ConditionType.EQ);
        segment28.setValue("SOURCE");
        segment28.setEndOperator("");
        segment28.setLogicBoolOperator(LogicOperatorType.OR);
        segment28.setOrder("28");
        customRule.getDefinitions().add(segment28);

        // STATUS_CODE
        CustomRuleSegmentType segment29 = new CustomRuleSegmentType();
        segment29.setStartOperator("");
        segment29.setCriteria(CriteriaType.POSITION);
        segment29.setSubCriteria(SubCriteriaType.STATUS_CODE);
        segment29.setCondition(ConditionType.EQ);
        segment29.setValue("STATUS_CODE");
        segment29.setEndOperator("");
        segment29.setLogicBoolOperator(LogicOperatorType.OR);
        segment29.setOrder("29");
        customRule.getDefinitions().add(segment29);

        // CLOSEST_COUNTRY_CODE
        CustomRuleSegmentType segment30 = new CustomRuleSegmentType();
        segment30.setStartOperator("");
        segment30.setCriteria(CriteriaType.POSITION);
        segment30.setSubCriteria(SubCriteriaType.CLOSEST_COUNTRY_CODE);
        segment30.setCondition(ConditionType.EQ);
        segment30.setValue("CLOSEST_COUNTRY_CODE");
        segment30.setEndOperator("");
        segment30.setLogicBoolOperator(LogicOperatorType.OR);
        segment30.setOrder("30");
        customRule.getDefinitions().add(segment30);

        // CLOSEST_PORT_CODE
        CustomRuleSegmentType segment31 = new CustomRuleSegmentType();
        segment31.setStartOperator("");
        segment31.setCriteria(CriteriaType.POSITION);
        segment31.setSubCriteria(SubCriteriaType.CLOSEST_PORT_CODE);
        segment31.setCondition(ConditionType.EQ);
        segment31.setValue("CLOSEST_PORT_CODE");
        segment31.setEndOperator("");
        segment31.setLogicBoolOperator(LogicOperatorType.OR);
        segment31.setOrder("31");
        customRule.getDefinitions().add(segment31);

        // ASSET_GROUP
        CustomRuleSegmentType segment32 = new CustomRuleSegmentType();
        segment32.setStartOperator("");
        segment32.setCriteria(CriteriaType.ASSET_GROUP);
        segment32.setSubCriteria(SubCriteriaType.ASSET_GROUP);
        segment32.setCondition(ConditionType.NE);
        segment32.setValue("ASSET_GROUP");
        segment32.setEndOperator("");
        segment32.setLogicBoolOperator(LogicOperatorType.OR);
        segment32.setOrder("32");
        customRule.getDefinitions().add(segment32);

        // VICINITY_OF
        CustomRuleSegmentType segment33 = new CustomRuleSegmentType();
        segment33.setStartOperator("");
        segment33.setCriteria(CriteriaType.POSITION);
        segment33.setSubCriteria(SubCriteriaType.VICINITY_OF);
        segment33.setCondition(ConditionType.EQ);
        segment33.setValue("VICINITY_OF");
        segment33.setEndOperator(")");
        segment33.setLogicBoolOperator(LogicOperatorType.NONE);
        segment33.setOrder("33");
        customRule.getDefinitions().add(segment33);

        // Action
        CustomRuleActionType action = new CustomRuleActionType();
        action.setAction(ActionType.TICKET);
        customRule.getActions().add(action);

        rawRules.add(customRule);

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append("activityCallback == \"ACTIVITY_CALLBACK\" || ");
        sb.append("activityMessageId == \"ACTIVITY_MESSAGE_ID\" || ");
        sb.append("activityMessageType == \"ACTIVITY_MESSAGE_TYPE\" || ");
        sb.append("areaCodes.contains(\"AREA_CODE\") || ");
        sb.append("!areaNames.contains(\"AREA_NAME\") || ");
        sb.append("!areaTypes.contains(\"AREA_TYPE\") || ");
        sb.append("!areaRemoteIds.contains(\"AREA_ID\") || ");
        sb.append("assetIdGearType == \"ASSET_ID_GEAR_TYPE\" || ");
        sb.append("externalMarking == \"EXTERNAL_MARKING\" || ");
        sb.append("flagState == \"FLAG_STATE\" || ");
        sb.append("vesselCfr == \"VESSEL_CFR\" || ");
        sb.append("vesselIrcs == \"VESSEL_IRCS\" || ");
        sb.append("vesselName == \"VESSEL_NAME\" || ");
        sb.append("comChannelType == \"COMCHANNEL_TYPE\" || ");
        sb.append("mobileTerminalType == \"MT_TYPE\" || ");
        sb.append("mobileTerminalDnid == \"MT_DNID\" || ");
        sb.append("mobileTerminalMemberNumber == \"MT_MEMBER_ID\" || ");
        sb.append("mobileTerminalSerialNumber == \"MT_SERIAL_NO\" || ");
        sb.append("altitude == \"ALTITUDE\" || ");
        sb.append("latitude == \"LATITUDE\" || ");
        sb.append("longitude == \"LONGITUDE\" || ");
        sb.append("calculatedCourse <= \"CALCULATED_COURSE\" || ");
        sb.append("calculatedSpeed >= \"CALCULATED_SPEED\" || ");
        sb.append("movementType == \"MOVEMENT_TYPE\" || ");
        sb.append("positionTime == \"POSITION_REPORT_TIME\" || ");
        sb.append("reportedCourse < \"REPORTED_COURSE\" || ");
        sb.append("reportedSpeed > \"REPORTED_SPEED\" || ");
        sb.append("segmentType != \"SEGMENT_TYPE\" || ");
        sb.append("source == \"SOURCE\" || ");
        sb.append("statusCode == \"STATUS_CODE\" || ");
        sb.append("closestCountryCode == \"CLOSEST_COUNTRY_CODE\" || ");
        sb.append("closestPortCode == \"CLOSEST_PORT_CODE\" || ");
        sb.append("!assetGroups.contains(\"ASSET_GROUP\") || ");
        sb.append("vicinityOf == \"VICINITY_OF\"");
        sb.append(")");

        String expectedRule = sb.toString();

        List<CustomRuleDto> rules = CustomRuleParser.parseRules(rawRules);
        assertEquals(expectedRule, rules.get(0).getExpression());
        assertEquals("TICKET,null;", rules.get(0).getAction());

    }

}
