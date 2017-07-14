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

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.AdditionalValidationObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

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
	private ActivityServiceBean activityService;

    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject) throws RulesValidationException {
		List<AbstractFact> facts = new ArrayList<>();
		AbstractGenerator generator = BusinessObjectFactory.getBusinessObjFactGenerator(businessObjectType);
		generator.setBusinessObjectMessage(businessObject);
		if(BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG.equals(businessObjectType)){
			generator.setAdditionalValidationObject(ruleAssetsBean.getAssetList(businessObject), AdditionalValidationObjectType.ASSET_LIST);
			generator.setAdditionalValidationObject(activityService.getNonUniqueIdsList(businessObject), AdditionalValidationObjectType.ACTIVITY_NON_UNIQUE_IDS);
		}
		mdrCacheServiceBean.loadMDRCache();
		facts.addAll(generator.generateAllFacts());
		templateEngine.evaluateFacts(facts);
		return facts;
    }

}
