/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.Local;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.EnrichedBRMessage;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

@Local
public interface MDRCacheRuleService {

    void loadCacheForFailureMessages();

    EnrichedBRMessage getErrorMessageForBrId(String brId);

    @Deprecated
    boolean isPresentInMDRList(String listName, String codeValue);

    boolean isPresentInMDRList(String listName, String codeValue, DateTime creationDateOfMessage);

    @Deprecated
    boolean isTypeCodeValuePresentInList(String listName, List<CodeType> typeCodes);

    boolean isTypeCodeValuePresentInList(String listName, List<CodeType> typeCodes, DateTime creationDateOfMessage);

    @Deprecated
    boolean isTypeCodeValuePresentInList(String listName, CodeType typeCode);

    boolean isTypeCodeValuePresentInList(String listName, CodeType typeCode, DateTime creationDateOfMessage);

    @Deprecated
    boolean isCodeTypePresentInMDRList(List<CodeType> valuesToMatch);

    boolean isCodeTypePresentInMDRList(List<CodeType> valuesToMatch, DateTime creationDateOfMessage);

    @Deprecated
    boolean isCodeTypePresentInMDRList(String listName, List<CodeType> valuesToMatch);

    boolean isCodeTypePresentInMDRList(String listName, List<CodeType> valuesToMatch, DateTime creationDateOfMessage);

    boolean isCodeTypeListIdPresentInMDRList(String listName, List<CodeType> valuesToMatch);

    @Deprecated
    boolean isIdTypePresentInMDRList(String listName, List<IdType> valuesToMatch);

    boolean isIdTypePresentInMDRList(String listName, List<IdType> valuesToMatch, DateTime creationDateOfMessage);

    @Deprecated
    boolean isIdTypePresentInMDRList(List<IdType> ids);

    boolean isIdTypePresentInMDRList(List<IdType> ids, DateTime creationDateOfMessage);

    @Deprecated
    boolean isIdTypePresentInMDRList(IdType id);

    boolean isIdTypePresentInMDRList(IdType id, DateTime creationDateOfMessage);

    boolean isSchemeIdPresentInMDRList(String listName, IdType idType);

    String getDataTypeForMDRList(String listName, String codeValue);

    String getValueForListId(String listId, List<CodeType> typeCodes);

    boolean isAllSchemeIdsPresentInMDRList(String listName, List<IdType> idTypes);

    boolean combinationExistsInConversionFactorList(List<FLUXLocation> specifiedFLUXLocations,  List<CodeType> appliedAAPProcessTypeCodes, CodeType speciesCode);

    boolean isNotMostPreciseFAOArea(IdType id, DateTime creationDateOfMessage);

    boolean isLocationNotInCountry(IdType id, IdType countryID, DateTime creationDateOfMessage);

    List<ObjectRepresentation> getObjectRepresentationList(MDRAcronymType mdrAcronym);
}
