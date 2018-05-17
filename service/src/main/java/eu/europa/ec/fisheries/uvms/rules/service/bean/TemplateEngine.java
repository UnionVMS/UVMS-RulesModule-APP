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
import javax.ejb.*;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.EnrichedBRMessage;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import static java.util.concurrent.TimeUnit.SECONDS;

@Singleton
@Slf4j
@Startup
@DependsOn({"FactRuleEvaluator","MDRCacheServiceBean"})
public class TemplateEngine {

    @EJB
    private RulesDomainModel rulesDb;

    @EJB
    private FactRuleEvaluator ruleEvaluator;

    @EJB
    private RulesStatusUpdater rulesStatusUpdaterBean;

    @EJB
    private MDRCacheRuleService cacheService;

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void initialize() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            List<TemplateRuleMapDto> templatesAndRules = rulesDb.getAllFactTemplatesAndRules();
            refreshRulesValidationMessages(templatesAndRules);
            log.info("[START] Initializing templates and rules...");
            ruleEvaluator.initializeRules(templatesAndRules);
            log.info("[END] It took "+ stopwatch + " to initialize the rules.");
        } catch (RulesModelException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void refreshRulesValidationMessages(List<TemplateRuleMapDto> templatesAndRules) {
        cacheService.loadCacheForFailureMessages();
        for (TemplateRuleMapDto templatesAndRule : templatesAndRules) {
            for (RuleType ruleType : templatesAndRule.getRules()) {
                EnrichedBRMessage enrichedBRMessage = cacheService.getErrorMessageForBrId(ruleType.getBrId());
                if (enrichedBRMessage != null){
                    String errorMessageForBrId = enrichedBRMessage.getMessage();
                    if (StringUtils.isNotEmpty(errorMessageForBrId)){
                        ruleType.setMessage(errorMessageForBrId.replaceAll("\"", "&quot;"));
                        enrichedBRMessage.setTemplateEntityName(templatesAndRule.getTemplateType().getType().name());
                        enrichedBRMessage.setExpression(ruleType.getExpression());
                    }
                }
            }
        }
    }

    @Lock(LockType.WRITE)
    @AccessTimeout(value = 180, unit = SECONDS)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void reInitialize() {
        initialize();
    }

    @Lock(LockType.WRITE)
    @AccessTimeout(value = 180, unit = SECONDS)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void evaluateFacts(List<AbstractFact> facts) throws RulesValidationException {
        if (CollectionUtils.isEmpty(facts)) {
            throw new RulesValidationException("No facts available for validation");
        }
        ruleEvaluator.setExceptionsList(new ArrayList<AbstractFact>());
        ruleEvaluator.validateFacts(facts);
        facts.addAll(ruleEvaluator.getExceptionsList());
    }
}
