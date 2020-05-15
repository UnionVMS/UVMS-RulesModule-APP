/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesActivityProducerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesExchangeProducerBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesActivityServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFaReportServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.IAssetClient;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;

import javax.ejb.EJB;
import javax.jms.Destination;
import javax.xml.bind.UnmarshalException;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class RulesFAReportServiceBeanTest {

    @Mock private RulesExchangeServiceBean exchangeServiceBean;
    @Mock private RulesActivityServiceBean rulesActivityService;
    @Mock private RulesEngineBean rulesEngine;
    @Mock private RulesDao rulesDaoBean;
    @Mock private IAssetClient assetServiceBean;
    @Mock private RulePostProcessBean rulesService;
    @Mock private RulesFLUXMessageHelper fluxMessageHelper;
    @Mock private RulesResponseConsumer rulesConsumer;
    @Mock private RulesActivityProducerBean activityProducer;
    @Mock private RulesExchangeProducerBean exchangeProducer;
    @Mock private GearMatrix fishingGearTypeCharacteristics;
    @InjectMocks private RulesFaReportServiceBean rulesFaReportServiceBean;

    private SetFLUXFAReportMessageRequest fluxfaReportMessageRequest;
    private FLUXFAReportMessage fluxfaReportMessage = new FLUXFAReportMessage();
    private FLUXReportDocument fluxReportDocument = new FLUXReportDocument();

    @Before
    public void before(){
        fluxfaReportMessageRequest = new SetFLUXFAReportMessageRequest();
        fluxfaReportMessageRequest.setPluginType(PluginType.FLUX);
        fluxfaReportMessageRequest.setLogGuid("guid");
        rulesFaReportServiceBean.init();
        Whitebox.setInternalState(rulesFaReportServiceBean, "fluxMessageHelper", fluxMessageHelper);
        fluxfaReportMessage.setFLUXReportDocument(fluxReportDocument);
        fluxfaReportMessage.getFAReportDocuments().add(new FAReportDocument());
    }

    @Test
    public void testEvaluateIncomingFluxFAHappy() throws UnmarshalException, RulesValidationException {

        Mockito.when(fluxMessageHelper.unMarshallAndValidateSchema(null)).thenReturn(fluxfaReportMessage);

        ValidationResult validationResult = new ValidationResult();
        validationResult.setError(false);
        Mockito.when(rulesService.checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class))).thenReturn(validationResult);
        Mockito.when(rulesActivityService.checkSubscriptionPermissions(anyString(), any(eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType.class))).thenReturn(true);

        rulesFaReportServiceBean.evaluateIncomingFLUXFAReport(fluxfaReportMessageRequest);

        InOrder inOrder = inOrder(rulesDaoBean, assetServiceBean, rulesEngine, rulesService, exchangeServiceBean, rulesActivityService);

        inOrder.verify(rulesDaoBean, times(1)).loadFADocumentIDByIdsByIds(anySet());
        inOrder.verify(rulesDaoBean, times(1)).loadExistingFaIdsPerTrip(anyList());
        inOrder.verify(assetServiceBean, times(1)).findHistoryOfAssetBy(anyList());
        inOrder.verify(rulesEngine, times(1)).evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap(), Matchers.anyString());
        inOrder.verify(rulesService, times(1)).checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class));
        inOrder.verify(rulesActivityService, times(1)).checkSubscriptionPermissions(anyString(), any(MessageType.class));
        inOrder.verify(rulesDaoBean, times(1)).saveFaIdsPerTripList(anyList());

    }

    @Test
    public void testEvaluateOutgoingFluxFAHappy() throws UnmarshalException, RulesValidationException, MessageException {

        Mockito.when(fluxMessageHelper.unMarshallAndValidateSchema(null)).thenReturn(fluxfaReportMessage);
        ValidationResult validationResult = new ValidationResult();
        validationResult.setError(false);
        Mockito.when(rulesService.checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class))).thenReturn(validationResult);

        rulesFaReportServiceBean.evaluateOutgoingFaReport(fluxfaReportMessageRequest);

        InOrder inOrder = inOrder(rulesDaoBean, assetServiceBean, rulesEngine, rulesService, exchangeServiceBean, rulesActivityService, exchangeProducer);

        inOrder.verify(rulesDaoBean, times(1)).loadFADocumentIDByIdsByIds(anySet());
        inOrder.verify(rulesDaoBean, times(1)).loadExistingFaIdsPerTrip(anyList());
        inOrder.verify(assetServiceBean, times(1)).findHistoryOfAssetBy(anyList());
        inOrder.verify(rulesEngine, times(1)).evaluate(any(BusinessObjectType.class), Matchers.anyObject(), anyMap(), Matchers.anyString());
        inOrder.verify(rulesService, times(1)).checkAndUpdateValidationResult(anyCollection(), anyString(), anyString(), any(RawMsgType.class));
        inOrder.verify(exchangeProducer, times(1)).sendModuleMessage(anyString(), any(Destination.class));

    }
}
