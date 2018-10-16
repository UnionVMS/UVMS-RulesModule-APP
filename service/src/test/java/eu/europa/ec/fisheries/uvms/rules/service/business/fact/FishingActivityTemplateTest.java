/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.datatype.DatatypeConfigurationException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

public class FishingActivityTemplateTest {

    private ActivityFactMapper activityFactMapper = new ActivityFactMapper();
    private ActivityObjectsHelper objectsHelper = new ActivityObjectsHelper();

    @Test
    public void testFishingActivityValidDatesWithOccurrenceDateShouldPass(){
        FishingActivity fishingActivity = objectsHelper.generateActivity("31-08-1982 10:20:56", "DEPARTURE");
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertTrue(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateShouldFail(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertFalse(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndWithOccurrenceShouldPass(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity("31-08-1982 10:20:56", "DEPARTURE");
        fishingActivity.setRelatedFishingActivities(Collections.singletonList(related));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity,  false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertTrue(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndWithoutOccurrenceShouldFail(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity(null, "DEPARTURE");
        fishingActivity.setRelatedFishingActivities(Collections.singletonList(related));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity,  false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertFalse(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndWithoutOccurrenceWithEmptyDelimitedShouldFail(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity(null, "DEPARTURE");
        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        fishingActivity.setRelatedFishingActivities(Collections.singletonList(related));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertFalse(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndWithoutOccurrenceWithWrongDelimitedShouldFail() throws ParseException, DatatypeConfigurationException {
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity(null, "DEPARTURE");
        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod.setEndDateTime(dateTimeType);
        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        fishingActivity.setRelatedFishingActivities(Collections.singletonList(related));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertFalse(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndWithoutOccurrenceWithWrongEndDelimitedShouldFail() throws ParseException, DatatypeConfigurationException {
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity(null, "DEPARTURE");
        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod.setStartDateTime(dateTimeType);
        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        fishingActivity.setRelatedFishingActivities(Collections.singletonList(related));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity,  false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertFalse(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndWithoutOccurrenceWithDelimitedShouldPass() throws ParseException, DatatypeConfigurationException {
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity(null, "DEPARTURE");
        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod.setStartDateTime(dateTimeType);
        delimitedPeriod.setEndDateTime(dateTimeType);
        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        fishingActivity.setRelatedFishingActivities(Collections.singletonList(related));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertTrue(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndOnlyOneOccurrenceWithDelimitedShouldFail() throws ParseException, DatatypeConfigurationException {
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related2 = objectsHelper.generateActivity(null, "DEPARTURE");

        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod.setStartDateTime(dateTimeType);
        delimitedPeriod.setEndDateTime(dateTimeType);
        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));

        DelimitedPeriod delimitedPeriod2 = new DelimitedPeriod();
        DateTimeType dateTimeType2 = new DateTimeType();
        dateTimeType2.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod2.setStartDateTime(dateTimeType2);

        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        related2.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod2));

        fishingActivity.setRelatedFishingActivities(Arrays.asList(related, related2));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertFalse(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndOnlyOneOccurrenceWithDelimitedShouldFail2() throws ParseException, DatatypeConfigurationException {
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related2 = objectsHelper.generateActivity(null, "DEPARTURE");

        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod.setStartDateTime(dateTimeType);
        delimitedPeriod.setEndDateTime(dateTimeType);
        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));

        DelimitedPeriod delimitedPeriod2 = new DelimitedPeriod();
        DateTimeType dateTimeType2 = new DateTimeType();
        dateTimeType2.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod2.setStartDateTime(dateTimeType2);

        related.setSpecifiedDelimitedPeriods(null);
        related2.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod2));

        fishingActivity.setRelatedFishingActivities(Arrays.asList(related, related2));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertFalse(specifiedActivityFact.validDates());
    }

    @Test
    public void testFishingActivityValidDatesWithoutOccurrenceDateWithOneRelatedActivityAndOnly2OccurrenceWithDelimitedShouldPass() throws ParseException, DatatypeConfigurationException {
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity related2 = objectsHelper.generateActivity(null, "DEPARTURE");

        DelimitedPeriod delimitedPeriod = new DelimitedPeriod();
        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod.setStartDateTime(dateTimeType);
        delimitedPeriod.setEndDateTime(dateTimeType);
        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));

        DelimitedPeriod delimitedPeriod2 = new DelimitedPeriod();
        DateTimeType dateTimeType2 = new DateTimeType();
        dateTimeType2.setDateTime(objectsHelper.toXMLGregorianCalendar("31-08-1982 10:20:56"));
        delimitedPeriod2.setStartDateTime(dateTimeType2);
        delimitedPeriod2.setEndDateTime(dateTimeType2);

        related.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        related2.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod2));

        fishingActivity.setRelatedFishingActivities(Arrays.asList(related, related2));
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        specifiedActivityFact.setRelatedFishingActivities(fishingActivity.getRelatedFishingActivities());
        assertTrue(specifiedActivityFact.validDates());
    }

    @Test
    public void testRffmoProvidedShouldFail(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        List<FLUXLocation> fluxLocations = objectsHelper.generateFluxLocationsWithPositionValue();
        fluxLocations.get(0).setRegionalFisheriesManagementOrganizationCode(null);
        assertFalse(specifiedActivityFact.rfmoProvided(fluxLocations));
    }

    @Test
    public void testisAllowedToHaveSubactivitiesPass(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivityFact actFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        assertTrue(actFact.isAllowedToHaveSubactivities());
    }

    @Test
    public void testisAllowedToHaveSubactivitiesPass2(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "FISHING_OPERATION");
        FishingActivity subActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        subActivity.setTypeCode(new CodeType("type","scheme","","","","","","","",""));
        fishingActivity.getRelatedFishingActivities().add(subActivity);
        FishingActivityFact actFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        actFact.setFaReportDocumentTypeCode(new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType("DECLARATION","scheme"));
        assertTrue(actFact.isAllowedToHaveSubactivities());
    }

    @Test
    public void testisAllowedToHaveSubactivitiesFail(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivity subActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        subActivity.setTypeCode(new CodeType("type","scheme","","","","","","","",""));
        fishingActivity.getRelatedFishingActivities().add(subActivity);
        FishingActivityFact actFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        assertFalse(actFact.isAllowedToHaveSubactivities());
    }

    @Test
    public void testRffmoProvidedShouldPass(){
        FishingActivity fishingActivity = objectsHelper.generateActivity(null, "DEPARTURE");
        FishingActivityFact specifiedActivityFact = activityFactMapper.generateFishingActivityFact(fishingActivity, false, null, null);
        List<FLUXLocation> fluxLocations = objectsHelper.generateFluxLocationsWithPositionValue();
        CodeType codeType = new CodeType();
        codeType.setValue("code");
        fluxLocations.get(0).setRegionalFisheriesManagementOrganizationCode(codeType);
        assertTrue(specifiedActivityFact.rfmoProvided(fluxLocations));
    }

}
