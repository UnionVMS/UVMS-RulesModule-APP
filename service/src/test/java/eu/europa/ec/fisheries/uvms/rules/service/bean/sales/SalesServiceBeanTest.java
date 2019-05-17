package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.SalesServiceBeanHelper;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceTechnicalException;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesServiceBeanTest {

    @InjectMocks
    SalesServiceBean service;

    @Mock
    SalesServiceBeanHelper helper;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    @Ignore
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenReportIsCorrectionAndItemTypeIsEqual() throws Exception {
        FLUXReportDocumentType fluxReportDocumentType = new FLUXReportDocumentType()
                .withPurposeCode(new CodeType().withValue("5"))
                .withReferencedID(new IDType().withValue("abc-123"));

        SalesReportType salesReportType = new SalesReportType().withItemTypeCode(new CodeType().withValue("SN"));

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocumentType)
                .withSalesReports(salesReportType);

        Optional<FLUXSalesReportMessage> fluxSalesReportMessageOptional = Optional.of(fluxSalesReportMessage);

        doReturn(fluxSalesReportMessageOptional).when(helper).findReport(fluxSalesReportMessage.getFLUXReportDocument().getReferencedID().getValue());

        boolean result = service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);
        assertFalse(result);
    }

    @Test
    @Ignore
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenReportIsCorrectionAndItemTypeIsNotEqual() throws Exception {
        FLUXReportDocumentType fluxReportDocumentType = new FLUXReportDocumentType()
                .withPurposeCode(new CodeType().withValue("5"))
                .withReferencedID(new IDType().withValue("abc-123"));

        SalesReportType salesReportType = new SalesReportType().withItemTypeCode(new CodeType().withValue("SN"));
        SalesReportType salesReportTypeOriginal = new SalesReportType().withItemTypeCode(new CodeType().withValue("TOD"));

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocumentType)
                .withSalesReports(salesReportType);
        FLUXSalesReportMessage fluxSalesReportMessageOriginal = new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocumentType)
                .withSalesReports(salesReportTypeOriginal);

        Optional<FLUXSalesReportMessage> fluxSalesReportMessageOptional = Optional.of(fluxSalesReportMessageOriginal);
        doReturn(fluxSalesReportMessageOptional).when(helper).findReport(fluxSalesReportMessage.getFLUXReportDocument().getReferencedID().getValue());

        boolean result = service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);
        assertTrue(result);
    }

    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenReportIsNotACorrection() throws Exception {
        FLUXReportDocumentType fluxReportDocumentType = new FLUXReportDocumentType()
                .withPurposeCode(new CodeType().withValue("9"))
                .withReferencedID(new IDType().withValue("abc-123"));

        SalesReportType salesReportType = new SalesReportType().withItemTypeCode(new CodeType().withValue("SN"));

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocumentType)
                .withSalesReports(salesReportType);

        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage));
    }


    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenReportIsNull() throws Exception {
        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(null));
    }

    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenFLUXReportDocumentInReportIsNull() throws Exception {
        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(new FLUXSalesReportMessage().withFLUXReportDocument(null)));
    }


    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenPurposeCodeInFLUXReportDocumentInReportIsNull() throws Exception {
        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(new FLUXSalesReportMessage().withFLUXReportDocument(new FLUXReportDocumentType().withPurposeCode(null))));
    }

    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenValueInPurposeCodeInFLUXReportDocumentInReportIsNull() throws Exception {
        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(new FLUXSalesReportMessage().withFLUXReportDocument(new FLUXReportDocumentType().withPurposeCode(new CodeType().withValue(null)))));
    }

    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenReportIsCorrectionAndReferencedIdIsNull() throws Exception {
        FLUXReportDocumentType fluxReportDocumentType = new FLUXReportDocumentType()
                .withPurposeCode(new CodeType().withValue("5"));

        SalesReportType salesReportType = new SalesReportType().withItemTypeCode(new CodeType().withValue("SN"));

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocumentType)
                .withSalesReports(salesReportType);

        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage));
    }

    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenReportIsCorrectionAndReferencedIdValueIsNull() throws Exception {
        FLUXReportDocumentType fluxReportDocumentType = new FLUXReportDocumentType()
                .withPurposeCode(new CodeType().withValue("5"))
                .withReferencedID(new IDType());

        SalesReportType salesReportType = new SalesReportType().withItemTypeCode(new CodeType().withValue("SN"));

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocumentType)
                .withSalesReports(salesReportType);

        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage));
    }

    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenReportIsCorrectionAndReferencedIdValueIsBlank() throws Exception {
        FLUXReportDocumentType fluxReportDocumentType = new FLUXReportDocumentType()
                .withPurposeCode(new CodeType().withValue("5"))
                .withReferencedID(new IDType().withValue(""));

        SalesReportType salesReportType = new SalesReportType().withItemTypeCode(new CodeType().withValue("SN"));

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocumentType)
                .withSalesReports(salesReportType);

        assertFalse(service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenNotEqual() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("TOD")));
        assertTrue(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenEqual() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenOriginalReportIsNull() throws Exception {
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(null, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenSalesReportsInOriginalReportIsEmpty() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(Lists.<SalesReportType>newArrayList());
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenItemTypeCodeInSalesReportsInOriginalReportIsNull() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(null));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenValueItemTypeCodeInSalesReportsInOriginalReportIsNull() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue(null)));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenValueItemTypeCodeInSalesReportsInOriginalReportIsBlank() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue(null)));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenCorrectedReportIsNull() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, null));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenSalesReportsInCorrectedReportIsEmpty() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(Lists.<SalesReportType>newArrayList());
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenItemTypeCodeInSalesReportsInCorrectedReportIsNull() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(null));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenValueItemTypeCodeInSalesReportsInCorrectedReportIsNull() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue(null)));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void isTypeCodeBetweenReportsNotEqualWhenValueItemTypeCodeInSalesReportsInCorrectedReportIsBlank() throws Exception {
        FLUXSalesReportMessage originalReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue("SN")));
        FLUXSalesReportMessage correctedReport = new FLUXSalesReportMessage()
                .withSalesReports(new SalesReportType().withItemTypeCode(new CodeType().withValue(null)));
        assertFalse(service.isTypeCodeBetweenReportsNotEqual(originalReport, correctedReport));
    }

    @Test
    public void areAnyOfTheseIdsNotUniqueWhenNotUnique() throws Exception {
        List<String> ids = Arrays.asList("abc");

        doReturn(true).when(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);

        assertTrue(service.areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY));
        verify(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
    }

    @Test
    public void areAnyOfTheseIdsNotUniqueWhenUnique() throws Exception {
        List<String> ids = Arrays.asList("abc");

        doReturn(false).when(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);

        assertFalse(service.areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY));
        verify(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
    }

    @Test
    public void areAnyOfTheseIdsNotUniqueWhenMessageExceptionWasThrown() throws Exception {
        List<String> ids = Arrays.asList("abc");

        doThrow(MessageException.class).when(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
        exception.expect(RulesServiceTechnicalException.class);
        exception.expectMessage("Something went wrong while sending/receiving of a sales request in areAnyOfTheseIdsNotUnique in SalesServiceBean");

        service.areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
        verify(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
    }

    @Test
    public void areAnyOfTheseIdsNotUniqueWhenJMSExceptionWasThrown() throws Exception {
        List<String> ids = Arrays.asList("abc");

        doThrow(JMSException.class).when(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
        exception.expect(RulesServiceTechnicalException.class);
        exception.expectMessage("Something went wrong while sending/receiving of a sales request in areAnyOfTheseIdsNotUnique in SalesServiceBean");

        service.areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
        verify(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
    }

    @Test
    public void areAnyOfTheseIdsNotUniqueWhenSalesMarshallExceptionWasThrown() throws Exception {
        List<String> ids = Arrays.asList("abc");

        doThrow(SalesMarshallException.class).when(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
        exception.expect(RulesServiceTechnicalException.class);
        exception.expectMessage("Something went wrong while sending/receiving of a sales request in areAnyOfTheseIdsNotUnique in SalesServiceBean");

        service.areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
        verify(helper).areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
    }

    @Test
    public void areAnyOfTheseIdsNotUniqueWhenListIsEmpty() throws Exception {
        List<String> ids = Lists.newArrayList();

        exception.expect(NullPointerException.class);
        exception.expectMessage("Null received in areAnyOfTheseIdsNotUnique. Sanitize your inputs");

        service.areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
    }

    @Test
    public void areAnyOfTheseIdsNotUniqueWhenListIsNull() throws Exception {
        List<String> ids = null;

        exception.expect(NullPointerException.class);
        exception.expectMessage("Null received in areAnyOfTheseIdsNotUnique. Sanitize your inputs");

        service.areAnyOfTheseIdsNotUnique(ids, SalesMessageIdType.SALES_QUERY);
    }

    @Test
    public void areAnyOfTheseIdsNotUniqueWhenTypeIsNull() throws Exception {
        List<String> ids = Arrays.asList("abc");

        exception.expect(NullPointerException.class);
        exception.expectMessage("Null received in areAnyOfTheseIdsNotUnique. Sanitize your inputs");

        service.areAnyOfTheseIdsNotUnique(ids, null);
    }



    @Test
    public void isIdNotUniqueWhenIdIsBlank() throws Exception {
        exception.expect(NullPointerException.class);
        exception.expectMessage("Null received in isIdNotUnique. Sanitize your inputs");

        service.isIdNotUnique("", SalesMessageIdType.SALES_QUERY);
    }

    @Test
    public void isIdNotUniqueWhenTypeIsNull() throws Exception {
        exception.expect(NullPointerException.class);
        exception.expectMessage("Null received in isIdNotUnique. Sanitize your inputs");

        service.isIdNotUnique("id", null);
    }

}