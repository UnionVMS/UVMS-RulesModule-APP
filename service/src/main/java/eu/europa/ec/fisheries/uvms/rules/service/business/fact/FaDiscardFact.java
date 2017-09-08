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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FaDiscardFact extends AbstractFact {

    private CodeType faReportDocumentTypeCode;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<CodeType> fluxLocationTypeCode;
    private List<CodeType> specifiedFACatchTypeCode;
    private CodeType reasonCode;


    public CodeType getFaReportDocumentTypeCode() {
        return faReportDocumentTypeCode;
    }

    public void setFaReportDocumentTypeCode(CodeType faReportDocumentTypeCode) {
        this.faReportDocumentTypeCode = faReportDocumentTypeCode;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public List<CodeType> getFluxLocationTypeCode() {
        return fluxLocationTypeCode;
    }

    public void setFluxLocationTypeCode(List<CodeType> fluxLocationTypeCode) {
        this.fluxLocationTypeCode = fluxLocationTypeCode;
    }

    public List<CodeType> getSpecifiedFACatchTypeCode() {
        return specifiedFACatchTypeCode;
    }

    public void setSpecifiedFACatchTypeCode(List<CodeType> specifiedFACatchTypeCode) {
        this.specifiedFACatchTypeCode = specifiedFACatchTypeCode;
    }

    public FaDiscardFact() {
        setFactType();
    }


    public CodeType getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(CodeType reasonCode) {
        this.reasonCode = reasonCode;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_DISCARD;
    }
}
