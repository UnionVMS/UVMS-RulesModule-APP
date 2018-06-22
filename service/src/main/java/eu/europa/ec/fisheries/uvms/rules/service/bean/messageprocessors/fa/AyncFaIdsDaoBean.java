package eu.europa.ec.fisheries.uvms.rules.service.bean.messageprocessors.fa;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;
import java.util.Set;

@Stateless
@LocalBean
public class AyncFaIdsDaoBean {

    @EJB
    private RulesDao rulesDaoBean;

    @Asynchronous
    public void saveIdsAssync(Set<FADocumentID> idsFromIncommingMessage, List<String> faIdsPerTripsFromMessage) throws ServiceException {
        rulesDaoBean.createFaDocumentIdEntity(idsFromIncommingMessage);
        rulesDaoBean.saveFaIdsPerTripList(faIdsPerTripsFromMessage);
    }

}
