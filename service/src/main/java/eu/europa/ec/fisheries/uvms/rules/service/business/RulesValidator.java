package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.service.entity.CustomRule;

//@Startup
@Singleton
public class RulesValidator {
    private final static Logger LOG = LoggerFactory.getLogger(RulesValidator.class);
    private static final String SANITY_RESOURCE_DRL = "/rules/SanityRules.drl";
    private static final String ACTION_RESOURCE_DRL = "/rules/ActionRules.drl";
    private static final String CUSTOM_RULE_TEMPLATE = "/templates/CustomPositionRulesTemplate.drt";
    private static final String CUSTOM_RULE_DRL = "src/main/resources/rules/TemplateRules.drl";

    @Inject
    private ErrorReportDao errorReportDao;

    @Inject
    private CustomRuleDao customRuleDao;

    @Inject
    private CustomEventDao customEventDao;

    private KieSession ksession;
    private KieServices kservices;
    private KieContainer kcontainer;
    private KieFileSystem kfs;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    @PreDestroy
    public void preDestroy() {
        destroy();
    }

    public void init() {
        LOG.info("Starting up cloud validation session");

        kservices = KieServices.Factory.get();
        KieModuleModel kieModuleModel = kservices.newKieModuleModel();

        KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel("rules-cloud")
                .setDefault(true)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.CLOUD);

        KieSessionModel ksessionModel1 = kieBaseModel1.newKieSessionModel("stateful-ksession-rules-cloud")
                .setDefault(true)
                .setType(KieSessionModel.KieSessionType.STATEFUL);

        kfs = kservices.newKieFileSystem();
    }

    public void destroy() {
        LOG.info("Shutting down validation session");
        if (ksession != null) {
            ksession.halt();
            ksession.dispose();
        }
    }

    public void evaluate(PositionEvent pe) {
        // Add rules
        kfs.write(kservices.getResources().newClassPathResource(SANITY_RESOURCE_DRL));

        // Add rules
        List<CustomRule> rules = customRuleDao.getCustomRules();
        String drl = generateDrl(CUSTOM_RULE_TEMPLATE, rules);
        kfs.write(CUSTOM_RULE_DRL, drl);

        // Add rules
        kfs.write(kservices.getResources().newClassPathResource(ACTION_RESOURCE_DRL));

        // Create session
        kservices.newKieBuilder(kfs).buildAll();
        kcontainer = kservices.newKieContainer(kservices.getRepository().getDefaultReleaseId());
        ksession = kcontainer.newKieSession();

        // Inject beans
        ksession.setGlobal("customEventDao", customEventDao);
        ksession.setGlobal("errorReportDao", errorReportDao);

        ksession.insert(pe);
        ksession.fireAllRules();
    }

    private String generateDrl(String template, List<CustomRule> rules) {
        InputStream templateStream = this.getClass().getResourceAsStream(template);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        TemplateDataListener listener = new TemplateDataListener(tc);

        int rowNum = 0;
        for (CustomRule customRule : rules) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, customRule.getAttribute(), 0);
            listener.newCell(rowNum, 1, customRule.getOperator(), 0);
            listener.newCell(rowNum, 2, customRule.getValue(), 0);
            listener.newCell(rowNum, 3, customRule.getAction(), 0);
            rowNum++;
        }
        listener.finishSheet();
        String drl = listener.renderDRL();

        LOG.info("Custom rule file:\n{}", drl);

        return drl;
    }

}
