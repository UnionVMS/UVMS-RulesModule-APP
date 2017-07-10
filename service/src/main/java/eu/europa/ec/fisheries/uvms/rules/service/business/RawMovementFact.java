/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.Date;

import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;

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

    // Position
    private Double altitude;
    private Double latitude;
    private Double longitude;

    // Asset
    private String assetGuid;
    private String cfr;
    private String ircs;
    private String assetName;         // from FLUX/MANUAL
    private String flagState;         // from FLUX/MANUAL
    private String externalMarking;   // from FLUX/MANUAL

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

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getFlagState() {
        return flagState;
    }

    public void setFlagState(String flagState) {
        this.flagState = flagState;
    }

    public String getExternalMarking() {
        return externalMarking;
    }

    public void setExternalMarking(String externalMarking) {
        this.externalMarking = externalMarking;
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

    public String getMobileTerminalMemberNumber() {
        return mobileTerminalMemberNumber;
    }

    public void setMobileTerminalMemberNumber(String mobileTerminalMemberNumber) {
        this.mobileTerminalMemberNumber = mobileTerminalMemberNumber;
    }

    public String getMobileTerminalSerialNumber() {
        return mobileTerminalSerialNumber;
    }

    public void setMobileTerminalSerialNumber(String mobileTerminalSerialNumber) {
        this.mobileTerminalSerialNumber = mobileTerminalSerialNumber;
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

    public String getAssetGuid() {
        return assetGuid;
    }

    public void setAssetGuid(String assetGuid) {
        this.assetGuid = assetGuid;
    }

    public String getCfr() {
        return cfr;
    }

    public void setCfr(String cfr) {
        this.cfr = cfr;
    }

    public String getIrcs() {
        return ircs;
    }

    public void setIrcs(String ircs) {
        this.ircs = ircs;
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
                ", assetName='" + assetName + '\'' +
                ", flagState='" + flagState + '\'' +
                ", externalMarking='" + externalMarking + '\'' +
                ", activityCallback='" + activityCallback + '\'' +
                ", activityMessageId='" + activityMessageId + '\'' +
                ", activityMessageType='" + activityMessageType + '\'' +
                ", altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", assetGuid='" + assetGuid + '\'' +
                ", cfr='" + cfr + '\'' +
                ", ircs='" + ircs + '\'' +
                ", mobileTerminalMemberNumber='" + mobileTerminalMemberNumber + '\'' +
                ", mobileTerminalDnid='" + mobileTerminalDnid + '\'' +
                ", mobileTerminalSerialNumber='" + mobileTerminalSerialNumber + '\'' +
                ", mobileTerminalType='" + mobileTerminalType + '\'' +
                ", mobileTerminalConnectId='" + mobileTerminalConnectId + '\'' +
                '}';
    }

}