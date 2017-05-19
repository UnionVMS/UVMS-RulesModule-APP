package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesFishingActivityFact extends AbstractFact {

    private List<IDType> ids;
    private eu.europa.ec.fisheries.schema.sales.CodeType typeCode;
    private DateTimeType occurrenceDateTime;
    private eu.europa.ec.fisheries.schema.sales.CodeType reasonCode;
    private eu.europa.ec.fisheries.schema.sales.CodeType vesselRelatedActivityCode;
    private eu.europa.ec.fisheries.schema.sales.CodeType fisheryTypeCode;
    private eu.europa.ec.fisheries.schema.sales.CodeType speciesTargetCode;
    private QuantityType operationsQuantity;
    private MeasureType fishingDurationMeasure;
    private List<FACatchType> specifiedFACatches;
    private List<FLUXLocationType> relatedFLUXLocations;
    private List<GearProblemType> specifiedGearProblems;
    private List<FLUXCharacteristicType> specifiedFLUXCharacteristics;
    private List<FishingGearType> specifiedFishingGears;
    private VesselStorageCharacteristicType sourceVesselStorageCharacteristic;
    private VesselStorageCharacteristicType destinationVesselStorageCharacteristic;
    private List<FishingActivityType> relatedFishingActivities;
    private List<FLAPDocumentType> specifiedFLAPDocuments;
    private List<DelimitedPeriodType> specifiedDelimitedPeriods;
    private FishingTripType specifiedFishingTrip;
    private List<VesselTransportMeansType> relatedVesselTransportMeans;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FISHING_ACTIVITY;
    }
}
