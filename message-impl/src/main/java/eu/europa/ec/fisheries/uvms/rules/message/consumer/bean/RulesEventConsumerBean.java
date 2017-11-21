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

package eu.europa.ec.fisheries.uvms.rules.message.consumer.bean;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.UUID;

import eu.europa.ec.fisheries.schema.rules.module.v1.PingResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportRequest;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@MessageDriven(mappedName = MessageConstants.RULES_MESSAGE_IN_QUEUE, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE_NAME),
        @ActivationConfigProperty(propertyName = "destinationJndiName", propertyValue = MessageConstants.RULES_MESSAGE_IN_QUEUE),
        @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = MessageConstants.CONNECTION_FACTORY)
})
@Slf4j
public class RulesEventConsumerBean implements MessageListener {

    @EJB
    private SetMovementReportRequestBean movementReportRequestBean;

    @EJB
    private PingReceivedBean pingReceivedBean;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {
        String id = UUID.randomUUID().toString();
        MDC.put(MessageConstants.MDC_IDENTIFIER, id);

        log.info("Message received in rules");

        TextMessage textMessage = (TextMessage) message;
        try {

            RulesBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, RulesBaseRequest.class);
            RulesModuleMethod method = request.getMethod();
            String username = request.getUsername();

            switch (method) {
                case SET_MOVEMENT_REPORT:
                    SetMovementReportRequest reportRequest = JAXBUtils.unMarshallMessage(textMessage.getText(), SetMovementReportRequest.class);
                    RulesMessageEvent event = new RulesMessageEvent(textMessage, reportRequest);
                    movementReportRequestBean.setMovementReportReceived(event, username);
                    break;

                case PING:


                    pingReceivedBean.pingReceived();
                    JAXBUtils.unMarshallMessage(textMessage.getText(), )
                    try {
                        PingResponse pingResponse = new PingResponse();
                        pingResponse.setResponse("pong");
                        String pingResponseText = JAXBMarshaller.marshallJaxBObjectToString(pingResponse);
                        producer.sendModuleResponseMessage(eventMessage.getJmsMessage(), pingResponseText);
                    } catch (RulesModelMarshallException | MessageException e) {
                        LOG.error("[ Error when responding to ping. ] {}", e.getMessage());
                        errorEvent.fire(eventMessage);
                    }
                    break;
                case GET_CUSTOM_RULE:
                    break;
                case GET_TICKETS_BY_MOVEMENTS:
                    break;
                case COUNT_TICKETS_BY_MOVEMENTS:
                    break;
                case GET_TICKETS_AND_RULES_BY_MOVEMENTS:
                    break;
                case SET_FLUX_FA_REPORT :
                    break;
                case SET_FLUX_MDR_SYNC_REQUEST :
                    break;
                case GET_FLUX_MDR_SYNC_RESPONSE :
                    break;
                case RECEIVE_SALES_QUERY:
                    break;
                case RECEIVE_SALES_RESPONSE:
                    break;
                case RECEIVE_SALES_REPORT:
                    break;
                case SEND_SALES_REPORT:
                    break;
                case SEND_SALES_RESPONSE:
                    break;

                default:
                    log.error("[ Request method '{}' is not implemented ]", request.getMethod().name());
                  //  errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Method not implemented:" + request.getMethod().name())));
                    break;
            }
            if (request.getMethod() == null) {
                log.error("[ Request method is null ]");
               // errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Error when receiving message in rules: Request method is null")));
            }

        } catch (JAXBException | JMSException e) {
            log.error("[ Error when receiving message in AssetModule. ]");
            //assetErrorEvent.fire(new AssetMessageEvent(textMessage, AssetModuleResponseMapper.createFaultMessage(eu.europa.ec.fisheries.uvms.asset.model.constants.FaultCode.ASSET_MESSAGE, "Method not implemented")));

        } catch (NullPointerException | RulesModelMarshallException e) {
            log.error("[ Error when receiving message in rules: {}]", e.getMessage());
           // errorEvent.fire(new EventMessage(textMessage, ModuleResponseMapper.createFaultMessage(FaultCode.RULES_MESSAGE, "Error when receiving message in rules:" + e.getMessage())));
        } finally {
            MDC.remove(MessageConstants.MDC_IDENTIFIER);
        }
    }

}
