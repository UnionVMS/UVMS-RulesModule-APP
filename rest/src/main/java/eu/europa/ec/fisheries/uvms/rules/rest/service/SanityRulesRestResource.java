package eu.europa.ec.fisheries.uvms.rules.rest.service;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sanityrules")
@Stateless
public class SanityRulesRestResource {

    private final static Logger LOG = LoggerFactory.getLogger(SanityRulesRestResource.class);

    @EJB
    ValidationService validationService;

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Get a list of all sanity rules
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "listAll")
    public ResponseDto getSanityRules() {
        LOG.info("Get all sanity rules invoked in rest layer");
        try {
            return new ResponseDto(validationService.getSanityRules(), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException ex) {
            LOG.error("[ Error when getting all sanity rules. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

}
