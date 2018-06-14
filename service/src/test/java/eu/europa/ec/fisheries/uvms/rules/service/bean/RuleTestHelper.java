package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.dto.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FactConstants;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLAPDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.StructuredAddress;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

public class RuleTestHelper {

    public static RuleType createRuleType(String expression, String brId, String note, ErrorType type, String errorMessage) {
        RuleType ruleType = new RuleType();
        ruleType.setExpression(expression);
        ruleType.setBrId(brId);
        ruleType.setNote(note);
        ruleType.setErrorType(type);
        ruleType.setMessage(errorMessage);
        ruleType.setLevel("LevelName");
        return ruleType;
    }

    public static CodeType getCodeType(String value, String listId) {
        CodeType codeType = new CodeType();
        codeType.setValue(value);
        codeType.setListId(listId);
        return codeType;
    }

    public static MeasureType getMeasureType(BigDecimal value, String unitCode) {
        MeasureType measureType = new MeasureType();
        measureType.setValue(value);
        measureType.setUnitCode(unitCode);
        return measureType;
    }

    public static IdType getIdType(String value, String schemeId) {
        IdType idType = new IdType();
        idType.setValue(value);
        idType.setSchemeId(schemeId);
        return idType;
    }

    public static NumericType getNumericType(BigDecimal value, String format) {
        NumericType numericType = new NumericType();
        numericType.setValue(value);
        numericType.setFormat(format);
        return numericType;
    }

    public static TextType getTextType(String value, String languageId, String languageLocaleId) {
        TextType textType = new TextType();
        textType.setValue(value);
        textType.setLanguageID(languageId);
        textType.setLanguageLocaleID(languageLocaleId);
        return textType;
    }

    public static List<FLUXLocation> createFluxLocationsWithPositionValue() {
        List<FLUXLocation> fluxLocations = new ArrayList<>(2);
        FLUXLocation fluxLocation = createFluxLocationWithTypeCodeValue("POSITION");
        fluxLocations.add(fluxLocation);
        fluxLocation = createFluxLocationWithTypeCodeValue("AREA");
        fluxLocations.add(fluxLocation);
        fluxLocation = createFluxLocationWithTypeCodeValue("TEST");
        fluxLocations.add(fluxLocation);
        return fluxLocations;
    }

    public static FLUXLocation createFluxLocationWithTypeCodeValue(String typeCodeValue) {
        FLUXLocation fluxLocation = new FLUXLocation();
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID("FLUX_LOCATION_TYPE");
        codeType.setValue(typeCodeValue);
        fluxLocation.setTypeCode(codeType);
        fluxLocation.setID(getIdTypeUNCEFACT("", "FARM"));
        return fluxLocation;
    }

    public static FACatch getFACatch(String typeCode,String speciesCode){
        FACatch faCatch = new FACatch();
        faCatch.setTypeCode(getCodeTypeUNCEFACT(typeCode, ""));
        faCatch.setSpeciesCode(getCodeTypeUNCEFACT(speciesCode, ""));
        return faCatch;
    }

    public static List<FACatch> getFACatchList() {
        List<FACatch> faCatches = new ArrayList<>(2);
        FACatch faCatch = new FACatch();

        faCatch.setTypeCode(getCodeTypeUNCEFACT("LOADED", ""));
        List<FLUXLocation> fluxLocations = new ArrayList<>();
        fluxLocations.add(createFluxLocationWithTypeCodeValue("AREA"));
        faCatch.setSpecifiedFLUXLocations(fluxLocations);
        faCatches.add(faCatch);

        FACatch allocatedQutaFaCatch = new FACatch();
        allocatedQutaFaCatch.setTypeCode(getCodeTypeUNCEFACT("ALLOCATED_TO_QUOTA", ""));
        faCatches.add(allocatedQutaFaCatch);

        FACatch bftFaCatch = new FACatch();
        bftFaCatch.setSpeciesCode(getCodeTypeUNCEFACT("BFT", ""));

        List<FLUXLocation> fluxLocationsBFT = new ArrayList<>();
        fluxLocationsBFT.add(createFluxLocationWithTypeCodeValue("LOCATION"));
        bftFaCatch.setDestinationFLUXLocations(fluxLocationsBFT);

        FACatch loadedBFT = new FACatch();
        loadedBFT.setTypeCode(getCodeTypeUNCEFACT("LOADED", ""));
        loadedBFT.setSpeciesCode(getCodeTypeUNCEFACT("BFT", ""));
        loadedBFT.setDestinationFLUXLocations(fluxLocationsBFT);
        fluxLocations.add(createFluxLocationWithTypeCodeValue("AREA"));
        loadedBFT.setSpecifiedFLUXLocations(fluxLocations);
        faCatches.add(loadedBFT);
        return faCatches;
    }

