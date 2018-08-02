/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.rest.service.unsecured;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.rules.service.bean.mdr.MDRCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.PropertiesBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesKieContainerInitializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/")
public class HealthResource {

    private static final String MDR_CACHE_LOADED = "mdr_cache_loaded";
    private static final String RULES_LOADED = "rules_loaded";
    private static final String APPLICATION_VERSION = "application.version";
    private static final String APPLICATION_NAME = "application.name";

    @EJB
    private MDRCache mdrCache;

    @EJB
    private PropertiesBean propertiesBean;

    @EJB
    private RulesKieContainerInitializer initializer;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getHealth() {
        Response response;
        Map<String, Object> properties = new HashMap<>();
        Map<String, Map<String, Object>> map = new HashMap<>();

        boolean mdrCacheLoaded = isMdrCacheLoaded();
        boolean rulesCacheLoaded = isRulesCacheLoaded();

        properties.put(MDR_CACHE_LOADED, mdrCacheLoaded);
        properties.put(RULES_LOADED, rulesCacheLoaded);
        properties.put(APPLICATION_VERSION, propertiesBean.getProperty(APPLICATION_VERSION));

        map.put(propertiesBean.getProperty(APPLICATION_NAME), properties);

        if (rulesCacheLoaded && mdrCacheLoaded) {
            response = Response.ok(map).build();
        } else {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
        return response;
    }

    private boolean isRulesCacheLoaded() {
        return initializer.isRulesLoaded();
    }

    private boolean isMdrCacheLoaded() {
        return mdrCache.isMdrCacheLoaded();
    }
}
