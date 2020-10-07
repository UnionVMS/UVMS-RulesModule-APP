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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;

import java.util.Date;
import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class FaQueryFact extends AbstractFact {

    private CodeType typeCode;

    private IdType id;

    private Date submittedDateTime;

    private String submittedDateTimeString;

    private List<IdType> submittedFLUXPartyIds;

    private List<IdType> nonUniqueIdsList;

    private DelimitedPeriod specifiedDelimitedPeriod;

    private String sdpStartDateTimeString;

    private String sdpEndDateTimeString;

    private List<CodeType> simpleFAQueryParameterTypeCodes;

    public FaQueryFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_QUERY;
    }

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public IdType getId() {
        return id;
    }

    public void setId(IdType id) {
        this.id = id;
    }

    public Date getSubmittedDateTime() {
        return submittedDateTime;
    }

    public void setSubmittedDateTime(Date submittedDateTime) {
        this.submittedDateTime = submittedDateTime;
    }

    public String getSubmittedDateTimeString() {
        return submittedDateTimeString;
    }

    public void setSubmittedDateTimeString(String submittedDateTimeString) {
        this.submittedDateTimeString = submittedDateTimeString;
    }

    public List<IdType> getSubmittedFLUXPartyIds() {
        return submittedFLUXPartyIds;
    }

    public void setSubmittedFLUXPartyIds(List<IdType> submittedFLUXPartyIds) {
        this.submittedFLUXPartyIds = submittedFLUXPartyIds;
    }

    public DelimitedPeriod getSpecifiedDelimitedPeriod() {
        return specifiedDelimitedPeriod;
    }

    public void setSpecifiedDelimitedPeriod(DelimitedPeriod specifiedDelimitedPeriod) {
        this.specifiedDelimitedPeriod = specifiedDelimitedPeriod;
    }

    public String getSdpStartDateTimeString() {
        return sdpStartDateTimeString;
    }

    public void setSdpStartDateTimeString(String sdpStartDateTimeString) {
        this.sdpStartDateTimeString = sdpStartDateTimeString;
    }

    public String getSdpEndDateTimeString() {
        return sdpEndDateTimeString;
    }

    public void setSdpEndDateTimeString(String sdpEndDateTimeString) {
        this.sdpEndDateTimeString = sdpEndDateTimeString;
    }

    public List<CodeType> getSimpleFAQueryParameterTypeCodes() {
        return simpleFAQueryParameterTypeCodes;
    }

    public void setSimpleFAQueryParameterTypeCodes(List<CodeType> simpleFAQueryParameterTypeCodes) {
        this.simpleFAQueryParameterTypeCodes = simpleFAQueryParameterTypeCodes;
    }

    public List<IdType> getNonUniqueIdsList() {
        return nonUniqueIdsList;
    }

    public void setNonUniqueIdsList(List<IdType> nonUniqueIdsList) {
        this.nonUniqueIdsList = nonUniqueIdsList;
    }

}
