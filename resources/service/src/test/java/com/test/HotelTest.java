package com.test;

import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.service.resources.IResourcesService;
import com.simpletour.service.resources.error.ResourcesServiceError;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import resources.Area;
import resources.Destination;
import resources.Hotel;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Author： XuHui/xuhui@simpletour.com
 * Brief : 住宿点测试
 * Date: 2016/3/23
 * Time: 11:26
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class HotelTest extends AbstractTestNGSpringContextTests {
    private long id;
    @Resource
    private IResourcesService resourcesService;

    private Destination destination1;
    private Destination destination2;

    public static String generateName() {
        String name = "";
        for (int i = 0; i < 20; ++i) {
            Integer idx = Double.valueOf(Math.random() * 25).intValue();
            char le = (char) ('a' + idx);
            name += le;
        }
        return name;
    }

    @BeforeClass
    public void setUp() {
//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
        Area area = new Area();
        area.setId(110100L);
        Destination destination = new Destination(generateName(), "目的地1", "目的地1", area, new BigDecimal(170), new BigDecimal(70));
        destination1 = resourcesService.addDestination(destination).get();
        destination = new Destination(generateName(), "目的地2", "目的地2", area, new BigDecimal(170), new BigDecimal(70));
        destination2 = resourcesService.addDestination(destination).get();
    }

    //test: 正常添加
    @Test
    public void testHotelAdd() {
        Destination destination = new Destination();
        destination.setId(destination1.getId());
        Hotel hotel = new Hotel(generateName(), "test add", "test add",
                Hotel.StayType.hotel, destination, new BigDecimal(170), new BigDecimal(70));
        resourcesService.addHotel(hotel);
    }

    //test:添加空的hotel、hotel中目的地为空、hotel中目的地id为空
    @Test
    public void testHotelAddNull() {
        Hotel hotel = null;
        try {
            resourcesService.addHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
        hotel = new Hotel(generateName(), "hotel address", "hotel reamrk",
                Hotel.StayType.hotel, null, new BigDecimal(170), new BigDecimal(70));
        try {
            resourcesService.addHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }

        Destination destination = new Destination();
        hotel.setDestination(destination);
        try {
            resourcesService.addHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    //test:hotel目的地不存在
    @Test
    public void testHotelDestinationNotExist() {
        Destination destination = new Destination();
        destination.setId(Long.MAX_VALUE);
        Hotel hotel = new Hotel(generateName(), "test add", "test add",
                Hotel.StayType.hotel, destination, new BigDecimal(170), new BigDecimal(70));
        try {
            resourcesService.addHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
    }

    //test:正常更新住宿点
    @Test
    public void testUpdateHotel() {
        Hotel hotel = new Hotel(generateName(), "test hotel address", "test hotel remark",
                Hotel.StayType.hotel, destination1, new BigDecimal(170), new BigDecimal(70));
        hotel = resourcesService.addHotel(hotel).get();
        Assert.assertNotNull(hotel);
        Integer version = hotel.getVersion() + 1;
        String name = generateName();
        hotel.setName(name);
        hotel = resourcesService.updateHotel(hotel).get();
        Assert.assertNotNull(hotel);
        Assert.assertEquals(version, hotel.getVersion(), "版本未更新");
        Assert.assertEquals(name, hotel.getName());
    }

    //test:添加空的hotel、hotel中目的地为空、hotel中目的地id为空
    @Test
    public void testUpdateHotelNull() {
        Hotel hotel = null;
        try {
            resourcesService.updateHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
        hotel = new Hotel(generateName(), "hotel address", "hotel reamrk",
                Hotel.StayType.hotel, null, new BigDecimal(170), new BigDecimal(70));
        try {
            resourcesService.updateHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }

        Destination destination = new Destination();
        hotel.setDestination(destination);
        try {
            resourcesService.updateHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    @Test
    public void testUpdateHotelDestinationNotExist() {
        Destination destination = new Destination();
        destination.setId(Long.MAX_VALUE);
        Hotel hotel = new Hotel(generateName(), "test add", "test add",
                Hotel.StayType.hotel, destination, new BigDecimal(170), new BigDecimal(70));
        try {
            resourcesService.updateHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
    }

    //test: 更新时hotel不存在
    @Test
    public void testUpdateHotelNotExist() {
        Hotel hotel = new Hotel(generateName(), "update address", "update remark", Hotel.StayType.hotel,
                destination1, new BigDecimal(170), new BigDecimal(70));
        hotel.setId(Long.MAX_VALUE);
        try {
            resourcesService.updateHotel(hotel);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    //test:正常删除
    @Test
    public void testDeleteHotel() {
        Hotel hotel = new Hotel(generateName(), "delete name", "delete address", Hotel.StayType.hotel,
                destination1, new BigDecimal(170), new BigDecimal(70));
        hotel = resourcesService.addHotel(hotel).get();
        Long idDel = hotel.getId();
        Assert.assertNotNull(hotel);
        resourcesService.deleteHotel(hotel.getId());
        hotel = resourcesService.getHotelById(idDel).get();
        Assert.assertTrue(hotel.getDel(), "删除失败");
    }

    //test:删除的住宿点不存在
    @Test
    public void testDeltetHotelNotExist() {
        try {
            resourcesService.deleteHotel(Long.MAX_VALUE);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }

    }
}
