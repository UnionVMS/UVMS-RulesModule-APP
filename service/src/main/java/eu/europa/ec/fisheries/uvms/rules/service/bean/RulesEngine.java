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

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.lifecycle.RuleLifecycleContainer;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class RulesEngine {
 
	@Inject
	private TemplateEngine templateEngine;

	@Inject
	private RuleLifecycleContainer ruleLifecycleContainer;

	@PostConstruct
	public void initialize() {
		List<TemplateRuleGenerator> templateRuleGenerator = new ArrayList<>();
		templateRuleGenerator.add(new CheckNullRuleGenerator());
		templateRuleGenerator.add(new WarningsRuleGenerator());
		ruleLifecycleContainer.registerTemplateDatasource(templateRuleGenerator);
	}
	
    public void evaluate(AbstractFact fact) throws RulesModelException {
    	templateEngine.evaluate(fact);
    }
}
