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

package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.List;

public class RuleError {

    private String ruleId;

    private String message;

    protected String level;

    protected List<String> xpaths;

    public RuleError(String ruleId, String message, String level, List<String> xpathsForProps) {
        this.ruleId = ruleId;
        this.message = message;
        this.level = level;
        this.xpaths = xpathsForProps;
    }


    public String getRuleId() {
        return ruleId;
    }
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public List<String> getXpaths() {
        return xpaths;
    }
    public void setXpaths(List<String> xpaths) {
        this.xpaths = xpaths;
    }
}
