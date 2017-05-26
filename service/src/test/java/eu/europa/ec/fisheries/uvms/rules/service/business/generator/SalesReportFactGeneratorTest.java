package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.SalesObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;


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
        salesReportFactGenerator.getAllFacts();
    }

    @Test
    public void getAllFactsWhenChainDoesntContainNull() throws Exception {
        Report report = generateObjectToConvertToFact();

        salesReportFactGenerator.setBusinessObjectMessage(report);
        List<AbstractFact> allFacts = salesReportFactGenerator.getAllFacts();

        assertEquals("I own this crib", ((SalesFLUXReportDocumentFact)allFacts.get(2)).getOwnerFLUXParty().getIDS().get(0).getValue());
        assertEquals("fluxReportDocumentExtId", ((SalesFLUXReportDocumentFact)allFacts.get(2)).getIDS().get(0).getValue());

        List<AbstractFact> allCorrectFacts = generateFacts();

        assertEquals(allCorrectFacts.size(), allFacts.size());


//        Doesn't work..
//        for (AbstractFact fact : allCorrectFacts) {
//            boolean doesContain = allFacts.contains(fact);
//
//            assertTrue(doesContain);
//        }
    }

    private Report generateObjectToConvertToFact() {

        FLUXReportDocumentType fluxReportDocument = new FLUXReportDocumentType()
                .withIDS(new IDType().withValue("fluxReportDocumentExtId"))
                .withOwnerFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("I own this crib")));

        FLUXLocationType fluxLocation1 = helper.getFluxLocationType("BEL");
        FLUXLocationType fluxLocation2 = helper.getFluxLocationType("NED");

        RegistrationLocationType registrationLocation = helper.getRegistrationLocationType("FRA");

        RegistrationEventType registrationEvent = helper.getRegistrationEventType(registrationLocation);

        VesselTransportMeansType vessel = helper.getVesselTransportMeansType("vesselName", "vesselExtId", registrationEvent);

        DelimitedPeriodType delimitedPeriodType = helper.getDelimitedPeriodType();

        FishingActivityType fishingActivity = helper.getFishingActivityType(fluxLocation2, vessel, delimitedPeriodType);

        SalesEventType salesEvent = helper.getSalesEventType();

        FLUXOrganizationType superstijnOrganization = new FLUXOrganizationType().withName(new TextType().withValue("Superstijn"));
        FLUXOrganizationType mathiblaaOrganization = new FLUXOrganizationType().withName(new TextType().withValue("Mathiblaa"));

        SalesPartyType salesParty1 = helper.getSalesPartyType("BUYER", mathiblaaOrganization);

        SalesPartyType salesParty2 = helper.getSalesPartyType("SELLER", superstijnOrganization);

        List<SalesPartyType> salesParties = newArrayList(salesParty1, salesParty2);

        SalesDocumentType salesDocument = helper.getSalesDocumentType(fluxLocation1, fishingActivity, salesEvent, salesParties);

        FLUXSalesReportMessage fluxSalesReportMessage = helper.getFluxSalesReportMessage(fluxReportDocument, salesDocument);

        return new Report().withFLUXSalesReportMessage(fluxSalesReportMessage).withAuctionSale(new AuctionSaleType());
    }



    private List<AbstractFact> generateFacts() {

        SalesFLUXReportDocumentFact fluxReportDocumentFact = new SalesFLUXReportDocumentFact();
        fluxReportDocumentFact.setIDS(newArrayList(new IDType().withValue("fluxReportDocumentExtId")));
        fluxReportDocumentFact.setOwnerFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("I own this crib")));

        FLUXReportDocumentType fluxReportDocument = new FLUXReportDocumentType()
                .withIDS(new IDType().withValue("fluxReportDocumentExtId"))
                .withOwnerFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("I own this crib")));

        SalesFLUXLocationFact fluxLocation1Fact = helper.getSalesFLUXLocationFact("BEL");
        SalesFLUXLocationFact fluxLocation2Fact = helper.getSalesFLUXLocationFact("NED");


        FLUXLocationType fluxLocation1 = helper.getFluxLocationType("BEL");
        FLUXLocationType fluxLocation2 = helper.getFluxLocationType("NED");

        RegistrationLocationType registrationLocation = helper.getRegistrationLocationType("FRA");

        RegistrationEventType registrationEvent = helper.getRegistrationEventType(registrationLocation);

        VesselTransportMeansType vesselTransportMeansType = helper.getVesselTransportMeansType("vesselName", "vesselExtId", registrationEvent);

        SalesVesselTransportMeansFact vesselTransportMeansFact = helper.getSalesVesselTransportMeansFact("vesselName", "vesselExtId", registrationEvent);

        DelimitedPeriodType delimitedPeriodType = helper.getDelimitedPeriodType();

        SalesDelimitedPeriodFact delimitedPeriodFact = helper.getSalesDelimitedPeriodFact();

        SalesFishingActivityFact fishingActivityFact = helper.getSalesFishingActivityFact(fluxLocation2, vesselTransportMeansType, delimitedPeriodType);

        FishingActivityType fishingActivity = helper.getFishingActivityType(fluxLocation2, vesselTransportMeansType, delimitedPeriodType);

        SalesEventFact salesEventFact = helper.getSalesEventFact();

        FLUXOrganizationType superstijnOrganization = new FLUXOrganizationType().withName(new TextType().withValue("Superstijn"));
        FLUXOrganizationType mathiblaaOrganization = new FLUXOrganizationType().withName(new TextType().withValue("Mathiblaa"));

        SalesFLUXOrganizationFact superstijnOrganizationFact = new SalesFLUXOrganizationFact();
        superstijnOrganizationFact.setName(new TextType().withValue("Superstijn"));

        SalesFLUXOrganizationFact mathiblaaOrganizationFact = new SalesFLUXOrganizationFact();
        mathiblaaOrganizationFact.setName(new TextType().withValue("Mathiblaa"));

        SalesPartyFact salesParty1Fact = helper.getSalesPartyFact("BUYER", mathiblaaOrganization);

        SalesPartyFact salesParty2Fact = helper.getSalesPartyFact("SELLER", superstijnOrganization);

        SalesEventType salesEvent = helper.getSalesEventType();

        SalesPartyType salesParty1 = new SalesPartyType()
                .withRoleCodes(new CodeType().withValue("BUYER"))
                .withSpecifiedFLUXOrganization(mathiblaaOrganization);

        SalesPartyType salesParty2 = new SalesPartyType()
                .withRoleCodes(new CodeType().withValue("SELLER"))
                .withSpecifiedFLUXOrganization(superstijnOrganization);

        List<SalesPartyType> salesParties = newArrayList(salesParty1, salesParty2);

        SalesDocumentFact salesDocumentFact = helper.getSalesDocumentFact(fluxLocation1, fishingActivity, salesEvent, salesParties);

        SalesDocumentType salesDocument = helper.getSalesDocumentType(fluxLocation1, fishingActivity, salesEvent, salesParties);

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage();
        fluxSalesReportMessage.withFLUXReportDocument(fluxReportDocument);
        fluxSalesReportMessage.withSalesReports(new SalesReportType().withIncludedSalesDocuments(salesDocument));

        SalesFLUXSalesReportMessageFact fluxSalesReportMessageFact = helper.getSalesFLUXSalesReportMessageFact(fluxReportDocument, salesDocument);


        SalesReportWrapperFact reportFact = new SalesReportWrapperFact();
        reportFact.setFluxSalesReportMessage(fluxSalesReportMessage);
        reportFact.setAuctionSale(new AuctionSaleType());

        SalesAuctionSaleFact salesAuctionSaleFact = new SalesAuctionSaleFact();

        ArrayList<AbstractFact> abstractFacts = newArrayList(
                reportFact,
                salesAuctionSaleFact,
                fluxSalesReportMessageFact,
                fluxReportDocumentFact,
                salesParty1Fact,
                salesDocumentFact,
                salesEventFact,
                fishingActivityFact,
                fluxLocation1Fact,
                salesParty2Fact,
                delimitedPeriodFact,
                vesselTransportMeansFact,
                fluxLocation2Fact,
                salesParty1Fact,
                mathiblaaOrganizationFact,
                salesParty2Fact,
                superstijnOrganizationFact
        );

        ArrayList<AbstractFact> mappedFactsToFillInAllArraysWithEmptyArrays = newArrayList();

        for (AbstractFact abstractFact : abstractFacts) {
            mappedFactsToFillInAllArraysWithEmptyArrays.add(mapper.map(abstractFact, abstractFact.getClass()));
        }

        return mappedFactsToFillInAllArraysWithEmptyArrays;
    }


}