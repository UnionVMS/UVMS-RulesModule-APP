/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.mapstruct.ap.internal.util.Collections;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@Slf4j
public class CustomMapper {

    private CustomMapper() {

    }

    public static boolean isDatePresent(DateTimeType dateTimeType) {
        return (dateTimeType != null);
    }

    public static List<MeasureType> getDurationMeasure(List<DelimitedPeriod> delimitedPeriods) {
        List<MeasureType> measureTypes = null;
        if (CollectionUtils.isNotEmpty(delimitedPeriods)) {
            measureTypes = new ArrayList<>();
            for (DelimitedPeriod delimitedPeriod : delimitedPeriods) {
                if (delimitedPeriod.getDurationMeasure() != null) {
                    measureTypes.add(ActivityFactMapper.INSTANCE.mapToMeasureType(delimitedPeriod.getDurationMeasure()));
                }
            }
        }
        return measureTypes;
    }

    public static List<FishingTrip> getRelatedFishingTrips(List<FishingActivity> relatedFishingActivities) {
        List<FishingTrip> fishingTrips = null;
        if (CollectionUtils.isNotEmpty(relatedFishingActivities)) {
            fishingTrips = new ArrayList<>();
            for (FishingActivity fishingActivity : relatedFishingActivities) {
                if(fishingActivity.getSpecifiedFishingTrip() != null) {
                    fishingTrips.add(fishingActivity.getSpecifiedFishingTrip());
                }
            }
        }
        return fishingTrips;
    }

    public static List<DelimitedPeriod> getDelimitedPeriod(List<FishingActivity> relatedFishingActivities) {
        List<DelimitedPeriod> delimitedPeriod = null;
        if (CollectionUtils.isNotEmpty(relatedFishingActivities)) {
            delimitedPeriod = new ArrayList<>();
            for (FishingActivity activity : relatedFishingActivities) {
                if (activity.getSpecifiedDelimitedPeriods() != null) {
                    delimitedPeriod.addAll(activity.getSpecifiedDelimitedPeriods());
                }
            }
        }
        return delimitedPeriod;
    }

    public static List<FLUXLocation> getFluxLocations(List<FishingActivity> relatedFishingActivities) {
        List<FLUXLocation> fluxLocations = null;
        if (CollectionUtils.isNotEmpty(relatedFishingActivities)) {
            fluxLocations = new ArrayList<>();
            for (FishingActivity activity : relatedFishingActivities) {
                if (activity.getRelatedFLUXLocations() != null) {
                    fluxLocations.addAll(activity.getRelatedFLUXLocations());
                }
            }
        }
        return fluxLocations;
    }

    public static List<CodeType> mapToIdTypeList(List<ContactParty> contactPartyList) {
        List<CodeType> codeTypes = null;

        if (!CollectionUtils.isEmpty(contactPartyList)) {

            codeTypes = Collections.newArrayList();

            for (ContactParty contactParty : contactPartyList) {
                codeTypes.addAll(ActivityFactMapper.INSTANCE.mapToCodeType(contactParty.getRoleCodes()));
            }
        }

        return codeTypes;
    }

    public static String getUUID(List<IDType> ids) {
        if (ids != null) {
            for (IDType idType : ids) {
                if (idType.getSchemeID().equalsIgnoreCase("UUID")) {
                    return idType.getValue();
                }
            }
        }
        return null;
    }

    public static List<ContactPerson> mapToContactPersonList(List<ContactParty> contactPartyList) {
        List<ContactPerson> contactPersonList = null;

        if (CollectionUtils.isNotEmpty(contactPartyList)) {

            contactPersonList = Collections.newArrayList();

            for (ContactParty contactParty : contactPartyList) {
                contactPersonList.addAll(contactParty.getSpecifiedContactPersons());
            }
        }

        return contactPersonList;
    }

    public static Date getDate(DateTimeType dateTimeType) {
        Date date = null;
        if (dateTimeType != null) {
            try {
                if (dateTimeType.getDateTime() != null) {
                    date = dateTimeType.getDateTime().toGregorianCalendar().getTime();
                } else {
                    String format = dateTimeType.getDateTimeString().getFormat();
                    String value = dateTimeType.getDateTimeString().getValue();
                    date = new SimpleDateFormat(format).parse(value);
                }
            } catch (Exception e) {
                log.debug("Error while trying to parse dateTimeType", e);
            }
        }

        return date;
    }

