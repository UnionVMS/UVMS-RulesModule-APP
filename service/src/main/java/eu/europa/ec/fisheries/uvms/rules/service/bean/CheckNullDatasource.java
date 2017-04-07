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

import eu.europa.ec.fisheries.schema.rules.template.v1.Template;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;
import eu.europa.ec.fisheries.uvms.rules.service.lifecycle.RuleLifecycleContainer;
import org.drools.template.ObjectDataCompiler;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless
public class CheckNullDatasource extends TemplateDatasource {

    public List<String> rulesDef = new ArrayList<>();


    public List<String> computeRules(Template template) {
        ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
        List<String> rules = new ArrayList<>();
        // retrieve here all your data
        for (String attribute : rulesDef) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("checkNullAttribute", attribute);
            String rule = objectDataCompiler.compile(Arrays.asList(data), template.getLhs());
            rules.add(rule);
        }
        return rules;
    }

    @PostConstruct
    // mock list, should be fetched from the database
    public void getAttributes() {
        // Make DB call
        rulesDef.add("person.name");
        rulesDef.add("person.firstName");
        rulesDef.add("person.lastName");

        RuleLifecycleContainer.registerTemplateDatasource(this, getTemplateType());
    }

    public TemplateType getTemplateType() {
        return TemplateType.CHECKNULL;
    }

}
