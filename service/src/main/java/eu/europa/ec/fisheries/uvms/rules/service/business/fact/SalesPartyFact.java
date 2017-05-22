package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXOrganizationType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.StructuredAddressType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.schema.sales.CodeType;

import java.util.List;

public class SalesPartyFact extends AbstractFact {

    private IDType id;
    private TextType name;
    private CodeType typeCode;
    private IDType countryID;
    private List<CodeType> roleCodes;
    private List<StructuredAddressType> specifiedStructuredAddresses;
    private FLUXOrganizationType specifiedFLUXOrganization;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_PARTY;
    }

    public IDType getId() {
        return this.id;
    }

    public TextType getName() {
        return this.name;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public IDType getCountryID() {
        return this.countryID;
    }

    public List<CodeType> getRoleCodes() {
        return this.roleCodes;
    }

    public List<StructuredAddressType> getSpecifiedStructuredAddresses() {
        return this.specifiedStructuredAddresses;
    }

    public FLUXOrganizationType getSpecifiedFLUXOrganization() {
        return this.specifiedFLUXOrganization;
    }

    public void setId(IDType id) {
        this.id = id;
    }

    public void setName(TextType name) {
        this.name = name;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setCountryID(IDType countryID) {
        this.countryID = countryID;
    }

    public void setRoleCodes(List<CodeType> roleCodes) {
        this.roleCodes = roleCodes;
    }

    public void setSpecifiedStructuredAddresses(List<StructuredAddressType> specifiedStructuredAddresses) {
        this.specifiedStructuredAddresses = specifiedStructuredAddresses;
    }

    public void setSpecifiedFLUXOrganization(FLUXOrganizationType specifiedFLUXOrganization) {
        this.specifiedFLUXOrganization = specifiedFLUXOrganization;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesPartyFact)) return false;
        final SalesPartyFact other = (SalesPartyFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$typeCode = this.getTypeCode();
        final Object other$typeCode = other.getTypeCode();
        if (this$typeCode == null ? other$typeCode != null : !this$typeCode.equals(other$typeCode)) return false;
        final Object this$countryID = this.getCountryID();
        final Object other$countryID = other.getCountryID();
        if (this$countryID == null ? other$countryID != null : !this$countryID.equals(other$countryID)) return false;
        final Object this$roleCodes = this.getRoleCodes();
        final Object other$roleCodes = other.getRoleCodes();
        if (this$roleCodes == null ? other$roleCodes != null : !this$roleCodes.equals(other$roleCodes)) return false;
        final Object this$specifiedStructuredAddresses = this.getSpecifiedStructuredAddresses();
        final Object other$specifiedStructuredAddresses = other.getSpecifiedStructuredAddresses();
        if (this$specifiedStructuredAddresses == null ? other$specifiedStructuredAddresses != null : !this$specifiedStructuredAddresses.equals(other$specifiedStructuredAddresses))
            return false;
        final Object this$specifiedFLUXOrganization = this.getSpecifiedFLUXOrganization();
        final Object other$specifiedFLUXOrganization = other.getSpecifiedFLUXOrganization();
        if (this$specifiedFLUXOrganization == null ? other$specifiedFLUXOrganization != null : !this$specifiedFLUXOrganization.equals(other$specifiedFLUXOrganization))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $typeCode = this.getTypeCode();
        result = result * PRIME + ($typeCode == null ? 43 : $typeCode.hashCode());
        final Object $countryID = this.getCountryID();
        result = result * PRIME + ($countryID == null ? 43 : $countryID.hashCode());
        final Object $roleCodes = this.getRoleCodes();
        result = result * PRIME + ($roleCodes == null ? 43 : $roleCodes.hashCode());
        final Object $specifiedStructuredAddresses = this.getSpecifiedStructuredAddresses();
        result = result * PRIME + ($specifiedStructuredAddresses == null ? 43 : $specifiedStructuredAddresses.hashCode());
        final Object $specifiedFLUXOrganization = this.getSpecifiedFLUXOrganization();
        result = result * PRIME + ($specifiedFLUXOrganization == null ? 43 : $specifiedFLUXOrganization.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesPartyFact;
    }
}
