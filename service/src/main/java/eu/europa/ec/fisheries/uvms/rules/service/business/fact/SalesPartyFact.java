package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.List;
import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.FLUXOrganizationType;
import eu.europa.ec.fisheries.schema.sales.StructuredAddressType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

public class SalesPartyFact extends SalesAbstractFact {

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
        if (!(o instanceof SalesPartyFact)) return false;
        SalesPartyFact that = (SalesPartyFact) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(countryID, that.countryID) &&
                Objects.equals(roleCodes, that.roleCodes) &&
                Objects.equals(specifiedStructuredAddresses, that.specifiedStructuredAddresses) &&
                Objects.equals(specifiedFLUXOrganization, that.specifiedFLUXOrganization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, typeCode, countryID, roleCodes, specifiedStructuredAddresses, specifiedFLUXOrganization);
    }
}
