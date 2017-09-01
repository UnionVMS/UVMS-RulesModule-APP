package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import com.google.common.base.Optional;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.util.Collection;

public class ObjectRepresentationHelper {

    private static Optional<ObjectRepresentation> findWithCode(String codeToSearchFor, Collection<ObjectRepresentation> objectRepresentations) {
        for (ObjectRepresentation objectRepresentation : objectRepresentations) {
            Optional<String> foundCode = getValueOfColumn("code", objectRepresentation);
            if (foundCode.isPresent() && foundCode.get().equalsIgnoreCase(codeToSearchFor)) {
                return Optional.of(objectRepresentation);
            }
        }
        return Optional.absent();
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

        Optional<ObjectRepresentation> foundObjectRepresentation = findWithCode(code, objectRepresentations);
        if (foundObjectRepresentation.isPresent()) {

            Optional<String> foundColumnValue = getValueOfColumn(columnName, foundObjectRepresentation.get());
            if (foundColumnValue.isPresent()) {
                return foundColumnValue.get().equalsIgnoreCase(columnValue);
            }
        }

        return false;
    }
}
