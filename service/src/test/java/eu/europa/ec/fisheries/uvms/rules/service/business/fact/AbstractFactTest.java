/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MessageType;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityFaReportFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class AbstractFactTest {

    private AbstractFact fact = new FaArrivalFact();

    private ActivityObjectsHelper objectsHelper = new ActivityObjectsHelper();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private RulesDao rulesDao;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEmpty(){
        List list = null;
        assertTrue(fact.isEmpty(list));
        assertTrue(fact.isEmpty(""));
        assertTrue(fact.isEmpty(new ArrayList<>()));
    }

    @Test
    public void testListIdContainsAll() {
        List<CodeType> codeTypes = Arrays.asList(objectsHelper.generateCodeType("val1", "AREA"), objectsHelper.generateCodeType("val2", "AREA1"));
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
    public void testIsGreaterOrEquals(){

        NumericType numericType = new NumericType();
        numericType.setValue(new BigDecimal("1"));

        assertTrue(fact.isGreaterOrEquals(Collections.singletonList(numericType), 1));

        numericType = new NumericType();
        numericType.setValue(new BigDecimal("0.5"));

        assertFalse(fact.isGreaterOrEquals(Collections.singletonList(numericType), 1));

        numericType = new NumericType();
        numericType.setValue(new BigDecimal("2.5"));

        assertTrue(fact.isGreaterOrEquals(Collections.singletonList(numericType), 1));

        numericType = new NumericType();
        numericType.setValue(new BigDecimal("2.5"));

        NumericType numericType2 = new NumericType();
        numericType2.setValue(new BigDecimal("0.5"));

        assertFalse(fact.isGreaterOrEquals(Arrays.asList(numericType, numericType2), 1));

        numericType = new NumericType();
        numericType.setValue(new BigDecimal("2.5"));

        numericType2 = new NumericType();
        numericType2.setValue(new BigDecimal("1.5"));

        assertTrue(fact.isGreaterOrEquals(Arrays.asList(numericType, numericType2), 1));

    }
    @Test
    public void testValidDelimitedPeriod(){
        FishingActivityFact fishingActivityFact = new FishingActivityFact();
        FishingActivity related1 = new FishingActivity();
        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        delimitedPeriod.setStartDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));
        related1.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        fishingActivityFact.setRelatedFishingActivities(Collections.singletonList(related1));
        assertFalse(fishingActivityFact.validDelimitedPeriod(fishingActivityFact.getRelatedFishingActivities()));
    }

    @Test
    public void testValidDelimitedPeriod2() throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        FishingActivityFact fishingActivityFact = new FishingActivityFact();
        FishingActivity related1 = new FishingActivity();
        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        delimitedPeriod.setStartDateTime(new DateTimeType(date2, new DateTimeType.DateTimeString("ddfldf", "72829")));
        delimitedPeriod.setEndDateTime(new DateTimeType(date2, new DateTimeType.DateTimeString("ddfldf", "72829")));
        related1.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        fishingActivityFact.setRelatedFishingActivities(Collections.singletonList(related1));
        assertTrue(fishingActivityFact.validDelimitedPeriod(fishingActivityFact.getRelatedFishingActivities()));
    }

    @Test
    public void testValidDelimitedPeriod3(){
        FishingActivityFact fishingActivityFact = new FishingActivityFact();
        FishingActivity related1 = new FishingActivity();
        related1.setSpecifiedDelimitedPeriods(null);
        fishingActivityFact.setRelatedFishingActivities(Collections.singletonList(related1));
        assertFalse(fishingActivityFact.validDelimitedPeriod(fishingActivityFact.getRelatedFishingActivities()));
    }

    @Test
    public void testValidDelimitedPeriod4(){
        FishingActivityFact fishingActivityFact = new FishingActivityFact();
        FishingActivity related1 = new FishingActivity();
        related1.setSpecifiedDelimitedPeriods(null);
        related1.setOccurrenceDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));
        fishingActivityFact.setRelatedFishingActivities(Collections.singletonList(related1));
        assertTrue(fishingActivityFact.validDelimitedPeriod(fishingActivityFact.getRelatedFishingActivities()));
    }

    @Test
    public void testValidDelimitedPeriod5(){
        FishingActivityFact fishingActivityFact = new FishingActivityFact();
        FishingActivity related1 = new FishingActivity();
        related1.setSpecifiedDelimitedPeriods(null);
        related1.setOccurrenceDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));

        FishingActivity related2 = new FishingActivity();
        related2.setSpecifiedDelimitedPeriods(null);

        fishingActivityFact.setRelatedFishingActivities(Arrays.asList(related1, related2));
        assertFalse(fishingActivityFact.validDelimitedPeriod(fishingActivityFact.getRelatedFishingActivities()));
    }

    @Test
    public void testValidFormat() {
        assertTrue(fact.validateFormat("2000-123", AbstractFact.FORMATS.JFO.getFormatStr()));
        assertTrue(fact.validateFormat("1999-142", AbstractFact.FORMATS.JFO.getFormatStr()));
        assertTrue(fact.validateFormat("2018-115", AbstractFact.FORMATS.JFO.getFormatStr()));
        assertFalse(fact.validateFormat("208-115", AbstractFact.FORMATS.JFO.getFormatStr()));
        assertFalse(fact.validateFormat("2018-15", AbstractFact.FORMATS.JFO.getFormatStr()));
        assertFalse(fact.validateFormat("999-1154", AbstractFact.FORMATS.JFO.getFormatStr()));
        assertTrue(fact.validateFormat("33EKLELKLEGFHDJjsks1", AbstractFact.FORMATS.FLUXTL_ON.getFormatStr()));
        assertTrue(fact.validateFormat("P-446", AbstractFact.FORMATS.EXT_MARK.getFormatStr()));
        assertFalse(fact.validateFormat("a9a42a57-f372-4ca3-9277-9e5faa59f8cn", AbstractFact.FORMATS.UUID.getFormatStr()));
        assertTrue(fact.validateFormat("c56a4180-65aA-42ec-a945-5fd21dec0538", AbstractFact.FORMATS.UUID.getFormatStr()));
        assertFalse(fact.validateFormat("33EKLELKLE", AbstractFact.FORMATS.FLUXTL_ON.getFormatStr()));
        assertFalse(fact.validateFormat("2018-07-31T08:27:00.1234567Z", AbstractFact.FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr()));
    }

    @Test
    public void testDateShouldPass() {
        assertTrue(fact.validateFormat("2018-07-31T08:27:00.421Z", AbstractFact.FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr()));
        assertTrue(fact.validateFormat("2018-07-31T08:27:00.4Z", AbstractFact.FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr()));
        assertTrue(fact.validateFormat("2018-07-31T08:27:00.000Z", AbstractFact.FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr()));
        assertTrue(fact.validateFormat("2018-07-31T08:27:00.99Z", AbstractFact.FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr()));
        assertTrue(fact.validateFormat("2018-07-31T08:27:00.4213Z", AbstractFact.FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr()));
        assertTrue(fact.validateFormat("2018-07-31T08:27:00.123456Z", AbstractFact.FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr()));
        assertTrue(fact.validateFormat("2018-07-31T08:27:00Z", AbstractFact.FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr()));
    }

    @Test
    public void testIsPositiveIntegerValueWithNegative() {
        assertFalse(fact.isPositiveIntegerValue(new BigDecimal(-1)));
    }

    @Test
    public void testNumberOfDecimals(){
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("0.001")), equalTo(3));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("0.21")), equalTo(2));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("0.1")), equalTo(1));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("1.000")), equalTo(0));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("1.00")), equalTo(0));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("1.0")), equalTo(0));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("1")), equalTo(0));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("990")), equalTo(0));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("10.1")), equalTo(1));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("10.01")), equalTo(2));
        assertThat(fact.getNumberOfDecimalPlaces(new BigDecimal("10.001")), equalTo(3));
    }

    @Test
    public void testFailsForFloat() {
        assertFalse(fact.isPositiveIntegerValue(new BigDecimal(1.5)));
        assertFalse(fact.isPositiveIntegerValue(new BigDecimal("1.00")));
    }

    @Test
    public void testIsPositiveIntegerValueWithPositive() {
        assertTrue(fact.isPositiveIntegerValue(new BigDecimal(1)));
    }

    @Test
    public void testIsPositiveIntegerValueWithZero() {
        assertTrue(fact.isPositiveIntegerValue(new BigDecimal(0)));
    }

    @Test
    public void testIsStrictPositiveIntegerValueWithZero() {
        assertFalse(fact.isStrictPositiveInteger(new BigDecimal(0)));
    }

    @Test
    public void testIsStrictPositiveNumericWith1() {
        NumericType numericType = new NumericType();
        numericType.setValue(new BigDecimal("1"));
        assertTrue(fact.isStrictPositiveNumeric(numericType));
    }

    @Test
    public void testIsStrictPositiveNumericWithZero() {
        NumericType numericType = new NumericType();
        numericType.setValue(new BigDecimal("0"));
        assertFalse(fact.isStrictPositiveNumeric(numericType));
    }

    @Test
    public void testIsStrictPositiveNumericWithNegative() {
        NumericType numericType = new NumericType();
        numericType.setValue(new BigDecimal("-1"));
        assertFalse(fact.isStrictPositiveNumeric(numericType));
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
    public void testValidateDelimitedPeriodShouldReturnFalseWhenStartEndDatePresent() throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        DelimitedPeriod period = new DelimitedPeriod();
        period.setStartDateTime(new DateTimeType(date2, new DateTimeType.DateTimeString("ddfldf", "72829")));
        period.setEndDateTime(new DateTimeType(date2, new DateTimeType.DateTimeString("ddfldf", "72829")));

        assertFalse(!fact.validDelimitedPeriod(period, true, true));
    }

    @Test
    public void testValidateDelimitedPeriodWithEndDateShouldFail() {
        DelimitedPeriod period = new DelimitedPeriod();
        period.setEndDateTime(new DateTimeType(null, new DateTimeType.DateTimeString("ddfldf", "72829")));
        assertFalse(fact.validDelimitedPeriod(period, true, false));
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
    public void testNumberOfDecimalsHappy2() {
        assertEquals(4, fact.numberOfDecimals(new BigDecimal("10.3000")));
    }

    @Test
    public void testNumberOfDecimalsSad() {
        assertEquals(-1, fact.numberOfDecimals(null));
    }

    @Test
    public void testIsInRangeWith90ShouldPassTheRule() {
        assertFalse(!fact.isInRange(new BigDecimal("-90"), -90, 90));
    }

    @Test
    public void testIsInRangeWithNullShouldFailTheRule() {
        assertTrue(!fact.isInRange(null, -90, 90));
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
    public void testIsPositiveListOfMeasureWithZer() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("0"));
        assertTrue(fact.isPositive(measureType));
    }

    @Test
    public void testIsStrictPositiveListOfMeasureHappy() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("1292"));
        assertTrue(fact.isStrictPositive(measureType));
    }

    @Test
    public void testIsStrictPositiveListOfMeasureWithZero() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("0"));
        assertFalse(fact.isStrictPositive(measureType));
    }

    @Test
    public void testIsPositiveListOfMeasureWithZero() {
        MeasureType measureType = new MeasureType();
        measureType.setValue(new BigDecimal("0"));
        assertTrue(fact.isPositive(measureType));
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
        assertFalse(fact.checkAliasFromContactList(contactPeople, true));
    }

    @Test
    public void testCheckAliasFromContactListShouldReturnHappy() {
        List<ContactPerson> contactPeople = new ArrayList<>();

        ContactPerson contactPerson = new ContactPerson();
        contactPeople.add(contactPerson);

        assertTrue(fact.checkAliasFromContactList(contactPeople, true));
    }

    @Test
    public void testCheckAliasFromContactListShouldReturnTrueWithNullAliasAndFalseCheckAliasEmptyness() {
        ContactPerson contactPerson1 = new ContactPerson();
        contactPerson1.setFamilyName(ActivityObjectsHelper.generateTextType("FamilyName1", null, null));
        contactPerson1.setAlias(ActivityObjectsHelper.generateTextType("Alias1", null, null));
        ContactPerson contactPerson2 = new ContactPerson();
        contactPerson2.setGivenName(ActivityObjectsHelper.generateTextType("GivenName2", null, null));

        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(contactPerson1);
        contactPeople.add(contactPerson2);

        assertTrue(fact.checkAliasFromContactList(contactPeople, false));
    }

    @Test
    public void testCheckAliasFromContactListShouldReturnTrueWithEmptyAliasAndTrueCheckAliasEmptyness() {
        ContactPerson contactPerson1 = new ContactPerson();
        contactPerson1.setFamilyName(ActivityObjectsHelper.generateTextType("FamilyName1", null, null));
        contactPerson1.setAlias(ActivityObjectsHelper.generateTextType("Alias1", null, null));
        ContactPerson contactPerson2 = new ContactPerson();
        contactPerson2.setGivenName(ActivityObjectsHelper.generateTextType("GivenName2", null, null));
        contactPerson2.setAlias(ActivityObjectsHelper.generateTextType(StringUtils.EMPTY, null, null));

        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(contactPerson1);
        contactPeople.add(contactPerson2);

        assertTrue(fact.checkAliasFromContactList(contactPeople, true));
    }

    @Test
    public void testCheckAliasFromContactListShouldReturnFalseWithAllDataTrueCheckAliasEmptyness() {
        ContactPerson contactPerson1 = new ContactPerson();
        contactPerson1.setGivenName(ActivityObjectsHelper.generateTextType("GivenName1", null, null));
        contactPerson1.setFamilyName(ActivityObjectsHelper.generateTextType("FamilyName1", null, null));
        contactPerson1.setAlias(ActivityObjectsHelper.generateTextType("Alias1", null, null));
        ContactPerson contactPerson2 = new ContactPerson();
        contactPerson2.setGivenName(ActivityObjectsHelper.generateTextType("GivenName2", null, null));
        contactPerson2.setAlias(ActivityObjectsHelper.generateTextType("FamilyName2", null, null));
        contactPerson2.setAlias(ActivityObjectsHelper.generateTextType("Alias2", null, null));

        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(contactPerson1);
        contactPeople.add(contactPerson2);

        assertFalse(fact.checkAliasFromContactList(contactPeople, true));
    }

    @Test
    public void testCheckAliasFromContactListShouldReturnFalseWithAllDataFalseCheckAliasEmptyness() {
        ContactPerson contactPerson1 = new ContactPerson();
        contactPerson1.setGivenName(ActivityObjectsHelper.generateTextType("GivenName1", null, null));
        contactPerson1.setFamilyName(ActivityObjectsHelper.generateTextType("FamilyName1", null, null));
        contactPerson1.setAlias(ActivityObjectsHelper.generateTextType("Alias1", null, null));
        ContactPerson contactPerson2 = new ContactPerson();
        contactPerson2.setGivenName(ActivityObjectsHelper.generateTextType("GivenName2", null, null));
        contactPerson2.setAlias(ActivityObjectsHelper.generateTextType("FamilyName2", null, null));
        contactPerson2.setAlias(ActivityObjectsHelper.generateTextType("Alias2", null, null));

        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(contactPerson1);
        contactPeople.add(contactPerson2);

        assertFalse(fact.checkAliasFromContactList(contactPeople, false));
    }

    @Test
    public void testCheckContactListContainsAnyHappy() {
        List<ContactPerson> contactPeople = new ArrayList<>();

        ContactPerson contactPerson = new ContactPerson();
        TextType givenName = new TextType();
        givenName.setValue("");
        contactPerson.setGivenName(givenName);
        contactPeople.add(contactPerson);

        assertTrue(fact.checkContactListContainsAny(contactPeople, true, true));
    }


    @Test
    public void testIsPositiveShouldReturnFalseWithNegativeValue() {
        assertFalse(fact.isPositive(new BigDecimal("-10")));
    }

    @Test
    public void testIsPositiveShouldReturnTrueWithNull() {
        assertTrue(fact.isPositive((BigDecimal) null));
    }

    @Test
    public void testIsPositiveShouldReturnTrueWithPositiveValue() {
        assertTrue(fact.isPositive(new BigDecimal("10")));
    }

    @Test
    public void testValidateDelimitedPeriodShouldReturnFalseWhenNull() {
        assertFalse(fact.validDelimitedPeriod(null, true, false));
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
        CodeType typeCode = objectsHelper.generateCodeType("PS", "GEAR_TYPE");
        assertFalse(fact.listIdNotContains(typeCode, "GEAR_TYPE"));
    }

    @Test
    public void testListIdContainsAnyMultiple() {
        List<CodeType> typeCodes = Arrays.asList(objectsHelper.generateCodeType("PS", "GEAR_TYPE"), objectsHelper.generateCodeType("LT", "VESSEL_ACTIVITY"));

        assertFalse(fact.listIdNotContains(typeCodes, "GEAR_TYPE"));
    }

    @Test
    public void testValidateIDTypeHappy() {
        IdType idType = new IdType();
        idType.setSchemeId("UUID");
        List<IdType> idTypes = Arrays.asList(idType);
        assertFalse(fact.schemeIdContainsAll(idTypes, "UUID"));
    }

    @Test
    public void testValidateIDType() {
        IdType idType = new IdType();
        idType.setSchemeId("UUID");
        IdType idType2 = new IdType();
        idType2.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        List<IdType> idTypes = Arrays.asList(idType, idType2);
        assertFalse(fact.schemeIdContainsAll(idTypes, "UUID"));
    }

    @Test
    public void testValidateIDTypeCFR() {
        IdType idType = new IdType();
        idType.setSchemeId("CFR");
        IdType idType2 = new IdType();
        idType2.setSchemeId("ICCAT");
        List<IdType> idTypes = Arrays.asList(idType, idType2);
        assertFalse(fact.schemeIdContainsAll(idTypes, "CFR"));
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
    public void testContainsSchemeIdHappy2() {
        IdType idType = new IdType();
        idType.setSchemeId("CFR");
        IdType idType2 = new IdType();
        idType2.setSchemeId("IRCS");
        List<IdType> idTypes = Arrays.asList(idType, idType2);
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
        assertFalse(result);
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
    public void dateNotInPastWhenDateInFuture() {
        DateTime dt = new DateTime(2020, 3, 26, 12, 0, 0, 0);
        assertTrue(fact.dateNotInPast(dt.toDate()));
    }

    @Test
    public void dateNotInPastWhenDateInPast() {
        DateTime dt = new DateTime(2000, 3, 26, 12, 0, 0, 0);
        assertFalse(fact.dateNotInPast(dt.toDate()));
    }

    @Test
    public void testAcceptanceDateNotBeforeCreationDate() {
        DateTime acceptance = new DateTime(2000, 3, 26, 12, 5, 0);
        DateTime creation = new DateTime(2000, 3, 26, 12, 0, 0);

        assertTrue(fact.acceptanceDateNotBeforeCreationDate(creation.toDate(), acceptance.toDate(), -10));
    }

    @Test
    public void testAcceptanceDateNotBeforeCreationDate2() {
        DateTime acceptance = new DateTime(2017, 6, 12, 19, 0, 0);
        DateTime creation = new DateTime(2017, 6, 12, 19, 2, 0);

        assertTrue(fact.acceptanceDateNotBeforeCreationDate(creation.toDate(), acceptance.toDate(), -10));
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
    public void testIsNumeric() {
        NumericType numericType1 = ActivityObjectsHelper.generateNumericType(new BigDecimal(12), "XXX");
        NumericType numericType2 = ActivityObjectsHelper.generateNumericType(new BigDecimal(12), "XXX");
        NumericType numericType3 = ActivityObjectsHelper.generateNumericType(new BigDecimal(12), "XXX");
        List<NumericType> numericTypes = Arrays.asList(numericType1, numericType2, numericType3);
        boolean result = fact.isNumeric(numericTypes);
        assertTrue(result);
    }

    @Test
    public void testValidateFormatEmptyValue() {
        IdType id = new IdType();
        id.setSchemeId("UUID");
        boolean result = fact.validateFormat(id);
        assertTrue(result);
    }

    @Test
    public void testSchemeIdContainsAny() {

        IdType idType1 = ActivityObjectsHelper.generateIdType("value1", "CFR");
        IdType idType2 = ActivityObjectsHelper.generateIdType("value12", "IRCS");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        boolean result = fact.schemeIdContainsAny(idTypes, "CFR");
        assertFalse(result);
    }


    @Test
    public void testSchemeIdContainsAnyWith1EU_TRIP_IDShouldReturnFalse() {

        IdType idType1 = ActivityObjectsHelper.generateIdType("value1", "EU_TRIP_ID");
        IdType idType3 = ActivityObjectsHelper.generateIdType("value3", "EU_TRIP_ID");
        IdType idType2 = ActivityObjectsHelper.generateIdType("value12", "EU");

        List<IdType> idTypes = Arrays.asList(idType1, idType2, idType3);
        boolean result = fact.schemeIdContainsAny(idTypes, "EU_TRIP_ID");
        assertFalse(result);
    }

    @Test
    public void testSchemeIdContainsAnyWith0EU_TRIP_IDShouldReturnTrue() {

        IdType idType1 = ActivityObjectsHelper.generateIdType("value1", "EU_ID");
        IdType idType2 = ActivityObjectsHelper.generateIdType("value12", "EU");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        boolean result = fact.schemeIdContainsAny(idTypes, "EU_TRIP_ID");
        assertTrue(result);
    }


    @Test
    public void testSchemeIdContainsAllWithNull() {

        assertTrue(fact.schemeIdContainsAll(new ArrayList<IdType>(), null));
    }

    @Test
    public void testSchemeIdContainsAnyWithNull() {

        assertTrue(fact.schemeIdContainsAny(new ArrayList<IdType>(), null));
    }

    @Test
    public void testCheckContactListContainsAnyWithNull() {

        assertFalse(fact.checkContactListContainsAny(null, true, true));
    }

    @Test
    public void testListIdContainsAny() {

        CodeType codeType1 = objectsHelper.generateCodeType("value1", "CFR");
        CodeType codeType2 = objectsHelper.generateCodeType("value12", "IRCS");

        List<CodeType> codeTypes = Arrays.asList(codeType1, codeType2);
        boolean result = fact.listIdNotContains(codeTypes, "CFR");
        assertFalse(result);

        result = fact.listIdNotContains(codeTypes, null);
        assertTrue(result);

        result = fact.listIdNotContains(codeTypes, "ABC");
        assertTrue(result);
    }

    @Test
    public void testValueContainsAny() {

        CodeType codeType1 = objectsHelper.generateCodeType("value1", "CFR");
        CodeType codeType2 = objectsHelper.generateCodeType("value12", "IRCS");

        List<CodeType> codeTypes = Arrays.asList(codeType1, codeType2);
        boolean result = fact.valueContainsAny(codeTypes, "value1");
        assertFalse(result);

        result = fact.valueContainsAny(codeType1, "value1");
        assertFalse(result);
    }

    @Test
    public void testValueContainsAny2() {
        CodeType declaration = objectsHelper.generateCodeType("DECLARATION", null);
        assertFalse(fact.valueContainsAny(declaration, "DECLARATION", "AREA_ENTRY"));

    }

    @Test
    public void testAnyValueContainsAll() {

        CodeType codeType1 = objectsHelper.generateCodeType("value1", "CFR");
        CodeType codeType2 = objectsHelper.generateCodeType("value12", "IRCS");

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
    public void testListIdDoesNotContainAllWhenListIdDoesNotContainAllValuesAndValueIsNull() {
        CodeType codeType = getCodeTypeWithListID("alb");

        List<CodeType> codeTypeList = Arrays.asList(null, codeType, null);

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
        CodeType codeType1 = getCodeTypeWithValue("BUYER");
        CodeType codeType2 = getCodeTypeWithValue("SELLER");

        SalesPartyFact salesPartyType1 = new SalesPartyFact();
        salesPartyType1.setRoleCodes(Arrays.asList(codeType1));

        SalesPartyFact salesPartyType2 = new SalesPartyFact();
        salesPartyType2.setRoleCodes(Arrays.asList(codeType2));


        assertTrue(fact.salesPartiesValueDoesNotContainAny(Arrays.asList(salesPartyType1, salesPartyType2), "SENDER"));
    }

    @Test
    public void testAnyValueDoesNotContainAllWhenValueContainsAnyValue() {
        CodeType codeType1 = getCodeTypeWithValue("BUYER");
        CodeType codeType2 = getCodeTypeWithValue("SELLER");
        CodeType codeType3 = getCodeTypeWithValue("SENDER");

        SalesPartyFact salesPartyType1 = new SalesPartyFact();
        salesPartyType1.setRoleCodes(Arrays.asList(codeType1));

        SalesPartyFact salesPartyType2 = new SalesPartyFact();
        salesPartyType2.setRoleCodes(Arrays.asList(codeType2));

        SalesPartyFact salesPartyType3 = new SalesPartyFact();
        salesPartyType3.setRoleCodes(Arrays.asList(codeType3));

        assertFalse(fact.salesPartiesValueDoesNotContainAny(Arrays.asList(salesPartyType1, salesPartyType2, salesPartyType3), "SENDER"));
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenListSizeIs2AndShouldBeBetween1And1() throws Exception {
        List<String> list = Arrays.asList("", "");

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 1, 1);
        assertFalse(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenListSizeIs1AndShouldBeBetween1And1() throws Exception {
        List<String> list = Arrays.asList("");

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 1, 1);
        assertTrue(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenListSizeIs2AndShouldBeBetween1And5() throws Exception {
        List<String> list = Arrays.asList("", "");

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 1, 5);
        assertTrue(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenListIsEmpty() throws Exception {
        List<String> list = Collections.emptyList();

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 1, 5);
        assertTrue(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenMinNumberOfItemsIsBiggerThanMaxNumberOfItems() throws Exception {
        expectedException.expectMessage("minNumberOfItems '5' can't be bigger than '1'");
        expectedException.expect(IllegalArgumentException.class);

        List<String> list = Collections.emptyList();

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 5, 1);
        assertTrue(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListNotEmptyAndBetweenNumberOfItemsWhenListSizeIs2AndShouldBeBetween1And5() throws Exception {
        List<String> list = Arrays.asList("", "");

        boolean listNotEmptyAndBetweenNumberOfItems = fact.isListNotEmptyAndBetweenNumberOfItems(list, 1, 5);
        assertTrue(listNotEmptyAndBetweenNumberOfItems);
    }

    @Test
    public void testIsListNotEmptyAndBetweenNumberOfItemsWhenListSizeIs2AndShouldBeBetween3And5() throws Exception {
        List<String> list = Arrays.asList("", "");

        boolean listNotEmptyAndBetweenNumberOfItems = fact.isListNotEmptyAndBetweenNumberOfItems(list, 3, 5);
        assertFalse(listNotEmptyAndBetweenNumberOfItems);
    }

    @Test
    public void testIsListNotEmptyAndBetweenNumberOfItemsWhenListSizeIs1AndShouldBeBetween1And1() throws Exception {
        List<String> list = Arrays.asList("");

        boolean listNotEmptyAndBetweenNumberOfItems = fact.isListNotEmptyAndBetweenNumberOfItems(list, 1, 1);
        assertTrue(listNotEmptyAndBetweenNumberOfItems);
    }

    @Test
    public void testIsListNotEmptyAndBetweenNumberOfItemsWhenListIsNull() throws Exception {
        boolean listNotEmptyAndBetweenNumberOfItems = fact.isListNotEmptyAndBetweenNumberOfItems(null, 1, 1);
        assertFalse(listNotEmptyAndBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrAllValuesUniqueWhenListContainsUniqueValues() throws Exception {
        List<CodeType> uniqueValues = Arrays.asList(new CodeType("a"), new CodeType("b"), new CodeType("c"));

        boolean listIsUnique = fact.isListEmptyOrAllValuesUnique(uniqueValues);
        assertTrue(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrAllValuesUniqueWhenListIsNull() throws Exception {
        boolean listIsUnique = fact.isListEmptyOrAllValuesUnique(null);
        assertTrue(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrAllValuesUniqueWhenListContainsDuplicateValues() throws Exception {
        List<CodeType> uniqueValues = Arrays.asList(new CodeType("a"), new CodeType("a"), new CodeType("c"));

        boolean listIsUnique = fact.isListEmptyOrAllValuesUnique(uniqueValues);
        assertFalse(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrAllValuesUniqueWhenListIsEmpty() throws Exception {
        List<CodeType> uniqueValues = Collections.emptyList();

        boolean listIsUnique = fact.isListEmptyOrAllValuesUnique(uniqueValues);
        assertTrue(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrAllListIdsUniqueWhenListContainsUniqueValues() throws Exception {
        CodeType codeTypeA = new CodeType();
        codeTypeA.setListId("a");

        CodeType codeTypeB = new CodeType();
        codeTypeB.setListId("b");

        CodeType codeTypeC = new CodeType();
        codeTypeC.setListId("c");

        List<CodeType> uniqueValues = Arrays.asList(codeTypeA, codeTypeB, codeTypeC);

        boolean listIsUnique = fact.isListEmptyOrAllListIdsUnique(uniqueValues);
        assertTrue(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrAllListIdsUniqueWhenListContainsDuplicateValues() throws Exception {
        CodeType codeTypeA = new CodeType();
        codeTypeA.setListId("duplicate");

        CodeType codeTypeB = new CodeType();
        codeTypeB.setListId("duplicate");

        CodeType codeTypeC = new CodeType();
        codeTypeC.setListId("c");

        List<CodeType> uniqueValues = Arrays.asList(codeTypeA, codeTypeB, codeTypeC);

        boolean listIsUnique = fact.isListEmptyOrAllListIdsUnique(uniqueValues);
        assertTrue(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrAllListIdsUniqueWhenListIsEmpty() throws Exception {
        List<CodeType> uniqueValues = Collections.emptyList();

        boolean listIsUnique = fact.isListEmptyOrAllListIdsUnique(uniqueValues);
        assertTrue(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrValuesMatchPassedArgumentsWhenPassedArgumentsAreAllFound() throws Exception {
        List<CodeType> list = Arrays.asList(new CodeType("a"), new CodeType("a"), new CodeType("b"), new CodeType("c"), new CodeType("d"), new CodeType("e"));

        boolean listEmptyOrValuesMatchPassedArguments = fact.isListEmptyOrValuesMatchPassedArguments(list, "a", "b", "c", "d", "e");
        assertTrue(listEmptyOrValuesMatchPassedArguments);
    }

    @Test
    public void testIsListEmptyOrValuesMatchPassedArgumentsWhenNotAllPassedArgumentsAreFound() throws Exception {
        List<CodeType> list = Arrays.asList(new CodeType("a"), new CodeType("b"), new CodeType("c"), new CodeType("d"), new CodeType("e"));

        boolean listEmptyOrValuesMatchPassedArguments = fact.isListEmptyOrValuesMatchPassedArguments(list, "a", "b", "c", "d");
        assertFalse(listEmptyOrValuesMatchPassedArguments);
    }

    @Test
    public void isIdTypeValidFormatWhenInvalidSchemeID() {
        IdType idType = new IdType();
        idType.setValue("bla");
        idType.setSchemeId("bla");

        assertFalse(fact.isIdTypeValidFormat("bla", idType));
    }

    @Test
    public void isIdTypeValidFormatWhenValidSchemeID() {
        IdType idType = new IdType();
        idType.setValue("aaa123456789");
        idType.setSchemeId("CFR");

        assertTrue(fact.isIdTypeValidFormat("CFR", idType));
    }

    @Test
    public void isIdTypeValidFormatWhenValidSchemeIDAndInvalidValue() {
        IdType idType = new IdType();
        idType.setValue("bla");
        idType.setSchemeId("CFR");

        assertFalse(fact.isIdTypeValidFormat("CFR", idType));
    }

    @Test
    public void isCodeTypeValidFormatWhenProvidedListIDDiffersFromRequiredListId() {
        CodeType codeType = new CodeType();
        codeType.setListId("CFR");
        codeType.setValue("aaa123456789");

        assertFalse(fact.isCodeTypeValidFormat("somethingElse", codeType));
    }

    @Test
    public void isCodeTypeValidFormatWhenListIDIsInvalid() {
        CodeType codeType = new CodeType();
        codeType.setListId("notvalid");
        codeType.setValue("notvalid");

        assertFalse(fact.isCodeTypeValidFormat("invalid", codeType));
    }

    @Test
    public void isCodeTypeValidFormatWhenListIDIsValid() {
        CodeType codeType = new CodeType();
        codeType.setListId("CFR");
        codeType.setValue("aaa123456789");

        assertTrue(fact.isCodeTypeValidFormat("CFR", codeType));
    }

    @Test
    public void validateFormatForFLUXSalesType() {
        CodeType codeType = new CodeType("THIS_IS_SPARTA");
        codeType.setListId("FLUX_SALES_TYPE");

        assertFalse(fact.isCodeTypeValidFormat("FLUX_SALES_TYPE", codeType));
    }

    private CodeType getCodeTypeWithListID(String listId) {
        CodeType codeType = new CodeType();
        codeType.setListId(listId);
        return codeType;
    }

    private CodeType getCodeTypeWithValue(String value) {
        CodeType codeType = new CodeType();
        codeType.setValue(value);
        return codeType;
    }

    @Test
    public void testGetValueForListId() {
        CodeType typeCode = new CodeType();
        typeCode.setListId("VESSEL_STORAGE_TYPE");
        typeCode.setValue("OHL");
        List<CodeType> typeCodes = Collections.singletonList(typeCode);
        String valueForListId = fact.getValueForListId("VESSEL_STORAGE_TYPE", typeCodes);
        assertNotNull(valueForListId);
        assertEquals("OHL", valueForListId);
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
    public void testAnyFluxLocationTypeCodeContainsValueWithCorrectValue() {
        List<FLUXLocation> fluxLocations = objectsHelper.generateFluxLocationsWithPositionValue();
        assertTrue(fact.anyFluxLocationTypeCodeContainsValue(fluxLocations, "POSITION"));
    }

    @Test
    public void testAnyFluxLocationTypeCodeContainsValueWithWrongValue() {
        List<FLUXLocation> fluxLocations = objectsHelper.generateFluxLocationsWithPositionValue();
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
        List<FLUXLocation> fluxLocations = objectsHelper.generateFluxLocationsWithPositionValue();
        assertFalse(fact.anyFluxLocationTypeCodeContainsValue(fluxLocations, null));
    }

    @Test
    public void testValueNotContainsEmptyList() {
        List<CodeType> codeTypes = new ArrayList<>();
        assertTrue(fact.valueNotContains(codeTypes, "ZZZ", 1));
    }

    @Test
    public void testValueNotContainsHappy() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setValue("TYPECODE");
        codeTypes.add(codeType);

        assertFalse(fact.valueNotContains(codeTypes, "TYPECODE", 1));
    }

    @Test
    public void testValueNotContainsHappyWithMoreHits() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setValue("TYPECODE");
        codeTypes.add(codeType);

        CodeType codeType2 = new CodeType();
        codeType2.setValue("TYPECODE");
        codeTypes.add(codeType2);

        assertFalse(fact.valueNotContains(codeTypes, "TYPECODE", 2));
    }

    @Test
    public void testValueNotContainsHappyWithMoreHits2() {

        List<CodeType> codeTypes = new ArrayList<>();
        CodeType codeType = new CodeType();
        codeType.setValue("TYPECODE");
        codeTypes.add(codeType);

        assertTrue(fact.valueNotContains(codeTypes, "TYPECODE", 2));
    }

    @Test
    public void testIsGreaterThanZero() {
        MeasureType measureType = objectsHelper.generateMeasureType(new BigDecimal(1), "km");
        assertTrue(fact.isStrictPositive(measureType));
        MeasureType measureType2 = objectsHelper.generateMeasureType(new BigDecimal(0), "km");
        assertFalse(fact.isStrictPositive(measureType2));
        MeasureType measureType3 = objectsHelper.generateMeasureType(new BigDecimal(-1), "km");
        assertFalse(fact.isStrictPositive(measureType3));
    }

    @Test
    public void testContainsMoreThenOneDeclarationPerTrip() {
        List<IdType> specifiedFishingTripIds = new ArrayList<>();
        Map<String, List<FishingActivityWithIdentifiers>> faTypesPerTrip = new HashMap<>();
        FaReportDocumentFact repDocFact = new FaReportDocumentFact();
        boolean result1 = repDocFact.containsMoreThenOneDeclarationPerTrip(specifiedFishingTripIds, faTypesPerTrip, FishingActivityType.DEPARTURE);
        assertFalse(result1);

        specifiedFishingTripIds.add(new IdType("id123", "someScheme"));

        boolean result2 = repDocFact.containsMoreThenOneDeclarationPerTrip(specifiedFishingTripIds, faTypesPerTrip, FishingActivityType.DEPARTURE);
        assertFalse(result2);

        faTypesPerTrip.put("", null);

        boolean result3 = repDocFact.containsMoreThenOneDeclarationPerTrip(specifiedFishingTripIds, faTypesPerTrip, FishingActivityType.DEPARTURE);
        assertFalse(result3);

        List<FishingActivityWithIdentifiers> fishingActivityWithIdentifiers = new ArrayList<>();
        fishingActivityWithIdentifiers.add(new FishingActivityWithIdentifiers("", "", ""));
        faTypesPerTrip.clear();
        faTypesPerTrip.put("id123", fishingActivityWithIdentifiers);

        boolean result4 = repDocFact.containsMoreThenOneDeclarationPerTrip(specifiedFishingTripIds, faTypesPerTrip, FishingActivityType.DEPARTURE);
        assertTrue(result4);

        List<FishingActivityWithIdentifiers> id123 = faTypesPerTrip.get("id123");

        id123.add(new FishingActivityWithIdentifiers("", "", "DEPARTURE"));
        id123.add(new FishingActivityWithIdentifiers("", "", "DEPARTURE"));
        id123.add(new FishingActivityWithIdentifiers("", "", "DEPARTURE"));

        boolean result5 = repDocFact.containsMoreThenOneDeclarationPerTrip(specifiedFishingTripIds, faTypesPerTrip, FishingActivityType.DEPARTURE);
        assertTrue(result5);
    }

    @Test
    public void testValueCodeTypeContainsAny() {
        List<CodeType> codeTypes = new ArrayList<>();
        String[] valuesToMatch = new String[1];
        boolean result = fact.valueCodeTypeContainsAny(codeTypes, valuesToMatch);
        assertTrue(result);

        codeTypes.add(new CodeType("one"));
        valuesToMatch[0] = "two";
        boolean result2 = fact.valueCodeTypeContainsAny(codeTypes, valuesToMatch);
        assertTrue(result2);

        valuesToMatch[0] = "one";
        boolean result3 = fact.valueCodeTypeContainsAny(codeTypes, valuesToMatch);
        assertFalse(result3);
    }

    @Test
    public void testListContainsAtLeastOneFromTheOtherListEquals(){
        IdType idType2 = new IdType();
        idType2.setValue("abcd");
        List<IdType> list = Collections.singletonList(idType2);
        IdType idType3 = new IdType();
        idType3.setValue("ABCD");
        List<IdType> otherList = Collections.singletonList(idType3);
        assertTrue(fact.containsAny(list, otherList));
    }

    @Test
    public void testListContainsAtLeastOneFromTheOtherList() {
        List<IdType> controlList = new ArrayList<>();
        List<IdType> elementsToMatchList = new ArrayList<>();
        boolean result = fact.containsAny(controlList, elementsToMatchList);
        assertFalse(result);

        IdType idType = new IdType("123");
        idType.setSchemeId("ssss");
        controlList.add(idType);
        boolean result2 = fact.containsAny(controlList, elementsToMatchList);
        assertFalse(result2);

        IdType idType2 = new IdType("234");
        idType2.setSchemeId("ssss");
        controlList.add(idType);
        elementsToMatchList.add(idType2);

        boolean result3 = fact.containsAny(controlList, elementsToMatchList);
        assertFalse(result3);

        elementsToMatchList.add(new IdType("123"));
        boolean result4 = fact.containsAny(controlList, elementsToMatchList);
        assertTrue(result4);
    }

    @Test
    public void testValidateFormatCodeTypes() {
        CodeType typeCode = new CodeType();
        typeCode.setListId("UUID");
        typeCode.setValue("OTR");
        CodeType typeCode2 = new CodeType();
        typeCode2.setListId("UUID");
        typeCode2.setValue("NCC");
        List<CodeType> typeCodes = Arrays.asList(typeCode, typeCode2);
        boolean result = fact.validateFormatCodeTypes(typeCodes);
        assertTrue(result);
    }


    @Test
    public void testMatchWithFluxTL() {
        IdType idType = new IdType();
        idType.setValue("TEST");
        fact.setSenderOrReceiver("TEST");
        assertTrue(fact.matchWithFluxTL(Collections.singletonList(idType)));
    }

    @Test
    public void testMatchWithFluxTLWithEmptyList() {
        assertFalse(fact.matchWithFluxTL(new ArrayList<IdType>()));
    }

    @Test
    public void testMatchWithFluxTLWithEmptyNull() {
        assertFalse(fact.matchWithFluxTL(null));
    }

    @Test
    public void testMatchWithFluxTLWithSenderReceiverNull() {
        fact.setSenderOrReceiver(null);
        assertFalse(fact.matchWithFluxTL(new ArrayList<IdType>()));
    }

    @Test
    public void testIsBlankWhenIdTypeAndIdIsNull() {
        assertTrue(fact.isBlank((IdType) null));
    }

    @Test
    public void testIsBlankWhenIdTypeAndIdIsEmptyString() {
        assertTrue(fact.isBlank(new IdType("")));
    }

    @Test
    public void testIsBlankWhenIdTypeAndIdIsNotBlank() {
        assertFalse(fact.isBlank(new IdType("findHistoryOfAssetBy")));
    }

    @Test
    public void testIsBlankWhenTextTypeAndTextIsNull() {
        assertTrue(fact.isBlank((eu.europa.ec.fisheries.schema.sales.TextType) null));
    }

    @Test
    public void testIsBlankWhenTextTypeAndTextIsEmptyString() {
        assertTrue(fact.isBlank(new eu.europa.ec.fisheries.schema.sales.TextType().withValue("")));
    }

    @Test
    public void testIsBlankWhenTextTypeAndTextIsNotBlank() {
        assertFalse(fact.isBlank(new eu.europa.ec.fisheries.schema.sales.TextType().withValue("findHistoryOfAssetBy")));
    }

    @Test
    public void testGetValueForSchemeId() {
        List<IdType> idTypes = new ArrayList<>();
        IdType idType1 = ActivityObjectsHelper.generateIdType("FARM_VALUE", "FARM");
        IdType idType2 = ActivityObjectsHelper.generateIdType("ICCAT_VALUE", "ICCAT");
        idTypes.add(idType1);
        idTypes.add(idType2);

        assertEquals("FARM_VALUE", fact.getValueForSchemeId("FARM", idTypes));
    }

    @Test
    public void testIsAllSchemeIdsPresent() {
        IdType idType1 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-BA21CE845119", "UUID");
        IdType idType2 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-BA21CE845119", "UUID");
        IdType idType3 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-BA21CE845119", "UUID");

        assertFalse(fact.isAllSchemeIdsPresent(Arrays.asList(idType1, idType2, idType3)));
    }

    @Test
    public void testIsAllSchemeIdsPresentOneIsNull() {
        IdType idType1 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-BA21CE845119", "UUID");
        IdType idType2 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-BA21CE845119", null);
        IdType idType3 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-BA21CE845119", "UUID");

        assertTrue(fact.isAllSchemeIdsPresent(Arrays.asList(idType1, idType2, idType3)));
    }

    @Test
    public void testIsAllSchemeIdsPresentOneIsEmpty() {
        IdType idType1 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-Ba21CE845119", "UUID");
        IdType idType2 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-BA21CE845119", StringUtils.EMPTY);
        IdType idType3 = ActivityObjectsHelper.generateIdType("E75BB8B-C24D-4D9C-B1FD-BA21CE845119", "UUID");

        assertTrue(fact.isAllSchemeIdsPresent(Arrays.asList(idType1, idType2, idType3)));
    }

    @Test
    public void validateFormatIsoDateStringWithMillis() {
        String isoDateStringWithMillis = "2016-08-01T03:48:23.000Z";

        assertTrue(fact.isIsoDateStringValidFormat(isoDateStringWithMillis));
    }

    @Test
    public void validateFormatIsoDateStringWithMillis2() {
        String isoDateStringWithMillis = "22016-08-01T03:48:23.000Z";
        assertFalse(fact.isIsoDateStringValidFormat(isoDateStringWithMillis));
    }

    @Test
    public void validateFormatIsoDateStringNoMillis() {
        String isoDateStringNoMillis = "2016-08-01T03:48:23Z";

        assertTrue(fact.isIsoDateStringValidFormat(isoDateStringNoMillis));
    }

    @Test
    public void testMatchWithFluxTLCorrectValues() {
        IdType idType1 = ActivityObjectsHelper.generateIdType("ABC:DEF", "FLUX_GP_PARTY");
        IdType idType2 = ActivityObjectsHelper.generateIdType("SDF:GHJ", "FLUX_GP_PARTY");
        IdType idType3 = ActivityObjectsHelper.generateIdType("WER:XCV", "FLUX_GP_PARTY");

        List<IdType> idTypes = Arrays.asList(idType1, idType2, idType3);

        fact.setSenderOrReceiver("SDF");

        assertTrue(fact.matchWithFluxTL(idTypes));
    }
    @Test
    public void testMatchWithFluxTL_1() {
        IdType idType3 = ActivityObjectsHelper.generateIdType("SRC", "FLUX_GP_PARTY");
        List<IdType> idTypes = Collections.singletonList(idType3);
        fact.setSenderOrReceiver("SRC:FTF");
        assertTrue(fact.matchWithFluxTL(idTypes));
    }


    @Test
    public void testMatchWithFluxTLCorrectValueAndNullAndEmpty() {
        IdType idType1 = ActivityObjectsHelper.generateIdType(null, "FLUX_GP_PARTY");
        IdType idType2 = ActivityObjectsHelper.generateIdType(StringUtils.EMPTY, "FLUX_GP_PARTY");
        IdType idType3 = ActivityObjectsHelper.generateIdType("SDF:GHJ", "FLUX_GP_PARTY");

        List<IdType> idTypes = Arrays.asList(idType1, idType2, idType3);

        fact.setSenderOrReceiver("SDF");

        assertTrue(fact.matchWithFluxTL(idTypes));
    }

    @Test
    public void testMatchWithFluxTLWrongValues() {
        IdType idType1 = ActivityObjectsHelper.generateIdType("SDF;DEF", "FLUX_GP_PARTY");
        IdType idType2 = ActivityObjectsHelper.generateIdType("SDFGHJ", "FLUX_GP_PARTY");
        IdType idType3 = ActivityObjectsHelper.generateIdType("SDF-XCV", "FLUX_GP_PARTY");

        List<IdType> idTypes = Arrays.asList(idType1, idType2, idType3);

        fact.setSenderOrReceiver("SDF");

        assertFalse(fact.matchWithFluxTL(idTypes));
    }

    @Test
    public void testMatchWithFluxTLExceptPartiesCorrectValues() {
        IdType idType1 = ActivityObjectsHelper.generateIdType("XEU:DEF", "FLUX_GP_PARTY");
        IdType idType2 = ActivityObjectsHelper.generateIdType("XFA:GHJ", "FLUX_GP_PARTY");
        IdType idType3 = ActivityObjectsHelper.generateIdType("WER:XCV", "FLUX_GP_PARTY");

        List<IdType> idTypes = Arrays.asList(idType1, idType2, idType3);

        fact.setSenderOrReceiver("XES");

        assertFalse(fact.matchWithFluxTL(idTypes));
    }

    @Test
    public void testMatchWithFluxTLExceptPartiesWrongValues() {
        IdType idType1 = ActivityObjectsHelper.generateIdType("XEU:DEF", "FLUX_GP_PARTY");
        IdType idType2 = ActivityObjectsHelper.generateIdType("XFA:GHJ", "FLUX_GP_PARTY");
        IdType idType3 = ActivityObjectsHelper.generateIdType("WER:XCV", "FLUX_GP_PARTY");

        List<IdType> idTypes = Arrays.asList(idType1, idType2, idType3);

        fact.setSenderOrReceiver("XEU");

        assertTrue(fact.matchWithFluxTL(idTypes));
    }

    @Test
    public void testGetIdTypeValueArrayCorrectValue() {
        IdType idType = ActivityObjectsHelper.generateIdType("XEU:DEF:DEY", "FLUX_GP_PARTY");
        assertTrue(fact.split(idType.getValue(), ":").length == 3);
    }

    @Test
    public void testGetIdTypeValueArrayWrongSeparator() {
        IdType idType = ActivityObjectsHelper.generateIdType("XEU:DEF:DEY", "FLUX_GP_PARTY");
        assertFalse(fact.split(idType.getValue(), "'").length == 3);
    }

    @Test
    public void testCodeTypeValueContainsMatch() {
        boolean result = fact.codeTypeValueContainsMatch(Collections.singletonList(objectsHelper.generateCodeType("TEST", null)), "TEST");
        assertTrue(result);
    }

    @Test
    public void testValueStartsWithMultipleIdTypesNoneCorrect() {
        List<IdType> idTypes = Arrays.asList(ActivityObjectsHelper.generateIdType("27.3.b.27", "FAO_AREA"),
                ActivityObjectsHelper.generateIdType("28.3.d.27", "FAO_AREA"), ActivityObjectsHelper.generateIdType("27.3.bd.27", "FAO_AREA"));
        assertFalse(fact.valueStartsWith(idTypes, "27.3.d"));
    }

    @Test
    public void testValueStartsWithMultipleIdTypesAllCorrect() {
        List<IdType> idTypes = Arrays.asList(ActivityObjectsHelper.generateIdType("27.3.d.27", "FAO_AREA"),
                ActivityObjectsHelper.generateIdType("27.3.d.27.b", "FAO_AREA"), ActivityObjectsHelper.generateIdType("27.3.d", "FAO_AREA"));

        assertTrue(fact.valueStartsWith(idTypes, "27.3.d"));
    }

    @Test
    public void testValueStartsWithSingleIdTypeOneCorrect() {
        IdType idType = ActivityObjectsHelper.generateIdType("27.3.d.27", "FAO_AREA");
        assertTrue(fact.valueStartsWith(idType, "27.3.d"));
    }

    @Test
    public void testValueStartsWithSingleIdTypeNoneCorrect() {
        IdType idType = ActivityObjectsHelper.generateIdType("27.3.b.27", "FAO_AREA");
        assertFalse(fact.valueStartsWith(idType, "27.3.d"));
    }

    @Test
    @SneakyThrows
    public void testIsEmptyCollectionsReflective(){
        FLUXFAReportMessage message = JAXBMarshaller.unMarshallMessage(
                IOUtils.toString(new FileInputStream("src/test/resources/testData/faRepDocForEmptynessCheck.xml")), FLUXFAReportMessage.class);
        ActivityFaReportFactGenerator generator = new ActivityFaReportFactGenerator(MessageType.PUSH);
        generator.setBusinessObjectMessage(message);
        for (AbstractFact abstractFact : generator.generateAllFacts()) {
            if(abstractFact instanceof FaReportDocumentFact){
                FaReportDocumentFact repDoc = (FaReportDocumentFact) abstractFact;
                assertTrue(fact.isEmpty(repDoc.getIds()));
                assertTrue(fact.isEmpty(repDoc.getRelatedReportIDs()));
                assertTrue(fact.isEmpty(repDoc.getRelatedFLUXReportDocumentIDs()));
                assertFalse(fact.isEmpty(repDoc.getFaSpecifiedFishingTripIds()));
            } else if(abstractFact instanceof VesselTransportMeansFact){
                VesselTransportMeansFact vessFact = (VesselTransportMeansFact) abstractFact;
                assertTrue(fact.isEmpty(vessFact.getSpecifiedContactPersons()));
                assertFalse(fact.isEmpty(vessFact.getSpecifiedContactPartyRoleCodes()));
                assertFalse(fact.isEmpty(vessFact.getSpecifiedContactParties()));
                assertFalse(fact.isEmpty(vessFact.getSpecifiedContactPartyRoleCodes()));
            }
        }

        FAReportDocument doc = new FAReportDocument();
        assertTrue(fact.isEmpty(Collections.singleton(doc)));

        // Case a passed List has a Field which is a List which has only Objects which are empty.
        doc.getRelatedReportIDs().add(new IDType());
        assertTrue(fact.isEmpty(Collections.singleton(doc)));
    }

    @Test
    public void testMdrAcronymsMatchMDRAcronymTypeDeclared() {
        List<String> list = Arrays.asList("PROD_USAGE",
                "FA_FISHERY", "GFCM_GSA", "SALE_BR", "FA_REASON_DISCARD", "TARGET_SPECIES_GROUP", "FA_GEAR_PROBLEM", "FLUX_GP_MSG_ID", "FISH_SIZE_CATEGORY", "FA_BFT_SIZE_CATEGORY", "FLUX_FA_REPORT_TYPE",
                "FLUX_SALES_QUERY_PARAM_ROLE", "FLUX_LOCATION_TYPE", "GEAR_TYPE", "WEIGHT_MEANS", "VESSEL_ACTIVITY", "FA_QUERY_TYPE",
                "TERRITORY_CURR", "FLUX_GP_PURPOSE", "FA_VESSEL_ROLE", "FLUX_SALES_PARTY_ROLE", "MEMBER_STATE", "FA_BR", "FISHING_TRIP_TYPE", "CONVERSION_FACTOR",
                "FA_REASON_ENTRY", "FLUX_FA_TYPE", "FARM", "EFFORT_ZONE", "TERRITORY", "GENDER", "FISH_FRESHNESS", "FA_REASON_ARRIVAL",
                "FA_CHARACTERISTIC", "FA_CATCH_TYPE", "FAO_AREA", "FLUX_VESSEL_ID_TYPE", "FISH_PRESENTATION", "FLUX_UNIT", "FLUX_CONTACT_ROLE", "FISH_PRESERVATION", "FLUX_SALES_PARTY_ID_TYPE",
                "FLUX_SALES_TYPE", "STAT_RECTANGLE", "FLAP_ID_TYPE", "FA_QUERY_PARAMETER", "FA_BR_DEF", "FA_GEAR_RECOVERY", "FISH_PACKAGING", "VESSEL_STORAGE_TYPE",
                "FA_GEAR_CHARACTERISTIC", "SALE_BR_DEF", "FLUX_FA_FMC", "FAO_SPECIES", "GFCM_STAT_RECTANGLE", "FLUX_GP_RESPONSE",
                "FLUX_GP_VALIDATION_TYPE", "FLUX_LOCATION_CHARACTERISTIC", "LOCATION", "FA_GEAR_ROLE", "FLUX_GP_PARTY", "FA_BAIT_TYPE", "FA_REASON_DEPARTURE", "RFMO", "FLUX_GP_VALIDATION_LEVEL",
                "FLUX_SALES_QUERY_PARAM", "ICES_STAT_RECTANGLE", "FISH_SIZE_CLASS", "FLUX_PROCESS_TYPE");
        List<String> finalList = new ArrayList<>();
        for (String val : list) {
            try {
                MDRAcronymType.fromValue(val);
            } catch (IllegalArgumentException ex) {
                System.out.println("\n" + val + "(" + "\"" + val + "\"" + ")");
                finalList.add(val);
            }
        }
        assertTrue(CollectionUtils.isEmpty(finalList));
    }

    @Test
    public void testCodeTypeValueEquals(){
        CodeType codeType = new CodeType();
        codeType.setValue("EEEE");
        assertTrue(fact.codeTypeValueEquals(codeType, "EEEE"));
    }

    @Test
    public void testCodeTypeValueEqualsFail(){
        CodeType codeType = new CodeType();
        codeType.setValue("DDDD");
        assertFalse(fact.codeTypeValueEquals(codeType, "EEEE"));
    }
}
