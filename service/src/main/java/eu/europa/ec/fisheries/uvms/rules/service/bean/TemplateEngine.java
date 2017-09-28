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
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleStatusType;
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
    private MDRCache mdrCache;

    @PostConstruct
    public void initialize() {
        log.info("[START] Initializing templates and rules...");
        ruleEvaluator.initializeRules(getAllTemplates());
        log.info("[START] Initializing MDR cache...");
        mdrCache.init();
        log.info("[START] Updating Rules Status...");
        updateRulesStatus(ruleEvaluator.getFailedRules());
        log.info("[END] Finished Initializing Templates.");
    }

    public void reInitialize() {
        log.info("Re-Initializing templates and rules [START]");
        ruleEvaluator.reInitializeKieSystem();
        initialize();
        log.info("Re-Initialization of templates and rules [FINISH]");
    }

    public void evaluateFacts(List<AbstractFact> facts) throws RulesValidationException {
        if (CollectionUtils.isEmpty(facts)) {
            throw new RulesValidationException("No facts available for validation");
        }
        ruleEvaluator.setExceptionsList(new ArrayList<AbstractFact>());
        ruleEvaluator.validateFact(facts);
        facts.addAll(ruleEvaluator.getExceptionsList());
    }

    private List<TemplateRuleMapDto> getAllTemplates() {
        try {
            return rulesDb.getAllFactTemplatesAndRules();
        } catch (RulesModelException e) {
            throw new IllegalStateException(e);
        }
    }

    private void updateRulesStatus(List<String> failedBrIds) {
        try {
            rulesDb.updateFailedRules(failedBrIds);
            rulesDb.updateRuleStatus(failedBrIds.isEmpty() ? RuleStatusType.SUCCESSFUL : RuleStatusType.FAILED);
        } catch (RulesModelException e) {
            throw new IllegalStateException(e);
        }
    }
}
