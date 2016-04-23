package com.test;

import com.simpletour.biz.inventory.IStockQueryBiz;
import com.simpletour.biz.resources.*;
import com.simpletour.biz.resources.vo.ProcurementVo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.commons.data.util.ThreadLocalUtil;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.inventory.Price;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.StockPrice;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.resources.*;
import com.simpletour.service.resources.IResourcesService;
import com.simpletour.service.resources.error.ResourcesServiceError;
import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Brief : 元素测试
 * Author: Hawk
 * Date  : 2016/3/24
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ProcurementTest extends AbstractTransactionalTestNGSpringContextTests
//public class ProcurementTest extends AbstractTestNGSpringContextTests
{

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
    private IEntertainmentBiz entertainmentBiz;

    @Resource
    private IResourcesService resourcesService;

    @Resource
    IStockQueryBiz stockQueryBiz;

    private Area area = null;
    private Destination destination = null;
    private Hotel hotel = null;
    private Scenic scenic = null;
    private Catering catering = null;
    private Entertainment entertainment = null;
    private OfflineServiceProvider osp = null;

    @Ignore
    //@BeforeClass
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

        Scenic tempScenic = generateNewScenic();
        scenic = scenicBiz.addScenic(tempScenic);
        Assert.assertNotNull(scenic);

        Catering tempCatering = generateNewCatering();
        catering = cateringBiz.addCatering(tempCatering);
        Assert.assertNotNull(catering);

        Entertainment tempEntertainment = generateNewEntertainment();
        entertainment = entertainmentBiz.addEntertainment(tempEntertainment);
        Assert.assertNotNull(entertainment);

        OfflineServiceProvider tempOsp = generateNewOsp();
        osp = ospBiz.addOfflineServiceProvider(tempOsp);
        Assert.assertNotNull(osp);
    }

    @Ignore
    //@AfterClass
    public void teardown() {
        removeCatchException(hotel);
        removeCatchException(osp);
        removeCatchException(catering);
        removeCatchException(scenic);
        removeCatchException(entertainment);
        removeCatchException(destination);
    }
    
    /**
     * 测试正常情况下的，增加元素
     */
    @Test
    public void testAddProcurement() {
        Procurement cateringProcurement = generateCateringProcurement();
        Assert.assertTrue(resourcesService.addProcurement(cateringProcurement).isPresent());
        
        Procurement hotelProcurement = generateHotelProcurement();
        Assert.assertTrue(resourcesService.addProcurement(hotelProcurement).isPresent());
        
        Procurement entertainmentProcuremnt = generateEntertainmentProcurement();
        Assert.assertTrue(resourcesService.addProcurement(entertainmentProcuremnt).isPresent());
        
        Procurement scenicProcurement = generateScenicProcurement();
        Assert.assertTrue(resourcesService.addProcurement(scenicProcurement).isPresent());
    }

    /**
     * 测试增加空的元素对象
     */
    @Test
    public void testAddProcurementAndEntityNull() {
        try {
            resourcesService.addProcurement(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.EMPTY_ENTITY, e.getError());
        }
    }

    /**
     * 测试增加元素时，目的地为null
     */
    @Test
    public void testAddProcurementAndDestinationNull() {
        Procurement procurement = generateScenicProcurement();
        procurement.setDestination(null);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NULL, e.getError());
        }
    }

    /**
     * 测试增加元素对象时，目的地ID为null
     */
    @Test
    public void testAddProcurementAndDestinationIdNull() {
        Procurement procurement = generateScenicProcurement();
        Destination tempDestination = new Destination();
        procurement.setDestination(tempDestination);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NULL, e.getError());
        }
    }

    /**
     * 测试增加元素对象时，目的地不存在
     */
    @Test
    public void testAddProcurementAndDestinationNotExist() {
        Procurement procurement = generateScenicProcurement();
        Destination tempDestination = generateNewDestination();
        tempDestination.setId(-1L);
        procurement.setDestination(tempDestination);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素对象时, ResourceId为null
     */
    @Test
    public void testAddProcurementAndResourceIdNull() {
        Procurement procurement = generateScenicProcurement();
        procurement.setResourceId(null);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素对象时, ResourceId没有有对应的资源
     */
    @Test
    public void testAddProcurementAndResourceIdError() {
        Procurement procurement = generateScenicProcurement();
        procurement.setResourceId(-1L);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素对象时, ResourceType为null
     */
    @Test
    public void testAddProcurementAndResourceTypeNull() {
        Procurement procurement = generateScenicProcurement();
        procurement.setResourceType(null);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素对象时, ResourceType与ResourceId不对应
     */
    @Test
    public void testAddProcurementAndResourceTypeError() {

        Scenic scenic1 = generateNewScenic();
        Scenic dbScenic = scenicBiz.addScenic(scenic1);
        Assert.assertNotNull(dbScenic);

        Procurement procurement = generateScenicProcurement();
        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setResourceId(dbScenic.getId() + 1);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素时，ResourceType与ResourceId错误
     */
    @Test
    public void testAddProcurementAndResourceTypeAndIdError() {
        Scenic scenic1 = generateNewScenic();
        Scenic dbScenic = scenicBiz.addScenic(scenic1);
        Assert.assertNotNull(dbScenic);

        Hotel hotel1 = generateNewHotel();
        Hotel dbHotel = hotelBiz.addHotel(hotel1);
        Assert.assertNotNull(dbHotel);

        Procurement procurement = generateScenicProcurement();
        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setResourceId(dbHotel.getId() + 1);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素时，Osp为null
     */
    @Test
    public void testAddProcurementAndOspNull() {
        Procurement procurement = generateScenicProcurement();
        procurement.setOsp(null);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素时，OspId为null
     */
    @Test
    public void testAddProcurementAndOspIdNull() {
        OfflineServiceProvider osp = new OfflineServiceProvider();
        Procurement procurement = generateScenicProcurement();
        procurement.setOsp(osp);
        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素时，Osp已经被伪删除了
     */
    @Test
    public void testAddProcurementAndOspDeleted() {
        OfflineServiceProvider osp = generateNewOsp();
        OfflineServiceProvider dbOsp = ospBiz.addOfflineServiceProvider(osp);
        Assert.assertNotNull(dbOsp);

        ospBiz.deleteOfflineServiceProvider(dbOsp.getId());

        Procurement procurement = generateScenicProcurement();
        procurement.setOsp(osp);

        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加元素时，Osp已经被物理删除了
     */
    @Test
    public void testAddProcurementAndOspRealDeleted() {
        OfflineServiceProvider osp = generateNewOsp();
        OfflineServiceProvider dbOsp = ospBiz.addOfflineServiceProvider(osp);
        Assert.assertNotNull(dbOsp);

        resourcesDao.removeEntity(dbOsp);

        Procurement procurement = generateScenicProcurement();
        procurement.setOsp(osp);

        try {
            resourcesService.addProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试正常情况下的，更新元素
     */
    @Test
    public void testUpdateProcurement() {
        Procurement tempProcurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(tempProcurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());
        
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

        Procurement tempProcurement2 = new Procurement();
        tempProcurement2.setName(tempStr);
        tempProcurement2.setRemark(tempStr);
        tempProcurement2.setId(dbProcurementOpt.get().getId());
        tempProcurement2.setOnline(Boolean.FALSE);
        tempProcurement2.setResourceId(dbHotel.getId());
        tempProcurement2.setResourceType(Procurement.ResourceType.hotel);
        tempProcurement2.setOsp(dbNewOsp);
        tempProcurement2.setDestination(dbNewDestination);
        tempProcurement2.setVersion(dbProcurementOpt.get().getVersion());
        Optional<Procurement> dbNewProcurementOpt = resourcesService.updateProcurement(tempProcurement2);
        Assert.assertTrue(dbNewProcurementOpt.isPresent());
        
        Assert.assertEquals(dbNewProcurementOpt.get().getDestination(), dbNewDestination);
        Assert.assertEquals(dbNewProcurementOpt.get().getOsp(), dbNewOsp);
        Assert.assertEquals(dbNewProcurementOpt.get().getOnline(), Boolean.FALSE);
        Assert.assertEquals(dbNewProcurementOpt.get().getResourceId(), dbHotel.getId());
        Assert.assertEquals(dbNewProcurementOpt.get().getResourceType(), Procurement.ResourceType.hotel);
        Assert.assertEquals(dbNewProcurementOpt.get().getName(), tempStr);
        Assert.assertEquals(dbNewProcurementOpt.get().getRemark(), tempStr);
    }

    /**
     * 测试元素更新时，元素为null
     */
    @Test
    public void testUpdateProcurementAndEntityNull() {
        try {
            resourcesService.updateProcurement(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.EMPTY_ENTITY, e.getError());
        }
    }

    /**
     * 测试元素更新时，元素中的destination为null
     */
    @Test
    public void testUpdateProcurementAndDestinationNull() {

        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(procurementOpt.isPresent());
        try {
            procurement.setDestination(null);
            procurement.setId(procurementOpt.get().getId());
            procurement.setVersion(procurementOpt.get().getVersion());
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NULL, e.getError());
        }
    }

    /**
     * 测试元素更新时，元素中的destinationId为null
     */
    @Test
    public void testUpdateProcurementAndDestinationIdNull() {
        try {
            Procurement procurement = generateScenicProcurement();
            Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
            Assert.assertTrue(procurementOpt.isPresent());

            Destination tempDestination = new Destination();
            procurement.setDestination(tempDestination);
            procurement.setId(procurementOpt.get().getId());
            procurement.setVersion(procurementOpt.get().getVersion());
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NULL, e.getError());
        }
    }

    /**
     * 测试元素更新时，元素中的destination不存在
     */
    @Test
    public void testUpdateProcurementAndDestinationNotExist() {
        try {
            Procurement procurement = generateScenicProcurement();
            Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
            Assert.assertTrue(procurementOpt.isPresent());

            Destination tempDestination = new Destination();
            tempDestination.setId(-1L);
            procurement.setDestination(tempDestination);
            procurement.setId(procurementOpt.get().getId());
            procurement.setVersion(procurementOpt.get().getVersion());
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，ResourceId为null
     */
    @Test
    public void testUpdateProcurementAndResourceIdNull() {

        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(procurementOpt.isPresent());

        try {
            procurement.setResourceId(null);
            procurement.setId(procurementOpt.get().getId());
            procurement.setVersion(procurementOpt.get().getVersion());
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，ResourceId错误
     */
    @Test
    public void testUpdateProcurementAndResourceIdError() {

        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(procurementOpt.isPresent());

        try {
            procurement.setResourceId(-1L);
            procurement.setId(procurementOpt.get().getId());
            procurement.setVersion(procurementOpt.get().getVersion());
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，ResourceType为null
     */
    @Test
    public void testUpdateProcurementAndResourceTypeNull() {

        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(procurementOpt.isPresent());

        try {
            procurement.setResourceType(null);
            procurement.setId(procurementOpt.get().getId());
            procurement.setVersion(procurementOpt.get().getVersion());
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，ResourceType错误
     */
    @Test
    public void testUpdateProcurementAndResourceTypeError() {
        Scenic scenic1 = generateNewScenic();
        Scenic dbScenic = scenicBiz.addScenic(scenic1);
        Assert.assertNotNull(dbScenic);

        Procurement procurement = generateScenicProcurement();
        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setResourceId(dbScenic.getId());
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());

        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setResourceId(dbScenic.getId() + 1);
        procurement.setId(dbProcurementOpt.get().getId());
        procurement.setVersion(dbProcurementOpt.get().getVersion());
        try {
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，ResourceType与ResourceId错误
     */
    @Test
    public void testUpdateProcurementAndResourceTypeAndIdError() {
        Scenic scenic1 = generateNewScenic();
        Scenic dbScenic = scenicBiz.addScenic(scenic1);
        Assert.assertNotNull(dbScenic);

        Hotel hotel1 = generateNewHotel();
        Hotel dbHotel = hotelBiz.addHotel(hotel1);
        Assert.assertNotNull(dbHotel);

        Procurement procurement = generateScenicProcurement();
        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setResourceId(dbScenic.getId());
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());

        procurement.setResourceType(Procurement.ResourceType.hotel);
        procurement.setResourceId(dbHotel.getId() + 1);
        procurement.setId(dbProcurementOpt.get().getId());
        procurement.setVersion(dbProcurementOpt.get().getVersion());
        try {
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，Osp为null
     */
    @Test
    public void testUpdateProcurementAndOspNull() {
        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());

        procurement.setId(dbProcurementOpt.get().getId());
        procurement.setVersion(dbProcurementOpt.get().getVersion());
        procurement.setOsp(null);

        try {
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，OspId为null
     */
    @Test
    public void testUpdateProcurementAndOspIdNull() {
        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());

        procurement.setId(dbProcurementOpt.get().getId());
        procurement.setVersion(dbProcurementOpt.get().getVersion());
        procurement.setOsp(new OfflineServiceProvider());

        try {
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，Osp已经被伪删除了
     */
    @Test
    public void testUpdateProcurementAndOspDeleted() {
        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());

        OfflineServiceProvider newOsp = generateNewOsp();
        OfflineServiceProvider dbOsp = ospBiz.addOfflineServiceProvider(newOsp);
        Assert.assertNotNull(dbOsp);

        ospBiz.deleteOfflineServiceProvider(dbOsp.getId());

        procurement.setId(dbProcurementOpt.get().getId());
        procurement.setVersion(dbProcurementOpt.get().getVersion());
        procurement.setOsp(dbOsp);

        try {
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试元素更新时，Osp已经被物理删除了
     */
    @Test
    public void testUpdateProcurementAndOspRealDeleted() {
        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());

        OfflineServiceProvider newOsp = generateNewOsp();
        OfflineServiceProvider dbOsp = ospBiz.addOfflineServiceProvider(newOsp);
        Assert.assertNotNull(dbOsp);

        resourcesDao.removeEntity(dbOsp);

        procurement.setId(dbProcurementOpt.get().getId());
        procurement.setVersion(dbProcurementOpt.get().getVersion());
        procurement.setOsp(dbOsp);

        try {
            resourcesService.updateProcurement(procurement);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试更新某一个元素时，元素已经被伪删除
     */
    @Test
    public void testUpdateProcurementAndDeleted() {
        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(procurementOpt.isPresent());

        resourcesService.deleteProcurement(procurementOpt.get().getId());

        procurement.setId(procurementOpt.get().getId());
        procurement.setVersion(procurementOpt.get().getVersion());
        try {
            resourcesService.updateProcurement(procurement);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试更新某一个元素时，元素已经被物理删除
     */
    @Test
    public void testUpdateProcurementAndRealDeleted() {
        Procurement procurement = generateScenicProcurement();
        Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(procurementOpt.isPresent());

        resourcesDao.removeEntity(procurementOpt.get());

        procurement.setId(procurementOpt.get().getId());
        procurement.setVersion(procurementOpt.get().getVersion());
        try {
            resourcesService.updateProcurement(procurement);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_NOT_EXIST, e.getError());
        }
    }
    
    /**
     * 测试正常情况下的，删除元素
     */
    @Test
    public void testDeleteProcurement() {
        Procurement tempProcurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(tempProcurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());
        
        resourcesService.deleteProcurement(dbProcurementOpt.get().getId());
        
        Procurement afterProcurement = resourcesDao.getEntityById(Procurement.class, dbProcurementOpt.get().getId());
        Assert.assertNotNull(afterProcurement);
        Assert.assertTrue(afterProcurement.getDel());
    }

    /**
     * 测试删除元素, 元素ID为null
     */
    @Test
    public void testDeleteProcurementAndIdNull() {
        try {
            resourcesService.deleteProcurement(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试删除元素, 元素ID为-1
     */
    @Test
    public void testDeleteProcurementAndIdMinuOne() {
        try {
            resourcesService.deleteProcurement(-1L);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试删除元素, 元素已经被伪删除
     */
    @Test
    public void testDeleteProcurementAndDeleted() {

        Procurement tempProcurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(tempProcurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());

        resourcesService.deleteProcurement(dbProcurementOpt.get().getId());
        try {
            resourcesService.deleteProcurement(dbProcurementOpt.get().getId());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试删除元素, 元素已经被物理删除
     */
    @Test
    public void testDeleteProcurementAndRealDeleted() {

        Procurement tempProcurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(tempProcurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());

        resourcesDao.removeEntity(dbProcurementOpt.get());

        try {
            resourcesService.deleteProcurement(dbProcurementOpt.get().getId());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.PROCUREMENT_NOT_EXIST, e.getError());
        }
    }
    
    /**
     * 测试正常情况下的，根据元素id，获取元素对象
     */
    @Test
    public void testGetProcurementById() {
        Procurement tempProcurement = generateScenicProcurement();
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(tempProcurement);
        Assert.assertNotNull(resourcesService.getProcurementById(dbProcurementOpt.get().getId()));
    }

    private Date paseDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date day = null;
        try {
            day = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("invalid date format!");
        }
        return day;
    }

    private StockKey generateProcurementStock() {
        final long DEFAULT_TENANT_ID = 1L;
        ThreadLocalUtil.setTenantId(DEFAULT_TENANT_ID);

        OfflineServiceProvider tempOsp = new OfflineServiceProvider();
        tempOsp.setName("简途旅行");
        tempOsp.setTenantId(DEFAULT_TENANT_ID);

        OfflineServiceProvider savedOsp = ospBiz.addOfflineServiceProvider(tempOsp);
        Assert.assertNotNull(savedOsp);

        Area tempArea = new Area();
        tempArea.setId(513337L);
        tempArea.setName("四川省-甘孜藏族自治州-稻城县");

        Destination tempDestination = new Destination();
        tempDestination.setAddress("四川省-甘孜藏族自治州-稻城县");
        tempDestination.setName("稻城亚丁");
        tempDestination.setLat(new BigDecimal(168.1));
        tempDestination.setLon(new BigDecimal(82.5));
        tempDestination.setArea(tempArea);
        tempDestination.setTenantId(DEFAULT_TENANT_ID);

        Destination savedDestination = destinationBiz.addDestination(tempDestination);
        Assert.assertNotNull(savedDestination);

        Scenic tempScenic = new Scenic();
        tempScenic.setName("稻城亚丁风景区");
        tempScenic.setAddress(savedDestination.getAddress());
        tempScenic.setDestination(savedDestination);
        tempScenic.setLat(savedDestination.getLat());
        tempScenic.setLon(savedDestination.getLon());
        tempScenic.setDel(false);
        tempScenic.setTenantId(DEFAULT_TENANT_ID);

        Scenic savedScenic = scenicBiz.addScenic(tempScenic);
        Assert.assertNotNull(savedScenic);

        Procurement procurement = new Procurement();
        procurement.setName("稻城亚丁门票-2张");
        procurement.setRemark("景点元素");
        procurement.setDestination(savedDestination);
        procurement.setOsp(savedOsp);
        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setResourceId(savedScenic.getId());
        procurement.setTenantId(DEFAULT_TENANT_ID);

        Optional<Procurement> procurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(procurementOpt.isPresent());
        return procurementOpt.get().getStockKey();
    }

    /**
     * 添加单个景点元素库存
     * @return
     */
    @Test
    public void testAddProcurementStock() {
        System.out.println("enter testAddProcurementStock");

        StockKey stockKey = generateProcurementStock();
        Date day = paseDate("2016-04-22");
        List<Date> days = new ArrayList<Date>(1){{add(day);}};

        List<Price> prices = new ArrayList<>(2);
        prices.add(new Price(stockKey.getInventoryType(), stockKey.getInventoryId(), day, Price.Type.adult, 12295, 13098, 15050));
        prices.add(new Price(stockKey.getInventoryType(), stockKey.getInventoryId(), day, Price.Type.child, 8086, 9550, 11000));

        StockPrice stockPrice = new StockPrice(stockKey, days, 50, prices, true);
        Optional<Stock> stockOptional = resourcesService.addProcurementStock(stockPrice);
        Assert.assertTrue(stockOptional.isPresent());

        Stock stock = stockOptional.get();
        Assert.assertEquals(stockPrice.getStockKey().getInventoryType(), stock.getInventoryType());
        Assert.assertEquals(stockPrice.getStockKey().getInventoryId(), stock.getInventoryId());
        Assert.assertEquals(stockPrice.getQuantity(), stock.getQuantity());

        System.out.println("end testAddProcurementStock");
    }

    /**
     * 批量添加景点元素库存
     * @return
     */
    @Test
    public void testAddProcurementStocks() {
        System.out.println("enter testAddProcurementStocks");

        StockKey stockKey = generateProcurementStock();
        Date day1 = paseDate("2016-04-23");
        Date day2 = paseDate("2016-04-24");
        Date day3 = paseDate("2016-04-25");
        List<Date> days = new ArrayList<Date>(2){{add(day1);add(day2);add(day3);}};

        List<Price> prices = new ArrayList<>(2);
        prices.add(new Price(stockKey.getInventoryType(), stockKey.getInventoryId(), null, Price.Type.adult, 10115, 11598, 12970));
        prices.add(new Price(stockKey.getInventoryType(), stockKey.getInventoryId(), null, Price.Type.child, 7011, 8160, 9900));
        prices.add(new Price(stockKey.getInventoryType(), stockKey.getInventoryId(), null, Price.Type.child, 7011, 8160, 9900));

        StockPrice stockPrice = new StockPrice(stockKey, days, 55, prices, true);
        List<Stock> stocks = resourcesService.addProcurementStocks(stockPrice);
        Assert.assertNotNull(stocks);
        Assert.assertEquals(3, stocks.size());

        System.out.println("end testAddProcurementStocks");
    }

    /**
     * 更新单个景点元素库存
     * @return
     */
    @Test
    public void testUpdateProcurementStock() {
        System.out.println("enter testUpdateProcurementStock");

        StockKey stockKey = generateProcurementStock();
        Date day = paseDate("2016-04-22");
        List<Date> days = new ArrayList<Date>(1){{add(day);}};

        List<Price> prices = new ArrayList<>(2);
        prices.add(new Price(stockKey.getInventoryType(), stockKey.getInventoryId(), day, Price.Type.adult, 12295, 13098, 15050));
        prices.add(new Price(stockKey.getInventoryType(), stockKey.getInventoryId(), day, Price.Type.child, 8086, 9550, 11000));

        StockPrice stockPrice = new StockPrice(stockKey, days, 50, prices, true);
        Optional<Stock> addStockOptional = resourcesService.addProcurementStock(stockPrice);
        Assert.assertTrue(addStockOptional.isPresent());

        Integer quantity = addStockOptional.get().getQuantity();
        Assert.assertEquals(stockPrice.getQuantity(), quantity);
        stockPrice.setQuantity(60);

        List<Price> savedPrices = stockQueryBiz.getPrices(stockPrice.getStockKey(), day, null);
        Price adultPrice = savedPrices.get(0);
        int adultCost = adultPrice.getCost();
        adultPrice.setCost(12200);

        Price childPrice = savedPrices.get(1);
        int childCost = childPrice.getCost();
        childPrice.setCost(8080);

        stockPrice.setPrices(savedPrices);
        Optional<Stock> updateStockOptional = resourcesService.updateProcurementStock(stockPrice);
        Assert.assertTrue(updateStockOptional.isPresent());

        Stock stock = updateStockOptional.get();
        Assert.assertEquals(addStockOptional.get().getId(), stock.getId());
        Assert.assertNotEquals(quantity, stock.getQuantity());
        Assert.assertNotEquals(adultCost, 12200);
        Assert.assertNotEquals(childCost, 8080);

        System.out.println("end testUpdateProcurementStock");
    }
    
    /**
     * 测试正常情况下，多条件查询，返回元素分页对象
     */
    @Test
    public void testQueryProcurementsPagesByConditions() {
        Procurement procurement = new Procurement();
        String tempStr = generateString("test-hotel-procurement");
        procurement.setName(tempStr);
        procurement.setRemark(tempStr);
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.catering);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(catering.getId());
        
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", tempStr);
        params.put("remark", tempStr);
        params.put("osp", osp);
        params.put("resourceType", Procurement.ResourceType.catering);
        params.put("tenantId", 1L);
        params.put("del", Boolean.FALSE);
        params.put("online", Boolean.TRUE);
        params.put("resourceId", catering.getId());
        DomainPage<Procurement> procurementDomainPage = resourcesService.queryProcurementsPagesByConditions(params
                , "id", IBaseDao.SortBy.DESC, 1, 10, Boolean.FALSE);

        Assert.assertTrue(procurementDomainPage != null
                && Long.valueOf(1L).equals(procurementDomainPage.getDomainTotalCount()));
        Procurement queriedProcurement = procurementDomainPage.getDomains().get(0);
        Assert.assertEquals(queriedProcurement.getName(), tempStr);
        Assert.assertEquals(queriedProcurement.getRemark(), tempStr);
        Assert.assertEquals(queriedProcurement.getOsp().getId(), osp.getId());
        Assert.assertEquals(queriedProcurement.getResourceType(), Procurement.ResourceType.catering);
        Assert.assertEquals(queriedProcurement.getTenantId(), Long.valueOf(1L));
        Assert.assertEquals(queriedProcurement.getDel(), Boolean.FALSE);
        Assert.assertEquals(queriedProcurement.getOnline(), Boolean.TRUE);
        Assert.assertEquals(queriedProcurement.getResourceId(), catering.getId());
    }
    
    /**
     * 测试正常情况下，多条件查询，返回ProcurementVo分页对象
     */
    @Test
    public void testQueryProcurementVoPagesByConditions() {
        Procurement procurement = new Procurement();
        String tempStr = generateString("test-hotel-procurement");
        procurement.setName(tempStr);
        procurement.setRemark(tempStr);
        procurement.setOsp(osp);
        procurement.setResourceType(Procurement.ResourceType.catering);
        procurement.setTenantId(1L);
        procurement.setDel(Boolean.FALSE);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceId(catering.getId());
        
        Optional<Procurement> dbProcurementOpt = resourcesService.addProcurement(procurement);
        Assert.assertTrue(dbProcurementOpt.isPresent());
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", tempStr);
        params.put("resourceType", Procurement.ResourceType.catering.name());
        params.put("online", Boolean.TRUE);
        params.put("destination", destination.getName());
        DomainPage<ProcurementVo> procurementDomainPage = resourcesService.queryProcurementVoPagesByConditions(params
                , "id", IBaseDao.SortBy.DESC, 1, 10);
        Assert.assertTrue(procurementDomainPage != null
                && Long.valueOf(1L).equals(procurementDomainPage.getDomainTotalCount()));
        ProcurementVo queriedProcurementVo = procurementDomainPage.getDomains().get(0);
        Assert.assertEquals(queriedProcurementVo.getName(), tempStr);
        Assert.assertEquals(queriedProcurementVo.getResourceType(), Procurement.ResourceType.catering.getRemark());
        Assert.assertEquals(queriedProcurementVo.getDestination(), destination.getName());
        Assert.assertTrue(queriedProcurementVo.isOnline());
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
        tempHotel.setAddress(generateString("test-procurement-hotel"));
        tempHotel.setName(generateString("test-procurement-hotel"));
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
        tempScenic.setAddress(generateString("test-procurement-scenic"));
        tempScenic.setName(generateString("test-procurement-scenic"));
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
        procurement.setRemark(generateString("test-hotel-procurement"));
        procurement.setName(generateString("test-hotel-procurement"));
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
        procurement.setRemark(generateString("test-hotel-procurement"));
        procurement.setName(generateString("test-hotel-procurement"));
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
        procurement.setRemark(generateString("test-hotel-procurement"));
        procurement.setName(generateString("test-hotel-procurement"));
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
        procurement.setRemark(generateString("test-hotel-procurement"));
        procurement.setName(generateString("test-hotel-procurement"));
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
        tempDestination.setAddress(generateString("test-procurement-destination"));
        tempDestination.setName(generateString("test-procurement-destination"));
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
        tempCatering.setAddress(generateString("test-procurement-catering"));
        tempCatering.setName(generateString("test-procurement-catering"));
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
        tempOsp.setRemark(generateString("test-procurement-osp"));
        tempOsp.setName(generateString("test-procurement-osp"));
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
        tempEntertainment.setAddress(generateString("test-procurement-entertainment"));
        tempEntertainment.setName(generateString("test-procurement-entertainment"));
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
