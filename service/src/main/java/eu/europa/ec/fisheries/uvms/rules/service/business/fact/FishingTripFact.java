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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class FishingTripFact extends AbstractFact {

    private List<IdType> ids;
    private CodeType typeCode;

    public FishingTripFact() {
        setFactType();
    }

    public boolean containsMoreThanOneOccurrenceOfIdForGivenSchemeId(){
        if(CollectionUtils.isEmpty(ids)){
            return true;
        }
        List<String> schemeIds = new ArrayList<>();
        List<IdType> duplicatedIds = ids.stream().filter(id -> {
            boolean isPresent = false;
            if (id != null) {
                if (schemeIds.contains(id.getSchemeId())) {
                    isPresent = true;
                }
                schemeIds.add(id.getSchemeId());
            }
            return isPresent;
        }).collect(Collectors.toList());
        return duplicatedIds.size() > 0;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FISHING_TRIP;
    }

}
