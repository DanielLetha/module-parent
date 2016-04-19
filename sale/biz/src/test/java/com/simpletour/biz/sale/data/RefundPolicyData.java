package com.simpletour.biz.sale.data;

import com.simpletour.commons.test.generator.AbstractDataGenerator;
import com.simpletour.dao.sale.IRefundPolicyDao;
import com.simpletour.domain.sale.RefundPolicy;
import com.simpletour.domain.sale.RefundRule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 16:27
 */
public class RefundPolicyData extends AbstractDataGenerator{
    private IRefundPolicyDao refundPolicyDao;

    public RefundPolicyData(IRefundPolicyDao refundPolicyDao){
        this.refundPolicyDao=refundPolicyDao;
    }

    @Override
    public void generator() {
        RefundPolicy refundPolicy=new RefundPolicy(generateName("policy1"),"");
        List<RefundRule> refundRuleList=new ArrayList<>();
        refundRuleList.add(new RefundRule(refundPolicy,2,50,""));
        refundRuleList.add(new RefundRule(refundPolicy,4,80,""));
        refundRuleList.add(new RefundRule(refundPolicy,6,90,""));
        refundPolicy.setRefundRules(refundRuleList);
        domains.add(refundPolicyDao.save(refundPolicy));

        RefundPolicy refundPolicy2=new RefundPolicy(generateName("policy2"),"");
        List<RefundRule> refundRuleList2=new ArrayList<>();
        refundRuleList2.add(new RefundRule(refundPolicy2,1,50,""));
        refundRuleList2.add(new RefundRule(refundPolicy2,3,80,""));
        refundRuleList2.add(new RefundRule(refundPolicy2,5,90,""));
        refundPolicy2.setRefundRules(refundRuleList2);
        domains.add(refundPolicyDao.save(refundPolicy2));
    }
}
