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

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.SalesRulesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.TemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.definition.KiePackage;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.definition.KnowledgePackage;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Singleton
@LocalBean
public class FactRuleEvaluator {

    @EJB
    private SalesRulesService salesRulesService;

    private KieServices kieServices = KieServices.Factory.get();
    private KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    private List<String> failedRules = new ArrayList<>();
    private List<AbstractFact> exceptionsList = new ArrayList<>();
    private List<String> systemPackagesPaths = new ArrayList<>();


    public void reInitializeKieSystem() {
        failedRules = new ArrayList<>();
        exceptionsList = new ArrayList<>();
        log.info("[START] --> Reinitializing KieSession and cleaning KiePackages..");
        KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KnowledgeBaseImpl kBase = (KnowledgeBaseImpl) kContainer.getKieBase();
        Collection<KiePackage> kiePackages = kBase.getKiePackages();
        if (CollectionUtils.isNotEmpty(kiePackages)) {
            kBase.removeKiePackage(kiePackages.iterator().next().getName());
        }
        kieFileSystem.delete(systemPackagesPaths.toArray(new String[systemPackagesPaths.size()]));
        log.info("[END] --> Deleted [" + systemPackagesPaths.size() + "] packages from KieFileSystem.");
        systemPackagesPaths.clear();
    }

    public void initializeRules(Collection<TemplateRuleMapDto> templates) {
        Map<String, String> drlsAndRules = new HashMap<>();
        for (TemplateRuleMapDto template : templates) {
            String templateFile = TemplateFactory.getTemplateFileName(template.getTemplateType().getType());
            String templateName = template.getTemplateType().getTemplateName();
            log.debug("Initializing template: " + templateName);
            drlsAndRules.putAll(generateRulesFromTemplate(templateName, templateFile, template.getRules()));
            drlsAndRules.putAll(generateExternalRulesFromTemplate(template.getExternalRules()));
        }
        Collection<KiePackage> packages = createAllPackages(drlsAndRules);
        buildAllPackages(packages);
    }


    public void validateFact(Collection<AbstractFact> facts) {
        KieSession ksession = null;
        try {
            KieContainer container = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            ksession = container.newKieSession();

            ksession.setGlobal("salesService", salesRulesService);

            for (AbstractFact fact : facts) { // Insert All the facts
                ksession.insert(fact);
            }
            ksession.fireAllRules();
            ksession.dispose();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Collection<?> objects = null; // FIXME whole fact is remove this is not right
            if (ksession != null) {
                objects = ksession.getObjects();
            }
            if (CollectionUtils.isNotEmpty(objects)) {
                Collection<AbstractFact> failedFacts = (Collection<AbstractFact>) objects;
                AbstractFact next = failedFacts.iterator().next();
                String message = e.getMessage();
                String brId = message.substring(message.indexOf('/') + 1, message.indexOf(".drl"));
                //     next.addWarningOrError("WARNING", message, brId, "L099", StringUtils.EMPTY);
                //     next.setOk(false);
                facts.remove(next);
                exceptionsList.add(next);
                validateFact(facts);
            }
        }

    }

    public List<AbstractFact> getExceptionsList() {
        return exceptionsList;
    }

    public void setExceptionsList(List<AbstractFact> exceptionsList) {
        this.exceptionsList = exceptionsList;
    }

    public List<String> getFailedRules() {
        return failedRules;
    }

    private Map<String, String> generateRulesFromTemplate(String templateName, String templateFile, List<RuleType> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyMap();
        }
        InputStream templateStream = this.getClass().getResourceAsStream(templateFile);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        Map<String, String> drlsAndBrId = new HashMap<>();
        TemplateDataListener listener = new TemplateDataListener(tc);
        int rowNum = 0;
        for (RuleType ruleDto : rules) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, templateName, 0);
            listener.newCell(rowNum, 1, ruleDto.getExpression(), 0);
            listener.newCell(rowNum, 2, ruleDto.getBrId(), 0);
            listener.newCell(rowNum, 3, ruleDto.getMessage(), 0);
            listener.newCell(rowNum, 4, ruleDto.getErrorType().value(), 0);
            listener.newCell(rowNum, 5, ruleDto.getLevel(), 0);
            listener.newCell(rowNum, 6, ruleDto.getPropertyNames(), 0);
            rowNum++;
        }
        listener.finishSheet();
        String drl = listener.renderDRL();
        log.debug(drl);
        drlsAndBrId.put(drl, templateName);
        return drlsAndBrId;
    }

    private Map<String, String> generateExternalRulesFromTemplate(List<ExternalRuleType> externalRules) {
        if (CollectionUtils.isEmpty(externalRules)) {
            return Collections.emptyMap();
        }
        Map<String, String> drlsAndBrId = new HashMap<>();
        for (ExternalRuleType extRuleType : externalRules) {
            String drl = extRuleType.getDrl();
            log.debug("DRL for BR Id {} : {} ", extRuleType.getBrId(), drl);
            drlsAndBrId.put(drl, extRuleType.getBrId());
        }
        return drlsAndBrId;
    }


    private Collection<KiePackage> createAllPackages(Map<String, String> drlsAndRules) {
        Collection<KiePackage> compiledPackages = new ArrayList<>();
        for (Map.Entry<String, String> ruleEntrySet : drlsAndRules.entrySet()) {
            String rule = ruleEntrySet.getKey();
            String templateName = ruleEntrySet.getValue();
            StringBuilder ruleName = new StringBuilder("src/main/resources/rule/");
            String systemPackage = ruleName.append(templateName).append(".drl").toString();
            systemPackagesPaths.add(systemPackage);
            kieFileSystem.write(systemPackage, rule);
            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
            if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
                log.error("Rule failed to build {} ", templateName);
                kieFileSystem.delete(ruleName.toString(), rule);
                failedRules.add(templateName);
                continue;
            }
            kieFileSystem.generateAndWritePomXML(kieServices.getRepository().getDefaultReleaseId());
            KieContainer container = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

            KieBase kBase = container.getKieBase();
            compiledPackages = kBase.getKiePackages();
        }
        return compiledPackages;
    }

    private void buildAllPackages(Collection<KiePackage> packages) {
        if (packages == null || packages.isEmpty()) {
            log.error("Rule not initialized as there is not Rules defined");
            return;
        }
        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
        kieFileSystem.writeKModuleXML(kieModuleModel.toXML());
        kieFileSystem.generateAndWritePomXML(kieServices.getRepository().getDefaultReleaseId());

        kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KnowledgeBaseImpl kBase = (KnowledgeBaseImpl) kContainer.getKieBase();

        Collection<KnowledgePackage> allPackages = Collections2.transform(packages, new Function<KiePackage, KnowledgePackage>() {
            @Override
            public KnowledgePackage apply(KiePackage kiePackage) {
                return (KnowledgePackage) kiePackage;
            }
        });

        kBase.addKnowledgePackages(allPackages);
    }
}
