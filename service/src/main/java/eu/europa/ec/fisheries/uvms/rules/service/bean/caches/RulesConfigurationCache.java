/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean.caches;


import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConfigSettingsBean;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.RulesResponseConsumerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesProducerBean;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kovian on 31/05/2017.
 */
@Singleton
@Startup
@Slf4j
public class RulesConfigurationCache extends AbstractConfigSettingsBean {

    @EJB
    private RulesResponseConsumerBean consumer;

    @EJB
    private RulesProducerBean producer;

    @Override
    protected RulesResponseConsumerBean getConsumer() {
        return consumer;
    }

    @Override
    protected AbstractProducer getProducer() {
        return producer;
    }

    @Override
    protected String getModuleName() {
        return "rules";
    }

}