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
