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
import eu.europa.ec.fisheries.uvms.rules.entity.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FactConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by padhyad on 4/19/2017.
 */
@Slf4j
public class FishingGearFact extends AbstractFact {

    private CodeType typeCode;

    private List<CodeType> roleCodes;

    private List<GearCharacteristic> applicableGearCharacteristics;

    private boolean fishingActivity;

    private List<FishingGearTypeCharacteristic> fishingGearTypeCharacteristics;

    public FishingGearFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FISHING_GEAR;
    }

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public List<CodeType> getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(List<CodeType> roleCodes) {
        this.roleCodes = roleCodes;
    }

    public List<GearCharacteristic> getApplicableGearCharacteristics() {
        return applicableGearCharacteristics;
    }

    public void setApplicableGearCharacteristics(List<GearCharacteristic> applicableGearCharacteristics) {
        this.applicableGearCharacteristics = applicableGearCharacteristics;
    }

    public boolean isFishingActivity() {
        return fishingActivity;
    }

    public void setFishingActivity(boolean fishingActivity) {
        this.fishingActivity = fishingActivity;
    }

    public void setFishingGearTypeCharacteristics(List<FishingGearTypeCharacteristic> fishingGearTypeCharacteristics) {
        this.fishingGearTypeCharacteristics = fishingGearTypeCharacteristics;
    }

    public boolean isRequiredGearCharacteristicsPresent(CodeType fishingGearTypeCode) {
        if (fishingGearTypeCode == null || StringUtils.isBlank(fishingGearTypeCode.getValue())
                || applicableGearCharacteristics == null || isEmpty(fishingGearTypeCharacteristics)) {
            return false;
        }

        List<String> requiredFishingGearCharacteristicCodes = new ArrayList<>();
        for (FishingGearTypeCharacteristic fishingGearTypeCharacteristic : fishingGearTypeCharacteristics) {
            String typeCode = fishingGearTypeCharacteristic.getId().getFishingGearTypeCode();
            boolean mandatoryFishingGearCharacteristic = fishingGearTypeCharacteristic.getMandatory();

            if (mandatoryFishingGearCharacteristic && typeCode.equals(fishingGearTypeCode.getValue())) {
                String characteristicCode = fishingGearTypeCharacteristic.getId().getFishingGearCharacteristicCode();
                requiredFishingGearCharacteristicCodes.add(characteristicCode);
            }
        }

        if (requiredFishingGearCharacteristicCodes.isEmpty()) {
            return true;
        } else if (requiredFishingGearCharacteristicCodes.size() > applicableGearCharacteristics.size()) {
            return false;
        }

        int hits = 0;
        for (GearCharacteristic applicableGearCharacteristic : applicableGearCharacteristics) {
            un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType applicableGearCharacteristicTypeCode = applicableGearCharacteristic.getTypeCode();

            String fishingGearCharacteristicCode = null;
            try {
                if (!FactConstants.FA_GEAR_CHARACTERISTIC.equals(applicableGearCharacteristicTypeCode.getListID())) {
                    continue;
                }

                fishingGearCharacteristicCode = applicableGearCharacteristic.getTypeCode().getValue();
            } catch (NullPointerException npe) {
                fishingGearCharacteristicCode = null;
            }

            if (StringUtils.isNotBlank(fishingGearCharacteristicCode) && requiredFishingGearCharacteristicCodes.contains(fishingGearCharacteristicCode)) {
                hits++;
                continue;
            }
        }

        return hits == requiredFishingGearCharacteristicCodes.size();
    }
}
