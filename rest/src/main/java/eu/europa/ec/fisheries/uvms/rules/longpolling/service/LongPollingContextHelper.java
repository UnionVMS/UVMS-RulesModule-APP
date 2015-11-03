package eu.europa.ec.fisheries.uvms.rules.longpolling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.servlet.AsyncContext;

@Singleton
public class LongPollingContextHelper {

    private Map<String, List<AsyncContext>> asyncContexts = new HashMap<>();

    /**
     * Adds an async context, associated with the given path.
     * 
     * @param ctx an asynchronous context
     * @param longPollingPath a long-polling path
     */
    public synchronized void add(AsyncContext ctx, String longPollingPath) {
        List<AsyncContext> ctxs = asyncContexts.get(longPollingPath);
        if (ctxs == null) {
            ctxs = new ArrayList<>();
            asyncContexts.put(longPollingPath, ctxs);
        }

        ctxs.add(ctx);
    }

    /**
     * Removes and returns the first async context, for a path.
     * 
     * @param longPollingPath a path
     * @return the first context for this path, or null if none exist
     */
    public synchronized AsyncContext popContext(String longPollingPath) {
        List<AsyncContext> ctxs = asyncContexts.get(longPollingPath);
        if (ctxs == null || ctxs.isEmpty()) {
            return null;
        }

        return ctxs.remove(0);
    }

    public synchronized void remove(AsyncContext ctx) {
        for (List<AsyncContext> ctxs : asyncContexts.values()) {
            if (ctxs.contains(ctx)) {
                ctxs.remove(ctx);
                break;
            }
        }
    }

}
