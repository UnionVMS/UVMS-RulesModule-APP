package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class AssetServiceBeanTest {

    private RulesAssetServiceBean assetServiceBean;

    @Before
    public void setUp() throws Exception {
        assetServiceBean = new RulesAssetServiceBean();
    }

    @Test
    public void assetComparator() {
        Date first = DateTime.parse("2018-04-10").toDate();
        Date second = DateTime.parse("2015-04-10").toDate();
        Date third = DateTime.parse("2010-04-10").toDate();
        List<AssetDTO> assets = getAssets(first, third, second);
        assets.sort(assetServiceBean.assetComparator());

        assertEquals(OffsetDateTime.ofInstant(first.toInstant(), ZoneId.systemDefault()), assets.get(0).getUpdateTime());
        assertEquals(OffsetDateTime.ofInstant(second.toInstant(), ZoneId.systemDefault()), assets.get(1).getUpdateTime());
        assertEquals(OffsetDateTime.ofInstant(third.toInstant(), ZoneId.systemDefault()), assets.get(2).getUpdateTime());
    }

    @Test
    public void findAssetHistoryByDate() throws Exception {
        Date first = DateTime.parse("2018-04-10").toDate();
        Date second = DateTime.parse("2015-04-10").toDate();
        Date third = DateTime.parse("2010-04-10").toDate();

        List<AssetDTO> assets = getAssets(first, second, third);

        OffsetDateTime offsetDateTimeSecond = OffsetDateTime.ofInstant(second.toInstant(), ZoneId.systemDefault());
        OffsetDateTime offsetDateTimeThird = OffsetDateTime.ofInstant(third.toInstant(), ZoneId.systemDefault());

        assertEquals(offsetDateTimeSecond, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2016-04-10").toDate(), assets).get().getUpdateTime());
        assertEquals(offsetDateTimeThird, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2012-04-10").toDate(), assets).get().getUpdateTime());
        assertEquals(offsetDateTimeThird, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2013-04-10").toDate(), assets).get().getUpdateTime());
        assertEquals(offsetDateTimeThird, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2014-04-10").toDate(), assets).get().getUpdateTime());
        assertEquals(offsetDateTimeThird, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2015-04-09").toDate(), assets).get().getUpdateTime());

    }

    @Test
    public void findAssetHistoryByDateWhenNoResult() throws Exception {
        Date landingDate = DateTime.parse("2000-04-10").toDate();

        Date first = DateTime.parse("2018-04-10").toDate();
        Date second = DateTime.parse("2015-04-10").toDate();
        Date third = DateTime.parse("2010-04-10").toDate();

        List<AssetDTO> assets = getAssets(first, second, third);

        Optional<AssetDTO> assetHistoryByDate = assetServiceBean.findAssetHistoryByDate(landingDate, assets);

        assertFalse(assetHistoryByDate.isPresent());
    }

    @Test
    public void findAssetHistoryByDateWhenLandingDateIsAfterFirstResult() throws Exception {
        Date landingDate = DateTime.parse("2019-04-10").toDate();

        Date first = DateTime.parse("2018-04-10").toDate();
        Date second = DateTime.parse("2015-04-10").toDate();
        Date third = DateTime.parse("2010-04-10").toDate();

        List<AssetDTO> assets = getAssets(first, second, third);

        Optional<AssetDTO> assetHistoryByDate = assetServiceBean.findAssetHistoryByDate(landingDate, assets);

        assertTrue(assetHistoryByDate.isPresent());
        assertEquals(OffsetDateTime.ofInstant(first.toInstant(), ZoneId.systemDefault()), assetHistoryByDate.get().getUpdateTime());
    }

    protected List<AssetDTO> getAssets(Date first, Date second, Date third) {

        AssetDTO asset1 = new AssetDTO();
        asset1.setVesselDateOfEntry(OffsetDateTime.ofInstant(first.toInstant(), ZoneId.systemDefault()));


        AssetDTO asset2 = new AssetDTO();
        asset2.setVesselDateOfEntry(OffsetDateTime.ofInstant(second.toInstant(), ZoneId.systemDefault()));

        AssetDTO asset3 = new AssetDTO();
        asset3.setVesselDateOfEntry(OffsetDateTime.ofInstant(third.toInstant(), ZoneId.systemDefault()));

        return Arrays.asList(asset1, asset2, asset3);
    }
}