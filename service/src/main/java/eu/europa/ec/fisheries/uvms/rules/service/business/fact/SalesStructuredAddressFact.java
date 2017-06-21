package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.SalesFactHelper;

public class SalesStructuredAddressFact extends AbstractFact {

    private IdType id;
    private CodeType postcodeCode;
    private TextType buildingName;
    private TextType streetName;
    private TextType cityName;
    private IdType countryID;
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

    public IdType getID() {
        return this.id;
    }

    public CodeType getPostcodeCode() {
        return this.postcodeCode;
    }

    public TextType getBuildingName() {
        return this.buildingName;
    }

    public TextType getStreetName() {
        return this.streetName;
    }

    public TextType getCityName() {
        return this.cityName;
    }

    public IdType getCountryID() {
        return this.countryID;
    }

    public TextType getCitySubDivisionName() {
        return this.citySubDivisionName;
    }

    public TextType getCountryName() {
        return this.countryName;
    }

    public TextType getCountrySubDivisionName() {
        return this.countrySubDivisionName;
    }

    public TextType getBlockName() {
        return this.blockName;
    }

    public TextType getPlotIdentification() {
        return this.plotIdentification;
    }

    public TextType getPostOfficeBox() {
        return this.postOfficeBox;
    }

    public TextType getBuildingNumber() {
        return this.buildingNumber;
    }

    public TextType getStaircaseNumber() {
        return this.staircaseNumber;
    }

    public TextType getFloorIdentification() {
        return this.floorIdentification;
    }

    public TextType getRoomIdentification() {
        return this.roomIdentification;
    }

    public TextType getPostalArea() {
        return this.postalArea;
    }

    public void setID(IdType id) {
        this.id = id;
    }

    public void setPostcodeCode(CodeType postcodeCode) {
        this.postcodeCode = postcodeCode;
    }

    public void setBuildingName(TextType buildingName) {
        this.buildingName = buildingName;
    }

    public void setStreetName(TextType streetName) {
        this.streetName = streetName;
    }

    public void setCityName(TextType cityName) {
        this.cityName = cityName;
    }

    public void setCountryID(IdType countryID) {
        this.countryID = countryID;
    }

    public void setCitySubDivisionName(TextType citySubDivisionName) {
        this.citySubDivisionName = citySubDivisionName;
    }

    public void setCountryName(TextType countryName) {
        this.countryName = countryName;
    }

    public void setCountrySubDivisionName(TextType countrySubDivisionName) {
        this.countrySubDivisionName = countrySubDivisionName;
    }

    public void setBlockName(TextType blockName) {
        this.blockName = blockName;
    }

    public void setPlotIdentification(TextType plotIdentification) {
        this.plotIdentification = plotIdentification;
    }

    public void setPostOfficeBox(TextType postOfficeBox) {
        this.postOfficeBox = postOfficeBox;
    }

