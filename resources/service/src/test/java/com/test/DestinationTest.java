package com.test;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.domain.DomainPage;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by yangdongfeng on 2015/11/21.
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class DestinationTest extends AbstractTestNGSpringContextTests {
    private long id;

    @Resource
    private IResourcesService IDestinationService;

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
    }

    /**
     * 正常添加
     */
    @Test
    public void testAddDestination() {
        Area area = new Area();
        area.setId(110000L);
        String destName = generateName();
        Destination dest = new Destination(destName, "test address", "test remark", area, new BigDecimal(180), new BigDecimal(90));
        dest.setTenantId(1L);
        Optional<Destination> destination = IDestinationService.addDestination(dest);
        Assert.assertTrue(destination.isPresent(), "保存失败");
        id = destination.get().getId();
    }

    /**
     * 测试：AREA_NOT_EXIST
     */
    @Test
    public void testAddDestinationAreaNotExist() {
        Area area = new Area();
        area.setId(110000L);
        String destName = generateName();
        Destination dest = new Destination(destName, "test address", "test remark", area, new BigDecimal(180), new BigDecimal(90));
        dest.setTenantId(1L);
        try {
            Area tarea = new Area();
            tarea.setId(0L);
            dest.setArea(tarea);
            Optional<Destination> res = IDestinationService.addDestination(dest);
            Assert.assertTrue(res.isPresent(), "添加失败");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.AREA_NOT_EXIST);
        }
    }

    /**
     * 测试：相同地区，相同名称
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void addDestinationSameAreaSameName() {
        Optional<Destination> dest = IDestinationService.getDestinationById(id);
        Destination destination = dest.get();
        Destination destNew = new Destination(destination.getName(), destination.getAddress(), destination.getRemark(), destination.getArea(), new BigDecimal(180), new BigDecimal(90));
        destNew.setTenantId(1L);
        try {
            Optional<Destination> res = IDestinationService.addDestination(destNew);
            Assert.assertTrue(res.isPresent(), "添加失败");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.DESTINATION_NAME_UNDER_SAME_AREA_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试：不同地区，相同名称
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void addDestinationDifAreaSameName() {
        Optional<Destination> dest = IDestinationService.getDestinationById(id);
        Destination destination = dest.get();
        Destination destNew = new Destination(destination.getName(), destination.getAddress(), destination.getRemark(), destination.getArea(), new BigDecimal(180), new BigDecimal(90));
        Area area = new Area();
        area.setId(110100L);
        destNew.setArea(area);
        destNew.setTenantId(1L);

        Optional<Destination> res = IDestinationService.addDestination(destNew);
        Assert.assertTrue(res.isPresent(), "添加成功");
    }

    /**
     * 测试:通过id获取目的地
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void getDestinationFail() {
        Optional<Destination> destination = IDestinationService.getDestinationById(Long.MAX_VALUE);
        Assert.assertEquals(destination.isPresent(), false);
    }

    /**
     * 测试：通过id获取目的地
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void getDestinationSuccess() {
        Optional<Destination> destination = IDestinationService.getDestinationById(id);
        Assert.assertEquals(destination.isPresent(), true);
    }

    private void printDestinationList(DomainPage<Destination> dests) {
        for (Destination dest : dests.getDomains()) {
            System.out.println("Id:" + dest.getId() + " Name:" + dest.getName()
                    + " Address:" + dest.getAddress());
        }
        System.out.println("");
    }

    @Test
    public void getDestinationList() {
        System.out.println("Page:1");
        DomainPage<Destination> dests = IDestinationService.getDestinationsByPage(1);
        printDestinationList(dests);

        System.out.println("Page:1000");
        dests = IDestinationService.getDestinationsByPage(1000);
        printDestinationList(dests);

        System.out.println("Page:2, PageSize:5");
        dests = IDestinationService.getDestinationsByPage(2, 5);
        printDestinationList(dests);
    }


    /**
     * 测试：正常添加
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void updateDestinationSuccess() {
        Optional<Destination> destinationOptional = IDestinationService.getDestinationById(id);
        Assert.assertTrue(destinationOptional.isPresent());
        Destination destination = destinationOptional.get();
        Integer version = destination.getVersion();
        String newName = generateName();
        destination.setName(newName);
        destination.setAddress("update address");
        destination.setRemark("update remark");

        Optional<Destination> updateDestination = IDestinationService.updateDestination(destination);

        Assert.assertTrue(updateDestination.isPresent());
        Assert.assertEquals(updateDestination.get().getName(), newName);
        Assert.assertEquals(new Integer(version + 1), updateDestination.get().getVersion());
    }


    /**
     * 测试：目的地不存在
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testUpdateDestinationAreaNotExist() {
        Optional<Destination> destinationOptional = IDestinationService.getDestinationById(id);
        Assert.assertTrue(destinationOptional.isPresent());
        Destination destination = destinationOptional.get();
        Destination dest = new Destination(destination.getName(), "update address", "update remark", destination.getArea(), new BigDecimal(180), new BigDecimal(90));
        dest.setTenantId(1L);
        Area tarea = new Area();
        tarea.setId(0L);
        dest.setArea(tarea);
        try {
            Optional<Destination> res = IDestinationService.addDestination(dest);
            Assert.assertTrue(res.isPresent(), "添加失败");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.AREA_NOT_EXIST);
        }
    }

    /**
     * 测试：相同地区、目的地重名
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testUpdateDestinationSameAreaSameName() {
        Optional<Destination> destinationOptionalOld=IDestinationService.getDestinationById(id);
        Assert.assertTrue(destinationOptionalOld.isPresent(), "更新对象不存在");
        Destination destinationOld = destinationOptionalOld.get();

        Area area = new Area();
        area.setId(110104L);
        Destination destinationNew = new Destination(generateName(), "test address", "test remark", area, new BigDecimal(170), new BigDecimal(70));
        Optional<Destination> destinationOptionalNew = IDestinationService.addDestination(destinationNew);
        Assert.assertTrue(destinationOptionalNew.isPresent(), "添加失败");
        destinationNew=destinationOptionalNew.get();

        Destination destinationUpdate = new Destination(destinationNew.getName(), "update address", "update remark", destinationNew.getArea(), new BigDecimal(180), new BigDecimal(90));
        destinationUpdate.setId(destinationOld.getId());
        try {
            Optional<Destination> destinationAfterUpdate = IDestinationService.updateDestination(destinationUpdate);
            //此处需要更新失败
            Assert.assertFalse(destinationAfterUpdate.isPresent(), "此处应该更新失败");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.DESTINATION_NAME_UNDER_SAME_AREA_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试：不同地区目的重名
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testUpdateDestinationDifAreaSameName() {
        Optional<Destination> destinationOptionalOld=IDestinationService.getDestinationById(id);
        Assert.assertTrue(destinationOptionalOld.isPresent(), "更新对象不存在");
        Destination destinationOld = destinationOptionalOld.get();

        Area area = new Area();
        area.setId(110104L);
        Destination destination = new Destination(generateName(), "test address", "test remark", area, new BigDecimal(170), new BigDecimal(70));

        Optional<Destination> destinationOptionalNew = IDestinationService.addDestination(destination);
        Assert.assertTrue(destinationOptionalNew.isPresent(), "添加失败");

        destinationOld.setName(destinationOptionalNew.get().getName());

        Optional<Destination> destinationAfterUpdate = IDestinationService.updateDestination(destinationOld);

        Assert.assertTrue(destinationAfterUpdate.isPresent(), "更新失败，与预期不符");
    }


    /**
     * 删除目的地
     * @throws Exception
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testDeleteDestination() throws Exception {
        Area area=new Area();
        area.setId(110104L);
        Destination destinationDel=new Destination(generateName(),"delete address","delete remark",area,new BigDecimal(170),new BigDecimal(70));
        Optional<Destination> destination = IDestinationService.addDestination(destinationDel);
        Assert.assertFalse(destination.get().getDel(), "已经被删除");
        IDestinationService.deleteDestination(destination.get().getId());
        destination = IDestinationService.getDestinationById(destination.get().getId());
        Assert.assertTrue(!destination.isPresent()||destination.get().getDel(), "删除失败");
    }
}
