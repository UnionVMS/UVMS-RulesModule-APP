/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union(""), 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation(""), either version 3 of
 the License(""), or any later version. The IFDM Suite is distributed in the hope that it will be useful(""), but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not(""), see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.constants;


public enum MDRAcronymType {

    EFFORT_ZONE("EFFORT_ZONE"),
    FAO_AREA("FAO_AREA"),
    FAO_SPECIES("FAO_SPECIES"),
    FARM("FARM"),
    FA_BAIT_TYPE("FA_BAIT_TYPE"),
    FA_BFT_SIZE_CATEGORY("FA_BFT_SIZE_CATEGORY"),
    FA_BR("FA_BR"),
    FA_CATCH_TYPE("FA_CATCH_TYPE"),
    FA_CHARACTERISTIC("FA_CHARACTERISTIC"),
    FA_FISHERY("FA_FISHERY"),
    FA_GEAR_CHARACTERISTIC("FA_GEAR_CHARACTERISTIC"),
    FA_GEAR_PROBLEM("FA_GEAR_PROBLEM"),
    FA_GEAR_RECOVERY("FA_GEAR_RECOVERY"),
    FA_GEAR_ROLE("FA_GEAR_ROLE"),
    FA_QUERY_PARAMETER("FA_QUERY_PARAMETER"),
    FA_QUERY_TYPE("FA_QUERY_TYPE"),
    FA_REASON_ARRIVAL("FA_REASON_ARRIVAL"),
    FA_REASON_DEPARTURE("FA_REASON_DEPARTURE"),
    FA_REASON_DISCARD("FA_REASON_DISCARD"),
    FA_REASON_ENTRY("FA_REASON_ENTRY"),
    FA_VESSEL_ROLE("FA_VESSEL_ROLE"),
    FISHING_TRIP_TYPE("FISHING_TRIP_TYPE"),
    FISH_FRESHNESS("FISH_FRESHNESS"),
    FISH_PACKAGING("FISH_PACKAGING"),
    FISH_PRESENTATION("FISH_PRESENTATION"),
    FISH_PRESERVATION("FISH_PRESERVATION"),
    FISH_SIZE_CATEGORY("FISH_SIZE_CATEGORY"),
    FISH_SIZE_CLASS("FISH_SIZE_CLASS"),
    FLAP_ID_TYPE("FLAP_ID_TYPE"),
    FLUX_CONTACT_ROLE("FLUX_CONTACT_ROLE"),
    FLUX_FA_FMC("FLUX_FA_FMC"),
    FLUX_FA_REPORT_TYPE("FLUX_FA_REPORT_TYPE"),
    FLUX_FA_TYPE("FLUX_FA_TYPE"),
    FLUX_GP_PARTY("FLUX_GP_PARTY"),
    FLUX_GP_PURPOSE("FLUX_GP_PURPOSE"),
    FLUX_GP_RESPONSE("FLUX_GP_RESPONSE"),
    FLUX_GP_VALIDATION_LEVEL("FLUX_GP_VALIDATION_LEVEL"),
    FLUX_GP_VALIDATION_TYPE("FLUX_GP_VALIDATION_TYPE"),
    FLUX_LOCATION_CHARACTERISTIC("FLUX_LOCATION_CHARACTERISTIC"),
    FLUX_LOCATION_TYPE("FLUX_LOCATION_TYPE"),
    FLUX_PROCESS_TYPE("FLUX_PROCESS_TYPE"),
    FLUX_SALES_PARTY_ID_TYPE("FLUX_SALES_PARTY_ID_TYPE"),
    FLUX_SALES_PARTY_ROLE("FLUX_SALES_PARTY_ROLE"),
    FLUX_SALES_QUERY_PARAM("FLUX_SALES_QUERY_PARAM"),
    FLUX_SALES_QUERY_PARAM_ROLE("FLUX_SALES_QUERY_PARAM_ROLE"),
    FLUX_SALES_TYPE("FLUX_SALES_TYPE"),
    FLUX_UNIT("FLUX_UNIT"),
    FLUX_VESSEL_ID_TYPE("FLUX_VESSEL_ID_TYPE"),
    GEAR_TYPE("GEAR_TYPE"),
    GFCM_GSA("GFCM_GSA"),
    GFCM_STAT_RECTANGLE("GFCM_STAT_RECTANGLE"),
    ICES_STAT_RECTANGLE("ICES_STAT_RECTANGLE"),
    LOCATION("LOCATION"),
    PROD_USAGE("PROD_USAGE"),
    RFMO("RFMO"),
    SALE_BR("SALE_BR"),
    TERRITORY("TERRITORY"),
    TERRITORY_CURR("TERRITORY_CURR"),
    VESSEL_ACTIVITY("VESSEL_ACTIVITY"),
    VESSEL_STORAGE_TYPE("VESSEL_STORAGE_TYPE"),
    WEIGHT_MEANS("WEIGHT_MEANS"),
    STAT_RECTANGLE("STAT_RECTANGLE"),
    MANAGEMENT_AREA("MANAGEMENT_AREA"),
    FLUX_GP_MSG_ID("FLUX_GP_MSG_ID"),
    CONVERSION_FACTOR("CONVERSION_FACTOR"),
    MEMBER_STATE("MEMBER_STATE"),
    TARGET_SPECIES_GROUP("TARGET_SPECIES_GROUP"),
    GENDER("GENDER"),
    FA_BR_DEF("FA_BR_DEF"),
    SALE_BR_DEF("SALE_BR_DEF");

    private final String value;

    MDRAcronymType(String v) {
        value = v;
    }

    public static MDRAcronymType fromValue(String v) {
        for (MDRAcronymType c : MDRAcronymType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
