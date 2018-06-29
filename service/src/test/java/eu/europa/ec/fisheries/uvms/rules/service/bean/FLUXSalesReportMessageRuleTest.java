package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.schema.sales.FLUXReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesRulesServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementsRulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by MATBUL on 22/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FLUXSalesReportMessageRuleTest {

    @InjectMocks
    private MovementsRulesValidator rulesValidator;

    @InjectMocks
    private SalesRulesServiceBean salesRulesService;

    @InjectMocks
    private MDRCacheServiceBean mdrCacheRuleService;

    @Test
    public void testFLUXReportDocumentMustBePresent() throws RulesValidationException {
        TemplateType template = new TemplateType();
        template.setTemplateName("SalesFLUXSalesReportMessage");
        template.setType(FactType.SALES_FLUX_SALES_REPORT_MESSAGE);

        RuleType ruleType = createRuleType("FLUXReportDocument == null", "abc", "note", ErrorType.ERROR, "doesn't look good");

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(Collections.singletonList(ruleType));
        templateRuleMapDto.setTemplateType(template);

        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();
        fact.setFLUXReportDocument(null);

        assertTrue(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }

    @Test
    public void testFLUXReportDocumentMustBePresentWhenEverythingIsOK() throws RulesValidationException {
        TemplateType template = new TemplateType();
        template.setTemplateName("SalesFLUXSalesReportMessage");
        template.setType(FactType.SALES_FLUX_SALES_REPORT_MESSAGE);

        RuleType ruleType = createRuleType("FLUXReportDocument == null", "abc", "note", ErrorType.ERROR, "doesn't look good");

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(Collections.singletonList(ruleType));
        templateRuleMapDto.setTemplateType(template);

        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();
        fact.setFLUXReportDocument(new FLUXReportDocumentType());

        assertTrue(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }

    public static RuleType createRuleType(String expression, String brId, String note, ErrorType type, String errorMessage) {
        RuleType ruleType = new RuleType();
        ruleType.setExpression(expression);
        ruleType.setBrId(brId);
        ruleType.setNote(note);
        ruleType.setErrorType(type);
        ruleType.setMessage(errorMessage);
        ruleType.setLevel("LevelName");
        return ruleType;
    }
}
