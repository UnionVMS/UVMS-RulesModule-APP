package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesVesselTransportMeansFact extends AbstractFact {

    private List<IDType> ids;
    private List<TextType> names;
    private List<eu.europa.ec.fisheries.schema.sales.CodeType> typeCodes;
    private DateTimeType commissioningDateTime;
    private eu.europa.ec.fisheries.schema.sales.CodeType operationalStatusCode;
    private eu.europa.ec.fisheries.schema.sales.CodeType hullMaterialCode;
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

    public List<IDType> getIDS() {
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

    public void setIDS(List<IDType> ids) {
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesVesselTransportMeansFact)) return false;
        final SalesVesselTransportMeansFact other = (SalesVesselTransportMeansFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ids = this.getIDS();
        final Object other$ids = other.getIDS();
        if (this$ids == null ? other$ids != null : !this$ids.equals(other$ids)) return false;
        final Object this$names = this.getNames();
        final Object other$names = other.getNames();
        if (this$names == null ? other$names != null : !this$names.equals(other$names)) return false;
        final Object this$typeCodes = this.getTypeCodes();
        final Object other$typeCodes = other.getTypeCodes();
        if (this$typeCodes == null ? other$typeCodes != null : !this$typeCodes.equals(other$typeCodes)) return false;
        final Object this$commissioningDateTime = this.getCommissioningDateTime();
        final Object other$commissioningDateTime = other.getCommissioningDateTime();
        if (this$commissioningDateTime == null ? other$commissioningDateTime != null : !this$commissioningDateTime.equals(other$commissioningDateTime))
            return false;
        final Object this$operationalStatusCode = this.getOperationalStatusCode();
        final Object other$operationalStatusCode = other.getOperationalStatusCode();
        if (this$operationalStatusCode == null ? other$operationalStatusCode != null : !this$operationalStatusCode.equals(other$operationalStatusCode))
            return false;
        final Object this$hullMaterialCode = this.getHullMaterialCode();
        final Object other$hullMaterialCode = other.getHullMaterialCode();
        if (this$hullMaterialCode == null ? other$hullMaterialCode != null : !this$hullMaterialCode.equals(other$hullMaterialCode))
            return false;
        final Object this$draughtMeasure = this.getDraughtMeasure();
        final Object other$draughtMeasure = other.getDraughtMeasure();
        if (this$draughtMeasure == null ? other$draughtMeasure != null : !this$draughtMeasure.equals(other$draughtMeasure))
            return false;
        final Object this$speedMeasure = this.getSpeedMeasure();
        final Object other$speedMeasure = other.getSpeedMeasure();
        if (this$speedMeasure == null ? other$speedMeasure != null : !this$speedMeasure.equals(other$speedMeasure))
            return false;
        final Object this$trawlingSpeedMeasure = this.getTrawlingSpeedMeasure();
        final Object other$trawlingSpeedMeasure = other.getTrawlingSpeedMeasure();
        if (this$trawlingSpeedMeasure == null ? other$trawlingSpeedMeasure != null : !this$trawlingSpeedMeasure.equals(other$trawlingSpeedMeasure))
            return false;
        final Object this$roleCode = this.getRoleCode();
        final Object other$roleCode = other.getRoleCode();
        if (this$roleCode == null ? other$roleCode != null : !this$roleCode.equals(other$roleCode)) return false;
        final Object this$registrationVesselCountry = this.getRegistrationVesselCountry();
        final Object other$registrationVesselCountry = other.getRegistrationVesselCountry();
        if (this$registrationVesselCountry == null ? other$registrationVesselCountry != null : !this$registrationVesselCountry.equals(other$registrationVesselCountry))
            return false;
        final Object this$specifiedVesselPositionEvents = this.getSpecifiedVesselPositionEvents();
        final Object other$specifiedVesselPositionEvents = other.getSpecifiedVesselPositionEvents();
        if (this$specifiedVesselPositionEvents == null ? other$specifiedVesselPositionEvents != null : !this$specifiedVesselPositionEvents.equals(other$specifiedVesselPositionEvents))
            return false;
        final Object this$specifiedRegistrationEvents = this.getSpecifiedRegistrationEvents();
        final Object other$specifiedRegistrationEvents = other.getSpecifiedRegistrationEvents();
        if (this$specifiedRegistrationEvents == null ? other$specifiedRegistrationEvents != null : !this$specifiedRegistrationEvents.equals(other$specifiedRegistrationEvents))
            return false;
        final Object this$specifiedConstructionEvent = this.getSpecifiedConstructionEvent();
        final Object other$specifiedConstructionEvent = other.getSpecifiedConstructionEvent();
        if (this$specifiedConstructionEvent == null ? other$specifiedConstructionEvent != null : !this$specifiedConstructionEvent.equals(other$specifiedConstructionEvent))
            return false;
        final Object this$attachedVesselEngines = this.getAttachedVesselEngines();
        final Object other$attachedVesselEngines = other.getAttachedVesselEngines();
        if (this$attachedVesselEngines == null ? other$attachedVesselEngines != null : !this$attachedVesselEngines.equals(other$attachedVesselEngines))
            return false;
        final Object this$specifiedVesselDimensions = this.getSpecifiedVesselDimensions();
        final Object other$specifiedVesselDimensions = other.getSpecifiedVesselDimensions();
        if (this$specifiedVesselDimensions == null ? other$specifiedVesselDimensions != null : !this$specifiedVesselDimensions.equals(other$specifiedVesselDimensions))
            return false;
        final Object this$onBoardFishingGears = this.getOnBoardFishingGears();
        final Object other$onBoardFishingGears = other.getOnBoardFishingGears();
        if (this$onBoardFishingGears == null ? other$onBoardFishingGears != null : !this$onBoardFishingGears.equals(other$onBoardFishingGears))
            return false;
        final Object this$applicableVesselEquipmentCharacteristics = this.getApplicableVesselEquipmentCharacteristics();
        final Object other$applicableVesselEquipmentCharacteristics = other.getApplicableVesselEquipmentCharacteristics();
        if (this$applicableVesselEquipmentCharacteristics == null ? other$applicableVesselEquipmentCharacteristics != null : !this$applicableVesselEquipmentCharacteristics.equals(other$applicableVesselEquipmentCharacteristics))
            return false;
        final Object this$applicableVesselAdministrativeCharacteristics = this.getApplicableVesselAdministrativeCharacteristics();
        final Object other$applicableVesselAdministrativeCharacteristics = other.getApplicableVesselAdministrativeCharacteristics();
        if (this$applicableVesselAdministrativeCharacteristics == null ? other$applicableVesselAdministrativeCharacteristics != null : !this$applicableVesselAdministrativeCharacteristics.equals(other$applicableVesselAdministrativeCharacteristics))
            return false;
        final Object this$illustrateFLUXPictures = this.getIllustrateFLUXPictures();
        final Object other$illustrateFLUXPictures = other.getIllustrateFLUXPictures();
        if (this$illustrateFLUXPictures == null ? other$illustrateFLUXPictures != null : !this$illustrateFLUXPictures.equals(other$illustrateFLUXPictures))
            return false;
        final Object this$specifiedContactParties = this.getSpecifiedContactParties();
        final Object other$specifiedContactParties = other.getSpecifiedContactParties();
        if (this$specifiedContactParties == null ? other$specifiedContactParties != null : !this$specifiedContactParties.equals(other$specifiedContactParties))
            return false;
        final Object this$specifiedVesselCrew = this.getSpecifiedVesselCrew();
        final Object other$specifiedVesselCrew = other.getSpecifiedVesselCrew();
        if (this$specifiedVesselCrew == null ? other$specifiedVesselCrew != null : !this$specifiedVesselCrew.equals(other$specifiedVesselCrew))
            return false;
        final Object this$applicableVesselStorageCharacteristics = this.getApplicableVesselStorageCharacteristics();
        final Object other$applicableVesselStorageCharacteristics = other.getApplicableVesselStorageCharacteristics();
        if (this$applicableVesselStorageCharacteristics == null ? other$applicableVesselStorageCharacteristics != null : !this$applicableVesselStorageCharacteristics.equals(other$applicableVesselStorageCharacteristics))
            return false;
        final Object this$applicableVesselTechnicalCharacteristics = this.getApplicableVesselTechnicalCharacteristics();
        final Object other$applicableVesselTechnicalCharacteristics = other.getApplicableVesselTechnicalCharacteristics();
        if (this$applicableVesselTechnicalCharacteristics == null ? other$applicableVesselTechnicalCharacteristics != null : !this$applicableVesselTechnicalCharacteristics.equals(other$applicableVesselTechnicalCharacteristics))
            return false;
        final Object this$grantedFLAPDocuments = this.getGrantedFLAPDocuments();
        final Object other$grantedFLAPDocuments = other.getGrantedFLAPDocuments();
        if (this$grantedFLAPDocuments == null ? other$grantedFLAPDocuments != null : !this$grantedFLAPDocuments.equals(other$grantedFLAPDocuments))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ids = this.getIDS();
        result = result * PRIME + ($ids == null ? 43 : $ids.hashCode());
        final Object $names = this.getNames();
        result = result * PRIME + ($names == null ? 43 : $names.hashCode());
        final Object $typeCodes = this.getTypeCodes();
        result = result * PRIME + ($typeCodes == null ? 43 : $typeCodes.hashCode());
        final Object $commissioningDateTime = this.getCommissioningDateTime();
        result = result * PRIME + ($commissioningDateTime == null ? 43 : $commissioningDateTime.hashCode());
        final Object $operationalStatusCode = this.getOperationalStatusCode();
        result = result * PRIME + ($operationalStatusCode == null ? 43 : $operationalStatusCode.hashCode());
        final Object $hullMaterialCode = this.getHullMaterialCode();
        result = result * PRIME + ($hullMaterialCode == null ? 43 : $hullMaterialCode.hashCode());
        final Object $draughtMeasure = this.getDraughtMeasure();
        result = result * PRIME + ($draughtMeasure == null ? 43 : $draughtMeasure.hashCode());
        final Object $speedMeasure = this.getSpeedMeasure();
        result = result * PRIME + ($speedMeasure == null ? 43 : $speedMeasure.hashCode());
        final Object $trawlingSpeedMeasure = this.getTrawlingSpeedMeasure();
        result = result * PRIME + ($trawlingSpeedMeasure == null ? 43 : $trawlingSpeedMeasure.hashCode());
        final Object $roleCode = this.getRoleCode();
        result = result * PRIME + ($roleCode == null ? 43 : $roleCode.hashCode());
        final Object $registrationVesselCountry = this.getRegistrationVesselCountry();
        result = result * PRIME + ($registrationVesselCountry == null ? 43 : $registrationVesselCountry.hashCode());
        final Object $specifiedVesselPositionEvents = this.getSpecifiedVesselPositionEvents();
        result = result * PRIME + ($specifiedVesselPositionEvents == null ? 43 : $specifiedVesselPositionEvents.hashCode());
        final Object $specifiedRegistrationEvents = this.getSpecifiedRegistrationEvents();
        result = result * PRIME + ($specifiedRegistrationEvents == null ? 43 : $specifiedRegistrationEvents.hashCode());
        final Object $specifiedConstructionEvent = this.getSpecifiedConstructionEvent();
        result = result * PRIME + ($specifiedConstructionEvent == null ? 43 : $specifiedConstructionEvent.hashCode());
        final Object $attachedVesselEngines = this.getAttachedVesselEngines();
        result = result * PRIME + ($attachedVesselEngines == null ? 43 : $attachedVesselEngines.hashCode());
        final Object $specifiedVesselDimensions = this.getSpecifiedVesselDimensions();
        result = result * PRIME + ($specifiedVesselDimensions == null ? 43 : $specifiedVesselDimensions.hashCode());
        final Object $onBoardFishingGears = this.getOnBoardFishingGears();
        result = result * PRIME + ($onBoardFishingGears == null ? 43 : $onBoardFishingGears.hashCode());
        final Object $applicableVesselEquipmentCharacteristics = this.getApplicableVesselEquipmentCharacteristics();
        result = result * PRIME + ($applicableVesselEquipmentCharacteristics == null ? 43 : $applicableVesselEquipmentCharacteristics.hashCode());
        final Object $applicableVesselAdministrativeCharacteristics = this.getApplicableVesselAdministrativeCharacteristics();
        result = result * PRIME + ($applicableVesselAdministrativeCharacteristics == null ? 43 : $applicableVesselAdministrativeCharacteristics.hashCode());
        final Object $illustrateFLUXPictures = this.getIllustrateFLUXPictures();
        result = result * PRIME + ($illustrateFLUXPictures == null ? 43 : $illustrateFLUXPictures.hashCode());
        final Object $specifiedContactParties = this.getSpecifiedContactParties();
        result = result * PRIME + ($specifiedContactParties == null ? 43 : $specifiedContactParties.hashCode());
        final Object $specifiedVesselCrew = this.getSpecifiedVesselCrew();
        result = result * PRIME + ($specifiedVesselCrew == null ? 43 : $specifiedVesselCrew.hashCode());
        final Object $applicableVesselStorageCharacteristics = this.getApplicableVesselStorageCharacteristics();
        result = result * PRIME + ($applicableVesselStorageCharacteristics == null ? 43 : $applicableVesselStorageCharacteristics.hashCode());
        final Object $applicableVesselTechnicalCharacteristics = this.getApplicableVesselTechnicalCharacteristics();
        result = result * PRIME + ($applicableVesselTechnicalCharacteristics == null ? 43 : $applicableVesselTechnicalCharacteristics.hashCode());
        final Object $grantedFLAPDocuments = this.getGrantedFLAPDocuments();
        result = result * PRIME + ($grantedFLAPDocuments == null ? 43 : $grantedFLAPDocuments.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesVesselTransportMeansFact;
    }
}
