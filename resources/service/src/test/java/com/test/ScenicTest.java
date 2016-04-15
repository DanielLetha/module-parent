package com.test;

import com.simpletour.biz.resources.IDestinationBiz;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.resources.Scenic;
import com.simpletour.service.resources.IResourcesService;
import com.simpletour.service.resources.error.ResourcesServiceError;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Brief : 景点测试
 * Author: Hawk
 * Date  : 2016/3/22
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ScenicTest extends AbstractTransactionalTestNGSpringContextTests {

    @Resource
    private IResourcesDao resourcesDao;
    @Resource
    private IDestinationBiz destinationBiz;
    @Resource
    private IResourcesService scenicService;

    @PersistenceContext
    protected EntityManager em;

    private Area area = null;
    private Destination destination = null;

    @BeforeClass
    public void setup() {

        area = new Area();
        area.setId(new Long(110000));

        Destination tempDestination = generateNewDestination();
        destination = destinationBiz.addDestination(tempDestination);
        Assert.assertNotNull(destination);

//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
    }

    @AfterClass
    public void teardown() {
        removeCatchException(destination);
    }

    /**
     * 测试正常增加
     */
    @Test
    public void testAddScenic() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> dbScenicOpt = scenicService.addScenic(scenic);
        Assert.assertTrue(dbScenicOpt.isPresent());
    }

    /**
     * 测试增加，当Scenic为空的情况
     */
    @Test
    public void testAddScenicAndEntityNull() {
        try {
            scenicService.addScenic(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.EMPTY_ENTITY, e.getError());
        }
    }

    /**
     * 测试增加，当Scenic的destination不存在的情况
     */
    @Test
    public void testAddScenicAndDestinaionNotExist() {
        try {
            Scenic scenic = generateNewScenic();
            scenic.setDestination(null);
            scenicService.addScenic(scenic);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NULL, e.getError());
        }
    }

    /**
     * 测试增加，当Scenic的destination已经被伪删除的情况
     */
    @Test
    public void testAddScenicAndDestinaionDeleted() {
        try {
            destinationBiz.deleteDestination(destination.getId());
            Scenic scenic = generateNewScenic();
            scenicService.addScenic(scenic);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试增加，当Scenic的destination已经被物理删除的情况
     */
    @Test
    public void testAddScenicAndDestinaionRealDeleted() {
        try {
            em.remove(em.merge(destination));
            Scenic scenic = generateNewScenic();
            scenicService.addScenic(scenic);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NOT_EXIST, e.getError());
        }
    }

    /**
     * 增加景点时，测试父景点已经被伪删除的情况
     */
    @Test
    public void testAddScenicAndParentDeleted() {
        Scenic parent = generateNewScenic();
        Optional<Scenic> parentOpt = scenicService.addScenic(parent);
        Assert.assertTrue(parentOpt.isPresent());

        scenicService.deleteScenic(parentOpt.get().getId());

        Scenic childScenic = generateNewScenic();
        childScenic.setParent(parentOpt.get());
        try {
            scenicService.addScenic(childScenic);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.SCENIC_PARENT_NOT_EXIST, e.getError());
        }
    }

    /**
     * 增加景点时，测试父景点已经被物理删除的情况
     */
    @Test
    public void testAddScenicAndParentRealDeleted() {
        Scenic parent = generateNewScenic();
        Optional<Scenic> parentOpt = scenicService.addScenic(parent);
        Assert.assertTrue(parentOpt.isPresent());

        em.remove(em.merge(parentOpt.get()));

        Scenic childScenic = generateNewScenic();
        childScenic.setParent(parentOpt.get());
        try {
            scenicService.addScenic(childScenic);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.SCENIC_PARENT_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试正常更新
     */
    @Test
    public void testUpdateScenic() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> tempSce = scenicService.addScenic(scenic);
        Assert.assertTrue(tempSce.isPresent());

        String tempName = generateName("test-scenic-name");
        String tempAddress = generateName("test-scenic-address");
        String tempRemark = generateName("test-scenic-remark");

        Scenic newScenic = new Scenic();
        newScenic.setName(tempName);
        newScenic.setAddress(tempAddress);
        newScenic.setRemark(tempRemark);
        newScenic.setLat(BigDecimal.valueOf(12.5));
        newScenic.setLon(BigDecimal.valueOf(45.9));
        newScenic.setId(tempSce.get().getId());
        newScenic.setVersion(tempSce.get().getVersion());
        newScenic.setDestination(destination);
        Optional<Scenic> dbScenic = scenicService.updateScenic(newScenic);
        Assert.assertTrue(dbScenic.isPresent());

        Assert.assertEquals(tempName, dbScenic.get().getName());
        Assert.assertEquals(tempAddress, dbScenic.get().getAddress());
        Assert.assertEquals(tempRemark, dbScenic.get().getRemark());
        Assert.assertEquals(BigDecimal.valueOf(12.5), dbScenic.get().getLat());
        Assert.assertEquals(BigDecimal.valueOf(45.9), dbScenic.get().getLon());
    }

    /**
     * 测试更新，当Scenic为空的情况
     */
    @Test
    public void testUpdateScenicAndEntityNull() {
        try {
            scenicService.updateScenic(null);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.EMPTY_ENTITY, e.getError());
        }
    }

    /**
     * 测试更新，当Scenic的destination不存在的情况
     */
    @Test
    public void testUpdateScenicAndDestinaionNotExist() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> tempScenic = scenicService.addScenic(scenic);
        Assert.assertTrue(tempScenic.isPresent());

        try {
            scenic.setDestination(null);
            scenic.setId(tempScenic.get().getId());
            scenic.setVersion(tempScenic.get().getVersion());
            scenicService.updateScenic(scenic);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NULL, e.getError());
        }
    }

    /**
     * 测试更新，当Scenic的destination已经被伪删除的情况
     */
    @Test
    public void testUpdateScenicAndDestinaionDeleted() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> scenicOpt = scenicService.addScenic(scenic);
        Assert.assertTrue(scenicOpt.isPresent());

        destination.setDel(Boolean.TRUE);
        em.merge(destination);
        try {
            scenicService.updateScenic(scenic);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            System.out.println(e.getError().getErrorMessage());
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试更新，当Scenic的destination已经被物理删除的情况
     */
    @Test
    public void testUpdateScenicAndRealDestinaionDeleted() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> scenicOpt = scenicService.addScenic(scenic);
        Assert.assertTrue(scenicOpt.isPresent());

        em.remove(em.merge(destination));
        try {
            scenicService.updateScenic(scenicOpt.get());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            System.out.println(e.getError().getErrorMessage());
            Assert.assertEquals(ResourcesServiceError.DESTINATION_NOT_EXIST, e.getError());
        }
    }

    /**
     * 更新景点时，测试父景点已经被伪删除的情况
     */
    @Test
    public void testUpdateScenicAndParentDeleted() {

        Scenic child = generateNewScenic();
        Optional<Scenic> childOpt = scenicService.addScenic(child);
        Assert.assertTrue(childOpt.isPresent());

        Scenic parent = generateNewScenic();
        Optional<Scenic> parentOpt = scenicService.addScenic(parent);
        Assert.assertTrue(parentOpt.isPresent());
        scenicService.deleteScenic(parentOpt.get().getId());

        childOpt.get().setParent(parentOpt.get());
        try {
            scenicService.updateScenic(childOpt.get());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.SCENIC_PARENT_NOT_EXIST, e.getError());
        }
    }

    /**
     * 更新景点时，测试父景点已经被物理删除的情况
     */
    @Test
    public void testUpdateScenicAndParentRealDeleted() {

        Scenic child = generateNewScenic();
        Optional<Scenic> childOpt = scenicService.addScenic(child);
        Assert.assertTrue(childOpt.isPresent());

        Scenic parent = generateNewScenic();
        Optional<Scenic> parentOpt = scenicService.addScenic(parent);
        Assert.assertTrue(parentOpt.isPresent());

        em.remove(em.merge(parentOpt.get()));

        childOpt.get().setParent(parentOpt.get());
        try {
            scenicService.updateScenic(childOpt.get());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.SCENIC_PARENT_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试更新，当Scenic的ID为空的情况
     */
    @Test
    public void testUpdateScenicAndIdError() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> scenicOpt = scenicService.addScenic(scenic);
        Assert.assertTrue(scenicOpt.isPresent());
        try {
            scenicOpt.get().setId(null);
            scenicService.updateScenic(scenicOpt.get());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.SCENIC_ID_ERROR, e.getError());
        }
    }

    /**
     * 测试更新，当Scenic的ID找不到对应的Scenic实体的情况
     */
    @Test
    public void testUpdateScenicAndIdNotExist() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> tempScenic = scenicService.addScenic(scenic);
        Assert.assertTrue(tempScenic.isPresent());

        try {
            tempScenic.get().setId(-1L);
            scenicService.updateScenic(tempScenic.get());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            System.out.println(e.getError().getErrorMessage());
            Assert.assertEquals(ResourcesServiceError.SCENIC_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试更新，当Scenic的ID找不到对应的Scenic实体已经被伪删除的情况
     */
    @Test
    public void testUpdateScenicAndScenicDeleted() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> tempScenic = scenicService.addScenic(scenic);
        Assert.assertTrue(tempScenic.isPresent());
        scenicService.deleteScenic(tempScenic.get().getId());
        try {
            scenicService.updateScenic(tempScenic.get());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            System.out.println(e.getError().getErrorMessage());
            Assert.assertEquals(ResourcesServiceError.SCENIC_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试更新，当Scenic的ID找不到对应的Scenic实体已经被物理删除的情况
     */
    @Test
    public void testUpdateScenicAndScenicRealDeleted() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> tempScenic = scenicService.addScenic(scenic);
        Assert.assertTrue(tempScenic.isPresent());
        em.remove(em.merge(tempScenic.get()));
        try {
            scenicService.updateScenic(tempScenic.get());
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.SCENIC_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试正常删除
     */
    @Test
    public void testDeleteScenic() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> scenicOpt = scenicService.addScenic(scenic);
        Assert.assertTrue(scenicOpt.isPresent());
        scenicService.deleteScenic(scenicOpt.get().getId());

        Scenic deletedScenic = resourcesDao.getEntityById(Scenic.class, scenicOpt.get().getId());
        Assert.assertNotNull(deletedScenic);
        Assert.assertTrue(deletedScenic.getDel());
    }

    /**
     * 测试更新，当Scenic不存在的情况
     */
    @Test
    public void testDeleteScenicAndEntityNotExist() {
        try {
            scenicService.deleteScenic(-1L);
            Assert.assertTrue(Boolean.FALSE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(ResourcesServiceError.SCENIC_NOT_EXIST, e.getError());
        }
    }

    /**
     * 测试正常情况下的，getScenicById
     */
    @Test
    public void testGetScenicById() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> newSce1 = scenicService.addScenic(scenic);
        Assert.assertTrue(newSce1.isPresent());

        Assert.assertTrue(scenicService.getScenicById(newSce1.get().getId()).isPresent());
    }

    /**
     * 测试正常情况下的，getScenicId，即只给出名字和目的地的ID，查找唯一的Scenic
     */
    @Test
    public void testGetScenicId() {
        Scenic scenic = generateNewScenic();
        Optional<Scenic> scenicOpt = scenicService.addScenic(scenic);
        Assert.assertTrue(scenicOpt.isPresent());

        Scenic querySce = new Scenic();
        Destination newDestination = generateNewDestination();
        newDestination.setId(destination.getId());
        querySce.setDestination(newDestination);
        querySce.setName(scenicOpt.get().getName());
        Assert.assertEquals(Long.valueOf(scenicService.getScenicId(querySce)), scenicOpt.get().getId());
    }

    /**
     * 测试多条件查询
     */
    @Test
    public void testQueryScenicsPagesByConditions() {

        String tempName = generateName("test-scenic-name");
        String tempAddress = generateName("test-scenic-address");
        String tempRemark = generateName("test-scenic-remark");

        Scenic scenic = generateNewScenic();
        scenic.setName(tempName);
        scenic.setAddress(tempAddress);
        scenic.setRemark(tempRemark);
        Optional<Scenic> newSce1 = scenicService.addScenic(scenic);
        Assert.assertTrue(newSce1.isPresent());

        Map<String, Object> params = new HashMap<>();
        params.put("name", tempName);
        params.put("address", tempAddress);
        params.put("remark", tempRemark);
        params.put("destination.id", destination.getId());
        params.put("destination.name", destination.getName());
        DomainPage<Scenic> scenicPage = scenicService.queryScenicsPagesByConditions(params, "id", IBaseDao.SortBy.DESC, 1, 10, Boolean.FALSE);
        Assert.assertTrue(scenicPage != null && Long.valueOf(1L).equals(scenicPage.getDomainTotalCount()));

        Scenic queriedScenic = scenicPage.getDomains().get(0);
        Assert.assertEquals(tempName, queriedScenic.getName());
        Assert.assertEquals(tempAddress, queriedScenic.getAddress());
        Assert.assertEquals(tempRemark, queriedScenic.getRemark());
        Assert.assertEquals(destination.getName(), queriedScenic.getDestination().getName());
        Assert.assertEquals(destination.getId(), queriedScenic.getDestination().getId());
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
        tempScenic.setName(generateName("test-scenic-name"));
        tempScenic.setRemark(generateName("test-scenic-remark"));
        tempScenic.setAddress(generateName("test-scenic-address"));
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
        tempDestination.setName(generateName("test-destination-name"));
        tempDestination.setRemark(generateName("test-destination-remark"));
        tempDestination.setAddress(generateName("test-destination-address"));
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
