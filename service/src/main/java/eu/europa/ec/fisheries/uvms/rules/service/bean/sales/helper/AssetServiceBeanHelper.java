package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;


import javax.ejb.Singleton;

@Singleton
public class AssetServiceBeanHelper {
//
//    @EJB
//    private RulesMessageProducer messageProducer;
//
//    @EJB
//    private RulesResponseConsumer messageConsumer;
//
//    protected List<Asset> receiveMessageFromAsset(String correlationId) throws MessageException, AssetModelMarshallException {
//        TextMessage receivedMessage = messageConsumer.getMessage(correlationId, TextMessage.class, 30000L);
//        return unmarshal(receivedMessage);
//    }
//
//    protected String sendMessageToAsset(String request) throws MessageException {
//        return messageProducer.sendDataSourceMessage(request, DataSourceQueue.ASSET);
//    }
//
//    protected List<Asset> unmarshal(TextMessage message) throws AssetModelMarshallException {
//        FindAssetHistoriesByCfrModuleResponse findAssetHistoriesByCfrModuleResponse = JAXBMarshaller.unmarshallTextMessage(message, FindAssetHistoriesByCfrModuleResponse.class);
//
//        return findAssetHistoriesByCfrModuleResponse.getAssetHistories();
//    }
//
//    public List<Asset> findHistoryOfAssetByCfr(String cfr) throws MessageException, AssetModelMarshallException {
//        String checkForUniqueIdRequest = AssetModuleRequestMapper.createFindAssetByCfrRequest(cfr);
//        String correlationID = sendMessageToAsset(checkForUniqueIdRequest);
//
//        return receiveMessageFromAsset(correlationID);
//    }
}
