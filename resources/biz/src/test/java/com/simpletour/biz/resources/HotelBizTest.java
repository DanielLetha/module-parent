package com.simpletour.biz.resources;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.common.security.token.EncryptedToken;
import com.simpletour.common.security.token.Token;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.resources.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XuHui/xuhui@simpletour.com
 *         Date: 2016/3/22
 *         Time: 14:25
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class HotelBizTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private IHotelBiz hotelBiz;
    @Autowired
    private IDestinationBiz destinationBiz;
    @Autowired
    private IResourcesBiz resourcesBiz;
    @Autowired
    private IOspBiz ospBiz;

    private Destination destination1;
    private Destination destination2;
    private Destination destination3;

    private Long id;

    /**
     * 设置token，
     * 添加目的地1；
     * 添加目的地2；
     */
    @BeforeClass
    public void setUp() throws Exception {
        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
        Area area = new Area();
        area.setId(110101L);
        destination1 = new Destination(generateName(), "添加住宿点目的地1",
                "住宿点目的地1", area, new BigDecimal(120), new BigDecimal(70));
        destination2 = new Destination(generateName(), "添加住宿点目的地2",
                "住宿点目的地2", area, new BigDecimal(120), new BigDecimal(70));
        destination3 = new Destination(generateName(), "添加住宿点目的地3",
                "住宿点目的地3", area, new BigDecimal(120), new BigDecimal(70));
        destination1 = destinationBiz.addDestination(destination1);
        Assert.assertNotNull(destination1, "添加目的地1失败");
        destination2 = destinationBiz.addDestination(destination2);
        Assert.assertNotNull(destination2, "添加目的地2失败");
        destination3 = destinationBiz.addDestination(destination3);
        Assert.assertNotNull(destination3, "添加目的地3失败");
    }

    //test: 正常添加住宿点
    @Test
    public void testAddHotel() throws Exception {
        Hotel hotel = new Hotel(generateName(), "test address", "test remark",
                Hotel.StayType.hotel, destination1, new BigDecimal(170), new BigDecimal(70));
        hotel = hotelBiz.addHotel(hotel);
        Assert.assertNotNull(hotel, "添加失败");
        id = hotel.getId();
    }

    //test: 存在相同的名称
    @Test(dependsOnMethods = "testAddHotel")
    public void testAddHotelSameName() {
        Hotel hotel = hotelBiz.getHotelById(id);
        Hotel hotel1 = new Hotel(hotel.getName(), hotel.getAddress(), hotel.getRemark(),
                hotel.getType(), hotel.getDestination(), hotel.getLon(), hotel.getLat());
        try {
            hotelBiz.addHotel(hotel1);
            Assert.assertNotNull(hotel1, "添加失败");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
        }

        hotel1.setDestination(destination2);
        hotel1 = hotelBiz.addHotel(hotel1);
        Assert.assertNotNull(hotel1, "不同目的地相同名称，添加失败");
    }

    //test: 正常更新
    @Test(dependsOnMethods = "testAddHotel")
    public void testUpdateHotel() throws Exception {
        Hotel hotel = hotelBiz.getHotelById(id);
        String updateName = generateName();
        int version = hotel.getVersion() + 1;
        hotel.setName(updateName);
        hotel = hotelBiz.updateHotel(hotel);
        Assert.assertNotNull(hotel);
        Assert.assertEquals(new Integer(version), hotel.getVersion(), "版本未变化");
    }

    //test: 存在相同的名称
    @Test(dependsOnMethods = "testAddHotel")
    public void testUpdateHotelSameName() {
        Hotel hotel = hotelBiz.getHotelById(id);
        String nameNew = generateName();
        Hotel hotel1 = new Hotel(nameNew, hotel.getAddress(), hotel.getRemark(),
                hotel.getType(), hotel.getDestination(), hotel.getLon(), hotel.getLat());
        hotel1 = hotelBiz.addHotel(hotel1);
        Assert.assertNotNull(hotel1, "添加失败");

        hotel.setName(nameNew);
        try {
            hotelBiz.updateHotel(hotel);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
        }

        hotel.setDestination(destination2);
        hotel = hotelBiz.updateHotel(hotel1);
        Assert.assertNotNull(hotel, "不同目的地相同名称，更新失败，应该可以更新");
    }

    @Test
    public void testQueryHotelsPagesByConditions() throws Exception {
        Map<String, Object> condition = new HashMap<>();
        condition.put("name","%te%");

        DomainPage<Hotel> dests = hotelBiz.queryHotelsPagesByConditions(condition, null,null, 1, 10,true);
    }

    //test:正常删除
    @Test
    public void testDeleteHotelLogic() throws Exception {
        Hotel hotel = new Hotel(generateName(), "delete address", "delete remark",
                Hotel.StayType.hotel, destination1, new BigDecimal(170), new BigDecimal(70));
        hotel = hotelBiz.addHotel(hotel);
        Assert.assertNotNull(hotel);
        Long id = hotel.getId();
        hotelBiz.deleteHotel(id);
        Hotel hotel1 = hotelBiz.getHotelById(id);
        Assert.assertNotNull(hotel1);
        Assert.assertTrue(hotel1.getDel(), "逻辑删除失败");
    }

