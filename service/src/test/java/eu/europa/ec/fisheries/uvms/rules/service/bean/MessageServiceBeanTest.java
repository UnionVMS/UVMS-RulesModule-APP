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

import java.util.Arrays;
import java.util.Collections;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by padhyad on 6/7/2017.
 */
public class MessageServiceBeanTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    MessageServiceBean messageServiceBean;

    @Mock
    RulesResponseConsumer consumer;

    @Mock
    RulesMessageProducer producer;

    @Mock
    RulesEngineBean rulesEngine;

    @Mock
    RulePostProcessBean rulePostprocessBean;

    @Mock
    RulesPreProcessBean rulesPreProcessBean;

    @Mock
    RulesConfigurationCache ruleModuleCache;

    @Test
    public void testGenerateFluxResponseMessage() {
        when(ruleModuleCache.getSingleConfig(any(String.class))).thenReturn("XEU");
        FLUXResponseMessage fluxResponseMessage = messageServiceBean.generateFluxResponseMessage(getValidationResult(), getFluxFaReportMessage());
        assertNotNull(fluxResponseMessage);
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getIDS());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getReferencedID());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRelatedValidationResultDocuments());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRejectionReason());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRespondentFLUXParty());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getResponseCode());
    }

    @Test
    public void testSendRequestToActivity() throws RulesServiceException, MessageException {
        Mockito.doReturn("abc-def").when(producer).sendDataSourceMessage(Mockito.anyString(), any(DataSourceQueue.class));
        messageServiceBean.sendRequestToActivity("<FLUXFaReportMessage></FLUXFaReportMessage>", "test", PluginType.FLUX);
    }

    @Test
    public void testSendResponseToExchange() throws RulesServiceException, RulesValidationException {
        when(ruleModuleCache.getSingleConfig(any(String.class))).thenReturn("XEU");
        FLUXResponseMessage fluxResponseMessage = messageServiceBean.generateFluxResponseMessage(getValidationResult(), getFluxFaReportMessage());
        Mockito.doReturn(Collections.emptyList()).when(rulesEngine).evaluate(BusinessObjectType.FLUX_ACTIVITY_RESPONSE_MSG, fluxResponseMessage);
        Mockito.doReturn(getValidationResult()).when(rulePostprocessBean).checkAndUpdateValidationResult(Mockito.anyList(), Mockito.anyString());
        messageServiceBean.sendResponseToExchange(fluxResponseMessage, "USER1");
    }

    private ValidationResultDto getValidationResult() {
        ValidationResultDto faReportValidationResult = new ValidationResultDto();
        faReportValidationResult.setIsError(true);
        faReportValidationResult.setIsOk(false);
        faReportValidationResult.setIsWarning(false);

        ValidationMessageType validationMessageType = new ValidationMessageType();
        validationMessageType.setBrId("brid1");
        validationMessageType.setErrorType(ErrorType.ERROR);
        validationMessageType.setLevel("L01");
        validationMessageType.setMessage("Test message");
        faReportValidationResult.setValidationMessages(Arrays.asList(validationMessageType));
        return faReportValidationResult;
    }

    private FLUXFAReportMessage getFluxFaReportMessage() {
        FLUXReportDocument fluxReportDocument = new FLUXReportDocument();
        IDType id = new IDType();
        id.setSchemeID("123-abc");
        id.setValue("val1");
        fluxReportDocument.setIDS(Arrays.asList(id));


        FLUXFAReportMessage msg = new FLUXFAReportMessage();
        msg.setFLUXReportDocument(fluxReportDocument);

        FAReportDocument doc = new FAReportDocument();
        doc.setRelatedFLUXReportDocument(fluxReportDocument);
        msg.setFAReportDocuments(Arrays.asList(doc));
        return msg;
    }
}
