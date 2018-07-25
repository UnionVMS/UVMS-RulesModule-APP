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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselStorageCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

@Data
@EqualsAndHashCode(callSuper = true)
public class FaRelocationFact extends AbstractFact {

    private List<CodeType> specifiedFACatchTypeCodes;
    private List<CodeType> relatedFLUXLocationTypeCodes;
    private List<CodeType> specifiedFACatchSpeciesCodes;
    private List<CodeType> destinationVesselStorageCharacteristicTypeCodes;
    private IdType destinationVesselStorageCharacteristicID;
    private List<IdType> relatedVesselTransportMeansIDs;
    private List<CodeType> sourceVesselStorageCharacteristicTypeCodes;
    private List<CodeType> relatedVesselTransportMeansRoleCodes;
    private List<CodeType> specifiedFACatchDestinationFluxLocationTypeCodes;
    private List<IdType> specifiedFACatchDestinationFluxLocationIDs;
    private List<CodeType> relatedVesselTransportMeansContactPartyRoleCodes;
    private List<TextType> relatedVesselTransportMeansNames;
    private List<FACatch> specifiedFACatches;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<FLUXLocation> destinationFLUXLocations;
    private List<VesselTransportMeans> relatedVesselTransportMeans;
    private VesselStorageCharacteristic destinationVesselStorageCharacteristic;
    private VesselStorageCharacteristic sourceVesselStorageCharacteristic;
    private List<IdType> faReportDocumentRelatedReportIds;
    private boolean isSubActivity;

    public FaRelocationFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_RELOCATION;
    }
}
