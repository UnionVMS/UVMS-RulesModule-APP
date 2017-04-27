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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.InOutType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaReportDocumentFact;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

public class FLUX_ReportDocumentRuleTest {

    private TemplateType template = new TemplateType();
    private TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
    private RuleType ruleID = new RuleType();

    @Before
    @SneakyThrows
    public void before() {


        URL url = ClassLoader.getSystemClassLoader().getResource("s001c_REP001-responding-to-QUE001_TRA.xml");

        //String xml = new java.util.Scanner(new File(url.toURI()),"UTF8").useDelimiter("\\Z").next();

        //JAXBMarshaller.unmarshallTextMessage();


        ruleID.setExpression("ids == null || ids.empty == true");
        ruleID.setBrId("FA-L00-00-0001");
        ruleID.setErrorType(ERROR);
        ruleID.setMessage("ID Must be present.");

        template.setInOutType(InOutType.IN);
        template.setTemplateName("Test Template");
        template.setType(FA_REPORT_DOCUMENT);

        templateRuleMapDto.setRules(Collections.singletonList(ruleID));
        templateRuleMapDto.setTemplateType(template);

    }

    @Test
    public void testIDAttributeShouldPass() {

        Collection<AbstractFact> facts = new ArrayList<>();
        FaReportDocumentFact fact = new FaReportDocumentFact();
        ArrayList<IDType> idTypes = new ArrayList<>();
        idTypes.add(new IDType());
        fact.setIds(idTypes);
        facts.add(fact);

        FactRuleEvaluator generator = FactRuleEvaluator.getInstance();
        generator.initializeRules(Collections.singletonList(templateRuleMapDto));
        generator.validateFact(facts);

        assertTrue(fact.getErrors().isEmpty());

    }

    @Ignore
    public void testIDAttributeShouldFail() {

        Collection<AbstractFact> facts = new ArrayList<>();
        FaReportDocumentFact fact = new FaReportDocumentFact();
        ArrayList<IDType> idTypes = new ArrayList<>();
        idTypes.add(new IDType());
        fact.setIds(idTypes);
        facts.add(fact);

        FactRuleEvaluator generator = FactRuleEvaluator.getInstance();
        generator.initializeRules(Collections.singletonList(templateRuleMapDto));
        generator.validateFact(facts);

        assertTrue(fact.getErrors().isEmpty());

    }

}
