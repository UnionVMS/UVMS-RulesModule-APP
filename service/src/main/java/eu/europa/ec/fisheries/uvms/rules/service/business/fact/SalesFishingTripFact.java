package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.List;
import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DelimitedPeriodType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

public class SalesFishingTripFact extends SalesAbstractFact {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFishingTripFact)) return false;
        SalesFishingTripFact that = (SalesFishingTripFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(specifiedDelimitedPeriods, that.specifiedDelimitedPeriods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, typeCode, specifiedDelimitedPeriods);
    }
}
