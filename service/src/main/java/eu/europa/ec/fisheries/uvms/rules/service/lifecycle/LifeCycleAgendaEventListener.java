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

package eu.europa.ec.fisheries.uvms.rules.service.lifecycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;

public class LifeCycleAgendaEventListener implements AgendaEventListener {

	@Override
	public void afterMatchFired(AfterMatchFiredEvent event) {
		// TODO Auto-generated method stub
		// in the rule define the metadata
		// @LegalRequirement("Section 103 RTA 1988")
		Rule rule  = event.getMatch().getRule();
		Map<String,Object> metadata = rule.getMetaData();
		String ruleType = (String) rule.getMetaData().get("rule_type");
		
		// getting fact object
		
		Collection factObjects = event.getKieRuntime().getObjects();
		AbstractFact factObject  = (AbstractFact) factObjects.iterator().next();

		event.getKieRuntime().setGlobal("ok", false);
		List<String> warnings  = (List<String>)event.getKieRuntime().getGlobal("warnings");
		List<String> errors = (List<String>)event.getKieRuntime().getGlobal("errors");

		// checking type of rule from the metadata

		if ("Warning".equals(ruleType)){
			String ruleWarning = (String) rule.getMetaData().get("rule_warning");
			warnings.add(ruleWarning);
		}else if ("Error".equals(ruleType)){
			String ruleError = (String) rule.getMetaData().get("rule_error");
			warnings.add(ruleError);
		}
	}

	@Override
	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void agendaGroupPopped(AgendaGroupPoppedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void agendaGroupPushed(AgendaGroupPushedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeMatchFired(BeforeMatchFiredEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void matchCancelled(MatchCancelledEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void matchCreated(MatchCreatedEvent arg0) {
		// TODO Auto-generated method stub

	}

}
