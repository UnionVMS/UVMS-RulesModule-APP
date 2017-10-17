package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.HashMap;
import java.util.Map;

/**
 * Output of the FactGeneratorHelper.
 * Contains an object that could become a fact, together with its properties
 * and their corresponding XPath.
 */
public class FactCandidate {

    private Object object;
    private Map<String, String> propertiesAndTheirXPaths;

    public FactCandidate(Object object) {
        this.object = object;
        this.propertiesAndTheirXPaths = new HashMap<>();
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Map<String, String> getPropertiesAndTheirXPaths() {
        return propertiesAndTheirXPaths;
    }

    public void addPropertyAndItsXPath(String property, String xPath) {
        propertiesAndTheirXPaths.put(property, xPath);
    }
}
