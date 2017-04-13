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

import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaReportDocumentFact;
import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FactRuleGenerator extends RuleGenerator {

    public List<String> computeRules(String template, List<RuleType> rules) {

        List<String> rulesDefinitions = new ArrayList<>();

        InputStream templateStream = this.getClass().getResourceAsStream(template);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        TemplateDataListener listener = new TemplateDataListener(tc);

        int rowNum = 0;
        for (RuleType ruleDto : rules) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, ruleDto.getExpression(), 0);
            listener.newCell(rowNum, 1, ruleDto.getBrId(), 0);
            listener.newCell(rowNum, 2, ruleDto.getNote(), 0);
            //listener.newCell(rowNum, 3, ruleDto.getRuleDef(), 0);
            //listener.newCell(rowNum, 4, ruleDto.getMessageType().value(), 0);

            listener.finishSheet();
            String drl = listener.renderDRL();
            rulesDefinitions.add(drl);
            rowNum++;
        }




/*        ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
        List<String> ruleDefinitions = new ArrayList<>();

        for (Rule rule : rules) {
            Map<String, Object> data = new HashMap<>();
            data.put("checkNullAttribute", ((NotNullRule)rule).getAttribute());
            String ruleDefinition = objectDataCompiler.compile(Arrays.asList(data), template.getLhs());
            ruleDefinitions.add(ruleDefinition);
        }*/
        return rulesDefinitions;
    }

    public void validateRule(String rule, FaReportDocumentFact fact) {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem  kieFileSystem = kieServices.newKieFileSystem();

        kieFileSystem.write("src/main/resources/rules/test.drl", rule);

        // Create session
        kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieContainer customKcontainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

        KieSession ksession = customKcontainer.newKieSession();

        ksession.insert(fact);
        ksession.fireAllRules();
    }

}
