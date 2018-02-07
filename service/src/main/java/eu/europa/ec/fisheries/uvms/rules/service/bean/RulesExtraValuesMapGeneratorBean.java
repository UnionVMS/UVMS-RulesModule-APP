/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;


import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ACTIVITY_NON_UNIQUE_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ACTIVITY_WITH_TRIP_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET_LIST;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FISHING_GEAR_TYPE_CHARACTERISTICS;

import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

@Stateless
@LocalBean
@Slf4j
public class RulesExtraValuesMapGeneratorBean {

    private static final String INFO_GOING_TO_GENERATE_EXRAVALUE_MAP_FOR = "[INFO] Going to generate exravalue map for : [";
    private static final String BUSINESS_OBJECCT_TYPE = "] BusinessObjecct type..";
    private static final String END_IT_TOOK = "[END] It took [";
    private static final String SECONDS_TO_GENERATE_THE_MAP = "] seconds to generate the map...";

    @EJB
    private RuleAssetsBean ruleAssetsBean;

    @EJB
    private RulesActivityServiceBean activityService;

    @EJB
    private RulesFishingGearBean rulesFishingGearBean;

    private StopWatch stopWatch = new StopWatch();

    public Map<ExtraValueType, Object> generateExtraValueMap(BusinessObjectType businessObjectType, Object businessObject) {
        log.info(INFO_GOING_TO_GENERATE_EXRAVALUE_MAP_FOR + businessObjectType + BUSINESS_OBJECCT_TYPE);
        stopWatch.reset();
        stopWatch.start();
        Map<ExtraValueType, Object> map = new HashMap<>();
        switch (businessObjectType) {
            case RECEIVING_FA_REPORT_MSG:
                map.put(ACTIVITY_NON_UNIQUE_IDS, activityService.getNonUniqueIdsList(businessObject));
                map.put(ACTIVITY_WITH_TRIP_IDS, activityService.getFishingActivitiesForTrips(businessObject));
                map.put(ASSET_LIST, ruleAssetsBean.getAssetList(businessObject));
                map.put(FISHING_GEAR_TYPE_CHARACTERISTICS, rulesFishingGearBean.getAllFishingGearTypeCharacteristics());
                break;
            case SENDING_FA_REPORT_MSG:
                map.put(ACTIVITY_WITH_TRIP_IDS, activityService.getFishingActivitiesForTrips(businessObject));
                map.put(ASSET_LIST, ruleAssetsBean.getAssetList(businessObject));
                map.put(FISHING_GEAR_TYPE_CHARACTERISTICS, rulesFishingGearBean.getAllFishingGearTypeCharacteristics());
                break;
            case RECEIVING_FA_QUERY_MSG:
                log.info("[INFO] No need for ExtraValuesMap for this type of Message!");
                break;
            case SENDING_FA_QUERY_MSG:
                log.info("[INFO] No need for ExtraValuesMap for this type of Message!");
                break;
        }
        stopWatch.stop();
        log.info(END_IT_TOOK + stopWatch.getTime(TimeUnit.SECONDS) + SECONDS_TO_GENERATE_THE_MAP);
        return map;

    }

}
