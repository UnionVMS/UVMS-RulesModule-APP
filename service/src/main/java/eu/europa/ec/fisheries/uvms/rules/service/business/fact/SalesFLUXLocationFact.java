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
public class SalesFLUXLocationFact extends AbstractFact {

    private eu.europa.ec.fisheries.schema.sales.CodeType typeCode;
    private IDType countryID;
    private IDType id;
    private eu.europa.ec.fisheries.schema.sales.CodeType geopoliticalRegionCode;
    private List<TextType> names;
    private IDType sovereignRightsCountryID;
    private IDType jurisdictionCountryID;
    private eu.europa.ec.fisheries.schema.sales.CodeType regionalFisheriesManagementOrganizationCode;
    private FLUXGeographicalCoordinateType specifiedPhysicalFLUXGeographicalCoordinate;
    private List<StructuredAddressType> postalStructuredAddresses;
    private StructuredAddressType physicalStructuredAddress;
    private List<SpecifiedPolygonType> boundarySpecifiedPolygons;
    private List<FLUXCharacteristicType> applicableFLUXCharacteristics;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_LOCATION;
    }
}
