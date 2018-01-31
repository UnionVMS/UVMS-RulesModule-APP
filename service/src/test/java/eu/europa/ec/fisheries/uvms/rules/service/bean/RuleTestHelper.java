package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.entity.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.entity.FishingGearTypeCharacteristicId;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FactConstants;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sanera on 10/05/2017.
 */
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

    public static List<ObjectRepresentation> getObjectRepresentationForFA_CATCH() {

        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        objectRepresentations.add(getObjectRepresentation("code", "ONBOARD", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "KEPT_IN_NET", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "TAKEN_ONBOARD", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "RELEASED", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "DISCARDED", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "DEMINIMIS", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "UNLOADED", "string"));

        return objectRepresentations;
    }


    public static List<ObjectRepresentation> getObjectRepresentationForGEAR_TYPE_CODES() {

        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        objectRepresentations.add(getObjectRepresentation("code", "PS1", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "LA", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "SB", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "SDN", "string"));
        objectRepresentations.add(getObjectRepresentation("code", "PTB", "string"));

        return objectRepresentations;
    }

    public static List<ObjectRepresentation> getObjectRepresentationForGEAR_CHARACTERISTIC() {

        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        objectRepresentations.add(getObjectRepresentationForGearCharacteristic());
        objectRepresentations.add(getObjectRepresentation("code", "KEPT_IN_NET", "string"));


        return objectRepresentations;
    }

    public static List<ObjectRepresentation> getObjectRepresentationForLOCATION() {

        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        objectRepresentations.add(getObjectRepresentationForGearCharacteristic());
        objectRepresentations.add(getObjectRepresentation("code", "BEOST", "string"));


        return objectRepresentations;
    }

    public static List<ObjectRepresentation> getObjectRepresentationForTERRITORY() {

        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        objectRepresentations.add(getObjectRepresentationForGearCharacteristic());
        objectRepresentations.add(getObjectRepresentation("code", "BEL", "string"));


        return objectRepresentations;
    }

    public static List<ObjectRepresentation> getObjectRepresentationForFLUX_SALES_QUERY_PARAM_ROLE() {

        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        objectRepresentations.add(getObjectRepresentationForGearCharacteristic());
        objectRepresentations.add(getObjectRepresentation("code", "FLAG", "string"));


        return objectRepresentations;
    }

    public static ObjectRepresentation getObjectRepresentationForGearCharacteristic() {

        List<ColumnDataType> columnDataTypes = new ArrayList<>();

        columnDataTypes.add(new ColumnDataType("code", "ME", "String"));
        columnDataTypes.add(new ColumnDataType("dataType", "MEASURE", "String"));

        return new ObjectRepresentation(columnDataTypes);
    }

    public static ObjectRepresentation getObjectRepresentation(String columnName, String columnValue, String columnDataType) {

        List<ColumnDataType> columnDataTypes = new ArrayList<>();

        columnDataTypes.add(new ColumnDataType(columnName, columnValue, columnDataType));

        return new ObjectRepresentation(columnDataTypes);
    }


    public static ColumnDataType getColumnDataType(String columnName, String columnValue, String columnDataType) {
        return new ColumnDataType(columnName, columnValue, columnDataType);
    }

    public static List<ObjectRepresentation> getObjectRepresentationForVESSEL_STORAGE_CHARACTERISTIC() {
        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        objectRepresentations.add(getObjectRepresentation("code", "OTR", "String"));
        objectRepresentations.add(getObjectRepresentation("code", "OSS", "String"));
        objectRepresentations.add(getObjectRepresentation("code", "NCC", "String"));
        objectRepresentations.add(getObjectRepresentation("code", "OHL", "String"));

        return objectRepresentations;
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
        fishingActivity.setRelatedVesselTransportMeans(Arrays.asList(getVesselTransportMeans()));
        fishingActivity.setSpecifiedFACatches(getFACatchList());
        return fishingActivity;
    }

    public static VesselTransportMeans getVesselTransportMeans() {
        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();

        vesselTransportMeans.setRoleCode(getCodeTypeUNCEFACT("FA_VESSEL_ROLE", "PARTICIPATING_VESSEL"));

        return vesselTransportMeans;
    }


    public static ValidationResultDocument getValidationResultDocument() {
        ValidationResultDocument validationResultDocument = new ValidationResultDocument();
        validationResultDocument.setValidatorID(getIdTypeUNCEFACT("value", "SchemeId"));

        return validationResultDocument;


    }

    public static FLUXCharacteristic getFLUXCharacteristic() {
        FLUXCharacteristic fluxCharacteristic = new FLUXCharacteristic();
        fluxCharacteristic.setTypeCode(getCodeTypeUNCEFACT("DESTINATION_LOCATION", ""));

        List<FLUXLocation> fluxLocations = new ArrayList<>();
        fluxLocations.add(getFLUXLocation(getCodeTypeUNCEFACT("LOCATION", ""), getIdTypeUNCEFACT("", "LOCATION")));
        fluxLocations.add(getFLUXLocation(getCodeTypeUNCEFACT("LOCATION", ""), getIdTypeUNCEFACT("", "FARM")));

        fluxCharacteristic.setSpecifiedFLUXLocations(fluxLocations);
        return fluxCharacteristic;
    }

    public static FLUXLocation getFLUXLocation(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode, IDType id) {
        FLUXLocation fluxLocation = new FLUXLocation();
        fluxLocation.setTypeCode(typeCode);
        fluxLocation.setID(id);
        return fluxLocation;
    }

    public static VesselTransportMeans getVesselTransportMeans(String schemeId, String value) {
        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        IDType id = new IDType();
        id.setSchemeID(schemeId);
        id.setValue(value);
        vesselTransportMeans.setIDS(Arrays.asList(id));

        return vesselTransportMeans;
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
            FishingGearTypeCharacteristicId fishingGearTypeCharacteristicId = new FishingGearTypeCharacteristicId();
            fishingGearTypeCharacteristicId.setFishingGearTypeCode("PS");
            fishingGearTypeCharacteristicId.setFishingGearCharacteristicCode(gearCharacteristic.getTypeCode().getValue());
            fishingGearTypeCharacteristic.setId(fishingGearTypeCharacteristicId);
            fishingGearTypeCharacteristic.setMandatory(true);

            fishingGearTypeCharacteristics.add(fishingGearTypeCharacteristic);
        }

        return fishingGearTypeCharacteristics;
    }

    public static ContactParty getContactParty(un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType roleCode,StructuredAddress structuredAddress){
        ContactParty contactParty = new ContactParty();
        contactParty.setRoleCodes(Arrays.asList(roleCode));
        contactParty.setSpecifiedStructuredAddresses(Arrays.asList(structuredAddress));
        return contactParty;
    }

    public static StructuredAddress getStructuredAddress(){
        StructuredAddress structuredAddress = new StructuredAddress();

        return structuredAddress;
    }


}
