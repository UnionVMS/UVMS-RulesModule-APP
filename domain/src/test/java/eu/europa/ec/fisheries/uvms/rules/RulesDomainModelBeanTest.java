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

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleIntervalType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleSegmentType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.bean.RulesDomainModelBean;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.CustomRule;
import eu.europa.ec.fisheries.uvms.rules.entity.Interval;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleAction;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleSegment;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.modelmock.ModelException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RulesDomainModelBeanTest {

    @Mock
    RulesDao dao;

    @InjectMocks
    private RulesDomainModelBean model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateModel() throws ModelException, DaoException, DaoMappingException, RulesModelException {
        Integer id = 1;

        CustomRuleType customRuleType = MockData.getModel(id.intValue());

        CustomRule entity = MockData.getCustomRuleEntity(id);

        when(dao.createCustomRule(any(CustomRule.class))).thenReturn(entity);

        CustomRuleType result = model.createCustomRule(customRuleType);
        assertEquals(entity.getName(), result.getName());

        assertEquals(entity.getActive(), result.isActive());
        assertEquals(entity.getAvailability(), result.getAvailability().name());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getUpdatedBy(), result.getUpdatedBy());

        List<RuleAction> actions = entity.getRuleActionList();
        for (RuleAction action : actions) {
            switch (action.getOrder()) {
                case 0:
                    assertEquals(action.getValue(), result.getActions().get(0).getValue());
                    break;
                case 1:
                    assertEquals(action.getValue(), result.getActions().get(1).getValue());
                    break;
                default:
                    Assert.fail();
                    break;
            }
        }

        List<RuleSegment> segments = entity.getRuleSegmentList();
        for (RuleSegment segment : segments) {
            switch (segment.getOrder()) {
                case 0:
                    assertEquals(segment.getStartOperator(), result.getDefinitions().get(0).getStartOperator());
                    assertEquals(segment.getCriteria(), result.getDefinitions().get(0).getCriteria().name());
                    assertEquals(segment.getSubCriteria(), result.getDefinitions().get(0).getSubCriteria().name());
                    assertEquals(segment.getCondition(), result.getDefinitions().get(0).getCondition().name());
                    assertEquals(segment.getValue(), result.getDefinitions().get(0).getValue());
                    assertEquals(segment.getEndOperator(), result.getDefinitions().get(0).getEndOperator());
                    assertEquals(segment.getLogicOperator(), result.getDefinitions().get(0).getLogicBoolOperator().name());
                    break;
                case 1:
                    assertEquals(segment.getStartOperator(), result.getDefinitions().get(1).getStartOperator());
                    assertEquals(segment.getCriteria(), result.getDefinitions().get(1).getCriteria().name());
                    assertEquals(segment.getSubCriteria(), result.getDefinitions().get(1).getSubCriteria().name());
                    assertEquals(segment.getCondition(), result.getDefinitions().get(1).getCondition().name());
                    assertEquals(segment.getValue(), result.getDefinitions().get(1).getValue());
                    assertEquals(segment.getEndOperator(), result.getDefinitions().get(1).getEndOperator());
                    assertEquals(segment.getLogicOperator(), result.getDefinitions().get(1).getLogicBoolOperator().name());
                    break;
                case 2:
                    assertEquals(segment.getStartOperator(), result.getDefinitions().get(2).getStartOperator());
                    assertEquals(segment.getCriteria(), result.getDefinitions().get(2).getCriteria().name());
                    assertEquals(segment.getSubCriteria(), result.getDefinitions().get(2).getSubCriteria().name());
                    assertEquals(segment.getCondition(), result.getDefinitions().get(2).getCondition().name());
                    assertEquals(segment.getValue(), result.getDefinitions().get(2).getValue());
                    assertEquals(segment.getEndOperator(), result.getDefinitions().get(2).getEndOperator());
                    assertEquals(segment.getLogicOperator(), result.getDefinitions().get(2).getLogicBoolOperator().name());
                    break;
                default:
                    Assert.fail();
                    break;
            }

        }

        List<Interval> intervals = entity.getIntervals();
        for (int i = 0; i < intervals.size(); i++) {
            assertEquals(DateUtils.dateToString(intervals.get(i).getStart()), result.getTimeIntervals().get(i).getEnd());
            assertEquals(DateUtils.dateToString(intervals.get(i).getEnd()), result.getTimeIntervals().get(i).getStart());
        }

    }

    @Test
    public void testGetCustomRuleList() throws ModelException, DaoException, DaoMappingException, RulesModelException {
        Integer id = 1;

        List<CustomRule> customRuleList = new ArrayList<>();
        CustomRule rule = MockData.getCustomRuleEntity(id);
        customRuleList.add(rule);
        when(dao.getCustomRulesByUser("dummyUser")).thenReturn(customRuleList);

        List<CustomRuleType> responseList = model.getCustomRulesByUser("dummyUser");
        assertSame(customRuleList.size(), responseList.size());

        assertSame(customRuleList.size(), responseList.size());

        CustomRuleType resultRule = responseList.get(0);
        assertEquals(rule.getGuid(), resultRule.getGuid());
        assertEquals(rule.getDescription(), resultRule.getDescription());
        assertEquals(rule.getTriggered(), DateUtils.stringToDate(resultRule.getLastTriggered()));
        assertEquals(rule.getName(), resultRule.getName());
        assertEquals(rule.getUpdated(), DateUtils.stringToDate(resultRule.getUpdated()));
        assertEquals(rule.getUpdatedBy(), resultRule.getUpdatedBy());
        assertEquals(rule.getAvailability(), resultRule.getAvailability().name());

        for (CustomRuleActionType action : resultRule.getActions()) {
            switch (action.getOrder()) {
                case "0":
                    assertEquals(rule.getRuleActionList().get(0).getAction(), action.getAction().name());
                    assertEquals(rule.getRuleActionList().get(0).getValue(), action.getValue());
                    break;
                case "1":
                    assertEquals(rule.getRuleActionList().get(1).getAction(), action.getAction().name());
                    assertEquals(rule.getRuleActionList().get(1).getValue(), action.getValue());
                    break;
                default:
                    Assert.fail();
                    break;

            }
        }

        for (CustomRuleSegmentType definition : resultRule.getDefinitions()) {
            switch (definition.getOrder()) {
                case "0":
                    assertEquals(rule.getRuleSegmentList().get(0).getStartOperator(), definition.getStartOperator());
                    assertEquals(rule.getRuleSegmentList().get(0).getCriteria(), definition.getCriteria().name());
                    assertEquals(rule.getRuleSegmentList().get(0).getSubCriteria(), definition.getSubCriteria().name());
                    assertEquals(rule.getRuleSegmentList().get(0).getCondition(), definition.getCondition().name());
                    assertEquals(rule.getRuleSegmentList().get(0).getValue(), definition.getValue());
                    assertEquals(rule.getRuleSegmentList().get(0).getEndOperator(), definition.getEndOperator());
                    assertEquals(rule.getRuleSegmentList().get(0).getLogicOperator(), definition.getLogicBoolOperator().name());
                    break;
                case "1":
                    assertEquals(rule.getRuleSegmentList().get(1).getStartOperator(), definition.getStartOperator());
                    assertEquals(rule.getRuleSegmentList().get(1).getCriteria(), definition.getCriteria().name());
                    assertEquals(rule.getRuleSegmentList().get(1).getSubCriteria(), definition.getSubCriteria().name());
                    assertEquals(rule.getRuleSegmentList().get(1).getCondition(), definition.getCondition().name());
                    assertEquals(rule.getRuleSegmentList().get(1).getValue(), definition.getValue());
                    assertEquals(rule.getRuleSegmentList().get(1).getEndOperator(), definition.getEndOperator());
                    assertEquals(rule.getRuleSegmentList().get(1).getLogicOperator(), definition.getLogicBoolOperator().name());
                    break;
                case "2":
                    assertEquals(rule.getRuleSegmentList().get(2).getStartOperator(), definition.getStartOperator());
                    assertEquals(rule.getRuleSegmentList().get(2).getCriteria(), definition.getCriteria().name());
                    assertEquals(rule.getRuleSegmentList().get(2).getSubCriteria(), definition.getSubCriteria().name());
                    assertEquals(rule.getRuleSegmentList().get(2).getCondition(), definition.getCondition().name());
                    assertEquals(rule.getRuleSegmentList().get(2).getValue(), definition.getValue());
                    assertEquals(rule.getRuleSegmentList().get(2).getEndOperator(), definition.getEndOperator());
                    assertEquals(rule.getRuleSegmentList().get(2).getLogicOperator(), definition.getLogicBoolOperator().name());
                    break;
                default:
                    Assert.fail();
                    break;
            }

        }

        List<CustomRuleIntervalType> intervals = resultRule.getTimeIntervals();
        assertEquals(rule.getIntervals().size(), intervals.size());
        for (int i = 0; i < intervals.size(); i++) {
            assertEquals(rule.getIntervals().get(i).getStart(), DateUtils.stringToDate(intervals.get(i).getStart()));
            assertEquals(rule.getIntervals().get(i).getEnd(), DateUtils.stringToDate(intervals.get(i).getEnd()));
        }

    }

}