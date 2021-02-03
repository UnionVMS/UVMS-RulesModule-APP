package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.IDType;

@Data
public class MovementReportDocOwnerFluxPartyIdFact extends AbstractFact {

    private IDType id;
    private DateTime creationDateTime;
    
    public boolean hasValidSchemeID(IDType id, String schemeID) {
        return schemeID.equals(id.getSchemeID());
    }
    
    @Override
    public void setFactType() {
        this.factType = FactType.MOVEMENT_REPORT_DOC_OWNER_FLUX_PARTY_ID;
    }
}
