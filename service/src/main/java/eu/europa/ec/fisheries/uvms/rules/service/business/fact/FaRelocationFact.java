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
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;

import java.util.List;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FaRelocationFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;

    private CodeType faReportDocumentTypeCode;

    private List<FACatch> specifiedFACatches;

    private List<FLUXLocation> relatedFLUXLocations;

    private List<VesselTransportMeans> relatedVesselTransportMeans;

    private VesselStorageCharacteristic destinationVesselStorageCharacteristic;

    private VesselStorageCharacteristic sourceVesselStorageCharacteristic;

    private List<IdType> faReportDocumentRelatedReportIds;

    public FaRelocationFact() {
        setFactType();
    }

    public CodeType getFishingActivityTypeCode() {
        return fishingActivityTypeCode;
    }

    public void setFishingActivityTypeCode(CodeType fishingActivityTypeCode) {
        this.fishingActivityTypeCode = fishingActivityTypeCode;
    }

    public CodeType getFaReportDocumentTypeCode() {
        return faReportDocumentTypeCode;
    }

    public void setFaReportDocumentTypeCode(CodeType faReportDocumentTypeCode) {
        this.faReportDocumentTypeCode = faReportDocumentTypeCode;
    }

    public List<FACatch> getSpecifiedFACatches() {
        return specifiedFACatches;
    }

    public void setSpecifiedFACatches(List<FACatch> specifiedFACatches) {
        this.specifiedFACatches = specifiedFACatches;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public List<VesselTransportMeans> getRelatedVesselTransportMeans() {
        return relatedVesselTransportMeans;
    }

    public void setRelatedVesselTransportMeans(List<VesselTransportMeans> relatedVesselTransportMeans) {
        this.relatedVesselTransportMeans = relatedVesselTransportMeans;
    }

    public VesselStorageCharacteristic getDestinationVesselStorageCharacteristic() {
        return destinationVesselStorageCharacteristic;
    }

    public void setDestinationVesselStorageCharacteristic(VesselStorageCharacteristic destinationVesselStorageCharacteristic) {
        this.destinationVesselStorageCharacteristic = destinationVesselStorageCharacteristic;
    }

    public VesselStorageCharacteristic getSourceVesselStorageCharacteristic() {
        return sourceVesselStorageCharacteristic;
    }

    public void setSourceVesselStorageCharacteristic(VesselStorageCharacteristic sourceVesselStorageCharacteristic) {
        this.sourceVesselStorageCharacteristic = sourceVesselStorageCharacteristic;
    }

    public List<IdType> getFaReportDocumentRelatedReportIds() {
        return faReportDocumentRelatedReportIds;
    }

    public void setFaReportDocumentRelatedReportIds(List<IdType> faReportDocumentRelatedReportIds) {
        this.faReportDocumentRelatedReportIds = faReportDocumentRelatedReportIds;
    }

    public boolean anyFluxLocationTypeCodesListIdContains(List<FLUXLocation> fluxLocations, String listId) {
        if (isEmpty(fluxLocations) || StringUtils.isBlank(listId)) {
            return false;
        }

        for (FLUXLocation fluxLocation : fluxLocations) {
            un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = fluxLocation.getTypeCode();

            if (typeCode != null && listId.equals(typeCode.getListID())) {
                return true;
            }
        }

        return false;
    }

    public boolean faCatchTypeCodeAndSpeciesCodeValuesContain(List<FACatch> faCatches, String typeCodeValue, String speciesCodeValue) {
        if (isEmpty(faCatches) || StringUtils.isBlank(typeCodeValue) || StringUtils.isBlank(speciesCodeValue)) {
            return false;
        }

        return anyFACatchTypeCodeValueContains(faCatches, typeCodeValue) && anyFaCatchSpeciesCodeContains(faCatches, speciesCodeValue);
    }

    public boolean anyFaCatchSpeciesCodeContains(List<FACatch> faCatches, String codeValue) {
        if (isEmpty(faCatches) || StringUtils.isBlank(codeValue)) {
            return false;
        }

        for (FACatch faCatch : faCatches) {
            un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = faCatch.getSpeciesCode();

            if (typeCode != null && codeValue.equals(typeCode.getValue())) {
                return true;
            }
        }

        return false;
    }

    public boolean anyFACatchTypeCodeValueContains(List<FACatch> faCatches, String... codes) {
        if (ArrayUtils.isEmpty(codes) || isEmpty(faCatches)) {
            return false;
        }

        for (String code : codes) {
            for (FACatch faCatch : faCatches) {
                if (faCatch.getTypeCode() != null && code.equals(faCatch.getTypeCode().getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean anyVesselTransportMeansSchemeIdContains(List<VesselTransportMeans> vesselTransportMeans, String... schemeIds) {
        if (ArrayUtils.isEmpty(schemeIds) || isEmpty(vesselTransportMeans)) {
            return false;
        }

        for (String schemeId : schemeIds) {
            for (VesselTransportMeans vesselTransportMean : vesselTransportMeans) {
                List<IdType> ids = ActivityFactMapper.mapToIdType(vesselTransportMean.getIDS());
                boolean vesselTransportMeanContainsSchemeId = !schemeIdContainsAny(ids, schemeId);

                if (vesselTransportMeanContainsSchemeId) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean anyVesselTransportMeansSchemeIdContainsAndValid(List<VesselTransportMeans> vesselTransportMeans, String format, String... schemeIds) {
        if (ArrayUtils.isEmpty(schemeIds) || isEmpty(vesselTransportMeans)) {
            return false;
        }

        for (String schemeId : schemeIds) {
            if (anyVesselTransportMeansSchemeIdContains(vesselTransportMeans, schemeId)) {
                String schemeIdValue = getVesselTransportMeansIdsSchemeIdValue(vesselTransportMeans, schemeId);

                return validateFormat(schemeIdValue, FORMATS.valueOf(format).getFormatStr());
            }
        }

        return false;
    }

    public boolean anyVesselTransportMeansRoleCodeContains(List<VesselTransportMeans> vesselTransportMeans, String... roleCodeValues) {
        if (ArrayUtils.isEmpty(roleCodeValues) || isEmpty(vesselTransportMeans)) {
            return false;
        }

        for (String roleCodeValue : roleCodeValues) {
            for (VesselTransportMeans vesselTransportMean : vesselTransportMeans) {
                if (vesselTransportMean.getRoleCode() != null && roleCodeValue.equals(vesselTransportMean.getRoleCode().getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean anyVesselTransportMeansNamePresent(List<VesselTransportMeans> vesselTransportMeans) {
        if (isEmpty(vesselTransportMeans)) {
            return false;
        }

        for (VesselTransportMeans vesselTransportMean : vesselTransportMeans) {
            if (!isEmpty(vesselTransportMean.getNames())) {
                return true;
            }
        }

        return false;
    }

    public boolean anyVesselTransportMeansContactPartyRoleCodePresent(List<VesselTransportMeans> vesselTransportMeans) {
        if (isEmpty(vesselTransportMeans)) {
            return false;
        }

        for (VesselTransportMeans vesselTransportMean : vesselTransportMeans) {
            for (ContactParty contactParty : vesselTransportMean.getSpecifiedContactParties()) {
                if (!isEmpty(contactParty.getRoleCodes())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean anyVesselTransportMeansContactPartyRoleCodeListIdAndValueContains(List<VesselTransportMeans> vesselTransportMeans, String listId, String value) {
        if (StringUtils.isBlank(listId) || StringUtils.isBlank(value) || isEmpty(vesselTransportMeans)) {
            return false;
        }

        for (VesselTransportMeans vesselTransportMean : vesselTransportMeans) {
            for (ContactParty contactParty : vesselTransportMean.getSpecifiedContactParties()) {
                List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> contactPartyRoleCodes = contactParty.getRoleCodes();

                for (un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType contactPartyRoleCode : contactPartyRoleCodes) {
                    String contactPartyRoleCodeListId = contactPartyRoleCode.getListID();
                    String contactPartyRoleCodeValue = contactPartyRoleCode.getValue();

                    if (StringUtils.equals(contactPartyRoleCodeListId, listId) && StringUtils.equals(contactPartyRoleCodeValue, value)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean anyFaCatchFluxLocationTypeCodeContainsValue(List<FACatch> faCatches, String codeValue) {
        if (isEmpty(faCatches) || StringUtils.isBlank(codeValue)) {
            return false;
        }

        for (FACatch faCatch : faCatches) {
            List<FLUXLocation> destinationFluxLocations = faCatch.getDestinationFLUXLocations();
            if (CollectionUtils.isNotEmpty(destinationFluxLocations) && anyFluxLocationTypeCodeContainsValue(destinationFluxLocations, codeValue)) {
                return true;
            }
        }

        return false;
    }

    public boolean anyFaCatchDestinationFluxLocationSchemeIdContains(List<FACatch> faCatches, String... schemeIds) {
        if (ArrayUtils.isEmpty(schemeIds) || isEmpty(faCatches)) {
            return false;
        }

        for (String schemeId : schemeIds) {
            for (FACatch faCatch : faCatches) {
                if (anyFluxLocationSchemeIdContains(faCatch.getDestinationFLUXLocations(), schemeIds)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getFaCatchDestinationFluxLocationSchemeIdValue(List<FACatch> faCatches, String schemeId) {
        if (StringUtils.isEmpty(schemeId) || isEmpty(faCatches)) {
            return null;
        }

        for (FACatch faCatch : faCatches) {
            if (anyFluxLocationSchemeIdContains(faCatch.getDestinationFLUXLocations(), schemeId)) {
                getFluxLocationIdsSchemeIdValue(faCatch.getDestinationFLUXLocations(), schemeId);
            }
        }

        return null;
    }

    public boolean anyFaCatchFluxLocationPresent(List<FACatch> faCatches) {
        if (isEmpty(faCatches)) {
            return false;
        }

        for (FACatch faCatch : faCatches) {
            if (!isEmpty(faCatch.getDestinationFLUXLocations())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_RELOCATION;
    }
}
