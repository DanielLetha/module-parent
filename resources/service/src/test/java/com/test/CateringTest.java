package com.test;

import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Catering;
import com.simpletour.domain.resources.Destination;
import com.simpletour.service.resources.IResourcesService;
import com.simpletour.service.resources.error.ResourcesServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Brief :  ${用途}
 * Author:  liangfei
 * Mail  :  liangfei@simpletour.com
 * Date  :  2016/3/23 17:14
 * Remark:  ${Remark}
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
@Transactional
public class CateringTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    protected IResourcesService resourcesService;

    @Autowired
    protected IResourcesDao resourcesDao;

    private Long destinationId;

    private Long cateringId;

    @BeforeClass
    public void initData() {
//        new EncryptedToken("0", "0", "12", "2", Token.ClientType.BROWSER);
    }

    /**
     * 使用空的目的地添加餐饮点
     */
    @Test(priority = 1)
    @Rollback(value = true)
    public void addCateringWithNullDestination() {
        Catering catering = new Catering();
        catering.setName("羊杂汤");
        catering.setRemark("冬季养生汤锅");
        catering.setType(Catering.Type.hotel);
        catering.setAddress("都江堰市");
        catering.setDestination(null);
        catering.setLon(new BigDecimal(178.1261));
        catering.setLat(new BigDecimal(87.1693));
        try {
            Optional op =  resourcesService.addCatering(catering);
            Assert.assertNotNull(op.get(),"错误-添加空的餐饮点成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.DESTINATION_NULL);
        }
    }

    /**
     * 使用不存在的目的地添加餐饮点
     */
    @Test(priority = 2)
    @Rollback(value = true)
    public void addCateringWithNotExistDestination() {
        Area area = resourcesService.getAreaById(340400L).get();
        Destination destination = new Destination("国色天乡", "国色天乡马戏停车场", "1", area, BigDecimal.valueOf(21.06), BigDecimal.valueOf(96.15));
        destination.setId(1231L);
        Catering catering = new Catering();
        catering.setName("羊杂汤1");
        catering.setRemark("冬季养生汤锅1");
        catering.setType(Catering.Type.hotel);
        catering.setAddress("都江堰市");
        catering.setDestination(destination);
        catering.setLon(new BigDecimal(178.1261));
        catering.setLat(new BigDecimal(87.1693));
        try {
            Optional<Catering> op=  resourcesService.addCatering(catering);
            Assert.assertNotNull(op.get(),"错误-添加不存在目的地的餐饮点成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
    }

    @Test(priority = 3)
    @Rollback(value = false)
    public void testAddCatering() {
        Area area = resourcesService.getAreaById(340400L).get();
        Destination destination = new Destination("国色天香游乐场", "国色天乡马戏停车场", "1", area, BigDecimal.valueOf(21.06), BigDecimal.valueOf(96.15));
        Optional<Destination> op = resourcesService.addDestination(destination);
        Assert.assertTrue(op.isPresent());
        destinationId = op.get().getId();

        Catering catering = new Catering();
        catering.setName("羊杂汤2");
        catering.setRemark("冬季养生汤锅2");
        catering.setType(Catering.Type.hotel);
        catering.setAddress("都江堰市");
        catering.setTenantId(12L);
        catering.setDestination(op.get());
        catering.setLon(new BigDecimal(178.1261));
        catering.setLat(new BigDecimal(87.1693));
        Optional<Catering> op1 = resourcesService.addCatering(catering);
        Assert.assertTrue(op1.isPresent());

        cateringId = op1.get().getId();
        Assert.assertTrue(op1.isPresent());
    }



    /**
     * 使用空的目的地更新餐饮点
     */
    @Test(priority = 4)
    public void updateCateringWithNullDes() {
        Optional<Catering> optCatering = resourcesService.getCateringById(cateringId);

        Catering catering = optCatering.get();
        String remark = catering.getRemark();
        catering.setTenantId(1L);
        catering.setLon(new BigDecimal(120.253));
        catering.setLat(new BigDecimal(76.809));
        catering.setDestination(null);
        catering.setRemark("营业时间：9:00~23:00");
        try {
            Optional<Catering> op =  resourcesService.updateCatering(catering);
            Assert.assertNotNull(op.get(),"错误-使用空目的地的餐饮点更新成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.DESTINATION_NULL);
        }
    }

    /**
     * 使用不存在的目的地更新餐饮点
     */
    @Test(priority = 5)
    public void updateCateringWithNotExistDes() {
        Optional<Catering> optCatering = resourcesService.getCateringById(cateringId);
        Area area = resourcesService.getAreaById(340400L).get();
        Destination destination = new Destination("国色天乡", "国色天乡马戏停车场", "1", area, BigDecimal.valueOf(21.06), BigDecimal.valueOf(96.15));
        destination.setId(1231L);
        Catering catering = optCatering.get();
        catering.setTenantId(1L);
        catering.setLon(new BigDecimal(120.253));
        catering.setLat(new BigDecimal(76.809));
        catering.setDestination(destination);
        catering.setRemark("营业时间：9:00~23:00");
        try {
            Optional<Catering> op =  resourcesService.updateCatering(catering);
            Assert.assertNotNull(op.get(),"错误-使用不存在的目的地更新餐饮点成功");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
    }


    @Test(priority = 6)
    public void testUpdateCatering(){
        Optional<Catering> op1 =  resourcesService.getCateringById(cateringId);
        op1.get().setName("牛肉汤");
        Optional<Catering> op2 =  resourcesService.updateCatering(op1.get());
        Assert.assertTrue(op2.isPresent());
    }
    @Test(priority = 7)
    public void testQueryCateringById(){
        Optional<Catering> op1 =  resourcesService.getCateringById(cateringId);
        Assert.assertNotNull(op1.get());
    }


    @Test(priority = 8)
    public void tesDelCatering() {
        resourcesService.deleteCatering(cateringId,true);
        Optional<Catering> op =  resourcesService.getCateringById(cateringId);
        Assert.assertFalse(op.isPresent(),"正确-已经被删除的数据无法查询出来");

    }
    @AfterTest
    public void clearData(){
        resourcesDao.removeEntity(resourcesService.getCateringById(cateringId).get());
        resourcesDao.removeEntity(resourcesService.getDestinationById(destinationId).get());
    }
}
