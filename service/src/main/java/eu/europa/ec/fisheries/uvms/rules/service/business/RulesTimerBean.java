package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

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

    @Inject
    RulesValidator rulesValidator;

    @PostConstruct
    public void postConstruct() {
        LOG.info("RulesTimerBean init");
    }

    @Schedule(second = "0", minute = "*/10", hour = "*", persistent = false)
    // @Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
    public void updatePlugins() {
        LOG.info("RulesTimerBean tick");
        try {
            // Get all previous reports from DB
            List<PreviousReportType> previousReports = rulesService.getPreviousMovementReports();

            // Test data
            // GregorianCalendar gregorianCalendar = new GregorianCalendar();
            // gregorianCalendar.add(GregorianCalendar.HOUR, -3);
            // DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            // XMLGregorianCalendar dummyPositionTime =
            // datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            // PreviousReportType testPos = new PreviousReportType();
            // testPos.setMovementGuid("DUMMY_MOVEMENT_GUID");
            // testPos.setVesselGuid("DUMMY_VESSEL_GUID");
            // testPos.setPositionTime(dummyPositionTime);
            // previousReports.add(testPos);

            // Map to fact, adding 2h to deadline
            List<PreviousReportFact> previousReportFacts = new ArrayList<PreviousReportFact>();
            for (PreviousReportType previousReport : previousReports) {
                PreviousReportFact fact = new PreviousReportFact();
                fact.setMovementGuid(previousReport.getMovementGuid());
                fact.setVesselGuid(previousReport.getVesselGuid());

                GregorianCalendar gregCal = previousReport.getPositionTime().toGregorianCalendar();
                gregCal.add(GregorianCalendar.HOUR, THRESHOLD);
                fact.setDeadline(gregCal.getTime());

                previousReportFacts.add(fact);
            }

            // Evaluate previous reports
            rulesValidator.evaluatePreviousReport(previousReportFacts);

        } catch (RulesServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
