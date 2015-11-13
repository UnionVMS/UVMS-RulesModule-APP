package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ComChannelAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ComChannelType;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalAttribute;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.MobileTerminalType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;

import java.util.List;
import java.util.UUID;

public class RawMovementFactMapper {
    public static RawMovementFact mapRawMovementFact(RawMovementType rawMovement, MobileTerminalType mobileTerminal, Vessel vessel, String pluginType) {
        RawMovementFact fact = new RawMovementFact();
        fact.setRawMovementType(rawMovement);
        fact.setOk(true);
        fact.setPluginType(pluginType);

        // Base
        if (rawMovement.getComChannelType() != null) {
            fact.setComChannelType(rawMovement.getComChannelType().name());
        }
        fact.setMovementGuid(UUID.randomUUID().toString());
        if (rawMovement.getMovementType() != null) {
            fact.setMovementType(rawMovement.getMovementType().name());
        }
        if (rawMovement.getPositionTime() != null) {
            fact.setPositionTime(rawMovement.getPositionTime().toGregorianCalendar().getTime());
        }
        fact.setReportedCourse(rawMovement.getReportedCourse());
        fact.setReportedSpeed(rawMovement.getReportedSpeed());
        if (rawMovement.getSource() != null) {
            fact.setSource(rawMovement.getSource().name());
        }
        fact.setStatusCode(rawMovement.getStatus());

        // Activity
        if (rawMovement.getActivity() != null) {
            fact.setActivityCallback(rawMovement.getActivity().getCallback());
            fact.setActivityMessageId(rawMovement.getActivity().getMessageId());
            if (rawMovement.getActivity().getMessageType() != null) {
                fact.setActivityMessageType(rawMovement.getActivity().getMessageType().name());
            }
        }

        // Position
        if (rawMovement.getPosition() != null) {
            fact.setAltitude(rawMovement.getPosition().getAltitude());
            fact.setLatitude(rawMovement.getPosition().getLatitude());
            fact.setLongitude(rawMovement.getPosition().getLongitude());
        }

        if (vessel != null) {
            fact.setVesselGuid(vessel.getVesselId().getGuid());
            fact.setVesselCfr(vessel.getCfr());
            fact.setVesselIrcs(vessel.getIrcs());
        }

        // From Mobile Terminal
        if (mobileTerminal != null) {
            List<ComChannelType> channels = mobileTerminal.getChannels();
            for (ComChannelType channel : channels) {
                List<ComChannelAttribute> chanAttributes = channel.getAttributes();
                for (ComChannelAttribute chanAttribute : chanAttributes) {
                    if (chanAttribute.getType().equals("DNID")) {
                        fact.setMobileTerminalDnid(chanAttribute.getValue());
                    }
                    if (chanAttribute.getType().equals("MEMBER_NUMBER")) {
                        fact.setMobileTerminalMemberNumber(chanAttribute.getValue());
                    }
                }
            }
            List<MobileTerminalAttribute> attributes = mobileTerminal.getAttributes();
            for (MobileTerminalAttribute attribute : attributes) {
                if (attribute.getType().equals("SERIAL_NUMBER")) {
                    fact.setMobileTerminalSerialNumber(attribute.getValue());
                }
            }
            fact.setMobileTerminalConnectId(mobileTerminal.getConnectId());
            fact.setMobileTerminalType(mobileTerminal.getType());
        }

        return fact;
    }

}
