package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import com.google.common.base.Optional;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObjectRepresentationHelper {

    private static List<ObjectRepresentation> findWithColumnAndValue(String columnWhichShouldHaveTheGivenValue, String codeToSearchFor, Collection<ObjectRepresentation> objectRepresentations) {
        List<ObjectRepresentation> foundObjectRepresentations = new ArrayList<>();

        for (ObjectRepresentation objectRepresentation : objectRepresentations) {
            Optional<String> foundCode = getValueOfColumn(columnWhichShouldHaveTheGivenValue, objectRepresentation);
            if (foundCode.isPresent() && foundCode.get().equalsIgnoreCase(codeToSearchFor)) {
                foundObjectRepresentations.add(objectRepresentation);
            }
        }
        return foundObjectRepresentations;
    }

    private static Optional<String> getValueOfColumn(String columnName, ObjectRepresentation objectRepresentation) {
        for (ColumnDataType column : objectRepresentation.getFields()) {
            if (column.getColumnName().equalsIgnoreCase(columnName)) {
                return Optional.of(column.getColumnValue());
            }
        }
        return Optional.absent();
    }

    public static boolean doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
            (String code, String columnName, String columnValue,
             Collection<ObjectRepresentation> objectRepresentations) {
        return doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues("code", code, columnName, columnValue, objectRepresentations);
    }

    public static boolean doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
            (String column1, String value1, String column2, String value2,
             Collection<ObjectRepresentation> objectRepresentations) {

        Collection<ObjectRepresentation> foundObjectRepresentation = findWithColumnAndValue(column1, value1, objectRepresentations);
        for (ObjectRepresentation objectRepresentation : foundObjectRepresentation) {
            Optional<String> foundColumnValue = getValueOfColumn(column2, objectRepresentation);
            if (foundColumnValue.isPresent() && foundColumnValue.get().equalsIgnoreCase(value2)) {
                return true;
            }
        }

        return false;
    }
}
