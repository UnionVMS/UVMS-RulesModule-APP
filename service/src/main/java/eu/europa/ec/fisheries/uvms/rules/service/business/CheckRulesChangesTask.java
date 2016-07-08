/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
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
        clearCustomRules();
        LOG.debug("Checking for changes in sanity rules");
        rulesValidator.updateSanityRules();
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
                    if (interval.getEnd() != null && interval.getStart() != null) {
                        Date end = DateUtils.parseToUTCDateTime(interval.getEnd());
                        Date start = DateUtils.parseToUTCDateTime(interval.getStart());
                        Date now = new Date();
                        if (start.before(now) && end.after(now)) {
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