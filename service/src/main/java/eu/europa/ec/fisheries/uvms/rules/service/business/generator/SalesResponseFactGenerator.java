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

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.Source;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import ma.glasnost.orika.MapperFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ORIGINATING_PLUGIN;

public class SalesResponseFactGenerator extends AbstractGenerator<FLUXSalesResponseMessage> {

    private FLUXSalesResponseMessage fluxResponseMessage;

    private List<AbstractFact> facts;
    private final HashMap<Class<?>, Class<? extends AbstractFact>> mappingsToFacts;
    private MapperFacade mapper;
    private FactGeneratorHelper factGeneratorHelper;


    public SalesResponseFactGenerator() {
        this.factGeneratorHelper = new FactGeneratorHelper();
        this.mapper = new DefaultOrikaMapper().getMapper();
        mappingsToFacts = new HashMap<>();
        fillMap();
    }

    public SalesResponseFactGenerator(FactGeneratorHelper factGeneratorHelper, MapperFacade mapperFacade) {
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
            SalesAbstractFact fact = (SalesAbstractFact) mapper.map(objectToMapToFact, mappingsToFacts.get(objectToMapToFact.getClass()));
            fact.setSource(Source.RESPONSE);
            fact.setOriginatingPlugin(((String)extraValueMap.get(ORIGINATING_PLUGIN)));
            facts.add(fact);
        }

        return facts;
    }

    @Override
    public void setBusinessObjectMessage(FLUXSalesResponseMessage businessObject) throws RulesValidationException {
        this.fluxResponseMessage = businessObject;
    }

    private List<Object> findObjectsToMapToFacts() {
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
