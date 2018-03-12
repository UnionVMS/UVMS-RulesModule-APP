/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.config;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.europa.ec.fisheries.schema.config.types.v1.SettingType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JMSUtils;
import eu.europa.ec.fisheries.uvms.config.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleResponseMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kovian on 28/02/2018.
 * <p>
 * <p>
 * This abstract class will serve as a base to be extended from any module that needs to get settings from
 * Config module (it retrieves settings that are : global and related to the module).
 * <p>
 * The following are a minimal set of methods that need to be overriten :
 * <p>
 * abstract AbstractConsumer getConsumer(); The consumer that will consume the response messages in the related modules Queue.
 * abstract AbstractProducer getProducer();  The producer which will send the request messages to >>Config<< module.
 * abstract String getModuleName(); The module name which will be used as a parameter to get the settings.
 * <p>
 * <p>
 * Check RulesConfigurationCache class in rules module as an example on how to use this abstract class.
 */
public abstract class AbstractConfigSettingsBean extends AbstractProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumer.class);

    private LoadingCache<String, Map<String, String>> cache;

    private Queue configQueue;

    /**
     * Initializes the Config Settings cache.
     * To be called in the extending class in a @PostConstruct block to load the settings for this module.
     */
    protected AbstractConfigSettingsBean() {
        configQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_CONFIG);
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
            LOGGER.info("[INFO] Finished loading settings for module : [" + getModuleName() + "].");
        }
    }

    /**
     * Get a single Setting related to this module from the cache.
     *
     * @param configKey
     * @return String (single Setting)
     */
    public String getSingleConfig(String configKey) {
        if (configKey == null) {
            throw new IllegalArgumentException("[ERROR] configKey cannot be null!");
        }
        Map<String, String> moduleConfigsMap = getAllSettingsForModule();
        return moduleConfigsMap.get(configKey);
    }

    /**
     * Calls Config module and gets all the settings related to the module with name = moduleName.
     *
     * @param moduleName
     * @return Map<String               ,                               String> the object (Settings map) to cache.
     */
    private Map<String, String> getAllModuleConfigurations(String moduleName) {
        Map<String, String> settingsMap = new HashMap<>();
        if (StringUtils.isNotEmpty(moduleName)) {
            try {
                List<SettingType> settingTypeList = getSettingTypes(moduleName);
                if (CollectionUtils.isNotEmpty(settingTypeList)) {
                    for (SettingType setting : settingTypeList) {
                        settingsMap.put(setting.getKey(), setting.getValue());
                    }
                }
            } catch (MessageException e) {
                LOGGER.error("[ERROR] Error while trying to fetch settings for module [" + getModuleName() + "]. {}", e);
            }
        }
        LOGGER.info("ConfigSettingsBean has just finished refreshing the " + getModuleName() + " Configuration cache.");
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
     * Calls the Config module to get all the settings for the related Module with name = moduleName.
     * Along with the module setting will also come the global settings, as per Config implementation of this!
     *
     * @param moduleName
     * @return List<SettingType>, the setting of the module we requested
     * @throws MessageException
     * @throws ModelMapperException
     * @throws JMSException
     */
    private List<SettingType> getSettingTypes(String moduleName) throws MessageException {
        try {
            String jmsMessageID = this.sendMessageToSpecificQueue(ModuleRequestMapper.toPullSettingsRequest(moduleName), getConfigQueue(), getConsumer().getDestination());
            TextMessage message = getConsumer().getMessage(jmsMessageID, TextMessage.class);
            return ModuleResponseMapper.getSettingsFromPullSettingsResponse(message);
        } catch (JMSException | ModelMapperException e) {
            throw new MessageException(e);
        }
    }

    protected abstract AbstractConsumer getConsumer();

    protected abstract String getModuleName();

    private Destination getConfigQueue() {
        return configQueue;
    }

    @Override
    /**
     * No need for destination for the type of usage in need here.
     * We just need the "sendMessageToSpecificQueue(...)" functionality of AbstractProducer.
     */
    public String getDestinationName() {
        return StringUtils.EMPTY;
    }

}
