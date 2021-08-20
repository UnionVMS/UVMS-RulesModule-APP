/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.
This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
*/

package eu.europa.ec.fisheries.uvms.rules.service.mapper.fact;

import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.commons.date.XMLDateUtils;
import eu.europa.ec.fisheries.uvms.rules.service.bean.movement.MovementVesselMappingContext;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MovementReportDocOwnerFluxPartyIdFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MovementReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MovementReportDocumentIdFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MovementSpecifiedVesselPositionEventFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MovementVesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MovementVesselTransportMeansIdFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.fluxvesselpositionmessage._4.FLUXVesselPositionMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselPositionEventType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.IDType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.COURSE_VALUE_MEASURE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.CREATION_DATE_TIME;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.FLUX_REPORT_DOCUMENT;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.LATITUDE_MEASURE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.LONGITUDE_MEASURE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.MOVEMENT_REPORT_DOCUMENT;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.OBTAINED_OCCURRENCE_DATE_TIME;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.OWNER_FLUX_PARTY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.PURPOSE_CODE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.REGISTRATION_VESSEL_COUNTRY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.REGISTRATION_VESSEL_COUNTRY_ID;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_VESSELPOSITION_EVENT;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_VESSEL_GEOGRAPHICAL_COORDINATE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPEED_VALUE_MEASURE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.TYPE_CODE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.VESSEL_TRANSPORT_MEANS;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.VESSEL_TRANSPORT_MEANS_ID;

@Slf4j
public class MovementReportDocumentFactMapper {

    public static final String ID = "id";
    private MovementVesselMappingContext movementVesselMappingContext;
    private XPathStringWrapper xPathUtil;
    private List<String> existingIds;


    public MovementReportDocumentFactMapper() {
        xPathUtil = new XPathStringWrapper();
    }
    public MovementReportDocumentFactMapper(XPathStringWrapper strUtil1) {
        this.xPathUtil = strUtil1;
    }


    public void setMovementVesselMappingContext (MovementVesselMappingContext movementVesselMappingContext){
        this.movementVesselMappingContext = movementVesselMappingContext;
    }

