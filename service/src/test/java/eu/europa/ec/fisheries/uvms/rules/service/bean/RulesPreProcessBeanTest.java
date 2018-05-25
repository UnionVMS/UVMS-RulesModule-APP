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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

/**
 * Created by padhyad on 6/7/2017.
 */
public class RulesPreProcessBeanTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    RulesPreProcessBean rulesPreProcessBean;

    @Mock
    RulesDomainModel rulesDomainModel;

    @Test
    public void testGetValidationResultNotExist() throws RulesModelException {
        Mockito.doReturn(Collections.emptyList()).when(rulesDomainModel).getValidationMessagesById(Mockito.anyList());

        ValidationResultDto validationResultDto = rulesPreProcessBean.loadValidationResults(Arrays.asList("123", "456"));
        assertTrue(validationResultDto.isOk());
    }

    @Test
    public void testGetValidationResultExist() throws RulesModelException {
        Mockito.doReturn(getMockValidationMessage()).when(rulesDomainModel).getValidationMessagesById(Mockito.anyList());

        ValidationResultDto validationResultDto = rulesPreProcessBean.loadValidationResults(Arrays.asList("123", "456"));
        assertTrue(validationResultDto.isError());
        assertEquals(1, validationResultDto.getValidationMessages().size());
    }

    @Test
    public void testDuplicateIdNotExistInRequest() throws RulesModelException, RulesServiceException {
        Mockito.doReturn(Collections.emptyList()).when(rulesDomainModel).getValidationMessagesById(Mockito.anyList());

        Map<Boolean, ValidationResultDto> map = rulesPreProcessBean.checkDuplicateIdInRequest(getFluxFaReportMessage());
        assertTrue(map.entrySet().iterator().next().getKey());
    }

    @Test
    public void testDuplicateIdExistInFluxMsg() throws RulesModelException, RulesServiceException {
        Mockito.doReturn(getMockValidationMessage()).when(rulesDomainModel).getValidationMessagesById(Mockito.anyList());

        Map<Boolean, ValidationResultDto> map = rulesPreProcessBean.checkDuplicateIdInRequest(getFluxFaReportMessage());
        assertFalse(map.entrySet().iterator().next().getKey());
    }

    private List<ValidationMessageType> getMockValidationMessage() {
        ValidationMessageType validationMessageType = new ValidationMessageType();
        validationMessageType.setBrId("123");
        validationMessageType.setErrorType(ErrorType.ERROR);
        validationMessageType.setLevel("L01");
        validationMessageType.setMessage("Test error");
        return Arrays.asList(validationMessageType);
    }

    private FLUXFAReportMessage getFluxFaReportMessage() {
        FLUXReportDocument fluxReportDocument = new FLUXReportDocument();
        IDType id = new IDType();
        id.setSchemeID("123-abc");
        id.setValue("val1");
        fluxReportDocument.setIDS(Arrays.asList(id));


        FLUXFAReportMessage msg = new FLUXFAReportMessage();
        msg.setFLUXReportDocument(fluxReportDocument);

        FAReportDocument doc = new FAReportDocument();
        doc.setRelatedFLUXReportDocument(fluxReportDocument);
        msg.setFAReportDocuments(Arrays.asList(doc));
        return msg;
    }
}
