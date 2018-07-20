/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import javax.jms.TextMessage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import lombok.SneakyThrows;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

/**
 * Created by kovian on 06/07/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RuleAssetsBeanTest {

    String testXmlPath = "src/test/resources/testData/fluxFaResponseMessage.xml";
    String testXmlPathWithoutVesselIDs = "src/test/resources/testData/fluxFaResponseMessage_without_vessIDs.xml";

    FLUXFAReportMessage faReportMessage;

    @Mock
    RuleAssetsBean ruleAssetsBean;

    @Mock
    RulesResponseConsumer consumer;

    @Mock
    RulesMessageProducer producer;

    @Mock
    ClientSession session;

    @Mock
    ActiveMQTextMessage textMessage;

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();


    @Before
    @SneakyThrows
    public void setUp() {
        ruleAssetsBean = new RuleAssetsBean();
        textMessage = new ActiveMQTextMessage(session);
        Whitebox.setInternalState(ruleAssetsBean, "consumer", consumer);
        Whitebox.setInternalState(ruleAssetsBean, "producer", producer);
        Whitebox.setInternalState(textMessage, "text", new SimpleString(getMockedAssetListResponse()));
        Whitebox.setInternalState(textMessage, "jmsCorrelationID", "SomeCorrId");
        faReportMessage = loadTestData(testXmlPath);
    }

    @After
    public void cleanUpEnviroment() {
        faReportMessage = null;
        ruleAssetsBean = null;
        consumer = null;
        producer = null;
        session = null;
        textMessage = null;
    }

    @Test
    @SneakyThrows
    public void testGetRuleAssetsList() {
        when(producer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.ASSET))).thenReturn("SomeCorrId");
        when(consumer.getMessage(anyString(), eq(TextMessage.class))).thenReturn(textMessage);

      //  List<IdTypeWithFlagState> assetList = ruleAssetsBean.getAssetListCFR(faReportMessage);
        //assertNotNull(assetList);
        //assertTrue(assetList.size() == 4);
    }

    @Test
    @SneakyThrows
    public void testGetRuleAssetsListNullpointer() {
        when(producer.sendDataSourceMessage(anyString(), eq(DataSourceQueue.ASSET))).thenReturn("SomeCorrId");
        when(consumer.getMessage(anyString(), eq(TextMessage.class))).thenReturn(textMessage);

       // List<IdTypeWithFlagState> assetList = ruleAssetsBean.getAssetListCFR(null);
       // assertTrue(CollectionUtils.isEmpty(assetList));
    }

    @SneakyThrows
    private FLUXFAReportMessage loadTestData(String testXml) throws IOException, MdrModelMarshallException {
        String fluxFaMessageStr = IOUtils.toString(new FileInputStream(testXml));
        return JAXBUtils.unMarshallMessage(fluxFaMessageStr, FLUXFAReportMessage.class);
    }

    public String getMockedAssetListResponse() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:ListAssetResponse xmlns:ns2=\"types.asset.wsdl.fisheries.ec.europa.eu\">\n" +
                "    <totalNumberOfPages>1</totalNumberOfPages>\n" +
                "    <currentPage>1</currentPage>\n" +
                "    <asset>\n" +
                "        <assetId>\n" +
                "            <type>GUID</type>\n" +
                "            <value>01c8eefe-daee-4cc7-b662-85a040879c0f</value>\n" +
                "            <guid>01c8eefe-daee-4cc7-b662-85a040879c0f</guid>\n" +
                "        </assetId>\n" +
                "        <active>true</active>\n" +
                "        <source>NATIONAL</source>\n" +
                "        <eventHistory>\n" +
                "            <eventId>5c9ef916-c7bb-4a3f-b00f-c9a6e2b6f38f</eventId>\n" +
                "            <eventCode>MOD</eventCode>\n" +
                "            <eventDate>2015-10-06T09:29:19.472Z</eventDate>\n" +
                "        </eventHistory>\n" +
                "        <name>EMMALIE</name>\n" +
                "        <countryCode>DNK</countryCode>\n" +
                "        <gearType>UNKNOWN</gearType>\n" +
                "        <hasIrcs>Y</hasIrcs>\n" +
                "        <ircs>OZHX</ircs>\n" +
                "        <externalMarking>RI433</externalMarking>\n" +
                "        <cfr>SWE000000069</cfr>\n" +
                "        <hasLicense>true</hasLicense>\n" +
                "        <homePort>DKHVS</homePort>\n" +
                "        <lengthOverAll>33.00</lengthOverAll>\n" +
                "        <grossTonnageUnit>LONDON</grossTonnageUnit>\n" +
                "        <rulesProducer>\n" +
                "            <id>1</id>\n" +
                "            <code>2</code>\n" +
                "            <zipcode>0</zipcode>\n" +
                "        </rulesProducer>\n" +
                "        <contact>\n" +
                "            <name>Valen</name>\n" +
                "            <number>0701333333</number>\n" +
                "            <email>val@havet</email>\n" +
                "            <owner>true</owner>\n" +
                "            <source>INTERNAL</source>\n" +
                "        </contact>\n" +
                "        <contact>\n" +
                "            <name>Hajen</name>\n" +
                "            <number>0701444444</number>\n" +
                "            <email>haj@havet</email>\n" +
                "            <owner>false</owner>\n" +
                "            <source>NATIONAL</source>\n" +
                "        </contact>\n" +
                "    </asset>\n" +
                "</ns2:ListAssetResponse>\n";
    }
}
