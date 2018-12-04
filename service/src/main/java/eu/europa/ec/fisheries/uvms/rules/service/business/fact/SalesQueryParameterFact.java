package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.Objects;

@ToString
public class SalesQueryParameterFact extends SalesAbstractFact {

    protected CodeType typeCode;

    protected CodeType valueCode;

    protected DateTimeType valueDateTime;

    protected IdType valueID;

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

    public IdType getValueID() {
        return valueID;
    }

    public void setValueID(IdType valueID) {
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
                Objects.equals(valueID, that.valueID) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCode, valueCode, valueDateTime, valueID, creationDateOfMessage);
    }

    public boolean hasTheNationalNumberPartOfTheValueIDAnIncorrectFormat() {
        return valueID != null && !validateFormat(valueID.getValue(), AbstractFact.FORMATS.EU_SALES_ID_SPECIFIC.getFormatStr());
    }

    public boolean hasTheCommonPartOfTheValueIDAnIncorrectFormat() {
        return valueID != null && !validateFormat(valueID.getValue(), AbstractFact.FORMATS.EU_SALES_ID_COMMON.getFormatStr());
    }
}
