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
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.config.AdditionalValidationObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SalesReportFactGenerator extends AbstractGenerator<Report> {

    private Report report;
    private List<AbstractFact> facts;
    private final HashMap<Class<?>, Class<? extends AbstractFact>> mappingsToFacts;
    private MapperFacade mapper;
    private FactGeneratorHelper factGeneratorHelper;


    public SalesReportFactGenerator() {
        this.factGeneratorHelper = new FactGeneratorHelper();
        this.mapper = new DefaultOrikaMapper().getMapper();
        mappingsToFacts = new HashMap<>();
        fillMap();
    }

    public SalesReportFactGenerator(FactGeneratorHelper factGeneratorHelper, MapperFacade mapperFacade) {
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

    private List<Object> findObjectsToMapToFacts() {
        try {
            return factGeneratorHelper.findAllObjectsWithOneOfTheFollowingClasses(report, findAllClassesFromOrikaMapperMap());
        } catch (IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace(); // TODO
            throw new RuntimeException();
        }
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
    public void setBusinessObjectMessage(Report businessObject) throws RulesValidationException {
        this.report = businessObject;
    }

    @Override
    public void setAdditionalValidationObject(Object additionalObject, AdditionalValidationObjectType validationType) {

    }

    private void fillMap() {
        mappingsToFacts.put(Report.class, SalesReportWrapperFact.class);
        mappingsToFacts.put(AuctionSaleType.class, SalesAuctionSaleFact.class);
        mappingsToFacts.put(FLUXSalesReportMessage.class, SalesFLUXSalesReportMessageFact.class);
        mappingsToFacts.put(AAPProcessType.class, SalesAAPProcessFact.class);
        mappingsToFacts.put(AAPProductType.class, SalesAAPProductFact.class);
        mappingsToFacts.put(SalesBatchType.class, SalesBatchFact.class);
        mappingsToFacts.put(ContactPartyType.class, SalesContactPartyFact.class);
        mappingsToFacts.put(ContactPersonType.class, SalesContactPersonFact.class);
        mappingsToFacts.put(DelimitedPeriodType.class, SalesDelimitedPeriodFact.class);
        mappingsToFacts.put(SalesDocumentType.class, SalesDocumentFact.class);
        mappingsToFacts.put(SalesEventType.class, SalesEventFact.class);
        mappingsToFacts.put(FishingActivityType.class, SalesFishingActivityFact.class);
        mappingsToFacts.put(FishingTripType.class, SalesFishingTripFact.class);
        mappingsToFacts.put(FLUXGeographicalCoordinateType.class, SalesFLUXGeographicalCoordinateFact.class);
        mappingsToFacts.put(FLUXLocationType.class, SalesFLUXLocationFact.class);
        mappingsToFacts.put(FLUXOrganizationType.class, SalesFLUXOrganizationFact.class);
        mappingsToFacts.put(FLUXPartyType.class, SalesFLUXPartyFact.class);
        mappingsToFacts.put(FLUXReportDocumentType.class, SalesFLUXReportDocumentFact.class);
        mappingsToFacts.put(FLUXSalesReportMessage.class, SalesFLUXSalesReportMessageFact.class);
        mappingsToFacts.put(SalesPartyType.class, SalesPartyFact.class);
        mappingsToFacts.put(SalesPriceType.class, SalesPriceFact.class);
        mappingsToFacts.put(SalesReportType.class, SalesReportFact.class);
        mappingsToFacts.put(SizeDistributionType.class, SalesSizeDistributionFact.class);
        mappingsToFacts.put(StructuredAddressType.class, SalesStructuredAddressFact.class);
        mappingsToFacts.put(VesselCountryType.class, SalesVesselCountryFact.class);
        mappingsToFacts.put(VesselTransportMeansType.class, SalesVesselTransportMeansFact.class);
    }


}
