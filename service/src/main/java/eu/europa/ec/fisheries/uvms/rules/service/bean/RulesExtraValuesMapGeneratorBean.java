/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;


import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

@Stateless
@LocalBean
@Slf4j
@Deprecated
public class RulesExtraValuesMapGeneratorBean {

    private static final String INFO_GOING_TO_GENERATE_EXRAVALUE_MAP_FOR = "[INFO] Going to generate exravalue map for : [";
    private static final String BUSINESS_OBJECCT_TYPE = "] BusinessObjecct type..";
    private static final String END_IT_TOOK = "[END] It took [";
    private static final String SECONDS_TO_GENERATE_THE_MAP = "] seconds to generate the map...";

    @EJB
    private RuleAssetsBean ruleAssetsBean;

    @EJB
    private RulesActivityServiceBean activityService;


    private StopWatch stopWatch = new StopWatch();

    @Deprecated
    public Map<ExtraValueType, Object> generateExtraValueMap(BusinessObjectType businessObjectType, Object businessObject, final String senderReceiver) {
        log.info(INFO_GOING_TO_GENERATE_EXRAVALUE_MAP_FOR + businessObjectType + BUSINESS_OBJECCT_TYPE);
        stopWatch.reset();
        stopWatch.start();
        Map<ExtraValueType, Object> map = new HashMap<ExtraValueType, Object>(){{put(SENDER_RECEIVER, senderReceiver);}};

        switch (businessObjectType) {
            case RECEIVING_FA_REPORT_MSG:
              //  map.put(ACTIVITY_NON_UNIQUE_IDS, activityService.getNonUniqueIdsList(businessObject));
              //  map.put(ACTIVITY_WITH_TRIP_IDS, activityService.getFishingActivitiesForTrips(businessObject));
              //  map.put(ASSET_LIST, ruleAssetsBean.getAssetList(businessObject));
                break;
            case SENDING_FA_REPORT_MSG:
               // map.put(ACTIVITY_WITH_TRIP_IDS, activityService.getFishingActivitiesForTrips(businessObject));
                //map.put(ASSET_LIST, ruleAssetsBean.getAssetList(businessObject));
                break;
            case RECEIVING_FA_QUERY_MSG:
                log.debug("[INFO] No need for ExtraValuesMap for this type of Message!");
                break;
            case SENDING_FA_QUERY_MSG:
                log.debug("[INFO] No need for ExtraValuesMap for this type of Message!");
                break;
            case RECEIVING_FA_RESPONSE_MSG:
                log.debug("[INFO] No need for ExtraValuesMap for this type of Message!");
                break;
        }
        stopWatch.stop();
        log.debug(END_IT_TOOK + stopWatch.getTime(TimeUnit.SECONDS) + SECONDS_TO_GENERATE_THE_MAP);
        return map;

    }

}
