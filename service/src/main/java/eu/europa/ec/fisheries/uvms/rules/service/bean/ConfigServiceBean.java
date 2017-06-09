/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.config.types.v1.SettingType;
import eu.europa.ec.fisheries.uvms.config.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

/**
 * Created by kovian, gregrinaldi on 30/05/2017.
 */
@Stateless
@LocalBean
@Slf4j
public class ConfigServiceBean {

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;

    public String getConfiguration(String configKey){
        try {
            if(StringUtils.isEmpty(configKey)){
                return null;
            }
            String request = ModuleRequestMapper.toPullSettingsRequest("rules");
            String jmsMessageID = producer.sendDataSourceMessage(request, DataSourceQueue.CONFIG);
            TextMessage message = consumer.getMessage(jmsMessageID, TextMessage.class);
            List<SettingType> settingTypeList = ModuleResponseMapper.getSettingsFromPullSettingsResponse(message);
            for(SettingType setting : settingTypeList){
                if(configKey.equals(setting.getKey())){
                    return setting.getValue();
                }
            }
        } catch (MessageException | JMSException | ModelMapperException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}