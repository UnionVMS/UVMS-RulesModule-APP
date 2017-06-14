package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesQueryFact extends AbstractFact {

    private IDType id;
    private DateTimeType submittedDateTime;
    private CodeType typeCode;
    private DelimitedPeriodType specifiedDelimitedPeriod;
    private FLUXPartyType submitterFLUXParty;
    private List<SalesQueryParameterType> simpleSalesQueryParameters;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_QUERY;
    }

    public IDType getID() {
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

    public void setID(IDType id) {
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesQueryFact)) return false;
        final SalesQueryFact other = (SalesQueryFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getID();
        final Object other$id = other.getID();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$submittedDateTime = this.getSubmittedDateTime();
        final Object other$submittedDateTime = other.getSubmittedDateTime();
        if (this$submittedDateTime == null ? other$submittedDateTime != null : !this$submittedDateTime.equals(other$submittedDateTime))
            return false;
        final Object this$typeCode = this.getTypeCode();
        final Object other$typeCode = other.getTypeCode();
        if (this$typeCode == null ? other$typeCode != null : !this$typeCode.equals(other$typeCode)) return false;
        final Object this$specifiedDelimitedPeriod = this.getSpecifiedDelimitedPeriod();
        final Object other$specifiedDelimitedPeriod = other.getSpecifiedDelimitedPeriod();
        if (this$specifiedDelimitedPeriod == null ? other$specifiedDelimitedPeriod != null : !this$specifiedDelimitedPeriod.equals(other$specifiedDelimitedPeriod))
            return false;
        final Object this$submitterFLUXParty = this.getSubmitterFLUXParty();
        final Object other$submitterFLUXParty = other.getSubmitterFLUXParty();
        if (this$submitterFLUXParty == null ? other$submitterFLUXParty != null : !this$submitterFLUXParty.equals(other$submitterFLUXParty))
            return false;
        final Object this$simpleSalesQueryParameters = this.getSimpleSalesQueryParameters();
        final Object other$simpleSalesQueryParameters = other.getSimpleSalesQueryParameters();
        if (this$simpleSalesQueryParameters == null ? other$simpleSalesQueryParameters != null : !this$simpleSalesQueryParameters.equals(other$simpleSalesQueryParameters))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getID();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $submittedDateTime = this.getSubmittedDateTime();
        result = result * PRIME + ($submittedDateTime == null ? 43 : $submittedDateTime.hashCode());
        final Object $typeCode = this.getTypeCode();
        result = result * PRIME + ($typeCode == null ? 43 : $typeCode.hashCode());
        final Object $specifiedDelimitedPeriod = this.getSpecifiedDelimitedPeriod();
        result = result * PRIME + ($specifiedDelimitedPeriod == null ? 43 : $specifiedDelimitedPeriod.hashCode());
        final Object $submitterFLUXParty = this.getSubmitterFLUXParty();
        result = result * PRIME + ($submitterFLUXParty == null ? 43 : $submitterFLUXParty.hashCode());
        final Object $simpleSalesQueryParameters = this.getSimpleSalesQueryParameters();
        result = result * PRIME + ($simpleSalesQueryParameters == null ? 43 : $simpleSalesQueryParameters.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesQueryFact;
    }
}
