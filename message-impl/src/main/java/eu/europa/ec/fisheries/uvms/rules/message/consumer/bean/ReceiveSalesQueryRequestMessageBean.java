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

import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesQueryRequest;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageProducer;
import eu.europa.ec.fisheries.uvms.rules.message.RulesMessageEvent;
import eu.europa.ec.fisheries.uvms.rules.service.MessageService;
import lombok.extern.slf4j.Slf4j;

@Stateless
@LocalBean
@Slf4j
public class ReceiveSalesQueryRequestMessageBean extends RulesMessageBase {

    @EJB
    private MessageService rulesService;

    @EJB
    private MessageProducer producer;

    @Override
    MessageProducer getProducer() {
        return producer;
    }

    @Override
    void processMessage(RulesMessageEvent message, String username) throws Exception {
        ReceiveSalesQueryRequest receiveSalesQueryRequest = (ReceiveSalesQueryRequest) message.getRequest();
        rulesService.receiveSalesQueryRequest(receiveSalesQueryRequest);
    }
}
