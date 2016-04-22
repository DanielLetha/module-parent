package com.simpletour.service.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IAgreementDao;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.domain.sale.SaleApp;
import com.simpletour.service.sale.data.AgreementData;
import com.simpletour.service.sale.error.AgreementServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/20.
 *
 * @since 2.0-SNAPSHOT
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class AgreementServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    IAgreementService agreementService;

    @Autowired
    IAgreementDao agreementDao;

    AgreementData agreementData;

    @BeforeClass
    public void before() {
        agreementData = new AgreementData(agreementDao);
        agreementData.createData();
    }

    @AfterClass
    public void after() {
        agreementData.clear();
    }

    /**
     * 测试查询销售协议分页
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:06
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 1)
    public void testFindAgreementByCondition() {
        DomainPage<Agreement> domainPage = agreementService.findAgreementByCondition(new HashMap<>(), "id", IBaseDao.SortBy.ASC, 1, 10, true);
        Assert.assertNotNull(domainPage);
        domainPage.getDomains().forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));

        Agreement agreement = (Agreement) agreementData.getDomains().get(0);
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("saleApp.name", agreement.getSaleApp().getName());
        conditions.put("enabled", agreement.isEnabled());
        domainPage = agreementService.findAgreementByCondition(conditions, "id", IBaseDao.SortBy.ASC, 1, 10, true);
        Assert.assertNotNull(domainPage);
        domainPage.getDomains().forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));
    }

    /**
     * 测试根据query对象查询销售协议分页
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:06
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 2)
    public void testFindAgreementByQuery() {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        DomainPage<Agreement> domainPage = agreementService.findAgreementByQuery(query);
        Assert.assertNotNull(domainPage);
        domainPage.getDomains().forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));

        Agreement agreement = (Agreement) agreementData.getDomains().get(0);
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("saleApp.name", agreement.getSaleApp().getName());
        conditionSet.addCondition("enabled", agreement.isEnabled());
        query.setCondition(conditionSet);
        domainPage = agreementService.findAgreementByQuery(query);
        Assert.assertNotNull(domainPage);
        domainPage.getDomains().forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));
    }

    /**
     * 测试根据query对象查询销售协议列表
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:06
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 3)
    public void testGetAgreementListByQuery() {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        List<Agreement> list = agreementService.getAgreementListByQuery(query);
        Assert.assertNotNull(list);
        list.forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));

        Agreement agreement = (Agreement) agreementData.getDomains().get(0);
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("saleApp.name", agreement.getSaleApp().getName());
        conditionSet.addCondition("enabled", agreement.isEnabled());
        query.setCondition(conditionSet);
        list = agreementService.getAgreementListByQuery(query);
        Assert.assertNotNull(list);
        list.forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));
    }

    /**
     * 测试根据ID获取销售协议对象
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:06
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 4)
    public void testGetAgreementById() {
        Agreement original = (Agreement) agreementData.getDomains().get(0);
        Optional<Agreement> agreement = agreementService.getAgreementById(original.getId());
        Assert.assertTrue(agreement.isPresent());
    }

    /**
     * 测试根据ID获取销售协议对象，ID为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:06
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 5)
    public void testGetAgreementByIdWithNullId() {
        try {
            agreementService.getAgreementById(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_EMPTY.getErrorMessage());
        }
    }

    /**
     * 测试新增销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:07
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 6)
    public void testAddAgreement() {
        Agreement agreement = agreementData.generateAgreement("add");
        Optional<Agreement> optional = agreementService.addAgreement(agreement);
        Assert.assertTrue(optional.isPresent());
        Assert.assertNotNull(optional.get().getId());
    }

    /**
     * 测试新增销售协议，销售协议对象为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:07
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 7)
    public void testAddAgreementWithNullAgreement() {
        try {
            agreementService.addAgreement(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_EMPTY.getErrorMessage());
        }
    }

    /**
     * 测试新增销售协议，销售端不存在
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:07
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 8)
    public void testAddAgreementWithNotExistedSaleApp() {
        try {
            Agreement agreement = new Agreement();
            SaleApp saleApp = new SaleApp();
            saleApp.setId(Long.MAX_VALUE);
            agreement.setSaleApp(saleApp);
            agreementService.addAgreement(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_APP_NOT_EXIST.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:08
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 9)
    public void testUpdateAgreement() {
        Agreement original = (Agreement) agreementData.getDomains().get(1);
        Agreement agreement = new Agreement(original.getSaleApp(), original.isEnabled(), System.currentTimeMillis() + "update remark");
        agreement.setId(original.getId());
        agreement.setVersion(original.getVersion());
        Optional<Agreement> dbAgreement = agreementService.updateAgreement(agreement);
        Assert.assertTrue(dbAgreement.isPresent());
    }

    /**
     * 测试更新销售协议，销售协议对象为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:08
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 11)
    public void testUpdateAgreementWithNullAgreement() {
        try {
            agreementService.updateAgreement(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_EMPTY.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议，销售协议不存在
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:08
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 12)
    public void testUpdateAgreementWithNotExistedAgreement() {
        try {
            Agreement agreement = new Agreement();
            agreement.setId(Long.MAX_VALUE);
            agreementService.updateAgreement(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_NOT_EXIST.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议，销售端不存在
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:08
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 13)
    public void testUpdateAgreementWithNotExistedSaleApp() {
        try {
            Agreement original = (Agreement) agreementData.getDomains().get(0);
            Agreement agreement = new Agreement();
            agreement.setId(original.getId());
            SaleApp saleApp = new SaleApp();
            saleApp.setId(Long.MAX_VALUE);
            agreement.setSaleApp(saleApp);
            agreementService.updateAgreement(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_APP_NOT_EXIST.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议状态
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:09
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 15)
    public void testUpdateStatus() {
        Agreement original = (Agreement) agreementData.getDomains().get(0);
        Boolean enabled = original.isEnabled();
        Optional<Agreement> agreement = agreementService.updateStatus(original.getId(), !original.isEnabled());
        Assert.assertTrue(agreement.isPresent());
        Assert.assertFalse(enabled.equals(agreement.get().isEnabled()));
    }

    /**
     * 测试更新销售协议状态，销售协议对象为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:09
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 16)
    public void testUpdateStatusWithNullAgreement() {
        try {
            agreementService.updateStatus(null, Boolean.TRUE);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_EMPTY.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议状态，销售协议不存在
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:09
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 17)
    public void testUpdateStatusWithNotExistedAgreement() {
        try {
            agreementService.updateStatus(Long.MAX_VALUE, Boolean.TRUE);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_NOT_EXIST.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议状态，状态重复更新
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 14:09
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 14)
    public void testUpdateAgreementWithRepeatStatus() {
        try {
            Agreement agreement1 = (Agreement) agreementData.getDomains().get(0);
            agreementService.updateStatus(agreement1.getId(), agreement1.isEnabled());
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_STATUS_ENABLED.getErrorMessage());
        }
        try {
            Agreement agreement2 = (Agreement) agreementData.getDomains().get(1);
            agreementService.updateStatus(agreement2.getId(), agreement2.isEnabled());
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementServiceError.AGREEMENT_STATUS_DISABLED.getErrorMessage());
        }
    }
}
