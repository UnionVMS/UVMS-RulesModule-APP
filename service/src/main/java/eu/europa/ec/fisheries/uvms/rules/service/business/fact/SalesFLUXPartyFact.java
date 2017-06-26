package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesFLUXPartyFact extends AbstractFact {

    private List<IdType> ids;
    private List<TextType> names;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_FLUX_PARTY;
    }

    public List<IdType> getIDS() {
        return this.ids;
    }

    public List<TextType> getNames() {
        return this.names;
    }

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setNames(List<TextType> names) {
        this.names = names;
    }

    public boolean anyInvalidPartyId(){
        String[] validParties = new String[248];
        validParties[0] = "ABW";
        validParties[1] = "AFG";
        validParties[2] = "AGO";
        validParties[3] = "AIA";
        validParties[4] = "ALA";
        validParties[5] = "ALB";
        validParties[6] = "AND";
        validParties[7] = "ARE";
        validParties[8] = "ARG";
        validParties[9] = "ARM";
        validParties[10] = "ASM";
        validParties[11] = "ATA";
        validParties[12] = "ATF";
        validParties[13] = "ATG";
        validParties[14] = "AUS";
        validParties[15] = "AUT";
        validParties[16] = "AZE";
        validParties[17] = "BDI";
        validParties[18] = "BEL";
        validParties[19] = "BEN";
        validParties[20] = "BES";
        validParties[21] = "BFA";
        validParties[22] = "BGD";
        validParties[23] = "BGR";
        validParties[24] = "BHR";
        validParties[25] = "BHS";
        validParties[26] = "BIH";
        validParties[27] = "BLM";
        validParties[28] = "BLR";
        validParties[29] = "BLZ";
        validParties[30] = "BMU";
        validParties[31] = "BOL";
        validParties[32] = "BRA";
        validParties[33] = "BRB";
        validParties[34] = "BRN";
        validParties[35] = "BTN";
        validParties[36] = "BVT";
        validParties[37] = "BWA";
        validParties[38] = "CAF";
        validParties[39] = "CAN";
        validParties[40] = "CCK";
        validParties[41] = "CHE";
        validParties[42] = "CHL";
        validParties[43] = "CHN";
        validParties[44] = "CIV";
        validParties[45] = "CMR";
        validParties[46] = "COD";
        validParties[47] = "COG";
        validParties[48] = "COK";
        validParties[49] = "COL";
        validParties[50] = "COM";
        validParties[51] = "CPV";
        validParties[52] = "CRI";
        validParties[53] = "CUB";
        validParties[54] = "CUW";
        validParties[55] = "CXR";
        validParties[56] = "CYM";
        validParties[57] = "CYP";
        validParties[58] = "CZE";
        validParties[59] = "DEU";
        validParties[60] = "DJI";
        validParties[61] = "DMA";
        validParties[62] = "DNK";
        validParties[63] = "DOM";
        validParties[64] = "DZA";
        validParties[65] = "ECU";
        validParties[66] = "EGY";
        validParties[67] = "ERI";
        validParties[68] = "ESH";
        validParties[69] = "ESP";
        validParties[70] = "EST";
        validParties[71] = "ETH";
        validParties[72] = "FIN";
        validParties[73] = "FJI";
        validParties[74] = "FLK";
        validParties[75] = "FRA";
        validParties[76] = "FRO";
        validParties[77] = "FSM";
        validParties[78] = "GAB";
        validParties[79] = "GBR";
        validParties[80] = "GEO";
        validParties[81] = "GGY";
        validParties[82] = "GHA";
        validParties[83] = "GIB";
        validParties[84] = "GIN";
        validParties[85] = "GLP";
        validParties[86] = "GMB";
        validParties[87] = "GNB";
        validParties[88] = "GNQ";
        validParties[89] = "GRC";
        validParties[90] = "GRD";
        validParties[91] = "GRL";
        validParties[92] = "GTM";
        validParties[93] = "GUF";
        validParties[94] = "GUM";
        validParties[95] = "GUY";
        validParties[96] = "HKG";
        validParties[97] = "HMD";
        validParties[98] = "HND";
        validParties[99] = "HRV";
        validParties[100] = "HTI";
        validParties[101] = "HUN";
        validParties[102] = "IDN";
        validParties[103] = "IMN";
        validParties[104] = "IND";
        validParties[105] = "IOT";
        validParties[106] = "IRL";
        validParties[107] = "IRN";
        validParties[108] = "IRQ";
        validParties[109] = "ISL";
        validParties[110] = "ISR";
        validParties[111] = "ITA";
        validParties[112] = "JAM";
        validParties[113] = "JEY";
        validParties[114] = "JOR";
        validParties[115] = "JPN";
        validParties[116] = "KAZ";
        validParties[117] = "KEN";
        validParties[118] = "KGZ";
        validParties[119] = "KHM";
        validParties[120] = "KIR";
        validParties[121] = "KNA";
        validParties[122] = "KOR";
        validParties[123] = "KWT";
        validParties[124] = "LAO";
        validParties[125] = "LBN";
        validParties[126] = "LBR";
        validParties[127] = "LBY";
        validParties[128] = "LCA";
        validParties[129] = "LIE";
        validParties[130] = "LKA";
        validParties[131] = "LSO";
        validParties[132] = "LTU";
        validParties[133] = "LUX";
        validParties[134] = "LVA";
        validParties[135] = "MAC";
        validParties[136] = "MAF";
        validParties[137] = "MAR";
        validParties[138] = "MCO";
        validParties[139] = "MDA";
        validParties[140] = "MDG";
        validParties[141] = "MDV";
        validParties[142] = "MEX";
        validParties[143] = "MHL";
        validParties[144] = "MKD";
        validParties[145] = "MLI";
        validParties[146] = "MLT";
        validParties[147] = "MMR";
        validParties[148] = "MNE";
        validParties[149] = "MNG";
        validParties[150] = "MNP";
        validParties[151] = "MOZ";
        validParties[152] = "MRT";
        validParties[153] = "MSR";
        validParties[154] = "MTQ";
        validParties[155] = "MUS";
        validParties[156] = "MWI";
        validParties[157] = "MYS";
        validParties[158] = "MYT";
        validParties[159] = "NAM";
        validParties[160] = "NCL";
        validParties[161] = "NER";
        validParties[162] = "NFK";
        validParties[163] = "NGA";
        validParties[164] = "NIC";
        validParties[165] = "NIU";
        validParties[166] = "NLD";
        validParties[167] = "NOR";
        validParties[168] = "NPL";
        validParties[169] = "NRU";
        validParties[170] = "NZL";
        validParties[171] = "OMN";
        validParties[172] = "PAK";
        validParties[173] = "PAN";
        validParties[174] = "PCN";
        validParties[175] = "PER";
        validParties[176] = "PHL";
        validParties[177] = "PLW";
        validParties[178] = "PNG";
        validParties[179] = "POL";
        validParties[180] = "PRI";
        validParties[181] = "PRK";
        validParties[182] = "PRT";
        validParties[183] = "PRY";
        validParties[184] = "PSE";
        validParties[185] = "PYF";
        validParties[186] = "QAT";
        validParties[187] = "REU";
        validParties[188] = "ROU";
        validParties[189] = "RUS";
        validParties[190] = "RWA";
        validParties[191] = "SAU";
        validParties[192] = "SDN";
        validParties[193] = "SEN";
        validParties[194] = "SGP";
        validParties[195] = "SGS";
        validParties[196] = "SHN";
        validParties[197] = "SJM";
        validParties[198] = "SLB";
        validParties[199] = "SLE";
        validParties[200] = "SLV";
        validParties[201] = "SMR";
        validParties[202] = "SOM";
        validParties[203] = "SPM";
        validParties[204] = "SRB";
        validParties[205] = "STP";
        validParties[206] = "SUR";
        validParties[207] = "SVK";
        validParties[208] = "SVN";
        validParties[209] = "SWE";
        validParties[210] = "SWZ";
        validParties[211] = "SXM";
        validParties[212] = "SYC";
        validParties[213] = "SYR";
        validParties[214] = "TCA";
        validParties[215] = "TCD";
        validParties[216] = "TGO";
        validParties[217] = "THA";
        validParties[218] = "TJK";
        validParties[219] = "TKL";
        validParties[220] = "TKM";
        validParties[221] = "TLS";
        validParties[222] = "TON";
        validParties[223] = "TTO";
        validParties[224] = "TUN";
        validParties[225] = "TUR";
        validParties[226] = "TUV";
        validParties[227] = "TWN";
        validParties[228] = "TZA";
        validParties[229] = "UGA";
        validParties[230] = "UKR";
        validParties[231] = "UMI";
        validParties[232] = "URY";
        validParties[233] = "USA";
        validParties[234] = "UZB";
        validParties[235] = "VAT";
        validParties[236] = "VCT";
        validParties[237] = "VEN";
        validParties[238] = "VGB";
        validParties[239] = "VIR";
        validParties[240] = "VNM";
        validParties[241] = "VUT";
        validParties[242] = "WLF";
        validParties[243] = "WSM";
        validParties[244] = "YEM";
        validParties[245] = "ZAF";
        validParties[246] = "ZMB";
        validParties[247] = "ZWE";

        return valueIdTypeContainsAny(getIDS(), validParties);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesFLUXPartyFact)) return false;
        SalesFLUXPartyFact that = (SalesFLUXPartyFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(names, that.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, names);
    }
}