    public MovementReportDocumentFact generateFactForMovementReportDocument (FLUXVesselPositionMessage vesselPositionMessage){

        if (vesselPositionMessage == null) {
            xPathUtil.clear();
            return null;
        }

        MovementReportDocumentFact fact = new MovementReportDocumentFact();
        String partialXpath = xPathUtil.append(MOVEMENT_REPORT_DOCUMENT).getValue();

        DateTimeType creationDateTime = vesselPositionMessage.getFLUXReportDocument().getCreationDateTime();
        fact.setCreationDateTimeString(dateTimeAsString(creationDateTime));
        fact.setCreationDateTime(getDate(creationDateTime));
        un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType type = new un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType();
        type.setDateTime(vesselPositionMessage.getFLUXReportDocument().getCreationDateTime().getDateTime());
        fact.setCreationDateTimeType(type);
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, XPathConstants.CREATION_DATE_TIME).storeInRepo(fact, CREATION_DATE_TIME);
        fact.setIds(vesselPositionMessage.getFLUXReportDocument().getIDS());
        fact.setExistingIds(existingIds);
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, XPathConstants.ID).storeInRepo(fact, "id");
        CodeType purposeCode = vesselPositionMessage.getFLUXReportDocument().getPurposeCode();
        fact.setPurposeCode(purposeCode);
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, PURPOSE_CODE).storeInRepo(fact, PURPOSE_CODE);
        fact.setOwnerFLUXParty(vesselPositionMessage.getFLUXReportDocument().getOwnerFLUXParty());
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, OWNER_FLUX_PARTY).storeInRepo(fact, OWNER_FLUX_PARTY);

        Optional.ofNullable(vesselPositionMessage.getVesselTransportMeans()).ifPresent(vtm -> {
            fact.setRegistrationVesselCountry(vesselPositionMessage.getVesselTransportMeans().getRegistrationVesselCountry());
            xPathUtil.appendWithoutWrapping(partialXpath).append(VESSEL_TRANSPORT_MEANS, REGISTRATION_VESSEL_COUNTRY).storeInRepo(fact, REGISTRATION_VESSEL_COUNTRY);

            Optional.ofNullable(vtm.getRegistrationVesselCountry()).ifPresent(rvc -> {
                fact.setRegistrationVesselCountryIdType(rvc.getID());
                xPathUtil.appendWithoutWrapping(partialXpath).append(REGISTRATION_VESSEL_COUNTRY, XPathConstants.ID).storeInRepo(fact, "registrationVesselCountryId");
            });
        });
        fact.setDateTime(new DateTime(getDate(creationDateTime)));

        return fact;
    }

    public List<MovementReportDocumentIdFact> generateFactForMovementReportDocumentId (FLUXVesselPositionMessage vesselPositionMessage){

        List<MovementReportDocumentIdFact> factList = new ArrayList<>();

        if (vesselPositionMessage == null || vesselPositionMessage.getFLUXReportDocument() == null || vesselPositionMessage.getFLUXReportDocument().getIDS() == null || vesselPositionMessage.getFLUXReportDocument().getIDS().isEmpty()) {
            xPathUtil.clear();
            return factList;
        }

        String partialXpath = xPathUtil.append(MOVEMENT_REPORT_DOCUMENT).getValue();

        List<IDType> ids = vesselPositionMessage.getFLUXReportDocument().getIDS();
        int index = 1;
        for (IDType idType : ids) {
            MovementReportDocumentIdFact fact = new MovementReportDocumentIdFact();
            fact.setId(idType);
            xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT).appendWithIndex(XPathConstants.ID, index).storeInRepo(fact, "id");
            factList.add(fact);
            index++;
        }
        return factList;
    }

    public List<MovementReportDocOwnerFluxPartyIdFact> generateFactForMovementReportDocOwnerFluxPartyId (FLUXVesselPositionMessage vesselPositionMessage){
        List<MovementReportDocOwnerFluxPartyIdFact> factList = new ArrayList<>();

        if (vesselPositionMessage == null || vesselPositionMessage.getFLUXReportDocument() == null || vesselPositionMessage.getFLUXReportDocument().getOwnerFLUXParty() == null || vesselPositionMessage.getFLUXReportDocument().getOwnerFLUXParty().getIDS().isEmpty()) {
            xPathUtil.clear();
            return factList;
        }

        String partialXpath = xPathUtil.append(MOVEMENT_REPORT_DOCUMENT).append(FLUX_REPORT_DOCUMENT).append(OWNER_FLUX_PARTY).getValue();
        List<IDType> ids = vesselPositionMessage.getFLUXReportDocument().getOwnerFLUXParty().getIDS();
        Date creationDate = getDate(vesselPositionMessage.getFLUXReportDocument().getCreationDateTime());
        int index = 1;
        for (IDType idType : ids) {
            MovementReportDocOwnerFluxPartyIdFact fact = new MovementReportDocOwnerFluxPartyIdFact();
            fact.setId(idType);
            fact.setCreationDateTime(new DateTime(creationDate));
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(XPathConstants.ID, index).storeInRepo(fact, "ownerFluxPartyId");
            factList.add(fact);
            index++;
        }
        return factList;
    }

    public List<MovementVesselTransportMeansIdFact> generateFactForMovementVesselTransportMeansId (FLUXVesselPositionMessage vesselPositionMessage){
        List<MovementVesselTransportMeansIdFact> factList = new ArrayList<>();


        if (vesselPositionMessage == null || vesselPositionMessage.getVesselTransportMeans() == null || vesselPositionMessage.getVesselTransportMeans().getIDS() == null || vesselPositionMessage.getVesselTransportMeans().getIDS().isEmpty()) {
            xPathUtil.clear();
            return factList;
        }

        String partialXpath = xPathUtil.append(MOVEMENT_REPORT_DOCUMENT).append(VESSEL_TRANSPORT_MEANS).getValue();
        List<IDType> ids = vesselPositionMessage.getVesselTransportMeans().getIDS();
        Date creationDate = getDate(vesselPositionMessage.getFLUXReportDocument().getCreationDateTime());
        int index = 1;
        for (IDType idType : ids) {
            MovementVesselTransportMeansIdFact fact = new MovementVesselTransportMeansIdFact();
            fact.setId(idType);
            fact.setCreationDateTime(new DateTime(creationDate));
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(XPathConstants.ID, index).storeInRepo(fact, VESSEL_TRANSPORT_MEANS_ID);
            factList.add(fact);
            index++;
        }
        return factList;
    }

    public MovementVesselTransportMeansFact generateFactForMovementVesselTransportMeans (FLUXVesselPositionMessage vesselPositionMessage){
        if (vesselPositionMessage == null || vesselPositionMessage.getVesselTransportMeans() == null || vesselPositionMessage.getVesselTransportMeans().getIDS() == null) {
            xPathUtil.clear();
            return null;
        }
        MovementVesselTransportMeansFact fact = new MovementVesselTransportMeansFact();
        Date creationDate = getDate(vesselPositionMessage.getFLUXReportDocument().getCreationDateTime());
        fact.setCreationDateTime(new DateTime(creationDate));
        String partialXpath = xPathUtil.append(MOVEMENT_REPORT_DOCUMENT).append(VESSEL_TRANSPORT_MEANS).getValue();
        fact.setIds(vesselPositionMessage.getVesselTransportMeans().getIDS());
        xPathUtil.appendWithoutWrapping(partialXpath).storeInRepo(fact, VESSEL_TRANSPORT_MEANS);
        fact.setRegistrationVesselCountry(vesselPositionMessage.getVesselTransportMeans().getRegistrationVesselCountry());
        xPathUtil.appendWithoutWrapping(partialXpath).append(REGISTRATION_VESSEL_COUNTRY).storeInRepo(fact, REGISTRATION_VESSEL_COUNTRY);
        fact.setSpecifiedVesselPositionEvents(vesselPositionMessage.getVesselTransportMeans().getSpecifiedVesselPositionEvents());
        xPathUtil.appendWithoutWrapping(partialXpath).append(SPECIFIED_VESSELPOSITION_EVENT).storeInRepo(fact, SPECIFIED_VESSELPOSITION_EVENT);

        RawMovementType rawMovement = movementVesselMappingContext.getRawMovement(vesselPositionMessage.getVesselTransportMeans());
        if (rawMovement != null) {
            fact.setAsset(movementVesselMappingContext.getAsset(rawMovement));
        }
        xPathUtil.appendWithoutWrapping(partialXpath).append(REGISTRATION_VESSEL_COUNTRY, ID).storeInRepo(fact, REGISTRATION_VESSEL_COUNTRY_ID);
        return fact;
    }

    public List<MovementSpecifiedVesselPositionEventFact> generateFactForSpecifiedVesselPositionEvent (FLUXVesselPositionMessage vesselPositionMessage){

        String partialXpath = xPathUtil.append(MOVEMENT_REPORT_DOCUMENT).append(VESSEL_TRANSPORT_MEANS).getValue();
        List<MovementSpecifiedVesselPositionEventFact> factList = new ArrayList<>();

        if (vesselPositionMessage == null || vesselPositionMessage.getVesselTransportMeans() == null || vesselPositionMessage.getVesselTransportMeans().getSpecifiedVesselPositionEvents().isEmpty()) {
            xPathUtil.clear();
            return factList;
        }

        List<VesselPositionEventType> specifiedVesselPositionEvents = vesselPositionMessage.getVesselTransportMeans().getSpecifiedVesselPositionEvents();
        Date creationDate = getDate(vesselPositionMessage.getFLUXReportDocument().getCreationDateTime());

        int index = 1;
        for (VesselPositionEventType vesselPositionEventType : specifiedVesselPositionEvents) {
            MovementSpecifiedVesselPositionEventFact fact = new MovementSpecifiedVesselPositionEventFact();
            fact.setCreationDateTime(new DateTime(creationDate));
            fact.setRegistrationVesselCountry(vesselPositionMessage.getVesselTransportMeans().getRegistrationVesselCountry());
            fact.setTypeCode(vesselPositionEventType.getTypeCode());
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_VESSELPOSITION_EVENT, index).append(TYPE_CODE).storeInRepo(fact, "typeCode");

            if (vesselPositionEventType.getSpeedValueMeasure() != null) {
                fact.setSpeedValue(vesselPositionEventType.getSpeedValueMeasure().getValue());
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_VESSELPOSITION_EVENT, index).append(SPEED_VALUE_MEASURE).storeInRepo(fact, "speedValue");
            } else {
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_VESSELPOSITION_EVENT, index).storeInRepo(fact, "speedValue");
            }


            if (vesselPositionEventType.getCourseValueMeasure() != null) {
                fact.setCourseValue(vesselPositionEventType.getCourseValueMeasure().getValue());
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_VESSELPOSITION_EVENT, index).append(COURSE_VALUE_MEASURE).storeInRepo(fact, "courseValue");
            } else {
                fact.setCourseValue(null);
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_VESSELPOSITION_EVENT, index).storeInRepo(fact, "courseValue");
            }

            fact.setCreationDateTimeString(dateTimeAsString(vesselPositionEventType.getObtainedOccurrenceDateTime()));
            un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType obtainedType = new un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType();
            obtainedType.setDateTime(vesselPositionEventType.getObtainedOccurrenceDateTime() == null ? null:vesselPositionEventType.getObtainedOccurrenceDateTime().getDateTime());
            fact.setObtainedDateTimeType(obtainedType);
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_VESSELPOSITION_EVENT, index).append(OBTAINED_OCCURRENCE_DATE_TIME).storeInRepo(fact, "creationDateTimeString");

            fact.setLatitudeMeasure(vesselPositionEventType.getSpecifiedVesselGeographicalCoordinate().getLatitudeMeasure().getValue());
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_VESSELPOSITION_EVENT, index).append(SPECIFIED_VESSEL_GEOGRAPHICAL_COORDINATE).append(LATITUDE_MEASURE).storeInRepo(fact, "latitudeMeasure");

            fact.setLongitudeMeasure(vesselPositionEventType.getSpecifiedVesselGeographicalCoordinate().getLongitudeMeasure().getValue());
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_VESSELPOSITION_EVENT, index).append(SPECIFIED_VESSEL_GEOGRAPHICAL_COORDINATE).append(LONGITUDE_MEASURE).storeInRepo(fact, "longitudeMeasure");

            factList.add(fact);
            index++;
        }

        return factList;
    }


    public static MovementReportDocumentFact mapToMovementReportDocumentFact (FLUXVesselPositionMessage vesselPositionMessage){

        if (vesselPositionMessage == null || vesselPositionMessage.getFLUXReportDocument() == null) {
            throw new IllegalArgumentException("FLUXVesselPositionMessage and FLUXReportDocument cannot be null");
        }

        MovementReportDocumentFact fact = new MovementReportDocumentFact();
        DateTimeType creationDateTime = vesselPositionMessage.getFLUXReportDocument().getCreationDateTime();
        fact.setCreationDateTime(getDate(creationDateTime));
        fact.setCreationDateTimeString(dateTimeAsString(creationDateTime));
        return fact;
    }

    private static String dateTimeAsString (DateTimeType dateTimeType){
        String dateAsString = null;

        if (dateTimeType != null) {
            try {
                if (dateTimeType.getDateTime() != null) {
                    dateAsString = dateTimeType.getDateTime().toString();
                }
            } catch (Exception e) {
                log.debug("Error while trying to parse dateTimeType", e);
            }
        }
        return dateAsString;
    }

    private static Date getDate (DateTimeType dateTimeType){
        Date date = null;
        if (dateTimeType != null) {
            try {
                if (dateTimeType.getDateTime() != null) {
                    date = dateTimeType.getDateTime().toGregorianCalendar().getTime();
                }
            } catch (Exception e) {
                log.debug("Error while trying to parse dateTimeType", e);
            }
        }

        return date;
    }

    public void setExistingIds (List < String > existingIds) {
        this.existingIds = existingIds;
    }



}