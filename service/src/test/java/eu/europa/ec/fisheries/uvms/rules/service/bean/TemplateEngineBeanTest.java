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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.DroolsEngineInitializer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.FaReportFactRuleEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.MasterEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityFaReportFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
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
public class TemplateEngineBeanTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    MasterEvaluator templateEngine;

    @InjectMocks
    DroolsEngineInitializer droolesInitializer;

    @Mock
    RulesDomainModel rulesDb;

    @Mock
    MDRCache mdrCache;

    @Mock
    FaReportFactRuleEvaluator ruleEvaluator;

    @Mock
    RulesStatusUpdater rulesStatusUpdaterBean;

    @Mock
    private MDRCacheRuleService cacheService;

    @Test
    public void testEvaluateFacts() throws RulesValidationException {

        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object facts = args[0];
                for (AbstractFact obj : (List<AbstractFact>) facts) {
                    obj.addWarningOrError("ERROR", "Error code", "br01", "L01", "null");
                    obj.setOk(false);
                }
                System.out.println("called with arguments: " + Arrays.toString(args));
                return null;
            }
        }).when(ruleEvaluator).validateFacts(Mockito.anyList());

        List<AbstractFact> facts = new ArrayList<>();
        ActivityFaReportFactGenerator generator = new ActivityFaReportFactGenerator();
        generator.setBusinessObjectMessage(getFluxFaReportMessage());
        facts.addAll(generator.generateAllFacts());
        templateEngine.evaluateFacts(facts, BusinessObjectType.RECEIVING_FA_REPORT_MSG);

        assertNotNull(facts);
        AbstractFact fact = facts.get(0);
        assertFalse(fact.isOk());
        assertEquals(false, fact.getErrors().isEmpty());
    }

    @Test
    public void testGenaratorThrows(){
        ActivityFaReportFactGenerator generator = new ActivityFaReportFactGenerator();
        boolean threw = false;
        try {
            generator.setBusinessObjectMessage(new String());
        } catch (RulesValidationException e) {
            threw = true;
        }
        assertTrue(threw);
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
