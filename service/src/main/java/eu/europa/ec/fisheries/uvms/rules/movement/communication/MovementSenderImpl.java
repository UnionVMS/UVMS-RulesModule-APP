/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.movement.communication;

import eu.europa.ec.fisheries.schema.movement.module.v1.FindRawMovementsRequest;
import eu.europa.ec.fisheries.schema.movement.module.v1.FindRawMovementsResponse;
import eu.europa.ec.fisheries.schema.movement.module.v1.MovementModuleMethod;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementModelException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link MovementSender}.
 */
@ApplicationScoped
class MovementSenderImpl implements MovementSender {

    private MovementClient movementClient;

    /**
     * Injection constructor
     *
     * @param movementClient The low-level client to the services of the activity module
     */
    @Inject
    public MovementSenderImpl(MovementClient movementClient) {
        this.movementClient = movementClient;
    }

    /**
     * Constructor for frameworks.
     */
    @SuppressWarnings("unused")
    MovementSenderImpl() {
        // NOOP
    }

    @Override
    public List<MovementBaseType> findRawMovements(List<String> guildList) throws RulesServiceException {
        try {
            FindRawMovementsRequest request = new FindRawMovementsRequest();
            request.setMethod(MovementModuleMethod.FIND_RAW_MOVEMENTS);
            request.getMovementGuids().addAll(guildList);
            FindRawMovementsResponse response = null;
            response = movementClient.findRawMovements(request);
            return response != null ? response.getResponse() : null;
        } catch (MovementModelException e) {
            throw new RulesServiceException("error converting to movement model", e);
        }
    }
}
