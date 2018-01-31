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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

//@formatter:off
@Entity
@Table(name = "alarmitem")
@XmlRootElement
//@formatter:on
public class AlarmItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "alarmitem_id")
    private Long id;

    @Column(name = "alarmitem_guid")
    private String guid;

    @Column(name = "alarmitem_rulename")
    private String ruleName;

    @Column(name = "alarmitem_ruleguid")
    private String ruleGuid;

    @Column(name = "alarmitem_updattim")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "alarmitem_upuser")
    @NotNull
    private String updatedBy;

    @JoinColumn(name = "alarmitem_alarmrep_id", referencedColumnName = "alarmrep_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AlarmReport alarmReport;

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

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleGuid() {
        return ruleGuid;
    }

    public void setRuleGuid(String ruleGuid) {
        this.ruleGuid = ruleGuid;
    }

    public AlarmReport getAlarmReport() {
        return alarmReport;
    }

    public void setAlarmReport(AlarmReport alarmReport) {
        this.alarmReport = alarmReport;
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

    @Override
    public String toString() {
        return "AlarmItem{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", ruleGuid='" + ruleGuid + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}