package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomRuleParser;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

//@Startup
@Singleton
public class RulesValidator {
    private final static Logger LOG = LoggerFactory.getLogger(RulesValidator.class);
//    private static final String SANITY_RESOURCE_DRL_FILE = "/rules/SanityRules.drl";
    private static final String CUSTOM_RULE_DRL_FILE = "src/main/resources/rules/CustomRules.drl";
    private static final String CUSTOM_RULE_TEMPLATE = "/templates/CustomRulesTemplate.drt";

    private static final String SANITY_RULES_DRL_FILE = "src/main/resources/rules/SanityRules.drl";
    private static final String SANITY_RULES_TEMPLATE = "/templates/SanityRulesTemplate.drt";

    @EJB
    ValidationService validationService;

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
//        kfs.write(ResourceFactory.newClassPathResource(SANITY_RESOURCE_DRL_FILE));
        loadSanityRules();

//        loadRules();

        KieBuilder kbuilder = kservices.newKieBuilder(kfs);
        kbuilder.buildAll();

        // Check for errors
        if (kbuilder.getResults().hasMessages(Level.ERROR)) {
            throw new IllegalArgumentException(kbuilder.getResults().toString());
        }

        // Get the Release ID (mvn style: groupId, artifactId,version)
        ReleaseId releaseId = kbuilder.getKieModule().getReleaseId();
        LOG.info("GroupId:{}, Artifact:{}, Version:{}", releaseId.getGroupId(), releaseId.getArtifactId(), releaseId.getVersion());

        // Create the Container, wrapping the KieModule with the given ReleaseId
        kcontainer = kservices.newKieContainer(releaseId);

        // Configure and create the KieBase
        KieBaseConfiguration kbconf = kservices.newKieBaseConfiguration();
        kbase = kcontainer.newKieBase(kbconf);

        // Configure and create the StatelessKieSession
        ksconf = kservices.newKieSessionConfiguration();
    }

    private void loadSanityRules() {
        // Fetch sanity rules from DB
        List<SanityRuleType> sanityRules = new ArrayList<>();
        try {
            sanityRules = validationService.getSanityRules();
        } catch (RulesServiceException | RulesFaultException  e) {
            LOG.error("[ Error when getting sanity rules ]");
            // TODO: Throw exception???
        }

        if (sanityRules != null && !sanityRules.isEmpty()) {
            // Add sanity rules
            String drl = generateSanityRuleDrl(SANITY_RULES_TEMPLATE, sanityRules);
            kfs.write(SANITY_RULES_DRL_FILE, drl);
        }
    }

    public void evaluate(RawMovementFact fact) {
        LOG.info("Verifying sanity rules");

        // Create session
        KieSession ksession = kbase.newKieSession(ksconf, null);

        // Inject beans
        ksession.setGlobal("validationService", validationService);
        ksession.setGlobal("logger", LOG);

        ksession.insert(fact);
        ksession.fireAllRules();

        ksession.dispose();
    }

    public void evaluate(MovementFact fact) {
        LOG.info("Verify user defined rules");

        // Fetch custom rules from DB
        List<CustomRuleDto> rules = new ArrayList<CustomRuleDto>();
        List<CustomRuleType> customRules = new ArrayList<CustomRuleType>();
        try {
            customRules = validationService.getRunnableCustomRules();
        } catch (RulesServiceException | RulesFaultException  e) {
            LOG.error("[ Error when getting custom rules ]");
            // TODO: Throw exception???
        }

        if (customRules != null && !customRules.isEmpty()) {
            // Add custom rules
            rules = CustomRuleParser.parseRules(customRules);
            String drl = generateCustomRuleDrl(CUSTOM_RULE_TEMPLATE, rules);
            kfs.write(CUSTOM_RULE_DRL_FILE, drl);

            // Create session
            kservices.newKieBuilder(kfs).buildAll();
            kcontainer = kservices.newKieContainer(kservices.getRepository().getDefaultReleaseId());
            KieSession ksession = kcontainer.newKieSession();
//        // Create session
//        KieSession ksession = kbase.newKieSession(ksconf, null);

            // Inject beans
            ksession.setGlobal("validationService", validationService);
            ksession.setGlobal("logger", LOG);

            ksession.insert(fact);
            ksession.fireAllRules();

            ksession.dispose();
        }

    }

    private String generateCustomRuleDrl(String template, List<CustomRuleDto> ruleDtos) {
        InputStream templateStream = this.getClass().getResourceAsStream(template);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        TemplateDataListener listener = new TemplateDataListener(tc);

        int rowNum = 0;
        for (CustomRuleDto ruleDto : ruleDtos) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, ruleDto.getRuleName(), 0);
            listener.newCell(rowNum, 1, ruleDto.getExpression(), 0);
            listener.newCell(rowNum, 2, ruleDto.getAction(), 0);
            listener.newCell(rowNum, 3, ruleDto.getRuleGuid(), 0);
            rowNum++;
        }
        listener.finishSheet();
        String drl = listener.renderDRL();

        LOG.debug("Custom rule file:\n{}", drl);

        return drl;
    }

    private String generateSanityRuleDrl(String template, List<SanityRuleType> sanityRules) {
        InputStream templateStream = this.getClass().getResourceAsStream(template);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        TemplateDataListener listener = new TemplateDataListener(tc);

        int rowNum = 0;
        for (SanityRuleType sanityRule : sanityRules) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, sanityRule.getName(), 0);
            listener.newCell(rowNum, 1, sanityRule.getExpression(), 0);
            rowNum++;
        }
        listener.finishSheet();
        String drl = listener.renderDRL();

        LOG.debug("Sanity rule file:\n{}", drl);

        return drl;
    }

}
