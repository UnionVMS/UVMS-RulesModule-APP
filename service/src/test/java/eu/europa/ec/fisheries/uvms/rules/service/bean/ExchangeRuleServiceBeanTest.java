/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import javax.jms.TextMessage;

import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.RulesResponseConsumerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesMessageProducerBean;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRuleServiceBeanTest {

    @InjectMocks
    private ExchangeRuleService exchangeRuleService = new ExchangeRuleServiceBean();

    @Mock
    private RulesResponseConsumer consumer = new RulesResponseConsumerBean();

    @Mock
    private RulesMessageProducer producer = new RulesMessageProducerBean();

    @Mock
    private TextMessage textMessage;

    @Test
    @SneakyThrows
    public void testIdentificationExistsTrue() {

        when(textMessage.getText()).thenReturn(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns2:LogIdByTypeExistsResponse xmlns:ns2=\"urn:module.exchange.schema.fisheries.ec.europa.eu:v1\">\n" +
                            "<messageGuid>AE93407F-5042-4BFE-AE33-CDEF2D8B987C</messageGuid>\n" +
                "</ns2:LogIdByTypeExistsResponse>\n");

        when(producer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.EXCHANGE))).thenReturn("SomeCorrId");
        when(consumer.getMessage(anyString(), eq(TextMessage.class))).thenReturn(textMessage);

        boolean exists = exchangeRuleService.identificationExists("DMLDKML", "FA_QUERY");

        assertTrue(exists);
    }

    @Test
    @SneakyThrows
    public void testIdentificationExistsFalse() {

        when(textMessage.getText()).thenReturn(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns2:LogIdByTypeExistsResponse xmlns:ns2=\"urn:module.exchange.schema.fisheries.ec.europa.eu:v1\">\n" +
                        "</ns2:LogIdByTypeExistsResponse>\n");

        when(producer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.EXCHANGE))).thenReturn("SomeCorrId");
        when(consumer.getMessage(anyString(), eq(TextMessage.class))).thenReturn(textMessage);

        boolean exists = exchangeRuleService.identificationExists("DMLDKML", "FA_QUERY");

        assertFalse(exists);
    }

    @Test
    @SneakyThrows
    public void testIdentificationRefExistsFalse() {

        when(textMessage.getText()).thenReturn(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns2:LogRefIdByTypeExistsResponse xmlns:ns2=\"urn:module.exchange.schema.fisheries.ec.europa.eu:v1\">\n" +
                        "</ns2:LogRefIdByTypeExistsResponse>\n");

        when(producer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.EXCHANGE))).thenReturn("SomeCorrId");
        when(consumer.getMessage(anyString(), eq(TextMessage.class))).thenReturn(textMessage);

        boolean exists = exchangeRuleService.identificationRefExists("DMLDKML", "FA_QUERY");

        assertFalse(exists);
    }

    @Test
    @SneakyThrows
    public void testIdentificationRefExistsTrue() {

        when(textMessage.getText()).thenReturn(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns2:LogRefIdByTypeExistsResponse xmlns:ns2=\"urn:module.exchange.schema.fisheries.ec.europa.eu:v1\">\n" +
                        "<refGuid>AE93407F-5042-4BFE-AE33-CDEF2D8B987C</refGuid>\n" +
                        "</ns2:LogRefIdByTypeExistsResponse>\n");

        when(producer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.EXCHANGE))).thenReturn("SomeCorrId");
        when(consumer.getMessage(anyString(), eq(TextMessage.class))).thenReturn(textMessage);

        boolean exists = exchangeRuleService.identificationRefExists("DMLDKML", "FA_QUERY");

        assertTrue(exists);
    }
}

