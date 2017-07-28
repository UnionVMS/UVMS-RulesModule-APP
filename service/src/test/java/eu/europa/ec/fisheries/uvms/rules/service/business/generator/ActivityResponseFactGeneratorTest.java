/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaResponseFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.ValidationResultDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQueryParameter;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationQualityAnalysis;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType;

import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sanera on 28/07/2017.
 */
public class ActivityResponseFactGeneratorTest {
    private ActivityResponseFactGenerator generator = new ActivityResponseFactGenerator();

    private IDType idType;
    private CodeType codeType;
    private DelimitedPeriod delimitedPeriod;
    private DateTimeType dateTimeType;
    private Date date;
    private FAQuery faQuery;
    private List<FAQueryParameter> faQueryParameterList;


    @Before
    @SneakyThrows
    public void before(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        date = sdf.parse("31-08-1982 10:20:56");

        idType = new IDType();
        idType.setValue("value");
        idType.setSchemeID("schemeId");

        codeType = new CodeType();
        codeType.setValue("value");

        delimitedPeriod = new DelimitedPeriod();
        MeasureType durationMeasure = new MeasureType();
        durationMeasure.setUnitCode("unitCode");
        durationMeasure.setValue(new BigDecimal(10));
        delimitedPeriod.setDurationMeasure(durationMeasure);

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);

        dateTimeType = new DateTimeType();
        dateTimeType.setDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        delimitedPeriod.setStartDateTime(dateTimeType);
        delimitedPeriod.setEndDateTime(dateTimeType);



        FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();
        fluxResponseDocument.setResponseCode(codeType);
        fluxResponseDocument.setIDS(Arrays.asList(idType));

        List<ValidationResultDocument> validationResultDocuments = new ArrayList<>();
        ValidationResultDocument validationResultDocument = new ValidationResultDocument();
        validationResultDocument.setCreationDateTime(dateTimeType);
        validationResultDocument.setValidatorID(idType);

        List<ValidationQualityAnalysis> validationQualityAnalysisList = new ArrayList<>();
        ValidationQualityAnalysis validationQualityAnalysis = new ValidationQualityAnalysis();
        validationQualityAnalysis.setTypeCode(codeType);
        validationQualityAnalysis.setLevelCode(codeType);
        validationQualityAnalysis.setID(idType);

        validationQualityAnalysisList.add(validationQualityAnalysis);
        validationResultDocument.setRelatedValidationQualityAnalysises(validationQualityAnalysisList);
        validationResultDocuments.add(validationResultDocument);

        fluxResponseDocument.setRelatedValidationResultDocuments(validationResultDocuments);

        FLUXResponseMessage message = new FLUXResponseMessage();
        message.setFLUXResponseDocument(fluxResponseDocument);

        generator.setBusinessObjectMessage(message);

    }

    @Test
    public void testEvaluate(){

        List<AbstractFact> abstractFacts = generator.generateAllFacts();
        FaResponseFact faResponseFact = (FaResponseFact) abstractFacts.get(0);
        ValidationResultDocumentFact validationResultDocumentFact = (ValidationResultDocumentFact) abstractFacts.get(1);

        assertEquals(codeType.getValue(), faResponseFact.getResponseCode().getValue());
        assertEquals(idType.getSchemeID(),validationResultDocumentFact.getValidatorID().getSchemeId());


    }

    @Test(expected = RulesValidationException.class)
    @SneakyThrows
    public void testEvaluateNoBusinessObjectException(){
        generator.setBusinessObjectMessage(null);
    }
}
