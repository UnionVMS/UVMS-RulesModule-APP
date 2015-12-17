package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetCustomRuleListByQueryResponse;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

import javax.ejb.Local;
import javax.jms.JMSException;
import java.util.List;

@Local
public interface ValidationService {
    List<CustomRuleType> getCustomRulesByUser(String userName) throws RulesServiceException, RulesFaultException;

    List<CustomRuleType> getRunnableCustomRules() throws RulesServiceException, RulesFaultException;

    List<SanityRuleType> getSanityRules() throws RulesServiceException, RulesFaultException;

    GetCustomRuleListByQueryResponse getCustomRulesByQuery(CustomRuleQuery query) throws RulesServiceException, RulesFaultException;

    // Triggered by rule engine
    void customRuleTriggered(String ruleName, String ruleGuid, MovementFact fact, String actions);

    // Triggered by rule engine
    void createAlarmReport(String ruleName, RawMovementFact fact) throws RulesServiceException;

    /**
     * @return number of open alarms
     * @throws RulesServiceException if unsuccessful
     */
    long getNumberOfOpenAlarmReports() throws RulesServiceException, RulesFaultException;

    /**
     * @return number of open tickets
     * @throws RulesServiceException if unsuccessful
     */
    long getNumberOfOpenTickets(String userName) throws RulesServiceException, RulesFaultException;
}
