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
package eu.europa.ec.fisheries.uvms.rules.bean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubscriptionTypeType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubscritionOperationType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.UpdateSubscriptionType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMessageType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.AlarmItem;
import eu.europa.ec.fisheries.uvms.rules.entity.AlarmReport;
import eu.europa.ec.fisheries.uvms.rules.entity.CustomRule;
import eu.europa.ec.fisheries.uvms.rules.entity.MessageId;
import eu.europa.ec.fisheries.uvms.rules.entity.PreviousReport;
import eu.europa.ec.fisheries.uvms.rules.entity.RawMessage;
import eu.europa.ec.fisheries.uvms.rules.entity.RawMovement;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleSubscription;
import eu.europa.ec.fisheries.uvms.rules.entity.SanityRule;
import eu.europa.ec.fisheries.uvms.rules.entity.Template;
import eu.europa.ec.fisheries.uvms.rules.entity.Ticket;
import eu.europa.ec.fisheries.uvms.rules.entity.ValidationMessage;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import eu.europa.ec.fisheries.uvms.rules.exception.InputArgumentException;
import eu.europa.ec.fisheries.uvms.rules.mapper.AlarmMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.CustomRuleMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.PreviousReportMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.RawMessageMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.RuleMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.SanityRuleMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.TemplateMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.TicketMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.AlarmSearchFieldMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.AlarmSearchValue;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.CustomRuleSearchFieldMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.CustomRuleSearchValue;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.TicketSearchFieldMapper;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.TicketSearchValue;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.CustomRuleListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class RulesDomainModelBean implements RulesDomainModel {

    private static final Logger LOG = LoggerFactory.getLogger(RulesDomainModelBean.class);

    @EJB
    private RulesDao rulesDao;

    @Override
    public List<TemplateRuleMapDto> getAllFactTemplatesAndRules() throws RulesModelException {
        List<TemplateRuleMapDto> templateRuleList = new ArrayList<>();
        try {
            List<Template> factTemplates = rulesDao.getAllFactTemplates();
            for (Template factTemplate : factTemplates) {
                TemplateType template = TemplateMapper.INSTANCE.mapToFactTemplateType(factTemplate);
                List<RuleType> rules = RuleMapper.INSTANCE.mapToAllFactRuleType(factTemplate.getFactRules());
                List<ExternalRuleType> extRules = RuleMapper.INSTANCE.mapToAllExternalFactRuleType(factTemplate.getExternalFactRules());
                TemplateRuleMapDto templateMap = new TemplateRuleMapDto(template, rules);
                templateMap.setExternalRules(extRules);
                templateRuleList.add(templateMap);
            }
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
        return templateRuleList;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void saveValidationMessages(RawMessageType rawMessageType) throws RulesModelException {
        try {
            RawMessage rawMessage = RawMessageMapper.INSTANCE.mapToRawMessageEntity(rawMessageType);
            if (rawMessage != null && rawMessage.getValidationMessages() != null) {
                for (ValidationMessage validationMessage : rawMessage.getValidationMessages()) {
                    validationMessage.setRawMessage(rawMessage);
                    if (validationMessage.getMessageIds() != null) {
                        for (MessageId messageId : validationMessage.getMessageIds()) {
                            messageId.setValidationMessage(validationMessage);
                        }
                    }
                }
                rulesDao.saveValidationMessages(Collections.singletonList(rawMessage));
            }
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<ValidationMessageType> getValidationMessagesById(List<String> ids) throws RulesModelException {
        try {
            List<ValidationMessage> validationMessages = rulesDao.getValidationMessagesById(ids);
            return RawMessageMapper.INSTANCE.mapToValidationMessageTypes(validationMessages);
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<ValidationMessageType>  getValidationMessagesByRawMsgGuid(String rawMsgGuid, String type) throws RulesModelException {
        try {
            List<ValidationMessage> validationMessages = rulesDao.getValidationMessagesByRawMsgGuid(rawMsgGuid, type);
            return RawMessageMapper.INSTANCE.mapToValidationMessageTypes(validationMessages);
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public CustomRuleType createCustomRule(CustomRuleType customRule) throws RulesModelException {
        LOG.debug("Create in Rules");
        try {
            CustomRule entity = CustomRuleMapper.toCustomRuleEntity(customRule);

            List<RuleSubscription> subscriptionEntities = new ArrayList<>();
            RuleSubscription creatorSubscription = new RuleSubscription();
            creatorSubscription.setCustomRule(entity);
            creatorSubscription.setOwner(customRule.getUpdatedBy());
            creatorSubscription.setType(SubscriptionTypeType.TICKET.value());
            subscriptionEntities.add(creatorSubscription);
            entity.getRuleSubscriptionList().addAll(subscriptionEntities);

            rulesDao.createCustomRule(entity);
            return CustomRuleMapper.toCustomRuleType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when creating CustomRule ] {}", e.getMessage());
            throw new RulesModelException("Error when creating CustomRule", e);
        }
    }

    @Override
    public CustomRuleType getByGuid(String guid) throws RulesModelException {
        try {
            CustomRule entity = rulesDao.getCustomRuleByGuid(guid);
            return CustomRuleMapper.toCustomRuleType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting CustomRule by GUID ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public CustomRuleType updateCustomRule(CustomRuleType customRule) throws RulesModelException {
        LOG.debug("Update custom rule in Rules");

        if (customRule == null) {
            LOG.error("[ERROR] Custom Rule is null, returning Exception ]");
            throw new InputArgumentException("Custom Rule is null", null);
        }

        if (customRule.getGuid() == null) {
            LOG.error("[ERROR] GUID of Custom Rule is null, returning Exception. ]");
            throw new InputArgumentException("GUID of Custom Rule is null", null);
        }

        try {

            CustomRule newEntity = CustomRuleMapper.toCustomRuleEntity(customRule);

            CustomRule oldEntity = rulesDao.getCustomRuleByGuid(customRule.getGuid());

            // Copy last triggered if entities are equal
            if (oldEntity.equals(newEntity)) {
                newEntity.setTriggered(oldEntity.getTriggered());
            }

            // Close old version
            oldEntity.setArchived(true);
            oldEntity.setActive(false);
            oldEntity.setEndDate(DateUtils.nowUTC().toGregorianCalendar().getTime());
            // Copy subscription list (ignore if provided)
            List<RuleSubscription> subscriptions = oldEntity.getRuleSubscriptionList();
            for (RuleSubscription subscription : subscriptions) {
                rulesDao.detachSubscription(subscription);
                newEntity.getRuleSubscriptionList().add(subscription);
                subscription.setCustomRule(newEntity);
            }

            newEntity = rulesDao.createCustomRule(newEntity);
            return CustomRuleMapper.toCustomRuleType(newEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when updating custom rule {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when updating custom rule. ]", e);
        }
    }

    @Override
    public CustomRuleType updateCustomRuleSubscription(UpdateSubscriptionType updateSubscriptionType) throws RulesModelException {
        LOG.debug("Update custom rule subscription in Rules");

        if (updateSubscriptionType == null) {
            LOG.error("[ERROR] Subscription is null, returning Exception ]");
            throw new InputArgumentException("Subscription is null", null);
        }

        if (updateSubscriptionType.getRuleGuid() == null) {
            LOG.error("[ERROR] Custom Rule GUID for Subscription is null, returning Exception. ]");
            throw new InputArgumentException("Custom Rule GUID for Subscription is null", null);
        }

        try {
            CustomRule customRuleEntity = rulesDao.getCustomRuleByGuid(updateSubscriptionType.getRuleGuid());

            if (SubscritionOperationType.ADD.equals(updateSubscriptionType.getOperation())) {
                RuleSubscription ruleSubscription = new RuleSubscription();
                ruleSubscription.setOwner(updateSubscriptionType.getSubscription().getOwner());
                if (updateSubscriptionType.getSubscription().getType() != null) {
                    ruleSubscription.setType(updateSubscriptionType.getSubscription().getType().name());
                }
                customRuleEntity.getRuleSubscriptionList().add(ruleSubscription);
                ruleSubscription.setCustomRule(customRuleEntity);
            } else if (SubscritionOperationType.REMOVE.equals(updateSubscriptionType.getOperation())) {
                List<RuleSubscription> subscriptions = customRuleEntity.getRuleSubscriptionList();
                for (RuleSubscription subscription : subscriptions) {
                    if (subscription.getOwner().equals(updateSubscriptionType.getSubscription().getOwner()) && subscription.getType().equals(updateSubscriptionType.getSubscription().getType().name())) {
                        customRuleEntity.getRuleSubscriptionList().remove(subscription);
                        rulesDao.removeSubscription(subscription);
                        break;
                    }
                }
            }

            return CustomRuleMapper.toCustomRuleType(customRuleEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when updating custom rule {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when updating custom rule. ]", e);
        }
    }

    @Override
    public CustomRuleType deleteCustomRule(String guid) throws RulesModelException {
        try {
            CustomRule entity = rulesDao.getCustomRuleByGuid(guid);
            entity.setArchived(true);
            entity.setActive(false);
            entity.setEndDate(DateUtils.nowUTC().toGregorianCalendar().getTime());
            return CustomRuleMapper.toCustomRuleType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting CustomRule by GUID ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public CustomRuleType updateLastTriggeredCustomRule(String ruleGuid) throws RulesModelException {
        LOG.info("[INFO] Update custom rule in Rules");

        if (ruleGuid == null) {
            LOG.error("[ERROR] GUID of Custom Rule is null, returning Exception. ]");
            throw new InputArgumentException("GUID of Custom Rule is null", null);
        }

        try {
            CustomRule entity = rulesDao.getCustomRuleByGuid(ruleGuid);
            entity.setTriggered(DateUtils.nowUTC().toGregorianCalendar().getTime());
            rulesDao.updateCustomRule(entity);

            return CustomRuleMapper.toCustomRuleType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when updating last triggered on rule {} {}", ruleGuid, e.getMessage());
            throw new RulesModelException("[ERROR] Error when updating last triggered on rule " + ruleGuid + ". ]", e);
        }
    }


    @Override
    public TicketType setTicketStatus(TicketType ticket) throws RulesModelException {
        LOG.info("[INFO] Update ticket status in Rules");

        try {
            if (ticket == null || ticket.getGuid() == null) {
                LOG.error("[ERROR] Ticket is null, can not update status ]");
                throw new InputArgumentException("Ticket is null", null);
            }
            Ticket entity = rulesDao.getTicketByGuid(ticket.getGuid());

            entity.setStatus(ticket.getStatus().name());
            entity.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
            entity.setUpdatedBy(ticket.getUpdatedBy());

            rulesDao.updateTicket(entity);

            return TicketMapper.toTicketType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when updating ticket status {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when updating ticket status. ]", e);
        }
    }

    @Override
    public TicketType updateTicketCount(TicketType ticket) throws RulesModelException {
        LOG.info("[INFO] Update ticket count in Rules");

        try {
            if (ticket == null || ticket.getGuid() == null) {
                LOG.error("[ERROR] Ticket is null, can not update status ]");
                throw new InputArgumentException("Ticket is null", null);
            }
            Ticket entity = rulesDao.getTicketByGuid(ticket.getGuid());

            entity.setTicketCount(ticket.getTicketCount());
            entity.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
            entity.setUpdatedBy(ticket.getUpdatedBy());

            rulesDao.updateTicket(entity);

            return TicketMapper.toTicketType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when updating ticket status {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when updating ticket status. ]", e);
        }
    }

    @Override
    public AlarmReportType setAlarmStatus(AlarmReportType alarmReportType) throws RulesModelException {
        LOG.info("[INFO] Update alarm status in Rules");

        try {
            AlarmReport entity = rulesDao.getAlarmReportByGuid(alarmReportType.getGuid());
            if (entity == null) {
                LOG.error("[ERROR] Alarm is null, can not update status ]");
                throw new InputArgumentException("Alarm is null", null);
            }

            entity.setStatus(alarmReportType.getStatus().name());
            entity.setUpdatedBy(alarmReportType.getUpdatedBy());
            entity.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
            if (entity.getRawMovement() != null) {
                entity.getRawMovement().setActive(!alarmReportType.isInactivatePosition());
            }

            rulesDao.updateAlarm(entity);

            return AlarmMapper.toAlarmReportType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when updating {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when updating. ]", e);
        }
    }

    @Override
    public List<CustomRuleType> getRunnableCustomRuleList() throws RulesModelException {
        LOG.debug("Getting list of Custom Rules that are active and not archived (rule engine)");
        try {
            List<CustomRuleType> list = new ArrayList<>();
            List<CustomRule> entityList = rulesDao.getRunnableCustomRuleList();

            for (CustomRule entity : entityList) {
                list.add(CustomRuleMapper.toCustomRuleType(entity));
            }
            return list;

        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting runnable custom rules {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when getting runnable custom rules. ]", e);
        }
    }

    @Override
    public List<SanityRuleType> getSanityRuleList() throws RulesModelException {
        LOG.debug("Getting list of Sanity Rules (rule engine)");
        try {
            List<SanityRuleType> list = new ArrayList<>();
            List<SanityRule> entityList = rulesDao.getSanityRules();

            for (SanityRule entity : entityList) {
                list.add(SanityRuleMapper.toSanityRuleType(entity));
            }
            return list;

        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting sanity rules {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when getting sanity rules. ]", e);
        }
    }

    @Override
    public List<CustomRuleType> getCustomRulesByUser(String updatedBy) throws RulesModelException {
        LOG.info("[INFO] Getting list of Custom Rules by user");
        try {
            List<CustomRuleType> list = new ArrayList<>();
            List<CustomRule> entityList = rulesDao.getCustomRulesByUser(updatedBy);

            for (CustomRule entity : entityList) {
                list.add(CustomRuleMapper.toCustomRuleType(entity));
            }
            return list;

        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting custom rules by user {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when getting custom rules by user. ]", e);
        }
    }

    @Override
    public CustomRuleListResponseDto getCustomRuleListByQuery(CustomRuleQuery query) throws RulesModelException {
        LOG.info("[INFO] Get list of custom rule from query.");

        if (query == null) {
            throw new InputArgumentException("Custom rule list query is null");
        }
        if (query.getPagination() == null) {
            throw new InputArgumentException("Pagination in custom rule list query is null");
        }
        if (query.getCustomRuleSearchCriteria() == null) {
            throw new InputArgumentException("No search criteria in custom rule list query");
        }

        try {
            CustomRuleListResponseDto response = new CustomRuleListResponseDto();
            List<CustomRuleType> customRuleList = new ArrayList<>();

            Integer page = query.getPagination().getPage();
            Integer listSize = query.getPagination().getListSize();

            List<CustomRuleSearchValue> searchKeyValues = CustomRuleSearchFieldMapper.mapSearchField(query.getCustomRuleSearchCriteria());

            String sql = CustomRuleSearchFieldMapper.createSelectSearchSql(searchKeyValues, query.isDynamic());
            String countSql = CustomRuleSearchFieldMapper.createCountSearchSql(searchKeyValues, query.isDynamic());

            Long numberMatches = rulesDao.getCustomRuleListSearchCount(countSql, searchKeyValues);
            List<CustomRule> customRuleEntityList = rulesDao.getCustomRuleListPaginated(page, listSize, sql, searchKeyValues);

            for (CustomRule entity : customRuleEntityList) {
                customRuleList.add(CustomRuleMapper.toCustomRuleType(entity));
            }

            int numberOfPages = (int) (numberMatches / listSize);
            if (numberMatches % listSize != 0) {
                numberOfPages += 1;
            }

            response.setTotalNumberOfPages(numberOfPages);
            response.setCurrentPage(query.getPagination().getPage());
            response.setCustomRuleList(customRuleList);

            return response;
        } catch (DaoMappingException | DaoException e) {
            LOG.error("[ERROR] Error when getting custom rule list by query ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    // Triggered by rule engine
    @Override
    public AlarmReportType createAlarmReport(AlarmReportType alarmReportType) throws RulesModelException {
        LOG.info("[INFO] Rule Engine creating Alarm Report");
        try {
            String movementGuid = null;
            if (alarmReportType.getRawMovement() != null) {
                movementGuid = alarmReportType.getRawMovement().getGuid();
            }
            AlarmReport alarmReportEntity = rulesDao.getOpenAlarmReportByMovementGuid(movementGuid);

            if (alarmReportEntity == null) {
                alarmReportEntity = new AlarmReport();

                RawMovement rawMovement = AlarmMapper.toRawMovementEntity(alarmReportType.getRawMovement());
                if (rawMovement != null) {
                    alarmReportEntity.setRawMovement(rawMovement);
                    rawMovement.setAlarmReport(alarmReportEntity);
                }
            }
            AlarmItem alarmItem = AlarmMapper.toAlarmItemEntity(alarmReportType.getAlarmItem().get(0));
            alarmItem.setAlarmReport(alarmReportEntity);
            alarmReportEntity.getAlarmItemList().add(alarmItem);

            AlarmReport entity = AlarmMapper.toAlarmReportEntity(alarmReportEntity, alarmReportType);
            if (entity.getRawMovement() != null) {
                entity.getRawMovement().setActive(!alarmReportType.isInactivatePosition());
            }

            AlarmReport createdReport = rulesDao.createAlarmReport(entity);
            return AlarmMapper.toAlarmReportType(createdReport);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when creating alarm report {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when creating alarm report. ]", e);
        }

    }

    // Triggered by rule engine
    @Override
    public TicketType createTicket(TicketType ticketType) throws RulesModelException {
        LOG.info("[INFO] Rule Engine creating Ticket");
        try {
            Ticket ticket = TicketMapper.toTicketEntity(ticketType);
            ticket.setTicketCount(1L);
            Ticket createdTicket = rulesDao.createTicket(ticket);
            return TicketMapper.toTicketType(createdTicket);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when creating ticket {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when creating ticket. ]", e);
        }
    }

    @Override
    public AlarmListResponseDto getAlarmListByQuery(AlarmQuery query) throws RulesModelException {
        LOG.info("[INFO] Get list of alarms from query.");

        if (query == null) {
            throw new InputArgumentException("Alarm list query is null");
        }
        if (query.getPagination() == null) {
            throw new InputArgumentException("Pagination in alarm list query is null");
        }
        if (query.getAlarmSearchCriteria() == null) {
            throw new InputArgumentException("No search criteria in alarm list query");
        }

        try {
            AlarmListResponseDto response = new AlarmListResponseDto();
            List<AlarmReportType> alarmList = new ArrayList<>();

            Integer page = query.getPagination().getPage();
            Integer listSize = query.getPagination().getListSize();

            List<AlarmSearchValue> searchKeyValues = AlarmSearchFieldMapper.mapSearchField(query.getAlarmSearchCriteria());

            String sql = AlarmSearchFieldMapper.createSelectSearchSql(searchKeyValues, query.isDynamic());
            String countSql = AlarmSearchFieldMapper.createCountSearchSql(searchKeyValues, query.isDynamic());

            Long numberMatches = rulesDao.getAlarmListSearchCount(countSql, searchKeyValues);
            List<AlarmReport> alarmEntityList = rulesDao.getAlarmListPaginated(page, listSize, sql, searchKeyValues);

            for (AlarmReport entity : alarmEntityList) {
                alarmList.add(AlarmMapper.toAlarmReportType(entity));
            }

            int numberOfPages = (int) (numberMatches / listSize);
            if (numberMatches % listSize != 0) {
                numberOfPages += 1;
            }

            response.setTotalNumberOfPages(numberOfPages);
            response.setCurrentPage(query.getPagination().getPage());
            response.setAlarmList(alarmList);

            return response;
        } catch (DaoMappingException | DaoException e) {
            LOG.error("[ERROR] Error when getting alarm list by query ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<TicketType> updateTicketStatusByQuery(String loggedInUser, TicketQuery query, TicketStatusType status) throws RulesModelException {
        LOG.info("[INFO] Update ticket status by query");
        try {
            if (loggedInUser == null) {
                LOG.error("[ERROR] LoggedInUser is null, can not update status ]");
                throw new InputArgumentException("LoggedInUser is null", null);
            }
            if (status == null) {
                LOG.error("[ERROR] Status is null, can not update status ]");
                throw new InputArgumentException("Status is null", null);
            }
            if (query == null) {
                LOG.error("[ERROR] Status is null, can not update status ]");
                throw new InputArgumentException("Status is null", null);
            }
            if (query.getTicketSearchCriteria() == null) {
                LOG.error("[ERROR] No search criteria in query, can not update status ]");
                throw new InputArgumentException("No search criteria in ticket list query");
            }
            List<TicketSearchValue> searchKeyValues = TicketSearchFieldMapper.mapSearchField(query.getTicketSearchCriteria());
            List<String> validRuleGuids = rulesDao.getCustomRulesForTicketsByUser(loggedInUser);
            String sql = TicketSearchFieldMapper.createSelectSearchSql(searchKeyValues, validRuleGuids, true);
            List<Ticket> tickets = rulesDao.getTicketList(sql, searchKeyValues);
            for (Ticket ticket : tickets) {
                ticket.setStatus(status.name());
                ticket.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
                ticket.setUpdatedBy(loggedInUser);

                rulesDao.updateTicket(ticket);
            }
            List<TicketType> ticketList = new ArrayList<>();
            for (Ticket ticket : tickets) {
                ticketList.add(TicketMapper.toTicketType(ticket));
            }
            return ticketList;
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when updating ticket status {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when updating ticket status. ]", e);
        }
    }

    @Override
    public TicketListResponseDto getTicketListByQuery(String loggedInUser, TicketQuery query) throws RulesModelException {
        LOG.info("[INFO] Get list of tickets from query.");

        if (query == null) {
            throw new InputArgumentException("Ticket list query is null");
        }
        if (query.getPagination() == null) {
            throw new InputArgumentException("Pagination in ticket list query is null");
        }
        if (query.getTicketSearchCriteria() == null) {
            throw new InputArgumentException("No search criteria in ticket list query");
        }

        try {
            TicketListResponseDto response = new TicketListResponseDto();
            List<TicketType> ticketList = new ArrayList<>();
            Integer listSize = query.getPagination().getListSize();
            List<TicketSearchValue> searchKeyValues = TicketSearchFieldMapper.mapSearchField(query.getTicketSearchCriteria());
            List<String> validRuleGuids = rulesDao.getCustomRulesForTicketsByUser(loggedInUser);

            String sql = TicketSearchFieldMapper.createSelectSearchSql(searchKeyValues, validRuleGuids, true);
            String countSql = TicketSearchFieldMapper.createCountSearchSql(searchKeyValues, validRuleGuids, true);
            Long numberMatches = rulesDao.getTicketListSearchCount(countSql, searchKeyValues);
            List<Ticket> ticketEntityList = rulesDao.getTicketListPaginated(query.getPagination().getPage(), listSize, sql, searchKeyValues);
            for (Ticket entity : ticketEntityList) {
                ticketList.add(TicketMapper.toTicketType(entity));
            }
            int numberOfPages = (int) (numberMatches / listSize);
            if (numberMatches % listSize != 0) {
                numberOfPages += 1;
            }
            response.setTotalNumberOfPages(numberOfPages);
            response.setCurrentPage(query.getPagination().getPage());
            response.setTicketList(ticketList);
            return response;
        } catch (DaoMappingException | DaoException e) {
            LOG.error("[ERROR] Error when getting ticket list by query ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public TicketListResponseDto getTicketListByMovements(List<String> movements) throws RulesModelException {
        LOG.info("[INFO] Get tickets by movements.");
        if (movements == null) {
            throw new InputArgumentException("Movements list is null");
        }
        if (movements.isEmpty()) {
            throw new InputArgumentException("Movements list is empty");
        }
        try {
            TicketListResponseDto response = new TicketListResponseDto();
            List<TicketType> ticketList = new ArrayList<>();
            List<Ticket> ticketEntityList = rulesDao.getTicketsByMovements(movements);
            for (Ticket entity : ticketEntityList) {
                ticketList.add(TicketMapper.toTicketType(entity));
            }
            response.setTicketList(ticketList);
            return response;
        } catch (DaoMappingException | DaoException e) {
            LOG.error("[ERROR] Error when getting tickets by movements ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public long countTicketListByMovements(List<String> movements) throws RulesModelException {
        LOG.info("[INFO] Count tickets by movements.");
        if (movements == null) {
            throw new InputArgumentException("Movements list is null");
        }
        if (movements.isEmpty()) {
            throw new InputArgumentException("Movements list is empty");
        }
        try {
            return rulesDao.countTicketListByMovements(movements);
        } catch (DaoException e) {
            LOG.error("[ERROR] Error when counting tickets by movements ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<PreviousReportType> getPreviousReports() throws RulesModelException {
        LOG.info("[INFO] Getting list of previous reports");
        try {
            List<PreviousReportType> previousReports = new ArrayList<>();
            List<PreviousReport> entityList = rulesDao.getPreviousReportList();
            for (PreviousReport entity : entityList) {
                previousReports.add(PreviousReportMapper.toPreviousReportType(entity));
            }
            return previousReports;
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting list {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when getting list. ]", e);
        }
    }

    @Override
    public PreviousReportType getPreviousReportByAssetGuid(String assetGuid) throws RulesModelException {
        LOG.info("[INFO] Getting previous report by asset GUID..");
        try {
            PreviousReport entity = rulesDao.getPreviousReportByAssetGuid(assetGuid);
            return PreviousReportMapper.toPreviousReportType(entity);
        } catch (DaoMappingException e) {
            LOG.error("[ERROR] Error when getting previous report by asset guid {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when getting previous report by asset guid", e);
        }
    }

    @Override
    public TicketType getTicketByAssetGuid(String assetGuid, String ruleGuid) throws RulesModelException {
        LOG.info("[INFO] Getting ticket by asset guid : {}", assetGuid);
        try {
            Ticket ticketEntity = rulesDao.getTicketByAssetAndRule(assetGuid, ruleGuid);
            return TicketMapper.toTicketType(ticketEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting list {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when getting list", e);
        }
    }

    @Override
    public AlarmReportType getAlarmReportByAssetAndRule(String assetGuid, String ruleGuid) throws RulesModelException {
        LOG.info("[INFO] Getting alarm report by asset guid:{}", assetGuid);
        try {
            AlarmReport alarmReportEntity = rulesDao.getAlarmReportByAssetAndRule(assetGuid, ruleGuid);
            return AlarmMapper.toAlarmReportType(alarmReportEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting list {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when getting list. ]", e);
        }
    }

    @Override
    public void upsertPreviousReport(PreviousReportType previousReport) throws RulesModelException {
        LOG.info("[INFO] Upserting previous report");
        try {
            PreviousReport entity = rulesDao.getPreviousReportByAssetGuid(previousReport.getAssetGuid());
            if (entity == null) {
                entity = PreviousReportMapper.toPreviousReportEntity(previousReport);
            } else {
                entity = PreviousReportMapper.toPreviousReportEntity(entity, previousReport);
            }
            rulesDao.updatePreviousReport(entity);
        } catch (DaoException | DaoMappingException e) {
            throw new RulesModelException("[ERROR] Error when upserting previous report. ]", e);
        }
    }

    @Override
    public AlarmReportType getAlarmReportByGuid(String guid) throws RulesModelException {
        LOG.info("[INFO] Getting alarm report by guid");
        try {
            return AlarmMapper.toAlarmReportType(rulesDao.getAlarmReportByGuid(guid));
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting alarm report by GUID {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public TicketType getTicketByGuid(String guid) throws RulesModelException {
        LOG.info("[INFO] Getting ticket by guid");
        try {
            Ticket ticketEntity = rulesDao.getTicketByGuid(guid);
            return TicketMapper.toTicketType(ticketEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting ticket by GUID {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public long getNumberOfOpenAlarms() throws RulesModelException {
        LOG.info("[INFO] Counting open alarms");
        try {
            return rulesDao.getNumberOfOpenAlarms();
        } catch (DaoException e) {
            LOG.error("[ERROR] Error when counting open alarms {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public long getNumberOfOpenTickets(String userName) throws RulesModelException {
        LOG.info("[INFO] Counting open tickets");
        try {
            List<String> validRuleGuids = rulesDao.getCustomRulesForTicketsByUser(userName);
            if (!validRuleGuids.isEmpty()) {
                return rulesDao.getNumberOfOpenTickets(validRuleGuids);
            }
            return 0;
        } catch (DaoException e) {
            LOG.error("[ERROR] Error when counting open tickets {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public long getNumberOfAssetsNotSending() throws RulesModelException {
        LOG.info("[INFO] Counting assets not sending");
        try {
            return rulesDao.getNumberOfTicketsByRuleGuid(UvmsConstants.ASSET_NOT_SENDING_RULE);
        } catch (DaoException e) {
            LOG.error("[ERROR] Error when counting open alarms {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<TicketAndRuleType> getTicketsAndRulesByMovements(List<String> movementGuids) throws RulesModelException {
        LOG.info("[INFO] Get Tickets and Rules by movements");
        List<TicketAndRuleType> ticketsAndRules = new ArrayList<>();
        try {
            // TODO: This can be done more efficiently with some join stuff
            List<Ticket> tickets = rulesDao.getTicketsByMovements(movementGuids);
            for (Ticket ticket : tickets) {
                CustomRule rule = rulesDao.getCustomRuleByGuid(ticket.getRuleGuid());
                TicketType ticketType = TicketMapper.toTicketType(ticket);
                CustomRuleType ruleType = CustomRuleMapper.toCustomRuleType(rule);
                TicketAndRuleType ticketsAndRule = new TicketAndRuleType();
                ticketsAndRule.setTicket(ticketType);
                ticketsAndRule.setRule(ruleType);
                ticketsAndRules.add(ticketsAndRule);
            }
            return ticketsAndRules;
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ERROR] Error when getting list {}", e.getMessage());
            throw new RulesModelException("[ERROR] Error when getting list.", e);
        }
    }
}