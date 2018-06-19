package eu.europa.ec.fisheries.uvms.rules.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "fishinggeartypecharacteristic")
@NamedQueries({
        @NamedQuery(name = FishingGearTypeCharacteristic.GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE,
                query = "SELECT c.id.fishingGearCharacteristicCode FROM FishingGearTypeCharacteristic c WHERE c.fishingGearTypeCode = :fishingGearTypeCode"),
        @NamedQuery(name = FishingGearTypeCharacteristic.GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE_AND_MANDATORY,
                query = "SELECT c.id.fishingGearCharacteristicCode FROM FishingGearTypeCharacteristic c WHERE c.fishingGearTypeCode = :fishingGearTypeCode and c.mandatory = :mandatory"),
        @NamedQuery(name = FishingGearTypeCharacteristic.GET_ALL_TYPE_CODES,
                query = "SELECT DISTINCT t.fishingGearTypeCode FROM FishingGearTypeCharacteristic t")
})
@Data
@EqualsAndHashCode(exclude = "mandatory")
@Deprecated
public class FishingGearTypeCharacteristic implements Serializable {

    public static final String GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE = "fishingGearTypeCharacteristic.getCharacteristicCodesByTypeCode";
    public static final String GET_ALL_TYPE_CODES = "fishingGearTypeCharacteristic.getAllTypeCodes";
    public static final String GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE_AND_MANDATORY = "fishingGearTypeCharacteristic.getCharacteristicCodesByTypeCodeAndMandatory";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fishing_gear_type_code", nullable = false)
    private String fishingGearTypeCode;

    @Column(name = "fishing_gear_characteristic_code", nullable = false)
    private String fishingGearCharacteristicCode;

    @Column(nullable = false)
    private Boolean mandatory;


}
