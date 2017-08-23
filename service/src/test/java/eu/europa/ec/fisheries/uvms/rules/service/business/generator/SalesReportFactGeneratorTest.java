package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import eu.europa.ec.fisheries.schema.sales.Report;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesAAPProcessFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesAAPProductFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesAuctionSaleFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesBatchFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesContactPartyFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesContactPersonFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesDelimitedPeriodFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesEventFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXGeographicalCoordinateFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXLocationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXOrganizationFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXPartyFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFishingActivityFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFishingTripFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesPartyFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesPriceFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesReportFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesReportWrapperFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesSizeDistributionFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesStructuredAddressFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesVesselCountryFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesVesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.SalesObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SalesReportFactGeneratorTest {

    private SalesReportFactGenerator salesReportFactGenerator;

    private MapperFacade mapper;

    private SalesObjectsHelper helper;

    @Before
    public void setUp() throws Exception {
        FactGeneratorHelper factGeneratorHelper = new FactGeneratorHelper();
        DefaultOrikaMapper defaultOrikaMapper = new DefaultOrikaMapper();
        helper = new SalesObjectsHelper();

        mapper = defaultOrikaMapper.getMapper();
        salesReportFactGenerator = new SalesReportFactGenerator(factGeneratorHelper, mapper);
    }

    @Test
    public void getAllFactsWhenChainContainsNull() throws Exception {
        Report report = new Report()
                .withFLUXSalesReportMessage(null);

        salesReportFactGenerator.setBusinessObjectMessage(report);
        salesReportFactGenerator.generateAllFacts();
    }

    @Test
    public void getAllFactsWhenChainDoesntContainNull() throws Exception {
        Report report = helper.generateFullFLUXSalesReportMessage();

        salesReportFactGenerator.setBusinessObjectMessage(report);
        List<AbstractFact> allFacts = salesReportFactGenerator.generateAllFacts();

        List<Class<? extends SalesAbstractFact>> listOfClassesThatShouldBeCreated = createListOfClassesThatShouldBeCreated();
        List<Class> listOfClassesThatWereCreated = newArrayList();

        for (Class clazz : listOfClassesThatShouldBeCreated) {
            boolean testValid = false;

            testValid = helper.checkIfFactsContainClass(allFacts, listOfClassesThatWereCreated, clazz, testValid);

            assertTrue(clazz + " not found while it was expected", testValid);
        }

        assertEquals(listOfClassesThatShouldBeCreated.size(), listOfClassesThatWereCreated.size());
    }

    private List<Class<? extends SalesAbstractFact>> createListOfClassesThatShouldBeCreated() {
        return Arrays.asList(SalesReportWrapperFact.class,
                SalesAuctionSaleFact.class,
                SalesFLUXSalesReportMessageFact.class,
                SalesAAPProcessFact.class,
                SalesAAPProductFact.class,
                SalesBatchFact.class,
                SalesContactPartyFact.class,
                SalesContactPersonFact.class,
                SalesDelimitedPeriodFact.class,
                SalesDocumentFact.class,
                SalesEventFact.class,
                SalesFishingActivityFact.class,
                SalesFishingTripFact.class,
                SalesFLUXGeographicalCoordinateFact.class,
                SalesFLUXLocationFact.class,
                SalesFLUXOrganizationFact.class,
                SalesFLUXPartyFact.class,
                SalesFLUXReportDocumentFact.class,
                SalesFLUXSalesReportMessageFact.class,
                SalesPartyFact.class,
                SalesPriceFact.class,
                SalesReportFact.class,
                SalesSizeDistributionFact.class,
                SalesStructuredAddressFact.class,
                SalesVesselCountryFact.class,
                SalesVesselTransportMeansFact.class);
    }
}