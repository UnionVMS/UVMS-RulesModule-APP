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
public class SalesContactPartyFact extends AbstractFact {

    private List<IDType> ids;
    private TextType name;
    private List<TextType> descriptions;
    private List<IDType> nationalityCountryIDs;
    private List<eu.europa.ec.fisheries.schema.sales.CodeType> languageCodes;
    private IDType residenceCountryID;
    private List<eu.europa.ec.fisheries.schema.sales.CodeType> roleCodes;
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
}
