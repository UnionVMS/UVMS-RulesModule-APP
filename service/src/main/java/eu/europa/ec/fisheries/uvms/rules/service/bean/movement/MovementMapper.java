/*
 * ﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 * © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.movement;

import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdType;
import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.List;

public class MovementMapper {

    private static final DozerBeanMapper mapper = new DozerBeanMapper();
    private static final MovementMapper INSTANCE = new MovementMapper();

    private MovementMapper() {
        mapper.setMappingFiles(getMapperFiles());
    }

    public static MovementMapper getInstance() {
        return INSTANCE;
    }

    private List<String> getMapperFiles() {
        List<String> files = new ArrayList<>();
        files.add("movementbasetype.xml");
        return files;
    }

    public DozerBeanMapper getMapper() {
        return mapper;
    }

    public static List<eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList> mapAssetIdList(
            List<eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList> inList) {
        List<eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList> outList = new ArrayList<>();
        for (eu.europa.ec.fisheries.schema.exchange.movement.asset.v1.AssetIdList inAssetId : inList) {
            eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList outAssetId = new eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList();
            AssetIdType idType = null;
            switch (inAssetId.getIdType()) {
                case CFR:
                    idType = AssetIdType.CFR;
                    break;
                case ID:
                    idType = AssetIdType.ID;
                    break;
                case IMO:
                    idType = AssetIdType.IMO;
                    break;
                case IRCS:
                    idType = AssetIdType.IRCS;
                    break;
                case MMSI:
                    idType = AssetIdType.MMSI;
                    break;
                case GUID:
                    idType = AssetIdType.GUID;
                    break;
                case UVI:
                    idType = AssetIdType.UVI;
                    break;
                case GFCM:
                    idType = AssetIdType.GFCM;
                    break;
            }
            outAssetId.setIdType(idType);
            outAssetId.setValue(inAssetId.getValue());
            outList.add(outAssetId);
        }
        return outList;
    }

    public static List<eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList> mapMobileTerminalIdList(
            List<eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.IdList> inList) {
        List<eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList> outList = new ArrayList<>();
        for (eu.europa.ec.fisheries.schema.exchange.movement.mobileterminal.v1.IdList inId : inList) {
            eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList outId = new eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList();
            IdType idType = null;
            switch (inId.getType()) {
                case DNID:
                    idType = IdType.DNID;
                    break;
                case LES:
                    idType = IdType.LES;
                    break;
                case MEMBER_NUMBER:
                    idType = IdType.MEMBER_NUMBER;
                    break;
                case SERIAL_NUMBER:
                    idType = IdType.SERIAL_NUMBER;
                    break;
            }
            outId.setType(idType);
            outId.setValue(inId.getValue());
            outList.add(outId);
        }
        return outList;
    }

}
