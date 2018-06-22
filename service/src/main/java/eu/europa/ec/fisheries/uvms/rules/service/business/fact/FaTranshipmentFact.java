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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class FaTranshipmentFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType faReportDocumentTypeCode;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<FLUXLocation> faCtchSpecifiedFLUXLocations;
    private List<VesselTransportMeans> relatedVesselTransportMeans;
    private List<FACatch> specifiedFACatches;
    private List<CodeType> faCatchTypeCodes ;
    private List<CodeType> fluxLocationTypeCodes;
    private List<CodeType> vesselTransportMeansRoleCodes;
    private List<CodeType> faCtchSpecifiedFLUXLocationsTypeCodes;
    private List<CodeType> fluxCharacteristicTypeCodes;
    private List<CodeType> facatchSpeciesCode;
    private List<FLUXCharacteristic> specifiedFLUXCharacteristics;
    private List<IdType> specifiedFlCharSpecifiedLocatIDs;
    private List<CodeType> specifiedFlCharSpecifiedLocatTypeCodes;

    public FaTranshipmentFact() {
        setFactType();
    }

    /**
     * This method checks if there are FLUXLocations present for every FaCatch. If not, it will return false.
     * @param specifiedFACatches
     * @return
     */
    public boolean checkFLUXLocationForFaCatch(List<FACatch> specifiedFACatches){
        if (CollectionUtils.isEmpty(specifiedFACatches)) {
            return false;
        }
        for (FACatch faCatch : specifiedFACatches){
            if (CollectionUtils.isEmpty(faCatch.getSpecifiedFLUXLocations())){
                return false;
            }
        }
        return true;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_TRANSHIPMENT;
    }

}
