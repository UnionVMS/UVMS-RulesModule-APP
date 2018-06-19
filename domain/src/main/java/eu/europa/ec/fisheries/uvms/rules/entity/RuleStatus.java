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

import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleStatusType;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by padhyad on 7/14/2017.
 */
@Entity
@Table(name = "rulestatus")
@ToString
@NamedQueries({
        @NamedQuery(name = RuleStatus.DELETE_ALL, query = "DELETE FROM RuleStatus")
})
public class RuleStatus implements Serializable {

    public static final String DELETE_ALL = "ruleStatus.deleteAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rule_status_id")
    private Long id;

    @Column(name = "rule_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RuleStatusType ruleStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RuleStatusType getRuleStatus() {
        return ruleStatus;
    }

    public void setRuleStatus(RuleStatusType ruleStatus) {
        this.ruleStatus = ruleStatus;
    }
}
