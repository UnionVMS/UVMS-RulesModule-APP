package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@ToString
public class SalesFishingActivityFact extends SalesAbstractFact {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFishingActivityFact)) return false;
        SalesFishingActivityFact that = (SalesFishingActivityFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(occurrenceDateTime, that.occurrenceDateTime) &&
                Objects.equals(reasonCode, that.reasonCode) &&
                Objects.equals(vesselRelatedActivityCode, that.vesselRelatedActivityCode) &&
                Objects.equals(fisheryTypeCode, that.fisheryTypeCode) &&
                Objects.equals(speciesTargetCode, that.speciesTargetCode) &&
                Objects.equals(operationsQuantity, that.operationsQuantity) &&
                Objects.equals(fishingDurationMeasure, that.fishingDurationMeasure) &&
                Objects.equals(specifiedFACatches, that.specifiedFACatches) &&
                Objects.equals(relatedFLUXLocations, that.relatedFLUXLocations) &&
                Objects.equals(specifiedGearProblems, that.specifiedGearProblems) &&
                Objects.equals(specifiedFLUXCharacteristics, that.specifiedFLUXCharacteristics) &&
                Objects.equals(specifiedFishingGears, that.specifiedFishingGears) &&
                Objects.equals(sourceVesselStorageCharacteristic, that.sourceVesselStorageCharacteristic) &&
                Objects.equals(destinationVesselStorageCharacteristic, that.destinationVesselStorageCharacteristic) &&
                Objects.equals(relatedFishingActivities, that.relatedFishingActivities) &&
                Objects.equals(specifiedFLAPDocuments, that.specifiedFLAPDocuments) &&
                Objects.equals(specifiedDelimitedPeriods, that.specifiedDelimitedPeriods) &&
                Objects.equals(specifiedFishingTrip, that.specifiedFishingTrip) &&
                Objects.equals(relatedVesselTransportMeans, that.relatedVesselTransportMeans) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage) &&
                Objects.equals(messageDataFlow, that.messageDataFlow) &&
                Objects.equals(creationJavaDateOfMessage, that.creationJavaDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, typeCode, occurrenceDateTime, reasonCode, vesselRelatedActivityCode, fisheryTypeCode, speciesTargetCode, operationsQuantity, fishingDurationMeasure, specifiedFACatches, relatedFLUXLocations, specifiedGearProblems, specifiedFLUXCharacteristics, specifiedFishingGears, sourceVesselStorageCharacteristic, destinationVesselStorageCharacteristic, relatedFishingActivities, specifiedFLAPDocuments, specifiedDelimitedPeriods, specifiedFishingTrip, relatedVesselTransportMeans, creationDateOfMessage, messageDataFlow, creationJavaDateOfMessage);
    }

    public boolean isRelatedFLUXLocationsEmptyOrTypeLocation() {
        if (isEmpty(relatedFLUXLocations)){
            return true;
        }

        for (FLUXLocationType location: relatedFLUXLocations) {
            if (location != null && location.getTypeCode() != null && !location.getTypeCode().getValue().equals("LOCATION")){
                return false;
            }
        }

        return true;
    }
}
