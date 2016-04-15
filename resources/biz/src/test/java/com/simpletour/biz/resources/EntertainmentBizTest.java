package com.simpletour.biz.resources;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.resources.Entertainment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：娱乐测试
 * Date: 2016/3/23
 * Time: 15:57
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class EntertainmentBizTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private IEntertainmentBiz entertainmentBiz;

    @Autowired
    private IDestinationBiz destinationBiz;

    private Destination destination1;
    private Destination destination2;

    private Long id;


    @BeforeClass
    public void setUp() {
//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
        Area area = new Area();
        area.setId(110101L);
        destination1 = new Destination(generateName(), "添加娱乐目的地1",
                "住宿点目的地1", area, new BigDecimal(120), new BigDecimal(70));
        destination2 = new Destination(generateName(), "添加娱乐目的地2",
                "住宿点目的地2", area, new BigDecimal(120), new BigDecimal(70));
        destination1 = destinationBiz.addDestination(destination1);
        Assert.assertNotNull(destination1, "添加目的地1失败");
        destination2 = destinationBiz.addDestination(destination2);
        Assert.assertNotNull(destination2, "添加目的地2失败");
    }

    //test:正常添加娱乐
    @Test
    public void testAddEntertainment() throws Exception {
        Entertainment entertainment = generateEntertainment();
        entertainment = entertainmentBiz.addEntertainment(entertainment);
        Assert.assertNotNull(entertainment, "添加失败");
        id = entertainment.getId();
    }

    //test:相同目的地，存在重名
    @Test
    public void testAddEntertainmentSameName() throws Exception {
        Entertainment entertainment = generateEntertainment();
        entertainment = entertainmentBiz.addEntertainment(entertainment);
        Entertainment entertainment1 = generateEntertainment();
        entertainment1.setName(entertainment.getName());
        try {
            entertainmentBiz.addEntertainment(entertainment1);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
        }
    }

    //test:不同目的地，存在重名
    @Test
    public void testAddEntertainmentDifDestinationSameName() throws Exception {
        Entertainment entertainment = generateEntertainment();
        entertainment = entertainmentBiz.addEntertainment(entertainment);
        Entertainment entertainment1 = generateEntertainment();
        entertainment1.setName(entertainment.getName());
        entertainment1.setDestination(destination2);

        entertainment1 = entertainmentBiz.addEntertainment(entertainment1);
        Assert.assertNotNull(entertainment1);
        Assert.assertNotNull(entertainment1.getId());

    }

    //test:正常更新
    @Test(dependsOnMethods = "testAddEntertainment")
    public void testUpdateEntertainment() throws Exception {
        Entertainment entertainment = entertainmentBiz.getEntertainmentById(id);
        Integer version=entertainment.getVersion()+1;
        String name=generateName();
        entertainment.setName(name);
        entertainment=entertainmentBiz.updateEntertainment(entertainment);
        Assert.assertNotNull(entertainment);
        Assert.assertEquals(version,entertainment.getVersion());
        Assert.assertEquals(name,entertainment.getName());
    }
    //test: 相同目的地，存在重名；
    @Test(dependsOnMethods = "testAddEntertainment")
    public void testUpdateEntertainmentSameName() throws Exception {
        Entertainment entertainment=entertainmentBiz.getEntertainmentById(id);

        Entertainment entertainment1=generateEntertainment();
        entertainment1=entertainmentBiz.addEntertainment(entertainment1);
        Assert.assertNotNull(entertainment1);

        entertainment1.setName(entertainment.getName());
        try {
            entertainmentBiz.updateEntertainment(entertainment1);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
        }
    }

    //test: 不同目的地，存在重名
    @Test(dependsOnMethods = "testAddEntertainment")
    public void testUpdateEntertainmentDifDestinationSameName() throws Exception {
        Entertainment entertainment=entertainmentBiz.getEntertainmentById(id);

        Entertainment entertainment1=generateEntertainment();
        entertainment1=entertainmentBiz.addEntertainment(entertainment1);
        Assert.assertNotNull(entertainment1);

        Integer version=entertainment1.getVersion()+1;
        String name=entertainment.getName();
        entertainment1.setName(name);
        entertainment1.setDestination(destination2);
        entertainment1=entertainmentBiz.updateEntertainment(entertainment1);

        Assert.assertNotNull(entertainment1);
        Assert.assertEquals(version,entertainment.getVersion());
        Assert.assertEquals(name,entertainment.getName());
    }

    //test:删除
    @Test(dependsOnMethods = "testAddEntertainment")
    public void testDeleteEntertainment() throws Exception {
        Entertainment entertainment=generateEntertainment();
        entertainment = entertainmentBiz.addEntertainment(entertainment);
        Assert.assertNotNull(entertainment);
        Assert.assertFalse(entertainment.getDel());

        Long idDel=entertainment.getId();
        entertainmentBiz.deleteEntertainment(idDel);

        Entertainment entertainment1 = entertainmentBiz.getEntertainmentById(idDel);
        Assert.assertTrue(entertainment1.getDel());
    }

    //test:依据id查询
    @Test(dependsOnMethods = "testAddEntertainment")
    public void testGetEntertainmentById() throws Exception {
        Entertainment entertainment=entertainmentBiz.getEntertainmentById(id);
        Assert.assertNotNull(entertainment);
        Assert.assertEquals(entertainment.getId(),id);
    }

    //test:依据id判断是否存在
    @Test(dependsOnMethods = "testAddEntertainment")
    public void testIsExist() throws Exception {
        //存在的id
        Assert.assertTrue(entertainmentBiz.isExisted(id));
        //不存在的id
        Assert.assertFalse(entertainmentBiz.isExisted(Long.MAX_VALUE));

        Entertainment entertainment=generateEntertainment();
        entertainment = entertainmentBiz.addEntertainment(entertainment);
        Long idDel=entertainment.getId();
        entertainmentBiz.deleteEntertainment(idDel);
        //被删除的id
        Assert.assertFalse(entertainmentBiz.isExisted(idDel));
    }

    @Test(dependsOnMethods = "testAddEntertainment")
    public void testGetEntertainmentsByPage() throws Exception {
        printPage(entertainmentBiz.getEntertainmentsByPage(1,10));
    }

    @Test(dependsOnMethods = "testAddEntertainment")
    public void testGetEntertainmentsByConditionPage() throws Exception {
        printPage(entertainmentBiz.getEntertainmentsByConditionPage("others","","",1,10));
    }

    private void printPage(DomainPage<Entertainment> page){
        System.out.println("page size:"+page.getPageSize()+";page count:"+
                page.getPageCount()+";page index:"+page.getPageIndex());
        System.out.println("name\ttype\tdel");
        page.getDomains().forEach(tmp->{
            System.out.println(tmp.getName()+"\t"+tmp.getType().getRemark()+"\t"+tmp.getDel());
        });
    }

    private Entertainment generateEntertainment() {
        return new Entertainment(generateName(), Entertainment.Type.activity, "add address", "add remark",
                destination1, new BigDecimal(170), new BigDecimal(80));
    }

    private static String generateName() {
        String name = "";
        for (int i = 0; i < 20; ++i) {
            Integer idx = Double.valueOf(Math.random() * 25).intValue();
            char le = (char) ('a' + idx);
            name += le;
        }
        return name;
    }
}