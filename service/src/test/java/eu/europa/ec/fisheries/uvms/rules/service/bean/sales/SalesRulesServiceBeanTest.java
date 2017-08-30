package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.SalesRulesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesRulesServiceBeanTest {

    SalesFLUXSalesReportMessageFact salesFLUXSalesReportMessageFact;

    SalesRulesService service;

    @Before
    public void setUp() throws Exception {
        salesFLUXSalesReportMessageFact = new SalesFLUXSalesReportMessageFact();
        service = new SalesRulesServiceBean();
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
                .withOccurrenceDateTime(new DateTimeType().withDateTime(now.minusHours(47).minusMinutes(59)));

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
        DelimitedPeriodType delimitedPeriodType1 = new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(1)));
        DelimitedPeriodType delimitedPeriodType2 = new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(48)));
        FishingActivityType fishingActivityType1 = new FishingActivityType()
                .withSpecifiedDelimitedPeriods(Arrays.asList(delimitedPeriodType1, delimitedPeriodType2));
        FishingActivityType fishingActivityType2 = new FishingActivityType()
                .withSpecifiedDelimitedPeriods(Arrays.asList(delimitedPeriodType2, delimitedPeriodType1));

        SalesReportType salesReportType = new SalesReportType()
                .withIncludedSalesDocuments(new SalesDocumentType().withSpecifiedFishingActivities(Arrays.asList(fishingActivityType1, fishingActivityType2)));
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(salesReportType));
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        assertFalse(service.isReceptionDate48hAfterLandingDeclaration(salesFLUXSalesReportMessageFact));
    }


    @Test
    public void isReceptionDate48hAfterLandingDeclarationWhenLandingDeclaration48hAfter() throws Exception {
        DelimitedPeriodType delimitedPeriodType1 = new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(1)));
        DelimitedPeriodType delimitedPeriodType2 = new DelimitedPeriodType()
                .withStartDateTime(new DateTimeType().withDateTime(DateTime.now().minusHours(49)));
        FishingActivityType fishingActivityType1 = new FishingActivityType()
                .withSpecifiedDelimitedPeriods(Arrays.asList(delimitedPeriodType1, delimitedPeriodType2));
        FishingActivityType fishingActivityType2 = new FishingActivityType()
                .withSpecifiedDelimitedPeriods(Arrays.asList(delimitedPeriodType2, delimitedPeriodType1));

        SalesReportType salesReportType = new SalesReportType()
                .withIncludedSalesDocuments(new SalesDocumentType().withSpecifiedFishingActivities(Arrays.asList(fishingActivityType1, fishingActivityType2)));
        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(salesReportType));
        salesFLUXSalesReportMessageFact.setFLUXReportDocument(new FLUXReportDocumentType().withCreationDateTime(new DateTimeType().withDateTime(DateTime.now())));
        assertTrue(service.isReceptionDate48hAfterLandingDeclaration(salesFLUXSalesReportMessageFact));
    }

    /**
     * isEmpty(fact.getSalesReports()) ||
     isEmpty(fact.getSalesReports().get(0).getIncludedSalesDocuments()) ||
     isEmpty(fact.getSalesReports().get(0).getIncludedSalesDocuments()
     .get(0).getSpecifiedFishingActivities()) ||
     * @throws Exception
     */
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


//
//    @Test
//    public void isReceptionDate48hAfterSaleDateWhenOccurrenceDateTimeIsNull() throws Exception {
//        SalesEventType salesEventType = new SalesEventType()
//                .withOccurrenceDateTime(null);
//
//        SalesDocumentType salesDocumentType = new SalesDocumentType()
//                .withSpecifiedSalesEvents(Arrays.asList(salesEventType));
//
//        salesFLUXSalesReportMessageFact.setSalesReports(Arrays.asList(new SalesReportType().withIncludedSalesDocuments(salesDocumentType)));
//
//        assertFalse(service.isReceptionDate48hAfterSaleDate(salesFLUXSalesReportMessageFact));
//    }

    @Test
    public void isReceptionDate48hAfterSaleDateWhenDateTimeIsNull() throws Exception {
//        SalesEventType salesEventType = new SalesEventType()
//                .withOccurrenceDateTime(new DateTimeType().withDateTime(null));
//        salesDocumentFact.setSpecifiedSalesEvents(Arrays.asList(salesEventType));
//
//        assertFalse(service.isReceptionDate48hAfterSaleDate(salesDocumentFact));
    }

}