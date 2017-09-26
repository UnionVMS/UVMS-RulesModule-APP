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

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rules.entity.FailedRule;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;

/**
 * Created by padhyad on 4/19/2017.
 */
public class FailedRuleDao extends AbstractDAO<FailedRule> {

    private EntityManager em;

    FailedRuleDao(EntityManager em) {
        this.em = em;
    }

    @Override public EntityManager getEntityManager() {
        return em;
    }

    public void updateFailedRules(List<String> brIds) throws ServiceException {
        Query query = getEntityManager().createNamedQuery(FailedRule.DELETE_ALL);
        query.executeUpdate();
        for (String string : brIds) {
            FailedRule failedRule = new FailedRule();
            failedRule.setBrId(string);
            createEntity(failedRule);
        }
    }

    public List<FailedRule> getAllFailedRules() throws ServiceException {
        return findAllEntity(FailedRule.class);
    }
}
