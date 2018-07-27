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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLAPDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

@Data
@EqualsAndHashCode(callSuper = false)
public class FaNotificationOfTranshipmentFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType faReportDocumentTypeCode;
    private List<CodeType> faCatchTypeCode;
    private List<CodeType> faCatchSpeciesCodes;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<CodeType> fluxLocationTypeCode;
    private List<CodeType> vesselTransportMeansRoleCodes;
    private List<VesselTransportMeans> relatedVesselTransportMeans;
    private List<MeasureType> fluxCharacteristicValueQuantity;
    private List<FLUXCharacteristic> specifiedFLUXCharacteristics;
    private List<FACatch> specifiedFACatches;
    private List<CodeType> faCatchSpecifiedFLUXLocationsTypeCodes;
    private List<CodeType> fluxCharacteristicTypeCodes;
    private List<FLAPDocument> specifiedFLAPDocuments;
    private List<IdType> flapDocumentIdTypes;

    /**
     * This method checks if Every FACatch with typeCode LOADED has atleast one AREA FluxLocation
     * IF yes, it returns true, else false.
     * @param specifiedFACatches
     * @return
     */
    public boolean ifFLUXLocationForFACatchIsAREA(List<FACatch> specifiedFACatches){
        if (CollectionUtils.isEmpty(specifiedFACatches)){
            return false;
        }
        boolean isPresent = true;
        for (FACatch faCatch : specifiedFACatches){
            if (faCatch.getTypeCode()!=null && faCatch.getTypeCode().getValue().equals("LOADED") )  {
                isPresent = false;
                 if (CollectionUtils.isEmpty(faCatch.getSpecifiedFLUXLocations())){
                     return false;
                 }
                 for (FLUXLocation fluxLocation : faCatch.getSpecifiedFLUXLocations()){
                     if (fluxLocation.getTypeCode()!=null && fluxLocation.getTypeCode().getValue().equals("AREA")){
                         isPresent = true;
                         break;
                     }
                 }
            }
        }
        return isPresent;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_NOTIFICATION_OF_TRANSHIPMENT;
    }

}
