package eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FactGeneratorHelper {

    public List<Object> findAllObjectsWithOneOfTheFollowingClasses(Object object, Collection<Class<?>> classesToSearchFor) throws IllegalAccessException, ClassNotFoundException {
        List<Object> foundObjects = new ArrayList<>();

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        if (classesToSearchFor.contains(clazz)) {
            foundObjects.add(object);
        }

        //loop over all fields of the object
        for (Field field : fields) {
            Class<?> fieldClazz = field.getType();

            boolean originallyAccessible = field.isAccessible();
            field.setAccessible(true);
            Object fieldObject = field.get(object);
            field.setAccessible(originallyAccessible);

            if (fieldObject != null) {

                //is the field a collection? Loop over all items in the collection!
                if (Collection.class.isAssignableFrom(fieldClazz)) {
                    Collection collection = (Collection) fieldObject;
                    for (Object collectionObject : collection) {
                        foundObjects.addAll(findAllObjectsWithOneOfTheFollowingClasses(collectionObject, classesToSearchFor));
                    }
                }

                //is the field an object of Union/UNCEFACT? Let's repeat this logic for that object
                if (fieldClazz.getName().startsWith("eu.") || fieldClazz.getName().startsWith("un.")) {
                    foundObjects.addAll(findAllObjectsWithOneOfTheFollowingClasses(fieldObject, classesToSearchFor));
                }

            }
        }

        return foundObjects;
    }

}
