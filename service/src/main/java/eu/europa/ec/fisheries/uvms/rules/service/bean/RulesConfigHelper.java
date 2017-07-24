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
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.config.constants.ConfigHelper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.MessageConstants;
import eu.europa.ec.fisheries.uvms.rules.service.config.ParameterKey;

@Stateless
public class RulesConfigHelper implements ConfigHelper {

    @Override
    public List<String> getAllParameterKeys() {
        List<String> keys = new ArrayList<>();
        for (ParameterKey parameterKey : ParameterKey.values()) {
            keys.add(parameterKey.getKey());
        }

        return keys;
    }

    @Override
    public String getModuleName() {
        return MessageConstants.MODULE_NAME;
    }

}