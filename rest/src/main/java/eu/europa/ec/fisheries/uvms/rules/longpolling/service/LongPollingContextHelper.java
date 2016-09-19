/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.longpolling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.*;
import javax.servlet.AsyncContext;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class LongPollingContextHelper {

    private Map<String, List<AsyncContext>> asyncContexts = new HashMap<>();

    /**
     * Adds an async context, associated with the given path.
     * 
     * @param ctx an asynchronous context
     * @param longPollingPath a long-polling path
     */
    @Lock(LockType.WRITE)
    public void add(AsyncContext ctx, String longPollingPath) {
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
    @Lock(LockType.WRITE)
    public AsyncContext popContext(String longPollingPath) {
        List<AsyncContext> ctxs = asyncContexts.get(longPollingPath);
        if (ctxs == null || ctxs.isEmpty()) {
            return null;
        }

        return ctxs.remove(0);
    }

    @Lock(LockType.WRITE)
    public void remove(AsyncContext ctx) {
        for (List<AsyncContext> ctxs : asyncContexts.values()) {
            if (ctxs.contains(ctx)) {
                ctxs.remove(ctx);
                break;
            }
        }
    }

}