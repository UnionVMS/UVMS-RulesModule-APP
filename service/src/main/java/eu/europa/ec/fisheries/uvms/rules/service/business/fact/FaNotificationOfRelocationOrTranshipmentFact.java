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

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class FaNotificationOfRelocationOrTranshipmentFact extends AbstractRelocationOrTranshipmentFact {

    @Override
    public void setFactType() {
        this.factType = FactType.FA_NOTIFICATION_OF_TRANSHIPMENT_OR_RELOCATION;
    }

    public boolean thereAreAtLeastTwoDifferentTypeCodesInList(List<CodeType> faCatchTypeCodes){
        if(CollectionUtils.isEmpty(faCatchTypeCodes)){
            return false;
        }
        List<CodeType> notNullOrEmptyTypeCodes = faCatchTypeCodes.stream().filter(typCode -> typCode != null && StringUtils.isNotEmpty(typCode.getValue())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(faCatchTypeCodes)){
            return false;
        }
        String firstCode = notNullOrEmptyTypeCodes.get(0).getValue();
        for (CodeType faCatchTypeCode : notNullOrEmptyTypeCodes) {
            if(!firstCode.equals(faCatchTypeCode.getValue())){ // If there is one code != then the first one then not all are equal => hence we have at least 2 different codes!
                return true;
            }
        }
        return false;
    }

}
