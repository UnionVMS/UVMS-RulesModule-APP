/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleStatusType;
import eu.europa.ec.fisheries.uvms.rules.entity.RuleStatus;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by padhyad on 7/14/2017.
 */
public class RuleStatusDao extends AbstractDAO<RuleStatus> {

    private EntityManager em;

    RuleStatusDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public RuleStatusType findRuleStatus() throws ServiceException {
        List<RuleStatus> ruleStatuses = findAllEntity(RuleStatus.class);
        if (!CollectionUtils.isEmpty(ruleStatuses)) {
            RuleStatus ruleStatus = ruleStatuses.get(0);
            return ruleStatus.getRuleStatus();
        }
        return null;
    }

    public void deleteRuleStatus() throws ServiceException {
        Query query = getEntityManager().createNamedQuery(RuleStatus.DELETE_ALL);
        query.executeUpdate();
    }

    public void createRuleStatus(RuleStatus ruleStatus) throws ServiceException {
        createEntity(ruleStatus);
    }
}
