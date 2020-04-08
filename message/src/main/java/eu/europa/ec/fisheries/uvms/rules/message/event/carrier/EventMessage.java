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
package eu.europa.ec.fisheries.uvms.rules.message.event.carrier;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;

import javax.jms.TextMessage;

public class EventMessage {

    private TextMessage jmsMessage;
    private RulesFault fault;
    private RulesBaseRequest rulesBaseRequest;

    public EventMessage(TextMessage jmsMessage) {
        this.jmsMessage = jmsMessage;
    }

    public EventMessage(TextMessage jmsMessage,RulesBaseRequest request) {
        this.jmsMessage = jmsMessage;
        this.rulesBaseRequest = request;
    }

    public EventMessage(TextMessage jmsMessage, RulesFault fault) {
        this.jmsMessage = jmsMessage;
        this.fault = fault;
    }

    public RulesBaseRequest getRulesBaseRequest() {
        return rulesBaseRequest;
    }

    public RulesFault getFault() {
        return fault;
    }

    public void setFault(RulesFault fault) {
        this.fault = fault;
    }

    public TextMessage getJmsMessage() {
        return jmsMessage;
    }

    public void setJmsMessage(TextMessage jmsMessage) {
        this.jmsMessage = jmsMessage;
    }

}