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

public enum CustomRuleSearchField {
    NAME("name", CustomRuleSearchTables.CUSTOM_RULE, String.class),
    GUID("guid", CustomRuleSearchTables.CUSTOM_RULE, String.class),
    TYPE("type", CustomRuleSearchTables.CUSTOM_RULE, String.class),
    AVAILABILITY("availability", CustomRuleSearchTables.CUSTOM_RULE, String.class),
    RULE_USER("updatedBy", CustomRuleSearchTables.CUSTOM_RULE, String.class),
    ARCHIVED("archived", CustomRuleSearchTables.CUSTOM_RULE, String.class),
    TICKET_ACTION_USER("value", CustomRuleSearchTables.ACTION, String.class);

    private final String fieldName;
    private final CustomRuleSearchTables searchTables;
    private Class clazz;

    CustomRuleSearchField(String fieldName, CustomRuleSearchTables searchTables, Class clazz) {
        this.fieldName = fieldName;
        this.searchTables = searchTables;
        this.clazz = clazz;
    }

    /**
     *
     * @return The field name in the Alarm entity. Must be exact.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     *
     * @return
     */
    public CustomRuleSearchTables getSearchTables() {
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