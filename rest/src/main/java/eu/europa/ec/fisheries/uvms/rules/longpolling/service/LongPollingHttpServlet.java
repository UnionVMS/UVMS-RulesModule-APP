package eu.europa.ec.fisheries.uvms.rules.longpolling.service;

import java.io.IOException;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.europa.ec.fisheries.uvms.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.rules.longpolling.constants.LongPollingConstants;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketEvent;

@WebServlet(asyncSupported = true, urlPatterns = { LongPollingConstants.ALARM_REPORT_PATH, LongPollingConstants.TICKET_UPDATE_PATH, LongPollingConstants.TICKET_COUNT_PATH, LongPollingConstants.ALARM_REPORT_COUNT_PATH })
public class LongPollingHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
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
        String guid = (String) message.getProperties().get("guid");
        completePoll(LongPollingConstants.ALARM_REPORT_PATH, createJsonMessage(guid));
    }

    public void observeTicketUpdate(@Observes @TicketEvent NotificationMessage message) throws IOException {
        String guid = (String) message.getProperties().get("guid");
        completePoll(LongPollingConstants.TICKET_UPDATE_PATH, createJsonMessage(guid));
    }

    public void observeTicketCount(@Observes @AlarmReportCountEvent NotificationMessage message) throws IOException {
        completePoll(LongPollingConstants.ALARM_REPORT_COUNT_PATH, createJsonMessageCount(true));
    }

    public void observeAlarmReportCount(@Observes @TicketCountEvent NotificationMessage message) throws IOException {
        completePoll(LongPollingConstants.TICKET_COUNT_PATH, createJsonMessageCount(true));
    }

    private String createJsonMessage(String guid) {
        JsonArrayBuilder array = Json.createArrayBuilder();
        if (guid != null) {
            array.add(guid);
        }

        return Json.createObjectBuilder().add("ids", array).build().toString();
    }

    private String createJsonMessageCount(boolean value) {
        return Json.createObjectBuilder().add("updated", value).build().toString();
    }

    private void completePoll(String resourcePath, String message) throws IOException {
        AsyncContext ctx = null;
        while ((ctx = asyncContexts.popContext(resourcePath)) != null) {
            completePoll(ctx, message);
        }
    }

    private void completePoll(AsyncContext ctx, String jsonMessage) throws IOException {
        ctx.getResponse().setContentType("application/json");
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
