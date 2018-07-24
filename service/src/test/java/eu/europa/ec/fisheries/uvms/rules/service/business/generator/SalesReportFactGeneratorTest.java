package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import eu.europa.ec.fisheries.schema.sales.AuctionSaleType;
import eu.europa.ec.fisheries.schema.sales.Report;
import eu.europa.ec.fisheries.schema.sales.SalesCategoryType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.SalesObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.CREATION_DATE_OF_MESSAGE;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class SalesReportFactGeneratorTest {

    private SalesReportFactGenerator salesReportFactGenerator;

    private MapperFacade mapper;

    private SalesObjectsHelper helper;

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
        salesReportFactGenerator.setExtraValueMap(Collections.<ExtraValueType, Object>emptyMap());
        salesReportFactGenerator.generateAllFacts();
    }
    @Test
    public void testSetBusinessObjectWithInvalidData() throws Exception {
        Report report = new Report()
                .withFLUXSalesReportMessage(null)
                .withAuctionSale(new AuctionSaleType().withSalesCategory(null));

        exception.expect(RuntimeException.class);
        exception.expectMessage("SalesCategory in AuctionSale cannot be null");

        salesReportFactGenerator.generateAllFacts(report, Collections.emptyMap());
    }

    @Test
    public void getAllFactsWhenChainDoesntContainNull() throws Exception {
        // data set
        Report report = helper.generateFullFLUXSalesReportMessage();
        report.getAuctionSale().setSalesCategory(SalesCategoryType.VARIOUS_SUPPLY);
        DateTime creationDateOfMessage = DateTime.now();

        Map<ExtraValueType, Object> extraValues = new HashMap<>();
        extraValues.put(SENDER_RECEIVER, "BEL");
        extraValues.put(CREATION_DATE_OF_MESSAGE, creationDateOfMessage);

        //execute
        List<AbstractFact> allFacts = salesReportFactGenerator.generateAllFacts(report, extraValues);

        List<Class<? extends SalesAbstractFact>> listOfClassesThatShouldBeCreated = createListOfClassesThatShouldBeCreated();

        for (Class clazz : listOfClassesThatShouldBeCreated) {
            assertTrue(clazz + " not found while it was expected", helper.checkIfFactsContainClass(allFacts, clazz));
        }

        checkSalesCategoryInFact(allFacts);

        for (AbstractFact fact : allFacts) {
            assertEquals("BEL", fact.getSenderOrReceiver());
            assertEquals(creationDateOfMessage, fact.getCreationDateOfMessage());
        }
    }

    private void checkSalesCategoryInFact(List<AbstractFact> allFacts) {
        for (AbstractFact fact : allFacts) {
            assertTrue(SalesCategoryType.VARIOUS_SUPPLY.equals((((SalesAbstractFact) fact).getSalesCategoryType())));
        }
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