/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.Date;
import lombok.Data;

public class RuleFromMDR {

    private String templateEntityName;
    private String note;
    private String expression;
    private String message;
    private String type;
    private String dataFlow;
    private Boolean active;
    private Date startDate;
    private Date endDate;

    public RuleFromMDR(String note, String message, String type, Boolean active, String dataFlow) {
        this.note = note;
        this.message = message;
        this.type = type;
        this.active = active;
        this.dataFlow = dataFlow;
    }

    public String getTemplateEntityName() {
        return templateEntityName;
    }

    public void setTemplateEntityName(String templateEntityName) {
        this.templateEntityName = templateEntityName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataFlow() {
        return dataFlow;
    }

    public void setDataFlow(String dataFlow) {
        this.dataFlow = dataFlow;
    }

    public Boolean isActive() {
        return active;
    }

    public void setIsActive(Boolean active) {
        this.active = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
