package eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper;

import eu.europa.ec.fisheries.uvms.rules.service.business.FactCandidate;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FactGeneratorHelper {

    private XPathStringWrapper xPathUtil;

    public FactGeneratorHelper(XPathStringWrapper xPathStringWrapper) {
        this.xPathUtil = xPathStringWrapper;
    }

    /**
     * Digs through the attribute tree of an object, and returns objects and their xpath who have one of the provided
     * classes.
     * @param object object to dig through
     * @param classesToSearchFor classes to search for, objects who have this class will be returned
     * @return a map with the xpath of the found object as key, and the found object itself as value
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public List<FactCandidate> findAllObjectsWithOneOfTheFollowingClasses(Object object, Collection<Class<?>> classesToSearchFor) throws IllegalAccessException, ClassNotFoundException {
        return findAllObjectsWithOneOfTheFollowingClasses(object, classesToSearchFor, "");
    }

    private List<FactCandidate> findAllObjectsWithOneOfTheFollowingClasses(Object object, Collection<Class<?>> classesToSearchFor, String currentXPath) throws IllegalAccessException, ClassNotFoundException {
        List<FactCandidate> foundObjects = new ArrayList<>();

        Class<?> clazz = object.getClass();

        if (classesToSearchFor.contains(clazz)) {
            foundObjects.add(createFactCandidate(object, currentXPath));
        }

        if (!clazz.isEnum()) {
            //loop over all fields of the object
            Field[] fields = clazz.getDeclaredFields();
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
                        List collectionAsList = new ArrayList(collection);
                        for (int i = 0; i < collectionAsList.size(); i++) {
                            //determine xPath
                            xPathUtil.appendWithoutWrapping(currentXPath);
                            xPathUtil.appendWithIndex(field.getName(), i);
                            String xPath = xPathUtil.getValue();

                            //add object to list
                            foundObjects.addAll(findAllObjectsWithOneOfTheFollowingClasses(collectionAsList.get(i), classesToSearchFor, xPath));
                        }
                    }

                    //is the field an object of Union/UNCEFACT? Let's repeat this logic for that object
                    if (fieldClazz.getName().startsWith("eu.") || fieldClazz.getName().startsWith("un.")) {
                        //determine xPath
                        xPathUtil.appendWithoutWrapping(currentXPath);
                        xPathUtil.append(field.getName());
                        String xPath = xPathUtil.getValue();

                        foundObjects.addAll(findAllObjectsWithOneOfTheFollowingClasses(fieldObject, classesToSearchFor, xPath));
                    }

                }
            }
        }


        return foundObjects;
    }

    private FactCandidate createFactCandidate(Object object, String currentXPath) {
        Field[] fields = object.getClass().getDeclaredFields();
        FactCandidate factCandidate = new FactCandidate(object);

        for (Field field : fields) {
            String xPathOfField = xPathUtil .appendWithoutWrapping(currentXPath)
                                            .append(field.getName())
                                            .getValue();
            factCandidate.addPropertyAndItsXPath(field.getName(), xPathOfField);
        }

        return factCandidate;
    }


}
