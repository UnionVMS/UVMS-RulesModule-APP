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
import javax.inject.Inject;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Startup
@Singleton
public class RulesTimerBean {
    private static final int THRESHOLD = 2;

    final static Logger LOG = LoggerFactory.getLogger(RulesTimerBean.class);

    @EJB
    RulesService rulesService;

    @PostConstruct
    public void postConstruct() {
        LOG.info("RulesTimerBean init");
    }

    @Schedule(second = "0", minute = "*/10", hour = "*", persistent = false)
//     @Schedule(second = "0", minute = "*/2", hour = "*", persistent = false)
    public void checkCommunication() {
        LOG.info("RulesTimerBean tick");
        try {
            // Get all previous reports from DB
            List<PreviousReportType> previousReports = rulesService.getPreviousMovementReports();

            // Map to fact, adding 2h to deadline
            for (PreviousReportType previousReport : previousReports) {
                PreviousReportFact fact = new PreviousReportFact();
                fact.setMovementGuid(previousReport.getMovementGuid());
                fact.setVesselGuid(previousReport.getVesselGuid());

                GregorianCalendar gregCal = previousReport.getPositionTime().toGregorianCalendar();
                gregCal.add(GregorianCalendar.HOUR, THRESHOLD);
                fact.setDeadline(gregCal.getTime());

                if (fact.getDeadline().getTime() <= new Date().getTime()) {
                    LOG.info("\t==> Executing RULE 'Asset not sending', deadline:" + fact.getDeadline() + ", vesselGuid:" + fact.getVesselGuid() + ", movementGuid:" + fact.getMovementGuid());

                    String ruleName = "Asset not sending";
                    String ruleGuid = "DEADLINE";
                    rulesService.timerRuleTriggered(ruleName, ruleGuid, fact);
                }

            }

        } catch (RulesServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RulesFaultException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
