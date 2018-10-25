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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.CREATION_DATE_OF_MESSAGE;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;

@Slf4j
public class SalesQueryFactGenerator extends AbstractGenerator<FLUXSalesQueryMessage> {

    private FLUXSalesQueryMessage fluxSalesQueryMessage;
    private List<AbstractFact> facts;
    private final HashMap<Class<?>, Class<? extends AbstractFact>> mappingsToFacts;
    private MapperFacade mapper;
    private FactGeneratorHelper factGeneratorHelper;
    private XPathStringWrapper xPathUtil;

    public SalesQueryFactGenerator(){
        this(MessageType.PULL);
    }

    public SalesQueryFactGenerator(MessageType messageType) {
        super(messageType);
        this.xPathUtil = new XPathStringWrapper();
        this.mapper = new DefaultOrikaMapper().getMapper();
        this.mappingsToFacts = new HashMap<>();
        this.factGeneratorHelper = new FactGeneratorHelper();
        fillMap();
    }

    public SalesQueryFactGenerator(FactGeneratorHelper factGeneratorHelper, MapperFacade mapperFacade, MessageType messageType) {
        this(messageType);
        this.factGeneratorHelper = factGeneratorHelper;
        this.mapper = mapperFacade;
    }

    private List<Class<?>> findAllClassesFromOrikaMapperMap() {
        List<Class<?>> classes = Lists.newArrayList();

        for (Map.Entry<Class<?>, Class<? extends AbstractFact>> classClassEntry : mappingsToFacts.entrySet()) {
            classes.add(classClassEntry.getKey());
        }

        return classes;
    }

    @Override
    public List<AbstractFact> generateAllFacts() {
        facts = new ArrayList<>();

        List<FactCandidate> objectsToMapToFacts = findObjectsToMapToFacts();

        for (FactCandidate objectToMapToFact : objectsToMapToFacts) {
            SalesAbstractFact fact = (SalesAbstractFact) mapper.map(objectToMapToFact.getObject(), mappingsToFacts.get(objectToMapToFact.getObject().getClass()));
            fact.setSource(Source.QUERY);
            fact.setSenderOrReceiver(((String) extraValueMap.get(SENDER_RECEIVER)));
            fact.setCreationDateOfMessage((DateTime) extraValueMap.get(CREATION_DATE_OF_MESSAGE));
            facts.add(fact);

            for (Map.Entry<String, String> propertyAndXPath : objectToMapToFact.getPropertiesAndTheirXPaths().entrySet()) {
                xPathUtil.appendWithoutWrapping(propertyAndXPath.getValue()).storeInRepo(fact, propertyAndXPath.getKey());
            }
        }

        return facts;
    }

    @Override
    public void setBusinessObjectMessage(FLUXSalesQueryMessage businessObject) throws RulesValidationException {
        this.fluxSalesQueryMessage = businessObject;
    }

    private List<FactCandidate> findObjectsToMapToFacts() {
        try {
            return factGeneratorHelper.findAllObjectsWithOneOfTheFollowingClasses(fluxSalesQueryMessage, findAllClassesFromOrikaMapperMap());
        } catch (IllegalAccessException | ClassNotFoundException e) {
            throw new RulesServiceException("Something went wrong when generating facts for a sales query", e);
        }
    }


    private void fillMap() {
        mappingsToFacts.put(FLUXSalesQueryMessage.class, SalesFLUXSalesQueryMessageFact.class);
        mappingsToFacts.put(SalesQueryType.class, SalesQueryFact.class);
        mappingsToFacts.put(FLUXPartyType.class, SalesFLUXPartyFact.class);
        mappingsToFacts.put(DelimitedPeriodType.class, SalesDelimitedPeriodFact.class);
        mappingsToFacts.put(SalesQueryParameterType.class, SalesQueryParameterFact.class);
    }
}
