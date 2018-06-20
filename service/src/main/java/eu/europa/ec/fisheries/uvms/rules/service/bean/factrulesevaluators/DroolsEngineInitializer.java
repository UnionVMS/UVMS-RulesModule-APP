package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.FaQueryFactEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.FaReportFactRuleEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.FaResponseFactRuleEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.SalesFactRuleEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.business.EnrichedBRMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.util.*;

import static java.util.concurrent.TimeUnit.SECONDS;


/**
 * @author Andi Kovi
 * <p>
 * This class serves to intitialize the Fact Evaluators, check if the engine is up and running and reinitialize the engine if needed.
 */
@Singleton
@Startup
@Slf4j
public class DroolsEngineInitializer {

    @EJB
    private RulesDomainModel rulesDb;

    @EJB
    private FaReportFactRuleEvaluator faReportRuleEvaluator;

    @EJB
    private FaResponseFactRuleEvaluator responseRuleEvaluator;

    @EJB
    private SalesFactRuleEvaluator salesRuleEvaluator;

    @EJB
    private FaQueryFactEvaluator faQueryEvaluator;

    @EJB
    private MDRCacheRuleService cacheService;

    private Map<ContainerType, KieContainer> containers;

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void initialize() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            List<TemplateRuleMapDto> allTemplates = rulesDb.getAllFactTemplatesAndRules();
            refreshRulesValidationMessages(allTemplates);

            // Devide the templates by type
            List<TemplateRuleMapDto> faResponseTemplatesAndRules = getFaResponseRules(allTemplates);
            List<TemplateRuleMapDto> faTemplatesAndRules = getFaMessageRules(allTemplates);
            List<TemplateRuleMapDto> salesTemplatesAndRules = getSalesRules(allTemplates);
            List<TemplateRuleMapDto> faQueryTemplatesAndRules = getFaQueryRules(allTemplates);

            log.info("[START] Initializing templates and rules for FA-Report facts. Nr. of Rules : [{}]", faTemplatesAndRules.size());
            KieContainer faReportContainer = faReportRuleEvaluator.initializeRules(faTemplatesAndRules);

            log.info("[START] Initializing templates and rules for FA-Response facts. Nr. of Rules : [{}]", faResponseTemplatesAndRules.size());
            KieContainer faRespContainer = responseRuleEvaluator.initializeRules(faResponseTemplatesAndRules);

            log.info("[START] Initializing templates and rules for FA-Query facts. Nr. of Rules : [{}]", faQueryTemplatesAndRules.size());
            KieContainer faQueryContainer = responseRuleEvaluator.initializeRules(faQueryTemplatesAndRules);

            log.info("[START] Initializing templates and rules forSales facts. Nr. of Rules : [{}]", salesTemplatesAndRules.size());
            KieContainer salesContainer = salesRuleEvaluator.initializeRules(salesTemplatesAndRules);

            containers = new HashMap<>();
            containers.put(ContainerType.FA_REPORT, faReportContainer);
            containers.put(ContainerType.FA_RESPONSE, faRespContainer);
            containers.put(ContainerType.FA_QUERY, faQueryContainer);
            containers.put(ContainerType.SALES, salesContainer);

