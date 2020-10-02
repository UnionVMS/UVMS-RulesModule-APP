package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway.client.config;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AssetRestClientConfigImpl implements AssetRestClientConfig {

    private static final String ASSET_GATEWAY_PATH = "/unionvms/asset/gateway/asset-gateway";

    @Resource(name = "java:global/asset_endpoint_internal")
    private String assetEndpointUrl;


    @Override
    public String getAssetEndpointUrl() {
        return assetEndpointUrl;
    }

    @Override
    public String getAssetBasePath() {
        return ASSET_GATEWAY_PATH;
    }
}
