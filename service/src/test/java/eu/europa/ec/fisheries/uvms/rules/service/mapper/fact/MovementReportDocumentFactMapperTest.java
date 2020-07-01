package eu.europa.ec.fisheries.uvms.rules.service.mapper.fact;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MovementReportDocumentFact;
import org.joda.time.DateTime;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxvesselpositionmessage._4.FLUXVesselPositionMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.FLUXReportDocumentType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselTransportMeansType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.DateTimeType;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class MovementReportDocumentFactMapperTest {

    @Test
    public void testGenerateFactForMovementReportDocumentNULLDates() {

        MovementReportDocumentFactMapper factMapper = new MovementReportDocumentFactMapper();
        FLUXVesselPositionMessage vesselPositionMessage =new FLUXVesselPositionMessage();
        vesselPositionMessage.setVesselTransportMeans(new VesselTransportMeansType());
        vesselPositionMessage.setFLUXReportDocument(new FLUXReportDocumentType());
        AbstractFact movementReportDocumentFact = factMapper.generateFactForMovementReportDocument(vesselPositionMessage);
    }

    @Test
    public void testGenerateFactForMovementReportDocumentNormalDates() throws DatatypeConfigurationException {

        MovementReportDocumentFactMapper factMapper = new MovementReportDocumentFactMapper();
        FLUXVesselPositionMessage vesselPositionMessage =new FLUXVesselPositionMessage();
        vesselPositionMessage.setVesselTransportMeans(new VesselTransportMeansType());
        FLUXReportDocumentType reportDocumentType = new FLUXReportDocumentType();
        GregorianCalendar calendar = new GregorianCalendar();
        Date d = new Date();
        calendar.setTime(d);
        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
        reportDocumentType.setCreationDateTime(dateTimeType);
        vesselPositionMessage.setFLUXReportDocument(reportDocumentType);
        MovementReportDocumentFact abstractFact = (MovementReportDocumentFact)factMapper.generateFactForMovementReportDocument(vesselPositionMessage);
        Date creationDateOfMessage = abstractFact.getCreationDateTime();
        assertEquals(d,creationDateOfMessage);
    }
}