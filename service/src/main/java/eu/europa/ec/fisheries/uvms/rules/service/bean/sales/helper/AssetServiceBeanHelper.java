package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;


import javax.ejb.Singleton;

@Singleton
public class AssetServiceBeanHelper {

//    public static final long TIME_TO_WAIT_FOR_A_RESPONSE = 30000L;
//
//    @EJB
//    private RulesMessageProducer messageProducer;
//
//    @EJB
//    private RulesResponseConsumer messageConsumer;
//
//    protected List<Asset> receiveMessageFromAsset(String correlationId) throws MessageException, AssetModelMarshallException {
//        TextMessage receivedMessage = messageConsumer.getMessage(correlationId, TextMessage.class, TIME_TO_WAIT_FOR_A_RESPONSE);
//        return unmarshal(receivedMessage);
//    }
//
//    protected String sendMessageToAsset(String request) throws MessageException {
//        return messageProducer.sendDataSourceMessage(request, DataSourceQueue.ASSET, TIME_TO_WAIT_FOR_A_RESPONSE + 1000L);
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
