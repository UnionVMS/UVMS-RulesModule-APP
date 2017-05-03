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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactPerson;

public class VesselTransportMeansFact extends AbstractFact {

    private List<IdType> ids;

    private IdType registrationVesselCountryId;

    private CodeType roleCode;

    private List<ContactParty> specifiedContactParties;

    private List<CodeType> specifiedContactPartyRoleCodes;

    private List<ContactPerson> specifiedContactPersons;

    public VesselTransportMeansFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.VESSEL_TRANSPORT_MEANS;
    }

    public CodeType getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(CodeType roleCode) {
        this.roleCode = roleCode;
    }

    public List<IdType> getIds() {
        return ids;
    }

    public void setIds(List<IdType> ids) {
        this.ids = ids;
    }

    public IdType getRegistrationVesselCountryId() {
        return registrationVesselCountryId;
    }

    public void setRegistrationVesselCountryId(IdType registrationVesselCountryId) {
        this.registrationVesselCountryId = registrationVesselCountryId;
    }

    public List<ContactParty> getSpecifiedContactParties() {
        return specifiedContactParties;
    }

    public void setSpecifiedContactParties(List<ContactParty> specifiedContactParties) {
        this.specifiedContactParties = specifiedContactParties;
    }

    public List<CodeType> getSpecifiedContactPartyRoleCodes() {
        return specifiedContactPartyRoleCodes;
    }

    public void setSpecifiedContactPartyRoleCodes(List<CodeType> specifiedContactPartyRoleCodes) {
        this.specifiedContactPartyRoleCodes = specifiedContactPartyRoleCodes;
    }

    public List<ContactPerson> getSpecifiedContactPersons() {
        return specifiedContactPersons;
    }

    public void setSpecifiedContactPersons(List<ContactPerson> specifiedContactPersons) {
        this.specifiedContactPersons = specifiedContactPersons;
    }
}
