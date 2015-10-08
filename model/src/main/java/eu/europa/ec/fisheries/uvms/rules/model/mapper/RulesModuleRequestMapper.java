package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportRequest;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;

public class RulesModuleRequestMapper {

	public static String createSetMovementReportRequest(MovementBaseType movementBaseType) throws RulesModelMapperException {
		SetMovementReportRequest request = new SetMovementReportRequest();
		request.setMethod(RulesModuleMethod.SET_MOVEMENT_REPORT);
		request.setRequest(movementBaseType);
		return JAXBMarshaller.marshallJaxBObjectToString(request);
	}
}
