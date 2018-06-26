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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.dto.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.dto.GearCharacteristicsConditions;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;

@Data
@EqualsAndHashCode(callSuper = false)
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

    public boolean isFishingActivity() {
        return fishingActivity;
    }

    public void setFishingActivity(boolean fishingActivity) {
        this.fishingActivity = fishingActivity;
    }

    public boolean valid(FishingGear fishingGear, GearCharacteristicsConditions characteristics) {
        if (fishingGear == null || CollectionUtils.isEmpty(fishingGear.getApplicableGearCharacteristics())) {
            return false;
        }

        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = fishingGear.getTypeCode();
        List<GearCharacteristicsConditions.Condition> conditions = characteristics.getCharacteristicMap().get(codeType.getValue());
        List<GearCharacteristic> incomingCharacteristics = fishingGear.getApplicableGearCharacteristics();

        MutableInt mandatoryConditionsTotal = new MutableInt(0);
        MutableInt optionalTotal = new MutableInt(0);
        MutableInt mandatoryHits = new MutableInt(0);
        MutableInt optionalHits = new MutableInt(0);
        Set<String> incomingMandatoryUniqueValues = new HashSet<>();

        for (GearCharacteristicsConditions.Condition next : conditions) {
            String value = next.getValue();
            if (!next.isOptional()) {
                if (!valid(incomingCharacteristics, mandatoryHits, value, incomingMandatoryUniqueValues, mandatoryConditionsTotal))
                    return false;
            } else {
                valid(incomingCharacteristics, optionalHits, value, new HashSet<String>(), optionalTotal);
            }
        }

        return mandatoryConditionsTotal.getValue() <= mandatoryHits.getValue() && ((optionalTotal.getValue() == 0)
                || (optionalTotal.getValue() > 0 && optionalHits.getValue() >= 1));
    }

    private boolean valid(List<GearCharacteristic> incomingGearCharacteristics, MutableInt hits, String value, Set<String> values, MutableInt total) {
        total.increment();
        for (GearCharacteristic incomingGearCharacteristic : incomingGearCharacteristics) {
            un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType code = incomingGearCharacteristic.getTypeCode();
            String incomingCodeTypeValue = code.getValue();
            values.add(incomingCodeTypeValue);
            if (value.equals(incomingCodeTypeValue)){
                hits.increment();
                return true;
            }
        }
        return false;
    }

}
