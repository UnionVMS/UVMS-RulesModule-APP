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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.QuantityType;

/**
 * Created by padhyad on 4/21/2017.
 */
public class GearProblemFact extends AbstractFact {

    private CodeType typeCode;

    private QuantityType affectedQuantity;

    private List<CodeType> recoveryMeasureCodes;

    private List<FLUXLocation> specifiedFluxLocations;

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public QuantityType getAffectedQuantity() {
        return affectedQuantity;
    }

    public void setAffectedQuantity(QuantityType affectedQuantity) {
        this.affectedQuantity = affectedQuantity;
    }

    public List<CodeType> getRecoveryMeasureCodes() {
        return recoveryMeasureCodes;
    }

    public void setRecoveryMeasureCodes(List<CodeType> recoveryMeasureCodes) {
        this.recoveryMeasureCodes = recoveryMeasureCodes;
    }

    public List<FLUXLocation> getSpecifiedFluxLocations() {
        return specifiedFluxLocations;
    }

    public void setSpecifiedFluxLocations(List<FLUXLocation> specifiedFluxLocations) {
        this.specifiedFluxLocations = specifiedFluxLocations;
    }

    public GearProblemFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.GEAR_PROBLEM;
    }
}
