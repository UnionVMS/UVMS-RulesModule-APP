package eu.europa.ec.fisheries.uvms.rules.rest.service;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CriteriaType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubCriteriaType;
import eu.europa.ec.fisheries.uvms.rest.security.RequiresFeature;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;

@Path("/config")
@Stateless
@RequiresFeature(UnionVMSFeature.viewMovements)
public class ConfigResource {

    final static Logger LOG = LoggerFactory.getLogger(ConfigResource.class);

    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "/criterias")
    public ResponseDto getCriterias() {
        try {
            return new ResponseDto(CriteriaType.values(), ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting criterias. ] {} ", ex.getMessage());
            return ErrorHandler.getFault(ex);
        }
    }

    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "/subcriterias")
    public ResponseDto getSubCriterias() {
        try {
            return new ResponseDto(SubCriteriaType.values(), ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting subcriterias. ] {} ", ex.getMessage());
            return ErrorHandler.getFault(ex);
        }
    }

}
