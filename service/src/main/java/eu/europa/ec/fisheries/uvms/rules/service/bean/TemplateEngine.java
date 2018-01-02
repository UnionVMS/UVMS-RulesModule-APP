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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Singleton
@Slf4j
@Startup
public class TemplateEngine {

    @EJB
    private RulesDomainModel rulesDb;

    @EJB
    private FactRuleEvaluator ruleEvaluator;

    @EJB
    private RulesStatusUpdater rulesStatusUpdaterBean;

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void initialize() {
        final long initTime = System.currentTimeMillis();
        log.info("[START] Initializing templates and rules... Started @ : [ "+new Date(initTime)+" ] ..");
        ruleEvaluator.initializeRules(getAllTemplates());
        log.info("[END] Finished Initializing templates and rules...");
        log.info("[INFO] It took ["+ TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - initTime)+"] minutes for drools engine to be initialized!");
        rulesStatusUpdaterBean.updateRulesStatus(ruleEvaluator.getFailedRules());
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void reInitialize() {
        log.info("[START] Re-Initializing templates and rules..");
        ruleEvaluator.reInitializeKieSystem();
        initialize();
        log.info("[END] Re-Initialization of templates and rules..");
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void evaluateFacts(List<AbstractFact> facts) throws RulesValidationException {
        if (CollectionUtils.isEmpty(facts)) {
            throw new RulesValidationException("No facts available for validation");
        }
        ruleEvaluator.setExceptionsList(new ArrayList<AbstractFact>());
        ruleEvaluator.validateFact(facts);
        facts.addAll(ruleEvaluator.getExceptionsList());
    }


    @PreDestroy
    public void shutDown() {
        log.info("[START] TemplateEngine shutting down. Going to destroy the Drools KnowledgeBase..");
        ruleEvaluator.reInitializeKieSystem();
        log.info("[END] Destroyed the Drools KnowledgeBase..");
    }


    private List<TemplateRuleMapDto> getAllTemplates() {
        try {
            return rulesDb.getAllFactTemplatesAndRules();
        } catch (RulesModelException e) {
            throw new IllegalStateException(e);
        }
    }

}
