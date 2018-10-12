package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;

public class FaDeclarationOfRelocationOrTranshipmentFact extends AbstractRelocationOrTranshipmentFact {

    @Override
    public void setFactType() {
        this.factType = FactType.FA_DECLARATION_OF_TRANSHIPMENT_OR_RELOCATION;
    }

}
