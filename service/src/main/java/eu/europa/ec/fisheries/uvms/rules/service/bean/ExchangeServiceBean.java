/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;

@Stateless
@LocalBean
@Slf4j
public class ExchangeServiceBean {

    @EJB
    private RulesMessageProducer rulesProducer;
    public void updateExchangeMessage(String logGuid, ExchangeLogStatusTypeType statusType) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, statusType);
            log.debug("Message to exchange to update status : {}", statusMsg);
            rulesProducer.sendDataSourceMessage(statusMsg, DataSourceQueue.EXCHANGE);
        } catch (ExchangeModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }
}
