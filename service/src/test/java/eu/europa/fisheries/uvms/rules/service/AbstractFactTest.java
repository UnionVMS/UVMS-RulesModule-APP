/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * @author Gregory Rinaldi
 */
public class AbstractFactTest {

    private AbstractFact fact = new FaArrivalFact();

    @Test
    public void testCheckDateInPastHappy() {

        Date date = new DateTime(2005, 3, 26, 12, 0, 0, 0).toDate();
        assertFalse(fact.checkDateInPast(date, 20));

    }

    @Test
    public void testCheckDateInPast() {

        Date date = new DateTime(2222, 3, 26, 12, 0, 0, 0).toDate();
        assertTrue(fact.checkDateInPast(date, 20));

    }


    @Test
    public void testValidateIDTypeHappy() {

        IdType idType = new IdType();
        idType.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");

        IdType idType2 = new IdType();
        idType2.setSchemeId("53e36fab361-7338327c7d81");

        List<IdType> idTypes = Arrays.asList(idType, idType2);

        assertTrue(fact.validate(idTypes, "UUID"));

    }

    @Test
    public void testValidateIDType() {

        IdType idType = new IdType();
        idType.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        IdType idType2 = new IdType();
        idType2.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        List<IdType> idTypes = Arrays.asList(idType, idType2);
        assertFalse(fact.validate(idTypes, "UUID"));

    }

}
