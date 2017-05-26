package eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper;

import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import org.joda.time.DateTime;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by MATBUL on 24/05/2017.
 */
public class SalesObjectsHelper {


    public SalesFLUXSalesReportMessageFact getSalesFLUXSalesReportMessageFact(FLUXReportDocumentType fluxReportDocument, SalesDocumentType salesDocument) {
        SalesFLUXSalesReportMessageFact fluxSalesReportMessageFact = new SalesFLUXSalesReportMessageFact();
        fluxSalesReportMessageFact.setFluxReportDocument(fluxReportDocument);
        fluxSalesReportMessageFact.setSalesReports(newArrayList(new SalesReportType().withIncludedSalesDocuments(salesDocument)));
        return fluxSalesReportMessageFact;
    }

    public SalesDocumentFact getSalesDocumentFact(FLUXLocationType fluxLocation1, FishingActivityType fishingActivity, SalesEventType salesEvent, List<SalesPartyType> salesParties) {
        SalesDocumentFact salesDocumentFact = new SalesDocumentFact();
        salesDocumentFact.setIDS(newArrayList(new IDType().withValue("AAA")));
        salesDocumentFact.setSpecifiedFLUXLocations(newArrayList(fluxLocation1));
        salesDocumentFact.setSpecifiedSalesEvents(newArrayList(salesEvent));
        salesDocumentFact.setSpecifiedFishingActivities(newArrayList(fishingActivity));
        salesDocumentFact.setSpecifiedSalesParties(salesParties);
        return salesDocumentFact;
    }

    public SalesPartyFact getSalesPartyFact(String buyerOrSeller, FLUXOrganizationType mathiblaaOrganization) {
        SalesPartyFact salesParty1Fact = new SalesPartyFact();
        salesParty1Fact.setRoleCodes(newArrayList(new CodeType().withValue("BUYER")));
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

    public SalesVesselTransportMeansFact getSalesVesselTransportMeansFact(String vesselName, String vesselExtId, RegistrationEventType registrationEvent) {
        SalesVesselTransportMeansFact vesselTransportMeansFact = new SalesVesselTransportMeansFact();
        vesselTransportMeansFact.setNames(newArrayList(new TextType().withValue("vesselName")));
        vesselTransportMeansFact.setIDS(newArrayList(new IDType().withValue("vesselExtId")));
        vesselTransportMeansFact.setSpecifiedRegistrationEvents(newArrayList(registrationEvent));
        return vesselTransportMeansFact;
    }

    public SalesFLUXLocationFact getSalesFLUXLocationFact(String id) {
        SalesFLUXLocationFact fluxLocation1Fact = new SalesFLUXLocationFact();
        fluxLocation1Fact.setID(new IDType().withValue(id));
        return fluxLocation1Fact;
    }
    public FLUXSalesReportMessage getFluxSalesReportMessage(FLUXReportDocumentType fluxReportDocument, SalesDocumentType salesDocument) {
        return new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocument)
                .withSalesReports(new SalesReportType().withIncludedSalesDocuments(salesDocument));
    }

    public SalesDocumentType getSalesDocumentType(FLUXLocationType fluxLocation1, FishingActivityType fishingActivity, SalesEventType salesEvent, List<SalesPartyType> salesParties) {
        return new SalesDocumentType()
                .withIDS(new IDType().withValue("id"))
                .withSpecifiedFLUXLocations(fluxLocation1)
                .withSpecifiedSalesEvents(salesEvent)
                .withSpecifiedFishingActivities(fishingActivity)
                .withSpecifiedSalesParties(salesParties);
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
