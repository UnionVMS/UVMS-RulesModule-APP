/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.movement.communication;

import eu.europa.ec.fisheries.schema.movement.module.v1.FindRawMovementsRequest;
import eu.europa.ec.fisheries.schema.movement.module.v1.FindRawMovementsResponse;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementModelException;

/**
 * Low-level client to interesting Movement module services.
 */
public interface MovementClient {

    /**
     * Call @{@code FIND_RAW_MOVEMENTS}
     *
     * @param request the movement module request
     * @return The movement module response
     */
    FindRawMovementsResponse findRawMovements(FindRawMovementsRequest request) throws MovementModelException;
}
