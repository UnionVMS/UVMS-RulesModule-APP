package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.schema.sales.FLUXReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesRulesServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by MATBUL on 22/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FLUXSalesReportMessageRuleTest {

    @Mock
    private FactRuleEvaluator validator;

    @InjectMocks
    private RulesValidator rulesValidator;

    @InjectMocks
    private SalesRulesServiceBean salesRulesService;

    @InjectMocks
    private MDRCacheServiceBean mdrCacheRuleService;

    @Test
    public void testFLUXReportDocumentMustBePresent() {
        TemplateType template = new TemplateType();
        template.setTemplateName("SalesFLUXSalesReportMessage");
        template.setType(FactType.SALES_FLUX_SALES_REPORT_MESSAGE);

        RuleType ruleType = RuleTestHelper.createRuleType("FLUXReportDocument == null", "abc", "note", ErrorType.ERROR, "doesn't look good");

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(Arrays.asList(ruleType));
        templateRuleMapDto.setTemplateType(template);

        validator.initializeRules(Collections.singletonList(templateRuleMapDto));

        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();
        fact.setFLUXReportDocument(null);

        validator.validateFact(Collections.<AbstractFact>singletonList(fact));

        assertTrue(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }

    @Test
    public void testFLUXReportDocumentMustBePresentWhenEverythingIsOK() {
        TemplateType template = new TemplateType();
        template.setTemplateName("SalesFLUXSalesReportMessage");
        template.setType(FactType.SALES_FLUX_SALES_REPORT_MESSAGE);

        RuleType ruleType = RuleTestHelper.createRuleType("FLUXReportDocument == null", "abc", "note", ErrorType.ERROR, "doesn't look good");

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(Arrays.asList(ruleType));
        templateRuleMapDto.setTemplateType(template);

        validator.initializeRules(Collections.singletonList(templateRuleMapDto));

        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();
        fact.setFLUXReportDocument(new FLUXReportDocumentType());

        validator.validateFact(Collections.<AbstractFact>singletonList(fact));

        assertTrue(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }
}
