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

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.FactRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.rules.template.v1.InOutType;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by padhyad on 4/10/2017.
 */
public class FactRuleEvaluatorTest {

    @Test
    public void testComputeRule() {
        RuleType rule = new FactRuleType();
        rule.setExpression("typeCode != null ");
        rule.setBrId("1");
        rule.setNote("Test Notes");
        rule.setErrorType(ErrorType.ERROR);
        rule.setMessage("This is test message");

        TemplateType template = new TemplateType();
        template.setInOutType(InOutType.IN);
        template.setTemplateName("Test Template");
        template.setType(FactType.FA_REPORT_DOCUMENT);

        List<TemplateRuleMapDto> templates = new ArrayList<>();
        TemplateRuleMapDto templateRuleMapDto = new TemplateRuleMapDto();
        templateRuleMapDto.setRules(Arrays.asList(rule));
        templateRuleMapDto.setTemplateType(template);
        templates.add(templateRuleMapDto);


        TemplateType vsl = new TemplateType();
        vsl.setInOutType(InOutType.IN);
        vsl.setTemplateName("Vessel Template");
        vsl.setType(FactType.VESSEL_TRANSPORT_MEANS);

        RuleType vrule = new FactRuleType();
        vrule.setExpression("typeCode == null");
        vrule.setBrId("1");
        vrule.setNote("Test Notes");
        vrule.setErrorType(ErrorType.WARNING);
        vrule.setMessage("This is test message");

        TemplateRuleMapDto vesselTmp = new TemplateRuleMapDto();
        vesselTmp.setRules(Arrays.asList(vrule));
        vesselTmp.setTemplateType(vsl);
        templates.add(vesselTmp);

        Collection<AbstractFact > facts = new ArrayList<>();
        FaReportDocumentFact fact = new FaReportDocumentFact();
        fact.setTypeCode("CODE");
        facts.add(fact);

        VesselTransportMeansFact vesselTransportMeansFact = new VesselTransportMeansFact();
        //vesselTransportMeansFact.setTypeCode("ABC");
        facts.add(vesselTransportMeansFact);

        FactRuleEvaluator generator = new FactRuleEvaluator();
        generator.computeRules(templates, facts);
        System.out.print("");
    }
}