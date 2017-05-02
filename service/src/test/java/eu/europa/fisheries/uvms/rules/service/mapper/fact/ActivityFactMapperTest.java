/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service.mapper.fact;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

/**
 * TODO create test
 */
public class ActivityFactMapperTest {

    @Test
    public void testGenerateFactForVesselTransportMean(){

        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        CodeType codeType = new CodeType();
        codeType.setValue("codeTypeValue");
        vesselTransportMeans.setRoleCode(codeType);

        IDType idType = new IDType();
        idType.setValue("idTypeValue");
        idType.setSchemeID("isSchemeId");
        vesselTransportMeans.setIDS(Collections.singletonList(idType));

        List<VesselTransportMeansFact> vesselTransportMeansFacts = ActivityFactMapper.INSTANCE.generateFactForVesselTransportMeans(Collections.singletonList(vesselTransportMeans));
        VesselTransportMeansFact mappedFact = vesselTransportMeansFacts.get(0);

        assertEquals(codeType.getValue(), mappedFact.getRoleCode().getValue());
        assertEquals(idType.getValue(), mappedFact.getIds().get(0).getValue());

    }
}
