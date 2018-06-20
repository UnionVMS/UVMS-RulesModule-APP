/*
 Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 © European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 © European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.dao;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Dao {

    protected EntityManager em;

    protected TemplateDao factTemplateDao;

    protected FailedRuleDao failedRuleDao;

    protected RawMessageDao rawMessageDao;

    protected ValidationMessageDao validationMessageDao;

    protected RuleStatusDao ruleStatusDao;

    protected FADocumentIDDAO fishingActivityIdDao;

    protected FishingGearTypeCharacteristicDao fishingGearTypeCharacteristicDao;

    protected FaIdsPerTripDao faIdsPerTripDao;

    @PersistenceContext(unitName = "rulesPostgresPU")
    private EntityManager postgres;

    @PersistenceContext(unitName = "rulesOraclePU")
    private EntityManager oracle;

    @PostConstruct
    public void initEntityManager() {
        String dbDialect = System.getProperty("db.dialect");
        if ("oracle".equalsIgnoreCase(dbDialect)) {
            em = oracle;
        } else {
            em = postgres;
        }
        factTemplateDao = new TemplateDao(em);
        failedRuleDao = new FailedRuleDao(em);
        rawMessageDao = new RawMessageDao(em);
        validationMessageDao = new ValidationMessageDao(em);
        ruleStatusDao = new RuleStatusDao(em);
        fishingGearTypeCharacteristicDao = new FishingGearTypeCharacteristicDao(em);
        fishingActivityIdDao = new FADocumentIDDAO(em);
        faIdsPerTripDao = new FaIdsPerTripDao(em);
    }
}
