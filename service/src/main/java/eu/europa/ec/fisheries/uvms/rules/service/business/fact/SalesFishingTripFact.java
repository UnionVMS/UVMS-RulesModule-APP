package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DelimitedPeriodType;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFishingTripFact extends AbstractFact {

    private List<IdType> ids;
    private CodeType typeCode;
    private List<DelimitedPeriodType> specifiedDelimitedPeriods;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FISHING_TRIP;
    }

    public List<IdType> getIDS() {
        return this.ids;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public List<DelimitedPeriodType> getSpecifiedDelimitedPeriods() {
        return this.specifiedDelimitedPeriods;
    }

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setSpecifiedDelimitedPeriods(List<DelimitedPeriodType> specifiedDelimitedPeriods) {
        this.specifiedDelimitedPeriods = specifiedDelimitedPeriods;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFishingTripFact)) return false;
        final SalesFishingTripFact other = (SalesFishingTripFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIDS();
        final Object other$ids = other.getIDS();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$typeCode = this.getTypeCode();
        final Object other$typeCode = other.getTypeCode();
        if (this$typeCode == null ? other$typeCode != null : !this$typeCode.equals(other$typeCode)) return false;
        final Object this$specifiedDelimitedPeriods = this.getSpecifiedDelimitedPeriods();
        final Object other$specifiedDelimitedPeriods = other.getSpecifiedDelimitedPeriods();
        if (this$specifiedDelimitedPeriods == null ? other$specifiedDelimitedPeriods != null : !this$specifiedDelimitedPeriods.equals(other$specifiedDelimitedPeriods))
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
        final Object $specifiedDelimitedPeriods = this.getSpecifiedDelimitedPeriods();
        result = result * PRIME + ($specifiedDelimitedPeriods == null ? 43 : $specifiedDelimitedPeriods.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFishingTripFact;
    }
}