            // To make sure that we have deployed all the templates!
            if (allTemplates.isEmpty()) {
                throw new RuntimeException("[FATAL] Please include all the <code>FactType</code> in the KieContainers!!");
            }
            log.info("[END] It took " + stopwatch + " to initialize the rules.");
        } catch (RulesModelException e) {
            log.error(e.getMessage(), e);
        }
    }

    @AccessTimeout(value = 180, unit = SECONDS)
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void reInitialize() {
        initialize();
    }

    private List<TemplateRuleMapDto> getSalesRules(List<TemplateRuleMapDto> templatesAndRules) {
        List<TemplateRuleMapDto> responseTemplates = new ArrayList<>();
        List<FactType> salesFactsTypes = getSalesFactsTypes();
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
        List<FactType> faReportFacts = getFaReportFactsTypes();
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
        for (TemplateRuleMapDto templatesAndRule : templatesAndRules) {
            if (FactType.FA_RESPONSE.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_VALIDATION_QUALITY_ANALYSIS.equals(templatesAndRule.getTemplateType().getType())) {
                responseTemplates.add(templatesAndRule);
            }
        }
        templatesAndRules.removeAll(responseTemplates);
        return responseTemplates;
    }

    private List<TemplateRuleMapDto> getFaQueryRules(List<TemplateRuleMapDto> allTemplates) {
        List<TemplateRuleMapDto> faQueryTemplates = new ArrayList<>();
        for (TemplateRuleMapDto actualTemplate : allTemplates) {
            if (FactType.FA_QUERY.equals(actualTemplate.getTemplateType().getType()) ||
                    FactType.FA_QUERY_PARAMETER.equals(actualTemplate.getTemplateType().getType())) {
                faQueryTemplates.add(actualTemplate);
            }
        }
        allTemplates.removeAll(faQueryTemplates);
        return faQueryTemplates;
    }

    private void refreshRulesValidationMessages(List<TemplateRuleMapDto> templatesAndRules) {
        cacheService.loadCacheForFailureMessages();
        for (TemplateRuleMapDto templatesAndRule : templatesAndRules) {
            for (RuleType ruleType : templatesAndRule.getRules()) {
                EnrichedBRMessage enrichedBRMessage = cacheService.getErrorMessageForBrId(ruleType.getBrId());
                if (enrichedBRMessage != null) {
                    String errorMessageForBrId = enrichedBRMessage.getMessage();
                    if (StringUtils.isNotEmpty(errorMessageForBrId)) {
                        ruleType.setMessage(errorMessageForBrId.replaceAll("\"", "&quot;"));
                        enrichedBRMessage.setTemplateEntityName(templatesAndRule.getTemplateType().getType().name());
                        enrichedBRMessage.setExpression(ruleType.getExpression());
                    }
                }
            }
        }
    }

    public KieContainer getContainerByType(ContainerType containerType) {
        return containers.get(containerType);
    }

    public void checkRulesAreDeployed(int retries) {
        if (!checkRulesAreDeployed()) {
            log.warn("[WARINIG] Rules were reinitialized cause they resulted not initialized!! Retry numer [{}]", retries);
            initialize();
            retries++;
            checkRulesAreDeployed(retries);
        }
    }

    public boolean checkRulesAreDeployed() {
        List<Rule> deployedRules = new ArrayList<>();
        KieContainer container = containers.get(ContainerType.FA_REPORT);
        Collection<KiePackage> kiePackages = container.getKieBase().getKiePackages();
        for (KiePackage kiePackage : kiePackages) {
            deployedRules.addAll(kiePackage.getRules());
        }
        return CollectionUtils.isNotEmpty(deployedRules);
    }


    private List<FactType> getFaReportFactsTypes() {
        return new ArrayList<FactType>() {{
            add(FactType.FA_REPORT_DOCUMENT);
            add(FactType.FLUX_FA_REPORT_MESSAGE);
            add(FactType.VESSEL_TRANSPORT_MEANS);
            add(FactType.STRUCTURED_ADDRESS);
            add(FactType.FISHING_GEAR);
            add(FactType.GEAR_CHARACTERISTIC);
            add(FactType.GEAR_PROBLEM);
            add(FactType.FA_CATCH);
            add(FactType.FISHING_TRIP);
            add(FactType.FLUX_LOCATION);
            add(FactType.FLUX_CHARACTERISTIC);
            add(FactType.VESSEL_STORAGE_CHARACTERISTIC);
            add(FactType.FISHING_ACTIVITY);
            add(FactType.FA_DEPARTURE);
            add(FactType.FA_ENTRY_TO_SEA);
            add(FactType.FA_FISHING_OPERATION);
            add(FactType.FA_JOINT_FISHING_OPERATION);
            add(FactType.FA_RELOCATION);
            add(FactType.FA_DISCARD);
            add(FactType.FA_EXIT_FROM_SEA);
            add(FactType.FA_NOTIFICATION_OF_ARRIVAL);
            add(FactType.FA_ARRIVAL);
            add(FactType.FA_LANDING);
            add(FactType.FA_TRANSHIPMENT);
            add(FactType.FA_NOTIFICATION_OF_TRANSHIPMENT);
        }};
    }

    private List<FactType> getSalesFactsTypes() {
        return new ArrayList<FactType>() {{
            add(FactType.SALES_FLUX_SALES_REPORT_MESSAGE);
            add(FactType.SALES_FLUX_REPORT_DOCUMENT);
            add(FactType.SALES_FLUX_PARTY);
            add(FactType.SALES_REPORT);
            add(FactType.SALES_DOCUMENT);
            add(FactType.SALES_PARTY);
            add(FactType.SALES_EVENT);
            add(FactType.SALES_BATCH);
            add(FactType.SALES_AAP_PRODUCT);
            add(FactType.SALES_AAP_PROCESS);
            add(FactType.SALES_SIZE_DISTRIBUTION);
            add(FactType.SALES_PRICE);
            add(FactType.SALES_FLUX_ORGANIZATION);
            add(FactType.SALES_FISHING_ACTIVITY);
            add(FactType.SALES_DELIMITED_PERIOD);
            add(FactType.SALES_VESSEL_TRANSPORT_MEANS);
            add(FactType.SALES_VESSEL_COUNTRY);
            add(FactType.SALES_CONTACT_PARTY);
            add(FactType.SALES_CONTACT_PERSON);
            add(FactType.SALES_FISHING_TRIP);
            add(FactType.SALES_FLUX_LOCATION);
            add(FactType.SALES_FLUX_GEOGRAPHICAL_COORDINATE);
            add(FactType.SALES_STRUCTURED_ADDRESS);
            add(FactType.SALES_QUERY);
            add(FactType.SALES_FLUX_RESPONSE_DOCUMENT);
            add(FactType.SALES_VALIDATION_RESULT_DOCUMENT);
            add(FactType.SALES_VALIDATION_QUALITY_ANALYSIS);
            add(FactType.SALES_REPORT_WRAPPER);
            add(FactType.SALES_AUCTION_SALE);
            add(FactType.SALES_FLUX_SALES_QUERY_MESSAGE);
            add(FactType.SALES_QUERY_PARAMETER);
            add(FactType.SALES_FLUX_SALES_RESPONSE_MESSAGE);
        }};
    }
}
