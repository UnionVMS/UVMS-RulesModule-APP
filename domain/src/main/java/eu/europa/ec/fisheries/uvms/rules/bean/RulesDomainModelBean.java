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

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.*;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.*;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.*;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import eu.europa.ec.fisheries.uvms.rules.exception.InputArgumentException;
import eu.europa.ec.fisheries.uvms.rules.mapper.*;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.*;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.CustomRuleListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
public class RulesDomainModelBean implements RulesDomainModel {

    private final static Logger LOG = LoggerFactory.getLogger(RulesDomainModelBean.class);

    @EJB
    RulesDao dao;

    @Override
    public List<TemplateRuleMapDto> getAllFactTemplatesAndRules() throws RulesModelException {
        List<TemplateRuleMapDto> templateRuleList = new ArrayList<>();
        try {
            List<Template> factTemplates = dao.getAllFactTemplates();
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
    public void updateFailedRules(List<String> failedBrId) throws RulesModelException {
        try {
            dao.updatedFailedRules(failedBrId);
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
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
                dao.saveValidationMessages(Arrays.asList(rawMessage));
            }
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<ValidationMessageType> getValidationMessagesById(List<String> ids) throws RulesModelException {
        try {
            List<ValidationMessage> validationMessages = dao.getValidationMessagesById(ids);
            return RawMessageMapper.INSTANCE.mapToValidationMessageTypes(validationMessages);
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public RuleStatusType checkRuleStatus() throws RulesModelException {
        try {
            return dao.checkRuleStatus();
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateRuleStatus(RuleStatusType ruleStatusType) throws RulesModelException {
        try {
            RuleStatus ruleStatus = new RuleStatus();
            ruleStatus.setRuleStatus(ruleStatusType);
            dao.deleteRuleStatus();
            dao.createRuleStatus(ruleStatus);
        } catch (DaoException e) {
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public CustomRuleType createCustomRule(CustomRuleType customRule) throws RulesModelException {
        LOG.info("Create in Rules");
        try {
            CustomRule entity = CustomRuleMapper.toCustomRuleEntity(customRule);

            List<RuleSubscription> subscriptionEntities = new ArrayList<>();
            RuleSubscription creatorSubscription = new RuleSubscription();
            creatorSubscription.setCustomRule(entity);
            creatorSubscription.setOwner(customRule.getUpdatedBy());
            creatorSubscription.setType(SubscriptionTypeType.TICKET.value());
            subscriptionEntities.add(creatorSubscription);
            entity.getRuleSubscriptionList().addAll(subscriptionEntities);

            dao.createCustomRule(entity);
            return CustomRuleMapper.toCustomRuleType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when creating CustomRule ] {}", e.getMessage());
            throw new RulesModelException("Error when creating CustomRule", e);
        }
    }

    @Override
    public CustomRuleType getByGuid(String guid) throws RulesModelException {
        try {
            CustomRule entity = dao.getCustomRuleByGuid(guid);
            return CustomRuleMapper.toCustomRuleType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting CustomRule by GUID ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public CustomRuleType updateCustomRule(CustomRuleType customRule) throws RulesModelException {
        LOG.info("Update custom rule in Rules");

        if (customRule == null) {
            LOG.error("[ Custom Rule is null, returning Exception ]");
            throw new InputArgumentException("Custom Rule is null", null);
        }

        if (customRule.getGuid() == null) {
            LOG.error("[ GUID of Custom Rule is null, returning Exception. ]");
            throw new InputArgumentException("GUID of Custom Rule is null", null);
        }

        try {

            CustomRule newEntity = CustomRuleMapper.toCustomRuleEntity(customRule);

            CustomRule oldEntity = dao.getCustomRuleByGuid(customRule.getGuid());

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
                dao.detachSubscription(subscription);
                newEntity.getRuleSubscriptionList().add(subscription);
                subscription.setCustomRule(newEntity);
            }

            newEntity = dao.createCustomRule(newEntity);
            return CustomRuleMapper.toCustomRuleType(newEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when updating custom rule. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when updating custom rule. ]", e);
        }
    }

    @Override
    public CustomRuleType updateCustomRuleSubscription(UpdateSubscriptionType updateSubscriptionType) throws RulesModelException {
        LOG.info("Update custom rule subscription in Rules");

        if (updateSubscriptionType == null) {
            LOG.error("[ Subscription is null, returning Exception ]");
            throw new InputArgumentException("Subscription is null", null);
        }

        if (updateSubscriptionType.getRuleGuid() == null) {
            LOG.error("[ Custom Rule GUID for Subscription is null, returning Exception. ]");
            throw new InputArgumentException("Custom Rule GUID for Subscription is null", null);
        }

        try {
            CustomRule customRuleEntity = dao.getCustomRuleByGuid(updateSubscriptionType.getRuleGuid());

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
                        dao.removeSubscription(subscription);
                        break;
                    }
                }
            }

            return CustomRuleMapper.toCustomRuleType(customRuleEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when updating custom rule. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when updating custom rule. ]", e);
        }
    }

    @Override
    public CustomRuleType deleteCustomRule(String guid) throws RulesModelException {
        try {
            CustomRule entity = dao.getCustomRuleByGuid(guid);
            entity.setArchived(true);
            entity.setActive(false);
            entity.setEndDate(DateUtils.nowUTC().toGregorianCalendar().getTime());
            return CustomRuleMapper.toCustomRuleType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting CustomRule by GUID ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public CustomRuleType updateLastTriggeredCustomRule(String ruleGuid) throws RulesModelException {
        LOG.info("Update custom rule in Rules");

        if (ruleGuid == null) {
            LOG.error("[ GUID of Custom Rule is null, returning Exception. ]");
            throw new InputArgumentException("GUID of Custom Rule is null", null);
        }

        try {
            CustomRule entity = dao.getCustomRuleByGuid(ruleGuid);
            entity.setTriggered(DateUtils.nowUTC().toGregorianCalendar().getTime());
            dao.updateCustomRule(entity);

            return CustomRuleMapper.toCustomRuleType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when updating last triggered on rule {}. ] {}", ruleGuid, e.getMessage());
            throw new RulesModelException("[ Error when updating last triggered on rule " + ruleGuid + ". ]", e);
        }
    }


    @Override
    public TicketType setTicketStatus(TicketType ticket) throws RulesModelException {
        LOG.info("Update ticket status in Rules");

        try {
            if (ticket == null || ticket.getGuid() == null) {
                LOG.error("[ Ticket is null, can not update status ]");
                throw new InputArgumentException("Ticket is null", null);
            }
            Ticket entity = dao.getTicketByGuid(ticket.getGuid());

            entity.setStatus(ticket.getStatus().name());
            entity.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
            entity.setUpdatedBy(ticket.getUpdatedBy());

            dao.updateTicket(entity);

            return TicketMapper.toTicketType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when updating ticket status. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when updating ticket status. ]", e);
        }
    }

    @Override
    public TicketType updateTicketCount(TicketType ticket) throws RulesModelException {
        LOG.info("Update ticket count in Rules");

        try {
            if (ticket == null || ticket.getGuid() == null) {
                LOG.error("[ Ticket is null, can not update status ]");
                throw new InputArgumentException("Ticket is null", null);
            }
            Ticket entity = dao.getTicketByGuid(ticket.getGuid());

            entity.setTicketCount(ticket.getTicketCount());
            entity.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
            entity.setUpdatedBy(ticket.getUpdatedBy());

            dao.updateTicket(entity);

            return TicketMapper.toTicketType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when updating ticket status. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when updating ticket status. ]", e);
        }
    }

    @Override
    public AlarmReportType setAlarmStatus(AlarmReportType alarmReportType) throws RulesModelException {
        LOG.info("Update alarm status in Rules");

        try {
            AlarmReport entity = dao.getAlarmReportByGuid(alarmReportType.getGuid());
            if (entity == null) {
                LOG.error("[ Alarm is null, can not update status ]");
                throw new InputArgumentException("Alarm is null", null);
            }

            entity.setStatus(alarmReportType.getStatus().name());
            entity.setUpdatedBy(alarmReportType.getUpdatedBy());
            entity.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
            if (entity.getRawMovement() != null) {
                entity.getRawMovement().setActive(!alarmReportType.isInactivatePosition());
            }

            dao.updateAlarm(entity);

            return AlarmMapper.toAlarmReportType(entity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when updating. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when updating. ]", e);
        }
    }

    @Override
    public List<CustomRuleType> getRunnableCustomRuleList() throws RulesModelException {
        LOG.info("Getting list of Custom Rules that are active and not archived (rule engine)");
        try {
            List<CustomRuleType> list = new ArrayList<>();
            List<CustomRule> entityList = dao.getRunnableCustomRuleList();

            for (CustomRule entity : entityList) {
                list.add(CustomRuleMapper.toCustomRuleType(entity));
            }
            return list;

        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting runnable custom rules. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when getting runnable custom rules. ]", e);
        }
    }

    @Override
    public List<SanityRuleType> getSanityRuleList() throws RulesModelException {
        LOG.info("Getting list of Sanity Rules (rule engine)");
        try {
            List<SanityRuleType> list = new ArrayList<>();
            List<SanityRule> entityList = dao.getSanityRules();

            for (SanityRule entity : entityList) {
                list.add(SanityRuleMapper.toSanityRuleType(entity));
            }
            return list;

        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting sanity rules. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when getting sanity rules. ]", e);
        }
    }

    @Override
    public List<CustomRuleType> getCustomRulesByUser(String updatedBy) throws RulesModelException {
        LOG.info("Getting list of Custom Rules by user");
        try {
            List<CustomRuleType> list = new ArrayList<>();
            List<CustomRule> entityList = dao.getCustomRulesByUser(updatedBy);

            for (CustomRule entity : entityList) {
                list.add(CustomRuleMapper.toCustomRuleType(entity));
            }
            return list;

        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting custom rules by user. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when getting custom rules by user. ]", e);
        }
    }

    @Override
    public CustomRuleListResponseDto getCustomRuleListByQuery(CustomRuleQuery query) throws RulesModelException {
        LOG.info("Get list of custom rule from query.");

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

            Long numberMatches = dao.getCustomRuleListSearchCount(countSql, searchKeyValues);
            List<CustomRule> customRuleEntityList = dao.getCustomRuleListPaginated(page, listSize, sql, searchKeyValues);

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
            LOG.error("[ Error when getting custom rule list by query ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    // Triggered by rule engine
    @Override
    public AlarmReportType createAlarmReport(AlarmReportType alarmReportType) throws RulesModelException {
        LOG.info("Rule Engine creating Alarm Report");
        try {
            String movementGuid = null;
            if (alarmReportType.getRawMovement() != null) {
                movementGuid = alarmReportType.getRawMovement().getGuid();
            }
            AlarmReport alarmReportEntity = dao.getOpenAlarmReportByMovementGuid(movementGuid);

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

            AlarmReport createdReport = dao.createAlarmReport(entity);
            return AlarmMapper.toAlarmReportType(createdReport);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when creating alarm report. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when creating alarm report. ]", e);
        }

    }

    // Triggered by rule engine
    @Override
    public TicketType createTicket(TicketType ticketType) throws RulesModelException {
        LOG.info("Rule Engine creating Ticket");
        try {
            Ticket ticket = TicketMapper.toTicketEntity(ticketType);
            ticket.setTicketCount(1L);
            Ticket createdTicket = dao.createTicket(ticket);
            return TicketMapper.toTicketType(createdTicket);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when creating ticket. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when creating ticket. ]", e);
        }
    }

    @Override
    public AlarmListResponseDto getAlarmListByQuery(AlarmQuery query) throws RulesModelException {
        LOG.info("Get list of alarms from query.");

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

            Long numberMatches = dao.getAlarmListSearchCount(countSql, searchKeyValues);
            List<AlarmReport> alarmEntityList = dao.getAlarmListPaginated(page, listSize, sql, searchKeyValues);

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
            LOG.error("[ Error when getting alarm list by query ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<TicketType> updateTicketStatusByQuery(String loggedInUser, TicketQuery query, TicketStatusType status) throws RulesModelException {
        LOG.info("Update ticket status by query");

        try {
            if (loggedInUser == null) {
                LOG.error("[ LoggedInUser is null, can not update status ]");
                throw new InputArgumentException("LoggedInUser is null", null);
            }
            if (status == null) {
                LOG.error("[ Status is null, can not update status ]");
                throw new InputArgumentException("Status is null", null);
            }
            if (query == null) {
                LOG.error("[ Status is null, can not update status ]");
                throw new InputArgumentException("Status is null", null);
            }
            if (query.getTicketSearchCriteria() == null) {
                LOG.error("[ No search criteria in query, can not update status ]");
                throw new InputArgumentException("No search criteria in ticket list query");
            }

            List<TicketSearchValue> searchKeyValues = TicketSearchFieldMapper.mapSearchField(query.getTicketSearchCriteria());

            List<String> validRuleGuids = dao.getCustomRulesForTicketsByUser(loggedInUser);

            String sql = TicketSearchFieldMapper.createSelectSearchSql(searchKeyValues, validRuleGuids, true);

            List<Ticket> tickets = dao.getTicketList(sql, searchKeyValues);

            for (Ticket ticket : tickets) {
                ticket.setStatus(status.name());
                ticket.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
                ticket.setUpdatedBy(loggedInUser);

                dao.updateTicket(ticket);
            }

            List<TicketType> ticketList = new ArrayList<>();
            for (Ticket ticket : tickets) {
                ticketList.add(TicketMapper.toTicketType(ticket));
            }

            return ticketList;
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when updating ticket status. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when updating ticket status. ]", e);
        }
    }

    @Override
    public TicketListResponseDto getTicketListByQuery(String loggedInUser, TicketQuery query) throws RulesModelException {
        LOG.info("Get list of tickets from query.");

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

            Integer page = query.getPagination().getPage();
            Integer listSize = query.getPagination().getListSize();

            List<TicketSearchValue> searchKeyValues = TicketSearchFieldMapper.mapSearchField(query.getTicketSearchCriteria());

            List<String> validRuleGuids = dao.getCustomRulesForTicketsByUser(loggedInUser);

            // If no valid guids, return empty ticket list
//            if (validRuleGuids.isEmpty()) {
//                response.setTotalNumberOfPages(0);
//                response.setCurrentPage(query.getPagination().getPage());
//                response.setTicketList(ticketList);
//                return response;
//            }

            String sql = TicketSearchFieldMapper.createSelectSearchSql(searchKeyValues, validRuleGuids, true);
            String countSql = TicketSearchFieldMapper.createCountSearchSql(searchKeyValues, validRuleGuids, true);

            Long numberMatches = dao.getTicketListSearchCount(countSql, searchKeyValues);
            List<Ticket> ticketEntityList = dao.getTicketListPaginated(page, listSize, sql, searchKeyValues);

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
            LOG.error("[ Error when getting ticket list by query ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public TicketListResponseDto getTicketListByMovements(List<String> movements) throws RulesModelException {
        LOG.info("Get tickets by movements.");

        if (movements == null) {
            throw new InputArgumentException("Movements list is null");
        }
        if (movements.isEmpty()) {
            throw new InputArgumentException("Movements list is empty");
        }
        try {
            TicketListResponseDto response = new TicketListResponseDto();
            List<TicketType> ticketList = new ArrayList<>();

            List<Ticket> ticketEntityList = dao.getTicketsByMovements(movements);

            for (Ticket entity : ticketEntityList) {
                ticketList.add(TicketMapper.toTicketType(entity));
            }

            response.setTicketList(ticketList);

            return response;
        } catch (DaoMappingException | DaoException e) {
            LOG.error("[ Error when getting tickets by movements ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public long countTicketListByMovements(List<String> movements) throws RulesModelException {
        LOG.info("Count tickets by movements.");

        if (movements == null) {
            throw new InputArgumentException("Movements list is null");
        }
        if (movements.isEmpty()) {
            throw new InputArgumentException("Movements list is empty");
        }
        try {
            long count = dao.countTicketListByMovements(movements);
            return count;
        } catch (DaoException e) {
            LOG.error("[ Error when counting tickets by movements ] {} ", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<PreviousReportType> getPreviousReports() throws RulesModelException {
        LOG.info("Getting list of previous reports");
        try {
            List<PreviousReportType> previousReports = new ArrayList<>();

            List<PreviousReport> entityList = dao.getPreviousReportList();

            for (PreviousReport entity : entityList) {
                previousReports.add(PreviousReportMapper.toPreviousReportType(entity));
            }
            return previousReports;

        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting list. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when getting list. ]", e);
        }
    }

    @Override
    public PreviousReportType getPreviousReportByAssetGuid(String assetGuid) throws RulesModelException {
        LOG.info("Get previous report by asset GUID");
        try {
            PreviousReport entity = dao.getPreviousReportByAssetGuid(assetGuid);
            return PreviousReportMapper.toPreviousReportType(entity);
        } catch (DaoMappingException e) {
            LOG.error("[ Error when getting previous report by asset guid. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when getting previous report by asset guid. ]", e);
        }
    }

    @Override
    public TicketType getTicketByAssetGuid(String assetGuid, String ruleGuid) throws RulesModelException {
        LOG.info("Getting ticket by asset guid:{}", assetGuid);
        try {
            Ticket ticketEntity = dao.getTicketByAssetAndRule(assetGuid, ruleGuid);
            return TicketMapper.toTicketType(ticketEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting list. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when getting list. ]", e);
        }
    }

    @Override
    public AlarmReportType getAlarmReportByAssetAndRule(String assetGuid, String ruleGuid) throws RulesModelException {
        LOG.info("Getting alarm report by asset guid:{}", assetGuid);
        try {
            AlarmReport alarmReportEntity = dao.getAlarmReportByAssetAndRule(assetGuid, ruleGuid);
            return AlarmMapper.toAlarmReportType(alarmReportEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting list. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when getting list. ]", e);
        }
    }

    @Override
    public void upsertPreviousReport(PreviousReportType previousReport) throws RulesModelException {
        LOG.info("Upserting previous report");
        try {
            PreviousReport entity = dao.getPreviousReportByAssetGuid(previousReport.getAssetGuid());
            if (entity == null) {
                entity = PreviousReportMapper.toPreviousReportEntity(previousReport);
            } else {
                entity = PreviousReportMapper.toPreviousReportEntity(entity, previousReport);
            }
            dao.updatePreviousReport(entity);
        } catch (DaoException | DaoMappingException e) {
            throw new RulesModelException("[ Error when upserting previous report. ]", e);
        }
    }

    @Override
    public AlarmReportType getAlarmReportByGuid(String guid) throws RulesModelException {
        LOG.info("Getting alarm report by guid");
        try {
            return AlarmMapper.toAlarmReportType(dao.getAlarmReportByGuid(guid));
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting alarm report by GUID. ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public TicketType getTicketByGuid(String guid) throws RulesModelException {
        LOG.info("Getting ticket by guid");
        try {
            Ticket ticketEntity = dao.getTicketByGuid(guid);
            return TicketMapper.toTicketType(ticketEntity);
        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting ticket by GUID. ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public long getNumberOfOpenAlarms() throws RulesModelException {
        LOG.info("Counting open alarms");
        try {
            long alarmCount = dao.getNumberOfOpenAlarms();
            return alarmCount;
        } catch (DaoException e) {
            LOG.error("[ Error when counting open alarms. ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public long getNumberOfOpenTickets(String userName) throws RulesModelException {
        LOG.info("Counting open tickets");
        try {
            List<String> validRuleGuids = dao.getCustomRulesForTicketsByUser(userName);
            if (!validRuleGuids.isEmpty()) {
                return dao.getNumberOfOpenTickets(validRuleGuids);
            }
            return 0;
        } catch (DaoException e) {
            LOG.error("[ Error when counting open tickets. ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public long getNumberOfAssetsNotSending() throws RulesModelException {
        LOG.info("Counting assets not sending");
        try {
            return dao.getNumberOfTicketsByRuleGuid(UvmsConstants.ASSET_NOT_SENDING_RULE);
        } catch (DaoException e) {
            LOG.error("[ Error when counting open alarms. ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<TicketAndRuleType> getTicketsAndRulesByMovements(List<String> movementGuids) throws RulesModelException {
        LOG.info("Get Tickets and Rules by movements");

        List<TicketAndRuleType> ticketsAndRules = new ArrayList<>();
        try {
            // TODO: This can be done more efficiently with some join stuff
            List<Ticket> tickets = dao.getTicketsByMovements(movementGuids);
            for (Ticket ticket : tickets) {
                CustomRule rule = dao.getCustomRuleByGuid(ticket.getRuleGuid());

                TicketType ticketType = TicketMapper.toTicketType(ticket);
                CustomRuleType ruleType = CustomRuleMapper.toCustomRuleType(rule);

                TicketAndRuleType ticketsAndRule = new TicketAndRuleType();
                ticketsAndRule.setTicket(ticketType);
                ticketsAndRule.setRule(ruleType);

                ticketsAndRules.add(ticketsAndRule);
            }
            return ticketsAndRules;

        } catch (DaoException | DaoMappingException e) {
            LOG.error("[ Error when getting list. ] {}", e.getMessage());
            throw new RulesModelException("[ Error when getting list. ]", e);
        }
    }

    @Override
    public List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode) throws RulesModelException {
        LOG.info("Retrieving fishing gear characteristic codes by gear type code: " + fishingGearTypeCode);
        try {
            return dao.getFishingGearCharacteristicCodes(fishingGearTypeCode);
        } catch (DaoException e) {
            LOG.error("[ Error while retrieving fishing gear characteristic codes by gear type code. ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode, boolean mandatory) throws RulesModelException {
        LOG.info("Retrieving all fishing gear characteristic codes by gear type code: " + fishingGearTypeCode);
        try {
            return dao.getFishingGearCharacteristicCodes(fishingGearTypeCode, mandatory);
        } catch (DaoException e) {
            LOG.error("[ Error while retrieving all fishing gear characteristic codes by gear type code and mandatory. ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getAllFishingGearTypeCodes() throws RulesModelException {
        LOG.info("Retrieving all fishing gear type codes");
        try {
            return dao.getAllFishingGearTypeCodes();
        } catch (DaoException e) {
            LOG.error("[ Error while retrieving all fishing gear type codes. ] {}", e.getMessage());
            throw new RulesModelException(e.getMessage(), e);
        }
    }
}