/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.activity;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMapperException;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SyncAsyncRequestType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesActivityProducerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesActivitySubsCheckProducer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesResponseQueueProducer;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.wsdl.subscription.module.SubscriptionPermissionAnswer;
import eu.europa.ec.fisheries.wsdl.subscription.module.SubscriptionPermissionResponse;
import eu.europa.fisheries.uvms.subscription.model.mapper.SubscriptionModuleResponseMapper;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

/**
 * Created by kovian on 17/07/2016.
 */
@Stateless
@LocalBean
@Slf4j
public class RulesActivityServiceBean {

    @EJB
    private RulesResponseConsumer rulesConsumer;

    @EJB
    private RulesActivityProducerBean activityProducer;

    @EJB
    private RulesActivitySubsCheckProducer rulesActivityServiceBean;

    @EJB
    private RulesResponseQueueProducer rulesResponseProducer;


    public boolean checkSubscriptionPermissions(String request, MessageType type) {
        try {
            String requestStr = ActivityModuleRequestMapper.mapToSubscriptionRequest(request, type);
            log.debug("Send MapToSubscriptionRequest to Activity");
            String corrId = rulesActivityServiceBean.sendModuleMessage(requestStr, rulesResponseProducer.getDestination());
            TextMessage message = rulesConsumer.getMessage(corrId,300000L);
            log.debug("Received response message from Subscription.");
            SubscriptionPermissionResponse subscriptionPermissionResponse = SubscriptionModuleResponseMapper.mapToSubscriptionPermissionResponse(message.getText());
            SubscriptionPermissionAnswer subscriptionCheck = subscriptionPermissionResponse.getSubscriptionCheck();
            return SubscriptionPermissionAnswer.YES.equals(subscriptionCheck);
        } catch (ActivityModelMapperException | JMSException | JAXBException | MessageException e) {
            log.error("[ERROR] while trying to check subscription permissions (Is [[[- Subscriptions -]]] module Deployed?).. Going to assume the request doesn't have permissions!!");
        }
        return false;
    }

    public void sendRequestToActivity(String activityMsgStr, PluginType pluginType, MessageType messageType, String exchangeLogGuid, String permissionData) {
        try {
            String activityRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportOrQueryMessageRequest(activityMsgStr, pluginType.toString(), messageType, SyncAsyncRequestType.ASYNC, exchangeLogGuid, permissionData);
            activityProducer.sendModuleMessage(activityRequest, rulesConsumer.getDestination());
        } catch (ActivityModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

}
