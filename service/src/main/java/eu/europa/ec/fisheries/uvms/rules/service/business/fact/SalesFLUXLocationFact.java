package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.SalesFactHelper;

import java.util.List;
import java.util.Objects;

public class SalesFLUXLocationFact extends SalesAbstractFact {

    private CodeType typeCode;
    private IdType countryID;
    private IdType id;
    private CodeType geopoliticalRegionCode;
    private List<TextType> names;
    private IdType sovereignRightsCountryID;
    private IdType jurisdictionCountryID;
    private CodeType regionalFisheriesManagementOrganizationCode;
    private FLUXGeographicalCoordinateType specifiedPhysicalFLUXGeographicalCoordinate;
    private List<StructuredAddressType> postalStructuredAddresses;
    private StructuredAddressType physicalStructuredAddress;
    private List<SpecifiedPolygonType> boundarySpecifiedPolygons;
    private List<FLUXCharacteristicType> applicableFLUXCharacteristics;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_LOCATION;
    }

    public CodeType getTypeCode() {
        return this.typeCode;
    }

    public IdType getCountryID() {
        return this.countryID;
    }

    public IdType getID() {
        return this.id;
    }

    public CodeType getGeopoliticalRegionCode() {
        return this.geopoliticalRegionCode;
    }

    public List<TextType> getNames() {
        return this.names;
    }

    public IdType getSovereignRightsCountryID() {
        return this.sovereignRightsCountryID;
    }

    public IdType getJurisdictionCountryID() {
        return this.jurisdictionCountryID;
    }

    public CodeType getRegionalFisheriesManagementOrganizationCode() {
        return this.regionalFisheriesManagementOrganizationCode;
    }

    public FLUXGeographicalCoordinateType getSpecifiedPhysicalFLUXGeographicalCoordinate() {
        return this.specifiedPhysicalFLUXGeographicalCoordinate;
    }

    public List<StructuredAddressType> getPostalStructuredAddresses() {
        return this.postalStructuredAddresses;
    }

    public StructuredAddressType getPhysicalStructuredAddress() {
        return this.physicalStructuredAddress;
    }

    public List<SpecifiedPolygonType> getBoundarySpecifiedPolygons() {
        return this.boundarySpecifiedPolygons;
    }

    public List<FLUXCharacteristicType> getApplicableFLUXCharacteristics() {
        return this.applicableFLUXCharacteristics;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public void setCountryID(IdType countryID) {
        this.countryID = countryID;
    }

    public void setID(IdType id) {
        this.id = id;
    }

    public void setGeopoliticalRegionCode(CodeType geopoliticalRegionCode) {
        this.geopoliticalRegionCode = geopoliticalRegionCode;
    }

    public void setNames(List<TextType> names) {
        this.names = names;
    }

    public void setSovereignRightsCountryID(IdType sovereignRightsCountryID) {
        this.sovereignRightsCountryID = sovereignRightsCountryID;
    }

    public void setJurisdictionCountryID(IdType jurisdictionCountryID) {
        this.jurisdictionCountryID = jurisdictionCountryID;
    }

    public void setRegionalFisheriesManagementOrganizationCode(CodeType regionalFisheriesManagementOrganizationCode) {
        this.regionalFisheriesManagementOrganizationCode = regionalFisheriesManagementOrganizationCode;
    }

    public void setSpecifiedPhysicalFLUXGeographicalCoordinate(FLUXGeographicalCoordinateType specifiedPhysicalFLUXGeographicalCoordinate) {
        this.specifiedPhysicalFLUXGeographicalCoordinate = specifiedPhysicalFLUXGeographicalCoordinate;
    }

    public void setPostalStructuredAddresses(List<StructuredAddressType> postalStructuredAddresses) {
        this.postalStructuredAddresses = postalStructuredAddresses;
    }

    public void setPhysicalStructuredAddress(StructuredAddressType physicalStructuredAddress) {
        this.physicalStructuredAddress = physicalStructuredAddress;
    }

    public void setBoundarySpecifiedPolygons(List<SpecifiedPolygonType> boundarySpecifiedPolygons) {
        this.boundarySpecifiedPolygons = boundarySpecifiedPolygons;
    }

    public void setApplicableFLUXCharacteristics(List<FLUXCharacteristicType> applicableFLUXCharacteristics) {
        this.applicableFLUXCharacteristics = applicableFLUXCharacteristics;
    }

    public boolean isCountryIdValid() {
        return SalesFactHelper.isCountryIdValid(countryID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXLocationFact)) return false;
        SalesFLUXLocationFact that = (SalesFLUXLocationFact) o;
        return Objects.equals(typeCode, that.typeCode) &&
                Objects.equals(countryID, that.countryID) &&
                Objects.equals(id, that.id) &&
                Objects.equals(geopoliticalRegionCode, that.geopoliticalRegionCode) &&
                Objects.equals(names, that.names) &&
                Objects.equals(sovereignRightsCountryID, that.sovereignRightsCountryID) &&
                Objects.equals(jurisdictionCountryID, that.jurisdictionCountryID) &&
                Objects.equals(regionalFisheriesManagementOrganizationCode, that.regionalFisheriesManagementOrganizationCode) &&
                Objects.equals(specifiedPhysicalFLUXGeographicalCoordinate, that.specifiedPhysicalFLUXGeographicalCoordinate) &&
                Objects.equals(postalStructuredAddresses, that.postalStructuredAddresses) &&
                Objects.equals(physicalStructuredAddress, that.physicalStructuredAddress) &&
                Objects.equals(boundarySpecifiedPolygons, that.boundarySpecifiedPolygons) &&
                Objects.equals(applicableFLUXCharacteristics, that.applicableFLUXCharacteristics) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCode, countryID, id, geopoliticalRegionCode, names, sovereignRightsCountryID, jurisdictionCountryID, regionalFisheriesManagementOrganizationCode, specifiedPhysicalFLUXGeographicalCoordinate, postalStructuredAddresses, physicalStructuredAddress, boundarySpecifiedPolygons, applicableFLUXCharacteristics, creationDateOfMessage);
    }
}
