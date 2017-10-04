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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

//@formatter:off
@Entity
@Table(name = "rawmoveactivity")
@XmlRootElement
//@formatter:on
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rawmoveact_id")
    private Long id;

    @Column(name = "rawmoveact_messagetype")
    private String messageType;

    @Column(name = "rawmoveact_messageid")
    private String messageId;

    @Column(name = "rawmoveact_callback")
    private String callback;

    @JoinColumn(name = "rawmoveact_rawmove_id", referencedColumnName = "rawmove_id")
    @OneToOne(fetch = FetchType.LAZY)
    private RawMovement rawMovement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public RawMovement getRawMovement() {
        return rawMovement;
    }

    public void setRawMovement(RawMovement rawMovement) {
        this.rawMovement = rawMovement;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", messageType='" + messageType + '\'' +
                ", messageId='" + messageId + '\'' +
                ", callback='" + callback + '\'' +
                '}';
    }
}