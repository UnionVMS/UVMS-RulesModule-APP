/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.FaReportRulesMessageServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.FaResponseRulesMessageServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesActivityServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FAReportQueryResponseIdsMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FLUXMessageHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;

@RunWith(MockitoJUnitRunner.class)
public class FaResponseRulesMessageServiceBeanTest {

    @Mock private RulesMessageProducer producer;
    @Mock private ExchangeServiceBean exchangeServiceBean;
    @Mock private RulesEngineBean rulesEngine;
    @Mock private RulePostProcessBean rulePostProcessBean;
    @Mock private RulesActivityServiceBean activityServiceBean;
    @Mock private ActivityOutQueueConsumer activityConsumer;
    @Mock private FaResponseRulesMessageServiceBean faResponseValidatorAndSender;
    @Mock private RulesDao rulesDaoBean;
    @Mock private FaReportRulesMessageServiceBean faReportRulesMessageBean;
    @InjectMocks private FaResponseRulesMessageServiceBean faResponseRulesMessageServiceBean;
    @Mock private FLUXMessageHelper fluxMessageHelper;

    private SetFluxFaResponseMessageRequest responseMessageRequest;
    private FLUXFAReportMessage fluxResponseMessage = new FLUXFAReportMessage();
    private FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
    private FAReportQueryResponseIdsMapper faIdsMapper;

    @Before
    public void before(){
       // fluxResponseMessage.setFAReportDocuments(fluxResponseDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateIncomingFluxResponseRequestWithNullShouldThrowException(){
        faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(responseMessageRequest);
    }
    @Test
    public void testEvaluateIncomingFluxResponseRequest() throws Exception {
        Mockito.when(fluxMessageHelper.unMarshallAndValidateSchema(null)).thenReturn(fluxResponseMessage);
        responseMessageRequest = new SetFluxFaResponseMessageRequest();
        faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(responseMessageRequest);
        // Mockito.verify(rulesEngine, times(1)).evaluate(null, null, null, null);
        // Mockito.verify(exchangeServiceBean, times(1)).updateExchangeMessage(null, ExchangeLogStatusTypeType.UNKNOWN);
        faResponseRulesMessageServiceBean.evaluateIncomingFluxResponseRequest(responseMessageRequest);
    }

}
