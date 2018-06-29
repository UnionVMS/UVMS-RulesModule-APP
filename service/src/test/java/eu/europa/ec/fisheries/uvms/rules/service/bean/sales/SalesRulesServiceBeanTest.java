package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivitySummary;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.rules.service.ActivityService;
import eu.europa.ec.fisheries.uvms.rules.service.SalesService;
import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesRulesServiceBeanTest {

    SalesFLUXSalesReportMessageFact salesFLUXSalesReportMessageFact;

    @InjectMocks
    SalesRulesServiceBean service;

    @Mock
    SalesService salesService;

    @Mock
    MapperFacade mapper;

    @Mock
    ActivityService activityService;

    @Mock
    private MDRCacheRuleService mdrService;

    @Before
    public void setUp() throws Exception {
        salesFLUXSalesReportMessageFact = new SalesFLUXSalesReportMessageFact();
    }

    @Test
    public void isReceptionDate48hAfterSaleDateWhenSaleDateMoreThan48hBeforeNow() throws Exception {
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));

        SalesEventType salesEventType1 = new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(1)));
        SalesEventType salesEventType2 = new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(2)));
        SalesEventType salesEventType3 = new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(3)));
        SalesEventType salesEventType4 = new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(48).minusMinutes(5)));

        SalesDocumentType salesDocumentFact1 = new SalesDocumentType();
        salesDocumentFact1.getSpecifiedSalesEvents().addAll(Arrays.asList(salesEventType1, salesEventType2, salesEventType3, salesEventType4));

        SalesDocumentType salesDocumentFact2 = new SalesDocumentType();
        salesDocumentFact2.getSpecifiedSalesEvents().addAll(Arrays.asList(salesEventType1, salesEventType2, salesEventType3, salesEventType4));

        SalesDocumentType salesDocumentFact3 = new SalesDocumentType();
        salesDocumentFact3.getSpecifiedSalesEvents().addAll(Arrays.asList(salesEventType1, salesEventType2, salesEventType3, salesEventType4));

        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(new SalesReportType().withIncludedSalesDocuments(Arrays.asList(salesDocumentFact1, salesDocumentFact2, salesDocumentFact3))));
        assertTrue(service.isReceptionDate48hAfterSaleDate(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterSaleDateWhenSaleDate1hBeforeNow() throws Exception {
        DateTime now = DateTime.now();
        SalesEventType salesEventType1 = new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(now.minusHours(1)));
        SalesEventType salesEventType2 = new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(now.minusHours(2)));
        SalesEventType salesEventType3 = new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(now.minusHours(3)));
        SalesEventType salesEventType4 = new SalesEventType()
                .withOccurrenceDateTime(new DateTimeType().withDateTime(now.minusHours(48)));

        SalesDocumentType salesDocumentFact1 = new SalesDocumentType();
        salesDocumentFact1.getSpecifiedSalesEvents().addAll(Arrays.asList(salesEventType1, salesEventType2, salesEventType3, salesEventType4));

        SalesDocumentType salesDocumentFact2 = new SalesDocumentType();
        salesDocumentFact2.getSpecifiedSalesEvents().addAll(Arrays.asList(salesEventType1, salesEventType2, salesEventType3, salesEventType4));

        SalesDocumentType salesDocumentFact3 = new SalesDocumentType();
        salesDocumentFact3.getSpecifiedSalesEvents().addAll(Arrays.asList(salesEventType1, salesEventType2, salesEventType3, salesEventType4));

        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(new SalesReportType().withIncludedSalesDocuments(Arrays.asList(salesDocumentFact1, salesDocumentFact2, salesDocumentFact3))));

        assertFalse(service.isReceptionDate48hAfterSaleDate(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterSaleDateWhenFluxReportDocumentIsNull() throws Exception {
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(new SalesReportType()));
        assertFalse(service.isReceptionDate48hAfterSaleDate(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterSaleDateWhenSalesReportsIsNull() throws Exception {
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        salesFLUXSalesReportMessageFact.setSalesReports(null);
        assertFalse(service.isReceptionDate48hAfterSaleDate(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterSaleDateWhenSalesReportsIsEmpty() throws Exception {
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        salesFLUXSalesReportMessageFact.setSalesReports(Lists.<SalesReportType>newArrayList());
        assertFalse(service.isReceptionDate48hAfterSaleDate(salesFLUXSalesReportMessageFact));
    }


    @Test
    public void isReceptionDate48hAfterSaleDateWhenFluxReportDocumentWhenCreationDateTimeIsNull() throws Exception {
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(null));
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(new SalesReportType()));
        assertFalse(service.isReceptionDate48hAfterSaleDate(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterSaleDateWhenFluxReportDocumentWhenDateTimeInCreationDateTimeIsNull() throws Exception {
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(null)));
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(new SalesReportType()));
        assertFalse(service.isReceptionDate48hAfterSaleDate(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterLandingDeclarationWhenLandingDeclaration48hBefore() throws Exception {
        //data set
        String fishingTripID = "MLT-TRP-20160630000001";

        DelimitedPeriodType delimitedPeriodType1 = new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(1)));
        DelimitedPeriodType delimitedPeriodType2 = new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(48)));
        FishingActivityType fishingActivityType1 = new FishingActivityType()
                .withSpecifiedDelimitedPeriods(Arrays.asList(delimitedPeriodType1, delimitedPeriodType2))
                .withSpecifiedFishingTrip(new FishingTripType().withIDS(new IDType().withValue(fishingTripID)));
        FishingActivityType fishingActivityType2 = new FishingActivityType()
                .withSpecifiedFishingTrip(new FishingTripType().withIDS(new IDType().withValue("MLT-TRP-20160630000002")))
                .withSpecifiedDelimitedPeriods(Arrays.asList(delimitedPeriodType2, delimitedPeriodType1));


        FishingActivitySummary fishingActivitySummary1 = new FishingActivitySummary();
        fishingActivitySummary1.setAcceptedDateTime(new XMLGregorianCalendarImpl(DateTime.parse("2018-01-02T13:00:12.36531Z").toGregorianCalendar()));
        fishingActivitySummary1.setActivityType("ARRIVAL");

        FishingActivitySummary fishingActivitySummary2 = new FishingActivitySummary();
        fishingActivitySummary2.setAcceptedDateTime(new XMLGregorianCalendarImpl(DateTime.now().toGregorianCalendar()));
        fishingActivitySummary2.setActivityType("LANDING");

        FishingTripResponse fishingTripResponse = new FishingTripResponse();
        fishingTripResponse.getFishingActivityLists().add(fishingActivitySummary1);
        fishingTripResponse.getFishingActivityLists().add(fishingActivitySummary2);

        Optional<FishingTripResponse> fishingTripResponse1 = Optional.of(fishingTripResponse);

        SalesReportType salesReportType = new SalesReportType()
                .withIncludedSalesDocuments(new SalesDocumentType().withSpecifiedFishingActivities(Arrays.asList(fishingActivityType1, fishingActivityType2)));
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(salesReportType));
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));

        //mock
        doReturn(fishingTripResponse1).when(activityService).getFishingTrip(fishingTripID);

        //execute

        assertFalse(service.isReceptionDate48hAfterLandingDeclaration(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterLandingDeclarationWhenLandingDeclaration48hAfter() throws Exception {
        //data set
        String fishingTripID = "MLT-TRP-20160630000001";

        DelimitedPeriodType delimitedPeriodType1 = new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(1)));
        DelimitedPeriodType delimitedPeriodType2 = new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(49)));
        FishingActivityType fishingActivityType1 = new FishingActivityType()
                .withSpecifiedDelimitedPeriods(Arrays.asList(delimitedPeriodType1, delimitedPeriodType2))
                .withSpecifiedFishingTrip(new FishingTripType().withIDS(new IDType().withValue(fishingTripID)));
        FishingActivityType fishingActivityType2 = new FishingActivityType()
                .withSpecifiedDelimitedPeriods(Arrays.asList(delimitedPeriodType2, delimitedPeriodType1))
                .withSpecifiedFishingTrip(new FishingTripType().withIDS(new IDType().withValue(fishingTripID)));

        FishingActivitySummary fishingActivitySummary1 = new FishingActivitySummary();
        fishingActivitySummary1.setAcceptedDateTime(new XMLGregorianCalendarImpl(DateTime.parse("2018-01-02T13:00:12.36531Z").toGregorianCalendar()));
        fishingActivitySummary1.setActivityType("ARRIVAL");

        FishingActivitySummary fishingActivitySummary2 = new FishingActivitySummary();
        fishingActivitySummary2.setAcceptedDateTime(new XMLGregorianCalendarImpl(DateTime.parse("2018-01-03T13:00:12.36531Z").toGregorianCalendar()));
        fishingActivitySummary2.setActivityType("LANDING");

        FishingTripResponse fishingTripResponse = new FishingTripResponse();
        fishingTripResponse.getFishingActivityLists().add(fishingActivitySummary1);
        fishingTripResponse.getFishingActivityLists().add(fishingActivitySummary2);

        Optional<FishingTripResponse> fishingTripResponse1 = Optional.of(fishingTripResponse);

        SalesReportType salesReportType = new SalesReportType()
                .withIncludedSalesDocuments(new SalesDocumentType().withSpecifiedFishingActivities(Arrays.asList(fishingActivityType1, fishingActivityType2)));
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(salesReportType));
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now().plusHours(49))));

        //mock
        doReturn(fishingTripResponse1).when(activityService).getFishingTrip(fishingTripID);

        //execute
        assertTrue(service.isReceptionDate48hAfterLandingDeclaration(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterLandingDeclarationWhenSalesReportsIsEmpty() throws Exception {
        salesFLUXSalesReportMessageFact.setSalesReports(Lists.<SalesReportType>newArrayList());
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        assertFalse(service.isReceptionDate48hAfterLandingDeclaration(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterLandingDeclarationWhenSalesReportsIsNull() throws Exception {
        salesFLUXSalesReportMessageFact.setSalesReports(null);
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        assertFalse(service.isReceptionDate48hAfterLandingDeclaration(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterLandingDeclarationWhenIncludedSalesDocumentsInSalesReportsIsEmpty() throws Exception {
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(new SalesReportType().withIncludedSalesDocuments(Lists.<SalesDocumentType>newArrayList())));
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        assertFalse(service.isReceptionDate48hAfterLandingDeclaration(salesFLUXSalesReportMessageFact));
    }

    @Test
    public void isReceptionDate48hAfterLandingDeclarationWhenIncludedSalesDocumentsInSalesReportsIsNull() throws Exception {
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(new SalesReportType().withIncludedSalesDocuments((SalesDocumentType) null)));
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        assertFalse(service.isReceptionDate48hAfterLandingDeclaration(salesFLUXSalesReportMessageFact));
    }


    @Test
    public void isIdNotUniqueWhenFactIsNull() throws Exception {
        assertFalse(service.isIdNotUnique((SalesDocumentFact) null));
    }


    @Test
    public void isIdNotUniqueWhenIdsInFactIsNull() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setIDS(null);
        assertFalse(service.isIdNotUnique(fact));
    }

    @Test
    public void isIdNotUniqueWhenIdsInFactIsEmpty() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setIDS(Lists.<IdType>newArrayList());
        assertFalse(service.isIdNotUnique(fact));
    }

    @Test
    public void isIdNotUniqueWhenFirstEntryInIdsInFactIsNull() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        List<IdType> listWithNull = new ArrayList<>();
        listWithNull.add(null);

        fact.setIDS(listWithNull);
        assertFalse(service.isIdNotUnique(fact));
    }

    @Test
    public void isIdNotUniqueWhenValueInIdsInFactIsBlank() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setIDS(Arrays.asList(new IdType("")));
        assertFalse(service.isIdNotUnique(fact));
    }

    @Test
    public void isIdNotUniqueWhenValueInIdsInFactIsNull() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setIDS(Arrays.asList(new IdType(null)));
        assertFalse(service.isIdNotUnique(fact));
    }

    @Test
    public void doesTakeOverDocumentIdExistWhenFactIsNull() throws Exception {
        assertTrue(service.doesTakeOverDocumentIdExist(null));
    }

    @Test
    public void doesTakeOverDocumentIdExistWhenTODIdsInFactIsNull() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setTakeoverDocumentIDs(null);
        assertTrue(service.doesTakeOverDocumentIdExist(fact));
    }

    @Test
    public void doesTakeOverDocumentIdExistWhenTODIdsInFactIsEmpty() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setTakeoverDocumentIDs(Lists.<IdType>newArrayList());
        assertTrue(service.doesTakeOverDocumentIdExist(fact));
    }

    @Test
    public void doesTakeOverDocumentIdExistWhenTODIdsInFactContainsNulls() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setTakeoverDocumentIDs(Arrays.asList(new IdType("aaa"), new IdType("bbb"), new IdType(null)));

        List<String> ids = Arrays.asList("aaa", "bbb");

        doReturn(true).when(salesService).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_REPORT);

        assertTrue(service.doesTakeOverDocumentIdExist(fact));
    }

    @Test
    public void doesSalesNoteIdExistWhenFactIsNull() throws Exception {
        assertTrue(service.doesSalesNoteIdExist(null));
    }

    @Test
    public void doesSalesNoteIdExistWhenTODIdsInFactIsNull() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setSalesNoteIDs(null);
        assertTrue(service.doesSalesNoteIdExist(fact));
    }

    @Test
    public void doesSalesNoteIdExistWhenTODIdsInFactIsEmpty() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setSalesNoteIDs(Lists.<IdType>newArrayList());
        assertTrue(service.doesSalesNoteIdExist(fact));
    }

    @Test
    public void doesSalesNoteIdExistWhenTODIdsInFactContainsNulls() throws Exception {
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setSalesNoteIDs(Arrays.asList(new IdType("aaa"), new IdType("bbb"), new IdType(null)));

        List<String> ids = Arrays.asList("aaa", "bbb");

        doReturn(true).when(salesService).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_REPORT);

        assertTrue(service.doesSalesNoteIdExist(fact));
    }

    @Test
    public void isStartDateMoreThan3YearsAgo() throws Exception {
        SalesDelimitedPeriodFact fact = new SalesDelimitedPeriodFact();
        fact.setStartDateTime(new DateTimeType().withDateTime(DateTime.now()));

        assertFalse(service.isStartDateMoreThan3YearsAgo(fact));
    }

    @Test
    public void isStartDateMoreThan3YearsAgoWhenMoreThan3YearsAgo() throws Exception {
        SalesDelimitedPeriodFact fact = new SalesDelimitedPeriodFact();
        fact.setStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusYears(4)));

        assertTrue(service.isStartDateMoreThan3YearsAgo(fact));
    }

    @Test
    public void isStartDateMoreThan3YearsAgoWhen3Years2MinutesAgo() throws Exception {
        SalesDelimitedPeriodFact fact = new SalesDelimitedPeriodFact();
        fact.setStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusYears(3).minusMinutes(2)));

        assertTrue(service.isStartDateMoreThan3YearsAgo(fact));
    }

    @Test
    public void isStartDateMoreThan3YearsAgoWhenFactIsNull() throws Exception {
        assertFalse(service.isStartDateMoreThan3YearsAgo(null));
    }

    @Test
    public void isStartDateMoreThan3YearsAgoWhenStartDateInFactIsNull() throws Exception {
        SalesDelimitedPeriodFact fact = new SalesDelimitedPeriodFact();
        fact.setStartDateTime(null);

        assertFalse(service.isStartDateMoreThan3YearsAgo(fact));
    }

    @Test
    public void isStartDateMoreThan3YearsAgoWhenDateTimeInStartDateInFactIsNull() throws Exception {
        SalesDelimitedPeriodFact fact = new SalesDelimitedPeriodFact();
        fact.setStartDateTime(new DateTimeType().withDateTime(null));

        assertFalse(service.isStartDateMoreThan3YearsAgo(fact));
    }

    @Test
    public void isDateOfValidationAfterCreationDateOfResponseWhenValidationIsBeforeCreationOfResponse() throws Exception {
        DateTime responseCreationDate = DateTime.now();
        DateTime validationResultCreationDate = DateTime.now().minusYears(10);

        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setCreationDateTime(new DateTimeType().withDateTime(responseCreationDate));
        fact.setRelatedValidationResultDocuments(Arrays.asList(
                new ValidationResultDocumentType().withCreationDateTime(new DateTimeType().withDateTime(validationResultCreationDate))));

        boolean result = service.isDateOfValidationAfterCreationDateOfResponse(fact);

        assertFalse(result);
    }

    @Test
    public void isDateOfValidationAfterCreationDateOfResponseWhenValidationIsAfterCreationOfResponse() throws Exception {
        DateTime responseCreationDate = DateTime.now();
        DateTime validationResultCreationDate = DateTime.now().plusYears(10);

        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setCreationDateTime(new DateTimeType().withDateTime(responseCreationDate));
        fact.setRelatedValidationResultDocuments(Arrays.asList(
                new ValidationResultDocumentType().withCreationDateTime(new DateTimeType().withDateTime(validationResultCreationDate))));

        boolean result = service.isDateOfValidationAfterCreationDateOfResponse(fact);

        assertTrue(result);
    }

    @Test
    public void isDateOfValidationAfterCreationDateOfResponseWhenFactIsNull() throws Exception {
        boolean result = service.isDateOfValidationAfterCreationDateOfResponse(null);

        assertFalse(result);
    }

    @Test
    public void isDateNotInPastWhenInThePast() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setCreationDateTime(new DateTimeType().withDateTime(DateTime.now().minusMinutes(10)));
        assertFalse(service.isDateNotInPast(fact));
    }

    @Test
    public void isDateNotInPastWhenInTheFuture() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setCreationDateTime(new DateTimeType().withDateTime(DateTime.now().plusMinutes(10)));
        assertTrue(service.isDateNotInPast(fact));
    }

    @Test
    public void isDateNotInPastWhenFactIsNull() throws Exception {
        assertFalse(service.isDateNotInPast(null));
    }


    @Test
    public void isDateNotInPastWhenCreationDateTimeInFactIsNull() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setCreationDateTime(null);
        assertFalse(service.isDateNotInPast(fact));
    }

    @Test
    public void isDateNotInPastWhenDateTimeInCreationDateTimeInFactIsNull() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setCreationDateTime(new DateTimeType().withDateTime(null));
        assertFalse(service.isDateNotInPast(fact));
    }

    @Test
    public void doesReferencedIdNotExistWhenItDoesNotExist() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setReferencedID(new IdType("referencedID"));

        doReturn(false).when(salesService).isIdNotUnique("referencedID", SalesMessageIdType.SALES_REFERENCED_ID);

        assertTrue(service.doesReferencedIdNotExist(fact));
        verify(salesService).isIdNotUnique("referencedID", SalesMessageIdType.SALES_REFERENCED_ID);
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReferencedIdNotExistWhenItExists() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setReferencedID(new IdType("referencedID"));

        doReturn(true).when(salesService).isIdNotUnique("referencedID", SalesMessageIdType.SALES_REFERENCED_ID);

        assertFalse(service.doesReferencedIdNotExist(fact));
        verify(salesService).isIdNotUnique("referencedID", SalesMessageIdType.SALES_REFERENCED_ID);
        verifyNoMoreInteractions(salesService);
    }


    @Test
    public void doesReferencedIdNotExistWhenFactIsNull() throws Exception {
        assertFalse(service.doesReferencedIdNotExist(null));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReferencedIdNotExistWhenReferencedIdInFactIsNull() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setReferencedID(null);
        assertFalse(service.doesReferencedIdNotExist(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReferencedIdNotExistWhenValueReferencedIdInFactIsNull() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setReferencedID(new IdType(null));
        assertFalse(service.doesReferencedIdNotExist(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReferencedIdNotExistWhenValueReferencedIdInFactIsBlank() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setReferencedID(new IdType(""));
        assertFalse(service.doesReferencedIdNotExist(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenIdIsNotUnique() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setIDS(Arrays.asList(new IdType("id")));

        doReturn(true).when(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_RESPONSE);

        assertTrue(service.isIdNotUnique(fact));
        verify(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_RESPONSE);
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenIdIsUnique() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setIDS(Arrays.asList(new IdType("id")));

        doReturn(false).when(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_RESPONSE);

        assertFalse(service.isIdNotUnique(fact));
        verify(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_RESPONSE);
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenSalesFLUXResponseDocumentFactIsNull() throws Exception {
        assertFalse(service.isIdNotUnique((SalesFLUXResponseDocumentFact) null));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenIdsInSalesFLUXResponseDocumentFactIsEmpty() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setIDS(Lists.<IdType>newArrayList());

        assertFalse(service.isIdNotUnique(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenValueInIdInIdsInSalesFLUXResponseDocumentFactIsNull() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setIDS(Arrays.asList(new IdType(null)));

        assertFalse(service.isIdNotUnique(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenValueInIdInIdsInSalesFLUXResponseDocumentFactIsBlank() throws Exception {
        SalesFLUXResponseDocumentFact fact = new SalesFLUXResponseDocumentFact();
        fact.setIDS(Arrays.asList(new IdType("")));

        assertFalse(service.isIdNotUnique(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenSalesQueryFactIdIsNotUnique() throws Exception {
        SalesQueryFact fact = new SalesQueryFact();
        fact.setID(new IdType("id"));

        doReturn(true).when(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_QUERY);

        assertTrue(service.isIdNotUnique(fact));
        verify(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_QUERY);
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenSalesQueryFactIdIsUnique() throws Exception {
        SalesQueryFact fact = new SalesQueryFact();
        fact.setID(new IdType("id"));

        doReturn(false).when(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_QUERY);

        assertFalse(service.isIdNotUnique(fact));
        verify(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_QUERY);
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenSalesQueryFactIdIsNull() throws Exception {
        assertFalse(service.isIdNotUnique((SalesQueryFact) null));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenIdInSalesQueryFactIdIsNull() throws Exception {
        SalesQueryFact fact = new SalesQueryFact();
        fact.setID(null);

        assertFalse(service.isIdNotUnique(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenValueInIdInSalesQueryFactIdIsNull() throws Exception {
        SalesQueryFact fact = new SalesQueryFact();
        fact.setID(new IdType(null));

        assertFalse(service.isIdNotUnique(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isIdNotUniqueWhenValueInIdInSalesQueryFactIdIsBlank() throws Exception {
        SalesQueryFact fact = new SalesQueryFact();
        fact.setID(new IdType(""));

        assertFalse(service.isIdNotUnique(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReportNotExistWithReferencedIdWhenReportExists() throws Exception {
        SalesFLUXReportDocumentFact fact = new SalesFLUXReportDocumentFact();
        fact.setIDS(Arrays.asList(new IdType("id")));

        doReturn(true).when(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_REPORT);

        boolean result = service.doesReportNotExistWithId(fact);

        assertFalse(result);
        verify(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_REPORT);
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReportNotExistWithReferencedIdWhenReportDoesNotExist() throws Exception {
        SalesFLUXReportDocumentFact fact = new SalesFLUXReportDocumentFact();
        fact.setIDS(Arrays.asList(new IdType("id")));

        doReturn(false).when(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_REPORT);

        boolean result = service.doesReportNotExistWithId(fact);

        assertTrue(result);
        verify(salesService).isIdNotUnique("id", SalesMessageIdType.SALES_REPORT);
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReportNotExistWithReferencedIdWhenFactIsNull() throws Exception {
        assertFalse(service.doesReportNotExistWithId(null));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReportNotExistWithReferencedIdWhenIdsInFactIsNull() throws Exception {
        SalesFLUXReportDocumentFact fact = new SalesFLUXReportDocumentFact();
        fact.setIDS(null);

        assertFalse(service.doesReportNotExistWithId(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReportNotExistWithReferencedIdWhenIdsInFactIsEmpty() throws Exception {
        SalesFLUXReportDocumentFact fact = new SalesFLUXReportDocumentFact();
        fact.setIDS(Lists.<IdType>newArrayList());

        assertFalse(service.doesReportNotExistWithId(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReportNotExistWithReferencedIdWhenValueInIdsInFactIsNull() throws Exception {
        SalesFLUXReportDocumentFact fact = new SalesFLUXReportDocumentFact();
        fact.setIDS(Arrays.asList(new IdType(null)));

        assertFalse(service.doesReportNotExistWithId(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void doesReportNotExistWithReferencedIdWhenValueInIdsInFactIsBlank() throws Exception {
        SalesFLUXReportDocumentFact fact = new SalesFLUXReportDocumentFact();
        fact.setIDS(Arrays.asList(new IdType("")));

        assertFalse(service.doesReportNotExistWithId(fact));
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenItIsTheSame() {
        SalesFLUXSalesReportMessageFact fluxSalesReportMessageFact = new SalesFLUXSalesReportMessageFact();
        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage();

        doReturn(fluxSalesReportMessage).when(mapper).map(fluxSalesReportMessageFact, FLUXSalesReportMessage.class);
        doReturn(true).when(salesService).isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);

        assertTrue(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessageFact));
        verify(mapper).map(fluxSalesReportMessageFact, FLUXSalesReportMessage.class);
        verify(salesService).isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);
        verifyNoMoreInteractions(mapper, salesService);
    }

    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenItIsNotTheSame() {
        SalesFLUXSalesReportMessageFact fluxSalesReportMessageFact = new SalesFLUXSalesReportMessageFact();
        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage();

        doReturn(fluxSalesReportMessage).when(mapper).map(fluxSalesReportMessageFact, FLUXSalesReportMessage.class);
        doReturn(false).when(salesService).isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);

        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessageFact));
        verify(mapper).map(fluxSalesReportMessageFact, FLUXSalesReportMessage.class);
        verify(salesService).isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);
        verifyNoMoreInteractions(mapper, salesService);
    }


    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenFactIsNull() {
        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(null));
        verifyNoMoreInteractions(mapper, salesService);
    }


    @Test
    public void isSalesQueryParameterValueNotValidWhenTypeCodeIsFlagAndValueCodeIsNotValid() throws Exception {
        //data set
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType valueCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        valueCode.setValue("INVALIDFLAG");

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType typeCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        typeCode.setValue("FLAG");

        //mock
        doReturn(false).when(mdrService).isPresentInMDRList(eq("TERRITORY"), eq("INVALIDFLAG"), Mockito.any(DateTime.class));

        //execute and verify
        assertTrue(service.isSalesQueryParameterValueNotValid(typeCode, valueCode));
        verify(mdrService).isPresentInMDRList(eq("TERRITORY"), eq("INVALIDFLAG"), Mockito.any(DateTime.class));
    }

    @Test
    public void isSalesQueryParameterValueNotValidWhenTypeCodeIsFlagAndValueCodeIsValid() throws Exception {
        //data set
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType valueCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        valueCode.setValue("BEL");
        valueCode.setListId("LOCATION");

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType typeCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        typeCode.setValue("FLAG");

        //mock
        when(mdrService.isPresentInMDRList(eq("TERRITORY"), eq("BEL"), Mockito.any(DateTime.class))).thenReturn(true);

        //execute and verify
        assertFalse(service.isSalesQueryParameterValueNotValid(typeCode, valueCode));
        verify(mdrService).isPresentInMDRList(eq("TERRITORY"), eq("BEL"), Mockito.any(DateTime.class));
    }

    @Test
    public void isSalesQueryParameterValueNotValidWhenTypeCodeIsRoleAndValueCodeIsNotValid() throws Exception {
        //data set
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType valueCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        valueCode.setValue("UNKNOWN");

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType typeCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        typeCode.setValue("ROLE");

        //mock
        doReturn(false).when(mdrService).isPresentInMDRList(eq("FLUX_SALES_QUERY_PARAM_ROLE"), eq("UNKNOWN"), Mockito.any(DateTime.class));

        //execute and verify
        assertTrue(service.isSalesQueryParameterValueNotValid(typeCode, valueCode));
        verify(mdrService).isPresentInMDRList(eq("FLUX_SALES_QUERY_PARAM_ROLE"), eq("UNKNOWN"), Mockito.any(DateTime.class));
    }

    @Test
    public void isSalesQueryParameterValueNotValidWhenTypeCodeIsRoleAndValueCodeIsValid() throws Exception {
        //data set
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType valueCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        valueCode.setValue("FLAG");

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType typeCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        typeCode.setValue("ROLE");

        //mock
        doReturn(true).when(mdrService).isPresentInMDRList(eq("FLUX_SALES_QUERY_PARAM_ROLE"), eq("FLAG"), Mockito.any(DateTime.class));

        //execute and verify
        assertFalse(service.isSalesQueryParameterValueNotValid(typeCode, valueCode));
        verify(mdrService).isPresentInMDRList(eq("FLUX_SALES_QUERY_PARAM_ROLE"), eq("FLAG"), Mockito.any(DateTime.class));
    }

    @Test
    public void isSalesQueryParameterValueNotValidWhenTypeCodeIsPlaceAndValueCodeIsNotValid() throws Exception {
        //data set
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType valueCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        valueCode.setValue("UNKNOWN");

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType typeCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        typeCode.setValue("PLACE");

        //mock
        doReturn(false).when(mdrService).isPresentInMDRList(eq("LOCATION"), eq("UNKNOWN"), Mockito.any(DateTime.class));

        //execute and verify
        assertTrue(service.isSalesQueryParameterValueNotValid(typeCode, valueCode));
        verify(mdrService).isPresentInMDRList(eq("LOCATION"), eq("UNKNOWN") , Mockito.any(DateTime.class));
    }

    @Test
    public void isSalesQueryParameterValueNotValidWhenTypeCodeIsPlaceAndValueCodeIsValid() throws Exception {
        //data set
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType valueCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        valueCode.setValue("BEOST");

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType typeCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        typeCode.setValue("PLACE");

        //mock
        doReturn(true).when(mdrService).isPresentInMDRList(eq("LOCATION"), eq("BEOST"), Mockito.any(DateTime.class));

        //execute and verify
        assertFalse(service.isSalesQueryParameterValueNotValid(typeCode, valueCode));
        verify(mdrService).isPresentInMDRList(eq("LOCATION"), eq("BEOST"), Mockito.any(DateTime.class));
    }

    @Test
    public void isSalesQueryParameterValueNotValidWhenInvalidTypeCode() throws Exception {
        //data set
        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType valueCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        valueCode.setValue("BEL");

        eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType typeCode = new eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType();
        typeCode.setValue("Please trigger the default case");

        //execute and verify
        assertTrue(service.isSalesQueryParameterValueNotValid(typeCode, valueCode));
    }

    @Test
    public void isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSalesWhenCurrencyIsNull() {
        //data set
        DateTime occurrence = DateTime.now();

        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setCurrencyCode(null);
        fact.setSpecifiedFLUXLocations(Arrays.asList(new FLUXLocationType().withCountryID(new IDType().withValue("BEL"))));
        fact.setSpecifiedSalesEvents(Arrays.asList(new SalesEventType().withOccurrenceDateTime(new DateTimeType().withDateTime(occurrence))));

        //execute and verify
        assertFalse(service.isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSales(fact));
        verifyNoMoreInteractions(mdrService);
    }

    @Test
    public void isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSalesWhenCountryIsNull() {
        //data set
        DateTime occurrence = DateTime.now();

        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setCurrencyCode(new CodeType("EUR"));
        fact.setSpecifiedSalesEvents(Arrays.asList(new SalesEventType().withOccurrenceDateTime(new DateTimeType().withDateTime(occurrence))));

        //execute and verify
        assertFalse(service.isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSales(fact));
        verifyNoMoreInteractions(mdrService);
    }

    @Test
    public void isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSalesWhenOccurrenceIsNull() {
        //data set
        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setCurrencyCode(new CodeType("EUR"));
        fact.setSpecifiedFLUXLocations(Arrays.asList(new FLUXLocationType().withCountryID(new IDType().withValue("BEL"))));
        fact.setSpecifiedSalesEvents(Arrays.asList(new SalesEventType().withOccurrenceDateTime(null)));

        //execute and verify
        assertFalse(service.isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSales(fact));
        verifyNoMoreInteractions(mdrService);
    }

    @Test
    public void isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSalesWhenTrue() {
        //data set
        DateTime occurrence = new DateTime(2018, 4, 3, 7, 0, 0);
        DateTime creationDate = new DateTime(2018, 4, 3, 10, 5, 3);

        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setCurrencyCode(new CodeType("EUR"));
        fact.setSpecifiedFLUXLocations(Arrays.asList(new FLUXLocationType().withCountryID(new IDType().withValue("BEL"))));
        fact.setSpecifiedSalesEvents(Arrays.asList(new SalesEventType().withOccurrenceDateTime(new DateTimeType().withDateTime(occurrence))));
        fact.setCreationDateOfMessage(creationDate);

        //mock
        doReturn(getTestEntriesForMDRListTERRITORY_CURRENCY()).when(mdrService).getObjectRepresentationList(MDRAcronymType.TERRITORY_CURR);

        //execute and verify
        assertTrue(service.isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSales(fact));
        verify(mdrService).getObjectRepresentationList(MDRAcronymType.TERRITORY_CURR);
    }

    @Test
    public void isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSalesWhenItWasNeverTheUsedCurrency() {
        //data set
        DateTime occurrence = new DateTime(2018, 4, 3, 7, 0, 0);
        DateTime creationDate = new DateTime(2018, 4, 3, 10, 5, 3);

        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setCurrencyCode(new CodeType("DKK"));
        fact.setSpecifiedFLUXLocations(Arrays.asList(new FLUXLocationType().withCountryID(new IDType().withValue("BEL"))));
        fact.setSpecifiedSalesEvents(Arrays.asList(new SalesEventType().withOccurrenceDateTime(new DateTimeType().withDateTime(occurrence))));
        fact.setCreationDateOfMessage(creationDate);

        //mock
        doReturn(getTestEntriesForMDRListTERRITORY_CURRENCY()).when(mdrService).getObjectRepresentationList(MDRAcronymType.TERRITORY_CURR);

        //execute and verify
        assertFalse(service.isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSales(fact));
        verify(mdrService).getObjectRepresentationList(MDRAcronymType.TERRITORY_CURR);
    }

    @Test
    public void isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSalesWhenItIsNoLongerTheUsedCurrency() {
        //data set
        DateTime occurrence = new DateTime(2020, 4, 3, 7, 0, 0);
        DateTime creationDate = new DateTime(2020, 4, 3, 10, 5, 3);

        SalesDocumentFact fact = new SalesDocumentFact();
        fact.setCurrencyCode(new CodeType("DKK"));
        fact.setSpecifiedFLUXLocations(Arrays.asList(new FLUXLocationType().withCountryID(new IDType().withValue("BEL"))));
        fact.setSpecifiedSalesEvents(Arrays.asList(new SalesEventType().withOccurrenceDateTime(new DateTimeType().withDateTime(occurrence))));
        fact.setCreationDateOfMessage(creationDate);

        //mock
        doReturn(getTestEntriesForMDRListTERRITORY_CURRENCY()).when(mdrService).getObjectRepresentationList(MDRAcronymType.TERRITORY_CURR);

        //execute and verify
        assertFalse(service.isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSales(fact));
        verify(mdrService).getObjectRepresentationList(MDRAcronymType.TERRITORY_CURR);
    }

    @Test
    public void isOriginalAndIsIdNotUniqueWhenReportIsOriginalAndIsUnique() {
        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();

        FLUXReportDocumentType fluxReportDocument = new FLUXReportDocumentType();
        fluxReportDocument.setPurposeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("9"));

        SalesDocumentType salesDocumentType = new SalesDocumentType();
        salesDocumentType.getIDS().add(new IDType().withSchemeID("UUID").withValue("MY UUID"));

        SalesReportType salesReportType = new SalesReportType();
        salesReportType.getIncludedSalesDocuments().add(salesDocumentType);

        fact.setFLUXReportDocument(fluxReportDocument);
        fact.setSalesReports(Arrays.asList(salesReportType));

        doReturn(false).when(salesService).isIdNotUnique("MY UUID", SalesMessageIdType.SALES_DOCUMENT);

        boolean result = service.isOriginalAndIsIdNotUnique(fact);

        assertFalse(result);
        verify(salesService).isIdNotUnique("MY UUID", SalesMessageIdType.SALES_DOCUMENT);
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void isOriginalAndIsIdNotUniqueWhenReportIsOriginalAndIsNotUnique() {
        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();

        FLUXReportDocumentType fluxReportDocument = new FLUXReportDocumentType();
        fluxReportDocument.setPurposeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("9"));

        SalesDocumentType salesDocumentType = new SalesDocumentType();
        salesDocumentType.getIDS().add(new IDType().withSchemeID("UUID").withValue("MY UUID"));

        SalesReportType salesReportType = new SalesReportType();
        salesReportType.getIncludedSalesDocuments().add(salesDocumentType);

        fact.setFLUXReportDocument(fluxReportDocument);
        fact.setSalesReports(Arrays.asList(salesReportType));

        doReturn(true).when(salesService).isIdNotUnique("MY UUID", SalesMessageIdType.SALES_DOCUMENT);

        boolean result = service.isOriginalAndIsIdNotUnique(fact);

        assertTrue(result);

        verify(salesService).isIdNotUnique("MY UUID", SalesMessageIdType.SALES_DOCUMENT);
        verifyNoMoreInteractions(salesService);
    }


    @Test
    public void isOriginalAndIsIdNotUniqueWhenReportIsNotOriginal() {
        SalesFLUXSalesReportMessageFact fact = new SalesFLUXSalesReportMessageFact();

        FLUXReportDocumentType fluxReportDocument = new FLUXReportDocumentType();
        fluxReportDocument.setPurposeCode(new eu.europa.ec.fisheries.schema.sales.CodeType().withValue("5"));

        SalesDocumentType salesDocumentType = new SalesDocumentType();
        salesDocumentType.getIDS().add(new IDType().withSchemeID("UUID").withValue("MY UUID"));

        SalesReportType salesReportType = new SalesReportType();
        salesReportType.getIncludedSalesDocuments().add(salesDocumentType);

        fact.setFLUXReportDocument(fluxReportDocument);
        fact.setSalesReports(Arrays.asList(salesReportType));

        boolean result = service.isOriginalAndIsIdNotUnique(fact);

        assertFalse(result);

        verifyNoMoreInteractions(salesService);
    }

    private List<ObjectRepresentation> getTestEntriesForMDRListTERRITORY_CURRENCY() {
        ColumnDataType code = new ColumnDataType();
        code.setColumnName("code");
        code.setColumnValue("EUR");

        ColumnDataType placesCode = new ColumnDataType();
        placesCode.setColumnName("placesCode");
        placesCode.setColumnValue("BEL");

        ColumnDataType startDate = new ColumnDataType();
        startDate.setColumnName("startDate");
        startDate.setColumnValue(new DateTime(2018, 1, 1, 0, 0,0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS"));

        ColumnDataType endDate = new ColumnDataType();
        endDate.setColumnName("endDate");
        endDate.setColumnValue(new DateTime(2019, 1, 1, 0, 0,0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS"));

        ObjectRepresentation objectRepresentation = new ObjectRepresentation();
        objectRepresentation.setFields(Arrays.asList(code, placesCode, startDate, endDate));

        return Arrays.asList(objectRepresentation);
    }
}
