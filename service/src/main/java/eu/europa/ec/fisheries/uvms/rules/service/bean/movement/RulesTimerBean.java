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
package eu.europa.ec.fisheries.uvms.rules.service.bean.movement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.concurrent.ScheduledFuture;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
@Singleton
@DependsOn({"MovementsRulesValidator"})
public class RulesTimerBean {

    private final static Logger LOG = LoggerFactory.getLogger(RulesTimerBean.class);

    @EJB
    private RulesMovementProcessorBean rulesService;

    @EJB
    private ValidationService validationService;

    @EJB
    private MovementsRulesValidator rulesValidator;

    private ScheduledFuture comm;

    private ScheduledFuture changes;

    @PostConstruct
    public void postConstruct() {
        //LOG.debug("RulesTimerBean init");
        //ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        //CheckCommunicationTask checkCommunicationTask = new CheckCommunicationTask(rulesService);
        //comm = executorService.scheduleWithFixedDelay(checkCommunicationTask, 10, 10, TimeUnit.MINUTES);
        //CheckRulesChangesTask checkRulesChangesTask = new CheckRulesChangesTask(validationService, rulesValidator, rulesService);
        //checkRulesChangesTask.run();
        //changes = executorService.scheduleWithFixedDelay(checkRulesChangesTask, 10, 10, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void preDestroy() {
        if (comm != null) {
            comm.cancel(true);
        }
        if (changes != null) {
            changes.cancel(true);
        }
    }

}