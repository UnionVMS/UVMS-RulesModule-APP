package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;


import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ListValueTypeFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SearchFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

@Singleton
public class ActivityServiceBeanHelper {

    @EJB
    RulesMessageProducer messageProducer;

    @EJB
    RulesResponseConsumer messageConsumer;

    protected Optional<FishingTripResponse> receiveMessageFromActivity(String correlationId) throws MessageException, JMSException, SalesMarshallException {
        TextMessage receivedMessageAsTextMessage = messageConsumer.getMessage(correlationId, TextMessage.class);
        String receivedMessageAsString = receivedMessageAsTextMessage.getText();
        return unmarshal(receivedMessageAsString);
    }

    protected Optional<FishingTripResponse> unmarshal(String message) throws SalesMarshallException {
        FishingTripResponse response = JAXBMarshaller.unmarshallString(message, FishingTripResponse.class);

        if (isEmpty(response.getFishingActivityLists()) || isEmpty(response.getFishingTripIdLists())) {
            return Optional.absent();
        }

        return Optional.of(response);
    }

    public Optional<FishingTripResponse> findTrip(String fishingTripID) throws MessageException, SalesMarshallException, JMSException, ActivityModelMarshallException {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(ISODateTimeFormat.dateTimeNoMillis()).toFormatter().withOffsetParsed();

        List<SingleValueTypeFilter> singleFilters = Arrays.asList(
                new SingleValueTypeFilter(SearchFilter.TRIP_ID, fishingTripID),
                new SingleValueTypeFilter(SearchFilter.PERIOD_START, formatter.print(DateTime.now().minusYears(3).toLocalDateTime())),
                new SingleValueTypeFilter(SearchFilter.PERIOD_END, formatter.print(DateTime.now().toLocalDateTime())));

        List<ListValueTypeFilter> listFilter = Arrays.asList(
                new ListValueTypeFilter(SearchFilter.ACTIVITY_TYPE, Arrays.asList("LANDING")));

        String request = ActivityModuleRequestMapper.mapToActivityGetFishingTripRequest(listFilter, singleFilters);
        String correlationId = messageProducer.sendDataSourceMessage(request, DataSourceQueue.ACTIVITY);

        return receiveMessageFromActivity(correlationId);
    }
}
