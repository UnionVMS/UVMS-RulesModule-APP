/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityQueryFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityRequestFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityResponseFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by padhyad on 4/19/2017.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessObjectFactory {

    public static AbstractGenerator getBusinessObjFactGenerator(BusinessObjectType businessObjectType) {
        if(businessObjectType == null){
            return null;
        }
        AbstractGenerator generator = null;
        switch (businessObjectType) {
            case FLUX_ACTIVITY_REQUEST_MSG:
                generator = new ActivityRequestFactGenerator();
                break;
            case FLUX_ACTIVITY_RESPONSE_MSG:
                generator = new ActivityResponseFactGenerator();
                break;
            case FLUX_ACTIVITY_QUERY_MSG:
                generator = new ActivityQueryFactGenerator();
                break;
            default: break;
        }
        return generator;
    }
}
