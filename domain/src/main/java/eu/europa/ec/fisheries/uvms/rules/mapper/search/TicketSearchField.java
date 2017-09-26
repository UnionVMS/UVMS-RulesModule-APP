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
package eu.europa.ec.fisheries.uvms.rules.mapper.search;

import java.util.Date;

public enum TicketSearchField {
    TICKET_GUID("guid", TicketSearchTables.TICKET, String.class),
    ASSET_GUID("assetGuid", TicketSearchTables.TICKET, String.class),
    RULE_GUID("ruleGuid", TicketSearchTables.TICKET, String.class),
    RULE_NAME("ruleName", TicketSearchTables.TICKET, String.class),
    RULE_RECIPIENT("recipient", TicketSearchTables.TICKET, String.class),
    STATUS("status", TicketSearchTables.TICKET, String.class),
    FROM_DATE("createdDate", TicketSearchTables.TICKET, Date.class),
    TO_DATE("createdDate", TicketSearchTables.TICKET, Date.class),
    UPDATED_BY("updatedBy", TicketSearchTables.TICKET, String.class);

    private final String fieldName;
    private final TicketSearchTables searchTables;
    private Class clazz;

    TicketSearchField(String fieldName, TicketSearchTables searchTables, Class clazz) {
        this.fieldName = fieldName;
        this.searchTables = searchTables;
        this.clazz = clazz;
    }

    /**
     *
     * @return The field name in the Ticket entity. Must be exact.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     *
     * @return
     */
    public TicketSearchTables getSearchTables() {
        return searchTables;
    }

    /**
     *
     * @return
     */
    public Class getClazz() {
        return clazz;
    }

}