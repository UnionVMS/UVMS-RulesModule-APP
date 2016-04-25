package eu.europa.ec.fisheries.uvms.rules.longpolling.service;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class TestLongPollingHttpServlet {

    LongPollingHttpServlet longPolling;
    
    @Before
    public void setup() {
        longPolling = new LongPollingHttpServlet();
    }

    @Test
    public void testNoAction() {
        String jsonMessage = longPolling.createJsonMessage("abc123");
        Assert.assertEquals("{\"ids\":[\"abc123\"]}", jsonMessage);
    }

    @Test
    public void testNoGuid() {
        String jsonMessage = longPolling.createJsonMessage(null);
        Assert.assertEquals("{\"ids\":[]}", jsonMessage);
    }

    @Test
    public void testUpdated() {
        String jsonMessage = longPolling.createJsonMessage("abc123", "updated");
        Assert.assertEquals("{\"updated\":{\"ids\":[\"abc123\"]}}", jsonMessage);
    }

    @Test
    public void testUpdatedNoGuid() {
        String jsonMessage = longPolling.createJsonMessage(null, "updated");
        Assert.assertEquals("{\"updated\":{\"ids\":[]}}", jsonMessage);
    }
    
    @Test
    public void testCountTrue() {
        String jsonMessage = longPolling.createJsonMessageCount(true);
        Assert.assertEquals("{\"updated\":true}", jsonMessage);
    }

    @Test
    public void testCountFalse() {
        String jsonMessage = longPolling.createJsonMessageCount(false);
        Assert.assertEquals("{\"updated\":false}", jsonMessage);
    }

}
