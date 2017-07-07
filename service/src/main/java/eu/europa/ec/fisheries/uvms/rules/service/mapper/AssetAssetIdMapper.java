/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;

public class AssetAssetIdMapper {

    public static AssetId mapAssetToAssetId(eu.europa.ec.fisheries.wsdl.asset.types.Asset asset) {
        AssetId newAssetId = new AssetId();
        AssetIdList assetIdList = mapAssetAssetId(asset);
        newAssetId.getAssetIdList().add(assetIdList);

        if (asset.getIrcs() != null) {
            AssetIdList ircs = new AssetIdList();
            ircs.setIdType(AssetIdType.IRCS);
            ircs.setValue(asset.getIrcs());
            newAssetId.getAssetIdList().add(ircs);
        }

        if (asset.getCfr() != null) {
            AssetIdList cfr = new AssetIdList();
            cfr.setValue(asset.getCfr());
            cfr.setIdType(AssetIdType.CFR);
            newAssetId.getAssetIdList().add(cfr);
        }

        return newAssetId;
    }

    private static AssetIdList mapAssetAssetId(Asset asset) {
        AssetIdList assetIdList = new AssetIdList();
        switch (asset.getAssetId().getType()) {
            case CFR:
                assetIdList.setIdType(AssetIdType.CFR);
                break;
            case GUID:
                assetIdList.setIdType(AssetIdType.GUID);
                break;
            case IMO:
                assetIdList.setIdType(AssetIdType.IMO);
                break;
            case INTERNAL_ID:
                assetIdList.setIdType(AssetIdType.ID);
                break;
            case IRCS:
                assetIdList.setIdType(AssetIdType.IRCS);
                break;
            case MMSI:
                assetIdList.setIdType(AssetIdType.MMSI);
                break;
        }
        assetIdList.setValue(asset.getAssetId().getValue());
        return assetIdList;
    }
}