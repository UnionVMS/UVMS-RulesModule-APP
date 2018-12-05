package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
public class SalesVesselTransportMeansFact extends SalesAbstractFact {

    private List<IdType> ids;
    private List<TextType> names;
    private List<CodeType> typeCodes;
    private DateTimeType commissioningDateTime;
    private CodeType operationalStatusCode;
    private CodeType hullMaterialCode;
    private eu.europa.ec.fisheries.schema.sales.MeasureType draughtMeasure;
    private eu.europa.ec.fisheries.schema.sales.MeasureType speedMeasure;
    private eu.europa.ec.fisheries.schema.sales.MeasureType trawlingSpeedMeasure;
    private CodeType roleCode;
    private VesselCountryType registrationVesselCountry;
    private List<VesselPositionEventType> specifiedVesselPositionEvents;
    private List<RegistrationEventType> specifiedRegistrationEvents;
    private ConstructionEventType specifiedConstructionEvent;
    private List<VesselEngineType> attachedVesselEngines;
    private List<VesselDimensionType> specifiedVesselDimensions;
    private List<FishingGearType> onBoardFishingGears;
    private List<VesselEquipmentCharacteristicType> applicableVesselEquipmentCharacteristics;
    private List<VesselAdministrativeCharacteristicType> applicableVesselAdministrativeCharacteristics;
    private List<FLUXPictureType> illustrateFLUXPictures;
    private List<ContactPartyType> specifiedContactParties;
    private VesselCrewType specifiedVesselCrew;
    private List<VesselStorageCharacteristicType> applicableVesselStorageCharacteristics;
    private List<VesselTechnicalCharacteristicType> applicableVesselTechnicalCharacteristics;
    private List<FLAPDocumentType> grantedFLAPDocuments;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_VESSEL_TRANSPORT_MEANS;
    }


    public boolean doContactPartiesWithTheSameRoleExist() {
        List<String> earlierFoundRoles = new ArrayList<>();

        for (ContactPartyType contactParty : specifiedContactParties) {
            List<String> contactPartyRoles = getRolesOfContactParty(contactParty);
            if (containsAny(earlierFoundRoles, contactPartyRoles)) {
                return true;
            } else {
                earlierFoundRoles.addAll(contactPartyRoles);
            }
        }

        return false;
    }

    private List<String> getRolesOfContactParty(ContactPartyType contactParty) {
        List<String> roles = new ArrayList<>();

        if (contactParty == null || contactParty.getRoleCodes() == null) {
            return roles;
        }

        for (eu.europa.ec.fisheries.schema.sales.CodeType roleCode : contactParty.getRoleCodes()) {
            if (roleCode != null && roleCode.getValue() != null) {
                roles.add(roleCode.getValue());
            }
        }
        return roles;
    }


    public List<IdType> getIDS() {
        return this.ids;
    }

    public List<TextType> getNames() {
        return this.names;
    }

    public List<CodeType> getTypeCodes() {
        return this.typeCodes;
    }

    public DateTimeType getCommissioningDateTime() {
        return this.commissioningDateTime;
    }

    public CodeType getOperationalStatusCode() {
        return this.operationalStatusCode;
    }

    public CodeType getHullMaterialCode() {
        return this.hullMaterialCode;
    }

    public MeasureType getDraughtMeasure() {
        return this.draughtMeasure;
    }

    public MeasureType getSpeedMeasure() {
        return this.speedMeasure;
    }

    public MeasureType getTrawlingSpeedMeasure() {
        return this.trawlingSpeedMeasure;
    }

    public CodeType getRoleCode() {
        return this.roleCode;
    }

    public VesselCountryType getRegistrationVesselCountry() {
        return this.registrationVesselCountry;
    }

    public List<VesselPositionEventType> getSpecifiedVesselPositionEvents() {
        return this.specifiedVesselPositionEvents;
    }

    public List<RegistrationEventType> getSpecifiedRegistrationEvents() {
        return this.specifiedRegistrationEvents;
    }

    public ConstructionEventType getSpecifiedConstructionEvent() {
        return this.specifiedConstructionEvent;
    }

    public List<VesselEngineType> getAttachedVesselEngines() {
        return this.attachedVesselEngines;
    }

    public List<VesselDimensionType> getSpecifiedVesselDimensions() {
        return this.specifiedVesselDimensions;
    }

    public List<FishingGearType> getOnBoardFishingGears() {
        return this.onBoardFishingGears;
    }

    public List<VesselEquipmentCharacteristicType> getApplicableVesselEquipmentCharacteristics() {
        return this.applicableVesselEquipmentCharacteristics;
    }

    public List<VesselAdministrativeCharacteristicType> getApplicableVesselAdministrativeCharacteristics() {
        return this.applicableVesselAdministrativeCharacteristics;
    }

    public List<FLUXPictureType> getIllustrateFLUXPictures() {
        return this.illustrateFLUXPictures;
    }

    public List<ContactPartyType> getSpecifiedContactParties() {
        return this.specifiedContactParties;
    }

    public VesselCrewType getSpecifiedVesselCrew() {
        return this.specifiedVesselCrew;
    }

    public List<VesselStorageCharacteristicType> getApplicableVesselStorageCharacteristics() {
        return this.applicableVesselStorageCharacteristics;
    }

    public List<VesselTechnicalCharacteristicType> getApplicableVesselTechnicalCharacteristics() {
        return this.applicableVesselTechnicalCharacteristics;
    }

    public List<FLAPDocumentType> getGrantedFLAPDocuments() {
        return this.grantedFLAPDocuments;
    }

    public void setIDS(List<IdType> ids) {
        this.ids = ids;
    }

    public void setNames(List<TextType> names) {
        this.names = names;
    }

    public void setTypeCodes(List<CodeType> typeCodes) {
        this.typeCodes = typeCodes;
    }

    public void setCommissioningDateTime(DateTimeType commissioningDateTime) {
        this.commissioningDateTime = commissioningDateTime;
    }

    public void setOperationalStatusCode(CodeType operationalStatusCode) {
        this.operationalStatusCode = operationalStatusCode;
    }

    public void setHullMaterialCode(CodeType hullMaterialCode) {
        this.hullMaterialCode = hullMaterialCode;
    }

    public void setDraughtMeasure(MeasureType draughtMeasure) {
        this.draughtMeasure = draughtMeasure;
    }

    public void setSpeedMeasure(MeasureType speedMeasure) {
        this.speedMeasure = speedMeasure;
    }

    public void setTrawlingSpeedMeasure(MeasureType trawlingSpeedMeasure) {
        this.trawlingSpeedMeasure = trawlingSpeedMeasure;
    }

    public void setRoleCode(CodeType roleCode) {
        this.roleCode = roleCode;
    }

    public void setRegistrationVesselCountry(VesselCountryType registrationVesselCountry) {
        this.registrationVesselCountry = registrationVesselCountry;
    }

    public void setSpecifiedVesselPositionEvents(List<VesselPositionEventType> specifiedVesselPositionEvents) {
        this.specifiedVesselPositionEvents = specifiedVesselPositionEvents;
    }

    public void setSpecifiedRegistrationEvents(List<RegistrationEventType> specifiedRegistrationEvents) {
        this.specifiedRegistrationEvents = specifiedRegistrationEvents;
    }

    public void setSpecifiedConstructionEvent(ConstructionEventType specifiedConstructionEvent) {
        this.specifiedConstructionEvent = specifiedConstructionEvent;
    }

    public void setAttachedVesselEngines(List<VesselEngineType> attachedVesselEngines) {
        this.attachedVesselEngines = attachedVesselEngines;
    }

    public void setSpecifiedVesselDimensions(List<VesselDimensionType> specifiedVesselDimensions) {
        this.specifiedVesselDimensions = specifiedVesselDimensions;
    }

    public void setOnBoardFishingGears(List<FishingGearType> onBoardFishingGears) {
        this.onBoardFishingGears = onBoardFishingGears;
    }

    public void setApplicableVesselEquipmentCharacteristics(List<VesselEquipmentCharacteristicType> applicableVesselEquipmentCharacteristics) {
        this.applicableVesselEquipmentCharacteristics = applicableVesselEquipmentCharacteristics;
    }

    public void setApplicableVesselAdministrativeCharacteristics(List<VesselAdministrativeCharacteristicType> applicableVesselAdministrativeCharacteristics) {
        this.applicableVesselAdministrativeCharacteristics = applicableVesselAdministrativeCharacteristics;
    }

    public void setIllustrateFLUXPictures(List<FLUXPictureType> illustrateFLUXPictures) {
        this.illustrateFLUXPictures = illustrateFLUXPictures;
    }

    public void setSpecifiedContactParties(List<ContactPartyType> specifiedContactParties) {
        this.specifiedContactParties = specifiedContactParties;
    }

    public void setSpecifiedVesselCrew(VesselCrewType specifiedVesselCrew) {
        this.specifiedVesselCrew = specifiedVesselCrew;
    }

    public void setApplicableVesselStorageCharacteristics(List<VesselStorageCharacteristicType> applicableVesselStorageCharacteristics) {
        this.applicableVesselStorageCharacteristics = applicableVesselStorageCharacteristics;
    }

    public void setApplicableVesselTechnicalCharacteristics(List<VesselTechnicalCharacteristicType> applicableVesselTechnicalCharacteristics) {
        this.applicableVesselTechnicalCharacteristics = applicableVesselTechnicalCharacteristics;
    }

    public void setGrantedFLAPDocuments(List<FLAPDocumentType> grantedFLAPDocuments) {
        this.grantedFLAPDocuments = grantedFLAPDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesVesselTransportMeansFact)) return false;
        SalesVesselTransportMeansFact that = (SalesVesselTransportMeansFact) o;
        return Objects.equals(ids, that.ids) &&
                Objects.equals(names, that.names) &&
                Objects.equals(typeCodes, that.typeCodes) &&
                Objects.equals(commissioningDateTime, that.commissioningDateTime) &&
                Objects.equals(operationalStatusCode, that.operationalStatusCode) &&
                Objects.equals(hullMaterialCode, that.hullMaterialCode) &&
                Objects.equals(draughtMeasure, that.draughtMeasure) &&
                Objects.equals(speedMeasure, that.speedMeasure) &&
                Objects.equals(trawlingSpeedMeasure, that.trawlingSpeedMeasure) &&
                Objects.equals(roleCode, that.roleCode) &&
                Objects.equals(registrationVesselCountry, that.registrationVesselCountry) &&
                Objects.equals(specifiedVesselPositionEvents, that.specifiedVesselPositionEvents) &&
                Objects.equals(specifiedRegistrationEvents, that.specifiedRegistrationEvents) &&
                Objects.equals(specifiedConstructionEvent, that.specifiedConstructionEvent) &&
                Objects.equals(attachedVesselEngines, that.attachedVesselEngines) &&
                Objects.equals(specifiedVesselDimensions, that.specifiedVesselDimensions) &&
                Objects.equals(onBoardFishingGears, that.onBoardFishingGears) &&
                Objects.equals(applicableVesselEquipmentCharacteristics, that.applicableVesselEquipmentCharacteristics) &&
                Objects.equals(applicableVesselAdministrativeCharacteristics, that.applicableVesselAdministrativeCharacteristics) &&
                Objects.equals(illustrateFLUXPictures, that.illustrateFLUXPictures) &&
                Objects.equals(specifiedContactParties, that.specifiedContactParties) &&
                Objects.equals(specifiedVesselCrew, that.specifiedVesselCrew) &&
                Objects.equals(applicableVesselStorageCharacteristics, that.applicableVesselStorageCharacteristics) &&
                Objects.equals(applicableVesselTechnicalCharacteristics, that.applicableVesselTechnicalCharacteristics) &&
                Objects.equals(grantedFLAPDocuments, that.grantedFLAPDocuments) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, names, typeCodes, commissioningDateTime, operationalStatusCode, hullMaterialCode, draughtMeasure, speedMeasure, trawlingSpeedMeasure, roleCode, registrationVesselCountry, specifiedVesselPositionEvents, specifiedRegistrationEvents, specifiedConstructionEvent, attachedVesselEngines, specifiedVesselDimensions, onBoardFishingGears, applicableVesselEquipmentCharacteristics, applicableVesselAdministrativeCharacteristics, illustrateFLUXPictures, specifiedContactParties, specifiedVesselCrew, applicableVesselStorageCharacteristics, applicableVesselTechnicalCharacteristics, grantedFLAPDocuments, creationDateOfMessage);
    }
}
