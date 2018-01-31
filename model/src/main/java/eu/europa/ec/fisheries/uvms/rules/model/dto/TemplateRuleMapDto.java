/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries � European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.model.dto;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by padhyad on 4/13/2017.
 */
public class TemplateRuleMapDto implements Serializable {

    private TemplateType templateType;

    private List<RuleType> rules;

    private List<ExternalRuleType> externalRules;


    public TemplateRuleMapDto() {
        super();
    }


    public TemplateRuleMapDto(TemplateType templateType, List<RuleType> rules) {
        this.templateType = templateType;
        this.rules = rules;
    }


    public TemplateRuleMapDto(TemplateType templateType, List<ExternalRuleType> externalRules, List<RuleType> rules) {
        this.templateType = templateType;
        this.externalRules = externalRules;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public List<RuleType> getRules() {
        return rules;
    }

    public void setRules(List<RuleType> rules) {
        this.rules = rules;
    }

    public List<ExternalRuleType> getExternalRules() {
        return externalRules;
    }

    public void setExternalRules(List<ExternalRuleType> externalRules) {
        this.externalRules = externalRules;
    }
}
