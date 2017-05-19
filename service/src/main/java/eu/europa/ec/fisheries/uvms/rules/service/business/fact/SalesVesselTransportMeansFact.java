package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
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
}
