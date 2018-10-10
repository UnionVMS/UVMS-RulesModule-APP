/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import static org.jgroups.util.Util.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class RulesFLUXMessageHelperTest {

    @Mock
    private RulesConfigurationCache ruleModuleCache;

    @InjectMocks
    private RulesFLUXMessageHelper helper;

    @Test
    public void testGenerateFluxResponseMessageForFaReport() throws Exception {

        FLUXReportDocument fluxReportDocument = new FLUXReportDocument();
        un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType idType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType();
        idType.setValue("idValue");
        List<IDType> idTypes = Collections.singletonList(idType);
        fluxReportDocument.setIDS(idTypes);
        FLUXFAReportMessage fluxfaReportMessage = new FLUXFAReportMessage();
        fluxfaReportMessage.setFLUXReportDocument(fluxReportDocument);

        ValidationResult validationResult = new ValidationResult();
        validationResult.setError(true);
        ValidationMessageType validationMessageType = new ValidationMessageType();
        validationMessageType.setBrId("123");
        validationMessageType.setErrorType(ErrorType.ERROR);
        validationResult.setValidationMessages(Collections.singletonList(validationMessageType));
        FLUXResponseMessage outgoingFluxResponseMessage = helper.generateFluxResponseMessageForFaReport(validationResult, fluxfaReportMessage);

        assertEquals(outgoingFluxResponseMessage.getFLUXResponseDocument().getReferencedID().getValue(), idTypes.get(0).getValue());
        assertEquals("UUID", outgoingFluxResponseMessage.getFLUXResponseDocument().getIDS().get(0).getSchemeID());
        assertNotNull(outgoingFluxResponseMessage.getFLUXResponseDocument().getIDS().get(0).getValue());
        assertNotNull(outgoingFluxResponseMessage.getFLUXResponseDocument().getCreationDateTime());
        assertEquals("NOK", outgoingFluxResponseMessage.getFLUXResponseDocument().getResponseCode().getValue());
        assertEquals("FLUX_GP_RESPONSE", outgoingFluxResponseMessage.getFLUXResponseDocument().getResponseCode().getListID());
        assertEquals("123", outgoingFluxResponseMessage.getFLUXResponseDocument().getRelatedValidationResultDocuments().get(0).getRelatedValidationQualityAnalysises().get(0).getID().getValue());
        assertEquals("FA_BR", outgoingFluxResponseMessage.getFLUXResponseDocument().getRelatedValidationResultDocuments().get(0).getRelatedValidationQualityAnalysises().get(0).getID().getSchemeID());

    }

    @Test
    public void testWithEmptyFLUXFAReportMessage(){
        FLUXFAReportMessage fluxfaReportMessage = new FLUXFAReportMessage();
        Set<FADocumentID> ids = helper.mapToFADocumentID(fluxfaReportMessage);
        assertTrue(CollectionUtils.isEmpty(ids));
    }

    @Test
    public void testWithNull(){
        Set<FADocumentID> ids = helper.mapToFADocumentID(null);
        assertTrue(CollectionUtils.isEmpty(ids));
    }

}
