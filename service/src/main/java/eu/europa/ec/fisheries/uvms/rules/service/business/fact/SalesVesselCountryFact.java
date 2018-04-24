package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.SalesFactHelper;

import java.util.Objects;

public class SalesVesselCountryFact extends SalesAbstractFact {

    private IdType id;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_VESSEL_COUNTRY;
    }

    public IdType getID() {
        return this.id;
    }

    public void setID(IdType id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesVesselCountryFact)) return false;
        SalesVesselCountryFact that = (SalesVesselCountryFact) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDateOfMessage);
    }

    public boolean isIdValid() {
        return SalesFactHelper.isCountryIdValid(getID());
    }
}
