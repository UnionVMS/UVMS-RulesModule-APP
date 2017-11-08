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

import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmListCriteria;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmSearchKey;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import eu.europa.ec.fisheries.uvms.rules.exception.SearchMapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmSearchFieldMapper {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmSearchFieldMapper.class);

    /**
     * Creates a search SQL based on the search fields
     *
     * @param searchFields
     * @param isDynamic
     * @return
     * @throws eu.europa.ec.fisheries.uvms.rules.exception.SearchMapperException
     */
    public static String createSelectSearchSql(List<AlarmSearchValue> searchFields, boolean isDynamic) throws SearchMapperException {
        StringBuilder selectBuffer = new StringBuilder();
        selectBuffer.append("SELECT ")
                .append(AlarmSearchTables.ALARM_REPORT.getTableAlias())
                .append(" FROM ")
                .append(AlarmSearchTables.ALARM_REPORT.getTableName())
                .append(" ")
                .append(AlarmSearchTables.ALARM_REPORT.getTableAlias())
                .append(" ");
        if (searchFields != null) {
            selectBuffer.append(createSearchSql(searchFields, isDynamic));
        }
        selectBuffer
                .append(" ORDER BY ")
                .append(AlarmSearchTables.ALARM_REPORT.getTableAlias())
                .append(".")
                .append(AlarmSearchField.FROM_DATE.getFieldName())
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
    public static String createCountSearchSql(List<AlarmSearchValue> searchFields, boolean isDynamic) throws SearchMapperException {
        StringBuilder countBuffer = new StringBuilder();
        countBuffer.append("SELECT COUNT(")
                .append(AlarmSearchTables.ALARM_REPORT.getTableAlias())
                .append(") FROM ")
                .append(AlarmSearchTables.ALARM_REPORT.getTableName())
                .append(" ")
                .append(AlarmSearchTables.ALARM_REPORT.getTableAlias())
                .append(" ");
        if (searchFields != null) {
            countBuffer.append(createSearchSql(searchFields, isDynamic));
        }

        LOG.debug("[ COUNT SQL: ]" + countBuffer.toString());
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
    private static String createSearchSql(List<AlarmSearchValue> criteriaList, boolean dynamic) throws SearchMapperException {

        String operator = " OR ";
        if (dynamic) {
            operator = " AND ";
        }

        StringBuilder builder = new StringBuilder();

        HashMap<AlarmSearchField, List<AlarmSearchValue>> orderedValues = combineSearchFields(criteriaList);

        builder.append(buildJoin(orderedValues, dynamic));
        if (!orderedValues.isEmpty()) {

            builder.append("WHERE ");

            boolean first = true;

            for (Entry<AlarmSearchField, List<AlarmSearchValue>> criteria : orderedValues.entrySet()) {
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
    private static void createCriteria(List<AlarmSearchValue> criteria, AlarmSearchField field, StringBuilder builder) throws SearchMapperException {
        if (criteria.size() == 1) {
            AlarmSearchValue searchValue = criteria.get(0);
            builder
                    .append(buildTableAliasName(searchValue.getField()))
                    .append(addParameters(searchValue));
        } else {
            builder
                    .append(buildInSqlStatement(criteria, field));
        }
    }

    private static String buildInSqlStatement(List<AlarmSearchValue> searchValues, AlarmSearchField field) throws SearchMapperException {
        StringBuilder builder = new StringBuilder();

        builder.append(buildTableAliasName(field));

        builder.append(" IN ( ");
        boolean first = true;
        for (AlarmSearchValue searchValue : searchValues) {
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

    private static String buildValueFromClassType(AlarmSearchValue entry) throws SearchMapperException {
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
     * Created the Join statement based on the join type. The resulting String
     * can be:
     *
     * JOIN LEFT JOIN JOIN FETCH ( based on fetch )
     *
     * @param fetch
     *            create a JOIN FETCH or plain JOIN
     * @param type
     * @return
     */
    private static String getJoin(boolean fetch, JoinType type) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ").append(type.name()).append(" ");
        builder.append("JOIN ");
        if (fetch) {
            builder.append("FETCH ");
        }
        return builder.toString();
    }

    /**
     * Builds JPA joins based on the search criteria provided. In some cases
     * there is no need for a join and the JQL query runs faster
     *
     * @param orderedValues
     * @param fetch
     * @return
     */
    private static String buildJoin(HashMap<AlarmSearchField, List<AlarmSearchValue>> orderedValues, boolean fetch) {
        StringBuilder builder = new StringBuilder();

        if (orderedValues.containsKey(AlarmSearchField.RULE_GUID) || orderedValues.containsKey(AlarmSearchField.RULE_NAME)) {
            builder.append(getJoin(false, JoinType.INNER))
            .append(AlarmSearchTables.ALARM_REPORT.getTableAlias())
            .append(".")
            .append("alarmItemList ")
            .append(AlarmSearchTables.ALARM_ITEM.getTableAlias()).append(" ");
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
    private static String addParameters(AlarmSearchValue entry) throws SearchMapperException {
        StringBuilder builder = new StringBuilder();

        switch (entry.getField()) {
            case ALARM_GUID:
                builder.append(" = ");
                break;
            case ASSET_GUID:
                builder.append(" = ");
                break;
            case STATUS:
                builder.append(" = ");
                break;
            case RULE_RECIPIENT:
                builder.append(" = ");
                break;
            case FROM_DATE:
                builder.append(" >= ");
                break;
            case TO_DATE:
                builder.append(" <= ");
                break;
            case RULE_GUID:
                builder.append(" = ");
                break;
            case RULE_NAME:
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
    private static String buildTableAliasName(AlarmSearchField field) {
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
    private static HashMap<AlarmSearchField, List<AlarmSearchValue>> combineSearchFields(List<AlarmSearchValue> searchValues) throws SearchMapperException {
        HashMap<AlarmSearchField, List<AlarmSearchValue>> values = new HashMap<>();
        for (AlarmSearchValue search : searchValues) {
            if (values.containsKey(search.getField())) {
                values.get(search.getField()).add(search);
            } else {
                values.put(search.getField(), new ArrayList<AlarmSearchValue>(Arrays.asList(search)));
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
    public static List<AlarmSearchValue> mapSearchField(List<AlarmListCriteria> criteriaList) throws DaoMappingException {

        if (criteriaList == null || criteriaList.isEmpty()) {
            LOG.debug(" Non valid search criteria when mapping AlarmListCriteria to AlarmSearchValue, List is null or empty");
            return new ArrayList<>();
        }

        List<AlarmSearchValue> searchFields = new ArrayList<>();
        for (AlarmListCriteria criteria : criteriaList) {
            try {
                AlarmSearchField field = mapCriteria(criteria.getKey());
                if(field.equals(AlarmSearchField.FROM_DATE) || field.equals(AlarmSearchField.TO_DATE)){
                    searchFields.add(new AlarmSearchValue(field, DateUtils.stringToUTC(criteria.getValue())));
                }else {
                    searchFields.add(new AlarmSearchValue(field, criteria.getValue()));
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
    private static AlarmSearchField mapCriteria(AlarmSearchKey key) throws SearchMapperException {
        switch (key) {
            case ALARM_GUID:
                return AlarmSearchField.ALARM_GUID;
            case ASSET_GUID:
                return AlarmSearchField.ASSET_GUID;
            case STATUS:
                return AlarmSearchField.STATUS;
            case RULE_RECIPIENT:
                return AlarmSearchField.RULE_RECIPIENT;
            case FROM_DATE:
                return AlarmSearchField.FROM_DATE;
            case TO_DATE:
                return AlarmSearchField.TO_DATE;
            case RULE_GUID:
                return AlarmSearchField.RULE_GUID;
            case RULE_NAME:
                return AlarmSearchField.RULE_NAME;
            default:
                throw new SearchMapperException("No field found: " + key.name());
        }
    }

    /**
     * The supported JOIN types see method getJoin for more info
     */
    public enum JoinType {
        INNER, LEFT;
    }

}