package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

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

    public TextType getGivenName() {
        return this.givenName;
    }

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesContactPersonFact)) return false;
        final SalesContactPersonFact other = (SalesContactPersonFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$givenName = this.getGivenName();
        final Object other$givenName = other.getGivenName();
        if (this$givenName == null ? other$givenName != null : !this$givenName.equals(other$givenName)) return false;
        final Object this$middleName = this.getMiddleName();
        final Object other$middleName = other.getMiddleName();
        if (this$middleName == null ? other$middleName != null : !this$middleName.equals(other$middleName))
            return false;
        final Object this$familyNamePrefix = this.getFamilyNamePrefix();
        final Object other$familyNamePrefix = other.getFamilyNamePrefix();
        if (this$familyNamePrefix == null ? other$familyNamePrefix != null : !this$familyNamePrefix.equals(other$familyNamePrefix))
            return false;
        final Object this$familyName = this.getFamilyName();
        final Object other$familyName = other.getFamilyName();
        if (this$familyName == null ? other$familyName != null : !this$familyName.equals(other$familyName))
            return false;
        final Object this$nameSuffix = this.getNameSuffix();
        final Object other$nameSuffix = other.getNameSuffix();
        if (this$nameSuffix == null ? other$nameSuffix != null : !this$nameSuffix.equals(other$nameSuffix))
            return false;
        final Object this$genderCode = this.getGenderCode();
        final Object other$genderCode = other.getGenderCode();
        if (this$genderCode == null ? other$genderCode != null : !this$genderCode.equals(other$genderCode))
            return false;
        final Object this$alias = this.getAlias();
        final Object other$alias = other.getAlias();
        if (this$alias == null ? other$alias != null : !this$alias.equals(other$alias)) return false;
        final Object this$birthDateTime = this.getBirthDateTime();
        final Object other$birthDateTime = other.getBirthDateTime();
        if (this$birthDateTime == null ? other$birthDateTime != null : !this$birthDateTime.equals(other$birthDateTime))
            return false;
        final Object this$birthplaceName = this.getBirthplaceName();
        final Object other$birthplaceName = other.getBirthplaceName();
        if (this$birthplaceName == null ? other$birthplaceName != null : !this$birthplaceName.equals(other$birthplaceName))
            return false;
        final Object this$telephoneTelecommunicationCommunication = this.getTelephoneTelecommunicationCommunication();
        final Object other$telephoneTelecommunicationCommunication = other.getTelephoneTelecommunicationCommunication();
        if (this$telephoneTelecommunicationCommunication == null ? other$telephoneTelecommunicationCommunication != null : !this$telephoneTelecommunicationCommunication.equals(other$telephoneTelecommunicationCommunication))
            return false;
        final Object this$faxTelecommunicationCommunication = this.getFaxTelecommunicationCommunication();
        final Object other$faxTelecommunicationCommunication = other.getFaxTelecommunicationCommunication();
        if (this$faxTelecommunicationCommunication == null ? other$faxTelecommunicationCommunication != null : !this$faxTelecommunicationCommunication.equals(other$faxTelecommunicationCommunication))
            return false;
        final Object this$emailURIEmailCommunication = this.getEmailURIEmailCommunication();
        final Object other$emailURIEmailCommunication = other.getEmailURIEmailCommunication();
        if (this$emailURIEmailCommunication == null ? other$emailURIEmailCommunication != null : !this$emailURIEmailCommunication.equals(other$emailURIEmailCommunication))
            return false;
        final Object this$websiteURIWebsiteCommunication = this.getWebsiteURIWebsiteCommunication();
        final Object other$websiteURIWebsiteCommunication = other.getWebsiteURIWebsiteCommunication();
        if (this$websiteURIWebsiteCommunication == null ? other$websiteURIWebsiteCommunication != null : !this$websiteURIWebsiteCommunication.equals(other$websiteURIWebsiteCommunication))
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
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $givenName = this.getGivenName();
        result = result * PRIME + ($givenName == null ? 43 : $givenName.hashCode());
        final Object $middleName = this.getMiddleName();
        result = result * PRIME + ($middleName == null ? 43 : $middleName.hashCode());
        final Object $familyNamePrefix = this.getFamilyNamePrefix();
        result = result * PRIME + ($familyNamePrefix == null ? 43 : $familyNamePrefix.hashCode());
        final Object $familyName = this.getFamilyName();
        result = result * PRIME + ($familyName == null ? 43 : $familyName.hashCode());
        final Object $nameSuffix = this.getNameSuffix();
        result = result * PRIME + ($nameSuffix == null ? 43 : $nameSuffix.hashCode());
        final Object $genderCode = this.getGenderCode();
        result = result * PRIME + ($genderCode == null ? 43 : $genderCode.hashCode());
        final Object $alias = this.getAlias();
        result = result * PRIME + ($alias == null ? 43 : $alias.hashCode());
        final Object $birthDateTime = this.getBirthDateTime();
        result = result * PRIME + ($birthDateTime == null ? 43 : $birthDateTime.hashCode());
        final Object $birthplaceName = this.getBirthplaceName();
        result = result * PRIME + ($birthplaceName == null ? 43 : $birthplaceName.hashCode());
        final Object $telephoneTelecommunicationCommunication = this.getTelephoneTelecommunicationCommunication();
        result = result * PRIME + ($telephoneTelecommunicationCommunication == null ? 43 : $telephoneTelecommunicationCommunication.hashCode());
        final Object $faxTelecommunicationCommunication = this.getFaxTelecommunicationCommunication();
        result = result * PRIME + ($faxTelecommunicationCommunication == null ? 43 : $faxTelecommunicationCommunication.hashCode());
        final Object $emailURIEmailCommunication = this.getEmailURIEmailCommunication();
        result = result * PRIME + ($emailURIEmailCommunication == null ? 43 : $emailURIEmailCommunication.hashCode());
        final Object $websiteURIWebsiteCommunication = this.getWebsiteURIWebsiteCommunication();
        result = result * PRIME + ($websiteURIWebsiteCommunication == null ? 43 : $websiteURIWebsiteCommunication.hashCode());
        final Object $specifiedUniversalCommunications = this.getSpecifiedUniversalCommunications();
        result = result * PRIME + ($specifiedUniversalCommunications == null ? 43 : $specifiedUniversalCommunications.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesContactPersonFact;
    }
}
