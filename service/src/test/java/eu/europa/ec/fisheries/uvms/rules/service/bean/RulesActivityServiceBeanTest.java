/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.UnmarshalException;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesActivityProducerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesResponseQueueProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesActivityServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RulesActivityServiceBeanTest {

    @Mock private RulesActivityProducerBean rulesActivityProducer;
    @Mock private RulesResponseQueueProducer producer;
    @Mock private RulesResponseConsumer consumer;
    @Mock private RulesMessageProducer rulesMessageProducer;
    @InjectMocks private RulesActivityServiceBean rulesActivityService;
    TextMessage mock = mock(TextMessage.class);

    @Before
    public void before(){

    }

    @Test
    public void testCheckSubscriptionPermissionsWithExceptionReturnsFalse() throws UnmarshalException, RulesValidationException, MessageException, JMSException {

        Mockito.when(rulesActivityProducer.sendModuleMessageWithProps(anyString(), any(javax.jms.Destination.class), anyMap())).thenReturn("value");
        when(mock.getText()).thenReturn("sometext");
        Mockito.when(consumer.getMessage(anyString(), anyLong())).thenReturn(mock);

        InOrder inOrder = inOrder( rulesActivityProducer, consumer);

        assertFalse(rulesActivityService.checkSubscriptionPermissions("", MessageType.FLUX_FA_REPORT_MESSAGE));

        inOrder.verify(rulesActivityProducer, times(1)).sendModuleMessageWithProps(anyString(), any(Destination.class), anyMap());
        inOrder.verify(consumer, times(1)).getMessage(anyString(), anyLong());

    }

    @Test
    public void testCheckSubscriptionPermissionsHappyReturnsFalse() throws UnmarshalException, RulesValidationException, MessageException, JMSException {

        Mockito.when(rulesActivityProducer.sendModuleMessageWithProps(anyString(), any(javax.jms.Destination.class), anyMap())).thenReturn("value");
        when(mock.getText()).thenReturn("<ns2:SubscriptionPermissionResponse xmlns:ns2=\"module.subscription.wsdl.fisheries.ec.europa.eu\">\n" +
                "    <subscriptionCheck>NO</subscriptionCheck>\n" +
                "</ns2:SubscriptionPermissionResponse>");
        Mockito.when(consumer.getMessage(anyString(), anyLong())).thenReturn(mock);

        InOrder inOrder = inOrder(rulesActivityProducer, consumer);

        assertFalse(rulesActivityService.checkSubscriptionPermissions("", MessageType.FLUX_FA_REPORT_MESSAGE));

        inOrder.verify(rulesActivityProducer, times(1)).sendModuleMessageWithProps(anyString(), any(Destination.class), anyMap());
        inOrder.verify(consumer, times(1)).getMessage(anyString(), anyLong());

    }

    @Test
    public void testSendRequestToActivity() throws MessageException {

        rulesActivityService.sendRequestToActivity("", PluginType.FLUX, MessageType.FLUX_FA_QUERY_MESSAGE, "");

        Mockito.verify(rulesMessageProducer, times(1)).sendDataSourceMessage(anyString(), any(DataSourceQueue.class));

    }
}
