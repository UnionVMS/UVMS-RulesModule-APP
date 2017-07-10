package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by patilva on 11/05/2017.
 */
public class FaLandingEntityRuleTest {
    private TemplateRuleMapDto templateRuleMapDto;
    private Collection<AbstractFact> facts;
    private AbstractFact fact;

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
        FactRuleEvaluator generator = new FactRuleEvaluator();
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
