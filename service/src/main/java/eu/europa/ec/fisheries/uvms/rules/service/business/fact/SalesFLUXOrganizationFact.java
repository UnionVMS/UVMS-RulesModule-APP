package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.List;
import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.StructuredAddressType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXOrganizationFact)) return false;
        SalesFLUXOrganizationFact that = (SalesFLUXOrganizationFact) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(postalStructuredAddresses, that.postalStructuredAddresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, postalStructuredAddresses);
    }
}
