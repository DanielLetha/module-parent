package com.simpletour.service.sale;

import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IRefundPolicyDao;
import com.simpletour.domain.sale.RefundPolicy;
import com.simpletour.domain.sale.RefundRule;
import com.simpletour.service.sale.data.RefundPolicyData;
import com.simpletour.service.sale.error.RefundPolicyServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static com.simpletour.commons.test.Utils.generateName;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 17:53
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class RefundPolicyServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private IRefundPolicyDao refundPolicyDao;

    @Autowired
    private IRefundPolicyService refundPolicyService;

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
    public void testAddRefundPolicy(){
        RefundPolicy refundPolicy=refundPolicyData.generateRefundPolicy("add1");
        Optional<RefundPolicy> refundPolicyOptional=refundPolicyService.addRefundPolicy(refundPolicy);
        Assert.assertTrue(refundPolicyOptional.isPresent());
        Assert.assertNotNull(refundPolicyOptional.get().getId());
        Assert.assertEquals(refundPolicyOptional.get().getRefundRules().size(),3);
    }

    //测试：添加异常
    @Test
    public void testAddRefundPolicyError(){
        //添加空模板
        RefundPolicy refundPolicy=null;
        try{
            refundPolicyService.addRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), RefundPolicyServiceError.REFUND_POLICY_NULL);
        }

        //添加模板id不为空
        refundPolicy=refundPolicyData.generateRefundPolicy("add2");
        refundPolicy.setId(2L);
        try{
            refundPolicyService.addRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), RefundPolicyServiceError.REFUND_POLICY_DATA_ERROR);
        }
        refundPolicy.setId(null);

        //添加模板中细则为空
        List<RefundRule> refundRuleList=refundPolicy.getRefundRules();
        refundPolicy.setRefundRules(null);
        try{
            refundPolicyService.addRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_NULL);
        }
        refundPolicy.setRefundRules(refundRuleList);

        //添加模板中细则id不为空
        refundPolicy.getRefundRules().get(0).setId(2L);
        try{
            refundPolicyService.addRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), RefundPolicyServiceError.REFUND_POLICY_DATA_ERROR);
        }
        refundPolicy.getRefundRules().get(0).setId(null);

        //细则时间重叠
        refundPolicy.getRefundRules().get(0).setTiming(refundPolicy.getRefundRules().get(1).getTiming());
        try{
            refundPolicyService.addRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_ERROR);
        }
    }

    //测试：正常更新
    @Test
    public void testUpdateRefundPolicy(){
        RefundPolicy refundPolicy= (RefundPolicy) refundPolicyData.getDomains().get(0);
        String name=generateName();
        refundPolicy.setName(name);
        Optional<RefundPolicy> refundPolicyOptional=refundPolicyService.updateRefundPolicy(refundPolicy);
        Assert.assertTrue(refundPolicyOptional.isPresent());
        Assert.assertEquals(refundPolicyOptional.get().getName(),name);
    }

    //测试：更新异常
    @Test
    public void testUpdateRefundPolicyError(){
        //更新空
        try{
            refundPolicyService.updateRefundPolicy(null);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_NULL);
        }

        //更新id为空
        RefundPolicy refundPolicy= (RefundPolicy) refundPolicyData.getDomains().get(0);
        Long id=refundPolicy.getId();
        refundPolicy.setId(null);
        try{
            refundPolicyService.updateRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_DATA_ERROR);
        }
        //更新id不存在
        refundPolicy.setId(Long.MAX_VALUE);
        try{
            refundPolicyService.updateRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_NOT_EXIST);
        }
        refundPolicy.setId(id);

        //更新细则为空
        List<RefundRule> refundRules=refundPolicy.getRefundRules();
        refundPolicy.setRefundRules(null);
        try{
            refundPolicyService.updateRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_NULL);
        }
        refundPolicy.setRefundRules(refundRules);

        //更新细则存在覆盖
        refundPolicy.getRefundRules().get(0).setTiming(refundPolicy.getRefundRules().get(1).getTiming());
        try{
            refundPolicyService.updateRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_ERROR);
        }
        refundPolicy.getRefundRules().get(0).setTiming(1);

        //存在更新细则id不为空，但不存在。
        Long ruleId=refundPolicy.getRefundRules().get(0).getId();
        refundPolicy.getRefundRules().get(0).setId(Long.MAX_VALUE);
        try{
            refundPolicyService.updateRefundPolicy(refundPolicy);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_NOT_EXIST);
        }
        refundPolicy.getRefundRules().get(0).setId(ruleId);
    }

    //测试：依据id获取退款规则模板
    @Test
    public void testGetRefundPolicyById(){
        try{
            refundPolicyService.getRefundPolicyById(null);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_NULL);
        }
        RefundPolicy refundPolicy= (RefundPolicy) refundPolicyData.getDomains().get(0);
        Optional<RefundPolicy> refundPolicyOptional=refundPolicyService.getRefundPolicyById(refundPolicy.getId());
        Assert.assertTrue(refundPolicyOptional.isPresent());
        Assert.assertNotNull(refundPolicyOptional.get().getId());

        Optional<RefundPolicy> refundPolicyOptional1=refundPolicyService.getRefundPolicyById(Long.MAX_VALUE);
        Assert.assertFalse(refundPolicyOptional1.isPresent());
    }

    //测试：依据id删除退款规则模板
    @Test
    public void testDeleteRefundPolicyById(){
        try{
            refundPolicyService.deleteRefundPolicy(null);
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_NULL);
        }
        try{
            refundPolicyService.deleteRefundPolicy(Long.MAX_VALUE);
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),RefundPolicyServiceError.REFUND_POLICY_NOT_EXIST);
        }

        RefundPolicy refundPolicy=refundPolicyData.generateRefundPolicy("delete");
        Optional<RefundPolicy> refundPolicyOptional=refundPolicyService.addRefundPolicy(refundPolicy);
        Assert.assertTrue(refundPolicyOptional.isPresent());

        Long id=refundPolicyOptional.get().getId();
        refundPolicyService.deleteRefundPolicy(id);

        refundPolicyOptional=refundPolicyService.getRefundPolicyById(id);
        Assert.assertFalse(refundPolicyOptional.isPresent());
    }


    //测试:page查询
    @Test
    public void testQueryPage() {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        query.setPageSize(10);
        query.setPageIndex(1);
        DomainPage page = refundPolicyService.findRefundPolicyPage(query);
        System.out.println(page.getDomains().size());
        page.getDomains().forEach(tmp -> System.out.println(((RefundPolicy) tmp).getName() + ";" + ((RefundPolicy) tmp).getRefundRules().size()));

        RefundPolicy refundPolicy = (RefundPolicy) refundPolicyData.getDomains().get(0);
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("c.name", refundPolicy.getName());
        query.setCondition(conditionSet);
        page = refundPolicyService.findRefundPolicyPage(query);
        System.out.println(page.getDomains().size());
        page.getDomains().forEach(tmp -> System.out.println(((RefundPolicy) tmp).getName() + ";" + ((RefundPolicy) tmp).getRefundRules().size()));
    }

    //测试：List查询
    @Test
    public void testQueryList() {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        query.setPageSize(10);
        query.setPageIndex(1);
        List list = refundPolicyService.findRefundPolicyList(query);
        System.out.println(list.size());
        list.forEach(tmp -> System.out.println(((RefundPolicy) tmp).getName() + ";" + ((RefundPolicy) tmp).getRefundRules().size()));

        RefundPolicy refundPolicy = (RefundPolicy) refundPolicyData.getDomains().get(0);
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("c.name", refundPolicy.getName());
        query.setCondition(conditionSet);
        list = refundPolicyService.findRefundPolicyList(query);
        System.out.println(list.size());
        list.forEach(tmp -> System.out.println(((RefundPolicy) tmp).getName() + ";" + ((RefundPolicy) tmp).getRefundRules().size()));
    }
}
