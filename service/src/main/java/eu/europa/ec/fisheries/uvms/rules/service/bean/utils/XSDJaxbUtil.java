package eu.europa.ec.fisheries.uvms.rules.service.bean.utils;

import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import org.xml.sax.SAXException;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

import javax.xml.XMLConstants;
import javax.xml.bind.UnmarshalException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class XSDJaxbUtil {

    private static final String FLUXFAREPORT_MESSAGE_3P1_XSD = "xsd/contract/fa/data/standard/FLUXFAReportMessage_3p1.xsd";
    private static final String FLUXFAQUERY_MESSAGE_3P0_XSD = "xsd/contract/fa/data/standard/FLUXFAQueryMessage_3p0.xsd";
    private static final String FLUXFARESPONSE_MESSAGE_6P0_XSD = "xsd/contract/fa/data/standard/FLUXResponseMessage_6p0.xsd";

    public static FLUXFAReportMessage unMarshallAndValidateSchema(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXFAReportMessage.class, loadXSDSchema(FLUXFAREPORT_MESSAGE_3P1_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    public static FLUXFAQueryMessage unMarshallFaQueryMessage(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXFAQueryMessage.class, loadXSDSchema(FLUXFAQUERY_MESSAGE_3P0_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    public static FLUXResponseMessage unMarshallFluxResponseMessage(String request) throws UnmarshalException {
        try {
            return JAXBUtils.unMarshallMessage(request, FLUXResponseMessage.class, loadXSDSchema(FLUXFARESPONSE_MESSAGE_6P0_XSD));
        } catch (Exception e) {
            throw new UnmarshalException(e.getMessage());
        }
    }

    public static Schema loadXSDSchema(String xsdLocation) throws UnmarshalException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = XSDJaxbUtil.class.getClassLoader().getResource(xsdLocation);
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
