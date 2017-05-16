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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

@Slf4j
public abstract class AbstractFact {

    protected FactType factType;

    protected List<RuleWarning> warnings;

    protected List<RuleError> errors;

    protected List<String> uniqueIds = new ArrayList<>();

    protected boolean ok = true;

    private String disabledRuleId;

    public AbstractFact() {
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public abstract void setFactType();

    public void addWarningOrError(String type, String msg, String brId, String level) {
        if (type.equalsIgnoreCase(ErrorType.ERROR.value())) {
            getErrors().add(new RuleError(brId, msg, level));
        } else {
            getWarnings().add(new RuleWarning(brId, msg, level));
        }
    }

    public boolean schemeIdContains(List<IdType> idTypes, String... values) {

        List<String> stringList = new ArrayList<>(Arrays.asList(values));

        Iterator<String> iterator = stringList.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            for (IdType IdType : idTypes) {
                if (next.equals(IdType.getSchemeId())) {
                    iterator.remove();
                }
            }
        }
        return !stringList.isEmpty();
    }

    public boolean listIdContains(List<CodeType> codeTypes, String... values) {

        List<String> stringList = new ArrayList<>(Arrays.asList(values));

        Iterator<String> iterator = stringList.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            for (CodeType IdType : codeTypes) {
                if (next.equals(IdType.getListID())) {
                    iterator.remove();
                }
            }
        }
        return !stringList.isEmpty();
    }

    public boolean schemeIdContains(IdType idType, String... values) {
        return schemeIdContains(Collections.singletonList(idType), values);
    }

    public boolean listIdContains(CodeType codeType, String... values) {
        return listIdContains(Collections.singletonList(codeType), values);
    }

    public boolean checkDateInPast(Date date, int minusHours) {
        DateTime now = getDateNow();
        now.minusHours(minusHours);
        return date == null || !new DateTime(date).isBefore(now);
    }

    protected DateTime getDateNow() {
        return new DateTime();
    }

    public boolean validateUUID(String value) {
        if (value == null) {
            return true;
        }
        try {
            UUID.fromString(value);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return true;
        }
        return false;
    }

    public List<RuleWarning> getWarnings() {
        return warnings;
    }

    public List<RuleError> getErrors() {
        return errors;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public FactType getFactType() {
        return factType;
    }

    public List<String> getUniqueIds() {
        return uniqueIds;
    }

    public void setUniqueIds(List<String> uniqueIds) {
        this.uniqueIds = uniqueIds;
    }

}
