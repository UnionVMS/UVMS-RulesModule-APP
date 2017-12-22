/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CodeTypeMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CodeTypeMapperImpl;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQueryParameter;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.MeasureType;

import javax.xml.datatype.DatatypeFactory;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by kovian on 6/7/2017.
 */
public class RulesMessageServiceBeanTest {

    String testXmlPath = "src/test/resources/testData/fluxFaResponseMessage.xml";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    RulesMessageServiceBean messageServiceBean;

    @Mock
    RulesMessageProducer producer;

    @Mock
    RulesEngineBean rulesEngine;

    @Spy
    private CodeTypeMapper mapper = new CodeTypeMapperImpl();

    @Mock
    RulePostProcessBean rulePostprocessBean;

    @Mock
    RulesConfigurationCache ruleModuleCache;

    private IDType idType;

    private CodeType codeType;
    private DelimitedPeriod delimitedPeriod;
    private DateTimeType dateTimeType;
    private Date date;
    private FAQuery faQuery;
    private List<FAQueryParameter> faQueryParameterList;
    private FLUXFAQueryMessage fluxfaQueryMessage;

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

        faQueryParameterList = new ArrayList<>();
        FAQueryParameter faQueryParameter = new FAQueryParameter();
        faQueryParameter.setTypeCode(codeType);
        faQueryParameter.setValueCode(codeType);
        faQueryParameter.setValueDateTime(dateTimeType);
        faQueryParameter.setValueID(idType);
        faQueryParameterList.add(faQueryParameter);

        faQuery = new FAQuery();
        faQuery.setID(idType);
        faQuery.setSimpleFAQueryParameters(faQueryParameterList);
        faQuery.setSpecifiedDelimitedPeriod(delimitedPeriod);
        faQuery.setSubmittedDateTime(dateTimeType);
        faQuery.setTypeCode(codeType);

        FLUXFAQueryMessage message = new FLUXFAQueryMessage();
        message.setFAQuery(faQuery);

