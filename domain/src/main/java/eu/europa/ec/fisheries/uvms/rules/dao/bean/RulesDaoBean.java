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
package eu.europa.ec.fisheries.uvms.rules.dao.bean;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleStatusType;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;
import eu.europa.ec.fisheries.uvms.rules.dao.Dao;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.*;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import eu.europa.ec.fisheries.uvms.rules.exception.NoEntityFoundException;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.AlarmSearchValue;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.CustomRuleSearchValue;
import eu.europa.ec.fisheries.uvms.rules.mapper.search.TicketSearchValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class RulesDaoBean extends Dao implements RulesDao {

    private final static Logger LOG = LoggerFactory.getLogger(RulesDaoBean.class);
    private static final String MOVEMENT_GUID_PARAMETER = "movementGuid";

    @Override
    public CustomRule createCustomRule(CustomRule entity) throws DaoException {
        try {
            em.persist(entity);
            return entity;
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            LOG.error("[ Error when creating. ] {}", e.getMessage());
            throw new DaoException("[ Error when creating. ]", e);
        } catch (PersistenceException e) {
            LOG.error("[ Error when creating. ] {}", e.getMessage());
            throw new DaoException("[ Error when creating. ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when creating. ] {}", e.getMessage());
            throw new DaoException("[ Error when creating. ]", e);
        }
    }

    @Override
    public CustomRule getCustomRuleByGuid(String guid) throws DaoException {

        try {
            TypedQuery<CustomRule> query = em.createNamedQuery(UvmsConstants.FIND_CUSTOM_RULE_BY_GUID, CustomRule.class);
            query.setParameter("guid", guid);
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOG.error("[ No custom rule with guid '{}' can be found ]", guid);
            throw new NoEntityFoundException("[ No custom rule with guid: " + guid + " can be found ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting CustomRule by GUID. ] '{}'", e.getMessage());
            throw new DaoException("[ Error when getting CustomRule by GUID. ] ", e);
        }

    }

    @Override
    public Ticket getTicketByGuid(String guid) throws DaoException {
        try {
            TypedQuery<Ticket> query = em.createNamedQuery(UvmsConstants.FIND_TICKET_BY_GUID, Ticket.class);
            query.setParameter("guid", guid);
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOG.error("[ Ticket with guid '{}' can't be found ]", guid);
            throw new NoEntityFoundException("[ Ticket with guid '" + guid + "' can't be found ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting Ticket by ID. ] {}", e.getMessage());
            throw new DaoException("[ Error when getting Ticket by ID. ] ", e);
        }
    }

    @Override
    public List<Ticket> getTicketsByMovements(List<String> movements) throws DaoException {
        try {
            TypedQuery<Ticket> query = em.createNamedQuery(UvmsConstants.FIND_TICKETS_BY_MOVEMENTS, Ticket.class);
            query.setParameter("movements", movements);
            return query.getResultList();
        } catch (NoResultException e) {
            // TODO: Return empty list???
            LOG.error("[ No tickets found for movements ]");
            throw new NoEntityFoundException("[ No tickets found for movements ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting Ticket by movements. ] {}", e.getMessage());
            throw new DaoException("[ Error when getting Ticket by movements. ] ", e);
        }
    }

    @Override
    public long countTicketListByMovements(List<String> movements) throws DaoException {
        try {
            TypedQuery<Long> query = em.createNamedQuery(UvmsConstants.COUNT_TICKETS_BY_MOVEMENTS, Long.class);
            query.setParameter("movements", movements);
            return query.getSingleResult();
        } catch (Exception e) {
            LOG.error("[ Error when getting counting tickets by movement. ] {}", e.getMessage());
            throw new DaoException("[ Error when counting tickets by movement. ] ", e);
        }
    }


    @Override
    public List<String> getCustomRulesForTicketsByUser(String owner) throws DaoException {
        try {
            TypedQuery<String> query = em.createNamedQuery(UvmsConstants.FIND_CUSTOM_RULE_GUID_FOR_TICKETS, String.class);
            query.setParameter("owner", owner);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            LOG.error("[ Error when getting Tickets by userName. ] {}", e.getMessage());
            throw new DaoException("[ Error when getting Tickets by userName. ] ", e);
        }
    }

    @Override
    public long getNumberOfOpenAlarms() throws DaoException {
        try {
            TypedQuery<Long> query = em.createNamedQuery(UvmsConstants.COUNT_OPEN_ALARMS, Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            LOG.error("[ Error when getting counting open alarms. ] {}", e.getMessage());
            throw new DaoException("[ Error when counting open alarms. ] ", e);
        }
    }

    @Override
    public long getNumberOfOpenTickets(List<String> validRuleGuids) throws DaoException {
        try {
            TypedQuery<Long> query = em.createNamedQuery(UvmsConstants.COUNT_OPEN_TICKETS, Long.class);
            query.setParameter("validRuleGuids", validRuleGuids);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            LOG.error("[ Error when getting counting open tickets. ] {}", e.getMessage());
            throw new DaoException("[ Error when counting open tickets. ] ", e);
        }
    }

    @Override
    public AlarmReport getAlarmReportByGuid(String guid) throws DaoException {
        try {
            TypedQuery<AlarmReport> query = em.createNamedQuery(UvmsConstants.FIND_ALARM_BY_GUID, AlarmReport.class);
            query.setParameter("guid", guid);
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOG.error("[ Alarm with guid {} can't be found ]", guid);
            throw new NoEntityFoundException("[ Alarm with guid " + guid + " can't be found ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting Alarm by ID. ] {}", e.getMessage());
            throw new DaoException("[ Error when getting Alarm by ID. ] ", e);
        }
    }

    @Override
    public CustomRule updateCustomRule(CustomRule entity) throws DaoException {
        try {
            em.merge(entity);
            em.flush();
            return entity;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            LOG.error("[ Error when updating CustomRule ] {}", e.getMessage());
            throw new DaoException("[ Error when updating CustomRule ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when updating CustomRule ] {}", e.getMessage());
            throw new DaoException("[ Error when updating CustomRule ]", e);
        }
    }

    @Override
    public void removeSubscription(RuleSubscription entity) throws DaoException {
        try {
            em.remove(entity);
            em.flush();
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            LOG.error("[ Error when removing subscription ] {}", e.getMessage());
            throw new DaoException("[ Error when removing subscription ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when removing subscription ] {}", e.getMessage());
            throw new DaoException("[ Error when removing subscription ]", e);
        }
    }

    @Override
    public void detachSubscription(RuleSubscription subscription) throws DaoException {
        try {
            em.detach(subscription);
            subscription.setId(null);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            LOG.error("[ Error when detaching subscription ] {}", e.getMessage());
            throw new DaoException("[ Error when detaching subscription ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when detaching subscription ] {}", e.getMessage());
            throw new DaoException("[ Error when detaching subscription ]", e);
        }
    }

    @Override
    public Ticket updateTicket(Ticket entity) throws DaoException {
        try {
            em.merge(entity);
            em.flush();
            return entity;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            LOG.error("[ Error when updating Ticket ] {}", e.getMessage());
            throw new DaoException("[ Error when updating Ticket ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when updating Ticket ] {}", e.getMessage());
            throw new DaoException("[ Error when updating Ticket ]", e);
        }
    }

    @Override
    public AlarmReport updateAlarm(AlarmReport entity) throws DaoException {
        try {
            em.merge(entity);
            em.flush();
            return entity;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            LOG.error("[ Error when updating Alarm ] {}", e.getMessage());
            throw new DaoException("[ Error when updating Alarm ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when updating Alarm ] {}", e.getMessage());
            throw new DaoException("[ Error when updating CustomAlarm ]", e);
        }
    }

    @Override
    public List<CustomRule> getRunnableCustomRuleList() throws DaoException {
        try {
            TypedQuery<CustomRule> query = em.createNamedQuery(UvmsConstants.GET_RUNNABLE_CUSTOM_RULES, CustomRule.class);
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error when getting runnable custom rules ] {}", e.getMessage());
            throw new DaoException("[ Error when getting runnable custom rules ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting runnable custom rules ] {}", e.getMessage());
            throw new DaoException("[ Error when getting runnable custom rules ] ", e);
        }
    }

    @Override
    public List<SanityRule> getSanityRules() throws DaoException {
        try {
            TypedQuery<SanityRule> query = em.createNamedQuery(UvmsConstants.FIND_ALL_SANITY_RULES, SanityRule.class);
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error when getting sanity rules ] {}", e.getMessage());
            throw new DaoException("[ Error when getting sanity rules ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting sanity rules ] {}", e.getMessage());
            throw new DaoException("[ Error when getting sanity rules ] ", e);
        }
    }

    @Override
    public List<CustomRule> getCustomRulesByUser(String updatedBy) throws DaoException {
        try {
            TypedQuery<CustomRule> query = em.createNamedQuery(UvmsConstants.LIST_CUSTOM_RULES_BY_USER, CustomRule.class);
            query.setParameter("updatedBy", updatedBy);
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error when getting custom rules by user ] {}", e.getMessage());
            throw new DaoException("[ Error when getting custom rules by user ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting custom rules by user ] {}", e.getMessage());
            throw new DaoException("[ Error when getting custom rules by user ] ", e);
        }
    }

    @Override
    public AlarmReport createAlarmReport(AlarmReport alarmReport) throws DaoException {
        LOG.info("Creating alarm report");
        try {
            em.persist(alarmReport);
            return alarmReport;
        } catch (Exception e) {
            LOG.error("[ Error when persisting alarm report. ] {}", e.getMessage());
            throw new DaoException("[ Error when persisting alarm report. ]", e);
        }
    }

    @Override
    public Ticket createTicket(Ticket ticket) throws DaoException {
        LOG.info("Creating ticket");
        try {
            em.persist(ticket);
            return ticket;
        } catch (Exception e) {
            LOG.error("[ Error when persisting ticket. ] {}", e.getMessage());
            throw new DaoException("[ Error when persisting ticket. ]", e);
        }
    }

    @Override
    public AlarmReport getOpenAlarmReportByMovementGuid(String guid) throws DaoException {
        AlarmReport errorReport;
        try {
            TypedQuery<AlarmReport> query = em.createNamedQuery(UvmsConstants.FIND_OPEN_ALARM_REPORT_BY_MOVEMENT_GUID, AlarmReport.class);
            query.setParameter(MOVEMENT_GUID_PARAMETER, guid);
            errorReport = query.getSingleResult();
        } catch (NoResultException e) {
            LOG.debug("Fist position report");
            return null;
        } catch (Exception e) {
            LOG.error("[ Error when getting error report. ] {}", e.getMessage());
            throw new DaoException("[ Error when getting error report. ]", e);
        }

        return errorReport;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getCustomRuleListSearchCount(String countSql, List<CustomRuleSearchValue> searchKeyValues) throws DaoException {
        LOG.debug("CUSTOM RULE SQL QUERY IN LIST COUNT: {}", countSql);

        TypedQuery<Long> query = em.createQuery(countSql, Long.class);

        return query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CustomRule> getCustomRuleListPaginated(Integer page, Integer listSize, String sql, List<CustomRuleSearchValue> searchKeyValues)
            throws DaoException {
        try {
            LOG.debug("CUSTOM RULE SQL QUERY IN LIST PAGINATED: {}", sql);

            TypedQuery<CustomRule> query = em.createQuery(sql, CustomRule.class);

            query.setFirstResult(listSize * (page - 1));
            query.setMaxResults(listSize);

            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error getting custom rule list paginated ] {}", e.getMessage());
            throw new DaoException("[ Error when getting custom rule list ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error getting custom rule list paginated ]  {}", e.getMessage());
            throw new DaoException("[ Error when getting custom rule list ] ", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getAlarmListSearchCount(String countSql, List<AlarmSearchValue> searchKeyValues) throws DaoException {
        LOG.debug("ALARM SQL QUERY IN LIST COUNT: {}", countSql);

        TypedQuery<Long> query = em.createQuery(countSql, Long.class);

        return query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AlarmReport> getAlarmListPaginated(Integer page, Integer listSize, String sql, List<AlarmSearchValue> searchKeyValues)
            throws DaoException {
        try {
            LOG.debug("ALARM SQL QUERY IN LIST PAGINATED: {}", sql);

            TypedQuery<AlarmReport> query = em.createQuery(sql, AlarmReport.class);

            query.setFirstResult(listSize * (page - 1));
            query.setMaxResults(listSize);

            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error getting alarm list paginated ] {}", e.getMessage());
            throw new DaoException("[ Error when getting alarm list ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error getting alarm list paginated ]  {}", e.getMessage());
            throw new DaoException("[ Error when getting alarm list ] ", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getTicketListSearchCount(String countSql, List<TicketSearchValue> searchKeyValues) throws DaoException {
        LOG.debug("TICKET SQL QUERY IN LIST COUNT: {}", countSql);

        TypedQuery<Long> query = em.createQuery(countSql, Long.class);

        return query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Ticket> getTicketListPaginated(Integer page, Integer listSize, String sql, List<TicketSearchValue> searchKeyValues)
            throws DaoException {
        try {
            LOG.debug("TICKET SQL QUERY IN LIST PAGINATED: {}", sql);

            TypedQuery<Ticket> query = em.createQuery(sql, Ticket.class);

            query.setFirstResult(listSize * (page - 1));
            query.setMaxResults(listSize);

            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error getting ticket list paginated ] {}", e.getMessage());
            throw new DaoException("[ Error when getting ticket list ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error getting ticket list paginated ]  {}", e.getMessage());
            throw new DaoException("[ Error when getting ticket list ] ", e);
        }
    }

    @Override
    public List<Ticket> getTicketList(String sql, List<TicketSearchValue> searchKeyValues)
            throws DaoException {
        try {
            LOG.debug("TICKET SQL QUERY IN LIST: {}", sql);

            TypedQuery<Ticket> query = em.createQuery(sql, Ticket.class);

            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error getting ticket list paginated ] {}", e.getMessage());
            throw new DaoException("[ Error when getting ticket list ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error getting ticket list paginated ]  {}", e.getMessage());
            throw new DaoException("[ Error when getting ticket list ] ", e);
        }
    }

    @Override
    public List<PreviousReport> getPreviousReportList() throws DaoException {
        try {
            TypedQuery<PreviousReport> query = em.createNamedQuery(UvmsConstants.GET_ALL_PREVIOUS_REPORTS, PreviousReport.class);
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error when getting list of previous reports ] {}", e.getMessage());
            throw new DaoException("[ Error when getting list ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting list of previous reports ] {}", e.getMessage());
            throw new DaoException("[ Error when getting list of previous reports ] ", e);
        }
    }

    // Used by timer to prevent duplicate tickets for passing the reporting
    // deadline
    @Override
    public Ticket getTicketByAssetAndRule(String assetGuid, String ruleGuid) throws DaoException {
        try {
            TypedQuery<Ticket> query = em.createNamedQuery(UvmsConstants.FIND_TICKET_BY_ASSET_AND_RULE, Ticket.class);
            query.setParameter("assetGuid", assetGuid);
            query.setParameter("ruleGuid", ruleGuid);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error when getting ticket by guid ] {}", e.getMessage());
            throw new DaoException("[ Error when getting ticket by guid ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting ticket by guid ] {}", e.getMessage());
            throw new DaoException("[ Error when getting ticket by guid ] ", e);
        }
    }

    // Used by timer to prevent duplicate tickets for passing the reporting
    // deadline
    @Override
    public AlarmReport getAlarmReportByAssetAndRule(String assetGuid, String ruleGuid) throws DaoException {
        try {
            TypedQuery<AlarmReport> query = em.createNamedQuery(UvmsConstants.FIND_ALARM_REPORT_BY_ASSET_GUID_AND_RULE_GUID, AlarmReport.class);
            query.setParameter("assetGuid", assetGuid);
            query.setParameter("ruleGuid", ruleGuid);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error when getting alarm report by guid ] {}", e.getMessage());
            throw new DaoException("[ Error when getting alarm report by guid ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error when getting alarm report by guid ] {}", e.getMessage());
            throw new DaoException("[ Error when getting alarm report by guid ] ", e);
        }
    }

    @Override
    public PreviousReport getPreviousReportByAssetGuid(String assetGuid) {
        try {
            TypedQuery<PreviousReport> query = em.createNamedQuery(UvmsConstants.FIND_PREVIOUS_REPORT_BY_ASSET_GUID, PreviousReport.class);
            query.setParameter("assetGuid", assetGuid);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void updatePreviousReport(PreviousReport report) throws DaoException {
        try {
            em.merge(report);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            LOG.error("[ Error when creating. ] {}", e.getMessage());
            throw new DaoException("[ Error when creating previous report. ]", e);
        } catch (PersistenceException e) {
            LOG.error("[ Error when creating. ] {}", e.getMessage());
            throw new DaoException("[ Error when creating previous report. ]", e);
        } catch (Exception e) {
            LOG.error("[ Error when creating. ] {}", e.getMessage());
            throw new DaoException("[ Error when creating previous report. ]", e);
        }
    }

    @Override
    public long getNumberOfTicketsByRuleGuid(String ruleGuid) throws DaoException {
        try {
            TypedQuery<Long> query = em.createNamedQuery(UvmsConstants.COUNT_ASSETS_NOT_SENDING, Long.class);
            query.setParameter("ruleGuid", ruleGuid);
            return query.getSingleResult();
        } catch (Exception e) {
            LOG.error("[ Error when getting counting tickets by movement. ] {}", e.getMessage());
            throw new DaoException("[ Error when counting tickets by movement. ] ", e);
        }
    }

    @Override
    public List<Template> getAllFactTemplates() throws DaoException {
        try {
            return factTemplateDao.listAllEnabled();
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public void updatedFailedRules(List<String> brIds) throws DaoException {
        try {
            failedRuleDao.updateFailedRules(brIds);
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public void saveValidationMessages(List<RawMessage> rawMessages) throws DaoException {
        try {
            rawMessageDao.saveRawMessages(rawMessages);
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<ValidationMessage> getValidationMessagesById(List<String> ids) throws DaoException {
        try {
            return validationMessageDao.getValidationMessagesById(ids);
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public RuleStatusType checkRuleStatus() throws DaoException {
        try {
            return ruleStatusDao.findRuleStatus();
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public void createRuleStatus(RuleStatus ruleStatus) throws DaoException {
        try {
            ruleStatusDao.createRuleStatus(ruleStatus);
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteRuleStatus() throws DaoException {
        try {
            ruleStatusDao.deleteRuleStatus();
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode) throws DaoException {
        try {
            return fishingGearTypeCharacteristicDao.getFishingGearCharacteristicCodes(fishingGearTypeCode);
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode, boolean onlyMandatory) throws DaoException {
        try {
            return fishingGearTypeCharacteristicDao.getFishingGearCharacteristicCodes(fishingGearTypeCode, onlyMandatory);
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public List<String> getAllFishingGearTypeCodes() throws DaoException {
        try {
            return fishingGearTypeCharacteristicDao.getAllFishingGearTypeCodes();
        } catch (ServiceException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }
}