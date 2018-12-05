package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.Objects;

@ToString
public class SalesFLUXGeographicalCoordinateFact extends SalesAbstractFact {

    private eu.europa.ec.fisheries.schema.sales.MeasureType longitudeMeasure;
    private eu.europa.ec.fisheries.schema.sales.MeasureType latitudeMeasure;
    private eu.europa.ec.fisheries.schema.sales.MeasureType altitudeMeasure;
    private IdType systemID;

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

    public IdType getSystemID() {
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

    public void setSystemID(IdType systemID) {
        this.systemID = systemID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXGeographicalCoordinateFact)) return false;
        SalesFLUXGeographicalCoordinateFact that = (SalesFLUXGeographicalCoordinateFact) o;
        return Objects.equals(longitudeMeasure, that.longitudeMeasure) &&
                Objects.equals(latitudeMeasure, that.latitudeMeasure) &&
                Objects.equals(altitudeMeasure, that.altitudeMeasure) &&
                Objects.equals(systemID, that.systemID) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitudeMeasure, latitudeMeasure, altitudeMeasure, systemID, creationDateOfMessage);
    }
}
