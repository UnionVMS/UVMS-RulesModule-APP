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
import java.util.Map;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
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
    private Map<String, List<GearMatrix.Condition>> matrix;
    private FishingGear fishingGear;

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

    public boolean valid(FishingGear fishingGear) {
        if (fishingGear == null || CollectionUtils.isEmpty(fishingGear.getApplicableGearCharacteristics())) {
            return false;
        }

        List<GearCharacteristic> gearCharacteristics = fishingGear.getApplicableGearCharacteristics();
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType codeType = fishingGear.getTypeCode();
        List<GearMatrix.Condition> conditions = matrix.get(codeType.getValue());
        MutableInt optional = new MutableInt(0);
        MutableInt mandatoryHits = new MutableInt(0);
        MutableInt optionalHits = new MutableInt(0);

        for (GearMatrix.Condition next : conditions) {
            String value = next.getValue();
            if (!next.isOptional()) {
                if (!valid(gearCharacteristics, mandatoryHits, value)) return false;
            } else {
                optional.increment();
                valid(gearCharacteristics, optionalHits, value);
            }
        }

        return ((optional.getValue() == 0) || (optional.getValue() > 0 && optionalHits.getValue() >= 1));
    }

    private boolean valid(List<GearCharacteristic> incomingGearCharacteristics, MutableInt hits, String value) {
        for (GearCharacteristic incomingGearCharacteristic : incomingGearCharacteristics) {
            un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType code = incomingGearCharacteristic.getTypeCode();
            String incomingCodeTypeValue = code.getValue();
            if (value.equals(incomingCodeTypeValue)){
                hits.increment();
                return true;
            }
        }
        return false;
    }
}
