package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesFLUXPartyFact extends SalesAbstractFact {

    private List<IdType> ids;
    private List<TextType> names;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_PARTY;
    }

    public List<IdType> getIDS() {
        return this.ids;
    }

    public List<TextType> getNames() {
        return this.names;
    }

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setNames(List<TextType> names) {
        this.names = names;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXPartyFact)) return false;
        SalesFLUXPartyFact that = (SalesFLUXPartyFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(names, that.names) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, names, creationDateOfMessage);
    }
}
