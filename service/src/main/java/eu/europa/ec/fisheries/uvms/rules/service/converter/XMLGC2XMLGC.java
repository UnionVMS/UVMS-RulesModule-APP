package eu.europa.ec.fisheries.uvms.rules.service.converter;

import org.dozer.DozerConverter;

import javax.xml.datatype.XMLGregorianCalendar;

public class XMLGC2XMLGC extends DozerConverter<XMLGregorianCalendar, XMLGregorianCalendar> {

    public XMLGC2XMLGC() {
        super(XMLGregorianCalendar.class, XMLGregorianCalendar.class);
    }

    @Override
    public XMLGregorianCalendar convertFrom(XMLGregorianCalendar src, XMLGregorianCalendar dest) {
        return src;
    }

    @Override
    public XMLGregorianCalendar convertTo(XMLGregorianCalendar src, XMLGregorianCalendar dest) {
        return dest;
    }
}
