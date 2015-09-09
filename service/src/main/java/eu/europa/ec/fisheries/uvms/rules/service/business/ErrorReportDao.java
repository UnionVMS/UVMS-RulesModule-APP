package eu.europa.ec.fisheries.uvms.rules.service.business;

import javax.ejb.Stateless;

import eu.europa.ec.fisheries.uvms.rules.service.entity.ErrorReport;
import eu.europa.ec.fisheries.uvms.rules.service.exception.ErrorReportDaoException;

@Stateless
public interface ErrorReportDao {

    public ErrorReport getErrorReportByOffendingObjectGuid(String guid) throws ErrorReportDaoException;

    public ErrorReport createError(String comment, String objectGuid) throws ErrorReportDaoException;

}
