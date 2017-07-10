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
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.SneakyThrows;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import javax.jms.TextMessage;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by padhyad, kovian on 6/7/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MdrCacheTest {


    @Mock
    MDRCacheServiceBean mdrCacheServiceBean;

    @Mock
    MDRCache mdrCache;

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
        mdrCache = new MDRCache();
        mdrCacheServiceBean = new MDRCacheServiceBean();
        textMessage = new ActiveMQTextMessage(session);

        Whitebox.setInternalState(mdrCacheServiceBean, "cache", mdrCache);
        Whitebox.setInternalState(mdrCache, "consumer", consumer);
        Whitebox.setInternalState(mdrCache, "producer", producer);
        Whitebox.setInternalState(textMessage, "text", new SimpleString(getMockedSettingsResponse()));
        Whitebox.setInternalState(textMessage, "jmsCorrelationID", "SomeCorrId");
    }

    @Test
    @SneakyThrows
    public void testGetListFromCache() {
        when(producer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.MDR_EVENT))).thenReturn("SomeCorrId");
        when(consumer.getMessage(anyString(), eq(TextMessage.class))).thenReturn(textMessage);

        List<ObjectRepresentation> faCatchTypeEntries = mdrCache.getEntry(MDRAcronymType.FA_CATCH_TYPE);

        assertTrue(CollectionUtils.isNotEmpty(faCatchTypeEntries));
    }


    public String getMockedSettingsResponse() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:MdrGetCodeListResponse xmlns:ns2=\"http://europa.eu/ec/fisheries/uvms/activity/model/schemas\">\n" +
                "    <method>MDR_CODE_LIST_RESP</method>\n" +
                "    <acronym>FLUX_GP_PURPOSE</acronym>\n" +
                "    <dataSet>\n" +
                "        <field>\n" +
                "            <columnName>id</columnName>\n" +
                "            <columnValue>1000</columnValue>\n" +
                "            <columnDataType>long</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>validity</columnName>\n" +
                "            <columnValue>DateRange(startDate=Sun Jan 01 00:00:00 UTC 1989, endDate=Fri Dec 31 00:00:00 UTC 2100)</columnValue>\n" +
                "            <columnDataType>class eu.europa.ec.fisheries.uvms.domain.DateRange</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>version</columnName>\n" +
                "            <columnValue>1.0</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>code</columnName>\n" +
                "            <columnValue>9</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>description</columnName>\n" +
                "            <columnValue>ORIGINAL</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_CODE_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_DESCRIPTION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_EN_DESCRIPTION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_VERSION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "    </dataSet>\n" +
                "    <dataSet>\n" +
                "        <field>\n" +
                "            <columnName>id</columnName>\n" +
                "            <columnValue>1001</columnValue>\n" +
                "            <columnDataType>long</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>validity</columnName>\n" +
                "            <columnValue>DateRange(startDate=Sun Jan 01 00:00:00 UTC 1989, endDate=Fri Dec 31 00:00:00 UTC 2100)</columnValue>\n" +
                "            <columnDataType>class eu.europa.ec.fisheries.uvms.domain.DateRange</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>version</columnName>\n" +
                "            <columnValue>1.0</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>code</columnName>\n" +
                "            <columnValue>1</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>description</columnName>\n" +
                "            <columnValue>CANCELLATION</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_CODE_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_DESCRIPTION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_EN_DESCRIPTION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_VERSION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "    </dataSet>\n" +
                "    <dataSet>\n" +
                "        <field>\n" +
                "            <columnName>id</columnName>\n" +
                "            <columnValue>1002</columnValue>\n" +
                "            <columnDataType>long</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>validity</columnName>\n" +
                "            <columnValue>DateRange(startDate=Sun Jan 01 00:00:00 UTC 1989, endDate=Fri Dec 31 00:00:00 UTC 2100)</columnValue>\n" +
                "            <columnDataType>class eu.europa.ec.fisheries.uvms.domain.DateRange</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>version</columnName>\n" +
                "            <columnValue>1.0</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>code</columnName>\n" +
                "            <columnValue>5</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>description</columnName>\n" +
                "            <columnValue>REPLACE</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_CODE_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_DESCRIPTION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_EN_DESCRIPTION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_VERSION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "    </dataSet>\n" +
                "    <dataSet>\n" +
                "        <field>\n" +
                "            <columnName>id</columnName>\n" +
                "            <columnValue>1003</columnValue>\n" +
                "            <columnDataType>long</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>validity</columnName>\n" +
                "            <columnValue>DateRange(startDate=Sun Jan 01 00:00:00 UTC 1989, endDate=Fri Dec 31 00:00:00 UTC 2100)</columnValue>\n" +
                "            <columnDataType>class eu.europa.ec.fisheries.uvms.domain.DateRange</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>version</columnName>\n" +
                "            <columnValue>1.0</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>code</columnName>\n" +
                "            <columnValue>3</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>description</columnName>\n" +
                "            <columnValue>DELETE</columnValue>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_CODE_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_DESCRIPTION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_EN_DESCRIPTION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <columnName>APP_VERSION_STR</columnName>\n" +
                "            <columnDataType>class java.lang.String</columnDataType>\n" +
                "        </field>\n" +
                "    </dataSet>\n" +
                "    <validation>\n" +
                "        <valid>OK</valid>\n" +
                "        <message>Validation is OK.</message>\n" +
                "    </validation>\n" +
                "</ns2:MdrGetCodeListResponse>\n";
    }
}
