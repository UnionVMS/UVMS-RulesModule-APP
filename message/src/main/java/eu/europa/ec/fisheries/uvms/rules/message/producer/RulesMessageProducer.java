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
package eu.europa.ec.fisheries.uvms.rules.message.producer;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;

import javax.ejb.Local;
import javax.enterprise.event.Observes;
import javax.jms.TextMessage;

@Local
public interface RulesMessageProducer {

    String sendDataSourceMessage(String text, DataSourceQueue queue) throws MessageException;

    /**
     * @param text the message that needs to be sent
     * @param queue the queue it needs to be sent to (DataSourceQueue)
     * @param timeToLiveInMillis the time that a message should live, in milliseconds.
     *                           If the message is not picked up, after this amount of time, the message will be discarded.
     *                           If DeliveryMode is set to "PERSISTENT", the message will be moved to the DLQ.
     *                           Otherwise, the message is just gone, which is acceptable for 'synchronous' queries.
     * @param deliveryMode DeliveryMode.PERSISTENT or DeliveryMode.NON_PERSISTENT
     * @return the correlation id
     * @throws MessageException
     */
    String sendDataSourceMessage(String text, DataSourceQueue queue, long timeToLiveInMillis, int deliveryMode) throws MessageException;

    void sendModuleResponseMessage(TextMessage message, String text) throws MessageException;

    void sendModuleErrorResponseMessage(@Observes @ErrorEvent EventMessage message);
}