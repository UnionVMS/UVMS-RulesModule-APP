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
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubscriptionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubscriptionTypeType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.entity.CustomRule;
import eu.europa.ec.fisheries.uvms.rules.entity.Interval;
import eu.europa.ec.fisheries.uvms.rules.entity.MessageId;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleAction;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleSegment;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleSubscription;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LocalBean
@Stateless
public class CustomRuleMapper {

    private final static Logger LOG = LoggerFactory.getLogger(CustomRuleMapper.class);

    public static CustomRuleType toCustomRuleType(CustomRuleType customRuleType, CustomRule customRuleEntity) throws DaoMappingException {
        try {
            // Base
            customRuleType.setGuid(customRuleEntity.getGuid());
            customRuleType.setName(customRuleEntity.getName());
            if (customRuleEntity.getAvailability() != null) {
                customRuleType.setAvailability(AvailabilityType.fromValue(customRuleEntity.getAvailability()));
            }
            customRuleType.setDescription(customRuleEntity.getDescription());
            customRuleType.setActive(customRuleEntity.getActive());
            customRuleType.setArchived(customRuleEntity.getArchived());
            customRuleType.setLastTriggered(DateUtils.dateToString(customRuleEntity.getTriggered()));
            customRuleType.setUpdated(DateUtils.dateToString(customRuleEntity.getUpdated()));
            customRuleType.setUpdatedBy(customRuleEntity.getUpdatedBy());
            customRuleType.setOrganisation(customRuleEntity.getOrganisation());

            // Rule segments
            List<RuleSegment> ruleSegments = customRuleEntity.getRuleSegmentList();
            for (RuleSegment ruleSegment : ruleSegments) {
                CustomRuleSegmentType customRuleSegmentType = new CustomRuleSegmentType();
                customRuleSegmentType.setStartOperator(ruleSegment.getStartOperator());
                if (ruleSegment.getCriteria() != null) {
                    customRuleSegmentType.setCriteria(CriteriaType.fromValue(ruleSegment.getCriteria()));
                }
                if (ruleSegment.getSubCriteria() != null) {
                    customRuleSegmentType.setSubCriteria(SubCriteriaType.fromValue(ruleSegment.getSubCriteria()));
                }
                if (ruleSegment.getCondition() != null) {
                    customRuleSegmentType.setCondition(ConditionType.fromValue(ruleSegment.getCondition()));
                }
                customRuleSegmentType.setValue(ruleSegment.getValue());
                customRuleSegmentType.setEndOperator(ruleSegment.getEndOperator());
                if (ruleSegment.getLogicOperator() != null) {
                    customRuleSegmentType.setLogicBoolOperator(LogicOperatorType.fromValue(ruleSegment.getLogicOperator()));
                }
                customRuleSegmentType.setOrder(ruleSegment.getOrder().toString());

                customRuleType.getDefinitions().add(customRuleSegmentType);
            }

            // Rule actions
            List<RuleAction> ruleActions = customRuleEntity.getRuleActionList();
            for (RuleAction ruleAction : ruleActions) {
                CustomRuleActionType customRuleActionType = new CustomRuleActionType();
                if (ruleAction.getAction() != null) {
                    customRuleActionType.setAction(ActionType.fromValue(ruleAction.getAction()));
                }
                customRuleActionType.setValue(ruleAction.getValue());
                customRuleActionType.setOrder(ruleAction.getOrder().toString());

                customRuleType.getActions().add(customRuleActionType);
            }

            // Intervals
            List<Interval> intervals = customRuleEntity.getIntervals();
            for (Interval interval : intervals) {
                CustomRuleIntervalType customRuleIntervalType = new CustomRuleIntervalType();
                customRuleIntervalType.setStart(DateUtils.dateToString(interval.getStart()));
                customRuleIntervalType.setEnd(DateUtils.dateToString(interval.getEnd()));

                customRuleType.getTimeIntervals().add(customRuleIntervalType);
            }

            // Rule subscriptions
            List<RuleSubscription> ruleSubscriptions = customRuleEntity.getRuleSubscriptionList();
            for (RuleSubscription ruleSubscription : ruleSubscriptions) {
                SubscriptionType subscriptionType = new SubscriptionType();
                subscriptionType.setOwner(ruleSubscription.getOwner());
                if (ruleSubscription.getType() != null) {
                    subscriptionType.setType(SubscriptionTypeType.fromValue(ruleSubscription.getType()));
                }
                customRuleType.getSubscriptions().add(subscriptionType);
            }

            return customRuleType;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to custom rule model. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to custom rule model. ]", e);
        }
    }

    public static CustomRule toCustomRuleEntity(CustomRule customRuleEntity, CustomRuleType customRuleType) throws DaoMappingException {
        try {
            Date now = DateUtils.nowUTC().toGregorianCalendar().getTime();

            // Base
            customRuleEntity.setName(customRuleType.getName());
            customRuleEntity.setAvailability(customRuleType.getAvailability().value());
            customRuleEntity.setDescription(customRuleType.getDescription());
            customRuleEntity.setActive(customRuleType.isActive());
            customRuleEntity.setArchived(customRuleType.isArchived());
            customRuleEntity.setStartDate(now);
            customRuleEntity.setTriggered(DateUtils.stringToDate(customRuleType.getLastTriggered()));
            customRuleEntity.setUpdated(now);
            customRuleEntity.setUpdatedBy(customRuleType.getUpdatedBy());
            customRuleEntity.setOrganisation(customRuleType.getOrganisation());

            // Rule segments
            List<CustomRuleSegmentType> ruleSegmentTypes = customRuleType.getDefinitions();
            List<RuleSegment> ruleSegmentEntities = new ArrayList<>();
            for (CustomRuleSegmentType customRuleSegmentType : ruleSegmentTypes) {
                RuleSegment ruleSegment = new RuleSegment();
                if (customRuleSegmentType != null) {
                    ruleSegment.setStartOperator(customRuleSegmentType.getStartOperator());
                    if (customRuleSegmentType.getCriteria() != null) {
                        ruleSegment.setCriteria(customRuleSegmentType.getCriteria().name());
                    }
                    if (customRuleSegmentType.getSubCriteria() != null) {
                        ruleSegment.setSubCriteria(customRuleSegmentType.getSubCriteria().name());
                    }
                    if (customRuleSegmentType.getCondition() != null) {
                        ruleSegment.setCondition(customRuleSegmentType.getCondition().name());
                    }
                    // Remove quotations from the value, since it totally messes up the rule engine
                    ruleSegment.setValue(customRuleSegmentType.getValue().replace("\"",""));
                    ruleSegment.setEndOperator(customRuleSegmentType.getEndOperator());
                    if (customRuleSegmentType.getLogicBoolOperator() != null) {
                        ruleSegment.setLogicOperator(customRuleSegmentType.getLogicBoolOperator().name());
                    }
                    ruleSegment.setOrder(Integer.valueOf(customRuleSegmentType.getOrder()));
                    ruleSegment.setCustomRule(customRuleEntity);
                }
                ruleSegmentEntities.add(ruleSegment);
            }
            customRuleEntity.getRuleSegmentList().addAll(ruleSegmentEntities);

            // Rule actions
            List<CustomRuleActionType> ruleActionTypes = customRuleType.getActions();
            List<RuleAction> ruleActionEntities = new ArrayList<>();
            for (CustomRuleActionType customRuleActionType : ruleActionTypes) {
                RuleAction ruleAction = new RuleAction();
                if (customRuleActionType != null) {
                    ruleAction.setAction(customRuleActionType.getAction().value());
                    ruleAction.setValue(customRuleActionType.getValue());
                    ruleAction.setOrder(Integer.valueOf(customRuleActionType.getOrder()));
                    ruleAction.setCustomRule(customRuleEntity);
                }
                ruleActionEntities.add(ruleAction);
            }
            customRuleEntity.getRuleActionList().addAll(ruleActionEntities);

            // Intervals
            List<CustomRuleIntervalType> customRuleIntervalTypes = customRuleType.getTimeIntervals();
            List<Interval> ruleActiveIntervalEntities = new ArrayList<>();
            for (CustomRuleIntervalType customRuleActionType : customRuleIntervalTypes) {
                Interval ruleActiveInterval = new Interval();
                if (customRuleActionType != null) {
                    ruleActiveInterval.setStart(DateUtils.stringToDate(customRuleActionType.getStart()));
                    ruleActiveInterval.setEnd(DateUtils.stringToDate(customRuleActionType.getEnd()));
                    ruleActiveInterval.setCustomRule(customRuleEntity);
                }
                ruleActiveIntervalEntities.add(ruleActiveInterval);
            }
            customRuleEntity.getIntervals().addAll(ruleActiveIntervalEntities);

//            // NOTE: Don't map subscriptions in this method

            return customRuleEntity;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to custom rule entity. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to custom rule entity. ]", e);
        }
    }

    public static CustomRule toCustomRuleEntity(CustomRuleType customRuleType) throws DaoMappingException {
        CustomRule customRuleEntity = new CustomRule();
        return toCustomRuleEntity(customRuleEntity, customRuleType);
    }

    public static CustomRuleType toCustomRuleType(CustomRule customRuleEntity) throws DaoMappingException {
        CustomRuleType customRuleType = new CustomRuleType();
        return toCustomRuleType(customRuleType, customRuleEntity);
    }

    public static String mapMessageIdToString(MessageId messageId) {
        return messageId.getMessageId();
    }
}