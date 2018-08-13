/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FISHING_GEAR;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FLUX_CHARACTERISTIC;
import static java.util.Collections.singletonList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.datatype.DatatypeFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaCatchFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaDepartureFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaDiscardFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaEntryToSeaFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaExitFromSeaFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaFishingOperationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaJointFishingOperationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaLandingFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaNotificationOfArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaNotificationOfTranshipmentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaQueryFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaQueryParameterFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaRelocationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaResponseFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaTranshipmentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingGearFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingTripFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FluxCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FluxFaReportMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FluxLocationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.GearCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.GearProblemFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.StructuredAddressFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.ValidationQualityAnalysisFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselStorageCharacteristicsFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FaResponseFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProcess;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProduct;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQueryParameter;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLAPDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXGeographicalCoordinate;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearProblem;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.SizeDistribution;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationQualityAnalysis;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
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

    String testXmlPath = "src/test/resources/testData/fluxFaResponseMessage.xml";
    FLUXFAReportMessage fluxFaTestMessage;

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
    private FAQuery faQuery;
    private List<FAQueryParameter> faQueryParameterList;
    private List<FLAPDocument> flapDocumentList;
    private FLUXResponseMessage fluxResponseMessage;
    private ValidationResultDocument validationResultDocument;
    private ValidationQualityAnalysis validationQualityAnalysis;

    ActivityFactMapper activityMapper = new ActivityFactMapper(new XPathStringWrapper());

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
        fishingTrip.setIDS(singletonList(idType));

        faCatch = new FACatch();
        faCatch.setTypeCode(codeType);

        fishingGear = new FishingGear();
        fishingGear.setTypeCode(codeType);
        fishingGear.setRoleCodes(singletonList(codeType));

        codeTypeList = singletonList(codeType);

        appliedAAPProcesses = new ArrayList<>();
        AAPProcess aapProcess = new AAPProcess();
        aapProcess.setTypeCodes(singletonList(codeType));

        measureType = new MeasureType();
        measureType.setUnitCode("unitCode");
        measureType.setValue(new BigDecimal(10));

        aapProduct = new AAPProduct();
        aapProduct.setWeightMeasure(measureType);
        aapProduct.setPackagingUnitQuantity(quantityType);

        aapProcess.setResultAAPProducts(singletonList(aapProduct));
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

        fluxFaTestMessage = loadTestData();

        faQueryParameterList = new ArrayList<>();
        FAQueryParameter faQueryParameter = new FAQueryParameter();
        faQueryParameter.setTypeCode(codeType);
        faQueryParameter.setValueCode(codeType);
        faQueryParameter.setValueDateTime(dateTimeType);
        faQueryParameter.setValueID(idType);
        faQueryParameterList.add(faQueryParameter);


        faQuery = new FAQuery();
        faQuery.setID(idType);
        faQuery.setSimpleFAQueryParameters(faQueryParameterList);
        faQuery.setSpecifiedDelimitedPeriod(delimitedPeriod);
        faQuery.setSubmittedDateTime(dateTimeType);
        faQuery.setTypeCode(codeType);

        flapDocumentList = new ArrayList<>();
        flapDocumentList.add(ActivityObjectsHelper.generateFLAPDocument());

        fluxResponseMessage = new FLUXResponseMessage();
        FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
        fluxResponseDocument.setResponseCode(codeType);
        fluxResponseDocument.setIDS(Collections.singletonList(idType));

        List<ValidationResultDocument> validationResultDocuments = new ArrayList<>();
        validationResultDocument = new ValidationResultDocument();
        validationResultDocument.setCreationDateTime(dateTimeType);
        validationResultDocument.setValidatorID(idType);

        List<ValidationQualityAnalysis> validationQualityAnalysisList = new ArrayList<>();
        validationQualityAnalysis = new ValidationQualityAnalysis();
        validationQualityAnalysis.setTypeCode(codeType);
        validationQualityAnalysis.setLevelCode(codeType);
        validationQualityAnalysis.setID(idType);

        validationQualityAnalysisList.add(validationQualityAnalysis);
        validationResultDocument.setRelatedValidationQualityAnalysises(validationQualityAnalysisList);
        validationResultDocuments.add(validationResultDocument);

        fluxResponseDocument.setRelatedValidationResultDocuments(validationResultDocuments);


        fluxResponseMessage.setFLUXResponseDocument(fluxResponseDocument);

    }

    @Test
    public void testGenerateFactsForNotificationOfTranshipment() {
        final FAReportDocument farep = fluxFaTestMessage.getFAReportDocuments().iterator().next();
        final FishingActivity fishAct = farep.getSpecifiedFishingActivities().iterator().next();
        final FaNotificationOfTranshipmentFact faNotificationOfTranshipmentFact = activityMapper.generateFactsForNotificationOfTranshipment(fishAct, farep);
        assertNotNull(faNotificationOfTranshipmentFact);
    }

    @Test
    public void testGenerateFactForVesselTransportMean() {

        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        vesselTransportMeans.setRoleCode(codeType);

        vesselTransportMeans.setIDS(singletonList(idType));

        VesselCountry vesselCountry = new VesselCountry();
        vesselCountry.setID(idType);
        vesselTransportMeans.setRegistrationVesselCountry(vesselCountry);

        ContactParty contactParty = new ContactParty();
        contactParty.setRoleCodes(singletonList(codeType));
        contactParty.setIDS(singletonList(idType));
        vesselTransportMeans.setSpecifiedContactParties(singletonList(contactParty));

        VesselTransportMeansFact mappedFact = activityMapper.generateFactForVesselTransportMean(vesselTransportMeans, false);

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
    public void testGenerateFactForGearCharacteristics() {

        GearCharacteristic gearCharacteristic = new GearCharacteristic();
        gearCharacteristic.setTypeCode(codeType);

        List<GearCharacteristicsFact> gearCharacteristicsFacts = activityMapper.generateFactsForGearCharacteristics(singletonList(gearCharacteristic), "null");

        assertEquals(codeType.getValue(), gearCharacteristicsFacts.get(0).getTypeCode().getValue());

    }

    @Test
    public void testGenerateFactForFishingTrip() {

        FishingTrip fishingTrip = new FishingTrip();
        fishingTrip.setIDS(singletonList(idType));
        fishingTrip.setTypeCode(codeType);

        List<FishingTripFact> fishingTripFacts = activityMapper.generateFactForFishingTrips(singletonList(fishingTrip), "null");

        assertEquals(idType.getValue(), fishingTripFacts.get(0).getIds().get(0).getValue());
        assertEquals(idType.getSchemeID(), fishingTripFacts.get(0).getIds().get(0).getSchemeId());
        assertEquals(codeType.getValue(), fishingTripFacts.get(0).getTypeCode().getValue());

    }

    @Test
    public void testGenerateFactForFaCatch() {

        final FACatch faCatch = new FACatch();
        faCatch.setTypeCode(codeType);
        faCatch.setSpeciesCode(codeType);
        faCatch.setUnitQuantity(quantityType);
        faCatch.setWeightMeasure(measureType);

        SizeDistribution sizeDistribution = new SizeDistribution();
        sizeDistribution.setClassCodes(codeTypeList);
        faCatch.setSpecifiedSizeDistribution(sizeDistribution);

        faCatch.setAppliedAAPProcesses(appliedAAPProcesses);

        FishingActivity faActivity = new FishingActivity();
        faActivity.setSpecifiedFACatches(new ArrayList<FACatch>() {{
            add(faCatch);
        }});
        faActivity.setRelatedFLUXLocations(null);

        FaCatchFact faCatchFact = activityMapper.generateFactsForFaCatch(faActivity,false, null).get(0);


        assertEquals(codeType.getValue(), faCatchFact.getTypeCode().getValue());
        assertEquals(measureType.getValue(), faCatchFact.getWeightMeasure().getValue());

    }

    @Test
    public void testMapToMeasureTypeWithEmptyValue(){
        ActivityFactMapper activityFactMapper = new ActivityFactMapper();
        MeasureType measureType = ActivityObjectsHelper.generateMeasureType("code", null);
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType measureType1 = activityFactMapper.mapToMeasureType(measureType);
        assertNull(measureType1);
    }

    @Test
    public void testMapToMeasureTypeWithNonEmptyValue(){
        ActivityFactMapper activityFactMapper = new ActivityFactMapper();
        MeasureType measureType = ActivityObjectsHelper.generateMeasureType("code", new BigDecimal("1"));
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType measureType1 = activityFactMapper.mapToMeasureType(measureType);
        assertNotNull(measureType1);
    }

    @Test
    public void testMapToMeasureTypeWithEmptySchemeID(){
        ActivityFactMapper activityFactMapper = new ActivityFactMapper();
        MeasureType measureType = ActivityObjectsHelper.generateMeasureType(null, new BigDecimal("1"));
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType measureType1 = activityFactMapper.mapToMeasureType(measureType);
        assertNotNull(measureType1);
    }

    @Test
    public void testMapToCodeTypeWithEmptyValue(){
        ActivityFactMapper activityFactMapper = new ActivityFactMapper();
        CodeType codeType = ActivityObjectsHelper.generateCodeTypeModel(null, "listID");
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType codeType1 = activityFactMapper.mapToCodeType(codeType);
        assertNull(codeType1);
    }

    @Test
    public void testMapToCodeTypeWithNonEmptyValue(){
        ActivityFactMapper activityFactMapper = new ActivityFactMapper();
        CodeType codeType = ActivityObjectsHelper.generateCodeTypeModel("someValue", null);
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType codeType1 = activityFactMapper.mapToCodeType(codeType);
        assertNotNull(codeType1);
    }

    @Test
    public void testMapToIDTypeWithNonEmptyValue(){
        ActivityFactMapper activityFactMapper = new ActivityFactMapper();
        IDType idType = ActivityObjectsHelper.generateIDTypeModel("someValue", null);
        IdType idType1 = activityFactMapper.mapToIdType(idType);
        assertNotNull(idType1);
    }

    @Test
    public void testMapToIDTypeWithEmptyValue(){
        ActivityFactMapper activityFactMapper = new ActivityFactMapper();
        IDType idType = ActivityObjectsHelper.generateIDTypeModel( null, null);
        IdType idType1 = activityFactMapper.mapToIdType(idType);
        assertNull(idType1);
    }


    @Test
    public void testGenerateFactsForFaLanding() {

        FishingActivity fishingActivity = new FishingActivity();
        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(singletonList(fluxLocation));
        fishingActivity.setSpecifiedFACatches(singletonList(faCatch));
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setSpecifiedFACatches(specifiedFACatch);

        FaLandingFact faLandingFact = activityMapper.generateFactsForLanding(fishingActivity, faReportDocument);

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

        FishingGearFact fishingGearFact = activityMapper.generateFactsForFishingGear(fishingGear, SPECIFIED_FISHING_GEAR);

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

        FluxLocationFact fluxLocationFact = activityMapper.generateFactForFluxLocation(fluxLocation, false, null, null, null);

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
        fishingActivity.setSpecifiedFACatches(singletonList(faCatch));
        fishingActivity.setRelatedFLUXLocations(singletonList(fluxLocation));
        fishingActivity.setSpecifiedFishingTrip(fishingTrip);
        fishingActivity.setSpecifiedFishingGears(singletonList(fishingGear));
        fishingActivity.setTypeCode(codeType);

        FaDepartureFact faDepartureFact = activityMapper.generateFactsForFaDeparture(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faDepartureFact.getFishingActivityTypeCode().getValue());

        assertEquals(codeType.getValue(), faDepartureFact.getReasonCode().getValue());
        //assertEquals(date, faDepartureFact.getOccurrenceDateTime().getStartDateTimes());
        assertEquals(fluxLocation, faDepartureFact.getRelatedFLUXLocations().get(0));
        assertEquals(fishingTrip, faDepartureFact.getSpecifiedFishingTrip());
        assertEquals(codeType.getValue(), faDepartureFact.getSpecifiedFACatchCodeTypes().get(0).getValue());
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
        fishingActivity.setSpecifiedFACatches(singletonList(faCatch));
        fishingActivity.setRelatedFLUXLocations(singletonList(fluxLocation));

        FaEntryToSeaFact faEntryToSeaFact = activityMapper.generateFactsForEntryIntoSea(fishingActivity, faReportDocument);

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
        fishingActivity.setRelatedFLUXLocations(singletonList(fluxLocation));
        fishingActivity.setVesselRelatedActivityCode(codeType);
        fishingActivity.setOperationsQuantity(quantityType);

        FaFishingOperationFact faFishingOperationFact = activityMapper.generateFactsForFishingOperation(fishingActivity, faReportDocument);

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
        fishingActivity.setRelatedFLUXLocations(singletonList(fluxLocation));

        List<FishingActivity> relatedFishingActivities = new ArrayList<>();
        FishingActivity relatedFishingActivity = new FishingActivity();
        relatedFishingActivity.setSpecifiedFACatches(singletonList(faCatch));
        relatedFishingActivities.add(relatedFishingActivity);
        fishingActivity.setRelatedFishingActivities(relatedFishingActivities);

        FaJointFishingOperationFact faJointFishingOperationFact = activityMapper.generateFactsForJointFishingOperation(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faJointFishingOperationFact.getFaReportDocumentTypeCode().getValue());
        assertEquals(codeType.getValue(), faJointFishingOperationFact.getFishingActivityTypeCode().getValue());
        assertEquals(fluxLocation, faJointFishingOperationFact.getRelatedFLUXLocations().get(0));
        assertEquals(relatedFishingActivities.size(), faJointFishingOperationFact.getRelatedFishingActivities().size());
        assertEquals(1, faJointFishingOperationFact.getRelatedFishingActivityFaCatch().size());

    }

    @Test
    public void testGenerateFactsForExitAreaFact() {

        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(singletonList(fluxLocation));

        FaExitFromSeaFact faExitFromSeaFact = activityMapper.generateFactsForExitArea(fishingActivity, faReportDocument);

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
        fishingActivity.setRelatedFLUXLocations(singletonList(fluxLocation));
        fishingActivity.setReasonCode(codeType);
        fishingActivity.setOccurrenceDateTime(dateTimeType);
        fishingActivity.setSpecifiedFACatches(singletonList(faCatch));

        FaNotificationOfArrivalFact faNotificationOfArrivalFact = activityMapper.generateFactsForPriorNotificationOfArrival(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faNotificationOfArrivalFact.getFaReportDocumentTypeCode().getValue());
        assertEquals(codeType.getValue(), faNotificationOfArrivalFact.getFishingActivityTypeCode().getValue());
        assertEquals(fluxLocation, faNotificationOfArrivalFact.getRelatedFLUXLocations().get(0));
        assertEquals(codeType.getValue(), faNotificationOfArrivalFact.getReasonCode().getValue());
        // assertEquals(date, faNotificationOfArrivalFact.getOccurrenceDateTime().getStartDateTimes());
        assertEquals(faCatch, faNotificationOfArrivalFact.getSpecifiedFACatches().get(0));

    }

    @Test
    public void testGenerateFactsForFaQuery() {

        FAQuery faQuery = new FAQuery();
        faQuery.setTypeCode(codeType);
        faQuery.setID(idType);
        faQuery.setSubmittedDateTime(dateTimeType);

        FaQueryFact faQueryFact = activityMapper.generateFactsForFaQuery(faQuery);

        assertEquals(codeType.getValue(), faQueryFact.getTypeCode().getValue());
        assertEquals(idType.getValue(), faQueryFact.getId().getValue());
        assertEquals(date, faQueryFact.getSubmittedDateTime());

    }

    @Test
    public void testGenerateFactsForFaDeclarationOfArrivalFact() {

        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setTypeCode(codeType);
        fishingActivity.setRelatedFLUXLocations(singletonList(fluxLocation));
        fishingActivity.setReasonCode(codeType);
        fishingActivity.setOccurrenceDateTime(dateTimeType);
        List<FishingGear> gears = new ArrayList<>();
        gears.add(fishingGear);
        fishingActivity.setSpecifiedFishingGears(gears);

        FaArrivalFact faDeclarationOfArrivalFact = activityMapper.generateFactsForDeclarationOfArrival(fishingActivity, faReportDocument);

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
        VesselStorageCharacteristicsFact vesselStorageCharacteristicsFact = activityMapper.generateFactsForVesselStorageCharacteristic(vesselStorageCharacteristic);

        assertEquals(codeType.getValue(), vesselStorageCharacteristicsFact.getTypeCodes().get(0).getValue());


    }


    @SneakyThrows
    private FLUXFAReportMessage loadTestData() throws IOException, MdrModelMarshallException {
        String fluxFaMessageStr = IOUtils.toString(new FileInputStream(testXmlPath));
        return JAXBUtils.unMarshallMessage(fluxFaMessageStr, FLUXFAReportMessage.class);
    }

    @Test
    public void testGenerateFactsForRelocation() {

        FishingActivity fishingActivity = new FishingActivity();

        fishingActivity.setSpecifiedFACatches(ActivityObjectsHelper.generateFACatchList());

        FaRelocationFact faRelocationFact = activityMapper.generateFactsForRelocation(fishingActivity, new FAReportDocument(), false);

        assertNotNull(faRelocationFact.getSpecifiedFACatches());
    }


    @Test
    public void testGenerateFactsForDiscard() {

        FishingActivity fishingActivity = new FishingActivity();
        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setTypeCode(codeType);

        fishingActivity.setTypeCode(codeType);

        FaDiscardFact faDiscardFact = activityMapper.generateFactsForDiscard(fishingActivity, faReportDocument);

        assertEquals(codeType.getValue(), faDiscardFact.getFaReportDocumentTypeCode().getValue());
    }

    @Test
    public void testGenerateFactsForFluxCharacteristics() {

        FLUXCharacteristic characteristic = new FLUXCharacteristic();
        characteristic.setTypeCode(codeType);

        List<FluxCharacteristicsFact> fluxCharacteristicsFacts = activityMapper.generateFactsForFluxCharacteristics(Arrays.asList(characteristic), SPECIFIED_FLUX_CHARACTERISTIC);

        assertEquals(codeType.getValue(), fluxCharacteristicsFacts.get(0).getTypeCode().getValue());
    }

    @Test
    public void testGenerateFactsForVesselStorageCharacteristics() {

        VesselStorageCharacteristic vesselStorageCharacteristic = new VesselStorageCharacteristic();
        vesselStorageCharacteristic.setTypeCodes(Arrays.asList(codeType));

        List<VesselStorageCharacteristicsFact> vesselStorageCharacteristicsFacts = activityMapper.generateFactsForVesselStorageCharacteristics(Arrays.asList(vesselStorageCharacteristic));

        assertEquals(codeType.getValue(), vesselStorageCharacteristicsFacts.get(0).getTypeCodes().get(0).getValue());
    }

    @Test
    public void testGenerateFactsForGearProblem() {
        GearProblem gearProblem = new GearProblem();
        gearProblem.setTypeCode(codeType);

        GearProblemFact gearProblemFact = activityMapper.generateFactsForGearProblem(gearProblem);

        assertEquals(codeType.getValue(), gearProblemFact.getTypeCode().getValue());
    }

    @Test
    public void testNullParameters() {

        final List<VesselStorageCharacteristicsFact> vesselStorageCharacteristicsFacts = activityMapper.generateFactsForVesselStorageCharacteristics(null);
        final List<FluxCharacteristicsFact> fluxCharacteristicsFacts = activityMapper.generateFactsForFluxCharacteristics(null, null);
        final FaDiscardFact faDiscardFact = activityMapper.generateFactsForDiscard(null, null);
        final FaRelocationFact faRelocationFact = activityMapper.generateFactsForRelocation(null, null, false);
        final VesselStorageCharacteristicsFact vesselStorageCharacteristicsFact = activityMapper.generateFactsForVesselStorageCharacteristic(null);
        final FaArrivalFact faArrivalFact = activityMapper.generateFactsForDeclarationOfArrival(null, null);
        final FaQueryFact faQueryFact = activityMapper.generateFactsForFaQuery(null);
        final FaNotificationOfArrivalFact faNotificationOfArrivalFact = activityMapper.generateFactsForPriorNotificationOfArrival(null, null);
        final FaJointFishingOperationFact faJointFishingOperationFact = activityMapper.generateFactsForJointFishingOperation(null, null);
        final FaEntryToSeaFact faEntryToSeaFact = activityMapper.generateFactsForEntryIntoSea(null, null);
        final FaFishingOperationFact faFishingOperationFact = activityMapper.generateFactsForFishingOperation(null, null);
        final FluxLocationFact fluxLocationFact = activityMapper.generateFactForFluxLocation(null, false, null, null, null);
        final FaDepartureFact faDepartureFact = activityMapper.generateFactsForFaDeparture(null, null);
        final List<FaCatchFact> faCatchFacts = activityMapper.generateFactsForFaCatch(null,false, null);
        final FaLandingFact faLandingFact = activityMapper.generateFactsForLanding(null, null);
        final List<GearCharacteristicsFact> gearList = activityMapper.generateFactsForGearCharacteristics(null, "null");
        final List<FishingTripFact> fishingTripFacts = activityMapper.generateFactForFishingTrips(null, null);
        final VesselTransportMeansFact vesselTransportMeansFact = activityMapper.generateFactForVesselTransportMean(null);
        final List<GearCharacteristicsFact> gearCharacteristicsFacts = activityMapper.generateFactsForGearCharacteristics(null, null);
        final FaResponseFact faResponseFact = new FaResponseFactMapper().generateFactsForFaResponse(null);
        final ValidationQualityAnalysisFact qualityAnalysisFact = activityMapper.generateFactsForValidationQualityAnalysis(null);

        final FluxFaReportMessageFact fluxFaReportMessageFact = activityMapper.generateFactForFluxFaReportMessage(null);
        final List<VesselTransportMeansFact> vesselTransportMeansFacts = activityMapper.generateFactForVesselTransportMeans(null);
        final List<StructuredAddressFact> structuredAddressFacts = activityMapper.generateFactsForStructureAddresses(null, null);
        final FishingGearFact fishingGearFact = activityMapper.generateFactsForFishingGear(null, null);
        final List<FishingGearFact> fishingGearFacts = activityMapper.generateFactsForFishingGears(null, null , new HashMap<String, List<GearMatrix.Condition>>());
        final FaReportDocumentFact faReportDocumentFact = activityMapper.generateFactForFaReportDocument(null);
        final GearCharacteristicsFact gearCharacteristicsFact = activityMapper.generateFactsForGearCharacteristic(null);
        final GearProblemFact gearProblemFact = activityMapper.generateFactsForGearProblem(null);

        final List<FaReportDocumentFact> faReportDocumentFacts = activityMapper.generateFactForFaReportDocuments(null);
        final List<GearProblemFact> gearProblemFacts = activityMapper.generateFactsForGearProblems(null);

        final FishingTripFact fishingTripFact = activityMapper.generateFactForFishingTrip(null);
        final List<FluxLocationFact> fluxLocationFacts = activityMapper.generateFactsForFluxLocations(null, false, null, null, null);
        final FluxCharacteristicsFact fluxCharacteristicsFact = activityMapper.generateFactForFluxCharacteristic(null, "");
        final FaExitFromSeaFact faExitFromSeaFact = activityMapper.generateFactsForExitArea(null, null);
        final FaTranshipmentFact faTranshipmentFact = activityMapper.generateFactsForTranshipment(null, null);
        final FaNotificationOfTranshipmentFact faNotificationOfTranshipmentFact = activityMapper.generateFactsForNotificationOfTranshipment(null, null);

        assertTrue(isEmpty(vesselStorageCharacteristicsFacts));
        assertTrue(isEmpty(fluxCharacteristicsFacts));
        assertTrue(isEmpty(faCatchFacts));
        assertTrue(isEmpty(gearList));
        assertTrue(isEmpty(fishingTripFacts));
        assertTrue(isEmpty(gearCharacteristicsFacts));
        assertTrue(isEmpty(vesselTransportMeansFacts));
        assertTrue(isEmpty(structuredAddressFacts));
        assertTrue(isEmpty(fishingGearFacts));
        assertTrue(isEmpty(faReportDocumentFacts));
        assertTrue(isEmpty(gearProblemFacts));
        assertTrue(isEmpty(fluxLocationFacts));

        assertNull(faDiscardFact);
        assertNull(faRelocationFact);
        assertNull(vesselStorageCharacteristicsFact);
        assertNull(faArrivalFact);
        assertNull(faQueryFact);
        assertNull(faNotificationOfArrivalFact);
        assertNull(faJointFishingOperationFact);
        assertNull(faEntryToSeaFact);
        assertNull(faFishingOperationFact);
        assertNull(fluxLocationFact);
        assertNull(faDepartureFact);
        assertNull(faLandingFact);
        assertNull(vesselTransportMeansFact);
        assertNull(fluxFaReportMessageFact);
        assertNull(fishingGearFact);
        assertNull(faReportDocumentFact);
        assertNull(gearCharacteristicsFact);
        assertNull(gearProblemFact);
        assertNull(fishingTripFact);
        assertNull(fluxCharacteristicsFact);
        assertNull(faExitFromSeaFact);
        assertNull(faTranshipmentFact);
        assertNull(faNotificationOfTranshipmentFact);
        assertNull(faResponseFact);
        assertNull(qualityAnalysisFact);

    }

    @Test
    public void testMapToMeasureType() {

        List<MeasureType> measureTypeList = new ArrayList<>();
        measureTypeList.add(measureType);

        List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType> measureTypes = activityMapper.mapToMeasureType(measureTypeList);

        assertEquals(measureType.getValue(), measureTypes.get(0).getValue());
    }

    @Test
    public void testVesselTransportMeanRegistrationVesselCountryID() {

        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        VesselCountry vesselCountry = new VesselCountry();
        vesselCountry.setID(idType);
        vesselTransportMeans.setRegistrationVesselCountry(vesselCountry);

        IDType idType = activityMapper.vesselTransportMeanRegistrationVesselCountryID(vesselTransportMeans);

        assertEquals(idType.getValue(), idType.getValue());
    }

    @Test //FIXME
    public void testNullInsideObjects() {
        FishingActivity faAct = new FishingActivity();
        final List<FaCatchFact> faCatchFacts = activityMapper.generateFactsForFaCatch(faAct,false, null);

        List<GearProblem> gearList = new ArrayList<GearProblem>() {{
            add(new GearProblem());
        }};
        activityMapper.generateFactsForGearProblems(gearList);
    }

    @Test
    public void testGenerateFactsForFaQueryParametersWithNull() {
        List<FaQueryParameterFact> faQueryParameterFacts =
                activityMapper.generateFactsForFaQueryParameters(null, null);

        assertTrue(isEmpty(faQueryParameterFacts));
    }

    @Test
    public void testGenerateFactsForFaQueryParametersHappy() {

        List<FaQueryParameterFact> faQueryParameterFacts =
                activityMapper.generateFactsForFaQueryParameters(faQueryParameterList, faQuery);

        FaQueryParameterFact fact = faQueryParameterFacts.get(0);

        assertEquals(codeType.getValue(), fact.getFaQueryTypeCode().getValue());
        assertEquals(codeType.getListID(), fact.getFaQueryTypeCode().getListId());
        assertEquals(codeType.getValue(), fact.getTypeCode().getValue());
        assertEquals(codeType.getListID(), fact.getTypeCode().getListId());
        assertEquals(codeType.getListID(), fact.getValueCode().getListId());
        assertEquals(codeType.getValue(), fact.getValueCode().getValue());
        assertEquals(idType.getSchemeID(), fact.getValueID().getSchemeId());
        assertEquals(idType.getValue(), fact.getValueID().getValue());

    }

    @Test
    public void testGetFLAPDocumentIds() {
        List<IdType> idTypes = activityMapper.getFLAPDocumentIds(flapDocumentList);

        List<IdType> expectedResult = new ArrayList<>();
        expectedResult.add(ActivityObjectsHelper.generateIdType("value", "FLAP_DOCUMENT_ID"));

        assertEquals(expectedResult.size(), idTypes.size());
        assertEquals(expectedResult.iterator().next().getValue(), idTypes.iterator().next().getValue());
    }

    @Test
    public void testGenerateFactsForFaResponse() {
        fluxResponseMessage.getFLUXResponseDocument().setRespondentFLUXParty(null);
        FaResponseFact faResponseFact = new FaResponseFactMapper().generateFactsForFaResponse(fluxResponseMessage);
        assertEquals(codeType.getValue(), faResponseFact.getResponseCode().getValue());
        assertEquals(null, faResponseFact.getFluxPartyIds());
    }

    @Test
    public void testGenerateFactsForFaResponse_nullDocument() {
        fluxResponseMessage.setFLUXResponseDocument(null);
        FaResponseFact faResponseFact = new FaResponseFactMapper().generateFactsForFaResponse(fluxResponseMessage);
        assertEquals(null, faResponseFact.getResponseCode());
    }

    @Test
    public void testGenerateFactsForValidationQualityAnalysis() {
        ValidationQualityAnalysisFact qualityAnalysisFact = activityMapper.generateFactsForValidationQualityAnalysis(validationQualityAnalysis);
        assertEquals(codeType.getValue(), qualityAnalysisFact.getLevelCode().getValue());
    }

    @Test
    public void testGetIdsWithoutSchemeId() {
        FLUXReportDocument document = new FLUXReportDocument();
        idType.setSchemeID(null);
        document.setIDS(singletonList(idType));
        assertTrue(isEmpty(ActivityFactMapper.getIds(document)));
    }

}