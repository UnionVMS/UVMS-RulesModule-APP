/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetCustomRuleListByQueryResponse;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

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