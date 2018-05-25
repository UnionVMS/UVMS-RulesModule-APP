package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;


import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.CheckForUniqueIdResponse;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.FindReportByIdResponse;
import eu.europa.ec.fisheries.schema.sales.SalesMessageIdType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.SalesModuleRequestMapper;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
@Singleton
public class SalesServiceBeanHelper {

    @EJB
    private RulesMessageProducer messageProducer;

    @EJB
    private RulesResponseConsumer messageConsumer;

    @Lock(LockType.READ)
    protected Optional<FLUXSalesReportMessage> receiveMessageFromSales(String correlationId) throws MessageException, JMSException, SalesMarshallException {
        TextMessage receivedMessageAsTextMessage = messageConsumer.getMessage(correlationId, TextMessage.class, 30000L);
        log.info("Received response message");
        String receivedMessageAsString = receivedMessageAsTextMessage.getText();
        return unmarshal(receivedMessageAsString);
    }

    @Lock(LockType.READ)
    protected String sendMessageToSales(String request) throws MessageException {
        return messageProducer.sendDataSourceMessage(request, DataSourceQueue.SALES);
    }

    @Lock(LockType.READ)
    protected Optional<FLUXSalesReportMessage> unmarshal(String message) throws SalesMarshallException {
        FindReportByIdResponse findReportByIdResponse = JAXBMarshaller.unmarshallString(message, FindReportByIdResponse.class);
        if (StringUtils.isNotBlank(findReportByIdResponse.getReport())) {
            return Optional.of((FLUXSalesReportMessage) JAXBMarshaller.unmarshallString(findReportByIdResponse.getReport(), FLUXSalesReportMessage.class));
        } else {
            return Optional.absent();
        }
    }

    @Lock(LockType.READ)
    public Optional<FLUXSalesReportMessage> findReport(String guid) throws MessageException, SalesMarshallException, JMSException {
        String findReportByIdRequest = SalesModuleRequestMapper.createFindReportByIdRequest(guid);
        log.info("Send FLUXSalesReportMessage message to Sales");
        String correlationId = sendMessageToSales(findReportByIdRequest);
        return receiveMessageFromSales(correlationId);
    }

    @Lock(LockType.READ)
    public boolean areAnyOfTheseIdsNotUnique(List<String> ids, SalesMessageIdType type) throws SalesMarshallException, MessageException, JMSException {
        String checkForUniqueIdRequest = SalesModuleRequestMapper.createCheckForUniqueIdRequest(ids, type);
        log.info("Send CheckForUniqueIdRequest message to Sales");
        String correlationID = sendMessageToSales(checkForUniqueIdRequest);

        TextMessage receivedMessageAsTextMessage = messageConsumer.getMessage(correlationID, TextMessage.class, 60000L);
        log.info("Received response message");
        CheckForUniqueIdResponse response = JAXBMarshaller.unmarshallString(receivedMessageAsTextMessage.getText(), CheckForUniqueIdResponse.class);
        return !response.isUnique();
    }
}
