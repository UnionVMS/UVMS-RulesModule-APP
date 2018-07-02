/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.business;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.FLUX_RESPONSE_MESSAGE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.ID;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.REGISTRATION_VESSEL_COUNTRY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.ROLE_CODE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_CONTACT_PARTY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_CONTACT_PERSON;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FISHING_ACTIVITY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_VESSEL_TRANSPORT_MEANS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityFaReportFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

/**
 * Created by kovian on 23/06/2017.
 */
@Ignore
public class XPathRepositoryTest {

    String testXmlPath = "src/test/resources/testData/fluxFaResponseMessage.xml";
    XPathStringWrapper xpathUtil;
    XPathRepository repo;
    FLUXFAReportMessage fluxMessage;
    List<AbstractFact> factList;
    Map<String, String> failedMap;

    @Before
    @SneakyThrows
    public void initialize(){
        xpathUtil = new XPathStringWrapper();
        repo = XPathRepository.INSTANCE;
        fluxMessage = loadTestData();
        failedMap = new HashMap<>();
        generateFactList();
    }

    @After
    public void tearDown(){
        xpathUtil.clear();
        repo.getXpathsMap().clear();
        failedMap.clear();
        fluxMessage = null;
        factList = null;
    }

    @Test
    @SneakyThrows
    public void testConcurrencyNotTemperingWithRepositoryValues(){

        final int expectedFinalFactListSize = factList.size();

        // Threads creation
        Thread thread1 = new Thread(getRunnable(1, 0));
        thread1.setName("Thread1");
        Thread thread2 = new Thread(getRunnable(2, 1000));
        thread2.setName("Thread2");
        Thread thread3 = new Thread(getRunnable(3, 500));
        thread3.setName("Thread3");
        Thread thread4 = new Thread(getRunnable(1, 0));
        thread4.setName("Thread4");
        Thread thread5 = new Thread(getRunnable(2, 1000));
        thread5.setName("Thread5");
        Thread thread6 = new Thread(getRunnable(3, 500));
        thread6.setName("Thread6");
        Thread thread7 = new Thread(getRunnable(1, 0));
        thread7.setName("Thread7");
        Thread thread8 = new Thread(getRunnable(2, 1000));
        thread8.setName("Thread8");
        Thread thread9 = new Thread(getRunnable(3, 500));
        thread9.setName("Thread9");

        // Threads start
        thread1.start(); repo.clear(factList);
        thread2.start(); repo.clear(factList);
        thread3.start(); repo.clear(factList);
        thread4.start(); repo.clear(factList);
        thread5.start(); repo.clear(factList);
        thread6.start(); repo.clear(factList);
        thread7.start(); repo.clear(factList);
        thread8.start(); repo.clear(factList);
        thread9.start(); repo.clear(factList);

        Thread.sleep(2000);

        final Map<Integer, Map<String, String>> xpathsMap = repo.getXpathsMap();
        for(Map.Entry<Integer, Map<String, String>> outMap : xpathsMap.entrySet()){
            for(Map.Entry<String, String> inMap : outMap.getValue().entrySet()){
                String value = inMap.getValue();
                testValueForDoubles(value);
            }
        }

        final int realFinalFactListSize = factList.size();
        boolean success = true;
        if(MapUtils.isNotEmpty(failedMap)){
            for(Map.Entry<String, String> val : failedMap.entrySet()){
                System.out.println("\nFailed value : " +val.getValue()+ " Result : " + val.getKey());
                success = false;
            }
        } else {
            System.out.println("Failed Map is empty. No errors..");
        }
        assertTrue(success);
        System.out.println("Initial fact size : ["+expectedFinalFactListSize+"]. End fact size : ["+realFinalFactListSize+"]");


    }

    private String testValueForDoubles(String value) {
        String test = StringUtils.countMatches(value,"FLUXFAReportMessage") > 1 ? "NO" : "OK";
        if("NO".equals(test)){
            failedMap.put(test, value);
        }
        return test;
    }

