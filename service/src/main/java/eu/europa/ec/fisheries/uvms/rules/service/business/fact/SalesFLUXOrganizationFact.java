package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.StructuredAddressType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFLUXOrganizationFact extends AbstractFact {

    private TextType name;
    private List<StructuredAddressType> postalStructuredAddresses;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_ORGANIZATION;
    }

    public TextType getName() {
        return this.name;
    }

    public List<StructuredAddressType> getPostalStructuredAddresses() {
        return this.postalStructuredAddresses;
    }

    public void setName(TextType name) {
        this.name = name;
    }

    public void setPostalStructuredAddresses(List<StructuredAddressType> postalStructuredAddresses) {
        this.postalStructuredAddresses = postalStructuredAddresses;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFLUXOrganizationFact)) return false;
        final SalesFLUXOrganizationFact other = (SalesFLUXOrganizationFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$postalStructuredAddresses = this.getPostalStructuredAddresses();
        final Object other$postalStructuredAddresses = other.getPostalStructuredAddresses();
        if (this$postalStructuredAddresses == null ? other$postalStructuredAddresses != null : !this$postalStructuredAddresses.equals(other$postalStructuredAddresses))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $postalStructuredAddresses = this.getPostalStructuredAddresses();
        result = result * PRIME + ($postalStructuredAddresses == null ? 43 : $postalStructuredAddresses.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFLUXOrganizationFact;
    }
}
