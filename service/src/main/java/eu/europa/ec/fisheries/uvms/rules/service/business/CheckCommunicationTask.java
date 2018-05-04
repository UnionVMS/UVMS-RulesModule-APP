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
package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckCommunicationTask implements Runnable {
    private static final long TWO_HOURS_IN_MILLISECONDS = 7200000;

    private final static Logger LOG = LoggerFactory.getLogger(CheckCommunicationTask.class);

    private RulesService rulesService;

    CheckCommunicationTask(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    public void run() {
        LOG.debug("RulesTimerBean tick");
        // Get all previous reports from DB
        List<PreviousReportType> previousReports = new ArrayList<>();
        try {
            previousReports = rulesService.getPreviousMovementReports();
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.warn("[WARN] No previous movement report found");
        }
        try {
            // Map to fact, adding 2h to deadline
            for (PreviousReportType previousReport : previousReports) {
                PreviousReportFact fact = new PreviousReportFact();
                fact.setMovementGuid(previousReport.getMovementGuid());
                fact.setAssetGuid(previousReport.getAssetGuid());

                Date positionTime = previousReport.getPositionTime();
                long time = positionTime.getTime() + TWO_HOURS_IN_MILLISECONDS;
                Date threshold = new Date(time+ TWO_HOURS_IN_MILLISECONDS);
                fact.setDeadline(threshold);

                if (fact.getDeadline().getTime() <= new Date().getTime()) {
                    LOG.info("\t[INFO] ==> Executing RULE '" + ServiceConstants.ASSET_NOT_SENDING_RULE + "', deadline:" + fact.getDeadline() + ", assetGuid:" + fact.getAssetGuid());
                    String ruleName = ServiceConstants.ASSET_NOT_SENDING_RULE;
                    rulesService.timerRuleTriggered(ruleName, fact);
                }
            }
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when running checkCommunication timer ] {}", e.getMessage());
        }
    }
}