package com.simpletour.biz.resources;


import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.biz.resources.vo.ProcurementVo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Brief : 元素Biz层测试
 * Author: Hawk
 * Date  : 2016/3/24
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ProcurementBizTest extends AbstractTransactionalTestNGSpringContextTests {

    @Resource
    private IOspBiz ospBiz;
    @Resource
    private IHotelBiz hotelBiz;
    @Resource
    private IScenicBiz scenicBiz;
    @Resource
    private ICateringBiz cateringBiz;
    @Resource
    private IResourcesDao resourcesDao;
    @Resource
    private IDestinationBiz destinationBiz;
    @Resource
    private IProcurementBiz procurementBiz;
    @Resource
    private IEntertainmentBiz entertainmentBiz;

    private Area area = null;
    private Destination destination = null;
    private Hotel hotel = null;
    private Scenic scenic = null;
    private Catering catering = null;
    private Entertainment entertainment = null;
    private OfflineServiceProvider osp = null;


    @BeforeClass
    public void setup() {
//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);

        area = new Area();
        area.setId(110101L);

        Destination tempDestination = generateNewDestination();
        destination = destinationBiz.addDestination(tempDestination);
        Assert.assertNotNull(destination);

        Hotel tempHotel = generateNewHotel();
        hotel = hotelBiz.addHotel(tempHotel);
        Assert.assertNotNull(hotel);

        Catering tempCatering = generateNewCatering();
        catering = cateringBiz.addCatering(tempCatering);
        Assert.assertNotNull(catering);

        Scenic tempScenic = generateNewScenic();
        scenic = scenicBiz.addScenic(tempScenic);
        Assert.assertNotNull(scenic);

        Entertainment tempEntertainment = generateNewEntertainment();
        entertainment = entertainmentBiz.addEntertainment(tempEntertainment);
        Assert.assertNotNull(entertainment);

        OfflineServiceProvider tempOsp = generateNewOsp();
        osp = ospBiz.addOfflineServiceProvider(tempOsp);
        Assert.assertNotNull(osp);
    }

    @AfterClass
    public void teardown() {
        removeCatchException(osp);
        removeCatchException(hotel);
        removeCatchException(entertainment);
        removeCatchException(scenic);
        removeCatchException(catering);
        removeCatchException(destination);
    }

    /**
     * 测试正常情况下的，增加元素
     */
    @Test
    public void testAddProcurement() {
        Procurement cateringProcurement = generateCateringProcurement();
        Assert.assertNotNull(procurementBiz.addProcurement(cateringProcurement));

        Procurement hotelProcurement = generateHotelProcurement();
        Assert.assertNotNull(procurementBiz.addProcurement(hotelProcurement));

        Procurement entertainmentProcuremnt = generateEntertainmentProcurement();
        Assert.assertNotNull(procurementBiz.addProcurement(entertainmentProcuremnt));

        Procurement scenicProcurement = generateScenicProcurement();
        Assert.assertNotNull(procurementBiz.addProcurement(scenicProcurement));
    }

    /**
     * 测试增加元素时，元素为null
     */
    @Test
    public void testAddProcurementAndNull() {
        try {
            procurementBiz.addProcurement(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.PROCUREMENT_NULL, e.getError());
        }
    }

    /**
     * 测试增加元素时，元素的del字段为true
     */
    @Test
    public void testAddProcurementAndDel() {
        Procurement cateringProcurement = generateCateringProcurement();
        cateringProcurement.setDel(Boolean.TRUE);
        try {
            procurementBiz.addProcurement(cateringProcurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.PROCUREMENT_ADD_AND_DEL, e.getError());
        }
    }

    /**
     * 测试增加的元素时，元素中的destination与资源中的destination不一致
     */
    @Test
    public void testAddProcurementAndDestinationNotConsistency() {

        Destination newDestination = generateNewDestination();
        Destination dbDestination = destinationBiz.addDestination(newDestination);
        Assert.assertNotNull(dbDestination);

        Procurement cateringProcurement = generateCateringProcurement();
        cateringProcurement.setDestination(dbDestination);
        try {
            procurementBiz.addProcurement(cateringProcurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.PROCUREMENT_RESOURCE_DESTINATION_NOMATCH, e.getError());
        }
    }

    /**
     * 测试添加元素时，同一个目的地下的元素名一样
     */
    @Test
    public void testAddProcurementAndNameRepeatOnOneDestination() {

        String tempName = generateString("test-procurement-name");

        Procurement procurement = generateCateringProcurement();
        procurement.setName(tempName);
        Procurement dbProcurement = procurementBiz.addProcurement(procurement);
        Assert.assertNotNull(dbProcurement);
        try {
            procurementBiz.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.PROCUREMENT_NAME_REPEAT_ON_ONE_DESTINATION, e.getError());
        }
    }

    /**
     * 测试正常情况下的，更新元素
     */
    @Test
    public void testUpdateProcurement() {
        Procurement tempProcurement = generateScenicProcurement();
        Procurement dbProcurement = procurementBiz.addProcurement(tempProcurement);
        Assert.assertNotNull(dbProcurement);

        String tempStr = generateString("qewqweqweoaisdfasdl124123");

        OfflineServiceProvider newOsp = generateNewOsp();
        OfflineServiceProvider dbNewOsp = ospBiz.addOfflineServiceProvider(newOsp);
        Assert.assertNotNull(dbNewOsp);

        Destination newDestination = generateNewDestination();
        Destination dbNewDestination = destinationBiz.addDestination(newDestination);
        Assert.assertNotNull(dbNewDestination);

        Hotel newHotel = generateNewHotel();
        newHotel.setDestination(dbNewDestination);
        Hotel dbHotel = hotelBiz.addHotel(newHotel);
        Assert.assertNotNull(dbHotel);

        tempProcurement.setName(tempStr);
        tempProcurement.setRemark(tempStr);
        tempProcurement.setId(dbProcurement.getId());
        tempProcurement.setOnline(Boolean.FALSE);
        tempProcurement.setResourceId(dbHotel.getId());
        tempProcurement.setResourceType(Procurement.ResourceType.hotel);
        tempProcurement.setOsp(dbNewOsp);
        tempProcurement.setDestination(dbNewDestination);
        tempProcurement.setVersion(dbProcurement.getVersion());
        Procurement dbNewProcurement = procurementBiz.updateProcurement(tempProcurement);
        Assert.assertNotNull(dbNewProcurement);

        Assert.assertEquals(dbNewProcurement.getDestination(), dbNewDestination);
        Assert.assertEquals(dbNewProcurement.getOsp(), dbNewOsp);
        Assert.assertEquals(dbNewProcurement.getOnline(), Boolean.FALSE);
        Assert.assertEquals(dbNewProcurement.getResourceId(), dbHotel.getId());
        Assert.assertEquals(dbNewProcurement.getResourceType(), Procurement.ResourceType.hotel);
        Assert.assertEquals(dbNewProcurement.getName(), tempStr);
        Assert.assertEquals(dbNewProcurement.getRemark(), tempStr);
    }

    /**
     * 测试更新元素时, 元素为null
     */
    @Test
    public void testUpdateProcurementAndNull() {
        try {
            procurementBiz.updateProcurement(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.PROCUREMENT_NULL, e.getError());
        }
    }

    /**
     * 测试更新元素时, 元素Id为null
     */
    @Test
    public void testUpdateProcurementAndIdNull() {
        Procurement procurement = generateScenicProcurement();
        try {
            procurementBiz.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.ILLEGAL_ID, e.getError());
        }
    }

    @Test
    public void testUpdateProcurementAndVersionNull() {
        Procurement procurement = generateScenicProcurement();
        Procurement dbProcurement = procurementBiz.addProcurement(procurement);
        Assert.assertNotNull(dbProcurement);
        try {
            dbProcurement.setVersion(null);
            procurementBiz.updateProcurement(dbProcurement);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.PROCUREMENT_UPDATE_VERSION_NULL, e.getError());
        }
    }


    /**
     * 测试更新元素时，资源的destination与元素的destination不一致
     */
    @Test
    public void testUpdateProcurementAndTypeAndIdNotConsistency() {
        Procurement tempProcurement = generateScenicProcurement();
        Procurement dbProcurement = procurementBiz.addProcurement(tempProcurement);
        Assert.assertNotNull(dbProcurement);

        Destination newDestination = generateNewDestination();
        Destination dbNewDestination = destinationBiz.addDestination(newDestination);
        Assert.assertNotNull(dbNewDestination);

        dbProcurement.setDestination(dbNewDestination);
        try {
            procurementBiz.updateProcurement(dbProcurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.PROCUREMENT_RESOURCE_DESTINATION_NOMATCH, e.getError());
        }
    }

    /**
     * 测试更新元素时，同一个目的地下的元素名一样
     */
    @Test
    public void testUpdateProcurementAndNameRepeatOnOneDestination() {

        String tempName = generateString("test-procurement-name");

        Procurement procurement1 = generateCateringProcurement();
        Procurement dbProcurement1 = procurementBiz.addProcurement(procurement1);
        Assert.assertNotNull(dbProcurement1);

        Procurement procurement2 = generateCateringProcurement();
        procurement2.setName(tempName);
        Procurement dbProcurement2 = procurementBiz.addProcurement(procurement2);
        Assert.assertNotNull(dbProcurement2);

        procurement1.setId(dbProcurement1.getId());
        procurement1.setVersion(dbProcurement1.getVersion());
        procurement1.setName(tempName);

        try {
            procurementBiz.updateProcurement(procurement1);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.PROCUREMENT_NAME_REPEAT_ON_ONE_DESTINATION, e.getError());
        }
    }

    /**
     * 测试正常情况下的，删除元素
     */
    @Test
    public void testDeleteProcurement() {
        Procurement tempProcurement = generateScenicProcurement();
        Procurement dbProcurement = procurementBiz.addProcurement(tempProcurement);
        Assert.assertNotNull(dbProcurement);

        procurementBiz.deleteProcurement(dbProcurement.getId());

        Procurement afterProcurement = resourcesDao.getEntityById(Procurement.class, dbProcurement.getId());
        Assert.assertNotNull(afterProcurement);
        Assert.assertTrue(afterProcurement.getDel());
    }

    /**
     * 测试正常情况下的，根据元素id，获取元素对象
     */
    @Test
    public void testGetProcurementById() {
        Procurement tempProcurement = generateScenicProcurement();
        Procurement dbProcurement = procurementBiz.addProcurement(tempProcurement);
        Assert.assertNotNull(procurementBiz.getProcurementById(dbProcurement.getId()));
    }

    /**
     * 测试正常情况下的，多条件查询，返回元素对象列表
     */
    @Test
    public void testFindProcurementsByConditions() {
        String tempName = generateString("test-procurement-name");
        String tempRemark = generateString("test-procurement-remark");

        Procurement procurement = new Procurement();
        procurement.setName(tempName);
        procurement.setRemark(tempRemark);
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.catering);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(catering.getId());

        Procurement dbProcurement = procurementBiz.addProcurement(procurement);
        Assert.assertNotNull(dbProcurement);

        Map<String, Object> params = new HashMap<>();
        params.put("name", tempName);
        params.put("remark", tempRemark);
        params.put("osp", osp);
        params.put("resourceType", Procurement.ResourceType.catering);
        params.put("tenantId", 1L);
        params.put("del", Boolean.FALSE);
        params.put("online", Boolean.TRUE);
        params.put("resourceId", catering.getId());
        List<Procurement> procurementList = procurementBiz.findProcurementsByConditions(params);
        Assert.assertTrue(procurementList != null && (!procurementList.isEmpty()) && procurementList.size() == 1);

        Procurement queriedProcurement = procurementList.get(0);
        Assert.assertEquals(queriedProcurement.getName(), tempName);
        Assert.assertEquals(queriedProcurement.getRemark(), tempRemark);
        Assert.assertEquals(queriedProcurement.getOsp().getId(), osp.getId());
        Assert.assertEquals(queriedProcurement.getResourceType(), Procurement.ResourceType.catering);
        Assert.assertEquals(queriedProcurement.getTenantId(), Long.valueOf(1L));
        Assert.assertEquals(queriedProcurement.getDel(), Boolean.FALSE);
        Assert.assertEquals(queriedProcurement.getOnline(), Boolean.TRUE);
        Assert.assertEquals(queriedProcurement.getResourceId(), catering.getId());
    }

    /**
     * 测试正常情况下，多条件查询，返回元素分页对象
     */
    @Test
    public void testQueryProcurementsPagesByConditions() {

        String tempName = generateString("test-procurement-name");
        String tempRemark = generateString("test-procurement-remark");

        Procurement procurement = new Procurement();
        procurement.setName(tempName);
        procurement.setRemark(tempRemark);
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.catering);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(catering.getId());

        Procurement dbProcurement = procurementBiz.addProcurement(procurement);
        Assert.assertNotNull(dbProcurement);

        Map<String, Object> params = new HashMap<>();
        params.put("name", tempName);
        params.put("remark", tempRemark);
        params.put("osp", osp);
        params.put("resourceType", Procurement.ResourceType.catering);
        params.put("tenantId", 1L);
        params.put("del", Boolean.FALSE);
        params.put("online", Boolean.TRUE);
        params.put("resourceId", catering.getId());
        DomainPage<Procurement> procurementDomainPage = procurementBiz.queryProcurementsPagesByConditions(params
                , "id", IBaseDao.SortBy.DESC, 1, 10, Boolean.FALSE);
        Assert.assertTrue(procurementDomainPage != null
                && Long.valueOf(1L).equals(procurementDomainPage.getDomainTotalCount()));
        Procurement queriedProcurement = procurementDomainPage.getDomains().get(0);
        Assert.assertEquals(queriedProcurement.getName(), tempName);
        Assert.assertEquals(queriedProcurement.getRemark(), tempRemark);
        Assert.assertEquals(queriedProcurement.getOsp().getId(), osp.getId());
        Assert.assertEquals(queriedProcurement.getResourceType(), Procurement.ResourceType.catering);
        Assert.assertEquals(queriedProcurement.getTenantId(), Long.valueOf(1L));
        Assert.assertEquals(queriedProcurement.getDel(), Boolean.FALSE);
        Assert.assertEquals(queriedProcurement.getOnline(), Boolean.TRUE);
        Assert.assertEquals(queriedProcurement.getResourceId(), catering.getId());
    }

    /**
     * 测试正常情况下，多条件查询，返回ProcurementVo的分页对象
     */
    @Test
    public void testQueryProcurementVoPagesByConditions() {

        String tempName = generateString("test-procurement-name");
        String tempRemark = generateString("test-procurement-remark");

        Procurement procurement = new Procurement();
        procurement.setName(tempName);
        procurement.setRemark(tempRemark);
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.catering);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(catering.getId());

        Procurement dbProcurement = procurementBiz.addProcurement(procurement);
        Assert.assertNotNull(dbProcurement);

        Map<String, Object> params = new HashMap<>();
        params.put("name", tempName);
        params.put("resourceType", Procurement.ResourceType.catering.name());
        params.put("online", Boolean.TRUE);
        params.put("destination", destination.getName());
        DomainPage<ProcurementVo> procurementDomainPage = procurementBiz.queryProcurementVoPagesByConditions(params
                , "id", IBaseDao.SortBy.DESC, 1, 10);
        Assert.assertTrue(procurementDomainPage != null
                && Long.valueOf(1L).equals(procurementDomainPage.getDomainTotalCount()));

        ProcurementVo queriedProcurementVo = procurementDomainPage.getDomains().get(0);
        Assert.assertEquals(queriedProcurementVo.getName(), tempName);
        Assert.assertEquals(queriedProcurementVo.getResourceType(), Procurement.ResourceType.catering.getRemark());
        Assert.assertEquals(queriedProcurementVo.getDestination(), destination.getName());
        Assert.assertTrue(queriedProcurementVo.isOnline());
    }

    /**
     * 测试元素是否存在
     */
    @Test
    public void testIsExist() {
        Procurement procurement = generateScenicProcurement();
        Procurement dbProcurement = procurementBiz.addProcurement(procurement);
        Assert.assertNotNull(dbProcurement);
        Assert.assertTrue(procurementBiz.isExisted(dbProcurement.getId()));
    }

    /**
     * 当元素伪删除时，测试元素是否存在
     */
    @Test
    public void testDeletedIsExist() {
        Procurement procurement = generateScenicProcurement();
        Procurement dbProcurement = procurementBiz.addProcurement(procurement);
        Assert.assertNotNull(dbProcurement);

        procurementBiz.deleteProcurement(dbProcurement.getId());

        Assert.assertTrue(!procurementBiz.isExisted(dbProcurement.getId()));
    }

    /**
     * 当元素物理删除时，测试元素是否存在
     */
    @Test
    public void testRealDeletedIsExist() {
        Procurement procurement = generateScenicProcurement();
        Procurement dbProcurement = procurementBiz.addProcurement(procurement);
        Assert.assertNotNull(dbProcurement);

        resourcesDao.removeEntity(dbProcurement);

        Assert.assertTrue(!procurementBiz.isExisted(dbProcurement.getId()));
    }


    /**
     * 避免数据库的重复
     * @param str
     * @return
     */
    private String generateString(String str) {
        return new Date().getTime() + str;
    }


    /**
     * 删除对象时，同时捕捉异常，避免接下来的操作无法继续进行
     * @param object
     */
    private <T extends BaseDomain> void removeCatchException(T object) {
        try {
            if (object != null) {
                resourcesDao.remove(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成一个新的Hotel
     * @return
     */
    private Hotel generateNewHotel() {
        Hotel tempHotel = new Hotel();
        tempHotel.setName(generateString("test-procurement-hotel"));
        tempHotel.setAddress(generateString("test-procurement-hotel"));
        tempHotel.setRemark(generateString("test-procurement-hotel"));
        tempHotel.setDel(Boolean.FALSE);
        tempHotel.setDestination(destination);
        tempHotel.setLat(new BigDecimal(88.8));
        tempHotel.setLon(new BigDecimal(88.8));
        tempHotel.setTenantId(1L);
        tempHotel.setType(Hotel.StayType.folk_house);
        return tempHotel;
    }

    /**
     * 生成一个新的Scenic
     * @return
     */
    private Scenic generateNewScenic() {
        Scenic tempScenic = new Scenic();
        tempScenic.setName(generateString("test-procurement-scenic"));
        tempScenic.setAddress(generateString("test-procurement-scenic"));
        tempScenic.setRemark(generateString("test-procurement-scenic"));
        tempScenic.setDestination(destination);
        tempScenic.setLat(new BigDecimal(88.8));
        tempScenic.setLon(new BigDecimal(88.8));
        tempScenic.setTenantId(1L);
        tempScenic.setDel(Boolean.FALSE);
        return tempScenic;
    }

    /**
     * 生成酒店元素对象
     * @return
     */
    private Procurement generateHotelProcurement() {
        Procurement procurement = new Procurement();
        procurement.setName(generateString("test-hotel-procurement"));
        procurement.setRemark(generateString("test-hotel-procurement"));
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.hotel);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(hotel.getId());
        return procurement;
    }

    /**
     * 生成餐饮点元素对象
     * @return
     */
    private Procurement generateCateringProcurement() {
        Procurement procurement = new Procurement();
        procurement.setName(generateString("test-hotel-procurement"));
        procurement.setRemark(generateString("test-hotel-procurement"));
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.catering);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(catering.getId());
        return procurement;
    }

    /**
     * 生成娱乐元素对象
     * @return
     */
    private Procurement generateEntertainmentProcurement() {
        Procurement procurement = new Procurement();
        procurement.setName(generateString("test-hotel-procurement"));
        procurement.setRemark(generateString("test-hotel-procurement"));
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.entertainment);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(entertainment.getId());
        return procurement;
    }

    /**
     * 生成景点元素对象
     * @return
     */
    private Procurement generateScenicProcurement() {
        Procurement procurement = new Procurement();
        procurement.setName(generateString("test-hotel-procurement"));
        procurement.setRemark(generateString("test-hotel-procurement"));
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(scenic.getId());
        return procurement;
    }

    /**
     * 生成一个Destination
     * @return
     */
    private Destination generateNewDestination() {
        Destination tempDestination = new Destination();
        tempDestination.setName(generateString("test-procurement-destination"));
        tempDestination.setAddress(generateString("test-procurement-destination"));
        tempDestination.setRemark(generateString("test-procurement-destination"));
        tempDestination.setLat(new BigDecimal(88.8));
        tempDestination.setLon(new BigDecimal(88.8));
        tempDestination.setArea(area);
        tempDestination.setTenantId(1L);
        tempDestination.setDel(Boolean.FALSE);
        return tempDestination;
    }

    /**
     * 生成一个Catering
     * @return
     */
    private Catering generateNewCatering() {
        Catering tempCatering = new Catering();
        tempCatering.setDestination(destination);
        tempCatering.setName(generateString("test-procurement-catering"));
        tempCatering.setAddress(generateString("test-procurement-catering"));
        tempCatering.setRemark(generateString("test-procurement-catering"));
        tempCatering.setType(Catering.Type.hotel);
        tempCatering.setLat(new BigDecimal(88.8));
        tempCatering.setLon(new BigDecimal(88.8));
        tempCatering.setTenantId(1L);
        tempCatering.setDel(Boolean.FALSE);
        return tempCatering;
    }

    /**
     * 生成一个Osp
     * @return
     */
    private OfflineServiceProvider generateNewOsp() {
        OfflineServiceProvider tempOsp = new OfflineServiceProvider();
        tempOsp.setName(generateString("test-procurement-osp"));
        tempOsp.setRemark(generateString("test-procurement-osp"));
        tempOsp.setDel(Boolean.FALSE);
        tempOsp.setTenantId(1L);
        return tempOsp;
    }

    /**
     * 生成新的Entertainment对象
     * @return
     */
    private Entertainment generateNewEntertainment() {
        Entertainment tempEntertainment = new Entertainment();
        tempEntertainment.setName(generateString("test-procurement-entertainment"));
        tempEntertainment.setAddress(generateString("test-procurement-entertainment"));
        tempEntertainment.setRemark(generateString("test-procurement-entertainment"));
        tempEntertainment.setDestination(destination);
        tempEntertainment.setLat(new BigDecimal(88.8));
        tempEntertainment.setLon(new BigDecimal(88.8));
        tempEntertainment.setTenantId(1L);
        tempEntertainment.setType(Entertainment.Type.activity);
        tempEntertainment.setDel(Boolean.FALSE);
        return tempEntertainment;
    }
}
