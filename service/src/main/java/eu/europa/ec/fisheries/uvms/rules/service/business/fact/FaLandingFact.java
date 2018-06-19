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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 *
 * @author patilva
 */
public class FaLandingFact extends AbstractFact {

    private CodeType fishingActivityCodeType;

    private CodeType faReportDocumentTypeCode;

    private List<FLUXLocation> relatedFluxLocations;

    private List<CodeType> specifiedFaCatchFluxLocationTypeCode;

    private List<FACatch> specifiedFaCatches;

    private List<FLUXLocation> specifiedFaCatchesSpecifiedFLUXLocations;

    private List<CodeType> specifiedFaCatchTypeCode;

    private List<CodeType> relatedFluxLocationTypeCodes;

    public List<CodeType> getRelatedFluxLocationTypeCodes() {
        return relatedFluxLocationTypeCodes;
    }

    public void setRelatedFluxLocationTypeCodes(List<CodeType> relatedFluxLocationTypeCodes) {
        this.relatedFluxLocationTypeCodes = relatedFluxLocationTypeCodes;
    }

    public List<FLUXLocation> getSpecifiedFaCatchesSpecifiedFLUXLocations() {
        return specifiedFaCatchesSpecifiedFLUXLocations;
    }

    public void setSpecifiedFaCatchesSpecifiedFLUXLocations(List<FLUXLocation> specifiedFaCatchesSpecifiedFLUXLocations) {
        this.specifiedFaCatchesSpecifiedFLUXLocations = specifiedFaCatchesSpecifiedFLUXLocations;
    }

    public List<CodeType> getSpecifiedFaCatchFluxLocationTypeCode() {
        return specifiedFaCatchFluxLocationTypeCode;
    }

    public void setSpecifiedFaCatchFluxLocationTypeCode(List<CodeType> specifiedFaCatchFluxLocationTypeCode) {
        this.specifiedFaCatchFluxLocationTypeCode = specifiedFaCatchFluxLocationTypeCode;
    }

    public CodeType getFishingActivityCodeType() {
        return fishingActivityCodeType;
    }

    public void setFishingActivityCodeType(CodeType fishingActivityCodeType) {
        this.fishingActivityCodeType = fishingActivityCodeType;
    }

    public CodeType getFaReportDocumentTypeCode() {
        return faReportDocumentTypeCode;
    }

    public void setFaReportDocumentTypeCode(CodeType faReportDocumentTypeCode) {
        this.faReportDocumentTypeCode = faReportDocumentTypeCode;
    }

    public List<FLUXLocation> getRelatedFluxLocations() {
        return relatedFluxLocations;
    }

    public void setRelatedFluxLocations(List<FLUXLocation> relatedFluxLocations) {
        this.relatedFluxLocations = relatedFluxLocations;
    }

    public List<FACatch> getSpecifiedFaCatches() {
        return specifiedFaCatches;
    }

    public void setSpecifiedFaCatches(List<FACatch> specifiedFaCatches) {
        this.specifiedFaCatches = specifiedFaCatches;
    }

    public List<CodeType> getSpecifiedFaCatchTypeCode() {
        return specifiedFaCatchTypeCode;
    }

    public void setSpecifiedFaCatchTypeCode(List<CodeType> specifiedFaCatchTypeCode) {
        this.specifiedFaCatchTypeCode = specifiedFaCatchTypeCode;
    }

    public FaLandingFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_LANDING;
    }
}
