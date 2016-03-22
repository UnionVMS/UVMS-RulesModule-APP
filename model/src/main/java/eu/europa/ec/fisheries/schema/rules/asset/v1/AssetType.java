
package eu.europa.ec.fisheries.schema.rules.asset.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AssetType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AssetType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VESSEL"/>
 *     &lt;enumeration value="AIR"/>
 *     &lt;enumeration value="VEHICLE"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AssetType")
@XmlEnum
public enum AssetType {

    VESSEL,
    AIR,
    VEHICLE,
    OTHER;

    public String value() {
        return name();
    }

    public static AssetType fromValue(String v) {
        return valueOf(v);
    }

}
