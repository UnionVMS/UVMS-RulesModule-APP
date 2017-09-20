/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

/**
 * Created by padhyad on 4/21/2017.
 */
public class StructuredAddressFact extends AbstractFact {

    private String postcodeCode;

    private String streetName;

    private String cityName;

    private IdType countryID;

    private String plotIdentification;

    public StructuredAddressFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.STRUCTURED_ADDRESS;
    }

    public String getPostcodeCode() {
        return postcodeCode;
    }

    public void setPostcodeCode(String postcodeCode) {
        this.postcodeCode = postcodeCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPlotIdentification() {
        return plotIdentification;
    }

    public void setPlotIdentification(String plotIdentification) {
        this.plotIdentification = plotIdentification;
    }

    public IdType getCountryID() {
        return countryID;
    }

    public void setCountryID(IdType countryID) {
        this.countryID = countryID;
    }

    // TODO test
    public boolean isIdValid() {
        String[] validCountries = new String[248];
        validCountries[0] = "ABW";
        validCountries[1] = "AFG";
        validCountries[2] = "AGO";
        validCountries[3] = "AIA";
        validCountries[4] = "ALA";
        validCountries[5] = "ALB";
        validCountries[6] = "AND";
        validCountries[7] = "ARE";
        validCountries[8] = "ARG";
        validCountries[9] = "ARM";
        validCountries[10] = "ASM";
        validCountries[11] = "ATA";
        validCountries[12] = "ATF";
        validCountries[13] = "ATG";
        validCountries[14] = "AUS";
        validCountries[15] = "AUT";
        validCountries[16] = "AZE";
        validCountries[17] = "BDI";
        validCountries[18] = "BEL";
        validCountries[19] = "BEN";
        validCountries[20] = "BES";
        validCountries[21] = "BFA";
        validCountries[22] = "BGD";
        validCountries[23] = "BGR";
        validCountries[24] = "BHR";
        validCountries[25] = "BHS";
        validCountries[26] = "BIH";
        validCountries[27] = "BLM";
        validCountries[28] = "BLR";
        validCountries[29] = "BLZ";
        validCountries[30] = "BMU";
        validCountries[31] = "BOL";
        validCountries[32] = "BRA";
        validCountries[33] = "BRB";
        validCountries[34] = "BRN";
        validCountries[35] = "BTN";
        validCountries[36] = "BVT";
        validCountries[37] = "BWA";
        validCountries[38] = "CAF";
        validCountries[39] = "CAN";
        validCountries[40] = "CCK";
        validCountries[41] = "CHE";
        validCountries[42] = "CHL";
        validCountries[43] = "CHN";
        validCountries[44] = "CIV";
        validCountries[45] = "CMR";
        validCountries[46] = "COD";
        validCountries[47] = "COG";
        validCountries[48] = "COK";
        validCountries[49] = "COL";
        validCountries[50] = "COM";
        validCountries[51] = "CPV";
        validCountries[52] = "CRI";
        validCountries[53] = "CUB";
        validCountries[54] = "CUW";
        validCountries[55] = "CXR";
        validCountries[56] = "CYM";
        validCountries[57] = "CYP";
        validCountries[58] = "CZE";
        validCountries[59] = "DEU";
        validCountries[60] = "DJI";
        validCountries[61] = "DMA";
        validCountries[62] = "DNK";
        validCountries[63] = "DOM";
        validCountries[64] = "DZA";
        validCountries[65] = "ECU";
        validCountries[66] = "EGY";
        validCountries[67] = "ERI";
        validCountries[68] = "ESH";
        validCountries[69] = "ESP";
        validCountries[70] = "EST";
        validCountries[71] = "ETH";
        validCountries[72] = "FIN";
        validCountries[73] = "FJI";
        validCountries[74] = "FLK";
        validCountries[75] = "FRA";
        validCountries[76] = "FRO";
        validCountries[77] = "FSM";
        validCountries[78] = "GAB";
        validCountries[79] = "GBR";
        validCountries[80] = "GEO";
        validCountries[81] = "GGY";
        validCountries[82] = "GHA";
        validCountries[83] = "GIB";
        validCountries[84] = "GIN";
        validCountries[85] = "GLP";
        validCountries[86] = "GMB";
        validCountries[87] = "GNB";
        validCountries[88] = "GNQ";
        validCountries[89] = "GRC";
        validCountries[90] = "GRD";
        validCountries[91] = "GRL";
        validCountries[92] = "GTM";
        validCountries[93] = "GUF";
        validCountries[94] = "GUM";
        validCountries[95] = "GUY";
        validCountries[96] = "HKG";
        validCountries[97] = "HMD";
        validCountries[98] = "HND";
        validCountries[99] = "HRV";
        validCountries[100] = "HTI";
        validCountries[101] = "HUN";
        validCountries[102] = "IDN";
        validCountries[103] = "IMN";
        validCountries[104] = "IND";
        validCountries[105] = "IOT";
        validCountries[106] = "IRL";
        validCountries[107] = "IRN";
        validCountries[108] = "IRQ";
        validCountries[109] = "ISL";
        validCountries[110] = "ISR";
        validCountries[111] = "ITA";
        validCountries[112] = "JAM";
        validCountries[113] = "JEY";
        validCountries[114] = "JOR";
        validCountries[115] = "JPN";
        validCountries[116] = "KAZ";
        validCountries[117] = "KEN";
        validCountries[118] = "KGZ";
        validCountries[119] = "KHM";
        validCountries[120] = "KIR";
        validCountries[121] = "KNA";
        validCountries[122] = "KOR";
        validCountries[123] = "KWT";
        validCountries[124] = "LAO";
        validCountries[125] = "LBN";
        validCountries[126] = "LBR";
        validCountries[127] = "LBY";
        validCountries[128] = "LCA";
        validCountries[129] = "LIE";
        validCountries[130] = "LKA";
        validCountries[131] = "LSO";
        validCountries[132] = "LTU";
        validCountries[133] = "LUX";
        validCountries[134] = "LVA";
        validCountries[135] = "MAC";
        validCountries[136] = "MAF";
        validCountries[137] = "MAR";
        validCountries[138] = "MCO";
        validCountries[139] = "MDA";
        validCountries[140] = "MDG";
        validCountries[141] = "MDV";
        validCountries[142] = "MEX";
        validCountries[143] = "MHL";
        validCountries[144] = "MKD";
        validCountries[145] = "MLI";
        validCountries[146] = "MLT";
        validCountries[147] = "MMR";
        validCountries[148] = "MNE";
        validCountries[149] = "MNG";
        validCountries[150] = "MNP";
        validCountries[151] = "MOZ";
        validCountries[152] = "MRT";
        validCountries[153] = "MSR";
        validCountries[154] = "MTQ";
        validCountries[155] = "MUS";
        validCountries[156] = "MWI";
        validCountries[157] = "MYS";
        validCountries[158] = "MYT";
        validCountries[159] = "NAM";
        validCountries[160] = "NCL";
        validCountries[161] = "NER";
        validCountries[162] = "NFK";
        validCountries[163] = "NGA";
        validCountries[164] = "NIC";
        validCountries[165] = "NIU";
        validCountries[166] = "NLD";
        validCountries[167] = "NOR";
        validCountries[168] = "NPL";
        validCountries[169] = "NRU";
        validCountries[170] = "NZL";
        validCountries[171] = "OMN";
        validCountries[172] = "PAK";
        validCountries[173] = "PAN";
        validCountries[174] = "PCN";
        validCountries[175] = "PER";
        validCountries[176] = "PHL";
        validCountries[177] = "PLW";
        validCountries[178] = "PNG";
        validCountries[179] = "POL";
        validCountries[180] = "PRI";
        validCountries[181] = "PRK";
        validCountries[182] = "PRT";
        validCountries[183] = "PRY";
        validCountries[184] = "PSE";
        validCountries[185] = "PYF";
        validCountries[186] = "QAT";
        validCountries[187] = "REU";
        validCountries[188] = "ROU";
        validCountries[189] = "RUS";
        validCountries[190] = "RWA";
        validCountries[191] = "SAU";
        validCountries[192] = "SDN";
        validCountries[193] = "SEN";
        validCountries[194] = "SGP";
        validCountries[195] = "SGS";
        validCountries[196] = "SHN";
        validCountries[197] = "SJM";
        validCountries[198] = "SLB";
        validCountries[199] = "SLE";
        validCountries[200] = "SLV";
        validCountries[201] = "SMR";
        validCountries[202] = "SOM";
        validCountries[203] = "SPM";
        validCountries[204] = "SRB";
        validCountries[205] = "STP";
        validCountries[206] = "SUR";
        validCountries[207] = "SVK";
        validCountries[208] = "SVN";
        validCountries[209] = "SWE";
        validCountries[210] = "SWZ";
        validCountries[211] = "SXM";
        validCountries[212] = "SYC";
        validCountries[213] = "SYR";
        validCountries[214] = "TCA";
        validCountries[215] = "TCD";
        validCountries[216] = "TGO";
        validCountries[217] = "THA";
        validCountries[218] = "TJK";
        validCountries[219] = "TKL";
        validCountries[220] = "TKM";
        validCountries[221] = "TLS";
        validCountries[222] = "TON";
        validCountries[223] = "TTO";
        validCountries[224] = "TUN";
        validCountries[225] = "TUR";
        validCountries[226] = "TUV";
        validCountries[227] = "TWN";
        validCountries[228] = "TZA";
        validCountries[229] = "UGA";
        validCountries[230] = "UKR";
        validCountries[231] = "UMI";
        validCountries[232] = "URY";
        validCountries[233] = "USA";
        validCountries[234] = "UZB";
        validCountries[235] = "VAT";
        validCountries[236] = "VCT";
        validCountries[237] = "VEN";
        validCountries[238] = "VGB";
        validCountries[239] = "VIR";
        validCountries[240] = "VNM";
        validCountries[241] = "VUT";
        validCountries[242] = "WLF";
        validCountries[243] = "WSM";
        validCountries[244] = "YEM";
        validCountries[245] = "ZAF";
        validCountries[246] = "ZMB";
        validCountries[247] = "ZWE";

        return valueIdTypeContainsAny(countryID, validCountries);
    }
}
