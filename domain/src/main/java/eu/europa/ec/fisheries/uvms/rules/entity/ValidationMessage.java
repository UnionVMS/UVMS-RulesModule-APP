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

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by padhyad on 5/3/2017.
 */
@Entity
@Table(name = "validationmessage")
@NamedQueries({
        @NamedQuery(name = ValidationMessage.BY_ID,
                query = "select vm from ValidationMessage vm " +
                        "inner join vm.messageIds vms " +
                        "where vms.messageId in (:messageIds) ")
})
public class ValidationMessage implements Serializable {

    public static final String BY_ID = "ValidationMessage.getById";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "error_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ErrorType errorType;

    @Column(name = "br_id", nullable = false)
    private String brId;

    @Column(columnDefinition = "text", name = "message", nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_message_id")
    private RawMessage rawMessage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "validationMessage", cascade = CascadeType.ALL)
    private Set<MessageId> messageIds;

    @Column(columnDefinition = "text", name = "xpath_list")
    private String xpathList;

    @Column(name = "level", nullable = false)
    private String level;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ErrorType getErrorType() {
        return errorType;
    }
    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }
    public String getBrId() {
        return brId;
    }
    public void setBrId(String brId) {
        this.brId = brId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public RawMessage getRawMessage() {
        return rawMessage;
    }
    public void setRawMessage(RawMessage rawMessage) {
        this.rawMessage = rawMessage;
    }
    public Set<MessageId> getMessageIds() {
        return messageIds;
    }
    public void setMessageIds(Set<MessageId> messageIds) {
        this.messageIds = messageIds;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getXpathList() {
        return xpathList;
    }
    public void setXpathList(String xpathList) {
        this.xpathList = xpathList;
    }
}
