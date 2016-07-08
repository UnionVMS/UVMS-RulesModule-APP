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
package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.Date;

public class PreviousReportFact {

    private Date deadline;
    private String assetIdType;
    private String assetIdValue;
    private String movementGuid;
    private String assetGuid;

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getAssetIdType() {
        return assetIdType;
    }

    public void setAssetIdType(String assetIdType) {
        this.assetIdType = assetIdType;
    }

    public String getAssetIdValue() {
        return assetIdValue;
    }

    public void setAssetIdValue(String assetIdValue) {
        this.assetIdValue = assetIdValue;
    }

    public String getMovementGuid() {
        return movementGuid;
    }

    public void setMovementGuid(String movementGuid) {
        this.movementGuid = movementGuid;
    }

    public String getAssetGuid() {
        return assetGuid;
    }

    public void setAssetGuid(String assetGuid) {
        this.assetGuid = assetGuid;
    }

}