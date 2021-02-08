/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import com.google.common.collect.ImmutableList;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;

import java.util.List;

public enum ContainerType {

    FA_REPORT("faReport", "ec.europa.eu.faReport", FactType.FA_REPORT_DOCUMENT,
            FactType.FLUX_FA_REPORT_MESSAGE, FactType.VESSEL_TRANSPORT_MEANS, FactType.STRUCTURED_ADDRESS,
            FactType.FISHING_GEAR, FactType.GEAR_CHARACTERISTIC, FactType.GEAR_PROBLEM, FactType.FA_CATCH,
            FactType.FISHING_TRIP, FactType.FLUX_LOCATION, FactType.FLUX_CHARACTERISTIC,
            FactType.VESSEL_STORAGE_CHARACTERISTIC, FactType.FISHING_ACTIVITY, FactType.FA_DEPARTURE,
            FactType.FA_ENTRY_TO_SEA, FactType.FA_FISHING_OPERATION, FactType.FA_JOINT_FISHING_OPERATION,
            FactType.FA_RELOCATION, FactType.FA_DISCARD, FactType.FA_EXIT_FROM_SEA,
            FactType.FA_NOTIFICATION_OF_ARRIVAL, FactType.FA_ARRIVAL, FactType.FA_LANDING,
            FactType.FA_TRANSHIPMENT, FactType.FA_APPLIED_AAP_PROCESS, FactType.FA_NOTIFICATION_OF_TRANSHIPMENT_OR_RELOCATION,
            FactType.FA_DECLARATION_OF_TRANSHIPMENT_OR_RELOCATION,FactType.FA_FLAP_DOCUMENT, FactType.SIMPLE_ID_TYPE_FACT,
            FactType.FA_VESSEL_POSITION_EVENT, FactType.FA_AAP_STOCK),

    FA_QUERY("faQuery", "ec.europa.eu.faQuery", FactType.FA_QUERY,
            FactType.FA_QUERY_PARAMETER),

    FA_RESPONSE("faResponse", "ec.europa.eu.faResponse", FactType.FA_RESPONSE,
            FactType.FA_VALIDATION_QUALITY_ANALYSIS),

    SALES("sales", "ec.europa.eu.sales", FactType.SALES_FLUX_SALES_REPORT_MESSAGE,
            FactType.SALES_FLUX_REPORT_DOCUMENT, FactType.SALES_FLUX_PARTY, FactType.SALES_REPORT,
            FactType.SALES_DOCUMENT, FactType.SALES_PARTY, FactType.SALES_EVENT,
            FactType.SALES_BATCH, FactType.SALES_AAP_PRODUCT, FactType.SALES_AAP_PROCESS,
            FactType.SALES_SIZE_DISTRIBUTION, FactType.SALES_PRICE, FactType.SALES_FLUX_ORGANIZATION,
            FactType.SALES_FISHING_ACTIVITY, FactType.SALES_DELIMITED_PERIOD, FactType.SALES_VESSEL_TRANSPORT_MEANS,
            FactType.SALES_VESSEL_COUNTRY, FactType.SALES_CONTACT_PARTY, FactType.SALES_CONTACT_PERSON,
            FactType.SALES_FISHING_TRIP, FactType.SALES_FLUX_LOCATION, FactType.SALES_FLUX_GEOGRAPHICAL_COORDINATE,
            FactType.SALES_STRUCTURED_ADDRESS, FactType.SALES_QUERY, FactType.SALES_FLUX_RESPONSE_DOCUMENT,
            FactType.SALES_VALIDATION_RESULT_DOCUMENT, FactType.SALES_VALIDATION_QUALITY_ANALYSIS,FactType.SALES_REPORT_WRAPPER,
            FactType.SALES_AUCTION_SALE, FactType.SALES_FLUX_SALES_QUERY_MESSAGE, FactType.SALES_QUERY_PARAMETER, FactType.SALES_FLUX_SALES_RESPONSE_MESSAGE),

    MOVEMENTS("movement","ec.europa.eu.movement", FactType.MOVEMENT_REPORT_DOCUMENT, FactType.MOVEMENT_REPORT_DOCUMENT_ID, 
            FactType.MOVEMENT_REPORT_DOC_OWNER_FLUX_PARTY_ID, FactType.MOVEMENT_VESSEL_TRANSPORT_MEANS_ID, FactType.MOVEMENT_SPECIFIED_VESSEL_POSITION_EVENT);

    private final String packageName;
    private final String containerName;
    private final List<FactType> factTypesList;

    ContainerType(String containerName, String packageName, FactType... factTypesList) {
        this.containerName = containerName;
        this.packageName = packageName;
        this.factTypesList = ImmutableList.copyOf(factTypesList);
    }

    public String getPackageName() {
        return packageName;
    }
    public String getContainerName() {
        return containerName;
    }
    public List<FactType> getFactTypesList() {
        return factTypesList;
    }
}
