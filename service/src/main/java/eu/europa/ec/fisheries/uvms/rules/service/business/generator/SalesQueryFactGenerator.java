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
import eu.europa.ec.fisheries.schema.sales.DelimitedPeriodType;
import eu.europa.ec.fisheries.schema.sales.FLUXPartyType;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesQueryMessage;
import eu.europa.ec.fisheries.schema.sales.SalesQueryParameterType;
import eu.europa.ec.fisheries.schema.sales.SalesQueryType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesDelimitedPeriodFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXPartyFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesQueryMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesQueryFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesQueryParameterFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.config.AdditionalValidationObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

@Slf4j
public class SalesQueryFactGenerator extends AbstractGenerator<FLUXSalesQueryMessage> {

    private FLUXSalesQueryMessage fluxSalesQueryMessage;
    private List<AbstractFact> facts;
    private final HashMap<Class<?>, Class<? extends AbstractFact>> mappingsToFacts;
    private MapperFacade mapper;
    private FactGeneratorHelper factGeneratorHelper;


    public SalesQueryFactGenerator() {
        this.factGeneratorHelper = new FactGeneratorHelper();
        this.mapper = new DefaultOrikaMapper().getMapper();
        mappingsToFacts = new HashMap<>();
        fillMap();
    }

    public SalesQueryFactGenerator(FactGeneratorHelper factGeneratorHelper, MapperFacade mapperFacade) {
        this();
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

    @Override public List<AbstractFact> generateAllFacts() {
        facts = new ArrayList<>();

        List<Object> objectsToMapToFacts = findObjectsToMapToFacts();

        for (Object objectToMapToFact : objectsToMapToFacts) {
            AbstractFact fact = mapper.map(objectToMapToFact, mappingsToFacts.get(objectToMapToFact.getClass()));
            facts.add(fact);
        }

        return facts;
    }

    @Override
    public void setBusinessObjectMessage(FLUXSalesQueryMessage businessObject) throws RulesValidationException {
        this.fluxSalesQueryMessage = businessObject;
    }

    @Override
    public void setAdditionalValidationObject(Object additionalObject, AdditionalValidationObjectType validationType) {

    }

    private List<Object> findObjectsToMapToFacts() {
        try {
            return factGeneratorHelper.findAllObjectsWithOneOfTheFollowingClasses(fluxSalesQueryMessage, findAllClassesFromOrikaMapperMap());
        } catch (IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace(); // TODO
            throw new RuntimeException();
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
