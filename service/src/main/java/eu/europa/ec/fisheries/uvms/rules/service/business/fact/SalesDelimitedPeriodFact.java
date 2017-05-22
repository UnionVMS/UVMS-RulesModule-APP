package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

public class SalesDelimitedPeriodFact extends AbstractFact {

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesDelimitedPeriodFact)) return false;
        final SalesDelimitedPeriodFact other = (SalesDelimitedPeriodFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$startDateTime = this.getStartDateTime();
        final Object other$startDateTime = other.getStartDateTime();
        if (this$startDateTime == null ? other$startDateTime != null : !this$startDateTime.equals(other$startDateTime))
            return false;
        final Object this$endDateTime = this.getEndDateTime();
        final Object other$endDateTime = other.getEndDateTime();
        if (this$endDateTime == null ? other$endDateTime != null : !this$endDateTime.equals(other$endDateTime))
            return false;
        final Object this$durationMeasure = this.getDurationMeasure();
        final Object other$durationMeasure = other.getDurationMeasure();
        if (this$durationMeasure == null ? other$durationMeasure != null : !this$durationMeasure.equals(other$durationMeasure))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $startDateTime = this.getStartDateTime();
        result = result * PRIME + ($startDateTime == null ? 43 : $startDateTime.hashCode());
        final Object $endDateTime = this.getEndDateTime();
        result = result * PRIME + ($endDateTime == null ? 43 : $endDateTime.hashCode());
        final Object $durationMeasure = this.getDurationMeasure();
        result = result * PRIME + ($durationMeasure == null ? 43 : $durationMeasure.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesDelimitedPeriodFact;
    }
}
