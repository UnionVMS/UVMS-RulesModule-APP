/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.time.StopWatch;
import org.joda.time.DateTime;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.CREATION_DATE_OF_MESSAGE;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ORIGINATING_PLUGIN;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;

@Slf4j
@Singleton
public class SalesResponseFactGenerator extends AbstractGenerator<FLUXSalesResponseMessage> {

    private HashMap<Class<?>, Class<? extends AbstractFact>> mappingsToFacts;
    private MapperFacade mapper;

    @EJB
    private FactGeneratorHelper factGeneratorHelper;

    private XPathStringWrapper xPathUtil;

    public SalesResponseFactGenerator(){
        this(MessageType.PULL);
    }

    public SalesResponseFactGenerator(MessageType messageType) {
        super(messageType);
        this.xPathUtil = new XPathStringWrapper();
        this.mapper = new DefaultOrikaMapper().getMapper();
        this.mappingsToFacts = new HashMap<>();
        fillMap();
    }

    public SalesResponseFactGenerator(FactGeneratorHelper factGeneratorHelper, MapperFacade mapperFacade, MessageType messageType) {
        this(messageType);
        this.factGeneratorHelper = factGeneratorHelper;
        this.mapper = mapperFacade;
    }

    @Lock(LockType.WRITE)
    public List<AbstractFact> generateAllFacts(Object businessObject, Map<ExtraValueType, Object> extraValues) {
        List<AbstractFact> facts = new ArrayList<>();
        FLUXSalesResponseMessage response = (FLUXSalesResponseMessage) businessObject;

        StopWatch stopWatch = StopWatch.createStarted();
        List<FactCandidate> objectsToMapToFacts = findObjectsToMapToFacts(response);

        log.info("Flow Response, Find objects to map to facts took: {} ms", stopWatch.getTime());
        stopWatch.reset();
        stopWatch.start();

        for (FactCandidate objectToMapToFact : objectsToMapToFacts) {
            SalesAbstractFact fact = (SalesAbstractFact) mapper.map(objectToMapToFact.getObject(), mappingsToFacts.get(objectToMapToFact.getObject().getClass()));
            fact.setSource(Source.RESPONSE);
            fact.setOriginatingPlugin(((String)extraValues.get(ORIGINATING_PLUGIN)));
            fact.setSenderOrReceiver(((String)extraValues.get(SENDER_RECEIVER)));
            fact.setCreationDateOfMessage((DateTime) extraValues.get(CREATION_DATE_OF_MESSAGE));

            facts.add(fact);

            for (Map.Entry<String, String> propertyAndXPath : objectToMapToFact.getPropertiesAndTheirXPaths().entrySet()) {
                xPathUtil.appendWithoutWrapping(propertyAndXPath.getValue());
                xPathUtil.storeInRepo(fact, propertyAndXPath.getKey());
            }
        }

        log.info("Flow Response, Mapping facts including xpath took: {} ms", stopWatch.getTime());

        return facts;
    }

    @Override
    @Deprecated
    public List<AbstractFact> generateAllFacts() {
        log.error("generateAllFacts() called in SalesResponseFactGenerator. This flow is not supported anymore");
        return Lists.newArrayList();
    }

    @Override
    @Deprecated
    public void setBusinessObjectMessage(FLUXSalesResponseMessage businessObject) throws RulesValidationException {
        log.error("setBusinessObjectMessage() called in SalesResponseFactGenerator. This flow is not supported anymore.");
    }

    private List<Class<?>> findAllClassesFromOrikaMapperMap() {
        List<Class<?>> classes = Lists.newArrayList();

        for (Map.Entry<Class<?>, Class<? extends AbstractFact>> classClassEntry : mappingsToFacts.entrySet()) {
            classes.add(classClassEntry.getKey());
        }

        return classes;
    }

    private List<FactCandidate> findObjectsToMapToFacts(FLUXSalesResponseMessage fluxResponseMessage) {
        try {
            return factGeneratorHelper.findAllObjectsWithOneOfTheFollowingClasses(fluxResponseMessage, findAllClassesFromOrikaMapperMap());
        } catch (IllegalAccessException | ClassNotFoundException e) {
            throw new RulesServiceException("Something went wrong when generating facts for a sales response", e);
        }
    }

    private void fillMap() {
        mappingsToFacts.put(FLUXSalesResponseMessage.class, SalesFLUXSalesResponseMessageFact.class);
        mappingsToFacts.put(FLUXResponseDocumentType.class, SalesFLUXResponseDocumentFact.class);
        mappingsToFacts.put(FLUXPartyType.class, SalesFLUXPartyFact.class);
        mappingsToFacts.put(ValidationResultDocumentType.class, SalesValidationResultDocumentFact.class);
        mappingsToFacts.put(ValidationQualityAnalysisType.class, SalesValidationQualityAnalysisFact.class);
    }
}
