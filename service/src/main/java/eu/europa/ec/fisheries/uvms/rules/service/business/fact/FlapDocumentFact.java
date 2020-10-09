package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FlapDocumentFact extends AbstractFact {

    private IdType id;

    @Override
    public void setFactType() {
        this.factType = FactType.FA_FLAP_DOCUMENT;
    }
}