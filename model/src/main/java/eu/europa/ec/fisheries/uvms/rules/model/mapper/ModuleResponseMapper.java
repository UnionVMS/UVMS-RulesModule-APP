package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetMovementReportResponse;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.uvms.rules.model.constant.FaultCode;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class ModuleResponseMapper {

    private static void validateResponse(TextMessage response, String correlationId) throws RulesModelMapperException, JMSException, RulesFaultException {

        if (response == null) {
            throw new RulesModelMapperException("Error when validating response in ResponseMapper: Response is Null");
        }

        if (response.getJMSCorrelationID() == null) {
            throw new RulesModelMapperException("No corelationId in response (Null) . Expected was: " + correlationId);
        }

        if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
            throw new RulesModelMapperException("Wrong corelationId in response. Expected was: " + correlationId + "But actual was: " + response.getJMSCorrelationID());
        }

        try {
            RulesFault rulesFault = JAXBMarshaller.unmarshallTextMessage(response, RulesFault.class);
            throw  new RulesFaultException(response.getText(), rulesFault);
        } catch (RulesModelMarshallException e) {
            // Do nothing because the response message is not a error message. Can continue to parse the response message to a GetCustomRuleResponse
        }

    }

    public static RulesFault createFaultMessage(FaultCode code, String message) {
        RulesFault fault = new RulesFault();
        fault.setCode(code.getCode());
        fault.setMessage(message);
        return fault;
    }

    public static MovementRefType mapSetMovementReportResponse(TextMessage response, String correlationId) throws RulesModelMapperException {
        try {
            validateResponse(response, correlationId);
            SetMovementReportResponse unmarshalledResponse = JAXBMarshaller.unmarshallTextMessage(response, SetMovementReportResponse.class);
            return unmarshalledResponse.getMovementRef();
        } catch (RulesModelMapperException | JMSException | RulesFaultException e) {
            //TODO take care of exception
            // TODO Don't catch RulesFaultException. For now, Exchange uses this method, so changes needs to be done there as well
            throw new RulesModelMapperException("FIX ME");
        }
    }

    public static String createSetMovementReportResponse(MovementRefType movementRefType) throws RulesModelMarshallException {
        SetMovementReportResponse response = new SetMovementReportResponse();
        response.setMovementRef(movementRefType);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }
}
