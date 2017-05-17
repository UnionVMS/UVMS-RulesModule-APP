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

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.*;

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

    public boolean schemeIdContainsAll(List<IdType> idTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(idTypes)) {
            return true;
        }
        int valLength = valuesToMatch.length;
        int hits      = 0;
        for (String val : valuesToMatch) {
            for (IdType IdType : idTypes) {
                if (val.equals(IdType.getSchemeId())) {
                    hits++;
                }
            }
        }
        return valLength != hits;
    }

    /**
     * Checks if one of the String... array elements exists in the idTypes list.
     *
     * @param idTypes
     * @param values
     * @return
     */
    public boolean schemeIdContainsAtLeastOne(List<IdType> idTypes, String... values){
        if (values == null || values.length == 0 || CollectionUtils.isEmpty(idTypes)) {
            return true;
        }
        for (String val : values) {
            for (IdType IdType : idTypes) {
                if (val.equals(IdType.getSchemeId())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *  Validate the format of the value depending on the schemeId for List<IdType>
     *
     * @param ids
     * @return
     */
    public boolean validateFormat(List<IdType> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        for (IdType id : ids) {
            if (validateFormat(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate the format of the value depending on the schemeId for single IdType
     *
     * @param id
     * @return
     */
    private boolean validateFormat(IdType id) {
        boolean validationIsOk;
        final String value    = id.getValue();
        final String schemeId = id.getSchemeId();
        if(StringUtils.isEmpty(value) || StringUtils.isEmpty(schemeId)){
            return true;
        }
        try {
            validationIsOk = validateFormat(value, FORMATS.valueOf(schemeId).getFormatStr());
        } catch (IllegalArgumentException ex){
            log.error("The SchemeId : '"+value+"' is not mapped in the AbstractFact.validateFormat(List<IdType> ids) method.", ex);
            return true;
        }
        if(!validationIsOk){
            return true;
        }
        return false;
    }


    private boolean validateFormat(String value, String format) {
        if(StringUtils.isEmpty(value)){
            return false;
        }
        return value.matches(format);
    }


    public boolean listIdContainsAll(List<CodeType> codeTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        int valLength = valuesToMatch.length;
        int hits      = 0;
        for (String val : valuesToMatch) {
            for (CodeType IdType : codeTypes) {
                if (val.equals(IdType.getListId())) {
                    hits++;
                }
            }
        }
        return valLength != hits;
    }

    public boolean schemeIdContainsAll(IdType idType, String... values) {
        return schemeIdContainsAll(Collections.singletonList(idType), values);
    }

    public boolean listIdContainsAll(CodeType codeType, String... values) {
        return listIdContainsAll(Collections.singletonList(codeType), values);
    }

    public boolean checkDateInPast(Date date, int minusHours) {
        DateTime now = getDateNow();
        now.minusHours(minusHours);
        return date == null || !new DateTime(date).isBefore(now);
    }

    protected DateTime getDateNow() {
        return new DateTime();
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

    private enum FORMATS {

        UUID("[a-f0-9]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"),
        EXT_MARK("someFromat"),
        IRCS("someFromat"),
        CFR("someFromat"),
        UVI("someFromat"),
        ICCAT("someFromat"),
        GFCM("someFromat");

        String formatStr;

        FORMATS(String someFromat) {
            setFormatStr(someFromat);
        }

        public String getFormatStr() {
            return formatStr;
        }
        public void setFormatStr(String formatStr) {
            this.formatStr = formatStr;
        }
    }

}
