package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.rule.v1.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import un.unece.uncefact.data.standard.mdr.communication.*;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;

import java.math.*;
import java.util.*;

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

        return fluxLocation;
    }
}
