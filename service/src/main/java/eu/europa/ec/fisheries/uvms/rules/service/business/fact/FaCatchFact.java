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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProduct;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FaCatchFact extends AbstractFact {

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

    public List<MeasureType> getResultAAPProductPackagingUnitQuantity() {
        return resultAAPProductPackagingUnitQuantity;
    }

    public void setResultAAPProductPackagingUnitQuantity(List<MeasureType> resultAAPProductPackagingUnitQuantity) {
        this.resultAAPProductPackagingUnitQuantity = resultAAPProductPackagingUnitQuantity;
    }

    public void setAppliedAAPProcessTypeCodes(List<CodeType> appliedAAPProcessTypeCodes) {
        this.appliedAAPProcessTypeCodes = appliedAAPProcessTypeCodes;

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

    public FaCatchFact() {
        setFactType();
    }

    public List<MeasureType> getResultAAPProductWeightMeasure() {
        return resultAAPProductWeightMeasure;
    }

    public void setResultAAPProductWeightMeasure(List<MeasureType> resultAAPProductWeightMeasure) {
        this.resultAAPProductWeightMeasure = resultAAPProductWeightMeasure;
    }



    @Override
    public void setFactType() {
        this.factType = FactType.FA_CATCH;
    }
}
