package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportResponse;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;

public class ModuleResponseMapper {

    private static void validateResponse(TextMessage response, String correlationId) throws RulesModelMapperException, JMSException {

        if (response == null) {
            throw new RulesModelMapperException("Error when validating response in ResponseMapper: Response is Null");
        }

        if (response.getJMSCorrelationID() == null) {
            throw new RulesModelMapperException("No corelationId in response (Null) . Expected was: " + correlationId);
        }

        if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
            throw new RulesModelMapperException("Wrong corelationId in response. Expected was: " + correlationId + "But actual was: " + response.getJMSCorrelationID());
        }

        //TODO unmarshall to RulesFault
    }

    public static MovementRefType mapSetMovementReportResponse(TextMessage response, String correlationId) throws RulesModelMapperException {
        try {
            validateResponse(response, correlationId);
            SetMovementReportResponse unmarshalledResponse = JAXBMarshaller.unmarshallTextMessage(response, SetMovementReportResponse.class);
            return unmarshalledResponse.getMovementRef();
        } catch (RulesModelMapperException | JMSException e) {
            //TODO take care of exception
            throw new RulesModelMapperException("FIX ME");
        }
    }

    public static String createSetMovementReportResponse(MovementRefType movementRefType) throws RulesModelMarshallException {
        SetMovementReportResponse response = new SetMovementReportResponse();
        response.setMovementRef(movementRefType);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }
}
