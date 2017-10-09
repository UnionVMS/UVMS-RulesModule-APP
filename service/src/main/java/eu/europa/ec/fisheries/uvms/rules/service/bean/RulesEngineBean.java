/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityTableType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdTypeWithFlagState;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 * @author Andi Kovi
 */
@Stateless
@Slf4j
@LocalBean
public class RulesEngineBean {

    @EJB
    private MDRCacheServiceBean mdrCacheServiceBean;

    @EJB
    private TemplateEngine templateEngine;

    @EJB
    private RuleAssetsBean ruleAssetsBean;

    @EJB
    private RulesActivityServiceBean activityService;

    @EJB
    private RulesDao rulesDao;

    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject, Map<ExtraValueType, Object> map) throws RulesValidationException {
        List<AbstractFact> facts = new ArrayList<>();
        AbstractGenerator generator = BusinessObjectFactory.getBusinessObjFactGenerator(businessObjectType);
        generator.setBusinessObjectMessage(businessObject);
        mdrCacheServiceBean.loadMDRCache();
        generator.setExtraValueMap(map);
        generator.setAdditionalValidationObject();
        facts.addAll(generator.generateAllFacts());
        templateEngine.evaluateFacts(facts);
        return facts;
    }

    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject) throws RulesValidationException {
        return evaluate(businessObjectType, businessObject, Collections.<ExtraValueType, Object>emptyMap());
    }

    public Map<ExtraValueType, Object> generateExtraValueMap(BusinessObjectType businessObjectType, Object businessObject) {

        Map<ExtraValueType, Object> map = new HashMap<>();

        if (FLUX_ACTIVITY_REQUEST_MSG.equals(businessObjectType)) {
            Map<ActivityTableType, List<IdType>> nonUniqueIdsList = activityService.getNonUniqueIdsList(businessObject);
            map.put(ACTIVITY_NON_UNIQUE_IDS, nonUniqueIdsList);
            Map<String, List<FishingActivityWithIdentifiers>> fishingActivitiesForTrips = activityService.getFishingActivitiesForTrips(businessObject);
            map.put(ACTIVITY_WITH_TRIP_IDS, fishingActivitiesForTrips);
            List<IdTypeWithFlagState> assetList = ruleAssetsBean.getAssetList(businessObject);
            map.put(ASSET_LIST, assetList);

            List<FishingGearTypeCharacteristic> fishingGearTypeCharacteristics = null;
            try {
                fishingGearTypeCharacteristics = rulesDao.getAllFishingGearTypeCharacteristics();
            } catch (DaoException e) {
                log.error("Error retrieving FishingGearTypeCharacteristics!", e);
            }

            map.put(FISHING_GEAR_TYPE_CHARACTERISTICS, fishingGearTypeCharacteristics);
        }

        return map;
    }

}
