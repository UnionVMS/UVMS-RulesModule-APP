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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;

import java.util.Date;
import java.util.List;

/**
 * Created by padhyad on 4/19/2017.
 */
public class FluxFaReportMessageFact extends AbstractFact {

    private String referencedID;

    private List<IdType> ids;

    private Date creationDateTime;

    private CodeType purposeCode;

    private List<IdType> ownerFluxPartyIds;

    private List<FAReportDocument> faReportDocuments;

    public String getReferencedID() {
        return referencedID;
    }

    public void setReferencedID(String referencedID) {
        this.referencedID = referencedID;
    }

    public List<IdType> getIds() {
        return ids;
    }

    public void setIds(List<IdType> ids) {
        this.ids = ids;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public CodeType getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(CodeType purposeCode) {
        this.purposeCode = purposeCode;
    }

    public List<IdType> getOwnerFluxPartyIds() {
        return ownerFluxPartyIds;
    }

    public void setOwnerFluxPartyIds(List<IdType> ownerFluxPartyIds) {
        this.ownerFluxPartyIds = ownerFluxPartyIds;
    }

    public List<FAReportDocument> getFaReportDocuments() {
        return faReportDocuments;
    }

    public void setFaReportDocuments(List<FAReportDocument> faReportDocuments) {
        this.faReportDocuments = faReportDocuments;
    }

    @Override
    public List<String> getUniqueIds() {
        return uniqueIds;
    }

    @Override
    public void setUniqueIds(List<String> uniqueIds) {
        this.uniqueIds = uniqueIds;
    }

    public FluxFaReportMessageFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FLUX_FA_REPORT_MESSAGE;
    }
}
