package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;

import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.FindReportByIdResponse;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.SalesModuleRequestMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jms.DeliveryMode;
import javax.jms.TextMessage;
import java.util.Optional;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JAXBMarshaller.class, SalesModuleRequestMapper.class})
@PowerMockIgnore( {"javax.management.*"})
public class SalesServiceBeanHelperTest {

    @InjectMocks
    private SalesServiceBeanHelper helper;

    @Mock
    private RulesMessageProducer producer;

    @Mock
    private RulesResponseConsumer consumer;

    @Test
    public void receiveMessageFromSales() throws Exception {
        mockStatic(JAXBMarshaller.class);

        TextMessage mockTextMessage = mock(TextMessage.class);
        doReturn("unmarshall this message").when(mockTextMessage).getText();
        doReturn(mockTextMessage).when(consumer).getMessage("correlationID", TextMessage.class, 30000L);
        when(JAXBMarshaller.unmarshallString("unmarshall this message content", FLUXSalesReportMessage.class))
                .thenReturn(new FLUXSalesReportMessage());
        when(JAXBMarshaller.unmarshallString("unmarshall this message", FindReportByIdResponse.class))
                .thenReturn(new FindReportByIdResponse().withReport("unmarshall this message content"));

        helper.receiveMessageFromSales("correlationID");

        verify(consumer).getMessage("correlationID", TextMessage.class, 30000L);
        verify(mockTextMessage).getText();

        verifyStatic();
        JAXBMarshaller.unmarshallString("unmarshall this message", FindReportByIdResponse.class);
        JAXBMarshaller.unmarshallString("unmarshall this message content", FLUXSalesReportMessage.class);

        verifyNoMoreInteractions(consumer, mockTextMessage);
    }

    @Test
    @Ignore
    public void sendMessageToSales() throws Exception {
        when(producer.sendDataSourceMessage("request", DataSourceQueue.SALES, 31000L, DeliveryMode.NON_PERSISTENT)).thenReturn("");

        helper.sendMessageToSales("request");

        verify(producer).sendDataSourceMessage("request", DataSourceQueue.SALES, 31000L, DeliveryMode.NON_PERSISTENT);
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
    @Ignore
    public void findReportWhenSuccess() throws Exception {
        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage();

        TextMessage mockTextMessage = mock(TextMessage.class);
        mockStatic(SalesModuleRequestMapper.class, JAXBMarshaller.class);
        doReturn("FindReportByIdResponse").when(mockTextMessage).getText();
        doReturn(mockTextMessage).when(consumer).getMessage("correlationId", TextMessage.class, 30000L);
        when(SalesModuleRequestMapper.createFindReportByIdRequest("guid")).thenReturn("FindReportByIdRequest");
        when(producer.sendDataSourceMessage("FindReportByIdRequest", DataSourceQueue.SALES, 31000L, DeliveryMode.NON_PERSISTENT)).thenReturn("correlationId");
        when(JAXBMarshaller.unmarshallString("unmarshall this message content", FLUXSalesReportMessage.class))
                .thenReturn(fluxSalesReportMessage);
        when(JAXBMarshaller.unmarshallString("FindReportByIdResponse", FindReportByIdResponse.class))
                .thenReturn(new FindReportByIdResponse().withReport("unmarshall this message content"));

        Optional<FLUXSalesReportMessage> returnedMessage = helper.findReport("guid");

        verify(mockTextMessage).getText();
        verify(consumer).getMessage("correlationId", TextMessage.class, 30000L);
        verify(producer).sendDataSourceMessage("FindReportByIdRequest", DataSourceQueue.SALES, 31000L, DeliveryMode.NON_PERSISTENT);

        verifyStatic();
        JAXBMarshaller.unmarshallString("FindReportByIdResponse", FindReportByIdResponse.class);
        JAXBMarshaller.unmarshallString("FLUXSalesReportMessage", FLUXSalesReportMessage.class);

        assertSame(fluxSalesReportMessage, returnedMessage.get());
    }

}