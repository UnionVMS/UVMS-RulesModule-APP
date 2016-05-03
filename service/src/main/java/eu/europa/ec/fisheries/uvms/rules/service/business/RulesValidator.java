package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;

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
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Startup
@Singleton
//@Stateless
public class RulesValidator {
    private final static Logger LOG = LoggerFactory.getLogger(RulesValidator.class);
//    private static final String SANITY_RESOURCE_DRL_FILE = "/rules/SanityRules.drl";
    private static final String CUSTOM_RULE_DRL_FILE = "src/main/resources/rules/CustomRules.drl";
    private static final String CUSTOM_RULE_TEMPLATE = "/templates/CustomRulesTemplate.drt";

    private static final String SANITY_RULES_DRL_FILE = "src/main/resources/rules/SanityRules.drl";
    private static final String SANITY_RULES_TEMPLATE = "/templates/SanityRulesTemplate.drt";

    @EJB
    ValidationService validationService;

    private KieServices kieServices;

    private KieFileSystem sanityKfs;
    private KieContainer sanityKcontainer;

    private KieFileSystem customKfs;
    private KieContainer customKcontainer;

    @PostConstruct
    public void init() {
        initServices();
        updateSanityRules();
        updateCustomRules();
    }

    private void initServices() {
        kieServices = KieServices.Factory.get();
    }

    public void updateSanityRules() {
        // Fetch sanity rules from DB
        List<SanityRuleType> sanityRules = new ArrayList<>();
        try {
            sanityRules = validationService.getSanityRules();
            if (sanityRules != null && !sanityRules.isEmpty()) {
                // Add sanity rules
                String drl = generateSanityRuleDrl(SANITY_RULES_TEMPLATE, sanityRules);

                sanityKfs = kieServices.newKieFileSystem();

                sanityKfs.write(SANITY_RULES_DRL_FILE, drl);
                kieServices.newKieBuilder(sanityKfs).buildAll();
                sanityKcontainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            }
        } catch (RulesServiceException | RulesFaultException  e) {
            LOG.error("[ Error when getting sanity rules ]");
            // TODO: Throw exception???
        }
    }

    public void updateCustomRules() {
        // Fetch custom rules from DB
        List<CustomRuleDto> rules = new ArrayList<>();
        List<CustomRuleType> customRules = new ArrayList<>();
        try {
            customRules = validationService.getRunnableCustomRules();
            if (customRules != null && !customRules.isEmpty()) {
                // Add custom rules
                rules = CustomRuleParser.parseRules(customRules);
                String drl = generateCustomRuleDrl(CUSTOM_RULE_TEMPLATE, rules);

                customKfs = kieServices.newKieFileSystem();

                customKfs.write(CUSTOM_RULE_DRL_FILE, drl);

                // Create session
                kieServices.newKieBuilder(customKfs).buildAll();
                customKcontainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            }
        } catch (RulesServiceException | RulesFaultException  e) {
            LOG.error("[ Error when getting custom rules ]");
            // TODO: Throw exception???
        }
    }

    public void evaluate(RawMovementFact fact) {
        LOG.info("Verify sanity rules");

        KieSession ksession = sanityKcontainer.newKieSession();

        // Inject beans
        ksession.setGlobal("validationService", validationService);
        ksession.setGlobal("logger", LOG);

        ksession.insert(fact);
        ksession.fireAllRules();
    }

    public void evaluate(MovementFact fact) {
        LOG.info("Verify user defined rules");

        KieSession ksession = customKcontainer.newKieSession();

        // Inject beans
        ksession.setGlobal("validationService", validationService);
        ksession.setGlobal("logger", LOG);

        ksession.insert(fact);
        ksession.fireAllRules();
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
