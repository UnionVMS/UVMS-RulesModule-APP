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

import java.util.ArrayList;
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
import eu.europa.ec.fisheries.uvms.rules.service.business.TemplateNameFactory;
import eu.europa.ec.fisheries.uvms.rules.service.lifecycle.RuleLifecycleContainer;

@Stateless
public class TemplateEngine {

    @Inject
    private RulesDomainModel rulesDb;

    @Inject
	private RuleLifecycleContainer ruleLifecycleContainer;
    
    public void evaluate(List<AbstractFact> fact) throws RulesModelException {
    	List<String> rules = generateAllRules(fact);
        // TODO do execution of rule
    	//ruleLifecycleContainer.triggerEvaluation(rules, fact);
    }
    
    private List<String> generateAllRules(List<AbstractFact> Type) throws RulesModelException {
        List<TemplateRuleMapDto> templates = rulesDb.getAllFactTemplatesAndRules();
        //Map<AdditionalTemplate, List<Rule>> additionalTemp = rulesDb.getAdditionalTemplates(type);
        //templates.addAll(additionalTemp);
        List<String> allRules = new ArrayList<>();
        for (TemplateRuleMapDto template : templates) {
        	//RuleGenerator datasource = ruleLifecycleContainer.getRuleGenerator(template);
            List<RuleType> rules = template.getRules();
            FactRuleGenerator factRuleGenerator = new FactRuleGenerator();
        	List<String> ruleDefinition  = factRuleGenerator.computeRules(template, rules);
        	allRules.addAll(ruleDefinition);
        }
    	return allRules;
    }
}
