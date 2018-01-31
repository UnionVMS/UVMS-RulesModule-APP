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
package eu.europa.ec.fisheries.uvms.rules.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ConditionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CriteriaType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleIntervalType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleSegmentType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.LogicOperatorType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubCriteriaType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.MockData;
import eu.europa.ec.fisheries.uvms.rules.entity.CustomRule;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapperTest {

    @InjectMocks
    private CustomRuleMapper mapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEntityToModel() throws DaoException, DaoMappingException {
        Integer id = 1;
        CustomRule entity = MockData.getCustomRuleEntity(id);

        CustomRuleType result = mapper.toCustomRuleType(entity);

        // Base
        assertSame(entity.getName(), result.getName());
        assertSame(entity.getAvailability(), result.getAvailability().value());
        assertSame(entity.getDescription(), result.getDescription());
        assertSame(entity.getGuid(), result.getGuid());
        assertSame(entity.getName(), result.getName());
        assertEquals(DateUtils.dateToString(entity.getTriggered()), result.getLastTriggered());
        assertEquals(DateUtils.dateToString(entity.getUpdated()), result.getUpdated());
        assertSame(entity.getUpdatedBy(), result.getUpdatedBy());

        // Rule segments
        List<CustomRuleSegmentType> segments = result.getDefinitions();
        assertSame(3, segments.size());

        for (CustomRuleSegmentType segment : segments) {
            switch (segment.getOrder()) {
            case "0":
                assertSame("(", segment.getStartOperator());
                assertSame(CriteriaType.ASSET, segment.getCriteria());
                assertSame(SubCriteriaType.ASSET_CFR, segment.getSubCriteria());
                assertSame(ConditionType.EQ, segment.getCondition());
                assertSame("SWE111222", segment.getValue());
                assertSame("", segment.getEndOperator());
                assertSame(LogicOperatorType.OR, segment.getLogicBoolOperator());
                break;
            case "1":
                assertSame("", segment.getStartOperator());
                assertSame(CriteriaType.ASSET, segment.getCriteria());
                assertSame(SubCriteriaType.ASSET_CFR, segment.getSubCriteria());
                assertSame(ConditionType.EQ, segment.getCondition());
                assertSame("SWE111333", segment.getValue());
                assertSame(")", segment.getEndOperator());
                assertSame(LogicOperatorType.AND, segment.getLogicBoolOperator());
                break;
            case "2":
                assertSame("", segment.getStartOperator());
                assertSame(CriteriaType.MOBILE_TERMINAL, segment.getCriteria());
                assertSame(SubCriteriaType.MT_MEMBER_ID, segment.getSubCriteria());
                assertSame(ConditionType.EQ, segment.getCondition());
                assertSame("ABC99", segment.getValue());
                assertSame("", segment.getEndOperator());
                assertSame(LogicOperatorType.NONE, segment.getLogicBoolOperator());
                break;
            default:
                Assert.fail();
                break;
            }

        }

        // Intervals
        List<CustomRuleIntervalType> intervals = result.getTimeIntervals();
        assertSame(2, intervals.size());

        assertEquals(DateUtils.dateToString(entity.getIntervals().get(0).getStart()), intervals.get(0).getStart());
        assertEquals(DateUtils.dateToString(entity.getIntervals().get(0).getEnd()), intervals.get(0).getEnd());

        assertEquals(DateUtils.dateToString(entity.getIntervals().get(1).getStart()), intervals.get(1).getStart());
        assertEquals(DateUtils.dateToString(entity.getIntervals().get(1).getEnd()), intervals.get(1).getEnd());

        // Actions
        List<CustomRuleActionType> actions = result.getActions();
        assertSame(2, actions.size());

        for (CustomRuleActionType action : actions) {
            switch (action.getOrder()) {
            case "0":
                assertSame(ActionType.SEND_TO_FLUX, action.getAction());
                assertSame("value1", action.getValue());
                break;
            case "1":
                assertSame(ActionType.EMAIL, action.getAction());
                assertSame("value2", action.getValue());
                break;
            case "2":
                assertSame(ActionType.SEND_TO_NAF, action.getAction());
                assertSame("value3", action.getValue());
                break;
            default:
                Assert.fail();
                break;
            }
        }

    }

    @Test
    public void testModelToEntity() throws DaoException, DaoMappingException {
        Integer id = 1;
        CustomRuleType model = MockData.getModel(id);

        CustomRule result = mapper.toCustomRuleEntity(model);

        assertSame(model.getName(), result.getName());
        assertSame(model.getAvailability().value(), result.getAvailability());
        assertSame(model.getDescription(), result.getDescription());
        assertSame(model.getName(), result.getName());
        assertEquals(DateUtils.stringToDate(model.getLastTriggered()), result.getTriggered());
        assertSame(model.getUpdatedBy(), result.getUpdatedBy());

        // TODO:
        // Rule segments
        // Intervals
        // Actions

    }

    // TODO:
    @Test
    public void testEntityAndModelToEntity() throws DaoException, DaoMappingException {
        int id = 1;
        CustomRule entity = MockData.getCustomRuleEntity(id);
        CustomRuleType model = MockData.getModel(1);

        CustomRule result = mapper.toCustomRuleEntity(entity, model);

        assertSame(result.getName(), model.getName());
    }

    // TODO:
    @Test
    public void testEntityAndModelToModel() throws DaoException, DaoMappingException {
        int id = 1;
        CustomRule entity = MockData.getCustomRuleEntity(id);
        CustomRuleType model = MockData.getModel(id);

        CustomRuleType result = mapper.toCustomRuleType(model, entity);

        assertSame(result.getName(), entity.getName());
    }
}