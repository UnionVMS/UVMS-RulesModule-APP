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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@Data
@EqualsAndHashCode(callSuper = true)
public class FaEntryToSeaFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType faReportDocumentTypeCode;
    private List<FLUXLocation> relatedFLUXLocations;
    private CodeType reasonCode;
    private CodeType speciesTargetCode;
    private List<CodeType> specifiedFACatchesTypeCodes;
    private List<FACatch> specifiedFACatches;
    private List<CodeType> relatedFluxLocationTypeCodes;
    private List<IdType> relatedFluxLocationIDs;
    private List<FishingActivity> relatedFishingActivities;

    public FaEntryToSeaFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_ENTRY_TO_SEA;
    }

    public boolean verifyFLUXLocationIDValue(List<FLUXLocation> relatedFLUXLocations){
        if (CollectionUtils.isEmpty(relatedFLUXLocations)){
            return false;
        }
        if (faReportDocumentTypeCode!=null && faReportDocumentTypeCode.getValue().equals("DECLARATION")) {
            for (FLUXLocation fluxLocation : relatedFLUXLocations) {
                if (fluxLocation.getTypeCode() != null && "AREA".equals(fluxLocation.getTypeCode().getValue())) {
                    IDType idType = fluxLocation.getID();
                    if (idType == null || !"EFFORT_ZONE".equals(idType.getSchemeID())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
