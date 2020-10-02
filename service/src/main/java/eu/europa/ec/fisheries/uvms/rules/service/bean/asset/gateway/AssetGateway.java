/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway;

import java.util.List;

import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

public interface AssetGateway {
    List<Asset> findHistoryOfAssetByCfr(String cfr);

    List<Asset> findHistoryOfAssetBy(String reportDate, String cfr, String regCountry, String ircs, String extMark, String iccat);
}
