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
package eu.europa.ec.fisheries.uvms.rules.rest.filter;

import eu.europa.ec.fisheries.uvms.rules.rest.constants.RestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 **/
@WebFilter(asyncSupported = true, urlPatterns = {"/*"})
public class RequestFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.debug("Requestfilter starting up!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader(RestConstants.ACCESS_CONTROL_ALLOW_ORIGIN, RestConstants.ACCESS_CONTROL_ALLOW_METHODS_ALL);
        response.setHeader(RestConstants.ACCESS_CONTROL_ALLOW_METHODS, RestConstants.ACCESS_CONTROL_ALLOWED_METHODS);
        response.setHeader(RestConstants.ACCESS_CONTROL_ALLOW_HEADERS, RestConstants.ACCESS_CONTROL_ALLOW_HEADERS_ALL);
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {
        LOG.debug("Requestfilter shutting down!");
    }

}