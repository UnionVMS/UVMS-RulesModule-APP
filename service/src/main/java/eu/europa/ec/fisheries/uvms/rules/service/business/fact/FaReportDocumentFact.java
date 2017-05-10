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


import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

/**
 * Created by padhyad on 4/7/2017.
 */
public class FaReportDocumentFact extends AbstractFact {

    private CodeType typeCode;

    private List<IdType> relatedReportIDs;

    private Date acceptanceDateTime;

    private List<IdType> ids;

    private FLUXReportDocument relatedFLUXReportDocument;

    private CodeType purposeCode;

    private IdType referencedID;

    private Date creationDateTime;

    private List<IdType> ownerFluxPartyIds;

    private VesselTransportMeans specifiedVesselTransportMeans;

    private List<FishingActivity> specifiedFishingActivities;

    public FaReportDocumentFact() {
        setFactType();
    }

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public List<IdType> getRelatedReportIDs() {
        return relatedReportIDs;
    }

    public void setRelatedReportIDs(List<IdType> relatedReportIDs) {
        this.relatedReportIDs = relatedReportIDs;
    }

    public Date getAcceptanceDateTime() {
        return acceptanceDateTime;
    }

    public void setAcceptanceDateTime(Date acceptanceDateTime) {
        this.acceptanceDateTime = acceptanceDateTime;
    }

    public List<IdType> getIds() {
        return ids;
    }

    public void setIds(List<IdType> ids) {
        this.ids = ids;
    }

    public FLUXReportDocument getRelatedFLUXReportDocument() {
        return relatedFLUXReportDocument;
    }

    public void setRelatedFLUXReportDocument(FLUXReportDocument relatedFLUXReportDocument) {
        this.relatedFLUXReportDocument = relatedFLUXReportDocument;
    }

    public CodeType getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(CodeType purposeCode) {
        this.purposeCode = purposeCode;
    }

    public IdType getReferencedID() {
        return referencedID;
    }

    public void setReferencedID(IdType referencedID) {
        this.referencedID = referencedID;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public List<IdType> getOwnerFluxPartyIds() {
        return ownerFluxPartyIds;
    }

    public void setOwnerFluxPartyIds(List<IdType> ownerFluxPartyIds) {
        this.ownerFluxPartyIds = ownerFluxPartyIds;
    }

    public VesselTransportMeans getSpecifiedVesselTransportMeans() {
        return specifiedVesselTransportMeans;
    }

    public void setSpecifiedVesselTransportMeans(VesselTransportMeans specifiedVesselTransportMeans) {
        this.specifiedVesselTransportMeans = specifiedVesselTransportMeans;
    }

    public List<FishingActivity> getSpecifiedFishingActivities() {
        return specifiedFishingActivities;
    }

    public void setSpecifiedFishingActivities(List<FishingActivity> specifiedFishingActivities) {
        this.specifiedFishingActivities = specifiedFishingActivities;
    }

    @Override
    public List<String> getUniqueIds() {
        return uniqueIds;
    }

    @Override
    public void setUniqueIds(List<String> uniqueIds) {
        this.uniqueIds = uniqueIds;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_REPORT_DOCUMENT;
    }
}


