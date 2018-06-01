/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/

package eu.europa.fisheries.uvms.rules.service.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityQueryFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityFaReportFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityResponseFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

/**
 * Created by kovian on 28/06/2017.
 */
public class BusinessObjectFactoryTest {

    String testXmlPath = "src/test/resources/testData/manyFishingActivityTypes.xml";

    @Test
    public void testAllValuesAreInGenerateor(){

        for(BusinessObjectType objType : BusinessObjectType.values()){
            AbstractGenerator businessObjFactGenerator = BusinessObjectFactory.getBusinessObjFactGenerator(objType);
            assertNotNull(businessObjFactGenerator);
            System.out.println("For BusinessObjectType ["+objType+"] found Generator ["+businessObjFactGenerator.getClass()+"]");
        }
    }

    @Test
    public void testGetBusinessObjFactoryNull(){
        AbstractGenerator businessObjFactGenerator = BusinessObjectFactory.getBusinessObjFactGenerator(null);
        assertNull(businessObjFactGenerator);
    }


    @Test
    @SneakyThrows
    public void testAllFishingActivityTypeFromGenerator(){
        ActivityFaReportFactGenerator generator = new ActivityFaReportFactGenerator();
        generator.setBusinessObjectMessage(loadTestData(testXmlPath));
        List<AbstractFact> facts = generator.generateAllFacts();

        assertNotNull(facts);
        assertTrue(CollectionUtils.isNotEmpty(facts));

        List<FactType> factTypes = new ArrayList<>();
        for(AbstractFact fact : facts){
            factTypes.add(fact.getFactType());
        }

        assertTrue(factTypes.contains(FactType.FISHING_ACTIVITY));
        assertTrue(factTypes.contains(FactType.FA_DEPARTURE));
        assertTrue(factTypes.contains(FactType.FA_EXIT_FROM_SEA));
        assertTrue(factTypes.contains(FactType.FA_ENTRY_TO_SEA));
        assertTrue(factTypes.contains(FactType.FA_JOINT_FISHING_OPERATION));
        assertTrue(factTypes.contains(FactType.FA_LANDING));
        assertTrue(factTypes.contains(FactType.FA_TRANSHIPMENT));
    }

    @Test
    @SneakyThrows
    public void testActivityResponseGeneration(){
        ActivityResponseFactGenerator actRespGenerator = new ActivityResponseFactGenerator();
        actRespGenerator.setBusinessObjectMessage(new FLUXResponseMessage());
        List<AbstractFact> abstractFacts = actRespGenerator.generateAllFacts();

        assertTrue(abstractFacts.size() == 1);
    }

    @Test
    public void testActivityResponseGenerationNULL(){
        ActivityResponseFactGenerator actRespGenerator = new ActivityResponseFactGenerator();
        boolean threw = false;
        try {
            actRespGenerator.setBusinessObjectMessage(null);
        } catch (RulesValidationException e) {
           threw = true;
        }
        assertTrue(threw);
    }

    @Test
    public void testSetBusinessObjectMessageNull_ActivityQueryFactGenerator() {
        try {
            ActivityQueryFactGenerator activityQueryFactGenerator = new ActivityQueryFactGenerator();
            activityQueryFactGenerator.setBusinessObjectMessage(new FLUXFAQueryMessage());
        } catch (RulesValidationException e) {
            assertNull(e);
        }
    }

    @Test(expected = RulesValidationException.class)
    @SneakyThrows
    public void testSetBusinessObjectMessageException_ActivityQueryFactGenerator() {
        ActivityQueryFactGenerator activityQueryFactGenerator = new ActivityQueryFactGenerator();
        activityQueryFactGenerator.setBusinessObjectMessage(new Object());
    }

    @Test(expected = NullPointerException.class)
    @Ignore
    @SneakyThrows
    public void testSetAdditionalValidation_ActivityQueryFactGenerator() {
        ActivityQueryFactGenerator activityQueryFactGenerator = new ActivityQueryFactGenerator();
        activityQueryFactGenerator.setAdditionalValidationObject();
        activityQueryFactGenerator.setAdditionalValidationObject();
        activityQueryFactGenerator.setAdditionalValidationObject();
        activityQueryFactGenerator.setAdditionalValidationObject();
    }

    @Test
    public void testGenerateAllFacts_ActivityQueryFactGenerator() {
        ActivityQueryFactGenerator activityQueryFactGenerator = new ActivityQueryFactGenerator();
        List<AbstractFact> facts = activityQueryFactGenerator.generateAllFacts();
        assertNotNull(facts);
    }


    @SneakyThrows
    private FLUXFAReportMessage loadTestData(String filePath) {
        String fluxFaMessageStr = IOUtils.toString(new FileInputStream(filePath));
        return JAXBUtils.unMarshallMessage(fluxFaMessageStr, FLUXFAReportMessage.class);
    }
}
