package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.List;
import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AAPProcessType;
import eu.europa.ec.fisheries.schema.sales.FLUXLocationType;
import eu.europa.ec.fisheries.schema.sales.FishingActivityType;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.schema.sales.QuantityType;
import eu.europa.ec.fisheries.schema.sales.SalesPriceType;
import eu.europa.ec.fisheries.schema.sales.SizeDistributionType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

public class SalesAAPProductFact extends AbstractFact {

    private CodeType speciesCode;
    private QuantityType unitQuantity;
    private eu.europa.ec.fisheries.schema.sales.MeasureType weightMeasure;
    private CodeType weighingMeansCode;
    private CodeType usageCode;
    private QuantityType packagingUnitQuantity;
    private CodeType packagingTypeCode;
    private MeasureType packagingUnitAverageWeightMeasure;
    private List<AAPProcessType> appliedAAPProcesses;
    private SalesPriceType totalSalesPrice;
    private SizeDistributionType specifiedSizeDistribution;
    private List<FLUXLocationType> originFLUXLocations;
    private FishingActivityType originFishingActivity;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_BATCH;
    }

    public CodeType getSpeciesCode() {
        return this.speciesCode;
    }

    public QuantityType getUnitQuantity() {
        return this.unitQuantity;
    }

    public MeasureType getWeightMeasure() {
        return this.weightMeasure;
    }

    public CodeType getWeighingMeansCode() {
        return this.weighingMeansCode;
    }

    public CodeType getUsageCode() {
        return this.usageCode;
    }

    public QuantityType getPackagingUnitQuantity() {
        return this.packagingUnitQuantity;
    }

    public CodeType getPackagingTypeCode() {
        return this.packagingTypeCode;
    }

    public MeasureType getPackagingUnitAverageWeightMeasure() {
        return this.packagingUnitAverageWeightMeasure;
    }

    public List<AAPProcessType> getAppliedAAPProcesses() {
        return this.appliedAAPProcesses;
    }

    public SalesPriceType getTotalSalesPrice() {
        return this.totalSalesPrice;
    }

    public SizeDistributionType getSpecifiedSizeDistribution() {
        return this.specifiedSizeDistribution;
    }

    public List<FLUXLocationType> getOriginFLUXLocations() {
        return this.originFLUXLocations;
    }

    public FishingActivityType getOriginFishingActivity() {
        return this.originFishingActivity;
    }

    public void setSpeciesCode(CodeType speciesCode) {
        this.speciesCode = speciesCode;
    }

    public void setUnitQuantity(QuantityType unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public void setWeightMeasure(MeasureType weightMeasure) {
        this.weightMeasure = weightMeasure;
    }

    public void setWeighingMeansCode(CodeType weighingMeansCode) {
        this.weighingMeansCode = weighingMeansCode;
    }

    public void setUsageCode(CodeType usageCode) {
        this.usageCode = usageCode;
    }

    public void setPackagingUnitQuantity(QuantityType packagingUnitQuantity) {
        this.packagingUnitQuantity = packagingUnitQuantity;
    }

    public void setPackagingTypeCode(CodeType packagingTypeCode) {
        this.packagingTypeCode = packagingTypeCode;
    }

    public void setPackagingUnitAverageWeightMeasure(MeasureType packagingUnitAverageWeightMeasure) {
        this.packagingUnitAverageWeightMeasure = packagingUnitAverageWeightMeasure;
    }

    public void setAppliedAAPProcesses(List<AAPProcessType> appliedAAPProcesses) {
        this.appliedAAPProcesses = appliedAAPProcesses;
    }

    public void setTotalSalesPrice(SalesPriceType totalSalesPrice) {
        this.totalSalesPrice = totalSalesPrice;
    }

    public void setSpecifiedSizeDistribution(SizeDistributionType specifiedSizeDistribution) {
        this.specifiedSizeDistribution = specifiedSizeDistribution;
    }

    public void setOriginFLUXLocations(List<FLUXLocationType> originFLUXLocations) {
        this.originFLUXLocations = originFLUXLocations;
    }

    public void setOriginFishingActivity(FishingActivityType originFishingActivity) {
        this.originFishingActivity = originFishingActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesAAPProductFact)) return false;
        SalesAAPProductFact that = (SalesAAPProductFact) o;
        return Objects.equals(speciesCode, that.speciesCode) &&
                Objects.equals(unitQuantity, that.unitQuantity) &&
                Objects.equals(weightMeasure, that.weightMeasure) &&
                Objects.equals(weighingMeansCode, that.weighingMeansCode) &&
                Objects.equals(usageCode, that.usageCode) &&
                Objects.equals(packagingUnitQuantity, that.packagingUnitQuantity) &&
                Objects.equals(packagingTypeCode, that.packagingTypeCode) &&
                Objects.equals(packagingUnitAverageWeightMeasure, that.packagingUnitAverageWeightMeasure) &&
                Objects.equals(appliedAAPProcesses, that.appliedAAPProcesses) &&
                Objects.equals(totalSalesPrice, that.totalSalesPrice) &&
                Objects.equals(specifiedSizeDistribution, that.specifiedSizeDistribution) &&
                Objects.equals(originFLUXLocations, that.originFLUXLocations) &&
                Objects.equals(originFishingActivity, that.originFishingActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speciesCode, unitQuantity, weightMeasure, weighingMeansCode, usageCode, packagingUnitQuantity, packagingTypeCode, packagingUnitAverageWeightMeasure, appliedAAPProcesses, totalSalesPrice, specifiedSizeDistribution, originFLUXLocations, originFishingActivity);
    }


    public boolean isInvalidUsageCode() {
        String[] validUsages = new String[10];
        validUsages[0] = "HCN";
        validUsages[1] = "HCN-INDIRECT";
        validUsages[2] = "IND";
        validUsages[3] = "BAI";
        validUsages[4] = "ANF";
        validUsages[5] = "WST";
        validUsages[6] = "WDR";
        validUsages[7] = "COV";
        validUsages[8] = "UNK";
        validUsages[9] = "STO";

        return valueContainsAny(usageCode, validUsages);
    }

}
