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
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactPerson;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.StructuredAddress;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselPositionEvent;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@Data
@EqualsAndHashCode(callSuper = false)
public class VesselTransportMeansFact extends AbstractFact {

    private boolean isFromFaReport;
    private CodeType fishingActivityType;
    private List<IdType> ids;
    private IdType registrationVesselCountryId;
    private CodeType roleCode;
    private List<ContactParty> specifiedContactParties;
    private List<StructuredAddress> specifiedStructuredAddresses;
    private List<CodeType> specifiedContactPartyRoleCodes;
    private List<ContactPerson> specifiedContactPersons;
    private VesselTransportMeansDto transportMeans;
    private List<VesselPositionEvent> vesselPositionEvents;
    private IdType specifiedVesselCountryId;
    private List<IdType> specifiedIds;
    private IdType relatedVesselCountryId;
    private List<IdType> relatedIds;
    private VesselTransportMeans relatedVesselTransportMeans;
    private VesselTransportMeans specifiedVesselTransportMeans;

    public VesselTransportMeansFact() {
        setFactType();
    }

    public boolean containsAtLeastOneCorrectIdOfTheRequired(String schemeId){
        boolean containsValidSchemeId = false;
        for (IdType id : ids) {
            if(schemeId.equals(id.getSchemeId()) && StringUtils.isNotEmpty(id.getValue())){
                containsValidSchemeId = true;
            }
        }
        return containsValidSchemeId;
    }

    public boolean containsAtLeastOneCorrectIdOfTheSpecified(String schemeId){
        boolean containsValidSchemeId = false;
        for (IdType id : specifiedIds) {
            if(schemeId.equals(id.getSchemeId()) && StringUtils.isNotEmpty(id.getValue())){
                containsValidSchemeId = true;
            }
        }
        return containsValidSchemeId;
    }

    public boolean containsAtLeastOneCorrectIdOfTheRelated(String schemeId){
        boolean containsValidSchemeId = false;

        if(relatedIds == null || relatedIds.isEmpty()){
            return true;
        }

        for (IdType id : relatedIds) {
            if(schemeId.equals(id.getSchemeId()) && StringUtils.isNotEmpty(id.getValue())){
                containsValidSchemeId = true;
            }
        }
        return containsValidSchemeId;
    }

    public boolean hasValueForVesselTransports(VesselTransportMeans relatedVesselTransportMeans,String value){
        if(relatedVesselTransportMeans == null ){
            return true;
        }

        for(IDType id :relatedVesselTransportMeans.getIDS()){
            if(id == null || id.getSchemeID() == null){
                continue;
            }

            if(value.equals(id.getSchemeID())){
               return true;
            }

        }

        return false;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.VESSEL_TRANSPORT_MEANS;
    }

    public boolean getIsFromFaReport() {
        return isFromFaReport;
    }
    public void setIsFromFaReport(boolean fromFaReport) {
        isFromFaReport = fromFaReport;
    }
}
