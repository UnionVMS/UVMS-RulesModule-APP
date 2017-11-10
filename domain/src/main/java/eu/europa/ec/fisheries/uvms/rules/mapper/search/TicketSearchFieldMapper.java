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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import eu.europa.ec.fisheries.schema.rules.search.v1.TicketListCriteria;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketSearchKey;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import eu.europa.ec.fisheries.uvms.rules.exception.SearchMapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketSearchFieldMapper {

    private static final Logger LOG = LoggerFactory.getLogger(TicketSearchFieldMapper.class);

    /**
     * Creates a search SQL based on the search fields
     *
     * @param searchFields
     * @param isDynamic
     * @return
     * @throws eu.europa.ec.fisheries.uvms.rules.exception.SearchMapperException
     */
    public static String createSelectSearchSql(List<TicketSearchValue> searchFields, List<String> validRuleGuids, boolean isDynamic) throws SearchMapperException {
        StringBuilder selectBuffer = new StringBuilder();
        selectBuffer.append("SELECT ")
                .append(TicketSearchTables.TICKET.getTableAlias())
                .append(" FROM ")
                .append(TicketSearchTables.TICKET.getTableName())
                .append(" ")
                .append(TicketSearchTables.TICKET.getTableAlias())
                .append(" ");
        if (searchFields != null && !searchFields.isEmpty()) {
            selectBuffer.append(createSearchSql(searchFields, isDynamic));

            selectBuffer.append(" AND ");
        } else {
            selectBuffer.append(" WHERE ");
        }
        selectBuffer.append(TicketSearchTables.TICKET.getTableAlias())
                .append(".")
                .append(TicketSearchField.RULE_GUID.getFieldName());

        selectBuffer.append(" IN ( ");
        selectBuffer.append("'").append(UvmsConstants.ASSET_NOT_SENDING_RULE).append("'");
        for (String validRuleGuid : validRuleGuids) {
            selectBuffer.append(", ");
            selectBuffer.append("'").append(validRuleGuid).append("'");
        }
        selectBuffer.append(" )");

        selectBuffer
                .append(" ORDER BY ")
                .append(TicketSearchTables.TICKET.getTableAlias())
                .append(".")
                .append(TicketSearchField.FROM_DATE.getFieldName())
                .append(" DESC");
        LOG.info("[ SQL: ] {}", selectBuffer.toString());
        return selectBuffer.toString();
    }

    /**
     *
     * Creates a JPQL count query based on the search fields. This is used for
     * when paginating lists
     *
     * @param searchFields
     * @param isDynamic
     * @return
     * @throws eu.europa.ec.fisheries.uvms.rules.exception.SearchMapperException
     */
    public static String createCountSearchSql(List<TicketSearchValue> searchFields, List<String> validRuleGuids, boolean isDynamic) throws SearchMapperException {
        StringBuilder countBuffer = new StringBuilder();
        countBuffer.append("SELECT COUNT(")
                .append(TicketSearchTables.TICKET.getTableAlias())
                .append(") FROM ")
                .append(TicketSearchTables.TICKET.getTableName())
                .append(" ")
                .append(TicketSearchTables.TICKET.getTableAlias())
                .append(" ");
        if (searchFields != null && !searchFields.isEmpty()) {
            countBuffer.append(createSearchSql(searchFields, isDynamic));
            countBuffer.append(" AND ");
        } else {
            countBuffer.append(" WHERE ");
        }
        countBuffer.append(TicketSearchTables.TICKET.getTableAlias())
                .append(".")
                .append(TicketSearchField.RULE_GUID.getFieldName());

        countBuffer.append(" IN ( ");
        countBuffer.append("'").append(UvmsConstants.ASSET_NOT_SENDING_RULE).append("'");
        for (String validRuleGuid : validRuleGuids) {
            countBuffer.append(", ");
            countBuffer.append("'").append(validRuleGuid).append("'");
        }
        countBuffer.append(" )");

        LOG.debug("[ COUNT SQL: ]{}", countBuffer.toString());
        return countBuffer.toString();
    }

    /**
     *
     * Creates the complete search SQL with joins and sets the values based on
     * the criteria
     *
     * @param criteriaList
     * @param dynamic
     * @return
     * @throws SearchMapperException
     */
    private static String createSearchSql(List<TicketSearchValue> criteriaList, boolean dynamic) throws SearchMapperException {

        String operator = " OR ";
        if (dynamic) {
            operator = " AND ";
        }

        StringBuilder builder = new StringBuilder();

        HashMap<TicketSearchField, List<TicketSearchValue>> orderedValues = combineSearchFields(criteriaList);

        if (!orderedValues.isEmpty()) {

            builder.append("WHERE ");

            boolean first = true;

            for (Entry<TicketSearchField, List<TicketSearchValue>> criteria : orderedValues.entrySet()) {
                first = createOperator(first, builder, operator);
                createCriteria(criteria.getValue(), criteria.getKey(), builder);
            }

        }

        return builder.toString();
    }

    private static boolean createOperator(boolean first, StringBuilder builder, String operator) {
        if (!first) {
            builder.append(operator);
        }
        return false;
    }

    /**
     * Creates the where condition. If the list has more than one value the
     * condition will be 'IN(value1, value2)' If the list has one value the
     * condition will be '= value'
     *
     * @param criteria
     * @param builder
     * @throws SearchMapperException
     */
    private static void createCriteria(List<TicketSearchValue> criteria, TicketSearchField field, StringBuilder builder) throws SearchMapperException {
        if (criteria.size() == 1) {
            TicketSearchValue searchValue = criteria.get(0);
            builder
                    .append(buildTableAliasName(searchValue.getField()))
                    .append(addParameters(searchValue));
        } else {
            builder
                    .append(buildInSqlStatement(criteria, field));
        }
    }

    private static String buildInSqlStatement(List<TicketSearchValue> searchValues, TicketSearchField field) throws SearchMapperException {
        StringBuilder builder = new StringBuilder();

        builder.append(buildTableAliasName(field));

        builder.append(" IN ( ");
        boolean first = true;
        for (TicketSearchValue searchValue : searchValues) {
            if (first) {
                first = false;
                builder.append(buildValueFromClassType(searchValue));
            } else {
                builder.append(", ").append(buildValueFromClassType(searchValue));
            }
        }
        builder.append(" )");
        return builder.toString();
    }

    private static String buildValueFromClassType(TicketSearchValue entry) throws SearchMapperException {
        StringBuilder builder = new StringBuilder();
        if (entry.getField().getClazz().isAssignableFrom(Integer.class)) {
            builder.append(entry.getValue());
        } else if (entry.getField().getClazz().isAssignableFrom(Double.class)) {
            builder.append(entry.getValue());
        } else {
            builder.append("'").append(entry.getValue()).append("'");
        }
        return builder.toString();
    }

    /**
     *
     * Creates at String that sets values based on what class the SearchValue
     * has. A String class returns [ = 'value' ] A Integer returns [ = value ]
     * Date is specifically handled and can return [ >= 'datavalue' ] or [ <=
     * 'datavalue' ]
     *
     * @param entry
     * @return
     * @throws SearchMapperException
     */
    private static String addParameters(TicketSearchValue entry) throws SearchMapperException {
        StringBuilder builder = new StringBuilder();

        switch (entry.getField()) {
            case TICKET_GUID:
                builder.append(" = ");
                break;
            case ASSET_GUID:
                builder.append(" = ");
                break;
            case RULE_GUID:
                builder.append(" = ");
                break;
            case RULE_NAME:
                builder.append(" = ");
                break;
            case RULE_RECIPIENT:
                builder.append(" = ");
                break;
            case STATUS:
                builder.append(" = ");
                break;
            case FROM_DATE:
                builder.append(" >= ");
                break;
            case TO_DATE:
                builder.append(" <= ");
                break;
            case UPDATED_BY:
                builder.append(" = ");
                break;
            default:
                break;
        }
        builder.append(buildValueFromClassType(entry)).append(" ");
        return builder.toString();
    }

    /**
     *
     * Builds a table alias for the query based on the search field
     *
     * EG [ theTableAlias.theColumnName ]
     *
     * @param field
     * @return
     */
    private static String buildTableAliasName(TicketSearchField field) {
        StringBuilder builder = new StringBuilder();
        builder.append(field.getSearchTables().getTableAlias()).append(".").append(field.getFieldName());
        return builder.toString();
    }

    /**
     *
     * Takes all the search values and categorizes them in lists to a key
     * according to the SearchField
     *
     * @param searchValues
     * @return
     */
    private static HashMap<TicketSearchField, List<TicketSearchValue>> combineSearchFields(List<TicketSearchValue> searchValues) throws SearchMapperException {
        HashMap<TicketSearchField, List<TicketSearchValue>> values = new HashMap<>();
        for (TicketSearchValue search : searchValues) {
            if (values.containsKey(search.getField())) {
                values.get(search.getField()).add(search);
            } else {
                values.put(search.getField(), new ArrayList<TicketSearchValue>(Arrays.asList(search)));
            }
        }
        return values;
    }

    /**
     *
     * Converts List<ListCriteria> to List<SearchValue> so that a JPQL query can
     * be built based on the criteria
     *
     * @param criteriaList
     * @return
     * @throws DaoMappingException
     */
    public static List<TicketSearchValue> mapSearchField(List<TicketListCriteria> criteriaList) throws DaoMappingException {

        if (criteriaList == null || criteriaList.isEmpty()) {
            LOG.debug(" Non valid search criteria when mapping TicketListCriteria to TicketSearchValue, List is null or empty");
            return new ArrayList<>();
        }

        List<TicketSearchValue> searchFields = new ArrayList<>();
        for (TicketListCriteria criteria : criteriaList) {
            try {
                TicketSearchField field = mapCriteria(criteria.getKey());
                // Oracle cannot handle time zone in String
                if(field.equals(TicketSearchField.FROM_DATE) || field.equals(TicketSearchField.TO_DATE)){
                    searchFields.add(new TicketSearchValue(field, DateUtils.stringToUTC(criteria.getValue())));
                }else {
                    searchFields.add(new TicketSearchValue(field, criteria.getValue()));
                }
            } catch (SearchMapperException ex) {
                LOG.debug("[ Error when mapping to search field... continuing with other criteria ]");
            }
        }

        return searchFields;
    }

    /**
     *
     * Maps the Search Key to a SearchField.
     *
     * @param key
     * @return
     * @throws SearchMapperException
     */
    private static TicketSearchField mapCriteria(TicketSearchKey key) throws SearchMapperException {
        switch (key) {
            case TICKET_GUID:
                return TicketSearchField.TICKET_GUID;
            case ASSET_GUID:
                return TicketSearchField.ASSET_GUID;
            case RULE_GUID:
                return TicketSearchField.RULE_GUID;
            case RULE_NAME:
                return TicketSearchField.RULE_NAME;
            case RULE_RECIPIENT:
                return TicketSearchField.RULE_RECIPIENT;
            case STATUS:
                return TicketSearchField.STATUS;
            case FROM_DATE:
                return TicketSearchField.FROM_DATE;
            case TO_DATE:
                return TicketSearchField.TO_DATE;
            case UPDATED_BY:
                return TicketSearchField.UPDATED_BY;
            default:
                throw new SearchMapperException("No field found: " + key.name());
        }

    }

}