/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleWarning;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @autor padhyad
 * @author Gregory Rinaldi
 */
@Slf4j
public class FactRuleEvaluatorTest {

    @Test
    public void testComputeRule() {
        List<TemplateRuleMapDto> templates = new ArrayList<>();
        templates.add(getTemplateRuleMapForFaReport());
        templates.add(getTemplateRuleMapForVesselTM());

        Collection<AbstractFact> facts = new ArrayList<>();
        facts.add(getFaReportDocumentFact());
        facts.add(getVesselTransportMeansFact());

        // First Validation
        FactRuleEvaluator generator = new FactRuleEvaluator();
        generator.initializeRules(templates);
        generator.validateFact(facts);

        // validate facts
        validateFacts(facts);

        // Second Validation
        facts.clear();

    }

    @Test
    public void testComputeExternalRule() {
        List<TemplateRuleMapDto> templates = new ArrayList<>();
        templates.add(getTemplateExternalRuleMapForFaReport());
        templates.add(getTemplateRuleMapForVesselTM());

        Collection<AbstractFact> facts = new ArrayList<>();
        facts.add(getFaReportDocumentFact());
        facts.add(getVesselTransportMeansFact());

        // First Validation
        FactRuleEvaluator generator = new FactRuleEvaluator();
        generator.initializeRules(templates);
        generator.validateFact(facts);

        // validate facts
        validateFacts(facts);

        // Second Validation
        facts.clear();

    }

    @Test
    public void testReInitializeKieSystem() {
        FactRuleEvaluator generator = new FactRuleEvaluator();
        generator.reInitializeKieSystem();
        assertEquals(generator.getExceptionsList().size(), 0);
        assertEquals(generator.getFailedRules().size(), 0);
    }

    @Test
    public void testValidateFact() {
        List<TemplateRuleMapDto> templates = new ArrayList<>();

        TemplateType template = new TemplateType();
        template.setTemplateName("Test Template");
        template.setType(FactType.FA_REPORT_DOCUMENT);

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();

        RuleType ruleTypeCode = RuleTestHelper.createRuleType("typeCode.value.isEmpty()", "1", "Test Notes", ErrorType.ERROR, "typeCode value is null");
        templateRuleMapDto.setRules(Arrays.asList(ruleTypeCode));
        templateRuleMapDto.setTemplateType(template);
        templateRuleMapDto.setExternalRules(new ArrayList<ExternalRuleType>());

        templates.add(templateRuleMapDto);

        Collection<AbstractFact> facts = new ArrayList<>();

        FaReportDocumentFact fact = new FaReportDocumentFact();
        fact.setTypeCode(RuleTestHelper.getCodeType(null, null));
        facts.add(fact);

        FactRuleEvaluator generator = new FactRuleEvaluator();
        generator.initializeRules(templates);
        generator.validateFact(facts);
        assertTrue(facts.isEmpty());
    }

    private VesselTransportMeansFact getVesselTransportMeansFact() {
        VesselTransportMeansFact vesselTransportMeansFact = new VesselTransportMeansFact();
        vesselTransportMeansFact.setRoleCode(new CodeType("ABC"));
        return vesselTransportMeansFact;
    }

    private FaReportDocumentFact getFaReportDocumentFact() {
        FaReportDocumentFact fact = new FaReportDocumentFact();

        fact.setTypeCode(RuleTestHelper.getCodeType("typecode",null));
        fact.setPurposeCode(RuleTestHelper.getCodeType("purpose2","FLUX_GP_PURPOSE"));
        fact.setAcceptanceDateTime(new Date());
        fact.setAcceptanceDateTime(new Date());
        return fact;
    }

    private void validateFacts(Collection<AbstractFact> facts) {
        for(AbstractFact abstractFact: facts){
            log.debug("------"+abstractFact.getFactType()+"------");
            List<RuleError> ruleErrors=  abstractFact.getErrors();
            for(RuleError ruleError:ruleErrors){
                log.debug("Id:"+ruleError.getRuleId()+" Error:"+ruleError.getMessage());
            }
            assertTrue(ruleErrors.isEmpty());
            List<RuleWarning> ruleWarnings=abstractFact.getWarnings();
            assertTrue(ruleWarnings.isEmpty());
            log.debug("------"+abstractFact.getFactType()+" is successful------");
        }
    }

