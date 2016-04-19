package com.simpletour.service.sale.data;

import com.simpletour.commons.test.generator.AbstractDataGenerator;
import com.simpletour.dao.sale.IRefundPolicyDao;
import com.simpletour.domain.sale.RefundPolicy;
import com.simpletour.domain.sale.RefundRule;

import java.util.ArrayList;
import java.util.List;

import static com.simpletour.commons.test.Utils.generateName;

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
        RefundPolicy refundPolicy=generateRefundPolicy("policy1");
        domains.add(refundPolicyDao.save(refundPolicy));

        RefundPolicy refundPolicy2=generateRefundPolicy("policy2");
        domains.add(refundPolicyDao.save(refundPolicy2));
    }

    public RefundPolicy generateRefundPolicy(String name) {
        RefundPolicy refundPolicy = new RefundPolicy(generateName(name), "");
        List<RefundRule> refundRuleList = new ArrayList<>();
        refundRuleList.add(new RefundRule(refundPolicy, 2, 50, ""));
        refundRuleList.add(new RefundRule(refundPolicy, 4, 80, ""));
        refundRuleList.add(new RefundRule(refundPolicy, 6, 100, ""));
        refundPolicy.setRefundRules(refundRuleList);
        return refundPolicy;
    }
}
