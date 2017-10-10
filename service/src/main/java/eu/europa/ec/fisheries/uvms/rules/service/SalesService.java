package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.SalesMessageIdType;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SalesService {

    boolean isCorrectionAndIsItemTypeTheSameAsInTheOriginal(FLUXSalesReportMessage fluxSalesReportMessage);

    boolean doesReportExistWithId(String id);

    boolean isIdNotUnique(String id, SalesMessageIdType type);

    boolean areAnyOfTheseIdsNotUnique(List<String> ids, SalesMessageIdType type);
}
