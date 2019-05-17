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
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FaExitFromSeaFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType faReportDocumentTypeCode;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<CodeType> specifiedFACatchesTypeCodes;
    private List<CodeType> relatedFluxLocationTypeCodes;
    private List<IdType> relatedFluxLocationIDs;
    private List<FishingActivity> relatedFishingActivities;

    public FaExitFromSeaFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_EXIT_FROM_SEA;
    }

    public CodeType getFishingActivityTypeCode() {
        return fishingActivityTypeCode;
    }

    public void setFishingActivityTypeCode(CodeType fishingActivityTypeCode) {
        this.fishingActivityTypeCode = fishingActivityTypeCode;
    }

    public CodeType getFaReportDocumentTypeCode() {
        return faReportDocumentTypeCode;
    }

    public void setFaReportDocumentTypeCode(CodeType faReportDocumentTypeCode) {
        this.faReportDocumentTypeCode = faReportDocumentTypeCode;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public List<CodeType> getSpecifiedFACatchesTypeCodes() {
        return specifiedFACatchesTypeCodes;
    }

    public void setSpecifiedFACatchesTypeCodes(List<CodeType> specifiedFACatchesTypeCodes) {
        this.specifiedFACatchesTypeCodes = specifiedFACatchesTypeCodes;
    }

    public void setRelatedFluxLocationTypeCodes(List<CodeType> relatedFluxLocationTypeCodes) {
        this.relatedFluxLocationTypeCodes = relatedFluxLocationTypeCodes;
    }

    public List<IdType> getRelatedFluxLocationIDs() {
        return relatedFluxLocationIDs;
    }

    public void setRelatedFluxLocationIDs(List<IdType> relatedFluxLocationIDs) {
        this.relatedFluxLocationIDs = relatedFluxLocationIDs;
    }

    public List<CodeType> getRelatedFluxLocationTypeCodes() {
        return relatedFluxLocationTypeCodes;
    }

    public List<FishingActivity> getRelatedFishingActivities() {
        return relatedFishingActivities;
    }

    public void setRelatedFishingActivities(List<FishingActivity> relatedFishingActivities) {
        this.relatedFishingActivities = relatedFishingActivities;
    }

    /**
     * For  Rule FA-L02-00-0251
     *
     * @return
     */
    public boolean verifyFLUXLocationIDValue(List<FLUXLocation> relatedFLUXLocations, String locationTypeCode, String locationId) {
        if (CollectionUtils.isEmpty(relatedFLUXLocations)) {
            return false;
        }
        for (FLUXLocation fluxLocation : relatedFLUXLocations) {
            if (fluxLocation.getTypeCode() != null && locationTypeCode.equals(fluxLocation.getTypeCode().getValue())) {
                IDType idType = fluxLocation.getID();
                if (idType == null || !locationId.equals(idType.getSchemeID())) {
                    return false;
                }
            }
        }
        return true;
    }

}
