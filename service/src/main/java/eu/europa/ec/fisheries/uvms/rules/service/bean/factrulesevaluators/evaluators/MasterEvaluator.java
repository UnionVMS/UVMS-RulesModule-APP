package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.*;
import java.util.List;

@Stateless
@LocalBean
@Slf4j
public class MasterEvaluator {

    @EJB
    private FaReportFactRuleEvaluator faReportRuleEvaluator;

    @EJB
    private FaResponseFactRuleEvaluator responseRuleEvaluator;

    @EJB
    private SalesFactRuleEvaluator salesRuleEvaluator;

    @EJB
    private FaQueryFactEvaluator faQueryEvaluator;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void evaluateFacts(List<AbstractFact> facts, BusinessObjectType businessObjectType) throws RulesValidationException {
        //droolsEngineInitializer.checkRulesAreDeployed(0);
        switch (businessObjectType) {
            case RECEIVING_FA_REPORT_MSG:
            case SENDING_FA_REPORT_MSG:
                faReportRuleEvaluator.validateFacts(facts);
                break;
            case SENDING_FA_RESPONSE_MSG:
            case RECEIVING_FA_RESPONSE_MSG:
                responseRuleEvaluator.validateFacts(facts);
                break;
            case RECEIVING_FA_QUERY_MSG:
            case SENDING_FA_QUERY_MSG:
                faQueryEvaluator.validateFacts(facts);
                break;
            case FLUX_SALES_REPORT_MSG:
            case FLUX_SALES_RESPONSE_MSG:
            case FLUX_SALES_QUERY_MSG:
                salesRuleEvaluator.validateFacts(facts);
                break;
            default:
                log.error("[ERROR-FATAL] Couldn't find an Evaluator for this type of message!");
        }
    }
}
