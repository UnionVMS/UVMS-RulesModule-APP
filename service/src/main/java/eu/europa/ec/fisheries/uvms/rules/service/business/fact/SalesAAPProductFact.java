package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesAAPProductFact)) return false;
        final SalesAAPProductFact other = (SalesAAPProductFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$speciesCode = this.getSpeciesCode();
        final Object other$speciesCode = other.getSpeciesCode();
        if (this$speciesCode == null ? other$speciesCode != null : !this$speciesCode.equals(other$speciesCode))
            return false;
        final Object this$unitQuantity = this.getUnitQuantity();
        final Object other$unitQuantity = other.getUnitQuantity();
        if (this$unitQuantity == null ? other$unitQuantity != null : !this$unitQuantity.equals(other$unitQuantity))
            return false;
        final Object this$weightMeasure = this.getWeightMeasure();
        final Object other$weightMeasure = other.getWeightMeasure();
        if (this$weightMeasure == null ? other$weightMeasure != null : !this$weightMeasure.equals(other$weightMeasure))
            return false;
        final Object this$weighingMeansCode = this.getWeighingMeansCode();
        final Object other$weighingMeansCode = other.getWeighingMeansCode();
        if (this$weighingMeansCode == null ? other$weighingMeansCode != null : !this$weighingMeansCode.equals(other$weighingMeansCode))
            return false;
        final Object this$usageCode = this.getUsageCode();
        final Object other$usageCode = other.getUsageCode();
        if (this$usageCode == null ? other$usageCode != null : !this$usageCode.equals(other$usageCode)) return false;
        final Object this$packagingUnitQuantity = this.getPackagingUnitQuantity();
        final Object other$packagingUnitQuantity = other.getPackagingUnitQuantity();
        if (this$packagingUnitQuantity == null ? other$packagingUnitQuantity != null : !this$packagingUnitQuantity.equals(other$packagingUnitQuantity))
            return false;
        final Object this$packagingTypeCode = this.getPackagingTypeCode();
        final Object other$packagingTypeCode = other.getPackagingTypeCode();
        if (this$packagingTypeCode == null ? other$packagingTypeCode != null : !this$packagingTypeCode.equals(other$packagingTypeCode))
            return false;
        final Object this$packagingUnitAverageWeightMeasure = this.getPackagingUnitAverageWeightMeasure();
        final Object other$packagingUnitAverageWeightMeasure = other.getPackagingUnitAverageWeightMeasure();
        if (this$packagingUnitAverageWeightMeasure == null ? other$packagingUnitAverageWeightMeasure != null : !this$packagingUnitAverageWeightMeasure.equals(other$packagingUnitAverageWeightMeasure))
            return false;
        final Object this$appliedAAPProcesses = this.getAppliedAAPProcesses();
        final Object other$appliedAAPProcesses = other.getAppliedAAPProcesses();
        if (this$appliedAAPProcesses == null ? other$appliedAAPProcesses != null : !this$appliedAAPProcesses.equals(other$appliedAAPProcesses))
            return false;
        final Object this$totalSalesPrice = this.getTotalSalesPrice();
        final Object other$totalSalesPrice = other.getTotalSalesPrice();
        if (this$totalSalesPrice == null ? other$totalSalesPrice != null : !this$totalSalesPrice.equals(other$totalSalesPrice))
            return false;
        final Object this$specifiedSizeDistribution = this.getSpecifiedSizeDistribution();
        final Object other$specifiedSizeDistribution = other.getSpecifiedSizeDistribution();
        if (this$specifiedSizeDistribution == null ? other$specifiedSizeDistribution != null : !this$specifiedSizeDistribution.equals(other$specifiedSizeDistribution))
            return false;
        final Object this$originFLUXLocations = this.getOriginFLUXLocations();
        final Object other$originFLUXLocations = other.getOriginFLUXLocations();
        if (this$originFLUXLocations == null ? other$originFLUXLocations != null : !this$originFLUXLocations.equals(other$originFLUXLocations))
            return false;
        final Object this$originFishingActivity = this.getOriginFishingActivity();
        final Object other$originFishingActivity = other.getOriginFishingActivity();
        if (this$originFishingActivity == null ? other$originFishingActivity != null : !this$originFishingActivity.equals(other$originFishingActivity))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $speciesCode = this.getSpeciesCode();
        result = result * PRIME + ($speciesCode == null ? 43 : $speciesCode.hashCode());
        final Object $unitQuantity = this.getUnitQuantity();
        result = result * PRIME + ($unitQuantity == null ? 43 : $unitQuantity.hashCode());
        final Object $weightMeasure = this.getWeightMeasure();
        result = result * PRIME + ($weightMeasure == null ? 43 : $weightMeasure.hashCode());
        final Object $weighingMeansCode = this.getWeighingMeansCode();
        result = result * PRIME + ($weighingMeansCode == null ? 43 : $weighingMeansCode.hashCode());
        final Object $usageCode = this.getUsageCode();
        result = result * PRIME + ($usageCode == null ? 43 : $usageCode.hashCode());
        final Object $packagingUnitQuantity = this.getPackagingUnitQuantity();
        result = result * PRIME + ($packagingUnitQuantity == null ? 43 : $packagingUnitQuantity.hashCode());
        final Object $packagingTypeCode = this.getPackagingTypeCode();
        result = result * PRIME + ($packagingTypeCode == null ? 43 : $packagingTypeCode.hashCode());
        final Object $packagingUnitAverageWeightMeasure = this.getPackagingUnitAverageWeightMeasure();
        result = result * PRIME + ($packagingUnitAverageWeightMeasure == null ? 43 : $packagingUnitAverageWeightMeasure.hashCode());
        final Object $appliedAAPProcesses = this.getAppliedAAPProcesses();
        result = result * PRIME + ($appliedAAPProcesses == null ? 43 : $appliedAAPProcesses.hashCode());
        final Object $totalSalesPrice = this.getTotalSalesPrice();
        result = result * PRIME + ($totalSalesPrice == null ? 43 : $totalSalesPrice.hashCode());
        final Object $specifiedSizeDistribution = this.getSpecifiedSizeDistribution();
        result = result * PRIME + ($specifiedSizeDistribution == null ? 43 : $specifiedSizeDistribution.hashCode());
        final Object $originFLUXLocations = this.getOriginFLUXLocations();
        result = result * PRIME + ($originFLUXLocations == null ? 43 : $originFLUXLocations.hashCode());
        final Object $originFishingActivity = this.getOriginFishingActivity();
        result = result * PRIME + ($originFishingActivity == null ? 43 : $originFishingActivity.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesAAPProductFact;
    }

    // TODO test
    public boolean isInvalidUsageCode(){
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
