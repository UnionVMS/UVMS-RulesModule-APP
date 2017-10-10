package eu.europa.ec.fisheries.uvms.rules.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FishingGearTypeCharacteristicId implements Serializable {

    @Column(name = "fishing_gear_type_code", nullable = false)
    private String fishingGearTypeCode;

    @Column(name = "fishing_gear_characteristic_code", nullable = false)
    private String fishingGearCharacteristicCode;

    public FishingGearTypeCharacteristicId() {
    }

    public FishingGearTypeCharacteristicId(String fishingGearTypeCode, String fishingGearCharacteristicCode) {
        this.fishingGearTypeCode = fishingGearTypeCode;
        this.fishingGearCharacteristicCode = fishingGearCharacteristicCode;
    }

    public String getFishingGearTypeCode() {
        return fishingGearTypeCode;
    }

    public void setFishingGearTypeCode(String fishingGearTypeCode) {
        this.fishingGearTypeCode = fishingGearTypeCode;
    }

    public String getFishingGearCharacteristicCode() {
        return fishingGearCharacteristicCode;
    }

    public void setFishingGearCharacteristicCode(String fishingGearCharacteristicCode) {
        this.fishingGearCharacteristicCode = fishingGearCharacteristicCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FishingGearTypeCharacteristicId)) return false;
        FishingGearTypeCharacteristicId that = (FishingGearTypeCharacteristicId) o;
        return Objects.equals(fishingGearTypeCode, that.fishingGearTypeCode) &&
                Objects.equals(fishingGearCharacteristicCode, that.fishingGearCharacteristicCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fishingGearTypeCode, fishingGearCharacteristicCode);
    }
}
