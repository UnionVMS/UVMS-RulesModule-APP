package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.FaReportFactRuleEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesRulesServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementsRulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FluxFaReportMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by sanera on 10/05/2017.
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class FLUX_FA_ReportMessageTest {

    private TemplateRuleMapDto templateRuleMapDto;
    private Collection<AbstractFact> facts;
    private AbstractFact fact;

    @Mock
    private FaReportFactRuleEvaluator validator;

    @InjectMocks
    private MovementsRulesValidator rulesValidator;

    @InjectMocks
    private SalesRulesServiceBean salesRulesService;

    @InjectMocks
    private MDRCacheServiceBean mdrCacheRuleService;

    @Before
    public void beforeClass() {
        templateRuleMapDto = getTemplateRuleMapForFLUXFaReportMessageFact();
        facts = new ArrayList<>();
        fact = getFLUXFaReportMessageFact();
        facts.add(fact);
    }


    @Test
    public void testComputeRule() throws RulesValidationException {
        List<TemplateRuleMapDto> templates = new ArrayList<>();
        templates.add(templateRuleMapDto);

        // Validation
        validator.initializeRules(templates);
        validator.validateFacts(facts);

        // validate facts
        for (AbstractFact abstractFact : facts) {

            assertTrue(abstractFact.getErrors().isEmpty());
            assertTrue(abstractFact.getWarnings().isEmpty());

        }
        // Clear facts
        facts.clear();

    }

    private TemplateRuleMapDto getTemplateRuleMapForFLUXFaReportMessageFact() {
        TemplateType template = new TemplateType();
        //template.setInOutType(InOutType.IN);
        template.setTemplateName("Test Template");
        template.setType(FactType.FLUX_FA_REPORT_MESSAGE);
        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(getRulesForFLUXFaReportMessageFact());
        templateRuleMapDto.setTemplateType(template);
        templateRuleMapDto.setExternalRules(new ArrayList<ExternalRuleType>());
        return templateRuleMapDto;
    }

    private List<RuleType> getRulesForFLUXFaReportMessageFact() {
        List<RuleType> rules = new ArrayList<>();
        RuleType ruleTypeCode = RuleTestHelper.createRuleType("ids == null || ids.empty == true ", "1", "Test Notes", ErrorType.ERROR, "ids is null or empty");
        //RuleType ruleTest = RuleTestHelper.createRuleType("$testStringList : testStringList ,  $testStringList contains 'test' ","7" ,"Test Notes", ErrorType.ERROR,"FISH_SIZE_CLASS do not exist");
        rules.add(ruleTypeCode);
        return rules;
    }


    private FluxFaReportMessageFact getFLUXFaReportMessageFact() {
        FluxFaReportMessageFact fact = new FluxFaReportMessageFact();
        fact.setIds(Collections.singletonList(RuleTestHelper.getIdType("id", "schemeID")));
        return fact;
    }

}
