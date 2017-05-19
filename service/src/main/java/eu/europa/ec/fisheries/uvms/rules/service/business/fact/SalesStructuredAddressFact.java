package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class SalesStructuredAddressFact extends AbstractFact {

    private IDType id;
    private eu.europa.ec.fisheries.schema.sales.CodeType postcodeCode;
    private TextType buildingName;
    private TextType streetName;
    private TextType cityName;
    private IDType countryID;
    private TextType citySubDivisionName;
    private TextType countryName;
    private TextType countrySubDivisionName;
    private TextType blockName;
    private TextType plotIdentification;
    private TextType postOfficeBox;
    private TextType buildingNumber;
    private TextType staircaseNumber;
    private TextType floorIdentification;
    private TextType roomIdentification;
    private TextType postalArea;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_STRUCTURED_ADDRESS;
    }
}
