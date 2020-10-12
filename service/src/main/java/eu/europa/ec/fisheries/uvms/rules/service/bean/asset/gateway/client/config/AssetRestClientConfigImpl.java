/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway.client.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import eu.europa.ec.fisheries.uvms.config.exception.ConfigServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AssetRestClientConfigImpl implements AssetRestClientConfig {

    public static final String ASSET_GATEWAY_ENDPOINT_PARAMETER_KEY = "asset.gateway.endpoint";

    @Inject
    private ParameterService parameterService;

    @Override
    public String getAssetGatewayEndpoint() {
        try {
            return parameterService.getParamValueById(ASSET_GATEWAY_ENDPOINT_PARAMETER_KEY);
        } catch (ConfigServiceException e) {
            log.error("Could not retrieve configuration parameter for key {}", ASSET_GATEWAY_ENDPOINT_PARAMETER_KEY);
            throw new RulesServiceException("Could not retrieve configuration parameter for key " + ASSET_GATEWAY_ENDPOINT_PARAMETER_KEY, e);
        }
    }

    @Override
    public boolean endpointHasChanged(String endpointSettingKey) {
        return endpointSettingKey.equalsIgnoreCase(ASSET_GATEWAY_ENDPOINT_PARAMETER_KEY);
    }
}
