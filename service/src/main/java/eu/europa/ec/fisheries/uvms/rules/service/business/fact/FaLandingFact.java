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
import lombok.Data;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 *
 * @author patilva
 */
@Data
public class FaLandingFact extends AbstractFact {

    private CodeType fishingActivityCodeType;
    private CodeType faReportDocumentTypeCode;
    private List<FLUXLocation> relatedFluxLocations;
    private List<CodeType> specifiedFaCatchFluxLocationTypeCode;
    private List<FACatch> specifiedFaCatches;
    private List<FLUXLocation> specifiedFaCatchesSpecifiedFLUXLocations;
    private List<CodeType> specifiedFaCatchTypeCode;
    private List<CodeType> relatedFluxLocationTypeCodes;

    public FaLandingFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_LANDING;
    }
}
