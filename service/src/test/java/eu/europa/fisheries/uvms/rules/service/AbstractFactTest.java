/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleTestHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MDRCacheHolder;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactPerson;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

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
    public void testUnitCodeContainsAllWithEmptyList() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("200"));
        measureType.setUnitCode("K");
        assertTrue(fact.unitCodeContainsAll(Arrays.asList(measureType)));
    }

    @Test
    public void testUnitCodeContainsAllShouldReturnFalseWhenValueToMachSame() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("200"));
        measureType.setUnitCode("K");
        assertFalse(fact.unitCodeContainsAll(Arrays.asList(measureType),"K"));
    }

    @Test
    public void testUnitCodeContainsAllShouldReturnFalseWhenValuesToMachSame() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("200"));
        measureType.setUnitCode("K");

        MeasureType measureType2 = new MeasureType();
        measureType2.setValue(new BigDecimal("20"));
        measureType2.setUnitCode("KK");

        assertFalse(fact.unitCodeContainsAll(Arrays.asList(measureType, measureType2),"K", "KK"));
    }

    @Test
    public void testUnitCodeContainsAllShouldReturnTrueWhenValuesToMachNotMatchAll() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("200"));
        measureType.setUnitCode("K");

        MeasureType measureType2 = new MeasureType();
        measureType2.setValue(new BigDecimal("20"));
        measureType2.setUnitCode("KK");

        assertTrue(fact.unitCodeContainsAll(Arrays.asList(measureType, measureType2),"K", "KKKKK"));
    }


    @Test
    public void testValidateDelimitedPeriodShouldReturnFalseWhenStartEndDatePresent() {

        List<DelimitedPeriod> periods = new ArrayList<>();

        DelimitedPeriod period = new DelimitedPeriod();
        period.setStartDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));

        period.setEndDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));

        periods.add(period);

        assertFalse(fact.validateDelimitedPeriod(periods, true,true));
    }

    @Test
    public void testValidateDelimitedPeriodShouldReturnTrueWhenStartDateNotPresent() {

        List<DelimitedPeriod> periods = new ArrayList<>();

        DelimitedPeriod period = new DelimitedPeriod();

        period.setEndDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));

        periods.add(period);

        assertTrue(fact.validateDelimitedPeriod(periods, true,false));
    }

    @Test
    public void testValidateFluxLocationsForFaCatchShouldReturnFalseWithEmptyList() {
        List<FACatch> faCatchFacts = new ArrayList<>();
        assertFalse(fact.validateFluxLocationsForFaCatch(faCatchFacts));
    }

    @Test
    public void testValidateFluxLocationsForFaCatchShouldReturnTrueWithEmptySpecifiedFLUXLocationsList() {
        List<FACatch> faCatchFacts = new ArrayList<>();

        FACatch faCatch = new FACatch();
        faCatch.setSpecifiedFLUXLocations(new ArrayList<FLUXLocation>());

        faCatchFacts.add(faCatch);

        assertTrue(fact.validateFluxLocationsForFaCatch(faCatchFacts));
    }

    @Test
    public void testValidateFluxLocationsForFaCatchShouldReturnFalseWithNonEmptySpecifiedFLUXLocationsList() {
        List<FACatch> faCatchFacts = new ArrayList<>();

        FACatch faCatch = new FACatch();
        ArrayList<FLUXLocation> fluxLocations = new ArrayList<>();

        FLUXLocation fluxLocation = new FLUXLocation();
        fluxLocations.add(fluxLocation);

        faCatch.setSpecifiedFLUXLocations(fluxLocations);

        faCatchFacts.add(faCatch);

        assertFalse(fact.validateFluxLocationsForFaCatch(faCatchFacts));
    }


    @Test
    public void testAllValueContainsMatchShouldReturnTrueWithNonMatchingValue() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setValue("ddd");
        codeTypes.add(codeType);

        assertTrue(fact.allValueContainsMatch(codeTypes, "dd"));

    }


    @Test
    public void testAllValueContainsMatchShouldReturnTrueWithNotAllMatchingValue() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setValue("ddd");
        codeTypes.add(codeType);

        CodeType codeType2 = new CodeType();
        codeType2.setValue("dd");
        codeTypes.add(codeType2);

        assertTrue(fact.allValueContainsMatch(codeTypes, "dd"));

    }

    @Test
    public void testAllValueContainsMatchHappy() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setValue("dd");
        codeTypes.add(codeType);

        CodeType codeType2 = new CodeType();
        codeType2.setValue("dd");
        codeTypes.add(codeType2);

        assertFalse(fact.allValueContainsMatch(codeTypes, "dd"));

    }

    @Test
    public void testNumberOfDecimalsHappy() {
        assertEquals(4, fact.numberOfDecimals(new BigDecimal("10.3902")));
    }

    @Test
    public void testNumberOfDecimalsSad() {
        assertEquals(-1, fact.numberOfDecimals(null));
    }

    @Test
    public void testIsInRangeHappy() {
        assertFalse(fact.isInRange(new BigDecimal("-9"), -10, 200));
    }

    @Test
    public void testIsInRangeSad() {
        assertTrue(fact.isInRange(new BigDecimal("-10"), -10, 200));
    }

    @Test
    public void testIsInRangeNull() {
        assertTrue(fact.isInRange(null, -10, 200));
    }

    @Test
    public void testIsPositiveListOfMeasureShouldReturnFalseWithEmptyList() {
        List<MeasureType> measureTypes = new ArrayList<>();
        assertFalse(fact.isPositive(measureTypes));
    }

    @Test
    public void testIsPositiveListOfMeasureHappy() {
        List<MeasureType> measureTypes = new ArrayList<>();
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("1292"));
        measureTypes.add(measureType);
        assertTrue(fact.isPositive(measureTypes));
    }


    @Test
    public void testIsPositiveListOfMeasureShouldReturnFalseWithNegative() {
        List<MeasureType> measureTypes = new ArrayList<>();
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("-1292"));
        measureTypes.add(measureType);
        assertFalse(fact.isPositive(measureTypes));
    }

    @Test
    public void testCheckAliasFromContactListShouldReturnTrueWithEmptyList() {
        List<ContactPerson> contactPeople = new ArrayList<>();
        assertTrue(fact.checkAliasFromContactList(contactPeople, true));
    }

    @Test
    public void testCheckAliasFromContactListShouldReturnHappy() {
        List<ContactPerson> contactPeople = new ArrayList<>();

        ContactPerson contactPerson = new ContactPerson();
        contactPeople.add(contactPerson);

        assertTrue(fact.checkAliasFromContactList(contactPeople, true));
    }

    @Test
    public void testCheckContactListContainsAnyHappy() {
        List<ContactPerson> contactPeople = new ArrayList<>();

        ContactPerson contactPerson = new ContactPerson();
        contactPeople.add(contactPerson);

        assertTrue(fact.checkContactListContainsAny(contactPeople, true, true));
    }



    @Test
    public void testIsPositiveShouldReturnTrueWithNegativeValue() {
        assertTrue(fact.isPositive(new BigDecimal("-10")));
    }

    @Test
    public void testIsPositiveShouldReturnTrueWithNull() {
        assertTrue(fact.isPositive((BigDecimal) null));
    }

    @Test
    public void testIsPositiveShouldReturnFalseWithPositiveValue() {
        assertFalse(fact.isPositive(new BigDecimal("10")));
    }

    @Test
    public void testValidateDelimitedPeriodShouldReturnTrueWhenNull() {

        assertTrue(fact.validateDelimitedPeriod(null, true,false));
    }

    @Test
    public void testUnitCodeContainsAllHappy() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("200"));
        measureType.setUnitCode("K");
        assertFalse(fact.unitCodeContainsAll(Arrays.asList(measureType),"K"));
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


    @Test
    public void testListIdContainsAny() {

        CodeType codeType1= RuleTestHelper.getCodeType("value1","CFR");
        CodeType codeType2= RuleTestHelper.getCodeType("value12","IRCS");

        List<CodeType> codeTypes = Arrays.asList(codeType1, codeType2);
        boolean result = fact.listIdContainsAny(codeTypes, "CFR");
        assertFalse(result);
    }


    @Test
    public void testValueContainsAny() {

        CodeType codeType1= RuleTestHelper.getCodeType("value1","CFR");
        CodeType codeType2= RuleTestHelper.getCodeType("value12","IRCS");

        List<CodeType> codeTypes = Arrays.asList(codeType1, codeType2);
        boolean result = fact.valueContainsAny(codeTypes, "value1");
        assertFalse(result);
    }

    @Test
    public void testAnyValueContainsAll() {

        CodeType codeType1= RuleTestHelper.getCodeType("value1","CFR");
        CodeType codeType2= RuleTestHelper.getCodeType("value12","IRCS");

        List<CodeType> codeTypes = Arrays.asList(codeType1, codeType2);
        boolean result = fact.anyValueContainsAll(codeTypes, "value1");
        assertFalse(result);
    }


}
