/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.message.consumer.bean;

import static eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils.unMarshallMessage;

import javax.jms.TextMessage;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.uvms.rules.message.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.RulesMessageEvent;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.constant.FaultCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class RulesMessageBase {

    abstract RulesMessageProducer getProducer();

    public void processMessage(TextMessage message, String username, Class clazz){

        try {

            RulesBaseRequest ruleRequest = unMarshallMessage(message.getText(), clazz);
            RulesMessageEvent event = new RulesMessageEvent(message, ruleRequest);
            processMessage(event, username);

        } catch (Exception e){
            log.error("[ Error when fetching rule by guid ] {}", e.getMessage());
            RulesFault rulesFault = new RulesFault();
            rulesFault.setCode(FaultCode.RULES_EVENT_SERVICE.getCode());
            rulesFault.setMessage(e.getMessage());
            getProducer().sendModuleErrorResponseMessage(new EventMessage(message, rulesFault));
        }

    }

    abstract void processMessage(RulesMessageEvent message, String username) throws Exception;

}
