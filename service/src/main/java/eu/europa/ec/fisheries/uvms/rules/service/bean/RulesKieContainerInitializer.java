/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ContextExpressionType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.TemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

@Singleton
@Startup
@Slf4j
public class RulesKieContainerInitializer {

    @EJB
    private RulesDomainModel rulesDb;

    private Map<ContainerType, KieContainer> containers;

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void init() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            List<TemplateRuleMapDto> allTemplates = rulesDb.getAllFactTemplatesAndRules();

            List<TemplateRuleMapDto> faResponseTemplatesAndRules = getFaResponseRules(allTemplates);
            List<TemplateRuleMapDto> faTemplatesAndRules = getFaMessageRules(allTemplates);
            List<TemplateRuleMapDto> salesTemplatesAndRules = getSalesRules(allTemplates);
            List<TemplateRuleMapDto> faQueryTemplatesAndRules = getFaQueryRules(allTemplates);

            log.info("Initializing templates and rules for FA-Report facts. Nr. of Rules : {}",  countRuleExpressions(faTemplatesAndRules));
            KieContainer faReportContainer = createContainer(faTemplatesAndRules);

            log.info("Initializing templates and rules for FA-Response facts. Nr. of Rules : {}",  countRuleExpressions(faResponseTemplatesAndRules));
            KieContainer faRespContainer = createContainer(faResponseTemplatesAndRules);

            log.info("Initializing templates and rules for FA-Query facts. Nr. of Rules : {}", countRuleExpressions(faQueryTemplatesAndRules));
            KieContainer faQueryContainer = createContainer(faQueryTemplatesAndRules);

            log.info("Initializing templates and rules for Sales facts. Nr. of Rules : {}", countRuleExpressions(salesTemplatesAndRules));
            KieContainer salesContainer = createContainer(salesTemplatesAndRules);

            containers = new EnumMap<>(ContainerType.class);
            containers.put(ContainerType.FA_REPORT, faReportContainer);
            containers.put(ContainerType.FA_RESPONSE, faRespContainer);
            containers.put(ContainerType.FA_QUERY, faQueryContainer);
            containers.put(ContainerType.SALES, salesContainer);

