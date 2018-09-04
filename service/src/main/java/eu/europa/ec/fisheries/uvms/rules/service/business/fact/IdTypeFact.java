package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;

@Data
public class IdTypeFact extends AbstractFact {

    private IdType id;

    public IdTypeFact() {
        super();
    }

    public IdTypeFact(IdType id) {
        this.id = id;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.SIMPLE_ID_TYPE_FACT;
    }
}
