package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

/**
 * Created by MATBUL on 16/06/2017.
 */
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
}
