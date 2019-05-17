/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.uvms.rules.service.business.RuleFromMDR;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProcess;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Local
public interface MDRCacheRuleService {

    List<RuleFromMDR> getFaBrsForBrId(String brId);

    boolean isPresentInMDRList(String listName, String codeValue, DateTime creationDateOfMessage);

    boolean isTypeCodeValuePresentInList(String listName, List<CodeType> typeCodes, DateTime creationDateOfMessage);

    boolean isTypeCodeValuePresentInList(String listName, CodeType typeCode, DateTime creationDateOfMessage);

    boolean isCodeTypePresentInMDRList(List<CodeType> valuesToMatch, DateTime creationDateOfMessage);

    boolean isCodeTypePresentInMDRList(CodeType valuesToMatch, DateTime creationDateOfMessage);

    boolean isCodeTypePresentInMDRList(String listName, List<CodeType> valuesToMatch, DateTime creationDateOfMessage);

    boolean isCodeTypeListIdPresentInMDRList(String listName, List<CodeType> valuesToMatch, DateTime creationDateOfMessage);

    boolean isIdTypePresentInMDRList(String listName, List<IdType> valuesToMatch, DateTime creationDateOfMessage);

    boolean isIdTypePresentInMDRList(List<IdType> ids, DateTime creationDateOfMessage);

    boolean isIdTypePresentInMDRList(IdType id, DateTime creationDateOfMessage);

    boolean combinationExistsInConversionFactorList(List<FLUXLocation> specifiedFLUXLocations, List<CodeType> appliedAAPProcessTypeCodes, CodeType speciesCode, DateTime validityDate, IdType farepDocSpecVesselTrpmRegVesselCountryId);

    boolean combinationExistsInConversionFactorListAndIsGreaterOrEqualToOne(List<FLUXLocation> specifiedFLUXLocations, List<AAPProcess> appliedAAPProceses,
                                                                            CodeType speciesCode, DateTime validityDate, IdType farepDocSpecVesselTrpmRegVesselCountryId);

    String getDataTypeForMDRList(String listName, String codeValue);

    boolean isAllSchemeIdsPresentInMDRList(String listName, List<IdType> idTypes, DateTime creationDateOfMessage);

    boolean isNotMostPreciseFAOArea(IdType id, DateTime creationDateOfMessage);

    boolean validateFormat(List<IdType> ids, DateTime creationDateOfMessage);

    boolean validateSchemeIdFormat(List<IdType> ids, String schemeID, DateTime creationDateOfMessage);

    boolean validateFormat(IdType id, DateTime creationDateOfMessage);

    boolean validateFormat(CodeType codeType, DateTime creationDateOfMessage);

    boolean isLocationNotInCountry(IdType id, IdType countryID, DateTime creationDateOfMessage);

    List<ObjectRepresentation> getObjectRepresentationList(MDRAcronymType mdrAcronym);

    RuleFromMDR getFaBrForBrIdAndContext(String brId, String df);

    List<RuleFromMDR> getFaBrListForBrIdAndContext(String brId, String context);

    RuleFromMDR getFaBrForBrIdAndDataFlow(String brId, String dataFlow, Date factDate);

    String getErrorMessageForBrIAndDFAndValidity(String brid, String df, Date msgDate);

    String getErrorTypeStrForForBrIAndDFAndValidity(String brid, String df, Date msgDate);

    List<String> getDataFlowListForBRId(String brId);

    boolean codeListExistsInMdr(String listName);

    Map<String, List<String>> getRulesContexts();

    boolean doesRuleExistInRulesTable(String brId, String context);

    String findContextForDf(String dataFlow);
}
