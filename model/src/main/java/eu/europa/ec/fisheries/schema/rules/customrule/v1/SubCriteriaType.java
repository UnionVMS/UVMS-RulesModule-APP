
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubCriteriaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubCriteriaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ASSET_GROUP"/>
 *     &lt;enumeration value="ACTIVITY_CALLBACK"/>
 *     &lt;enumeration value="ACTIVITY_MESSAGE_ID"/>
 *     &lt;enumeration value="ACTIVITY_MESSAGE_TYPE"/>
 *     &lt;enumeration value="AREA_CODE"/>
 *     &lt;enumeration value="AREA_TYPE"/>
 *     &lt;enumeration value="AREA_CODE_ENT"/>
 *     &lt;enumeration value="AREA_TYPE_ENT"/>
 *     &lt;enumeration value="AREA_CODE_EXT"/>
 *     &lt;enumeration value="AREA_TYPE_EXT"/>
 *     &lt;enumeration value="ASSET_ID_GEAR_TYPE"/>
 *     &lt;enumeration value="EXTERNAL_MARKING"/>
 *     &lt;enumeration value="FLAG_STATE"/>
 *     &lt;enumeration value="ASSET_CFR"/>
 *     &lt;enumeration value="ASSET_IRCS"/>
 *     &lt;enumeration value="ASSET_NAME"/>
 *     &lt;enumeration value="ASSET_STATUS"/>
 *     &lt;enumeration value="COMCHANNEL_TYPE"/>
 *     &lt;enumeration value="MT_TYPE"/>
 *     &lt;enumeration value="MT_DNID"/>
 *     &lt;enumeration value="MT_MEMBER_ID"/>
 *     &lt;enumeration value="MT_SERIAL_NO"/>
 *     &lt;enumeration value="MT_STATUS"/>
 *     &lt;enumeration value="ALTITUDE"/>
 *     &lt;enumeration value="LATITUDE"/>
 *     &lt;enumeration value="LONGITUDE"/>
 *     &lt;enumeration value="CALCULATED_COURSE"/>
 *     &lt;enumeration value="CALCULATED_SPEED"/>
 *     &lt;enumeration value="MOVEMENT_TYPE"/>
 *     &lt;enumeration value="POSITION_REPORT_TIME"/>
 *     &lt;enumeration value="REPORTED_COURSE"/>
 *     &lt;enumeration value="REPORTED_SPEED"/>
 *     &lt;enumeration value="SEGMENT_TYPE"/>
 *     &lt;enumeration value="SOURCE"/>
 *     &lt;enumeration value="STATUS_CODE"/>
 *     &lt;enumeration value="CLOSEST_COUNTRY_CODE"/>
 *     &lt;enumeration value="CLOSEST_PORT_CODE"/>
 *     &lt;enumeration value="TIME_DIFF_POSITION_REPORT"/>
 *     &lt;enumeration value="SUM_POSITION_REPORT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubCriteriaType")
@XmlEnum
public enum SubCriteriaType {

    ASSET_GROUP,
    ACTIVITY_CALLBACK,
    ACTIVITY_MESSAGE_ID,
    ACTIVITY_MESSAGE_TYPE,
    AREA_CODE,
    AREA_TYPE,
    AREA_CODE_ENT,
    AREA_TYPE_ENT,
    AREA_CODE_EXT,
    AREA_TYPE_EXT,
    ASSET_ID_GEAR_TYPE,
    EXTERNAL_MARKING,
    FLAG_STATE,
    ASSET_CFR,
    ASSET_IRCS,
    ASSET_NAME,
    ASSET_STATUS,
    COMCHANNEL_TYPE,
    MT_TYPE,
    MT_DNID,
    MT_MEMBER_ID,
    MT_SERIAL_NO,
    MT_STATUS,
    ALTITUDE,
    LATITUDE,
    LONGITUDE,
    CALCULATED_COURSE,
    CALCULATED_SPEED,
    MOVEMENT_TYPE,
    POSITION_REPORT_TIME,
    REPORTED_COURSE,
    REPORTED_SPEED,
    SEGMENT_TYPE,
    SOURCE,
    STATUS_CODE,
    CLOSEST_COUNTRY_CODE,
    CLOSEST_PORT_CODE,
    TIME_DIFF_POSITION_REPORT,
    SUM_POSITION_REPORT;

    public String value() {
        return name();
    }

    public static SubCriteriaType fromValue(String v) {
        return valueOf(v);
    }

}
