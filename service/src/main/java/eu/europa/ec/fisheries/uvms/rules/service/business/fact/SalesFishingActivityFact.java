package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFishingActivityFact extends AbstractFact {

    private List<IdType> ids;
    private CodeType typeCode;
    private DateTimeType occurrenceDateTime;
    private CodeType reasonCode;
    private CodeType vesselRelatedActivityCode;
    private CodeType fisheryTypeCode;
    private CodeType speciesTargetCode;
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

    public List<IdType> getIDS() {
        return this.ids;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public DateTimeType getOccurrenceDateTime() {
        return this.occurrenceDateTime;
    }

    public CodeType getReasonCode() {
        return this.reasonCode;
    }

    public CodeType getVesselRelatedActivityCode() {
        return this.vesselRelatedActivityCode;
    }

    public CodeType getFisheryTypeCode() {
        return this.fisheryTypeCode;
    }

    public CodeType getSpeciesTargetCode() {
        return this.speciesTargetCode;
    }

    public QuantityType getOperationsQuantity() {
        return this.operationsQuantity;
    }

    public MeasureType getFishingDurationMeasure() {
        return this.fishingDurationMeasure;
    }

    public List<FACatchType> getSpecifiedFACatches() {
        return this.specifiedFACatches;
    }

    public List<FLUXLocationType> getRelatedFLUXLocations() {
        return this.relatedFLUXLocations;
    }

    public List<GearProblemType> getSpecifiedGearProblems() {
        return this.specifiedGearProblems;
    }

    public List<FLUXCharacteristicType> getSpecifiedFLUXCharacteristics() {
        return this.specifiedFLUXCharacteristics;
    }

    public List<FishingGearType> getSpecifiedFishingGears() {
        return this.specifiedFishingGears;
    }

    public VesselStorageCharacteristicType getSourceVesselStorageCharacteristic() {
        return this.sourceVesselStorageCharacteristic;
    }

    public VesselStorageCharacteristicType getDestinationVesselStorageCharacteristic() {
        return this.destinationVesselStorageCharacteristic;
    }

    public List<FishingActivityType> getRelatedFishingActivities() {
        return this.relatedFishingActivities;
    }

    public List<FLAPDocumentType> getSpecifiedFLAPDocuments() {
        return this.specifiedFLAPDocuments;
    }

    public List<DelimitedPeriodType> getSpecifiedDelimitedPeriods() {
        return this.specifiedDelimitedPeriods;
    }

    public FishingTripType getSpecifiedFishingTrip() {
        return this.specifiedFishingTrip;
    }

    public List<VesselTransportMeansType> getRelatedVesselTransportMeans() {
        return this.relatedVesselTransportMeans;
    }

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setOccurrenceDateTime(DateTimeType occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
    }

    public void setReasonCode(CodeType reasonCode) {
        this.reasonCode = reasonCode;
    }

    public void setVesselRelatedActivityCode(CodeType vesselRelatedActivityCode) {
        this.vesselRelatedActivityCode = vesselRelatedActivityCode;
    }

    public void setFisheryTypeCode(CodeType fisheryTypeCode) {
        this.fisheryTypeCode = fisheryTypeCode;
    }

    public void setSpeciesTargetCode(CodeType speciesTargetCode) {
        this.speciesTargetCode = speciesTargetCode;
    }

    public void setOperationsQuantity(QuantityType operationsQuantity) {
        this.operationsQuantity = operationsQuantity;
    }

    public void setFishingDurationMeasure(MeasureType fishingDurationMeasure) {
        this.fishingDurationMeasure = fishingDurationMeasure;
    }

    public void setSpecifiedFACatches(List<FACatchType> specifiedFACatches) {
        this.specifiedFACatches = specifiedFACatches;
    }

    public void setRelatedFLUXLocations(List<FLUXLocationType> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public void setSpecifiedGearProblems(List<GearProblemType> specifiedGearProblems) {
        this.specifiedGearProblems = specifiedGearProblems;
    }

    public void setSpecifiedFLUXCharacteristics(List<FLUXCharacteristicType> specifiedFLUXCharacteristics) {
        this.specifiedFLUXCharacteristics = specifiedFLUXCharacteristics;
    }

    public void setSpecifiedFishingGears(List<FishingGearType> specifiedFishingGears) {
        this.specifiedFishingGears = specifiedFishingGears;
    }

    public void setSourceVesselStorageCharacteristic(VesselStorageCharacteristicType sourceVesselStorageCharacteristic) {
        this.sourceVesselStorageCharacteristic = sourceVesselStorageCharacteristic;
    }

    public void setDestinationVesselStorageCharacteristic(VesselStorageCharacteristicType destinationVesselStorageCharacteristic) {
        this.destinationVesselStorageCharacteristic = destinationVesselStorageCharacteristic;
    }

    public void setRelatedFishingActivities(List<FishingActivityType> relatedFishingActivities) {
        this.relatedFishingActivities = relatedFishingActivities;
    }

    public void setSpecifiedFLAPDocuments(List<FLAPDocumentType> specifiedFLAPDocuments) {
        this.specifiedFLAPDocuments = specifiedFLAPDocuments;
    }

    public void setSpecifiedDelimitedPeriods(List<DelimitedPeriodType> specifiedDelimitedPeriods) {
        this.specifiedDelimitedPeriods = specifiedDelimitedPeriods;
    }

    public void setSpecifiedFishingTrip(FishingTripType specifiedFishingTrip) {
        this.specifiedFishingTrip = specifiedFishingTrip;
    }

    public void setRelatedVesselTransportMeans(List<VesselTransportMeansType> relatedVesselTransportMeans) {
        this.relatedVesselTransportMeans = relatedVesselTransportMeans;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFishingActivityFact)) return false;
        final SalesFishingActivityFact other = (SalesFishingActivityFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIDS();
        final Object other$ids = other.getIDS();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$typeCode = this.getTypeCode();
        final Object other$typeCode = other.getTypeCode();
        if (this$typeCode == null ? other$typeCode != null : !this$typeCode.equals(other$typeCode)) return false;
        final Object this$occurrenceDateTime = this.getOccurrenceDateTime();
        final Object other$occurrenceDateTime = other.getOccurrenceDateTime();
        if (this$occurrenceDateTime == null ? other$occurrenceDateTime != null : !this$occurrenceDateTime.equals(other$occurrenceDateTime))
            return false;
        final Object this$reasonCode = this.getReasonCode();
        final Object other$reasonCode = other.getReasonCode();
        if (this$reasonCode == null ? other$reasonCode != null : !this$reasonCode.equals(other$reasonCode))
            return false;
        final Object this$vesselRelatedActivityCode = this.getVesselRelatedActivityCode();
        final Object other$vesselRelatedActivityCode = other.getVesselRelatedActivityCode();
        if (this$vesselRelatedActivityCode == null ? other$vesselRelatedActivityCode != null : !this$vesselRelatedActivityCode.equals(other$vesselRelatedActivityCode))
            return false;
        final Object this$fisheryTypeCode = this.getFisheryTypeCode();
        final Object other$fisheryTypeCode = other.getFisheryTypeCode();
        if (this$fisheryTypeCode == null ? other$fisheryTypeCode != null : !this$fisheryTypeCode.equals(other$fisheryTypeCode))
            return false;
        final Object this$speciesTargetCode = this.getSpeciesTargetCode();
        final Object other$speciesTargetCode = other.getSpeciesTargetCode();
        if (this$speciesTargetCode == null ? other$speciesTargetCode != null : !this$speciesTargetCode.equals(other$speciesTargetCode))
            return false;
        final Object this$operationsQuantity = this.getOperationsQuantity();
        final Object other$operationsQuantity = other.getOperationsQuantity();
        if (this$operationsQuantity == null ? other$operationsQuantity != null : !this$operationsQuantity.equals(other$operationsQuantity))
            return false;
        final Object this$fishingDurationMeasure = this.getFishingDurationMeasure();
        final Object other$fishingDurationMeasure = other.getFishingDurationMeasure();
        if (this$fishingDurationMeasure == null ? other$fishingDurationMeasure != null : !this$fishingDurationMeasure.equals(other$fishingDurationMeasure))
            return false;
        final Object this$specifiedFACatches = this.getSpecifiedFACatches();
        final Object other$specifiedFACatches = other.getSpecifiedFACatches();
        if (this$specifiedFACatches == null ? other$specifiedFACatches != null : !this$specifiedFACatches.equals(other$specifiedFACatches))
            return false;
        final Object this$relatedFLUXLocations = this.getRelatedFLUXLocations();
        final Object other$relatedFLUXLocations = other.getRelatedFLUXLocations();
        if (this$relatedFLUXLocations == null ? other$relatedFLUXLocations != null : !this$relatedFLUXLocations.equals(other$relatedFLUXLocations))
            return false;
        final Object this$specifiedGearProblems = this.getSpecifiedGearProblems();
        final Object other$specifiedGearProblems = other.getSpecifiedGearProblems();
        if (this$specifiedGearProblems == null ? other$specifiedGearProblems != null : !this$specifiedGearProblems.equals(other$specifiedGearProblems))
            return false;
        final Object this$specifiedFLUXCharacteristics = this.getSpecifiedFLUXCharacteristics();
        final Object other$specifiedFLUXCharacteristics = other.getSpecifiedFLUXCharacteristics();
        if (this$specifiedFLUXCharacteristics == null ? other$specifiedFLUXCharacteristics != null : !this$specifiedFLUXCharacteristics.equals(other$specifiedFLUXCharacteristics))
            return false;
        final Object this$specifiedFishingGears = this.getSpecifiedFishingGears();
        final Object other$specifiedFishingGears = other.getSpecifiedFishingGears();
        if (this$specifiedFishingGears == null ? other$specifiedFishingGears != null : !this$specifiedFishingGears.equals(other$specifiedFishingGears))
            return false;
        final Object this$sourceVesselStorageCharacteristic = this.getSourceVesselStorageCharacteristic();
        final Object other$sourceVesselStorageCharacteristic = other.getSourceVesselStorageCharacteristic();
        if (this$sourceVesselStorageCharacteristic == null ? other$sourceVesselStorageCharacteristic != null : !this$sourceVesselStorageCharacteristic.equals(other$sourceVesselStorageCharacteristic))
            return false;
        final Object this$destinationVesselStorageCharacteristic = this.getDestinationVesselStorageCharacteristic();
        final Object other$destinationVesselStorageCharacteristic = other.getDestinationVesselStorageCharacteristic();
        if (this$destinationVesselStorageCharacteristic == null ? other$destinationVesselStorageCharacteristic != null : !this$destinationVesselStorageCharacteristic.equals(other$destinationVesselStorageCharacteristic))
            return false;
        final Object this$relatedFishingActivities = this.getRelatedFishingActivities();
        final Object other$relatedFishingActivities = other.getRelatedFishingActivities();
        if (this$relatedFishingActivities == null ? other$relatedFishingActivities != null : !this$relatedFishingActivities.equals(other$relatedFishingActivities))
            return false;
        final Object this$specifiedFLAPDocuments = this.getSpecifiedFLAPDocuments();
        final Object other$specifiedFLAPDocuments = other.getSpecifiedFLAPDocuments();
        if (this$specifiedFLAPDocuments == null ? other$specifiedFLAPDocuments != null : !this$specifiedFLAPDocuments.equals(other$specifiedFLAPDocuments))
            return false;
        final Object this$specifiedDelimitedPeriods = this.getSpecifiedDelimitedPeriods();
        final Object other$specifiedDelimitedPeriods = other.getSpecifiedDelimitedPeriods();
        if (this$specifiedDelimitedPeriods == null ? other$specifiedDelimitedPeriods != null : !this$specifiedDelimitedPeriods.equals(other$specifiedDelimitedPeriods))
            return false;
        final Object this$specifiedFishingTrip = this.getSpecifiedFishingTrip();
        final Object other$specifiedFishingTrip = other.getSpecifiedFishingTrip();
        if (this$specifiedFishingTrip == null ? other$specifiedFishingTrip != null : !this$specifiedFishingTrip.equals(other$specifiedFishingTrip))
            return false;
        final Object this$relatedVesselTransportMeans = this.getRelatedVesselTransportMeans();
        final Object other$relatedVesselTransportMeans = other.getRelatedVesselTransportMeans();
        if (this$relatedVesselTransportMeans == null ? other$relatedVesselTransportMeans != null : !this$relatedVesselTransportMeans.equals(other$relatedVesselTransportMeans))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ids = this.getIDS();
        result = result * PRIME + ($ids == null ? 43 : $ids.hashCode());
        final Object $typeCode = this.getTypeCode();
        result = result * PRIME + ($typeCode == null ? 43 : $typeCode.hashCode());
        final Object $occurrenceDateTime = this.getOccurrenceDateTime();
        result = result * PRIME + ($occurrenceDateTime == null ? 43 : $occurrenceDateTime.hashCode());
        final Object $reasonCode = this.getReasonCode();
        result = result * PRIME + ($reasonCode == null ? 43 : $reasonCode.hashCode());
        final Object $vesselRelatedActivityCode = this.getVesselRelatedActivityCode();
        result = result * PRIME + ($vesselRelatedActivityCode == null ? 43 : $vesselRelatedActivityCode.hashCode());
        final Object $fisheryTypeCode = this.getFisheryTypeCode();
        result = result * PRIME + ($fisheryTypeCode == null ? 43 : $fisheryTypeCode.hashCode());
        final Object $speciesTargetCode = this.getSpeciesTargetCode();
        result = result * PRIME + ($speciesTargetCode == null ? 43 : $speciesTargetCode.hashCode());
        final Object $operationsQuantity = this.getOperationsQuantity();
        result = result * PRIME + ($operationsQuantity == null ? 43 : $operationsQuantity.hashCode());
        final Object $fishingDurationMeasure = this.getFishingDurationMeasure();
        result = result * PRIME + ($fishingDurationMeasure == null ? 43 : $fishingDurationMeasure.hashCode());
        final Object $specifiedFACatches = this.getSpecifiedFACatches();
        result = result * PRIME + ($specifiedFACatches == null ? 43 : $specifiedFACatches.hashCode());
        final Object $relatedFLUXLocations = this.getRelatedFLUXLocations();
        result = result * PRIME + ($relatedFLUXLocations == null ? 43 : $relatedFLUXLocations.hashCode());
        final Object $specifiedGearProblems = this.getSpecifiedGearProblems();
        result = result * PRIME + ($specifiedGearProblems == null ? 43 : $specifiedGearProblems.hashCode());
        final Object $specifiedFLUXCharacteristics = this.getSpecifiedFLUXCharacteristics();
        result = result * PRIME + ($specifiedFLUXCharacteristics == null ? 43 : $specifiedFLUXCharacteristics.hashCode());
        final Object $specifiedFishingGears = this.getSpecifiedFishingGears();
        result = result * PRIME + ($specifiedFishingGears == null ? 43 : $specifiedFishingGears.hashCode());
        final Object $sourceVesselStorageCharacteristic = this.getSourceVesselStorageCharacteristic();
        result = result * PRIME + ($sourceVesselStorageCharacteristic == null ? 43 : $sourceVesselStorageCharacteristic.hashCode());
        final Object $destinationVesselStorageCharacteristic = this.getDestinationVesselStorageCharacteristic();
        result = result * PRIME + ($destinationVesselStorageCharacteristic == null ? 43 : $destinationVesselStorageCharacteristic.hashCode());
        final Object $relatedFishingActivities = this.getRelatedFishingActivities();
        result = result * PRIME + ($relatedFishingActivities == null ? 43 : $relatedFishingActivities.hashCode());
        final Object $specifiedFLAPDocuments = this.getSpecifiedFLAPDocuments();
        result = result * PRIME + ($specifiedFLAPDocuments == null ? 43 : $specifiedFLAPDocuments.hashCode());
        final Object $specifiedDelimitedPeriods = this.getSpecifiedDelimitedPeriods();
        result = result * PRIME + ($specifiedDelimitedPeriods == null ? 43 : $specifiedDelimitedPeriods.hashCode());
        final Object $specifiedFishingTrip = this.getSpecifiedFishingTrip();
        result = result * PRIME + ($specifiedFishingTrip == null ? 43 : $specifiedFishingTrip.hashCode());
        final Object $relatedVesselTransportMeans = this.getRelatedVesselTransportMeans();
        result = result * PRIME + ($relatedVesselTransportMeans == null ? 43 : $relatedVesselTransportMeans.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFishingActivityFact;
    }
}
