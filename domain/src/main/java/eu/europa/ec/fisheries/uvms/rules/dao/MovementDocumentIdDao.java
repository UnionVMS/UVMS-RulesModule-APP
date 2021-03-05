/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.rules.entity.MovementDocumentId;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class MovementDocumentIdDao extends AbstractDAO<MovementDocumentId> {

    private EntityManager em;

    public MovementDocumentIdDao(EntityManager em) {
        this.em = em;
    }

    @Override public EntityManager getEntityManager() {
        return em;
    }

    public List<MovementDocumentId> loadMovementDocumentIDByIds(Set<MovementDocumentId> ids) {
        Set<String> stringSet = new HashSet<>();
        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<>();
        }
        for (MovementDocumentId id : ids) {
            String uuid = id.getUuid();
            if(StringUtils.isNotEmpty(uuid)){
                stringSet.add(uuid);
                stringSet.add(uuid.toLowerCase());
                stringSet.add(uuid.toUpperCase());
            }
        }
        Query query = getEntityManager().createNamedQuery(MovementDocumentId.LOAD_BY_UUID);
        query.setParameter("uuids", stringSet);
        return query.getResultList();
    }

}
