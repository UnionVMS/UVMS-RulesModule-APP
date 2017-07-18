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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FaNotificationOfTranshipmentFact extends AbstractFact {


    private CodeType fishingActivityTypeCode;

    private CodeType faReportDocumentTypeCode;

    private List<CodeType> faCatchTypeCode;

    private List<FLUXLocation> relatedFLUXLocations;

    private List<CodeType> fluxLocationTypeCodes;

    private List<CodeType> vesselTransportMeansRoleCodes;

    public List<CodeType> getFluxLocationTypeCodes() {
        return fluxLocationTypeCodes;
    }

    public void setFluxLocationTypeCodes(List<CodeType> fluxLocationTypeCodes) {
        this.fluxLocationTypeCodes = fluxLocationTypeCodes;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    private List<CodeType> fluxLocationTypeCode;

    private List<VesselTransportMeans> relatedVesselTransportMeans;

    private List<CodeType> vesselTransportMeansRoleCode;

    private List<MeasureType> fluxCharacteristicValueQuantity;

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

    public List<CodeType> getFaCatchTypeCode() {
        return faCatchTypeCode;
    }

    public void setFaCatchTypeCode(List<CodeType> faCatchTypeCode) {
        this.faCatchTypeCode = faCatchTypeCode;
    }

    public List<CodeType> getFluxLocationTypeCode() {
        return fluxLocationTypeCode;
    }

    public void setFluxLocationTypeCode(List<CodeType> fluxLocationTypeCode) {
        this.fluxLocationTypeCode = fluxLocationTypeCode;
    }

    public List<VesselTransportMeans> getRelatedVesselTransportMeans() {
        return relatedVesselTransportMeans;
    }

    public void setRelatedVesselTransportMeans(List<VesselTransportMeans> relatedVesselTransportMeans) {
        this.relatedVesselTransportMeans = relatedVesselTransportMeans;
    }

    public List<CodeType> getVesselTransportMeansRoleCode() {
        return vesselTransportMeansRoleCode;
    }

    public void setVesselTransportMeansRoleCode(List<CodeType> vesselTransportMeansRoleCode) {
        this.vesselTransportMeansRoleCode = vesselTransportMeansRoleCode;
    }

    public List<MeasureType> getFluxCharacteristicValueQuantity() {
        return fluxCharacteristicValueQuantity;
    }

    public List<CodeType> getVesselTransportMeansRoleCodes() {
        return vesselTransportMeansRoleCodes;
    }

    public void setVesselTransportMeansRoleCodes(List<CodeType> vesselTransportMeansRoleCodes) {
        this.vesselTransportMeansRoleCodes = vesselTransportMeansRoleCodes;
    }

    public void setFluxCharacteristicValueQuantity(List<MeasureType> fluxCharacteristicValueQuantity) {
        this.fluxCharacteristicValueQuantity = fluxCharacteristicValueQuantity;
    }

    public FaNotificationOfTranshipmentFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_NOTIFICATION_OF_TRANSHIPMENT;
    }
}
