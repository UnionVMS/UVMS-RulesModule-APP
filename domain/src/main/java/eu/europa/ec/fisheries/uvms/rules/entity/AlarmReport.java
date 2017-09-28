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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;

//@formatter:off
@Entity
@Table(name = "alarmreport")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = UvmsConstants.FIND_ALARM_REPORT_BY_ID, query = "SELECT ar FROM AlarmReport ar WHERE ar.id = :id"),
    @NamedQuery(name = UvmsConstants.FIND_OPEN_ALARM_REPORT_BY_MOVEMENT_GUID, query = "SELECT ar FROM AlarmReport ar WHERE ar.rawMovement.guid = :movementGuid and status = 'OPEN'"),
    @NamedQuery(name = UvmsConstants.FIND_ALARM_BY_GUID, query = "SELECT ar FROM AlarmReport ar WHERE ar.guid = :guid"),
    @NamedQuery(name = UvmsConstants.FIND_ALARM_REPORT_BY_ASSET_GUID_AND_RULE_GUID, query = "SELECT ar FROM AlarmReport ar left join ar.alarmItemList ai WHERE ar.assetGuid = :assetGuid and ar.status = 'OPEN' and ai.ruleGuid = :ruleGuid"),
    @NamedQuery(name = UvmsConstants.COUNT_OPEN_ALARMS, query = "SELECT count(ar) FROM AlarmReport ar where ar.status = 'OPEN'")
})
//@formatter:on
public class AlarmReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "alarmrep_id")
    private Long id;

    @Column(name = "alarmrep_plugintype")
    private String pluginType;

    @Column(name = "alarmrep_guid")
    private String guid;

    @Column(name = "alarmrep_assetguid")
    private String assetGuid;

    @Column(name = "alarmrep_status")
    private String status;

    @Column(name = "alarmrep_recipient")
    private String recipient;

    @Column(name = "alarmrep_createddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "alarmrep_updattim")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "alarmrep_upuser")
    @NotNull
    private String updatedBy;

    @OneToOne(mappedBy = "alarmReport", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RawMovement rawMovement;

    @OneToMany(mappedBy = "alarmReport", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AlarmItem> alarmItemList;

    public AlarmReport() {
        this.guid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAssetGuid() {
        return assetGuid;
    }

    public void setAssetGuid(String assetGuid) {
        this.assetGuid = assetGuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

    public RawMovement getRawMovement() {
        return rawMovement;
    }

    public void setRawMovement(RawMovement rawMovement) {
        this.rawMovement = rawMovement;
    }

    public List<AlarmItem> getAlarmItemList() {
        if (alarmItemList == null) {
            alarmItemList = new ArrayList<AlarmItem>();
        }
        return alarmItemList;
    }

    public void setAlarmItemList(List<AlarmItem> alarmItemList) {
        this.alarmItemList = alarmItemList;
    }

    @Override
    public String toString() {
        return "AlarmReport{" +
                "id=" + id +
                ", pluginType='" + pluginType + '\'' +
                ", guid='" + guid + '\'' +
                ", assetGuid='" + assetGuid + '\'' +
                ", status='" + status + '\'' +
                ", recipient='" + recipient + '\'' +
                ", createdDate=" + createdDate +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
//                ", rawMovement=" + rawMovement +
//                ", alarmItemList=" + alarmItemList +
                '}';
    }
}