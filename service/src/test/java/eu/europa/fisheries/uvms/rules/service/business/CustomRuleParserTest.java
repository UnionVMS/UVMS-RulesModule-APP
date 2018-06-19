/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
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
        customRule.setAvailability(AvailabilityType.PRIVATE);

        // First part of rule
        CustomRuleSegmentType segment1 = new CustomRuleSegmentType();
        segment1.setStartOperator("(");
        segment1.setCriteria(CriteriaType.ASSET);
        segment1.setSubCriteria(SubCriteriaType.ASSET_CFR);
        segment1.setCondition(ConditionType.EQ);
        segment1.setValue("\"SWE111111\"");  // Test that quotation is removed
        segment1.setEndOperator("");
        segment1.setLogicBoolOperator(LogicOperatorType.OR);
        segment1.setOrder("0");
        customRule.getDefinitions().add(segment1);

        // Second part of rule
        CustomRuleSegmentType segment2 = new CustomRuleSegmentType();
        segment2.setStartOperator("");
        segment2.setCriteria(CriteriaType.ASSET);
        segment2.setSubCriteria(SubCriteriaType.ASSET_CFR);
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

        // First action
        CustomRuleActionType action1 = new CustomRuleActionType();
        action1.setAction(ActionType.EMAIL);
        action1.setValue("user@company.se");
        customRule.getActions().add(action1);

        // Second action
        CustomRuleActionType action2 = new CustomRuleActionType();
        action2.setAction(ActionType.SEND_TO_FLUX);
        action2.setValue("DNK");
        customRule.getActions().add(action2);

        // Third action
        CustomRuleActionType action3 = new CustomRuleActionType();
        action3.setAction(ActionType.SEND_TO_NAF);
        action3.setValue("DNK");
        customRule.getActions().add(action3);

        rawRules.add(customRule);

        String expectedRule =
                "(cfr == \"SWE111111\" || cfr == \"SWE222222\") && mobileTerminalMemberNumber == \"ABC99\" && (RulesUtil.stringToDate(\"2000-10-01 02:00:00 +0200\") <= positionTime && positionTime <= RulesUtil.stringToDate(\"2000-10-30 01:00:00 +0100\") || RulesUtil.stringToDate(\"2015-01-01 01:00:00 +0100\") <= positionTime && positionTime <= RulesUtil.stringToDate(\"2016-12-31 01:00:00 +0100\"))";

        List<CustomRuleDto> rules = CustomRuleParser.parseRules(rawRules);
        assertEquals(expectedRule, rules.get(0)
                .getExpression());
        assertEquals("EMAIL,user@company.se;SEND_TO_FLUX,DNK;SEND_TO_NAF,DNK;", rules.get(0).getAction());

    }

    @Test
    public void testAllCriterias() throws Exception {
        List<CustomRuleType> rawRules = new ArrayList<CustomRuleType>();

        CustomRuleType customRule = new CustomRuleType();
        customRule.setName("DummyName");
        customRule.setAvailability(AvailabilityType.PRIVATE);

        // ACTIVITY_CALLBACK
        CustomRuleSegmentType segment0 = new CustomRuleSegmentType();
        segment0.setStartOperator("");
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
        segment3.setValue("DNK");
        segment3.setEndOperator("");
        segment3.setLogicBoolOperator(LogicOperatorType.OR);
        segment3.setOrder("3");
        customRule.getDefinitions().add(segment3);

        // AREA_TYPE
        CustomRuleSegmentType segment4 = new CustomRuleSegmentType();
        segment4.setStartOperator("");
        segment4.setCriteria(CriteriaType.AREA);
        segment4.setSubCriteria(SubCriteriaType.AREA_TYPE);
        segment4.setCondition(ConditionType.NE);
        segment4.setValue("EEZ");
        segment4.setEndOperator("");
        segment4.setLogicBoolOperator(LogicOperatorType.OR);
        segment4.setOrder("4");
        customRule.getDefinitions().add(segment4);

        // ASSET_ID_GEAR_TYPE
        CustomRuleSegmentType segment5 = new CustomRuleSegmentType();
        segment5.setStartOperator("");
        segment5.setCriteria(CriteriaType.ASSET);
        segment5.setSubCriteria(SubCriteriaType.ASSET_ID_GEAR_TYPE);
        segment5.setCondition(ConditionType.EQ);
        segment5.setValue("ASSET_ID_GEAR_TYPE");
        segment5.setEndOperator("");
        segment5.setLogicBoolOperator(LogicOperatorType.OR);
        segment5.setOrder("5");
        customRule.getDefinitions().add(segment5);

        // EXTERNAL_MARKING
        CustomRuleSegmentType segment6 = new CustomRuleSegmentType();
        segment6.setStartOperator("");
        segment6.setCriteria(CriteriaType.ASSET);
        segment6.setSubCriteria(SubCriteriaType.EXTERNAL_MARKING);
        segment6.setCondition(ConditionType.EQ);
        segment6.setValue("GE-49");
        segment6.setEndOperator("");
        segment6.setLogicBoolOperator(LogicOperatorType.OR);
        segment6.setOrder("6");
        customRule.getDefinitions().add(segment6);

        // FLAG_STATE
        CustomRuleSegmentType segment7 = new CustomRuleSegmentType();
        segment7.setStartOperator("");
        segment7.setCriteria(CriteriaType.ASSET);
        segment7.setSubCriteria(SubCriteriaType.FLAG_STATE);
        segment7.setCondition(ConditionType.EQ);
        segment7.setValue("SWE");
        segment7.setEndOperator("");
        segment7.setLogicBoolOperator(LogicOperatorType.OR);
        segment7.setOrder("7");
        customRule.getDefinitions().add(segment7);

        // ASSET_CFR
        CustomRuleSegmentType segment8 = new CustomRuleSegmentType();
        segment8.setStartOperator("");
        segment8.setCriteria(CriteriaType.ASSET);
        segment8.setSubCriteria(SubCriteriaType.ASSET_CFR);
        segment8.setCondition(ConditionType.EQ);
        segment8.setValue("GBR000A11447");
        segment8.setEndOperator("");
        segment8.setLogicBoolOperator(LogicOperatorType.OR);
        segment8.setOrder("8");
        customRule.getDefinitions().add(segment8);

        // ASSET_IRCS
        CustomRuleSegmentType segment9 = new CustomRuleSegmentType();
        segment9.setStartOperator("");
        segment9.setCriteria(CriteriaType.ASSET);
        segment9.setSubCriteria(SubCriteriaType.ASSET_IRCS);
        segment9.setCondition(ConditionType.EQ);
        segment9.setValue("SECT");
        segment9.setEndOperator("");
        segment9.setLogicBoolOperator(LogicOperatorType.OR);
        segment9.setOrder("9");
        customRule.getDefinitions().add(segment9);

        // ASSET_NAME
        CustomRuleSegmentType segment10 = new CustomRuleSegmentType();
        segment10.setStartOperator("");
        segment10.setCriteria(CriteriaType.ASSET);
        segment10.setSubCriteria(SubCriteriaType.ASSET_NAME);
        segment10.setCondition(ConditionType.EQ);
        segment10.setValue("SETTE MARI");
        segment10.setEndOperator("");
        segment10.setLogicBoolOperator(LogicOperatorType.OR);
        segment10.setOrder("10");
        customRule.getDefinitions().add(segment10);

        // COMCHANNEL_TYPE
        CustomRuleSegmentType segment11 = new CustomRuleSegmentType();
        segment11.setStartOperator("");
        segment11.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment11.setSubCriteria(SubCriteriaType.COMCHANNEL_TYPE);
        segment11.setCondition(ConditionType.EQ);
        segment11.setValue("VMS");
        segment11.setEndOperator("");
        segment11.setLogicBoolOperator(LogicOperatorType.OR);
        segment11.setOrder("11");
        customRule.getDefinitions().add(segment11);

        // MT_TYPE
        CustomRuleSegmentType segment12 = new CustomRuleSegmentType();
        segment12.setStartOperator("");
        segment12.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment12.setSubCriteria(SubCriteriaType.MT_TYPE);
        segment12.setCondition(ConditionType.EQ);
        segment12.setValue("MT_TYPE");
        segment12.setEndOperator("");
        segment12.setLogicBoolOperator(LogicOperatorType.OR);
        segment12.setOrder("12");
        customRule.getDefinitions().add(segment12);

        // MT_DNID
        CustomRuleSegmentType segment13 = new CustomRuleSegmentType();
        segment13.setStartOperator("");
        segment13.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment13.setSubCriteria(SubCriteriaType.MT_DNID);
        segment13.setCondition(ConditionType.EQ);
        segment13.setValue("MT_DNID");
        segment13.setEndOperator("");
        segment13.setLogicBoolOperator(LogicOperatorType.OR);
        segment13.setOrder("13");
        customRule.getDefinitions().add(segment13);

        // MT_MEMBER_ID
        CustomRuleSegmentType segment14 = new CustomRuleSegmentType();
        segment14.setStartOperator("");
        segment14.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment14.setSubCriteria(SubCriteriaType.MT_MEMBER_ID);
        segment14.setCondition(ConditionType.EQ);
        segment14.setValue("MT_MEMBER_ID");
        segment14.setEndOperator("");
        segment14.setLogicBoolOperator(LogicOperatorType.OR);
        segment14.setOrder("14");
        customRule.getDefinitions().add(segment14);

        // MT_SERIAL_NO
        CustomRuleSegmentType segment15 = new CustomRuleSegmentType();
        segment15.setStartOperator("");
        segment15.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment15.setSubCriteria(SubCriteriaType.MT_SERIAL_NO);
        segment15.setCondition(ConditionType.EQ);
        segment15.setValue("MT_SERIAL_NO");
        segment15.setEndOperator("");
        segment15.setLogicBoolOperator(LogicOperatorType.OR);
        segment15.setOrder("15");
        customRule.getDefinitions().add(segment15);

        // ALTITUDE
        CustomRuleSegmentType segment16 = new CustomRuleSegmentType();
        segment16.setStartOperator("");
        segment16.setCriteria(CriteriaType.POSITION);
        segment16.setSubCriteria(SubCriteriaType.ALTITUDE);
        segment16.setCondition(ConditionType.EQ);
        segment16.setValue("0");
        segment16.setEndOperator("");
        segment16.setLogicBoolOperator(LogicOperatorType.OR);
        segment16.setOrder("16");
        customRule.getDefinitions().add(segment16);

        // LATITUDE
        CustomRuleSegmentType segment17 = new CustomRuleSegmentType();
        segment17.setStartOperator("");
        segment17.setCriteria(CriteriaType.POSITION);
        segment17.setSubCriteria(SubCriteriaType.LATITUDE);
        segment17.setCondition(ConditionType.EQ);
        segment17.setValue("57");
        segment17.setEndOperator("");
        segment17.setLogicBoolOperator(LogicOperatorType.OR);
        segment17.setOrder("17");
        customRule.getDefinitions().add(segment17);

        // LONGITUDE
        CustomRuleSegmentType segment18 = new CustomRuleSegmentType();
        segment18.setStartOperator("");
        segment18.setCriteria(CriteriaType.POSITION);
        segment18.setSubCriteria(SubCriteriaType.LONGITUDE);
        segment18.setCondition(ConditionType.EQ);
        segment18.setValue("11");
        segment18.setEndOperator("");
        segment18.setLogicBoolOperator(LogicOperatorType.OR);
        segment18.setOrder("18");
        customRule.getDefinitions().add(segment18);

        // CALCULATED_COURSE
        CustomRuleSegmentType segment19 = new CustomRuleSegmentType();
        segment19.setStartOperator("");
        segment19.setCriteria(CriteriaType.POSITION);
        segment19.setSubCriteria(SubCriteriaType.CALCULATED_COURSE);
        segment19.setCondition(ConditionType.LE);
        segment19.setValue("46");
        segment19.setEndOperator("");
        segment19.setLogicBoolOperator(LogicOperatorType.OR);
        segment19.setOrder("19");
        customRule.getDefinitions().add(segment19);

        // CALCULATED_SPEED
        CustomRuleSegmentType segment20 = new CustomRuleSegmentType();
        segment20.setStartOperator("");
        segment20.setCriteria(CriteriaType.POSITION);
        segment20.setSubCriteria(SubCriteriaType.CALCULATED_SPEED);
        segment20.setCondition(ConditionType.GE);
        segment20.setValue("10.1");
        segment20.setEndOperator("");
        segment20.setLogicBoolOperator(LogicOperatorType.OR);
        segment20.setOrder("20");
        customRule.getDefinitions().add(segment20);

        // MOVEMENT_TYPE
        CustomRuleSegmentType segment21 = new CustomRuleSegmentType();
        segment21.setStartOperator("");
        segment21.setCriteria(CriteriaType.POSITION);
        segment21.setSubCriteria(SubCriteriaType.MOVEMENT_TYPE);
        segment21.setCondition(ConditionType.EQ);
        segment21.setValue("POS");
        segment21.setEndOperator("");
        segment21.setLogicBoolOperator(LogicOperatorType.OR);
        segment21.setOrder("21");
        customRule.getDefinitions().add(segment21);

        // POSITION_REPORT_TIME
        CustomRuleSegmentType segment22 = new CustomRuleSegmentType();
        segment22.setStartOperator("");
        segment22.setCriteria(CriteriaType.POSITION);
        segment22.setSubCriteria(SubCriteriaType.POSITION_REPORT_TIME);
        segment22.setCondition(ConditionType.EQ);
        segment22.setValue("2000-10-30 01:00:00 +0100");
        segment22.setEndOperator("");
        segment22.setLogicBoolOperator(LogicOperatorType.OR);
        segment22.setOrder("22");
        customRule.getDefinitions().add(segment22);

        // REPORTED_COURSE
        CustomRuleSegmentType segment23 = new CustomRuleSegmentType();
        segment23.setStartOperator("");
        segment23.setCriteria(CriteriaType.POSITION);
        segment23.setSubCriteria(SubCriteriaType.REPORTED_COURSE);
        segment23.setCondition(ConditionType.LT);
        segment23.setValue("45");
        segment23.setEndOperator("");
        segment23.setLogicBoolOperator(LogicOperatorType.OR);
        segment23.setOrder("23");
        customRule.getDefinitions().add(segment23);

        // REPORTED_SPEED
        CustomRuleSegmentType segment24 = new CustomRuleSegmentType();
        segment24.setStartOperator("");
        segment24.setCriteria(CriteriaType.POSITION);
        segment24.setSubCriteria(SubCriteriaType.REPORTED_SPEED);
        segment24.setCondition(ConditionType.GT);
        segment24.setValue("10");
        segment24.setEndOperator("");
        segment24.setLogicBoolOperator(LogicOperatorType.OR);
        segment24.setOrder("24");
        customRule.getDefinitions().add(segment24);

        // SEGMENT_TYPE
        CustomRuleSegmentType segment25 = new CustomRuleSegmentType();
        segment25.setStartOperator("");
        segment25.setCriteria(CriteriaType.POSITION);
        segment25.setSubCriteria(SubCriteriaType.SEGMENT_TYPE);
        segment25.setCondition(ConditionType.NE);
        segment25.setValue("GAP");
        segment25.setEndOperator("");
        segment25.setLogicBoolOperator(LogicOperatorType.OR);
        segment25.setOrder("25");
        customRule.getDefinitions().add(segment25);

        // SOURCE
        CustomRuleSegmentType segment26 = new CustomRuleSegmentType();
        segment26.setStartOperator("");
        segment26.setCriteria(CriteriaType.POSITION);
        segment26.setSubCriteria(SubCriteriaType.SOURCE);
        segment26.setCondition(ConditionType.EQ);
        segment26.setValue("INMARSAT_C");
        segment26.setEndOperator("");
        segment26.setLogicBoolOperator(LogicOperatorType.OR);
        segment26.setOrder("26");
        customRule.getDefinitions().add(segment26);

        // STATUS_CODE
        CustomRuleSegmentType segment27 = new CustomRuleSegmentType();
        segment27.setStartOperator("");
        segment27.setCriteria(CriteriaType.POSITION);
        segment27.setSubCriteria(SubCriteriaType.STATUS_CODE);
        segment27.setCondition(ConditionType.EQ);
        segment27.setValue("010");
        segment27.setEndOperator("");
        segment27.setLogicBoolOperator(LogicOperatorType.OR);
        segment27.setOrder("27");
        customRule.getDefinitions().add(segment27);

        // CLOSEST_COUNTRY_CODE
        CustomRuleSegmentType segment28 = new CustomRuleSegmentType();
        segment28.setStartOperator("");
        segment28.setCriteria(CriteriaType.POSITION);
        segment28.setSubCriteria(SubCriteriaType.CLOSEST_COUNTRY_CODE);
        segment28.setCondition(ConditionType.EQ);
        segment28.setValue("DNK");
        segment28.setEndOperator("");
        segment28.setLogicBoolOperator(LogicOperatorType.OR);
        segment28.setOrder("28");
        customRule.getDefinitions().add(segment28);

        // CLOSEST_PORT_CODE
        CustomRuleSegmentType segment29 = new CustomRuleSegmentType();
        segment29.setStartOperator("");
        segment29.setCriteria(CriteriaType.POSITION);
        segment29.setSubCriteria(SubCriteriaType.CLOSEST_PORT_CODE);
        segment29.setCondition(ConditionType.EQ);
        segment29.setValue("CLOSEST_PORT_CODE");
        segment29.setEndOperator("");
        segment29.setLogicBoolOperator(LogicOperatorType.OR);
        segment29.setOrder("29");
        customRule.getDefinitions().add(segment29);

        // ASSET_GROUP
        CustomRuleSegmentType segment30 = new CustomRuleSegmentType();
        segment30.setStartOperator("");
        segment30.setCriteria(CriteriaType.ASSET_GROUP);
        segment30.setSubCriteria(SubCriteriaType.ASSET_GROUP);
        segment30.setCondition(ConditionType.NE);
        segment30.setValue("ASSET_GROUP");
        segment30.setEndOperator("");
        segment30.setLogicBoolOperator(LogicOperatorType.OR);
        segment30.setOrder("30");
        customRule.getDefinitions().add(segment30);

        // REPORT
        CustomRuleSegmentType segment31 = new CustomRuleSegmentType();
        segment31.setStartOperator("");
        segment31.setCriteria(CriteriaType.REPORT);
        segment31.setSubCriteria(SubCriteriaType.SUM_POSITION_REPORT);
        segment31.setCondition(ConditionType.GT);
        segment31.setValue("10");
        segment31.setEndOperator("");
        segment31.setLogicBoolOperator(LogicOperatorType.OR);
        segment31.setOrder("31");
        customRule.getDefinitions().add(segment31);

        // REPORT
        CustomRuleSegmentType segment32 = new CustomRuleSegmentType();
        segment32.setStartOperator("");
        segment32.setCriteria(CriteriaType.REPORT);
        segment32.setSubCriteria(SubCriteriaType.TIME_DIFF_POSITION_REPORT);
        segment32.setCondition(ConditionType.LT);
        segment32.setValue("60");
        segment32.setEndOperator("");
        segment32.setLogicBoolOperator(LogicOperatorType.OR);
        segment32.setOrder("32");
        customRule.getDefinitions().add(segment32);


        // AREA_CODE_ENT
        CustomRuleSegmentType segment33 = new CustomRuleSegmentType();
        segment33.setStartOperator("");
        segment33.setCriteria(CriteriaType.AREA);
        segment33.setSubCriteria(SubCriteriaType.AREA_CODE_ENT);
        segment33.setCondition(ConditionType.EQ);
        segment33.setValue("DNK");
        segment33.setEndOperator("");
        segment33.setLogicBoolOperator(LogicOperatorType.OR);
        segment33.setOrder("33");
        customRule.getDefinitions().add(segment33);

        // AREA_TYPE_ENT
        CustomRuleSegmentType segment34 = new CustomRuleSegmentType();
        segment34.setStartOperator("");
        segment34.setCriteria(CriteriaType.AREA);
        segment34.setSubCriteria(SubCriteriaType.AREA_TYPE_ENT);
        segment34.setCondition(ConditionType.NE);
        segment34.setValue("EEZ");
        segment34.setEndOperator("");
        segment34.setLogicBoolOperator(LogicOperatorType.OR);
        segment34.setOrder("34");
        customRule.getDefinitions().add(segment34);

        // AREA_CODE_EXT
        CustomRuleSegmentType segment35 = new CustomRuleSegmentType();
        segment35.setStartOperator("");
        segment35.setCriteria(CriteriaType.AREA);
        segment35.setSubCriteria(SubCriteriaType.AREA_CODE_EXT);
        segment35.setCondition(ConditionType.EQ);
        segment35.setValue("SWE");
        segment35.setEndOperator("");
        segment35.setLogicBoolOperator(LogicOperatorType.OR);
        segment35.setOrder("35");
        customRule.getDefinitions().add(segment35);

        // AREA_TYPE_EXT
        CustomRuleSegmentType segment36 = new CustomRuleSegmentType();
        segment36.setStartOperator("");
        segment36.setCriteria(CriteriaType.AREA);
        segment36.setSubCriteria(SubCriteriaType.AREA_TYPE_EXT);
        segment36.setCondition(ConditionType.NE);
        segment36.setValue("EEZ");
        segment36.setEndOperator("");
        segment36.setLogicBoolOperator(LogicOperatorType.OR);
        segment36.setOrder("36");
        customRule.getDefinitions().add(segment36);

        // ASSET_STATUS
        CustomRuleSegmentType segment37 = new CustomRuleSegmentType();
        segment37.setStartOperator("");
        segment37.setCriteria(CriteriaType.ASSET);
        segment37.setSubCriteria(SubCriteriaType.ASSET_STATUS);
        segment37.setCondition(ConditionType.EQ);
        segment37.setValue("ACTIVE");
        segment37.setEndOperator("");
        segment37.setLogicBoolOperator(LogicOperatorType.OR);
        segment37.setOrder("37");
        customRule.getDefinitions().add(segment37);

        // MT_STATUS
        CustomRuleSegmentType segment38 = new CustomRuleSegmentType();
        segment38.setStartOperator("");
        segment38.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment38.setSubCriteria(SubCriteriaType.MT_STATUS);
        segment38.setCondition(ConditionType.NE);
        segment38.setValue("INACTIVE");
        segment38.setEndOperator("");
        segment38.setLogicBoolOperator(LogicOperatorType.NONE);
        segment38.setOrder("38");
        customRule.getDefinitions().add(segment38);



        // VICINITY_OF
//        CustomRuleSegmentType segment33 = new CustomRuleSegmentType();
//        segment33.setStartOperator("");
//        segment33.setCriteria(CriteriaType.POSITION);
//        segment33.setSubCriteria(SubCriteriaType.VICINITY_OF);
//        segment33.setCondition(ConditionType.EQ);
//        segment33.setValue("VICINITY_OF");
//        segment33.setEndOperator(")");
//        segment33.setLogicBoolOperator(LogicOperatorType.NONE);
//        segment33.setOrder("33");
//        customRule.getDefinitions().add(segment33);

        // Action
        CustomRuleActionType action = new CustomRuleActionType();
        action.setAction(ActionType.SEND_TO_FLUX);
        action.setValue("DNK");
        action.setOrder("0");
        customRule.getActions().add(action);

        rawRules.add(customRule);

        StringBuilder sb = new StringBuilder();
        sb.append("activityCallback == \"ACTIVITY_CALLBACK\" || ");
        sb.append("activityMessageId == \"ACTIVITY_MESSAGE_ID\" || ");
        sb.append("activityMessageType == \"ACTIVITY_MESSAGE_TYPE\" || ");
        sb.append("areaCodes.contains(\"DNK\") || ");
        sb.append("!areaTypes.contains(\"EEZ\") || ");
        sb.append("assetIdGearType == \"ASSET_ID_GEAR_TYPE\" || ");
        sb.append("externalMarking == \"GE-49\" || ");
        sb.append("flagState == \"SWE\" || ");
        sb.append("cfr == \"GBR000A11447\" || ");
        sb.append("ircs == \"SECT\" || ");
        sb.append("assetName == \"SETTE MARI\" || ");
        sb.append("comChannelType == \"VMS\" || ");
        sb.append("mobileTerminalType == \"MT_TYPE\" || ");
        sb.append("mobileTerminalDnid == \"MT_DNID\" || ");
        sb.append("mobileTerminalMemberNumber == \"MT_MEMBER_ID\" || ");
        sb.append("mobileTerminalSerialNumber == \"MT_SERIAL_NO\" || ");
        sb.append("altitude == \"0\" || ");
        sb.append("latitude == \"57\" || ");
        sb.append("longitude == \"11\" || ");
        sb.append("calculatedCourse <= \"46\" || ");
        sb.append("calculatedSpeed >= \"10.1\" || ");
        sb.append("movementType == \"POS\" || ");
        sb.append("positionTime == RulesUtil.stringToDate(\"2000-10-30 01:00:00 +0100\") || ");
        sb.append("reportedCourse < \"45\" || ");
        sb.append("reportedSpeed > \"10\" || ");
        sb.append("segmentType != \"GAP\" || ");
        sb.append("source == \"INMARSAT_C\" || ");
        sb.append("statusCode == \"010\" || ");
        sb.append("closestCountryCode == \"DNK\" || ");
        sb.append("closestPortCode == \"CLOSEST_PORT_CODE\" || ");
        sb.append("!assetGroups.contains(\"ASSET_GROUP\") || ");
        sb.append("sumPositionReport > \"10\" || ");
        sb.append("timeDiffPositionReport < \"60\" || ");
        sb.append("entAreaCodes.contains(\"DNK\") || ");
        sb.append("!entAreaTypes.contains(\"EEZ\") || ");
        sb.append("extAreaCodes.contains(\"SWE\") || ");
        sb.append("!extAreaTypes.contains(\"EEZ\") || ");
        sb.append("assetStatus == \"ACTIVE\" || ");
        sb.append("mobileTerminalStatus != \"INACTIVE\"");
//        sb.append("vicinityOf == \"VICINITY_OF\"");

        String expectedRule = sb.toString();

        List<CustomRuleDto> rules = CustomRuleParser.parseRules(rawRules);
        assertEquals(expectedRule, rules.get(0).getExpression());
        assertEquals("SEND_TO_FLUX,DNK;", rules.get(0).getAction());

    }

    @Test
    public void testNullStartAndEndOperator() throws Exception {
        List<CustomRuleType> rawRules = new ArrayList<CustomRuleType>();

        CustomRuleType customRule = new CustomRuleType();
        customRule.setName("DummyName");
        customRule.setAvailability(AvailabilityType.PRIVATE);

        // ACTIVITY_CALLBACK
        CustomRuleSegmentType segment0 = new CustomRuleSegmentType();
        segment0.setCriteria(CriteriaType.ACTIVITY);
        segment0.setSubCriteria(SubCriteriaType.ACTIVITY_CALLBACK);
        segment0.setCondition(ConditionType.EQ);
        segment0.setValue("ACTIVITY_CALLBACK");
        segment0.setLogicBoolOperator(LogicOperatorType.OR);
        segment0.setOrder("0");
        customRule.getDefinitions().add(segment0);

        // Action
        CustomRuleActionType action = new CustomRuleActionType();
        action.setAction(ActionType.SEND_TO_FLUX);
        action.setValue("DNK");
        action.setOrder("0");
        customRule.getActions().add(action);

        rawRules.add(customRule);

        StringBuilder sb = new StringBuilder();
        sb.append("activityCallback == \"ACTIVITY_CALLBACK\" || ");

        String expectedRule = sb.toString();

        List<CustomRuleDto> rules = CustomRuleParser.parseRules(rawRules);
        assertEquals(expectedRule, rules.get(0).getExpression());
        assertEquals("SEND_TO_FLUX,DNK;", rules.get(0).getAction());

    }
    
    @Test
    public void testParseRulesUnorderedSegments() throws Exception {
        List<CustomRuleType> rawRules = new ArrayList<CustomRuleType>();

        CustomRuleType customRule = new CustomRuleType();
        customRule.setName("Name");
        customRule.setAvailability(AvailabilityType.PRIVATE);

        // Second part of rule
        CustomRuleSegmentType segment2 = new CustomRuleSegmentType();
        segment2.setStartOperator("");
        segment2.setCriteria(CriteriaType.ASSET);
        segment2.setSubCriteria(SubCriteriaType.ASSET_CFR);
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

        // First part of rule
        CustomRuleSegmentType segment1 = new CustomRuleSegmentType();
        segment1.setStartOperator("(");
        segment1.setCriteria(CriteriaType.ASSET);
        segment1.setSubCriteria(SubCriteriaType.ASSET_CFR);
        segment1.setCondition(ConditionType.EQ);
        segment1.setValue("\"SWE111111\"");  // Test that quotation is removed
        segment1.setEndOperator("");
        segment1.setLogicBoolOperator(LogicOperatorType.OR);
        segment1.setOrder("0");
        customRule.getDefinitions().add(segment1);

        rawRules.add(customRule);

        String expectedRule = "(cfr == \"SWE111111\" || cfr == \"SWE222222\") && mobileTerminalMemberNumber == \"ABC99\"";

        List<CustomRuleDto> rules = CustomRuleParser.parseRules(rawRules);
        assertEquals(expectedRule, rules.get(0).getExpression());
    }

}