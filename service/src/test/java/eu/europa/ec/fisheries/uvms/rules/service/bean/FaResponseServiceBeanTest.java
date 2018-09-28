/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.xml.bind.UnmarshalException;
import java.util.HashMap;
import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesActivityServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFAResponseServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFaReportServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class FaResponseServiceBeanTest {

    @Mock private RulesMessageProducer producer;
    @Mock private RulesExchangeServiceBean exchangeServiceBean;
    @Mock private RulesEngineBean rulesEngine;
    @Mock private RulePostProcessBean rulesService;
    @Mock private RulesActivityServiceBean activityServiceBean;
    @Mock private ActivityOutQueueConsumer activityConsumer;
    @Mock private RulesFAResponseServiceBean faResponseValidatorAndSender;
    @Mock private RulesConfigurationCache ruleModuleCache;
    @Mock private RulesDao rulesDaoBean;
    @Mock private RulesFaReportServiceBean faReportRulesMessageBean;
    @InjectMocks private RulesFAResponseServiceBean faResponseRulesMessageServiceBean;
    @Mock private RulesFLUXMessageHelper fluxMessageHelper;

    private SetFluxFaResponseMessageRequest responseMessageRequest;
    private FLUXResponseMessage fluxResponseMessage = new FLUXResponseMessage();
    private FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
    private HashMap mdc = new HashMap<String, String>();

    @Before
    public void before(){
        faResponseRulesMessageServiceBean.init();
        Whitebox.setInternalState(faResponseRulesMessageServiceBean, "fluxMessageHelper", fluxMessageHelper);
        fluxResponseMessage.setFLUXResponseDocument(fluxResponseDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateIncomingFluxResponseRequestWithNullShouldThrowException(){
        faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(responseMessageRequest);
    }

    @Test
    public void testIncomingFluxResponseShouldEvaluateSaveValidationUpdateExchange() throws Exception {
        Mockito.when(fluxMessageHelper.unMarshallFluxResponseMessage(null)).thenReturn(fluxResponseMessage);
        responseMessageRequest = new SetFluxFaResponseMessageRequest();
        faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(responseMessageRequest);

        InOrder inOrder = inOrder(rulesEngine, rulesService, exchangeServiceBean);

        inOrder.verify(rulesEngine, times(1)).evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap(), anyString());
        inOrder.verify(rulesService, times(1)).checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class));
        inOrder.verify(exchangeServiceBean, times(1)).updateExchangeMessage(null, ExchangeLogStatusTypeType.UNKNOWN);
    }

    @Test
    public void testIncomingFluxResponseWithWrongXMLShouldUpdateExchange() throws RulesValidationException {

        try {
            Mockito.when(fluxMessageHelper.unMarshallFluxResponseMessage(null)).thenThrow(UnmarshalException.class);
            responseMessageRequest = new SetFluxFaResponseMessageRequest();
            faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(responseMessageRequest);
        } catch (Exception e) {
            assertTrue((e instanceof RulesServiceException));
            InOrder inOrder = inOrder(rulesEngine, rulesService, exchangeServiceBean);
            inOrder.verify(rulesEngine, times(0)).evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap(), anyString());
            inOrder.verify(rulesService, times(0)).checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class));
            inOrder.verify(exchangeServiceBean, times(1)).updateExchangeMessage(null, ExchangeLogStatusTypeType.FAILED);
        }

    }

    @Test
    public void testIncomingFluxResponseWithValidationExceptionShouldUpdateExchange() throws RulesValidationException {

        try {
            Mockito.when(fluxMessageHelper.unMarshallFluxResponseMessage(null)).thenReturn(fluxResponseMessage);
            Mockito.when(rulesEngine.evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap(), anyString())).thenThrow(RulesValidationException.class);
            responseMessageRequest = new SetFluxFaResponseMessageRequest();
            faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(responseMessageRequest);
        } catch (Exception e) {
            assertTrue((e instanceof RulesValidationException));
            InOrder inOrder = inOrder(rulesEngine, rulesService, exchangeServiceBean);
            inOrder.verify(rulesEngine, times(0)).evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap(), anyString());
            inOrder.verify(rulesService, times(0)).checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class));
            inOrder.verify(exchangeServiceBean, times(1)).updateExchangeMessage(null, ExchangeLogStatusTypeType.FAILED);
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateAndSendToExchangeWithNullID(){
        faResponseRulesMessageServiceBean.evaluateAndSendToExchange(fluxResponseMessage, new SetFaQueryMessageRequest(), PluginType.FLUX, true, mdc);
    }

    @Test
    public void testEvaluateAndSendToExchangeWithSetFaQueryMessageRequest() throws RulesValidationException, ServiceException {

        Mockito.when(fluxMessageHelper.getIDs(fluxResponseMessage)).thenReturn("value");

        faResponseRulesMessageServiceBean.evaluateAndSendToExchange(fluxResponseMessage, new SetFaQueryMessageRequest(), PluginType.FLUX, true, mdc);

        InOrder inOrder = inOrder(ruleModuleCache, rulesDaoBean, fluxMessageHelper, rulesEngine, rulesService);
        inOrder.verify(fluxMessageHelper, times(1)).getIDs(any(FLUXResponseMessage.class));
        inOrder.verify(ruleModuleCache, times(1)).getSingleConfig(anyString());
        inOrder.verify(fluxMessageHelper, times(1)).mapToResponseToFADocumentID(any(FLUXResponseMessage.class));
        inOrder.verify(rulesDaoBean, times(1)).loadFADocumentIDByIdsByIds(anySet());
        inOrder.verify(rulesEngine, times(1)).evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap());
       inOrder.verify(rulesService, times(1)).checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class));
        inOrder.verify(rulesDaoBean, times(1)).createFaDocumentIdEntity(anySet());

    }
}
