/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by kovian on 13/12/2017.
 */
@Slf4j
@NoArgsConstructor
public abstract class AbstractRulesBaseMapper {

    String joinListOfStringsInCommaSeparatedString(List<String> xpaths) {
        if (CollectionUtils.isEmpty(xpaths)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.join(xpaths, ",");
    }

    List<String> splitSingleStringByComma(String xpathListCommaSeparated) {
        if (StringUtils.isEmpty(xpathListCommaSeparated)) {
            return Collections.singletonList(null);
        }
        return Arrays.asList(xpathListCommaSeparated.split("\\s*,\\s*"));
    }

}
