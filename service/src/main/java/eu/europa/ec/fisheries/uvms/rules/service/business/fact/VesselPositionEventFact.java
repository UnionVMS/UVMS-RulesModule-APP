package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselGeographicalCoordinate;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;


@Data
@EqualsAndHashCode(callSuper = false)
public class VesselPositionEventFact extends AbstractFact {

    private CodeType typeCode;
    private DateTimeType obtainedOccurrenceDateTime;
    private MeasureType speedValueMeasure;
    private MeasureType courseValueMeasure;
    private CodeType activityTypeCode;
    private VesselGeographicalCoordinate specifiedVesselGeographicalCoordinate;

    public VesselPositionEventFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_VESSEL_POSITION_EVENT;
    }


}