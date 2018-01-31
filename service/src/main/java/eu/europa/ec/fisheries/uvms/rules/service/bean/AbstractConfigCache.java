/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.europa.ec.fisheries.schema.config.types.v1.SettingType;
import eu.europa.ec.fisheries.uvms.config.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *  Created by kovian on 31/05/2017.
 *
 *  This abstract class will serve as a base to be extended from any module that needs to get settings from
 *  Config module (global or related to the module).
 *
 *  The following are a minimal set of methods that need to be overriten :
 *
 *      abstract RulesResponseConsumer getConsumer(); The consumer that will consume the response messages in the related modules Queue.
 *      abstract RulesMessageProducer getProducer();  The rulesProducer which will send the request messages to Config module.
 *      abstract String getModuleName(); The module name which will be used as a parameter to get the settings.
 *
 */
@Slf4j
public abstract class AbstractConfigCache {

    private LoadingCache<String, Map<String, String>> cache;


    /**
     * Initializes the Config Settings cache.
     *
     */
    protected void initializeCache() {
        if (cache == null) {
            cache = CacheBuilder.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .build(new CacheLoader<String, Map<String, String>>() {
                               @Override
                               public Map<String, String> load(String moduleName) throws Exception {
                                   return getAllModuleConfigurations(moduleName);
                               }
                           }
                    );
        }
    }

    /**
     * Get a single Setting related to this module from the cache.
     *
     * @param configKey
     * @return String (single Setting)
     */
    public String getSingleConfig(String configKey) {
        String result = null;
        if (configKey != null) {
            Map<String, String> rules = getAllSettingsForModule();
            result = rules.get(configKey);
        }
        return result;
    }

    /**
     * Calls Config module and gets all the settings related to the module with name = moduleName.
     *
     * @param moduleName
     * @return Map<String, String> the object (Settings map) to cache.
     */
    private Map<String, String> getAllModuleConfigurations(String moduleName){
        Map<String, String> settingsMap = new HashMap<>();
        if(StringUtils.isNotEmpty(moduleName)) {
            try {
                List<SettingType> settingTypeList = getSettingTypes(moduleName);
                if(CollectionUtils.isNotEmpty(settingTypeList)){
                    for (SettingType setting : settingTypeList) {
                        settingsMap.put(setting.getKey(), setting.getValue());
                    }
                }
            } catch (MessageException | JMSException | ModelMapperException e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("RulesConfigurationCache has just finished refreshing the Rules Configuration cache.");
        return settingsMap;
    }

    /**
     * Returns all the settings related to this module - getModuleName() -.
     *
     * @return
     */
    public Map<String, String> getAllSettingsForModule() {
        Map<String, String> result = null;
        String moduleName = getModuleName();
        if (moduleName != null) {
            result = cache.getUnchecked(moduleName);
        }
        return result != null ? result : new HashMap<String, String>();
    }

    /**
     *  Calls the Config module to get all the settings for the related Module with name = moduleName.
     *  Along with the module setting will also come the global settings, as per Config implementation of this!
     *
     * @param moduleName
     * @return List<SettingType>, the setting of the module we requested
     * @throws MessageException
     * @throws ModelMapperException
     * @throws JMSException
     */
    private List<SettingType> getSettingTypes(String moduleName) throws MessageException, ModelMapperException, JMSException {
        String request = ModuleRequestMapper.toPullSettingsRequest(moduleName);
        String jmsMessageID = getProducer().sendDataSourceMessage(request, DataSourceQueue.CONFIG);
        TextMessage message = getConsumer().getMessage(jmsMessageID, TextMessage.class);
        return ModuleResponseMapper.getSettingsFromPullSettingsResponse(message);
    }

    /**
     *  TODO : In order for this class to be in commons and be extended by all the other modules that need it,
     *  we need to implement a common AbstractConsumer and AbstractProducer, or extend the existing ones changing the sending and receiving
     *  part.. Example : In the existing AbstractProducer sendDataSourceMessage(...){...} doesn't exist. And, this method is used here..
     */
    protected abstract RulesResponseConsumer getConsumer();

    protected abstract RulesMessageProducer getProducer();

    protected abstract String getModuleName();

}
