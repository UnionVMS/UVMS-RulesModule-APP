/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleTestHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MDRCacheHolder;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Gregory Rinaldi
 */
public class AbstractFactTest {

    private AbstractFact fact = new FaArrivalFact();

    @Before
    public void before() {

        String[] gearTypeCodes = new String[] { "PS1", "LA", "SB", "SDN", "PTB" };
        String[] faCatchCodes = new String[] { "ONBOARD", "KEPT_IN_NET", "TAKEN_ONBOARD", "RELEASED", "DISCARDED", "DEMINIMIS", "UNLOADED" };
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.GEAR_TYPE, Arrays.asList(gearTypeCodes));
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.FA_CATCH_TYPE, Arrays.asList(faCatchCodes));
    }


    @Test
    public void testCheckDateNowHappy() {
        Date date = new DateTime(2005, 3, 26, 12, 0, 0, 0).toDate();
        assertTrue(date.before(fact.dateNow(1)));
    }

    @Test
    public void testListIdContainsAll() {
        List<CodeType> codeTypes = Arrays.asList(RuleTestHelper.getCodeType("val1", "AREA"), RuleTestHelper.getCodeType("val2", "AREA1"));
        assertTrue(fact.listIdContainsAll(codeTypes, "AREA"));
    }

    @Test
    public void testValidateIDTypeHappy() {
        IdType idType = new IdType();
        idType.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        IdType idType2 = new IdType();
        idType2.setSchemeId("53e36fab361-7338327c7d81");
        List<IdType> idTypes = Arrays.asList(idType, idType2);
        assertTrue(fact.schemeIdContainsAll(idTypes, "UUID"));
    }

    @Test
    public void testValidateIDType() {
        IdType idType = new IdType();
        idType.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        IdType idType2 = new IdType();
        idType2.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        List<IdType> idTypes = Arrays.asList(idType, idType2);
        assertTrue(fact.schemeIdContainsAll(idTypes, "UUID"));
    }

    @Test
    public void testContainsSchemeIdHappy() {
        IdType idType = new IdType();
        idType.setSchemeId("CFR");
        IdType idType2 = new IdType();
        idType2.setSchemeId("IRCS");
        IdType idType3 = new IdType();
        idType3.setSchemeId("EXT_MARK");
        List<IdType> idTypes = Arrays.asList(idType, idType2, idType3);
        boolean result = fact.schemeIdContainsAll(idTypes, "IRCS", "CFR");
        assertFalse(result);
    }

    @Test
    public void testContainsSchemeIdSad() {

        IdType idType = new IdType();
        idType.setSchemeId("CFR");
        IdType idType2 = new IdType();
        idType2.setSchemeId("IRCS");
        IdType idType3 = new IdType();
        idType3.setSchemeId("UUID");
        List<IdType> idTypes = Arrays.asList(idType, idType2, idType3);
        boolean result = fact.schemeIdContainsAll(idTypes, "UUID");
        assertFalse(result);
    }

    @Test
    public void testValidateFormatUUID_OK(){
        IdType uuidIdType = new IdType();
        uuidIdType.setSchemeId("UUID");
        uuidIdType.setValue(UUID.randomUUID().toString());
        List<IdType> idTypes = Arrays.asList(uuidIdType);
        boolean result = fact.validateFormat(idTypes);
        assertFalse(result);
    }

    @Test
    public void testValidateFormatUUID_NOT_OK(){
        IdType uuidIdType = new IdType();
        uuidIdType.setSchemeId("UUID");
        uuidIdType.setValue("ballshjshdhdfhsgfd");
        List<IdType> idTypes = Arrays.asList(uuidIdType);
        boolean result = fact.validateFormat(idTypes);
        assertTrue(result);
    }


    @Test
    public void testIsPresentInMDRList(){
        boolean result=fact.isPresentInMDRList("GEAR_TYPE","LA");
        assertEquals(true,result);
    }

    @Test
    public void testIsCodeTypePresentInMDRList(){

        List<CodeType> codeTypes = new ArrayList<>();
        codeTypes.add(new CodeType("RELEASED"));
        codeTypes.add(new CodeType("DISCARDED"));
        codeTypes.add(new CodeType("DEMINIMIS"));
        boolean result=fact.isCodeTypePresentInMDRList("FA_CATCH_TYPE",codeTypes);
        assertEquals(true,result);
    }

    @Test
    public void testIsIdTypePresentInMDRList(){

        List<IdType> codeTypes = new ArrayList<>();
        codeTypes.add(new IdType("RELEASED"));
        codeTypes.add(new IdType("DISCARDED"));
        codeTypes.add(new IdType("DEMINIMIS"));
        boolean result=fact.isIdTypePresentInMDRList("FA_CATCH_TYPE",codeTypes);
        assertEquals(true,result);
    }


    @Test
    public void testValueContainsAll() {

        IdType idType1= RuleTestHelper.getIdType("value1","CFR");
        IdType idType2= RuleTestHelper.getIdType("value12","IRCS");
        IdType idType3= RuleTestHelper.getIdType("value13","UUID");

        List<IdType> idTypes = Arrays.asList(idType1, idType2, idType3);
        boolean result = fact.valueContainsAll(idTypes, "value1");
        assertFalse(result);
    }

    @Test
    public void testIsNumeric() {

        NumericType numericType1= RuleTestHelper.getNumericType(new BigDecimal(12),"XXX");
        NumericType numericType2= RuleTestHelper.getNumericType(new BigDecimal(12),"XXX");
        NumericType numericType3= RuleTestHelper.getNumericType(new BigDecimal(12),"XXX");


        List<NumericType> numericTypes = Arrays.asList(numericType1, numericType2, numericType3);
        boolean result = fact.isNumeric(numericTypes);
        assertFalse(result);
    }

    @Test
    public void testIdListContainsValue() {

        IdType idType1= RuleTestHelper.getIdType("value1","CFR");
        IdType idType2= RuleTestHelper.getIdType("value12","IRCS");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        boolean result = fact.idListContainsValue(idTypes, "value1","CFR");
        assertTrue(result);
    }

    @Test
    public void testSchemeIdContainsAny() {

        IdType idType1= RuleTestHelper.getIdType("value1","CFR");
        IdType idType2= RuleTestHelper.getIdType("value12","IRCS");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        boolean result = fact.schemeIdContainsAny(idTypes, "CFR");
        assertFalse(result);
    }

    @Test
    public void testSchemeIdContainsAllOrNone() {

        IdType idType1= RuleTestHelper.getIdType("value1","CFR");
        IdType idType2= RuleTestHelper.getIdType("value12","IRCS");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        boolean result = fact.schemeIdContainsAllOrNone(idTypes, "CFR1");
        assertFalse(result);
    }


}
