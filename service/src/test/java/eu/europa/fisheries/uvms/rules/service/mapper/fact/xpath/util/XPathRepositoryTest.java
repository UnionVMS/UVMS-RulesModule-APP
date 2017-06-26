/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.fisheries.uvms.rules.service.mapper.fact.xpath.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by kovian on 23/06/2017.
 */
public class XPathRepositoryTest {

    String testXmlPath = "src/test/resources/fluxFaResponseMessage.xml";

    XPathStringWrapper xpathUtil;

    XPathRepository repo;

    FLUXFAReportMessage fluxMessage;

    List<AbstractFact> factList;

    @Before
    @SneakyThrows
    public void initialize(){
        xpathUtil = XPathStringWrapper.INSTANCE;
        repo = XPathRepository.INSTANCE;
        fluxMessage = loadTestData();
        factList = new ArrayList<>();
        AbstractGenerator generator = BusinessObjectFactory.getBusinessObjFactGenerator(BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG);
        generator.setBusinessObjectMessage(fluxMessage);
        factList.addAll(generator.generateAllFacts());
    }

    @After
    public void tearDown(){
        xpathUtil.clear();
        repo.clear();
        fluxMessage = null;
        factList = null;
    }


    @Test
    @SneakyThrows
    public void testRepositoryInsertionAndExtractionOfXpaths(){

        assertNotNull(factList);
        assertTrue(CollectionUtils.isNotEmpty(factList));
        assertTrue(MapUtils.isNotEmpty(repo.getXpathsMap()));

        for(AbstractFact fact : factList){
            Map<String, String> xpathMap = repo.getMapForSequence(fact.getSequence());
            assertNotNull(xpathMap);
            System.out.print("Fact Class : ["+fact.getClass().getName()+"] \\n XPathsMap : "  +preetyPrint(xpathMap)+"\n");
        }
    }


    @Test
    public void testManualInsertion(){
        VesselTransportMeansFact vessFact = null;
        for(AbstractFact fact : factList){
            if(fact.getClass().equals(VesselTransportMeansFact.class)){
                vessFact = (VesselTransportMeansFact) fact;
                xpathUtil.clear();
                repo.clear();
                break;
            }
        }
        assertNotNull(vessFact);
        String partialXpath = xpathUtil.append(FLUX_RESPONSE_MESSAGE, SPECIFIED_FISHING_ACTIVITY, SPECIFIED_VESSEL_TRANSPORT_MEANS).getValue();

        xpathUtil.appendWithoutWrapping(partialXpath).append(ID).storeInRepo(vessFact, "ids");
        xpathUtil.appendWithoutWrapping(partialXpath).append(REGISTRATION_VESSEL_COUNTRY).storeInRepo(vessFact, "registrationVesselCountryId");
        xpathUtil.appendWithoutWrapping(partialXpath).append(ROLE_CODE).storeInRepo(vessFact, "roleCode");
        xpathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_CONTACT_PARTY).storeInRepo(vessFact, "specifiedContactParties");
        xpathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_CONTACT_PARTY, ROLE_CODE).storeInRepo(vessFact, "specifiedContactPartyRoleCodes");
        xpathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_CONTACT_PERSON).storeInRepo(vessFact, "specifiedContactPersons");

        int sequence = vessFact.getSequence();

        assertTrue(MapUtils.isNotEmpty(repo.getXpathsMap()));
        assertTrue(MapUtils.isNotEmpty(repo.getMapForSequence(sequence)));

        assertNotNull(repo.getMapForSequence(sequence, "ids"));
        assertNotNull(repo.getMapForSequence(sequence, "registrationVesselCountryId"));
        assertNotNull(repo.getMapForSequence(sequence, "roleCode"));
        assertNotNull(repo.getMapForSequence(sequence, "specifiedContactParties"));
        assertNotNull(repo.getMapForSequence(sequence, "specifiedContactPartyRoleCodes"));
        assertNotNull(repo.getMapForSequence(sequence, "specifiedContactPersons"));

    }


    private FLUXFAReportMessage loadTestData() throws IOException, MdrModelMarshallException {
        String fluxFaMessageStr = readTestFile(testXmlPath);
        return JAXBMarshaller.unmarshallTextMessage(fluxFaMessageStr, FLUXFAReportMessage.class);
    }


    /**
     * Read test data xml file and return it asa string.
     *
     * @return
     * @throws IOException
     */
    private String readTestFile(String fileName) throws IOException {
        return IOUtils.toString(new FileInputStream(fileName));
    }

    public static String preetyPrint(Object obj) throws JsonProcessingException {
        return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true).writeValueAsString(obj);
    }

}
