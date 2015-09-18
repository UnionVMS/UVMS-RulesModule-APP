package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.io.InputStream;
import java.util.ArrayList;
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

import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.ServiceException;

//@Startup
@Singleton
public class RulesValidator {
    private final static Logger LOG = LoggerFactory.getLogger(RulesValidator.class);
    private static final String SANITY_RESOURCE_DRL = "/rules/SanityRules.drl";
    private static final String ACTION_RESOURCE_DRL = "/rules/ActionRules.drl";
    private static final String CUSTOM_RULE_TEMPLATE = "/templates/CustomPositionRulesTemplate.drt";
    private static final String CUSTOM_RULE_DRL = "src/main/resources/rules/TemplateRules.drl";

    @Inject
    RulesService rulesService;

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

    public void evaluate(PositionFact p) {
        // Add sanity rules
        kfs.write(kservices.getResources().newClassPathResource(SANITY_RESOURCE_DRL));

        // Fetch custom rules from DB
        List<CustomRuleDto> rules = new ArrayList<CustomRuleDto>();
        List<CustomRuleType> rawRules = new ArrayList<CustomRuleType>();
        try {
            rawRules = rulesService.getCustomRuleList();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Add custom rules
        rules = RulesUtil.parseRules(rawRules);
        String drl = generateDrl(CUSTOM_RULE_TEMPLATE, rules);
        kfs.write(CUSTOM_RULE_DRL, drl);

        // Add action rules
        kfs.write(kservices.getResources().newClassPathResource(ACTION_RESOURCE_DRL));

        // Create session
        kservices.newKieBuilder(kfs).buildAll();
        kcontainer = kservices.newKieContainer(kservices.getRepository().getDefaultReleaseId());
        ksession = kcontainer.newKieSession();

        // Inject beans
        ksession.setGlobal("rulesService", rulesService);

        ksession.insert(p);
        ksession.fireAllRules();
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
            rowNum++;
        }
        listener.finishSheet();
        String drl = listener.renderDRL();

        LOG.info("Custom rule file:\n{}", drl);

        return drl;
    }

}
