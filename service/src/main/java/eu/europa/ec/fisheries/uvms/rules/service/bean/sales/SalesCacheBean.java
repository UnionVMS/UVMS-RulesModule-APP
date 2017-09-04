package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;

import javax.ejb.Singleton;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

@Singleton
public class SalesCacheBean implements SalesCache {

    private Cache<String, FLUXSalesReportMessage> cache;

    public SalesCacheBean() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build();
    }

    @Override
    public Optional<FLUXSalesReportMessage> retrieveMessageFromCache(String messageGuid) {
        checkArgument(!isNullOrEmpty(messageGuid),"You're trying to retrieve a message with a null/blank guid");

        return Optional.fromNullable(cache.getIfPresent(messageGuid));
    }

    @Override
    public Boolean isMessageCached(String messageGuid) {
        checkArgument(!isNullOrEmpty(messageGuid),"You're trying to retrieve a message with a null/blank guid");

        return Optional.fromNullable(cache.getIfPresent(messageGuid)).isPresent();
    }

    @Override
    public void cacheMessage(String guid, FLUXSalesReportMessage report) {
        checkArgument(!isNullOrEmpty(guid), "You're trying to add a null or empty key to the SalesCache");
        // Allow null reports here, so when a report doesn't exist in Sales, it's still cached and doesn't use ActiveMQ.

        cache.put(guid, report);
    }
}
