package eu.europa.ec.fisheries.uvms.rules.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.rules.rest.service.AlarmRestResource;
import eu.europa.ec.fisheries.uvms.rules.rest.service.ConfigResource;
import eu.europa.ec.fisheries.uvms.rules.rest.service.RulesRestResource;
import eu.europa.ec.fisheries.uvms.rules.rest.service.TicketRestResource;

@ApplicationPath(RestConstants.MODULE_REST)
public class RestActivator extends Application {

    final static Logger LOG = LoggerFactory.getLogger(RestActivator.class);

    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> set = new HashSet<>();

    public RestActivator() {
        set.add(RulesRestResource.class);
        set.add(AlarmRestResource.class);
        set.add(TicketRestResource.class);
        set.add(ConfigResource.class);
        LOG.info(RestConstants.MODULE_NAME + " module starting up");
    }

    @Override
    public Set<Class<?>> getClasses() {
        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
