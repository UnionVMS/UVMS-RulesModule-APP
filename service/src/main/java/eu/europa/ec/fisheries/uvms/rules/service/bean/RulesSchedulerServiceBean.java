/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.rules.service.RulesSchedulerService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.transaction.Transactional;
import java.util.Collection;

/**
 * @author Created by kovian, gregrinaldi on 30/05/2017
 *         <p>
 *         EJB that provides the MDR Synchronization Functionality.
 *         Methods for handeling the automatic job scheduler configuration.
 */
@Slf4j
@Stateless
@Transactional
public class RulesSchedulerServiceBean implements RulesSchedulerService {

    private static final String RULES_SYNCHRONIZATION_TIMER = "RulesSynchronizationTimer";
    private static final TimerConfig TIMER_CONFIG = new TimerConfig(RULES_SYNCHRONIZATION_TIMER, false);
    private static final String RULES_SCHEDULER_CONFIG_KEY = "RULES_SCHEDULER_CONFIG";

    @EJB
    private RulesConfigurationCache rulesConfigCache;

    @EJB
    private TemplateEngine templateEngine;

    @Resource
    private TimerService timerServ;


    /**
     * Method that will be called when a timer has been set for this EJB.
     */
    @Timeout
    public void timeOut() {
        log.info("\n\n\t---> Reinitializing Rules DROOLS engine as scheduled... ("+timerServ.getAllTimers().iterator().next().getSchedule().toString()+")\n");
        templateEngine.reInitialize();
    }


    /**
     * Gets the actual Rules Synchronization Configuration (from config);
     *
     * @return mdrSynch;
     */
    @Override
    public String getActualSchedulerConfiguration() {
        return rulesConfigCache.getSingleConfig(RULES_SCHEDULER_CONFIG_KEY);
    }

    /**
     * Given the schedulerExpressionStr creates a new timer for this bean.
     *
     * @param schedulerExpressionStr
     */
    @Override
    public void setUpScheduler(String schedulerExpressionStr) throws IllegalArgumentException {
        try {
            // Parse the Cron-Job expression;
            ScheduleExpression expression = parseExpression(schedulerExpressionStr);
            // Firstly, we need to cancel the current timer, if already exists one;
            cancelPreviousTimer();
            // Set up the new timer for this EJB;
            timerServ.createCalendarTimer(expression, TIMER_CONFIG);
        } catch (IllegalArgumentException ex) {
            log.warn("Error creating new scheduled synchronization timer!", ex);
            throw ex;
        }
        log.info("New timer scheduler created successfully : " + schedulerExpressionStr);
    }

    /**
     * Cancels the previous set up of the timer for this bean.
     */
    private void cancelPreviousTimer() {
        Collection<Timer> allTimers = timerServ.getTimers();
        for (Timer currentTimer : allTimers) {
            if (TIMER_CONFIG.getInfo().equals(currentTimer.getInfo())) {
                currentTimer.cancel();
                log.info("Current Rules scheduler timer cancelled.");
                break;
            }
        }
    }

    /**
     * Creates a ScheduleExpression object with the given schedulerExpressionStr String expression.
     *
     * @param schedulerExpressionStr
     * @return
     */
    private ScheduleExpression parseExpression(String schedulerExpressionStr) {
        ScheduleExpression expression = new ScheduleExpression();
        String[] args = schedulerExpressionStr.split("\\s");
        if (args.length != 5) {
            throw new IllegalArgumentException("Invalid scheduler expression: " + schedulerExpressionStr);
        }
        return expression.minute(args[0]).hour(args[1]).dayOfMonth(args[2]).month(args[3]).year(args[4]);
    }
}
