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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXGeographicalCoordinate;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.StructuredAddress;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FluxLocationFact extends AbstractFact {

    private CodeType typeCode;
    private IdType countryID;
    private IdType id;
    private FLUXGeographicalCoordinate specifiedPhysicalFLUXGeographicalCoordinate;
    private StructuredAddress physicalStructuredAddress;
    private List<CodeType> applicableFLUXCharacteristicTypeCode;


    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public IdType getCountryID() {
        return countryID;
    }

    public void setCountryID(IdType countryID) {
        this.countryID = countryID;
    }

    public IdType getId() {
        return id;
    }

    public void setId(IdType id) {
        this.id = id;
    }

    public FLUXGeographicalCoordinate getSpecifiedPhysicalFLUXGeographicalCoordinate() {
        return specifiedPhysicalFLUXGeographicalCoordinate;
    }

    public void setSpecifiedPhysicalFLUXGeographicalCoordinate(FLUXGeographicalCoordinate specifiedPhysicalFLUXGeographicalCoordinate) {
        this.specifiedPhysicalFLUXGeographicalCoordinate = specifiedPhysicalFLUXGeographicalCoordinate;
    }

    public StructuredAddress getPhysicalStructuredAddress() {
        return physicalStructuredAddress;
    }

    public void setPhysicalStructuredAddress(StructuredAddress physicalStructuredAddress) {
        this.physicalStructuredAddress = physicalStructuredAddress;
    }

    public List<CodeType> getApplicableFLUXCharacteristicTypeCode() {
        return applicableFLUXCharacteristicTypeCode;
    }

    public void setApplicableFLUXCharacteristicTypeCode(List<CodeType> applicableFLUXCharacteristicTypeCode) {
        this.applicableFLUXCharacteristicTypeCode = applicableFLUXCharacteristicTypeCode;
    }

    public FluxLocationFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FLUX_LOCATION;
    }
}
