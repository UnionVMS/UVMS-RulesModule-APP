package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.service.SalesService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.SalesServiceBeanHelper;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.JMSException;

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

        try {
            Optional<FLUXSalesReportMessage> originalReport = helper.findReport(correctedReport.getFLUXReportDocument().getReferencedID().getValue());
            if (originalReport.isPresent()) {
                return isTypeCodeBetweenReportsNotEqual(originalReport.get(), correctedReport);
            }

            //TODO: returns false, does this rule also return when the referencedID in the corrected report doesn't exist?
            return false;
        } catch (SalesMarshallException | JMSException | eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException e) {
            e.printStackTrace();//TODO: exception handling
        }


        return false;
    }

    private boolean isTypeCodeBetweenReportsNotEqual(FLUXSalesReportMessage originalReport, FLUXSalesReportMessage correctedReport) {
        if (originalReport == null) {
            return false;
        }

        String typeCodeOfCorrection = correctedReport.getSalesReports().get(0).getItemTypeCode().getValue();
        String typeCodeOfOriginal = originalReport.getSalesReports().get(0).getItemTypeCode().getValue();

        return !typeCodeOfOriginal.equals(typeCodeOfCorrection);
    }


    @Override
    public boolean doesReportExistWithId(String id) {
        try {
            Optional<FLUXSalesReportMessage> optionalReport = helper.findReport(id);
            if (optionalReport.isPresent()) {
                return true;
            }

            return false;
        } catch (MessageException | SalesMarshallException | JMSException e) {
            e.printStackTrace(); //TODO: exception handling
        }

        return false;
    }
}
