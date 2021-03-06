/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import lombok.ToString;
import static eu.europa.ec.fisheries.uvms.rules.entity.Template.LIST_ALL_ENABLED;

@Entity
@Table(name = "template")
@ToString(exclude = {"factRules"})
@NamedQueries({
        @NamedQuery(name = LIST_ALL_ENABLED, query = "FROM Template t LEFT JOIN FETCH t.factRules f WHERE f.disabled IS NULL OR f.disabled <> true")
})
public class Template implements Serializable {

    public static final String LIST_ALL_ENABLED = "template.listAllEnabled";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "template_id")
    private Long id;

    @Column(name = "template_name", nullable = false, unique = true)
    private String templateName;

    @Column(name = "fact_template", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private FactType type;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private Set<Rule> factRules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public FactType getType() {
        return type;
    }

    public void setType(FactType type) {
        this.type = type;
    }

    public Set<Rule> getFactRules() {
        return factRules;
    }

    public void setFactRules(Set<Rule> factRules) {
        this.factRules = factRules;
    }

}
