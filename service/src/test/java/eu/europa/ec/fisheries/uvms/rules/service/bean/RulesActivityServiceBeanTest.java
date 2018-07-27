/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityTableType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import lombok.SneakyThrows;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

/**
 * Created by padhyad, ankovi on 6/7/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RulesActivityServiceBeanTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    RulesActivityServiceBean activityServiceBean;

    @Mock
    RulesResponseConsumer consumer;

    @Mock
    RulesMessageProducer producer;

    @Mock
    ClientSession session;

    TextMessage responseMsg;

    String testXmlPath = "src/findHistoryOfAssetBy/resources/testData/fluxFaResponseMessage.xml";

    @Before
    public void setUp() throws JMSException {
        responseMsg = new ActiveMQTextMessage(session);
        Whitebox.setInternalState(responseMsg, "text", new SimpleString(getStrResponse()));
        Whitebox.setInternalState(responseMsg, "jmsCorrelationID", "SomeCorrId");
    }

    @Test
    public void testGetFishingActivitiesForTripsNull() throws MessageException {
        Whitebox.setInternalState(responseMsg, "text", new SimpleString(getResponseStr2()));
        Mockito.doReturn("id").when(producer).sendDataSourceMessage(Mockito.any(String.class), Mockito.any(DataSourceQueue.class));
        Mockito.doReturn(responseMsg).when(consumer).getMessage(Mockito.any(String.class), Mockito.any(Class.class));

        Map<String, List<FishingActivityWithIdentifiers>> isUnique = activityServiceBean.getFishingActivitiesForTrips(null);
    }

    @Test
    public void testGetFishingActivitiesForTrips() throws MessageException {
        Whitebox.setInternalState(responseMsg, "text", new SimpleString(getResponseStr2()));
        Mockito.doReturn("SomeCorrId").when(producer).sendDataSourceMessage(Mockito.any(String.class), Mockito.any(DataSourceQueue.class));
        Mockito.doReturn(responseMsg).when(consumer).getMessage(Mockito.any(String.class), Mockito.any(Class.class));

        Map<String, List<FishingActivityWithIdentifiers>> respList = activityServiceBean.getFishingActivitiesForTrips(getMockedMessage());

        assertNotNull(respList);
    }

    @Test
    public void testIsGetNonUniqueIdsListNull() throws MessageException {
        Mockito.doReturn("id").when(producer).sendDataSourceMessage(Mockito.any(String.class), Mockito.any(DataSourceQueue.class));
        Mockito.doReturn(responseMsg).when(consumer).getMessage(Mockito.any(String.class), Mockito.any(Class.class));
        Map<ActivityTableType, List<IdType>> isUnique = activityServiceBean.getNonUniqueIdsList(null);

        assertNotNull(isUnique);
        assertTrue(MapUtils.isEmpty(isUnique));
    }

    @Test
    public void testIGetNonUniqueIdsList() throws MessageException {
        Mockito.doReturn("SomeCorrId").when(producer).sendDataSourceMessage(Mockito.any(String.class), Mockito.any(DataSourceQueue.class));
        Mockito.doReturn(responseMsg).when(consumer).getMessage(Mockito.any(String.class), Mockito.any(Class.class));
        Map<ActivityTableType, List<IdType>> isUnique = activityServiceBean.getNonUniqueIdsList(getMockedMessage());

        assertNotNull(isUnique);
        assertTrue(MapUtils.isNotEmpty(isUnique));
        assertTrue(isUnique.size() == 2);
    }

    @SneakyThrows
    public Object getMockedMessage() {
        String fluxFaMessageStr = IOUtils.toString(new FileInputStream(testXmlPath));
        return JAXBUtils.unMarshallMessage(fluxFaMessageStr, FLUXFAReportMessage.class);
    }

    public String getStrResponse() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:GetNonUniqueIdsResponse xmlns:ns2=\"http://europa.eu/ec/fisheries/uvms/activity/model/schemas\">\n" +
                "    <method>GET_NON_UNIQUE_IDS</method>\n" +
                "    <activityUniquinessList>\n" +
                "        <activityTableType>RELATED_FLUX_REPORT_DOCUMENT_ENTITY</activityTableType>\n" +
                "        <ids>\n" +
                "            <value>46DCC44C-0AE2-434C-BC14-B85D86B29512bbbbb</value>\n" +
                "            <identifierSchemeId>scheme-idqq</identifierSchemeId>\n" +
                "        </ids>\n" +
                "        <ids/>\n" +
                "    </activityUniquinessList>\n" +
                "    <activityUniquinessList>\n" +
                "        <activityTableType>FLUX_REPORT_DOCUMENT_ENTITY</activityTableType>\n" +
                "        <ids>\n" +
                "            <value>46DCC44C-0AE2-434C-BC14-B85D86B29512bbbbb</value>\n" +
                "            <identifierSchemeId>scheme-idqq</identifierSchemeId>\n" +
                "        </ids>\n" +
                "        <ids/>\n" +
                "    </activityUniquinessList>\n" +
                "</ns2:GetNonUniqueIdsResponse>";
    }

    public String getResponseStr2() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:GetFishingActivitiesForTripResponse xmlns:ns2=\"http://europa.eu/ec/fisheries/uvms/activity/model/schemas\"/>\n";
    }
}
