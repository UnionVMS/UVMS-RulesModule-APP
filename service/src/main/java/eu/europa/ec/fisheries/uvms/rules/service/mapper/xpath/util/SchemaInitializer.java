package eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util;

import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.UnmarshalException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class SchemaInitializer {

    public static final Map<String, Schema> SCHEMA_MAP;

    static {
        try {
            HashMap<String, Schema> schemaMap = new HashMap<>();
            schemaMap.put(RulesFLUXMessageHelper.FLUXFAREPORT_MESSAGE_3P1_XSD, SchemaInitializer.loadXSDSchema(RulesFLUXMessageHelper.FLUXFAREPORT_MESSAGE_3P1_XSD));
            schemaMap.put(RulesFLUXMessageHelper.FLUXFAQUERY_MESSAGE_3P0_XSD, SchemaInitializer.loadXSDSchema(RulesFLUXMessageHelper.FLUXFAQUERY_MESSAGE_3P0_XSD));
            schemaMap.put(RulesFLUXMessageHelper.FLUXFARESPONSE_MESSAGE_6P0_XSD, SchemaInitializer.loadXSDSchema(RulesFLUXMessageHelper.FLUXFARESPONSE_MESSAGE_6P0_XSD));
            SCHEMA_MAP = Collections.unmodifiableMap(schemaMap);
        } catch (UnmarshalException e) {
            throw new RuntimeException("Cannot initialize XSD", e);
        }
    }

    private static Schema loadXSDSchema(String xsdLocation) throws UnmarshalException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = RulesFLUXMessageHelper.class.getClassLoader().getResource(xsdLocation);
        if (resource != null) {
            try {
                return sf.newSchema(resource);
            } catch (SAXException e) {
                throw new UnmarshalException(e.getMessage(), e);
            }
        }
        throw new UnmarshalException("ERROR WHILE TRYING TO LOOKUP XSD SCHEMA");
    }
}
