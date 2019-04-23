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
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.domain.Audit;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "rule")
@ToString
@Data
public class Rule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rule_id")
    private Long id;

    @AttributeOverrides({
            @AttributeOverride(name = "createdOn",
                    column = @Column(name = "rule_created_on")),
    })
    @Embedded
    private Audit audit;

    @Column(name = "br_id", nullable = false, unique = true)
    private String brId;
    @Column(name = "note", nullable = false, columnDefinition = "text")
    private String note;

    @Column(name = "error_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ErrorType errorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "rule", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RuleContextExpression> ruleContextExpressionList;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "property_names", nullable = false)
    private String propertyNames;

    private Boolean disabled;

    public Long getId() {
        return id;
    }

    @PrePersist
    private void onCreate() {
        audit = new Audit(DateUtils.nowUTC().toDate());
    }
}
