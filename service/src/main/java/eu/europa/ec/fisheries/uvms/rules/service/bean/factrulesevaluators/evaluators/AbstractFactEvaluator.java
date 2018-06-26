package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ExternalRuleType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.TemplateFactory;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceTechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

@Slf4j
abstract class AbstractFactEvaluator {

    private List<String> failedRules = new ArrayList<>();
    private List<AbstractFact> exceptionsList = new ArrayList<>();

    public KieContainer initializeRules(Collection<TemplateRuleMapDto> templates, String containerName, String packageName) {
        Map<String, String> drlsAndRules = new HashMap<>();
        for (TemplateRuleMapDto template : templates) {
            String templateFile = TemplateFactory.getTemplateFileName(template.getTemplateType().getType());
            String templateName = template.getTemplateType().getTemplateName();
            drlsAndRules.putAll(generateRulesFromTemplate(templateName, templateFile, template.getRules()));
            drlsAndRules.putAll(generateExternalRulesFromTemplate(template.getExternalRules()));
        }
        return createContainer(containerName, drlsAndRules, packageName);
    }


    public KieContainer createContainer(String containerName, Map<String, String> drlsAndRules, String packageName){
        KieServices kieServices = KieServices.Factory.get();
        ReleaseId releaseId = kieServices.newReleaseId(packageName, containerName, "1.0.0");
        Resource resource = kieServices.getResources().newByteArrayResource(createKJar(releaseId, drlsAndRules));
        KieModule module = kieServices.getRepository().addKieModule(resource);
        KieContainer kieContainer = kieServices.newKieContainer(module.getReleaseId());
        kieServices.getRepository().removeKieModule(module.getReleaseId());
        return kieContainer;
    }

    public static byte[] createKJar(ReleaseId releaseId, Map<String, String> drlsAndRules) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.generateAndWritePomXML(releaseId);
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
                log.debug(result.getText());
            }
            throw new RuntimeException("COMPILATION ERROR IN RULES. PLEASE ADAPT THE FAILING EXPRESSIONS AND TRY AGAIN");
        }
        InternalKieModule kieModule = (InternalKieModule) kieServices.getRepository().getKieModule(releaseId);
        return kieModule.getBytes();
    }

    public Map<String, String> generateRulesFromTemplate(String templateName, String templateFile, List<RuleType> rules) {
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


    public Map<String, String> generateExternalRulesFromTemplate(List<ExternalRuleType> externalRules) {
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

    public void validateFacts(Collection<AbstractFact> facts, KieContainer container) {
        KieSession ksession = null;
        Map<String, Object> globalsMap = getGlobalsMap();
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            ksession = container.newKieSession();
            if(MapUtils.isNotEmpty(globalsMap)){
                for(Map.Entry<String, Object> entry : globalsMap.entrySet()){
                    ksession.setGlobal(entry.getKey(), entry.getValue());
                }
            }
            for (AbstractFact fact : facts) { // Insert All the facts
                ksession.insert(fact);
            }
            int numberOfFiredRules = ksession.fireAllRules();
            ksession.dispose();
            log.debug("Drools eval took : [{}] ms. In this time [{}] rules were fired.", stopwatch.elapsed(TimeUnit.MILLISECONDS), numberOfFiredRules);
        } catch (RuntimeException e) {
            String errorMessage = "Unable to validate facts. Reason: " + e.getMessage();
            log.error(errorMessage);
            throw new RulesServiceTechnicalException(errorMessage, e);
        } catch (Exception e) {
            log.warn("EXCEPTION IN EVALUATION OF RULE");
            Collection<?> objects = null;
            if (ksession != null) {
                objects = ksession.getObjects();
            }
            if (CollectionUtils.isNotEmpty(objects)) {
                Collection<AbstractFact> failedFacts = (Collection<AbstractFact>) objects;
                if (CollectionUtils.isNotEmpty(failedFacts)){
                    for (AbstractFact failedFact : failedFacts) {
                        String message = e.getMessage();
                        failedFact.addWarningOrError("WARNING", message, StringUtils.EMPTY, "L099", StringUtils.EMPTY);
                        failedFact.setOk(false);
                        facts.remove(failedFact); // remove fact with exception and re-validate the other facts
                        getExceptionsList().add(failedFact);
                    }
                }
                if(CollectionUtils.isNotEmpty(facts)){ // Avoid looping if the last fact launched an Exception
                    validateFacts(facts, container);
                }
            }
        }
    }

    abstract Map<String, Object> getGlobalsMap();

    public void setExceptionsList(List<AbstractFact> exceptionsList1) {
        exceptionsList =   exceptionsList1;
    }
    public List<AbstractFact> getExceptionsList() {
        return exceptionsList;
    }

}
