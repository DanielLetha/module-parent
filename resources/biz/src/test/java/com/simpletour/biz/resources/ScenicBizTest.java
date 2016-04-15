package com.simpletour.biz.resources;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.resources.Scenic;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
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
 * Brief : 景点的Biz的测试类
 * Author: Hawk
 * Date  : 2016/3/22
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ScenicBizTest extends AbstractTransactionalTestNGSpringContextTests {

    @Resource
    private IScenicBiz scenicBiz;

    @Resource
    private IDestinationBiz destinationBiz;

    @Resource
    private IResourcesDao resourcesDao;

    private Area area = null;
    private Destination destination = null;

    @BeforeClass
    public void setup() {

//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);

        area = new Area();
        area.setId(new Long(110000));

        Destination tempDestination = generateNewDestination();
        destination = destinationBiz.addDestination(tempDestination);
        Assert.assertNotNull(destination);
    }

    @AfterClass
    public void teardown() {
        removeCatchException(destination);
    }

    /**
     * 测试正常情况
     */
    @Test
    public void testAddScenic() {
        Scenic scenic = generateNewScenic();
        Assert.assertNotNull(scenicBiz.addScenic(scenic));
    }

    /**
     * 测试正常情况(带父景点)
     */
    @Test
    public void testAddScenicAndParent() {

        Scenic scenicParent = generateNewScenic();
        Scenic parent = scenicBiz.addScenic(scenicParent);
        Assert.assertNotNull(parent);

        Scenic scenicChild = generateNewScenic();
        scenicChild.setParent(parent);
        Assert.assertNotNull(scenicBiz.addScenic(scenicChild));
    }

    /**
     * 测试Scenic对象为空的情况
     */
    @Test
    public void testAddScenicNull() {
        try {
            scenicBiz.addScenic(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.SCENIC_NULL, e.getError());
        }
    }

    /**
     * 测试增加一个Scenic对象，该对象的del字段为true
     */
    @Test
    public void testAddScenicDel() {
        Scenic scenic = generateNewScenic();
        scenic.setDel(Boolean.TRUE);
        try {
            scenicBiz.addScenic(scenic);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.ADD_SCENIC_DEL, e.getError());
        }
    }

    /**
     * 测试增加景点，在同一个地点下，重名
     */
    @Test
    public void testAddScenicAndRepeatName() {

        String tempName = generateName("test-name");

        Scenic scenic = generateNewScenic();
        scenic.setName(tempName);
        scenicBiz.addScenic(scenic);

        Scenic scenic2 = generateNewScenic();
        scenic2.setName(tempName);
        try {
            scenicBiz.addScenic(scenic2);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.SCENIC_NAME_REPAET_ON_DESTINATION, e.getError());
        }
    }

    /**
     * 景点正常删除
     */
    @Test
    public void testNormalDeleteScenic() {
        Scenic scenic = generateNewScenic();
        Scenic newScenic = scenicBiz.addScenic(scenic);
        Assert.assertNotNull(newScenic);

        scenicBiz.deleteScenic(newScenic.getId());

        Scenic deletedScenic = resourcesDao.getEntityById(Scenic.class, newScenic.getId());
        Assert.assertTrue(deletedScenic.getDel());
    }

    /**
     * 正常情况
     */
    @Test
    public void testUpdateScenic() {
        Scenic scenic = generateNewScenic();
        Scenic tempScenic = scenicBiz.addScenic(scenic);
        Assert.assertNotNull(tempScenic);

        Destination destination2 = generateNewDestination();
        Destination newDestination = destinationBiz.addDestination(destination2);
        Assert.assertNotNull(newDestination);

        Scenic newScenic = new Scenic();
        newScenic.setName("testAddScenicAndChild1");
        newScenic.setAddress("testAddScenicAndChild1");
        newScenic.setRemark("testAddScenicAndChild1");
        newScenic.setLat(new BigDecimal(12.4));
        newScenic.setLon(new BigDecimal(45.7));
        newScenic.setId(tempScenic.getId());
        newScenic.setDestination(newDestination);
        newScenic.setVersion(tempScenic.getVersion());
        Scenic dbScenic = scenicBiz.updateScenic(newScenic);
        Assert.assertNotNull(dbScenic);

        Assert.assertEquals(dbScenic.getName(), "testAddScenicAndChild1");
        Assert.assertEquals(dbScenic.getAddress(), "testAddScenicAndChild1");
        Assert.assertEquals(dbScenic.getRemark(), "testAddScenicAndChild1");
        Assert.assertEquals(dbScenic.getLat(), new BigDecimal(12.4));
        Assert.assertEquals(dbScenic.getLon(), new BigDecimal(45.7));
        Assert.assertEquals(dbScenic.getDestination(), newDestination);
    }

    /**
     * 更新时，Scenic为null
     */
    @Test
    public void testUpdateScenicAndNull() {
        try {
            scenicBiz.updateScenic(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.SCENIC_NULL, e.getError());
        }
    }

    /**
     * 更新时，Scenic的id为null
     */
    @Test
    public void testUpdateScenicAndIdNull() {
        try {
            Scenic newSce = new Scenic();
            scenicBiz.updateScenic(newSce);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.ILLEGAL_ID, e.getError());
        }
    }

    /**
     * 更新时，Scenic的version为null
     */
    @Test
    public void testUPdateScenicAndVersionNull() {
        Scenic scenic = generateNewScenic();
        Scenic dbScenic = scenicBiz.addScenic(scenic);
        Assert.assertNotNull(dbScenic);
        try {
            dbScenic.setVersion(null);
            scenicBiz.updateScenic(dbScenic);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.SCENIC_UPDATE_VERSION_NULL, e.getError());
        }
    }

    /**
     * 更新时，Scenic的UnionEntityKey为null
     */
    @Test
    public void testUpdateScenicAndUnionEntityKeyNull() {
        try {
            Scenic newSce = new Scenic();
            newSce.setId(12L);
            newSce.setVersion(0);
            scenicBiz.updateScenic(newSce);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.INVALID_RESOURCE_AND_DESTINATION, e.getError());
        }
    }

    /**
     * 更新时，Scenic同一个目的地下重名
     */
    @Test
    public void testUpdateScenicAndRepeatName() {

        String tempName = generateName("test-scenic-name");

        Scenic scenic = generateNewScenic();
        scenic.setName(tempName);
        Scenic newSce1 = scenicBiz.addScenic(scenic);
        Assert.assertNotNull(newSce1);

        Scenic scenic2 = generateNewScenic();
        Scenic newSce2 = scenicBiz.addScenic(scenic2);
        Assert.assertNotNull(newSce2);

        try {
            Scenic newSce3 = generateNewScenic();
            newSce3.setName(tempName);
            newSce3.setId(newSce2.getId());
            newSce3.setVersion(newSce2.getVersion());
            scenicBiz.updateScenic(newSce3);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.SCENIC_NAME_REPAET_ON_DESTINATION, e.getError());
        }
    }


    /**
     * 更新时，Scenic与父景点一样
     */
    @Test
    public void testUpdateScenicAndParentSameName() {

        Scenic scenic1 = generateNewScenic();
        Scenic newSce1 = scenicBiz.addScenic(scenic1);
        Assert.assertNotNull(newSce1);

        try {
            Scenic newSce2 = generateNewScenic();
            newSce2.setId(newSce1.getId());
            newSce2.setVersion(newSce1.getVersion());
            newSce2.setParent(newSce1);
            scenicBiz.updateScenic(newSce2);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesBizError.CANNOT_DEPEND_SAME_RESOURCE, e.getError());
        }
    }


    /**
     * 测试根据Scenic的ID，查询Scenic
     */
    @Test
    public void testFindScenicById() {
        Scenic scenic = generateNewScenic();
        Scenic newSce1 = scenicBiz.addScenic(scenic);
        Assert.assertNotNull(newSce1);

        Assert.assertNotNull(scenicBiz.findScenicById(newSce1.getId()));
    }

    /**
     * 根据多条件进行查询,返回list
     */
    @Test
    public void testFindScenicsPagesByConditions() {

        String tempName = generateName("test-scenic-name");
        String tempAddress = generateName("test-scenic-address");
        String tempRemark = generateName("test-scenic-remark");

        Scenic scenic = new Scenic();
        scenic.setDestination(destination);
        scenic.setName(tempName);
        scenic.setAddress(tempAddress);
        scenic.setRemark(tempRemark);
        scenic.setTenantId(1L);
        scenic.setLat(new BigDecimal(12.3));
        scenic.setLon(new BigDecimal(45.6));
        Scenic newSce1 = scenicBiz.addScenic(scenic);
        Assert.assertNotNull(newSce1);

        Map<String, Object> params = new HashMap<>();
        params.put("name", tempName);
        params.put("tenantId", 1L);
        params.put("remark", tempRemark);
        params.put("address", tempAddress);
        params.put("destination.id", destination.getId());
        params.put("destination.name", destination.getName());
        List<Scenic> scenicList = scenicBiz.findScenicsPagesByConditions(params);
        Assert.assertTrue(scenicList != null && !scenicList.isEmpty() && scenicList.size() == 1);

        Scenic dbScenic = scenicList.get(0);
        Assert.assertEquals(dbScenic.getName(), tempName);
        Assert.assertEquals(dbScenic.getAddress(), tempAddress);
        Assert.assertEquals(dbScenic.getRemark(), tempRemark);
        Assert.assertEquals(dbScenic.getTenantId(), Long.valueOf(1L));
        Assert.assertEquals(dbScenic.getDestination().getId(), destination.getId());
        Assert.assertEquals(dbScenic.getDestination().getName(), destination.getName());
    }

    /**
     * 根据多条件进行查询,分页
     */
    @Test
    public void testQueryScenicsPagesByConditions() {

        String tempName = generateName("test-scenic-name");
        String tempAddress = generateName("test-scenic-address");
        String tempRemark = generateName("test-scenic-remark");

        Scenic scenic = new Scenic();
        scenic.setDestination(destination);
        scenic.setName(tempName);
        scenic.setTenantId(1L);
        scenic.setAddress(tempAddress);
        scenic.setRemark(tempRemark);
        scenic.setLat(new BigDecimal(12.3));
        scenic.setLon(new BigDecimal(45.6));
        Scenic newSce1 = scenicBiz.addScenic(scenic);
        Assert.assertNotNull(newSce1);

        Map<String, Object> params = new HashMap<>();
        params.put("name", tempName);
        params.put("tenantId", 1L);
        params.put("remark", tempRemark);
        params.put("address", tempAddress);
        params.put("destination.id", destination.getId());
        params.put("destination.name", destination.getName());
        DomainPage<Scenic> scenicPage = scenicBiz.queryScenicsPagesByConditions(params, "id", IBaseDao.SortBy.DESC, 1, 10, Boolean.FALSE);
        Assert.assertTrue(scenicPage != null && Long.valueOf(1L).equals(scenicPage.getDomainTotalCount()));

        Scenic dbScenic = scenicPage.getDomains().get(0);
        Assert.assertEquals(dbScenic.getName(), tempName);
        Assert.assertEquals(dbScenic.getAddress(), tempAddress);
        Assert.assertEquals(dbScenic.getRemark(), tempRemark);
        Assert.assertEquals(dbScenic.getTenantId(), Long.valueOf(1L));
        Assert.assertEquals(dbScenic.getDestination().getId(), destination.getId());
        Assert.assertEquals(dbScenic.getDestination().getName(), destination.getName());
    }

    /**
     * 存在性检查
     */
    @Test
    public void testIsExist() {
        Scenic scenic = generateNewScenic();
        Scenic newSce1 = scenicBiz.addScenic(scenic);
        Assert.assertNotNull(newSce1);

        Assert.assertTrue(scenicBiz.isExisted(newSce1.getId()));
    }

    /**
     * 加上时间戳，以避免名字相同
     * @param name
     * @return
     */
    private String generateName(String name) {
        return new Date().getTime() + name;
    }

    /**
     * 生成一个新的Scenic
     * @return
     */
    private Scenic generateNewScenic() {
        Scenic tempScenic = new Scenic();
        tempScenic.setName(generateName("test-procurement-scenic"));
        tempScenic.setAddress(generateName("test-procurement-scenic"));
        tempScenic.setRemark(generateName("test-procurement-scenic"));
        tempScenic.setDestination(destination);
        tempScenic.setLat(new BigDecimal(88.8));
        tempScenic.setLon(new BigDecimal(88.8));
        tempScenic.setTenantId(1L);
        tempScenic.setDel(Boolean.FALSE);
        return tempScenic;
    }

    /**
     * 生成一个Destination
     * @return
     */
    private Destination generateNewDestination() {
        Destination tempDestination = new Destination();
        tempDestination.setName(generateName("test-procurement-destination"));
        tempDestination.setAddress(generateName("test-procurement-destination"));
        tempDestination.setRemark(generateName("test-procurement-destination"));
        tempDestination.setLat(new BigDecimal(88.8));
        tempDestination.setLon(new BigDecimal(88.8));
        tempDestination.setArea(area);
        tempDestination.setTenantId(1L);
        tempDestination.setDel(Boolean.FALSE);
        return tempDestination;
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
}
