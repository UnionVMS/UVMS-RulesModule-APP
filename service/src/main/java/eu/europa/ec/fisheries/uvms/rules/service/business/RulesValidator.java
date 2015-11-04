package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

//@Startup
@Singleton
public class RulesValidator {
    private final static Logger LOG = LoggerFactory.getLogger(RulesValidator.class);
    private static final String SANITY_RESOURCE_DRL = "/rules/SanityRules.drl";
    private static final String TIMER_DRL = "/rules/TimerRules.drl";
    private static final String CUSTOM_RULE_TEMPLATE = "/templates/CustomRulesTemplate.drt";
    private static final String CUSTOM_RULE_DRL = "src/main/resources/rules/TemplateRules.drl";

    @Inject
    RulesService rulesService;

    private KieBase kbase;
    private KieSessionConfiguration ksconf;

    private KieServices kservices;
    private KieContainer kcontainer;
    private KieFileSystem kfs;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    private void init() {
        LOG.info("Initializing Rules Validator");

        kservices = KieServices.Factory.get();

        kfs = kservices.newKieFileSystem();
        kfs.write(ResourceFactory.newClassPathResource(SANITY_RESOURCE_DRL));
//        kfs.write(ResourceFactory.newClassPathResource(TIMER_DRL));
        KieBuilder kbuilder = kservices.newKieBuilder(kfs);
        kbuilder.buildAll();

        // Check for errors
        if (kbuilder.getResults().hasMessages(Level.ERROR)) {
            throw new IllegalArgumentException(kbuilder.getResults().toString());
        }

        // Get the Release ID (mvn style: groupId, artifactId,version)
        ReleaseId releaseId = kbuilder.getKieModule().getReleaseId();
        // ReleaseId releaseId = kservices.newReleaseId("kits.drools.testrules",
        // "testrules", "1.0-SNAPSHOT");
        LOG.info("GroupId:{}, Artifact:{}, Version:{}", releaseId.getGroupId(), releaseId.getArtifactId(), releaseId.getVersion());

        // Create the Container, wrapping the KieModule with the given ReleaseId
        kcontainer = kservices.newKieContainer(releaseId);

        // Configure and create the KieBase
        KieBaseConfiguration kbconf = kservices.newKieBaseConfiguration();
        kbase = kcontainer.newKieBase(kbconf);

        // Configure and create the StatelessKieSession
        ksconf = kservices.newKieSessionConfiguration();
    }

    public void evaluate(RawMovementFact fact) {
        LOG.info("Verifying raw movement");

        KieSession ksession = kbase.newKieSession(ksconf, null);

        // Inject beans
        ksession.setGlobal("rulesService", rulesService);
        ksession.setGlobal("logger", LOG);

        ksession.insert(fact);
        ksession.fireAllRules();

        ksession.dispose();
    }

    public void evaluate(MovementFact fact) {
        LOG.info("Custom rule check");

        // Fetch custom rules from DB
        List<CustomRuleDto> rules = new ArrayList<CustomRuleDto>();
        List<CustomRuleType> customRules = new ArrayList<CustomRuleType>();
        try {
            customRules = rulesService.getCustomRuleList();
        } catch (RulesServiceException e) {
            LOG.error("[ Error when getting rules ]");
            // TODO: Throw exception???
        }

        if (customRules != null && !customRules.isEmpty()) {
            // Add custom rules
            rules = RulesUtil.parseRules(customRules);
            String drl = generateDrl(CUSTOM_RULE_TEMPLATE, rules);
            kfs.write(CUSTOM_RULE_DRL, drl);

            // Create session
            kservices.newKieBuilder(kfs).buildAll();
            kcontainer = kservices.newKieContainer(kservices.getRepository().getDefaultReleaseId());
            KieSession ksession = kcontainer.newKieSession();

            // Inject beans
            ksession.setGlobal("rulesService", rulesService);
            ksession.setGlobal("logger", LOG);

            ksession.insert(fact);
            ksession.fireAllRules();

            ksession.dispose();
        }

    }

    private String generateDrl(String template, List<CustomRuleDto> rules) {
        InputStream templateStream = this.getClass().getResourceAsStream(template);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        TemplateDataListener listener = new TemplateDataListener(tc);

        int rowNum = 0;
        for (CustomRuleDto rule : rules) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, rule.getRuleName(), 0);
            listener.newCell(rowNum, 1, rule.getExpression(), 0);
            listener.newCell(rowNum, 2, rule.getAction(), 0);
            listener.newCell(rowNum, 3, rule.getRuleGuid(), 0);
            rowNum++;
        }
        listener.finishSheet();
        String drl = listener.renderDRL();

        LOG.info("Custom rule file:\n{}", drl);

        return drl;
    }

}
