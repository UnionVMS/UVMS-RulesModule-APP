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

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.longpolling.constants.LongPollingConstants;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketUpdateEvent;

@WebServlet(asyncSupported = true, urlPatterns = { LongPollingConstants.ALARM_REPORT_PATH, LongPollingConstants.TICKET_UPDATE_PATH, LongPollingConstants.TICKET_COUNT_PATH, LongPollingConstants.ALARM_REPORT_COUNT_PATH })
public class LongPollingHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    LongPollingContextHelper asyncContexts;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext ctx = req.startAsync(req, resp);
        ctx.setTimeout(LongPollingConstants.ASYNC_TIMEOUT);
        ctx.addListener(new LongPollingAsyncListener() {

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                AsyncContext ctx = event.getAsyncContext();
                asyncContexts.remove(ctx);
                completePoll(ctx, createJsonMessage(null));
            }

        });

        asyncContexts.add(ctx, req.getServletPath());
    }

    public void observeAlarmCreated(@Observes @AlarmReportEvent NotificationMessage message) throws IOException {
        String guid = (String) message.getProperties().get(LongPollingConstants.PROPERTY_GUID);
        completePoll(LongPollingConstants.ALARM_REPORT_PATH, createJsonMessage(guid));
    }

    public void observeTicketUpdate(@Observes @TicketEvent NotificationMessage message) throws IOException {
        String guid = (String) message.getProperties().get(LongPollingConstants.PROPERTY_GUID);
        completePoll(LongPollingConstants.TICKET_UPDATE_PATH, createJsonMessage(guid, LongPollingConstants.ACTION_CREATED));
    }

    public void observeTicketUpdateEvent(@Observes @TicketUpdateEvent NotificationMessage message) throws IOException {
        String guid = (String) message.getProperties().get(LongPollingConstants.PROPERTY_GUID);
        completePoll(LongPollingConstants.TICKET_UPDATE_PATH, createJsonMessage(guid, LongPollingConstants.ACTION_UPDATED));
    }

    public void observeTicketCount(@Observes @AlarmReportCountEvent NotificationMessage message) throws IOException {
        completePoll(LongPollingConstants.ALARM_REPORT_COUNT_PATH, createJsonMessageCount(true));
    }

    public void observeAlarmReportCount(@Observes @TicketCountEvent NotificationMessage message) throws IOException {
        completePoll(LongPollingConstants.TICKET_COUNT_PATH, createJsonMessageCount(true));
    }

    protected String createJsonMessage(String guid) {
        return createJsonMessage(guid, null);
    }

    protected String createJsonMessage(String guid, String action) {
        JsonArrayBuilder ids = Json.createArrayBuilder();
        if (guid != null) {
            ids.add(guid);
        }

        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add(LongPollingConstants.PROPERTY_IDS, ids);

        if (action != null) {
            message = Json.createObjectBuilder().add(action, message);
        }

        return message.build().toString();
    }

    protected String createJsonMessageCount(boolean value) {
        return Json.createObjectBuilder().add(LongPollingConstants.ACTION_UPDATED, value).build().toString();
    }

    protected void completePoll(String resourcePath, String message) throws IOException {
        AsyncContext ctx = null;
        while ((ctx = asyncContexts.popContext(resourcePath)) != null) {
            completePoll(ctx, message);
        }
    }

    protected void completePoll(AsyncContext ctx, String jsonMessage) throws IOException {
        ctx.getResponse().setContentType(LongPollingConstants.CONTENT_TYPE_APPLICATION_JSON);
        ctx.getResponse().getWriter().write(jsonMessage);
        ctx.complete();
    }

    private abstract static class LongPollingAsyncListener implements AsyncListener {

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
            // Do nothing
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
            // Do nothing
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {
            // Do nothing
        }

    }

}