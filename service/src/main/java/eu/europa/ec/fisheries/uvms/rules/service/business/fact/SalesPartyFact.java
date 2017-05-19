package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXOrganizationType;
import eu.europa.ec.fisheries.schema.sales.StructuredAddressType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesPartyFact extends AbstractFact {

    private IDType id;
    private TextType name;
    private un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode;
    private IDType countryID;
    private List<CodeType> roleCodes;
    private List<StructuredAddressType> specifiedStructuredAddresses;
    private FLUXOrganizationType specifiedFLUXOrganization;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_PARTY;
    }
}
