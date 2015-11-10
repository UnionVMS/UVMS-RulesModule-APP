package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RawMovementFact {
    // TODO: Add asset id to RawMovement for FLUX stuff.

    private RawMovementType rawMovementType;
    private boolean ok = true;
    private String pluginType;
    private String alarmGuid;

    // Base
    private String comChannelType;
    private String movementGuid;
    private String movementType; // MovementTypeType
    private Date positionTime;
    private Double reportedCourse;
    private Double reportedSpeed;
    private String source;
    private String statusCode;

    // Activity
    private String activityCallback;
    private String activityMessageId;
    private String activityMessageType;

    // AssetType
    private String assetType;

    // Position
    private Double altitude;
    private Double latitude;
    private Double longitude;

    // Vessel
    private String vesselConnectId;
    private String vesselCfr;
    private String vesselIrcs;
    private String vesselImo;
    private String vesselMmsi;

    // Mobile Terminal
    private String mobileTerminalMemberNumber;
    private String mobileTerminalDnid;
    private String mobileTerminalSerialNumber;
    private String mobileTerminalType;
    private String mobileTerminalConnectId;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
    }

    public String getAlarmGuid() {
        return alarmGuid;
    }

    public void setAlarmGuid(String alarmGuid) {
        this.alarmGuid = alarmGuid;
    }

    public RawMovementType getRawMovementType() {
        return rawMovementType;
    }

    public void setRawMovementType(RawMovementType rawMovementType) {
        this.rawMovementType = rawMovementType;
    }

    public Date getPositionTime() {
        return positionTime;
    }

    public void setPositionTime(Date positionTime) {
        this.positionTime = positionTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Double getReportedCourse() {
        return reportedCourse;
    }

    public void setReportedCourse(Double reportedCourse) {
        this.reportedCourse = reportedCourse;
    }

    public Double getReportedSpeed() {
        return reportedSpeed;
    }

    public void setReportedSpeed(Double reportedSpeed) {
        this.reportedSpeed = reportedSpeed;
    }

    public String getMobileTerminalConnectId() {
        return mobileTerminalConnectId;
    }

    public void setMobileTerminalConnectId(String mobileTerminalConnectId) {
        this.mobileTerminalConnectId = mobileTerminalConnectId;
    }

    public String getMovementGuid() {
        return movementGuid;
    }

    public void setMovementGuid(String movementGuid) {
        this.movementGuid = movementGuid;
    }

    public String getComChannelType() {
        return comChannelType;
    }

    public void setComChannelType(String comChannelType) {
        this.comChannelType = comChannelType;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
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

    public String getActivityCallback() {
        return activityCallback;
    }

    public void setActivityCallback(String activityCallback) {
        this.activityCallback = activityCallback;
    }

    public String getActivityMessageId() {
        return activityMessageId;
    }

    public void setActivityMessageId(String activityMessageId) {
        this.activityMessageId = activityMessageId;
    }

    public String getActivityMessageType() {
        return activityMessageType;
    }

    public void setActivityMessageType(String activityMessageType) {
        this.activityMessageType = activityMessageType;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getMobileTerminalMemberNumber() {
        return mobileTerminalMemberNumber;
    }

    public String getMobileTerminalSerialNumber() {
        return mobileTerminalSerialNumber;
    }

    public void setMobileTerminalSerialNumber(String mobileTerminalSerialNumber) {
        this.mobileTerminalSerialNumber = mobileTerminalSerialNumber;
    }

    public void setMobileTerminalMemberNumber(String mobileTerminalMemberNumber) {
        this.mobileTerminalMemberNumber = mobileTerminalMemberNumber;
    }

    public String getMobileTerminalDnid() {
        return mobileTerminalDnid;
    }

    public void setMobileTerminalDnid(String mobileTerminalDnid) {
        this.mobileTerminalDnid = mobileTerminalDnid;
    }

    public String getMobileTerminalType() {
        return mobileTerminalType;
    }

    public void setMobileTerminalType(String mobileTerminalType) {
        this.mobileTerminalType = mobileTerminalType;
    }

    public String getVesselConnectId() {
        return vesselConnectId;
    }

    public void setVesselConnectId(String vesselConnectId) {
        this.vesselConnectId = vesselConnectId;
    }

    public String getVesselCfr() {
        return vesselCfr;
    }

    public void setVesselCfr(String vesselCfr) {
        this.vesselCfr = vesselCfr;
    }

    public String getVesselIrcs() {
        return vesselIrcs;
    }

    public void setVesselIrcs(String vesselIrcs) {
        this.vesselIrcs = vesselIrcs;
    }

    public String getVesselImo() {
        return vesselImo;
    }

    public void setVesselImo(String vesselImo) {
        this.vesselImo = vesselImo;
    }

    public String getVesselMmsi() {
        return vesselMmsi;
    }

    public void setVesselMmsi(String vesselMmsi) {
        this.vesselMmsi = vesselMmsi;
    }

    @Override
    public String toString() {
        return "RawMovementFact{" +
                "rawMovementType=" + rawMovementType +
                ", ok=" + ok +
                ", pluginType='" + pluginType + '\'' +
                ", alarmGuid='" + alarmGuid + '\'' +
                ", comChannelType='" + comChannelType + '\'' +
                ", movementGuid='" + movementGuid + '\'' +
                ", movementType='" + movementType + '\'' +
                ", positionTime=" + positionTime +
                ", reportedCourse=" + reportedCourse +
                ", reportedSpeed=" + reportedSpeed +
                ", source='" + source + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", activityCallback='" + activityCallback + '\'' +
                ", activityMessageId='" + activityMessageId + '\'' +
                ", activityMessageType='" + activityMessageType + '\'' +
                ", assetType='" + assetType + '\'' +
                ", altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", vesselConnectId='" + vesselConnectId + '\'' +
                ", vesselCfr='" + vesselCfr + '\'' +
                ", vesselIrcs='" + vesselIrcs + '\'' +
                ", vesselImo='" + vesselImo + '\'' +
                ", vesselMmsi='" + vesselMmsi + '\'' +
                ", mobileTerminalMemberNumber='" + mobileTerminalMemberNumber + '\'' +
                ", mobileTerminalDnid='" + mobileTerminalDnid + '\'' +
                ", mobileTerminalSerialNumber='" + mobileTerminalSerialNumber + '\'' +
                ", mobileTerminalType='" + mobileTerminalType + '\'' +
                ", mobileTerminalConnectId='" + mobileTerminalConnectId + '\'' +
                '}';
    }

}
