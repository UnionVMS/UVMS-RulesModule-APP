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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;
import lombok.Data;

@Entity
@Table(name = "ticket")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = UvmsConstants.FIND_TICKET_BY_GUID, query = "SELECT t FROM Ticket t WHERE t.guid = :guid"),
        @NamedQuery(name = UvmsConstants.FIND_TICKET_BY_ASSET_AND_RULE, query = "SELECT t FROM Ticket t WHERE t.assetGuid = :assetGuid and status <> 'CLOSED' and ruleGuid = :ruleGuid"),
        @NamedQuery(name = UvmsConstants.COUNT_OPEN_TICKETS, query = "SELECT count(t) FROM Ticket t where t.status = 'OPEN' AND t.ruleGuid IN :validRuleGuids"),
        @NamedQuery(name = UvmsConstants.FIND_TICKETS_BY_MOVEMENTS, query = "SELECT t FROM Ticket t where t.movementGuid IN :movements"),
        @NamedQuery(name = UvmsConstants.COUNT_TICKETS_BY_MOVEMENTS, query = "SELECT count(t) FROM Ticket t where t.movementGuid IN :movements"),
        @NamedQuery(name = UvmsConstants.COUNT_ASSETS_NOT_SENDING, query = "SELECT count(t) FROM Ticket t where t.ruleGuid = :ruleGuid")
})
@Data
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ticket_id")
    private Long id;

    @Column(name = "ticket_guid")
    private String guid;

    @Column(name = "ticket_assetguid")
    private String assetGuid;

    @Column(name = "ticket_mobileterminalguid")
    private String mobileTerminalGuid;

    @Column(name = "ticket_channelguid")
    private String channelGuid;

    @Column(name = "ticket_ruleguid")
    private String ruleGuid;

    @Column(name = "ticket_rulename")
    private String ruleName;

    @Column(name = "ticket_recipient")
    private String recipient;

    @Column(name = "ticket_movementguid")
    private String movementGuid;

    @Column(name = "ticket_status")
    private String status;

    @Column(name = "ticket_count")
    private Long ticketCount;

    @Column(name = "ticket_createddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "ticket_updattim")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "ticket_upuser")
    @NotNull
    private String updatedBy;

    public Ticket() {
        this.guid = UUID.randomUUID().toString();
    }

}