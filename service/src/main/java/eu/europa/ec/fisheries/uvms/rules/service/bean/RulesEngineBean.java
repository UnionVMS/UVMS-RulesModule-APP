/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.XML;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.RESPONSE_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheService;
import eu.europa.ec.fisheries.uvms.rules.service.SalesRulesService;
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
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceTechnicalException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FaResponseFactMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

@Stateless
@Slf4j
@LocalBean
public class RulesEngineBean {

    @EJB
    private SalesRulesService salesRulesService;

    @EJB
    private MDRCacheRuleService mdrCacheRuleService;

    @EJB
    private MDRCacheService mdrCacheService;

    @EJB
    private SalesReportFactGenerator salesReportFactGenerator;

    @EJB
    private SalesResponseFactGenerator salesResponseFactGenerator;


    private FaResponseFactMapper faResponseFactMapper;

    @EJB
    private RulesKieContainerInitializer initializer;

    @PostConstruct
    public void init(){
        faResponseFactMapper = new FaResponseFactMapper();
    }

    public Collection<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject, Map<ExtraValueType, Object> extraValues, String identifier) throws RulesValidationException {
        mdrCacheService.loadMDRCache(!BusinessObjectType.SENDING_FA_RESPONSE_MSG.equals(businessObjectType));

        if (businessObject != null) {
            log.info(String.format("Validating %s %s", businessObject.getClass().getSimpleName(), identifier));
            Stopwatch stopwatch = Stopwatch.createStarted();

            if (businessObjectType == BusinessObjectType.RECEIVING_FA_REPORT_MSG || businessObjectType == BusinessObjectType.SENDING_FA_REPORT_MSG) {

                AbstractGenerator generator = new ActivityFaReportFactGenerator();
                generator.setBusinessObjectMessage(businessObject);
                generator.setExtraValueMap(extraValues);
                generator.setAdditionalValidationObject();
                List<AbstractFact> facts = generator.generateAllFacts();
                Map<String, Object> globals = new HashMap<>();
                globals.put("mdrService", mdrCacheRuleService);
                return validateFacts(facts, initializer.getContainerByType(ContainerType.FA_REPORT), globals, extraValues);

            } else if (businessObjectType == BusinessObjectType.SENDING_FA_RESPONSE_MSG) {

                String from = (String) extraValues.get(SENDER_RECEIVER);
                List<IdType> idsFromDb = (List<IdType>) extraValues.get(RESPONSE_IDS);
                faResponseFactMapper.setFaResponseIds(idsFromDb);
                faResponseFactMapper.setFrom(from);
                faResponseFactMapper.setMessageType(MessageType.PUSH);
                AbstractGenerator generator = new ActivityResponseFactGenerator((FLUXResponseMessage) businessObject, faResponseFactMapper);
                List<AbstractFact> facts = generator.generateAllFacts();
                Map<String, Object> globals = new HashMap<>();
                globals.put("mdrService", mdrCacheRuleService);
                return validateFacts(facts, initializer.getContainerByType(ContainerType.FA_REPORT), globals, extraValues);

            } else if (businessObjectType == BusinessObjectType.RECEIVING_FA_RESPONSE_MSG) {

                String from = (String) extraValues.get(SENDER_RECEIVER);
                List<IdType> idsFromDb = (List<IdType>) extraValues.get(RESPONSE_IDS);
                faResponseFactMapper.setFaResponseIds(idsFromDb);
                faResponseFactMapper.setFrom(from);
                faResponseFactMapper.setMessageType(MessageType.PULL);
                AbstractGenerator generator = new ActivityResponseFactGenerator((FLUXResponseMessage) businessObject, faResponseFactMapper);
                List<AbstractFact> facts = generator.generateAllFacts();
                Map<String, Object> globals = new HashMap<>();
                globals.put("mdrService", mdrCacheRuleService);
                return validateFacts(facts, initializer.getContainerByType(ContainerType.FA_RESPONSE), globals, extraValues);

            } else if (businessObjectType == BusinessObjectType.RECEIVING_FA_QUERY_MSG || businessObjectType == BusinessObjectType.SENDING_FA_QUERY_MSG) {

                AbstractGenerator generator = new ActivityQueryFactGenerator();
                generator.setBusinessObjectMessage(businessObject);
                generator.setExtraValueMap(extraValues);
                generator.setAdditionalValidationObject();
                List<AbstractFact> facts = generator.generateAllFacts();
                Map<String, Object> globals = new HashMap<>();
                globals.put("mdrService", mdrCacheRuleService);
                return validateFacts(facts, initializer.getContainerByType(ContainerType.FA_QUERY), globals, extraValues);

            } else if (businessObjectType == BusinessObjectType.FLUX_SALES_QUERY_MSG) {

                AbstractGenerator generator = new SalesQueryFactGenerator();
                generator.setBusinessObjectMessage(businessObject);
                generator.setExtraValueMap(extraValues);
                generator.setAdditionalValidationObject();
                List<AbstractFact> facts = generator.generateAllFacts();
                Map<String, Object> globals = new HashMap<>();
                globals.put("mdrService", mdrCacheRuleService);
                globals.put("salesService", salesRulesService);
                return validateFacts(facts, initializer.getContainerByType(ContainerType.SALES), globals, extraValues);

            } else if (businessObjectType == BusinessObjectType.FLUX_SALES_REPORT_MSG) {

                StopWatch stopWatch = StopWatch.createStarted();

                List<AbstractFact> facts = salesReportFactGenerator.generateAllFacts(businessObject, extraValues);
                log.info("Flow Report, Generating the facts took: {} ms", stopWatch.getTime());
                stopWatch.reset();
                stopWatch.start();

                Map<String, Object> globals = new HashMap<>();
                globals.put("mdrService", mdrCacheRuleService);
                globals.put("salesService", salesRulesService);

                return validateFacts(facts, initializer.getContainerByType(ContainerType.SALES), globals, extraValues);
            } else if (businessObjectType == BusinessObjectType.FLUX_SALES_RESPONSE_MSG) {
                StopWatch stopWatch = StopWatch.createStarted();

                List<AbstractFact> facts = salesResponseFactGenerator.generateAllFacts(businessObject, extraValues);
                log.info("Flow Response, Generating the facts took: {} ms", stopWatch.getTime());
                stopWatch.reset();
                stopWatch.start();

                Map<String, Object> globals = new HashMap<>();
                globals.put("mdrService", mdrCacheRuleService);
                globals.put("salesService", salesRulesService);

                return validateFacts(facts, initializer.getContainerByType(ContainerType.SALES), globals, extraValues);
            }

            log.info(String.format("It took %s to evaluate the message.", stopwatch));
            log.debug(String.format("%s fact instances holding in memory.", AbstractFact.getNumOfInstances()));
        }
        return new ArrayList<>();
    }

    public Collection<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject, Map<ExtraValueType, Object> extraValues) throws RulesValidationException {
            return evaluate(businessObjectType, businessObject, extraValues, "undefined");
    }

    public Collection<AbstractFact> evaluate(BusinessObjectType businessObjectType, Object businessObject) throws RulesValidationException {
        return evaluate(businessObjectType, businessObject, Collections.<ExtraValueType, Object>emptyMap(), "undefined");
    }

    public Collection<AbstractFact> validateFacts(Collection<AbstractFact> facts, KieContainer container, Map<String, Object> globals, Map<ExtraValueType, Object> extraValues) {
        KieSession ksession = container.newKieSession();
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            if(MapUtils.isNotEmpty(globals)){
                for(Map.Entry<String, Object> entry : globals.entrySet()){
                    ksession.setGlobal(entry.getKey(), entry.getValue());
                }
            }
            for (AbstractFact fact : facts) { // Insert All the facts
                ksession.insert(fact);
            }
            int numberOfFiredRules = ksession.fireAllRules();
            log.info("Drools eval took : {} ms failed {}", stopwatch.elapsed(TimeUnit.MILLISECONDS), numberOfFiredRules);
            ksession.dispose();
            return facts;
        } catch (RuntimeException e) {
            ksession.dispose();
            String errorMessage = "Unable to validate facts. Reason: " + e.getMessage();
            log.error(errorMessage);
            log.error("{}", extraValues.get(XML));
            throw new RulesServiceTechnicalException(errorMessage, e);
        }
    }

}
