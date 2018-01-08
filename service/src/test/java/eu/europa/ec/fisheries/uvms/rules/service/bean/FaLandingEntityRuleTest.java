package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertTrue;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesRulesServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaLandingFact;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.drools.compiler.kie.builder.impl.KieFileSystemImpl;
import org.drools.compiler.kie.builder.impl.KieServicesImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

/**
 * Created by patilva on 11/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FaLandingEntityRuleTest {

    private TemplateRuleMapDto templateRuleMapDto;
    private Collection<AbstractFact> facts;
    private AbstractFact fact;

    @Mock
    private FactRuleEvaluator generator;

    @InjectMocks
    private RulesValidator rulesValidator;

    @InjectMocks
    private SalesRulesServiceBean salesRulesService;

    @InjectMocks
    private MDRCacheServiceBean mdrCacheRuleService;

    @Before
    public void setUp() throws Exception {
        Whitebox.setInternalState(generator, "kieServices", new KieServicesImpl());
        Whitebox.setInternalState(generator, "kieFileSystem", new KieFileSystemImpl());
    }

    @Before
    public void beforeClass() {
        templateRuleMapDto = getTemplateRuleMapForFaLanding();
        facts = new ArrayList<>();
        fact = getFaLandingFact();
        facts.add(fact);
    }


    @Test
    public void testComputeRule() {
        List<TemplateRuleMapDto> templates = new ArrayList<>();
        templates.add(templateRuleMapDto);
        // Validation
        generator.initializeRules(templates);
        generator.validateFact(facts);
        // validate facts
        for (AbstractFact abstractFact : facts) {
            assertTrue(abstractFact.getErrors().isEmpty());
            assertTrue(abstractFact.getWarnings().isEmpty());
        }
        // Clear facts
        facts.clear();

    }

    private TemplateRuleMapDto getTemplateRuleMapForFaLanding() {
        TemplateType template = new TemplateType();
        template.setTemplateName("Test Template");
        template.setType(FactType.FA_LANDING);
        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(getRulesForFaLandingFact());
        templateRuleMapDto.setTemplateType(template);
        templateRuleMapDto.setExternalRules(new ArrayList<ExternalRuleType>());
        return templateRuleMapDto;
    }

    private List<RuleType> getRulesForFaLandingFact() {
        List<RuleType> rules = new ArrayList<>();
        RuleType ruleFishingActivityTypeCode = RuleTestHelper.createRuleType("fishingActivityCodeType.value == null || fishingActivityCodeType.listId !='LANDING' ", "1", "Test Notes", ErrorType.ERROR, "typeCode value is null or is not correct");
        RuleType ruleFaReportDocumentTypeCode = RuleTestHelper.createRuleType("faReportDocumentTypeCode.value == null || faReportDocumentTypeCode.listId !='DECLARATION' ", "2", "Test Notes", ErrorType.ERROR, "TypeCode value is null or is not correct");
        RuleType ruleRelatedFluxLocation=RuleTestHelper.createRuleType("relatedFluxLocations == null","3" ,"Test Notes", ErrorType.ERROR,"relatedFluxLocation is null ,relatedFluxLocation must be present");
        rules.add(ruleFishingActivityTypeCode);
        rules.add(ruleFaReportDocumentTypeCode);
        rules.add(ruleRelatedFluxLocation);
        return rules;
    }


    private FaLandingFact getFaLandingFact() {
        FaLandingFact fact = new FaLandingFact();
        fact.setFishingActivityCodeType(RuleTestHelper.getCodeType("fishingActivityCodeType", "LANDING"));
        fact.setFaReportDocumentTypeCode(RuleTestHelper.getCodeType("faReportDocumentTypeCode", "DECLARATION"));
        ArrayList<FLUXLocation> relatedFluxLocationsList=new ArrayList<>();
        FLUXLocation fluxLocation=new FLUXLocation();
        relatedFluxLocationsList.add(fluxLocation);
        fact.setRelatedFluxLocations(relatedFluxLocationsList);
        return fact;
    }

}
