package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.FindReportByIdResponse;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesCache;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.SalesModuleRequestMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jms.TextMessage;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JAXBMarshaller.class, SalesModuleRequestMapper.class})
@PowerMockIgnore( {"javax.management.*"})
public class SalesServiceBeanHelperTest {

    @InjectMocks
    SalesServiceBeanHelper helper;

    @Mock
    RulesMessageProducer producer;

    @Mock
    RulesResponseConsumer consumer;

    @Mock
    SalesCache cache;

    @Test
    public void receiveMessageFromSales() throws Exception {

        mockStatic(JAXBMarshaller.class);

        TextMessage mockTextMessage = mock(TextMessage.class);
        doReturn("unmarshall this message").when(mockTextMessage).getText();
        doReturn(mockTextMessage).when(consumer).getMessage("correlationID", TextMessage.class);
        when(JAXBMarshaller.unmarshallString("unmarshall this message content", FLUXSalesReportMessage.class))
                .thenReturn(new FLUXSalesReportMessage());
        when(JAXBMarshaller.unmarshallString("unmarshall this message", FindReportByIdResponse.class))
                .thenReturn(new FindReportByIdResponse().withReport("unmarshall this message content"));

        helper.receiveMessageFromSales("correlationID");

        verify(consumer).getMessage("correlationID", TextMessage.class);
        verify(mockTextMessage).getText();

        verifyStatic();
        JAXBMarshaller.unmarshallString("unmarshall this message", FindReportByIdResponse.class);
        JAXBMarshaller.unmarshallString("unmarshall this message content", FLUXSalesReportMessage.class);

        verifyNoMoreInteractions(consumer, mockTextMessage);
    }

    @Test
    public void sendMessageToSales() throws Exception {
        when(producer.sendDataSourceMessage("request", DataSourceQueue.SALES)).thenReturn("");

        helper.sendMessageToSales("request");

        verify(producer).sendDataSourceMessage("request", DataSourceQueue.SALES);
        verifyNoMoreInteractions(producer, consumer);
    }

    @Test
    public void unmarshal() throws Exception {
        mockStatic(JAXBMarshaller.class);
        when(JAXBMarshaller.unmarshallString("FindReportByIdResponse", FindReportByIdResponse.class)).thenReturn(new FindReportByIdResponse().withReport("FLUXSalesReportMessage"));
        when(JAXBMarshaller.unmarshallString("FLUXSalesReportMessage", FLUXSalesReportMessage.class)).thenReturn(new FLUXSalesReportMessage());

        helper.unmarshal("FindReportByIdResponse");

        verifyStatic();
        JAXBMarshaller.unmarshallString("FindReportByIdResponse", FindReportByIdResponse.class);
        JAXBMarshaller.unmarshallString("FLUXSalesReportMessage", FLUXSalesReportMessage.class);
    }

    @Test
    public void findReportWhenReportIsCached() throws Exception {

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage();
        Optional<FLUXSalesReportMessage> fluxSalesReportMessageOptional = Optional.of(fluxSalesReportMessage);
        doReturn(fluxSalesReportMessageOptional).when(cache).retrieveMessageFromCache("guid");


        Optional<FLUXSalesReportMessage> returnedMessage = helper.findReport("guid");

        verify(cache).retrieveMessageFromCache("guid");
        verifyNoMoreInteractions(cache, consumer, producer);
        assertSame(fluxSalesReportMessage, returnedMessage.get());
    }

    @Test
    public void findReportWhenReportIsNotCached() throws Exception {
        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage();
        Optional<FLUXSalesReportMessage> fluxSalesReportMessageOptional = Optional.absent();


        TextMessage mockTextMessage = mock(TextMessage.class);
        mockStatic(SalesModuleRequestMapper.class, JAXBMarshaller.class);
        doReturn(fluxSalesReportMessageOptional).when(cache).retrieveMessageFromCache("guid");
        doReturn("FindReportByIdResponse").when(mockTextMessage).getText();
        doReturn(mockTextMessage).when(consumer).getMessage("correlationId", TextMessage.class);
        when(SalesModuleRequestMapper.createFindReportByIdRequest("guid")).thenReturn("FindReportByIdRequest");
        when(producer.sendDataSourceMessage("FindReportByIdRequest", DataSourceQueue.SALES)).thenReturn("correlationId");
        when(JAXBMarshaller.unmarshallString("unmarshall this message content", FLUXSalesReportMessage.class))
                .thenReturn(fluxSalesReportMessage);
        when(JAXBMarshaller.unmarshallString("FindReportByIdResponse", FindReportByIdResponse.class))
                .thenReturn(new FindReportByIdResponse().withReport("unmarshall this message content"));


        Optional<FLUXSalesReportMessage> returnedMessage = helper.findReport("guid");

        verify(cache).retrieveMessageFromCache("guid");
        verify(cache).cacheMessage("guid", fluxSalesReportMessage);
        verify(mockTextMessage).getText();
        verify(consumer).getMessage("correlationId", TextMessage.class);
        verify(producer).sendDataSourceMessage("FindReportByIdRequest", DataSourceQueue.SALES);

        verifyStatic();
        JAXBMarshaller.unmarshallString("FindReportByIdResponse", FindReportByIdResponse.class);
        JAXBMarshaller.unmarshallString("FLUXSalesReportMessage", FLUXSalesReportMessage.class);

        verifyNoMoreInteractions(cache, consumer, producer);
        assertSame(fluxSalesReportMessage, returnedMessage.get());
    }

}