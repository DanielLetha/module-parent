package com.simpletour.biz.resources;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Destination;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * User: XuHui
 * Date: 2016/3/21
 * Time: 14:48
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class DestinationBizTest extends AbstractTestNGSpringContextTests {
    private long id;

    @Resource
    private IDestinationBiz destinationBiz;

    @BeforeClass
    public void setUp() throws Exception {
//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
    }

    /**
     * 测试：正常添加
     *
     * @throws Exception
     */
    @Test
    public void testAddDestination() throws Exception {
        Area area = new Area();
        area.setId(110000L);

        String destName = generateName();
        Destination dest = new Destination(destName, "test address", "test remark", area, new BigDecimal(80), new BigDecimal(90));
//        dest.setTenantId(1L);
        Destination res = destinationBiz.addDestination(dest);
        Assert.assertNotNull(res, "添加失败");
        id = res.getId();
    }

    /**
     * 测试：相同地区，相同名称
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void addDestinationSameAreaSameName() {
        Destination dest = destinationBiz.getDestinationById(id);

        Destination destNew = new Destination(dest.getName(), dest.getAddress(), dest.getRemark(), dest.getArea(), new BigDecimal(180), new BigDecimal(90));

        try {
            Destination res = destinationBiz.addDestination(destNew);
            Assert.assertNotNull(res, "添加失败");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.DESTINATION_NAME_UNDER_SAME_AREA_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试：不同地区，相同名称
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void addDestinationDifAreaSameName() {
        Destination dest = destinationBiz.getDestinationById(id);
        Destination destNew = new Destination(dest.getName(), dest.getAddress(), dest.getRemark(), dest.getArea(), new BigDecimal(180), new BigDecimal(90));
        Area area = new Area();
        area.setId(110100L);
        destNew.setArea(area);
//        destNew.setTenantId(1L);

        Destination res = destinationBiz.addDestination(destNew);
        Assert.assertNotNull(res, "添加成功");
    }

    /**
     * 测试：正常更新
     *
     * @throws Exception
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testUpdateDestination() throws Exception {
        Destination destination = destinationBiz.getDestinationById(id);
        Integer version = destination.getVersion();
        destination.setName(generateName());
        String newName = destination.getName();
        String newAddress = "update Address";
        destination.setAddress(newAddress);
        Destination destination1 = destinationBiz.updateDestination(destination);
        Assert.assertEquals(newName, destination1.getName(), "更新未写入");
        Assert.assertEquals(newAddress, destination1.getAddress(), "更新未写入");
        Assert.assertEquals(new Integer(version + 1), destination1.getVersion(), "版本号未更新");
    }

    /**
     * 测试：相同地区目的重名
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testUpdateDestinationSameAreaSameName() {
        Destination destinationOld = destinationBiz.getDestinationById(id);
        Assert.assertNotNull(destinationOld, "更新对象不存在");

        Area area = new Area();
        area.setId(110104L);
        Destination destinationNew = new Destination(generateName(), "test address", "test remark", area, new BigDecimal(170), new BigDecimal(70));
        destinationNew = destinationBiz.addDestination(destinationNew);
        Assert.assertNotNull(destinationNew, "添加失败");

        Destination destinationUpdate = new Destination(destinationNew.getName(), "update address", "update remark", destinationNew.getArea(), new BigDecimal(180), new BigDecimal(90));
        destinationUpdate.setId(destinationOld.getId());
        try {
            Destination destinationAfterUpdate = destinationBiz.updateDestination(destinationUpdate);
            //此处需要更新失败
            Assert.assertNull(destinationAfterUpdate, "此处应该更新失败");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.DESTINATION_NAME_UNDER_SAME_AREA_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试：不同地区目的重名
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testUpdateDestinationDifAreaSameName() {
        Destination destinationOld = destinationBiz.getDestinationById(id);
        Assert.assertNotNull(destinationOld, "更新对象不存在");

        Area area = new Area();
        area.setId(110104L);
        Destination destination = new Destination(generateName(), "test address", "test remark", area, new BigDecimal(170), new BigDecimal(70));

        Destination destinationNew = destinationBiz.addDestination(destination);
        Assert.assertNotNull(destinationNew, "添加失败");

        destinationOld.setName(destinationNew.getName());

        Destination destinationAfterUpdate = destinationBiz.updateDestination(destinationOld);

        Assert.assertNotNull(destinationAfterUpdate, "更新失败，与预期不符");
    }

    @Test(dependsOnMethods = "testAddDestination")
    public void testDeleteDestination() throws Exception {
        Destination destination = destinationBiz.getDestinationById(id);
        Assert.assertFalse(destination.getDel(), "已经被删除");
        destinationBiz.deleteDestination(id);
        destination = destinationBiz.getDestinationById(id);
        Assert.assertTrue(destination.getDel(), "删除失败");
    }

    @Test
    public void testGetDestinationsByPage() {
        System.out.println("Page:1");
        DomainPage<Destination> dests = destinationBiz.getDestinationsByPage(1, 10);
        printDestinationList(dests);

        System.out.println("Page:1000");
        dests = destinationBiz.getDestinationsByPage(1000, 10);
        printDestinationList(dests);

        System.out.println("Page:2, PageSize:5");
        dests = destinationBiz.getDestinationsByPage(2, 5);
        printDestinationList(dests);
    }

    @Test
    public void testGetDestionationsByConditonPage() {
        DomainPage<Destination> dests = destinationBiz.getDestionationsByConditonPage("te", "北", 1, 10);
        printDestinationList(dests);

        dests = destinationBiz.getDestionationsByConditonPage("te", "not exists area", 1, 10);
        Assert.assertEquals(dests.getDomains().size(), 0);

        destinationBiz.getDestionationsByConditonPage("te", "", 1, 10);
    }

    /**
     * 测试：查询不存在id；
     *
     * @throws Exception
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testGetDestinationByIdNotExist() throws Exception {
        Destination destination = destinationBiz.getDestinationById(Long.MAX_VALUE);
        Assert.assertNull(destination);
    }

    /**
     * 测试：正确查询id；
     *
     * @throws Exception
     */
    @Test(dependsOnMethods = "testAddDestination")
    public void testGetDestinationById() throws Exception {
        Destination destination = destinationBiz.getDestinationById(id);
        Assert.assertNotNull(destination, "查询失败");
    }

    @Test
    public void testIsExistDestination(){
        Area area=new Area();
        area.setId(110104L);
        Destination destination=new Destination(generateName(),"exist destination","esist destination",area,new BigDecimal(170),new BigDecimal(70));
        destination=destinationBiz.addDestination(destination);
        Assert.assertNotNull(destination);
        Long idDel=destination.getId();
        Assert.assertTrue(destinationBiz.isExisted(destination.getId()),"无法查询");
        destinationBiz.deleteDestination(idDel);
        Assert.assertFalse(destinationBiz.isExisted(destination.getId()),"无法查询");
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

    private void printDestinationList(DomainPage<Destination> dests) {
        for (Destination dest : dests.getDomains()) {
            System.out.println("Id:" + dest.getId() + " Name:" + dest.getName()
                    + " Address:" + dest.getAddress());
        }
        System.out.println("");
    }
}