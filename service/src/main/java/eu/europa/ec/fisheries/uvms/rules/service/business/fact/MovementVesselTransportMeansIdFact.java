package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.IDType;

@Data
public class MovementVesselTransportMeansIdFact extends AbstractFact {

    private IDType id;
    private DateTime creationDateTime;
    
    @Override
    public void setFactType() {
        this.factType = FactType.MOVEMENT_VESSEL_TRANSPORT_MEANS_ID;
    }
}
