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

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by padhyad on 4/19/2017.
 */
@Entity
@Table(name = "failedrule")
@ToString
@NamedQueries({
        @NamedQuery(name = FailedRule.DELETE_ALL, query = "DELETE FROM FailedRule")
})
public class FailedRule implements Serializable {

    public static final String DELETE_ALL = "failedRule.deleteAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rule_id")
    private Long id;

    @Column(name = "br_id", nullable = false)
    private String brId;

    public Long getId() {
        return id;
    }

    public String getBrId() {
        return brId;
    }

    public void setBrId(String brId) {
        this.brId = brId;
    }
}
