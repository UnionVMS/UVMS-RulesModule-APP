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

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@NoArgsConstructor
@ToString
public class CodeType {

    private String value;
    private String listId;

    public CodeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String typeCode) {
        this.value = typeCode;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeType)) return false;
        CodeType codeType = (CodeType) o;
        return Objects.equals(value, codeType.value) &&
                Objects.equals(listId, codeType.listId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, listId);
    }
}
