package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleIntervalType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleListCriteria;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.uvms.exchange.model.util.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Startup
@Singleton
public class RulesTimerBean {
    private static final int THRESHOLD = 2;

    private final static Logger LOG = LoggerFactory.getLogger(RulesTimerBean.class);

    @EJB
    RulesService rulesService;

    @EJB
    ValidationService validationService;

    @EJB
    RulesValidator rulesValidator;

    @PostConstruct
    public void postConstruct() {
        LOG.info("RulesTimerBean init");
    }

    @Schedule(second = "0", minute = "*/10", hour = "*", persistent = false)
//    @Schedule(second = "0", minute = "*/2", hour = "*", persistent = false)
    public void checkCommunication() {
        LOG.debug("RulesTimerBean tick");
            // Get all previous reports from DB
         List<PreviousReportType> previousReports = new ArrayList<>();
         try {
             previousReports = rulesService.getPreviousMovementReports();
         } catch (RulesServiceException | RulesFaultException e) {
             LOG.warn("No previous movement report found");
         }

         try {
            // Map to fact, adding 2h to deadline
            for (PreviousReportType previousReport : previousReports) {
                PreviousReportFact fact = new PreviousReportFact();
                fact.setMovementGuid(previousReport.getMovementGuid());
                fact.setAssetGuid(previousReport.getAssetGuid());

                GregorianCalendar gregCal = previousReport.getPositionTime().toGregorianCalendar();
                gregCal.add(GregorianCalendar.HOUR, THRESHOLD);
                fact.setDeadline(gregCal.getTime());

                if (fact.getDeadline().getTime() <= new Date().getTime()) {
                    LOG.info("\t==> Executing RULE '" + ServiceConstants.ASSET_NOT_SENDING_RULE + "', deadline:" + fact.getDeadline() + ", assetGuid:" + fact.getAssetGuid());

                    String ruleName = ServiceConstants.ASSET_NOT_SENDING_RULE;
                    rulesService.timerRuleTriggered(ruleName, fact);
                }
            }
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when running checkCommunication timer ] {}", e.getMessage());
        }
    }

    @Schedule(second = "0", minute = "*/10", hour = "*", persistent = false)
    public void updateRules() {
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
