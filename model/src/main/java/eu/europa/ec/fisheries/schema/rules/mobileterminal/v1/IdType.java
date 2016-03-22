
package eu.europa.ec.fisheries.schema.rules.mobileterminal.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IdType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IdType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SERIAL_NUMBER"/>
 *     &lt;enumeration value="LES"/>
 *     &lt;enumeration value="DNID"/>
 *     &lt;enumeration value="MEMBER_NUMBER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IdType")
@XmlEnum
public enum IdType {

    SERIAL_NUMBER,
    LES,
    DNID,
    MEMBER_NUMBER;

    public String value() {
        return name();
    }

    public static IdType fromValue(String v) {
        return valueOf(v);
    }

}
