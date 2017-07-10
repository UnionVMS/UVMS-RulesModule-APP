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

import javax.ejb.Stateless;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

@Slf4j
@Stateless
public class RuleLifecycleContainer {

    public void triggerEvaluation(List<String> rules, AbstractFact fact) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        for (String rule : rules) {
            int index = 0;
            StringBuilder ruleName = new StringBuilder("src/main/resources/rule");
            ruleName.append(index).append(".drl");
            kfs.write(ruleName.toString(), kieServices.getResources().newReaderResource(new StringReader(rule)));
        }
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();

        // check there have been no errors for rule setup
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            log.error(results.getMessages().toString());
            throw new IllegalStateException("errors");
        }

        KieContainer kieContainer = kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId());
        KieSession kieSession = kieContainer.newKieSession();

        setGlobalVariables(kieSession);
        addListeners(kieSession);
        // insert facts and fire rules
        kieSession.insert(fact);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    private void setGlobalVariables(KieSession kieSession) {
        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        kieSession.setGlobal("ok", true);
        kieSession.setGlobal("warnings", warnings);
        kieSession.setGlobal("errors", errors);
    }

    private void addListeners(KieSession kieSession) {
        AgendaEventListener lifecycleAgendaEventListener = new LifeCycleAgendaEventListener();
        kieSession.addEventListener(lifecycleAgendaEventListener);

        ProcessEventListener lifecycleProcessEventListener = new LifecycleProcessEventListener();
        kieSession.addEventListener(lifecycleProcessEventListener);

        RuleRuntimeEventListener ruleRuntimeEventListener = new LifecycleRuleEventListener();
        kieSession.addEventListener(ruleRuntimeEventListener);
    }
}
