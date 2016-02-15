package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.schema.exchange.module.v1.ExchangeModuleMethod;
import eu.europa.ec.fisheries.schema.exchange.module.v1.ProcessedMovementResponse;
import eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.IdType;
import eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.MobileTerminalId;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.SetReportMovementType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMapperException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.JAXBMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExchangeMovementMapper {
    private final static Logger LOG = LoggerFactory.getLogger(ExchangeMovementMapper.class);

    /**
     * Maps only what Exchange needs to complete it's work in the callback
     *
     * @param rawMovement
     * @return the necessary parts of the original SetMovement request
     */
    public static SetReportMovementType mapExchangeMovement(RawMovementType rawMovement) {
        LOG.debug("Mapping response movement to Exchange");

        SetReportMovementType setReportMovementType = new SetReportMovementType();
        setReportMovementType.setTimestamp(rawMovement.getDateRecieved());
        setReportMovementType.setPluginName(rawMovement.getPluginName());
        if (rawMovement.getPluginType() != null) {
            setReportMovementType.setPluginType(PluginType.valueOf(rawMovement.getPluginType()));
        }
        MovementBaseType movement = new MovementBaseType();
        if (rawMovement.getSource() != null) {
            movement.setSource(MovementSourceType.fromValue(rawMovement.getSource().value()));
        }

        // MobileTerminalId
        if (rawMovement.getMobileTerminal() != null) {
            MobileTerminalId mobileTerminalId = new MobileTerminalId();
            mobileTerminalId.setGuid(rawMovement.getMobileTerminal().getGuid());
            mobileTerminalId.setConnectId(rawMovement.getMobileTerminal().getConnectId());
            List<IdList> mtIds = rawMovement.getMobileTerminal().getMobileTerminalIdList();
            for (IdList mtId : mtIds) {
                eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.IdList idList = new eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.IdList();
                idList.setType(IdType.fromValue(mtId.getType().value()));
                idList.setValue(mtId.getValue());

                mobileTerminalId.getMobileTerminalIdList().add(idList);
            }
            movement.setMobileTerminalId(mobileTerminalId);
        }

        // AssetId
        if (rawMovement.getAssetId() != null) {
            eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetId assetId = new eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetId();
            if (rawMovement.getAssetId().getAssetType() != null) {
                assetId.setAssetType(eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetType.fromValue(rawMovement.getAssetId().getAssetType().value()));
            }
            List<AssetIdList> aIds = rawMovement.getAssetId().getAssetIdList();
            for (AssetIdList aId : aIds) {
                eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList idList = new eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList();
                idList.setIdType(eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdType.fromValue(aId.getIdType().value()));
                idList.setValue(aId.getValue());

                assetId.getAssetIdList().add(idList);
            }
            movement.setAssetId(assetId);
        }

        setReportMovementType.setMovement(movement);

        return setReportMovementType;
    }

    public static String mapToProcessedMovementResponse(SetReportMovementType orgRequest, MovementRefType movementRef) throws ExchangeModelMapperException {
        ProcessedMovementResponse response = new ProcessedMovementResponse();
        response.setMethod(ExchangeModuleMethod.PROCESSED_MOVEMENT);
        response.setOrgRequest(orgRequest);
        response.setMovementRefType(movementRef);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

}
