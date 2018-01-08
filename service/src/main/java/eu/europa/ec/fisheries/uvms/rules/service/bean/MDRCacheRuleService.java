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

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;

@Local
public interface MDRCacheRuleService {

    boolean isPresentInMDRList(String listName, String codeValue);

    boolean isCodeTypePresentInMDRList(List<CodeType> valuesToMatch);

    boolean isCodeTypePresentInMDRList(String listName, List<CodeType> valuesToMatch);

    boolean isCodeTypeListIdPresentInMDRList(String listName, List<CodeType> valuesToMatch);

    boolean isIdTypePresentInMDRList(String listName, List<IdType> valuesToMatch);

    boolean isIdTypePresentInMDRList(List<IdType> ids);

    boolean isIdTypePresentInMDRList(IdType id);

    boolean isSchemeIdPresentInMDRList(String listName, IdType idType);

    String getDataTypeForMDRList(String listName, String codeValue);

    String getValueForListId(String listId, List<CodeType> typeCodes);

    boolean isTypeCodeValuePresentInList(String listName, List<CodeType> typeCodes);

    boolean isTypeCodeValuePresentInList(String listName, CodeType typeCode);

    boolean isAllSchemeIdsPresentInMDRList(String listName, List<IdType> idTypes);

}
