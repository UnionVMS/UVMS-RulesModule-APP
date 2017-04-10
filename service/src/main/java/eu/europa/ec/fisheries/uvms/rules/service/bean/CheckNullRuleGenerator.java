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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.schema.rules.rule.v1.NotNullRule;
import eu.europa.ec.fisheries.schema.rules.rule.v1.Rule;
import eu.europa.ec.fisheries.schema.rules.template.v1.Template;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import lombok.SneakyThrows;
import org.drools.template.ObjectDataCompiler;

public class CheckNullRuleGenerator extends TemplateRuleGenerator {

    public List<String> computeRules(Template template, List<Rule> rules) {

        ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
        List<String> ruleDefinitions = new ArrayList<>();

        for (Rule rule : rules) {
            Map<String, Object> data = new HashMap<>();
            data.put("checkNullAttribute", ((NotNullRule)rule).getAttribute());
            String ruleDefinition = objectDataCompiler.compile(Arrays.asList(data), template.getLhs());
            ruleDefinitions.add(ruleDefinition);
        }
        return ruleDefinitions;
    }

    public TemplateType getTemplateType() {
        return TemplateType.CHECKNULL;
    }
}
