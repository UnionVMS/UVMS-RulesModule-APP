package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFLUXPartyFact extends AbstractFact {

    private List<IDType> ids;
    private List<TextType> names;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_PARTY;
    }

    public List<IDType> getIds() {
        return this.ids;
    }

    public List<TextType> getNames() {
        return this.names;
    }

    public void setIds(List<IDType> ids) {
        this.ids = ids;
    }

    public void setNames(List<TextType> names) {
        this.names = names;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFLUXPartyFact)) return false;
        final SalesFLUXPartyFact other = (SalesFLUXPartyFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIds();
        final Object other$ids = other.getIds();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$names = this.getNames();
        final Object other$names = other.getNames();
        if (this$names == null ? other$names != null : !this$names.equals(other$names)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ids = this.getIds();
        result = result * PRIME + ($ids == null ? 43 : $ids.hashCode());
        final Object $names = this.getNames();
        result = result * PRIME + ($names == null ? 43 : $names.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFLUXPartyFact;
    }
}
