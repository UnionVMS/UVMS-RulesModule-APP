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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

/**
 * @autor padhyad
 * @author Gregory Rinaldi
 */
public class FaTranshipmentFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;

    private CodeType faReportDocumentTypeCode;

    private List<FLUXLocation> relatedFLUXLocations;

    private List<VesselTransportMeans> relatedVesselTransportMeans;

    private List<FACatch> specifiedFACatches;

    public FaTranshipmentFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_TRANSHIPMENT;
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

    public List<VesselTransportMeans> getRelatedVesselTransportMeans() {
        return relatedVesselTransportMeans;
    }

    public void setRelatedVesselTransportMeans(List<VesselTransportMeans> relatedVesselTransportMeans) {
        this.relatedVesselTransportMeans = relatedVesselTransportMeans;
    }

    public List<FACatch> getSpecifiedFACatches() {
        return specifiedFACatches;
    }

    public void setSpecifiedFACatches(List<FACatch> specifiedFACatches) {
        this.specifiedFACatches = specifiedFACatches;
    }
}
