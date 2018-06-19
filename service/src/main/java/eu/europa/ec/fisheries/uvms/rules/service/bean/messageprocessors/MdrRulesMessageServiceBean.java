package eu.europa.ec.fisheries.uvms.rules.service.bean.messageprocessors;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.*;

@Slf4j
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MdrRulesMessageServiceBean {

    @EJB
    private RulesMessageProducer producer;

    /*
     * Maps a Request String to a eu.europa.ec.fisheries.schema.exchange.module.v1.SetFLUXMDRSyncMessageRequest
     * to send a message to ExchangeModule
     */
    public void mapAndSendFLUXMdrRequestToExchange(String request, String fr) {
        String exchangerStrReq;
        try {
            exchangerStrReq = ExchangeModuleRequestMapper.createFluxMdrSyncEntityRequest(request, StringUtils.EMPTY, fr);
            if (StringUtils.isNotEmpty(exchangerStrReq)) {
                producer.sendDataSourceMessage(exchangerStrReq, DataSourceQueue.EXCHANGE);
            } else {
                log.error("ERROR : REQUEST TO BE SENT TO EXCHANGE MODULE RESULTS NULL. NOT SENDING IT!");
            }
        } catch (ExchangeModelMarshallException e) {
            log.error("Unable to marshall SetFLUXMDRSyncMessageRequest in RulesServiceBean.mapAndSendFLUXMdrRequestToExchange(String) : " + e.getMessage());
        } catch (MessageException e) {
            log.error("Unable to send SetFLUXMDRSyncMessageRequest to ExchangeModule : " + e.getMessage());
        }
    }

    public void mapAndSendFLUXMdrResponseToMdrModule(String request) {
        String mdrSyncResponseReq;
        try {
            mdrSyncResponseReq = MdrModuleMapper.createFluxMdrSyncEntityRequest(request, StringUtils.EMPTY);
            if (StringUtils.isNotEmpty(mdrSyncResponseReq)) {
                producer.sendDataSourceMessage(mdrSyncResponseReq, DataSourceQueue.MDR_EVENT);
            } else {
                log.error("ERROR : REQUEST TO BE SENT TO MDR MODULE RESULTS NULL. NOT SENDING IT!");
            }
        } catch (MdrModelMarshallException e) {
            log.error("Unable to marshall SetFLUXMDRSyncMessageResponse in RulesServiceBean.mapAndSendFLUXMdrResponseToMdrModule(String) : " + e.getMessage());
        } catch (MessageException e) {
            log.error("Unable to send SetFLUXMDRSyncMessageResponse to MDR Module : " + e.getMessage());
        }
    }
}
