package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by MATBUL on 21/06/2017.
 */
public class SalesFactHelper {

    static HashSet<String> validCountries = new HashSet<>();

    static List<String> validCategories;


    static {
        validCountries.add("ABW");
        validCountries.add("AFG");
        validCountries.add("AGO");
        validCountries.add("AIA");
        validCountries.add("ALA");
        validCountries.add("ALB");
        validCountries.add("AND");
        validCountries.add("ARE");
        validCountries.add("ARG");
        validCountries.add("ARM");
        validCountries.add("ASM");
        validCountries.add("ATA");
        validCountries.add("ATF");
        validCountries.add("ATG");
        validCountries.add("AUS");
        validCountries.add("AUT");
        validCountries.add("AZE");
        validCountries.add("BDI");
        validCountries.add("BEL");
        validCountries.add("BEN");
        validCountries.add("BES");
        validCountries.add("BFA");
        validCountries.add("BGD");
        validCountries.add("BGR");
        validCountries.add("BHR");
        validCountries.add("BHS");
        validCountries.add("BIH");
        validCountries.add("BLM");
        validCountries.add("BLR");
        validCountries.add("BLZ");
        validCountries.add("BMU");
        validCountries.add("BOL");
        validCountries.add("BRA");
        validCountries.add("BRB");
        validCountries.add("BRN");
        validCountries.add("BTN");
        validCountries.add("BVT");
        validCountries.add("BWA");
        validCountries.add("CAF");
        validCountries.add("CAN");
        validCountries.add("CCK");
        validCountries.add("CHE");
        validCountries.add("CHL");
        validCountries.add("CHN");
        validCountries.add("CIV");
        validCountries.add("CMR");
        validCountries.add("COD");
        validCountries.add("COG");
        validCountries.add("COK");
        validCountries.add("COL");
        validCountries.add("COM");
        validCountries.add("CPV");
        validCountries.add("CRI");
        validCountries.add("CUB");
        validCountries.add("CUW");
        validCountries.add("CXR");
        validCountries.add("CYM");
        validCountries.add("CYP");
        validCountries.add("CZE");
        validCountries.add("DEU");
        validCountries.add("DJI");
        validCountries.add("DMA");
        validCountries.add("DNK");
        validCountries.add("DOM");
        validCountries.add("DZA");
        validCountries.add("ECU");
        validCountries.add("EGY");
        validCountries.add("ERI");
        validCountries.add("ESH");
        validCountries.add("ESP");
        validCountries.add("EST");
        validCountries.add("ETH");
        validCountries.add("FIN");
        validCountries.add("FJI");
        validCountries.add("FLK");
        validCountries.add("FRA");
        validCountries.add("FRO");
        validCountries.add("FSM");
        validCountries.add("GAB");
        validCountries.add("GBR");
        validCountries.add("GEO");
        validCountries.add("GGY");
        validCountries.add("GHA");
        validCountries.add("GIB");
        validCountries.add("GIN");
        validCountries.add("GLP");
        validCountries.add("GMB");
        validCountries.add("GNB");
        validCountries.add("GNQ");
        validCountries.add("GRC");
        validCountries.add("GRD");
        validCountries.add("GRL");
        validCountries.add("GTM");
        validCountries.add("GUF");
        validCountries.add("GUM");
        validCountries.add("GUY");
        validCountries.add("HKG");
        validCountries.add("HMD");
        validCountries.add("HND");
        validCountries.add("HRV");
        validCountries.add("HTI");
        validCountries.add("HUN");
        validCountries.add("IDN");
        validCountries.add("IMN");
        validCountries.add("IND");
        validCountries.add("IOT");
        validCountries.add("IRL");
        validCountries.add("IRN");
        validCountries.add("IRQ");
        validCountries.add("ISL");
        validCountries.add("ISR");
        validCountries.add("ITA");
        validCountries.add("JAM");
        validCountries.add("JEY");
        validCountries.add("JOR");
        validCountries.add("JPN");
        validCountries.add("KAZ");
        validCountries.add("KEN");
        validCountries.add("KGZ");
        validCountries.add("KHM");
        validCountries.add("KIR");
        validCountries.add("KNA");
        validCountries.add("KOR");
        validCountries.add("KWT");
        validCountries.add("LAO");
        validCountries.add("LBN");
        validCountries.add("LBR");
        validCountries.add("LBY");
        validCountries.add("LCA");
        validCountries.add("LIE");
        validCountries.add("LKA");
        validCountries.add("LSO");
        validCountries.add("LTU");
        validCountries.add("LUX");
        validCountries.add("LVA");
        validCountries.add("MAC");
        validCountries.add("MAF");
        validCountries.add("MAR");
        validCountries.add("MCO");
        validCountries.add("MDA");
        validCountries.add("MDG");
        validCountries.add("MDV");
        validCountries.add("MEX");
        validCountries.add("MHL");
        validCountries.add("MKD");
        validCountries.add("MLI");
        validCountries.add("MLT");
        validCountries.add("MMR");
        validCountries.add("MNE");
        validCountries.add("MNG");
        validCountries.add("MNP");
        validCountries.add("MOZ");
        validCountries.add("MRT");
        validCountries.add("MSR");
        validCountries.add("MTQ");
        validCountries.add("MUS");
        validCountries.add("MWI");
        validCountries.add("MYS");
        validCountries.add("MYT");
        validCountries.add("NAM");
        validCountries.add("NCL");
        validCountries.add("NER");
        validCountries.add("NFK");
        validCountries.add("NGA");
        validCountries.add("NIC");
        validCountries.add("NIU");
        validCountries.add("NLD");
        validCountries.add("NOR");
        validCountries.add("NPL");
        validCountries.add("NRU");
        validCountries.add("NZL");
        validCountries.add("OMN");
        validCountries.add("PAK");
        validCountries.add("PAN");
        validCountries.add("PCN");
        validCountries.add("PER");
        validCountries.add("PHL");
        validCountries.add("PLW");
        validCountries.add("PNG");
        validCountries.add("POL");
        validCountries.add("PRI");
        validCountries.add("PRK");
        validCountries.add("PRT");
        validCountries.add("PRY");
        validCountries.add("PSE");
        validCountries.add("PYF");
        validCountries.add("QAT");
        validCountries.add("REU");
        validCountries.add("ROU");
        validCountries.add("RUS");
        validCountries.add("RWA");
        validCountries.add("SAU");
        validCountries.add("SDN");
        validCountries.add("SEN");
        validCountries.add("SGP");
        validCountries.add("SGS");
        validCountries.add("SHN");
        validCountries.add("SJM");
        validCountries.add("SLB");
        validCountries.add("SLE");
        validCountries.add("SLV");
        validCountries.add("SMR");
        validCountries.add("SOM");
        validCountries.add("SPM");
        validCountries.add("SRB");
        validCountries.add("STP");
        validCountries.add("SUR");
        validCountries.add("SVK");
        validCountries.add("SVN");
        validCountries.add("SWE");
        validCountries.add("SWZ");
        validCountries.add("SXM");
        validCountries.add("SYC");
        validCountries.add("SYR");
        validCountries.add("TCA");
        validCountries.add("TCD");
        validCountries.add("TGO");
        validCountries.add("THA");
        validCountries.add("TJK");
        validCountries.add("TKL");
        validCountries.add("TKM");
        validCountries.add("TLS");
        validCountries.add("TON");
        validCountries.add("TTO");
        validCountries.add("TUN");
        validCountries.add("TUR");
        validCountries.add("TUV");
        validCountries.add("TWN");
        validCountries.add("TZA");
        validCountries.add("UGA");
        validCountries.add("UKR");
        validCountries.add("UMI");
        validCountries.add("URY");
        validCountries.add("USA");
        validCountries.add("UZB");
        validCountries.add("VAT");
        validCountries.add("VCT");
        validCountries.add("VEN");
        validCountries.add("VGB");
        validCountries.add("VIR");
        validCountries.add("VNM");
        validCountries.add("VUT");
        validCountries.add("WLF");
        validCountries.add("WSM");
        validCountries.add("YEM");
        validCountries.add("ZAF");
        validCountries.add("ZMB");
        validCountries.add("ZWE");

        validCategories = Arrays.asList("1", "2", "3", "4", "4a", "4b", "4c", "6", "7", "7a", "7b", "8", "N/A");
    }



    // TODO test
    public static boolean isCountryIdValid(IdType idType){
        if (idType == null || StringUtils.isBlank(idType.getValue()) || idType.getValue().length() != 3) {
            return false;
        }

        return validCountries.contains(idType.getValue());
    }

    public static String[] getValidCategories() {
        return (String[])validCategories.toArray();
    }

}
