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

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FaEntryToSeaFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;

    private CodeType faReportDocumentTypeCode;

    private List<FLUXLocation> relatedFLUXLocations;

    private CodeType reasonCode;

    private CodeType speciesTargetCode;

    private List<FACatch> specifiedFACatches;

    public FaEntryToSeaFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_ENTRY_TO_SEA;
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

    public CodeType getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(CodeType reasonCode) {
        this.reasonCode = reasonCode;
    }

    public CodeType getSpeciesTargetCode() {
        return speciesTargetCode;
    }

    public void setSpeciesTargetCode(CodeType speciesTargetCode) {
        this.speciesTargetCode = speciesTargetCode;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public List<CodeType> getRelatedFluxLocationTypeCode(){

        List<CodeType> codeTypes = null;

        if (relatedFLUXLocations != null){
            codeTypes = new ArrayList<>();

            for(FLUXLocation location : relatedFLUXLocations){
                un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = location.getTypeCode();
                if(typeCode != null){
                    CodeType codeType = new CodeType();
                    codeType.setListId(typeCode.getListID());
                    codeType.setValue(typeCode.getValue());
                    codeTypes.add(codeType);
                }
            }
        }

        return codeTypes;
    }

    public List<CodeType> getSpecifiedFACatchesTypeCode(){

        List<CodeType> codeTypes = null;

        if (specifiedFACatches != null){
            codeTypes = new ArrayList<>();

            for(FACatch faCatch : specifiedFACatches){
                un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = faCatch.getTypeCode();

                if(typeCode != null){
                    CodeType codeType = new CodeType();
                    codeType.setListId(typeCode.getListID());
                    codeType.setValue(typeCode.getValue());
                    codeTypes.add(codeType);
                }
            }
        }

        return codeTypes;
    }

    public List<IdType> getRelatedFluxLocationID(){

        List<IdType> idTypes = null;

        if (relatedFLUXLocations != null){
            idTypes = new ArrayList<>();

            for(FLUXLocation location : relatedFLUXLocations){
                IDType id = location.getID();
                if(id != null){
                    IdType idType = new IdType();
                    idType.setValue(id.getValue());
                    idType.setSchemeId(id.getSchemeID());
                    idTypes.add(idType);
                }
            }
        }

        return idTypes;
    }

    public List<FACatch> getSpecifiedFACatches() {
        return specifiedFACatches;
    }

    public void setSpecifiedFACatches(List<FACatch> specifiedFACatches) {
        this.specifiedFACatches = specifiedFACatches;
    }
}
