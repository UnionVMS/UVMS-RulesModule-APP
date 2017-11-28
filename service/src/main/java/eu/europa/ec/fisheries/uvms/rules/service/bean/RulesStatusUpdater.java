/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleStatusType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kovian on 23/11/2017.
 */

@Stateless
@LocalBean
@Slf4j
public class RulesStatusUpdater {

    @EJB
    private RulesDomainModel rulesDb;


    public void updateRulesStatus(List<String> failedBrIds) {
        try {
            log.info("[START] Updating Rules Statuses...");
            rulesDb.updateFailedRules(failedBrIds);
            rulesDb.updateRuleStatus(failedBrIds.isEmpty() ? RuleStatusType.SUCCESSFUL : RuleStatusType.FAILED);
            log.info("[END] Finished Updating Rules Statuses..");
        } catch (RulesModelException e) {
            throw new IllegalStateException(e);
        }
    }
}
