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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselStorageCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
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

    public FaRelocationFact() {
        setFactType();
    }

    public List<CodeType> getSpecifiedFACatchTypeCodes() {
        return specifiedFACatchTypeCodes;
    }

    public void setSpecifiedFACatchTypeCodes(List<CodeType> specifiedFACatchTypeCodes) {
        this.specifiedFACatchTypeCodes = specifiedFACatchTypeCodes;
    }

    public List<CodeType> getRelatedFLUXLocationTypeCodes() {
        return relatedFLUXLocationTypeCodes;
    }

    public void setRelatedFLUXLocationTypeCodes(List<CodeType> relatedFLUXLocationTypeCodes) {
        this.relatedFLUXLocationTypeCodes = relatedFLUXLocationTypeCodes;
    }

    public List<CodeType> getSpecifiedFACatchSpeciesCodes() {
        return specifiedFACatchSpeciesCodes;
    }

    public void setSpecifiedFACatchSpeciesCodes(List<CodeType> specifiedFACatchSpeciesCodes) {
        this.specifiedFACatchSpeciesCodes = specifiedFACatchSpeciesCodes;
    }

    public List<CodeType> getDestinationVesselStorageCharacteristicTypeCodes() {
        return destinationVesselStorageCharacteristicTypeCodes;
    }

    public void setDestinationVesselStorageCharacteristicTypeCodes(List<CodeType> destinationVesselStorageCharacteristicTypeCodes) {
        this.destinationVesselStorageCharacteristicTypeCodes = destinationVesselStorageCharacteristicTypeCodes;
    }

    public IdType getDestinationVesselStorageCharacteristicID() {
        return destinationVesselStorageCharacteristicID;
    }

    public void setDestinationVesselStorageCharacteristicID(IdType destinationVesselStorageCharacteristicID) {
        this.destinationVesselStorageCharacteristicID = destinationVesselStorageCharacteristicID;
    }

    public List<IdType> getRelatedVesselTransportMeansIDs() {
        return relatedVesselTransportMeansIDs;
    }

    public void setRelatedVesselTransportMeansIDs(List<IdType> relatedVesselTransportMeansIDs) {
        this.relatedVesselTransportMeansIDs = relatedVesselTransportMeansIDs;
    }

    public List<CodeType> getSourceVesselStorageCharacteristicTypeCodes() {
        return sourceVesselStorageCharacteristicTypeCodes;
    }

    public void setSourceVesselStorageCharacteristicTypeCodes(List<CodeType> sourceVesselStorageCharacteristicTypeCodes) {
        this.sourceVesselStorageCharacteristicTypeCodes = sourceVesselStorageCharacteristicTypeCodes;
    }

    public List<CodeType> getRelatedVesselTransportMeansRoleCodes() {
        return relatedVesselTransportMeansRoleCodes;
    }

    public void setRelatedVesselTransportMeansRoleCodes(List<CodeType> relatedVesselTransportMeansRoleCodes) {
        this.relatedVesselTransportMeansRoleCodes = relatedVesselTransportMeansRoleCodes;
    }

    public List<CodeType> getSpecifiedFACatchDestinationFluxLocationTypeCodes() {
        return specifiedFACatchDestinationFluxLocationTypeCodes;
    }

    public void setSpecifiedFACatchDestinationFluxLocationTypeCodes(List<CodeType> specifiedFACatchDestinationFluxLocationTypeCodes) {
        this.specifiedFACatchDestinationFluxLocationTypeCodes = specifiedFACatchDestinationFluxLocationTypeCodes;
    }

    public List<IdType> getSpecifiedFACatchDestinationFluxLocationIDs() {
        return specifiedFACatchDestinationFluxLocationIDs;
    }

    public void setSpecifiedFACatchDestinationFluxLocationIDs(List<IdType> specifiedFACatchDestinationFluxLocationIDs) {
        this.specifiedFACatchDestinationFluxLocationIDs = specifiedFACatchDestinationFluxLocationIDs;
    }

    public List<CodeType> getRelatedVesselTransportMeansContactPartyRoleCodes() {
        return relatedVesselTransportMeansContactPartyRoleCodes;
    }

    public void setRelatedVesselTransportMeansContactPartyRoleCodes(List<CodeType> relatedVesselTransportMeansContactPartyRoleCodes) {
        this.relatedVesselTransportMeansContactPartyRoleCodes = relatedVesselTransportMeansContactPartyRoleCodes;
    }

    public List<TextType> getRelatedVesselTransportMeansNames() {
        return relatedVesselTransportMeansNames;
    }

    public void setRelatedVesselTransportMeansNames(List<TextType> relatedVesselTransportMeansNames) {
        this.relatedVesselTransportMeansNames = relatedVesselTransportMeansNames;
    }

    public List<FACatch> getSpecifiedFACatches() {
        return specifiedFACatches;
    }

    public void setSpecifiedFACatches(List<FACatch> specifiedFACatches) {
        this.specifiedFACatches = specifiedFACatches;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public List<FLUXLocation> getDestinationFLUXLocations() {
        return destinationFLUXLocations;
    }

    public void setDestinationFLUXLocations(List<FLUXLocation> destinationFLUXLocations) {
        this.destinationFLUXLocations = destinationFLUXLocations;
    }

    public List<VesselTransportMeans> getRelatedVesselTransportMeans() {
        return relatedVesselTransportMeans;
    }

    public void setRelatedVesselTransportMeans(List<VesselTransportMeans> relatedVesselTransportMeans) {
        this.relatedVesselTransportMeans = relatedVesselTransportMeans;
    }

    public VesselStorageCharacteristic getDestinationVesselStorageCharacteristic() {
        return destinationVesselStorageCharacteristic;
    }

    public void setDestinationVesselStorageCharacteristic(VesselStorageCharacteristic destinationVesselStorageCharacteristic) {
        this.destinationVesselStorageCharacteristic = destinationVesselStorageCharacteristic;
    }

    public VesselStorageCharacteristic getSourceVesselStorageCharacteristic() {
        return sourceVesselStorageCharacteristic;
    }

    public void setSourceVesselStorageCharacteristic(VesselStorageCharacteristic sourceVesselStorageCharacteristic) {
        this.sourceVesselStorageCharacteristic = sourceVesselStorageCharacteristic;
    }

    public List<IdType> getFaReportDocumentRelatedReportIds() {
        return faReportDocumentRelatedReportIds;
    }

    public void setFaReportDocumentRelatedReportIds(List<IdType> faReportDocumentRelatedReportIds) {
        this.faReportDocumentRelatedReportIds = faReportDocumentRelatedReportIds;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_RELOCATION;
    }
}
