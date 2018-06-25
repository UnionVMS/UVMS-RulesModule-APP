/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

<<<<<<< HEAD:service/src/test/java/eu/europa/ec/fisheries/uvms/rules/service/bean/RuleTestHelper.java
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.NumericType;
=======
import eu.europa.ec.fisheries.uvms.rules.dto.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FactConstants;
>>>>>>> 983ed00d1bdf91b1f6b7b6b04a430fb5bbaafa9c:service/src/test/java/eu/europa/ec/fisheries/uvms/rules/service/business/generator/helper/ActivityObjectsHelper.java
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLAPDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.StructuredAddress;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
<<<<<<< HEAD:service/src/test/java/eu/europa/ec/fisheries/uvms/rules/service/bean/RuleTestHelper.java
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

public class RuleTestHelper {
=======
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

public class ActivityObjectsHelper {

    private ActivityObjectsHelper(){
>>>>>>> 983ed00d1bdf91b1f6b7b6b04a430fb5bbaafa9c:service/src/test/java/eu/europa/ec/fisheries/uvms/rules/service/business/generator/helper/ActivityObjectsHelper.java

    }

    public static FishingActivity generateActivity(String occurrence, String activityType){
        FishingActivity departure = new FishingActivity();
        CodeType codeType = new CodeType();
        codeType.setValue(activityType);
        try {
            DateTimeType dateTimeType = new DateTimeType();
            dateTimeType.setDateTime(toXMLGregorianCalendar(occurrence));
            departure.setTypeCode(codeType);
            departure.setOccurrenceDateTime(dateTimeType);
        } catch (ParseException | DatatypeConfigurationException e){
            e.printStackTrace();
        }
        return departure;
    }

    private static XMLGregorianCalendar toXMLGregorianCalendar(String dateString) throws ParseException, DatatypeConfigurationException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = sdf.parse(dateString);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }

    public static eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType generateCodeType(String value, String listId) {
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType codeType = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        codeType.setValue(value);
        codeType.setListId(listId);
        return codeType;
    }

    public static MeasureType generateMeasureType(BigDecimal value, String unitCode) {
        MeasureType measureType = new MeasureType();
        measureType.setValue(value);
        measureType.setUnitCode(unitCode);
        return measureType;
    }

    public static IdType generateIdType(String value, String schemeId) {
        IdType idType = new IdType();
        idType.setValue(value);
        idType.setSchemeId(schemeId);
        return idType;
    }

    public static NumericType generateNumericType(BigDecimal value, String format) {
        NumericType numericType = new NumericType();
        numericType.setValue(value);
        numericType.setFormat(format);
        return numericType;
    }

    public static TextType generateTextType(String value, String languageId, String languageLocaleId) {
        TextType textType = new TextType();
        textType.setValue(value);
        textType.setLanguageID(languageId);
        textType.setLanguageLocaleID(languageLocaleId);
        return textType;
    }

    public static List<FLUXLocation> generateFluxLocationsWithPositionValue() {
        List<FLUXLocation> fluxLocations = new ArrayList<>(2);
        FLUXLocation fluxLocation = generateFluxLocationWithTypeCodeValue("POSITION");
        fluxLocations.add(fluxLocation);
        fluxLocation = generateFluxLocationWithTypeCodeValue("AREA");
        fluxLocations.add(fluxLocation);
        fluxLocation = generateFluxLocationWithTypeCodeValue("TEST");
        fluxLocations.add(fluxLocation);
        return fluxLocations;
    }

    public static FLUXLocation generateFluxLocationWithTypeCodeValue(String typeCodeValue) {
        FLUXLocation fluxLocation = new FLUXLocation();
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID("FLUX_LOCATION_TYPE");
        codeType.setValue(typeCodeValue);
        fluxLocation.setTypeCode(codeType);
        fluxLocation.setID(generateIdTypeUNCEFACT("", "FARM"));
        return fluxLocation;
    }

    public static FACatch generateFACatch(String typeCode, String speciesCode){
        FACatch faCatch = new FACatch();
        faCatch.setTypeCode(generateCodeTypeUNCEFACT(typeCode, ""));
        faCatch.setSpeciesCode(generateCodeTypeUNCEFACT(speciesCode, ""));
        return faCatch;
    }

    public static List<FACatch> generateFACatchList() {
        List<FACatch> faCatches = new ArrayList<>(2);
        FACatch faCatch = new FACatch();

        faCatch.setTypeCode(generateCodeTypeUNCEFACT("LOADED", ""));
        List<FLUXLocation> fluxLocations = new ArrayList<>();
        fluxLocations.add(generateFluxLocationWithTypeCodeValue("AREA"));
        faCatch.setSpecifiedFLUXLocations(fluxLocations);
        faCatches.add(faCatch);

        FACatch allocatedQutaFaCatch = new FACatch();
        allocatedQutaFaCatch.setTypeCode(generateCodeTypeUNCEFACT("ALLOCATED_TO_QUOTA", ""));
        faCatches.add(allocatedQutaFaCatch);

        FACatch bftFaCatch = new FACatch();
        bftFaCatch.setSpeciesCode(generateCodeTypeUNCEFACT("BFT", ""));

        List<FLUXLocation> fluxLocationsBFT = new ArrayList<>();
        fluxLocationsBFT.add(generateFluxLocationWithTypeCodeValue("LOCATION"));
        bftFaCatch.setDestinationFLUXLocations(fluxLocationsBFT);

        FACatch loadedBFT = new FACatch();
        loadedBFT.setTypeCode(generateCodeTypeUNCEFACT("LOADED", ""));
        loadedBFT.setSpeciesCode(generateCodeTypeUNCEFACT("BFT", ""));
        loadedBFT.setDestinationFLUXLocations(fluxLocationsBFT);
        fluxLocations.add(generateFluxLocationWithTypeCodeValue("AREA"));
        loadedBFT.setSpecifiedFLUXLocations(fluxLocations);
        faCatches.add(loadedBFT);
        return faCatches;
    }

