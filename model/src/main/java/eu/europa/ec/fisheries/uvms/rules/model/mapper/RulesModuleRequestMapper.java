package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportRequest;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;

import java.util.List;

public class RulesModuleRequestMapper {

    public static String createSetMovementReportRequest(PluginType type, RawMovementType rawMovementType) throws RulesModelMapperException {
        SetMovementReportRequest request = new SetMovementReportRequest();
        request.setMethod(RulesModuleMethod.SET_MOVEMENT_REPORT);
        request.setType(type);
        request.setRequest(rawMovementType);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createGetTicketsAndRulesByMovementsRequest(List<String> movementsGuids) throws RulesModelMarshallException {
        GetTicketsAndRulesByMovementsRequest request = new GetTicketsAndRulesByMovementsRequest();
        request.setMethod(RulesModuleMethod.GET_TICKETS_AND_RULES_BY_MOVEMENTS);
        request.getMovementGuids().addAll(movementsGuids);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

}
