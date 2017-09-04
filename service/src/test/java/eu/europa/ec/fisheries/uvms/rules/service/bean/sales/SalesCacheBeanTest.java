package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.FLUXReportDocumentType;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class SalesCacheBeanTest {

    private SalesCacheBean cache;

    private FLUXSalesReportMessage fluxSalesReportMessage;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        cache = new SalesCacheBean();
        fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(new FLUXReportDocumentType().withPurposeCode(new CodeType().withValue("9")));
        cache.cacheMessage("123", fluxSalesReportMessage);
    }

    @Test
    public void retrieveMessageFromCacheWhenReportIsCached() throws Exception {
        Optional<FLUXSalesReportMessage> fluxSalesReportMessageOptional = cache.retrieveMessageFromCache("123");
        assertSame(fluxSalesReportMessage, fluxSalesReportMessageOptional.get());
    }

    @Test
    public void retrieveMessageFromCacheWhenReportIsNotCached() throws Exception {
        Optional<FLUXSalesReportMessage> fluxSalesReportMessageOptional = cache.retrieveMessageFromCache("321");

        assertFalse(fluxSalesReportMessageOptional.isPresent());
    }

    @Test
    public void retrieveMessageFromCacheWhenPassingBlankString() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You're trying to retrieve a message with a null/blank guid");
        cache.retrieveMessageFromCache("");
    }

    @Test
    public void retrieveMessageFromCacheWhenPassingNull() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You're trying to retrieve a message with a null/blank guid");
        cache.retrieveMessageFromCache(null);
    }

    @Test
    public void isMessageCachedWhenMessageIsCached() throws Exception {
        assertTrue(cache.isMessageCached( "123"));
    }

    @Test
    public void isMessageCachedWhenMessageIsNotCached() throws Exception {
        assertFalse(cache.isMessageCached( "321"));
    }

    @Test
    public void isMessageCachedWhenPassingBlankString() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You're trying to retrieve a message with a null/blank guid");
        assertFalse(cache.isMessageCached( ""));
    }

    @Test
    public void isMessageCachedWhenPassingNull() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You're trying to retrieve a message with a null/blank guid");
        assertFalse(cache.isMessageCached( null));
    }

    @Test
    public void cacheMessageWhenEverythingIsOK() throws Exception {
        FLUXSalesReportMessage report = new FLUXSalesReportMessage();
        cache.cacheMessage("456", report);

        FLUXSalesReportMessage fluxSalesReportMessageOptional = cache.retrieveMessageFromCache("456").get();
        assertSame(report, fluxSalesReportMessageOptional);
    }

    @Test
    public void cacheMessageWhenPassingBlankKey() throws Exception {
        FLUXSalesReportMessage report = new FLUXSalesReportMessage();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You're trying to add a null or empty key to the SalesCache");
        cache.cacheMessage("", report);
    }

    @Test
    public void cacheMessageWhenPassingNullKey() throws Exception {
        FLUXSalesReportMessage report = new FLUXSalesReportMessage();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You're trying to add a null or empty key to the SalesCache");
        cache.cacheMessage("", report);
    }

    @Test
    public void cacheMessageWhenPassedReportIsNull() throws Exception {
        exception.expect(NullPointerException.class);
        cache.cacheMessage("123", null);
    }

}