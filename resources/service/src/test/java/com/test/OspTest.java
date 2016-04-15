package com.test;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.OfflineServiceProvider;
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

import java.util.Optional;

/**
 * @Brief :  供应商service的测试类
 * @Author:  liangfei/liangfei@simpletour.com
 * @Date  :  2016/3/23 17:14
 * @Since ： 2.0.0-SNAPSHOT
 * @Remark:  本类仅用于对供应商sservice的单元测试
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
@Transactional
public class OspTest extends AbstractTransactionalTestNGSpringContextTests {
    private long id;

    @Autowired
    IResourcesService resourcesService;
    @Autowired
    IResourcesDao resourcesDao;


    @BeforeClass
    private void initData() {
//        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
    }

    @Test(priority = 1)
    @Rollback(value = false)
    public void addOsp() {
        OfflineServiceProvider offlineServiceProvider = new OfflineServiceProvider();
        offlineServiceProvider.setName("测试");
        offlineServiceProvider.setTenantId(new Long(1));
        offlineServiceProvider.setRemark("test remark");
        Optional<OfflineServiceProvider> res = resourcesService.addOfflineServiceProvider(offlineServiceProvider);
        Assert.assertTrue(res.isPresent());
        id = res.get().getId();
    }


    @Test(priority = 2)
    public void addNullOsp() {
        try {
            Optional<OfflineServiceProvider> op = resourcesService.addOfflineServiceProvider(null);
            Assert.assertTrue(op.isPresent(), "错误-添加了空的供应商");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().equals(ResourcesServiceError.EMPTY_ENTITY.getErrorMessage()));
        }
    }


    @Test(priority = 3)
    @Rollback(value = true)
    public void deleteOsp() {
        resourcesService.deleteOfflineServiceProvider(id);
        Optional<OfflineServiceProvider> op = resourcesService.getOfflineServiceProviderById(id);
        Assert.assertFalse(op.isPresent(),"正确-已经被删除的数据无法被查询出来");

    }

    @Test(priority = 4)
    public void getOspById() {
        Optional<OfflineServiceProvider> res = resourcesService.getOfflineServiceProviderById(id);
        Assert.assertEquals(res.isPresent(), true);

        res = resourcesService.getOfflineServiceProviderById(Long.MAX_VALUE);
        Assert.assertEquals(res.isPresent(), false);
    }

    private void printOsp(OfflineServiceProvider osp) {
        System.out.println("Osp: id:" + osp.getId() + " name:" + osp.getName());
    }

    private void printOspList(DomainPage<OfflineServiceProvider> ospList) {
        for (OfflineServiceProvider osp : ospList.getDomains()) {
            printOsp(osp);
        }
        System.out.println("");
    }

    @Test(priority = 5)
    public void getOspList() {
        printOspList(resourcesService.getOfflineServiceProvidersByPage(null, 0, 10));
        printOspList(resourcesService.getOfflineServiceProvidersByPage(null, 1, 10));
        printOspList(resourcesService.getOfflineServiceProvidersByPage(null, 2, 10));
        printOspList(resourcesService.getOfflineServiceProvidersByPage(null, 1000000, 10));
    }

    @Test(priority = 6)
    public void getOspListByCondition() {
        printOspList(resourcesService.getOfflineServiceProvidersByConditionPage("te", null, 0, 10));
        printOspList(resourcesService.getOfflineServiceProvidersByConditionPage("te", null, 1, 10));
        printOspList(resourcesService.getOfflineServiceProvidersByConditionPage("te", null, 2, 10));
        printOspList(resourcesService.getOfflineServiceProvidersByConditionPage("te", null, 10000000, 10));
    }

    @Test(priority = 7)
    @Rollback(value = false)
    public void updateOps() {
        OfflineServiceProvider osp = resourcesService.getOfflineServiceProviderById(id).get();
        osp.setName("test osp updated" + DestinationTest.generateName());
        osp.setTenantId(new Long(1));
        osp.setRemark("test remark updated");
        Optional<OfflineServiceProvider> res = resourcesService.updateOfflineServiceProvider(osp);
        Assert.assertEquals(res.isPresent(), true);
    }

    @Test(priority = 8)
    public void updateNullOps() {
        try {
            Optional<OfflineServiceProvider> op = resourcesService.updateOfflineServiceProvider(null);
            Assert.assertNotNull(op.get(), "错误-空的供应商依然可以被更新");
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    @AfterTest
    private void clearData() {
        DomainPage<OfflineServiceProvider> domainPage = resourcesService.getOfflineServiceProvidersByConditionPage("test osp updated", false, 1, 30);
        for (OfflineServiceProvider osp : domainPage.getDomains()) {
            resourcesDao.removeEntity(osp);
        }
    }
}