//    //test:存在依赖的删除
//    @Test
//    public void testDeleteHotelWithDependent() {
//        Hotel hotel = new Hotel(generateName(), "delete address", "delete remark",
//                Hotel.StayType.hotel, destination1, new BigDecimal(170), new BigDecimal(70));
//        Hotel hotel1 = hotelBiz.addHotel(hotel);
//        Assert.assertNotNull(hotel1, "添加失败");
//        Long hotelId = hotel1.getId();
//        OfflineServiceProvider osp = new OfflineServiceProvider();
//        osp.setName(generateName());
//        osp.setRemark("测试供应商");
//        osp = ospBiz.addOfflineServiceProvider(osp);
//        Assert.assertNotNull(osp, "添加供应商失败");
//
//        Procurement procurement = new Procurement(null, Procurement.ResourceType.hotel,
//                hotelId, "删除测试", "name:删除测试", destination1, Boolean.FALSE, null, osp);
//        Optional<Procurement> procurement1 = resourcesBiz.addProcurement(procurement);
//        Assert.assertTrue(procurement1.isPresent(), "添加元素失败");
//        try {
//            hotelBiz.deleteHotel(hotelId);
//        } catch (BaseSystemException e) {
//            Assert.assertEquals(e.getError(), ResourcesBizError.CANNOT_DEL_DEPENDENT_RESOURCE);
//        }
//    }

    //test:测试通过id获取住宿点
    @Test(dependsOnMethods = "testAddHotel")
    public void testGetHotelById() throws Exception {
        Hotel hotel = hotelBiz.getHotelById(id);
        Assert.assertNotNull(hotel, "查询失败");
    }

    @Test
    public void testIsHotelExist(){
        Hotel hotel=new Hotel(generateName(),"hotel address","hotel remark", Hotel.StayType.hotel,
                destination1,new BigDecimal(170),new BigDecimal(70));
        hotel = hotelBiz.addHotel(hotel);
        Long idDel=hotel.getId();
        Assert.assertNotNull(hotel,"添加失败");
        Assert.assertTrue(hotelBiz.isExisted(hotel.getId()),"判定失败");
        hotelBiz.deleteHotel(idDel);
        Assert.assertFalse(hotelBiz.isExisted(idDel),"判定失败");
        Assert.assertFalse(hotelBiz.isExisted(Long.MAX_VALUE),"判定失败");
    }

    public static String generateName() {
        String name = "";
        for (int i = 0; i < 20; ++i) {
            Integer idx = Double.valueOf(Math.random() * 25).intValue();
            char le = (char) ('a' + idx);
            name += le;
        }
        return name;
    }
}