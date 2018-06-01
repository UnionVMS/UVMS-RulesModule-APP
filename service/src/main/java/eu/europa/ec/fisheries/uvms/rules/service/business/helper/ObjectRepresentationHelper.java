package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import com.google.common.base.Optional;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ObjectRepresentationHelper {

    private ObjectRepresentationHelper() {
        //hide public constructor
    }

    private static List<ObjectRepresentation> findWithColumnAndValue(String columnWhichShouldHaveTheGivenValue, String codeToSearchFor, Collection<ObjectRepresentation> objectRepresentations, DateTime date) {
        List<ObjectRepresentation> foundObjectRepresentations = new ArrayList<>();

        //find all object representations with a value for a certain column
        for (ObjectRepresentation objectRepresentation : objectRepresentations) {
            Optional<String> foundCode = getValueOfColumn(columnWhichShouldHaveTheGivenValue, objectRepresentation);
            if (foundCode.isPresent() && foundCode.get().equalsIgnoreCase(codeToSearchFor)) {
                foundObjectRepresentations.add(objectRepresentation);
            }
        }

        //keep only those that are valid at the given date
        Iterator<ObjectRepresentation> foundObjectRepresentationsIterator = foundObjectRepresentations.iterator();
        while (foundObjectRepresentationsIterator.hasNext()) {
            ObjectRepresentation next = foundObjectRepresentationsIterator.next();
            Optional<String> startDate = getValueOfColumn("startDate", next);
            Optional<String> endDate = getValueOfColumn("endDate", next);
            if (!startDate.isPresent() || !endDate.isPresent()
                    || date.isBefore(parseDate(startDate.get()))
                    || date.isAfter(parseDate(endDate.get()))) {
                foundObjectRepresentationsIterator.remove();
            }
        }
        return foundObjectRepresentations;
    }

    public static DateTime parseDate(String date) {
        return DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static Optional<String> getValueOfColumn(String columnName, ObjectRepresentation objectRepresentation) {
        for (ColumnDataType column : objectRepresentation.getFields()) {
            if (column.getColumnName().equalsIgnoreCase(columnName)) {
                return Optional.of(column.getColumnValue());
            }
        }
        return Optional.absent();
    }

    public static boolean doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
            (String code, String columnName, String columnValue,
             Collection<ObjectRepresentation> objectRepresentations, DateTime date) {
        return doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues("code", code, columnName, columnValue, objectRepresentations, date);
    }

    public static boolean doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
            (String column1, String value1, String column2, String value2,
             Collection<ObjectRepresentation> objectRepresentations, DateTime date) {

        Collection<ObjectRepresentation> foundObjectRepresentation = findWithColumnAndValue(column1, value1, objectRepresentations, date);
        for (ObjectRepresentation objectRepresentation : foundObjectRepresentation) {
            Optional<String> foundColumnValue = getValueOfColumn(column2, objectRepresentation);
            if (foundColumnValue.isPresent() && foundColumnValue.get().equalsIgnoreCase(value2)) {
                return true;
            }
        }

        return false;
    }
}
