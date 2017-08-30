package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;

import javax.ejb.Local;

@Local
public interface SalesCache {

    Optional<FLUXSalesReportMessage> retrieveMessageFromCache(String messageGuid);
    Boolean isMessageCached(String messageGuid);

    void cacheMessage(String guid, FLUXSalesReportMessage originalReport);
}
