package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway.client.config;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AssetRestClientConfigImpl implements AssetRestClientConfig {

    private static final String ASSET_GATEWAY_PATH = "/internal/asset-gateway";

    @Resource(name = "java:global/asset_endpoint_internal")
    private String assetEndpoint;


    @Override
    public String getAssetEndpoint() {
        return assetEndpoint;
    }

    @Override
    public String getAssetGatewayPath() {
        return ASSET_GATEWAY_PATH;
    }
}
