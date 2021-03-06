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

package eu.europa.ec.fisheries.uvms.rules.service.config;

/**
 * Created by padhyad on 4/19/2017.
 */
public enum BusinessObjectType {

    RECEIVING_FA_REPORT_MSG,
    RECEIVING_FA_QUERY_MSG,
    SENDING_FA_REPORT_MSG,
    SENDING_FA_QUERY_MSG,
    SENDING_FA_RESPONSE_MSG,
    RECEIVING_FA_RESPONSE_MSG,
    FLUX_SALES_REPORT_MSG,
    FLUX_SALES_RESPONSE_MSG,
    FLUX_SALES_QUERY_MSG
}
