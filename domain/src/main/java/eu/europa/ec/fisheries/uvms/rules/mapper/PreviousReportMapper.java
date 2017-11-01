/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.mapper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.entity.PreviousReport;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LocalBean
@Stateless
public class PreviousReportMapper {

    private final static Logger LOG = LoggerFactory.getLogger(PreviousReportMapper.class);

    public static PreviousReportType toPreviousReportType(PreviousReportType previousReportType, PreviousReport previousReportEntity)
            throws DaoMappingException {
        try {
            previousReportType.setAssetGuid(previousReportEntity.getAssetGuid());
            previousReportType.setPositionTime(previousReportEntity.getPositionTime());

            return previousReportType;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to model. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to model. ]", e);
        }
    }

    public static PreviousReport toPreviousReportEntity(PreviousReport previousReportEntity, PreviousReportType previousReportType)
            throws DaoMappingException {
        try {
            previousReportEntity.setAssetGuid(previousReportType.getAssetGuid());
            previousReportEntity.setPositionTime(previousReportType.getPositionTime());
            previousReportEntity.setUpdated(DateUtils.nowUTC().toGregorianCalendar().getTime());
            previousReportEntity.setUpdatedBy("UVMS");

            return previousReportEntity;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to entity. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping to entity. ]", e);
        }
    }

    public static PreviousReport toPreviousReportEntity(PreviousReportType previousReportType) throws DaoMappingException {
        PreviousReport previousReportEntity = new PreviousReport();
        return toPreviousReportEntity(previousReportEntity, previousReportType);
    }

    public static PreviousReportType toPreviousReportType(PreviousReport previousReportEntity) throws DaoMappingException {
        PreviousReportType previousReportType = new PreviousReportType();
        return toPreviousReportType(previousReportType, previousReportEntity);
    }
}