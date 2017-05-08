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

import static eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType.FLUX_UNIT;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType.FLUX_VESSEL_ID_TYPE;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@Stateless
@LocalBean
@Slf4j
public class RulesEngine implements IRulesEngine {

    @EJB
	private TemplateEngine templateEngine;

    @EJB
    private MDRCache mdrCache;

    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject) throws RulesServiceException {
		try {
			List<AbstractFact> facts = new ArrayList<>();
			AbstractGenerator generator = BusinessObjectFactory.getBusinessObjFactGenerator(businessObjectType);
			generator.setBusinessObjectMessage(businessObject);

            Map<MDRAcronymType, List<String>> stringListMap = fetchMdrCodeLists();

            facts.addAll(generator.getAllFacts(stringListMap));
			templateEngine.evaluateFacts(facts);
			return facts;
		} catch (RulesModelException e) {
			throw new RulesServiceException(e.getMessage(), e);
		}
    }

    private Map<MDRAcronymType, List<String>> fetchMdrCodeLists() {
        Map<MDRAcronymType, List<String>> stringListMap = Collections.emptyMap();
        List<String> fluxVesselIdType = mdrCache.getEntry(FLUX_VESSEL_ID_TYPE);
        List<String> fluxUnit = mdrCache.getEntry(FLUX_UNIT);
        stringListMap.put(FLUX_VESSEL_ID_TYPE, fluxVesselIdType);
        stringListMap.put(FLUX_UNIT, fluxUnit);
        return stringListMap;
    }

}
