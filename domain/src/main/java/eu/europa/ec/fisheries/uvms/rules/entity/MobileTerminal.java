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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

//@formatter:off
@Entity
@Table(name = "rawmovemobileterminal")
@XmlRootElement
//@formatter:on
public class MobileTerminal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rawmovemob_id")
    private Long id;

    @Column(name = "rawmovemob_guid")
    private String guid;

    @Column(name = "rawmovemob_connectid")
    private String connectId;

    @JoinColumn(name = "rawmovemob_rawmove_id", referencedColumnName = "rawmove_id")
    @OneToOne(fetch = FetchType.LAZY)
    private RawMovement rawMovement;

    @OneToMany(mappedBy = "mobileTerminal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MobileTerminalId> mobileTerminalId;

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

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public RawMovement getRawMovement() {
        return rawMovement;
    }

    public void setRawMovement(RawMovement rawMovement) {
        this.rawMovement = rawMovement;
    }

    public List<MobileTerminalId> getMobileTerminalId() {
        return mobileTerminalId;
    }

    public void setMobileTerminalId(List<MobileTerminalId> mobileTerminalId) {
        this.mobileTerminalId = mobileTerminalId;
    }

    @Override
    public String toString() {
        return "MobileTerminal{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", connectId='" + connectId + '\'' +
//                ", mobileTerminalId=" + mobileTerminalId +
                '}';
    }
}