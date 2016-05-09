package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleIntervalType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.exchange.model.util.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class CheckRulesChangesTask implements Runnable {

    private final static Logger LOG = LoggerFactory.getLogger(CheckRulesChangesTask.class);

    ValidationService validationService;
    RulesValidator rulesValidator;
    RulesService rulesService;

    public CheckRulesChangesTask(ValidationService validationService, RulesValidator rulesValidator, RulesService rulesService) {
        this.validationService = validationService;
        this.rulesValidator = rulesValidator;
        this.rulesService = rulesService;
    }

    @Override
    public void run() {
        try {
            clearCustomRules();
            LOG.debug("Checking for changes in sanity rules");
            rulesValidator.updateSanityRules();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //handle execption
        }
    }

    private void clearCustomRules() {
        LOG.debug("Looking outdated custom rules");
        try {
            List<CustomRuleType> customRules = validationService.getRunnableCustomRules();
            boolean updateNeeded = false;
            for (CustomRuleType rule : customRules) {
                // If there are no time intervals, we do not need to check if the rule should be inactivated.
                boolean inactivate = rule.getTimeIntervals().size() > 0;
                for (CustomRuleIntervalType interval : rule.getTimeIntervals()) {
                    if (interval.getEnd() != null) {
                        Date end = DateUtils.parseToUTCDateTime(interval.getEnd());
                        if (end.after(new Date())) {
                            inactivate = false;
                            break;
                        }
                    }
                }
                if (inactivate) {
                    LOG.debug("Inactivating {}", rule.getName());
                    rule.setActive(false);
                    rule.setUpdatedBy("UVMS");
                    rulesService.updateCustomRule(rule);
                    updateNeeded = true;
                }
            }
            if (updateNeeded) {
                LOG.debug("Clear outdated custom rules");
                rulesValidator.updateCustomRules();
            }
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting sanity rules ]");
            // TODO: Throw exception???
        }
    }
}
