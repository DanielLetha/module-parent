package com.simpletour.biz.sale;

import com.simpletour.biz.sale.data.AgreementData;
import com.simpletour.biz.sale.error.AgreementBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IAgreementDao;
import com.simpletour.dao.sale.ISaleAppDao;
import com.simpletour.domain.sale.Agreement;
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

/**
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/19.
 *
 * @since 2.0-SNAPSHOT
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class AgreementBizTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    IAgreementBiz agreementBiz;

    @Autowired
    IAgreementDao agreementDao;

    @Autowired
    ISaleAppDao saleAppDao;

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
     * Time:   2016-04-19 18:26
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 1)
    public void testFindAgreementByConditions() {
        DomainPage<Agreement> domainPage = agreementBiz.findAgreementByCondition(new HashMap<>(), "id", IBaseDao.SortBy.ASC, 1, 10, true);
        Assert.assertNotNull(domainPage);
        domainPage.getDomains().forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));

        Agreement agreement = (Agreement) agreementData.getDomains().get(0);
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("saleApp.name", agreement.getSaleApp().getName());
        conditions.put("enabled", agreement.isEnabled());
        domainPage = agreementBiz.findAgreementByCondition(conditions, "id", IBaseDao.SortBy.ASC, 1, 10, true);
        Assert.assertNotNull(domainPage);
        domainPage.getDomains().forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));
    }

    /**
     * 测试根据query对象查询销售协议分页
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:27
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 2)
    public void testFindAgreementByQuery() {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        DomainPage<Agreement> domainPage = agreementBiz.findAgreementByQuery(query);
        Assert.assertNotNull(domainPage);
        domainPage.getDomains().forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));

        Agreement agreement = (Agreement) agreementData.getDomains().get(0);
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("saleApp.name", agreement.getSaleApp().getName());
        conditionSet.addCondition("enabled", agreement.isEnabled());
        query.setCondition(conditionSet);
        domainPage = agreementBiz.findAgreementByQuery(query);
        Assert.assertNotNull(domainPage);
        domainPage.getDomains().forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));
    }

    /**
     * 测试根据query对象查询销售协议列表
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:27
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 3)
    public void testGetAgreementListByQuery() {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        List<Agreement> list = agreementBiz.getAgreementListByQuery(query);
        Assert.assertNotNull(list);
        list.forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));

        Agreement agreement = (Agreement) agreementData.getDomains().get(0);
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("saleApp.name", agreement.getSaleApp().getName());
        conditionSet.addCondition("enabled", agreement.isEnabled());
        query.setCondition(conditionSet);
        list = agreementBiz.getAgreementListByQuery(query);
        Assert.assertNotNull(list);
        list.forEach(o -> System.out.println("编号：" + o.getId() + " 销售端：" + o.getSaleApp().getName()));
    }

    /**
     * 测试根据ID获取销售协议对象
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:27
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 4)
    public void testGetAgreementById() {
        Agreement original = (Agreement) agreementData.getDomains().get(0);
        Agreement agreement = agreementBiz.getAgreementById(original.getId());
        Assert.assertNotNull(agreement);
    }

    /**
     * 测试新增销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:27
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 5)
    public void testAddAgreement() {
        Agreement agreement = agreementData.generateAgreement("add");
        agreement = agreementBiz.addAgreement(agreement);
        Assert.assertNotNull(agreement);
        Assert.assertNotNull(agreement.getId());
    }

    /**
     * 测试新增销售协议，销售协议对象为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:28
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 6)
    public void testAddAgreementWithNullAgreement() {
        try {
            agreementBiz.addAgreement(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_EMPTY.getErrorMessage());
        }
    }

    /**
     * 测试新增销售协议，销售端对象为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:28
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 7)
    public void testAddAgreementWithNullSaleApp() {
        try {
            agreementBiz.addAgreement(new Agreement());
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_APP_NULL.getErrorMessage());
        }
    }

    /**
     * 测试新增销售协议，销售端在当前公司已存在销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:29
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 8)
    public void testAddAgreementWithExistedSaleApp() {
        try {
            Agreement original = (Agreement) agreementData.getDomains().get(0);
            Agreement agreement = new Agreement(original.getSaleApp(), true, "remark");
            agreementBiz.addAgreement(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_APP_EXIST.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:29
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 9)
    public void testUpdateAgreement() {
        Agreement original = (Agreement) agreementData.getDomains().get(1);
        Agreement agreement = new Agreement(original.getSaleApp(), original.isEnabled(), System.currentTimeMillis() + "update remark");
        agreement.setId(original.getId());
        agreement.setVersion(original.getVersion());
        Agreement dbAgreement = agreementBiz.updateAgreement(agreement);
        Assert.assertNotNull(dbAgreement);
    }

    /**
     * 测试更新销售协议，销售协议对象为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:30
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 10)
    public void testUpdateAgreementWithNullAgreement() {
        try {
            agreementBiz.updateAgreement(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_EMPTY.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议，销售协议ID为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:30
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 11)
    public void testUpdateAgreementWithNullId() {
        try {
            Agreement agreement = new Agreement();
            agreement.setId(null);
            agreement.setVersion(0);
            agreementBiz.updateAgreement(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_EMPTY.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议，version为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 15:25
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 12)
    public void testUpdateAgreementWithNullVersion() {
        try {
            Agreement original = (Agreement) agreementData.getDomains().get(0);
            Agreement agreement = new Agreement(original.getSaleApp(), original.isEnabled(), original.getRemark());
            agreement.setId(original.getId());
            agreement.setVersion(null);
            agreementBiz.updateAgreement(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_VERSION_NULL.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议，销售端对象为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:30
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 13)
    public void testUpdateAgreementWithNullSaleApp() {
        try {
            Agreement original = (Agreement) agreementData.getDomains().get(0);
            Agreement agreement = new Agreement(null, original.isEnabled(), original.getRemark());
            agreement.setId(original.getId());
            agreement.setVersion(original.getVersion());
            agreementBiz.updateAgreement(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_APP_NULL.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议，销售协议不存在
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:31
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 14)
    public void testUpdateAgreementWithNotExistAgreement() {
        try {
            Agreement original = (Agreement) agreementData.getDomains().get(0);
            Agreement agreement = new Agreement(original.getSaleApp(), original.isEnabled(), original.getRemark());
            agreement.setId(Long.MAX_VALUE);
            agreement.setVersion(original.getVersion());
            agreementBiz.updateAgreement(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_NOT_EXIST.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议，销售端在当前公司已存在销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:31
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 15)
    public void testUpdateAgreementWithExistedSaleApp() {
        try {
            Agreement original1 = (Agreement) agreementData.getDomains().get(0);
            Agreement original2 = (Agreement) agreementData.getDomains().get(1);
            original2.setSaleApp(original1.getSaleApp());
            agreementBiz.updateAgreement(original2);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_APP_EXIST.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议状态
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:31
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 16)
    public void testUpdateStatus() {
        Agreement original = (Agreement) agreementData.getDomains().get(0);
        Boolean enabled = original.isEnabled();
        original.setEnabled(!original.isEnabled());
        Agreement agreement = agreementBiz.updateStatus(original);
        Assert.assertNotNull(agreement);
        Assert.assertFalse(enabled.equals(agreement.isEnabled()));
    }

    /**
     * 测试更新销售协议状态，销售协议对象为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:32
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 17)
    public void testUpdateStatusWithNullAgreement() {
        try {
            agreementBiz.updateStatus(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_EMPTY.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议状态，version为空
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-20 15:24
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 18)
    public void testUpdateStatusWithNullVersion() {
        try {
            Agreement original = (Agreement) agreementData.getDomains().get(0);
            Agreement agreement = new Agreement(original.getSaleApp(), original.isEnabled(), original.getRemark());
            agreement.setId(original.getId());
            agreement.setVersion(null);
            agreementBiz.updateStatus(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_VERSION_NULL.getErrorMessage());
        }
    }

    /**
     * 测试更新销售协议状态，销售协议不存在
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 18:33
     *
     * @since 2.0-SNAPSHOT
     */
    @Test(priority = 19)
    public void testUpdateStatusWithNotExistAgreement() {
        try {
            Agreement original = (Agreement) agreementData.getDomains().get(0);
            Agreement agreement = new Agreement(original.getSaleApp(), original.isEnabled(), original.getRemark());
            agreement.setId(Long.MAX_VALUE);
            agreement.setVersion(original.getVersion());
            agreementBiz.updateStatus(agreement);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getMessage(), AgreementBizError.AGREEMENT_NOT_EXIST.getErrorMessage());
        }
    }
}
