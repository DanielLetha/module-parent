package com.test;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.biz.resources.vo.ProcurementVo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.error.DefaultError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.commons.test.TestClassWithLazyLoad;
import com.simpletour.domain.resources.*;
import com.simpletour.service.resources.IResourcesService;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * 文件描述：资源服务模块测试用例类
 * 创建人员：石广路
 * 创建日期：2015/11/25 10:57
 * 备注说明：null
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ResourcesTest extends TestClassWithLazyLoad {
    private long id;

    private long tid;

    @Resource
    IResourcesService resourcesService;


    @BeforeClass
    private void setTenantId(){
//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
    }

    private Destination addDestination() {
        Destination dest = null;
        DomainPage<Destination> destsList = resourcesService.getDestionationsByConditonPage("都江堰市", "四川省-成都市-都江堰市", 1, resourcesService.DEFAULT_PAGE_SIZE);

        if (0 == destsList.getDomainTotalCount()) {
            try {
                Area area = new Area();
                area.setId(510181L);

                dest = new Destination();
                dest = new Destination();
                dest.setTenantId(1L);
                dest.setArea(area);
                dest.setName("都江堰市");
                dest.setAddress("四川省-成都市-都江堰市");
                dest.setLon(new BigDecimal(121.982));
                dest.setLat(new BigDecimal(71.819));
                dest.setRemark("拜水都江堰");
                Optional<Destination> resDest = resourcesService.addDestination(dest);
                Assert.assertTrue(resDest.isPresent());
                dest = resDest.get();
                id = dest.getId();
                System.out.println("add Destination id=" + id + " successfully!");
            } catch (BaseSystemException e) {
                e.printStackTrace();
                Assert.fail("add Destination failed!");
            }
        } else {
            dest = destsList.getDomains().get(0);
        }

        Assert.assertNotNull(dest);

        return dest;
    }

    private void onlyCheckScenicCoordinate(Scenic scenic, Double lon, Double lat) {
        scenic.setLon(null == lon ? null : BigDecimal.valueOf(lon));
        scenic.setLat(null == lat ? null : BigDecimal.valueOf(lat));
        scenic.setDestination(null);

        try {
            resourcesService.addScenic(scenic);
        } catch (BaseSystemException bse) {
            String errorMsg = bse.getError().getErrorMessage();
            if (ResourcesBizError.INVALID_COORDINATE.getErrorMessage().equals(errorMsg)) {
                System.err.println("the Scenic coordinate is invalid!");
            } else {
                bse.printStackTrace();
                System.out.println("unable to add this Scenic, but its coordinate is valid!");
            }
        }
    }

    //@Ignore
    @Test(priority = 10)
    public void addScenic() {
        Scenic scenic = new Scenic();
        scenic.setName("青城山");  // 青城山青幽街
        scenic.setAddress("青城山-都江堰风景区");
        scenic.setRemark("成都周边");

        // only check those invalid scenics
        onlyCheckScenicCoordinate(scenic, 180.000, 90.000);
        onlyCheckScenicCoordinate(scenic, -180.002, 92.005);
        onlyCheckScenicCoordinate(scenic, 180.002, -92.005);
        onlyCheckScenicCoordinate(scenic, null, null);
        onlyCheckScenicCoordinate(scenic, 0.0, 0.0);

        // add a valid scenic
        scenic.setLon(new BigDecimal(121.982));
        scenic.setLat(new BigDecimal(71.819));
        scenic.setDestination(addDestination());

        try {
            Optional<Scenic> optScenic = resourcesService.addScenic(scenic);
            Assert.assertTrue(optScenic.isPresent());
            tid = optScenic.get().getId();
            Assert.assertNotEquals(0, tid);
            System.out.println("add Scenic id=" + tid + " successfully!");
        } catch (BaseSystemException e) {
            String errorMsg = e.getError().getErrorMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Scenic id=" + tid + " tenant id is not same one!");
            } else if (ResourcesBizError.RESOURCE_IS_EXISTING.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Scenic is existing!");
                tid = resourcesService.getScenicId(scenic);
                Assert.assertNotEquals(0, tid);
                System.out.println("the existing Scenic id=" + tid);
            } else {
                e.printStackTrace();
                Assert.fail("add Scenic failed!");
            }
        }
    }

    @Test(priority = 11)
    public void updateScenic() {
        //tid = 1; // just for testing
        System.out.println("update Scenic id=" + tid);

        Optional<Scenic> optScenic = resourcesService.getScenicById(tid);
        Assert.assertTrue(optScenic.isPresent());

        Scenic scenic = optScenic.get();
        String remark = scenic.getRemark();
        scenic.setTenantId(1L);
        //scenic.setName("白马王朗风"); // just for testing
        scenic.setLon(new BigDecimal(120.253));
        scenic.setLat(new BigDecimal(76.809));
        scenic.setRemark("大成都周边" + Math.abs((new Random(System.currentTimeMillis())).nextInt()) % 32 + "日游");

        try {
            Optional<Scenic> savedScenic = resourcesService.updateScenic(scenic);
            Assert.assertTrue(savedScenic.isPresent());
            Assert.assertNotEquals(remark, savedScenic.get().getRemark());
            System.out.println("update Scenic successfully!");
        } catch (BaseSystemException e) {
            String errorMsg = e.getError().getErrorMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Scenic id=" + tid + " tenant id is not same one!");
            } else if (ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Scenic id=" + tid + " with same name is existing!");
            } else {
                e.printStackTrace();
                Assert.fail("update Scenic failed!");
            }
        }
    }

    @Test(priority = 12)
    public void getScenics() {
        //List<Scenic> scenics = resourcesBiz.getResourcesListByDestId(Scenic.class, 21);

        Map conditions = new HashMap<String, String>();
        conditions.put("name", "青城山");
        conditions.put("address", "青城山-都江堰风景区");
        conditions.put("destination.name", "都江堰市");
        DomainPage<Scenic> scenicsList = resourcesService.queryScenicsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, false);
        Assert.assertNotNull(scenicsList);
        Assert.assertNotEquals(0, scenicsList.getDomainTotalCount());
        System.out.println("get Scenics complete!");
    }

