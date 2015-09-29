package eu.europa.ec.fisheries.uvms.rules.service;

import java.util.List;

import javax.ejb.Local;

import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.service.business.PositionFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Local
public interface RulesService {

    /**
     * Creates a new custom rule
     *
     * @param customRule
     *            the rule to be added
     * @return
     * @throws RulesServiceException
     */
    public CustomRuleType createCustomRule(CustomRuleType customRule) throws RulesServiceException;

    /**
     * Lists (all) custom rules
     *
     * @return
     * @throws RulesServiceException
     */
    public List<CustomRuleType> getCustomRuleList() throws RulesServiceException;

    /**
     * Get an object by id
     *
     * @param id
     * @return
     * @throws RulesServiceException
     */
    public CustomRuleType getById(Long id) throws RulesServiceException;

    /**
     * Update an object
     *
     * @param data
     * @throws RulesServiceException
     */
    public CustomRuleType update(CustomRuleType customRuleType) throws RulesServiceException;

    /**
     * Creates an error report
     *
     * @param comment
     *            note on the error occured
     * @param guid
     *            the offending guid
     * @throws RulesServiceException
     */
    public void createErrorReport(String comment, String guid) throws RulesServiceException;

    /**
     * Entry point of action performed as a result of a custom rule triggered
     *
     * @param f
     *            the fact that triggered the rule
     * @param action
     *            the action(s) to be performed
     */
    public void customRuleTriggered(PositionFact f, String action);

}
