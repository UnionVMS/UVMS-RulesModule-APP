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

import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rules.entity.RawMessage;
import eu.europa.ec.fisheries.uvms.rules.entity.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by padhyad on 5/8/2017.
 */
@Slf4j
public class ValidationMessageDao extends AbstractDAO<ValidationMessage> {

    private EntityManager em;

    public ValidationMessageDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ValidationMessage> getValidationMessagesById(List<String> ids) throws ServiceException {
        TypedQuery query = this.getEntityManager().createNamedQuery(ValidationMessage.BY_ID, ValidationMessage.class);
        query.setParameter("messageIds", ids);
        return query.getResultList();
    }

    public List<ValidationMessage> getValidationMessagesByRawMessageGuid(String rawMsgGuid, String type) throws ServiceException {
        TypedQuery query = this.getEntityManager().createNamedQuery(RawMessage.BY_GUID, RawMessage.class);
        query.setParameter("rawMsgGuid", rawMsgGuid);
        RawMsgType typeType = null;
        try{
            typeType = RawMsgType.fromValue(type);
        } catch(IllegalArgumentException ex){
            log.error("[ERROR] Type "+type+" is not present in RawMsgType class..");
        }
        query.setParameter("msgType", typeType);
        List<RawMessage> resultList = (List<RawMessage>) query.getResultList();
        List<ValidationMessage> validMessages = new ArrayList<>();
        if(CollectionUtils.isEmpty(resultList)){
            return validMessages;
        }
        for(RawMessage rawMsg : resultList){
            validMessages.addAll(rawMsg.getValidationMessages());
        }
        return validMessages;
    }
}
