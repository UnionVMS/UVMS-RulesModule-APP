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
    
    public boolean hasValidUvi(IDType id) {
        int[] multipliers = {7,6,5,4,3,2};
        String value = id.getValue();
        int sum = 0;
        for(int i = 0; i < 6; i++) {
            sum += Integer.parseInt(value.substring(i, i + 1)) * multipliers[i];
        }
        return String.valueOf(value.charAt(6)).equals(String.valueOf(sum % 10));
    }
    
    
    @Override
    public void setFactType() {
        this.factType = FactType.MOVEMENT_VESSEL_TRANSPORT_MEANS_ID;
    }
}
