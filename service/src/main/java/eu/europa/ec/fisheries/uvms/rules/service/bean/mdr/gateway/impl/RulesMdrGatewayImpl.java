package eu.europa.ec.fisheries.uvms.rules.service.bean.mdr.gateway.impl;

import eu.europa.ec.fisheries.uvms.mdr.rest.client.MdrClient;
import eu.europa.ec.fisheries.uvms.rules.service.bean.mdr.gateway.RulesMdrGateway;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetAllCodeListsResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListRequest;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetLastRefreshDateResponse;
import un.unece.uncefact.data.standard.mdr.response.FLUXMDRReturnMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class RulesMdrGatewayImpl implements RulesMdrGateway {

    @Inject
    private MdrClient mdrClient;

    @Override
    public void syncMdrEntityMessage(FLUXMDRReturnMessage message){
        try {
            mdrClient.syncMdrCodeList(message);
        }
        catch (Exception e) {
            log.error("Error in communication with Mdr: " + e.getMessage());
        }
    }

    @Override
    public MdrGetCodeListResponse getMdrCodeList(MdrGetCodeListRequest request) {
        try {
            return mdrClient.getSingleMdrCodeListMessage(request);
        }
        catch (Exception e) {
            log.error("Error in communication with Mdr: " + e.getMessage());
        }
        return new MdrGetCodeListResponse();
    }

    @Override
    public MdrGetCodeListResponse getMdrStatus() {
        try {
            return mdrClient.getStatusRequest();
        }
        catch (Exception e) {
            log.error("Error in communication with Mdr: " + e.getMessage());
        }
        return new MdrGetCodeListResponse();
    }

    @Override
    public MdrGetAllCodeListsResponse getAllMdrCodeList() {
        try {
         return mdrClient.getAllMdrCodeList();
        }
        catch (Exception e) {
            log.error("Error in communication with Mdr: " + e.getMessage());
        }
        return new MdrGetAllCodeListsResponse();
    }

    @Override
    public MdrGetLastRefreshDateResponse getLastRefreshDate() {
        try {
           return mdrClient.getLastRefreshDate();
        }
        catch (Exception e) {
            log.error("Error in communication with Mdr: " + e.getMessage());
        }
        return new MdrGetLastRefreshDateResponse();
    }
}
