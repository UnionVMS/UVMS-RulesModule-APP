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
import lombok.Data;
import lombok.EqualsAndHashCode;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;

@Data
@EqualsAndHashCode(callSuper = false)
public class FluxFaReportMessageFact extends AbstractFact {

    private IdType referencedID;
    private List<IdType> ids;
    private Date creationDateTime;
    private String creationDateTimeString;
    private CodeType purposeCode;
    private List<IdType> ownerFluxPartyIds;
    private List<FAReportDocument> faReportDocuments;
    private List<IdType> nonUniqueIdsList;

    public FluxFaReportMessageFact() {
        setFactType();
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
        this.factType = FactType.FLUX_FA_REPORT_MESSAGE;
    }

}
