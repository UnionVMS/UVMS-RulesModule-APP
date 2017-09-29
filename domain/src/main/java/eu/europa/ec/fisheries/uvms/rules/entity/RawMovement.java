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
package eu.europa.ec.fisheries.uvms.rules.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

//@formatter:off
@Entity
@Table(name = "rawmovement")
@XmlRootElement
//@formatter:on
public class RawMovement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rawmove_id")
    private Long id;

    @Column(name = "rawmove_guid")
    private String guid;

    @Column(name = "rawmove_status")
    private String status;

    @Column(name = "rawmove_comchanneltype")
    private String comChannelType;

    @Column(name = "rawmove_connectid")
    private String connectId;

    @Column(name = "rawmove_reportedspeed")
    private Double reportedSpeed;

    @Column(name = "rawmove_reportedcourse")
    private Double reportedCourse;

    @Column(name = "rawmove_movementtype")
    private String movementType;

    @Column(name = "rawmove_source")
    private String source;

    @Column(name = "rawmove_active")
    private Boolean active;

    @Column(name = "rawmove_positiontime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date positionTime;

    @Column(name = "rawmove_updattim")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "rawmove_upuser")
    @NotNull
    private String updatedBy;

    @Column(name = "rawmove_assetname")
    private String assetName;
    @Column(name = "rawmove_flagstate")
    private String flagState;
    @Column(name = "rawmove_externalmarking")
    private String externalMarking;


    @JoinColumn(name = "rawmove_alarmrep_id", referencedColumnName = "alarmrep_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AlarmReport alarmReport;

    @OneToOne(mappedBy = "rawMovement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Asset asset;

    @OneToOne(mappedBy = "rawMovement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Position position;

    @OneToOne(mappedBy = "rawMovement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Activity activity;

    @OneToOne(mappedBy = "rawMovement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MobileTerminal mobileTerminal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComChannelType() {
        return comChannelType;
    }

    public void setComChannelType(String comChannelType) {
        this.comChannelType = comChannelType;
    }

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public Double getReportedSpeed() {
        return reportedSpeed;
    }

    public void setReportedSpeed(Double reportedSpeed) {
        this.reportedSpeed = reportedSpeed;
    }

    public Double getReportedCourse() {
        return reportedCourse;
    }

    public void setReportedCourse(Double reportedCourse) {
        this.reportedCourse = reportedCourse;
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

    public Date getPositionTime() {
        return positionTime;
    }

    public void setPositionTime(Date positionTime) {
        this.positionTime = positionTime;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public AlarmReport getAlarmReport() {
        return alarmReport;
    }

    public void setAlarmReport(AlarmReport alarmReport) {
        this.alarmReport = alarmReport;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MobileTerminal getMobileTerminal() {
        return mobileTerminal;
    }

    public void setMobileTerminal(MobileTerminal mobileTerminal) {
        this.mobileTerminal = mobileTerminal;
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

    @Override
    public String toString() {
        return "RawMovement{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", status='" + status + '\'' +
                ", comChannelType='" + comChannelType + '\'' +
                ", connectId='" + connectId + '\'' +
                ", reportedSpeed=" + reportedSpeed +
                ", reportedCourse=" + reportedCourse +
                ", movementType='" + movementType + '\'' +
                ", source='" + source + '\'' +
                ", positionTime=" + positionTime +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                ", assetName='" + assetName + '\'' +
                ", flagState='" + flagState + '\'' +
                ", externalMarking='" + externalMarking + '\'' +
//                ", asset=" + asset +
//                ", position=" + position +
//                ", activity=" + activity +
//                ", mobileTerminal=" + mobileTerminal +
                '}';
    }
}