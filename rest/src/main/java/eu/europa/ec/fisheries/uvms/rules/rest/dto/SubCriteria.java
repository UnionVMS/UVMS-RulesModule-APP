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
package eu.europa.ec.fisheries.uvms.rules.rest.dto;

public enum SubCriteria {

    ASSET_GROUP(MainCriteria.ASSET_GROUP),

    ACTIVITY_CALLBACK(MainCriteria.ACTIVITY),
    ACTIVITY_MESSAGE_ID(MainCriteria.ACTIVITY),
    ACTIVITY_MESSAGE_TYPE(MainCriteria.ACTIVITY),

    AREA_CODE(MainCriteria.AREA),
    AREA_TYPE(MainCriteria.AREA),
    AREA_CODE_ENT(MainCriteria.AREA),
    AREA_TYPE_ENT(MainCriteria.AREA),
    AREA_CODE_EXT(MainCriteria.AREA),
    AREA_TYPE_EXT(MainCriteria.AREA),

    ASSET_ID_GEAR_TYPE(MainCriteria.ASSET),
    EXTERNAL_MARKING(MainCriteria.ASSET),
    FLAG_STATE(MainCriteria.ASSET),
    ASSET_CFR(MainCriteria.ASSET),
    ASSET_IRCS(MainCriteria.ASSET),
    ASSET_NAME(MainCriteria.ASSET),
    ASSET_STATUS(MainCriteria.ASSET),

    COMCHANNEL_TYPE(MainCriteria.MOBILE_TERMINAL),
    MT_TYPE(MainCriteria.MOBILE_TERMINAL),
    MT_DNID(MainCriteria.MOBILE_TERMINAL),
    MT_MEMBER_ID(MainCriteria.MOBILE_TERMINAL),
    MT_SERIAL_NO(MainCriteria.MOBILE_TERMINAL),
    MT_STATUS(MainCriteria.MOBILE_TERMINAL),

    ALTITUDE(MainCriteria.POSITION),
    LATITUDE(MainCriteria.POSITION),
    LONGITUDE(MainCriteria.POSITION),
    CALCULATED_COURSE(MainCriteria.POSITION),
    CALCULATED_SPEED(MainCriteria.POSITION),
    MOVEMENT_TYPE(MainCriteria.POSITION),
    POSITION_REPORT_TIME(MainCriteria.POSITION),
    REPORTED_COURSE(MainCriteria.POSITION),
    REPORTED_SPEED(MainCriteria.POSITION),
    SEGMENT_TYPE(MainCriteria.POSITION),
    SOURCE(MainCriteria.POSITION),
    STATUS_CODE(MainCriteria.POSITION),
    VICINITY_OF(MainCriteria.POSITION), // NOT IMPLEMENTED YET
    CLOSEST_COUNTRY_CODE(MainCriteria.POSITION),
    CLOSEST_PORT_CODE(MainCriteria.POSITION),

    TIME_DIFF_POSITION_REPORT(MainCriteria.REPORT),
    SUM_POSITION_REPORT(MainCriteria.REPORT);

    private final MainCriteria mainCriteria;

    SubCriteria(MainCriteria mainCriteria) {
        this.mainCriteria = mainCriteria;
    }

    public MainCriteria getMainCriteria() {
        return mainCriteria;
    }

}