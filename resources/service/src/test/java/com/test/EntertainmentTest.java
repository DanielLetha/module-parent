package com.test;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.resources.Entertainment;
import com.simpletour.service.resources.IResourcesService;
import com.simpletour.service.resources.error.ResourcesServiceError;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：resourcesService中娱乐相关接口测试
 * Date: 2016/3/23
 * Time: 15:23
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class EntertainmentTest extends AbstractTestNGSpringContextTests {

    private long id;

    @Resource
    private IResourcesService resourcesService;

    private Destination destination1;


    @BeforeClass
    public void setUp() {
//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
        Area area = new Area();
        area.setId(110100L);
        Destination destination = new Destination(generateName(), "目的地1", "目的地1", area, new BigDecimal(170), new BigDecimal(70));
        destination1 = resourcesService.addDestination(destination).get();
    }

    //test: 正常添加
    @Test
    public void testAddEntertainment() {
        Entertainment entertainment = generateEntertainment();
        Optional<Entertainment> entertainmentOptional = resourcesService.addEntertainment(entertainment);
        Assert.assertTrue(entertainmentOptional.isPresent());
        Assert.assertNotNull(entertainmentOptional.get().getId());
        id = entertainmentOptional.get().getId();
    }

    //test:添加空的entertainment，entertainment中目的地为空，目的地id为空
    @Test
    public void testAddEntertainmentNull() {
        try {
            resourcesService.addEntertainment(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }

        Entertainment entertainment = generateEntertainment();
        entertainment.setDestination(null);
        try {
            resourcesService.addEntertainment(entertainment);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }

        Destination destination = new Destination();
        entertainment.setDestination(destination);
        try {
            resourcesService.addEntertainment(entertainment);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    //test:entertainment目的地不存在
    @Test
    public void testAddEntertainmentDestinationNotExist() {
        Destination destination = new Destination();
        destination.setId(Long.MAX_VALUE);
        Entertainment entertainment = generateEntertainment();
        entertainment.setDestination(destination);
        try {
            resourcesService.addEntertainment(entertainment);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
    }

    //test:正常更新entertainment
    @Test(dependsOnMethods = "testAddEntertainment")
    public void testUpdateEntertainment() {
        Entertainment entertainment = generateEntertainment();
        entertainment = resourcesService.getEntertainmentById(id).get();
        Assert.assertNotNull(entertainment);
        String name = generateName();
        Integer version = entertainment.getVersion() + 1;
        entertainment.setName(name);
        entertainment = resourcesService.updateEntertainment(entertainment).get();
        Assert.assertNotNull(entertainment);
        Assert.assertEquals(version, entertainment.getVersion());
        Assert.assertEquals(name, entertainment.getName());
    }

    //test:更新空的entertainment，entertainment中目的地为空，目的地id为空
    @Test
    public void testUpdateEntertainmentNull() {
        try {
            resourcesService.updateEntertainment(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }

        Entertainment entertainment = generateEntertainment();
        entertainment.setDestination(null);
        try {
            resourcesService.updateEntertainment(entertainment);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }

        Destination destination = new Destination();
        entertainment.setDestination(destination);
        try {
            resourcesService.updateEntertainment(entertainment);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    //test:更新entertainment目的地不存在
    @Test
    public void testUpdateEntertainmentDestinationNotExist() {
        Destination destination = new Destination();
        destination.setId(Long.MAX_VALUE);
        Entertainment entertainment = resourcesService.getEntertainmentById(id).get();
        entertainment.setDestination(destination);
        try {
            resourcesService.updateEntertainment(entertainment);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
    }

    //test:更新entertainment不存在
    @Test
    public void testUpdateEntertainmentNotExist() {
        Entertainment entertainment = resourcesService.getEntertainmentById(id).get();
        entertainment.setId(Long.MAX_VALUE);
        try {
            resourcesService.updateEntertainment(entertainment);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    //test:正常删除
    @Test
    public void testDeleteEntertainment() {
        Entertainment entertainment = generateEntertainment();
        entertainment = resourcesService.addEntertainment(entertainment).get();
        Assert.assertNotNull(entertainment);
        Long idDel = entertainment.getId();
        resourcesService.deleteEntertainment(idDel);
        Entertainment entertainment1 = resourcesService.getEntertainmentById(idDel).get();
        Assert.assertTrue(entertainment1.getDel());
    }

    //test:删除的id不存在
    @Test
    public void testDeleteEntertainmentNotExist() {
        try {
            resourcesService.deleteEntertainment(Long.MAX_VALUE);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    @Test(dependsOnMethods = "testAddEntertainment")
    public void testGetEntertainmentById() {
        Optional<Entertainment> entertainmentOptional = resourcesService.getEntertainmentById(id);
        Assert.assertTrue(entertainmentOptional.isPresent());
    }

    @Test(dependsOnMethods = "testAddEntertainment")
    public void testGetEntertainmentsByPage() {
        printPage(resourcesService.getEntertainmentsByPage(1));
        printPage(resourcesService.getEntertainmentsByPage(1, 12));
    }

    @Test(dependsOnMethods = "testAddEntertainment")
    public void testGetEntertainmentsByConditionPage() {
        printPage(resourcesService.getEntertainmentsByConditionPage("others", "", "", 1, 10));
    }


    private void printPage(DomainPage<Entertainment> page) {
        System.out.println("page size:" + page.getPageSize() + ";page count:" +
                page.getPageCount() + ";page index:" + page.getPageIndex());
        System.out.println("name\ttype\tdel");
        page.getDomains().forEach(tmp -> {
            System.out.println(tmp.getName() + "\t" + tmp.getType().getRemark() + "\t" + tmp.getDel());
        });
    }

    private Entertainment generateEntertainment() {
        return new Entertainment(generateName(), Entertainment.Type.activity, "add address", "add remark",
                destination1, new BigDecimal(170), new BigDecimal(80));
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


