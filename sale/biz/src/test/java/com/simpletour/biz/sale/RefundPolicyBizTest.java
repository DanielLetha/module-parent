package com.simpletour.biz.sale;

import com.simpletour.biz.sale.data.RefundPolicyData;
import com.simpletour.biz.sale.error.RefundPolicyBizError;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IRefundPolicyDao;
import com.simpletour.domain.sale.RefundPolicy;
import com.simpletour.domain.sale.RefundRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.simpletour.commons.test.Utils.generateName;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 16:27
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class RefundPolicyBizTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private IRefundPolicyBiz refundPolicyBiz;

    @Autowired
    private IRefundPolicyDao refundPolicyDao;

    private RefundPolicyData refundPolicyData;

    @BeforeClass
    public void setUp() {
        refundPolicyData = new RefundPolicyData(refundPolicyDao);
        refundPolicyData.createData();
    }

    @AfterClass
    public void tearDown() throws Exception {
        int size = refundPolicyData.getDomains().size();
        for (int i = size - 1; i >= 0; i--) {
            refundPolicyDao.remove(refundPolicyData.getDomains().get(i));
        }
    }

    //测试：正常添加
    @Test
    public void testAddRefundPolicy() {
        RefundPolicy refundPolicy = refundPolicyData.generateRefundPolicy("add1");
        RefundPolicy refundPolicy1 = refundPolicyBiz.addRefundPolicy(refundPolicy);
        Assert.assertNotNull(refundPolicy1);
        Assert.assertNotNull(refundPolicy1.getId());
        Assert.assertEquals(refundPolicy1.getRefundRules().size(), 3);
    }

    //测试：添加存在重名
    @Test
    public void testAddRefundPolicyNameExisted() {
        RefundPolicy refundPolicy = refundPolicyData.generateRefundPolicy("add2");
        RefundPolicy refundPolicyExist = (RefundPolicy) refundPolicyData.getDomains().get(0);
        refundPolicy.setName(refundPolicyExist.getName());
        try {
            RefundPolicy refundPolicy1 = refundPolicyBiz.addRefundPolicy(refundPolicy);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), RefundPolicyBizError.REFUND_POLICY_NAME_EXIST);
        }
    }

    //测试：正常更新
    @Test
    public void testUpdateRefundPolicy() {
        RefundPolicy refundPolicy = (RefundPolicy) refundPolicyData.getDomains().get(0);
        String name = generateName();
        refundPolicy.setName(name);
        RefundPolicy refundPolicy1 = refundPolicyBiz.updateRefundPolicy(refundPolicy);
        Assert.assertNotNull(refundPolicy1);
        Assert.assertEquals(refundPolicy1.getName(), name);
        Assert.assertEquals(refundPolicy1.getRefundRules().size(), 3);
    }

    //测试：更新存在重名
    @Test
    public void testUpdateRefundPolicyNameExisted() {
        RefundPolicy refundPolicy1 = (RefundPolicy) refundPolicyData.getDomains().get(0);
        RefundPolicy refundPolicy2 = (RefundPolicy) refundPolicyData.getDomains().get(1);
        refundPolicy1.setName(refundPolicy2.getName());
        try {
            RefundPolicy refundPolicy3 = refundPolicyBiz.updateRefundPolicy(refundPolicy1);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), RefundPolicyBizError.REFUND_POLICY_NAME_EXIST);
        }
    }

    //测试删除
    @Test
    public void testDelete() {
        RefundPolicy refundPolicy = refundPolicyData.generateRefundPolicy("delete1");
        refundPolicy = refundPolicyBiz.addRefundPolicy(refundPolicy);
        refundPolicy = refundPolicyBiz.getRefundPolicyById(refundPolicy.getId());
        Assert.assertNotNull(refundPolicy);
        refundPolicyBiz.deleteRefundPolicy(refundPolicy.getId());
        refundPolicy = refundPolicyBiz.getRefundPolicyById(refundPolicy.getId());
        Assert.assertNull(refundPolicy);
    }

    //测试:page查询
    @Test
    public void testQueryPage() {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        DomainPage page = refundPolicyBiz.findRefundPolicyPage(query);
        System.out.println(page.getDomains().size());
        page.getDomains().forEach(tmp -> System.out.println(((RefundPolicy) tmp).getName() + ";" + ((RefundPolicy) tmp).getRefundRules().size()));

        RefundPolicy refundPolicy = (RefundPolicy) refundPolicyData.getDomains().get(0);
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("c.name", refundPolicy.getName());
        query.setCondition(conditionSet);
        page = refundPolicyBiz.findRefundPolicyPage(query);
        System.out.println(page.getDomains().size());
        page.getDomains().forEach(tmp -> System.out.println(((RefundPolicy) tmp).getName() + ";" + ((RefundPolicy) tmp).getRefundRules().size()));
    }

    //测试：List查询
    @Test
    public void testQueryList() {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        List list = refundPolicyBiz.findRefundPolicyList(query);
        System.out.println(list.size());
        list.forEach(tmp -> System.out.println(((RefundPolicy) tmp).getName() + ";" + ((RefundPolicy) tmp).getRefundRules().size()));

        RefundPolicy refundPolicy = (RefundPolicy) refundPolicyData.getDomains().get(0);
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("c.name", refundPolicy.getName());
        query.setCondition(conditionSet);
        list = refundPolicyBiz.findRefundPolicyList(query);
        System.out.println(list.size());
        list.forEach(tmp -> System.out.println(((RefundPolicy) tmp).getName() + ";" + ((RefundPolicy) tmp).getRefundRules().size()));
    }

    //测试：根据id判断退款模板是否存在
    @Test
    public void testIsExisted() {
        RefundPolicy refundPolicy1 = (RefundPolicy) refundPolicyData.getDomains().get(0);
        Assert.assertTrue(refundPolicyBiz.isRefundPolicyExisted(refundPolicy1.getId()));
        Assert.assertFalse(refundPolicyBiz.isRefundPolicyExisted(Long.MAX_VALUE));
    }

    //测试：根据id判断退款细则是否存在
    @Test
    public void testIsRunleIsExisted(){
        RefundPolicy refundPolicy= (RefundPolicy) refundPolicyData.getDomains().get(0);
        Assert.assertTrue(refundPolicyBiz.isRefundRuleExisted(refundPolicy.getRefundRules().get(0).getId()));
        Assert.assertFalse(refundPolicyBiz.isRefundRuleExisted(Long.MAX_VALUE));
    }

}
