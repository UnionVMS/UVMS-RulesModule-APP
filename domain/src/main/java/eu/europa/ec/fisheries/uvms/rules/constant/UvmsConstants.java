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
package eu.europa.ec.fisheries.uvms.rules.constant;

public final class UvmsConstants {
    // Queries
    public static final String FIND_OPEN_ALARM_REPORT_BY_MOVEMENT_GUID = "AlarmReport.findByMovementGuid";
    public static final String FIND_ALARM_REPORT_BY_ID = "AlarmReport.findById";
    public static final String FIND_ALARM_BY_GUID = "AlarmReport.findByGuid";
    public static final String COUNT_OPEN_ALARMS = "AlarmReport.countOpenAlarms";
    public static final String COUNT_OPEN_TICKETS = "AlarmReport.countOpenTickets";
    public static final String COUNT_OPEN_TICKETS_NO_RULES = "AlarmReport.countOpenTicketsNoRules";
    public static final String FIND_ALARM_REPORT_BY_ASSET_GUID_AND_RULE_GUID = "AlarmReport.findByAssetGuidRuleGuid";
    public static final String GET_RUNNABLE_CUSTOM_RULES = "CustomRule.getValidCustomRule";
    public static final String LIST_CUSTOM_RULES_BY_USER = "CustomRule.listCustomRules";  // rule engine
    public static final String FIND_CUSTOM_RULE_BY_GUID = "CustomRule.findByGuid";
    public static final String FIND_CUSTOM_RULE_GUID_FOR_TICKETS = "CustomRule.findRuleGuidsForTickets";
    public static final String FIND_TICKET_BY_GUID = "Ticket.findByGuid";
    public static final String FIND_TICKET_BY_ASSET_AND_RULE = "Ticket.findByAssetGuid";
    public static final String FIND_TICKETS_BY_MOVEMENTS = "Ticket.findByMovementGuids";
    public static final String COUNT_TICKETS_BY_MOVEMENTS = "Ticket.countByMovementGuids";
    public static final String GET_ALL_PREVIOUS_REPORTS = "PreviousReport.findAll";
    public static final String FIND_PREVIOUS_REPORT_BY_ASSET_GUID = "PreviousReport.findByAssetGuid";
    public static final String FIND_ALL_SANITY_RULES = "SanityRule.findAll";
    public static final String COUNT_ASSETS_NOT_SENDING = "Ticket.countAssetsNotSending";
    // JMS
    public static final String QUEUE_DOMAIN_MODEL = "jms/queue/UVMSRulesModel";
    public static final String QUEUE_DOMAIN_MODEL_NAME = "UVMSRulesModel";
    public static final String CONNECTION_FACTORY = "ConnectionFactory";
    public static final String CONNECTION_TYPE = "javax.jms.MessageListener";
    public static final String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";
    // Rule for not sending transponders
    public static final String ASSET_NOT_SENDING_RULE = "Asset not sending";
    public static final String RESPONSEMESSAGERULE_FIND_ALL = "ResponsemessageRule.findAll";


    private UvmsConstants() {
    }

}