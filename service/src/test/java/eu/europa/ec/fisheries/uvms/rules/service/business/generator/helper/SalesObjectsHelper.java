package eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.AAPProcessType;
import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.AuctionSaleType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.ContactPartyType;
import eu.europa.ec.fisheries.schema.sales.ContactPersonType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.DelimitedPeriodType;
import eu.europa.ec.fisheries.schema.sales.FLUXGeographicalCoordinateType;
import eu.europa.ec.fisheries.schema.sales.FLUXLocationType;
import eu.europa.ec.fisheries.schema.sales.FLUXOrganizationType;
import eu.europa.ec.fisheries.schema.sales.FLUXPartyType;
import eu.europa.ec.fisheries.schema.sales.FLUXReportDocumentType;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.FishingActivityType;
import eu.europa.ec.fisheries.schema.sales.FishingTripType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.RegistrationEventType;
import eu.europa.ec.fisheries.schema.sales.RegistrationLocationType;
import eu.europa.ec.fisheries.schema.sales.Report;
import eu.europa.ec.fisheries.schema.sales.SalesBatchType;
import eu.europa.ec.fisheries.schema.sales.SalesDocumentType;
import eu.europa.ec.fisheries.schema.sales.SalesEventType;
import eu.europa.ec.fisheries.schema.sales.SalesPartyType;
import eu.europa.ec.fisheries.schema.sales.SalesPriceType;
import eu.europa.ec.fisheries.schema.sales.SalesReportType;
import eu.europa.ec.fisheries.schema.sales.SizeDistributionType;
import eu.europa.ec.fisheries.schema.sales.StructuredAddressType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.schema.sales.VesselCountryType;
import eu.europa.ec.fisheries.schema.sales.VesselTransportMeansType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesDelimitedPeriodFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesEventFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFishingActivityFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFishingTripFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesPartyFact;
import org.joda.time.DateTime;

/**
 * Created by MATBUL on 24/05/2017.
 */
public class SalesObjectsHelper {

