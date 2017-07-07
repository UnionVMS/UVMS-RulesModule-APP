/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

/**
 * Created by kovian on 03/07/2017.
 */
public class IdTypeWithFlagState {

    private String schemeId;

    private String value;

    private String flagState;

    public IdTypeWithFlagState(){
        super();
    }

    public IdTypeWithFlagState(String schemeId, String value, String flagState) {
        this.schemeId = schemeId;
        this.value = value;
        this.flagState = flagState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdTypeWithFlagState)) return false;

        IdTypeWithFlagState that = (IdTypeWithFlagState) o;

        if (getSchemeId() != null ? !getSchemeId().equals(that.getSchemeId()) : that.getSchemeId() != null)
            return false;
        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
        return getFlagState() != null ? getFlagState().equals(that.getFlagState()) : that.getFlagState() == null;
    }

    public String getSchemeId() {
        return schemeId;
    }
    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getFlagState() {
        return flagState;
    }
    public void setFlagState(String flagState) {
        this.flagState = flagState;
    }
}


