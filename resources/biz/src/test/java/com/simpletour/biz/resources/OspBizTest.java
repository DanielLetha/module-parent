package com.simpletour.biz.resources;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.error.DefaultError;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.OfflineServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

//import com.simpletour.common.security.token.EncryptedToken;
//import com.simpletour.common.security.token.Token;


/**
 * @Brief :  供应商biz单元测试类
 * @Author:  liangfei/liangfei@simpletour.com
 * @Date  :  2016/3/21 11:39
 * @Since ： 2.0.0-SNAPSHOT
 * @Remark:  本类仅用于对于ospBiz的单元测试
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
@Transactional
public class OspBizTest extends AbstractTestNGSpringContextTests {

    @Autowired
    protected IOspBiz ospBiz;
    @Autowired
    protected IResourcesDao resourcesDao;

    private Long id;

    /**
     * 构造token信息
     */
    @BeforeClass(alwaysRun = true)
    public void initData() {
//        new EncryptedToken("0", "0", "12", "2", Token.ClientType.BROWSER);
    }
    /**
     * 测试添加供应商,初始化一部分数据
     */
    @Test(priority = 1)
    @Rollback(value = false)
    public void testAddOfflineServiceProvider() {
        List list = new ArrayList<>();
        //测试数据验证
        for (int i = 0; i < 15; i++) {
            OfflineServiceProvider offlineServiceProvider = new OfflineServiceProvider();
            offlineServiceProvider.setName("简途" + i);
            offlineServiceProvider.setDel(false);
            offlineServiceProvider.setTenantId(1L);
            offlineServiceProvider.setRemark("111");
            OfflineServiceProvider osp =  ospBiz.addOfflineServiceProvider(offlineServiceProvider);
            id= osp.getId();
            list.add(osp);
        }
        Assert.assertEquals(list.size(),15);
    }

    /**
     * 测试添加供应商,供应商的值为空
     */
    @Test(priority = 2)
    @Rollback(value = false)
    public void testAddNull() {
        //测试传入空对象
        try {
            OfflineServiceProvider osp =  ospBiz.addOfflineServiceProvider(null);
            Assert.assertNotNull(osp,"错误-添加空值的供应商成功");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().equals(ResourcesBizError.EMPTY_ENTITY.getErrorMessage()));
        }
    }



    /**
     * 测试添加供应商,测试名称唯一
     */
    @Test(priority = 3)
    @Rollback(value = false)
    public void testAddNameOnly() {
        OfflineServiceProvider offlineServiceProvider = new OfflineServiceProvider();
        offlineServiceProvider.setName("简途0");
        offlineServiceProvider.setDel(false);
        offlineServiceProvider.setTenantId(1L);
        offlineServiceProvider.setRemark("111");
        try {
            OfflineServiceProvider osp = ospBiz.addOfflineServiceProvider(offlineServiceProvider);
            Assert.assertNotNull(osp,"错误-添加已经存在名称的供应商成功");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().equals(ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING.getErrorMessage()));

        }
    }


    @Test(priority = 4)
    public void testGetOfflineServiceProvidersByPage() {
        DomainPage<OfflineServiceProvider> domainPage = ospBiz.getOfflineServiceProvidersByPage(false, 1, 10);
        Assert.assertTrue(domainPage.getDomains().size() >= 0);
    }

    @Test(priority = 5)
    public void testGetOfflineServiceProvidersByConditionPage() {

        DomainPage<OfflineServiceProvider> domainPage = ospBiz.getOfflineServiceProvidersByConditionPage("简途", false, 1, 10);
        Assert.assertTrue(domainPage.getDomains().size() > 0);
    }

    @Test(priority = 6)
    public void testGetOfflineServiceProviderById() {

        OfflineServiceProvider offlineServiceProvider = ospBiz.getOfflineServiceProvidersByPage(false, 1, 10).getDomains().get(0);
        Assert.assertNotNull(offlineServiceProvider);
    }

    /**
     * 测试更新供应商,使用和token相同的租户
     */
    @Test(priority = 7)
    @Rollback(value = false)
    public void testUpdateUseSameTenant() {
        OfflineServiceProvider offlineServiceProvider = ospBiz.getOfflineServiceProvidersByPage(false, 1, 10).getDomains().get(0);
        offlineServiceProvider.setTenantId(12L);
        OfflineServiceProvider offlineServiceProvider1 = ospBiz.updateOfflineServiceProvider(offlineServiceProvider);
        Assert.assertNotNull(offlineServiceProvider1);
    }

    /**
     * 测试更新供应商,构造不同的租户
     */
    @Test(priority = 8)
    @Rollback(value = false)
    public void testUpdateUseDifTenant() {
        OfflineServiceProvider offlineServiceProvider = ospBiz.getOfflineServiceProvidersByPage(false, 1, 10).getDomains().get(0);
        offlineServiceProvider.setTenantId(10L);
        try {
           OfflineServiceProvider osp =  ospBiz.updateOfflineServiceProvider(offlineServiceProvider);
            Assert.assertNull(osp,"错误-不同的租户id的供应商更新成功");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().equals(DefaultError.NOT_SAME_TENANT_ID.getErrorMessage()));
        }
    }

    /**
     * 测试更新供应商,使用已经存在的名字
     */
    @Test(priority = 9)
    @Rollback(value = false)
    public void testUpdateUseExistName() {
        OfflineServiceProvider offlineServiceProvider = ospBiz.getOfflineServiceProvidersByPage(false, 1, 10).getDomains().get(0);
        offlineServiceProvider.setName("简途0");
        try {
            OfflineServiceProvider osp = ospBiz.updateOfflineServiceProvider(offlineServiceProvider);
            Assert.assertNotNull(osp,"错误-添加已经存在名称的供应商成功");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().equals(ResourcesBizError.OSP_NAME_MUST_BE_NUIQUE.getErrorMessage()));
        }
    }

    @Test(priority = 10)
    public void testIsExisted(){
        ospBiz.deleteOfflineServiceProvider(id);
        OfflineServiceProvider osp = ospBiz.getOfflineServiceProviderById(id);
        Assert.assertNull(osp);
        Boolean bool = ospBiz.isExisted(id);
        Assert.assertFalse(bool);

    }

    @AfterClass(alwaysRun = true)
    public void clearData() {
        DomainPage<OfflineServiceProvider> domainPage = ospBiz.getOfflineServiceProvidersByPage(false, 1, 50);
        for (OfflineServiceProvider offlineServiceProvider : domainPage.getDomains()) {
            resourcesDao.removeEntity(offlineServiceProvider);
        }

    }

}
