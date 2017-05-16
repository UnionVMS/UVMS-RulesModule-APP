/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType.ERROR;
import static eu.europa.ec.fisheries.schema.rules.template.v1.FactType.FA_REPORT_DOCUMENT;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Gregory Rinaldi
 */
public class FLUX_ReportDocumentRuleTest {

    private FactRuleEvaluator generator = new FactRuleEvaluator();

    private TemplateType template = new TemplateType();
    private TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
    private RuleType ruleID = new RuleType();
    private RuleType ruleCreationDateTime = new RuleType();
    private RuleType ruleReferencedID = new RuleType();

    @Before
    public void beforeClass() {

        ruleCreationDateTime.setExpression("creationDateTime == null");
        ruleCreationDateTime.setBrId("FA-L00-00-0005");
        ruleCreationDateTime.setErrorType(ERROR);
        ruleCreationDateTime.setMessage("CreationDateTime Must be present.");

        ruleReferencedID.setExpression("referencedID == null || referencedID.schemeId == null || isUUID(referencedID.schemeId) == false");
        ruleReferencedID.setBrId("FA-L00-00-0011");
        ruleReferencedID.setErrorType(ERROR);
        ruleReferencedID.setMessage("SchemeID Must be UUID.");
        ruleReferencedID.setLevel("Level");

        template.setTemplateName("Test Template");
        template.setType(FA_REPORT_DOCUMENT);

        templateRuleMapDto.setTemplateType(template);
        templateRuleMapDto.setExternalRules(new ArrayList<ExternalRuleType>());

    }

    @Test
    public void testRuleReferencedID() {

        templateRuleMapDto.setRules(Collections.singletonList(ruleReferencedID));
        templateRuleMapDto.setExternalRules(new ArrayList<ExternalRuleType>());
        generator.initializeRules(Collections.singletonList(templateRuleMapDto));

        AbstractFact fact = new FaReportDocumentFact();
        IdType idType = new IdType();
        idType.setValue("id1");
        idType.setSchemeId("1cc5c060-2b84-11e7-93ae-92361f002671");
        ((FaReportDocumentFact) fact).setReferencedID(idType);

        generator.validateFact(Collections.singletonList(fact));

        assertTrue(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }

    @Test
    public void testRuleCreationDateTime() {

        templateRuleMapDto.setRules(Collections.singletonList(ruleCreationDateTime));
        templateRuleMapDto.setExternalRules(new ArrayList<ExternalRuleType>());
        generator.initializeRules(Collections.singletonList(templateRuleMapDto));

        AbstractFact fact = new FaReportDocumentFact();
        ((FaReportDocumentFact) fact).setCreationDateTime(new Date());

        generator.validateFact(Collections.singletonList(fact));

        assertTrue(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }

    @Test
    public void testRuleID() {

        ruleID = new RuleType();
        ruleID.setExpression("ids == null || ids.empty == true || ids.get(1).value != 'abc'");
        ruleID.setBrId("FA-L00-00-0001");
        ruleID.setErrorType(ERROR);
        ruleID.setMessage("ID Must be present.");

        templateRuleMapDto.setRules(Collections.singletonList(ruleID));
        templateRuleMapDto.setExternalRules(new ArrayList<ExternalRuleType>());
        generator.initializeRules(Collections.singletonList(templateRuleMapDto));

        AbstractFact fact = new FaReportDocumentFact();
        ArrayList<IdType> idTypes = new ArrayList<>();
        idTypes.add(new IdType());
        IdType idType = new IdType();
        idType.setValue("abc");
        idTypes.add(idType);
        ((FaReportDocumentFact) fact).setIds(idTypes);

        generator.validateFact(Collections.singletonList(fact));

        assertTrue(fact.getErrors().isEmpty());
        assertTrue(fact.getWarnings().isEmpty());

    }
}
