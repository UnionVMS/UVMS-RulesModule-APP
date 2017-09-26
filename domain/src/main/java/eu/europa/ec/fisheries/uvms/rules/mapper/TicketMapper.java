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

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.entity.Ticket;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LocalBean
@Stateless
public class TicketMapper {

    private final static Logger LOG = LoggerFactory.getLogger(TicketMapper.class);

    public static TicketType toTicketType(TicketType ticketType, Ticket ticketEntity) throws DaoMappingException {
        if (ticketEntity == null) {
            return null;
        }

        try {
            ticketType.setAssetGuid(ticketEntity.getAssetGuid());
            ticketType.setMobileTerminalGuid(ticketEntity.getMobileTerminalGuid());
            ticketType.setChannelGuid(ticketEntity.getChannelGuid());
            ticketType.setGuid(ticketEntity.getGuid());
            ticketType.setStatus(TicketStatusType.valueOf(ticketEntity.getStatus()));
            ticketType.setOpenDate(DateUtils.dateToString(ticketEntity.getCreatedDate()));
            ticketType.setUpdated(DateUtils.dateToString(ticketEntity.getUpdated()));
            ticketType.setUpdatedBy(ticketEntity.getUpdatedBy());
            ticketType.setRuleGuid(ticketEntity.getRuleGuid());
            ticketType.setMovementGuid(ticketEntity.getMovementGuid());
            ticketType.setRuleName(ticketEntity.getRuleName());
            ticketType.setRecipient(ticketEntity.getRecipient());
            if (ticketEntity.getTicketCount() != null) {
                ticketType.setTicketCount(ticketEntity.getTicketCount());
            }

            return ticketType;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to model. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to model. ]", e);
        }
    }

    public static Ticket toTicketEntity(Ticket ticketEntity, TicketType ticketType) throws DaoMappingException {
        try {
            ticketEntity.setAssetGuid(ticketType.getAssetGuid());
            ticketEntity.setMobileTerminalGuid(ticketType.getMobileTerminalGuid());
            ticketEntity.setChannelGuid(ticketType.getChannelGuid());
            ticketEntity.setGuid(ticketType.getGuid());
            ticketEntity.setStatus(ticketType.getStatus().name());
            ticketEntity.setCreatedDate(DateUtils.stringToDate(ticketType.getOpenDate()));
            ticketEntity.setRuleGuid(ticketType.getRuleGuid());
            ticketEntity.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
            ticketEntity.setUpdatedBy(ticketType.getUpdatedBy());
            ticketEntity.setMovementGuid(ticketType.getMovementGuid());
            ticketEntity.setRuleName(ticketType.getRuleName());
            ticketEntity.setRecipient(ticketType.getRecipient());

            return ticketEntity;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to entity. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to entity. ]", e);
        }
    }

    public static Ticket toTicketEntity(TicketType ticketType) throws DaoMappingException {
        Ticket ticketEntity = new Ticket();
        return toTicketEntity(ticketEntity, ticketType);
    }

    public static TicketType toTicketType(Ticket ticketEntity) throws DaoMappingException {
        TicketType ticketType = new TicketType();
        return toTicketType(ticketType, ticketEntity);
    }
}