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

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.RESPONSE_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;

import javax.annotation.PostConstruct;
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
import eu.europa.ec.fisheries.uvms.rules.service.business.MessageType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityFaReportFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityQueryFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityResponseFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.SalesQueryFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.SalesReportFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.SalesResponseFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FaResponseFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

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

    private FaResponseFactMapper faResponseFactMapper;

    @PostConstruct
    public void init(){
        faResponseFactMapper = new FaResponseFactMapper(new XPathStringWrapper());
    }

    @SuppressWarnings("unchecked")
    public List<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject, Map<ExtraValueType, Object> map) throws RulesValidationException {

        mdrCacheService.loadMDRCache(!BusinessObjectType.SENDING_FA_RESPONSE_MSG.equals(businessObjectType));

        if (businessObject != null) {
            log.info(String.format("[START] Validating %s ", businessObject.getClass().getSimpleName()));
            Stopwatch stopwatch = Stopwatch.createStarted();

            AbstractGenerator generator = null;

            if (businessObjectType == BusinessObjectType.RECEIVING_FA_REPORT_MSG || businessObjectType == BusinessObjectType.SENDING_FA_REPORT_MSG) {
                generator = new ActivityFaReportFactGenerator();
                generator.setBusinessObjectMessage(businessObject);
                generator.setExtraValueMap(map);
                generator.setAdditionalValidationObject();
            } else if (businessObjectType == BusinessObjectType.SENDING_FA_RESPONSE_MSG) {
                String from = (String) map.get(SENDER_RECEIVER);
                List<IdType> idsFromDb = (List<IdType>) map.get(RESPONSE_IDS);
                faResponseFactMapper.setFaResponseIds(idsFromDb);
                faResponseFactMapper.setFrom(from);
                faResponseFactMapper.setMessageType(MessageType.PUSH);
                generator = new ActivityResponseFactGenerator((FLUXResponseMessage) businessObject, faResponseFactMapper);
            } else if (businessObjectType == BusinessObjectType.RECEIVING_FA_RESPONSE_MSG) {
                String from = (String) map.get(SENDER_RECEIVER);
                List<IdType> idsFromDb = (List<IdType>) map.get(RESPONSE_IDS);
                faResponseFactMapper.setFaResponseIds(idsFromDb);
                faResponseFactMapper.setFrom(from);
                faResponseFactMapper.setMessageType(MessageType.PULL);
                generator = new ActivityResponseFactGenerator((FLUXResponseMessage) businessObject, faResponseFactMapper);
            } else if (businessObjectType == BusinessObjectType.RECEIVING_FA_QUERY_MSG || businessObjectType == BusinessObjectType.SENDING_FA_QUERY_MSG) {
                generator = new ActivityQueryFactGenerator();
                generator.setBusinessObjectMessage(businessObject);
                generator.setExtraValueMap(map);
                generator.setAdditionalValidationObject();

            } else if (businessObjectType == BusinessObjectType.FLUX_SALES_QUERY_MSG) {
                generator = new SalesQueryFactGenerator();
                generator.setBusinessObjectMessage(businessObject);
                generator.setExtraValueMap(map);
                generator.setAdditionalValidationObject();

            } else if (businessObjectType == BusinessObjectType.FLUX_SALES_REPORT_MSG) {
                generator = new SalesReportFactGenerator();
                generator.setBusinessObjectMessage(businessObject);
                generator.setExtraValueMap(map);
                generator.setAdditionalValidationObject();

            } else if (businessObjectType == BusinessObjectType.FLUX_SALES_RESPONSE_MSG) {
                generator = new SalesResponseFactGenerator();
                generator.setBusinessObjectMessage(businessObject);
                generator.setExtraValueMap(map);
                generator.setAdditionalValidationObject();
            }

            if (generator != null){
                List<AbstractFact> facts = new ArrayList<>(generator.generateAllFacts());
                masterEvaluator.evaluateFacts(facts, businessObjectType);
                log.info(String.format("[END] It took %s to evaluate the message.", stopwatch));
                log.info(String.format("%s fact instances holding in memory.", AbstractFact.getNumOfInstances()));
                return facts;
            }
        }
        return new ArrayList<>();
    }

}
