package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.InOutType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaCatchFact;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by sanera on 10/05/2017.
 */
@Slf4j
public class FaCatchEntityRuleTest {

    private TemplateRuleMapDto templateRuleMapDto;
    private Collection<AbstractFact> facts;
    private  AbstractFact fact;

    @Before
    public void beforeClass() {

        templateRuleMapDto = getTemplateRuleMapForFaCatch();

        facts = new ArrayList<>();
        fact=getFaCatchFact();
        facts.add(getFaCatchFact());
    }


    @Test
    public void testComputeRule() {
        List<TemplateRuleMapDto> templates = new ArrayList<>();
        templates.add(templateRuleMapDto);

        // Validation
        FactRuleEvaluator generator = FactRuleEvaluator.getInstance();
        generator.initializeRules(templates);
        generator.validateFact(facts);

        // validate facts
        assertTrue(fact.getErrors().isEmpty());

        // Clear facts
        facts.clear();

    }

    private TemplateRuleMapDto getTemplateRuleMapForFaCatch() {

        TemplateType template = new TemplateType();
        template.setInOutType(InOutType.IN);
        template.setTemplateName("Test Template");
        template.setType(FactType.FA_CATCH);

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(getRulesForFaCatchFact());
        templateRuleMapDto.setTemplateType(template);

        return templateRuleMapDto;
    }

    private List<RuleType> getRulesForFaCatchFact() {
        List<RuleType> rules = new ArrayList<>();

        RuleType ruleTypeCode = RuleTestHelper.createRuleType("typeCode.value == null || typeCode.listId !='FA_CATCH_TYPE' ","1" ,"Test Notes", ErrorType.ERROR,"typeCode value is null");
        rules.add(ruleTypeCode);

        return rules;
    }


    private FaCatchFact getFaCatchFact() {
        FaCatchFact fact = new FaCatchFact();
        fact.setTypeCode(RuleTestHelper.getCodeType("typecode","FA_CATCH_TYPE"));

        return fact;
    }

}
