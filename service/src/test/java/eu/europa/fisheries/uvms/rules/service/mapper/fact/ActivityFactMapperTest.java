/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service.mapper.fact;

import static org.junit.Assert.assertEquals;

import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaDepartureFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaEntryToSeaFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaExitFromSeaFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaFishingOperationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaJointFishingOperationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaNotificationOfArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaQueryFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingActivityFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingTripFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.GearCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselCountry;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.QuantityType;

/**
 * @author Gregory Rinaldi
 */
public class ActivityFactMapperTest {

    private IDType idType;
    private CodeType codeType;
    private DelimitedPeriod delimitedPeriod;
    private QuantityType quantityType;
    private DateTimeType dateTimeType;
    private Date date;
    private FLUXLocation fluxLocation;
    private FACatch faCatch;
    private FishingTrip fishingTrip;
    private FishingGear fishingGear;

    @Before
    @SneakyThrows
    public void before() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        date = sdf.parse("31-08-1982 10:20:56");

        idType = new IDType();
        idType.setValue("value");
        idType.setSchemeID("schemeId");

        codeType = new CodeType();
        codeType.setValue("value");

        quantityType = new QuantityType();
        quantityType.setUnitCode("unitCode");
        quantityType.setValue(new BigDecimal(10));

        delimitedPeriod = new DelimitedPeriod();
        MeasureType durationMeasure = new MeasureType();
        durationMeasure.setUnitCode("unitCode");
        durationMeasure.setValue(new BigDecimal(10));
        delimitedPeriod.setDurationMeasure(durationMeasure);

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);

        dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        delimitedPeriod.setStartDateTime(dateTimeType);
        delimitedPeriod.setEndDateTime(dateTimeType);

        fluxLocation = new FLUXLocation();
        fluxLocation.setID(idType);

        fishingTrip = new FishingTrip();
        fishingTrip.setIDS(Collections.singletonList(idType));

        faCatch = new FACatch();
        faCatch.setTypeCode(codeType);

        fishingGear = new FishingGear();
        fishingGear.setTypeCode(codeType);
        fishingGear.setRoleCodes(Collections.singletonList(codeType));
    }

    @Test
    public void testGenerateFactForVesselTransportMean(){

        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        vesselTransportMeans.setRoleCode(codeType);

        vesselTransportMeans.setIDS(Collections.singletonList(idType));

        VesselCountry vesselCountry = new VesselCountry();
        vesselCountry.setID(idType);
        vesselTransportMeans.setRegistrationVesselCountry(vesselCountry);

        ContactParty contactParty = new ContactParty();
        contactParty.setRoleCodes(Collections.singletonList(codeType));
        contactParty.setIDS(Collections.singletonList(idType));
        vesselTransportMeans.setSpecifiedContactParties(Collections.singletonList(contactParty));

        VesselTransportMeansFact mappedFact = ActivityFactMapper.INSTANCE.generateFactForVesselTransportMean(vesselTransportMeans);

        assertEquals(codeType.getValue(), mappedFact.getRoleCode().getValue());

        assertEquals(idType.getValue(), mappedFact.getRegistrationVesselCountryId().getValue());
        assertEquals(idType.getSchemeID(), mappedFact.getRegistrationVesselCountryId().getSchemeId());

        assertEquals(idType.getValue(), mappedFact.getSpecifiedContactParties().get(0).getIDS().get(0).getValue());
        assertEquals(idType.getSchemeID(), mappedFact.getSpecifiedContactParties().get(0).getIDS().get(0).getSchemeID());

        assertEquals(idType.getValue(), mappedFact.getIds().get(0).getValue());
        assertEquals(idType.getSchemeID(), mappedFact.getIds().get(0).getSchemeId());

        assertEquals(codeType.getValue(), mappedFact.getSpecifiedContactPartyRoleCodes().get(0).getValue());

    }

    @Test
    public void testGenerateFactForFishingActivity() {

        FishingActivity fishingActivity = new FishingActivity();

        fishingActivity.setReasonCode(codeType);
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setSpecifiedDelimitedPeriods(Collections.singletonList(delimitedPeriod));
        fishingActivity.setFisheryTypeCode(codeType);
        fishingActivity.setOperationsQuantity(quantityType);
        fishingActivity.setOccurrenceDateTime(dateTimeType);
        fishingActivity.setSpeciesTargetCode(codeType);
        fishingActivity.setRelatedFishingActivities(Collections.singletonList(fishingActivity));
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));
        fishingActivity.setSpecifiedFishingTrip(fishingTrip);

        List<FishingActivityFact> fishingActivityFacts = ActivityFactMapper.INSTANCE.generateFactForFishingActivities(Collections.singletonList(fishingActivity));

        assertEquals(codeType.getValue(), fishingActivityFacts.get(0).getReasonCode().getValue());
        assertEquals(codeType.getValue(), fishingActivityFacts.get(0).getTypeCode().getValue());
        assertEquals(delimitedPeriod.getDurationMeasure(), fishingActivityFacts.get(0).getDelimitedPeriods().get(0).getDurationMeasure());
        assertEquals(delimitedPeriod.getEndDateTime(), fishingActivityFacts.get(0).getDelimitedPeriods().get(0).getEndDateTime());
        assertEquals(delimitedPeriod.getStartDateTime(), fishingActivityFacts.get(0).getDelimitedPeriods().get(0).getStartDateTime());
        assertEquals(codeType.getValue(), fishingActivityFacts.get(0).getFisheryTypeCode().getValue());
        assertEquals(quantityType.getValue().toString(), fishingActivityFacts.get(0).getOperationQuantity());
        assertEquals(date, fishingActivityFacts.get(0).getOccurrenceDateTime());
        assertEquals(codeType.getValue(), fishingActivityFacts.get(0).getSpeciesTargetCode().getValue());
        assertEquals(fishingActivity, fishingActivityFacts.get(0).getRelatedFishingActivities().get(0));
        assertEquals(fluxLocation, fishingActivityFacts.get(0).getRelatedFLUXLocations().get(0));
        assertEquals(fishingTrip, fishingActivityFacts.get(0).getSpecifiedFishingTrip());

    }

    @Test
    public void testGenerateFactForGearCharacteristics() {

        GearCharacteristic gearCharacteristic = new GearCharacteristic();
        gearCharacteristic.setTypeCode(codeType);

        List<GearCharacteristicsFact> gearCharacteristicsFacts = ActivityFactMapper.INSTANCE.generateFactsForGearCharacteristics(Collections.singletonList(gearCharacteristic));

        assertEquals(codeType.getValue(), gearCharacteristicsFacts.get(0).getTypeCode().getValue());

    }

    @Test
    public void testGenerateFactForFishingTrip() {

        FishingTrip fishingTrip = new FishingTrip();
        fishingTrip.setIDS(Collections.singletonList(idType));
        fishingTrip.setTypeCode(codeType);

        List<FishingTripFact> fishingTripFacts = ActivityFactMapper.INSTANCE.generateFactForFishingTrips(Collections.singletonList(fishingTrip));

        assertEquals(idType.getValue(), fishingTripFacts.get(0).getIds().get(0).getValue());
        assertEquals(idType.getSchemeID(), fishingTripFacts.get(0).getIds().get(0).getSchemeId());
        assertEquals(codeType.getValue(), fishingTripFacts.get(0).getTypeCode().getValue());

    }

    @Test
    public void testGenerateFactsForFaDeparture() {

        FishingActivity fishingActivity = new FishingActivity();
        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        fishingActivity.setReasonCode(codeType);
        fishingActivity.setOccurrenceDateTime(dateTimeType);
        fishingActivity.setSpecifiedFACatches(Collections.singletonList(faCatch));
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));
        fishingActivity.setSpecifiedFishingTrip(fishingTrip);
        fishingActivity.setSpecifiedFishingGears(Collections.singletonList(fishingGear));
        fishingActivity.setTypeCode(codeType);

        FaDepartureFact faDepartureFact = ActivityFactMapper.INSTANCE.generateFactsForFaDeparture(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faDepartureFact.getFishingActivityTypeCode().getValue());

        assertEquals(codeType.getValue(), faDepartureFact.getReasonCode().getValue());
        assertEquals(date, faDepartureFact.getOccurrenceDateTime());
        assertEquals(fluxLocation, faDepartureFact.getRelatedFLUXLocations().get(0));
        assertEquals(fishingTrip, faDepartureFact.getSpecifiedFishingTrip());
        assertEquals(faCatch, faDepartureFact.getSpecifiedFACatches().get(0));
        assertEquals(codeType.getValue(), faDepartureFact.getSpecifiedFishingGears().get(0).getRoleCodes().get(0).getValue());
        assertEquals(codeType.getValue(), faDepartureFact.getSpecifiedFishingGears().get(0).getTypeCode().getValue());
        assertEquals(codeType.getValue(), faDepartureFact.getFaReportDocumentTypeCode().getValue());

    }

    @Test
    public void testGenerateFactsForFaEntryToSeaFact() {

        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setReasonCode(codeType);
        fishingActivity.setSpeciesTargetCode(codeType);
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setSpecifiedFACatches(Collections.singletonList(faCatch));
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));

        FaEntryToSeaFact faEntryToSeaFact = ActivityFactMapper.INSTANCE.generateFactsForEntryIntoSea(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faEntryToSeaFact.getReasonCode().getValue());
        assertEquals(codeType.getValue(), faEntryToSeaFact.getSpeciesTargetCode().getValue());
        assertEquals(codeType.getValue(), faEntryToSeaFact.getFishingActivityTypeCode().getValue());
        assertEquals(codeType.getValue(), faEntryToSeaFact.getFaReportDocumentTypeCode().getValue());
        assertEquals(codeType.getValue(), faEntryToSeaFact.getSpeciesTargetCode().getValue());
        assertEquals(fluxLocation, faEntryToSeaFact.getRelatedFLUXLocations().get(0));

    }

    @Test
    public void testGenerateFactsForFaFishingOperationFact() {

        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));
        fishingActivity.setVesselRelatedActivityCode(codeType);
        fishingActivity.setOperationsQuantity(quantityType);

        FaFishingOperationFact faFishingOperationFact = ActivityFactMapper.INSTANCE.generateFactsForFishingOperation(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faFishingOperationFact.getFaReportDocumentTypeCode().getValue());
        assertEquals(codeType.getValue(), faFishingOperationFact.getFishingActivityTypeCode().getValue());
        assertEquals(fluxLocation, faFishingOperationFact.getRelatedFLUXLocations().get(0));
        assertEquals(quantityType.getValue().toString(), faFishingOperationFact.getOperationsQuantity());
        assertEquals(codeType.getValue(), faFishingOperationFact.getVesselRelatedActivityCode().getValue());

    }

    @Test
    public void testGenerateFactsForFaJointFishingOperationFact() {

        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));

        FaJointFishingOperationFact faJointFishingOperationFact = ActivityFactMapper.INSTANCE.generateFactsForJointFishingOperation(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faJointFishingOperationFact.getFaReportDocumentTypeCode().getValue());
        assertEquals(codeType.getValue(), faJointFishingOperationFact.getFishingActivityTypeCode().getValue());
        assertEquals(fluxLocation, faJointFishingOperationFact.getRelatedFLUXLocations().get(0));

    }

    @Test
    public void testGenerateFactsForExitAreaFact() {

        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));

        FaExitFromSeaFact faExitFromSeaFact = ActivityFactMapper.INSTANCE.generateFactsForExitArea(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faExitFromSeaFact.getFaReportDocumentTypeCode().getValue());
        assertEquals(codeType.getValue(), faExitFromSeaFact.getFishingActivityTypeCode().getValue());
        assertEquals(fluxLocation, faExitFromSeaFact.getRelatedFLUXLocations().get(0));

    }

    @Test
    public void testGenerateFactsForFaNotificationOfArrivalFact() {

        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));
        fishingActivity.setReasonCode(codeType);
        fishingActivity.setOccurrenceDateTime(dateTimeType);
        fishingActivity.setSpecifiedFACatches(Collections.singletonList(faCatch));

        FaNotificationOfArrivalFact faNotificationOfArrivalFact = ActivityFactMapper.INSTANCE.generateFactsForPriorNotificationOfArrival(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faNotificationOfArrivalFact.getFaReportDocumentTypeCode().getValue());
        assertEquals(codeType.getValue(), faNotificationOfArrivalFact.getFishingActivityTypeCode().getValue());
        assertEquals(fluxLocation, faNotificationOfArrivalFact.getRelatedFLUXLocations().get(0));
        assertEquals(codeType.getValue(), faNotificationOfArrivalFact.getReasonCode().getValue());
        assertEquals(date, faNotificationOfArrivalFact.getOccurrenceDateTime());
        assertEquals(faCatch, faNotificationOfArrivalFact.getSpecifiedFACatches().get(0));

    }

    @Test
    public void testGenerateFactsForFaQuery() {

        FAQuery faQuery = new FAQuery();
        faQuery.setTypeCode(codeType);
        faQuery.setID(idType);
        faQuery.setSubmittedDateTime(dateTimeType);

        FaQueryFact faQueryFact = ActivityFactMapper.INSTANCE.generateFactsForFaQuery(faQuery);

        assertEquals(codeType.getValue(), faQueryFact.getTypeCode().getValue());
        assertEquals(idType.getValue(), faQueryFact.getId().getValue());
        assertEquals(date, faQueryFact.getSubmittedDateTime());

    }


}
