package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselGeographicalCoordinateType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.DateTimeType;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

@Data
public class MovementSpecifiedVesselPositionEventFact extends AbstractFact {
    private CodeType typeCode;
    private DateTime creationDateTime;
    private BigDecimal speedValue;
    private BigDecimal courseValue;
    private String creationDateTimeString;
    private BigDecimal latitudeMeasure;
    private BigDecimal longitudeMeasure;

    public boolean hasValidSpeedValue(BigDecimal speedValue) {
        return speedValue.signum() > 0 && speedValue.scale() < 3;
    }

    public boolean hasValidCourseValue(BigDecimal courseValue) {
        return courseValue.compareTo(new BigDecimal(360)) <= 0 && courseValue.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean hasValidTypeCodeValueListID(CodeType typeCode) {
        if(typeCode == null || typeCode.getListID() == null){
            return false;
        }
        return typeCode.getListID().equals("FLUX_VESSEL_POSITION_TYPE");
    }

    public boolean hasValidCreationDateTime(String creationDateTimeString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true)
                .optionalEnd()
                .appendLiteral('Z')//timezone must always be utc, thus the literal Z
                .parseStrict().toFormatter();
        try {
            formatter.parse(creationDateTimeString);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean hasValidLatitudeMeasure(BigDecimal latitudeMeasure) {
        return latitudeMeasure.compareTo(new BigDecimal(90)) <= 0 && latitudeMeasure.compareTo(new BigDecimal(-90)) >= 0 && latitudeMeasure.scale() >= 3 && latitudeMeasure.scale() <= 6;
    }

    public boolean hasValidLongitudeMeasure(BigDecimal longitudeMeasure) {
        return  longitudeMeasure.compareTo(new BigDecimal(180)) <= 0 && longitudeMeasure.compareTo(new BigDecimal(-180)) >= 0 && longitudeMeasure.scale() >= 3 && longitudeMeasure.scale() <= 6;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.MOVEMENT_SPECIFIED_VESSEL_POSITION_EVENT;
    }
}