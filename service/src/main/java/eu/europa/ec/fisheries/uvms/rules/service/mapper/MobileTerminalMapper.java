package eu.europa.ec.fisheries.uvms.rules.service.mapper;


import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.MobileTerminalType;

/**
 * Created by osdjup on 2016-05-18.
 */
public class MobileTerminalMapper {
    public static MobileTerminalType mapMobileTerminal(eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType mobileTerminalType) {
        MobileTerminalType rawMobileTerminalType = new MobileTerminalType();
        rawMobileTerminalType.setConnectId(mobileTerminalType.getConnectId());
        rawMobileTerminalType.setGuid(mobileTerminalType.getMobileTerminalId().getGuid());

        return rawMobileTerminalType;
    }
}
