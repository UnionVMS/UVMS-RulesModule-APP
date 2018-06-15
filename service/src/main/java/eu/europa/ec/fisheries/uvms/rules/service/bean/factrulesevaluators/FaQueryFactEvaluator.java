package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators;

import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.ExchangeRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementsRulesValidator;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.kie.api.runtime.KieContainer;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Stateless
@LocalBean
public class FaQueryFactEvaluator extends AbstractFactEvaluator {

    @EJB
    private MDRCacheRuleService mdrCacheRuleService;

    @EJB
    private MovementsRulesValidator movementRulesValidator;

    @EJB
    private ExchangeRuleService exchangeRuleService;

    @EJB
    private DroolsEngineInitializer initializer;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public KieContainer initializeRules(Collection<TemplateRuleMapDto> templates) {
        return initializeRules(templates, "fa-query", "ec.europa.eu.faQuery");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void validateFacts(Collection<AbstractFact> facts) throws RulesValidationException {
        if (CollectionUtils.isEmpty(facts)) {
            throw new RulesValidationException("No facts available for validation");
        }
        setExceptionsList(new ArrayList<AbstractFact>());
        validateFacts(facts, initializer.getContainerByType(ContainerType.FA_QUERY));
        facts.addAll(getExceptionsList());
    }

    @Override
    Map<String, Object> getGlobalsMap() {
        return  new HashMap<String, Object>() {{
            put("mdrService", mdrCacheRuleService);
            put("exchangeService", exchangeRuleService);
        }};
    }

}