    private Runnable getRunnable(final int runnableIndex, final int waitTime) {
        return new Runnable() {
                @Override
                @SneakyThrows
                public void run() {
                    System.out.println("Runnable ["+runnableIndex+"] START");
                    Thread.sleep(waitTime);
                    generateFactList();
                    System.out.println("Runnable ["+runnableIndex+"] END");
                }
            };
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
            System.out.print("\nFact Class : ["+fact.getClass().getName()+"] \\n XPathsMap : "  +preetyPrint(xpathMap)+"\n");
        }
    }

    @Test
    @SneakyThrows
    public void testFirstElementIsAlwaysFluxFaMessage(){

        assertNotNull(factList);
        assertTrue(CollectionUtils.isNotEmpty(factList));
        assertTrue(MapUtils.isNotEmpty(repo.getXpathsMap()));

        for(AbstractFact fact : factList){
            Map<String, String> xpathMap = repo.getMapForSequence(fact.getSequence());
            assertNotNull(xpathMap);
            for(Map.Entry<String, String> entrySet : xpathMap.entrySet()){
                String value = entrySet.getValue();
                int matches = StringUtils.countMatches(value, "FLUXFAReportMessage");
                if(matches == 2){ // Control for dubble appending!
                    System.out.print("\nFact Class : "+fact.getClass().getName());
                    System.out.print("\nValue xpath : "+value);
                    fail("Found 'FLUXFAReportMessage' twice in the same XPATH!");
                }
                System.out.print("\nTesting value : " + value);
                int valMaxLength = value.length() >= 50 ? 50 : value.length();
                String val = value.substring(0, valMaxLength);
                if(!val.contains("FLUXFAReportMessage")){
                    System.out.print("Testing value fails : "+value+"\n Fact Class : "+fact.getClass().getName());
                }
                assertTrue(val.contains("FLUXFAReportMessage"));
            }
        }
    }

    @Test
    @SneakyThrows
    @Ignore
    public void testXpathCorrectness(){

        String filePath = new File(".", testXmlPath).getAbsolutePath();

        VTDGen vdGen =  new VTDGen();
        boolean fileParsed = vdGen.parseFile(filePath, true);

        assertTrue(fileParsed);

        VTDNav vn = vdGen.getNav();
        AutoPilot ap = new AutoPilot(vn);

        for(AbstractFact fact : factList){
            Map<String, String> xpathMap = repo.getMapForSequence(fact.getSequence());
            assertNotNull(xpathMap);
            for(Map.Entry<String, String> entrySet : xpathMap.entrySet()){
                String xpathVal = entrySet.getValue();
                if(!validateXPath(xpathVal, ap, vn, fact)){
                    fail("\nTesting xpathVal fails : "+xpathVal+"\nFact Class : "+fact.getClass().getName());
                }
            }
        }
    }

    @Test
    public void testManualInsertion(){
        VesselTransportMeansFact vessFact = null;
        for(AbstractFact fact : factList){
            if(fact.getClass().equals(VesselTransportMeansFact.class)){
                vessFact = (VesselTransportMeansFact) fact;
                xpathUtil.clear();
                repo.getXpathsMap().clear();
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

    @Test
    public void testXpathNullPassing() {
        xpathUtil.append();
        xpathUtil.append(StringUtils.EMPTY);
    }

    @Test
    @SneakyThrows
    public void testAllXPathConstants(){
        List<String> constantsList = new ArrayList<>();
        constantsList.add(XPathConstants.FLUXFA_REPORT_MESSAGE);
        constantsList.add(XPathConstants.FA_REPORT_DOCUMENT);
        constantsList.add(XPathConstants.SPECIFIED_FISHING_ACTIVITY);
        constantsList.add(XPathConstants.FLUX_REPORT_DOCUMENT);
        constantsList.add(XPathConstants.CREATION_DATE_TIME);
        constantsList.add(XPathConstants.ID);
        constantsList.add(XPathConstants.REFERENCED_ID);
        constantsList.add(XPathConstants.OWNER_FLUX_PARTY);
        constantsList.add(XPathConstants.PURPOSE_CODE);
        constantsList.add(XPathConstants.REGISTRATION_VESSEL_COUNTRY);
        constantsList.add(XPathConstants.SPECIFIED_CONTACT_PARTY);
        constantsList.add(XPathConstants.SPECIFIED_CONTACT_PERSON);
        constantsList.add(XPathConstants.ROLE_CODE);
        constantsList.add(XPathConstants.ACCEPTANCE_DATE_TIME);
        constantsList.add(XPathConstants.RELATED_FLUX_REPORT_DOCUMENT);
        constantsList.add(XPathConstants.TYPE_CODE);
        constantsList.add(XPathConstants.RELATED_REPORT_ID);
        constantsList.add(XPathConstants.SPECIFIED_VESSEL_TRANSPORT_MEANS);
        constantsList.add(XPathConstants.RELATED_FISHING_ACTIVITY);
        constantsList.add(XPathConstants.SPECIFIED_DELIMITED_PERIOD);
        constantsList.add(XPathConstants.SPECIFIED_FISHING_TRIP);
        constantsList.add(XPathConstants.DURATION_MEASURE);
        constantsList.add(XPathConstants.OPERATIONS_QUANTITY);
        constantsList.add(XPathConstants.RELATED_FLUX_LOCATION);
        constantsList.add(XPathConstants.OCCURRENCE_DATE_TIME);
        constantsList.add(XPathConstants.SPECIFIED_FLUX_CHARACTERISTIC);
        constantsList.add(XPathConstants.REASON_CODE);
        constantsList.add(XPathConstants.FISHERY_TYPE_CODE);
        constantsList.add(XPathConstants.SPECIES_TARGET_CODE);
        constantsList.add(XPathConstants.VESSEL_RELATED_ACTIVITY_CODE);
        constantsList.add(XPathConstants.SPECIFIED_FA_CATCH);
        constantsList.add(XPathConstants.SPECIES_CODE);
        constantsList.add(XPathConstants.APPLIED_AAP_PROCESS);
        constantsList.add(XPathConstants.RESULT_AAP_PRODUCT);
        constantsList.add(XPathConstants.CLASS_CODE);
        constantsList.add(XPathConstants.SPECIFIED_SIZE_DISTRIBUTION);
        constantsList.add(XPathConstants.UNIT_QUANTITY);
        constantsList.add(XPathConstants.WEIGHT_MEASURE);
        constantsList.add(XPathConstants.PACKAGING_TYPE_CODE);
        constantsList.add(XPathConstants.PACKAGING_UNIT_QUANTITY);
        constantsList.add(XPathConstants.PACKAGING_UNIT_AVERAGE_WEIGHT_MEASURE);
        constantsList.add(XPathConstants.SPECIFIED_FISHING_GEAR);
        constantsList.add(XPathConstants.APPLICABLE_GEAR_CHARACTERISTIC);
        constantsList.add(XPathConstants.SPECIFIED_GEAR_PROBLEM);
        constantsList.add(XPathConstants.APPLICABLE_FLUX_CHARACTERISTIC);
        constantsList.add(XPathConstants.COUNTRY_ID);
        constantsList.add(XPathConstants.SPECIFIED_PHYSICAL_FLUX_GEOGRAPHICAL_COORDINATE);
        constantsList.add(XPathConstants.PHYSICAL_STRUCTURED_ADDRESS);
        constantsList.add(XPathConstants.POSTAL_STRUCTURED_ADDRESS);
        constantsList.add(XPathConstants.POSTAL_AREA);
        constantsList.add(XPathConstants.CITY_NAME);
        constantsList.add(XPathConstants.STREET_NAME);
        constantsList.add(XPathConstants.PLOT_IDENTIFICATION);
        constantsList.add(XPathConstants.SPECIFIED_STRUCTURED_ADDRESS);
        constantsList.add(XPathConstants.RELATED_VESSEL_TRANSPORT_MEANS);
        constantsList.add(XPathConstants.SPECIFIED_FLUX_LOCATION);
        constantsList.add(XPathConstants.DESTINATION_FLUX_LOCATION);
        constantsList.add(XPathConstants.CONVERSION_FACTOR_NUMERIC);
        constantsList.add(XPathConstants.CATEGORY_CODE);
        constantsList.add(XPathConstants.WEIGHING_MEANS_CODE);
        constantsList.add(XPathConstants.RELATED_FISHING_TRIP);
        constantsList.add(XPathConstants.VALUE_QUANTITY);
        constantsList.add(XPathConstants.RELATED_FISHING_GEAR);
        constantsList.add(XPathConstants.USED_FISHING_GEAR);
        constantsList.add(XPathConstants.FLUX_RESPONSE_DOCUMENT);
        constantsList.add(XPathConstants.FLUX_RESPONSE_MESSAGE);
        constantsList.add(XPathConstants.AVERAGE_WEIGHT_MEASURE);
        constantsList.add(XPathConstants.REGIONAL_FISHERIES_MANAGEMENT_ORGANIZATION_CODE);

        assertTrue(CollectionUtils.isNotEmpty(constantsList));

    }

    private void generateFactList() throws RulesValidationException {
        factList = new ArrayList<>();
        AbstractGenerator generator = new ActivityFaReportFactGenerator();
        generator.setBusinessObjectMessage(fluxMessage);
        factList.addAll(generator.generateAllFacts());
    }

    @SneakyThrows
    private FLUXFAReportMessage loadTestData() throws IOException, MdrModelMarshallException {
        String fluxFaMessageStr = IOUtils.toString(new FileInputStream(testXmlPath));
        return JAXBUtils.unMarshallMessage(fluxFaMessageStr, FLUXFAReportMessage.class);
    }

    public static String preetyPrint(Object obj) throws JsonProcessingException {
        return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true).writeValueAsString(obj);
    }

    private boolean validateXPath(String xpathVal, AutoPilot ap, VTDNav vn, AbstractFact fact) throws XPathParseException, NavException, XPathEvalException {

        System.out.println("Validating factClass : "+fact.getClass().getName());

        ap.selectXPath(xpathVal);
        final int evalResult = ap.evalXPath();
        if(evalResult != -1){
            printXMLFragment(ap, vn, evalResult);
            return true;
        } else {
            int lastIndexOfSlashSlashStar = xpathVal.lastIndexOf("//*");

            if(lastIndexOfSlashSlashStar <= 0){ // We already cut "a lot"
                System.out.println("\nFailed in the tentative to cut last appended strings... Probably this XPath doesn't exist!\nFaulty xpath : "+xpathVal);
                return false;
            }

            String newXpath = cutLastAppend(xpathVal, lastIndexOfSlashSlashStar);

            if(StringUtils.isEmpty(newXpath)){
                return false;
            }

            System.out.println("Retrying with altered XPATH (Cutting last append) : \nPrev xpath : \n" + xpathVal + "\nNew xpath : \n" + newXpath);
            return validateXPath(newXpath, ap, vn, fact);
        }
    }

    private String cutLastAppend(String xpathVal, int lastIndexOfSlashSlashStar) {
        String newXpath = xpathVal.substring(0, lastIndexOfSlashSlashStar);
        String testPath = xpathVal.substring(lastIndexOfSlashSlashStar, xpathVal.length());

        // If it is indexed append, cut the first '(' character.
        if(StringUtils.countMatches(testPath, "[") == 2){
            newXpath = newXpath.substring(1, newXpath.length());
        }

        return newXpath;
    }

    private void printXMLFragment(AutoPilot ap, VTDNav vn, int evalResult) throws XPathEvalException, NavException {
        int i = evalResult;
        while(i != -1){
            i = ap.evalXPath();
            int pos = (int) vn.getContentFragment();
            System.out.println("\nVal != null (Xpath exists) :  "+ vn.toString(pos, (pos >> 30)));
        }
    }

}
