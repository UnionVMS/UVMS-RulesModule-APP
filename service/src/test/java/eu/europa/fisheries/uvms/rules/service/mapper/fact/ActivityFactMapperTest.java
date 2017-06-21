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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaCatchFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaDepartureFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaEntryToSeaFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaExitFromSeaFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaFishingOperationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaJointFishingOperationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaLandingFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaNotificationOfArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaQueryFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingActivityFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingGearFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingTripFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FluxLocationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.GearCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselStorageCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProcess;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProduct;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXGeographicalCoordinate;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.SizeDistribution;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselCountry;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselStorageCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.QuantityType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

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
    private List<CodeType> codeTypeList;
    private MeasureType measureType;
    private List<AAPProcess> appliedAAPProcesses;
    private AAPProduct aapProduct;
    private List<GearCharacteristic> applicableGearCharacteristics;
    private FLUXGeographicalCoordinate fluxGeographicalCoordinate;
    private List<FLUXLocation> specifiedFluxLocation;
    private List<FACatch> specifiedFACatch;

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

        measureType = new MeasureType();
        measureType.setUnitCode("unitCode");
        measureType.setValue(new BigDecimal(10));

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

        codeTypeList = Collections.singletonList(codeType);

        appliedAAPProcesses = new ArrayList<>();
        AAPProcess aapProcess = new AAPProcess();
        aapProcess.setTypeCodes(Collections.singletonList(codeType));

        measureType = new MeasureType();
        measureType.setUnitCode("unitCode");
        measureType.setValue(new BigDecimal(10));

        aapProduct = new AAPProduct();
        aapProduct.setWeightMeasure(measureType);
        aapProduct.setPackagingUnitQuantity(quantityType);

        aapProcess.setResultAAPProducts(Collections.singletonList(aapProduct));
        appliedAAPProcesses.add(aapProcess);

        GearCharacteristic gearCharacteristic = new GearCharacteristic();
        gearCharacteristic.setTypeCode(codeType);
        gearCharacteristic.setValue(new TextType("testValue", null, null));

        applicableGearCharacteristics = new ArrayList<>();
        applicableGearCharacteristics.add(gearCharacteristic);

        fluxGeographicalCoordinate = new FLUXGeographicalCoordinate();
        fluxGeographicalCoordinate.setAltitudeMeasure(measureType);
        fluxGeographicalCoordinate.setLatitudeMeasure(measureType);

        fluxLocation = new FLUXLocation();
        fluxLocation.setTypeCode(codeType);
        faCatch = new FACatch();
        faCatch.setTypeCode(codeType);
        specifiedFluxLocation = new ArrayList<>();
        specifiedFluxLocation.add(fluxLocation);
        faCatch.setSpecifiedFLUXLocations(specifiedFluxLocation);
        specifiedFACatch = new ArrayList<>();
        specifiedFACatch.add(faCatch);

    }

    @Test
    public void testGenerateFactForVesselTransportMean() {

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

        VesselTransportMeansFact mappedFact = ActivityFactMapper.INSTANCE.generateFactForVesselTransportMean(vesselTransportMeans, false);

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
        assertEquals(quantityType.getValue().intValue(), fishingActivityFacts.get(0).getOperationQuantity(), 0);
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
    public void testGenerateFactForFaCatch() {

        FACatch faCatch = new FACatch();
        faCatch.setTypeCode(codeType);
        faCatch.setSpeciesCode(codeType);
        faCatch.setUnitQuantity(quantityType);
        faCatch.setWeightMeasure(measureType);

        SizeDistribution sizeDistribution = new SizeDistribution();
        sizeDistribution.setClassCodes(codeTypeList);
        faCatch.setSpecifiedSizeDistribution(sizeDistribution);

        faCatch.setAppliedAAPProcesses(appliedAAPProcesses);


        FaCatchFact faCatchFact = ActivityFactMapper.INSTANCE.generateFactsForFaCatch(faCatch, null);


        assertEquals(codeType.getValue(), faCatchFact.getTypeCode().getValue());
        assertEquals(measureType.getValue(), faCatchFact.getWeightMeasure().getValue());

    }

    @Test
    public void testGenerateFactsForFaLanding() {

        FishingActivity fishingActivity = new FishingActivity();
        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));
        fishingActivity.setSpecifiedFACatches(Collections.singletonList(faCatch));
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setSpecifiedFACatches(specifiedFACatch);

        FaLandingFact faLandingFact = ActivityFactMapper.INSTANCE.generateFactsForLanding(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faLandingFact.getFishingActivityCodeType().getValue());
        assertEquals(fluxLocation, faLandingFact.getRelatedFluxLocations().get(0));
        //  assertEquals(faCatch, faLandingFact.getSpecifiedFaCatches().get(0));
        assertEquals(codeType.getValue(), faLandingFact.getFaReportDocumentTypeCode().getValue());
        assertEquals(specifiedFACatch.size(), faLandingFact.getSpecifiedFaCatchFluxLocationTypeCode().size());
        assertEquals(specifiedFACatch.size(), faLandingFact.getSpecifiedFaCatchTypeCode().size());

    }


    @Test
    public void testGenerateFactForFishingGear() {

        FishingGear fishingGear = new FishingGear();
        fishingGear.setTypeCode(codeType);
        fishingGear.setApplicableGearCharacteristics(applicableGearCharacteristics);

        FishingGearFact fishingGearFact = ActivityFactMapper.INSTANCE.generateFactsForFishingGear(fishingGear);

        assertEquals(codeType.getValue(), fishingGearFact.getTypeCode().getValue());
        assertNotNull(fishingGearFact.getApplicableGearCharacteristics());

    }

    @Test
    public void testGenerateFactForFLUXLocation() {

        FLUXLocation fluxLocation = new FLUXLocation();
        fluxLocation.setTypeCode(codeType);
        fluxLocation.setCountryID(idType);
        fluxLocation.setID(idType);
        fluxLocation.setSpecifiedPhysicalFLUXGeographicalCoordinate(fluxGeographicalCoordinate);

        FluxLocationFact fluxLocationFact = ActivityFactMapper.INSTANCE.generateFactForFluxLocation(fluxLocation);

        assertEquals(codeType.getValue(), fluxLocationFact.getTypeCode().getValue());
        assertNotNull(fluxLocationFact.getSpecifiedPhysicalFLUXGeographicalCoordinate());

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
        //assertEquals(date, faDepartureFact.getOccurrenceDateTime().getDate());
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
        // assertEquals(date, faNotificationOfArrivalFact.getOccurrenceDateTime().getDate());
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
        // assertEquals(date, faQueryFact.getSubmittedDateTime().getDate());

    }

    @Test
    public void testGenerateFactsForFaDeclarationOfArrivalFact() {

        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(Collections.singletonList(fluxLocation));
        fishingActivity.setReasonCode(codeType);
        fishingActivity.setOccurrenceDateTime(dateTimeType);
        List<FishingGear> gears = new ArrayList<>();
        gears.add(fishingGear);
        fishingActivity.setSpecifiedFishingGears(gears);

        FaArrivalFact faDeclarationOfArrivalFact = ActivityFactMapper.INSTANCE.generateFactsForDeclarationOfArrival(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faDeclarationOfArrivalFact.getFaReportTypeCode().getValue());
        assertEquals(codeType.getValue(), faDeclarationOfArrivalFact.getFishingActivityTypeCode().getValue());
        assertEquals(fluxLocation, faDeclarationOfArrivalFact.getRelatedFLUXLocations().get(0));
        assertEquals(codeType.getValue(), faDeclarationOfArrivalFact.getReasonCode().getValue());
        assertEquals(dateTimeType, faDeclarationOfArrivalFact.getOccurrenceDateTime());
        assertNotSame(0, faDeclarationOfArrivalFact.getFishingGearRoleCodes().size());
    }

    @Test
    public void testGenerateFactsForVesselStorageCharacteristic() {

        VesselStorageCharacteristic vesselStorageCharacteristic = new VesselStorageCharacteristic();
        vesselStorageCharacteristic.setTypeCodes(codeTypeList);
        VesselStorageCharacteristicsFact vesselStorageCharacteristicsFact = ActivityFactMapper.INSTANCE.generateFactsForVesselStorageCharacteristic(vesselStorageCharacteristic);

        assertEquals(codeType.getValue(), vesselStorageCharacteristicsFact.getTypeCodes().get(0).getValue());


    }


}
