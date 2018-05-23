package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.SalesFactHelper;

import java.util.Objects;

public class SalesStructuredAddressFact extends SalesAbstractFact {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesStructuredAddressFact)) return false;
        SalesStructuredAddressFact that = (SalesStructuredAddressFact) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(postcodeCode, that.postcodeCode) &&
                Objects.equals(buildingName, that.buildingName) &&
                Objects.equals(streetName, that.streetName) &&
                Objects.equals(cityName, that.cityName) &&
                Objects.equals(countryID, that.countryID) &&
                Objects.equals(citySubDivisionName, that.citySubDivisionName) &&
                Objects.equals(countryName, that.countryName) &&
                Objects.equals(countrySubDivisionName, that.countrySubDivisionName) &&
                Objects.equals(blockName, that.blockName) &&
                Objects.equals(plotIdentification, that.plotIdentification) &&
                Objects.equals(postOfficeBox, that.postOfficeBox) &&
                Objects.equals(buildingNumber, that.buildingNumber) &&
                Objects.equals(staircaseNumber, that.staircaseNumber) &&
                Objects.equals(floorIdentification, that.floorIdentification) &&
                Objects.equals(roomIdentification, that.roomIdentification) &&
                Objects.equals(postalArea, that.postalArea) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postcodeCode, buildingName, streetName, cityName, countryID, citySubDivisionName, countryName, countrySubDivisionName, blockName, plotIdentification, postOfficeBox, buildingNumber, staircaseNumber, floorIdentification, roomIdentification, postalArea, creationDateOfMessage);
    }
}
