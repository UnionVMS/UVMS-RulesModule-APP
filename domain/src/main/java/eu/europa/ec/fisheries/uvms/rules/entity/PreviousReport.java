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

import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

//@formatter:off
@Entity
@Table(name = "previousreport")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = UvmsConstants.GET_ALL_PREVIOUS_REPORTS, query = "SELECT pr FROM PreviousReport pr"),
        @NamedQuery(name = UvmsConstants.FIND_PREVIOUS_REPORT_BY_ASSET_GUID, query = "SELECT pr FROM PreviousReport pr WHERE assetGuid = :assetGuid")
})
//@formatter:on
public class PreviousReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "prevrep_id")
    private Long id;

    @Column(name = "prevrep_assetguid", unique = true)
    private String assetGuid;

    @Column(name = "prevrep_positiontime")
    private Date positionTime;

    @Column(name = "prevrep_updattim")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "prevrep_upuser")
    @NotNull
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetGuid() {
        return assetGuid;
    }

    public void setAssetGuid(String assetGuid) {
        this.assetGuid = assetGuid;
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

    @Override
    public String toString() {
        return "PreviousReport{" +
                "id=" + id +
                ", assetGuid='" + assetGuid + '\'' +
                ", positionTime=" + positionTime +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}