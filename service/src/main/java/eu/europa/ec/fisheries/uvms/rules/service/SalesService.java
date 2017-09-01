package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.schema.sales.UniqueIDType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesDocumentFact;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SalesService {

    boolean isCorrectionAndIsItemTypeTheSameAsInTheOriginal(FLUXSalesReportMessage fluxSalesReportMessage);

    boolean doesReportExistWithId(String id);

    boolean isIdNotUnique(String id, UniqueIDType type);

    boolean areAnyOfTheseIdsNotUnique(List<String> ids, UniqueIDType type);
}
