package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.Date;

public class PositionFact {
    // Temp raw factoids
    private String VESSEL_CFR;
    private String MOBILE_TERMINAL_Member_id;

    private Integer id;
    private String guid;
    private String assetName;
    private String country;
    private Double latitude;
    private Double longitude;
    private Date timestamp;
    private String comment;
    private Double calculatedSpeed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getVESSEL_CFR() {
        return VESSEL_CFR;
    }

    public void setVESSEL_CFR(String vESSEL_CFR) {
        VESSEL_CFR = vESSEL_CFR;
    }

    public String getMOBILE_TERMINAL_Member_id() {
        return MOBILE_TERMINAL_Member_id;
    }

    public void setMOBILE_TERMINAL_Member_id(String mOBILE_TERMINAL_Member_id) {
        MOBILE_TERMINAL_Member_id = mOBILE_TERMINAL_Member_id;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getCalculatedSpeed() {
        return calculatedSpeed;
    }

    public void setCalculatedSpeed(Double calculatedSpeed) {
        this.calculatedSpeed = calculatedSpeed;
    }

}
