package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesContactPartyFact extends SalesAbstractFact {

    private List<IdType> ids;
    private TextType name;
    private List<TextType> descriptions;
    private List<IdType> nationalityCountryIDs;
    private List<CodeType> languageCodes;
    private IdType residenceCountryID;
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

    public List<IdType> getIDS() {
        return this.ids;
    }

    public TextType getName() {
        return this.name;
    }

    public List<TextType> getDescriptions() {
        return this.descriptions;
    }

    public List<IdType> getNationalityCountryIDs() {
        return this.nationalityCountryIDs;
    }

    public List<CodeType> getLanguageCodes() {
        return this.languageCodes;
    }

    public IdType getResidenceCountryID() {
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

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setName(TextType name) {
        this.name = name;
    }

    public void setDescriptions(List<TextType> descriptions) {
        this.descriptions = descriptions;
    }

    public void setNationalityCountryIDs(List<IdType> nationalityCountryIDs) {
        this.nationalityCountryIDs = nationalityCountryIDs;
    }

    public void setLanguageCodes(List<CodeType> languageCodes) {
        this.languageCodes = languageCodes;
    }

    public void setResidenceCountryID(IdType residenceCountryID) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesContactPartyFact)) return false;
        SalesContactPartyFact that = (SalesContactPartyFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(name, that.name) &&
                Objects.equals(descriptions, that.descriptions) &&
                Objects.equals(nationalityCountryIDs, that.nationalityCountryIDs) &&
                Objects.equals(languageCodes, that.languageCodes) &&
                Objects.equals(residenceCountryID, that.residenceCountryID) &&
                Objects.equals(roleCodes, that.roleCodes) &&
                Objects.equals(specifiedStructuredAddresses, that.specifiedStructuredAddresses) &&
                Objects.equals(specifiedContactPersons, that.specifiedContactPersons) &&
                Objects.equals(telephoneTelecommunicationCommunications, that.telephoneTelecommunicationCommunications) &&
                Objects.equals(faxTelecommunicationCommunications, that.faxTelecommunicationCommunications) &&
                Objects.equals(uriEmailCommunications, that.uriEmailCommunications) &&
                Objects.equals(specifiedUniversalCommunications, that.specifiedUniversalCommunications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, name, descriptions, nationalityCountryIDs, languageCodes, residenceCountryID, roleCodes, specifiedStructuredAddresses, specifiedContactPersons, telephoneTelecommunicationCommunications, faxTelecommunicationCommunications, uriEmailCommunications, specifiedUniversalCommunications);
    }
}
