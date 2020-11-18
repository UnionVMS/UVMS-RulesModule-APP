/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.mdr;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesExchangeProducerBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.mdr.gateway.RulesMdrGateway;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.mdr.communication.SetFLUXMDRSyncMessageResponse;
import un.unece.uncefact.data.standard.mdr.response.FLUXMDRReturnMessage;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;

@Slf4j
@Stateless
@LocalBean
public class MdrRulesMessageServiceBean {

    @EJB
    private RulesResponseConsumer rulesConsumer;

    @EJB
    private RulesExchangeProducerBean exchangeProducer;

    @Inject
    private RulesMdrGateway rulesMdrGateway;

    /*
     * Maps a Request String to a eu.europa.ec.fisheries.schema.exchange.module.v1.SetFLUXMDRSyncMessageRequest
     * to send a message to ExchangeModule
     */
    public void mapAndSendFLUXMdrRequestToExchange(String request, String fr) {
        String exchangerStrReq;
        try {
            exchangerStrReq = ExchangeModuleRequestMapper.createFluxMdrSyncEntityRequest(request, StringUtils.EMPTY, fr);
            if (StringUtils.isNotEmpty(exchangerStrReq)) {
                exchangeProducer.sendModuleMessage(exchangerStrReq, rulesConsumer.getDestination());
            } else {
                log.error("REQUEST TO BE SENT TO EXCHANGE MODULE RESULTS NULL. NOT SENDING IT!");
            }
        } catch (ExchangeModelMarshallException e) {
            log.error("Unable to marshall SetFLUXMDRSyncMessageRequest in RulesServiceBean.mapAndSendFLUXMdrRequestToExchange(String) : " + e.getMessage());
        } catch (MessageException e) {
            log.error("Unable to send SetFLUXMDRSyncMessageRequest to ExchangeModule : " + e.getMessage());
        }
    }

    public void mapAndSendFLUXMdrResponseToMdrModule(String request) {

        SetFLUXMDRSyncMessageResponse fluxMdrSyncEntityRequest = MdrModuleMapper.createFluxMdrSyncEntityRequest(request);
        try {
            FLUXMDRReturnMessage returnMessage = JAXBUtils.unMarshallMessage(fluxMdrSyncEntityRequest.getRequest(), FLUXMDRReturnMessage.class);
            rulesMdrGateway.syncMdrEntityMessage(returnMessage);
        } catch (JAXBException e) {
            log.error("[ERROR] Error while attempting to Unmarshall Flux Response Object (XML MDR Entity)! Maybe not a FLUXMDRReturnMessage!!");
        }
    }
}
