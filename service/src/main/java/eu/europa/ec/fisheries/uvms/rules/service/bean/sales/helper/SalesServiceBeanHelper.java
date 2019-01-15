package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;


import eu.europa.ec.fisheries.schema.sales.CheckForUniqueIdResponse;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.FindReportByIdResponse;
import eu.europa.ec.fisheries.schema.sales.SalesMessageIdType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesSalesProducerBean;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.SalesModuleRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Optional;

@Slf4j
@Singleton
public class SalesServiceBeanHelper {

    private static final long TIME_TO_WAIT_FOR_A_RESPONSE = 30000L;

    @EJB
    private RulesResponseConsumer rulesConsumer;

    @EJB
    private RulesSalesProducerBean salesProducer;

    @Lock(LockType.READ)
    protected Optional<FLUXSalesReportMessage> receiveMessageFromSales(String correlationId) throws MessageException, JMSException, SalesMarshallException {
        TextMessage receivedMessageAsTextMessage = rulesConsumer.getMessage(correlationId, TextMessage.class, TIME_TO_WAIT_FOR_A_RESPONSE);
        log.info("Received response message");
        String receivedMessageAsString = receivedMessageAsTextMessage.getText();
        return unmarshal(receivedMessageAsString);
    }

    @Lock(LockType.READ)
    protected String sendMessageToSales(String request) throws MessageException {
        return salesProducer.sendModuleMessageNonPersistent(request, rulesConsumer.getDestination(), TIME_TO_WAIT_FOR_A_RESPONSE + 1000L);
    }

    @Lock(LockType.READ)
    protected Optional<FLUXSalesReportMessage> unmarshal(String message) throws SalesMarshallException {
        FindReportByIdResponse findReportByIdResponse = JAXBMarshaller.unmarshallString(message, FindReportByIdResponse.class);
        if (StringUtils.isNotBlank(findReportByIdResponse.getReport())) {
            return Optional.of(JAXBMarshaller.unmarshallString(findReportByIdResponse.getReport(), FLUXSalesReportMessage.class));
        } else {
            return Optional.empty();
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

        TextMessage receivedMessageAsTextMessage = rulesConsumer.getMessage(correlationID, TextMessage.class, TIME_TO_WAIT_FOR_A_RESPONSE);
        log.info("Received response message");
        CheckForUniqueIdResponse response = JAXBMarshaller.unmarshallString(receivedMessageAsTextMessage.getText(), CheckForUniqueIdResponse.class);
        return !response.isUnique();
    }
}
