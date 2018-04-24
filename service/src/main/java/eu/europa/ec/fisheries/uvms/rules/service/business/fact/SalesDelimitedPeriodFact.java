package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

import java.util.Objects;

public class SalesDelimitedPeriodFact extends SalesAbstractFact {

    private DateTimeType startDateTime;
    private DateTimeType endDateTime;
    private eu.europa.ec.fisheries.schema.sales.MeasureType durationMeasure;


    @Override
    public void setFactType() {
        this.factType = FactType.SALES_DELIMITED_PERIOD;
    }

    public DateTimeType getStartDateTime() {
        return this.startDateTime;
    }

    public DateTimeType getEndDateTime() {
        return this.endDateTime;
    }

    public eu.europa.ec.fisheries.schema.sales.MeasureType getDurationMeasure() {
        return this.durationMeasure;
    }

    public void setStartDateTime(DateTimeType startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(DateTimeType endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setDurationMeasure(MeasureType durationMeasure) {
        this.durationMeasure = durationMeasure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesDelimitedPeriodFact)) return false;
        SalesDelimitedPeriodFact that = (SalesDelimitedPeriodFact) o;
        return Objects.equals(startDateTime, that.startDateTime) &&
                Objects.equals(endDateTime, that.endDateTime) &&
                Objects.equals(durationMeasure, that.durationMeasure) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime, durationMeasure, creationDateOfMessage);
    }
}
