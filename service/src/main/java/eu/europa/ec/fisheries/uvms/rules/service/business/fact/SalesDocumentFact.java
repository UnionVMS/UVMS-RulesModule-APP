package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;
import java.util.Objects;

public class SalesDocumentFact extends AbstractFact {

    private List<IdType> ids;
    private CodeType currencyCode;
    private List<IdType> transportDocumentIDs;
    private List<IdType> salesNoteIDs;
    private List<IdType> takeoverDocumentIDs;
    private List<SalesBatchType> specifiedSalesBatches;
    private List<SalesEventType> specifiedSalesEvents;
    private List<FishingActivityType> specifiedFishingActivities;
    private List<FLUXLocationType> specifiedFLUXLocations;
    private List<SalesPartyType> specifiedSalesParties;
    private VehicleTransportMeansType specifiedVehicleTransportMeans;
    private List<ValidationResultDocumentType> relatedValidationResultDocuments;
    private SalesPriceType totalSalesPrice;
    private FLUXLocationType departureSpecifiedFLUXLocation;
    private FLUXLocationType arrivalSpecifiedFLUXLocation;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_DOCUMENT;
    }

    public List<IdType> getIDS() {
        return this.ids;
    }

    public CodeType getCurrencyCode() {
        return this.currencyCode;
    }

    public List<IdType> getTransportDocumentIDs() {
        return this.transportDocumentIDs;
    }

    public List<IdType> getSalesNoteIDs() {
        return this.salesNoteIDs;
    }

    public List<IdType> getTakeoverDocumentIDs() {
        return this.takeoverDocumentIDs;
    }

    public List<SalesBatchType> getSpecifiedSalesBatches() {
        return this.specifiedSalesBatches;
    }

    public List<SalesEventType> getSpecifiedSalesEvents() {
        return this.specifiedSalesEvents;
    }

    public List<FishingActivityType> getSpecifiedFishingActivities() {
        return this.specifiedFishingActivities;
    }

    public List<FLUXLocationType> getSpecifiedFLUXLocations() {
        return this.specifiedFLUXLocations;
    }

    public List<SalesPartyType> getSpecifiedSalesParties() {
        return this.specifiedSalesParties;
    }

    public VehicleTransportMeansType getSpecifiedVehicleTransportMeans() {
        return this.specifiedVehicleTransportMeans;
    }

    public List<ValidationResultDocumentType> getRelatedValidationResultDocuments() {
        return this.relatedValidationResultDocuments;
    }

    public SalesPriceType getTotalSalesPrice() {
        return this.totalSalesPrice;
    }

    public FLUXLocationType getDepartureSpecifiedFLUXLocation() {
        return this.departureSpecifiedFLUXLocation;
    }

    public FLUXLocationType getArrivalSpecifiedFLUXLocation() {
        return this.arrivalSpecifiedFLUXLocation;
    }

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setCurrencyCode(CodeType currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setTransportDocumentIDs(List<IdType> transportDocumentIDs) {
        this.transportDocumentIDs = transportDocumentIDs;
    }

    public void setSalesNoteIDs(List<IdType> salesNoteIDs) {
        this.salesNoteIDs = salesNoteIDs;
    }

    public void setTakeoverDocumentIDs(List<IdType> takeoverDocumentIDs) {
        this.takeoverDocumentIDs = takeoverDocumentIDs;
    }

    public void setSpecifiedSalesBatches(List<SalesBatchType> specifiedSalesBatches) {
        this.specifiedSalesBatches = specifiedSalesBatches;
    }

    public void setSpecifiedSalesEvents(List<SalesEventType> specifiedSalesEvents) {
        this.specifiedSalesEvents = specifiedSalesEvents;
    }

    public void setSpecifiedFishingActivities(List<FishingActivityType> specifiedFishingActivities) {
        this.specifiedFishingActivities = specifiedFishingActivities;
    }

    public void setSpecifiedFLUXLocations(List<FLUXLocationType> specifiedFLUXLocations) {
        this.specifiedFLUXLocations = specifiedFLUXLocations;
    }

    public void setSpecifiedSalesParties(List<SalesPartyType> specifiedSalesParties) {
        this.specifiedSalesParties = specifiedSalesParties;
    }

    public void setSpecifiedVehicleTransportMeans(VehicleTransportMeansType specifiedVehicleTransportMeans) {
        this.specifiedVehicleTransportMeans = specifiedVehicleTransportMeans;
    }

    public void setRelatedValidationResultDocuments(List<ValidationResultDocumentType> relatedValidationResultDocuments) {
        this.relatedValidationResultDocuments = relatedValidationResultDocuments;
    }

    public void setTotalSalesPrice(SalesPriceType totalSalesPrice) {
        this.totalSalesPrice = totalSalesPrice;
    }

    public void setDepartureSpecifiedFLUXLocation(FLUXLocationType departureSpecifiedFLUXLocation) {
        this.departureSpecifiedFLUXLocation = departureSpecifiedFLUXLocation;
    }

    public void setArrivalSpecifiedFLUXLocation(FLUXLocationType arrivalSpecifiedFLUXLocation) {
        this.arrivalSpecifiedFLUXLocation = arrivalSpecifiedFLUXLocation;
    }

    public boolean isInvalidCurencyCode(){
        String[] validCurrencies = new String[161];
        validCurrencies[0] = "AWG";
        validCurrencies[1] = "AFN";
        validCurrencies[2] = "AOA";
        validCurrencies[3] = "XCD";
        validCurrencies[4] = "EUR";
        validCurrencies[5] = "ALL";
        validCurrencies[6] = "AED";
        validCurrencies[7] = "ARS";
        validCurrencies[8] = "AMD";
        validCurrencies[9] = "USD";
        validCurrencies[10] = "AUD";
        validCurrencies[11] = "AZN";
        validCurrencies[12] = "BIF";
        validCurrencies[13] = "XOF";
        validCurrencies[14] = "BDT";
        validCurrencies[15] = "BGN";
        validCurrencies[16] = "BHD";
        validCurrencies[17] = "BSD";
        validCurrencies[18] = "BAM";
        validCurrencies[19] = "BYR";
        validCurrencies[20] = "BZD";
        validCurrencies[21] = "BMD";
        validCurrencies[22] = "BOB";
        validCurrencies[23] = "BOV";
        validCurrencies[24] = "BRL";
        validCurrencies[25] = "BBD";
        validCurrencies[26] = "BND";
        validCurrencies[27] = "BTN";
        validCurrencies[28] = "INR";
        validCurrencies[29] = "NOK";
        validCurrencies[30] = "BWP";
        validCurrencies[31] = "XAF";
        validCurrencies[32] = "CAD";
        validCurrencies[33] = "CHE";
        validCurrencies[34] = "CHF";
        validCurrencies[35] = "CHW";
        validCurrencies[36] = "CLF";
        validCurrencies[37] = "CLP";
        validCurrencies[38] = "CNY";
        validCurrencies[39] = "CDF";
        validCurrencies[40] = "NZD";
        validCurrencies[41] = "COP";
        validCurrencies[42] = "COU";
        validCurrencies[43] = "KMF";
        validCurrencies[44] = "CVE";
        validCurrencies[45] = "CRC";
        validCurrencies[46] = "CUC";
        validCurrencies[47] = "CUP";
        validCurrencies[48] = "ANG";
        validCurrencies[49] = "KYD";
        validCurrencies[50] = "CZK";
        validCurrencies[51] = "DJF";
        validCurrencies[52] = "DKK";
        validCurrencies[53] = "DOP";
        validCurrencies[54] = "DZD";
        validCurrencies[55] = "EGP";
        validCurrencies[56] = "ERN";
        validCurrencies[57] = "MAD";
        validCurrencies[58] = "ETB";
        validCurrencies[59] = "FJD";
        validCurrencies[60] = "FKP";
        validCurrencies[61] = "GBP";
        validCurrencies[62] = "GEL";
        validCurrencies[63] = "GHS";
        validCurrencies[64] = "GIP";
        validCurrencies[65] = "GNF";
        validCurrencies[66] = "GMD";
        validCurrencies[67] = "GTQ";
        validCurrencies[68] = "GYD";
        validCurrencies[69] = "HKD";
        validCurrencies[70] = "HNL";
        validCurrencies[71] = "HRK";
        validCurrencies[72] = "HTG";
        validCurrencies[73] = "HUF";
        validCurrencies[74] = "IDR";
        validCurrencies[75] = "IRR";
        validCurrencies[76] = "IQD";
        validCurrencies[77] = "ISK";
        validCurrencies[78] = "ILS";
        validCurrencies[79] = "JMD";
        validCurrencies[80] = "JOD";
        validCurrencies[81] = "JPY";
        validCurrencies[82] = "KZT";
        validCurrencies[83] = "KES";
        validCurrencies[84] = "KGS";
        validCurrencies[85] = "KHR";
        validCurrencies[86] = "KRW";
        validCurrencies[87] = "KWD";
        validCurrencies[88] = "LAK";
        validCurrencies[89] = "LBP";
        validCurrencies[90] = "LRD";
        validCurrencies[91] = "LYD";
        validCurrencies[92] = "LKR";
        validCurrencies[93] = "LSL";
        validCurrencies[94] = "ZAR";
        validCurrencies[95] = "MOP";
        validCurrencies[96] = "MDL";
        validCurrencies[97] = "MGA";
        validCurrencies[98] = "MVR";
        validCurrencies[99] = "MXN";
        validCurrencies[100] = "MKD";
        validCurrencies[101] = "MMK";
        validCurrencies[102] = "MNT";
        validCurrencies[103] = "MZN";
        validCurrencies[104] = "MRO";
        validCurrencies[105] = "MUR";
        validCurrencies[106] = "MWK";
        validCurrencies[107] = "MYR";
        validCurrencies[108] = "NAD";
        validCurrencies[109] = "XPF";
        validCurrencies[110] = "NGN";
        validCurrencies[111] = "NIO";
        validCurrencies[112] = "NPR";
        validCurrencies[113] = "OMR";
        validCurrencies[114] = "PKR";
        validCurrencies[115] = "PAB";
        validCurrencies[116] = "PEN";
        validCurrencies[117] = "PHP";
        validCurrencies[118] = "PGK";
        validCurrencies[119] = "PLN";
        validCurrencies[120] = "KPW";
        validCurrencies[121] = "PYG";
        validCurrencies[122] = "QAR";
        validCurrencies[123] = "RON";
        validCurrencies[124] = "RUB";
        validCurrencies[125] = "RWF";
        validCurrencies[126] = "SAR";
        validCurrencies[127] = "SDG";
        validCurrencies[128] = "SGD";
        validCurrencies[129] = "SHP";
        validCurrencies[130] = "SBD";
        validCurrencies[131] = "SLL";
        validCurrencies[132] = "SVC";
        validCurrencies[133] = "SOS";
        validCurrencies[134] = "RSD";
        validCurrencies[135] = "STD";
        validCurrencies[136] = "SRD";
        validCurrencies[137] = "SEK";
        validCurrencies[138] = "SZL";
        validCurrencies[139] = "SCR";
        validCurrencies[140] = "SYP";
        validCurrencies[141] = "THB";
        validCurrencies[142] = "TJS";
        validCurrencies[143] = "TMT";
        validCurrencies[144] = "TOP";
        validCurrencies[145] = "TTD";
        validCurrencies[146] = "TND";
        validCurrencies[147] = "TRY";
        validCurrencies[148] = "TWD";
        validCurrencies[149] = "TZS";
        validCurrencies[150] = "UGX";
        validCurrencies[151] = "UAH";
        validCurrencies[152] = "UYU";
        validCurrencies[153] = "UZS";
        validCurrencies[154] = "VEF";
        validCurrencies[155] = "VND";
        validCurrencies[156] = "VUV";
        validCurrencies[157] = "WST";
        validCurrencies[158] = "YER";
        validCurrencies[159] = "ZMK";
        validCurrencies[160] = "ZWL";

        return valueContainsAny(currencyCode, validCurrencies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesDocumentFact)) return false;
        SalesDocumentFact that = (SalesDocumentFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(transportDocumentIDs, that.transportDocumentIDs) &&
                Objects.equals(salesNoteIDs, that.salesNoteIDs) &&
                Objects.equals(takeoverDocumentIDs, that.takeoverDocumentIDs) &&
                Objects.equals(specifiedSalesBatches, that.specifiedSalesBatches) &&
                Objects.equals(specifiedSalesEvents, that.specifiedSalesEvents) &&
                Objects.equals(specifiedFishingActivities, that.specifiedFishingActivities) &&
                Objects.equals(specifiedFLUXLocations, that.specifiedFLUXLocations) &&
                Objects.equals(specifiedSalesParties, that.specifiedSalesParties) &&
                Objects.equals(specifiedVehicleTransportMeans, that.specifiedVehicleTransportMeans) &&
                Objects.equals(relatedValidationResultDocuments, that.relatedValidationResultDocuments) &&
                Objects.equals(totalSalesPrice, that.totalSalesPrice) &&
                Objects.equals(departureSpecifiedFLUXLocation, that.departureSpecifiedFLUXLocation) &&
                Objects.equals(arrivalSpecifiedFLUXLocation, that.arrivalSpecifiedFLUXLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, currencyCode, transportDocumentIDs, salesNoteIDs, takeoverDocumentIDs, specifiedSalesBatches, specifiedSalesEvents, specifiedFishingActivities, specifiedFLUXLocations, specifiedSalesParties, specifiedVehicleTransportMeans, relatedValidationResultDocuments, totalSalesPrice, departureSpecifiedFLUXLocation, arrivalSpecifiedFLUXLocation);
    }
}