    private TemplateRuleMapDto getTemplateRuleMapForFaReport() {

        TemplateType template = new TemplateType();
        template.setTemplateName("Test Template");
        template.setType(FactType.FA_REPORT_DOCUMENT);

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(getRulesForFaReportDocumentFact());
        templateRuleMapDto.setTemplateType(template);
        templateRuleMapDto.setExternalRules(new ArrayList<ExternalRuleType>());

        return templateRuleMapDto;
    }

    private TemplateRuleMapDto getTemplateExternalRuleMapForFaReport() {

        TemplateType template = new TemplateType();
        template.setTemplateName("Test Template");
        template.setType(FactType.FA_REPORT_DOCUMENT);

        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setExternalRules(getExternalRulesForFaReportDocumentFact());
        templateRuleMapDto.setTemplateType(template);

        return templateRuleMapDto;
    }

    private List<RuleType> getRulesForFaReportDocumentFact() {
        List<RuleType> rules = new ArrayList<>();

        RuleType ruleTypeCode = RuleTestHelper.createRuleType("typeCode.value == null","1" ,"Test Notes",ErrorType.ERROR,"typeCode value is null");
        RuleType ruleAcceptanceDateTime = RuleTestHelper.createRuleType("acceptanceDateTime == null","3" ,"Test Notes",ErrorType.ERROR,"acceptanceDateTime is null");
        RuleType rulePurposeCode = RuleTestHelper.createRuleType("purposeCode == null","4" ,"Test Notes",ErrorType.ERROR,"purposeCode is null");
        RuleType rulePurposeCodeListId = RuleTestHelper.createRuleType("purposeCode.listId != 'FLUX_GP_PURPOSE' ","5" ,"Test Notes",ErrorType.ERROR,"rulePurposeCodeListId is not FLUX_GP_PURPOSE");

        rules.add(ruleTypeCode);
        rules.add(ruleAcceptanceDateTime);
        rules.add(rulePurposeCode);
        rules.add(rulePurposeCodeListId);
        return rules;
    }

    private List<ExternalRuleType> getExternalRulesForFaReportDocumentFact() {
        List<ExternalRuleType> rules = new ArrayList<>();

        String drl1 = "SOMEDRLSTOPUTHERE";
        String drl2 = "SOMEDRLSTOPUTHERE";
        String drl3 = "SOMEDRLSTOPUTHERE";
        String drl4 = "SOMEDRLSTOPUTHERE";

        ExternalRuleType ruleTypeCode = createExternalRuleType("typeCode.value == null","1" ,drl1 ,ErrorType.ERROR,"typeCode value is null");
        ExternalRuleType ruleAcceptanceDateTime = createExternalRuleType("acceptanceDateTime == null","3" ,"Test Notes",ErrorType.ERROR,"acceptanceDateTime is null");
        ExternalRuleType rulePurposeCode = createExternalRuleType("purposeCode == null","4" ,drl1 ,ErrorType.ERROR,"purposeCode is null");
        ExternalRuleType rulePurposeCodeListId = createExternalRuleType("purposeCode.listId != 'FLUX_GP_PURPOSE' ","5" ,drl1 ,ErrorType.ERROR,"rulePurposeCodeListId is not FLUX_GP_PURPOSE");

        rules.add(ruleTypeCode);
        rules.add(ruleAcceptanceDateTime);
        rules.add(rulePurposeCode);
        rules.add(rulePurposeCodeListId);
        return rules;
    }

    private TemplateRuleMapDto getTemplateRuleMapForVesselTM() {
        TemplateType vsl = new TemplateType();
        vsl.setTemplateName("Vessel Template");
        vsl.setType(FactType.VESSEL_TRANSPORT_MEANS);

        RuleType vrule = RuleTestHelper.createRuleType("roleCode.value == null","2" ,"Test Notes",ErrorType.ERROR,"Role code value is null");

        TemplateRuleMapDto vesselTmp = new TemplateRuleMapDto();
        vesselTmp.setRules(Arrays.asList(vrule));
        vesselTmp.setTemplateType(vsl);
        vesselTmp.setExternalRules(new ArrayList<ExternalRuleType>());
        return vesselTmp;
    }


    private ExternalRuleType createExternalRuleType(String drl, String brId,String note, ErrorType type, String errorMessage){
        ExternalRuleType ruleType = new ExternalRuleType();
        ruleType.setDrl(drl);
        ruleType.setBrId(brId );
        ruleType.setNote(note);
        ruleType.setErrorType(type);
        ruleType.setMessage(errorMessage);

        return ruleType;
    }

}