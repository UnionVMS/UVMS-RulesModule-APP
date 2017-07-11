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
package eu.europa.ec.fisheries.uvms.rules.message.constants;

public class MessageConstants {

    private MessageConstants() {
        super();
    }

    public static final String CONNECTION_FACTORY = "ConnectionFactory";
    public static final String CONNECTION_TYPE = "javax.jms.MessageListener";
    public static final String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";
    public static final String RULES_MESSAGE_IN_QUEUE = "jms/queue/UVMSRulesEvent";
    public static final String RULES_MESSAGE_IN_QUEUE_NAME = "UVMSRulesEvent";
    public static final String RULES_RESPONSE_QUEUE = "jms/queue/UVMSRules";
    public static final String QUEUE_DATASOURCE_INTERNAL = "jms/queue/UVMSRulesModel";
    public static final String SALES_QUEUE = "jms/queue/UVMSSalesEvent";

    public static final String MOVEMENT_MESSAGE_IN_QUEUE = "jms/queue/UVMSMovementEvent";
    public static final String ASSET_MESSAGE_IN_QUEUE = "jms/queue/UVMSAssetEvent";
    public static final String MOBILE_TERMINAL_MESSAGE_IN_QUEUE = "jms/queue/UVMSMobileTerminalEvent";
    public static final String EXCHANGE_MESSAGE_IN_QUEUE = "jms/queue/UVMSExchangeEvent";
    public static final String USER_MESSAGE_IN_QUEUE = "jms/queue/UVMSUserEvent";
    public static final String AUDIT_MESSAGE_IN_QUEUE = "jms/queue/UVMSAuditEvent";

    public static final String MDC_IDENTIFIER = "clientName";

    public static final String MODULE_NAME = "rules";

    public static final String ACTIVITY_MESSAGE_IN_QUEUE = "jms/queue/UVMSActivityEvent";
    public static final String MDR_EVENT = "jms/queue/UVMSMdrEvent";
}