//    @Test(priority = 13)
//    public void deleteScenic() {
//        //tid = 1;
//        System.out.println("delete Scenic id=" + tid);
//
//        try {
//            resourcesService.deleteScenic(tid, true);
//            Optional<Scenic> optScenic = resourcesService.getScenicById(tid);
//            Assert.assertTrue(optScenic.isPresent());
//
//            resourcesService.deleteScenic(tid, false);
//            optScenic = resourcesService.getScenicById(tid);
//            Assert.assertFalse(optScenic.isPresent());
//
//            System.out.println("delete Scenic complete!");
//        } catch (BaseSystemException e) {
//            String errorMsg = e.getError().getErrorMessage();
//            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
//                System.out.println("the Scenic id=" + tid + " tenant id is not same one!");
//            } else if (ResourcesBizError.CANNOT_DEL_DEPENDENT_RESOURCE.getErrorMessage().equals(errorMsg)) {
//                System.out.println("cannot delete the dependent resource by procurement!");
//            } else {
//                e.printStackTrace();
//                Assert.fail("delete Stock failed!");
//            }
//        }
//    }


    @Test(priority = 14)
    public void addCatering() {
        Catering catering = new Catering();
        //catering.setName("黄哈达烤羊--豪华藏餐");
        //catering.setRemark("臧家风情");
        catering.setName("星宇酒店冬季滋补羊肉汤锅1份（2人食用）");
        catering.setRemark("冬季养生汤锅");
        catering.setType(Catering.Type.hotel);
        catering.setAddress("都江堰市");
        catering.setDestination(addDestination());
        catering.setLon(new BigDecimal(178.1261));
        catering.setLat(new BigDecimal(87.1693));

        try {
            Optional<Catering> optCatering = resourcesService.addCatering(catering);
            Assert.assertTrue(optCatering.isPresent());
            tid = optCatering.get().getId();
            Assert.assertNotEquals(0, tid);
            System.out.println("add Catering id=" + tid + " successfully!");
        } catch (BaseSystemException e) {
            String errorMsg = e.getError().getErrorMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Catering id=" + tid + " tenant id is not same one!");
            } else if (ResourcesBizError.RESOURCE_IS_EXISTING.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Catering is existing!");
                tid = resourcesService.getCateringId(catering);
                Assert.assertNotEquals(0, tid);
                System.out.println("the existing Catering id=" + tid);
            } else {
                e.printStackTrace();
                Assert.fail("add Catering failed!");
            }
        }
    }

    @Test(priority = 15)
    public void updateCatering() {
        //tid = 15;   // just for testing
        System.out.println("delete Catering id=" + tid);

        Optional<Catering> optCatering = resourcesService.getCateringById(tid);
        Assert.assertTrue(optCatering.isPresent());

        Catering catering = optCatering.get();
        String remark = catering.getRemark();
        catering.setTenantId(1L);
        catering.setLon(new BigDecimal(120.253));
        catering.setLat(new BigDecimal(76.809));
        catering.setRemark("营业时间：9:00~23:00");

        try {
            Optional<Catering> savedCatering = resourcesService.updateCatering(catering);
            Assert.assertTrue(savedCatering.isPresent());
            Assert.assertNotEquals(remark, savedCatering.get().getRemark());
            System.out.println("update Catering successfully!");
        } catch (BaseSystemException e) {
            String errorMsg = e.getError().getErrorMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Catering id=" + tid + " tenant id is not same one!");
            } else if (ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Catering id=" + tid + " with same name is existing!");
            } else {
                e.printStackTrace();
                Assert.fail("update Catering failed!");
            }
        }
    }

    @Test(priority = 16)
    public void getCaterings() {
        Map conditions = new HashMap<String, Object>(3);
        conditions.put("type", Catering.Type.hotel);
        conditions.put("address", "都江堰市");
        conditions.put("destination.name", "都江堰市");
        DomainPage<Catering> cateringsList = resourcesService.queryCateringsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, false);
        Assert.assertNotNull(cateringsList);
        Assert.assertNotEquals(0, cateringsList.getDomainTotalCount());
        System.out.println("get Caterings complete!");
    }

    @Test(priority = 17)
    public void deleteCatering() {
        //tid = 1;
        System.out.println("delete Catering id=" + tid);

        try {
            resourcesService.deleteCatering(tid, true);
            Optional<Catering> optCatering = resourcesService.getCateringById(tid);
            Assert.assertTrue(optCatering.isPresent());

            resourcesService.deleteCatering(tid, false);
            optCatering = resourcesService.getCateringById(tid);
            Assert.assertFalse(optCatering.isPresent());

            System.out.println("delete Catering complete!");
        } catch (BaseSystemException e) {
            String errorMsg = e.getError().getErrorMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Catering id=" + tid + " tenant id is not same one!");
            } else if (ResourcesBizError.CANNOT_DEL_DEPENDENT_RESOURCE.getErrorMessage().equals(errorMsg)) {
                System.out.println("cannot delete the dependent resource by procurement!");
            } else {
                e.printStackTrace();
                Assert.fail("delete Catering failed!");
            }
        }
    }

