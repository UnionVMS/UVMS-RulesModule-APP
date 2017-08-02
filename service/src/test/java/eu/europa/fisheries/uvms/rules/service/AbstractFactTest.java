/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.bean.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.*;
import org.apache.commons.lang3.*;
import org.joda.time.*;
import org.junit.*;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

import java.math.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Gregory Rinaldi
 */
public class AbstractFactTest {

    private AbstractFact fact = new FaArrivalFact();

    @Before
    public void before() {
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.GEAR_TYPE, RuleTestHelper.getObjectRepresentationForGEAR_TYPE_CODES());
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.FA_CATCH_TYPE, RuleTestHelper.getObjectRepresentationForFA_CATCH());
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.FA_GEAR_CHARACTERISTIC, RuleTestHelper.getObjectRepresentationForGEAR_CHARACTERISTIC());
        MDRCacheHolder.getInstance().addToCache(MDRAcronymType.VESSEL_STORAGE_TYPE, RuleTestHelper.getObjectRepresentationForVESSEL_STORAGE_CHARACTERISTIC());
    }


    @Test
    public void testCheckDateNowHappy() {
        Date date = new DateTime(2005, 3, 26, 12, 0, 0, 0).toDate();
        assertTrue(date.before(fact.dateNow(1)));
    }

    @Test
    public void testListIdContainsAll() {
        List<CodeType> codeTypes = Arrays.asList(RuleTestHelper.getCodeType("val1", "AREA"), RuleTestHelper.getCodeType("val2", "AREA1"));
        assertTrue(fact.listIdContainsAll(codeTypes, "AREA", "AREA1", "BLA"));
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
        assertFalse(fact.unitCodeContainsAll(Arrays.asList(measureType), "K"));
    }

    @Test
    public void testUnitCodeContainsAllShouldReturnFalseWhenValuesToMachSame() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("200"));
        measureType.setUnitCode("K");

        MeasureType measureType2 = new MeasureType();
        measureType2.setValue(new BigDecimal("20"));
        measureType2.setUnitCode("KK");

        assertFalse(fact.unitCodeContainsAll(Arrays.asList(measureType, measureType2), "K", "KK"));
    }

    @Test
    public void testUnitCodeContainsAllShouldReturnTrueWhenValuesToMachNotMatchAll() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("200"));
        measureType.setUnitCode("K");

        MeasureType measureType2 = new MeasureType();
        measureType2.setValue(new BigDecimal("20"));
        measureType2.setUnitCode("KK");

        assertTrue(fact.unitCodeContainsAll(Arrays.asList(measureType, measureType2), "K", "KKKKK"));
    }


    @Test
    public void testValidateDelimitedPeriodShouldReturnFalseWhenStartEndDatePresent() {

        List<DelimitedPeriod> periods = new ArrayList<>();

        DelimitedPeriod period = new DelimitedPeriod();
        period.setStartDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));

        period.setEndDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));

        periods.add(period);

        assertFalse(fact.validateDelimitedPeriod(periods, true, true));
    }

    @Test
    public void testValidateDelimitedPeriodShouldReturnTrueWhenStartDateNotPresent() {

        List<DelimitedPeriod> periods = new ArrayList<>();

        DelimitedPeriod period = new DelimitedPeriod();

        period.setEndDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));

        periods.add(period);

        assertTrue(fact.validateDelimitedPeriod(periods, true, false));
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

        assertTrue(fact.allValueContainsMatch(codeTypes, null));

    }

    @Test
    public void TestIsEmpty() {
        assertTrue(fact.isEmpty(new ArrayList<>()));
    }

    @Test
    public void testVesselIdsMatch() {
        List<IdType> vesselIds = null;
        IdType vesselCountryId = null;
        List<IdTypeWithFlagState> additionalObjectList = null;
        boolean result = fact.vesselIdsMatch(vesselIds, vesselCountryId, additionalObjectList);
        assertFalse(result);

        vesselIds = Arrays.asList(RuleTestHelper.getIdType("VSl1", "TESTVSL"));
        vesselCountryId = RuleTestHelper.getIdType("BEL", "TESTCOUNTRY");
        additionalObjectList = Arrays.asList(new IdTypeWithFlagState("TESTVSL", "VSl1", "BELGIUM"));
        ;
        result = fact.vesselIdsMatch(vesselIds, vesselCountryId, additionalObjectList);
        assertFalse(result);

        additionalObjectList = Arrays.asList(new IdTypeWithFlagState("TESTVSL", "VSl1", "BEL"));
        result = fact.vesselIdsMatch(vesselIds, vesselCountryId, additionalObjectList);
        assertTrue(result);
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

        assertTrue(fact.validateDelimitedPeriod(null, true, false));
    }

    @Test
    public void testUnitCodeContainsAllHappy() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("200"));
        measureType.setUnitCode("K");
        assertFalse(fact.unitCodeContainsAll(Arrays.asList(measureType), "K"));
    }

    @Test
    public void testListIdContainsAnySingle() {
        CodeType typeCode = RuleTestHelper.getCodeType("PS", "GEAR_TYPE");
        assertFalse(fact.listIdNotContains(typeCode, "GEAR_TYPE"));
    }

    @Test
    public void testListIdContainsAnyMultiple() {
        List<CodeType> typeCodes = Arrays.asList(RuleTestHelper.getCodeType("PS", "GEAR_TYPE"), RuleTestHelper.getCodeType("LT", "VESSEL_ACTIVITY"));

        assertFalse(fact.listIdNotContains(typeCodes, "GEAR_TYPE"));
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
    public void testValidateFormatUUID_OK() {
        IdType uuidIdType = new IdType();
        uuidIdType.setSchemeId("UUID");
        uuidIdType.setValue(UUID.randomUUID().toString());
        List<IdType> idTypes = Arrays.asList(uuidIdType);
        boolean result = fact.validateFormat(idTypes);
        assertFalse(result);
    }

    @Test
    public void testValidateFormatUUID_NOT_OK() {
        IdType uuidIdType = new IdType();
        uuidIdType.setSchemeId("UUID");
        uuidIdType.setValue("ballshjshdhdfhsgfd");
        List<IdType> idTypes = Arrays.asList(uuidIdType);
        boolean result = fact.validateFormat(idTypes);
        assertTrue(result);
    }

    @Test
    public void testValidateFormat_Exception() {
        IdType uuidIdType = new IdType();
        uuidIdType.setSchemeId("ABC");
        uuidIdType.setValue("ballshjshdhdfhsgfd");
        List<IdType> idTypes = Arrays.asList(uuidIdType);
        boolean result = fact.validateFormat(idTypes);
        assertFalse(result);
    }

    @Test
    public void testValidateFormat_IdNull() {
        List<IdType> ids = null;
        boolean result = fact.validateFormat(ids);
        assertTrue(result);

        IdType id = null;
        result = fact.validateFormat(id);
        assertTrue(result);
    }

    @Test
    public void testSchemeIdContainsAll() {
        IdType id = null;
        boolean result = fact.schemeIdContainsAll(id, "ABC");
        assertTrue(result);
    }

    @Test
    public void testListIdContainsAll_WithoutList() {
        CodeType codeType = null;
        boolean result = fact.listIdContainsAll(codeType, "ABC");
        assertTrue(result);
    }

    @Test
    public void testDateNow() {
        Date date = fact.dateNow(-10);
        assertNotNull(date);
    }


    @Test
    public void testIsPresentInMDRList() {
        boolean result = fact.isPresentInMDRList("GEAR_TYPE", "LA");
        assertEquals(true, result);
    }

    @Test
    public void testIsCodeTypePresentInMDRList() {

        List<CodeType> codeTypes = new ArrayList<>();
        codeTypes.add(new CodeType("RELEASED"));
        codeTypes.add(new CodeType("DISCARDED"));
        codeTypes.add(new CodeType("DEMINIMIS"));
        boolean result = fact.isCodeTypePresentInMDRList("FA_CATCH_TYPE", codeTypes);
        assertEquals(true, result);
    }

    @Test
    public void testIsPresentInMdrList() {
        boolean result = fact.isPresentInMDRList("TEST", "TEST");
        assertFalse(false);
    }

    @Test
    public void testGetSetUniqueId() {
        fact.setUniqueIds(Arrays.asList("TEST"));
        List<String> ids = fact.getUniqueIds();
        assertTrue(ids.contains("TEST"));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(fact.isEmpty(""));
    }

    @Test
    public void testIsIdTypePresentInMDRList() {

        List<IdType> codeTypes = new ArrayList<>();
        codeTypes.add(new IdType("RELEASED"));
        codeTypes.add(new IdType("DISCARDED"));
        codeTypes.add(new IdType("DEMINIMIS"));
        boolean result = fact.isIdTypePresentInMDRList("FA_CATCH_TYPE", codeTypes);
        assertEquals(true, result);

        result = fact.isIdTypePresentInMDRList(null, codeTypes);
        assertFalse(result);

        result = fact.isIdTypePresentInMDRList("FA_CATCH_TYPE", Collections.<IdType>emptyList());
        assertFalse(result);

        result = fact.isIdTypePresentInMDRList("FA_CATCH_TYPE", Arrays.asList(new IdType("BOARD")));
        assertFalse(result);
    }


    @Test
    public void testValueContainsAll() {

        IdType idType1 = RuleTestHelper.getIdType("value1", "CFR");
        IdType idType2 = RuleTestHelper.getIdType("value12", "IRCS");
        IdType idType3 = RuleTestHelper.getIdType("value13", "UUID");

        List<IdType> idTypes = Arrays.asList(idType1, idType2, idType3);
        boolean result = fact.valueContainsAll(idTypes, "value1");
        assertFalse(result);
    }

    @Test
    public void testIsNumeric() {

        NumericType numericType1 = RuleTestHelper.getNumericType(new BigDecimal(12), "XXX");
        NumericType numericType2 = RuleTestHelper.getNumericType(new BigDecimal(12), "XXX");
        NumericType numericType3 = RuleTestHelper.getNumericType(new BigDecimal(12), "XXX");


        List<NumericType> numericTypes = Arrays.asList(numericType1, numericType2, numericType3);
        boolean result = fact.isNumeric(numericTypes);
        assertFalse(result);
    }

    @Test
    public void testIdListContainsValue() {

        IdType idType1 = RuleTestHelper.getIdType("value1", "CFR");
        IdType idType2 = RuleTestHelper.getIdType("value12", "IRCS");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        boolean result = fact.idListContainsValue(idTypes, "value1", "CFR");
        assertTrue(result);
    }

    @Test
    public void testSchemeIdContainsAny() {

        IdType idType1 = RuleTestHelper.getIdType("value1", "CFR");
        IdType idType2 = RuleTestHelper.getIdType("value12", "IRCS");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        boolean result = fact.schemeIdContainsAny(idTypes, "CFR");
        assertFalse(result);
    }

    @Test
    public void testSchemeIdContainsAllOrNone() {

        IdType idType1 = RuleTestHelper.getIdType("value1", "CFR");
        IdType idType2 = RuleTestHelper.getIdType("value12", "IRCS");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        boolean result = fact.schemeIdContainsAllOrNone(idTypes, "CFR1");
        assertFalse(result);
    }

    @Test
    public void testSchemeIdContainsAllWithNull() {

        assertTrue(fact.schemeIdContainsAll(new ArrayList<IdType>(), null));
    }

    @Test
    public void testValueContainsAllWithNull() {

        assertTrue(fact.valueContainsAll(new ArrayList<IdType>(), null));
    }

    @Test
    public void testSchemeIdContainsAnyWithNull() {

        assertTrue(fact.schemeIdContainsAny(new ArrayList<IdType>(), null));
    }

    @Test
    public void testCheckContactListContainsAnyWithNull() {

        assertTrue(fact.checkContactListContainsAny(null, true, true));
    }

    @Test
    public void testListIdContainsAny() {

        CodeType codeType1 = RuleTestHelper.getCodeType("value1", "CFR");
        CodeType codeType2 = RuleTestHelper.getCodeType("value12", "IRCS");

        List<CodeType> codeTypes = Arrays.asList(codeType1, codeType2);
        boolean result = fact.listIdNotContains(codeTypes, "CFR");
        assertFalse(result);

        result = fact.listIdNotContains(codeTypes, null);
        assertTrue(result);

        CodeType newCodeType = RuleTestHelper.getCodeType("value1", "CFR");
        result = fact.listIdNotContains(codeTypes, "ABC");
        assertTrue(result);
    }


    @Test
    public void testValueContainsAny() {

        CodeType codeType1 = RuleTestHelper.getCodeType("value1", "CFR");
        CodeType codeType2 = RuleTestHelper.getCodeType("value12", "IRCS");

        List<CodeType> codeTypes = Arrays.asList(codeType1, codeType2);
        boolean result = fact.valueContainsAny(codeTypes, "value1");
        assertFalse(result);

        result = fact.valueContainsAny(codeType1, "value1");
        assertFalse(result);
    }

    @Test
    public void testAnyValueContainsAll() {

        CodeType codeType1 = RuleTestHelper.getCodeType("value1", "CFR");
        CodeType codeType2 = RuleTestHelper.getCodeType("value12", "IRCS");

        List<CodeType> codeTypes = Arrays.asList(codeType1, codeType2);
        boolean result = fact.anyValueContainsAll(codeTypes, "value1");
        assertFalse(result);
    }

    @Test
    public void testValidateFormatWhenPassingAStringAndResultIsOK() {
        boolean b = fact.validateFormat("aaa", "aaa");
        assertTrue(b);
    }

    @Test
    public void testValidateFormatWhenPassingAStringAndResultIsNOKBecauseArgumentIsNull() {
        boolean b = fact.validateFormat(null, null);
        assertFalse(b);
    }

    @Test
    public void testValidateFormatWhenPassingAStringAndResultIsNOKBecauseArgumentDoesNotApplyToTheFormat() {
        boolean b = fact.validateFormat("aap", "paa");
        assertFalse(b);
    }


    @Test
    public void testValidateFormatWhenSalesSpecificIDAndResultIsOK() {
        boolean b = fact.validateFormat("BEL-SN-2017-123456", ".*-.*-[A-Za-z0-9\\-]{1,20}");
        assertTrue(b);
    }

    @Test
    public void testListIdDoesNotContainAllWhenListIdDoesNotContainAllValues() {
        CodeType codeType1 = getCodeTypeWithListID("bla");
        CodeType codeType2 = getCodeTypeWithListID("alb");

        List<CodeType> codeTypeList = Arrays.asList(codeType1, codeType2);

        assertTrue(fact.listIdDoesNotContainAll(codeTypeList, "bla", "notbla"));
    }

    @Test
    public void testListIdDoesNotContainAllWhenListIdDoesContainAllValues() {
        CodeType codeType1 = getCodeTypeWithListID("bla");
        CodeType codeType2 = getCodeTypeWithListID("alb");

        List<CodeType> codeTypeList = Arrays.asList(codeType1, codeType2);

        assertFalse(fact.listIdDoesNotContainAll(codeTypeList, "bla", "alb"));
    }


    @Test
    public void testAnyValueDoesNotContainAllWhenValueDoesNotContainAnyValue() {
        eu.europa.ec.fisheries.schema.sales.CodeType codeType1 = getCodeTypeWithValue("BUYER");
        eu.europa.ec.fisheries.schema.sales.CodeType codeType2 = getCodeTypeWithValue("SELLER");

        SalesPartyType salesPartyType1 = new SalesPartyType();
        salesPartyType1.withRoleCodes(codeType1);

        SalesPartyType salesPartyType2 = new SalesPartyType();
        salesPartyType2.withRoleCodes(codeType2);


        assertTrue(fact.salesPartiesValueDoesNotContainAny(Arrays.asList(salesPartyType1, salesPartyType2), "SENDER"));
    }

    @Test
    public void testAnyValueDoesNotContainAllWhenValueContainsAnyValue() {
        eu.europa.ec.fisheries.schema.sales.CodeType codeType1 = getCodeTypeWithValue("BUYER");
        eu.europa.ec.fisheries.schema.sales.CodeType codeType2 = getCodeTypeWithValue("SELLER");
        eu.europa.ec.fisheries.schema.sales.CodeType codeType3 = getCodeTypeWithValue("SENDER");

        SalesPartyType salesPartyType1 = new SalesPartyType();
        salesPartyType1.withRoleCodes(codeType1);

        SalesPartyType salesPartyType2 = new SalesPartyType();
        salesPartyType2.withRoleCodes(codeType2);

        SalesPartyType salesPartyType3 = new SalesPartyType();
        salesPartyType3.withRoleCodes(codeType3);

        assertFalse(fact.salesPartiesValueDoesNotContainAny(Arrays.asList(salesPartyType1, salesPartyType2, salesPartyType3), "SENDER"));
    }

    @Test
    public void testValueIdTypeContainsAnyWhenValueIsPresent() {
        IdType idType1 = new IdType();
        idType1.setValue("value");
        IdType idType2 = new IdType();
        idType2.setValue("MASTER");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);

        assertFalse(fact.valueIdTypeContainsAny(idTypes, "MASTER", "AGENT", "OWNER", "OPERATOR"));
    }

    @Test
    public void testValueIdTypeContainsAnyWhenValueIsNotPresent() {
        IdType idType1 = new IdType();
        idType1.setValue("value");
        IdType idType2 = new IdType();
        idType2.setValue("eulav");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);

        assertTrue(fact.valueIdTypeContainsAny(idTypes, "MASTER", "AGENT", "OWNER", "OPERATOR"));
    }


    private CodeType getCodeTypeWithListID(String listId) {
        CodeType codeType = new CodeType();
        codeType.setListId(listId);
        return codeType;
    }

    private eu.europa.ec.fisheries.schema.sales.CodeType getCodeTypeWithValue(String value) {
        eu.europa.ec.fisheries.schema.sales.CodeType codeType = new eu.europa.ec.fisheries.schema.sales.CodeType();
        codeType.setValue(value);
        return codeType;
    }

    @Test
    public void testIsTypeCodeValuePresentInList() {
        CodeType typeCode = new CodeType();
        typeCode.setListId("VESSEL_STORAGE_TYPE");
        typeCode.setValue("OTR");
        CodeType typeCode2 = new CodeType();
        typeCode2.setListId("FAKE_LIST_ID");
        typeCode2.setValue("NCC");
        List<CodeType> typeCodes = Arrays.asList(typeCode, typeCode2);
        boolean typeCodeValuePresentInList = fact.isCodeTypePresentInMDRList("VESSEL_STORAGE_TYPE", typeCodes);
        assertEquals(true, typeCodeValuePresentInList);
    }

    @Test
    public void testGetValueForListId() {
        CodeType typeCode = new CodeType();
        typeCode.setListId("VESSEL_STORAGE_TYPE");
        typeCode.setValue("OHL");
        List<CodeType> typeCodes = Arrays.asList(typeCode);
        String valueForListId = fact.getValueForListId("VESSEL_STORAGE_TYPE", typeCodes);
        assertNotNull(valueForListId);
        assertEquals("OHL", valueForListId);
    }

    @Test
    public void testRetrieveFishingGearTypeCode() {
        FishingGearFact fishingGearFact = new FishingGearFact();
        CodeType typeCode = new CodeType();
        typeCode.setListId("GEAR_TYPE");
        typeCode.setValue("PS");

        FishingGearTypeCode fishingGearTypeCode = fishingGearFact.retrieveFishingGearTypeCode(typeCode);
        assertEquals(FishingGearTypeCode.PS, fishingGearTypeCode);
    }

    @Test
    public void testRetrieveFishingGearTypeCodeIsNull() {
        FishingGearFact fishingGearFact = new FishingGearFact();
        CodeType typeCode = new CodeType();
        typeCode.setListId("GEAR_TYPE");
        typeCode.setValue(StringUtils.EMPTY);

        FishingGearTypeCode fishingGearTypeCode = fishingGearFact.retrieveFishingGearTypeCode(typeCode);
        assertNull(fishingGearTypeCode);
    }

    @Test
    public void testGearCharacteristicCode() {
        FishingGearFact fishingGearFact = new FishingGearFact();
        CodeType typeCode = new CodeType();
        typeCode.setListId(FactConstants.GEAR_TYPE);
        typeCode.setValue("PS");
        GearCharacteristic gearCharacteristic = new GearCharacteristic();
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID(FactConstants.FA_GEAR_CHARACTERISTIC);
        codeType.setValue("ME");
        gearCharacteristic.setTypeCode(codeType);

        FishingGearCharacteristicCode fishingGearCharacteristicCode = fishingGearFact.retrieveGearCharacteristicCode(gearCharacteristic);
        assertEquals(FishingGearCharacteristicCode.ME, fishingGearCharacteristicCode);
    }

    @Test
    public void testGearCharacteristicCodeIsNull() {
        FishingGearFact fishingGearFact = new FishingGearFact();
        CodeType typeCode = new CodeType();
        typeCode.setListId(FactConstants.GEAR_TYPE);
        typeCode.setValue(StringUtils.EMPTY);
        GearCharacteristic gearCharacteristic = new GearCharacteristic();
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID(FactConstants.FA_GEAR_CHARACTERISTIC);
        codeType.setValue(StringUtils.EMPTY);

        FishingGearCharacteristicCode fishingGearCharacteristicCode = fishingGearFact.retrieveGearCharacteristicCode(gearCharacteristic);
        assertNull(fishingGearCharacteristicCode);
    }

    @Test
    public void testIsRequiredGearCharacteristicsPresent() {
        FishingGearFact fishingGearFact = new FishingGearFact();
        List<GearCharacteristic> gearCharacteristics = new ArrayList<>();
        GearCharacteristic gearCharacteristic = new GearCharacteristic();
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID(FactConstants.FA_GEAR_CHARACTERISTIC);
        codeType.setValue("ME");
        gearCharacteristic.setTypeCode(codeType);
        gearCharacteristics.add(gearCharacteristic);
        gearCharacteristic = new GearCharacteristic();
        codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID(FactConstants.FA_GEAR_CHARACTERISTIC);
        codeType.setValue("GM");
        gearCharacteristic.setTypeCode(codeType);
        gearCharacteristics.add(gearCharacteristic);
        gearCharacteristic = new GearCharacteristic();
        codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID(FactConstants.FA_GEAR_CHARACTERISTIC);
        codeType.setValue("GN");
        gearCharacteristic.setTypeCode(codeType);
        gearCharacteristics.add(gearCharacteristic);

        CodeType typeCode = new CodeType();
        typeCode.setListId(FactConstants.GEAR_TYPE);
        typeCode.setValue("TBB");

        fishingGearFact.setTypeCode(typeCode);
        fishingGearFact.setApplicableGearCharacteristics(gearCharacteristics);

        assertTrue(fishingGearFact.isRequiredGearCharacteristicsPresent(typeCode));
    }

    @Test
    public void testIsRequiredGearCharacteristicsPresentNoRequired() {
        FishingGearFact fishingGearFact = new FishingGearFact();
        List<GearCharacteristic> gearCharacteristics = new ArrayList<>();

        CodeType typeCode = new CodeType();
        typeCode.setListId(FactConstants.GEAR_TYPE);
        typeCode.setValue("RG");

        fishingGearFact.setTypeCode(typeCode);
        fishingGearFact.setApplicableGearCharacteristics(gearCharacteristics);

        assertTrue(fishingGearFact.isRequiredGearCharacteristicsPresent(typeCode));
    }

    @Test
    public void testGetDataTypeForMDRList() {

        String result = fact.getDataTypeForMDRList("FA_GEAR_CHARACTERISTIC", "ME");
        assertEquals("MEASURE", result);
    }


    @Test
    public void testCodeTypeValuesUniqueShouldReturnFalseWithNonUniqueValues() {

        CodeType codeType = new CodeType();
        codeType.setValue("value1");

        CodeType codeType2 = new CodeType();
        codeType2.setValue("value2");

        CodeType codeType3 = new CodeType();
        codeType3.setValue("value2");

        assertFalse(fact.codeTypeValuesUnique(Arrays.asList(codeType, codeType2, codeType3)));

    }

    @Test
    public void testCodeTypeValuesUniqueShouldReturnTrueWithUniqueValues() {

        CodeType codeType = new CodeType();
        codeType.setValue("value1");

        CodeType codeType2 = new CodeType();
        codeType2.setValue("value2");

        CodeType codeType3 = new CodeType();
        codeType3.setValue("value3");

        assertTrue(fact.codeTypeValuesUnique(Arrays.asList(codeType, codeType2, codeType3)));

    }

    @Test
    public void testCodeTypeValuesUniqueShouldReturnShouldReturnFalseWithNull() {
        assertFalse(fact.codeTypeValuesUnique(null));
    }

    @Test
    public void testListContainsEitherThen() {
        List<String> activityTypes = new ArrayList<String>() {{
            add("YEAH");
            add("NO");
            add("BLAH");
        }};

        final boolean contains = fact.listContainsEitherThen(activityTypes, "YEAH", "BLAH");
        assertTrue(contains);

        final boolean contains2 = fact.listContainsEitherThen(activityTypes, "NO", "BLAH", "YEAH");
        assertFalse(contains2);

        final boolean contains3 = fact.listContainsEitherThen(null, "NO", "BLAH", "YEAH");
        assertFalse(contains3);

        final boolean contains4 = fact.listContainsEitherThen(activityTypes);
        assertFalse(contains4);


    }

    @Test
    public void testDateComparison() {
        Date date1 = new GregorianCalendar(2017, Calendar.FEBRUARY, 10).getTime();
        Date date2 = new GregorianCalendar(2017, Calendar.FEBRUARY, 11).getTime();
        Date date3 = new GregorianCalendar(2017, Calendar.FEBRUARY, 11).getTime();
        Date date4 = new GregorianCalendar(2017, Calendar.FEBRUARY, 14).getTime();
        final boolean contains1 = fact.containsSameDayMoreTheOnce(Arrays.asList(date1, date2, date3, date4));
        System.out.println("List contains sameDate [true]: " + contains1);
        assertTrue(contains1);

        date1 = new GregorianCalendar(2017, Calendar.FEBRUARY, 10).getTime();
        date2 = new GregorianCalendar(2017, Calendar.FEBRUARY, 12).getTime();
        date3 = new GregorianCalendar(2017, Calendar.FEBRUARY, 11).getTime();
        date4 = new GregorianCalendar(2017, Calendar.FEBRUARY, 14).getTime();
        final boolean contains2 = fact.containsSameDayMoreTheOnce(Arrays.asList(date1, date2, date3, date4));
        System.out.println("List contains sameDate [false]: " + contains2);
        assertFalse(contains2);

    }

    @Test
    public void testAnyFluxLocationTypeCodeContainsValueWithCorrectValue() {
        List<FLUXLocation> fluxLocations = RuleTestHelper.createFluxLocationsWithPositionValue();

        assertTrue(fact.anyFluxLocationTypeCodeContainsValue(fluxLocations, "POSITION"));
    }

    @Test
    public void testAnyFluxLocationTypeCodeContainsValueWithWrongValue() {
        List<FLUXLocation> fluxLocations = RuleTestHelper.createFluxLocationsWithPositionValue();

        assertFalse(fact.anyFluxLocationTypeCodeContainsValue(fluxLocations, "ARG4376mn.l"));
    }

    @Test
    public void testAnyFluxLocationTypeCodeContainsValueWithEmptyList() {
        List<FLUXLocation> fluxLocations = new ArrayList<>();

        assertFalse(fact.anyFluxLocationTypeCodeContainsValue(fluxLocations, "POSITION"));
    }

    @Test
    public void testAnyFluxLocationTypeCodeContainsValueWithNullList() {
        assertFalse(fact.anyFluxLocationTypeCodeContainsValue(null, "POSITION"));
    }

    @Test
    public void testAnyFluxLocationTypeCodeContainsValueWithNullValue() {
        List<FLUXLocation> fluxLocations = RuleTestHelper.createFluxLocationsWithPositionValue();

        assertFalse(fact.anyFluxLocationTypeCodeContainsValue(fluxLocations, null));
    }

    @Test
    public void testListIdNotContainsEmptyList() {

        List<CodeType> codeTypes = new ArrayList<>();
        assertTrue(fact.listIdNotContains(codeTypes, "ZZZ", 1));
    }

    @Test
    public void testListIdNotContainsHappy() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setListId("TYPECODE");
        codeTypes.add(codeType);

        assertFalse(fact.listIdNotContains(codeTypes, "TYPECODE", 1));
    }

    @Test
    public void testListIdNotContainsHappyWithMoreHits() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setListId("TYPECODE");
        codeTypes.add(codeType);

        CodeType codeType2 = new CodeType();
        codeType2.setListId("TYPECODE");
        codeTypes.add(codeType2);

        assertFalse(fact.listIdNotContains(codeTypes, "TYPECODE", 2));
    }

    @Test
    public void testListIdNotContainsHappyWithMoreHits2() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setListId("TYPECODE");
        codeTypes.add(codeType);

        assertTrue(fact.listIdNotContains(codeTypes, "TYPECODE", 2));
    }

    @Test
    public void testIsGreaterThanZero() {
        List<MeasureType> measureTypeList = Arrays.asList(RuleTestHelper.getMeasureType(new BigDecimal(1), "km"));
        assertTrue(fact.isGreaterThanZero(measureTypeList));
    }


    @Test
    public void testGetDataTypeForMDRListNullCheck() {
        String result = fact.getDataTypeForMDRList("TEST", null);
        assertEquals("", result);
    }
}