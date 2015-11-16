package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

import javax.ejb.Local;
import javax.jms.JMSException;
import java.util.List;

@Local
public interface ValidationService {
    List<CustomRuleType> getCustomRuleList() throws RulesServiceException, RulesFaultException;

    // Triggered by rule engine
    void customRuleTriggered(String ruleName, String ruleGuid, MovementFact fact, String actions);

    // Triggered by rule engine
    void createAlarmReport(String ruleName, RawMovementFact fact) throws RulesServiceException;

}
