package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

public class SalesQueryParameterFact extends AbstractFact {

    protected CodeType typeCode;

    protected CodeType valueCode;

    protected DateTimeType valueDateTime;

    protected IDType valueID;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_QUERY_PARAMETER;
    }

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public CodeType getValueCode() {
        return valueCode;
    }

    public void setValueCode(CodeType valueCode) {
        this.valueCode = valueCode;
    }

    public DateTimeType getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(DateTimeType valueDateTime) {
        this.valueDateTime = valueDateTime;
    }

    public IDType getValueID() {
        return valueID;
    }

    public void setValueID(IDType valueID) {
        this.valueID = valueID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesQueryParameterFact)) return false;
        SalesQueryParameterFact that = (SalesQueryParameterFact) o;
        return Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(valueCode, that.valueCode) &&
                Objects.equals(valueDateTime, that.valueDateTime) &&
                Objects.equals(valueID, that.valueID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCode, valueCode, valueDateTime, valueID);
    }
}
