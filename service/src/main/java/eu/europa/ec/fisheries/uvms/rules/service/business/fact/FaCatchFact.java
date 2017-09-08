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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProcess;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProduct;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FaCatchFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType typeCode;
    private CodeType speciesCode;
    private MeasureType unitQuantity;
    private MeasureType weightMeasure;
    private List<CodeType> sizeDistributionClassCode;
    private List<CodeType> appliedAAPProcessTypeCodes;
    private List<AAPProduct> resultAAPProduct;
    private List<MeasureType> resultAAPProductUnitQuantity;
    private List<MeasureType> resultAAPProductWeightMeasure;
    private List<CodeType> resultAAPProductPackagingTypeCode;
    private List<MeasureType> resultAAPProductPackagingUnitAverageWeightMeasure;
    private List<MeasureType> resultAAPProductPackagingUnitQuantity;
    private List<String> testStringList;
    private List<IdType> fluxLocationId;
    private CodeType weighingMeansCode;
    private CodeType categoryCode;
    private List<NumericType> appliedAAPProcessConversionFactorNumber;
    private List<FLUXLocation> specifiedFLUXLocations;
    private List<CodeType> specifiedFluxLocationRFMOCodeList;
    private List<AAPProcess> appliedAAPProcess;
    List<FLUXLocation> destinationFLUXLocations;
    private List<IdType> faCatchFluxLocationId;

    public FaCatchFact() {
        setFactType();
    }

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public CodeType getSpeciesCode() {
        return speciesCode;
    }

    public void setSpeciesCode(CodeType speciesCode) {
        this.speciesCode = speciesCode;
    }

    public List<AAPProduct> getResultAAPProduct() {
        return resultAAPProduct;
    }

    public void setResultAAPProduct(List<AAPProduct> resultAAPProduct) {
        this.resultAAPProduct = resultAAPProduct;
    }

    public MeasureType getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(MeasureType unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public MeasureType getWeightMeasure() {
        return weightMeasure;
    }

    public void setWeightMeasure(MeasureType weightMeasure) {
        this.weightMeasure = weightMeasure;
    }

    public List<CodeType> getSizeDistributionClassCode() {
        return sizeDistributionClassCode;
    }

    public void setSizeDistributionClassCode(List<CodeType> sizeDistributionClassCode) {
        this.sizeDistributionClassCode = sizeDistributionClassCode;
    }

    public List<CodeType> getAppliedAAPProcessTypeCodes() {
        return appliedAAPProcessTypeCodes;
    }

    public void setAppliedAAPProcessTypeCodes(List<CodeType> appliedAAPProcessTypeCodes) {
        this.appliedAAPProcessTypeCodes = appliedAAPProcessTypeCodes;

    }

    public List<MeasureType> getResultAAPProductPackagingUnitQuantity() {
        return resultAAPProductPackagingUnitQuantity;
    }

    public void setResultAAPProductPackagingUnitQuantity(List<MeasureType> resultAAPProductPackagingUnitQuantity) {
        this.resultAAPProductPackagingUnitQuantity = resultAAPProductPackagingUnitQuantity;
    }

    public List<CodeType> getResultAAPProductPackagingTypeCode() {
        return resultAAPProductPackagingTypeCode;
    }

    public void setResultAAPProductPackagingTypeCode(List<CodeType> resultAAPProductPackagingTypeCode) {
        this.resultAAPProductPackagingTypeCode = resultAAPProductPackagingTypeCode;
    }

    public List<MeasureType> getResultAAPProductPackagingUnitAverageWeightMeasure() {
        return resultAAPProductPackagingUnitAverageWeightMeasure;
    }

    public void setResultAAPProductPackagingUnitAverageWeightMeasure(List<MeasureType> resultAAPProductPackagingUnitAverageWeightMeasure) {
        this.resultAAPProductPackagingUnitAverageWeightMeasure = resultAAPProductPackagingUnitAverageWeightMeasure;
    }

    public List<MeasureType> getResultAAPProductUnitQuantity() {
        return resultAAPProductUnitQuantity;
    }

    public void setResultAAPProductUnitQuantity(List<MeasureType> resultAAPProductUnitQuantity) {
        this.resultAAPProductUnitQuantity = resultAAPProductUnitQuantity;
    }

    public List<MeasureType> getResultAAPProductWeightMeasure() {
        return resultAAPProductWeightMeasure;
    }

    public void setResultAAPProductWeightMeasure(List<MeasureType> resultAAPProductWeightMeasure) {
        this.resultAAPProductWeightMeasure = resultAAPProductWeightMeasure;
    }

    public List<String> getTestStringList() {
        return testStringList;
    }

    public void setTestStringList(List<String> testStringList) {
        this.testStringList = testStringList;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_CATCH;
    }

    public List<IdType> getFluxLocationId() {
        return fluxLocationId;
    }

    public void setFluxLocationId(List<IdType> fluxLocationId) {
        this.fluxLocationId = fluxLocationId;
    }

    public CodeType getWeighingMeansCode() {
        return weighingMeansCode;
    }

    public void setWeighingMeansCode(CodeType weighingMeansCode) {
        this.weighingMeansCode = weighingMeansCode;
    }

    public CodeType getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(CodeType categoryCode) {
        this.categoryCode = categoryCode;
    }

    public List<NumericType> getAppliedAAPProcessConversionFactorNumber() {
        return appliedAAPProcessConversionFactorNumber;
    }

    public void setAppliedAAPProcessConversionFactorNumber(List<NumericType> appliedAAPProcessConversionFactorNumber) {
        this.appliedAAPProcessConversionFactorNumber = appliedAAPProcessConversionFactorNumber;
    }

    public List<CodeType> getSpecifiedFluxLocationRFMOCodeList() {
        return specifiedFluxLocationRFMOCodeList;
    }

    public void setSpecifiedFluxLocationRFMOCodeList(List<CodeType> specifiedFluxLocationRFMOCodeList) {
        this.specifiedFluxLocationRFMOCodeList = specifiedFluxLocationRFMOCodeList;
    }

    public List<FLUXLocation> getSpecifiedFLUXLocations() {
        return specifiedFLUXLocations;
    }

    public void setSpecifiedFLUXLocations(List<FLUXLocation> specifiedFLUXLocations) {
        this.specifiedFLUXLocations = specifiedFLUXLocations;
    }

    public CodeType getFishingActivityTypeCode() {
        return fishingActivityTypeCode;
    }

    public void setFishingActivityTypeCode(CodeType fishingActivityTypeCode) {
        this.fishingActivityTypeCode = fishingActivityTypeCode;
    }

    public List<AAPProcess> getAppliedAAPProcess() {
        return appliedAAPProcess;
    }

    public void setAppliedAAPProcess(List<AAPProcess> appliedAAPProcess) {
        this.appliedAAPProcess = appliedAAPProcess;
    }

    public List<FLUXLocation> getDestinationFLUXLocations() {
        return destinationFLUXLocations;
    }

    public void setDestinationFLUXLocations(List<FLUXLocation> destinationFLUXLocations) {
        this.destinationFLUXLocations = destinationFLUXLocations;
    }

    public List<IdType> getFaCatchFluxLocationId() {
        return faCatchFluxLocationId;
    }

    public void setFaCatchFluxLocationId(List<IdType> faCatchFluxLocationId) {
        this.faCatchFluxLocationId = faCatchFluxLocationId;
    }
}
