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

package eu.europa.ec.fisheries.uvms.rules.dao;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.rules.entity.FailedRule;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by padhyad on 4/19/2017.
 */
public class FailedRuleDaoTest  extends RulesModulesDaoTest {

    private FailedRuleDao failedRuleDao = new FailedRuleDao(em);

    @Before
    public void prepare() {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_FAILED_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void testSaveFailedRules() {
        em.getTransaction().begin();
        failedRuleDao.updateFailedRules(Arrays.asList("brId5", "brId6"));
        em.flush();
        List<FailedRule> failedRules = failedRuleDao.getAllFailedRules();
        assertNotNull(failedRules);
        FailedRule failedRule = failedRules.iterator().next();
        boolean isPresent = Arrays.asList("brId5", "brId6").contains(failedRule.getBrId());
        assertTrue(isPresent);
    }

    @Test
    @SneakyThrows
    public void testSaveFailedRules_whenDBEmpty() {
        Operation operation = sequenceOf(DELETE_ALL);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);

        em.getTransaction().begin();
        failedRuleDao.updateFailedRules(Arrays.asList("brId5", "brId6"));
        em.flush();
        List<FailedRule> failedRules = failedRuleDao.getAllFailedRules();
        assertNotNull(failedRules);
        FailedRule failedRule = failedRules.iterator().next();
        boolean isPresent = Arrays.asList("brId5", "brId6").contains(failedRule.getBrId());
        assertTrue(isPresent);
    }
}
