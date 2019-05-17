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
import java.util.ArrayList;
import java.util.Date;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFAResponseServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CodeTypeMapperImpl;
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class RulesFAResponseServiceBeanTest {

    @Mock private RulesExchangeServiceBean exchangeServiceBean;
    @Mock private RulesEngineBean rulesEngine;
    @Mock private RulePostProcessBean rulesService;
    @Mock private RulesFLUXMessageHelper fluxMessageHelper;
    @InjectMocks private RulesFAResponseServiceBean faResponseRulesMessageServiceBean;

    private SetFluxFaResponseMessageRequest responseMessageRequest;
    private FLUXResponseMessage fluxResponseMessage = new FLUXResponseMessage();
    private FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();

    @Before
    public void before(){
        faResponseRulesMessageServiceBean.init();
        Whitebox.setInternalState(faResponseRulesMessageServiceBean, "fluxMessageHelper", fluxMessageHelper);
        Whitebox.setInternalState(faResponseRulesMessageServiceBean, "codeTypeMapper", new CodeTypeMapperImpl());
        fluxResponseMessage.setFLUXResponseDocument(fluxResponseDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateIncomingFluxResponseRequestWithNullShouldThrowException(){
        faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(responseMessageRequest);
    }

    @Test
    public void testIncomingFluxResponseShouldEvaluateSaveValidationUpdateExchange() throws Exception {
        Mockito.when(fluxMessageHelper.unMarshallFluxResponseMessage(null)).thenReturn(fluxResponseMessage);
        Mockito.when(fluxMessageHelper.calculateMessageValidationStatus(null)).thenReturn(ExchangeLogStatusTypeType.UNKNOWN);

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
            inOrder.verify(exchangeServiceBean, times(1)).updateExchangeMessage(null, null);
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


    @Test
    public void testSendFLUXResponseMessageOnEmptyResultOrPermissionDenied(){

        ValidationResult validationResult = new ValidationResult();
        validationResult.setValidationMessages(new ArrayList<ValidationMessageType>());

        Mockito.when(rulesService.checkAndUpdateValidationResultForGeneralBusinessRules(any(RuleError.class), anyString(), anyString(), any(RawMsgType.class), any(Date.class))).thenReturn(validationResult);

        eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest rulesBaseRequest = new SetFaQueryMessageRequest();
        rulesBaseRequest.setLogGuid("guid");

        faResponseRulesMessageServiceBean.sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(null, rulesBaseRequest, null, Rule9998Or9999ErrorType.PERMISSION_DENIED, null, validationResult);

        Mockito.verify(exchangeServiceBean, times(1)).evaluateAndSendToExchange(any(FLUXResponseMessage.class), any(eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest.class), any(PluginType.class), anyBoolean(), anyMap());

    }

}
