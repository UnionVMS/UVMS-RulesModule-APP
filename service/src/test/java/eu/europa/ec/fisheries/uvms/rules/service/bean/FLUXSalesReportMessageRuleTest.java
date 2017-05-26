package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.schema.sales.FLUXReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MATBUL on 22/05/2017.
 */
public class FLUXSalesReportMessageRuleTest {

    private FactRuleEvaluator generator;

    @Before
    public void setUp() throws Exception {
        generator = new FactRuleEvaluator();
    }

    @Test
    public void testFLUXReportDocumentMustBePresent() {
        TemplateType template = new TemplateType();
        template.setTemplateName("SalesFLUXSalesReportMessage");
        template.setType(FactType.SALES_FLUX_SALES_REPORT_MESSAGE);

        RuleType ruleType = RuleTestHelper.createRuleType("FLUXReportDocument == null", "abc", "note", ErrorType.ERROR, "doesn't look good");

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(Arrays.asList(ruleType));
        templateRuleMapDto.setTemplateType(template);

        generator.initializeRules(Collections.singletonList(templateRuleMapDto));

        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();
        fact.setFLUXReportDocument(null);

        generator.validateFact(Collections.<AbstractFact>singletonList(fact));

        assertFalse(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }

    @Test
    public void testFLUXReportDocumentMustBePresentWhenEverythingIsOK() {
        TemplateType template = new TemplateType();
        template.setTemplateName("SalesFLUXSalesReportMessage");
        template.setType(FactType.SALES_FLUX_SALES_REPORT_MESSAGE);

        RuleType ruleType = RuleTestHelper.createRuleType("fluxReportDocument == null", "abc", "note", ErrorType.ERROR, "doesn't look good");

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(Arrays.asList(ruleType));
        templateRuleMapDto.setTemplateType(template);

        generator.initializeRules(Collections.singletonList(templateRuleMapDto));

        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();
        fact.setFLUXReportDocument(new FLUXReportDocumentType());

        generator.validateFact(Collections.<AbstractFact>singletonList(fact));

        assertTrue(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }
}
