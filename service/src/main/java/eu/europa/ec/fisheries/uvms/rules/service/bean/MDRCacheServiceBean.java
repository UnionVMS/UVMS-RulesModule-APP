/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.MDRCacheHolder;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;


@Stateless
@LocalBean
@Slf4j
public class MDRCacheServiceBean {

    @EJB
    private MDRCache cache;

    public void loadMDRCache() {
        log.debug("[START] Loading MDR Cache..");
        for (MDRAcronymType acronymType : MDRAcronymType.values()) {
            List<ObjectRepresentation> values = cache.getEntry(acronymType);
            MDRCacheHolder.getInstance().addToCache(acronymType, values);
        }
        log.debug("[END] Cache loading is complete.");
    }

}
