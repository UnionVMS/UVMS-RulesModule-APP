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
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesActivityServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFaQueryServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class RulesFaQueryServiceBeanTest {

    @Mock private RulesFLUXMessageHelper fluxMessageHelper;
    @Mock private RulesDao rulesDaoBean;
    @Mock private RulesEngineBean rulesEngine;
    @Mock private RulePostProcessBean rulePostProcessBean;
    @Mock private RulesExchangeServiceBean exchangeServiceBean;
    @Mock private RulesActivityServiceBean activityService;

    @InjectMocks
    private RulesFaQueryServiceBean rulesFaQueryServiceBean;

    private FLUXFAQueryMessage fluxfaQueryMessage = new FLUXFAQueryMessage();

    @Before
    public void before(){
        FAQuery faQuery = new FAQuery();
        faQuery.setID(new IDType());
        fluxfaQueryMessage.setFAQuery(faQuery);
    }

    @Test
    public void testEvaluateIncomingFAQuery() throws UnmarshalException, RulesValidationException {

        Mockito.when(fluxMessageHelper.unMarshallFaQueryMessage(null)).thenReturn(fluxfaQueryMessage);

        rulesFaQueryServiceBean.evaluateIncomingFAQuery(new SetFaQueryMessageRequest());

        InOrder inOrder = inOrder(rulesDaoBean, rulesEngine, rulePostProcessBean, exchangeServiceBean, activityService);

        inOrder.verify(rulesDaoBean, times(1)).loadFADocumentIDByIdsByIds(anySet());
        inOrder.verify(rulesEngine, times(1)).evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap(), anyString());
        inOrder.verify(rulePostProcessBean, times(1)).checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class));
        inOrder.verify(exchangeServiceBean, times(1)).evaluateAndSendToExchange(any(FLUXResponseMessage.class), any(RulesBaseRequest.class), any(PluginType.class),anyBoolean(), anyMap());
        inOrder.verify(activityService, times(0)).checkSubscriptionPermissions(anyString(), any(MessageType.class));

    }

    @Test
    public void testEvaluateOutgoingFAQuery() throws RulesValidationException {

        rulesFaQueryServiceBean.evaluateOutgoingFAQuery(new SetFaQueryMessageRequest());

        InOrder inOrder = inOrder(rulesDaoBean, rulesEngine, rulePostProcessBean, exchangeServiceBean, activityService);

        inOrder.verify(rulesDaoBean, times(1)).loadFADocumentIDByIdsByIds(anySet());
        inOrder.verify(rulesEngine, times(1)).evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap());
        inOrder.verify(rulePostProcessBean, times(1)).checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class));
        inOrder.verify(exchangeServiceBean, times(0)).evaluateAndSendToExchange(any(FLUXResponseMessage.class), any(RulesBaseRequest.class), any(PluginType.class),anyBoolean(), anyMap());
        inOrder.verify(activityService, times(0)).checkSubscriptionPermissions(anyString(), any(MessageType.class));

    }

}