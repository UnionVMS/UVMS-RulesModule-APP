package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;


import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMarshallException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.wsdl.asset.module.FindAssetHistoriesByCfrModuleResponse;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.TextMessage;
import java.util.List;

@Singleton
public class AssetServiceBeanHelper {

    public static final long TIME_TO_WAIT_FOR_A_RESPONSE = 30000L;

    @EJB
    private RulesMessageProducer messageProducer;

    @EJB
    private RulesResponseConsumer messageConsumer;

    protected List<Asset> receiveMessageFromAsset(String correlationId) throws MessageException, AssetModelMarshallException {
        TextMessage receivedMessage = messageConsumer.getMessage(correlationId, TextMessage.class, TIME_TO_WAIT_FOR_A_RESPONSE);
        return unmarshal(receivedMessage);
    }

    protected String sendMessageToAsset(String request) throws MessageException {
        return messageProducer.sendDataSourceMessage(request, DataSourceQueue.ASSET, TIME_TO_WAIT_FOR_A_RESPONSE + 1000L);
    }

    protected List<Asset> unmarshal(TextMessage message) throws AssetModelMarshallException {
        FindAssetHistoriesByCfrModuleResponse findAssetHistoriesByCfrModuleResponse = JAXBMarshaller.unmarshallTextMessage(message, FindAssetHistoriesByCfrModuleResponse.class);

        return findAssetHistoriesByCfrModuleResponse.getAssetHistories();
    }

    public List<Asset> findHistoryOfAssetByCfr(String cfr) throws MessageException, AssetModelMarshallException {
        String checkForUniqueIdRequest = AssetModuleRequestMapper.createFindAssetByCfrRequest(cfr);
        String correlationID = sendMessageToAsset(checkForUniqueIdRequest);

        return receiveMessageFromAsset(correlationID);
    }
}
