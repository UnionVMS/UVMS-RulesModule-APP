/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.dao;

import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.BaseDAOTest;

import static com.ninja_squad.dbsetup.Operations.*;

public abstract class RulesModulesDaoTest extends BaseDAOTest {

    static final Operation DELETE_ALL = sequenceOf(
            deleteAllFrom("rules.rule", "rules.template", "rules.failedrule", "rules.validationmessage", "rules.rawmessage", "rules.messageid", "rules.rulestatus")
    );

    static final Operation INSERT_RULE_DATA = sequenceOf(
            insertInto("rules.rule")
                    .columns("rule_id", "br_id", "expression", "note", "error_type", "message", "rule_created_on", "template_id", "level", "property_names", "disabled")
                    .values(1L, "br1", "typeCode != null", "check type code", "ERROR", "type code is not set", java.sql.Date.valueOf("2014-12-12"), 1, "L00", "ids", true)
                    .values(2L, "br2", "typeCode == null", "check type code", "ERROR", "type code is set", java.sql.Date.valueOf("2014-12-13"), 2, "L00", "ids", false)
                    .values(3L, "br3", "typeCode != \"abc\"", "check type code", "ERROR", "type code is not abc", java.sql.Date.valueOf("2014-12-14"), 3, "L00", "ids", false)
                    .values(4L, "br4", "typeCode != id", "check type code", "WARNING", "type code is same as id", java.sql.Date.valueOf("2014-12-15"), 4, "L00", "ids", false)
                    .build());

    static final Operation INSERT_TEMPLATE_DATA = sequenceOf(
            insertInto("rules.template")
                    .columns("template_id", "template_name", "fact_template")
                    .values(1L, "FaTemplate", "FA_REPORT_DOCUMENT")
                    .values(2L, "VesselTemplate", "VESSEL_TRANSPORT_MEANS")
                    .values(3L, "ActivityTemplate", "FISHING_ACTIVITY")
                    .values(4L, "QueryTemplate", "FA_QUERY")
                    .build());

    static final Operation INSERT_FAILED_DATA = sequenceOf(
            insertInto("rules.failedrule")
                    .columns("rule_id", "br_id")
                    .values(1L, "br1")
                    .values(2L, "br2")
            .build());

    static final Operation INSERT_RAW_MESSAGE = sequenceOf(
                insertInto("rules.rawmessage")
                .columns("id", "raw_message")
                .values(1L, "TEST MESSAGE")
                .build()
            );

    static final Operation INSERT_VALIDATION_MESSAGE = sequenceOf(
                insertInto("rules.validationmessage")
                .columns("id", "error_type", "br_id", "message", "raw_message_id", "level")
                .values(1L, "ERROR", "123", "Invalid", 1L, "L00")
                .values(2L, "ERROR", "1234", "Invalid", 1L, "L01")
                .build()
            );

    static final Operation INSERT_MESSAGE_ID = sequenceOf(
                insertInto("rules.messageid")
                .columns("id", "message_id", "validation_message_id")
                        .values(1L, "abc", 1L)
                        .values(2L, "bcd", 2L)
                        .values(3L, "abc", 1L)
                        .values(4L, "bcd", 2L)
                        .build()
            );


    @Override protected String getSchema() {
        return "rules";
    }

    @Override protected String getPersistenceUnitName() {
        return "rulesPostgresTestPU";
    }
}
