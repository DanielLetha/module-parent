package com.simpletour;

import com.simpletour.biz.sale.ISaleAppBiz;
import com.simpletour.biz.sale.error.SaleAppBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.ISaleAppDao;
import com.simpletour.domain.sale.SaleApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Brief :  销售端测试用例
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/7 16:55
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class SaleAppBizTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private ISaleAppBiz saleAppBiz;
    @Autowired
    private ISaleAppDao saleAppDao;

    private Long id;

    @BeforeClass
    public void init() {

    }

    /**
     * 测试添加空的销售端对象
     */
    @Test(priority = 1)
    public void testAddNullSaleApp() {
        try {
            SaleApp saleApp = saleAppBiz.addSaleApp(null);
            Assert.assertFalse(true);
        } catch (BaseSystemException e) {
            Assert.assertEquals( e.getError(), SaleAppBizError.SALE_APP_EMPTY);
        }
    }

    /**
     * 测试添加已经删除的销售对象
     */
    @Test(priority = 2)
    public void testAddDelSaleApp() {
        SaleApp saleApp = new SaleApp("携程OTA销售平台", "asdadasd123key", "ertertert456secret", "携程业务员", "13800138000", "028-12345678", "test@ctrip.com", "028-69567896",5, "备注");
        saleApp.setDel(true);
        try {
            SaleApp saleApp1 = saleAppBiz.addSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppBizError.SAL_APP_DEL);
        }

    }

    /**
     * 测试正常添加5组数据
     */
    @Test(priority = 3)
    @Rollback(value = false)
    public void testAddSaleApp() {
        List<SaleApp> saleAppList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SaleApp saleApp = new SaleApp("携程OTA销售平台" + i, "asdadasd123key" + i, "ertertert456secret" + i, "携程业务员", "13800138000", "028-12345678", "test@ctrip.com", "028-69567896",5, "备注");
            SaleApp saleApp1 = saleAppBiz.addSaleApp(saleApp);
            saleAppList.add(saleApp1);
        }
        id = saleAppList.get(0).getId();
        Assert.assertEquals(5, saleAppList.size());


    }

    /**
     * 测试添加已经存在名称的销售端
     */
    @Test(priority = 4)
    public void testAddExistNameSaleApp() {
        SaleApp saleApp = new SaleApp("携程OTA销售平台0", "qweweer123key", "fewrwer890secret", "去哪儿业务员", "15600156000", "028-89965632", "test@qunaert.com", "028-12312339",5, "备注21");
        try {
            SaleApp saleApp1 = saleAppBiz.addSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppBizError.SALE_APP_NAME_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试添加已经存在key的销售端
     */
    @Test(priority = 5)
    public void testAddExistKeySaleApp() {
        SaleApp saleApp = new SaleApp("途牛OTA销售平台", "asdadasd123key0", "ewrwer890secret", "途牛业务员", "15600156000", "028-89965632", "test@tuniu.com", "028-12312339",5, "备注21");
        try {
            SaleApp saleApp1 = saleAppBiz.addSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppBizError.SALE_APP_KEY_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试添加已经存在secret的销售端
     */
    @Test(priority = 6)
    public void testAddExistSecretSaleApp() {
        SaleApp saleApp = new SaleApp("同程OTA销售平台", "powerqwrq23key", "ertertert456secret0", "同程业务员", "13100131000", "028-12345678", "test@tongc.com", "028-69567896",5, "备注");
        try {
            SaleApp saleApp1 = saleAppBiz.addSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals( e.getError(), SaleAppBizError.SALE_APP_SECRET_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试使用存在的名字更新销售端
     */
    @Test(priority = 7)
    public void testUpdateWithEixstNameSaleApp() {
        SaleApp saleApp = saleAppBiz.findSaleAppById(id);
        Assert.assertNotNull(saleApp);
        saleApp.setName("携程OTA销售平台1");
        try {
            SaleApp saleApp1 = saleAppBiz.updateSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppBizError.SALE_APP_NAME_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试使用存在的key更新销售端
     */
    @Test(priority = 8)
    public void testUpdateWithExistKeySaleApp() {
        SaleApp saleApp = saleAppBiz.findSaleAppById(id);
        Assert.assertNotNull(saleApp);
        saleApp.setKey("asdadasd123key1");
        try {
            SaleApp saleApp1 = saleAppBiz.updateSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppBizError.SALE_APP_KEY_MUST_BE_UNIQUE);
        }

    }
    /**
     * 测试使用存在的secret更新销售端
     */
    @Test(priority = 9)
    public void testUpdateWthExistSecretSaleApp() {
        SaleApp saleApp = saleAppBiz.findSaleAppById(id);
        Assert.assertNotNull(saleApp);
        saleApp.setKey("ertertert456secret1");
        try {
            SaleApp saleApp1 = saleAppBiz.updateSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppBizError.SALE_APP_SECRET_MUST_BE_UNIQUE);
        }
    }

    /**
     * 测试使用空的key更新销售端
     */
    @Test(priority = 10)
    public void testUpdateWithNullKeySaleApp() {
        SaleApp saleApp = saleAppBiz.findSaleAppById(id);
        Assert.assertNotNull(saleApp);
        saleApp.setKey(null);
        try {
            SaleApp saleApp1 = saleAppBiz.updateSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppBizError.SALE_APP_KEY_MUST_NOT_NULL);
        }
    }
    /**
     * 测试使用空的secret更新销售端
     */
    @Test(priority = 11)
    public void testUpdateWithNullSecretSaleApp() {
        SaleApp saleApp = saleAppBiz.findSaleAppById(id);
        Assert.assertNotNull(saleApp);
        saleApp.setSecret(null);
        try {
            SaleApp saleApp1 = saleAppBiz.updateSaleApp(saleApp);
            Assert.assertNotNull(saleApp1);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppBizError.SALE_APP_SECRET_MUST_NOT_NULL);
        }
    }

    /**
     * 测试正常更新销售端
     */
    @Test(priority = 12)
    public void testUpdateSaleApp() {
        SaleApp saleApp = saleAppBiz.findSaleAppById(id);
        Assert.assertNotNull(saleApp);
        saleApp.setName("携程协调专员");
        SaleApp saleApp1 = saleAppBiz.updateSaleApp(saleApp);
        Assert.assertNotNull(saleApp1);
    }

    /**
     * 根据id查询销售端对象
     */
    @Test(priority = 13)
    public void testGetSaleAppById() {
        SaleApp saleApp = saleAppBiz.findSaleAppById(id);
        Assert.assertNotNull(saleApp);
    }

    /**
     * 测试分页查询销售端
     */
    @Test(priority = 14)
    public void testGetSaleAppDomainPage() {
        DomainPage<SaleApp> saleAppDomainPage = saleAppBiz.querySaleAppByCondition(null, "id", IBaseDao.SortBy.ASC, 1, 10, false);
        Assert.assertTrue(saleAppDomainPage.getDomains().size() > 0);
    }

    /**
     * 测试删除销售端
     */
    @Test(priority = 15)
    public void testDelSaleApp() {
        saleAppBiz.deleteSaleApp(id, false);
        SaleApp saleApp = saleAppBiz.findSaleAppById(id);
        Assert.assertNull(saleApp);
    }

    @AfterTest
    public void clearData() {
        DomainPage<SaleApp> saleAppDomainPage = saleAppBiz.querySaleAppByCondition(null, "id", IBaseDao.SortBy.ASC, 1, 10, false);
        for (SaleApp saleApp : saleAppDomainPage.getDomains()) {
            saleAppDao.remove(saleApp);

        }

    }
}
