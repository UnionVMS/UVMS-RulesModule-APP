package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesSizeDistributionFact extends AbstractFact {

    private CodeType categoryCode;
    private List<CodeType> classCodes;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_SIZE_DISTRIBUTION;
    }

    public CodeType getCategoryCode() {
        return this.categoryCode;
    }

    public List<CodeType> getClassCodes() {
        return this.classCodes;
    }

    public void setCategoryCode(CodeType categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setClassCodes(List<CodeType> classCodes) {
        this.classCodes = classCodes;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesSizeDistributionFact)) return false;
        final SalesSizeDistributionFact other = (SalesSizeDistributionFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$categoryCode = this.getCategoryCode();
        final Object other$categoryCode = other.getCategoryCode();
        if (this$categoryCode == null ? other$categoryCode != null : !this$categoryCode.equals(other$categoryCode))
            return false;
        final Object this$classCodes = this.getClassCodes();
        final Object other$classCodes = other.getClassCodes();
        if (this$classCodes == null ? other$classCodes != null : !this$classCodes.equals(other$classCodes))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $categoryCode = this.getCategoryCode();
        result = result * PRIME + ($categoryCode == null ? 43 : $categoryCode.hashCode());
        final Object $classCodes = this.getClassCodes();
        result = result * PRIME + ($classCodes == null ? 43 : $classCodes.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesSizeDistributionFact;
    }
}
