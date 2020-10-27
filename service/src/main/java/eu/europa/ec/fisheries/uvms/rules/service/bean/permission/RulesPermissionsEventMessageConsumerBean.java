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

package eu.europa.ec.fisheries.uvms.rules.service.bean.permission;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.UUID;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.context.PropagateFluxEnvelopeData;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFaReportServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message driven bean that receives all messages that
 * have no message selector.
 */
@MessageDriven(mappedName = "jms/queue/UVMSRulesPermissionEvent", activationConfig = {
        @ActivationConfigProperty(propertyName = MessageConstants.MESSAGING_TYPE_STR, propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_TYPE_STR, propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_STR, propertyValue = "UVMSRulesPermissionEvent"),
        @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "messageSelector IS NULL"),
        @ActivationConfigProperty(propertyName = "maxMessagesPerSessions", propertyValue = "3"),
        @ActivationConfigProperty(propertyName = "initialRedeliveryDelay", propertyValue = "120000"),
        @ActivationConfigProperty(propertyName = "maximumRedeliveries", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "maxSessions", propertyValue = "5"),
})
public class RulesPermissionsEventMessageConsumerBean implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(RulesPermissionsEventMessageConsumerBean.class);

    @Inject
    private RulesFaReportServiceBean rulesFaReportServiceBean;

    @Override
    @PropagateFluxEnvelopeData
    public void onMessage(Message message) {
        LOG.debug("Message received in rules. Times redelivered: {}", getTimesRedelivered(message));
        TextMessage textMessage = (TextMessage) message;
        try {
            PermissionData permissionData = JAXBMarshaller.unmarshallTextMessage(textMessage, PermissionData.class);
            permissionData.setRequestPermitted(textMessage.getBooleanProperty("isPermitted"));
            rulesFaReportServiceBean.completeIncomingFLUXFAReportEvaluation(permissionData);
        } catch (NullPointerException | RulesModelMarshallException | JMSException e) {
            LOG.error("[ Error when receiving message in rules: {}]", e.getMessage());
        }
    }

    private int getTimesRedelivered(Message message) {
        try {
            return (message.getIntProperty("JMSXDeliveryCount") - 1);
        } catch (Exception e) {
            return 0;
        }
    }
}