//    @Test(priority = 18)
//    public void addHotel() {
//        Hotel hotel = new Hotel();
//        //hotel.setName("都江堰喜临门酒店");
//        //hotel.setName("都江堰星宇国际大酒店");
//        hotel.setName("都江堰喜如家酒店");
//        hotel.setAddress("都江堰市");
//        hotel.setType(Hotel.StayType.hotel);
//        hotel.setDestination(addDestination());
//        hotel.setLon(new BigDecimal(161.16));
//        hotel.setLat(new BigDecimal(31.48));
//
//        try {
//            Optional<Hotel> optHotel = resourcesService.addHotel(hotel);
//            Assert.assertTrue(optHotel.isPresent());
//            tid = optHotel.get().getId();
//            Assert.assertNotEquals(0, tid);
//            System.out.println("add Hotel id=" + tid + " successfully!");
//        } catch (BaseSystemException e) {
//            String errorMsg = e.getError().getErrorMessage();
//            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
//                System.out.println("the Hotel id=" + tid + " tenant id is not same one!");
//            } else if (ResourcesBizError.RESOURCE_IS_EXISTING.getErrorMessage().equals(errorMsg)) {
//                System.out.println("the Hotel is existing!");
//                tid = resourcesService.getHotelId(hotel);
//                Assert.assertNotEquals(0, tid);
//                System.out.println("the existing Hotel id=" + tid);
//            } else {
//                e.printStackTrace();
//                Assert.fail("add Hotel failed!");
//            }
//        }
//    }
//
//    @Test(priority = 19)
//    public void updateHotel() {
//        //tid = 4;   // just for testing
//        System.out.println("delete Hotel id=" + tid);
//
//        Optional<Hotel> optHotel = resourcesService.getHotelById(tid);
//        Assert.assertTrue(optHotel.isPresent());
//
//        Hotel hotel = optHotel.get();
//        String remark = hotel.getRemark();
//        hotel.setTenantId(1L);
//        hotel.setLon(new BigDecimal(164.12));
//        hotel.setLat(new BigDecimal(33.18));
//
//        hotel.setRemark("营业时间：" + DateFormat.getTimeInstance().format(new Date()));
//
//        try {
//            Optional<Hotel> savedHotel = resourcesService.updateHotel(hotel);
//            Assert.assertTrue(savedHotel.isPresent());
//            Assert.assertNotEquals(remark, savedHotel.get().getRemark());
//            System.out.println("update Hotel successfully!");
//        } catch (BaseSystemException e) {
//            String errorMsg = e.getError().getErrorMessage();
//            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
//                System.out.println("the Hotel id=" + tid + " tenant id is not same one!");
//            } else if (ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING.getErrorMessage().equals(errorMsg)) {
//                System.out.println("the Hotel id=" + tid + " with same name is existing!");
//            } else {
//                e.printStackTrace();
//                Assert.fail("update Hotel failed!");
//            }
//        }
//    }
//
//    @Test(priority = 20)
//    public void getHotels() {
//        Map conditions = new HashMap<String, Object>(3);
//        conditions.put("type", Hotel.StayType.hotel); // hotel
//        conditions.put("address", "都江堰市");
//        conditions.put("destination.name", "都江堰市");
//        DomainPage<Hotel> hotelsList = resourcesService.queryHotelsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, false);
//        Assert.assertNotNull(hotelsList);
//        Assert.assertNotEquals(0, hotelsList.getDomainTotalCount());
//        System.out.println("get Hotels complete!");
//    }
//
//    @Test(priority = 21)
//    public void deleteHotel() {
//        //tid = 4;
//        System.out.println("delete Hotel id=" + tid);
//
//        try {
//            resourcesService.deleteHotel(tid, true);
//            Optional<Hotel> optHotel = resourcesService.getHotelById(tid);
//            Assert.assertTrue(optHotel.isPresent());
//
//            resourcesService.deleteHotel(tid, false);
//            optHotel = resourcesService.getHotelById(tid);
//            Assert.assertFalse(optHotel.isPresent());
//
//            System.out.println("delete Hotel complete!");
//        } catch (BaseSystemException e) {
//            String errorMsg = e.getError().getErrorMessage();
//            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
//                System.out.println("the Hotel id=" + tid + " tenant id is not same one!");
//            } else if (ResourcesBizError.CANNOT_DEL_DEPENDENT_RESOURCE.getErrorMessage().equals(errorMsg)) {
//                System.out.println("cannot delete the dependent resource by procurement!");
//            } else {
//                e.printStackTrace();
//                Assert.fail("delete Hotel failed!");
//            }
//        }
//    }

    private Scenic bindResource() {
        Destination dest = null;
        Scenic scenic = null;
        DomainPage<Destination> destsList = resourcesService.getDestionationsByConditonPage("白马王朗", "四川省-绵阳市-平武县",1, resourcesService.DEFAULT_PAGE_SIZE);

        if (0 == destsList.getDomainTotalCount()) {
            try {
                Area area = new Area();
                area.setId(510727L);
                dest = new Destination();
                dest.setTenantId(2L);
                dest.setArea(area);
                dest.setName("白马王朗"+DestinationTest.generateName());
                dest.setAddress("四川省-绵阳市-平武县");
                dest.setLon(new BigDecimal(127.982));
                dest.setLat(new BigDecimal(75.819));
                dest.setRemark("国家级自然景区");
                Optional<Destination> resDest = resourcesService.addDestination(dest);
                Assert.assertEquals(resDest.isPresent(), true);
                dest = resDest.get();
            } catch (BaseSystemException e) {
                e.printStackTrace();
                Assert.fail("add Destination failed!");
            }
        } else {
            dest = destsList.getDomains().get(0);
            Assert.assertNotNull(dest);
        }

        Map conditions = new HashMap<String, String>(2);
        conditions.put("name", "白马王朗风国家级自然景区");
        conditions.put("address", "四川省-绵阳市-平武县");
        DomainPage<Scenic> scenicsList = resourcesService.queryScenicsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, false);
        Assert.assertNotNull(scenicsList);

        if (0 == scenicsList.getDomainTotalCount()) {
            scenic = new Scenic();
            scenic.setName("白马王朗风国家级自然景区");
            scenic.setAddress(dest.getAddress());
            scenic.setRemark("绵阳市旅游景点");
            scenic.setLon(new BigDecimal(127.982));
            scenic.setLat(new BigDecimal(75.819));
            scenic.setDestination(dest);

            try {
                Optional<Scenic> optScenic = resourcesService.addScenic(scenic);
                Assert.assertTrue(optScenic.isPresent());
                scenic = optScenic.get();
            } catch (BaseSystemException e) {
                e.printStackTrace();
                Assert.fail("add Scenic failed!");
            }
        } else {
            scenic = scenicsList.getDomains().get(0);
        }

        Assert.assertNotNull(scenic);

        return scenic;
    }

    private OfflineServiceProvider getServiceProvider() {
        OfflineServiceProvider offlineServiceProvider = null;
        DomainPage<OfflineServiceProvider> ospsList = resourcesService.getOfflineServiceProvidersByConditionPage("中国青旅", null, 1, resourcesService.DEFAULT_PAGE_SIZE);

        if (0 == ospsList.getDomainTotalCount()) {
            offlineServiceProvider = new OfflineServiceProvider();
            offlineServiceProvider.setName("中国青旅");
            offlineServiceProvider.setTenantId(2L);
            offlineServiceProvider.setRemark("中国青旅国际旅行社");
            try {
                Optional<OfflineServiceProvider> res = resourcesService.addOfflineServiceProvider(offlineServiceProvider);
                Assert.assertTrue(res.isPresent());
                offlineServiceProvider = res.get();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Assert.fail("add OfflineServiceProvider failed!");
            }
        } else {
            offlineServiceProvider = ospsList.getDomains().get(0);
            Assert.assertNotNull(offlineServiceProvider);
        }

        return offlineServiceProvider;
    }

    @Test(priority = 22)
    public void addProcurement() {
        Procurement procurement = new Procurement();
        procurement.setTenantId(2L);
        procurement.setOnline(true);
        procurement.setRemark("上线景点");
        procurement.setOsp(getServiceProvider());

        // 添加景点
        Scenic scenic = bindResource();
        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setResourceId(scenic.getId());
        procurement.setName("白马王朗景区门票1张");    // 白马王朗景区观光车票1张 白马王朗景区美食票1张 白马王朗景区门票1张
        Destination destination = scenic.getDestination();
        procurement.setDestination(destination);


        // 添加住宿点
//        Optional<Hotel> optHotel = resourcesService.getHotelById(3);
//        Assert.assertTrue(optHotel.isPresent());
//        Hotel hotel = optHotel.get();
//        procurement.setResourceType(Procurement.ResourceType.hotel);
//        procurement.setResourceId(hotel.getId());
//        procurement.setName("都江堰喜临门酒店");
//        procurement.setDestination(hotel.getDestination());

        // 添加餐饮点
//        Optional<Catering> optCatering = resourcesService.getCateringById(1);
//        Assert.assertTrue(optCatering.isPresent());
//        Catering catering = optCatering.get();
//        procurement.setResourceType(Procurement.ResourceType.catering);
//        procurement.setResourceId(catering.getId());
//        procurement.setName("黄哈达烤羊--豪华藏餐");
//        procurement.setDestination(catering.getDestination());

        try {
            Optional<Procurement> optProcurement = resourcesService.addProcurement(procurement);
            Assert.assertTrue(optProcurement.isPresent());
            tid = optProcurement.get().getId();
            Assert.assertNotEquals(0, tid);
            System.out.println("add Procurement id=" + tid + " successfully!");
        } catch (BaseSystemException e) {
            String errorMsg = e.getError().getErrorMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Procurement id=" + tid + " tenant id is not same one!");
            } else if (ResourcesBizError.RESOURCE_IS_EXISTING.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Procurement is existing!");
                Map<String, Object> conditions = new HashMap(4);
                conditions.put("resourceType", procurement.getResourceType());
                conditions.put("online", true);
                conditions.put("name", procurement.getName());
                conditions.put("destination.name", destination.getName());
                DomainPage<Procurement> procurementsList = resourcesService.queryProcurementsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, false);
                Assert.assertNotNull(procurementsList);
                procurement = procurementsList.getDomains().get(0);
                tid = procurement.getId();
                Assert.assertNotEquals(0, tid);
                System.out.println("the existing Procurement id=" + tid);
            } else {
                e.printStackTrace();
                Assert.fail("add Procurement failed!");
            }
        }
    }

    @Test(priority = 23)
    public void updateProcurement() {
        //tid = 5;   // just for testing
        System.out.println("update Procurement id=" + tid);

        Optional<Procurement> optProcurement = resourcesService.getProcurementById(tid);
        Assert.assertTrue(optProcurement.isPresent());

        Procurement procurement = optProcurement.get();
        String remark = procurement.getRemark();
        procurement.setRemark("景点上线时间：2015-11-23 15:19");
        //procurement.setName("白马王朗景区美食票1张");   // just for testing

        try {
            Optional<Procurement> savedProcurement = resourcesService.updateProcurement(procurement);
            Assert.assertTrue(savedProcurement.isPresent());
            Assert.assertNotEquals(remark, savedProcurement.get().getRemark());
            System.out.println("update Procurement successfully!");
        } catch (BaseSystemException e) {
            String errorMsg = e.getError().getErrorMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Procurement id=" + tid + " tenant id is not same one!");
            } else if (ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Procurement id=" + tid + " with same name is existing!");
            } else {
                e.printStackTrace();
                Assert.fail("update Procurement failed!");
            }
        }
    }

    @Test(priority = 24)
    public void queryProcurementsPagesByConditions() {
        Map<String, Object> conditions = new HashMap(4);
        conditions.put("resourceType", Procurement.ResourceType.scenic);
        conditions.put("online", true);
        conditions.put("name", "白马王朗景区门票1张");
        conditions.put("destination.name", "白马王朗");
        DomainPage<Procurement> procurementsList = resourcesService.queryProcurementsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, false);
        Assert.assertNotNull(procurementsList);

        conditions.put("name", "白马王朗景区");
        conditions.put("del", null);
        procurementsList = resourcesService.queryProcurementsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, true);
        Assert.assertNotNull(procurementsList);
    }

    @Test(priority = 25)
    public void queryProcurementsPages() {
        Map<String, Object> conditions = new HashMap(4);
        conditions.put("resourceType", "scenic");
        conditions.put("online", true);
        conditions.put("name", "白马王朗景区门票1张");
        conditions.put("destination.name", "白马王朗");
//        conditions.put("resourceName", "test");
        DomainPage<ProcurementVo> procurementsList = resourcesService.queryProcurementVoPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE);
        Assert.assertNotNull(procurementsList);

        conditions.put("name", "白马王朗景区");
//        conditions.put("del", null);
        procurementsList = resourcesService.queryProcurementVoPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE);
        Assert.assertNotNull(procurementsList);
    }

    @Test(priority = 26)
    public void deleteProcurement() {
        //tid = 29;   // just for testing
        System.out.println("delete Procurement id=" + tid);

        try {
            resourcesService.deleteProcurement(tid);
            Optional<Procurement> optProcurement = resourcesService.getProcurementById(tid);
            Assert.assertTrue(optProcurement.isPresent());

            resourcesService.deleteProcurement(tid);
            optProcurement = resourcesService.getProcurementById(tid);
            Assert.assertFalse(optProcurement.isPresent());
        } catch (BaseSystemException e) {
            String errorMsg = e.getError().getErrorMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Procurement id=" + tid + " tenant id is not same one!");
            } else if (ResourcesBizError.CANNOT_DEL_DEPENDENT_PROCUREMENT.getErrorMessage().equals(errorMsg)) {
                System.out.println("the Procurement is depended by stock!");
                Assert.assertTrue(resourcesService.getProcurementById(tid).isPresent());
            } else {
                e.printStackTrace();
                Assert.fail("delete Procurement failed!");
            }
        }

        System.out.println("delete Procurement complete!");
    }


}
