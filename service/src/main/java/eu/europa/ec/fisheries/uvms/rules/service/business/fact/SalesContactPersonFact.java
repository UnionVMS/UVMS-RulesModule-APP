package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesContactPersonFact extends AbstractFact {

    private TextType title;
    private TextType givenName;
    private TextType middleName;
    private TextType familyNamePrefix;
    private TextType familyName;
    private TextType nameSuffix;
    private eu.europa.ec.fisheries.schema.sales.CodeType genderCode;
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
}
