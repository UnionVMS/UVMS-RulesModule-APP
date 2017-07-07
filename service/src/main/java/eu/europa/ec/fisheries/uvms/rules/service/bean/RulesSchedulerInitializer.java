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
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.*;

import static javax.ejb.ConcurrencyManagementType.BEAN;

/**
 * Created by kovian, gregrinaldi on 30/05/2017.
 */
@Slf4j
@Singleton
@Startup
@ConcurrencyManagement(BEAN)
@DependsOn(value = {"RulesSchedulerServiceBean"})
public class RulesSchedulerInitializer {

    private static final String DEFAULT_SCHED_CONFIGURATION = "0 1 20 * *";

    @EJB
    RulesSchedulerService rulesSchedulerService;

    /**
     * Method for start up Jobs of Rules Module (Deploy phase)
     *
     *  1. Setting up the scheduler timer for Rules DROOLS re-initialization - at start up -.
     *
     */
    @PostConstruct
    public void startUpMdrInitializationProcess() {
        String schedConfigCronExpr = rulesSchedulerService.getActualSchedulerConfiguration();
        if(StringUtils.isNotEmpty(schedConfigCronExpr)){
            try {
                rulesSchedulerService.setUpScheduler(schedConfigCronExpr);
            } catch (IllegalArgumentException ex){
                log.error("Couldn't setUp scheduler for rules re-initialization because the cron expression from Config module is wrong! Going to setUp with the default scheduler expression : " + DEFAULT_SCHED_CONFIGURATION);
                rulesSchedulerService.setUpScheduler(DEFAULT_SCHED_CONFIGURATION);
            }
        } else {
            log.warn("No configuration found for Rules. Check the config module! The default schedule will be considered : " + DEFAULT_SCHED_CONFIGURATION);
            rulesSchedulerService.setUpScheduler(DEFAULT_SCHED_CONFIGURATION);
        }
    }
}
