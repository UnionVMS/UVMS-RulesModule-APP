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

import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXGeographicalCoordinate;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.StructuredAddress;

@Data
@EqualsAndHashCode(callSuper = false)
public class FluxLocationFact extends AbstractFact {

    private CodeType typeCode;

    private String activityType;
    private String faRepDocType;
    private FACatch faCatch;

    private IdType countryID;
    private IdType id;
    private FLUXGeographicalCoordinate specifiedPhysicalFLUXGeographicalCoordinate;
    private StructuredAddress physicalStructuredAddress;
    private List<CodeType> applicableFLUXCharacteristicTypeCode;
    private boolean isSpecifiedFluxLocFromFaCatch;
    private CodeType rfmo;

    public FluxLocationFact() {
        setFactType();
    }

    public boolean getIsSpecifiedFluxLocFromFaCatch() {
        return isSpecifiedFluxLocFromFaCatch;
    }

    public boolean faCatchContainsSpecifiedFluxLocationOfType(String typeStr){
        if(faCatch == null || CollectionUtils.isEmpty(faCatch.getSpecifiedFLUXLocations()) || StringUtils.isEmpty(typeStr)){
            return false;
        }
        boolean itContains = false;
        for (FLUXLocation specifiedFLUXLocation : faCatch.getSpecifiedFLUXLocations()) {
            if(specifiedFLUXLocation.getTypeCode() != null && typeStr.equals(specifiedFLUXLocation.getTypeCode().getValue())){
                itContains = true;
                break;
            }
        }
        return itContains;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FLUX_LOCATION;
    }
}
