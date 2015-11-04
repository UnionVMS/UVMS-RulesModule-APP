package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ValidationService {
    List<CustomRuleType> getCustomRuleList() throws RulesServiceException;

    // Triggered by rule engine
    void createAlarmReport(String ruleName, RawMovementFact fact) throws RulesServiceException;
}