    public static List<AAPProduct> getAppliedProcessAAPProducts(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return java.util.Collections.emptyList();
        }
        List<AAPProduct> aapProducts = new ArrayList<>();

        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                aapProducts.addAll(aapProcess.getResultAAPProducts());
            }
        }
        return aapProducts;
    }

    /**
     * Extract List<MeasureType> from AAPProduct. List will be created from different attributes of AAPProduct based on parameter methodToChoose.
     *
     * @param appliedAAPProcesses
     * @param methodToChoose
     * @return
     */
    public static List<MeasureType> getMeasureTypeFromAAPProcess(List<AAPProcess> appliedAAPProcesses, String methodToChoose) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return java.util.Collections.emptyList();
        }
        List<MeasureType> measureTypes = new ArrayList<>();
        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                for (AAPProduct aapProduct : aapProcess.getResultAAPProducts()) {
                    switch (methodToChoose) {
                        case ActivityFactMapper.AAP_PRODUCT_PACKAGING_UNIT_QUANTITY:
                            if (aapProduct.getPackagingUnitQuantity() != null) {
                                measureTypes.add(ActivityFactMapper.INSTANCE.mapQuantityTypeToMeasureType(aapProduct.getPackagingUnitQuantity()));
                            }
                            break;
                        case ActivityFactMapper.AAP_PRODUCT_AVERAGE_WEIGHT_MEASURE:
                            if (aapProduct.getPackagingUnitAverageWeightMeasure() != null) {
                                measureTypes.add(ActivityFactMapper.INSTANCE.mapToMeasureType(aapProduct.getPackagingUnitAverageWeightMeasure()));
                            }
                            break;
                        case ActivityFactMapper.AAP_PRODUCT_WEIGHT_MEASURE:
                            if (aapProduct.getWeightMeasure() != null) {
                                measureTypes.add(ActivityFactMapper.INSTANCE.mapToMeasureType(aapProduct.getWeightMeasure()));
                            }
                            break;
                        case ActivityFactMapper.AAP_PRODUCT_UNIT_QUANTITY:
                            if (aapProduct.getUnitQuantity() != null) {
                                measureTypes.add(ActivityFactMapper.INSTANCE.mapQuantityTypeToMeasureType(aapProduct.getUnitQuantity()));
                            }
                            break;
                    }

                }
            }
        }
        return measureTypes;
    }

    public static List<CodeType> getAAPProductPackagingTypeCode(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return java.util.Collections.emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                for (AAPProduct aapProduct : aapProcess.getResultAAPProducts()) {
                    if (aapProduct.getPackagingTypeCode() != null) {
                        codeTypes.add(ActivityFactMapper.INSTANCE.mapToCodeType(aapProduct.getPackagingTypeCode()));
                    }
                }
            }
        }
        return codeTypes;
    }

    public static List<CodeType> getAppliedProcessTypeCodes(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return java.util.Collections.emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();

        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getTypeCodes())) {
                codeTypes.addAll(ActivityFactMapper.INSTANCE.mapToCodeType(aapProcess.getTypeCodes()));
            }
        }
        return codeTypes;
    }

    public static List<CodeType> getApplicableFLUXCharacteristicsTypeCode(List<FLUXCharacteristic> fluxCharacteristics) {
        if (CollectionUtils.isEmpty(fluxCharacteristics)) {
            return java.util.Collections.emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FLUXCharacteristic fluxCharacteristic : fluxCharacteristics) {
            if (fluxCharacteristic.getTypeCode() != null) {
                codeTypes.add(ActivityFactMapper.INSTANCE.mapToCodeType(fluxCharacteristic.getTypeCode()));
            }
        }
        return codeTypes;
    }

    public static List<MeasureType> getApplicableFLUXCharacteristicsValueQuantity(List<FLUXCharacteristic> fluxCharacteristics) {
        if (CollectionUtils.isEmpty(fluxCharacteristics)) {
            return java.util.Collections.emptyList();
        }
        List<MeasureType> measureTypes = new ArrayList<>();
        for (FLUXCharacteristic fluxCharacteristic : fluxCharacteristics) {
            if (fluxCharacteristic.getValueQuantity() != null) {
                measureTypes.add(ActivityFactMapper.INSTANCE.mapQuantityTypeToMeasureType(fluxCharacteristic.getValueQuantity()));
            }
        }
        return measureTypes;
    }

    public static List<CodeType> getFLUXLocationTypeCodes(List<FLUXLocation> fluxLocations) {
        if (CollectionUtils.isEmpty(fluxLocations)) {
            return java.util.Collections.emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FLUXLocation fluxLocation : fluxLocations) {
            if (fluxLocation.getTypeCode() != null) {
                codeTypes.add(ActivityFactMapper.INSTANCE.mapToCodeType(fluxLocation.getTypeCode()));
            }
        }
        return codeTypes;
    }

    public static List<CodeType> getFishingGearRoleCodes(List<FishingGear> fishingGears) {
        if (CollectionUtils.isEmpty(fishingGears)) {
            return java.util.Collections.emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FishingGear fishingGear : fishingGears) {

            if (CollectionUtils.isNotEmpty(fishingGear.getRoleCodes())) {
                codeTypes.addAll(ActivityFactMapper.INSTANCE.mapToCodeType(fishingGear.getRoleCodes()));
            }
        }
        return codeTypes;
    }

    public static List<CodeType> getVesselTransportMeansRoleCodes(List<VesselTransportMeans> vesselTransportMeanses) {
        if (CollectionUtils.isEmpty(vesselTransportMeanses)) {
            return java.util.Collections.emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMeanses) {

            if (vesselTransportMeans.getRoleCode()!=null) {
                codeTypes.add(ActivityFactMapper.INSTANCE.mapToCodeType(vesselTransportMeans.getRoleCode()));
            }
        }
        return codeTypes;
    }

  /**
     * Fetch List<CodeType> from FACatch. CodeType List will be created from FACatch based on parameter methodToChoose
     * i.e code type for FACatch or code type for specified fluxlocation
     *
     * @param faCatch
    // * @param methodToChoose
     * @return
     */
    public static List<CodeType> getCodeTypesFromFaCatch(List<FACatch> faCatch, String methodToChoose) {
        if (CollectionUtils.isEmpty(faCatch)) {
            return java.util.Collections.emptyList();
        }
        List<CodeType> codeTypes = new ArrayList<>();
        for (FACatch faCatches : faCatch) {


            switch (methodToChoose) {

                case ActivityFactMapper.CODE_TYPE_FOR_FACATCH:
                    if (faCatches.getTypeCode() != null) {
                        codeTypes.add(ActivityFactMapper.INSTANCE.mapToCodeType(faCatches.getTypeCode()));
                    }
                    break;

                case ActivityFactMapper.CODE_TYPE_FOR_FACATCH_FLUXLOCATION:

                    if (CollectionUtils.isNotEmpty(faCatches.getSpecifiedFLUXLocations())) {
                        for (FLUXLocation specifiedFluxLocation : faCatches.getSpecifiedFLUXLocations()) {
                            if (specifiedFluxLocation.getTypeCode() != null) {
                                codeTypes.add(ActivityFactMapper.INSTANCE.mapToCodeType(specifiedFluxLocation.getTypeCode()));
                            }
                        }
                    }
                    break;

            }
        }
        return codeTypes;
    }

    public static List<String> getIds(FLUXFAReportMessage fluxfaReportMessage) {
        if (fluxfaReportMessage == null) {
            return java.util.Collections.emptyList();
        }
        return getIds(fluxfaReportMessage.getFLUXReportDocument());
    }

    public static List<String> getIds(FAReportDocument faReportDocument) {
        if (faReportDocument == null) {
            return java.util.Collections.emptyList();
        }
        return getIds(faReportDocument.getRelatedFLUXReportDocument());
    }

    public static List<String> getIds(FLUXResponseDocument fluxResponseDocument) {
        if (fluxResponseDocument == null) {
            return java.util.Collections.emptyList();
        }
        List<IDType> idTypes = fluxResponseDocument.getIDS();
        List<String> ids = new ArrayList<>();
        for (IDType idType : idTypes) {
            ids.add(idType.getValue().concat("_").concat(idType.getSchemeID()));
        }
        return ids;
    }

    public static List<String> getIds(FLUXReportDocument fluxReportDocument) {
        if (fluxReportDocument == null) {
            return java.util.Collections.emptyList();
        }
        List<IDType> idTypes = fluxReportDocument.getIDS();
        List<String> ids = new ArrayList<>();
        for (IDType idType : idTypes) {
            ids.add(idType.getValue().concat("_").concat(idType.getSchemeID()));
        }
        return ids;
    }


    public static  List<FLUXLocation> getFluxLocationFromFaCatch(List<FACatch> faCatch){
        if (CollectionUtils.isEmpty(faCatch)) {
            return java.util.Collections.emptyList();
        }
        List<FLUXLocation> specifiedFaCatchSpecifiedFluxLocationList=new ArrayList<>();
        for (FACatch faCatc : faCatch){
            specifiedFaCatchSpecifiedFluxLocationList.addAll(faCatc.getSpecifiedFLUXLocations());




        }

        return  specifiedFaCatchSpecifiedFluxLocationList;
    }

}
