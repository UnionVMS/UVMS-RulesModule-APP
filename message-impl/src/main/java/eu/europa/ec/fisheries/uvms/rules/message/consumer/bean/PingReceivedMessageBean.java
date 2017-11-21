/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.message.consumer.bean;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

import eu.europa.ec.fisheries.schema.rules.module.v1.PingResponse;
import eu.europa.ec.fisheries.uvms.commons.message.api.Fault;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageProducer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.message.RulesMessageEvent;
import eu.europa.ec.fisheries.uvms.rules.model.constant.FaultCode;
import lombok.extern.slf4j.Slf4j;

@Stateless
@LocalBean
@Slf4j
public class PingReceivedMessageBean {

    @EJB
    private MessageProducer producer;

    public void pingReceived(RulesMessageEvent message, String username) {

        try {
            PingResponse pingResponse = new PingResponse();
            pingResponse.setResponse("pong");
            producer.sendModuleResponseMessage(message.getMessage(), JAXBUtils.marshallJaxBObjectToString(pingResponse));
        } catch (JAXBException e) {
            log.error("[ Error when marshalling ping response ]");
            producer.sendFault(message.getMessage(), new Fault(FaultCode.RULES_EVENT_SERVICE.getCode(), e.getMessage()));
        }

    }
}
