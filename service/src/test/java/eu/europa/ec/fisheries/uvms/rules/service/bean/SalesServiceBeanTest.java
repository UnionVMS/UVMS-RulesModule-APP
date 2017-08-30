package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.SalesServiceBeanHelper;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.SalesModuleRequestMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JAXBMarshaller.class, SalesModuleRequestMapper.class})
@PowerMockIgnore( {"javax.management.*"})
public class SalesServiceBeanTest {

    @InjectMocks
    SalesServiceBean service;

    @Mock
    SalesServiceBeanHelper helper;


    @Test
    public void isCorrectionAndIsItemTypeTheSameAsInTheOriginalWhenReportIsCorrectionAndItemTypeIsEqual() throws Exception {
        FLUXReportDocumentType fluxReportDocumentType = new FLUXReportDocumentType()
                .withPurposeCode(new CodeType().withValue("5"))
                .withReferencedID(new IDType().withValue("abc-123"));

        SalesReportType salesReportType = new SalesReportType().withItemTypeCode(new CodeType().withValue("SN"));

        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(fluxReportDocumentType)
                .withSalesReports(salesReportType);

        doReturn(fluxSalesReportMessage).when(helper).findReport(fluxSalesReportMessage.getFLUXReportDocument().getReferencedID().getValue());

        boolean result = service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);
        assertTrue(result);
    }

    @Test
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

        doReturn(fluxSalesReportMessageOriginal).when(helper).findReport(fluxSalesReportMessage.getFLUXReportDocument().getReferencedID().getValue());

        boolean result = service.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);
        assertFalse(result);
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

}