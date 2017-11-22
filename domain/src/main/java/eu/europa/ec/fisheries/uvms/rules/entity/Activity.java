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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import lombok.Data;

@Entity
@Table(name = "rawmoveactivity")
@XmlRootElement
@Data
public class Activity implements Serializable {

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
    @OneToOne
    private RawMovement rawMovement;

}