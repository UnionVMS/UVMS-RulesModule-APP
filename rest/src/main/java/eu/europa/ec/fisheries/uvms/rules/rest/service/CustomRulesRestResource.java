package eu.europa.ec.fisheries.uvms.rules.rest.service;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/customrules")
@Stateless
public class CustomRulesRestResource {

    private final static Logger LOG = LoggerFactory.getLogger(CustomRulesRestResource.class);

    @EJB
    RulesService rulesService;

    @EJB
    ValidationService validationService;

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
    public ResponseDto createCustomRule(final CustomRuleType customRule) {
        LOG.info("Create invoked in rest layer");
        try {
            return new ResponseDto(rulesService.createCustomRule(customRule), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException | RulesFaultException e) {
            LOG.error("[ Error when creating. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
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
    @Path(value = "listAll/{userName}")
    public ResponseDto getCustomRulesByUser(@PathParam(value = "userName") final String userName) {
        LOG.info("Get all custom rules invoked in rest layer");
        try {
            return new ResponseDto(validationService.getCustomRulesByUser(userName), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException ex) {
            LOG.error("[ Error when getting all custom rules. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Get a list of custom rules by query
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("listByQuery")
    public ResponseDto getCustomRulesByQuery(CustomRuleQuery query) {
        LOG.info("Get custom rules by query invoked in rest layer");
        try {
            return new ResponseDto(validationService.getCustomRulesByQuery(query), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException ex) {
            LOG.error("[ Error when getting custom rules by query. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Get a custom rule by GUID
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "{guid}")
    public ResponseDto getCustomRuleByGuid(@PathParam(value = "guid") final String guid) {
        LOG.info("Get custom rule by guid invoked in rest layer");
        try {
            return new ResponseDto(rulesService.getCustomRuleByGuid(guid), ResponseCode.OK);
        } catch (RulesFaultException | RulesModelMapperException | RulesServiceException | NullPointerException ex) {
            LOG.error("[ Error when geting custom rule by guid. ] {} ", ex.getStackTrace());
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
            return new ResponseDto(rulesService.updateCustomRule(customRuleType), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when updating. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Archive a custom rule
     *
     */
    @DELETE
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/{guid}")
    public ResponseDto deleteCustomRule(@PathParam(value = "guid") final String guid) {
        LOG.info("Delete custom rule invoked in rest layer");
        try {
            return new ResponseDto(rulesService.deleteCustomRule(guid), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when deleting custom rule. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        }
    }

}
