package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.europa.ec.fisheries.schema.sales.AmountType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by MATBUL on 21/06/2017.
 */
public class SalesFactHelper {

    static HashSet<String> validCountries = new HashSet<>();

    static List<String> validCategories;

    static HashSet<String> validCurrencies = new HashSet<>();

    static {
        fillValidCountries();
        fillValidCurrencies();
        validCategories = Arrays.asList("1", "2", "3", "4", "4a", "4b", "4c", "6", "7", "7a", "7b", "8", "N/A");
    }

    public static boolean doesSetContainAnyValue(List<String> values, Set set) {
        for (String value : values) {
            if (set.contains(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCountryIdValid(IdType idType) {
        if (idType == null || StringUtils.isBlank(idType.getValue()) || idType.getValue().length() != 3) {
            return false;
        }

        return validCountries.contains(idType.getValue());
    }

    public static String[] getValidCategories() {
        return (String[]) validCategories.toArray();
    }

    public static boolean allValuesGreaterOrEqualToZero(List<AmountType> amountTypes) {
        for (AmountType amountType : amountTypes) {
            if (amountType == null || amountType.getValue() == null || amountType.getValue().compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean anyValueEqualToZero(List<AmountType> amountTypes) {
        for (AmountType amountType : amountTypes) {
            if (amountType == null || amountType.getValue() == null || amountType.getValue().compareTo(BigDecimal.ZERO) == 0) {
                return true;
            }
        }

        return false;
    }

    public static HashSet<String> getValidCountries() {
        return validCountries;
    }


    public static HashSet<String> getValidCurrencies() {
        return validCurrencies;
    }


    private static void fillValidCountries() {

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
    }

    private static void fillValidCurrencies() {
        validCurrencies.add("AWG");
        validCurrencies.add("AFN");
        validCurrencies.add("AOA");
        validCurrencies.add("XCD");
        validCurrencies.add("EUR");
        validCurrencies.add("ALL");
        validCurrencies.add("AED");
        validCurrencies.add("ARS");
        validCurrencies.add("AMD");
        validCurrencies.add("USD");
        validCurrencies.add("AUD");
        validCurrencies.add("AZN");
        validCurrencies.add("BIF");
        validCurrencies.add("XOF");
        validCurrencies.add("BDT");
        validCurrencies.add("BGN");
        validCurrencies.add("BHD");
        validCurrencies.add("BSD");
        validCurrencies.add("BAM");
        validCurrencies.add("BYR");
        validCurrencies.add("BZD");
        validCurrencies.add("BMD");
        validCurrencies.add("BOB");
        validCurrencies.add("BOV");
        validCurrencies.add("BRL");
        validCurrencies.add("BBD");
        validCurrencies.add("BND");
        validCurrencies.add("BTN");
        validCurrencies.add("INR");
        validCurrencies.add("NOK");
        validCurrencies.add("BWP");
        validCurrencies.add("XAF");
        validCurrencies.add("CAD");
        validCurrencies.add("CHE");
        validCurrencies.add("CHF");
        validCurrencies.add("CHW");
        validCurrencies.add("CLF");
        validCurrencies.add("CLP");
        validCurrencies.add("CNY");
        validCurrencies.add("CDF");
        validCurrencies.add("NZD");
        validCurrencies.add("COP");
        validCurrencies.add("COU");
        validCurrencies.add("KMF");
        validCurrencies.add("CVE");
        validCurrencies.add("CRC");
        validCurrencies.add("CUC");
        validCurrencies.add("CUP");
        validCurrencies.add("ANG");
        validCurrencies.add("KYD");
        validCurrencies.add("CZK");
        validCurrencies.add("DJF");
        validCurrencies.add("DKK");
        validCurrencies.add("DOP");
        validCurrencies.add("DZD");
        validCurrencies.add("EGP");
        validCurrencies.add("ERN");
        validCurrencies.add("MAD");
        validCurrencies.add("ETB");
        validCurrencies.add("FJD");
        validCurrencies.add("FKP");
        validCurrencies.add("GBP");
        validCurrencies.add("GEL");
        validCurrencies.add("GHS");
        validCurrencies.add("GIP");
        validCurrencies.add("GNF");
        validCurrencies.add("GMD");
        validCurrencies.add("GTQ");
        validCurrencies.add("GYD");
        validCurrencies.add("HKD");
        validCurrencies.add("HNL");
        validCurrencies.add("HRK");
        validCurrencies.add("HTG");
        validCurrencies.add("HUF");
        validCurrencies.add("IDR");
        validCurrencies.add("IRR");
        validCurrencies.add("IQD");
        validCurrencies.add("ISK");
        validCurrencies.add("ILS");
        validCurrencies.add("JMD");
        validCurrencies.add("JOD");
        validCurrencies.add("JPY");
        validCurrencies.add("KZT");
        validCurrencies.add("KES");
        validCurrencies.add("KGS");
        validCurrencies.add("KHR");
        validCurrencies.add("KRW");
        validCurrencies.add("KWD");
        validCurrencies.add("LAK");
        validCurrencies.add("LBP");
        validCurrencies.add("LRD");
        validCurrencies.add("LYD");
        validCurrencies.add("LKR");
        validCurrencies.add("LSL");
        validCurrencies.add("ZAR");
        validCurrencies.add("MOP");
        validCurrencies.add("MDL");
        validCurrencies.add("MGA");
        validCurrencies.add("MVR");
        validCurrencies.add("MXN");
        validCurrencies.add("MKD");
        validCurrencies.add("MMK");
        validCurrencies.add("MNT");
        validCurrencies.add("MZN");
        validCurrencies.add("MRO");
        validCurrencies.add("MUR");
        validCurrencies.add("MWK");
        validCurrencies.add("MYR");
        validCurrencies.add("NAD");
        validCurrencies.add("XPF");
        validCurrencies.add("NGN");
        validCurrencies.add("NIO");
        validCurrencies.add("NPR");
        validCurrencies.add("OMR");
        validCurrencies.add("PKR");
        validCurrencies.add("PAB");
        validCurrencies.add("PEN");
        validCurrencies.add("PHP");
        validCurrencies.add("PGK");
        validCurrencies.add("PLN");
        validCurrencies.add("KPW");
        validCurrencies.add("PYG");
        validCurrencies.add("QAR");
        validCurrencies.add("RON");
        validCurrencies.add("RUB");
        validCurrencies.add("RWF");
        validCurrencies.add("SAR");
        validCurrencies.add("SDG");
        validCurrencies.add("SGD");
        validCurrencies.add("SHP");
        validCurrencies.add("SBD");
        validCurrencies.add("SLL");
        validCurrencies.add("SVC");
        validCurrencies.add("SOS");
        validCurrencies.add("RSD");
        validCurrencies.add("STD");
        validCurrencies.add("SRD");
        validCurrencies.add("SEK");
        validCurrencies.add("SZL");
        validCurrencies.add("SCR");
        validCurrencies.add("SYP");
        validCurrencies.add("THB");
        validCurrencies.add("TJS");
        validCurrencies.add("TMT");
        validCurrencies.add("TOP");
        validCurrencies.add("TTD");
        validCurrencies.add("TND");
        validCurrencies.add("TRY");
        validCurrencies.add("TWD");
        validCurrencies.add("TZS");
        validCurrencies.add("UGX");
        validCurrencies.add("UAH");
        validCurrencies.add("UYU");
        validCurrencies.add("UZS");
        validCurrencies.add("VEF");
        validCurrencies.add("VND");
        validCurrencies.add("VUV");
        validCurrencies.add("WST");
        validCurrencies.add("YER");
        validCurrencies.add("ZMK");
        validCurrencies.add("ZWL");
    }
}
