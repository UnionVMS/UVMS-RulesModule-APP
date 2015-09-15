package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.entity.ErrorItem;
import eu.europa.ec.fisheries.uvms.rules.service.entity.ErrorReport;
import eu.europa.ec.fisheries.uvms.rules.service.exception.ErrorReportDaoException;

@Stateless
public class ErrorReportDaoImpl implements ErrorReportDao {

    private static final String GUID_PARAMETER = "guid";

    final static Logger LOG = LoggerFactory.getLogger(ErrorReportDaoImpl.class);

    @PersistenceContext(unitName = "internalPU")
    EntityManager em;

    @Override
    public ErrorReport getErrorReportByOffendingObjectGuid(String guid) throws ErrorReportDaoException {
        ErrorReport errorReport;
        try {
            TypedQuery<ErrorReport> query = em.createNamedQuery(ServiceConstants.FIND_ERROR_REPORT_BY_GUID, ErrorReport.class);
            query.setParameter(GUID_PARAMETER, guid);
            errorReport = query.getSingleResult();
        } catch (NoResultException e) {
            LOG.debug("Fist position report");
            return null;
        } catch (Exception e) {
            LOG.error("[ Error when getting error report. ] {}", e.getMessage());
            throw new ErrorReportDaoException("[ Error when getting error report. ]", e);
        }

        return errorReport;
    }

    @Override
    public ErrorReport createError(String comment, String guid) throws ErrorReportDaoException {
        LOG.info("Creating error report for object guid:{} ({})", guid, comment);

        ErrorReport errorReport = getErrorReportByOffendingObjectGuid(guid);
        if (errorReport == null) {
            errorReport = new ErrorReport(guid);
        }

        errorReport.setUpdated(new Date());
        errorReport.setUpdatedBy("UVMS");

        ErrorItem errorItem = new ErrorItem();
        errorItem.setComment(comment);
        errorItem.setUpdated(new Date());
        errorItem.setUpdatedBy("UVMS");
        errorItem.setErrorReport(errorReport);
        errorReport.getErrorList().add(errorItem);

        try {
            em.persist(errorReport);
        } catch (Exception e) {
            LOG.error("[ Error when persisting error report. ] {}", e.getMessage());
            throw new ErrorReportDaoException("[ Error when persisting error report. ]", e);
        }
        return errorReport;
    }

}
