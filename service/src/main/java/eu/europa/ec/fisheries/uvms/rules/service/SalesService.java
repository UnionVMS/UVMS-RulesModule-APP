package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;

import javax.ejb.Local;

@Local
public interface SalesService {

    boolean isCorrectionAndIsItemTypeTheSameAsInTheOriginal(FLUXSalesReportMessage fluxSalesReportMessage);

    boolean doesReportExistWithId(String id);

}