    public static un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType getCodeTypeUNCEFACT(String value, String listId) {
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType();
        codeType.setListID(listId);
        codeType.setValue(value);
        return codeType;
    }

    public static IDType getIdTypeUNCEFACT(String value, String schemeId) {
        IDType idType = new IDType();
        idType.setValue(value);
        idType.setSchemeID(schemeId);
        return idType;
    }

    public static FLAPDocument getFLAPDocument() {
        FLAPDocument flapDocument = new FLAPDocument();
        flapDocument.setID(getIdTypeUNCEFACT("value", "FLAP_DOCUMENT_ID"));
        return flapDocument;
    }

    public static FishingActivity getFishingActivity() {
        FishingActivity fishingActivity = new FishingActivity();
        fishingActivity.setRelatedVesselTransportMeans(Collections.singletonList(getVesselTransportMeans()));
        fishingActivity.setSpecifiedFACatches(getFACatchList());
        return fishingActivity;
    }

    public static VesselTransportMeans getVesselTransportMeans() {
        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        vesselTransportMeans.setRoleCode(getCodeTypeUNCEFACT("FA_VESSEL_ROLE", "PARTICIPATING_VESSEL"));
        return vesselTransportMeans;
    }

    public static List<VesselTransportMeans> getListOfVesselTransportMeans() {
        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        vesselTransportMeans.setRoleCode(getCodeTypeUNCEFACT("FA_VESSEL_ROLE", "PARTICIPATING_VESSEL"));
        return Collections.singletonList(vesselTransportMeans);
    }

    public static ValidationResultDocument getValidationResultDocument() {
        ValidationResultDocument validationResultDocument = new ValidationResultDocument();
        validationResultDocument.setValidatorID(getIdTypeUNCEFACT("value", "SchemeId"));
        return validationResultDocument;
    }

    public static FLUXLocation getFLUXLocation(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode, IDType id) {
        FLUXLocation fluxLocation = new FLUXLocation();
        fluxLocation.setTypeCode(typeCode);
        fluxLocation.setID(id);
        return fluxLocation;
    }

    public static List<GearCharacteristic> getGearCharacteristics() {
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

    public static List<FishingGearTypeCharacteristic> getFishingGearTypeCharacteristics() {
        List<FishingGearTypeCharacteristic> fishingGearTypeCharacteristics = new ArrayList<>();
        List<GearCharacteristic> gearCharacteristics = getGearCharacteristics();
        for (GearCharacteristic gearCharacteristic : gearCharacteristics) {
            FishingGearTypeCharacteristic fishingGearTypeCharacteristic = new FishingGearTypeCharacteristic();
            fishingGearTypeCharacteristic.setMandatory(true);
            fishingGearTypeCharacteristic.setFishingGearCharacteristicCode(gearCharacteristic.getTypeCode().getValue());
            fishingGearTypeCharacteristic.setFishingGearTypeCode("PS");
            fishingGearTypeCharacteristics.add(fishingGearTypeCharacteristic);
        }
        return fishingGearTypeCharacteristics;
    }

    public static ContactParty getContactParty(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType roleCode,StructuredAddress structuredAddress){
        ContactParty contactParty = new ContactParty();
        contactParty.setRoleCodes(Collections.singletonList(roleCode));
        contactParty.setSpecifiedStructuredAddresses(Collections.singletonList(structuredAddress));
        return contactParty;
    }

    public static StructuredAddress getStructuredAddress(){
        return new StructuredAddress();
    }
}