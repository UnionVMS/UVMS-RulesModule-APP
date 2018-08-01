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
import eu.europa.ec.fisheries.uvms.rules.service.business.FactCandidate;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.Source;
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

import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;

@Slf4j
@Singleton
public class SalesReportFactGenerator extends AbstractGenerator<Report> {

    private HashMap<Class<?>, Class<? extends AbstractFact>> mappingsToFacts;
    private MapperFacade mapper;

    @EJB
    private FactGeneratorHelper factGeneratorHelper;

    private XPathStringWrapper xPathUtil;


    public SalesReportFactGenerator() {
        this.mapper = new DefaultOrikaMapper().getMapper();
        this.mappingsToFacts = new HashMap<>();
        fillMap();
    }

    public SalesReportFactGenerator(FactGeneratorHelper factGeneratorHelper, MapperFacade mapperFacade) {
        this();
        this.factGeneratorHelper = factGeneratorHelper;
        this.mapper = mapperFacade;
    }

    @Lock(LockType.WRITE)
    public List<AbstractFact> generateAllFacts(Object businessObject, Map<ExtraValueType, Object> extraValues) {
        this.xPathUtil = new XPathStringWrapper();
        Report report = (Report) businessObject;

        if (report.getAuctionSale() != null && report.getAuctionSale().getSalesCategory() == null) {
            throw new NullPointerException("SalesCategory in AuctionSale cannot be null");
        }

        StopWatch stopWatch = StopWatch.createStarted();

        List<FactCandidate> objectsToMapToFacts = findObjectsToMapToFacts(report);

        log.info("Flow Report, Find objects to map to facts took: {} ms", stopWatch.getTime());
        stopWatch.reset();
        stopWatch.start();

        List<AbstractFact> facts = new ArrayList<>();
        for (FactCandidate objectToMapToFact : objectsToMapToFacts) {
            SalesAbstractFact fact = (SalesAbstractFact) mapper.map(objectToMapToFact.getObject(), mappingsToFacts.get(objectToMapToFact.getObject().getClass()));
            fillMappingVariables(fact, report, extraValues);
            facts.add(fact);

            for (Map.Entry<String, String> propertyAndXPath : objectToMapToFact.getPropertiesAndTheirXPaths().entrySet()) {
                xPathUtil.appendWithoutWrapping(propertyAndXPath.getValue());
                xPathUtil.storeInRepo(fact, propertyAndXPath.getKey());
            }
        }

        log.info("Flow Report, Mapping facts including xpath took: {} ms", stopWatch.getTime());

        return facts;
    }

    @Override
    @Deprecated
    public List<AbstractFact> generateAllFacts() {
        log.error("generateAllFacts() called in SalesReportFactGenerator. This flow is not supported anymore");
        return Lists.newArrayList();
    }

    @Override
    @Deprecated
    public void setBusinessObjectMessage(Report businessObject) throws RulesValidationException {
        log.error("setBusinessObjectMessage() called in SalesReportFactGenerator. This flow is not supported anymore.");
    }

    private List<Class<?>> findAllClassesFromOrikaMapperMap() {
        List<Class<?>> classes = Lists.newArrayList();

        for (Map.Entry<Class<?>, Class<? extends AbstractFact>> classClassEntry : mappingsToFacts.entrySet()) {
            classes.add(classClassEntry.getKey());
        }

        return classes;
    }

    private List<FactCandidate> findObjectsToMapToFacts(Report report) {
        try {
            return factGeneratorHelper.findAllObjectsWithOneOfTheFollowingClasses(report, findAllClassesFromOrikaMapperMap());
        } catch (IllegalAccessException | ClassNotFoundException e) {
            throw new RulesServiceException("Something went wrong when generating facts for a sales report", e);
        }
    }

    private void fillMappingVariables(SalesAbstractFact fact, Report report, Map<ExtraValueType, Object> extraValueMap) {
        fact.setSource(Source.REPORT);
        fact.setOriginatingPlugin(((String) extraValueMap.get(ORIGINATING_PLUGIN)));
        fact.setSenderOrReceiver(((String) extraValueMap.get(SENDER_RECEIVER)));
        fact.setCreationDateOfMessage((DateTime) extraValueMap.get(CREATION_DATE_OF_MESSAGE));

        if (report.getAuctionSale() != null) {
            fact.setSalesCategoryType(report.getAuctionSale().getSalesCategory());
        }
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
        mappingsToFacts.put(SalesPartyType.class, SalesPartyFact.class);
        mappingsToFacts.put(SalesPriceType.class, SalesPriceFact.class);
        mappingsToFacts.put(SalesReportType.class, SalesReportFact.class);
        mappingsToFacts.put(SizeDistributionType.class, SalesSizeDistributionFact.class);
        mappingsToFacts.put(StructuredAddressType.class, SalesStructuredAddressFact.class);
        mappingsToFacts.put(VesselCountryType.class, SalesVesselCountryFact.class);
        mappingsToFacts.put(VesselTransportMeansType.class, SalesVesselTransportMeansFact.class);
    }


}
