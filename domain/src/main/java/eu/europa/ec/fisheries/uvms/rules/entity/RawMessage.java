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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by padhyad on 5/3/2017.
 */
@Entity
@Table(name = "rawmessage")
public class RawMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(columnDefinition = "text", name = "raw_message", nullable = false)
    private String rawMessage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rawMessage", cascade = CascadeType.ALL)
    private Set<ValidationMessage> validationMessages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
