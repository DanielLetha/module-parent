package com.simpletour.biz.resources;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import resources.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Brief :  餐饮点biz单元测试类
 * @Author:  liangfei/liangfei@simpletour.com
 * @Date  :  2016/3/22 11:39
 * @Since ： 2.0.0-SNAPSHOT
 * @Remark:  本类仅用于对于cateringBiz的单元测试
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
@Transactional
public class CateringBizTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    protected ICateringBiz cateringBiz;
    @Autowired
    private IResourcesBiz resourcesBiz;
    @Autowired
    protected IDestinationBiz destinationBiz;
    @Resource
    protected IResourcesDao resourcesDao;
    @Resource
    private IProcurementBiz procurementBiz;

    private long cateringId;

    /**
     * 构造数据
     */
    @BeforeClass(alwaysRun = true)
    public void initData() {
        //获取area,根据具体数据库的数据来设置
        Area area = resourcesBiz.getAreaById(340400L);
        Destination destination = new Destination("国色天乡花展会", "国色天乡马戏停车场", "1", area, BigDecimal.valueOf(21.06), BigDecimal.valueOf(96.15));
        destination.setTenantId(12L);
        destinationBiz.addDestination(destination);
//        new EncryptedToken("0", "0", "12", "2", Token.ClientType.BROWSER);

    }

    /**
     * 测试添加空值
     */
    @Test
    @Rollback(value = true)
    public void testAddNullCatering() {
        try {
            Catering catering = cateringBiz.addCatering(null);
            Assert.assertNotNull(catering, "错误-添加空值的餐饮店成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.EMPTY_ENTITY);
        }
    }

    /**
     * 测试添加已经删除的数据
     */
    @Test
    @Rollback(value = true)
    public void testAddDelCatering() {
        Destination destination = destinationBiz.getDestinationsByPage(1, 10).getDomains().get(0);
        Catering catering = new Catering();
        catering.setName("大排档");
        catering.setDestination(destination);
        catering.setType(Catering.Type.other);
        catering.setTenantId(12L);
        catering.setDel(Boolean.TRUE);
        try {
            Catering catering1 = cateringBiz.addCatering(catering);
            Assert.assertNotNull(catering1, "错误-添加已经删除的数据成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.ADD_CATERING_DEL);
        }
    }


    /**
     * 测试添加15条正常数据
     */
    @Test
    @Rollback(value = false)
    public void testAddCatering() {
        List list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Destination destination = destinationBiz.getDestinationsByPage(1, 10).getDomains().get(0);
            Catering catering = new Catering();
            catering.setName("大排档" + i);
            catering.setDestination(destination);
            catering.setType(Catering.Type.other);
            catering.setTenantId(12L);
            Catering catering1 = cateringBiz.addCatering(catering);
            cateringId = catering1.getId();
            list.add(catering1);
        }
        Assert.assertTrue(list.size() == 15);
    }

    /**
     * 判断某个餐饮点是否存在
     */
    @Test
    public void testIsExisted() {

        boolean bool =  cateringBiz.isExisted(cateringId);
        Assert.assertTrue(bool);

    }
    /**
     * 判断某个餐饮点是否存在
     */
    @Test
    public void testIsExistedWhenDeleted() {
        cateringBiz.deleteCatering(cateringId,false);
       Catering catering= cateringBiz.getCateringById(cateringId);
        Assert.assertNull(catering);

        boolean bool =  cateringBiz.isExisted(cateringId);
        Assert.assertFalse(bool);

    }

    /**
     * 添加相同的餐饮点
     */
    @Test
    @Rollback(value = true)
    public void testAddExistCatering() {
        try {
            Destination destination = destinationBiz.getDestinationsByPage(1, 10).getDomains().get(0);
            Catering catering = new Catering();
            catering.setName("大排档0");
            catering.setTenantId(12L);
            catering.setDestination(destination);
            catering.setType(Catering.Type.hotel);
            Catering catering1 = cateringBiz.addCatering(catering);
            Assert.assertNotNull(catering1, "错误-添加已存在的餐饮点成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.CATERING_NAME_MUST_BE_NUIQUE);
        }

    }


    /**
     * 测试更新餐饮点时，传入ID为空的餐饮点
     */
    @Test
    @Rollback(value = true)
    public void testUpdateCateringWithNull() {
        try {
            Map condition = new HashMap<>(2);
            condition.put("name", "大排档0");
            condition.put("del", Boolean.FALSE);
            Catering catering = (Catering) cateringBiz.queryCateringsPagesByConditions(null, "id", IBaseDao.SortBy.ASC, 1, 10, false).getDomains().get(0);
            catering.setId(null);
            Catering  catering1 = cateringBiz.updateCatering(catering);
            Assert.assertNotNull(catering1, "错误-更新ID为空的餐饮点成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.CATERING_NOT_EXIST);
        }
    }

    /**
     * 测试更新餐饮点时，更新一个已经存在的名字
     */
    @Test
    @Rollback(value = true)
    public void testUpdateCateringWithExistName() {
        try {
            Map condition = new HashMap<>(2);
//            condition.put("name", "大排档0");
//            condition.put("del", Boolean.FALSE);
            Catering catering = (Catering) cateringBiz.queryCateringsPagesByConditions(condition, "id", IBaseDao.SortBy.ASC, 1, 10, false).getDomains().get(0);
            catering.setName("大排档12");
            Catering catering1 = cateringBiz.updateCatering(catering);
            Assert.assertNotNull(catering1, "错误-更新名称存在的餐饮点成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.CATERING_NAME_MUST_BE_NUIQUE);
        }
    }

    /**
     * 测试更新餐饮点(正常情况)
     */
    @Test
    @Rollback(value = true)
    public void testUpdateCatering() {
        Catering catering = cateringBiz.queryCateringsPagesByConditions(null, "id", IBaseDao.SortBy.ASC, 1, 10, false).getDomains().get(0);
        catering.setRemark("更新餐饮点");
        Catering catering1 = cateringBiz.updateCatering(catering);
        Assert.assertNotNull(catering1);
    }

    /**
     * 测试分页查询餐饮点
     */
    @Test
    @Rollback(value = true)
    public void testqueryCateringsPagesByConditions() {
        Map condition = new HashMap<>();
        condition.put("name", "大排档");
        condition.put("del", Boolean.FALSE);
        DomainPage<Catering> domainPage = cateringBiz.queryCateringsPagesByConditions(condition, "id", IBaseDao.SortBy.ASC, 1, 10, true);
        Assert.assertEquals(domainPage.getDomains().size(), 10);
    }


    /**
     * 测试正常删除
     */
    @Test
    @Rollback(value = true)
    public void testDeleteCatering() {
        Destination destination = destinationBiz.getDestinationsByPage(1, 10).getDomains().get(0);
        Catering catering = new Catering();
        catering.setName("大排档20");
        catering.setTenantId(12L);
        catering.setDestination(destination);
        catering.setType(Catering.Type.hotel);
        Catering  catering1 = cateringBiz.addCatering(catering);
        Assert.assertNotNull(catering1);

        cateringBiz.deleteCatering(catering1.getId(), true);

        Catering catering2 = cateringBiz.getCateringById(catering1.getId());
        Assert.assertNull(catering2, "正确-无法查询已经被删除的数据");
    }

    /**
     * 测试包含元素的依赖删除
     */
    @Test
    @Rollback(value = true)
    public void testDelCateringWithDependency() {
        Destination destination = destinationBiz.getDestinationsByPage(1, 10).getDomains().get(0);
        Catering catering = new Catering();
        catering.setName("香格里拉大酒店");
        catering.setTenantId(12L);
        catering.setDestination(destination);
        catering.setType(Catering.Type.hotel);
        Catering catering1 = cateringBiz.addCatering(catering);
        Assert.assertNotNull(catering1);

        OfflineServiceProvider osp = new OfflineServiceProvider();
        osp.setName("万豪酒店");
        osp.setTenantId(12L);
        osp.setRemark("万豪酒店");
        OfflineServiceProvider newOsp = resourcesDao.save(osp);
        Assert.assertNotNull(newOsp);

        Procurement procurement = new Procurement();
        procurement.setRemark("酒店");
        procurement.setName("香格里拉-酒店");
        procurement.setTenantId(1L);
        procurement.setDestination(destination);
        procurement.setOnline(Boolean.TRUE);
        procurement.setResourceType(Procurement.ResourceType.scenic);
        procurement.setResourceId(catering1.getId());
        procurement.setOsp(newOsp);
        Procurement procurement1 = resourcesDao.save(procurement);
        Assert.assertNotNull(procurement1);
        try {
            cateringBiz.deleteCatering(catering1.getId(), true);
            //再次查询判断是否删除
            Catering  catering2  = cateringBiz.getCateringById(catering1.getId());
            Assert.assertNull(catering2, "错误-被依赖的餐饮点删除成功了");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.CANNOT_DEL_DEPENDENT_RESOURCE);
        }
    }


    /**
     * 测试查询单个餐饮点
     */
    @Test
    @Rollback(value = true)
    public void testGetCateringById() {
        Destination destination = destinationBiz.getDestinationsByPage(1, 10).getDomains().get(0);
        Catering catering = new Catering();
        catering.setName("简途总部");
        catering.setTenantId(12L);
        catering.setDestination(destination);
        catering.setType(Catering.Type.hotel);
       Catering catering1 = cateringBiz.addCatering(catering);
        Assert.assertNotNull(catering1);


       Catering catering2 = cateringBiz.getCateringById(catering1.getId());
        Assert.assertNotNull(catering2);

    }


    @AfterTest(alwaysRun = true)
    public void clearData() {
        DomainPage<Procurement> procurementPage = procurementBiz.queryProcurementsPagesByConditions(null, "id", IBaseDao.SortBy.ASC, 1, 20, false);
        for (Procurement procurement : procurementPage.getDomains()) {
            resourcesDao.removeEntity(procurement);
        }

        DomainPage<Catering> domainPage = cateringBiz.queryCateringsPagesByConditions(null, "id", IBaseDao.SortBy.ASC, 1, 30, false);
        for (Catering catering : domainPage.getDomains()) {
            resourcesDao.removeEntity(catering);
        }
        DomainPage<Destination> destinationDomainPage = destinationBiz.getDestinationsByPage(1, 20);
        for (Destination destination : destinationDomainPage.getDomains()) {
            resourcesDao.removeEntity(destination);
        }

    }
}
