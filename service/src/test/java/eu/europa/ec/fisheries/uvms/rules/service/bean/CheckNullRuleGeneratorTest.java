/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
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

import eu.europa.ec.fisheries.schema.rules.rule.v1.Rule;
import eu.europa.ec.fisheries.schema.rules.rule.v1.WarningRule;
import eu.europa.ec.fisheries.schema.rules.template.v1.Template;
import org.junit.Test;

/**
 * Created by padhyad on 4/10/2017.
 */
public class CheckNullRuleGeneratorTest {

    @Test
    public void testComputeRule() {
        CheckNullRuleGenerator nullRuleGenerator = new CheckNullRuleGenerator();
        Template template = new Template();
    }

    private String getRuleTemplate(Template template, Rule rule) {

        String ruleStr = "template header" + "\n\n" +
                "rule" + "\n\n" +
                // Add input
                "package eu.europa.ec.fisheries.uvms.rules.service.business." + template.getTemplateName() + "\n\n" +
                "import eu.europa.ec.fisheries.uvms.rules.service.business.FaReportDocumentFact" + "\n\n\n" +
                "template " + template.getTemplateName() + "\n\n" +
                "rule " + template.getTemplateName() + " : @{row.rowNumber} @{rule.id}" + "\n\n" +
                "when" + "\n\n" +
                "$fact : FaReportDocumentFact(" + template.getLhs() + ")" + "\n\n" +
                "then" + "\n\n" +
                "$fact.setOk(false)" + "\n\n" +
                template.getThenStatement() + "\n\n" +
                "end" + "\n\n" +
                "end template" + "\n\n";

        return ruleStr;

    }
}