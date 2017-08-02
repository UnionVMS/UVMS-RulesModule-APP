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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLAPDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXCharacteristic;
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

    public List<FLUXCharacteristic> getSpecifiedFLUXCharacteristics() {
        return specifiedFLUXCharacteristics;
    }

    public void setSpecifiedFLUXCharacteristics(List<FLUXCharacteristic> specifiedFLUXCharacteristics) {
        this.specifiedFLUXCharacteristics = specifiedFLUXCharacteristics;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
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

    public List<CodeType> getFaCatchSpeciesCodes() {
        return faCatchSpeciesCodes;
    }

    public void setFaCatchSpeciesCodes(List<CodeType> faCatchSpeciesCodes) {
        this.faCatchSpeciesCodes = faCatchSpeciesCodes;
    }

    public List<IdType> getFlapDocumentIdTypes() {
        return flapDocumentIdTypes;
    }

    public void setFlapDocumentIdTypes(List<IdType> flapDocumentIdTypes) {
        this.flapDocumentIdTypes = flapDocumentIdTypes;
    }

    public List<FACatch> getSpecifiedFACatches() {
        return specifiedFACatches;
    }

    public void setSpecifiedFACatches(List<FACatch> specifiedFACatches) {
        this.specifiedFACatches = specifiedFACatches;
    }

    public List<FLAPDocument> getSpecifiedFLAPDocuments() {
        return specifiedFLAPDocuments;
    }

    public void setSpecifiedFLAPDocuments(List<FLAPDocument> specifiedFLAPDocuments) {
        this.specifiedFLAPDocuments = specifiedFLAPDocuments;
    }

    /**
     * This method checks if Every FACatch with typeCode LOADED has atleast one AREA FluxLocation
     * IF yes, it returns true, else false.
     * @param specifiedFACatches
     * @return
     */
    public boolean ifFLUXLocationForFACatchIsAREA(List<FACatch> specifiedFACatches){

        if(CollectionUtils.isEmpty(specifiedFACatches)){
            return false;
        }

        boolean isPresent = true;
        for(FACatch faCatch : specifiedFACatches){

            if(faCatch.getTypeCode()!=null && faCatch.getTypeCode().getValue().equals("LOADED") )  {
                isPresent = false;
                 if(CollectionUtils.isEmpty(faCatch.getSpecifiedFLUXLocations())){
                     return false;
                 }
                 for(FLUXLocation fluxLocation : faCatch.getSpecifiedFLUXLocations()){
                     if(fluxLocation.getTypeCode()!=null && fluxLocation.getTypeCode().getValue().equals("AREA")){
                         isPresent = true;
                         break;
                     }
                 }
            }

        }

        return isPresent;

    }


    /**
     * This method checks if atleast one FACatch from specifiedFACatches has matching speciesCode and typeCode value
     * @param specifiedFACatches FACatches from this list would be matched against
     * @param speciesCode FACatch speciesCode value to be matched
     * @param typeCode FACatch typeCode value to be matched
     * @return  TRUE : Atleast one FACatch with matching criteria found
 *              FALSE :  No FACatch with matching criteria found
     */
    public boolean containsAnyFaCatch(List<FACatch> specifiedFACatches,String speciesCode, String typeCode){
        if(CollectionUtils.isEmpty(specifiedFACatches) || speciesCode ==null || typeCode ==null){
            return false;
        }


        for(FACatch faCatch : specifiedFACatches){
            if(faCatch.getSpeciesCode() !=null && faCatch.getTypeCode() !=null && speciesCode.equals(faCatch.getSpeciesCode()) && typeCode.equals(faCatch.getTypeCode())){
                return true;
            }
        }

        return false;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_NOTIFICATION_OF_TRANSHIPMENT;
    }

    public List<CodeType> getFluxCharacteristicTypeCodes() {
        return fluxCharacteristicTypeCodes;
    }

    public void setFluxCharacteristicTypeCodes(List<CodeType> fluxCharacteristicTypeCodes) {
        this.fluxCharacteristicTypeCodes = fluxCharacteristicTypeCodes;
    }

    public List<CodeType> getFaCatchSpecifiedFLUXLocationsTypeCodes() {
        return faCatchSpecifiedFLUXLocationsTypeCodes;
    }

    public void setFaCatchSpecifiedFLUXLocationsTypeCodes(List<CodeType> faCatchSpecifiedFLUXLocationsTypeCodes) {
        this.faCatchSpecifiedFLUXLocationsTypeCodes = faCatchSpecifiedFLUXLocationsTypeCodes;
    }
}
