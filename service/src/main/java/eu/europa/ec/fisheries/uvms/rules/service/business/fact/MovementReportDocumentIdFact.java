package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.IDType;


@Data
public class MovementReportDocumentIdFact extends AbstractFact {

    private IDType id;

    public boolean uuidValidateRegex(IDType id){
        if(id == null){
            return true;
        }

        String UUID_PATTERN = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";

        if("UUID".equals(id.getSchemeID()) && id.getValue() != null){
            return id.getValue().matches(UUID_PATTERN);
        }


        return true;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.MOVEMENT_REPORT_DOCUMENT_ID;
    }
}
