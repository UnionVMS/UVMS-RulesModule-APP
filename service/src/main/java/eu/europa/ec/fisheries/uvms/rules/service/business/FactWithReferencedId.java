package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;

public interface FactWithReferencedId {

    IdType getReferencedID();

    void setReferencedID(IdType referencedID);

}
