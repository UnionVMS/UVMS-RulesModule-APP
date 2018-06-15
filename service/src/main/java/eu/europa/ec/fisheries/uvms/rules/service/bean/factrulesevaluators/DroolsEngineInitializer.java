package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.EnrichedBRMessage;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
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
 *
 * This class serves to intitialize the Fact Evaluators, check if the engine is up and running and reinitialize the engine if needed.
 *
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
    private ResponseFactRuleEvaluator responseRuleEvaluator;

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
            List<TemplateRuleMapDto> faResponseTemplatesAndRules = getResponseRules(allTemplates);
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
            KieContainer salesContainer = salesRuleEvaluator.initializeRules(faResponseTemplatesAndRules);

            containers = new HashMap<>();
            containers.put(ContainerType.Fa_REPORT, faReportContainer);
            containers.put(ContainerType.FA_RESPONSE, faRespContainer);
            containers.put(ContainerType.FA_QUERY, faQueryContainer);
            containers.put(ContainerType.SALES, salesContainer);

            // To make sure that we have deployed all the templates!
            assert allTemplates.isEmpty();

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
        for (TemplateRuleMapDto templatesAndRule : templatesAndRules) {
            if (FactType.SALES_FLUX_SALES_RESPONSE_MESSAGE.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_QUERY_PARAMETER.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FLUX_SALES_QUERY_MESSAGE.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_AUCTION_SALE.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_REPORT_WRAPPER.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_VALIDATION_QUALITY_ANALYSIS.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_VALIDATION_RESULT_DOCUMENT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FLUX_RESPONSE_DOCUMENT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_QUERY.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_STRUCTURED_ADDRESS.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FLUX_GEOGRAPHICAL_COORDINATE.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FLUX_LOCATION.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FISHING_TRIP.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_CONTACT_PERSON.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_CONTACT_PARTY.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_VESSEL_COUNTRY.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_VESSEL_TRANSPORT_MEANS.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_DELIMITED_PERIOD.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FISHING_ACTIVITY.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FLUX_ORGANIZATION.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_PRICE.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_SIZE_DISTRIBUTION.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_AAP_PROCESS.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_AAP_PRODUCT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_BATCH.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_EVENT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_PARTY.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_DOCUMENT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_REPORT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FLUX_PARTY.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FLUX_REPORT_DOCUMENT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.SALES_FLUX_SALES_REPORT_MESSAGE.equals(templatesAndRule.getTemplateType().getType())) {
                responseTemplates.add(templatesAndRule);
            }
        }
        templatesAndRules.removeAll(responseTemplates);
        return responseTemplates;
    }

    private List<TemplateRuleMapDto> getFaMessageRules(List<TemplateRuleMapDto> templatesAndRules) {
        List<TemplateRuleMapDto> faTemplates = new ArrayList<>();
        for (TemplateRuleMapDto templatesAndRule : templatesAndRules) {
            if (FactType.FA_REPORT_DOCUMENT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_VALIDATION_QUALITY_ANALYSIS.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FLUX_FA_REPORT_MESSAGE.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.VESSEL_TRANSPORT_MEANS.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.STRUCTURED_ADDRESS.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FISHING_GEAR.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.GEAR_CHARACTERISTIC.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.GEAR_PROBLEM.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_CATCH.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FISHING_TRIP.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FLUX_LOCATION.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FLUX_CHARACTERISTIC.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.VESSEL_STORAGE_CHARACTERISTIC.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_DEPARTURE.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_ENTRY_TO_SEA.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_FISHING_OPERATION.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_JOINT_FISHING_OPERATION.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_RELOCATION.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_DISCARD.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_EXIT_FROM_SEA.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_NOTIFICATION_OF_ARRIVAL.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_ARRIVAL.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_LANDING.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_TRANSHIPMENT.equals(templatesAndRule.getTemplateType().getType()) ||
                    FactType.FA_NOTIFICATION_OF_TRANSHIPMENT.equals(templatesAndRule.getTemplateType().getType())) {
                faTemplates.add(templatesAndRule);
            }
        }
        templatesAndRules.removeAll(faTemplates);
        return faTemplates;
    }

    private List<TemplateRuleMapDto> getResponseRules(List<TemplateRuleMapDto> templatesAndRules) {
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
        KieContainer container = containers.get(ContainerType.Fa_REPORT);
        Collection<KiePackage> kiePackages = container.getKieBase().getKiePackages();
        for (KiePackage kiePackage : kiePackages) {
            deployedRules.addAll(kiePackage.getRules());
        }
        return CollectionUtils.isNotEmpty(deployedRules);
    }
}
