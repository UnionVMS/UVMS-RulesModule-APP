package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.InOutType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaCatchFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
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
        for(AbstractFact abstractFact: facts){

           assertTrue(abstractFact.getErrors().isEmpty());
           assertTrue(abstractFact.getWarnings().isEmpty());

        }

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

        RuleType ruleTypeCode = RuleTestHelper.createRuleType("typeCode.value == null || typeCode.listId !='FA_CATCH_TYPE' ","1" ,"Test Notes", ErrorType.ERROR,"typeCode value is null or listId is not correct");
        RuleType ruleSpeciesCode = RuleTestHelper.createRuleType("speciesCode.value == null || speciesCode.listId !='FAO_SPECIES' ","2" ,"Test Notes", ErrorType.ERROR,"speciesCode value is null or listId is not correct");
        RuleType ruleResultAAPProductUnitQuantity = RuleTestHelper.createRuleType("resultAAPProductUnitQuantity.empty == true ","3" ,"Test Notes", ErrorType.ERROR,"ResultAAPProduct do not have unitQuantity");
        RuleType ruleCatchUnitQuantityPositive = RuleTestHelper.createRuleType("unitQuantity.value < 0 ","4" ,"Test Notes", ErrorType.ERROR,"FACatch unitQuantity is less than 0 ");
        RuleType ruleCatchWeightMeasure = RuleTestHelper.createRuleType("weightMeasure==null || weightMeasure.unitCode !='KGM' ","5" ,"Test Notes", ErrorType.ERROR,"FACatch weightMeasure is not present OR  unitCode is not KGM");
        RuleType ruleCatchWeightMeasureDecimal = RuleTestHelper.createRuleType("weightMeasure.value matches '^[0-9]*.[0-9]{0,2}' == false ","5" ,"Test Notes", ErrorType.ERROR,"WeightMeasure is more than 2 decimal places");

        //RuleType ruleTest = RuleTestHelper.createRuleType("$testStringList : testStringList ,  $testStringList contains 'test' ","7" ,"Test Notes", ErrorType.ERROR,"FISH_SIZE_CLASS do not exist");

        rules.add(ruleTypeCode);
        rules.add(ruleSpeciesCode);
        rules.add(ruleResultAAPProductUnitQuantity);
        rules.add(ruleCatchUnitQuantityPositive);
        rules.add(ruleCatchWeightMeasure);
        rules.add(ruleCatchWeightMeasureDecimal);


        return rules;
    }


    private FaCatchFact getFaCatchFact() {
        FaCatchFact fact = new FaCatchFact();
        fact.setTypeCode(RuleTestHelper.getCodeType("typecode","FA_CATCH_TYPE"));
        fact.setSpeciesCode(RuleTestHelper.getCodeType("speciesCode","FAO_SPECIES"));

        List<MeasureType> measureTypes = new ArrayList<>();
        measureTypes.add(RuleTestHelper.getMeasureType(new BigDecimal(220),"KGM"));
        fact.setResultAAPProductUnitQuantity(measureTypes);
        fact.setUnitQuantity(RuleTestHelper.getMeasureType(new BigDecimal(220),"KGM"));
        fact.setWeightMeasure(RuleTestHelper.getMeasureType(new BigDecimal(2236.00),"KGM"));

        List<CodeType> sizeClassList = new ArrayList<>();
        sizeClassList.add(RuleTestHelper.getCodeType("sizeClass","FISH_SIZE_CLASS"));
        fact.setSizeDistributionClassCode(sizeClassList);


        return fact;
    }

}
