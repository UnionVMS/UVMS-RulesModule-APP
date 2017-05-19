package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.StructuredAddressType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesFLUXOrganizationFact extends AbstractFact {

    private TextType name;
    private List<StructuredAddressType> postalStructuredAddresses;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_ORGANIZATION;
    }
}
