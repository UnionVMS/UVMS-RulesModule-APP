package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXOrganizationType;
import eu.europa.ec.fisheries.schema.sales.StructuredAddressType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesPartyFact extends AbstractFact {

    private IdType id;
    private TextType name;
    private CodeType typeCode;
    private IdType countryID;
    private List<CodeType> roleCodes;
    private List<StructuredAddressType> specifiedStructuredAddresses;
    private FLUXOrganizationType specifiedFLUXOrganization;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_PARTY;
    }

    public IdType getID() {
        return this.id;
    }

    public TextType getName() {
        return this.name;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public IdType getCountryID() {
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

    public void setID(IdType id) {
        this.id = id;
    }

    public void setName(TextType name) {
        this.name = name;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setCountryID(IdType countryID) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalesPartyFact that = (SalesPartyFact) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (typeCode != null ? !typeCode.equals(that.typeCode) : that.typeCode != null) return false;
        if (countryID != null ? !countryID.equals(that.countryID) : that.countryID != null) return false;
        if (roleCodes != null ? !roleCodes.equals(that.roleCodes) : that.roleCodes != null) return false;
        if (specifiedStructuredAddresses != null ? !specifiedStructuredAddresses.equals(that.specifiedStructuredAddresses) : that.specifiedStructuredAddresses != null)
            return false;
        return specifiedFLUXOrganization != null ? specifiedFLUXOrganization.equals(that.specifiedFLUXOrganization) : that.specifiedFLUXOrganization == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (typeCode != null ? typeCode.hashCode() : 0);
        result = 31 * result + (countryID != null ? countryID.hashCode() : 0);
        result = 31 * result + (roleCodes != null ? roleCodes.hashCode() : 0);
        result = 31 * result + (specifiedStructuredAddresses != null ? specifiedStructuredAddresses.hashCode() : 0);
        result = 31 * result + (specifiedFLUXOrganization != null ? specifiedFLUXOrganization.hashCode() : 0);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesPartyFact;
    }
}
