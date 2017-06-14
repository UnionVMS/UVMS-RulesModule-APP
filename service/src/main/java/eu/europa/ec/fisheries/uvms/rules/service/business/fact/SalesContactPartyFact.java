package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesContactPartyFact extends AbstractFact {

    private List<IDType> ids;
    private TextType name;
    private List<TextType> descriptions;
    private List<IDType> nationalityCountryIDs;
    private List<CodeType> languageCodes;
    private IDType residenceCountryID;
    private List<CodeType> roleCodes;
    private List<StructuredAddressType> specifiedStructuredAddresses;
    private List<ContactPersonType> specifiedContactPersons;
    private List<TelecommunicationCommunicationType> telephoneTelecommunicationCommunications;
    private List<TelecommunicationCommunicationType> faxTelecommunicationCommunications;
    private List<EmailCommunicationType> uriEmailCommunications;
    private List<UniversalCommunicationType> specifiedUniversalCommunications;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_CONTACT_PARTY;
    }

    public List<IDType> getIDS() {
        return this.ids;
    }

    public TextType getName() {
        return this.name;
    }

    public List<TextType> getDescriptions() {
        return this.descriptions;
    }

    public List<IDType> getNationalityCountryIDs() {
        return this.nationalityCountryIDs;
    }

    public List<CodeType> getLanguageCodes() {
        return this.languageCodes;
    }

    public IDType getResidenceCountryID() {
        return this.residenceCountryID;
    }

    public List<CodeType> getRoleCodes() {
        return this.roleCodes;
    }

    public List<StructuredAddressType> getSpecifiedStructuredAddresses() {
        return this.specifiedStructuredAddresses;
    }

    public List<ContactPersonType> getSpecifiedContactPersons() {
        return this.specifiedContactPersons;
    }

    public List<TelecommunicationCommunicationType> getTelephoneTelecommunicationCommunications() {
        return this.telephoneTelecommunicationCommunications;
    }

    public List<TelecommunicationCommunicationType> getFaxTelecommunicationCommunications() {
        return this.faxTelecommunicationCommunications;
    }

    public List<EmailCommunicationType> getUriEmailCommunications() {
        return this.uriEmailCommunications;
    }

    public List<UniversalCommunicationType> getSpecifiedUniversalCommunications() {
        return this.specifiedUniversalCommunications;
    }

    public void setIDS(List<IDType> ids) {
        this.ids = ids;
    }

    public void setName(TextType name) {
        this.name = name;
    }

    public void setDescriptions(List<TextType> descriptions) {
        this.descriptions = descriptions;
    }

    public void setNationalityCountryIDs(List<IDType> nationalityCountryIDs) {
        this.nationalityCountryIDs = nationalityCountryIDs;
    }

    public void setLanguageCodes(List<CodeType> languageCodes) {
        this.languageCodes = languageCodes;
    }

    public void setResidenceCountryID(IDType residenceCountryID) {
        this.residenceCountryID = residenceCountryID;
    }

    public void setRoleCodes(List<CodeType> roleCodes) {
        this.roleCodes = roleCodes;
    }

    public void setSpecifiedStructuredAddresses(List<StructuredAddressType> specifiedStructuredAddresses) {
        this.specifiedStructuredAddresses = specifiedStructuredAddresses;
    }

    public void setSpecifiedContactPersons(List<ContactPersonType> specifiedContactPersons) {
        this.specifiedContactPersons = specifiedContactPersons;
    }

    public void setTelephoneTelecommunicationCommunications(List<TelecommunicationCommunicationType> telephoneTelecommunicationCommunications) {
        this.telephoneTelecommunicationCommunications = telephoneTelecommunicationCommunications;
    }

    public void setFaxTelecommunicationCommunications(List<TelecommunicationCommunicationType> faxTelecommunicationCommunications) {
        this.faxTelecommunicationCommunications = faxTelecommunicationCommunications;
    }

    public void setUriEmailCommunications(List<EmailCommunicationType> uriEmailCommunications) {
        this.uriEmailCommunications = uriEmailCommunications;
    }

    public void setSpecifiedUniversalCommunications(List<UniversalCommunicationType> specifiedUniversalCommunications) {
        this.specifiedUniversalCommunications = specifiedUniversalCommunications;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesContactPartyFact)) return false;
        final SalesContactPartyFact other = (SalesContactPartyFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIDS();
        final Object other$ids = other.getIDS();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$descriptions = this.getDescriptions();
        final Object other$descriptions = other.getDescriptions();
        if (this$descriptions == null ? other$descriptions != null : !this$descriptions.equals(other$descriptions))
            return false;
        final Object this$nationalityCountryIDs = this.getNationalityCountryIDs();
        final Object other$nationalityCountryIDs = other.getNationalityCountryIDs();
        if (this$nationalityCountryIDs == null ? other$nationalityCountryIDs != null : !this$nationalityCountryIDs.equals(other$nationalityCountryIDs))
            return false;
        final Object this$languageCodes = this.getLanguageCodes();
        final Object other$languageCodes = other.getLanguageCodes();
        if (this$languageCodes == null ? other$languageCodes != null : !this$languageCodes.equals(other$languageCodes))
            return false;
        final Object this$residenceCountryID = this.getResidenceCountryID();
        final Object other$residenceCountryID = other.getResidenceCountryID();
        if (this$residenceCountryID == null ? other$residenceCountryID != null : !this$residenceCountryID.equals(other$residenceCountryID))
            return false;
        final Object this$roleCodes = this.getRoleCodes();
        final Object other$roleCodes = other.getRoleCodes();
        if (this$roleCodes == null ? other$roleCodes != null : !this$roleCodes.equals(other$roleCodes)) return false;
        final Object this$specifiedStructuredAddresses = this.getSpecifiedStructuredAddresses();
        final Object other$specifiedStructuredAddresses = other.getSpecifiedStructuredAddresses();
        if (this$specifiedStructuredAddresses == null ? other$specifiedStructuredAddresses != null : !this$specifiedStructuredAddresses.equals(other$specifiedStructuredAddresses))
            return false;
        final Object this$specifiedContactPersons = this.getSpecifiedContactPersons();
        final Object other$specifiedContactPersons = other.getSpecifiedContactPersons();
        if (this$specifiedContactPersons == null ? other$specifiedContactPersons != null : !this$specifiedContactPersons.equals(other$specifiedContactPersons))
            return false;
        final Object this$telephoneTelecommunicationCommunications = this.getTelephoneTelecommunicationCommunications();
        final Object other$telephoneTelecommunicationCommunications = other.getTelephoneTelecommunicationCommunications();
        if (this$telephoneTelecommunicationCommunications == null ? other$telephoneTelecommunicationCommunications != null : !this$telephoneTelecommunicationCommunications.equals(other$telephoneTelecommunicationCommunications))
            return false;
        final Object this$faxTelecommunicationCommunications = this.getFaxTelecommunicationCommunications();
        final Object other$faxTelecommunicationCommunications = other.getFaxTelecommunicationCommunications();
        if (this$faxTelecommunicationCommunications == null ? other$faxTelecommunicationCommunications != null : !this$faxTelecommunicationCommunications.equals(other$faxTelecommunicationCommunications))
            return false;
        final Object this$uriEmailCommunications = this.getUriEmailCommunications();
        final Object other$uriEmailCommunications = other.getUriEmailCommunications();
        if (this$uriEmailCommunications == null ? other$uriEmailCommunications != null : !this$uriEmailCommunications.equals(other$uriEmailCommunications))
            return false;
        final Object this$specifiedUniversalCommunications = this.getSpecifiedUniversalCommunications();
        final Object other$specifiedUniversalCommunications = other.getSpecifiedUniversalCommunications();
        if (this$specifiedUniversalCommunications == null ? other$specifiedUniversalCommunications != null : !this$specifiedUniversalCommunications.equals(other$specifiedUniversalCommunications))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ids = this.getIDS();
        result = result * PRIME + ($ids == null ? 43 : $ids.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $descriptions = this.getDescriptions();
        result = result * PRIME + ($descriptions == null ? 43 : $descriptions.hashCode());
        final Object $nationalityCountryIDs = this.getNationalityCountryIDs();
        result = result * PRIME + ($nationalityCountryIDs == null ? 43 : $nationalityCountryIDs.hashCode());
        final Object $languageCodes = this.getLanguageCodes();
        result = result * PRIME + ($languageCodes == null ? 43 : $languageCodes.hashCode());
        final Object $residenceCountryID = this.getResidenceCountryID();
        result = result * PRIME + ($residenceCountryID == null ? 43 : $residenceCountryID.hashCode());
        final Object $roleCodes = this.getRoleCodes();
        result = result * PRIME + ($roleCodes == null ? 43 : $roleCodes.hashCode());
        final Object $specifiedStructuredAddresses = this.getSpecifiedStructuredAddresses();
        result = result * PRIME + ($specifiedStructuredAddresses == null ? 43 : $specifiedStructuredAddresses.hashCode());
        final Object $specifiedContactPersons = this.getSpecifiedContactPersons();
        result = result * PRIME + ($specifiedContactPersons == null ? 43 : $specifiedContactPersons.hashCode());
        final Object $telephoneTelecommunicationCommunications = this.getTelephoneTelecommunicationCommunications();
        result = result * PRIME + ($telephoneTelecommunicationCommunications == null ? 43 : $telephoneTelecommunicationCommunications.hashCode());
        final Object $faxTelecommunicationCommunications = this.getFaxTelecommunicationCommunications();
        result = result * PRIME + ($faxTelecommunicationCommunications == null ? 43 : $faxTelecommunicationCommunications.hashCode());
        final Object $uriEmailCommunications = this.getUriEmailCommunications();
        result = result * PRIME + ($uriEmailCommunications == null ? 43 : $uriEmailCommunications.hashCode());
        final Object $specifiedUniversalCommunications = this.getSpecifiedUniversalCommunications();
        result = result * PRIME + ($specifiedUniversalCommunications == null ? 43 : $specifiedUniversalCommunications.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesContactPartyFact;
    }
}
