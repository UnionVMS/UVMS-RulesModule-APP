/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleWarning;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by kovian on 27/06/2017.
 */
public class RuleWarningErrorTest {

    @Test
    public void createRuleWarning() {
        List<String> xpathProps = Arrays.asList("ids", "roleCode");
        RuleWarning ruleWarning = new RuleWarning("fake-brid", "fake warning message", "L01", xpathProps);
        assertNotNull(ruleWarning);
        ruleWarning.setLevel("L03");
        assertNotNull(ruleWarning.getLevel());
        ruleWarning.setMessage("Message");
        assertNotNull(ruleWarning.getMessage());
        ruleWarning.setRuleId("F023-0001");
        assertNotNull(ruleWarning.getLevel());
        ruleWarning.setXpaths(new ArrayList<String>());
        assertNotNull(ruleWarning.getXpaths());

    }

    @Test
    public void createRuleError() {
        List<String> xpathProps = Arrays.asList("ids", "roleCode");
        RuleError ruleError = new RuleError("fake-brid", "fake warning message", "L01", xpathProps);
        assertNotNull(ruleError);
        ruleError.setLevel("L03");
        assertNotNull(ruleError.getLevel());
        ruleError.setMessage("Message");
        assertNotNull(ruleError.getMessage());
        ruleError.setRuleId("F023-0001");
        assertNotNull(ruleError.getLevel());
        ruleError.setXpaths(new ArrayList<String>());
        assertNotNull(ruleError.getXpaths());
    }

}
