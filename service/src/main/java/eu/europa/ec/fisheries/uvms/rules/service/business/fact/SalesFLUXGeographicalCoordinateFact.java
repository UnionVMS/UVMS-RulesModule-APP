package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

public class SalesFLUXGeographicalCoordinateFact extends AbstractFact {

    private eu.europa.ec.fisheries.schema.sales.MeasureType longitudeMeasure;
    private eu.europa.ec.fisheries.schema.sales.MeasureType latitudeMeasure;
    private eu.europa.ec.fisheries.schema.sales.MeasureType altitudeMeasure;
    private IDType systemID;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_GEOGRAPHICAL_COORDINATE;
    }

    public eu.europa.ec.fisheries.schema.sales.MeasureType getLongitudeMeasure() {
        return this.longitudeMeasure;
    }

    public MeasureType getLatitudeMeasure() {
        return this.latitudeMeasure;
    }

    public MeasureType getAltitudeMeasure() {
        return this.altitudeMeasure;
    }

    public IDType getSystemID() {
        return this.systemID;
    }

    public void setLongitudeMeasure(MeasureType longitudeMeasure) {
        this.longitudeMeasure = longitudeMeasure;
    }

    public void setLatitudeMeasure(MeasureType latitudeMeasure) {
        this.latitudeMeasure = latitudeMeasure;
    }

    public void setAltitudeMeasure(MeasureType altitudeMeasure) {
        this.altitudeMeasure = altitudeMeasure;
    }

    public void setSystemID(IDType systemID) {
        this.systemID = systemID;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFLUXGeographicalCoordinateFact)) return false;
        final SalesFLUXGeographicalCoordinateFact other = (SalesFLUXGeographicalCoordinateFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$longitudeMeasure = this.getLongitudeMeasure();
        final Object other$longitudeMeasure = other.getLongitudeMeasure();
        if (this$longitudeMeasure == null ? other$longitudeMeasure != null : !this$longitudeMeasure.equals(other$longitudeMeasure))
            return false;
        final Object this$latitudeMeasure = this.getLatitudeMeasure();
        final Object other$latitudeMeasure = other.getLatitudeMeasure();
        if (this$latitudeMeasure == null ? other$latitudeMeasure != null : !this$latitudeMeasure.equals(other$latitudeMeasure))
            return false;
        final Object this$altitudeMeasure = this.getAltitudeMeasure();
        final Object other$altitudeMeasure = other.getAltitudeMeasure();
        if (this$altitudeMeasure == null ? other$altitudeMeasure != null : !this$altitudeMeasure.equals(other$altitudeMeasure))
            return false;
        final Object this$systemID = this.getSystemID();
        final Object other$systemID = other.getSystemID();
        if (this$systemID == null ? other$systemID != null : !this$systemID.equals(other$systemID)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $longitudeMeasure = this.getLongitudeMeasure();
        result = result * PRIME + ($longitudeMeasure == null ? 43 : $longitudeMeasure.hashCode());
        final Object $latitudeMeasure = this.getLatitudeMeasure();
        result = result * PRIME + ($latitudeMeasure == null ? 43 : $latitudeMeasure.hashCode());
        final Object $altitudeMeasure = this.getAltitudeMeasure();
        result = result * PRIME + ($altitudeMeasure == null ? 43 : $altitudeMeasure.hashCode());
        final Object $systemID = this.getSystemID();
        result = result * PRIME + ($systemID == null ? 43 : $systemID.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFLUXGeographicalCoordinateFact;
    }
}
