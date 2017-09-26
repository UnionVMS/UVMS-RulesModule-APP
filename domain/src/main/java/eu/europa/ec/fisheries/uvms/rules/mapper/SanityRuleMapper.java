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
import java.util.Date;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.entity.SanityRule;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LocalBean
@Stateless
public class SanityRuleMapper {

    private final static Logger LOG = LoggerFactory.getLogger(SanityRuleMapper.class);

    public static SanityRuleType toSanityRuleType(SanityRuleType sanityRuleType, SanityRule sanityRuleEntity) throws DaoMappingException {
        try {
            sanityRuleType.setName(sanityRuleEntity.getName());
            sanityRuleType.setGuid(sanityRuleEntity.getGuid());
            sanityRuleType.setExpression(sanityRuleEntity.getExpression());
            sanityRuleType.setDescription(sanityRuleEntity.getDescription());
            sanityRuleType.setUpdated(DateUtils.dateToString(sanityRuleEntity.getUpdated()));
            sanityRuleType.setUpdatedBy(sanityRuleEntity.getUpdatedBy());

            return sanityRuleType;
        } catch (Exception e) {
            LOG.error("[ Error when mapping sanity to model. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping sanity to model. ]", e);
        }
    }

    public static SanityRule toSanityRuleEntity(SanityRule sanityRuleEntity, SanityRuleType sanityRuleType) throws DaoMappingException {
        try {
            Date now = DateUtils.nowUTC().toGregorianCalendar().getTime();

            // Base
            sanityRuleEntity.setName(sanityRuleType.getName());
            sanityRuleEntity.setGuid(sanityRuleType.getGuid());
            sanityRuleEntity.setExpression(sanityRuleType.getExpression());
            sanityRuleEntity.setDescription(sanityRuleType.getDescription());
            sanityRuleEntity.setUpdated(now);
            sanityRuleEntity.setUpdatedBy(sanityRuleType.getUpdatedBy());

            return sanityRuleEntity;
        } catch (Exception e) {
            LOG.error("[ Error when mapping sanity to entity. ] {}", e.getMessage());
            throw new DaoMappingException("[ Error when mapping sanity to entity. ]", e);
        }
    }

    public static SanityRule toSanityRuleEntity(SanityRuleType sanityRuleType) throws DaoMappingException {
        SanityRule sanityRuleEntity = new SanityRule();
        return toSanityRuleEntity(sanityRuleEntity, sanityRuleType);
    }

    public static SanityRuleType toSanityRuleType(SanityRule sanityRuleEntity) throws DaoMappingException {
        SanityRuleType sanityRuleType = new SanityRuleType();
        return toSanityRuleType(sanityRuleType, sanityRuleEntity);
    }
}