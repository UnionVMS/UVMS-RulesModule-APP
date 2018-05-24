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
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.rules.service.bean.MDRCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.PropertiesBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.TemplateEngine;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
@Path("/")
public class DoctorResource {

    private static final String MDR_CACHE_LOADED = "mdr_cache_loaded";
    private static final String RULES_LOADED = "rules_loaded";
    private static final String APPLICATION_VERSION = "application.version";
    private static final String APPLICATION_STATUS = "application.status";
    private static final String APPLICATION_NAME = "application.name";

    @EJB
    private MDRCache mdrCache;

    @EJB
    private TemplateEngine templateEngine;

    @EJB
    private PropertiesBean propertiesBean;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctor() {
        Boolean healthy = true;
        Map<String, Object> properties = new HashMap<>();
        Map<String, Map<String, Object>> metrics = new HashMap<>();
        long size = mdrCache.getCache().size();
        boolean check = size > 10;
        properties.put(MDR_CACHE_LOADED, check);
        if (!check){
            healthy = false;
        }
        List<RuleError> ruleErrors = templateEngine.checkRulesAreLoaded(5);
        check = CollectionUtils.isNotEmpty(ruleErrors);
        properties.put(RULES_LOADED, check);
        if (!check){
            healthy = false;
        }
        properties.put(APPLICATION_VERSION, propertiesBean.getProperty(APPLICATION_VERSION));
        if (healthy){
            properties.put(APPLICATION_STATUS, HttpServletResponse.SC_ACCEPTED);
        }
        else {
            properties.put(APPLICATION_STATUS, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        metrics.put(propertiesBean.getProperty(APPLICATION_NAME), properties);
        return Response.ok(properties).build();
    }
}
