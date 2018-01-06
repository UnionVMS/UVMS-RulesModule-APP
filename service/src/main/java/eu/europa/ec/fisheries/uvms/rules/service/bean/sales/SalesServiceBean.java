package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isBlank;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.SalesMessageIdType;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.service.SalesService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.SalesServiceBeanHelper;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class SalesServiceBean implements SalesService {

    @EJB
    SalesServiceBeanHelper helper;

    @Override
    public boolean isCorrectionAndIsItemTypeTheSameAsInTheOriginal(FLUXSalesReportMessage correctedReport) {
        // The report is not a correction, return false so the rule doesn't fire
        if (correctedReport == null ||
                correctedReport.getFLUXReportDocument() == null ||
                correctedReport.getFLUXReportDocument().getPurposeCode() == null ||
                correctedReport.getFLUXReportDocument().getPurposeCode().getValue() == null ||
                !correctedReport.getFLUXReportDocument().getPurposeCode().getValue().equals("5")) {
            return false;
        }

        // Don't fire when referenced id is not provided
        if (correctedReport.getFLUXReportDocument().getReferencedID() == null ||
                isBlank(correctedReport.getFLUXReportDocument().getReferencedID().getValue())) {
            return false;
        }

        try {
            Optional<FLUXSalesReportMessage> originalReport = helper.findReport(correctedReport.getFLUXReportDocument().getReferencedID().getValue());
            return originalReport.isPresent() && isTypeCodeBetweenReportsNotEqual(originalReport.get(), correctedReport);
        } catch (SalesMarshallException | JMSException | eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException e) {
            throw new RulesServiceException("Something went wrong while sending/receiving of a sales request in isCorrectionAndIsItemTypeTheSameAsInTheOriginal in SalesServiceBean", e);
        }
    }

    @Override
    public boolean doesReportExistWithId(String id) {
        try {
            Optional<FLUXSalesReportMessage> optionalReport = helper.findReport(id);
            return optionalReport.isPresent();
        } catch (MessageException | SalesMarshallException | JMSException e) {
            throw new RulesServiceException("Something went wrong while sending/receiving of a sales request in doesReportExistWithId in SalesServiceBean", e);
        }
    }


    @Override
    public boolean isIdNotUnique(String id, SalesMessageIdType type) {
        if (isBlank(id) || type == null) {
            throw new NullPointerException("Null received in isIdNotUnique. Sanitize your inputs");
        }

        return areAnyOfTheseIdsNotUnique(Arrays.asList(id), type);
    }

    @Override
    public boolean areAnyOfTheseIdsNotUnique(List<String> ids, SalesMessageIdType type) {
        if (isEmpty(ids) || type == null) {
            throw new NullPointerException("Null received in areAnyOfTheseIdsNotUnique. Sanitize your inputs");
        }

        try {
            return helper.areAnyOfTheseIdsNotUnique(ids, type);
        } catch (MessageException | JMSException | SalesMarshallException e) {
            throw new RulesServiceException("Something went wrong while sending/receiving of a sales request in areAnyOfTheseIdsNotUnique in SalesServiceBean", e);
        }
    }

    protected boolean isTypeCodeBetweenReportsNotEqual(FLUXSalesReportMessage originalReport, FLUXSalesReportMessage correctedReport) {
        if (originalReport == null || originalReport.getSalesReports() == null || isEmpty(originalReport.getSalesReports()) ||
                originalReport.getSalesReports().get(0) == null || originalReport.getSalesReports().get(0).getItemTypeCode() == null ||
                isBlank(originalReport.getSalesReports().get(0).getItemTypeCode().getValue())) {
            return false;
        }
        
        if (correctedReport == null || correctedReport.getSalesReports() == null || isEmpty(correctedReport.getSalesReports()) ||
                correctedReport.getSalesReports().get(0) == null || correctedReport.getSalesReports().get(0).getItemTypeCode() == null ||
                isBlank(correctedReport.getSalesReports().get(0).getItemTypeCode().getValue())) {
            return false;
        }

        String typeCodeOfCorrection = correctedReport.getSalesReports().get(0).getItemTypeCode().getValue();
        String typeCodeOfOriginal = originalReport.getSalesReports().get(0).getItemTypeCode().getValue();

        return !typeCodeOfOriginal.equals(typeCodeOfCorrection);
    }
}
