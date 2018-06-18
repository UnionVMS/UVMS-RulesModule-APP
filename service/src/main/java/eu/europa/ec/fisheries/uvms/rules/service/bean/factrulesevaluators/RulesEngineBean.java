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

package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators.MasterEvaluator;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
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
    private MDRCacheService mdrCacheService;

    @EJB
    private MasterEvaluator masterEvaluator;

    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject) throws RulesValidationException {
        return evaluate(businessObjectType, businessObject, Collections.<ExtraValueType, Object>emptyMap());
    }

    @SuppressWarnings("unchecked")
    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject, Map<ExtraValueType, Object> map) throws RulesValidationException {
        if (businessObject != null) {
            log.info(String.format("[START] Validating %s ", businessObject.getClass().getSimpleName()));
            Stopwatch stopwatch = Stopwatch.createStarted();
            AbstractGenerator generator = BusinessObjectFactory.getBusinessObjFactGenerator(businessObjectType);
            generator.setBusinessObjectMessage(businessObject);
            mdrCacheService.loadMDRCache(!BusinessObjectType.SENDING_FA_RESPONSE_MSG.equals(businessObjectType));
            generator.setExtraValueMap(map);
            generator.setAdditionalValidationObject();
            List<AbstractFact> facts = new ArrayList<>(generator.generateAllFacts());
            masterEvaluator.evaluateFacts(facts, businessObjectType);
            log.info(String.format("[END] It took %s to evaluate the message.", stopwatch));
            log.info(String.format("%s fact instances holding in memory.", AbstractFact.getNumOfInstances()));
            return facts;
        }
        return new ArrayList<>();
    }

}