    public void setBuildingNumber(TextType buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public void setStaircaseNumber(TextType staircaseNumber) {
        this.staircaseNumber = staircaseNumber;
    }

    public void setFloorIdentification(TextType floorIdentification) {
        this.floorIdentification = floorIdentification;
    }

    public void setRoomIdentification(TextType roomIdentification) {
        this.roomIdentification = roomIdentification;
    }

    public void setPostalArea(TextType postalArea) {
        this.postalArea = postalArea;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesStructuredAddressFact)) return false;
        final SalesStructuredAddressFact other = (SalesStructuredAddressFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getID();
        final Object other$id = other.getID();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$postcodeCode = this.getPostcodeCode();
        final Object other$postcodeCode = other.getPostcodeCode();
        if (this$postcodeCode == null ? other$postcodeCode != null : !this$postcodeCode.equals(other$postcodeCode))
            return false;
        final Object this$buildingName = this.getBuildingName();
        final Object other$buildingName = other.getBuildingName();
        if (this$buildingName == null ? other$buildingName != null : !this$buildingName.equals(other$buildingName))
            return false;
        final Object this$streetName = this.getStreetName();
        final Object other$streetName = other.getStreetName();
        if (this$streetName == null ? other$streetName != null : !this$streetName.equals(other$streetName))
            return false;
        final Object this$cityName = this.getCityName();
        final Object other$cityName = other.getCityName();
        if (this$cityName == null ? other$cityName != null : !this$cityName.equals(other$cityName)) return false;
        final Object this$countryID = this.getCountryID();
        final Object other$countryID = other.getCountryID();
        if (this$countryID == null ? other$countryID != null : !this$countryID.equals(other$countryID)) return false;
        final Object this$citySubDivisionName = this.getCitySubDivisionName();
        final Object other$citySubDivisionName = other.getCitySubDivisionName();
        if (this$citySubDivisionName == null ? other$citySubDivisionName != null : !this$citySubDivisionName.equals(other$citySubDivisionName))
            return false;
        final Object this$countryName = this.getCountryName();
        final Object other$countryName = other.getCountryName();
        if (this$countryName == null ? other$countryName != null : !this$countryName.equals(other$countryName))
            return false;
        final Object this$countrySubDivisionName = this.getCountrySubDivisionName();
        final Object other$countrySubDivisionName = other.getCountrySubDivisionName();
        if (this$countrySubDivisionName == null ? other$countrySubDivisionName != null : !this$countrySubDivisionName.equals(other$countrySubDivisionName))
            return false;
        final Object this$blockName = this.getBlockName();
        final Object other$blockName = other.getBlockName();
        if (this$blockName == null ? other$blockName != null : !this$blockName.equals(other$blockName)) return false;
        final Object this$plotIdentification = this.getPlotIdentification();
        final Object other$plotIdentification = other.getPlotIdentification();
        if (this$plotIdentification == null ? other$plotIdentification != null : !this$plotIdentification.equals(other$plotIdentification))
            return false;
        final Object this$postOfficeBox = this.getPostOfficeBox();
        final Object other$postOfficeBox = other.getPostOfficeBox();
        if (this$postOfficeBox == null ? other$postOfficeBox != null : !this$postOfficeBox.equals(other$postOfficeBox))
            return false;
        final Object this$buildingNumber = this.getBuildingNumber();
        final Object other$buildingNumber = other.getBuildingNumber();
        if (this$buildingNumber == null ? other$buildingNumber != null : !this$buildingNumber.equals(other$buildingNumber))
            return false;
        final Object this$staircaseNumber = this.getStaircaseNumber();
        final Object other$staircaseNumber = other.getStaircaseNumber();
        if (this$staircaseNumber == null ? other$staircaseNumber != null : !this$staircaseNumber.equals(other$staircaseNumber))
            return false;
        final Object this$floorIdentification = this.getFloorIdentification();
        final Object other$floorIdentification = other.getFloorIdentification();
        if (this$floorIdentification == null ? other$floorIdentification != null : !this$floorIdentification.equals(other$floorIdentification))
            return false;
        final Object this$roomIdentification = this.getRoomIdentification();
        final Object other$roomIdentification = other.getRoomIdentification();
        if (this$roomIdentification == null ? other$roomIdentification != null : !this$roomIdentification.equals(other$roomIdentification))
            return false;
        final Object this$postalArea = this.getPostalArea();
        final Object other$postalArea = other.getPostalArea();
        if (this$postalArea == null ? other$postalArea != null : !this$postalArea.equals(other$postalArea))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getID();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $postcodeCode = this.getPostcodeCode();
        result = result * PRIME + ($postcodeCode == null ? 43 : $postcodeCode.hashCode());
        final Object $buildingName = this.getBuildingName();
        result = result * PRIME + ($buildingName == null ? 43 : $buildingName.hashCode());
        final Object $streetName = this.getStreetName();
        result = result * PRIME + ($streetName == null ? 43 : $streetName.hashCode());
        final Object $cityName = this.getCityName();
        result = result * PRIME + ($cityName == null ? 43 : $cityName.hashCode());
        final Object $countryID = this.getCountryID();
        result = result * PRIME + ($countryID == null ? 43 : $countryID.hashCode());
        final Object $citySubDivisionName = this.getCitySubDivisionName();
        result = result * PRIME + ($citySubDivisionName == null ? 43 : $citySubDivisionName.hashCode());
        final Object $countryName = this.getCountryName();
        result = result * PRIME + ($countryName == null ? 43 : $countryName.hashCode());
        final Object $countrySubDivisionName = this.getCountrySubDivisionName();
        result = result * PRIME + ($countrySubDivisionName == null ? 43 : $countrySubDivisionName.hashCode());
        final Object $blockName = this.getBlockName();
        result = result * PRIME + ($blockName == null ? 43 : $blockName.hashCode());
        final Object $plotIdentification = this.getPlotIdentification();
        result = result * PRIME + ($plotIdentification == null ? 43 : $plotIdentification.hashCode());
        final Object $postOfficeBox = this.getPostOfficeBox();
        result = result * PRIME + ($postOfficeBox == null ? 43 : $postOfficeBox.hashCode());
        final Object $buildingNumber = this.getBuildingNumber();
        result = result * PRIME + ($buildingNumber == null ? 43 : $buildingNumber.hashCode());
        final Object $staircaseNumber = this.getStaircaseNumber();
        result = result * PRIME + ($staircaseNumber == null ? 43 : $staircaseNumber.hashCode());
        final Object $floorIdentification = this.getFloorIdentification();
        result = result * PRIME + ($floorIdentification == null ? 43 : $floorIdentification.hashCode());
        final Object $roomIdentification = this.getRoomIdentification();
        result = result * PRIME + ($roomIdentification == null ? 43 : $roomIdentification.hashCode());
        final Object $postalArea = this.getPostalArea();
        result = result * PRIME + ($postalArea == null ? 43 : $postalArea.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesStructuredAddressFact;
    }

    // TODO test
    public boolean isCountryIdValid() {
        return SalesFactHelper.isCountryIdValid(countryID);
    }
}
