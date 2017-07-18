package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.DelimitedPeriodType;
import eu.europa.ec.fisheries.schema.sales.FLUXPartyType;
import eu.europa.ec.fisheries.schema.sales.SalesQueryParameterType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesQueryFact extends SalesAbstractFact {

    private IdType id;
    private DateTimeType submittedDateTime;
    private CodeType typeCode;
    private DelimitedPeriodType specifiedDelimitedPeriod;
    private FLUXPartyType submitterFLUXParty;
    private List<SalesQueryParameterType> simpleSalesQueryParameters;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_QUERY;
    }

    public IdType getID() {
        return this.id;
    }

    public DateTimeType getSubmittedDateTime() {
        return this.submittedDateTime;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public DelimitedPeriodType getSpecifiedDelimitedPeriod() {
        return this.specifiedDelimitedPeriod;
    }

    public FLUXPartyType getSubmitterFLUXParty() {
        return this.submitterFLUXParty;
    }

    public List<SalesQueryParameterType> getSimpleSalesQueryParameters() {
        return this.simpleSalesQueryParameters;
    }

    public void setID(IdType id) {
        this.id = id;
    }

    public void setSubmittedDateTime(DateTimeType submittedDateTime) {
        this.submittedDateTime = submittedDateTime;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setSpecifiedDelimitedPeriod(DelimitedPeriodType specifiedDelimitedPeriod) {
        this.specifiedDelimitedPeriod = specifiedDelimitedPeriod;
    }

    public void setSubmitterFLUXParty(FLUXPartyType submitterFLUXParty) {
        this.submitterFLUXParty = submitterFLUXParty;
    }

    public void setSimpleSalesQueryParameters(List<SalesQueryParameterType> simpleSalesQueryParameters) {
        this.simpleSalesQueryParameters = simpleSalesQueryParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesQueryFact)) return false;
        SalesQueryFact that = (SalesQueryFact) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(submittedDateTime, that.submittedDateTime) &&
                Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(specifiedDelimitedPeriod, that.specifiedDelimitedPeriod) &&
                Objects.equals(submitterFLUXParty, that.submitterFLUXParty) &&
                Objects.equals(simpleSalesQueryParameters, that.simpleSalesQueryParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, submittedDateTime, typeCode, specifiedDelimitedPeriod, submitterFLUXParty, simpleSalesQueryParameters);
    }
}
