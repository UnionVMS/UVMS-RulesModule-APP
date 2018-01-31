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
package eu.europa.ec.fisheries.uvms.rules;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.AvailabilityType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ConditionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CriteriaType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleIntervalType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleSegmentType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.LogicOperatorType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubCriteriaType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.entity.CustomRule;
import eu.europa.ec.fisheries.uvms.rules.entity.Interval;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleAction;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleSegment;

public class MockData {

    private static final Date A_DATE = new Date(1441065600000l);
    private static final String ACTION_SEND_TO_ENDPOINT = "SEND_TO_FLUX";
    private static final String ACTION_EMAIL = "EMAIL";
    private static final String GUID = "DummyGuid";
    private static final String RULE_NAME = "DummyRuleName";
    private static final String AVAILABILITY = "PUBLIC";
    private static final String DESCRIPTION = "DummyDesc";

    public static CustomRuleType getModel(int id) {
        CustomRuleType dto = new CustomRuleType();
        // Base
        dto.setGuid(GUID + id);
        dto.setName(RULE_NAME + id);
        dto.setAvailability(AvailabilityType.PUBLIC);
        dto.setDescription(DESCRIPTION);
        dto.setActive(true);
        dto.setArchived(false);
        dto.setLastTriggered(DateUtils.dateToString(A_DATE));
        dto.setUpdatedBy("UVMS");

        // Actions
        CustomRuleActionType action1 = new CustomRuleActionType();
        action1.setAction(ActionType.SEND_TO_FLUX);
        action1.setValue("value1");
        action1.setOrder("0");

        CustomRuleActionType action2 = new CustomRuleActionType();
        action2.setAction(ActionType.EMAIL);
        action2.setValue("value2");
        action2.setOrder("1");
        
        CustomRuleActionType action3 = new CustomRuleActionType();
        action3.setAction(ActionType.SEND_TO_NAF);
        action3.setValue("value3");
        action3.setOrder("0");

        dto.getActions().add(action1);
        dto.getActions().add(action2);
        dto.getActions().add(action3);

        // Rule segments
        CustomRuleSegmentType segment1 = new CustomRuleSegmentType();
        segment1.setStartOperator("(");
        segment1.setCriteria(CriteriaType.ASSET);
        segment1.setSubCriteria(SubCriteriaType.ASSET_CFR);
        segment1.setCondition(ConditionType.EQ);
        segment1.setValue("SWE111222");
        segment1.setEndOperator("");
        segment1.setLogicBoolOperator(LogicOperatorType.OR);
        segment1.setOrder("0");

        CustomRuleSegmentType segment2 = new CustomRuleSegmentType();
        segment2.setStartOperator("");
        segment2.setCriteria(CriteriaType.ASSET);
        segment2.setSubCriteria(SubCriteriaType.ASSET_CFR);
        segment2.setCondition(ConditionType.EQ);
        segment2.setValue("SWE111333");
        segment2.setEndOperator(")");
        segment2.setLogicBoolOperator(LogicOperatorType.AND);
        segment2.setOrder("1");

        CustomRuleSegmentType segment3 = new CustomRuleSegmentType();
        segment3.setStartOperator("");
        segment3.setCriteria(CriteriaType.MOBILE_TERMINAL);
        segment3.setSubCriteria(SubCriteriaType.MT_MEMBER_ID);
        segment3.setCondition(ConditionType.EQ);
        segment3.setValue("ABC99");
        segment3.setEndOperator("");
        segment3.setLogicBoolOperator(LogicOperatorType.NONE);
        segment3.setOrder("2");

        dto.getDefinitions().add(segment1);
        dto.getDefinitions().add(segment2);
        dto.getDefinitions().add(segment3);

        // Intervals
        CustomRuleIntervalType interval1 = new CustomRuleIntervalType();
        interval1.setStart(DateUtils.dateToString(A_DATE));
        interval1.setEnd(DateUtils.dateToString(A_DATE));

        CustomRuleIntervalType interval2 = new CustomRuleIntervalType();
        interval2.setStart(DateUtils.dateToString(A_DATE));
        interval2.setEnd(DateUtils.dateToString(A_DATE));

        dto.getTimeIntervals().add(interval1);
        dto.getTimeIntervals().add(interval2);

        return dto;
    }

    public static CustomRule getCustomRuleEntity(int id) {
        CustomRule entity = new CustomRule();
        // Base
        entity.setGuid(GUID + id);
        entity.setName(RULE_NAME + id);
        entity.setAvailability(AVAILABILITY);
        entity.setDescription(DESCRIPTION);
        entity.setActive(true);
        entity.setArchived(false);
        entity.setTriggered(A_DATE);
        entity.setUpdated(A_DATE);
        entity.setUpdatedBy("UVMS");

        // Actions
        List<RuleAction> ruleActionList = new ArrayList<>();
        RuleAction action1 = new RuleAction();
        action1.setAction(ACTION_SEND_TO_ENDPOINT);
        action1.setOrder(0);
        action1.setValue("value1");

        RuleAction action2 = new RuleAction();
        action2.setAction(ACTION_EMAIL);
        action2.setOrder(1);
        action2.setValue("value2");

        ruleActionList.add(action1);
        ruleActionList.add(action2);
        entity.setRuleActionList(ruleActionList);

        // Rule segments
        List<RuleSegment> segments = entity.getRuleSegmentList();
        RuleSegment segment1 = new RuleSegment();
        segment1.setStartOperator("(");
        segment1.setCriteria(CriteriaType.ASSET.name());
        segment1.setSubCriteria(SubCriteriaType.ASSET_CFR.name());
        segment1.setCondition("EQ");
        segment1.setValue("SWE111222");
        segment1.setEndOperator("");
        segment1.setLogicOperator("OR");
        segment1.setOrder(0);
        segment1.setCustomRule(entity);

        RuleSegment segment2 = new RuleSegment();
        segment2.setStartOperator("");
        segment2.setCriteria(CriteriaType.ASSET.name());
        segment2.setSubCriteria(SubCriteriaType.ASSET_CFR.name());
        segment2.setCondition("EQ");
        segment2.setValue("SWE111333");
        segment2.setEndOperator(")");
        segment2.setLogicOperator("AND");
        segment2.setOrder(1);
        segment2.setCustomRule(entity);

        RuleSegment segment3 = new RuleSegment();
        segment3.setStartOperator("");
        segment3.setCriteria(CriteriaType.MOBILE_TERMINAL.name());
        segment3.setSubCriteria(SubCriteriaType.MT_MEMBER_ID.name());
        segment3.setCondition("EQ");
        segment3.setValue("ABC99");
        segment3.setEndOperator("");
        segment3.setLogicOperator("NONE");
        segment3.setOrder(2);
        segment3.setCustomRule(entity);

        segments.add(segment1);
        segments.add(segment2);
        segments.add(segment3);
        entity.setRuleSegmentList(segments);

        // Intervals
        List<Interval> intervals = entity.getIntervals();
        Interval interval1 = new Interval();
        interval1.setStart(A_DATE);
        interval1.setEnd(A_DATE);
        interval1.setCustomRule(entity);

        Interval interval2 = new Interval();
        interval2.setStart(A_DATE);
        interval2.setEnd(A_DATE);
        interval2.setCustomRule(entity);

        intervals.add(interval1);
        intervals.add(interval2);
        entity.setIntervals(intervals);

        return entity;
    }
}