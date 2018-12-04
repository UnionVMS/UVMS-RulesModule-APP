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
package eu.europa.ec.fisheries.uvms.rules.service.mapper;


import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.MobileTerminalType;

/**
 * Created by osdjup on 2016-05-18.
 */
public class MobileTerminalMapper {
    public static MobileTerminalType mapMobileTerminal(eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType mobileTerminalType) {
        if (mobileTerminalType == null) {
            return null;
        }
        MobileTerminalType rawMobileTerminalType = new MobileTerminalType();
        rawMobileTerminalType.setConnectId(mobileTerminalType.getConnectId());
        rawMobileTerminalType.setGuid(mobileTerminalType.getMobileTerminalId().getGuid());

        return rawMobileTerminalType;
    }
}