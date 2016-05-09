package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Startup
@Singleton
public class RulesTimerBean {

    private final static Logger LOG = LoggerFactory.getLogger(RulesTimerBean.class);

    @EJB
    RulesService rulesService;

    @EJB
    ValidationService validationService;

    @EJB
    RulesValidator rulesValidator;

    @Resource(lookup="java:/UvmsExecutorService")
    private ManagedScheduledExecutorService executorService;

    @PostConstruct
    public void postConstruct() {
        LOG.info("RulesTimerBean init");
        CheckCommunicationTask checkCommunicationTask = new CheckCommunicationTask(rulesService);
        executorService.scheduleWithFixedDelay(checkCommunicationTask, 10, 10, TimeUnit.MINUTES);

        CheckRulesChangesTask checkRulesChangesTask = new CheckRulesChangesTask(validationService, rulesValidator, rulesService);
        executorService.scheduleWithFixedDelay(checkRulesChangesTask, 10, 10, TimeUnit.MINUTES);
    }

}
