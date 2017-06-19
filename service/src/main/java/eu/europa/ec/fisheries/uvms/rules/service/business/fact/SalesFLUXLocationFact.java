package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesFLUXLocationFact extends AbstractFact {

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesFLUXLocationFact)) return false;
        final SalesFLUXLocationFact other = (SalesFLUXLocationFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$typeCode = this.getTypeCode();
        final Object other$typeCode = other.getTypeCode();
        if (this$typeCode == null ? other$typeCode != null : !this$typeCode.equals(other$typeCode)) return false;
        final Object this$countryID = this.getCountryID();
        final Object other$countryID = other.getCountryID();
        if (this$countryID == null ? other$countryID != null : !this$countryID.equals(other$countryID)) return false;
        final Object this$id = this.getID();
        final Object other$id = other.getID();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$geopoliticalRegionCode = this.getGeopoliticalRegionCode();
        final Object other$geopoliticalRegionCode = other.getGeopoliticalRegionCode();
        if (this$geopoliticalRegionCode == null ? other$geopoliticalRegionCode != null : !this$geopoliticalRegionCode.equals(other$geopoliticalRegionCode))
            return false;
        final Object this$names = this.getNames();
        final Object other$names = other.getNames();
        if (this$names == null ? other$names != null : !this$names.equals(other$names)) return false;
        final Object this$sovereignRightsCountryID = this.getSovereignRightsCountryID();
        final Object other$sovereignRightsCountryID = other.getSovereignRightsCountryID();
        if (this$sovereignRightsCountryID == null ? other$sovereignRightsCountryID != null : !this$sovereignRightsCountryID.equals(other$sovereignRightsCountryID))
            return false;
        final Object this$jurisdictionCountryID = this.getJurisdictionCountryID();
        final Object other$jurisdictionCountryID = other.getJurisdictionCountryID();
        if (this$jurisdictionCountryID == null ? other$jurisdictionCountryID != null : !this$jurisdictionCountryID.equals(other$jurisdictionCountryID))
            return false;
        final Object this$regionalFisheriesManagementOrganizationCode = this.getRegionalFisheriesManagementOrganizationCode();
        final Object other$regionalFisheriesManagementOrganizationCode = other.getRegionalFisheriesManagementOrganizationCode();
        if (this$regionalFisheriesManagementOrganizationCode == null ? other$regionalFisheriesManagementOrganizationCode != null : !this$regionalFisheriesManagementOrganizationCode.equals(other$regionalFisheriesManagementOrganizationCode))
            return false;
        final Object this$specifiedPhysicalFLUXGeographicalCoordinate = this.getSpecifiedPhysicalFLUXGeographicalCoordinate();
        final Object other$specifiedPhysicalFLUXGeographicalCoordinate = other.getSpecifiedPhysicalFLUXGeographicalCoordinate();
        if (this$specifiedPhysicalFLUXGeographicalCoordinate == null ? other$specifiedPhysicalFLUXGeographicalCoordinate != null : !this$specifiedPhysicalFLUXGeographicalCoordinate.equals(other$specifiedPhysicalFLUXGeographicalCoordinate))
            return false;
        final Object this$postalStructuredAddresses = this.getPostalStructuredAddresses();
        final Object other$postalStructuredAddresses = other.getPostalStructuredAddresses();
        if (this$postalStructuredAddresses == null ? other$postalStructuredAddresses != null : !this$postalStructuredAddresses.equals(other$postalStructuredAddresses))
            return false;
        final Object this$physicalStructuredAddress = this.getPhysicalStructuredAddress();
        final Object other$physicalStructuredAddress = other.getPhysicalStructuredAddress();
        if (this$physicalStructuredAddress == null ? other$physicalStructuredAddress != null : !this$physicalStructuredAddress.equals(other$physicalStructuredAddress))
            return false;
        final Object this$boundarySpecifiedPolygons = this.getBoundarySpecifiedPolygons();
        final Object other$boundarySpecifiedPolygons = other.getBoundarySpecifiedPolygons();
        if (this$boundarySpecifiedPolygons == null ? other$boundarySpecifiedPolygons != null : !this$boundarySpecifiedPolygons.equals(other$boundarySpecifiedPolygons))
            return false;
        final Object this$applicableFLUXCharacteristics = this.getApplicableFLUXCharacteristics();
        final Object other$applicableFLUXCharacteristics = other.getApplicableFLUXCharacteristics();
        if (this$applicableFLUXCharacteristics == null ? other$applicableFLUXCharacteristics != null : !this$applicableFLUXCharacteristics.equals(other$applicableFLUXCharacteristics))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $typeCode = this.getTypeCode();
        result = result * PRIME + ($typeCode == null ? 43 : $typeCode.hashCode());
        final Object $countryID = this.getCountryID();
        result = result * PRIME + ($countryID == null ? 43 : $countryID.hashCode());
        final Object $id = this.getID();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $geopoliticalRegionCode = this.getGeopoliticalRegionCode();
        result = result * PRIME + ($geopoliticalRegionCode == null ? 43 : $geopoliticalRegionCode.hashCode());
        final Object $names = this.getNames();
        result = result * PRIME + ($names == null ? 43 : $names.hashCode());
        final Object $sovereignRightsCountryID = this.getSovereignRightsCountryID();
        result = result * PRIME + ($sovereignRightsCountryID == null ? 43 : $sovereignRightsCountryID.hashCode());
        final Object $jurisdictionCountryID = this.getJurisdictionCountryID();
        result = result * PRIME + ($jurisdictionCountryID == null ? 43 : $jurisdictionCountryID.hashCode());
        final Object $regionalFisheriesManagementOrganizationCode = this.getRegionalFisheriesManagementOrganizationCode();
        result = result * PRIME + ($regionalFisheriesManagementOrganizationCode == null ? 43 : $regionalFisheriesManagementOrganizationCode.hashCode());
        final Object $specifiedPhysicalFLUXGeographicalCoordinate = this.getSpecifiedPhysicalFLUXGeographicalCoordinate();
        result = result * PRIME + ($specifiedPhysicalFLUXGeographicalCoordinate == null ? 43 : $specifiedPhysicalFLUXGeographicalCoordinate.hashCode());
        final Object $postalStructuredAddresses = this.getPostalStructuredAddresses();
        result = result * PRIME + ($postalStructuredAddresses == null ? 43 : $postalStructuredAddresses.hashCode());
        final Object $physicalStructuredAddress = this.getPhysicalStructuredAddress();
        result = result * PRIME + ($physicalStructuredAddress == null ? 43 : $physicalStructuredAddress.hashCode());
        final Object $boundarySpecifiedPolygons = this.getBoundarySpecifiedPolygons();
        result = result * PRIME + ($boundarySpecifiedPolygons == null ? 43 : $boundarySpecifiedPolygons.hashCode());
        final Object $applicableFLUXCharacteristics = this.getApplicableFLUXCharacteristics();
        result = result * PRIME + ($applicableFLUXCharacteristics == null ? 43 : $applicableFLUXCharacteristics.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesFLUXLocationFact;
    }
}
