package eu.europa.ec.fisheries.uvms.rules.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "fishinggeartypecharacteristic")
@NamedQueries({
        @NamedQuery(name = FishingGearTypeCharacteristic.GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE,
                query = "SELECT c.id.fishingGearCharacteristicCode FROM FishingGearTypeCharacteristic c WHERE c.id.fishingGearTypeCode = :fishingGearTypeCode"),
        @NamedQuery(name = FishingGearTypeCharacteristic.GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE_AND_MANDATORY,
                query = "SELECT c.id.fishingGearCharacteristicCode FROM FishingGearTypeCharacteristic c WHERE c.id.fishingGearTypeCode = :fishingGearTypeCode and c.mandatory = :mandatory"),
        @NamedQuery(name = FishingGearTypeCharacteristic.GET_ALL_TYPE_CODES,
                query = "SELECT DISTINCT t.id.fishingGearTypeCode FROM FishingGearTypeCharacteristic t")
})
public class FishingGearTypeCharacteristic implements Serializable {

    public static final String GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE = "fishingGearTypeCharacteristic.getCharacteristicCodesByTypeCode";
    public static final String GET_ALL_TYPE_CODES = "fishingGearTypeCharacteristic.getAllTypeCodes";
    public static final String GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE_AND_MANDATORY = "fishingGearTypeCharacteristic.getCharacteristicCodesByTypeCodeAndMandatory";

    @EmbeddedId
    private FishingGearTypeCharacteristicId id;

    @Column(name = "mandatory")
    private Boolean mandatory;

    public FishingGearTypeCharacteristicId getId() {
        return id;
    }

    public void setId(FishingGearTypeCharacteristicId id) {
        this.id = id;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FishingGearTypeCharacteristic)) return false;
        FishingGearTypeCharacteristic that = (FishingGearTypeCharacteristic) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
