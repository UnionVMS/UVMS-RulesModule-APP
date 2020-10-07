/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway.AssetGateway;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway.client.AssetClient;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AssetGatewayImpl implements AssetGateway {

    @Inject
    private AssetClient assetClient;

    @Override
    public List<Asset> findHistoryOfAssetByCfr(String cfr) {
        try {
            return assetClient.findHistoryOfAssetByCfr(cfr);
        } catch (Exception e) { // previous implementation in case of any error returned empty list (from timeout(?) to sql error)
            log.error("Error in communication with asset: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Asset> findHistoryOfAssetBy(String reportDate, String cfr, String regCountry, String ircs, String extMark, String iccat) {
        try {
            return assetClient.findHistoryOfAssetBy(reportDate, cfr, regCountry, ircs, extMark, iccat);
        } catch (Exception e) { // previous implementation in case of any error returned empty list (from timeout(?) to sql error)
            log.error("Error in communication with asset: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
