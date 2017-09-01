package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;


import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesCache;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.SalesModuleRequestMapper;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

@Singleton
public class SalesServiceBeanHelper {

    @EJB
    RulesMessageProducer messageProducer;

    @EJB
    RulesResponseConsumer messageConsumer;

    @EJB
    SalesCache cache;


    protected FLUXSalesReportMessage receiveMessageFromSales(String correlationId) throws MessageException, JMSException, SalesMarshallException {
        TextMessage receivedMessageAsTextMessage = messageConsumer.getMessage(correlationId, TextMessage.class);
        String receivedMessageAsString = receivedMessageAsTextMessage.getText();
        return unmarshal(receivedMessageAsString);
    }

    protected String sendMessageToSales(String request) throws MessageException {
        return messageProducer.sendDataSourceMessage(request, DataSourceQueue.SALES);
    }


    protected FLUXSalesReportMessage unmarshal(String message) throws SalesMarshallException {
        FindReportByIdResponse findReportByIdResponse = JAXBMarshaller.unmarshallString(message, FindReportByIdResponse.class);
        return JAXBMarshaller.unmarshallString(findReportByIdResponse.getReport(), FLUXSalesReportMessage.class);
    }

    public Optional<FLUXSalesReportMessage> findReport(String guid) throws MessageException, SalesMarshallException, JMSException {
        // If the report was retrieved earlier, we return the cached version
        Optional<FLUXSalesReportMessage> cachedMessageOptional = cache.retrieveMessageFromCache(guid);
        if (cachedMessageOptional.isPresent()) {
            return cachedMessageOptional;
        }

        // Report not in cache, send message to sales and wait for a reply
        String findReportByIdRequest = SalesModuleRequestMapper.createFindReportByIdRequest(guid);

        String correlationId = sendMessageToSales(findReportByIdRequest);
        FLUXSalesReportMessage originalReport = receiveMessageFromSales(correlationId);

        // Cache the result
        cache.cacheMessage(guid, originalReport);

        return Optional.fromNullable(originalReport);
    }

    public boolean areAnyOfTheseIdsNotUnique(List<String> id, UniqueIDType type) throws SalesMarshallException, MessageException, JMSException {
        String checkForUniqueIdRequest = SalesModuleRequestMapper.createCheckForUniqueIdRequest(id, type);
        String correlationID = sendMessageToSales(checkForUniqueIdRequest);

        TextMessage receivedMessageAsTextMessage = messageConsumer.getMessage(correlationID, TextMessage.class);

        CheckForUniqueIdResponse response = JAXBMarshaller.unmarshallString(receivedMessageAsTextMessage.getText(), CheckForUniqueIdResponse.class);
        return !response.isUnique();
    }
}
