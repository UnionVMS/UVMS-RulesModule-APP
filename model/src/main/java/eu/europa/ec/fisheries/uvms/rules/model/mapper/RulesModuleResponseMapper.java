package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.schema.rules.module.v1.CountTicketsByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetCustomRuleResponse;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

public class RulesModuleResponseMapper {

    private static void validateResponse(TextMessage response, String correlationId) throws RulesModelMapperException, JMSException, RulesFaultException {

        if (response == null) {
            throw new RulesModelMapperException("Error when validating response in ResponseMapper: Reesponse is Null");
        }

        if (response.getJMSCorrelationID() == null) {
            throw new RulesModelMapperException("No corelationId in response (Null) . Expected was: " + correlationId);
        }

        if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
            throw new RulesModelMapperException("Wrong corelationId in response. Expected was: " + correlationId + "But actual was: " + response.getJMSCorrelationID());
        }

        try {
            RulesFault rulesFault = JAXBMarshaller.unmarshallTextMessage(response, RulesFault.class);
            throw new RulesFaultException(response.getText(), rulesFault);
        } catch (RulesModelMarshallException e) {
            // All is well
        }
    }

    public static String mapToGetTicketListByMovementsResponse(List<TicketType> movementList) throws RulesModelMarshallException {
        GetTicketsByMovementsResponse response = new GetTicketsByMovementsResponse();
        response.getTickets().addAll(movementList);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String mapToCountTicketListByMovementsResponse(long count) throws RulesModelMarshallException {
        CountTicketsByMovementsResponse response = new CountTicketsByMovementsResponse();
        response.setCount(count);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String mapToGetCustomRuleResponse(CustomRuleType rule) throws RulesModelMarshallException {
        GetCustomRuleResponse response = new GetCustomRuleResponse();
        response.setCustomRule(rule);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static GetTicketsAndRulesByMovementsResponse mapToGetTicketsAndRulesByMovementsFromResponse(TextMessage message) throws RulesModelMarshallException, RulesModelMapperException, JMSException, RulesFaultException {
        validateResponse(message, message.getJMSCorrelationID());
        GetTicketsAndRulesByMovementsResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetTicketsAndRulesByMovementsResponse.class);
        return response;
    }

    public static String getTicketsAndRulesByMovementsResponse(List<TicketAndRuleType> ticketAndRuleType) throws RulesModelMapperException {
        GetTicketsAndRulesByMovementsResponse response = new GetTicketsAndRulesByMovementsResponse();
        response.getTicketsAndRules().addAll(ticketAndRuleType);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

}
