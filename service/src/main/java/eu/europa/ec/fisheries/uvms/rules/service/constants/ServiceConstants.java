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
package eu.europa.ec.fisheries.uvms.rules.service.constants;

public class ServiceConstants {

    // Rule GUID for Asset not sending rule
    public static final String ASSET_NOT_SENDING_RULE = "Asset not sending";

    public static final String INVALID_XML_RULE="FA-L00-00-0000";
    public static final String INVALID_XML_RULE_MESSAGE="Verify whether or not the message is valid XML and validates against the XSD schema.";


    public static final String PERMISSION_DENIED_RULE="FA-L00-00-9999";
    public static final String PERMISSION_DENIED_RULE_MESSAGE="PERMISSION: The querier is not allowed access to the requested data. No FLUXFAReportMessage containing the response will be sent.";

    public static final String EMPTY_REPORT_RULE="FA-L03-00-9998";
    public static final String EMPTY_REPORT_RULE_MESSAGE="UNAVAILABLE: The requested data is not available. No FLUXFAReportMessage containing the response will be sent.";
}