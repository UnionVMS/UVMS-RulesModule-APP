package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import org.junit.Before;

public class AssetServiceBeanTest {

//    private AssetServiceBean assetServiceBean;

    @Before
    public void setUp() throws Exception {
//        assetServiceBean = new AssetServiceBean();
    }
//
//    @Test
//    public void assetComparator() {
//        Date first = DateTime.parse("2018-04-10").toDate();
//        Date second = DateTime.parse("2015-04-10").toDate();
//        Date third = DateTime.parse("2010-04-10").toDate();
//
//
//        List<Asset> assets = getAssets(first, third, second);
//
//        assets.sort(assetServiceBean.assetComparator());
//
//        assertEquals(first, assets.get(0).getEventHistory().getEventDate());
//        assertEquals(second, assets.get(1).getEventHistory().getEventDate());
//        assertEquals(third, assets.get(2).getEventHistory().getEventDate());
//    }
//
//    @Test
//    public void findAssetHistoryByDate() throws Exception {
//        Date first = DateTime.parse("2018-04-10").toDate();
//        Date second = DateTime.parse("2015-04-10").toDate();
//        Date third = DateTime.parse("2010-04-10").toDate();
//
//        List<Asset> assets = getAssets(first, second, third);
//
//        assertEquals(second, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2016-04-10").toDate(), assets).get().getEventHistory().getEventDate());
//        assertEquals(third, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2012-04-10").toDate(), assets).get().getEventHistory().getEventDate());
//        assertEquals(third, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2013-04-10").toDate(), assets).get().getEventHistory().getEventDate());
//        assertEquals(third, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2014-04-10").toDate(), assets).get().getEventHistory().getEventDate());
//        assertEquals(third, assetServiceBean.findAssetHistoryByDate(DateTime.parse("2015-04-09").toDate(), assets).get().getEventHistory().getEventDate());
//
//    }
//
//    @Test
//    public void findAssetHistoryByDateWhenNoResult() throws Exception {
//        Date landingDate = DateTime.parse("2000-04-10").toDate();
//
//        Date first = DateTime.parse("2018-04-10").toDate();
//        Date second = DateTime.parse("2015-04-10").toDate();
//        Date third = DateTime.parse("2010-04-10").toDate();
//
//        List<Asset> assets = getAssets(first, second, third);
//
//        Optional<Asset> assetHistoryByDate = assetServiceBean.findAssetHistoryByDate(landingDate, assets);
//
//        assertFalse(assetHistoryByDate.isPresent());
//    }
//
//    @Test
//    public void findAssetHistoryByDateWhenLandingDateIsAfterFirstResult() throws Exception {
//        Date landingDate = DateTime.parse("2019-04-10").toDate();
//
//        Date first = DateTime.parse("2018-04-10").toDate();
//        Date second = DateTime.parse("2015-04-10").toDate();
//        Date third = DateTime.parse("2010-04-10").toDate();
//
//        List<Asset> assets = getAssets(first, second, third);
//
//        Optional<Asset> assetHistoryByDate = assetServiceBean.findAssetHistoryByDate(landingDate, assets);
//
//        assertTrue(assetHistoryByDate.isPresent());
//        assertEquals(first, assetHistoryByDate.get().getEventHistory().getEventDate());
//    }
//
//    protected List<Asset> getAssets(Date first, Date second, Date third) {
//        AssetHistoryId assetHistoryId1 = new AssetHistoryId();
//        assetHistoryId1.setEventDate(first);
//
//        AssetHistoryId assetHistoryId2 = new AssetHistoryId();
//        assetHistoryId2.setEventDate(second);
//
//        AssetHistoryId assetHistoryId3 = new AssetHistoryId();
//        assetHistoryId3.setEventDate(third);
//
//
//        Asset asset1 = new Asset();
//        asset1.setEventHistory(assetHistoryId1);
//
//
//        Asset asset2 = new Asset();
//        asset2.setEventHistory(assetHistoryId2);
//
//        Asset asset3 = new Asset();
//        asset3.setEventHistory(assetHistoryId3);
//
//        return Arrays.asList(asset1, asset2, asset3);
//    }
}