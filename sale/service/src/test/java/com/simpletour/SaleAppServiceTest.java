package com.simpletour;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.ISaleAppDao;
import com.simpletour.domain.sale.SaleApp;
import com.simpletour.service.sale.ISaleAppService;
import com.simpletour.service.sale.error.SaleAppServiceError;
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
import java.util.Optional;

/**
 * @Brief : 销售端服务的测试类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/8 10:56
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class SaleAppServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    ISaleAppService saleAppService;

    @Autowired
    private ISaleAppDao saleAppDao;

    private Long id;

    @BeforeClass
    public void init() {

    }

    /**
     *测试添加空销售端
     */
    @Test(priority = 1)
    public void addNullSaleApp() {
        try {
            Optional<SaleApp> saleAppOptional = saleAppService.addSaleApp(null);
            Assert.assertTrue(saleAppOptional.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppServiceError.SALE_APP_EMPTY);
        }

    }

    /**
     * 测试添加已经删除的销售端
     */
    @Test(priority = 2)
    public void addDelSaleApp() {
        try {
            SaleApp saleApp = new SaleApp("携程OTA销售平台", "asdadasd123key", "ertertert456secret", "携程业务员", "13800138000", "028-12345678", "test@ctrip.com", "028-69567896",5, "备注");
            saleApp.setDel(true);
            Optional<SaleApp> saleAppOptional = saleAppService.addSaleApp(saleApp);
            Assert.assertTrue(saleAppOptional.isPresent());
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), SaleAppServiceError.SALE_APP_DEL);
        }
    }

    /**
     * 测试正常添加5条销售端记录
     */
    @Test(priority = 3)
    @Rollback(value = false)
    public void addSaleApp() {
        List<SaleApp> saleAppList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SaleApp saleApp = new SaleApp("携程OTA销售平台" + i, "asdadasd123key" + i, "ertertert456secret" + i, "携程业务员", "13800138000", "028-12345678", "test@ctrip.com", "028-69567896",5, "备注");
            Optional<SaleApp> saleApp1 = saleAppService.addSaleApp(saleApp);
            saleAppList.add(saleApp1.get());
        }
        id = saleAppList.get(0).getId();
        Assert.assertEquals(5, saleAppList.size());
    }

    /**
     * 测试更新空的销售端
     */
    @Test(priority = 4)
    public void updateNullSaleApp() {
        try {
            Optional<SaleApp> saleApp1 = saleAppService.updateSaleApp(null);
            Assert.assertTrue(saleApp1.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppServiceError.SALE_APP_EMPTY);
        }
    }

    /**
     * 测试更新已经删除的销售端
     */
    @Test(priority = 5)
    public void updateDelSaleApp() {
        try {
            Optional<SaleApp> saleApp1 = saleAppService.getSaleAppById(id);
            Assert.assertTrue(saleApp1.isPresent());
            saleApp1.get().setDel(Boolean.TRUE);
            Optional<SaleApp> saleApp2 =  saleAppService.updateSaleApp(saleApp1.get());
            Assert.assertTrue(!saleApp2.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), SaleAppServiceError.SALE_APP_DEL);
        }
    }


    /**
     * 测试使用id查询销售端
     */
    @Test(priority = 6)
    public void findSaleAppById() {
        Optional<SaleApp> saleApp1 = saleAppService.getSaleAppById(id);
        Assert.assertTrue(saleApp1.isPresent());
    }

    /**
     * 测试删除不存在的销售端
     */
    @Test(priority = 7)
    public void deleteNotExistSaleApp() {
        try{
            saleAppService.deleteSaleApp(123123L);

        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), SaleAppServiceError.SALE_APP_NOT_EXIST);
        }
        Optional<SaleApp> saleAppOptional =  saleAppService.getSaleAppById(123123L);
        Assert.assertTrue(!saleAppOptional.isPresent());
    }

    /**
     * 测试分页查询销售端
     */
    @Test(priority = 8)
    public void querySaleApp() {
        DomainPage<SaleApp> saleAppDomainPage =   saleAppService.querySaleAppsPagesByConditions(null,"id", IBaseDao.SortBy.ASC,1,10,true);
        Assert.assertTrue(saleAppDomainPage.getDomains().size()>0);
    }

    /**
     *测试删除销售端
     */

    @Test(priority = 9)
    public void deleteSaleApp() {
        saleAppService.deleteSaleApp(id);
        Optional<SaleApp> saleAppOptional =  saleAppService.getSaleAppById(id);
        Assert.assertTrue(!saleAppOptional.isPresent());

    }

    @AfterTest
    public void clearData() {
        DomainPage<SaleApp> saleAppDomainPage = saleAppService.querySaleAppsPagesByConditions(null, "id", IBaseDao.SortBy.ASC, 1, 10, false);
        for (SaleApp saleApp : saleAppDomainPage.getDomains()) {
            saleAppDao.remove(saleApp);

        }

    }

}
