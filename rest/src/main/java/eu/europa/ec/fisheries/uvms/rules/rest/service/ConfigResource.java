package eu.europa.ec.fisheries.uvms.rules.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.ConditionType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.LogicOperatorType;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.MainCriteria;
import eu.europa.ec.fisheries.uvms.rest.security.RequiresFeature;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.SubCriteria;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;

@Path("/config")
@Stateless
@RequiresFeature(UnionVMSFeature.viewMovements)
public class ConfigResource {

    final static Logger LOG = LoggerFactory.getLogger(ConfigResource.class);

    // TODO:
    // Is it possible (in an easy way) to map permitted operators to a specific (sub)criteria?

    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "/")
    public ResponseDto getConfig() {
        try {
            Map map = new HashMap();

            Map<String, ArrayList<String>> crit = getCriterias();
            ConditionType[] con = getConditions();
            Map act = getActions();
            LogicOperatorType[] log = getLogicOperatorType();

            map.put("CRITERIA", crit);
            map.put("CONDITIONS", con);
            map.put("ACTIONS", act);
            map.put("LOGIC_OPERATORS", log);

            return new ResponseDto(map, ResponseCode.OK);
        } catch (Exception ex) {
            LOG.error("[ Error when getting actions. ] {} ", ex.getMessage());
            return ErrorHandler.getFault(ex);
        }
    }

    private Map<String, ArrayList<String>> getCriterias() {
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

        MainCriteria[] mainCriterias = MainCriteria.values();
        for (int i = 0; i < mainCriterias.length; i++) {

            ArrayList<String> subResult = new ArrayList<String>();
            String mainCrit = mainCriterias[i].toString();

            SubCriteria[] subCriterias = SubCriteria.values();
            for (int j = 0; j < subCriterias.length; j++) {
//                if (mainCriterias[i].equals(MainCriteria.ROOT)) {
//                    // Add the "subCriteria" as mainCriteria
//                    mainCrit = subCriterias[j].toString();
//                } else {
                    if (subCriterias[j].getMainCriteria().equals(mainCriterias[i])) {
                        subResult.add(subCriterias[j].toString());
                    }
//                }
                map.put(mainCrit, subResult);
            }
        }
        return map;
    }
    
    private LogicOperatorType[] getLogicOperatorType() {
        return LogicOperatorType.values();
    }

    private Map getActions() {
        Map map = new HashMap();
        ActionType[] actionTypes = ActionType.values();
        for (int i = 0; i < actionTypes.length; i++) {

            boolean needValue = false;
            ActionType actionType = actionTypes[i];
            switch (actionType) {
                case SEND_TO_ENDPOINT:
                    break;
                case MANUAL_POLL:
                    break;
                case ON_HOLD:
                    break;
                case TICKET:
                    break;
                case TOP_BAR_NOTIFICATION:
                    break;
                case EMAIL:
                    needValue = true;
                    break;
                case SMS:
                    needValue = true;
                    break;
            }
            map.put(actionType, needValue);
        }
        return map;
    }

    private ConditionType[] getConditions() {
        return ConditionType.values();
    }

}
