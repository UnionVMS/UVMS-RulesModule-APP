package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CheckCommunicationTask implements Runnable {
    private static final int THRESHOLD = 2;

    private final static Logger LOG = LoggerFactory.getLogger(CheckCommunicationTask.class);

    RulesService rulesService;

    public CheckCommunicationTask(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    public void run() {
        try {
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
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //handle execption
        }
    }
}