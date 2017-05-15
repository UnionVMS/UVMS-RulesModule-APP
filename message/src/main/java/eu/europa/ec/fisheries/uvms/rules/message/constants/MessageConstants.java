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

public interface MessageConstants {

    String CONNECTION_FACTORY = "ConnectionFactory";
    String CONNECTION_TYPE = "javax.jms.MessageListener";
    String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";
    String RULES_MESSAGE_IN_QUEUE = "jms/queue/UVMSRulesEvent";
    String RULES_MESSAGE_IN_QUEUE_NAME = "UVMSRulesEvent";
    String RULES_RESPONSE_QUEUE = "jms/queue/UVMSRules";
    String QUEUE_DATASOURCE_INTERNAL = "jms/queue/UVMSRulesModel";

    String MOVEMENT_MESSAGE_IN_QUEUE = "jms/queue/UVMSMovementEvent";
    String ASSET_MESSAGE_IN_QUEUE = "jms/queue/UVMSAssetEvent";
    String MOBILE_TERMINAL_MESSAGE_IN_QUEUE = "jms/queue/UVMSMobileTerminalEvent";
    String EXCHANGE_MESSAGE_IN_QUEUE = "jms/queue/UVMSExchangeEvent";
    String USER_MESSAGE_IN_QUEUE = "jms/queue/UVMSUserEvent";
    String SALES_QUEUE = "jms/queue/UVMSSalesEvent";
    String AUDIT_MESSAGE_IN_QUEUE = "jms/queue/UVMSAuditEvent";

    String MDC_IDENTIFIER = "clientName";

    String MODULE_NAME = "rules";

    String ACTIVITY_MESSAGE_IN_QUEUE = "jms/queue/UVMSActivityEvent";
    String MDR_EVENT = "jms/queue/UVMSMdrEvent";
}
