/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import lombok.SneakyThrows;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.TextMessage;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by kovian on 01/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigRulesCacheTest {

    @Mock
    private AbstractConfigCache rulesSettingsCache;

    @Mock
    private RulesResponseConsumer consumer;

    @Mock
    private RulesMessageProducer producer;

    @Mock
    private ActiveMQTextMessage textMessage;

    @Mock
    ClientSession session;

    @Before
    @SneakyThrows
    public void setUp() {
        rulesSettingsCache = new RulesConfigurationCache();
        textMessage        = new ActiveMQTextMessage(session);
        Whitebox.setInternalState(rulesSettingsCache, "consumer", consumer);
        Whitebox.setInternalState(rulesSettingsCache, "producer", producer);
        Whitebox.setInternalState(textMessage, "text", new SimpleString(getMockedSettingsResponse()));
        Whitebox.setInternalState(textMessage, "jmsCorrelationID", "SomeCorrId");
    }

    @SneakyThrows
    @Test
    public void testCacheInitilialization() {
        when(producer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.CONFIG))).thenReturn("SomeCorrId");
        when(consumer.getMessage(anyString(), eq(TextMessage.class))).thenReturn(textMessage);

        rulesSettingsCache.initializeCache();

        Map<String, String> allSettingsForModel = rulesSettingsCache.getAllSettingsForModule();
        assertEquals(11, allSettingsForModel.size());

        final String rules_scheduler_config = rulesSettingsCache.getSingleConfig("RULES_SCHEDULER_CONFIG");
        assertNotNull(rules_scheduler_config);
        assertEquals("0/10 * * * *", rules_scheduler_config);

        assertNotNull(rulesSettingsCache.getSingleConfig("RULES_SCHEDULER_CONFIG"));
        assertNotNull(rulesSettingsCache.getSingleConfig("flux_local_nation_code"));
        assertNull(rulesSettingsCache.getSingleConfig("blabla"));
        assertNull(rulesSettingsCache.getSingleConfig("YYYYY"));
        assertNull(rulesSettingsCache.getSingleConfig("nation_code"));

    }


    public String getMockedSettingsResponse() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:PullSettingsResponse xmlns:ns2=\"urn:module.config.schema.fisheries.ec.europa.eu:v1\">\n" +
                "    <status>OK</status>\n" +
                "    <settings>\n" +
                "        <id>73</id>\n" +
                "        <key>RULES_SCHEDULER_CONFIG</key>\n" +
                "        <value>0/10 * * * *</value>\n" +
                "        <description>Rules scheduler for re initialization of DROOLS engine.</description>\n" +
                "        <module>rules</module>\n" +
                "        <global>false</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>1</id>\n" +
                "        <key>measurementSystem</key>\n" +
                "        <value>metric</value>\n" +
                "        <description>Choise of metric system, typically metric or imperial.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>2</id>\n" +
                "        <key>coordinateFormat</key>\n" +
                "        <value>degreesMinutesSeconds</value>\n" +
                "        <description>Coordinate system.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>3</id>\n" +
                "        <key>dateTimeFormat</key>\n" +
                "        <value>YYYY-MM-DD HH:mm:ss</value>\n" +
                "        <description>Choice of datetime format.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>4</id>\n" +
                "        <key>defaultHomePage</key>\n" +
                "        <value>reporting</value>\n" +
                "        <description>Default home page.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>5</id>\n" +
                "        <key>availableLanguages</key>\n" +
                "        <value>en-gb</value>\n" +
                "        <description>List of available language codes, comma-separated.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>6</id>\n" +
                "        <key>distanceUnit</key>\n" +
                "        <value>nm</value>\n" +
                "        <description>Unit used for distances.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>7</id>\n" +
                "        <key>speedUnit</key>\n" +
                "        <value>kts</value>\n" +
                "        <description>Unit used for speed.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>8</id>\n" +
                "        <key>maxSpeed</key>\n" +
                "        <value>15</value>\n" +
                "        <description>Maximum allowed speed, measured in nautical miles.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>9</id>\n" +
                "        <key>timezone</key>\n" +
                "        <value>0</value>\n" +
                "        <description>Global timezone (offset from UTC in minutes).</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "    <settings>\n" +
                "        <id>74</id>\n" +
                "        <key>flux_local_nation_code</key>\n" +
                "        <value>BEL</value>\n" +
                "        <description>Nation code global to all the modules.</description>\n" +
                "        <global>true</global>\n" +
                "    </settings>\n" +
                "</ns2:PullSettingsResponse>";
    }
}




