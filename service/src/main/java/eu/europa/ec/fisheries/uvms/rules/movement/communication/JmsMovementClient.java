/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.movement.communication;

import eu.europa.ec.fisheries.schema.movement.module.v1.FindRawMovementsRequest;
import eu.europa.ec.fisheries.schema.movement.module.v1.FindRawMovementsResponse;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementModelException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.RulesResponseConsumerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesProducerBean;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceTechnicalException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Queue;
import javax.jms.TextMessage;

/**
 * JMS implementation of the {@link MovementClient}.
 */
@ApplicationScoped
public class JmsMovementClient implements MovementClient {

    private RulesProducerBean rulesProducerBean;
    private RulesResponseConsumerBean rulesResponseConsumerBean;
    private Queue movementQueue;

    /**
     * Injection constructor.
     *
     * @param rulesProducerBean         The rules module producer bean
     * @param rulesResponseConsumerBean The rules module consumer bean
     * @param movementQueue            The movement specific queue created by ManagedObjectsProducer
     */
    @Inject
    public JmsMovementClient(RulesProducerBean rulesProducerBean, RulesResponseConsumerBean rulesResponseConsumerBean, @MovementQueue Queue movementQueue) {
        this.rulesProducerBean = rulesProducerBean;
        this.rulesResponseConsumerBean = rulesResponseConsumerBean;
        this.movementQueue = movementQueue;
    }

    /**
     * Constructor for frameworks.
     */
    @SuppressWarnings("unused")
    JmsMovementClient() {
        // NOOP
    }

    @Override
    public FindRawMovementsResponse findRawMovements(FindRawMovementsRequest request) throws MovementModelException {
        try {
            String correlationId = rulesProducerBean.sendMessageToSpecificQueue(JAXBMarshaller.marshallJaxBObjectToString(request), movementQueue, rulesResponseConsumerBean.getDestination());
            FindRawMovementsResponse response = null;
            if (correlationId != null) {
                TextMessage textMessage = rulesResponseConsumerBean.getMessage(correlationId, TextMessage.class);
                response = JAXBMarshaller.unmarshallTextMessage(textMessage, FindRawMovementsResponse.class);
            }
            return response;
        } catch (MessageException e) {
            throw new RulesServiceTechnicalException("error while calling findRawMovements", e);
        }
    }
}
