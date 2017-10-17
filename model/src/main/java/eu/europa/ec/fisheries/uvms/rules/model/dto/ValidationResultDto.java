/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.model.dto;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by padhyad on 5/5/2017.
 */
public class ValidationResultDto {

    private boolean isError;

    private boolean isWarning;

    private boolean isOk;

    private List<ValidationMessageType> validationMessages = new ArrayList<>();

    public boolean isError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setIsWarning(boolean isWarning) {
        this.isWarning = isWarning;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setIsOk(boolean isOk) {
        this.isOk = isOk;
    }

    public List<ValidationMessageType> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(List<ValidationMessageType> validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o instanceof ValidationResultDto) return false;
        ValidationResultDto that = (ValidationResultDto) o;
        return isError == that.isError &&
                isWarning == that.isWarning &&
                isOk == that.isOk &&
                Objects.equals(validationMessages, that.validationMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isError, isWarning, isOk, validationMessages);
    }
}