    public static un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType generateCodeTypeUNCEFACT(String value, String listId) {
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID(listId);
        codeType.setValue(value);
        return codeType;
    }

    public static IDType generateIdTypeUNCEFACT(String value, String schemeId) {
        IDType idType = new IDType();
        idType.setValue(value);
        idType.setSchemeID(schemeId);
        return idType;
    }

<<<<<<< HEAD:service/src/test/java/eu/europa/ec/fisheries/uvms/rules/service/bean/RuleTestHelper.java
    public GearCharacteristic generateGearCharacteristic(String typeCodeValue){
        GearCharacteristic gearCharacteristic = new GearCharacteristic();
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID("FA_GEAR_CHARACTERISTIC");
        codeType.setValue(typeCodeValue);
        gearCharacteristic.setTypeCode(codeType);
        return gearCharacteristic;
    }


    public static FLAPDocument getFLAPDocument() {
=======
    public static FLAPDocument generateFLAPDocument() {
>>>>>>> 983ed00d1bdf91b1f6b7b6b04a430fb5bbaafa9c:service/src/test/java/eu/europa/ec/fisheries/uvms/rules/service/business/generator/helper/ActivityObjectsHelper.java
        FLAPDocument flapDocument = new FLAPDocument();
        flapDocument.setID(generateIdTypeUNCEFACT("value", "FLAP_DOCUMENT_ID"));
        return flapDocument;
    }

    public static FishingActivity generateActivity() {
        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setRelatedVesselTransportMeans(Collections.singletonList(getVesselTransportMeans()));
        fishingActivity.setSpecifiedFACatches(generateFACatchList());
        return fishingActivity;
    }

    public static VesselTransportMeans getVesselTransportMeans() {
        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        vesselTransportMeans.setRoleCode(ActivityObjectsHelper.generateCodeTypeUNCEFACT("FA_VESSEL_ROLE", "PARTICIPATING_VESSEL"));
        return vesselTransportMeans;
    }

    public static List<VesselTransportMeans> generateListOfVesselTransportMeans() {
        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        vesselTransportMeans.setRoleCode(ActivityObjectsHelper.generateCodeTypeUNCEFACT("FA_VESSEL_ROLE", "PARTICIPATING_VESSEL"));
        return Collections.singletonList(vesselTransportMeans);
    }

    public static ValidationResultDocument generateValidationResultDocument() {
        ValidationResultDocument validationResultDocument = new ValidationResultDocument();
        validationResultDocument.setValidatorID(ActivityObjectsHelper.generateIdTypeUNCEFACT("value", "SchemeId"));
        return validationResultDocument;
    }

    public static FLUXLocation generateFLUXLocation(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode, IDType id) {
        FLUXLocation fluxLocation = new FLUXLocation();
        fluxLocation.setTypeCode(typeCode);
        fluxLocation.setID(id);
        return fluxLocation;
    }

<<<<<<< HEAD:service/src/test/java/eu/europa/ec/fisheries/uvms/rules/service/bean/RuleTestHelper.java
    public static ContactParty getContactParty(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType roleCode,StructuredAddress structuredAddress){
=======
    public static List<GearCharacteristic> generateGearCharacteristics() {
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
        codeType.setValue("HE");
        gearCharacteristic.setTypeCode(codeType);
        gearCharacteristics.add(gearCharacteristic);

        gearCharacteristic = new GearCharacteristic();
        codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID(FactConstants.FA_GEAR_CHARACTERISTIC);
        codeType.setValue("GD");
        gearCharacteristic.setTypeCode(codeType);
        gearCharacteristics.add(gearCharacteristic);

        return gearCharacteristics;
    }

    public static List<FishingGearTypeCharacteristic> generateFishingGearTypeCharacteristics() {
        List<FishingGearTypeCharacteristic> fishingGearTypeCharacteristics = new ArrayList<>();
        List<GearCharacteristic> gearCharacteristics = ActivityObjectsHelper.generateGearCharacteristics();
        for (GearCharacteristic gearCharacteristic : gearCharacteristics) {
            FishingGearTypeCharacteristic fishingGearTypeCharacteristic = new FishingGearTypeCharacteristic();
            fishingGearTypeCharacteristic.setMandatory(true);
            fishingGearTypeCharacteristic.setFishingGearCharacteristicCode(gearCharacteristic.getTypeCode().getValue());
            fishingGearTypeCharacteristic.setFishingGearTypeCode("PS");
            fishingGearTypeCharacteristics.add(fishingGearTypeCharacteristic);
        }
        return fishingGearTypeCharacteristics;
    }

    public static ContactParty generateContactParty(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType roleCode, StructuredAddress structuredAddress){
>>>>>>> 983ed00d1bdf91b1f6b7b6b04a430fb5bbaafa9c:service/src/test/java/eu/europa/ec/fisheries/uvms/rules/service/business/generator/helper/ActivityObjectsHelper.java
        ContactParty contactParty = new ContactParty();
        contactParty.setRoleCodes(Collections.singletonList(roleCode));
        contactParty.setSpecifiedStructuredAddresses(Collections.singletonList(structuredAddress));
        return contactParty;
    }

    public static StructuredAddress generateStructuredAddress(){
        return new StructuredAddress();
    }
}
