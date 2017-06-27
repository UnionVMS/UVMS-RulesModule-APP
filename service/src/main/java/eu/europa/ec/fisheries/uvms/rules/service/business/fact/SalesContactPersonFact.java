package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesContactPersonFact extends AbstractFact {

    private TextType title;
    private TextType givenName;
    private TextType middleName;
    private TextType familyNamePrefix;
    private TextType familyName;
    private TextType nameSuffix;
    private CodeType genderCode;
    private TextType alias;
    private DateTimeType birthDateTime;
    private TextType birthplaceName;
    private TelecommunicationCommunicationType telephoneTelecommunicationCommunication;
    private TelecommunicationCommunicationType faxTelecommunicationCommunication;
    private EmailCommunicationType emailURIEmailCommunication;
    private WebsiteCommunicationType websiteURIWebsiteCommunication;
    private List<UniversalCommunicationType> specifiedUniversalCommunications;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_CONTACT_PERSON;
    }

    public TextType getTitle() {
        return this.title;
    }

    public TextType getGivenName() { return this.givenName; }

    public TextType getMiddleName() {
        return this.middleName;
    }

    public TextType getFamilyNamePrefix() {
        return this.familyNamePrefix;
    }

    public TextType getFamilyName() {
        return this.familyName;
    }

    public TextType getNameSuffix() {
        return this.nameSuffix;
    }

    public CodeType getGenderCode() {
        return this.genderCode;
    }

    public TextType getAlias() {
        return this.alias;
    }

    public DateTimeType getBirthDateTime() {
        return this.birthDateTime;
    }

    public TextType getBirthplaceName() {
        return this.birthplaceName;
    }

    public TelecommunicationCommunicationType getTelephoneTelecommunicationCommunication() {
        return this.telephoneTelecommunicationCommunication;
    }

    public TelecommunicationCommunicationType getFaxTelecommunicationCommunication() {
        return this.faxTelecommunicationCommunication;
    }

    public EmailCommunicationType getEmailURIEmailCommunication() {
        return this.emailURIEmailCommunication;
    }

    public WebsiteCommunicationType getWebsiteURIWebsiteCommunication() {
        return this.websiteURIWebsiteCommunication;
    }

    public List<UniversalCommunicationType> getSpecifiedUniversalCommunications() {
        return this.specifiedUniversalCommunications;
    }

    public void setTitle(TextType title) {
        this.title = title;
    }

    public void setGivenName(TextType givenName) {
        this.givenName = givenName;
    }

    public void setMiddleName(TextType middleName) {
        this.middleName = middleName;
    }

    public void setFamilyNamePrefix(TextType familyNamePrefix) {
        this.familyNamePrefix = familyNamePrefix;
    }

    public void setFamilyName(TextType familyName) {
        this.familyName = familyName;
    }

    public void setNameSuffix(TextType nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public void setGenderCode(CodeType genderCode) {
        this.genderCode = genderCode;
    }

    public void setAlias(TextType alias) {
        this.alias = alias;
    }

    public void setBirthDateTime(DateTimeType birthDateTime) {
        this.birthDateTime = birthDateTime;
    }

    public void setBirthplaceName(TextType birthplaceName) {
        this.birthplaceName = birthplaceName;
    }

    public void setTelephoneTelecommunicationCommunication(TelecommunicationCommunicationType telephoneTelecommunicationCommunication) {
        this.telephoneTelecommunicationCommunication = telephoneTelecommunicationCommunication;
    }

    public void setFaxTelecommunicationCommunication(TelecommunicationCommunicationType faxTelecommunicationCommunication) {
        this.faxTelecommunicationCommunication = faxTelecommunicationCommunication;
    }

    public void setEmailURIEmailCommunication(EmailCommunicationType emailURIEmailCommunication) {
        this.emailURIEmailCommunication = emailURIEmailCommunication;
    }

    public void setWebsiteURIWebsiteCommunication(WebsiteCommunicationType websiteURIWebsiteCommunication) {
        this.websiteURIWebsiteCommunication = websiteURIWebsiteCommunication;
    }

    public void setSpecifiedUniversalCommunications(List<UniversalCommunicationType> specifiedUniversalCommunications) {
        this.specifiedUniversalCommunications = specifiedUniversalCommunications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesContactPersonFact)) return false;
        SalesContactPersonFact that = (SalesContactPersonFact) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(givenName, that.givenName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(familyNamePrefix, that.familyNamePrefix) &&
                Objects.equals(familyName, that.familyName) &&
                Objects.equals(nameSuffix, that.nameSuffix) &&
                Objects.equals(genderCode, that.genderCode) &&
                Objects.equals(alias, that.alias) &&
                Objects.equals(birthDateTime, that.birthDateTime) &&
                Objects.equals(birthplaceName, that.birthplaceName) &&
                Objects.equals(telephoneTelecommunicationCommunication, that.telephoneTelecommunicationCommunication) &&
                Objects.equals(faxTelecommunicationCommunication, that.faxTelecommunicationCommunication) &&
                Objects.equals(emailURIEmailCommunication, that.emailURIEmailCommunication) &&
                Objects.equals(websiteURIWebsiteCommunication, that.websiteURIWebsiteCommunication) &&
                Objects.equals(specifiedUniversalCommunications, that.specifiedUniversalCommunications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, givenName, middleName, familyNamePrefix, familyName, nameSuffix, genderCode, alias, birthDateTime, birthplaceName, telephoneTelecommunicationCommunication, faxTelecommunicationCommunication, emailURIEmailCommunication, websiteURIWebsiteCommunication, specifiedUniversalCommunications);
    }
}
