package eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper;

import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ListValueTypeFilter;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SingleValueTypeFilter;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class ActivityModuleRequestMapperFacade {

    public String mapToActivityGetFishingTripRequest(List<ListValueTypeFilter> listFilter, List<SingleValueTypeFilter> singleFilters) throws ActivityModelMarshallException {
        return ActivityModuleRequestMapper.mapToActivityGetFishingTripRequest(listFilter, singleFilters);
    }
}
