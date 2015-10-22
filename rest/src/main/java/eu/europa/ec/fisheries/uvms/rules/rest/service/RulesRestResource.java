package eu.europa.ec.fisheries.uvms.rules.rest.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Path("/customrules")
@Stateless
public class RulesRestResource {

    final static Logger LOG = LoggerFactory.getLogger(RulesRestResource.class);

    @EJB
    RulesService serviceLayer;

    /**
     *
     * @responseMessage 200 A custom rule successfully created
     * @responseMessage 500 No custom rule was created
     *
     * @summary Create a custom rule
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    public ResponseDto create(final CustomRuleType customRule) {
        LOG.info("Create invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.createCustomRule(customRule), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException ex) {
            LOG.error("[ Error when creating. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Get a list of all custom rules
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("list")
    public ResponseDto getCustomRuleList() {
        LOG.info("Get list invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.getCustomRuleList(), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException ex) {
            LOG.error("[ Error when geting list. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary [Description]
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "{id}")
    public ResponseDto getById(@PathParam(value = "id") final Long id) {
        LOG.info("Get by id invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.getById(id), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException ex) {
            LOG.error("[ Error when geting by id. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Update a custom rule
     *
     */
    @PUT
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    public ResponseDto update(final CustomRuleType customRuleType) {
        LOG.info("Update custom rule invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.updateCustomRule(customRuleType), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException ex) {
            LOG.error("[ Error when updating. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

}
