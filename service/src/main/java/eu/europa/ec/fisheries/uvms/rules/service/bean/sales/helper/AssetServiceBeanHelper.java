package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;


import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMarshallException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.wsdl.asset.module.ActivityRulesAssetModuleResponse;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteria;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.asset.types.ConfigSearchField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Singleton
@Slf4j
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
        return messageProducer.sendDataSourceMessage(request, DataSourceQueue.ASSET,TIME_TO_WAIT_FOR_A_RESPONSE + 1000L);
    }

    protected List<Asset> unmarshal(TextMessage message) throws AssetModelMarshallException {
        ActivityRulesAssetModuleResponse activityRulesAssetModuleResponse = JAXBMarshaller.unmarshallTextMessage(message, ActivityRulesAssetModuleResponse.class);
        return activityRulesAssetModuleResponse.getAssetHistories();
    }

    public List<Asset> findHistoryOfAssetByCfr(String cfr) throws MessageException, AssetModelMarshallException {
        String checkForUniqueIdRequest = AssetModuleRequestMapper.createFindAssetByCfrRequest(cfr);
        String correlationID = sendMessageToAsset(checkForUniqueIdRequest);

        return receiveMessageFromAsset(correlationID);
    }

    public List<Asset> findHistoryOfAssetBy(String reportDate, String cfr, String regCountry, String ircs, String extMark, String iccat) {
        AssetListCriteria assetListCriteria = new AssetListCriteria();
        if (StringUtils.isNotEmpty(reportDate)){
            createCriteriaPair(reportDate, assetListCriteria, ConfigSearchField.DATE);
        }
        else {
            return new ArrayList<>();
        }
        if (StringUtils.isNotEmpty(cfr)){
            createCriteriaPair(cfr, assetListCriteria, ConfigSearchField.CFR);
        }
        else {
            if (StringUtils.isNotEmpty(regCountry)){
                createCriteriaPair(regCountry, assetListCriteria, ConfigSearchField.FLAG_STATE);
            }
            else {
                return new ArrayList<>();
            }
            if (StringUtils.isNotEmpty(ircs)){
                createCriteriaPair(ircs, assetListCriteria, ConfigSearchField.IRCS);
            }

            else if (StringUtils.isNotEmpty(extMark)){
                createCriteriaPair(extMark.replace("-", "").toUpperCase(), assetListCriteria, ConfigSearchField.EXTERNAL_MARKING);
            }
            else if (StringUtils.isNotEmpty(iccat)){
                createCriteriaPair(iccat, assetListCriteria, ConfigSearchField.ICCAT);
            }
            else {
                return new ArrayList<>();
            }
        }

        String createActivityRulesAssetModuleRequest;

        try {
            createActivityRulesAssetModuleRequest = AssetModuleRequestMapper.createActivityRulesAssetModuleRequest(assetListCriteria);
            String correlationID = sendMessageToAsset(createActivityRulesAssetModuleRequest);
            return receiveMessageFromAsset(correlationID);
        } catch (AssetModelMarshallException | MessageException e) {
            log.warn(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private void createCriteriaPair(String reportDate, AssetListCriteria assetListCriteria, ConfigSearchField date) {
        AssetListCriteriaPair assetListCriteriaPair = new AssetListCriteriaPair();
        assetListCriteriaPair.setKey(date);
        assetListCriteriaPair.setValue(reportDate);
        assetListCriteria.getCriterias().add(assetListCriteriaPair);
    }
}
