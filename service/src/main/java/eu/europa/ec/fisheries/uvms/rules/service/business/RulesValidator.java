package eu.europa.ec.fisheries.uvms.rules.service.business;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Startup
@Singleton
public class RulesValidator {
    private final static Logger LOG = LoggerFactory.getLogger(RulesValidator.class);

    private static final String NEW_POSITION_STREAM = "NewPositionStream";

    @Inject
    private ErrorReportDao errorReportDao;

    private KieSession ksession;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    @PreDestroy
    public void preDestroy() {
        destroy();
    }

    public void init() {
        LOG.info("Starting up stream validation session");
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        ksession = kContainer.newKieSession("ksession-rules-stream");

        ksession.setGlobal("errorReportDao", errorReportDao);

        // Stuff for debugging
        // ksession.addEventListener(new DebugAgendaEventListener());
        // ksession.addEventListener(new DebugRuleRuntimeEventListener());

    }

    public void destroy() {
        LOG.info("Shutting down stream validation session");
        ksession.halt();
        ksession.dispose();
    }

    public void evaluate(PositionEvent pe) {
        try {
            EntryPoint entryPoint = ksession.getEntryPoint(NEW_POSITION_STREAM);
            entryPoint.insert(pe);
            LOG.debug("Fire all rule. PE guid:{}, longitude:{}, latitude:{}", pe.getGuid(), pe.getLongitude(), pe.getLatitude());
            ksession.fireAllRules();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
