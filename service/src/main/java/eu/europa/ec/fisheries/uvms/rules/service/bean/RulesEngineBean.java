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

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.AdditionalValidationObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
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
    private RulesActivityServiceBean activityService;

    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject) throws RulesValidationException {
		List<AbstractFact> facts = new ArrayList<>();
		AbstractGenerator generator = BusinessObjectFactory.getBusinessObjFactGenerator(businessObjectType);
		generator.setBusinessObjectMessage(businessObject);
        mdrCacheServiceBean.loadMDRCache();
        setAdditionalObjects(businessObjectType, businessObject, generator);
        mdrCacheServiceBean.loadMDRCache();
        facts.addAll(generator.generateAllFacts());
        templateEngine.evaluateFacts(facts);
		return facts;
    }

    private void setAdditionalObjects(BusinessObjectType businessObjectType, Object businessObject, AbstractGenerator generator) {
        if (BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG.equals(businessObjectType)) {
            generator.setAdditionalValidationObject(ruleAssetsBean.getAssetList(businessObject), AdditionalValidationObjectType.ASSET_LIST);
            generator.setAdditionalValidationObject(activityService.getNonUniqueIdsList(businessObject), AdditionalValidationObjectType.ACTIVITY_NON_UNIQUE_IDS);
            generator.setAdditionalValidationObject(activityService.getFishingActivitiesForTrips(businessObject), AdditionalValidationObjectType.ACTIVITY_WITH_TRIP_IDS);
        }
    }

}
