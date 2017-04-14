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
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.TemplateFactory;
import org.drools.core.marshalling.impl.ProtobufMessages;
import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FactRuleEvaluator {

    public void computeRules(Collection<TemplateRuleMapDto> templates, Collection<AbstractFact> facts) {

        List<String> rules = new ArrayList<>();

        for (TemplateRuleMapDto template : templates) {
            String templateFile = TemplateFactory.getTemplateFileName(template.getTemplateType().getType());
            String templateName = template.getTemplateType().getTemplateName();
            rules.addAll(generateRulesFromTemplate(templateName, templateFile, template.getRules()));
        }
        validateRule(rules, facts);
    }

    private List<String> generateRulesFromTemplate(String templateName, String templateFile, List<RuleType> rules) {
        InputStream templateStream = this.getClass().getResourceAsStream(templateFile);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        TemplateDataListener listener = new TemplateDataListener(tc);
        List<String> rulesDefinitions = new ArrayList<>();

        int rowNum = 0;
        for (RuleType ruleDto : rules) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, templateName, 0);
            listener.newCell(rowNum, 1, ruleDto.getExpression(), 0);
            listener.newCell(rowNum, 2, ruleDto.getBrId(), 0);
            listener.newCell(rowNum, 3, ruleDto.getMessage(), 0);
            listener.newCell(rowNum, 4, ruleDto.getErrorType().value(), 0);
            listener.finishSheet();
            String drl = listener.renderDRL();
            rulesDefinitions.add(drl);
            rowNum++;
        }
        return rulesDefinitions;
    }

    public void validateRule(List<String> rules, Collection<AbstractFact> facts) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem  kieFileSystem = kieServices.newKieFileSystem();

        int ruleIndex = 0;
        for (String rule : rules) {
            StringBuilder ruleName = new StringBuilder("src/main/resources/rule");
            ruleName.append(ruleIndex).append(".drl");
            kieFileSystem.write(ruleName.toString(), rule);
            ruleIndex ++;
        }
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();

        List<String> failedRules = new ArrayList<>();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            List<Message> messages = kieBuilder.getResults().getMessages(Message.Level.ERROR);
            for (Message message : messages) {
                failedRules.add(message.getPath());
            }
            //TODO check for rules with errors
            throw new RuntimeException();
        }

        KieContainer container = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieSession ksession = container.newKieSession();
        for (AbstractFact fact : facts) { // Insert All the facts
            ksession.insert(fact);
        }
        ksession.fireAllRules();
    }
}