    public Report generateFullFLUXSalesReportMessage() {
        FLUXReportDocumentType fluxReportDocument = new FLUXReportDocumentType()
                .withIDS(new IDType().withValue("fluxReportDocumentExtId"))
                .withOwnerFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("I own this crib")));

        FLUXGeographicalCoordinateType fluxGeographicalCoordinateType = new FLUXGeographicalCoordinateType();

        FLUXLocationType fluxLocation1 = getFluxLocationType("BEL")
                .withSpecifiedPhysicalFLUXGeographicalCoordinate(fluxGeographicalCoordinateType)
                .withPhysicalStructuredAddress(new StructuredAddressType());
        FLUXLocationType fluxLocation2 = getFluxLocationType("NED")
                .withSpecifiedPhysicalFLUXGeographicalCoordinate(fluxGeographicalCoordinateType)
                .withPhysicalStructuredAddress(new StructuredAddressType());

        RegistrationLocationType registrationLocation = getRegistrationLocationType("FRA");

        RegistrationEventType registrationEvent = getRegistrationEventType(registrationLocation);

        ContactPersonType contactPersonType = new ContactPersonType();
        ContactPartyType contactPartyType = new ContactPartyType().withSpecifiedContactPersons(contactPersonType);

        VesselTransportMeansType vesselTransportMeansType = getVesselTransportMeansType("vesselName", "vesselExtId", registrationEvent)
                .withSpecifiedContactParties(contactPartyType)
                .withRegistrationVesselCountry(new VesselCountryType());


        SalesFishingTripFact salesFishingTripFact = new SalesFishingTripFact();
        FishingTripType fishingTripType = new FishingTripType();

        DelimitedPeriodType delimitedPeriodType = getDelimitedPeriodType();

        FishingActivityType fishingActivity = getFishingActivityType(fluxLocation2, vesselTransportMeansType, delimitedPeriodType);
        fishingActivity.withRelatedVesselTransportMeans(vesselTransportMeansType);
        fishingActivity.withSpecifiedFishingTrip(fishingTripType);

        FLUXOrganizationType superstijnOrganization = new FLUXOrganizationType().withName(new TextType().withValue("Superstijn"));

        FLUXOrganizationType mathiblaaOrganization = new FLUXOrganizationType().withName(new TextType().withValue("Mathiblaa"));
        SalesEventType salesEvent = getSalesEventType();

        SalesPartyType salesParty1 = new SalesPartyType()
                .withRoleCodes(new CodeType().withValue("BUYER"))
                .withSpecifiedFLUXOrganization(mathiblaaOrganization);

        SalesPartyType salesParty2 = new SalesPartyType()
                .withRoleCodes(new CodeType().withValue("SELLER"))
                .withSpecifiedFLUXOrganization(superstijnOrganization);

        List<SalesPartyType> salesParties = newArrayList(salesParty1, salesParty2);

        AAPProductType aapProductType = new AAPProductType()
                .withAppliedAAPProcesses(new AAPProcessType())
                .withSpecifiedSizeDistribution(new SizeDistributionType())
                .withTotalSalesPrice(new SalesPriceType());

        List<AAPProductType> products = newArrayList(aapProductType);

        SalesDocumentType salesDocument = getSalesDocumentType(fluxLocation1, fishingActivity, salesEvent, salesParties, products);

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage();
        fluxSalesReportMessage.withFLUXReportDocument(fluxReportDocument);
        fluxSalesReportMessage.withSalesReports(new SalesReportType().withIncludedSalesDocuments(salesDocument));

        Report report = new Report()
                .withFLUXSalesReportMessage(fluxSalesReportMessage)
                .withAuctionSale(new AuctionSaleType());

        return report;
    }

    public boolean checkIfFactsContainClass(List<AbstractFact> allFacts, List<Class> listOfClassesThatWereCreated, Class<? extends AbstractFact> clazz, boolean testValid) {
        for (AbstractFact fact : allFacts) {
            if (fact.getClass().equals(clazz)) {
                listOfClassesThatWereCreated.add(fact.getClass());
                testValid = true;
                break;
            }
            testValid = false;
        }
        return testValid;
    }

    public SalesFLUXSalesReportMessageFact getSalesFLUXSalesReportMessageFact(FLUXReportDocumentType fluxReportDocument, SalesDocumentType salesDocument) {
        SalesFLUXSalesReportMessageFact fluxSalesReportMessageFact = new SalesFLUXSalesReportMessageFact();
        fluxSalesReportMessageFact.setFLUXReportDocument(fluxReportDocument);
        fluxSalesReportMessageFact.setSalesReports(newArrayList(new SalesReportType().withIncludedSalesDocuments(salesDocument)));
        return fluxSalesReportMessageFact;
    }


    public SalesPartyFact getSalesPartyFact(String buyerOrSeller, FLUXOrganizationType mathiblaaOrganization) {
        SalesPartyFact salesParty1Fact = new SalesPartyFact();
        salesParty1Fact.setRoleCodes(Lists.newArrayList(new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType(buyerOrSeller)));
        salesParty1Fact.setSpecifiedFLUXOrganization(mathiblaaOrganization);
        return salesParty1Fact;
    }

    public SalesEventFact getSalesEventFact() {
        SalesEventFact salesEventFact = new SalesEventFact();
        salesEventFact.setOccurrenceDateTime(new DateTimeType().withDateTime(new DateTime(2017, 3, 2, 15, 0)));
        return salesEventFact;
    }

    public SalesFishingActivityFact getSalesFishingActivityFact(FLUXLocationType fluxLocation2, VesselTransportMeansType vesselTransportMeansType, DelimitedPeriodType delimitedPeriodType) {
        SalesFishingActivityFact fishingActivityFact = new SalesFishingActivityFact();
        fishingActivityFact.setRelatedVesselTransportMeans(newArrayList(vesselTransportMeansType));
        fishingActivityFact.setSpecifiedDelimitedPeriods(newArrayList(delimitedPeriodType));
        fishingActivityFact.setRelatedFLUXLocations(newArrayList(fluxLocation2));
        return fishingActivityFact;
    }

    public SalesDelimitedPeriodFact getSalesDelimitedPeriodFact() {
        SalesDelimitedPeriodFact delimitedPeriodFact = new SalesDelimitedPeriodFact();
        delimitedPeriodFact.setStartDateTime(new DateTimeType().withDateTime(new DateTime(2017, 3, 3, 14, 0)));
        return delimitedPeriodFact;
    }


    public FLUXSalesReportMessage getFluxSalesReportMessage(FLUXReportDocumentType fluxReportDocument, SalesDocumentType salesDocument) {
        return new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocument)
                .withSalesReports(new SalesReportType().withIncludedSalesDocuments(salesDocument));
    }

    public SalesDocumentType getSalesDocumentType(FLUXLocationType fluxLocation1, FishingActivityType fishingActivity, SalesEventType salesEvent, List<SalesPartyType> salesParties, List<AAPProductType> products) {
        return new SalesDocumentType()
                .withIDS(new IDType().withValue("id"))
                .withSpecifiedFLUXLocations(fluxLocation1)
                .withSpecifiedSalesEvents(salesEvent)
                .withSpecifiedFishingActivities(fishingActivity)
                .withSpecifiedSalesParties(salesParties)
                .withSpecifiedSalesBatches(Lists.newArrayList(new SalesBatchType().withSpecifiedAAPProducts(products)));
    }

    public SalesPartyType getSalesPartyType(String buyerOrSeller, FLUXOrganizationType fluxOrganizationType) {
        return new SalesPartyType()
                .withRoleCodes(new CodeType().withValue(buyerOrSeller))
                .withSpecifiedFLUXOrganization(fluxOrganizationType);
    }

    public SalesEventType getSalesEventType() {
        return new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(new DateTime(2017, 3, 2, 15, 0)));
    }

    public FishingActivityType getFishingActivityType(FLUXLocationType fluxLocation2, VesselTransportMeansType vessel, DelimitedPeriodType delimitedPeriodType) {
        return new FishingActivityType()
                .withRelatedVesselTransportMeans(vessel)
                .withSpecifiedDelimitedPeriods(delimitedPeriodType)
                .withRelatedFLUXLocations(fluxLocation2);
    }

    public DelimitedPeriodType getDelimitedPeriodType() {
        return new DelimitedPeriodType().withStartDateTime(new DateTimeType().withDateTime(new DateTime(2017, 3, 3, 14, 0)));
    }

    public VesselTransportMeansType getVesselTransportMeansType(String vesselName, String vesselExtId, RegistrationEventType registrationEvent) {
        return new VesselTransportMeansType()
                .withNames(new TextType().withValue(vesselName))
                .withIDS(new IDType().withValue(vesselExtId))
                .withSpecifiedRegistrationEvents(registrationEvent);
    }

    public RegistrationEventType getRegistrationEventType(RegistrationLocationType registrationLocation) {
        return new RegistrationEventType()
                .withRelatedRegistrationLocation(registrationLocation);
    }

    public RegistrationLocationType getRegistrationLocationType(String param) {
        return new RegistrationLocationType()
                .withCountryID(new IDType().withValue(param));
    }

    public FLUXLocationType getFluxLocationType(String param) {
        return new FLUXLocationType()
                .withID(new IDType().withValue(param));
    }
}