            // To make sure that we have deployed all the templates!
            if (!allTemplates.isEmpty()) {
                throw new RuntimeException("Please include all the <code>FactType</code> in the KieContainers");
            }
            log.info("It took " + stopwatch + " to initialize the rules.");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Couldn't initialize rules engine!!");
        }
    }

    private int countRuleExpressions(List<TemplateRuleMapDto> templates) {
        AtomicInteger nrOfFaRules = new AtomicInteger();
        templates.forEach(temp -> {
            temp.getRules().forEach(rule->{
                log.info("Adding rule {} which has {} contextExpressions.", rule.getBrId(), rule.getContextExpressionList().size());
                nrOfFaRules.addAndGet(rule.getContextExpressionList().size());
            });
        });
        return nrOfFaRules.get();
    }

    private KieContainer createContainer(Collection<TemplateRuleMapDto> templates) {
        Map<String, String> drlsAndRules = new HashMap<>();
        for (TemplateRuleMapDto template : templates) {
            String templateFile = TemplateFactory.getTemplateFileName(template.getTemplateType().getType());
            String templateName = template.getTemplateType().getTemplateName();
            List<RuleType> rulesListForThisTemplate = template.getRules();
            drlsAndRules.putAll(generateRulesFromTemplate(templateName, templateFile, rulesListForThisTemplate));
        }

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        for (Map.Entry<String, String> ruleEntrySet : drlsAndRules.entrySet()) {
            String rule = ruleEntrySet.getKey();
            String templateName = ruleEntrySet.getValue();
            String systemPackage = "src/main/resources/rule/" + templateName + ".drl";
            kieFileSystem.write(systemPackage, rule);
        }
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();

        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            for (Message result : results.getMessages()) {
                log.info(result.getText());
            }
            throw new RuntimeException("COMPILATION ERROR IN RULES. PLEASE ADAPT THE FAILING EXPRESSIONS AND TRY AGAIN");
        }
        return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    }

    private Map<String, String> generateRulesFromTemplate(String templateName, String templateFile, List<RuleType> ruleTypeList) {
        if (CollectionUtils.isEmpty(ruleTypeList)) {
            return Collections.emptyMap();
        }
        InputStream templateStream = this.getClass().getResourceAsStream(templateFile);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        Map<String, String> drlsAndBrId = new HashMap<>();
        TemplateDataListener listener = new TemplateDataListener(tc);
        int rowNum = 0;
        for (RuleType ruleDto : ruleTypeList) {
            for (ContextExpressionType contextExpressionType : ruleDto.getContextExpressionList()) {
                String ruleContext = contextExpressionType.getContext() != null ? contextExpressionType.getContext() : "NULL";
                listener.newRow(rowNum, 0);
                listener.newCell(rowNum, 0, templateName, 0);
                listener.newCell(rowNum, 1, contextExpressionType.getExpression(), 0);
                listener.newCell(rowNum, 2, ruleDto.getBrId(), 0);
                listener.newCell(rowNum, 3, contextExpressionType.getFailureMessage(), 0);
                listener.newCell(rowNum, 4, ruleDto.getErrorType().value(), 0);
                listener.newCell(rowNum, 5, ruleDto.getLevel(), 0);
                listener.newCell(rowNum, 6, ruleDto.getPropertyNames(), 0);
                listener.newCell(rowNum, 7, ruleContext, 0);
                rowNum++;
            }
        }
        listener.finishSheet();
        String drl = listener.renderDRL();
        log.debug(drl);
        drlsAndBrId.put(drl, templateName);
        return drlsAndBrId;
    }

    @AccessTimeout(value = 180, unit = SECONDS)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void reload() {
        init();
    }

    private List<TemplateRuleMapDto> getSalesRules(List<TemplateRuleMapDto> templatesAndRules) {
        List<TemplateRuleMapDto> responseTemplates = new ArrayList<>();
        List<FactType> salesFactsTypes = ContainerType.SALES.getFactTypesList();
        for (TemplateRuleMapDto templatesAndRule : templatesAndRules) {
            if (salesFactsTypes.contains(templatesAndRule.getTemplateType().getType())) {
                responseTemplates.add(templatesAndRule);
            }
        }
        templatesAndRules.removeAll(responseTemplates);
        return responseTemplates;
    }

    private List<TemplateRuleMapDto> getFaMessageRules(List<TemplateRuleMapDto> templatesAndRules) {
        List<TemplateRuleMapDto> faTemplates = new ArrayList<>();
        List<FactType> faReportFacts = ContainerType.FA_REPORT.getFactTypesList();
        for (TemplateRuleMapDto templatesAndRule : templatesAndRules) {
            if (faReportFacts.contains(templatesAndRule.getTemplateType().getType())) {
                faTemplates.add(templatesAndRule);
            }
        }
        templatesAndRules.removeAll(faTemplates);
        return faTemplates;
    }

    private List<TemplateRuleMapDto> getFaResponseRules(List<TemplateRuleMapDto> templatesAndRules) {
        List<TemplateRuleMapDto> responseTemplates = new ArrayList<>();
        List<FactType> factTypesList = ContainerType.FA_RESPONSE.getFactTypesList();
        for (TemplateRuleMapDto templatesAndRule : templatesAndRules) {
            if (factTypesList.contains(templatesAndRule.getTemplateType().getType())) {
                responseTemplates.add(templatesAndRule);
            }
        }
        templatesAndRules.removeAll(responseTemplates);
        return responseTemplates;
    }

    private List<TemplateRuleMapDto> getFaQueryRules(List<TemplateRuleMapDto> allTemplates) {
        List<TemplateRuleMapDto> faQueryTemplates = new ArrayList<>();
        List<FactType> factTypesList = ContainerType.FA_QUERY.getFactTypesList();
        for (TemplateRuleMapDto actualTemplate : allTemplates) {
            if (factTypesList.contains(actualTemplate.getTemplateType().getType())) {
                faQueryTemplates.add(actualTemplate);
            }
        }
        allTemplates.removeAll(faQueryTemplates);
        return faQueryTemplates;
    }

    public KieContainer getContainerByType(ContainerType containerType) {
        return containers.get(containerType);
    }

    public boolean isRulesLoaded() {
        List<Rule> deployedRules = new ArrayList<>();
        KieContainer container = containers.get(ContainerType.FA_REPORT);
        Collection<KiePackage> kiePackages = container.getKieBase().getKiePackages();
        for (KiePackage kiePackage : kiePackages) {
            deployedRules.addAll(kiePackage.getRules());
        }
        return CollectionUtils.isNotEmpty(deployedRules);
    }
}
