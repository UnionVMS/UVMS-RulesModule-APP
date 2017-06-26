package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesSizeDistributionFact)) return false;
        SalesSizeDistributionFact that = (SalesSizeDistributionFact) o;
        return Objects.equals(categoryCode, that.categoryCode) &&
                Objects.equals(classCodes, that.classCodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryCode, classCodes);
    }
}
