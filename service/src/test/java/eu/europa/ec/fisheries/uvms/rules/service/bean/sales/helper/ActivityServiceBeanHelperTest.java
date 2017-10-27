package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.*;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JAXBMarshaller.class})
@PowerMockIgnore( {"javax.management.*"})
public class ActivityServiceBeanHelperTest {

    @InjectMocks
    private ActivityServiceBeanHelper helper;

    @Mock
    private RulesMessageProducer messageProducer;

    @Mock
    private RulesResponseConsumer messageConsumer;

    @Mock
    private ActivityModuleRequestMapperFacade activityMapper;

    @Test
    public void receiveMessageFromActivity() throws Exception {
        String correlationId = "correlationId";
        String message = "<ns2:FishingTripResponse xmlns:ns2=\"http://europa.eu/ec/fisheries/uvms/activity/model/schemas\"/>";
        mockStatic(JAXBMarshaller.class);

        FishingTripResponse fishingTripResponse = new FishingTripResponse();
        fishingTripResponse.getFishingActivityLists().add(new FishingActivitySummary());
        fishingTripResponse.getFishingTripIdLists().add(new FishingTripIdWithGeometry());

        when(JAXBMarshaller.unmarshallString(message, FishingTripResponse.class))
                .thenReturn(fishingTripResponse);

        TextMessage mockTextMessage = mock(TextMessage.class);
        doReturn(message).when(mockTextMessage).getText();
        doReturn(mockTextMessage).when(messageConsumer).getMessage(correlationId, TextMessage.class);

        Optional<FishingTripResponse> fishingTripResponseOptional = helper.receiveMessageFromActivity(correlationId);

        verify(mockTextMessage).getText();
        verify(messageConsumer).getMessage(correlationId, TextMessage.class);
        verifyNoMoreInteractions(messageConsumer, messageProducer);

        verifyStatic();
        JAXBMarshaller.unmarshallString(message, FishingTripResponse.class);

        assertSame(fishingTripResponse, fishingTripResponseOptional.get());
    }

    @Test
    public void unmarshal() throws Exception {
        String message = "<ns2:FishingTripResponse xmlns:ns2=\"http://europa.eu/ec/fisheries/uvms/activity/model/schemas\"/>";
        mockStatic(JAXBMarshaller.class);

        FishingTripResponse fishingTripResponse = new FishingTripResponse();
        fishingTripResponse.getFishingActivityLists().add(new FishingActivitySummary());
        fishingTripResponse.getFishingTripIdLists().add(new FishingTripIdWithGeometry());

        when(JAXBMarshaller.unmarshallString(message, FishingTripResponse.class))
                .thenReturn(fishingTripResponse);

        Optional<FishingTripResponse> fishingTripResponseOptional = helper.unmarshal(message);

        verifyStatic();
        JAXBMarshaller.unmarshallString(message, FishingTripResponse.class);

        assertTrue(fishingTripResponseOptional.isPresent());
        assertSame(fishingTripResponse, fishingTripResponseOptional.get());
    }

    @Test
    public void findTrip() throws Exception {
        mockStatic(JAXBMarshaller.class);
        String correlationId = "correlationId";
        String message = "<ns2:FishingTripResponse xmlns:ns2=\"http://europa.eu/ec/fisheries/uvms/activity/model/schemas\"/>";
        String fishingTripID = "fishingTripID";

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(ISODateTimeFormat.dateTimeNoMillis()).toFormatter().withOffsetParsed();
        List<SingleValueTypeFilter> singleFilters = Arrays.asList(
                new SingleValueTypeFilter(SearchFilter.TRIP_ID, fishingTripID),
                new SingleValueTypeFilter(SearchFilter.PERIOD_START, formatter.print(DateTime.now().minusYears(3).toLocalDateTime())),
                new SingleValueTypeFilter(SearchFilter.PERIOD_END, formatter.print(DateTime.now().toLocalDateTime())));

        List<ListValueTypeFilter> listFilter = Arrays.asList(
                new ListValueTypeFilter(SearchFilter.ACTIVITY_TYPE, Arrays.asList("LANDING")));

        FishingTripResponse fishingTripResponse = new FishingTripResponse();
        fishingTripResponse.getFishingActivityLists().add(new FishingActivitySummary());
        fishingTripResponse.getFishingTripIdLists().add(new FishingTripIdWithGeometry());

        TextMessage mockTextMessage = mock(TextMessage.class);
        doReturn(message).when(mockTextMessage).getText();
        doReturn(mockTextMessage).when(messageConsumer).getMessage(correlationId, TextMessage.class);
        doReturn(correlationId).when(messageProducer).sendDataSourceMessage("FishingTripResponse", DataSourceQueue.ACTIVITY);
        doReturn("FishingTripResponse").when(activityMapper).mapToActivityGetFishingTripRequest(listFilter, singleFilters);

        when(JAXBMarshaller.unmarshallString(message, FishingTripResponse.class))
                .thenReturn(fishingTripResponse);

        Optional<FishingTripResponse> fishingTripResponseOptional = helper.findTrip(fishingTripID);

        verify(mockTextMessage).getText();
        verify(messageConsumer).getMessage(correlationId, TextMessage.class);
        verify(messageProducer).sendDataSourceMessage("FishingTripResponse", DataSourceQueue.ACTIVITY);

        verifyNoMoreInteractions(messageConsumer, messageProducer);

        verifyStatic();
        JAXBMarshaller.unmarshallString(message, FishingTripResponse.class);

        assertTrue(fishingTripResponseOptional.isPresent());
        assertSame(fishingTripResponse, fishingTripResponseOptional.get());
    }

}