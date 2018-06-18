package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators;

import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.SalesRulesService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.ExchangeRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.ContainerType;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.DroolsEngineInitializer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.AbstractFactEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementsRulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.kie.api.runtime.KieContainer;

import javax.ejb.*;
import java.util.*;

@Slf4j
@Stateless
@LocalBean
public class SalesFactRuleEvaluator extends AbstractFactEvaluator {

    @EJB
    private SalesRulesService salesRulesService;

    @EJB
    private MDRCacheRuleService mdrCacheRuleService;

    @EJB
    private ExchangeRuleService exchangeRuleService;

    @EJB
    private DroolsEngineInitializer initializer;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public KieContainer initializeRules(Collection<TemplateRuleMapDto> templates) {
        return initializeRules(templates, ContainerType.SALES.getContainerName(), ContainerType.SALES.getPackageName());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void validateFacts(Collection<AbstractFact> facts) throws RulesValidationException {
        if (CollectionUtils.isEmpty(facts)) {
            throw new RulesValidationException("No facts available for validation");
        }
        setExceptionsList(new ArrayList<AbstractFact>());
        validateFacts(facts, initializer.getContainerByType(ContainerType.SALES));
        facts.addAll(getExceptionsList());
    }

    @Override
    Map<String, Object> getGlobalsMap() {
        return new HashMap<String, Object>() {{
            put("mdrService", mdrCacheRuleService);
            put("exchangeService", exchangeRuleService);
            put("salesService", salesRulesService);
        }};
    }

}
