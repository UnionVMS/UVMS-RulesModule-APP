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

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ACTIVITY_NON_UNIQUE_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ACTIVITY_WITH_TRIP_IDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.FaReportFactRuleEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.MasterEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

/**
 * Created by padhyad on 6/7/2017.
 */
public class RulesEngineBeanTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    RulesEngineBean rulesEngineBean;

    @InjectMocks
    RulesExtraValuesMapGeneratorBean extraValueGenerator;

    @Mock
    MDRCacheServiceBean mdrCacheServiceBean;

    @Mock
    MasterEvaluator templateEngine;

    @Mock
    RulesDomainModel rulesDb;

    @Mock
    FaReportFactRuleEvaluator ruleEvaluator;

    @Mock
    RuleAssetsBean ruleAssetsBean;

    @Mock
    RulesActivityServiceBean activityService;

    @Mock
    RulesFishingGearBean rulesFishingGearBean;

    @Test
    public void testEvaluate() throws RulesValidationException {

        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object facts = args[0];
                for (AbstractFact obj : (ArrayList<AbstractFact>)facts) {
                    obj.addWarningOrError("ERROR", "Error code", "br01", "L01", "null");
                    obj.setOk(false);
                }
                System.out.println("called with arguments: " + Arrays.toString(args));
                return null;
            }
        }).when(templateEngine).evaluateFacts(Mockito.anyList(), BusinessObjectType.RECEIVING_FA_REPORT_MSG);

        List<AbstractFact> facts = rulesEngineBean.evaluate(BusinessObjectType.RECEIVING_FA_REPORT_MSG, getFluxFaReportMessage());
        assertNotNull(facts);
        AbstractFact fact = facts.get(0);
        assertFalse(fact.isOk());
        assertEquals(false, fact.getErrors().isEmpty());
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

    @Test
    public void testGenerateExtraValueMap(){
        Map<ExtraValueType, Object> extraValueTypeObjectMap = extraValueGenerator.generateExtraValueMap(BusinessObjectType.RECEIVING_FA_REPORT_MSG, getFluxFaReportMessage(), "XEU");
        assertEquals(0, ((Map) extraValueTypeObjectMap.get(ACTIVITY_NON_UNIQUE_IDS)).size());
        assertEquals(0, ((Map) extraValueTypeObjectMap.get(ACTIVITY_WITH_TRIP_IDS)).size());
    }
}