        fluxfaQueryMessage = new FLUXFAQueryMessage();
        fluxfaQueryMessage.setFAQuery(faQuery);

    }

    @Test
    public void testGenerateFluxResponseMessage() {
        when(ruleModuleCache.getSingleConfig(any(String.class))).thenReturn("XEU");
        FLUXResponseMessage fluxResponseMessage = messageServiceBean.generateFluxResponseMessage(getValidationResult(), getFluxFaReportMessage());
        assertNotNull(fluxResponseMessage);
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getIDS());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getReferencedID());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRelatedValidationResultDocuments());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRejectionReason());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRespondentFLUXParty());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getResponseCode());
    }

    @Test
    public void testGenerateFluxResponseMessageForFaQuery() {
        when(ruleModuleCache.getSingleConfig(any(String.class))).thenReturn("XEU");
        FLUXResponseMessage fluxResponseMessage = messageServiceBean.generateFluxResponseMessage(getValidationResult(), fluxfaQueryMessage);
        assertNotNull(fluxResponseMessage);
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getIDS());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getReferencedID());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRelatedValidationResultDocuments());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRejectionReason());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getRespondentFLUXParty());
        assertNotNull(fluxResponseMessage.getFLUXResponseDocument().getResponseCode());
    }

    @Test(expected = NullPointerException.class)
    public void testSetFLUXFAReportMessageReceivedNULL(){
        messageServiceBean.receiveFLUXFAReportRequest(null);
    }

    @Test(expected = NullPointerException.class)
    @SneakyThrows
    public void testSetFLUXFAReportMessageReceived(){

        SetFLUXFAReportMessageRequest req = new SetFLUXFAReportMessageRequest();
        req.setRequest(IOUtils.toString(new FileInputStream(testXmlPath)));
        req.setType(PluginType.MANUAL);
        req.setMethod(RulesModuleMethod.SET_FLUX_FA_REPORT);
        req.setLogGuid("SOME-GUID");
        messageServiceBean.receiveFLUXFAReportRequest(req);

    }

    @Test
    public void testSendRequestToActivity() throws RulesServiceException, MessageException {
        Mockito.doReturn("abc-def").when(producer).sendDataSourceMessage(Mockito.anyString(), any(DataSourceQueue.class));
        messageServiceBean.sendRequestToActivity("<FLUXFaReportMessage></FLUXFaReportMessage>", "test", PluginType.FLUX);
    }

    @Test
    public void testSendResponseToExchange() throws RulesServiceException, RulesValidationException {
        when(ruleModuleCache.getSingleConfig(any(String.class))).thenReturn("XEU");
        FLUXResponseMessage fluxResponseMessage = messageServiceBean.generateFluxResponseMessage(getValidationResult(), getFluxFaReportMessage());
        Mockito.doReturn(emptyList()).when(rulesEngine).evaluate(BusinessObjectType.FLUX_ACTIVITY_RESPONSE_MSG, fluxResponseMessage, null);
        Mockito.doReturn(getValidationResult()).when(rulePostprocessBean).checkAndUpdateValidationResult(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.any(RawMsgType.class));
        RulesBaseRequest request = new SetFLUXFAReportMessageRequest();
        request.setUsername("USER1");
        messageServiceBean.sendResponseToExchange(fluxResponseMessage, request, PluginType.FLUX);
    }

    @Test
    public void guidOrFluxOnValueWhenBrIdIsInListOfIds() throws Exception {
        ValidationMessageType validationMessageType = new ValidationMessageType();
        validationMessageType.setBrId("SALE-L01-00-0011");

        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setValidationMessages(Arrays.asList(validationMessageType));


        boolean guidOrFluxOnValue = messageServiceBean.shouldUseFluxOn(validationResultDto);

        assertEquals(true, guidOrFluxOnValue);
    }

    @Test
    public void guidOrFluxOnValueWhenBrIdIsNotInListOfIds() throws Exception {
        ValidationMessageType validationMessageType = new ValidationMessageType();
        validationMessageType.setBrId("SALE-L01-00-0012");

        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setValidationMessages(Arrays.asList(validationMessageType));


        boolean guidOrFluxOnValue = messageServiceBean.shouldUseFluxOn(validationResultDto);

        assertEquals(false, guidOrFluxOnValue);
    }

    private ValidationResultDto getValidationResult() {
        ValidationResultDto faReportValidationResult = new ValidationResultDto();
        faReportValidationResult.setIsError(true);
        faReportValidationResult.setIsOk(false);
        faReportValidationResult.setIsWarning(false);

        ValidationMessageType validationMessageType = new ValidationMessageType();
        validationMessageType.setBrId("brid1");
        validationMessageType.setErrorType(ErrorType.ERROR);
        validationMessageType.setLevel("L01");
        validationMessageType.setMessage("Test message");
        faReportValidationResult.setValidationMessages(singletonList(validationMessageType));
        return faReportValidationResult;
    }

    private FLUXFAReportMessage getFluxFaReportMessage() {
        FLUXReportDocument fluxReportDocument = new FLUXReportDocument();
        IDType id = new IDType();
        id.setSchemeID("123-abc");
        id.setValue("val1");
        fluxReportDocument.setIDS(singletonList(id));


        FLUXFAReportMessage msg = new FLUXFAReportMessage();
        msg.setFLUXReportDocument(fluxReportDocument);

        FAReportDocument doc = new FAReportDocument();
        doc.setRelatedFLUXReportDocument(fluxReportDocument);
        msg.setFAReportDocuments(singletonList(doc));
        return msg;
    }


    @SneakyThrows
    private FLUXFAReportMessage loadTestData(String filePath) {
        String fluxFaMessageStr = IOUtils.toString(new FileInputStream(filePath));
        return JAXBUtils.unMarshallMessage(fluxFaMessageStr, FLUXFAReportMessage.class);
    }


}
