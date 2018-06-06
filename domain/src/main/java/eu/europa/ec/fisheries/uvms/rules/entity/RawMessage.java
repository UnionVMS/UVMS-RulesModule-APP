/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * Created by padhyad on 5/3/2017.
 */
@Entity
@Table(name = "rawmessage")
@NamedQueries({
        @NamedQuery(name = RawMessage.BY_GUID,
                query = "SELECT DISTINCT rawMsg from RawMessage rawMsg " +
                        "LEFT JOIN FETCH rawMsg.validationMessages " +
                        "WHERE rawMsg.guid=:rawMsgGuid AND ((:msgType is NULL) OR rawMsg.rawMsgType=:msgType)")
})
public class RawMessage implements Serializable {

    public static final String BY_GUID = "rawMsg.byGuid";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "raw_message_guid")
    private String guid;

    @Column(columnDefinition = "text", name = "raw_message", nullable = false)
    private String rawMessage;

    @Enumerated(EnumType.STRING)
    @Column(name="raw_msg_type")
    private RawMsgType rawMsgType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rawMessage", cascade = CascadeType.ALL)
    private Set<ValidationMessage> validationMessages;

    @Column(name = "mdc_request_id")
    private String mdcRequestId;

    @PrePersist
    public void prePersist(){
        if (StringUtils.isNotEmpty(rawMessage)){
            rawMessage = rawMessage.replaceAll("<\\?xml(.+?)\\?>", "")
                    .replaceAll("(?s)<!--.*?-->", "").trim();
        }

        setMdcRequestId(MDC.get("requestId"));
    }
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
    public String getRawMessage() {
        return rawMessage;
    }
    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }
    public Set<ValidationMessage> getValidationMessages() {
        return validationMessages;
    }
    public void setValidationMessages(Set<ValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }
    public RawMsgType getRawMsgType() {
        return rawMsgType;
    }
    public void setRawMsgType(RawMsgType rawMsgType) {
        this.rawMsgType = rawMsgType;
    }

    public String getMdcRequestId() {
        return mdcRequestId;
    }

    public void setMdcRequestId(String mdcRequestId) {
        this.mdcRequestId = mdcRequestId;
    }
}
