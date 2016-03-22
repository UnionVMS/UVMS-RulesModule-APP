
package eu.europa.ec.fisheries.schema.rules.asset.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AssetIdType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AssetIdType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ID"/>
 *     &lt;enumeration value="CFR"/>
 *     &lt;enumeration value="IRCS"/>
 *     &lt;enumeration value="IMO"/>
 *     &lt;enumeration value="MMSI"/>
 *     &lt;enumeration value="GUID"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AssetIdType")
@XmlEnum
public enum AssetIdType {

    ID,
    CFR,
    IRCS,
    IMO,
    MMSI,
    GUID;

    public String value() {
        return name();
    }

    public static AssetIdType fromValue(String v) {
        return valueOf(v);
    }

}
