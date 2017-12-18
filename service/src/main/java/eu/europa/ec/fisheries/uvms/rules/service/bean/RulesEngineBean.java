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

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ACTIVITY_NON_UNIQUE_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ACTIVITY_WITH_TRIP_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET_LIST;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FISHING_GEAR_TYPE_CHARACTERISTICS;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityTableType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.rules.entity.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdTypeWithFlagState;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;

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
    private FactRuleEvaluator evaluator;

    @EJB
    private RulesActivityServiceBean activityService;

    @EJB
    private RulesFishingGearBean rulesFishingGearBean;

    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject) throws RulesValidationException {
        return evaluate(businessObjectType, businessObject, Collections.<ExtraValueType, Object>emptyMap());
    }

    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject, Map<ExtraValueType, Object> map) throws RulesValidationException {
        List<AbstractFact> facts = new ArrayList<>();
        if (evaluator.anyRulesDeployed()){
            AbstractGenerator generator = BusinessObjectFactory.getBusinessObjFactGenerator(businessObjectType);
            generator.setBusinessObjectMessage(businessObject);
            mdrCacheServiceBean.loadMDRCache();
            generator.setExtraValueMap(map);
            generator.setAdditionalValidationObject();
            facts.addAll(generator.generateAllFacts());
            templateEngine.evaluateFacts(facts);
        }
        return facts;
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
            List<FishingGearTypeCharacteristic> fishingGearTypeCharacteristics = rulesFishingGearBean.getAllFishingGearTypeCharacteristics();
            map.put(FISHING_GEAR_TYPE_CHARACTERISTICS, fishingGearTypeCharacteristics);
        }

        return map;
    }

}
