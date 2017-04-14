/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

@Stateless
public class TemplateEngine {

    @Inject
    private RulesDomainModel rulesDb;
    
    public void evaluateFacts(List<AbstractFact> facts) throws RulesModelException {
        List<TemplateRuleMapDto> templates = rulesDb.getAllFactTemplatesAndRules();
        if (!templates.isEmpty()) {
            FactRuleEvaluator ruleEvaluator = new FactRuleEvaluator();
            ruleEvaluator.computeRules(templates, facts);
        }
        /*for (TemplateRuleMapDto template : templates) {
            AbstractFact factToEvaluate = getFactToEvaluate(template, facts);
            templatesWithFacts.put(template, factToEvaluate);
        }
        if (!templatesWithFacts.isEmpty()) {

            ruleEvaluator.computeRules(templatesWithFacts);
        }*/
    }

/*    private AbstractFact getFactToEvaluate(TemplateRuleMapDto template, List<AbstractFact> facts) {
        for (AbstractFact fact : facts) {
            if (fact.getFactType().equals(template.getTemplateType().getType())) {
                return fact;
            }
        }
        return null;
    }*/
}
