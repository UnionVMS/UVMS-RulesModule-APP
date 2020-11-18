package eu.europa.ec.fisheries.uvms.rules.service.bean.mdr.gateway;

import un.unece.uncefact.data.standard.mdr.communication.MdrGetAllCodeListsResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListRequest;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetLastRefreshDateResponse;
import un.unece.uncefact.data.standard.mdr.response.FLUXMDRReturnMessage;

public interface RulesMdrGateway {

    void syncMdrEntityMessage(FLUXMDRReturnMessage message);

    MdrGetCodeListResponse getMdrCodeList(MdrGetCodeListRequest request);

    MdrGetCodeListResponse getMdrStatus();

    MdrGetAllCodeListsResponse getAllMdrCodeList();

    MdrGetLastRefreshDateResponse getLastRefreshDate();
}
